package com.jd.blockchain.ledger;

import java.security.cert.X509Certificate;

public interface RootCAUpdateOperationBuilder {

    RootCAUpdateOperationBuilder add(String certificate);

    RootCAUpdateOperationBuilder add(X509Certificate certificate);

    RootCAUpdateOperationBuilder update(String certificate);

    RootCAUpdateOperationBuilder update(X509Certificate certificate);

    RootCAUpdateOperationBuilder remove(String certificate);

    RootCAUpdateOperationBuilder remove(X509Certificate certificate);

}
