package test.com.jd.blockchain.binaryproto;

import com.jd.blockchain.binaryproto.DataContract;
import com.jd.blockchain.binaryproto.DataField;
import com.jd.blockchain.binaryproto.PrimitiveType;
import com.jd.blockchain.utils.Bytes;
import com.jd.blockchain.utils.io.BytesUtils;

/**
 * 验证数据契约的属性方法采用 {@link Override} 标注从父类型继承而来的情况下，能够正确处理；
 * 
 * @author huanghaiquan
 *
 */
public interface FieldOverrideDataContractTest {

	/**
	 * 非数据企业的的超类；
	 * 
	 * @author huanghaiquan
	 *
	 * @param <T>
	 */
	public static interface Data<K, T> {

		K getKey();

		/**
		 * 返回ID；
		 * 
		 * @return
		 */
		default int getId() {
			return getDataValue().hashCode();
		}

		default String getString() {
			return "ID=" + getId();
		}

		T getDataValue();

	}
	
	public static interface BytesData extends Data<Bytes, Bytes> {
		
		@Override
		Bytes getKey();
		
		@Override
		int getId();
		
		@Override
		Bytes getDataValue();
		
	}

	@DataContract(code = 20200914)
	public static interface TestDataContract extends BytesData {

		@DataField(order = 0, primitiveType = PrimitiveType.BYTES)
		@Override
		Bytes getKey();

		@DataField(order = 2, primitiveType = PrimitiveType.INT32)
		@Override
		int getId();

		@DataField(order = 1, primitiveType = PrimitiveType.BYTES)
		@Override
		Bytes getDataValue();

	}

	public static class TestData implements TestDataContract {

		private int id;

		private Bytes value;

		private Bytes key;

		public TestData(String key, int id, byte[] value) {
			this.key = new Bytes(BytesUtils.toBytes(key));
			this.id = id;
			this.value = new Bytes(value);
		}

		@Override
		public Bytes getKey() {
			return key;
		}

		@Override
		public Bytes getDataValue() {
			return value;
		}

		@Override
		public int getId() {
			return id;
		}

	}

}
