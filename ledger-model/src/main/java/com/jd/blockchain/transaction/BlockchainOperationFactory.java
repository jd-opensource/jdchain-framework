package com.jd.blockchain.transaction;

import com.jd.blockchain.ledger.*;
import utils.Bytes;
import utils.Property;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author huanghaiquan
 *
 */
public class BlockchainOperationFactory implements ClientOperator, LedgerInitOperator {

	private static final SecurityOperationBuilderImpl SECURITY_OP_BUILDER = new SecurityOperationBuilderImpl();

	private static final LedgerInitOperationBuilderImpl LEDGER_INIT_OP_BUILDER = new LedgerInitOperationBuilderImpl();

	private static final UserRegisterOperationBuilderImpl USER_REG_OP_BUILDER = new UserRegisterOperationBuilderImpl();

	private static final DataAccountRegisterOperationBuilderImpl DATA_ACC_REG_OP_BUILDER = new DataAccountRegisterOperationBuilderImpl();

	private static final ContractCodeDeployOperationBuilderImpl CONTRACT_CODE_DEPLOY_OP_BUILDER = new ContractCodeDeployOperationBuilderImpl();

	private SecurityOperationBuilderFilter securityOpBuilder = new SecurityOperationBuilderFilter();

	private static final ParticipantRegisterOperationBuilderImpl PARTICIPANT_REG_OP_BUILDER = new ParticipantRegisterOperationBuilderImpl();

	private static final ParticipantStateUpdateOperationBuilderImpl PARTICIPANT_STATE_UPDATE_OP_BUILDER = new ParticipantStateUpdateOperationBuilderImpl();

	private static final ConsensusSettingsUpdateOperationBuilderImpl CONSENSUS_UPDATE_OPERATION_BUILDER = new ConsensusSettingsUpdateOperationBuilderImpl();

	private static final SettingsOperationBuilderImpl SETTINGS_OPERATION_BUILDER = new SettingsOperationBuilderImpl();

	private static final EventAccountRegisterOperationBuilderImpl EVENT_ACC_REG_OP_BUILDER = new EventAccountRegisterOperationBuilderImpl();

	private LedgerInitOperationBuilder ledgerInitOpBuilder = new LedgerInitOperationBuilderFilter();

	private UserRegisterOperationBuilder userRegOpBuilder = new UserRegisterOperationBuilderFilter();

	private DataAccountRegisterOperationBuilder dataAccRegOpBuilder = new DataAccountRegisterOperationBuilderFilter();

	private ContractCodeDeployOperationBuilder contractCodeDeployOpBuilder = new ContractCodeDeployOperationBuilderFilter();

	private ContractInvocationProxyBuilder contractInvoProxyBuilder = new ContractInvocationProxyBuilder();

	private ParticipantRegisterOperationBuilder participantRegOpBuilder = new ParticipantRegisterOperationBuilderFilter();

	private ParticipantStateUpdateOperationBuilder participantStateModifyOpBuilder = new ParticipantStateUpdateOperationBuilderFilter();

	private ConsensusSettingsUpdateOperationBuilder consensusSettingsUpdateOperationBuilder = new ConsensusSettingsUpdateOperationBuilderFilter();

	private SettingsOperationBuilder settingsOperationBuilder = new SettingsOperationBuilderFilter();

	private EventAccountRegisterOperationBuilder eventAccRegOpBuilder = new EventAccountRegisterOperationBuilderFilter();

	private MetaInfoUpdateOperationBuilder metaInfoUpdateOpBuilder = new MetaInfoUpdateOperationBuilderFilter();

	// TODO: 暂时只支持单线程情形，未考虑多线程；
	private List<Operation> operationList = Collections.synchronizedList(new ArrayList<>());

	@Override
	public LedgerInitOperationBuilder ledgers() {
		return ledgerInitOpBuilder;
	}

	@Override
	public SecurityOperationBuilder security() {
		return securityOpBuilder;
	}

	@Override
	public UserRegisterOperationBuilder users() {
		return userRegOpBuilder;
	}

	@Override
	public UserUpdateOperationBuilder user(String address) {
		return user(Bytes.fromBase58(address));
	}

