package com.jd.blockchain.binaryproto.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import com.jd.blockchain.binaryproto.BinaryProtocol;
import com.jd.blockchain.binaryproto.BinarySliceSpec;
import com.jd.blockchain.binaryproto.BytesConverter;
import com.jd.blockchain.binaryproto.DataContract;
import com.jd.blockchain.binaryproto.DataContractAutoRegistrar;
import com.jd.blockchain.binaryproto.DataContractEncoder;
import com.jd.blockchain.binaryproto.DataContractException;
import com.jd.blockchain.binaryproto.DataContractRegistry;
import com.jd.blockchain.binaryproto.DataField;
import com.jd.blockchain.binaryproto.DataSpecification;
import com.jd.blockchain.binaryproto.EnumContract;
import com.jd.blockchain.binaryproto.EnumField;
import com.jd.blockchain.binaryproto.EnumSpecification;
import com.jd.blockchain.binaryproto.FieldSpec;
import com.jd.blockchain.binaryproto.NumberEncoding;
import com.jd.blockchain.binaryproto.PrimitiveType;
import com.jd.blockchain.binaryproto.impl.EnumSpecificationInfo.EnumConstant;
import com.jd.blockchain.utils.ArrayUtils;
import com.jd.blockchain.utils.io.BytesSerializable;
import com.jd.blockchain.utils.io.BytesUtils;
import com.jd.blockchain.utils.io.NumberMask;
import com.jd.blockchain.utils.provider.Provider;
import com.jd.blockchain.utils.provider.ProviderManager;
import com.jd.blockchain.utils.security.SHA256Hash;
import com.jd.blockchain.utils.security.ShaUtils;

public class DataContractContext {

	private static ProviderManager pm = new ProviderManager();

	public static DataContractEncoderLookup ENCODER_LOOKUP;

	private static final Object MUTEX = new Object();

	private static final BinarySliceSpec HEAD_SLICE = BinarySliceSpec.newFixedSlice(HeaderEncoder.HEAD_BYTES, "HEAD",
			"The code and version of data contract.");

	private static final byte SINGLE_TYPE = 0;
	private static final byte REPEATABLE_TYPE = 1;

	/**
	 * 基本类型的字段；
	 */
	private static final byte PRIMITIVE_TYPE_FIELD = 0;

	/**
	 * 枚举类型的字段；
	 */
	private static final byte ENUM_CONTRACT_FIELD = 1;

	/**
	 * 引用一个具体的数据契约类型的字段；
	 */
	private static final byte DATA_CONTRACT_FIELD = 2;

	/**
	 * 动态的数据契约类型的字段；
	 */
	private static final byte DYNAMIC_CONTRACT_FIELD = 3;

	private static Map<Integer, ContractTypeVersionContext> codeMap = new ConcurrentHashMap<>();
	private static Map<Class<?>, DataContractEncoder> typeMap = new ConcurrentHashMap<>();

	private static Map<Class<?>, EnumSpecification> enumContractSpecMap = new ConcurrentHashMap<>();

	private static Map<PrimitiveType, Map<Class<?>, ValueConverter>> primitiveTypeConverters = new HashMap<>();

	private static Map<PrimitiveType, Map<Class<?>, Map<NumberMask, ValueConverter>>> numberEncodingConverters = new HashMap<>();

	static {
		addConverterMapping(PrimitiveType.BOOLEAN, boolean.class, new BoolConverter());
		addConverterMapping(PrimitiveType.BOOLEAN, Boolean.class, new BoolWrapperConverter());

		addConverterMapping(PrimitiveType.INT8, byte.class, new Int8ByteConverter(), Int8ByteEncodingConverter.class);
		addConverterMapping(PrimitiveType.INT8, Byte.class, new Int8ByteWrapperConverter(),
				Int8ByteWrapperEncodingConverter.class);
		addConverterMapping(PrimitiveType.INT16, short.class, new Int16ShortConverter(),
				Int16ShortEncodingConverter.class);
		addConverterMapping(PrimitiveType.INT16, Short.class, new Int16ShortWrapperConverter(),
				Int16ShortWrapperEncodingConverter.class);
		addConverterMapping(PrimitiveType.INT16, char.class, new Int16CharConverter(),
				Int16CharEncodingConverter.class);
		addConverterMapping(PrimitiveType.INT16, Character.class, new Int16CharWrapperConverter(),
				Int16CharWrapperEncodingConverter.class);
		addConverterMapping(PrimitiveType.INT32, int.class, new Int32IntConverter(), Int32IntEncodingConverter.class);
		addConverterMapping(PrimitiveType.INT32, Integer.class, new Int32IntWrapperConverter(),
				Int32IntWrapperEncodingConverter.class);
		addConverterMapping(PrimitiveType.INT64, long.class, new Int64LongConverter(),
				Int64LongEncodingConverter.class);
		addConverterMapping(PrimitiveType.INT64, Long.class, new Int64LongWrapperConverter(),
				Int64LongWrapperEncodingConverter.class);

		addConverterMapping(PrimitiveType.TEXT, String.class, new StringValueConverter());
		addConverterMapping(PrimitiveType.BYTES, byte[].class, new BytesValueConverter());

		ENCODER_LOOKUP = new DataContractEncoderLookup() {
			@Override
			public DataContractEncoder lookup(int code, long version) {
				ContractTypeVersionContext ctx = codeMap.get(code);
				if (ctx == null) {
					return null;
				}
				// TODO: 未实现多个版本的处理；
				return ctx.contractEncoder;
			}

			@Override
			public DataContractEncoder lookup(Class<?> contractType) {
				return typeMap.get(contractType);
			}
		};

		// 加载自动注册提供者，注册类型；
		autoRegister();
	}

