package com.jd.blockchain.sdk.service.event;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.sdk.BlockchainEventListener;
import com.jd.blockchain.sdk.EventListenerHandle;
import com.jd.blockchain.sdk.EventPoint;
import com.jd.blockchain.sdk.EventQueryService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

public abstract class AbstractEventListenerHandle<E extends EventPoint> implements EventListenerHandle<E> {

    protected static final int THREAD_CORE = 1;

    private volatile boolean isRegistered = false;

    private EventQueryService queryService;

    private Set<E> eventPointSet = new HashSet<>();

    private BlockchainEventListener<E> listener;

    private HashDigest ledgerHash;

    private ScheduledThreadPoolExecutor executor;

    public AbstractEventListenerHandle(EventQueryService queryService) {
        this.queryService = queryService;
    }

    public void register(HashDigest ledgerHash, E[] eventPoints, BlockchainEventListener<E> listener) {
        checkArgs(ledgerHash, listener, eventPoints);
        this.ledgerHash = ledgerHash;
        this.listener = listener;
        eventPointSet.addAll(Arrays.asList(eventPoints));
        registered();
        startListener();
    }

    public void register(HashDigest ledgerHash, E eventPoint, BlockchainEventListener<E> listener) {
        checkArgs(ledgerHash, listener, eventPoint);
        this.ledgerHash = ledgerHash;
        this.listener = listener;
        eventPointSet.add(eventPoint);
        registered();
        startListener();
    }

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

    private void startListener() {
        // 启动定时任务线程，定时发送http请求至
        executor = scheduledThreadPoolExecutor();
        executor.scheduleAtFixedRate(eventRunnable(),
                delayMilliSeconds(), periodMilliSeconds(), TimeUnit.MILLISECONDS);
    }

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

    protected long delayMilliSeconds() {
        return 1000L;
    }

    protected long periodMilliSeconds() {
        return 1000L;
    }

    @Override
    public Set<E> getEventPoints() {
        return eventPointSet;
    }

    @Override
    public void cancel() {
        if (isRegistered && executor != null) {
            List<Runnable> runnables = executor.shutdownNow();
            // todo 打印未执行的线程
        }
    }

    private ThreadPoolExecutor initLedgerLoadExecutor() {
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("ledger-loader-%d").build();

        return new ThreadPoolExecutor(1, 1,
                60, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1024),
                threadFactory,
                new ThreadPoolExecutor.AbortPolicy());
    }

    public EventQueryService getQueryService() {
        return queryService;
    }

    public BlockchainEventListener<E> getListener() {
        return listener;
    }

    public HashDigest getLedgerHash() {
        return ledgerHash;
    }

    abstract AbstractEventRunnable eventRunnable();
}
