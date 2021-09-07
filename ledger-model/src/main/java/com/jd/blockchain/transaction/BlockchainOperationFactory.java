package com.jd.blockchain.transaction;

import com.jd.blockchain.ca.X509Utils;
import com.jd.blockchain.ledger.BlockchainIdentity;
import com.jd.blockchain.ledger.BlockchainIdentityData;
import com.jd.blockchain.ledger.BytesValue;
import com.jd.blockchain.ledger.BytesValueList;
import com.jd.blockchain.ledger.ConsensusSettingsUpdateOperation;
import com.jd.blockchain.ledger.ContractCodeDeployOperation;
import com.jd.blockchain.ledger.ContractEventSendOperation;
import com.jd.blockchain.ledger.DataAccountKVSetOperation;
import com.jd.blockchain.ledger.DataAccountRegisterOperation;
import com.jd.blockchain.ledger.EventAccountRegisterOperation;
import com.jd.blockchain.ledger.EventPublishOperation;
import com.jd.blockchain.ledger.LedgerInitOperation;
import com.jd.blockchain.ledger.LedgerInitSetting;
import com.jd.blockchain.ledger.Operation;
import com.jd.blockchain.ledger.ParticipantNodeState;
import com.jd.blockchain.ledger.ParticipantRegisterOperation;
import com.jd.blockchain.ledger.ParticipantStateUpdateOperation;
import com.jd.blockchain.ledger.RootCAUpdateOperation;
import com.jd.blockchain.ledger.RootCAUpdateOperationBuilder;
import com.jd.blockchain.ledger.RootCAUpdateOperationBuilderImpl;
import com.jd.blockchain.ledger.UserCAUpdateOperation;
import com.jd.blockchain.ledger.UserRegisterOperation;