	@Override
	public UserUpdateOperationBuilder user(Bytes address) {
		return new UserOperationBuilderFilter(address);
	}

	@Override
	public DataAccountRegisterOperationBuilder dataAccounts() {
		return dataAccRegOpBuilder;
	}

	@Override
	public DataAccountOperationBuilder dataAccount(String accountAddress) {
		return new DataAccountOperationBuilderFilter(Bytes.fromBase58(accountAddress));
	}

	@Override
	public DataAccountOperationBuilder dataAccount(Bytes accountAddress) {
		return new DataAccountOperationBuilderFilter(accountAddress);
	}

	@Override
	public ContractCodeDeployOperationBuilder contracts() {
		return contractCodeDeployOpBuilder;
	}

	@Override
	public ParticipantRegisterOperationBuilder participants() {return participantRegOpBuilder;}

	@Override
	public ParticipantStateUpdateOperationBuilder states() {return participantStateModifyOpBuilder;}

	@Override
	public ConsensusSettingsUpdateOperationBuilder consensus() {return consensusSettingsUpdateOperationBuilder;}

	@Override
	public SettingsOperationBuilder settings() {return settingsOperationBuilder;}

	@Override
	public EventAccountRegisterOperationBuilder eventAccounts() {
		return eventAccRegOpBuilder;
	}

	@Override
	public EventOperationBuilder eventAccount(String accountAddress) {
		return new EventOperationBuilderFilter(Bytes.fromBase58(accountAddress));
	}

	@Override
	public EventOperationBuilder eventAccount(Bytes accountAddress) {
		return new EventOperationBuilderFilter(accountAddress);
	}

	@Override
	public <T> T contract(String address, Class<T> contractIntf) {
		return contractInvoProxyBuilder.create(address, -1L, contractIntf, new ContractOperationBuilderFilter(Bytes.fromBase58(address)));
	}

	@Override
	public <T> T contract(Bytes address, Class<T> contractIntf) {
		return contractInvoProxyBuilder.create(address, -1L, contractIntf, new ContractOperationBuilderFilter(address));
	}

	@Override
	public ContractOperationBuilder contract(Bytes address) {
		return new ContractOperationBuilderFilter(address);
	}

	@Override
	public ContractOperationBuilder contract(String address) {
		return new ContractOperationBuilderFilter(Bytes.fromBase58(address));
	}

	@Override
	public ContractOperationBuilder contract() {
		return new ContractOperationBuilderFilter();
	}

	/**
	 * 返回已经定义的操作列表；
	 *
	 * @return
	 */
	public Collection<Operation> getOperations() {
		return operationList;
	}

	/**
	 * 返回与操作列表对应的返回值处理器；
	 *
	 * @return
	 */
	public Collection<OperationResultHandle> getReturnValueHandlers() {
		List<OperationResultHandle> resultHandlers = new ArrayList<OperationResultHandle>();
		int index = 0;
		for (Operation op : operationList) {
			if (op instanceof ContractEventSendOperation) {
				// 操作具有返回值，创建对应的结果处理器；
				ContractEventSendOpTemplate opTemp = (ContractEventSendOpTemplate) op;
				ContractInvocation invocation = opTemp.getInvocation();
				OperationResultHandle retnHandler;
				if (invocation == null) {
					retnHandler = new NullOperationReturnValueHandler(index);
				} else {
					invocation.setOperationIndex(index);
					retnHandler = invocation;
				}
				resultHandlers.add(retnHandler);
			}
			index++;
		}

		return resultHandlers;
	}

	public void clear() {
		operationList.clear();
	}

	@Override
	public MetaInfoUpdateOperationBuilder metaInfo() {
		return metaInfoUpdateOpBuilder;
	}

	// --------------------------------- 内部类型 -----------------------------------

	private class LedgerInitOperationBuilderFilter implements LedgerInitOperationBuilder {

		@Override
		public LedgerInitOperation create(LedgerInitSetting initSetting) {
			LedgerInitOperation op = LEDGER_INIT_OP_BUILDER.create(initSetting);
			operationList.add(op);
			return op;
		}

	}

	private class UserRegisterOperationBuilderFilter implements UserRegisterOperationBuilder {

