package com.jd.blockchain.transaction;

public class ContractReturnValue {

	private ContractReturnValue() {
	}

	/**
	 * 解析合约方法调用的返回值；
	 * <p>
	 * 用法示例：<br>
	 * <code>
	 * import static com.jd.blockchain.transaction.ContractReturnValue.*; <p><p><p>
	 * 
	 * 
	 * ValueHolder<String retnHolder = decode(contract.issue(assetKey, amount)); <br>
	 * 
	 * PreparedTransaction prepTx = tx.prepare();<br>
	 * prepTx.sign(userKey);<br>
	 * prepTx.commit()<br><br>
	 * 
	 * String retnValue = retnHolder.get(); //这是同步方法，会阻塞当前线程等待交易提交后返回结果；<br>
	 * </code>
	 * 
	 * @param <T>
	 * @param call
	 * @return 返回值代理对象；如果上下文的调用没有返回值，则返回 null；
	 */
	public static <T> GenericValueHolder<T> decode(T call) {
		ContractInvocation invocation = ContractInvocationStub.take();
		if (invocation == null) {
			return null;
		}
		return new GenericValueHolder<T>(invocation);
	}

	/**
	 * 解析合约方法调用的返回值；
	 * <p>
	 * 用法示例：<br>
	 * <code>
	 * import static com.jd.blockchain.transaction.ContractReturnValue.*; <p><p><p>
	 * 
	 * 
	 * LongValueHolder retnHolder = decode(contract.issue(assetKey, amount)); <br>
	 * 
	 * PreparedTransaction prepTx = tx.prepare();<br>
	 * prepTx.sign(userKey);<br>
	 * prepTx.commit()<br><br>
	 * 
	 * long retnValue = retnHolder.get(); //这是同步方法，会阻塞当前线程等待交易提交后返回结果；<br>
	 * </code>
	 * 
	 * @param call
	 * @return 返回值代理对象；如果上下文的调用没有返回值，则返回 null；
	 */
	public static LongValueHolder decode(long call) {
		ContractInvocation invocation = ContractInvocationStub.take();
		if (invocation == null) {
			return null;
		}
		return new LongValueHolder(invocation);
	}

	/**
	 * 解析合约方法调用的返回值；
	 * <p>
	 * 用法示例：<br>
	 * <code>
	 * import static com.jd.blockchain.transaction.ContractReturnValue.*; <p><p><p>
	 * 
	 * 
	 * IntValueHolder retnHolder = decode(contract.issue(assetKey, amount)); <br>
	 * 
	 * PreparedTransaction prepTx = tx.prepare();<br>
	 * prepTx.sign(userKey);<br>
	 * prepTx.commit()<br><br>
	 * 
	 * int retnValue = retnHolder.get(); //这是同步方法，会阻塞当前线程等待交易提交后返回结果；<br>
	 * </code>
	 * 
	 * @param call
	 * @return 返回值代理对象；如果上下文的调用没有返回值，则返回 null；
	 */
	public static IntValueHolder decode(int call) {
		ContractInvocation invocation = ContractInvocationStub.take();
		if (invocation == null) {
			return null;
		}
		return new IntValueHolder(invocation);
	}

	/**
	 * 解析合约方法调用的返回值；
	 * <p>
	 * 用法示例：<br>
	 * <code>
	 * import static com.jd.blockchain.transaction.ContractReturnValue.*; <p><p><p>
	 * 
	 * 
	 * ShortValueHolder retnHolder = decode(contract.issue(assetKey, amount)); <br>
	 * 
	 * PreparedTransaction prepTx = tx.prepare();<br>
	 * prepTx.sign(userKey);<br>
	 * prepTx.commit()<br><br>
	 * 
	 * short retnValue = retnHolder.get(); //这是同步方法，会阻塞当前线程等待交易提交后返回结果；<br>
	 * </code>
	 * 
	 * @param call
	 * @return 返回值代理对象；如果上下文的调用没有返回值，则返回 null；
	 */
	public static ShortValueHolder decode(short call) {
		ContractInvocation invocation = ContractInvocationStub.take();
		if (invocation == null) {
			return null;
		}
		return new ShortValueHolder(invocation);
	}

	/**
	 * 解析合约方法调用的返回值；
	 * <p>
	 * 用法示例：<br>
	 * <code>
	 * import static com.jd.blockchain.transaction.ContractReturnValue.*; <p><p><p>
	 * 
	 * 
	 * ByteValueHolder retnHolder = decode(contract.issue(assetKey, amount)); <br>
	 * 
	 * PreparedTransaction prepTx = tx.prepare();<br>
	 * prepTx.sign(userKey);<br>
	 * prepTx.commit()<br><br>
	 * 
	 * byte retnValue = retnHolder.get(); //这是同步方法，会阻塞当前线程等待交易提交后返回结果；<br>
	 * </code>
	 * 
	 * @param call
	 * @return 返回值代理对象；如果上下文的调用没有返回值，则返回 null；
	 */
	public static ByteValueHolder decode(byte call) {
		ContractInvocation invocation = ContractInvocationStub.take();
		if (invocation == null) {
			return null;
		}
		return new ByteValueHolder(invocation);
	}

	/**
	 * 解析合约方法调用的返回值；
	 * <p>
	 * 用法示例：<br>
	 * <code>
	 * import static com.jd.blockchain.transaction.ContractReturnValue.*; <p><p><p>
	 * 
	 * 
	 * BooleanValueHolder retnHolder = decode(contract.issue(assetKey, amount)); <br>
	 * 
	 * PreparedTransaction prepTx = tx.prepare();<br>
	 * prepTx.sign(userKey);<br>
	 * prepTx.commit()<br><br>
	 * 
	 * boolean retnValue = retnHolder.get(); //这是同步方法，会阻塞当前线程等待交易提交后返回结果；<br>
	 * </code>
	 * 
	 * @param call
	 * @return 返回值代理对象；如果上下文的调用没有返回值，则返回 null；
	 */
	public static BooleanValueHolder decode(boolean call) {
		ContractInvocation invocation = ContractInvocationStub.take();
		if (invocation == null) {
			return null;
		}
		return new BooleanValueHolder(invocation);
	}

	// ----------------------- 内部类型 -----------------------

}
