package test.com.jd.blockchain.utils.hash;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

import com.jd.blockchain.utils.Bytes;
import com.jd.blockchain.utils.hash.MurmurHash3;

public class MurmurHashTest {

	@Test
	public void test() {
		
		Random rand = new Random();

		byte[] bytes1 = new byte[128];
		rand.nextBytes(bytes1);
		byte[] bytes2 = new byte[256];
		rand.nextBytes(bytes2);

		Bytes prefix = new Bytes(bytes1);
		Bytes bytesObj = new Bytes(prefix, bytes2);
		
		byte[] bytesArray = new byte[bytes1.length + bytes2.length];
		rand.nextBytes(bytesArray);
		Bytes bytesObj2 = new Bytes(bytesArray);
		
		int seed = 10010;
		
		int count = 1000000;
		long ts1 = System.nanoTime();
		for (int i = 0; i < count; i++) {
			MurmurHash3.murmurhash3_x64_64_1(bytesObj, 0, bytesObj.size(), seed);
		}
		long span1 = System.nanoTime()-ts1;
		
		long ts2 = System.nanoTime();
		for (int i = 0; i < count; i++) {
			MurmurHash3.murmurhash3_x64_64_1(bytesObj2, 0, bytesObj2.size(), seed);
		}
		long span2 = System.nanoTime()-ts2;
		
		long ts3 = System.nanoTime();
		for (int i = 0; i < count; i++) {
			MurmurHash3.murmurhash3_x64_64_1(bytesArray, 0, bytesArray.length, seed);
		}
		long span3 = System.nanoTime()-ts3;
		
		System.out.printf("span1=[%s]\r\nspan2=[%s]\r\nspan3=[%s]\r\n", span1, span2, span3);
	}

}
