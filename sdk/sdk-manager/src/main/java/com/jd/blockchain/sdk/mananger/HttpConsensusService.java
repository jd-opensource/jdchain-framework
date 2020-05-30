package com.jd.blockchain.sdk.mananger;

import com.jd.blockchain.ledger.TransactionRequest;
import com.jd.blockchain.ledger.TransactionResponse;
import com.jd.blockchain.sdk.converters.BinarySerializeRequestConverter;
import com.jd.blockchain.sdk.converters.BinarySerializeResponseConverter;
import com.jd.blockchain.transaction.TransactionService;
import com.jd.blockchain.utils.http.HttpAction;
import com.jd.blockchain.utils.http.HttpMethod;
import com.jd.blockchain.utils.http.HttpService;
import com.jd.blockchain.utils.http.RequestBody;
import com.jd.blockchain.utils.web.client.WebResponseConverterFactory;

@HttpService(path="/management", defaultRequestBodyConverter = BinarySerializeRequestConverter.class, responseConverterFactory=WebResponseConverterFactory.class)
public interface HttpConsensusService extends TransactionService {

	@HttpAction(method = HttpMethod.POST, path = "/delegate/tx", contentType = BinarySerializeRequestConverter.CONTENT_TYPE_VALUE)
	@Override
	TransactionResponse process(@RequestBody TransactionRequest txRequest);
}
