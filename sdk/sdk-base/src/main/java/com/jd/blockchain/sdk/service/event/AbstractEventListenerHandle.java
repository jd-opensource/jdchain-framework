package com.jd.blockchain.sdk.service.event;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.sdk.BlockchainEventListener;
import com.jd.blockchain.sdk.EventListenerHandle;
import com.jd.blockchain.sdk.EventPoint;
import com.jd.blockchain.transaction.BlockchainQueryService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

/**
 * 抽象事件监听处理器
 *
 * @author shaozhuguang
 *
 * @param <E>
 */
public abstract class AbstractEventListenerHandle<E extends EventPoint> implements EventListenerHandle<E> {

    /**
     * 定时线程池线程数量
     */
    protected static final int THREAD_CORE = 1;

    private volatile boolean isRegistered = false;

    /**
     * 查询接口，可通过http访问网关节点
     */
    private BlockchainQueryService queryService;

    /**
     * eventPoint集合
     */
    private Set<E> eventPointSet = new HashSet<>();

    /**
     * 事件监听器
     */
    private BlockchainEventListener<E> listener;

    /**
     * 事件监听对应的账本
     */
    private HashDigest ledgerHash;

    /**
     * 定时任务线程池
     */
    private ScheduledThreadPoolExecutor executor;

    public AbstractEventListenerHandle(BlockchainQueryService queryService) {
        this.queryService = queryService;
    }

    /**
     * 注册事件集合
     *
     * @param ledgerHash
     *             账本Hash
     * @param eventPoints
     *             事件集合
     * @param listener
     *             事件监听器
     */
    public void register(HashDigest ledgerHash, E[] eventPoints, BlockchainEventListener<E> listener) {
        checkArgs(ledgerHash, listener, eventPoints);
        this.ledgerHash = ledgerHash;
        this.listener = listener;
        eventPointSet.addAll(Arrays.asList(eventPoints));
        registered();
        startListener();
    }

    /**
     * 注册事件
     *
     * @param ledgerHash
     *             账本Hash
     * @param eventPoint
     *             事件
     * @param listener
     *             事件监听器
     */
    public void register(HashDigest ledgerHash, E eventPoint, BlockchainEventListener<E> listener) {
        checkArgs(ledgerHash, listener, eventPoint);
        this.ledgerHash = ledgerHash;
        this.listener = listener;
        eventPointSet.add(eventPoint);
        registered();
        startListener();
    }

    /**
     * 注册参数校验
     *
     * @param ledgerHash
     * @param listener
     * @param eventPoint
     */
    private void checkArgs(HashDigest ledgerHash, BlockchainEventListener listener, EventPoint... eventPoint) {
        if (isRegistered) {
            throw new IllegalStateException("Can not register eventPoints repeatedly !!!");
        }
        if (ledgerHash == null) {
            throw new IllegalArgumentException("LedgerHash can not be null !!!");
        }
        if (eventPoint == null || eventPoint.length == 0 || listener == null) {
            throw new IllegalArgumentException("EventPoints and listener can not be null both !!!");
        }
    }

    /**
     * 启动监听器
     */
    private void startListener() {
        // 启动定时任务线程，定时发送http请求至
        executor = scheduledThreadPoolExecutor();
        executor.scheduleAtFixedRate(eventRunnable(),
                delayMilliSeconds(), periodMilliSeconds(), TimeUnit.MILLISECONDS);
    }

    /**
     * 创建定时线程池
     *         可被覆写
     * @return
     */
    protected ScheduledThreadPoolExecutor scheduledThreadPoolExecutor() {
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("event-pull-%d").build();
        return new ScheduledThreadPoolExecutor(THREAD_CORE,
                threadFactory,
                new ThreadPoolExecutor.AbortPolicy());
    }

    private void registered() {
        isRegistered = true;
    }

    /**
     * 定时线程池延时启动时间
     *         可被覆写
     * @return
     */
    protected long delayMilliSeconds() {
        return 1000L;
    }

    /**
     * 定时线程池定时调用时间
     *         可被覆写
     * @return
     */
    protected long periodMilliSeconds() {
        return 1000L;
    }

    @Override
    public Set<E> getEventPoints() {
        return eventPointSet;
    }

    /**
     * 取消监听
     *         线程池会停止相关调用，但并不会马上停止
     */
    @Override
    public void cancel() {
        if (isRegistered && executor != null) {
            List<Runnable> runnables = executor.shutdownNow();
            // todo 打印未执行的线程
        }
    }

    public BlockchainQueryService getQueryService() {
        return queryService;
    }

    public BlockchainEventListener<E> getListener() {
        return listener;
    }

    public HashDigest getLedgerHash() {
        return ledgerHash;
    }

    /**
     * 定时线程池执行的对应线程
     *
     * @return
     */
    abstract AbstractEventRunnable eventRunnable();
}
