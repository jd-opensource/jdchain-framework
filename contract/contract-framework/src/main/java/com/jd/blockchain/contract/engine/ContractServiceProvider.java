package com.jd.blockchain.contract.engine;

/**
 * 合约服务提供者 {@link ContractServiceProvider}
 * 
 * 用于定义了一种特定的合约引擎({@link ContractEngine})的实现。<p>
 * 
 * {@link ContractServiceProvider} 作为合约引擎({@link ContractEngine})实例的工厂，同时管理合约引擎的生命周期。
 * 
 * @author huanghaiquan
 *
 */
public interface ContractServiceProvider {

	/**
	 * 合约服务提供者的名称；
	 * 
	 * @return
	 */
	String getName();

	/**
	 * 返回合约代码执行引擎实例；
	 * 
	 * @return
	 */
	ContractEngine getEngine();

}
