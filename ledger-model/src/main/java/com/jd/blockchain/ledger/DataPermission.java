package com.jd.blockchain.ledger;

import com.jd.binaryproto.DataContract;
import com.jd.binaryproto.DataField;
import com.jd.binaryproto.PrimitiveType;
import com.jd.blockchain.consts.DataCodes;
import utils.Bytes;

/**
 * Basic Dataset Permissions
 */
@DataContract(code = DataCodes.DATA_PERMISSION)
public interface DataPermission {

    /**
     * Mode bits, like file permission in linux:
     *
     * @return
     */
    @DataField(order = 0, primitiveType = PrimitiveType.BYTES)
    AccountModeBits getModeBits();

    /**
     * Data set owners, use endpoint signers in creating tx
     *
     * @return
     */
    @DataField(order = 1, primitiveType = PrimitiveType.BYTES, list = true)
    Bytes[] getOwners();

    /**
     * All users belonging to the role have the same role permissions access to the data
     *
     * @return
     */
    @DataField(order = 2, primitiveType = PrimitiveType.TEXT)
    String getRole();
}
