package com.jd.blockchain.ledger;

import com.jd.blockchain.crypto.HashDigest;

/**
 * 运行时环境账本查询
 */
public interface LedgerQueryService {

    /**
     * 获取账本信息
     */
    LedgerAdminInfo getLedgerAdminInfo();

    /**
     * 返回参与者信息列表
     */
    ParticipantNode[] getConsensusParticipants();

    /**
     * 返回账本元数据
     */
    LedgerMetadata getLedgerMetadata();

    /**
     * 返回交易总数
     */
    long getTransactionTotalCount();

    /**
     * 返回数据账户总数
     *
     * @return
     */
    long getDataAccountTotalCount();

    /**
     * 返回用户总数
     */
    long getUserTotalCount();

    /**
     * 返回合约总数
     */
    long getContractTotalCount();

    /**
     * 根据交易内容的哈希获取对应的交易记录
     *
     * @param contentHash 交易内容的hash
     * @return
     */
    LedgerTransaction getTransactionByContentHash(HashDigest contentHash);

    /**
     * 根据交易内容的哈希获取对应的交易状态
     *
     * @param contentHash 交易内容的hash
     * @return
     */
    TransactionState getTransactionStateByContentHash(HashDigest contentHash);

    /**
     * 返回用户信息
     *
     * @param address
     * @return
     */
    UserInfo getUser(String address);

    /**
     * 返回数据账户信息
     *
     * @param address
     * @return
     */
    DataAccountInfo getDataAccount(String address);

    /**
     * 返回数据账户中指定的键的最新值
     * <p>
     * 返回结果的顺序与指定的键的顺序是一致的；<br>
     * <p>
     * 如果某个键不存在，则返回版本为 -1 的数据项
     *
     * @param address
     * @param keys
     * @return
     */
    TypedKVEntry[] getDataEntries(String address, String... keys);

    TypedKVEntry[] getDataEntries(String address, KVInfoVO kvInfoVO);

    /**
     * 返回指定数据账户中KV数据的总数
     *
     * @param address
     * @return
     */
    long getDataEntriesTotalCount(String address);

    /**
     * 返回数据账户中指定序号的最新值； 返回结果的顺序与指定的序号的顺序是一致的
     *
     * @param address   数据账户地址
     * @param fromIndex 开始的记录数
     * @param count     本次返回的记录数
     *                  如果参数值为 -1，则返回全部的记录
     * @return
     */
    TypedKVEntry[] getDataEntries(String address, int fromIndex, int count);

    /**
     * 返回合约账户信息
     *
     * @param address
     * @return
     */
    ContractInfo getContract(String address);

    /**
     * 返回系统事件
     *
     * @param eventName    事件名
     * @param fromSequence 开始的事件序列号
     * @param count        最大数量
     * @return
     */
    Event[] getSystemEvents(String eventName, long fromSequence, int count);

    /**
     * 返回系统事件名称总数
     */
    long getSystemEventNameTotalCount();

    /**
     * 返回系统事件名称列表
     *
     * @param fromIndex
     * @param count
     * @return
     */
    String[] getSystemEventNames(int fromIndex, int count);

    /**
     * 返回最新系统事件
     *
     * @param eventName
     * @return
     */
    Event getLatestEvent(String eventName);

    /**
     * 返回指定系统事件名称下事件总数
     *
     * @param eventName
     * @return
     */
    long getSystemEventsTotalCount(String eventName);

    /**
     * 返回自定义事件账户
     *
     * @param fromIndex
     * @param count
     * @return
     */
    BlockchainIdentity[] getUserEventAccounts(int fromIndex, int count);

    /**
     * 返回事件账户信息
     *
     * @param address
     * @return
     */
    EventAccountInfo getUserEventAccount(String address);

    /**
     * 返回事件账户总数
     */
    long getUserEventAccountTotalCount();

    /**
     * 返回指定事件账户事件名称总数
     *
     * @param address
     * @return
     */
    long getUserEventNameTotalCount(String address);

    /**
     * 返回指定事件账户事件名称列表
     *
     * @param address
     * @return
     */
    String[] getUserEventNames(String address, int fromIndex, int count);

    /**
     * 返回最新用户自定义事件
     *
     * @param address
     * @param eventName
     * @return
     */
    Event getLatestEvent(String address, String eventName);

    /**
     * 返回指定事件账户，指定事件名称下事件总数
     *
     * @param address
     * @param eventName
     * @return
     */
    long getUserEventsTotalCount(String address, String eventName);

    /**
     * 返回自定义事件
     *
     * @param address      事件账户地址
     * @param eventName    事件名
     * @param fromSequence 开始的事件序列号
     * @param count        最大数量
     * @return
     */
    Event[] getUserEvents(String address, String eventName, long fromSequence, int count);

    /**
     * 返回合约账户信息
     *
     * @param address
     * @param version
     * @return
     */
    ContractInfo getContract(String address, long version);

    /**
     * 分页查询用户
     *
     * @param fromIndex
     * @param count
     * @return
     */
    BlockchainIdentity[] getUsers(int fromIndex, int count);

    /**
     * 分页查询数据账户
     *
     * @param fromIndex
     * @param count
     * @return
     */
    BlockchainIdentity[] getDataAccounts(int fromIndex, int count);

    /**
     * 分页查询合约账户
     *
     * @param fromIndex
     * @param count
     * @return
     */
    BlockchainIdentity[] getContractAccounts(int fromIndex, int count);

    /**
     * 根据角色名称查询角色信息
     *
     * @param roleName
     * @return
     */
    PrivilegeSet getRolePrivileges(String roleName);

    /**
     * 用户对应的权限信息
     *
     * @param userAddress
     * @return
     */
    UserPrivilegeSet getUserPrivileges(String userAddress);

}