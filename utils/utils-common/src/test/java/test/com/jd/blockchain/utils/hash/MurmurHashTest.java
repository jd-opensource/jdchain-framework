package test.com.jd.blockchain.utils.hash;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import com.jd.blockchain.utils.Bytes;
import com.jd.blockchain.utils.codec.Base58Utils;
import com.jd.blockchain.utils.codec.HexUtils;
import com.jd.blockchain.utils.hash.MurmurHash3;
import com.jd.blockchain.utils.io.BytesUtils;
import com.jd.blockchain.utils.security.RandomUtils;
import com.jd.blockchain.utils.security.ShaUtils;

import net.openhft.hashing.LongHashFunction;

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
		for (int i = 0; i < count; i++) {
			MurmurHash3.murmurhash3_x64_64_1(bytesObj, 0, bytesObj.size(), seed);
		}

		long ts1 = System.nanoTime();
		for (int i = 0; i < count; i++) {
			MurmurHash3.murmurhash3_x64_64_1(bytesObj, 0, bytesObj.size(), seed);
		}
		long span1 = System.nanoTime() - ts1;

		long ts2 = System.nanoTime();
		for (int i = 0; i < count; i++) {
			MurmurHash3.murmurhash3_x64_64_1(bytesObj2, 0, bytesObj2.size(), seed);
		}
		long span2 = System.nanoTime() - ts2;

		long ts3 = System.nanoTime();
		for (int i = 0; i < count; i++) {
			MurmurHash3.murmurhash3_x64_64_1(bytesArray, 0, bytesArray.length, seed);
		}
		long span3 = System.nanoTime() - ts3;

		System.out.printf("span1=[%s]\r\nspan2=[%s]\r\nspan3=[%s]\r\n", span1, span2, span3);
	}

	@Test
	public void test2() {

		Random rand = new Random();

		byte[] bytes1 = new byte[128];
		rand.nextBytes(bytes1);
		byte[] bytes2 = new byte[256];
		rand.nextBytes(bytes2);

		int seed = 10010;

		int count = 1000000;
		for (int i = 0; i < count; i++) {
			MurmurHash3.murmurhash3_x64_64_1(bytes1, 0, bytes1.length, seed);
		}

		long ts1 = System.nanoTime();
		for (int i = 0; i < count; i++) {
			MurmurHash3.murmurhash3_x86_32(bytes1, 0, bytes1.length, seed);
		}
		long span1 = System.nanoTime() - ts1;

		long ts2 = System.nanoTime();
		for (int i = 0; i < count; i++) {
			MurmurHash3.murmurhash3_x64_64_1(bytes1, 0, bytes1.length, seed);
		}
		long span2 = System.nanoTime() - ts2;

		System.out.printf("span1=[%s]\r\nspan2=[%s]\r\n", span1, span2);
	}

	/**
	 * 测试 Murmur3 哈希算法的不变性，即同样的输入得到完全一致的输出；
	 */
	@Test
	public void testMurmurHashImmutability() {
		int seed = 102400;
		byte[] data = BytesUtils.toBytes("TEST_DATA_" + System.currentTimeMillis());
		long h1;
		long h2;
		long[] hashs = new long[2];
		MurmurHash3.murmurhash3_x64_128(data, 0, data.length, seed, hashs);
		h1 = hashs[0];
		h2 = hashs[1];

		for (int i = 0; i < 10240000; i++) {
			MurmurHash3.murmurhash3_x64_128(data, 0, data.length, seed, hashs);
			assertEquals(h1, hashs[0]);
			assertEquals(h2, hashs[1]);
		}
	}

	/**
	 * 测试 Murmur3 哈希算法的不变性，即同样的输入得到完全一致的输出；
	 */
	@Test
	public void testXXHashImmutability() {
		int seed = 102400;
		byte[] data = BytesUtils.toBytes("TEST_DATA_" + System.currentTimeMillis());
		long h1;
		long h2;

		LongHashFunction hashFunction = LongHashFunction.xx(seed);
		h1 = hashFunction.hashBytes(data);

		for (int i = 0; i < 10240000; i++) {
			h2 = hashFunction.hashBytes(data);
			assertEquals(h1, h2);
		}
	}

	@Test
	public void testPerformance() {
		byte[] data = BytesUtils.toBytes("kkksjdlfjioejqoafjwef23fwdasdfa");

		int count = 50000000;
		long hashCode;

		LongHashFunction hashFunction = LongHashFunction.xx(8323);

		long start = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			hashCode = hashFunction.hashBytes(data);
		}
		long end = System.currentTimeMillis();

		System.out.println("[XXHash] Milli-seconds eclipsed: " + (end - start) + " ms.");

		start = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			hashCode = MurmurHash3.murmurhash3_x64_64_1(data, 0, data.length, 8323);
		}
		end = System.currentTimeMillis();

		System.out.println("[Murmur3Hash] Milli-seconds eclipsed: " + (end - start) + " ms.");
	}

	/**
	 * 测试用 Murmur3 哈希算法找到同一个值的另一个哈希需要的计算次数（第一原像攻击）；
	 * <p>
	 * 以后缀递增的方式进行测试，测试结果表明：
	 * <p>
	 * 在 32 位哈希空间下，平均需要 10 亿次计算以上；<br>
	 * 这是估算值，验证过程中只凑巧找到一个Base58格式字符的输入 [HtN9KS6oybwrd8TXFWBnJkLCJAtjnPnDmvdSoiKcb] 在
	 * 5 亿 7 千万次(571838775)计算后才找到另外一个输入
	 * [HtN9KS6oybwrd8TXFWBnJkLCJAtjnPnDmvdTgF9Mo];<br>
	 * 
	 * 验证发现，对于一个30个字节长度的固定输入，比较容易找到另外一个不同输入产生相同的32位哈希值；
	 * 
	 * 而此外尝试了许多其它情形的随机数据，在计算了 20 亿次以后仍然未找到匹配值；
	 * <p>
	 * 
	 * 在 64 位哈希空间下需要更多的计算，未能发现匹配输入，故此估算 64 位空间下的计算次数是 32 位空间下的指数倍；
	 * <p>
	 * 
	 * 
	 */
