package test.com.jd.blockchain.binaryproto;

public class PrimitiveArrayTestData implements PrimitiveArrayData{
	
	private long[] values;

	@Override
	public long[] getValues() {
		return values;
	}
	
	public void setValues(long[] values) {
		this.values = values;
	}

}
