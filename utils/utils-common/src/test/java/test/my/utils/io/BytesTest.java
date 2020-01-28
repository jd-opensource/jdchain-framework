package test.my.utils.io;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Random;

import org.junit.Test;

import com.jd.blockchain.utils.Bytes;
import com.jd.blockchain.utils.io.BytesUtils;

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

	}

}
