package com.jd.blockchain.sdk.service.event;

import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.ledger.Event;
import com.jd.blockchain.sdk.BlockchainEventListener;
import com.jd.blockchain.sdk.EventContext;
import com.jd.blockchain.sdk.EventListenerHandle;
import com.jd.blockchain.sdk.EventPoint;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 抽象事件处理线程
 *
 * @param <E>
 */
public abstract class AbstractEventRunnable<E extends EventPoint> implements Runnable {

    private static final int MAX_COUNT = 10;

    private final Map<String, Long> eventSequences = new ConcurrentHashMap<>();

    /**
     * 账本Hash
     */
    private HashDigest ledgerHash;

    /**
     * 事件集合
     */
    private Set<E> eventPointSet;

    /**
     * 事件监听器
     */
    private BlockchainEventListener<E> listener;

    /**
     * 事件监听处理器
     */
    private EventListenerHandle<E> handle;

    public AbstractEventRunnable(HashDigest ledgerHash, Set<E> eventPointSet, BlockchainEventListener<E> listener,
                                 EventListenerHandle<E> handle) {
        this.ledgerHash = ledgerHash;
        this.eventPointSet = eventPointSet;
        this.listener = listener;
        this.handle = handle;
        initEventSequences();
    }



    @Override
    public void run() {
        // 发送一个请求至网关
        for(E eventPoint : eventPointSet) {
            Event[] events = loadEvent(eventPoint, eventSequence(eventPoint), MAX_COUNT);
            onEvent(eventPoint, events);
        }
    }

    public HashDigest getLedgerHash() {
        return ledgerHash;
    }

    public EventListenerHandle<E> getHandle() {
        return handle;
    }

    /**
     * 处理接收到的事件
     *
     * @param eventPoint
     * @param events
     */
    private void onEvent(E eventPoint, Event[] events) {
        if (events != null && events.length > 0) {
            long fromSequence = -1L;
            for (Event event : events) {
                EventContext<E> context = eventContext(event);
                listener.onEvent(event, context);
                fromSequence = Math.max(fromSequence, event.getSequence());
            }
            updateEventSequence(eventPoint.getEventName(), fromSequence + 1);
        }
    }

    /**
     * 更新事件对应的from序号
     *
     * @param eventName
     *             事件名称
     * @param sequence
     *             事件对应的fromSequence
     */
    private synchronized void updateEventSequence(String eventName, long sequence) {
        eventSequences.put(eventName, sequence);
    }

    /**
     * 获取事件对应的sequence
     *
     * @param eventPoint
     *             事件
     *
     * @return
     */
    private synchronized long eventSequence(E eventPoint) {
        String key = eventPoint.getEventName();
        Long fromSequence = eventSequences.get(key);
        if (fromSequence == null) {
            return -1L;
        } else {
            return fromSequence;
        }
    }

    /**
     * 初始化事件对应的Sequence
     *
     */
    private void initEventSequences() {
        for (E eventPoint : eventPointSet) {
            eventSequences.put(eventPoint.getEventName(), eventPoint.getSequence());
        }
    }

    /**
     * 从远端加载对应的事件
     *
     * @param eventPoint
     * @param fromSequence
     * @param maxCount
     * @return
     */
    abstract Event[] loadEvent(E eventPoint, long fromSequence, int maxCount);

    /**
     * 将事件转换为事件上下文
     *
     * @param event
     * @return
     */
    abstract EventContext<E> eventContext(Event event);
}