	private static void autoRegister() {
		// 从当前类型的类加载器加载服务提供者；
		pm.installAllProviders(DataContractAutoRegistrar.class, BinaryProtocol.class.getClassLoader());
		// 从线程上下文类加载器加载服务提供者；（多次加载避免由于类加载器的原因产生遗漏，ProviderManager 内部会过滤重复加载）；
		pm.installAllProviders(DataContractAutoRegistrar.class, Thread.currentThread().getContextClassLoader());

		Iterable<Provider<DataContractAutoRegistrar>> providers = pm.getAllProviders(DataContractAutoRegistrar.class);
		List<DataContractAutoRegistrar> autoRegistrars = new ArrayList<DataContractAutoRegistrar>();
		for (Provider<DataContractAutoRegistrar> provider : providers) {
			autoRegistrars.add(provider.getService());
		}

		//排序；
		autoRegistrars.sort(new Comparator<DataContractAutoRegistrar>() {
			@Override
			public int compare(DataContractAutoRegistrar o1, DataContractAutoRegistrar o2) {
				return o1.order() - o2.order();
			}
		});
		
		for (DataContractAutoRegistrar registrar : autoRegistrars) {
			registrar.initContext(DataContractRegistry.getInstance());
		}
	}

	private static void initNumberEncodingConverterMapping(PrimitiveType protocalType, Class<?> javaType,
			Class<? extends NumberEncodingConverter> numberEncodingConverterType) {
		Map<Class<?>, Map<NumberMask, ValueConverter>> converterMap = numberEncodingConverters.get(protocalType);
		if (converterMap == null) {
			converterMap = new HashMap<>();
			numberEncodingConverters.put(protocalType, converterMap);
		}
		Map<NumberMask, ValueConverter> converters = new HashMap<NumberMask, ValueConverter>();
		converterMap.put(javaType, converters);

		try {
			Constructor<? extends ValueConverter> constructor = numberEncodingConverterType
					.getConstructor(NumberMask.class);
			for (NumberMask numMask : NumberMask.values()) {
				ValueConverter converter = constructor.newInstance(numMask);
				converters.put(numMask, converter);
			}
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	private synchronized static void addConverterMapping(PrimitiveType protocalType, Class<?> javaType,
			ValueConverter converter, Class<? extends NumberEncodingConverter> numberEncodingConverterType) {
		addConverterMapping(protocalType, javaType, converter);
		initNumberEncodingConverterMapping(protocalType, javaType, numberEncodingConverterType);
	}

	private synchronized static void addConverterMapping(PrimitiveType protocalType, Class<?> javaType,
			ValueConverter converter) {
		Map<Class<?>, ValueConverter> converterMap = primitiveTypeConverters.get(protocalType);
		if (converterMap == null) {
			converterMap = new HashMap<>();
			primitiveTypeConverters.put(protocalType, converterMap);
		}
		converterMap.put(javaType, converter);
	}

	private static ValueConverter getPrimitiveTypeConverter(PrimitiveType protocalType, Class<?> javaType,
			NumberEncoding numberEncoding) {
		if (numberEncoding != null && numberEncoding != NumberEncoding.NONE) {
			Map<Class<?>, Map<NumberMask, ValueConverter>> typeConverters = numberEncodingConverters.get(protocalType);
			if (typeConverters != null) {
				Map<NumberMask, ValueConverter> encodingConverters = typeConverters.get(javaType);
				if (encodingConverters != null) {
					ValueConverter converter = encodingConverters.get(numberEncoding.MASK);
					if (converter != null) {
						return converter;
					}
				}
			}
		}
		Map<Class<?>, ValueConverter> converterMap = primitiveTypeConverters.get(protocalType);
		if (converterMap != null) {
			ValueConverter converter = converterMap.get(javaType);
			if (converter != null) {
				return converter;
			}
			if (PrimitiveType.BYTES == protocalType && BytesSerializable.class.isAssignableFrom(javaType)) {
				converter = new BytesSerializableValueConverter(javaType);
				converterMap.put(javaType, converter);
				return converter;
			}
		}
		throw new IllegalArgumentException(String.format("Unsupport types mapping: [PrimitiveType=%s]-[JavaType=%s]",
				protocalType.toString(), javaType.toString()));
	}

	public synchronized static <T> void registerBytesConverter(Class<T> javaType, BytesConverter<T> converter) {
		DelegatingBytesValueConverter<T> bytesValueConverter = new DelegatingBytesValueConverter<>(javaType, converter);
		addConverterMapping(PrimitiveType.BYTES, javaType, bytesValueConverter);
	}

	public static DataContractEncoder resolve(Class<?> contractType) {
		DataContractEncoder encoder = typeMap.get(contractType);
		if (encoder != null) {
			return encoder;
		}
		synchronized (MUTEX) {
			encoder = typeMap.get(contractType);
			if (encoder != null) {
				return encoder;
			}
			ContractTypeVersionContext ctx = resolveContract(contractType);
			encoder = ctx.contractEncoder;
		}

		return encoder;
	}

	/**
	 * 解析数据契约； <br>
	 * 
	 * @param contractType
	 * @return
	 */
	private static ContractTypeVersionContext resolveContract(Class<?> contractType) {
		// TODO: 未处理可能存在的循环依赖问题，这会导致解析方法陷入死循环；
		if (!contractType.isInterface()) {
			throw new IllegalArgumentException(
					"The specified contractType [" + contractType.toString() + "] is not a interface!");
		}
		DataContract annoContract = contractType.getAnnotation(DataContract.class);
		if (annoContract == null) {
			throw new IllegalArgumentException(
					"Class[" + contractType.toString() + "] isn't annotated as DataContract!");
		}
		int contractCode = annoContract.code();
		ContractTypeVersionContext ctx = codeMap.get(contractCode);
		if (ctx != null) {
			if (ctx.contractType == contractType) {
				return ctx;
			} else {
				throw new IllegalStateException(String.format(
						"Contract Code[%s] has been registered by type[%s]! Cann't register again with type[%s]!",
						contractCode, ctx.contractType.getName(), contractType.getName()));
			}
		}

		DataContractEncoder contractEncoder = resolveEncoder(contractType, annoContract);

		ctx = new ContractTypeVersionContext(contractType, contractEncoder);

		codeMap.put(contractCode, ctx);
		typeMap.put(contractType, contractEncoder);

		return ctx;
	}

	/**
	 * @param contractType
	 * @param annoContract
	 * @return
	 */
	private static DataContractEncoder resolveEncoder(Class<?> contractType, DataContract annoContract) {
		DataContractEncoder encoder = typeMap.get(contractType);
		if (encoder != null) {
			return encoder;
		}

		if (!contractType.isInterface()) {
			throw new IllegalArgumentException(
					"The registering contract type is not a interface! --" + contractType.getName());
		}

		// 解析获得数据契约的有序的字段列表；
		List<FieldDeclaredInfo> allFields = resolveContractFields(contractType, annoContract);

		// 解析每一个字段，生成字段的编码器和二进制片段描述符；
		FieldSpecInfo[] fieldSpecs = new FieldSpecInfo[allFields.size()];
		BinarySliceSpec[] dataSliceSpecs = new BinarySliceSpec[allFields.size() + 1];
		FieldEncoder[] fieldEncoders = new FieldEncoder[allFields.size()];

		dataSliceSpecs[0] = HEAD_SLICE;

		SHA256Hash versionHash = ShaUtils.hash_256();// 用于计算 DataContract 的版本号的哈希生成器；
		int i = 0;
		for (FieldDeclaredInfo fieldInfo : allFields) {
			fieldSpecs[i] = fieldInfo.fieldSpec;

			// 构建二进制片段；
			dataSliceSpecs[i + 1] = buildSlice(fieldInfo.fieldSpec);

			fieldEncoders[i] = buildFieldEncoder(fieldInfo, dataSliceSpecs[i + 1]);

			// 按顺序计算字段类型，
			byte[] fieldType = generateFieldTypeCode(fieldInfo.fieldSpec);
			versionHash.update(fieldType);

			i++;
		}

		// 数据契约的版本号取自对所有字段的数据类型的哈希前 8 位；
		byte[] allFieldTypesHash = versionHash.complete();
		long version = BytesUtils.toLong(allFieldTypesHash);

		HeaderEncoder headerEncoder = new HeaderEncoder(HEAD_SLICE, annoContract.code(), version, annoContract.name(),
				annoContract.description());

		DataContractSpecification spec = new DataContractSpecification(annoContract.code(), version,
				annoContract.name(), annoContract.description(), dataSliceSpecs, fieldSpecs);

		DataContractEncoderImpl contractEncoder = new DataContractEncoderImpl(contractType, spec, headerEncoder,
				fieldEncoders);

		return contractEncoder;
	}

	/**
	 * 解析获得数据契约的有序的字段列表；
	 * 
	 * @param contractType
	 * @param annoContract
	 * @return
	 */
	private static List<FieldDeclaredInfo> resolveContractFields(Class<?> contractType, DataContract annoContract) {
		// 解析每一个方法，获得标注的合约字段，并按照声明的合约类型进行分组；
		Map<Class<?>, DeclaredFieldGroup> declaredFielGroups = new HashMap<>();
		Method[] methods = contractType.getMethods();
		for (Method method : methods) {
			DataField annoField = method.getAnnotation(DataField.class);
			if (annoField == null) {
				continue;
			}
			// 当数据契约类型继承自父接口，覆盖（Override）父接口中的字段方法的情况下，
			// 尽管父接口中并未使用 DataField 标注，只在子接口中声明为 DataField，
			// 某些情况下 method.getAnnotation(DataField.class) 仍然能够返回 DataField
			// 实例，（JDK1.8.0_212下测试，原因未知）
			// 由于父接口中声明为泛型，方法返回类型在反射中得到的是 Object.class, 进而导致以下的解析出现字段声明类型与实际类型不匹配的异常；
			// 父接口在方法中返回的这类 Method 实例都被标记为 default 方法，因此忽略这类方法可以避免错误；
			if (method.isDefault()) {
				continue;
			}
			Class<?> declaredType = method.getDeclaringClass();
			DeclaredFieldGroup group = declaredFielGroups.get(declaredType);
			if (group != null) {
				FieldSpecInfo fieldSpec = resolveFieldSpec(method, annoField);
				group.addField(method, annoField, fieldSpec);
				continue;
			}
			if (declaredType == contractType) {
				// 字段是由当前的数据契约类型所声明；
				FieldSpecInfo fieldSpec = resolveFieldSpec(method, annoField);
				group = new DeclaredFieldGroup(contractType, annoContract, method, annoField, fieldSpec);
				declaredFielGroups.put(contractType, group);
				continue;
			}
			// 字段由父接口声明，所以取父接口上的标注定义进行解析；
			DataContract declaredContractAnnotation = declaredType.getAnnotation(DataContract.class);
			if (declaredContractAnnotation == null) {
				throw new DataContractException("Declare data contract field in a non-data-contract type! --[Type="
						+ declaredType.getName() + "]");
			}

			FieldSpecInfo fieldSpec = resolveFieldSpec(method, annoField);
			group = new DeclaredFieldGroup(declaredType, declaredContractAnnotation, method, annoField, fieldSpec);
			declaredFielGroups.put(declaredType, group);
		}

		DeclaredFieldGroup[] groups = declaredFielGroups.values()
				.toArray(new DeclaredFieldGroup[declaredFielGroups.size()]);
		for (DeclaredFieldGroup group : groups) {
			// 计算继承距离；
			int extendsionDistance = computeExtendsionDistance(contractType, group.declaredContractType);
			if (extendsionDistance < 0) {
				// 实际不会进入此分支；
				throw new IllegalStateException("Illegal state that isn't expected to occur!");
			}
			group.setExtendsionDistance(extendsionDistance);
		}

		// 按继承距离和数据契约的编码进行倒序排序，如果继承距离相同，则编码小的在前；
		// 达到的效果：父接口声明的字段在前，子接口声明的字段在后；同一个继承级别，则编码小的在前；
		Arrays.sort(groups,
				(g1, g2) -> (g2.extendsionDistance == g1.extendsionDistance
						? g1.declaredContractAnnotation.code() - g2.declaredContractAnnotation.code()
						: g2.extendsionDistance - g1.extendsionDistance));

		List<FieldDeclaredInfo> allFields = new ArrayList<>();
		for (DeclaredFieldGroup grp : groups) {
			allFields.addAll(grp.getFields());
		}

		return allFields;
	}

	/**
	 * 创建字段的编码器；
	 * 
	 * @param fieldInfo
	 * @param sliceSpec
	 * @return
	 */
	private static FieldEncoder buildFieldEncoder(FieldDeclaredInfo fieldInfo, BinarySliceSpec sliceSpec) {
		FieldSpecInfo fieldSpec = fieldInfo.fieldSpec;
		if (fieldSpec.getPrimitiveType() != null) {
			return buildPrimitiveFieldEncoder(fieldInfo, sliceSpec);
		} else if (fieldSpec.getRefEnum() != null) {
			return buildEnumFieldEncoder(fieldInfo, sliceSpec);
		} else if (fieldSpec.getRefContract() != null) {
			return buildContractFieldEncoder(fieldInfo, sliceSpec);
		} else {
			throw new IllegalStateException("Illegal states that has no type definition for field! --[ReadMethod="
					+ fieldInfo.reader.toString() + "");
		}
	}

	/**
	 * 创建数据契约引用字段的编码器；
	 * 
	 * @param fieldInfo
	 * @param sliceSpec
	 * @return
	 */
	private static FieldEncoder buildContractFieldEncoder(FieldDeclaredInfo fieldInfo, BinarySliceSpec sliceSpec) {
		ValueConverter valueConverter;
		if (fieldInfo.fieldSpec.isGenericContract()) {
			Class<?> contractType = fieldInfo.fieldSpec.getDataType();
			valueConverter = new DataContractGenericRefConverter(contractType, ENCODER_LOOKUP);
		} else {
			Class<?> contractType = fieldInfo.fieldSpec.getDataType();
			DataContractEncoder encoder = typeMap.get(contractType);
			valueConverter = new DataContractValueConverter(encoder);
		}

		return createFieldEncoder(sliceSpec, fieldInfo.fieldSpec, fieldInfo.reader, valueConverter);
	}

	/**
	 * 创建枚举类型的字段编码器；
	 * 
	 * @param fieldInfo
	 * @param sliceSpec
	 * @return
	 */
	private static FieldEncoder buildEnumFieldEncoder(FieldDeclaredInfo fieldInfo, BinarySliceSpec sliceSpec) {
		// 枚举类型的值转换器是由枚举值的范围检查加上一个基本类型的值转换器组成；
		Class<?> enumType = fieldInfo.fieldSpec.getDataType();
		EnumSpecificationInfo enumSpec = (EnumSpecificationInfo) fieldInfo.fieldSpec.getRefEnum();
		int[] values = enumSpec.getItemValues();
		Object[] constants = enumSpec.getConstants();
		PrimitiveType codeType = enumSpec.getValueType();

		ValueConverter baseConverter = getPrimitiveTypeConverter(codeType, enumSpec.getDataType(), null);

		EnumValueConverter valueConverter = new EnumValueConverter(enumType, codeType, values, constants,
				(FixedValueConverter) baseConverter);

		return createFieldEncoder(sliceSpec, fieldInfo.fieldSpec, fieldInfo.reader, valueConverter);
	}

	/**
	 * 创建基本类型字段的编码器；
	 * 
	 * @param fieldInfo
	 * @param sliceSpec
	 * @return
	 */
	private static FieldEncoder buildPrimitiveFieldEncoder(FieldDeclaredInfo fieldInfo, BinarySliceSpec sliceSpec) {
		ValueConverter valueConverter = getPrimitiveTypeConverter(fieldInfo.fieldSpec.getPrimitiveType(),
				fieldInfo.fieldSpec.getDataType(), fieldInfo.fieldSpec.getNumberEncoding());
		return createFieldEncoder(sliceSpec, fieldInfo.fieldSpec, fieldInfo.reader, valueConverter);
	}

	private static FieldEncoder createFieldEncoder(BinarySliceSpec sliceSpec, FieldSpec fieldSpec, Method reader,
			ValueConverter valueConverter) {
		if (sliceSpec.isRepeatable()) {
			if (sliceSpec.isDynamic()) {
				return new DynamicArrayFieldEncoder(sliceSpec, fieldSpec, reader,
						(DynamicValueConverter) valueConverter);
			} else {
				return new FixedArrayFieldEncoder(sliceSpec, fieldSpec, reader, (FixedValueConverter) valueConverter);
			}
		} else {
			if (sliceSpec.isDynamic()) {
				return new DynamicFieldEncoder(sliceSpec, fieldSpec, reader, (DynamicValueConverter) valueConverter);
			} else {
				return new FixedFieldEncoder(sliceSpec, fieldSpec, reader, (FixedValueConverter) valueConverter);
			}
		}
	}

	private static BinarySliceSpec buildSlice(FieldSpecInfo fieldSpec) {

		int len = -1;
		PrimitiveType fixedValueType = null;

		PrimitiveType specPrimitiveType = fieldSpec.getPrimitiveType();
		if (specPrimitiveType != null && specPrimitiveType != PrimitiveType.NIL) {
			NumberEncoding numberEncoding = fieldSpec.getNumberEncoding();
			if (numberEncoding == null || numberEncoding == NumberEncoding.NONE
					|| !numberEncodingConverters.containsKey(specPrimitiveType)) {
				fixedValueType = fieldSpec.getPrimitiveType();
			}
		} else if (fieldSpec.getRefEnum() != null) {
			fixedValueType = fieldSpec.getRefEnum().getValueType();
		}

		boolean fixed = false;
		if (fixedValueType != null) {
			switch (fixedValueType) {
			case BOOLEAN:
				fixed = true;
				len = 1;
				break;
			case INT8:
				fixed = true;
				len = 1;
				break;
			case INT16:
				fixed = true;
				len = 2;
				break;
			case INT32:
				fixed = true;
				len = 4;
				break;
			case INT64:
				fixed = true;
				len = 8;
				break;
			default:
				break;
			}
		}
		if (fieldSpec.isRepeatable()) {
			if (fixed) {
				return BinarySliceSpec.newRepeatableFixedSlice(len, fieldSpec.getName(), fieldSpec.getDescription());
			} else {
				return BinarySliceSpec.newRepeatableDynamicSlice(fieldSpec.getName(), fieldSpec.getDescription());
			}
		} else {
			if (fixed) {
				return BinarySliceSpec.newFixedSlice(len, fieldSpec.getName(), fieldSpec.getDescription());
			} else {
				return BinarySliceSpec.newDynamicSlice(fieldSpec.getName(), fieldSpec.getDescription());
			}
		}
	}

	private static byte[] generateFieldTypeCode(FieldSpecInfo fieldSpec) {
		byte repeatable = fieldSpec.isRepeatable() ? REPEATABLE_TYPE : SINGLE_TYPE;
		byte[] codeBytes;
		if (fieldSpec.getPrimitiveType() != null) {
			// repeatable + type indicator + code of primitive type;
			// 1 + 1 + 4;
			codeBytes = new byte[6];
			codeBytes[0] = repeatable;
			codeBytes[1] = PRIMITIVE_TYPE_FIELD;
			BytesUtils.toBytes(fieldSpec.getPrimitiveType().CODE, codeBytes, 2);
		} else if (fieldSpec.getRefEnum() != null) {
			// repeatable + type indicator + code of enum contract + version of enum
			// contract;
			// 1+ 1 + 4 + 8;
			codeBytes = new byte[14];
			codeBytes[0] = repeatable;
			codeBytes[1] = ENUM_CONTRACT_FIELD;
			EnumSpecification enumSpec = fieldSpec.getRefEnum();
			BytesUtils.toBytes(enumSpec.getCode(), codeBytes, 2);
			BytesUtils.toBytes(enumSpec.getVersion(), codeBytes, 6);
		} else if (fieldSpec.getRefContract() != null) {
			// repeatable + type indicator + code of enum contract + version of enum
			// contract;
			// 1+ 1 + 4 + 8;
			DataSpecification dataSpec = fieldSpec.getRefContract();
			codeBytes = new byte[14];
			codeBytes[0] = repeatable;
			if (fieldSpec.isGenericContract()) {
				codeBytes[1] = DYNAMIC_CONTRACT_FIELD;
			} else {
				codeBytes[1] = DATA_CONTRACT_FIELD;
			}
			BytesUtils.toBytes(dataSpec.getCode(), codeBytes, 2);
			BytesUtils.toBytes(dataSpec.getVersion(), codeBytes, 6);
		} else {
			throw new DataContractException("Unknow field type!");
		}
		return codeBytes;
	}

	/**
	 * 计算指定两个接口类型之间的继承距离；<br>
	 * 
	 * 如果不具有继承关系，则返回 -1；
	 * 
	 * @param subsTypes 子类型；
	 * @param superType 父类型；
	 * @return
	 */
	private static int computeExtendsionDistance(Class<?> subsTypes, Class<?> superType) {
		if (subsTypes == superType) {
			return 0;
		}
		Class<?>[] superIntfs = subsTypes.getInterfaces();
		for (Class<?> si : superIntfs) {
			int dis = computeExtendsionDistance(si, superType);
			if (dis > -1) {
				return dis + 1;
			}
		}
		return -1;
	}

	private static FieldSpecInfo resolveFieldSpec(Method accessor, DataField annoField) {
		String name = annoField.name();
		name = name == null ? null : name.trim();
		if (name == null || name.length() > 0) {
			name = accessor.getName();
		}
		String desc = annoField.decription();
		desc = desc == null ? null : desc.trim();

		int order = annoField.order();

		boolean repeatable = annoField.list();
		Class<?> dataType = accessor.getReturnType();

		if (repeatable) {
			if (!dataType.isArray()) {
				throw new DataContractException("The annotated repeatable type mismatch non-array type["
						+ dataType.getName() + "]! --[Field=" + accessor.toString() + "]");
			}
			dataType = dataType.getComponentType();
		}

		int maxSize = annoField.maxSize();
		PrimitiveType primitiveType = annoField.primitiveType();
		if (primitiveType != null) {
			primitiveType = verifyPrimitiveType(primitiveType, dataType, accessor);
		}

		boolean refEnum = annoField.refEnum();
		EnumSpecification enumSpecification = null;
		if (refEnum) {
			EnumContract annoEnumContract = dataType.getAnnotation(EnumContract.class);
			if (annoEnumContract == null) {
				throw new DataContractException("The data type of annotated enum field is not a EnumContract! --[Field="
						+ accessor.toString() + "]");
			}
			enumSpecification = resolveEnumContract(dataType, annoEnumContract);
		}

		boolean refContract = annoField.refContract();
		DataSpecification contractSpecification = null;
		// Class<?> contractTypeResolverClazz = null;
		if (refContract) {
			// DataContract annoContract = dataType.getAnnotation(DataContract.class);
			// if (annoContract == null) {
			// throw new DataContractException(
			// "The data type of annotated contract field is not a DataContract! --[Field="
			// + accessor.toString() + "]");
			// }

			ContractTypeVersionContext contractContext = resolveContract(dataType);
			DataContractEncoder encoder = contractContext.contractEncoder;
			contractSpecification = encoder.getSpecification();
			// contractTypeResolverClazz = annoField.contractTypeResolver();
			// if (contractTypeResolverClazz != null
			// && (!ContractTypeResolver.class.isAssignableFrom(contractTypeResolverClazz)))
			// {
			// throw new DataContractException(
			// "The contract type resolver of contract field doesn't implement
			// ContractTypeResolver interface! --[Field="
			// + accessor.toString() + "]");
			// }
		}

		if (primitiveType == null && enumSpecification == null && contractSpecification == null) {
			throw new DataContractException(
					"Miss data type definition of field! --[Field=" + accessor.toString() + "]");
		}

		FieldSpecInfo fieldSpec = null;
		if (primitiveType != null) {
			fieldSpec = new FieldSpecInfo(order, name, desc, primitiveType, annoField.numberEncoding(), repeatable,
					maxSize, dataType);
		} else if (enumSpecification != null) {
			fieldSpec = new FieldSpecInfo(order, name, desc, enumSpecification, repeatable, dataType);
		} else {
			fieldSpec = new FieldSpecInfo(order, name, desc, contractSpecification, repeatable, dataType,
					annoField.genericContract());
		}
		return fieldSpec;
	}

	private static EnumSpecification resolveEnumContract(Class<?> dataType, EnumContract annoEnumContract) {
		EnumSpecificationInfo enumSpec = (EnumSpecificationInfo) enumContractSpecMap.get(dataType);
		if (enumSpec != null) {
			return enumSpec;
		}
		try {
			if (!dataType.isEnum()) {
				throw new DataContractException("Field's type is not a enum type! --[" + dataType.toString() + "]");
			}
			// TODO：暂时硬编码检索 code 字段；
			Field codeField = dataType.getField("CODE");
			if (codeField == null) {
				throw new DataContractException("Enum type miss the 'CODE' field! --[" + dataType.toString() + "]");
			}
			EnumField fieldAnno = codeField.getAnnotation(EnumField.class);
			if (fieldAnno == null) {
				throw new DataContractException("Enum's 'CODE' field is not annotated with @EnumField !");
			}
			// TODO: 暂时未实现枚举契约的版本号计算；
			long version = 0;
			enumSpec = new EnumSpecificationInfo(fieldAnno.type(), annoEnumContract.code(), version,
					annoEnumContract.name(), annoEnumContract.decription(), codeField.getType());
			// get enum constants and CODE
			Object[] enumItems = dataType.getEnumConstants();
			for (Object item : enumItems) {
				int code = codeField.getInt(item);
				EnumConstant constant = new EnumConstant(code, item.toString(), item);
				enumSpec.addConstant(constant);
				// enumSpec.setVersion(); if need
			}
			enumContractSpecMap.put(dataType, enumSpec);
			return enumSpec;
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			throw new DataContractException(e.getMessage(), e);
		}
	}

	/**
	 * 解析指定的类型是否匹配；<br>
	 * 
	 * 要求必须是显式地声明类型，因此不会根据 Java 语言的声明类型做自动转换；
	 * 
	 * @param primitiveType
	 * @param dataType
	 * @return
	 */
	private static PrimitiveType verifyPrimitiveType(PrimitiveType primitiveType, Class<?> dataType, Method accessor) {
		switch (primitiveType) {
		case NIL:
			return null;
		case BOOLEAN:
			if (dataType != Boolean.class && dataType != boolean.class) {
				throw new DataContractException("The annotated primitive type[" + primitiveType.toString()
						+ "] mismatch java type[" + dataType.getName() + "]! --[Field=" + accessor.toString() + "]");
			}
			break;
		case INT8:
			if (dataType != Byte.class && dataType != byte.class) {
				throw new DataContractException("The annotated primitive type[" + primitiveType.toString()
						+ "] mismatch java type[" + dataType.getName() + "]! --[Field=" + accessor.toString() + "]");
			}
			break;
		case INT16:
			if (dataType != Character.class && dataType != char.class && dataType != short.class) {
				throw new DataContractException("The annotated primitive type[" + primitiveType.toString()
						+ "] mismatch java type[" + dataType.getName() + "]! --[Field=" + accessor.toString() + "]");
			}
			break;
		case INT32:
			if (dataType != Integer.class && dataType != int.class) {
				throw new DataContractException("The annotated primitive type[" + primitiveType.toString()
						+ "] mismatch java type[" + dataType.getName() + "]! --[Field=" + accessor.toString() + "]");
			}
			break;
		case INT64:
			if (dataType != Long.class && dataType != long.class) {
				throw new DataContractException("The annotated primitive type[" + primitiveType.toString()
						+ "] mismatch java type[" + dataType.getName() + "]! --[Field=" + accessor.toString() + "]");
			}
			break;
		case TEXT:
			if (dataType != String.class) {
				throw new DataContractException("The annotated primitive type[" + primitiveType.toString()
						+ "] mismatch java type[" + dataType.getName() + "]! --[Field=" + accessor.toString() + "]");
			}
			break;
		case BYTES:
			if (dataType != byte[].class && (!BytesSerializable.class.isAssignableFrom(dataType))) {
				throw new DataContractException("The annotated primitive type[" + primitiveType.toString()
						+ "] mismatch java type[" + dataType.getName() + "]! --[Field=" + accessor.toString() + "]");
			}
			break;
		default:
			throw new DataContractException("Unsupported primitive type[" + primitiveType.toString() + "] ! --[Field="
					+ accessor.toString() + "]");
		}

		return primitiveType;
	}

	private static class DeclaredFieldGroup {

		/**
		 * 声明的合约类型；
		 */
		public Class<?> declaredContractType;

		/**
		 * 
		 */
		public DataContract declaredContractAnnotation;

		/**
		 * 声明类型距离要解析的类型的继承距离；直接继承的父接口的距离为 1，父接口的父接口为 2，以此类推；
		 */
		private int extendsionDistance;

		private TreeMap<Integer, FieldDeclaredInfo> orderedFields = new TreeMap<>();

		public Collection<FieldDeclaredInfo> getFields() {
			return orderedFields.values();
		}

		public DeclaredFieldGroup(Class<?> declaredContractType, DataContract declaredContractAnnotation,
				Method accessor, DataField fieldAnnotation, FieldSpecInfo fieldSpec) {
			this.declaredContractType = declaredContractType;
			this.declaredContractAnnotation = declaredContractAnnotation;

			addField(accessor, fieldAnnotation, fieldSpec);
		}

		private void addField(Method accessor, DataField fieldAnnotation, FieldSpecInfo fieldSpec) {
			// 检查字段的是否有重复序号；
			FieldDeclaredInfo fieldInfo = new FieldDeclaredInfo(accessor, fieldAnnotation, fieldSpec);
			FieldDeclaredInfo conflictedField = orderedFields.put(fieldSpec.getOrder(), fieldInfo);
			if (conflictedField != null) {
				// 有两个字段都声明了相同的序号，这容易导致无序状态；
				throw new DataContractException(String.format("Declare two fields with the same order! --[%s][%s]",
						fieldInfo.reader.toString(), conflictedField.reader.toString()));
			}
		}

		@SuppressWarnings("unused")
		public int getExtendsionDistance() {
			return extendsionDistance;
		}

		public void setExtendsionDistance(int extendsionDistance) {
			this.extendsionDistance = extendsionDistance;
		}

	}

	private static class FieldDeclaredInfo {
		public FieldSpecInfo fieldSpec;

		public Method reader;

		@SuppressWarnings("unused")
		public DataField annoField;

		public FieldDeclaredInfo(Method accessor, DataField annoField, FieldSpecInfo fieldSpec) {
			this.reader = accessor;
			this.annoField = annoField;
			this.fieldSpec = fieldSpec;
		}
	}

	private static class ContractTypeVersionContext {

		public Class<?> contractType;

		public DataContractEncoder contractEncoder;

		// TODO:未实现多版本；
		// private HashMap<Long, DataSpecification> versionMap = new HashMap<>();

		public ContractTypeVersionContext(Class<?> contractType, DataContractEncoder encoder) {
			this.contractType = contractType;
			this.contractEncoder = encoder;
		}

	}
}
