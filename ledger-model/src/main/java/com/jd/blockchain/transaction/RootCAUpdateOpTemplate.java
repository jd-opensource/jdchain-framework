package com.jd.blockchain.transaction;

import com.jd.binaryproto.DataContractRegistry;
import com.jd.blockchain.ledger.RootCAUpdateOperation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @description: 根证书更新
 * @author: imuge
 * @date: 2021/8/25
 **/
public class RootCAUpdateOpTemplate implements RootCAUpdateOperation {

    static {
        DataContractRegistry.register(RootCAUpdateOperation.class);
    }

    private Set<String> certsAdd = new HashSet<>();
    private Set<String> certsUpdate = new HashSet<>();
    private Set<String> certsRemove = new HashSet<>();

    public RootCAUpdateOpTemplate() {
    }

    public RootCAUpdateOpTemplate(RootCAUpdateOperation operation) {
        if (null != operation.getCertificatesAdd()) {
            certsAdd = new HashSet<>(Arrays.asList(operation.getCertificatesAdd()));
        }
        if (null != operation.getCertificatesUpdate()) {
            certsUpdate = new HashSet<>(Arrays.asList(operation.getCertificatesUpdate()));
        }
        if (null != operation.getCertificatesRemove()) {
            certsRemove = new HashSet<>(Arrays.asList(operation.getCertificatesRemove()));
        }
    }

    @Override
    public String[] getCertificatesAdd() {
        return certsAdd.toArray(new String[0]);
    }

    @Override
    public String[] getCertificatesUpdate() {
        return certsUpdate.toArray(new String[0]);
    }

    @Override
    public String[] getCertificatesRemove() {
        return certsRemove.toArray(new String[0]);
    }

    public void addCertificate(String certificate) {
        certsAdd.add(certificate);
    }

    public void updateCertificate(String certificate) {
        certsUpdate.add(certificate);
    }

    public void removeCertificate(String certificate) {
        certsRemove.add(certificate);
    }
}
