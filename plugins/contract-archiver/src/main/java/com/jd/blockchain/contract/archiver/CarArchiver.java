package com.jd.blockchain.contract.archiver;

import org.codehaus.plexus.archiver.jar.JarArchiver;

public class CarArchiver extends JarArchiver {
	
	public static final String TYPE = "car";
	
	public CarArchiver() {
		archiveType = TYPE;
	}
	
}
