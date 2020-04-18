package com.jd.blockchain.contract.archiver;

import org.codehaus.plexus.archiver.jar.JarArchiver;

public class CLibArchiver extends JarArchiver {
	
	public static final String TYPE = "clib";
	
	public CLibArchiver() {
		archiveType = TYPE;
	}
	
}
