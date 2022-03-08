package com.jd.blockchain.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;

import java.util.concurrent.atomic.AtomicLong;

public class LedgerMetrics {

    public static final String LEDGER_HASH = "ledger";
    public static final String BLOCK_HEIGHT_METER = "block_height";

    private AtomicLong blockHeight;

    public LedgerMetrics(String ledger, MeterRegistry registry) {
        blockHeight = registry.gauge(BLOCK_HEIGHT_METER, Tags.of(LEDGER_HASH, ledger), new AtomicLong(0), h -> h.get());
    }

    public void block(long height) {
        blockHeight.set(height);
    }
}
