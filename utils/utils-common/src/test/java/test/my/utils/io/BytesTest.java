package test.my.utils.io;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.junit.Test;

import com.jd.blockchain.utils.Bytes;
import com.jd.blockchain.utils.io.BytesUtils;
import com.jd.blockchain.utils.security.ShaUtils;

public class BytesTest {

	@Test
	public void test() {
		Bytes prefix = Bytes.fromString("A");
		byte[] textBytes = BytesUtils.toBytes("B");
		Bytes key1 = new Bytes(prefix, textBytes);
		byte[] key1Bytes = key1.toBytes();
		assertEquals(prefix.size() + textBytes.length, key1.size());
		assertEquals(prefix.size() + textBytes.length, key1Bytes.length);
		assertEquals(Arrays.hashCode(key1Bytes), key1.hashCode());

		Random rand = new Random();

		byte[] bytes1 = new byte[128];
		rand.nextBytes(bytes1);
		byte[] bytes2 = new byte[256];
		rand.nextBytes(bytes2);

		prefix = new Bytes(bytes1);
		Bytes bytes = new Bytes(prefix, bytes2);

		assertEquals(bytes1.length + bytes2.length, bytes.size());

		for (int i = 0; i < bytes1.length; i++) {
			assertEquals(bytes1[i], bytes.read(i));
		}

		for (int i = 0; i < bytes2.length; i++) {
			assertEquals(bytes2[i], bytes.read(128 + i));
		}

		// 测试对字节分段的一致性；
		byte[] dataBuff = new byte[128];
		rand.nextBytes(dataBuff);

		byte[] prefixBytes = new byte[32];
		System.arraycopy(dataBuff, 0, prefixBytes, 0, prefixBytes.length);
		byte[] dataBytes = new byte[dataBuff.length - prefixBytes.length];
		System.arraycopy(dataBuff, prefixBytes.length, dataBytes, 0, dataBytes.length);
		
		prefix = new Bytes(prefixBytes);
		Bytes data1 = new Bytes(prefix, dataBytes);
		Bytes data2 = new Bytes(dataBuff);
		byte[] dtBytes1 = data1.toBytes();
		byte[] dtBytes2 = data2.toBytes();
		assertTrue(BytesUtils.equals(dataBuff, dtBytes1));
		assertTrue(BytesUtils.equals(dataBuff, dtBytes2));
		assertEquals(data1.hashCode(), data2.hashCode());
		assertTrue(data1.equals(data2));
		assertEquals(data1, data2);
		

		// 测试对于 Map 的正确性；
		Map<Bytes, String> map = new HashMap<Bytes, String>();
		Bytes[] keys = new Bytes[200];
		byte[][] keyBytess = new byte[keys.length][];
		dataBuff = new byte[128];
		for (int i = 0; i < keys.length; i++) {
			rand.nextBytes(dataBuff);
			byte[] hash = ShaUtils.hash_256(dataBuff);
			keys[i] = new Bytes(hash);
			keyBytess[i] = hash;
			map.put(keys[i], "ITEM-" + i);
		}

		for (int i = 0; i < keys.length; i++) {
			assertTrue(map.containsKey(keys[i]));
			Bytes nkey = new Bytes(keyBytess[i]);
			assertTrue(map.containsKey(nkey));
		}

	}

}
