package com.jd.blockchain.transaction;

import com.jd.blockchain.crypto.AsymmetricKeypair;
import com.jd.blockchain.ledger.DigitalSignature;
import com.jd.blockchain.ledger.OperationResult;
import com.jd.blockchain.ledger.TransactionRequestBuilder;
import com.jd.blockchain.ledger.TransactionResponse;
import org.springframework.cglib.proxy.UndeclaredThrowableException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

class StatefulPreparedTx extends PreparedTx {

    private OperationResultHandle[] opReturnValueHandlers;

    private TxStateManager stateManager;

    /**
     * 创建一个“就绪交易”对象；
     *
     * @param txReqBuilder             交易请求构建器；
     * @param txProcessor              交易处理服务；
     * @param opReturnValueHandlerList 操作返回值处理器列表；
     */
    StatefulPreparedTx(TxStateManager stateManager, TransactionRequestBuilder txReqBuilder,
                       TransactionService txProcessor, Collection<OperationResultHandle> opReturnValueHandlerList) {
        super(txReqBuilder, txProcessor);
        this.stateManager = stateManager;

        this.opReturnValueHandlers = opReturnValueHandlerList
                .toArray(new OperationResultHandle[opReturnValueHandlerList.size()]);
        // 按照操作索引升序排列;
        Arrays.sort(opReturnValueHandlers, new Comparator<OperationResultHandle>() {
            @Override
            public int compare(OperationResultHandle o1, OperationResultHandle o2) {
                return o1.getOperationIndex() - o2.getOperationIndex();
            }
        });
    }

    @Override
    public DigitalSignature sign(AsymmetricKeypair keyPair) {
        DigitalSignature signature = SignatureUtils.sign(getTransactionHash(), keyPair);
        addSignature(signature);
        return signature;
    }

    @Override
    public TransactionResponse commit() {
        stateManager.commit();
        TransactionResponse txResponse = null;
        try {
            // 发起交易请求；
            txResponse = super.commit();

            stateManager.complete();
        } catch (Exception ex) {
            stateManager.close();
            handleError(ex);
            throw new UndeclaredThrowableException(ex);
        }

        if (txResponse != null) {
            handleResults(txResponse);
        }

        return txResponse;
    }

    @Override
    public void close() throws IOException {
        if (!stateManager.close()) {
            TransactionCancelledExeption error = new TransactionCancelledExeption(
                    "Prepared transaction has been cancelled!");
            handleError(error);
        }
    }

    private void handleError(Throwable error) {
        for (OperationResultHandle handle : opReturnValueHandlers) {
            handle.complete(error);
        }
    }

    private void handleResults(TransactionResponse txResponse) {
        // 解析返回值；正常的情况下，返回结果列表与结果处理器列表中元素对应的操作索引是一致的；
        OperationResult[] opResults = txResponse.getOperationResults();
        if (opResults != null && opResults.length > 0) {
            for (int i = 0; i < opResults.length; i++) {
                if(null == opResults[i]) {
                    continue;
                }
                int index = opResults[i].getIndex();
                OperationResultHandle handle = null;
                for (int j = 0; j < opReturnValueHandlers.length; j++) {
                    if (index == opReturnValueHandlers[j].getOperationIndex()) {
                        handle = opReturnValueHandlers[j];
                        break;
                    }
                }
                if (null == handle) {
                    throw new IllegalStateException(
                            "The operation indexes of the items in the result list and in the handler list don't match!");
                }
                handle.complete(opResults[i].getResult());
            }
        }
    }

}
