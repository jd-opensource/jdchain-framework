package com.jd.blockchain.sdk;

import com.jd.blockchain.sdk.converters.BinarySerializeRequestConverter;
import com.jd.blockchain.setting.GatewayAuthResponse;
import com.jd.httpservice.HttpAction;
import com.jd.httpservice.HttpMethod;
import com.jd.httpservice.HttpService;
import com.jd.httpservice.RequestBody;
import com.jd.httpservice.utils.agent.WebResponseConverterFactory;


@HttpService(path="/management", defaultRequestBodyConverter = BinarySerializeRequestConverter.class, responseConverterFactory=WebResponseConverterFactory.class)
public interface ManagementHttpService {
	
	public static final String URL_GET_ACCESS_SPEC = "/access-spec";
	
	public static final String URL_AUTH_GATEWAY = "/gateway/auth";
	
	@HttpAction(method=HttpMethod.GET, path=URL_GET_ACCESS_SPEC)
	public AccessSpecification getAccessSpecification();
	
	@HttpAction(method=HttpMethod.POST, path=URL_AUTH_GATEWAY, contentType = BinarySerializeRequestConverter.CONTENT_TYPE_VALUE)
	public GatewayAuthResponse authenticateGateway(@RequestBody GatewayAuthRequest clientIdentifications) ;
	
}
