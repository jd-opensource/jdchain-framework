package com.jd.blockchain.consensus;

import com.jd.blockchain.crypto.PubKey;
import utils.Bytes;

/**
 * @Author: zhangshuang
 * @Date: 2021/12/1 2:21 PM
 * Version 1.0
 */
public class ReplicaImpl implements Replica {

    private int id;
    private Bytes address;
    private String name;
    private PubKey pubKey;

    public ReplicaImpl(int id, Bytes address, String name, PubKey pubKey) {
        this.id = id;
        this.address = address;
        this.name = name;
        this.pubKey = pubKey;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Bytes getAddress() {
        return address;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public PubKey getPubKey() {
        return pubKey;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPubKey(PubKey pubKey) {
        this.pubKey = pubKey;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(Bytes address) {
        this.address = address;
    }
}
