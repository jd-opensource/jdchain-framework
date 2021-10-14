package com.jd.blockchain.ledger;

import com.jd.binaryproto.DataContract;
import com.jd.binaryproto.DataField;
import com.jd.binaryproto.PrimitiveType;
import com.jd.blockchain.consts.DataCodes;
import utils.Bytes;

@DataContract(code = DataCodes.TX_OP_ACC_PERMISSION_SET)
public interface AccountPermissionSetOperation extends Operation {

    /**
     * Account address
     *
     * @return
     */
    @DataField(order = 0, primitiveType = PrimitiveType.BYTES)
    Bytes getAddress();

    /**
     * Account type
     *
     * @return
     */
    @DataField(order = 2, refEnum = true)
    AccountType getAccountType();

    /**
     * Mode bits value in int
     *
     * @return
     */
    @DataField(order = 3, primitiveType = PrimitiveType.INT32)
    int getMode();

    /**
     * New role to replace the old
     *
     * @return
     */
    @DataField(order = 4, primitiveType = PrimitiveType.TEXT)
    String getRole();

}
