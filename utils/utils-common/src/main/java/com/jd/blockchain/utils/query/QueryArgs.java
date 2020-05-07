package com.jd.blockchain.utils.query;

public class QueryArgs {

    private int from;

    private int count;

    public QueryArgs() {
    }

    public QueryArgs(int from, int count) {
        this.from = from;
        this.count = count;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
