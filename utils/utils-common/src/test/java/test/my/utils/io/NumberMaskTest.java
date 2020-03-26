package test.my.utils.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.jd.blockchain.utils.io.NumberMask;

public class NumberMaskTest {

	@Test
	public void testLongNumberMask() {
		assertTrue(NumberMask.TINY.MAX_HEADER_LENGTH == 1);
		assertEquals(256, NumberMask.TINY.getBoundarySize(1));

		assertTrue(NumberMask.SHORT.MAX_HEADER_LENGTH == 2);
		assertEquals(128, NumberMask.SHORT.getBoundarySize(1));
		assertEquals(32768, NumberMask.SHORT.getBoundarySize(2));

		assertTrue(NumberMask.NORMAL.MAX_HEADER_LENGTH == 4);
		assertEquals(64, NumberMask.NORMAL.getBoundarySize(1));
		assertEquals(16384, NumberMask.NORMAL.getBoundarySize(2));
		assertEquals(4194304, NumberMask.NORMAL.getBoundarySize(3));
		assertEquals(1073741824, NumberMask.NORMAL.getBoundarySize(4));

		assertTrue(NumberMask.LONG.MAX_HEADER_LENGTH == 8);
		assertEquals(32L, NumberMask.LONG.getBoundarySize((byte) 1));
		assertEquals(8192L, NumberMask.LONG.getBoundarySize((byte) 2));
		assertEquals(2097152L, NumberMask.LONG.getBoundarySize((byte) 3));
		assertEquals(536870912L, NumberMask.LONG.getBoundarySize((byte) 4));
		assertEquals(137438953472L, NumberMask.LONG.getBoundarySize((byte) 5));
		assertEquals(35184372088832L, NumberMask.LONG.getBoundarySize((byte) 6));
		assertEquals(9007199254740992L, NumberMask.LONG.getBoundarySize((byte) 7));
		assertEquals(2305843009213693952L, NumberMask.LONG.getBoundarySize((byte) 8));

		testLong(0, 1);
		testLong(17, 1);
		testLong(31, 1);

		testLong(32, 2);
		testLong(57, 2);
		testLong(8191, 2);

		testLong(8192L, 3);
		testLong(103200, 3);
		testLong(2000320, 3);
		testLong(2097151L, 3);

		testLong(2097152L, 4);
		testLong(403332200, 4);
		testLong(536870911L, 4);

		testLong(536870912L, 5);
		testLong(103388332200L, 5);
		testLong(137438953471L, 5);

		testLong(137438953472L, 6);
		testLong(25243388332201L, 6);
		testLong(35184372088831L, 6);

		testLong(35184372088832L, 7);
		testLong(7985243388332201L, 7);
		testLong(9007199254740991L, 7);

		testLong(9007199254740992L, 8);
		testLong(1985932243388332201L, 8);
		testLong(2305843009213693951L, 8);
	}

	private void testLong(long number, int expectedLength) {
		byte[] bytes = NumberMask.LONG.generateMask(number);

		int len = NumberMask.LONG.getMaskLength(number);

		int resolvedLen = NumberMask.LONG.resolveMaskLength(bytes[0]);
		long resolvedNumber = NumberMask.LONG.resolveMaskedNumber(bytes);

		assertEquals(number, resolvedNumber);
		assertEquals(expectedLength, bytes.length);
		assertEquals(expectedLength, len);
		assertEquals(expectedLength, resolvedLen);
	}

	/**
	 * 对比采用针对 64 位整数设计的 LongNumberMask 和针对32位整数设计的 NumberMask 的性能；
	 */
	@Test
	public void testNumberMaskEfficiency() {
		int runTimes = 100000000;

		byte[] buff1 = new byte[8];
		byte[] buff2 = new byte[8];

		// 对比采用针对 64 位整数设计的 LongNumberMask 和针对32位整数设计的 NumberMask 的性能；
		int number = 12832;
		System.out.println("------ 16bit ------");
		long ts1 = testPerformance(number, NumberMask_v0.SHORT, buff1, runTimes);
		long ts2 = testPerformance(number, NumberMask.SHORT, buff2, runTimes);
		System.out.printf("Time for int : %s\r\nTime for long: %s\r\n", ts1, ts2);

		System.out.println("------ 32bit ------");
		ts1 = testPerformance(number, NumberMask_v0.NORMAL, buff1, runTimes);
		ts2 = testPerformance(number, NumberMask.NORMAL, buff2, runTimes);
		System.out.printf("Time for int : %s\r\nTime for long: %s\r\n", ts1, ts2);

		System.out.println("------ 64bit ------");
//		ts1 = testPerformance(number, NumberMask.NORMAL, buff1, runTimes);
		ts2 = testPerformance(number, NumberMask.LONG, buff2, runTimes);
		System.out.printf("Time for int : ----\r\nTime for long: %s\r\n", ts2);
	}

	private long testPerformance(int number, NumberMask_v0 mask, byte[] buffer, int runTimes) {
		long startTs = System.nanoTime();
		for (int i = 0; i < runTimes; i++) {
			mask.writeMask(number, buffer, 0);
		}
		long elapseTs = System.nanoTime() - startTs;
		return elapseTs;
	}

	private long testPerformance(int number, NumberMask mask, byte[] buffer, int runTimes) {
		long startTs = System.nanoTime();
		for (int i = 0; i < runTimes; i++) {
			mask.writeMask(number, buffer, 0);
		}
		long elapseTs = System.nanoTime() - startTs;
		return elapseTs;
	}

	/**
	 * 显示整数的所有位；
	 */
	@Test
	public void showBits() {
		long v = -3;
		System.out.printf("Binary of [%s]=[", v);
		printBits(v);
		System.out.println("]");
		v = v * -1;
		System.out.printf("Binary of [ %s]=[", v);
		printBits(v);
		System.out.println("]");
	}

	private void printBits(long v) {
		long f = 0x1 << 63;
		for (int i = 0; i < 64; i++) {
			f = f >> i;
			byte p = (byte) (((f & v) >>> (63 - i)) & 0x1);
			System.out.printf("%s", p);
		}
	}
}
