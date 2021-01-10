package com.jd.blockchain.transaction;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jd.blockchain.contract.ContractType;

import utils.Bytes;

/**
 * 合约调用代理的构建器；
 * 
 * @author huanghaiquan
 *
 */
public class ContractInvocationProxyBuilder {

	private Map<Class<?>, ContractType> contractTypes = new ConcurrentHashMap<>();

	public <T> T create(String address, long version, Class<T> contractIntf, ContractEventSendOperationBuilder contractEventBuilder) {
		return create(Bytes.fromBase58(address), version, contractIntf, contractEventBuilder);
	}

	@SuppressWarnings("unchecked")
	public <T> T create(Bytes address, long version, Class<T> contractIntf, ContractEventSendOperationBuilder contractEventBuilder) {
		ContractType contractType = resolveContractType(contractIntf);

		ContractInvocationHandler proxyHandler = new ContractInvocationHandler(address, version, contractType, contractEventBuilder);

		T proxy = (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
				new Class<?>[] { contractIntf }, proxyHandler);
		return proxy;
	}

	private ContractType resolveContractType(Class<?> contractIntf) {
		ContractType contractType = contractTypes.get(contractIntf);
		if (contractType != null) {
			return contractType;
		}
		ContractType ct = ContractType.resolve(contractIntf);
		contractTypes.put(contractIntf, ct);
		return ct;
	}
}
