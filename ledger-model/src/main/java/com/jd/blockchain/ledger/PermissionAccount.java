package com.jd.blockchain.ledger;

import com.jd.binaryproto.DataContract;
import com.jd.binaryproto.DataField;
import com.jd.blockchain.consts.DataCodes;
import com.jd.blockchain.ledger.AccountModeBits;
import com.jd.blockchain.ledger.DataPermission;

/**
 * Account with Dataset permission.
 */
@DataContract(code= DataCodes.ACCOUNT_PERMISSION)
public interface PermissionAccount {

    /**
     * Get Account Dataset permission
     *
     * @return
     */
    @DataField(order = 0, refContract = true)
    DataPermission getPermission();

    /**
     * Set Account Dataset permission
     *
     * @param permission
     */
    void setPermission(DataPermission permission);

    /**
     * Update mode bits for the Account Dataset
     *
     * @param modeBits
     */
    void setModeBits(AccountModeBits modeBits);

    /**
     * Update role of the Account Dataset
     *
     * @param role
     */
    void setRole(String role);

}
