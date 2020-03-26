package com.jd.blockchain.utils.io;

public class DynamicBytesSliceArray implements BytesSlices {

	private byte[] dataBytes;

	private int totalSize;

	private int[] offsets;

	private int[] sizes;

	private DynamicBytesSliceArray(byte[] dataBytes, int totalSize, int[] offsets, int[] sizes) {
		this.dataBytes = dataBytes;
		this.totalSize = totalSize;
		this.offsets = offsets;
		this.sizes = sizes;
	}

	@Override
	public int getTotalSize() {
		return totalSize;
	}

	@Override
	public int getCount() {
		return sizes.length;
	}

	@Override
	public BytesSlice getDataSlice(int idx) {
		return new BytesSlice(dataBytes, offsets[idx], sizes[idx]);
	}

	public static BytesSlices resolve(BytesInputStream in) {
		int p1 = in.getPosition();
		int count = (int) NumberMask.NORMAL.resolveMaskedNumber(in);

		int[] offsets = new int[count];
		int[] sizes = new int[count];

		int size;
		for (int i = 0; i < count; i++) {
			size = (int) NumberMask.NORMAL.resolveMaskedNumber(in);
			sizes[i] = size;
			offsets[i] = in.getPosition();
			in.skip(size);
		}
		int totalSize = in.getPosition() - p1;
		return new DynamicBytesSliceArray(in.getOriginBytes(), totalSize, offsets, sizes);
	}

	public static BytesSlices resolveNumbers(NumberMask numberMask, BytesInputStream in) {
		int p1 = in.getPosition();
		int count = (int) NumberMask.NORMAL.resolveMaskedNumber(in);

		int[] offsets = new int[count];
		int[] sizes = new int[count];

		int size;
		byte headByte;
		for (int i = 0; i < count; i++) {
			offsets[i] = in.getPosition();

			headByte = in.readByte();
			size = (int) numberMask.resolveMaskLength(headByte);
			sizes[i] = size;

			in.skip(size - 1);
		}
		int totalSize = in.getPosition() - p1;
		return new DynamicBytesSliceArray(in.getOriginBytes(), totalSize, offsets, sizes);
	}

}
