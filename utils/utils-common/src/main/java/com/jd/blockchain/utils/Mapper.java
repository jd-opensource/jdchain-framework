package com.jd.blockchain.utils;

public interface Mapper<S, T> {
	
	T from(S source);
	
}
