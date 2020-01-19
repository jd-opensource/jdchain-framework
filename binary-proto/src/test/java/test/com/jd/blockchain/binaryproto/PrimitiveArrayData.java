package test.com.jd.blockchain.binaryproto;

import com.jd.blockchain.binaryproto.DataContract;
import com.jd.blockchain.binaryproto.DataField;
import com.jd.blockchain.binaryproto.PrimitiveType;

@DataContract(code = 0x11, name = "PrimitiveArray", description = "")
public interface PrimitiveArrayData {
	
	@DataField(order = 8, primitiveType = PrimitiveType.INT64, list = true)
	long[] getValues();
	
}
