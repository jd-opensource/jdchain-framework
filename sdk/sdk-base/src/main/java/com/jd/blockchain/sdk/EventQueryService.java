package com.jd.blockchain.sdk;

import com.jd.blockchain.ledger.EventResponse;
import com.jd.blockchain.ledger.SystemEventRequest;
import com.jd.blockchain.ledger.UserEventRequest;
import com.jd.blockchain.sdk.converters.BinarySerializeRequestConverter;
import com.jd.blockchain.sdk.converters.BinarySerializeResponseConverter;
import com.jd.blockchain.utils.http.HttpAction;
import com.jd.blockchain.utils.http.HttpMethod;
import com.jd.blockchain.utils.http.HttpService;
import com.jd.blockchain.utils.http.RequestBody;

@HttpService(defaultRequestBodyConverter = BinarySerializeRequestConverter.class, defaultResponseConverter = BinarySerializeResponseConverter.class)
public interface EventQueryService {

	@HttpAction(method = HttpMethod.POST, path = "event/system", contentType = BinarySerializeRequestConverter.CONTENT_TYPE_VALUE)
	EventResponse loadSystemEvent(@RequestBody SystemEventRequest eventRequest);

	@HttpAction(method = HttpMethod.POST, path = "event/user", contentType = BinarySerializeRequestConverter.CONTENT_TYPE_VALUE)
	EventResponse loadUserEvent(@RequestBody UserEventRequest eventRequest);
}
