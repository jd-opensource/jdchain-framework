/**
 * Copyright: Copyright 2016-2020 JD.COM All Right Reserved
 * FileName: com.jd.blockchain.consensus.ClientIdentificationsProvider
 * Author: shaozhuguang
 * Department: 区块链研发部
 * Date: 2018/12/19 下午3:59
 * Description:
 */
package com.jd.blockchain.consensus;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author shaozhuguang
 * @create 2018/12/19
 * @since 1.0.0
 */

public class ClientIdentificationsProvider implements ClientIdentifications {

    private List<ClientCredential> clientIdentifications = new ArrayList<>();

    public void add(ClientCredential clientIdentification) {
        clientIdentifications.add(clientIdentification);
    }

    @Override
    public ClientCredential[] getClientIdentifications() {
        return clientIdentifications.toArray(new ClientCredential[clientIdentifications.size()]);
    }
}