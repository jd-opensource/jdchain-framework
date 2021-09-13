package com.jd.blockchain.transaction;

import com.jd.binaryproto.DataContractRegistry;
import com.jd.blockchain.ledger.BytesValue;
import com.jd.blockchain.ledger.BytesValueList;
import com.jd.blockchain.ledger.ContractCrossEventSendOperation;
import com.jd.blockchain.ledger.ContractEventSendOperation;
import utils.Bytes;

public class ContractCrossEventSendOpTemplate implements ContractCrossEventSendOperation {

    static {
        DataContractRegistry.register(ContractCrossEventSendOperation.class);
    }

    private ContractEventSendOperation eventSendOperation;
    private BytesValue result;

    public ContractCrossEventSendOpTemplate(ContractEventSendOperation eventSendOperation, BytesValue result) {
        this.eventSendOperation = eventSendOperation;
        this.result = result;
    }

    @Override
    public BytesValue getResult() {
        return result;
    }

    @Override
    public Bytes getContractAddress() {
        return eventSendOperation.getContractAddress();
    }

    @Override
    public String getEvent() {
        return eventSendOperation.getEvent();
    }

    @Override
    public BytesValueList getArgs() {
        return eventSendOperation.getArgs();
    }

    @Override
    public long getVersion() {
        return eventSendOperation.getVersion();
    }
}
