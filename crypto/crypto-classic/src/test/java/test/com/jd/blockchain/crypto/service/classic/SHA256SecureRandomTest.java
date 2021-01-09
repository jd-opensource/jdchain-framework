package test.com.jd.blockchain.crypto.service.classic;

import static org.junit.Assert.assertArrayEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;

import org.junit.Test;

import com.jd.blockchain.crypto.service.classic.SHA256SecureRandom;

import utils.io.BytesUtils;

public class SHA256SecureRandomTest {

	/**
	 * 验证随机数按照相同的种子输入，输出总长度，总能得到相同的字节序列；
	 * @throws IOException 
	 */
	@Test
	public void testRandomConsistantWithFixedSeed() throws IOException {
		byte[] seed = BytesUtils.toBytes(System.currentTimeMillis());

		int totalSize = 200;
		
		byte[] standardOutput = new byte[totalSize];
		SHA256SecureRandom random1 = new SHA256SecureRandom(seed);
		random1.nextBytes(standardOutput);
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] stepBytes = new byte[100];
		SHA256SecureRandom random2 = new SHA256SecureRandom(seed);
		random2.nextBytes(stepBytes);
		out.write(stepBytes);
		random2.nextBytes(stepBytes);
		out.write(stepBytes);
		
		byte[] outputBytes = out.toByteArray();
		assertArrayEquals(standardOutput, outputBytes);

		for (int i = 0; i < totalSize; i++) {
			// 按照不同的步长输出随机数，检查在相同的总长度下，得到的最终序列是否一致；
			byte[] output = generateRandom(new SHA256SecureRandom(seed), totalSize, i+1);
			assertArrayEquals("", standardOutput, output);
		}
	}

	private static byte[] generateRandom(SecureRandom random, int totalSize, int stepSize) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buff = new byte[stepSize];
		int left = totalSize;
		while (left > 0) {
			if (left < buff.length) {
				buff = new byte[left];
			}
			random.nextBytes(buff);
			out.write(buff);
			left -= buff.length;
		}
		return out.toByteArray();
	}

}
