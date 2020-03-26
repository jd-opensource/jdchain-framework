package test.my.utils.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

import org.junit.Test;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.jd.blockchain.utils.io.BytesOutputBuffer;

public class BytesOutputBufferTest {

	/**
	 * 对 BytesOutputBuffer 与 JDK 的 ByteArrayOutputStream 以及 Google Guava 的
	 * BytesArrayOutput 进行性能比较；
	 * <p>
	 * 
	 * 性能测试的结果是：<br>
	 * JDK 实现和 Guava 实现适合中小数据量(数百KB以下)，JDK 实现更优；自定义实现的 BytesOutputBuffer 相差并不大<br>
	 * 在更大写入数据量时， 自定义实现的 BytesOutputBuffer 性能更优，Guava 实现最差；
	 */
	@Test
	public void testPerformance() {
		// 与 JDK 的 ByteArrayOutputStream 对比写入性能；
		Random rand = new Random();
		byte[][] datas = new byte[10][];
		for (int i = 0; i < datas.length; i++) {
			datas[i] = new byte[rand.nextInt(64)];
			rand.nextBytes(datas[i]);
		}

		// 热身
		testJDKOutputStream(datas, 100, false);
		testBytesOutputBuffer(datas, 100, false);
		testByteArrayDataOutput(datas, 100, false);

		// 小数据量测试；
		int round = 1000;
		System.out.println("\r\n------- [Low] BytesOutputBuffer And ByteArrayOutput performance comparation -------");
		for (int i = 0; i < 5; i++) {
			testJDKOutputStream(datas, round, true);
			testBytesOutputBuffer(datas, round, true);
			testByteArrayDataOutput(datas, round, true);
			System.out.println();
		}

		// 中等数据量测试；
		round = 10000;
		System.out
				.println("\r\n------- [Medium] BytesOutputBuffer And ByteArrayOutput performance comparation -------");
		for (int i = 0; i < 5; i++) {
			testJDKOutputStream(datas, round, true);
			testBytesOutputBuffer(datas, round, true);
			testByteArrayDataOutput(datas, round, true);
			System.out.println();
		}

		// 大数据量测试；
		round = 1000000;
		System.out.println("\r\n------- [High] BytesOutputBuffer And ByteArrayOutput performance comparation -------");
		for (int i = 0; i < 5; i++) {
			testJDKOutputStream(datas, round, true);
			testBytesOutputBuffer(datas, round, true);
			testByteArrayDataOutput(datas, round, true);
			System.out.println();
		}
	}

	/**
	 * 测试 JDK 自带的 ByteArrayOutputStream 的写入性能；
	 * 
	 * @param datas
	 * @param round
	 * @return 返回写入的数据大小；
	 */
	private int testJDKOutputStream(byte[][] datas, int round, boolean display) {
		try {
			long startTs = System.nanoTime();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int n = datas.length;
			for (int i = 0; i < round; i++) {
				out.write(datas[i % n]);
			}

			long outputTs = System.nanoTime();

			byte[] totalBytes = out.toByteArray();

			long endTs = System.nanoTime();

			if (display) {
				System.out.printf("ByteArrayOutputStream: Size=%s B; \tOutputTime=%s ns; \tTotalTime=%s ns\r\n",
						totalBytes.length, outputTs - startTs, endTs - startTs);
			}

			return totalBytes.length;
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 测试自定义的 BytesOutputBuffer 的写入性能；
	 * 
	 * @param datas
	 * @param round
	 * @return 返回写入的数据大小；
	 */
	private int testBytesOutputBuffer(byte[][] datas, int round, boolean display) {
		try {
			long startTs = System.nanoTime();
			BytesOutputBuffer out = new BytesOutputBuffer();
			int n = datas.length;
			for (int i = 0; i < round; i++) {
				out.write(datas[i % n]);
			}

			long outputTs = System.nanoTime();

			byte[] totalBytes = out.toBytes();

			long endTs = System.nanoTime();

			if (display) {
				System.out.printf("    BytesOutputBuffer: Size=%s B; \tOutputTime=%s ns; \tTotalTime=%s ns\r\n",
						totalBytes.length, outputTs - startTs, endTs - startTs);
			}

			return totalBytes.length;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 测试 Google Guava 的 ByteArrayDataOutput 的写入性能；
	 * 
	 * @param datas
	 * @param round
	 * @return 返回写入的数据大小；
	 */
	private int testByteArrayDataOutput(byte[][] datas, int round, boolean display) {
		try {
			long startTs = System.nanoTime();
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			int n = datas.length;
			for (int i = 0; i < round; i++) {
				out.write(datas[i % n]);
			}

			long outputTs = System.nanoTime();

			byte[] totalBytes = out.toByteArray();

			long endTs = System.nanoTime();

			if (display) {
				System.out.printf("  ByteArrayDataOutput: Size=%s B; \tOutputTime=%s ns; \tTotalTime=%s ns\r\n",
						totalBytes.length, outputTs - startTs, endTs - startTs);
			}

			return totalBytes.length;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}
