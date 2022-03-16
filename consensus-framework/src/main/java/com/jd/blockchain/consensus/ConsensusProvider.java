package com.jd.blockchain.consensus;

import com.jd.blockchain.consensus.client.ConsensusClientProvider;
import com.jd.blockchain.consensus.manage.ConsensusManagerProvider;
import com.jd.blockchain.consensus.service.ConsensusServiceProvider;
import com.jd.blockchain.ledger.ConsensusTypeEnum;

public interface ConsensusProvider extends ConsensusClientProvider, ConsensusServiceProvider, ConsensusManagerProvider {

	String getName();

	SettingsFactory getSettingsFactory();

	ConsensusTypeEnum getConsensusType();
}