		@Override
		public UserRegisterOperation register(BlockchainIdentity userID) {
			UserRegisterOperation op = USER_REG_OP_BUILDER.register(userID);
			operationList.add(op);
			return op;
		}

		@Override
		public UserRegisterOperation register(X509Certificate certificate) {
			UserRegisterOperation op = USER_REG_OP_BUILDER.register(certificate);
			operationList.add(op);
			return op;
		}

	}

	private class UserOperationBuilderFilter implements UserUpdateOperationBuilder {

		private Bytes address;

		public UserOperationBuilderFilter(Bytes address) {
			this.address = address;
		}

		@Override
		public UserStateUpdateOperation revoke() {
			UserStateUpdateOperation op = new UserStateUpdateOpTemplate(address, AccountState.REVOKE);
			operationList.add(op);
			return op;
		}

		@Override
		public UserStateUpdateOperation freeze() {
			UserStateUpdateOperation op = new UserStateUpdateOpTemplate(address, AccountState.FREEZE);
			operationList.add(op);
			return op;
		}

		@Override
		public UserStateUpdateOperation restore() {
			UserStateUpdateOperation op = new UserStateUpdateOpTemplate(address, AccountState.NORMAL);
			operationList.add(op);
			return op;
		}

		@Override
		public UserStateUpdateOperation state(AccountState state) {
			UserStateUpdateOperation op = new UserStateUpdateOpTemplate(address, state);
			operationList.add(op);
			return op;
		}

		@Override
		public UserCAUpdateOperation ca(X509Certificate cert) {
			UserCAUpdateOperation op = new UserCAUpdateOpTemplate(address, cert);
			operationList.add(op);
			return op;
		}
	}

	private class ContractOperationBuilderFilter implements ContractOperationBuilder {

		private Bytes address;

		public ContractOperationBuilderFilter(Bytes address) {
			this.address = address;
		}

		public ContractOperationBuilderFilter() {
		}

		@Override
		public ContractStateUpdateOperation revoke() {
			ContractStateUpdateOperation op = new ContractStateUpdateOpTemplate(address, AccountState.REVOKE);
			operationList.add(op);
			return op;
		}

		@Override
		public ContractStateUpdateOperation freeze() {
			ContractStateUpdateOperation op = new ContractStateUpdateOpTemplate(address, AccountState.FREEZE);
			operationList.add(op);
			return op;
		}

		@Override
		public ContractStateUpdateOperation restore() {
			ContractStateUpdateOperation op = new ContractStateUpdateOpTemplate(address, AccountState.NORMAL);
			operationList.add(op);
			return op;
		}

		@Override
		public ContractStateUpdateOperation state(AccountState state) {
			ContractStateUpdateOperation op = new ContractStateUpdateOpTemplate(address, state);
			operationList.add(op);
			return op;
		}

		@Override
		public AccountPermissionSetOperationBuilder permission() {
			return new AccountPermissionSetOperationBuilderFilter(address, AccountType.CONTRACT);
		}

		@Override
		public ContractEventSendOperation send(String address, String event, BytesValueList args) {
			return send(Bytes.fromBase58(address), event, args);
		}

		@Override
		public ContractEventSendOperation send(Bytes address, String event, BytesValueList args) {
			this.address = address;
			return invoke(event, args);
		}

		@Override
		public ContractEventSendOperation invoke(String event, BytesValueList args) {
			return invoke(event, args, -1);
		}

		@Override
		public ContractEventSendOperation invoke(String event, BytesValueList args, long version) {
			ContractEventSendOperation op = new ContractEventSendOpTemplate(address, event, args, version);
			operationList.add(op);
			return op;
		}
	}

	private class SecurityOperationBuilderFilter implements SecurityOperationBuilder {

		@Override
		public RolesConfigurer roles() {
			RolesConfigurer rolesConfigurer = SECURITY_OP_BUILDER.roles();
			operationList.add(rolesConfigurer.getOperation());
			return rolesConfigurer;
		}

		@Override
		public UserAuthorizer authorziations() {
			UserAuthorizer userAuthorizer = SECURITY_OP_BUILDER.authorziations();
			operationList.add(userAuthorizer.getOperation());
			return userAuthorizer;
		}
	}

