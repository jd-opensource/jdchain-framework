package com.jd.blockchain.sdk.client;

import com.jd.blockchain.ledger.TransactionRequest;
import com.jd.blockchain.ledger.TransactionResponse;
import com.jd.blockchain.sdk.converters.BinarySerializeRequestConverter;
import com.jd.blockchain.sdk.converters.BinarySerializeResponseConverter;
import com.jd.blockchain.transaction.TransactionService;
import com.jd.httpservice.HttpAction;
import com.jd.httpservice.HttpMethod;
import com.jd.httpservice.HttpService;
import com.jd.httpservice.RequestBody;

@HttpService(defaultRequestBodyConverter = BinarySerializeRequestConverter.class, defaultResponseConverter = BinarySerializeResponseConverter.class)
public interface HttpConsensusService extends TransactionService {

	@HttpAction(method = HttpMethod.POST, path = "rpc/tx", contentType = BinarySerializeRequestConverter.CONTENT_TYPE_VALUE)
	@Override
	TransactionResponse process(@RequestBody TransactionRequest txRequest);
}
