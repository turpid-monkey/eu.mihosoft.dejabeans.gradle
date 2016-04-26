package eu.mihosoft.dejabeans.gradle;

import java.io.File;

public class DejabeansExtension {

	File outputDir;
	String[] classes;

	public File getOutputDir() {
		return outputDir;
	}

	public void setOutputDir(File outputDir) {
		this.outputDir = outputDir;
	}

	public String[] getClasses() {
		return classes;
	}

	public void setClasses(String[] classes) {
		this.classes = classes;
	}
}