	private class DataAccountRegisterOperationBuilderFilter implements DataAccountRegisterOperationBuilder {

		@Override
		public DataAccountRegisterOperation register(BlockchainIdentity accountID) {
			DataAccountRegisterOperation op = DATA_ACC_REG_OP_BUILDER.register(accountID);
			operationList.add(op);
			return op;
		}
	}

	private class DataAccountOperationBuilderFilter implements DataAccountOperationBuilder {

		private Bytes address;
		private DataAccountKVSetOperationBuilder innerBuilder;

		private DataAccountKVSetOperation op;

		public DataAccountOperationBuilderFilter(Bytes accountAddress) {
			address = accountAddress;
			innerBuilder = new DataAccountKVSetOperationBuilderImpl(accountAddress);
		}

		@Override
		public DataAccountKVSetOperation getOperation() {
			return innerBuilder.getOperation();
		}

		private void setOperation() {
			if (op == null) {
				op = innerBuilder.getOperation();
				operationList.add(op);
			}
		}

		@Override
		public DataAccountKVSetOperationBuilder set(String key, BytesValue value, long expVersion) {
			innerBuilder.set(key, value, expVersion);
			setOperation();
			return this;
		}

		@Override
		public DataAccountKVSetOperationBuilder setText(String key, String value, long expVersion) {
			innerBuilder.setText(key, value, expVersion);
			setOperation();
			return this;
		}

		@Override
		public DataAccountKVSetOperationBuilder setInt64(String key, long value, long expVersion) {
			innerBuilder.setInt64(key, value, expVersion);
			setOperation();
			return this;
		}

		@Override
		public DataAccountKVSetOperationBuilder setBytes(String key, Bytes value, long expVersion) {
			innerBuilder.setBytes(key, value, expVersion);
			setOperation();
			return this;
		}

		@Override
		public DataAccountKVSetOperationBuilder setBytes(String key, byte[] value, long expVersion) {
			innerBuilder.setBytes(key, value, expVersion);
			setOperation();
			return this;
		}

		@Override
		public DataAccountKVSetOperationBuilder setImage(String key, byte[] value, long expVersion) {
			innerBuilder.setImage(key, value, expVersion);
			setOperation();
			return this;
		}

		@Override
		public DataAccountKVSetOperationBuilder setJSON(String key, String value, long expVersion) {
			innerBuilder.setJSON(key, value, expVersion);
			setOperation();
			return this;
		}

		@Override
		public DataAccountKVSetOperationBuilder setXML(String key, String value, long expVersion) {
			innerBuilder.setXML(key, value, expVersion);
			setOperation();
			return this;
		}

		@Override
		public DataAccountKVSetOperationBuilder setTimestamp(String key, long value, long expVersion) {
			innerBuilder.setTimestamp(key, value, expVersion);
			setOperation();
			return this;
		}

		@Override
		public AccountPermissionSetOperationBuilder permission() {
			return new AccountPermissionSetOperationBuilderFilter(address, AccountType.DATA);
		}
	}

	private class ContractCodeDeployOperationBuilderFilter implements ContractCodeDeployOperationBuilder {
		@Override
		public ContractCodeDeployOperation deploy(BlockchainIdentity id, byte[] chainCode) {
			ContractCodeDeployOperation op = CONTRACT_CODE_DEPLOY_OP_BUILDER.deploy(id, chainCode);
			operationList.add(op);
			return op;
		}

		@Override
		public ContractCodeDeployOperation deploy(BlockchainIdentity id, byte[] chainCode, ContractLang lang) {
			ContractCodeDeployOperation op = CONTRACT_CODE_DEPLOY_OP_BUILDER.deploy(id, chainCode, lang);
			operationList.add(op);
			return op;
		}

		@Override
		public ContractCodeDeployOperation deploy(BlockchainIdentity id, byte[] chainCode, long version) {
			ContractCodeDeployOperation op = CONTRACT_CODE_DEPLOY_OP_BUILDER.deploy(id, chainCode, version);
			operationList.add(op);
			return op;
		}

		@Override
		public ContractCodeDeployOperation deploy(BlockchainIdentity id, byte[] chainCode, long version, ContractLang lang) {
			ContractCodeDeployOperation op = CONTRACT_CODE_DEPLOY_OP_BUILDER.deploy(id, chainCode, version, lang);
			operationList.add(op);
			return op;
		}
	}

