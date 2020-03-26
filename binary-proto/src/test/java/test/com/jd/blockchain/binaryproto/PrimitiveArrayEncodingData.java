package test.com.jd.blockchain.binaryproto;

import com.jd.blockchain.binaryproto.DataContract;
import com.jd.blockchain.binaryproto.DataField;
import com.jd.blockchain.binaryproto.NumberEncoding;
import com.jd.blockchain.binaryproto.PrimitiveType;

@DataContract(code = 0x12, name = "PrimitiveArray", description = "")
public interface PrimitiveArrayEncodingData {

	@DataField(order = 8, primitiveType = PrimitiveType.INT64, list = true, numberEncoding = NumberEncoding.LONG)
	long[] getValues();

}
