package com.jd.blockchain.ledger;

import utils.io.BytesSerializable;

import java.io.Serializable;
import java.util.Arrays;
import java.util.BitSet;

/**
 * Mode bits, like file permission in linux:
 * <p>
 * Each Dataset has three user based permission groups:
 * <p>
 * owners – The Owners permissions apply only the owners of the Dataset, they will not impact the actions of other users.
 * role – The Role permissions apply only to the role that has been assigned to the Dataset, they will not effect the actions of other users.
 * all users – The All Users permissions apply to all other users on the system, this is the permission group that you want to watch the most.
 * <p>
 * data type  owners' permissions  role's permissions  all users' permissions
 * 0            1   2   3           4   5   6           7   8   9
 * -/c         -/r -/w -/x         -/r -/w -/x         -/r -/w -/x
 * 0/1         0/1 0/1 0/1         0/1 0/1 0/1         0/1 0/1 0/1
 *
 * @return
 */
public class AccountModeBits implements BytesSerializable, Serializable {
    private static final int MODE_BITS_SIZE = 10;

    private BitSet modeBits;

    public AccountModeBits(byte[] modeBits) {
        this.modeBits = BitSet.valueOf(modeBits);
    }

    public AccountModeBits(BitSet modeBits) {
        this.modeBits = (BitSet) modeBits.clone();
    }

    public AccountModeBits(AccountType type) {
        modeBits = new BitSet(MODE_BITS_SIZE);
        if (type == AccountType.CONTRACT) {
            modeBits.set(0);
        }
    }

    public AccountModeBits(String perms) {
        if (null == perms || perms.length() != MODE_BITS_SIZE) {
            throw new IllegalStateException(String.format("Mode bits [%s] error!", null != perms ? perms : ""));
        }
        modeBits = new BitSet(MODE_BITS_SIZE);
        if ('c' == perms.charAt(0)) {
            modeBits.set(0);
        }
        if ('r' == perms.charAt(1)) {
            modeBits.set(1);
        }
        if ('w' == perms.charAt(2)) {
            modeBits.set(2);
        }
        if ('x' == perms.charAt(3)) {
            modeBits.set(3);
        }
        if ('r' == perms.charAt(4)) {
            modeBits.set(4);
        }
        if ('w' == perms.charAt(5)) {
            modeBits.set(5);
        }
        if ('x' == perms.charAt(6)) {
            modeBits.set(6);
        }
        if ('r' == perms.charAt(7)) {
            modeBits.set(7);
        }
        if ('w' == perms.charAt(8)) {
            modeBits.set(8);
        }
        if ('x' == perms.charAt(9)) {
            modeBits.set(9);
        }
    }

    public AccountModeBits(AccountType type, int mode) {
        if (mode > 777 || mode < 0) {
            throw new IllegalStateException(String.format("Mode value [%s] error!", mode));
        }
        modeBits = new BitSet(MODE_BITS_SIZE);
        if (type == AccountType.CONTRACT) {
            modeBits.set(0);
        }
        setGroupBits(BitGroup.OWNERS, mode / 100);
        setGroupBits(BitGroup.ROLE, mode % 100 / 10);
        setGroupBits(BitGroup.ALL, mode % 10);
    }

    private void setGroupBits(BitGroup group, int value) {
        switch (value) {
            case 1:
                modeBits.set(group.offset + 2);
                break;
            case 2:
                modeBits.set(group.offset + 1);
                break;
            case 3:
                modeBits.set(group.offset + 1);
                modeBits.set(group.offset + 2);
                break;
            case 4:
            case 5:
                modeBits.set(group.offset);
                break;
            case 6:
                modeBits.set(group.offset);
                modeBits.set(group.offset + 1);
                break;
            case 7:
                modeBits.set(group.offset);
                modeBits.set(group.offset + 1);
                modeBits.set(group.offset + 2);
                break;
        }
    }

    @Override
    public byte[] toBytes() {
        return modeBits.toByteArray();
    }

    public boolean get(BitGroup group, int index) {
        return modeBits.get(group.offset + index);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(modeBits.get(0) ? "c" : "-");
        builder.append(modeBits.get(1) ? "r" : "-");
        builder.append(modeBits.get(2) ? "w" : "-");
        builder.append(modeBits.get(3) ? "x" : "-");
        builder.append(modeBits.get(4) ? "r" : "-");
        builder.append(modeBits.get(5) ? "w" : "-");
        builder.append(modeBits.get(6) ? "x" : "-");
        builder.append(modeBits.get(7) ? "r" : "-");
        builder.append(modeBits.get(8) ? "w" : "-");
        builder.append(modeBits.get(9) ? "x" : "-");

        return builder.toString();
    }

    public enum BitGroup {
        ZERO(0),
        OWNERS(1),
        ROLE(4),
        ALL(7);

        int offset;

        BitGroup(int offset) {
            this.offset = offset;
        }

    }
}
