package com.jd.blockchain.sdk.service;

import com.alibaba.fastjson.JSON;
import com.jd.blockchain.consensus.MessageService;
import com.jd.blockchain.transaction.MonitorService;
import com.jd.blockchain.utils.concurrent.AsyncFuture;
import com.jd.blockchain.utils.net.NetworkAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PeerMonitorHandler implements MonitorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PeerMonitorHandler.class);

    private static final long MAX_WAIT_MILL_SECONDS = 10000;

    private final Lock lock = new ReentrantLock();

    private NodeSigningAppender nodeSigningAppender;

    public PeerMonitorHandler(NodeSigningAppender nodeSigningAppender) {
        this.nodeSigningAppender = nodeSigningAppender;
    }

    @Override
    public List<NetworkAddress> loadMonitors() {
        lock.lock();
        try {
            MessageService messageService = nodeSigningAppender.getMessageService();
            if (messageService != null) {
                try {
                    AsyncFuture<byte[]> future = messageService.sendUnordered(LOAD_MONITOR);
                    byte[] bytes = future.get(MAX_WAIT_MILL_SECONDS, TimeUnit.MILLISECONDS);
                    return convert(bytes);
                } catch (Exception e) {
                    LOGGER.error("Load monitors error !!!", e);
                }
            }
            return null;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 将应答结果转换为网络列表
     *
     * @param response
     * @return
     */
    private List<NetworkAddress> convert(byte[] response) {
        // 使用fastJson进行序列化/反序列化操作
        String responseText = new String(response, StandardCharsets.UTF_8);
        return JSON.parseObject(responseText, List.class);
    }
}
