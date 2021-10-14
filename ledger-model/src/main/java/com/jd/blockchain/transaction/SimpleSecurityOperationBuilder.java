package com.jd.blockchain.transaction;

import com.jd.blockchain.ledger.LedgerPermission;
import com.jd.blockchain.ledger.RolesPolicy;
import com.jd.blockchain.ledger.TransactionPermission;
import utils.Bytes;

public interface SimpleSecurityOperationBuilder {

    SimpleRoleConfigurer role(String role);

    SimpleUserAuthorizer authorziation(Bytes user);

    interface SimpleRoleConfigurer {

        void enable(LedgerPermission... enableLedgerPermissions);

        void enable(TransactionPermission... enableTransactionPermissions);

        void enable(LedgerPermission[] enableLedgerPermissions, TransactionPermission[] enableTransactionPermissions);

        void disable(LedgerPermission... disableLedgerPermissions);

        void disable(TransactionPermission... disableTransactionPermissions);

        void disable(LedgerPermission[] disableLedgerPermissions, TransactionPermission[] disableTransactionPermissions);

        void configure(LedgerPermission[] enableLedgerPermissions, TransactionPermission[] enableTransactionPermissions, LedgerPermission[] disableLedgerPermissions, TransactionPermission[] disableTransactionPermissions);
    }

    interface SimpleUserAuthorizer {

        void authorize(String... roles);

        void unauthorize(String... roles);

        void setPolicy(RolesPolicy rolePolicy);
    }

}