	private class ParticipantRegisterOperationBuilderFilter implements ParticipantRegisterOperationBuilder {
		@Override
		public ParticipantRegisterOperation register(String  participantName, BlockchainIdentity participantIdentity) {
			ParticipantRegisterOperation op = PARTICIPANT_REG_OP_BUILDER.register(participantName, participantIdentity);
			operationList.add(op);
			return op;
		}

		@Override
		public ParticipantRegisterOperation register(String participantName, X509Certificate certificate) {
			ParticipantRegisterOperation op = PARTICIPANT_REG_OP_BUILDER.register(participantName, certificate);
			operationList.add(op);
			return op;
		}
	}

	private class ParticipantStateUpdateOperationBuilderFilter implements ParticipantStateUpdateOperationBuilder {
		@Override
		public ParticipantStateUpdateOperation update(BlockchainIdentity blockchainIdentity, ParticipantNodeState participantNodeState) {
			ParticipantStateUpdateOperation op = PARTICIPANT_STATE_UPDATE_OP_BUILDER.update(blockchainIdentity, participantNodeState);
			operationList.add(op);
			return op;
		}
	}

	private class ConsensusSettingsUpdateOperationBuilderFilter implements ConsensusSettingsUpdateOperationBuilder {
		@Override
		public ConsensusSettingsUpdateOperation update(Property[] properties) {
			ConsensusSettingsUpdateOperation op = CONSENSUS_UPDATE_OPERATION_BUILDER.update(properties);
			operationList.add(op);
			return op;
		}

		@Override
		public ConsensusSettingsUpdateOperation update(String provider, Property[] properties) {
			ConsensusSettingsUpdateOperation op = CONSENSUS_UPDATE_OPERATION_BUILDER.update(provider, properties);
			operationList.add(op);
			return op;
		}

		@Override
		public ConsensusReconfigOperation reconfig(String type) {
			ConsensusReconfigOperation op = CONSENSUS_UPDATE_OPERATION_BUILDER.reconfig(type);
			operationList.add(op);
			return op;
		}
	}

	private class SettingsOperationBuilderFilter implements SettingsOperationBuilder {

		@Override
		public HashAlgorithmUpdateOperation hashAlgorithm(String algorithm) {
			HashAlgorithmUpdateOperation op = SETTINGS_OPERATION_BUILDER.hashAlgorithm(algorithm);
			operationList.add(op);
			return op;
		}
	}

	private class EventAccountRegisterOperationBuilderFilter implements EventAccountRegisterOperationBuilder {
		@Override
		public EventAccountRegisterOperation register(BlockchainIdentity accountID) {
			EventAccountRegisterOperation op = EVENT_ACC_REG_OP_BUILDER.register(accountID);
			operationList.add(op);
			return op;
		}
	}

	private class MetaInfoUpdateOperationBuilderFilter implements MetaInfoUpdateOperationBuilder {

		@Override
		public RootCAUpdateOperationBuilder ca() {
			return new RootCAUpdateOperationBuilderFilter();
		}
	}

	private class RootCAUpdateOperationBuilderFilter implements RootCAUpdateOperationBuilder {

		private RootCAUpdateOperationBuilderImpl innerBuilder;
		private RootCAUpdateOperation op;

		private void addOperation() {
			if (op == null) {
				op = innerBuilder.getOperation();
				operationList.add(op);
			}
		}

		RootCAUpdateOperationBuilderFilter() {
			this.innerBuilder = new RootCAUpdateOperationBuilderImpl();
		}

		@Override
		public RootCAUpdateOperationBuilder add(String certificate) {
			innerBuilder.add(certificate);
			addOperation();
			return this;
		}

		@Override
		public RootCAUpdateOperationBuilder add(X509Certificate certificate) {
			innerBuilder.add(certificate);
			addOperation();
			return this;
		}

		@Override
		public RootCAUpdateOperationBuilder update(String certificate) {
			innerBuilder.update(certificate);
			addOperation();
			return this;
		}

		@Override
		public RootCAUpdateOperationBuilder update(X509Certificate certificate) {
			innerBuilder.update(certificate);
			addOperation();
			return this;
		}

