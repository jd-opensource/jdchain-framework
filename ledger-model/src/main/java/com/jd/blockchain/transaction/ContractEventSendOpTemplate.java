package com.jd.blockchain.transaction;

import com.jd.binaryproto.DataContractRegistry;
import com.jd.blockchain.ledger.BytesValueList;
import com.jd.blockchain.ledger.ContractEventSendOperation;

import utils.Bytes;

public class ContractEventSendOpTemplate implements ContractEventSendOperation {

	static {
		DataContractRegistry.register(ContractEventSendOperation.class);
	}

	private Bytes contractAddress;
	private long version;
	private BytesValueList args;
	private String event;
	
	private ContractInvocation invocation;

	public ContractEventSendOpTemplate(Bytes contractAddress, String event, BytesValueList args) {
		this.contractAddress = contractAddress;
		this.event = event;
		this.args = args;
	}

	@Override
	public Bytes getContractAddress() {
		return contractAddress;
	}

	@Override
	public String getEvent() {
		return event;
	}

	@Override
	public BytesValueList getArgs() {
		return args;
	}

	public ContractInvocation getInvocation() {
		return invocation;
	}
	
	public void setInvocation(ContractInvocation invocation) {
		this.invocation = invocation;
	}

	@Override
	public long getVersion() {
		return version;
	}
}
