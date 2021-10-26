package test.com.jd.blockchain.ledger;

import com.jd.binaryproto.BinaryProtocol;
import com.jd.blockchain.ledger.AccountDataPermission;
import com.jd.blockchain.ledger.AccountModeBits;
import com.jd.blockchain.ledger.AccountType;
import com.jd.blockchain.ledger.DataPermission;
import org.junit.Assert;
import org.junit.Test;
import utils.Bytes;

import java.util.BitSet;

/**
 * @description: 账户权限值测试用例
 * @author: imuge
 * @date: 2021/10/25
 **/
public class AccountModeBitsTest {

    @Test
    public void testConstructors() {
        AccountModeBits modeBits = new AccountModeBits(new byte[0]);
        Assert.assertEquals("----------", modeBits.toString());
        modeBits = new AccountModeBits(new byte[]{1});
        Assert.assertEquals("c---------", modeBits.toString());
        modeBits = new AccountModeBits(new byte[]{-2, 3});
        Assert.assertEquals("-rwxrwxrwx", modeBits.toString());
        modeBits = new AccountModeBits(BitSet.valueOf(new byte[0]));
        Assert.assertEquals("----------", modeBits.toString());
        modeBits = new AccountModeBits(BitSet.valueOf(new byte[]{0}));
        Assert.assertEquals("----------", modeBits.toString());
        modeBits = new AccountModeBits(BitSet.valueOf(new byte[]{1}));
        Assert.assertEquals("c---------", modeBits.toString());
        modeBits = new AccountModeBits(BitSet.valueOf(new byte[]{-2, 3}));
        Assert.assertEquals("-rwxrwxrwx", modeBits.toString());
        modeBits = new AccountModeBits(AccountType.DATA);
        Assert.assertEquals("----------", modeBits.toString());
        modeBits = new AccountModeBits(AccountType.EVENT);
        Assert.assertEquals("----------", modeBits.toString());
        modeBits = new AccountModeBits(AccountType.CONTRACT);
        Assert.assertEquals("c---------", modeBits.toString());
        modeBits = new AccountModeBits("----------");
        Assert.assertEquals(1, modeBits.toBytes().length);
        modeBits = new AccountModeBits("1---------");
        Assert.assertEquals(1, modeBits.toBytes().length);
        modeBits = new AccountModeBits("-rwxrwxrwx");
        Assert.assertArrayEquals(new byte[]{-2, 3}, modeBits.toBytes());
        modeBits = new AccountModeBits("crwxrwxrwx");
        Assert.assertArrayEquals(new byte[]{-1, 3}, modeBits.toBytes());
        modeBits = new AccountModeBits("-rwx---rwx");
        Assert.assertArrayEquals(new byte[]{-114, 3}, modeBits.toBytes());
        modeBits = new AccountModeBits("crwx---rwx");
        Assert.assertArrayEquals(new byte[]{-113, 3}, modeBits.toBytes());
        modeBits = new AccountModeBits("-------rwx");
        Assert.assertArrayEquals(new byte[]{-128, 3}, modeBits.toBytes());
        modeBits = new AccountModeBits("c------rwx");
        Assert.assertArrayEquals(new byte[]{-127, 3}, modeBits.toBytes());
        modeBits = new AccountModeBits("-rwx------");
        Assert.assertArrayEquals(new byte[]{14}, modeBits.toBytes());
        modeBits = new AccountModeBits("crwx------");
        Assert.assertArrayEquals(new byte[]{15}, modeBits.toBytes());
        modeBits = new AccountModeBits("----rwx---");
        Assert.assertArrayEquals(new byte[]{112}, modeBits.toBytes());
        modeBits = new AccountModeBits("c---rwx---");
        Assert.assertArrayEquals(new byte[]{113}, modeBits.toBytes());
    }

