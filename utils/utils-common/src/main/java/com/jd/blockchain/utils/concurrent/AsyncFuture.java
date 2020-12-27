package com.jd.blockchain.utils.concurrent;

import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * 提供对异步操作的结果描述；
 * 
 * @param <V> class
 */
public interface AsyncFuture<V> extends Future<V> {

	/**
	 * 返回异步操作的结果；<br>
	 * 
	 * 如果异步操作发生了异常，则此方法也将以抛出异常的方式返回；
	 * <p>
	 * 
	 * 注：此方法将堵塞当前线程直至异步操作完成并返回结果；
	 * 
	 * @return v
	 */
	V get();

	/**
	 * 返回异步操作的结果；<br>
	 * 
	 * 如果异步操作发生了异常，则此方法也将以抛出异常的方式返回；
	 * <p>
	 * 
	 * 注：此方法将堵塞当前线程等待操作完成并返回结果，如果等待超过指定的超时时间则抛出超时异常 {@link RuntimeTimeoutException}；
	 * 
	 * @param timeout
	 * @param unit
	 * @return
	 */
	V get(long timeout, TimeUnit unit);

	/**
	 * 操作是否已完成；
	 * 
	 * 当操作成功返回或者异常返回时，都表示为已完成；
	 * 
	 * @return boolean
	 */
	boolean isDone();

	/**
	 * 操作是否已成功；
	 * 
	 * @return boolean
	 */
	boolean isExceptionally();
	
	/**
	 * 取消异步执行的任务；
	 * 
	 * @return
	 */
	default boolean cancel() {
		return cancel(true);
	}

	/**
	 * 取消异步执行的任务；；
	 * 
	 * @param mayInterruptIfRunning 是否中断正在执行的任务；
	 */
	boolean cancel(boolean mayInterruptIfRunning);

	/**
	 * 异步任务是否已经取消；
	 */
	boolean isCancelled();
	

	public AsyncFuture<V> thenAccept(Consumer<? super V> action);

	public AsyncFuture<V> thenAcceptAsync(Consumer<? super V> action);

	public AsyncFuture<V> thenAcceptAsync(Consumer<? super V> action, Executor executor);

	public AsyncFuture<V> thenRun(Runnable action);

	public AsyncFuture<V> thenRunAsync(Runnable action);

	public AsyncFuture<V> thenRunAsync(Runnable action, Executor executor);

	public AsyncFuture<V> whenComplete(AsyncHandle<? super V> action);

	public AsyncFuture<V> whenCompleteAsync(AsyncHandle<? super V> action);

	public AsyncFuture<V> whenCompleteAsync(AsyncHandle<? super V> action, Executor executor);

}
