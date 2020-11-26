package com.jd.blockchain.binaryproto;

public interface DataContractAutoRegistrar {

	/**
	 * 优先级顺序；
	 * <p>
	 * 
	 * 系统内不同的优先级的自动注册器按照升序排列进行初始化；
	 * <p>
	 * 
	 * 默认为 1000；
	 * 
	 * @return
	 */
	default int order() {
		return 1000;
	}

	/**
	 * 初始化上下文；
	 * 
	 * @param registry
	 */
	void initContext(DataContractRegistry registry);

}
