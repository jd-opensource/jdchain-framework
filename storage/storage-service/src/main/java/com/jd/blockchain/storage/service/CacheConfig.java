package com.jd.blockchain.storage.service;

import utils.StringUtils;

import java.net.URI;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 缓存配置
 */
public class CacheConfig {

    static final String BLOOM_PARAM_KEY = "bloom";
    static final String LRU_PARAM_KEY = "lru";

    // 布隆过滤器配置
    private BloomFilterConfig bloomConfig;
    // LRU缓存配置
    private LRUCacheConfig lruCacheConfig;

    public CacheConfig(URI uri) {
        Map<String, String> query_pairs = new HashMap<>();
        String query = uri.getQuery();
        if (StringUtils.isEmpty(query)) {
            bloomConfig = new BloomFilterConfig(false);
            lruCacheConfig = new LRUCacheConfig(false);
            return;
        }
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            try {
                int idx = pair.indexOf("=");
                query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
            } catch (Exception e) {
            }
        }
        if (!query_pairs.containsKey(BLOOM_PARAM_KEY)) {
            bloomConfig = new BloomFilterConfig(false);
        } else {
            try {
                String[] split = query_pairs.get(BLOOM_PARAM_KEY).split(",");
                bloomConfig = new BloomFilterConfig(true, Integer.valueOf(split[0]), Double.valueOf(split[1]));
            } catch (Exception e) {
                bloomConfig = new BloomFilterConfig(false);
            }
        }
        if (!query_pairs.containsKey(LRU_PARAM_KEY)) {
            lruCacheConfig = new LRUCacheConfig(false);
        } else {
            try {
                String[] split = query_pairs.get(LRU_PARAM_KEY).split(",");
                lruCacheConfig = new LRUCacheConfig(true, Integer.valueOf(split[0]), Integer.valueOf(split[1]));
            } catch (Exception e) {
                lruCacheConfig = new LRUCacheConfig(false);
            }
        }
    }

    @Override
    public String toString() {
        return "CacheConfig{" +
                "bloomConfig=" + bloomConfig +
                ", lruCacheConfig=" + lruCacheConfig +
                '}';
    }

    public BloomFilterConfig getBloomConfig() {
        return bloomConfig;
    }

    public void setBloomConfig(BloomFilterConfig bloomConfig) {
        this.bloomConfig = bloomConfig;
    }

    public LRUCacheConfig getLruCacheConfig() {
        return lruCacheConfig;
    }

    public void setLruCacheConfig(LRUCacheConfig lruCacheConfig) {
        this.lruCacheConfig = lruCacheConfig;
    }

    public class BloomFilterConfig {
        private boolean enable;
        private int expectedInsertions;
        private double fpp;

        public BloomFilterConfig(boolean enable) {
            this.enable = enable;
        }

        public BloomFilterConfig(boolean enable, int expectedInsertions, double fpp) {
            this.enable = enable;
            this.expectedInsertions = expectedInsertions;
            this.fpp = fpp;
        }

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public int getExpectedInsertions() {
            return expectedInsertions;
        }

        public void setExpectedInsertions(int expectedInsertions) {
            this.expectedInsertions = expectedInsertions;
        }

        public double getFpp() {
            return fpp;
        }

        public void setFpp(double fpp) {
            this.fpp = fpp;
        }

        @Override
        public String toString() {
            return "BloomFilterConfig{" +
                    "enable=" + enable +
                    ", expectedInsertions=" + expectedInsertions +
                    ", fpp=" + fpp +
                    '}';
        }
    }

    public class LRUCacheConfig {
        private boolean enable;
        private int initialCapacity;
        private int maximumSize;

        public LRUCacheConfig(boolean enable) {
            this.enable = enable;
        }

        public LRUCacheConfig(boolean enable, int initialCapacity, int maximumSize) {
            this.enable = enable;
            this.initialCapacity = initialCapacity;
            this.maximumSize = maximumSize;
        }

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public int getInitialCapacity() {
            return initialCapacity;
        }

        public void setInitialCapacity(int initialCapacity) {
            this.initialCapacity = initialCapacity;
        }

        public int getMaximumSize() {
            return maximumSize;
        }

        public void setMaximumSize(int maximumSize) {
            this.maximumSize = maximumSize;
        }

        @Override
        public String toString() {
            return "LRUCacheConfig{" +
                    "enable=" + enable +
                    ", initialCapacity=" + initialCapacity +
                    ", maximumSize=" + maximumSize +
                    '}';
        }
    }
}
