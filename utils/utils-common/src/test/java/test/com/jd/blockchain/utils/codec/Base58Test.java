package test.com.jd.blockchain.utils.codec;

import com.jd.blockchain.utils.codec.Base58Utils;
import com.jd.blockchain.utils.io.BytesUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class Base58Test {

    @Test
    public void testText() {
        Random random = new Random();
        byte[][] bss = new byte[1024][];
        for (int i = 0; i < bss.length; i++) {
            bss[i] = generateString(random, random.nextInt(1024)).getBytes();
        }
        for (byte[] bs : bss) {
            Assert.assertArrayEquals(bs, Base58Utils.decode(Base58Utils.encode(bs)));
        }
    }

    @Test
    public void testBoolean() {
        byte[][] bss = new byte[][]{BytesUtils.toBytes(true), BytesUtils.toBytes(false)};
        for (byte[] bs : bss) {
            Assert.assertArrayEquals(bs, Base58Utils.decode(Base58Utils.encode(bs)));
        }
    }

    @Test
    public void testInt64() {
        byte[][] bss = new byte[2001][];
        bss[0] = BytesUtils.toBytes(Long.MAX_VALUE);
        bss[1] = BytesUtils.toBytes(1 - Long.MAX_VALUE);
        for (int i = -999; i < 1000; i++) {
            bss[1001 + i] = BytesUtils.toBytes((long) i);
        }
        for (byte[] bs : bss) {
            Assert.assertArrayEquals(bs, Base58Utils.decode(Base58Utils.encode(bs)));
        }
    }

    @Test
    public void testBytes() {
        Random random = new Random();
        byte[][] bss = new byte[1024][];
        for (int i = 0; i < bss.length; i++) {
            bss[i] = generateString(random, random.nextInt(1024)).getBytes();
        }
        for (byte[] bs : bss) {
            Assert.assertArrayEquals(bs, Base58Utils.decode(Base58Utils.encode(bs)));
        }
    }

    public String generateString(Random random, int length) {
        String chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }

        return sb.toString();
    }

}