    @Test
    public void testSerialize() {
        AccountDataPermission permission = new AccountDataPermission(new AccountModeBits(AccountType.DATA, 0), new Bytes[]{}, "DEFAULT");
        byte[] encode = BinaryProtocol.encode(permission);
        DataPermission permissionDecoded = BinaryProtocol.decode(encode);
        Assert.assertEquals(permission.getModeBits().toString(), permissionDecoded.getModeBits().toString());
        Assert.assertArrayEquals(permission.getModeBits().toBytes(), permissionDecoded.getModeBits().toBytes());

        permission = new AccountDataPermission(new AccountModeBits(AccountType.CONTRACT, 0), new Bytes[]{}, "DEFAULT");
        encode = BinaryProtocol.encode(permission);
        permissionDecoded = BinaryProtocol.decode(encode);
        Assert.assertEquals(permission.getModeBits().toString(), permissionDecoded.getModeBits().toString());
        Assert.assertArrayEquals(permission.getModeBits().toBytes(), permissionDecoded.getModeBits().toBytes());

        permission = new AccountDataPermission(new AccountModeBits(AccountType.DATA, 777), new Bytes[]{}, "DEFAULT");
        encode = BinaryProtocol.encode(permission);
        permissionDecoded = BinaryProtocol.decode(encode);
        Assert.assertEquals(permission.getModeBits().toString(), permissionDecoded.getModeBits().toString());
        Assert.assertArrayEquals(permission.getModeBits().toBytes(), permissionDecoded.getModeBits().toBytes());

        permission = new AccountDataPermission(new AccountModeBits(AccountType.CONTRACT, 777), new Bytes[]{}, "DEFAULT");
        encode = BinaryProtocol.encode(permission);
        permissionDecoded = BinaryProtocol.decode(encode);
        Assert.assertEquals(permission.getModeBits().toString(), permissionDecoded.getModeBits().toString());
        Assert.assertArrayEquals(permission.getModeBits().toBytes(), permissionDecoded.getModeBits().toBytes());

        permission = new AccountDataPermission(new AccountModeBits(AccountType.DATA, 700), new Bytes[]{}, "DEFAULT");
        encode = BinaryProtocol.encode(permission);
        permissionDecoded = BinaryProtocol.decode(encode);
        Assert.assertEquals(permission.getModeBits().toString(), permissionDecoded.getModeBits().toString());
        Assert.assertArrayEquals(permission.getModeBits().toBytes(), permissionDecoded.getModeBits().toBytes());

        permission = new AccountDataPermission(new AccountModeBits(AccountType.CONTRACT, 700), new Bytes[]{}, "DEFAULT");
        encode = BinaryProtocol.encode(permission);
        permissionDecoded = BinaryProtocol.decode(encode);
        Assert.assertEquals(permission.getModeBits().toString(), permissionDecoded.getModeBits().toString());
        Assert.assertArrayEquals(permission.getModeBits().toBytes(), permissionDecoded.getModeBits().toBytes());

        permission = new AccountDataPermission(new AccountModeBits(AccountType.DATA, 70), new Bytes[]{}, "DEFAULT");
        encode = BinaryProtocol.encode(permission);
        permissionDecoded = BinaryProtocol.decode(encode);
        Assert.assertEquals(permission.getModeBits().toString(), permissionDecoded.getModeBits().toString());
        Assert.assertArrayEquals(permission.getModeBits().toBytes(), permissionDecoded.getModeBits().toBytes());

        permission = new AccountDataPermission(new AccountModeBits(AccountType.CONTRACT, 70), new Bytes[]{}, "DEFAULT");
        encode = BinaryProtocol.encode(permission);
        permissionDecoded = BinaryProtocol.decode(encode);
        Assert.assertEquals(permission.getModeBits().toString(), permissionDecoded.getModeBits().toString());
        Assert.assertArrayEquals(permission.getModeBits().toBytes(), permissionDecoded.getModeBits().toBytes());
    }

}
