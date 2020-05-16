package com.jd.blockchain.maven.plugins.utils;

import java.io.File;

import org.codehaus.plexus.util.ReaderFactory;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;

public class Pom {

	private String groupId;

	private String artifactId;

	private String version;

	private String packaging;
	
	
	public String getGroupId() {
		return groupId;
	}


	public String getArtifactId() {
		return artifactId;
	}


	public String getVersion() {
		return version;
	}


	public String getPackaging() {
		return packaging;
	}


	private Pom() {
	}
	

	public static Pom resolve(File pluginPom) {
		try {
			Xpp3Dom pluginPomDom = Xpp3DomBuilder.build(ReaderFactory.newXmlReader(pluginPom));

			String groupId = resolveFromRootThenParent(pluginPomDom, "groupId");
			String artifactId = pluginPomDom.getChild("artifactId").getValue();
			String version = resolveFromRootThenParent(pluginPomDom, "version");
			String packaging = resolveFromRootThenParent(pluginPomDom, "packaging");
			
			Pom pom = new Pom();
			pom.groupId = groupId;
			pom.artifactId = artifactId;
			pom.version = version;
			pom.packaging = packaging;
			
			return pom;

		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	/**
	 * sometimes the parent element might contain the correct value so generalize
	 * that access
	 *
	 * TODO find out where this is probably done elsewhere
	 *
	 * @param pluginPomDom
	 * @param element
	 * @return
	 * @throws Exception
	 */
	private static String resolveFromRootThenParent(Xpp3Dom pluginPomDom, String element) throws Exception {
		Xpp3Dom elementDom = pluginPomDom.getChild(element);

		// parent might have the group Id so resolve it
		if (elementDom == null) {
			Xpp3Dom pluginParentDom = pluginPomDom.getChild("parent");

			if (pluginParentDom != null) {
				elementDom = pluginParentDom.getChild(element);

				if (elementDom == null) {
					throw new Exception("unable to determine " + element);
				}

				return elementDom.getValue();
			}

			throw new Exception("unable to determine " + element);
		}

		return elementDom.getValue();
	}
}
