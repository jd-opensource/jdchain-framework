package com.jd.blockchain.sdk.service;

import com.jd.binaryproto.BinaryProtocol;
import com.jd.blockchain.consensus.MessageService;
import com.jd.blockchain.consensus.NodeNetworkAddresses;
import com.jd.blockchain.consensus.service.MonitorService;

import utils.concurrent.AsyncFuture;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PeerMonitorHandler implements MonitorService {

    private static final long MAX_WAIT_MILL_SECONDS = 10000;

    private final Lock lock = new ReentrantLock();

    private NodeSigningAppender nodeSigningAppender;

    public PeerMonitorHandler(NodeSigningAppender nodeSigningAppender) {
        this.nodeSigningAppender = nodeSigningAppender;
    }

    @Override
    public NodeNetworkAddresses loadMonitors() {
        lock.lock();
        try {
            MessageService messageService = nodeSigningAppender.getMessageService();
            if (messageService != null) {
                AsyncFuture<byte[]> future = messageService.sendUnordered(LOAD_MONITOR);
                byte[] bytes = future.get(MAX_WAIT_MILL_SECONDS, TimeUnit.MILLISECONDS);
                return convert(bytes);
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
    private NodeNetworkAddresses convert(byte[] response) {
        return BinaryProtocol.decode(response);
    }
}
