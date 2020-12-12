package com.jd.blockchain.sdk;

import com.jd.blockchain.sdk.converters.BinarySerializeRequestConverter;
import com.jd.blockchain.setting.GatewayIncomingSetting;
import com.jd.blockchain.utils.http.HttpAction;
import com.jd.blockchain.utils.http.HttpMethod;
import com.jd.blockchain.utils.http.HttpService;
import com.jd.blockchain.utils.http.RequestBody;
import com.jd.blockchain.utils.web.client.WebResponseConverterFactory;


@HttpService(path="/management", defaultRequestBodyConverter = BinarySerializeRequestConverter.class, responseConverterFactory=WebResponseConverterFactory.class)
public interface ManagementHttpService {
	
	public static final String URL_GET_ACCESS_SPEC = "/access-spec";
	
	public static final String URL_AUTH_GATEWAY = "/gateway/auth";
	
	@HttpAction(method=HttpMethod.GET, path=URL_GET_ACCESS_SPEC)
	public AccessSpecification getAccessSpecification();
	
	@HttpAction(method=HttpMethod.POST, path=URL_AUTH_GATEWAY, contentType = BinarySerializeRequestConverter.CONTENT_TYPE_VALUE)
	public GatewayIncomingSetting authenticateGateway(@RequestBody GatewayAuthRequest clientIdentifications) ;
	
}
