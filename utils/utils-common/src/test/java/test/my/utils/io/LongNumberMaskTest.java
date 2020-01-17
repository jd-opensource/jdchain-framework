package test.my.utils.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.jd.blockchain.utils.io.LongNumberMask;

public class LongNumberMaskTest {

	@Test
	public void testLongNumberMask() {
		assertTrue(LongNumberMask.TINY.MAX_HEADER_LENGTH == 1);
		assertEquals(256, LongNumberMask.TINY.getBoundarySize(1));

		assertTrue(LongNumberMask.SHORT.MAX_HEADER_LENGTH == 2);
		assertEquals(128, LongNumberMask.SHORT.getBoundarySize(1));
		assertEquals(32768, LongNumberMask.SHORT.getBoundarySize(2));

		assertTrue(LongNumberMask.NORMAL.MAX_HEADER_LENGTH == 4);
		assertEquals(64, LongNumberMask.NORMAL.getBoundarySize(1));
		assertEquals(16384, LongNumberMask.NORMAL.getBoundarySize(2));
		assertEquals(4194304, LongNumberMask.NORMAL.getBoundarySize(3));
		assertEquals(1073741824, LongNumberMask.NORMAL.getBoundarySize(4));

		assertTrue(LongNumberMask.LONG.MAX_HEADER_LENGTH == 8);
		assertEquals(32L, LongNumberMask.LONG.getBoundarySize((byte) 1));
		assertEquals(8192L, LongNumberMask.LONG.getBoundarySize((byte) 2));
		assertEquals(2097152L, LongNumberMask.LONG.getBoundarySize((byte) 3));
		assertEquals(536870912L, LongNumberMask.LONG.getBoundarySize((byte) 4));
		assertEquals(137438953472L, LongNumberMask.LONG.getBoundarySize((byte) 5));
		assertEquals(35184372088832L, LongNumberMask.LONG.getBoundarySize((byte) 6));
		assertEquals(9007199254740992L, LongNumberMask.LONG.getBoundarySize((byte) 7));
		assertEquals(2305843009213693952L, LongNumberMask.LONG.getBoundarySize((byte) 8));

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
		byte[] bytes = LongNumberMask.LONG.generateMask(number);
		
		int len = LongNumberMask.LONG.getMaskLength(number);
		
		int resolvedLen = LongNumberMask.LONG.resolveMaskLength(bytes[0]);
		long resolvedNumber = LongNumberMask.LONG.resolveMaskedNumber(bytes);
		
		assertEquals(number, resolvedNumber);
		assertEquals(expectedLength, bytes.length);
		assertEquals(expectedLength, len);
		assertEquals(expectedLength, resolvedLen);
	}

}