import com.jd.blockchain.ledger.UserState;
import com.jd.blockchain.ledger.UserStateUpdateOperation;
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

	private static final ConsensusSettingsUpdateOperationBuilderImpl CONSENSUS_SETTINGS_UPDATE_OPERATION_BUILDER = new ConsensusSettingsUpdateOperationBuilderImpl();

	private static final EventAccountRegisterOperationBuilderImpl EVENT_ACC_REG_OP_BUILDER = new EventAccountRegisterOperationBuilderImpl();

	private LedgerInitOperationBuilder ledgerInitOpBuilder = new LedgerInitOperationBuilderFilter();

	private UserRegisterOperationBuilder userRegOpBuilder = new UserRegisterOperationBuilderFilter();

	private DataAccountRegisterOperationBuilder dataAccRegOpBuilder = new DataAccountRegisterOperationBuilderFilter();

	private ContractCodeDeployOperationBuilder contractCodeDeployOpBuilder = new ContractCodeDeployOperationBuilderFilter();

	private ContractEventSendOperationBuilder contractEventSendOpBuilder = new ContractEventSendOperationBuilderFilter();

	private ContractInvocationProxyBuilder contractInvoProxyBuilder = new ContractInvocationProxyBuilder();

	private ParticipantRegisterOperationBuilder participantRegOpBuilder = new ParticipantRegisterOperationBuilderFilter();

	private ParticipantStateUpdateOperationBuilder participantStateModifyOpBuilder = new ParticipantStateUpdateOperationBuilderFilter();

	private ConsensusSettingsUpdateOperationBuilder consensusSettingsUpdateOperationBuilder = new ConsensusSettingsUpdateOperationBuilderFilter();

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
		return new UserUpdateOperationBuilderFilter(address);
	}

	@Override
	public DataAccountRegisterOperationBuilder dataAccounts() {
		return dataAccRegOpBuilder;
	}

	@Override
	public DataAccountKVSetOperationBuilder dataAccount(String accountAddress) {
		return new DataAccountKVSetOperationBuilderFilter(Bytes.fromBase58(accountAddress));
	}

	@Override
	public DataAccountKVSetOperationBuilder dataAccount(Bytes accountAddress) {
		return new DataAccountKVSetOperationBuilderFilter(accountAddress);
	}

	@Override
	public ContractCodeDeployOperationBuilder contracts() {
		return contractCodeDeployOpBuilder;
	}

	public ContractEventSendOperationBuilder contractEvents() {
		return contractEventSendOpBuilder;
	}

	@Override
	public ParticipantRegisterOperationBuilder participants() {return participantRegOpBuilder;}

	@Override
	public ParticipantStateUpdateOperationBuilder states() {return participantStateModifyOpBuilder;}

	@Override
	public ConsensusSettingsUpdateOperationBuilder settings() {return consensusSettingsUpdateOperationBuilder;}

	@Override
	public EventAccountRegisterOperationBuilder eventAccounts() {
		return eventAccRegOpBuilder;
	}

	@Override
	public EventPublishOperationBuilder eventAccount(String accountAddress) {
		return new EventPublishOperationBuilderFilter(Bytes.fromBase58(accountAddress));
	}

	@Override
	public EventPublishOperationBuilder eventAccount(Bytes accountAddress) {
		return new EventPublishOperationBuilderFilter(accountAddress);
	}

	@Override
	public <T> T contract(String address, Class<T> contractIntf) {
		return contractInvoProxyBuilder.create(address, -1L, contractIntf, contractEventSendOpBuilder);
	}

	@Override
	public <T> T contract(Bytes address, Class<T> contractIntf) {
		return contractInvoProxyBuilder.create(address, -1L, contractIntf, contractEventSendOpBuilder);
	}

	@Override
	public ContractEventSendOperationBuilder contract() {
		return contractEventSendOpBuilder;
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
	public Collection<OperationResultHandle> getReturnValuetHandlers() {
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

	private class UserUpdateOperationBuilderFilter implements UserUpdateOperationBuilder {

		private Bytes address;

		public UserUpdateOperationBuilderFilter(Bytes address) {
			this.address = address;
		}

		@Override
		public UserStateUpdateOperation revoke() {
			UserStateUpdateOperation op = new UserStateUpdateOpTemplate(address, UserState.REVOKE);
			operationList.add(op);
			return op;
		}

		@Override
		public UserStateUpdateOperation freeze() {
			UserStateUpdateOperation op = new UserStateUpdateOpTemplate(address, UserState.FREEZE);
			operationList.add(op);
			return op;
		}

		@Override
		public UserStateUpdateOperation restore() {
			UserStateUpdateOperation op = new UserStateUpdateOpTemplate(address, UserState.NORMAL);
			operationList.add(op);
			return op;
		}

		@Override
		public UserStateUpdateOperation state(UserState state) {
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

	private class DataAccountKVSetOperationBuilderFilter implements DataAccountKVSetOperationBuilder {

		private DataAccountKVSetOperationBuilder innerBuilder;

		private DataAccountKVSetOperation op;

		public DataAccountKVSetOperationBuilderFilter(Bytes accountAddress) {
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

	}

	private class ContractCodeDeployOperationBuilderFilter implements ContractCodeDeployOperationBuilder {
		@Override
		public ContractCodeDeployOperation deploy(BlockchainIdentity id, byte[] chainCode) {
			ContractCodeDeployOperation op = CONTRACT_CODE_DEPLOY_OP_BUILDER.deploy(id, chainCode);
			operationList.add(op);
			return op;
		}

		@Override
		public ContractCodeDeployOperation deploy(BlockchainIdentity id, byte[] chainCode, long version) {
			ContractCodeDeployOperation op = CONTRACT_CODE_DEPLOY_OP_BUILDER.deploy(id, chainCode, version);
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
			return register(participantName, new BlockchainIdentityData(X509Utils.resolvePubKey(certificate)));
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
			ConsensusSettingsUpdateOperation op = CONSENSUS_SETTINGS_UPDATE_OPERATION_BUILDER.update(properties);
			operationList.add(op);
			return op;
		}
	}

	private class ContractEventSendOperationBuilderFilter implements ContractEventSendOperationBuilder {

		@Override
		public ContractEventSendOperation send(String address, String event, BytesValueList args) {
			return send(Bytes.fromBase58(address), event, args);
		}

		@Override
		public synchronized ContractEventSendOperation send(Bytes address, String event, BytesValueList args) {
			ContractEventSendOpTemplate op = new ContractEventSendOpTemplate(address, event, args);
			operationList.add(op);
			return op;
		}

	}

	private class EventAccountRegisterOperationBuilderFilter implements EventAccountRegisterOperationBuilder{

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

	private class EventPublishOperationBuilderFilter implements EventPublishOperationBuilder{

		private EventPublishOperationBuilder innerBuilder;

		private EventPublishOperation op;

		public EventPublishOperationBuilderFilter(Bytes accountAddress) {
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
