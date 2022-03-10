package com.jd.blockchain.ledger.binaryproto;

import com.jd.binaryproto.DataContractAutoRegistrar;
import com.jd.binaryproto.DataContractRegistry;
import com.jd.blockchain.crypto.CryptoAlgorithm;
import com.jd.blockchain.crypto.CryptoProvider;
import com.jd.blockchain.ledger.*;

public class LedgerModelDataContractAutoRegistrar implements DataContractAutoRegistrar {

    @Override
    public void initContext(DataContractRegistry registry) {
        DataContractRegistry.register(MerkleSnapshot.class);
        DataContractRegistry.register(BlockchainIdentity.class);
        DataContractRegistry.register(AccountSnapshot.class);

        DataContractRegistry.register(DataAccountInfo.class);
        DataContractRegistry.register(ContractInfo.class);
        DataContractRegistry.register(EventAccountInfo.class);

        DataContractRegistry.register(BytesValue.class);
        DataContractRegistry.register(BytesValueList.class);
        DataContractRegistry.register(BlockchainIdentity.class);
        DataContractRegistry.register(LedgerBlock.class);
        DataContractRegistry.register(BlockBody.class);
        DataContractRegistry.register(LedgerDataSnapshot.class);
        DataContractRegistry.register(LedgerAdminInfo.class);
        DataContractRegistry.register(TransactionContent.class);
        DataContractRegistry.register(TransactionRequest.class);
        DataContractRegistry.register(TransactionResult.class);
        DataContractRegistry.register(LedgerTransaction.class);
        DataContractRegistry.register(Operation.class);
        DataContractRegistry.register(LedgerInitOperation.class);
        DataContractRegistry.register(UserRegisterOperation.class);
        DataContractRegistry.register(UserInfoSetOperation.class);
        DataContractRegistry.register(UserInfoSetOperation.KVEntry.class);
        DataContractRegistry.register(DataAccountRegisterOperation.class);
        DataContractRegistry.register(DataAccountKVSetOperation.class);
        DataContractRegistry.register(DataAccountKVSetOperation.KVWriteEntry.class);
        DataContractRegistry.register(ContractCodeDeployOperation.class);
        DataContractRegistry.register(ContractEventSendOperation.class);
        DataContractRegistry.register(ContractCrossEventSendOperation.class);
        DataContractRegistry.register(ParticipantRegisterOperation.class);
        DataContractRegistry.register(ParticipantStateUpdateOperation.class);
        DataContractRegistry.register(TransactionResponse.class);
        DataContractRegistry.register(OperationResult.class);
        DataContractRegistry.register(RolesConfigureOperation.class);
        DataContractRegistry.register(RolesConfigureOperation.RolePrivilegeEntry.class);
        DataContractRegistry.register(UserAuthorizeOperation.class);
        DataContractRegistry.register(UserAuthorizeOperation.UserRolesEntry.class);
        DataContractRegistry.register(EventAccountRegisterOperation.class);
        DataContractRegistry.register(EventPublishOperation.class);
        DataContractRegistry.register(EventPublishOperation.EventEntry.class);
        DataContractRegistry.register(ConsensusSettingsUpdateOperation.class);
        DataContractRegistry.register(ConsensusReconfigOperation.class);
        DataContractRegistry.register(HashAlgorithmUpdateOperation.class);
        DataContractRegistry.register(PrivilegeSet.class);
        DataContractRegistry.register(RoleSet.class);
        DataContractRegistry.register(SecurityInitSettings.class);
        DataContractRegistry.register(RoleInitSettings.class);
        DataContractRegistry.register(UserAuthInitSettings.class);
        DataContractRegistry.register(Event.class);
        DataContractRegistry.register(LedgerMetadata.class);
        DataContractRegistry.register(LedgerMetadata_V2.class);
        DataContractRegistry.register(LedgerInitSetting.class);
        DataContractRegistry.register(LedgerSettings.class);
        DataContractRegistry.register(ParticipantNode.class);
        DataContractRegistry.register(ParticipantStateUpdateInfo.class);
        DataContractRegistry.register(CryptoSetting.class);
        DataContractRegistry.register(CryptoProvider.class);
        DataContractRegistry.register(UserAccountHeader.class);
        DataContractRegistry.register(UserInfo.class);
        DataContractRegistry.register(HashObject.class);
        DataContractRegistry.register(CryptoAlgorithm.class);
        DataContractRegistry.register(DigitalSignature.class);
        DataContractRegistry.register(DigitalSignatureBody.class);
        DataContractRegistry.register(LedgerTransactions.class);
        DataContractRegistry.register(UserCAUpdateOperation.class);
        DataContractRegistry.register(RootCAUpdateOperation.class);
        DataContractRegistry.register(UserStateUpdateOperation.class);
        DataContractRegistry.register(GenesisUser.class);
        DataContractRegistry.register(ContractStateUpdateOperation.class);
        DataContractRegistry.register(AccountPermissionSetOperation.class);
        DataContractRegistry.register(ContractRuntimeConfig.class);
    }
}
