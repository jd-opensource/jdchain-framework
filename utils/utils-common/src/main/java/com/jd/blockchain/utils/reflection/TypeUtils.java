package com.jd.blockchain.utils.reflection;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public class TypeUtils {

	/**
	 * 返回一个对象实例实际声明的泛型类型；
	 * 
	 * @param obj               对象实例；
	 * @param genericSuperclass 继承的泛型超类型；
	 * @param genericTypeName   要获得的在泛型超类中声明的类型参数的名称；
	 * @return
	 */
	public static Class<?> getGenericClass(final Object obj, Class<?> genericSuperclass, String genericTypeName) {
		final Class<?> instanceClass = obj.getClass();
		Class<?> clazz = instanceClass;
		while (true) {
			if (clazz.getSuperclass() == genericSuperclass) {
				int typeIndex = -1;
				TypeVariable<?>[] typeParams = clazz.getSuperclass().getTypeParameters();
				for (int i = 0; i < typeParams.length; i++) {
					if (genericTypeName.equals(typeParams[i].getName())) {
						typeIndex = i;
						break;
					}
				}

				if (typeIndex < 0) {
					throw new IllegalStateException(
							"Unknow generic type '" + genericTypeName + "': " + genericSuperclass);
				}

				Type genericSuperType = clazz.getGenericSuperclass();
				if (!(genericSuperType instanceof ParameterizedType)) {
					return Object.class;
				}

				Type[] actualTypeArgs = ((ParameterizedType) genericSuperType).getActualTypeArguments();

				Type actualType = actualTypeArgs[typeIndex];
				if (actualType instanceof Class) {
					return (Class<?>) actualType;
				}
				if (actualType instanceof ParameterizedType) {
					actualType = ((ParameterizedType) actualType).getRawType();
				}
				if (actualType instanceof GenericArrayType) {
					Type componentType = ((GenericArrayType) actualType).getGenericComponentType();
					if (componentType instanceof ParameterizedType) {
						componentType = ((ParameterizedType) componentType).getRawType();
					}
					if (componentType instanceof Class) {
						return Array.newInstance((Class<?>) componentType, 0).getClass();
					}
				}
				if (actualType instanceof TypeVariable) {
					TypeVariable<?> type = (TypeVariable<?>) actualType;
					clazz = instanceClass;
					if (!(type.getGenericDeclaration() instanceof Class)) {
						return Object.class;
					}

					genericSuperclass = (Class<?>) type.getGenericDeclaration();
					genericTypeName = type.getName();
					if (genericSuperclass.isAssignableFrom(instanceClass)) {
						continue;
					} else {
						return Object.class;
					}
				}

				throw new IllegalStateException(
						"Cannot find the type of the generic type '" + genericTypeName + "': " + instanceClass);
			}
			
			clazz = clazz.getSuperclass();
			if (clazz == null) {
				throw new IllegalStateException(
						"Cannot find the type of the generic type '" + genericTypeName + "': " + instanceClass);
			}
		}// End of while;
	}

}
