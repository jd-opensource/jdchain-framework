package com.jd.blockchain.ledger.json;

import com.jd.blockchain.ledger.BytesValue;
import com.jd.blockchain.ledger.ConsensusSettingsUpdateOperation;
import com.jd.blockchain.ledger.ContractCodeDeployOperation;
import com.jd.blockchain.ledger.ContractEventSendOperation;
import com.jd.blockchain.ledger.DataAccountKVSetOperation;
import com.jd.blockchain.ledger.DataAccountRegisterOperation;
import com.jd.blockchain.ledger.EventAccountRegisterOperation;
import com.jd.blockchain.ledger.EventPublishOperation;
import com.jd.blockchain.ledger.LedgerInitOperation;
import com.jd.blockchain.ledger.LedgerPrivilegeBitset;
import com.jd.blockchain.ledger.ParticipantRegisterOperation;
import com.jd.blockchain.ledger.ParticipantStateUpdateOperation;
import com.jd.blockchain.ledger.RolesConfigureOperation;
import com.jd.blockchain.ledger.RootCAUpdateOperation;
import com.jd.blockchain.ledger.TransactionPrivilegeBitset;
import com.jd.blockchain.ledger.UserAuthorizeOperation;
import com.jd.blockchain.ledger.UserCAUpdateOperation;
import com.jd.blockchain.ledger.UserInfoSetOperation;
import com.jd.blockchain.ledger.UserRegisterOperation;
import com.jd.blockchain.ledger.UserStateUpdateOperation;
import utils.Bytes;
import utils.io.BytesSlice;
import utils.serialize.json.JSONAutoConfigure;
import utils.serialize.json.JSONConfigurator;

public class LedgerModelJSONConfigure implements JSONAutoConfigure {

	@Override
	public void configure(JSONConfigurator configurator) {
		//以下配置针对 TransactionContent.getOperations() 方法定义的 Operation 
		configurator.configProxyInterfaces(ConsensusSettingsUpdateOperation.class);
		configurator.configProxyInterfaces(ContractCodeDeployOperation.class);
		configurator.configProxyInterfaces(ContractEventSendOperation.class);
		configurator.configProxyInterfaces(DataAccountKVSetOperation.class);
		configurator.configProxyInterfaces(DataAccountRegisterOperation.class);
		configurator.configProxyInterfaces(EventAccountRegisterOperation.class);
		configurator.configProxyInterfaces(EventPublishOperation.class);
		configurator.configProxyInterfaces(LedgerInitOperation.class);
		configurator.configProxyInterfaces(ParticipantRegisterOperation.class);
		configurator.configProxyInterfaces(ParticipantStateUpdateOperation.class);
		configurator.configProxyInterfaces(RolesConfigureOperation.class);
		configurator.configProxyInterfaces(UserAuthorizeOperation.class);
		configurator.configProxyInterfaces(UserInfoSetOperation.class);
		configurator.configProxyInterfaces(UserRegisterOperation.class);
		configurator.configProxyInterfaces(UserCAUpdateOperation.class);
		configurator.configProxyInterfaces(UserStateUpdateOperation.class);
		configurator.configProxyInterfaces(RootCAUpdateOperation.class);

		// BytesValue
		configurator.configSuperSerializer(BytesValue.class, BytesValueSerializer.INSTANCE);
		configurator.configDeserializer(BytesValue.class, BytesValueDeserializer.INSTANCE);
		// Bytes
		configurator.configSerializer(Bytes.class, BytesSerializer.INSTANCE);
		configurator.configDeserializer(Bytes.class, BytesDeserializer.INSTANCE);
		// BytesSlice
		configurator.configSerializer(BytesSlice.class, BytesSliceSerializer.INSTANCE);
		configurator.configDeserializer(BytesSlice.class, BytesSliceDeserializer.INSTANCE);
		// LedgerPrivilegeBitset
		configurator.configDeserializer(LedgerPrivilegeBitset.class, LedgerPrivilegeBitsetDeserializer.INSTANCE);
		// TransactionPrivilegeBitset
		configurator.configDeserializer(TransactionPrivilegeBitset.class, TransactionPrivilegeBitsetDeserializer.INSTANCE);
	}

}
