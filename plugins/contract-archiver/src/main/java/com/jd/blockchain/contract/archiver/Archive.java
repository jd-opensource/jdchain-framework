package com.jd.blockchain.contract.archiver;

import java.io.File;

public interface Archive {
	
	String getType();
	
	ArchiveLayout getLayout();
	
	File getOutputFile();
	
}
