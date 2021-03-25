package test.com.jd.blockchain.ledger;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.jd.binaryproto.impl.DataContractContext;
import com.jd.blockchain.ledger.BlockchainKeyGenerator;
import com.jd.blockchain.ledger.BlockchainKeypair;
import com.jd.blockchain.ledger.BytesValue;
import com.jd.blockchain.ledger.DataAccountKVSetOperation;
import com.jd.blockchain.ledger.DataAccountKVSetOperation.KVWriteEntry;
import com.jd.blockchain.ledger.TypedValue;
import com.jd.blockchain.transaction.DataAccountKVSetOpTemplate;

public class DataAccountKVSetOperationTest {

	/**
	 * 测试对 {@link KVWriteEntry#getValue()} 返回类型由 {@link BytesValue} 更改为 byte[]
	 * 之后，数据反序列化的兼容性；
	 */
	@Test
	public void testCompatibility() {
		BlockchainKeypair keypair = BlockchainKeyGenerator.getInstance().generate();

		// 在序列化上下文1 中注册旧的类型，并进行序列化；
		DataContractContext context1 = new DataContractContext();
		DataAccountKVSetOpTemplate op1 = new DataAccountKVSetOpTemplate(keypair.getAddress());
		String key = "dataKey001";
		BytesValue value = TypedValue.fromText("dataValue001");
		long expVersion = -1;
		op1.set(key, value, expVersion);

		byte[] opBytes = context1.register(DataAccountKVSetOperation.class).encode(op1);

		// 在序列化上下文2 中注册新类型并进行反序列化；
		DataContractContext context2 = new DataContractContext();
		DataAccountKVSetOperation2 op2 = context2.register(DataAccountKVSetOperation2.class).decode(opBytes);

		// 验证结果的一致性；
		assertEquals(op1.getAccountAddress(), op2.getAccountAddress());
		assertEquals(1, op1.getWriteSet().length);
		assertEquals(op1.getWriteSet().length, op2.getWriteSet().length);
		assertEquals(op1.getWriteSet()[0].getKey(), op2.getWriteSet()[0].getKey());
		assertEquals(op1.getWriteSet()[0].getExpectedVersion(), op2.getWriteSet()[0].getExpectedVersion());
		byte[] valueBytes = context1.lookup(BytesValue.class).encode(op1.getWriteSet()[0].getValue());
		assertArrayEquals(valueBytes, op2.getWriteSet()[0].getValue());
	}

}
