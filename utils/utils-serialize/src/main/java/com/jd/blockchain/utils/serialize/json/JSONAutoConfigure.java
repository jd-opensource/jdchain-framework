package com.jd.blockchain.utils.serialize.json;

/**
 * JSON 自动配置；
 * <p>
 * 
 * {@link JSONAutoConfigure} 是一个服务提供者接口（SPI），提供了自动化JSON配置的能力；
 * <p>
 * 
 * 实现者需要以 Service Provider 方式注册，通过
 * {@link JSONSerializeUtils#enableAutoConfigure()} 激活所有的提供者； <br>
 * 
 * 实现者需要保证幂等性，即保证即使被多次激活配置也保证一致的结果；
 * 
 * @author huanghaiquan
 *
 */
public interface JSONAutoConfigure {

	void configure(JSONConfigurator configure);

}