//	@Test
	public void testConfliction_PreImageAttack() {
		int seed = 102400;

		// 30字节（data.length == 30）；对于 32 位哈希的时候，输入 30 字节长度的顺序递增的数据会使的 Murmur3
		// 哈希算法容易出现碰撞；
//		byte[] data = Base58Utils.decode("HtN9KS6oybwrd8TXFWBnJkLCJAtjnPnDmvdSoiKcb");

		byte[] data = RandomUtils.generateRandomBytes(62);
		int dataLength = data.length;
		int subfixIndex = dataLength - 8;
		long[] hashs = new long[2];

		long i = 0;

		BytesUtils.toBytes(i, data, subfixIndex);

		// 输出 32 位哈希；
		// int target = MurmurHash3.murmurhash3_x86_32(data, 0, dataLength, seed);

		// 输出 64 位哈希；
		long target = MurmurHash3.murmurhash3_x64_64_1(data, 0, dataLength, seed);

		System.out.println("Target Input=" + Base58Utils.encode(data));

		// 计算找到相同哈希的另一个数值；测量需要消耗的计算次数；
		// long v;
		long v;
		i = 1;// 从 1 开始；
		do {
			BytesUtils.toBytes(i, data, subfixIndex);
			// v = MurmurHash3.murmurhash3_x86_32(data, 0, dataLength, seed);
			v = MurmurHash3.murmurhash3_x64_64_1(data, 0, dataLength, seed);

			i++;
		} while (v != target && i < 2000000000L);

		if (v == target) {
			System.out.println("Fixed target in " + i + " times!");
			System.out.println("Fixed Input=" + Base58Utils.encode(data));
		} else {
			System.out.println("Have not fixed target in " + i + " times!");
		}
	}

	/**
	 * 测试抗冲突性（第二原像攻击）；<br>
	 * 
	 * 随机选择 N 组输入时，检查获得两个相同输出的概率；<br>
	 * 
	 * M 位的哈希值，实现产生两个相同输出的概率大于二分之一，需要的 N 的最小值约为 2 的 M/2 次方；
	 */
	@Test
	public void testConfliction_Second_PreImageAttack() {
		// 1152921504606846975
		// 开始 4 位为 0 ；
		// final long CODE_MASK = 0xFFFFFFFFFFFFFFFL;
		final long CODE_MASK = 0xFFFFFFFFL;

		List<HashConfliction> conflictions = new LinkedList<HashConfliction>();

		HashMap<Long, String> hashMap = new HashMap<Long, String>();

		//
		int count = 200000;
		LongHashFunction hashFunction = LongHashFunction.xx(8233);
		Random rand = new Random();
		byte[] data = new byte[8];
		for (int i = 0; i < count; i++) {
			rand.nextBytes(data);

			// 通过和 CODE_MASK 执行“按位与”操作，截取去掉前面的多位，以降低实现“第二原像攻击”所需要的理论计算次数；

			// Murmur3 算法；
			Long code = MurmurHash3.murmurhash3_x64_64_1(data, 0, data.length, 8233) & CODE_MASK;

			// XXHash 算法；
			// Long code = hashFunction.hashBytes(data) & CODE_MASK;

			// SHA-2 算法；
			// Long code = BytesUtils.toLong(ShaUtils.hash_256(data)) & CODE_MASK;

			String strKey = HexUtils.encode(data);
			if (hashMap.containsKey(code)) {
				System.out.println(
						String.format("Hash confliction: [%s] -- [%s], [%s]", code, hashMap.get(code), strKey));
				conflictions.add(new HashConfliction(code, hashMap.get(code), strKey));
			}
			hashMap.put(code, strKey);
		}

		System.out.println(String.format("\r\nSequence keys hash confliction: %s / %s . CODE_MASK=%s",
				conflictions.size(), count, CODE_MASK));
	}

	private static class HashConfliction {

		long code;

		private String key1;

		private String key2;

		public HashConfliction(long code, String key1, String key2) {
			this.code = code;
			this.key1 = key1;
			this.key2 = key2;
		}

		public long getCode() {
			return code;
		}

		public String getKey1() {
			return key1;
		}

		public String getKey2() {
			return key2;
		}

	}

}
