package com.jd.blockchain.web.converters;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.jd.binaryproto.BinaryProtocol;
import com.jd.binaryproto.DataContractRegistry;
import com.jd.blockchain.consensus.ClientCredential;
import com.jd.blockchain.consensus.action.ActionRequest;
import com.jd.blockchain.consensus.action.ActionResponse;
import com.jd.blockchain.ledger.ConsensusSettingsUpdateOperation;
import com.jd.blockchain.ledger.ContractCodeDeployOperation;
import com.jd.blockchain.ledger.ContractEventSendOperation;
import com.jd.blockchain.ledger.DataAccountKVSetOperation;
import com.jd.blockchain.ledger.DataAccountRegisterOperation;
import com.jd.blockchain.ledger.EventAccountRegisterOperation;
import com.jd.blockchain.ledger.EventPublishOperation;
import com.jd.blockchain.ledger.LedgerTransactions;
import com.jd.blockchain.ledger.Operation;
import com.jd.blockchain.ledger.ParticipantRegisterOperation;
import com.jd.blockchain.ledger.ParticipantStateUpdateOperation;
import com.jd.blockchain.ledger.TransactionContent;
import com.jd.blockchain.ledger.TransactionRequest;
import com.jd.blockchain.ledger.TransactionResponse;
import com.jd.blockchain.ledger.UserRegisterOperation;

import utils.io.BytesUtils;

/**
 * 针对二进制对象的序列化和反序列化的 HTTP 消息转换器；
 * 
 * @author huanghaiquan
 *
 */
public class BinaryMessageConverter extends AbstractHttpMessageConverter<Object> {

    public static final String CONTENT_TYPE_VALUE = "application/bin-obj;charset=UTF-8";


	public BinaryMessageConverter() {
		super(MediaType.valueOf(CONTENT_TYPE_VALUE));
	}

	@Override
	protected boolean supports(Class<?> aClass) {
		return true;
	}

    @Override
    protected boolean canRead(MediaType mediaType) {
        if (mediaType == null) {
            return false;
        }
        for (MediaType supportedMediaType : getSupportedMediaTypes()) {
            if (supportedMediaType.includes(mediaType)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean canWrite(MediaType mediaType) {
        if (mediaType == null) {
            return false;
        } else if (MediaType.ALL.equals(mediaType)) {
            return true;
        }
        for (MediaType supportedMediaType : getSupportedMediaTypes()) {
            if (supportedMediaType.isCompatibleWith(mediaType)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object readInternal(Class<? extends Object> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        InputStream in = inputMessage.getBody();
        byte[] serializeBytes = BytesUtils.readBytes(in);
        Object resolvedObj = BinaryProtocol.decode(serializeBytes);
        return resolvedObj;
    }

    @Override
    public void writeInternal(Object t, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        OutputStream out = outputMessage.getBody();
        if (t instanceof TransactionResponse) {
            byte[] serializeBytes = BinaryProtocol.encode(t, TransactionResponse.class);
            out.write(serializeBytes);
            out.flush();
        } else if (t instanceof LedgerTransactions) {
            byte[] serializeBytes = BinaryProtocol.encode(t, LedgerTransactions.class);
            out.write(serializeBytes);
            out.flush();
        }
    }
}
