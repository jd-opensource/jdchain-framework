package com.jd.blockchain.contract;

import com.jd.blockchain.ledger.LedgerQueryService;
import com.jd.blockchain.transaction.BlockchainQueryService;
import com.jd.blockchain.transaction.DataAccountOperator;
import com.jd.blockchain.transaction.EventOperator;
import com.jd.blockchain.transaction.MetaInfoOperator;
import com.jd.blockchain.transaction.SimpleContractOperator;
import com.jd.blockchain.transaction.SimpleSecurityOperator;
import com.jd.blockchain.transaction.UserOperator;

public interface LedgerContext extends BlockchainQueryService, UserOperator, DataAccountOperator, EventOperator, LedgerQueryService, MetaInfoOperator, SimpleContractOperator, SimpleSecurityOperator {

}
