package test.com.jd.blockchain.ledger;

import com.jd.binaryproto.BinaryProtocol;
import com.jd.blockchain.ledger.DataType;
import com.jd.blockchain.ledger.TypedValue;
import org.junit.Assert;
import org.junit.Test;
import utils.Bytes;

public class TypedValueTest {

    @Test
    public void testEmptyValue() {
        String text = "";
        Bytes bytes1 = Bytes.fromString(text);
        byte[] bytes2 = text.getBytes();
        TypedValue[] origins = new TypedValue[]{
                TypedValue.fromBytes(bytes1),
                TypedValue.fromBytes(bytes2),
                TypedValue.fromImage(bytes2),
                TypedValue.fromText(text),
                TypedValue.fromXML(text),
                TypedValue.fromJSON(text),
                TypedValue.fromType(DataType.TEXT, bytes2),
                TypedValue.fromType(DataType.TIMESTAMP, bytes2),
                TypedValue.fromType(DataType.BOOLEAN, bytes2),
                TypedValue.fromType(DataType.INT8, bytes2),
                TypedValue.fromType(DataType.INT16, bytes2),
                TypedValue.fromType(DataType.INT32, bytes2),
                TypedValue.fromType(DataType.INT64, bytes2)
        };

        for (TypedValue tv : origins) {
            byte[] encoded = BinaryProtocol.encode(tv);
            TypedValue decoded = TypedValue.wrap(BinaryProtocol.decode(encoded));
            Assert.assertTrue(decoded.isNil());
            Assert.assertEquals(tv.getType(), decoded.getType());
            Assert.assertEquals(tv.getBytes(), decoded.getBytes());
        }
    }

    @Test
    public void testNullValue() {
        Bytes bytes1 = null;
        byte[] bytes2 = null;
        TypedValue[] origins = new TypedValue[]{
                TypedValue.fromBytes(bytes1),
                TypedValue.fromBytes(bytes2),
                TypedValue.fromImage(bytes2),
                TypedValue.fromText(null),
                TypedValue.fromXML(null),
                TypedValue.fromJSON(null),
                TypedValue.fromType(DataType.TIMESTAMP, null),
                TypedValue.fromType(DataType.BOOLEAN, null),
                TypedValue.fromType(DataType.INT8, null),
                TypedValue.fromType(DataType.INT16, null),
                TypedValue.fromType(DataType.INT32, null),
                TypedValue.fromType(DataType.INT64, null),
                TypedValue.fromType(DataType.TEXT, null)
        };

        for (TypedValue tv : origins) {
            byte[] encoded = BinaryProtocol.encode(tv);
            TypedValue decoded = TypedValue.wrap(BinaryProtocol.decode(encoded));
            Assert.assertTrue(decoded.isNil());
            Assert.assertEquals(tv.getType(), decoded.getType());
            Assert.assertEquals(tv.getValue(), decoded.getValue());
        }
    }

}
