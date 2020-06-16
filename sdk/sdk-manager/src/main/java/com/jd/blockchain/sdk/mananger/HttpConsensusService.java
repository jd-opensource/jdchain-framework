package com.jd.blockchain.sdk.mananger;

import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.ledger.TransactionResponse;
import com.jd.blockchain.sdk.converters.HashDigestToStringConverter;
import com.jd.blockchain.transaction.ActiveParticipantService;
import com.jd.blockchain.utils.http.*;
import com.jd.blockchain.utils.web.client.WebResponseConverterFactory;

@HttpService(path="/management", responseConverterFactory=WebResponseConverterFactory.class)
public interface HttpConsensusService extends ActiveParticipantService {

	@HttpAction(method = HttpMethod.POST, path = "/delegate/activeparticipant")
	@Override
	TransactionResponse activateParticipant(@RequestParam(name="ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash);
}