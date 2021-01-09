package com.jd.blockchain.ledger;

import com.jd.binaryproto.DataContract;
import com.jd.binaryproto.DataField;
import com.jd.binaryproto.PrimitiveType;
import com.jd.blockchain.consts.DataCodes;
import com.jd.blockchain.utils.Bytes;

@DataContract(code = DataCodes.METADATA_LEDGER_SETTING)
public interface LedgerSettings {

    @DataField(order=0, primitiveType=PrimitiveType.TEXT)
    String getConsensusProvider();

    @DataField(order=1, primitiveType=PrimitiveType.BYTES)
    Bytes getConsensusSetting();

    @DataField(order=2, refContract=true)
    CryptoSetting getCryptoSetting();

}