		@Override
		public RootCAUpdateOperationBuilder remove(String certificate) {
			innerBuilder.remove(certificate);
			addOperation();
			return this;
		}

		@Override
		public RootCAUpdateOperationBuilder remove(X509Certificate certificate) {
			innerBuilder.remove(certificate);
			addOperation();
			return this;
		}
	}

	private class EventOperationBuilderFilter implements EventOperationBuilder {

		private Bytes account;

		private EventPublishOperationBuilder innerBuilder;

		private EventPublishOperation op;

		public EventOperationBuilderFilter(Bytes accountAddress) {
			account = accountAddress;
			innerBuilder = new EventPublishOperationBuilderImpl(accountAddress);
		}

		private void addOperation() {
			if (op == null) {
				op = innerBuilder.getOperation();
				operationList.add(op);
			}
		}

		@Override
		public EventPublishOperation getOperation() {
			return innerBuilder.getOperation();
		}

		@Override
		public EventPublishOperationBuilder publish(String name, byte[] content, long sequence) {
			innerBuilder.publish(name, content, sequence);
			addOperation();
			return this;
		}

		@Override
		public EventPublishOperationBuilder publish(String name, Bytes content, long sequence) {
			innerBuilder.publish(name, content, sequence);
			addOperation();
			return this;
		}

		@Override
		public EventPublishOperationBuilder publish(String name, String content, long sequence) {
			innerBuilder.publish(name, content, sequence);
			addOperation();
			return this;
		}

		@Override
		public EventPublishOperationBuilder publish(String name, long content, long sequence) {
			innerBuilder.publish(name, content, sequence);
			addOperation();
			return this;
		}

		@Override
		public EventPublishOperationBuilder publishTimestamp(String name, long content, long sequence) {
			innerBuilder.publishTimestamp(name, content, sequence);
			addOperation();
			return this;
		}

		@Override
		public EventPublishOperationBuilder publishImage(String name, byte[] content, long sequence) {
			innerBuilder.publishImage(name, content, sequence);
			addOperation();
			return this;
		}

		@Override
		public EventPublishOperationBuilder publishJSON(String name, String content, long sequence) {
			innerBuilder.publishJSON(name, content, sequence);
			addOperation();
			return this;
		}

		@Override
		public EventPublishOperationBuilder publishXML(String name, String content, long sequence) {
			innerBuilder.publishXML(name, content, sequence);
			addOperation();
			return this;
		}

		@Override
		public AccountPermissionSetOperationBuilder permission() {
			return new AccountPermissionSetOperationBuilderFilter(account, AccountType.EVENT);
		}
	}

	private class AccountPermissionSetOperationBuilderFilter implements AccountPermissionSetOperationBuilder {

		private AccountPermissionSetOperationBuilder innerBuilder;

		private AccountPermissionSetOperation op;

		public AccountPermissionSetOperationBuilderFilter(Bytes accountAddress, AccountType accountType) {
			innerBuilder = new AccountPermissionSetOperationBuilderImpl(accountAddress, accountType);
		}

		@Override
		public AccountPermissionSetOperation getOperation() {
			return innerBuilder.getOperation();
		}

		private void addOperation() {
			if (op == null) {
				op = innerBuilder.getOperation();
				operationList.add(op);
			}
		}

		@Override
		public AccountPermissionSetOperationBuilder mode(int mode) {
			innerBuilder.mode(mode);
			addOperation();
			return this;
		}

		@Override
		public AccountPermissionSetOperationBuilder role(String role) {
			innerBuilder.role(role);
			addOperation();
			return this;
		}
	}

	/**
	 * 不做任何操作的返回值处理器；
	 *
	 * @author huanghaiquan
	 *
	 */
	private static class NullOperationReturnValueHandler implements OperationResultHandle {

		private int operationIndex;

		public NullOperationReturnValueHandler(int operationIndex) {
			this.operationIndex = operationIndex;
		}

		@Override
		public int getOperationIndex() {
			return operationIndex;
		}

		@Override
		public Object complete(BytesValue bytesValue) {
			return null;
		}

		@Override
		public void complete(Throwable error) {
		}

	}

}
