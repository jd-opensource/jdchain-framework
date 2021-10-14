package com.jd.blockchain.transaction;

import com.jd.blockchain.ledger.AccountPermissionSetOperation;

public interface AccountPermissionSetOperationBuilder {

    /**
     * Account permission operation
     *
     * @return
     */
    AccountPermissionSetOperation getOperation();

    /**
     * Mode setting
     *
     * @param mode
     * @return
     */
    AccountPermissionSetOperationBuilder mode(int mode);

    /**
     * Roles setting
     *
     * @param role
     * @return
     */
    AccountPermissionSetOperationBuilder role(String role);

}
