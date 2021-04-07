package com.jd.blockchain.crypto;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.ServiceLoader;

import utils.Bytes;
import utils.io.BytesEncoding;
import utils.io.BytesUtils;
import utils.io.NumberMask;
import utils.security.RipeMD160Utils;
import utils.security.ShaUtils;

public class AddressEncoding {

	private static volatile AddressGenerator addressGenerator;

	static {
		initAddresssGenerator();
	}

	private static void initAddresssGenerator() {
		AddressGenerator addressGenerator = loadAddressGenerator(AddressGenerator.class.getClassLoader());
		if (addressGenerator == null) {
			addressGenerator = loadAddressGenerator(Thread.currentThread().getContextClassLoader());
		}
		if (addressGenerator == null) {
			addressGenerator = new DefaultAddressGenerator();
		}
		AddressEncoding.addressGenerator = addressGenerator;
	}

	private static AddressGenerator loadAddressGenerator(ClassLoader classLoader) {
		ServiceLoader<AddressGenerator> loader = ServiceLoader.load(AddressGenerator.class,
				classLoader);
		AddressGenerator addrGen = null;
		for (AddressGenerator addressGenerator : loader) {
			if (addrGen != null) {
				throw new IllegalStateException("Registered more than one AddressGenerator instances! --["
						+ addrGen.getClass().getName() + "] and [" + addressGenerator.getClass().getName() + "]");
			}
			addrGen = addressGenerator;
		}
		return addrGen;
	}

	/**
	 * 将区块链地址写入到输出流；<br>
	 * 
	 * 现将地址按 Base58 解码为字节数组，并将字节数组以 {@link BytesEncoding} 的方式写入输出流；<br>
	 * 
	 * 如果指定的地址为 null，则仅写入空字节数组；注：此种情况下，输出流并不是完全没有写入，而是实际上会被写入一个表示内容长度为 0 的头部字节；<br>
	 * 
	 * @param address 要写入的区块链地址；
	 * @param out     输出流；
	 * @return 写入的地址的字节数；如果指定地址为 null，则返回值为写入的头部字节数；；
	 */
	public static int writeAddress(Bytes address, OutputStream out) {
		return address.writeTo(out);
	}

	/**
	 * 从流中读取区块链地址；
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static Bytes readAddress(InputStream in) throws IOException {
		byte[] bytesAddress = BytesEncoding.read(NumberMask.TINY, in);
		if (bytesAddress.length == 0) {
			return null;
		}
		return new Bytes(bytesAddress);
	}

	/**
	 * 从公钥生成地址；
	 * 
	 * @param pubKey
	 * @return
	 */
	public static Bytes generateAddress(PubKey pubKey) {
		return addressGenerator.generate(pubKey);
	}

}
