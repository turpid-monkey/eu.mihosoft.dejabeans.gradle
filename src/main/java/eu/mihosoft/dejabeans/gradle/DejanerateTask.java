package eu.mihosoft.dejabeans.gradle;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.gradle.api.DefaultTask;
import org.gradle.api.logging.Logger;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.compile.JavaCompile;
import org.mism.modelgen.CommandLineInterface;

public class DejanerateTask extends DefaultTask {

	public DejanerateTask() {
		setName("dejanerate");
		this.dependsOn("compileJava");
	}

	@TaskAction
	public void message() {
		DejabeansExtension config = getProject().getExtensions().getByType(
				DejabeansExtension.class);
		Logger log = getProject().getLogger();
		SourceSet mainSourceSet = getProject().getConvention()
				.getPlugin(JavaPluginConvention.class).getSourceSets()
				.getByName("main");
		
		File outputDir;
		if (config.getOutputDir()==null)
		{
			outputDir = mainSourceSet.getJava().getSrcDirs().iterator().next();
		} else
		{
			outputDir = config.getOutputDir();
		}
		log.debug("OutputDir=" + outputDir);
		
		HashSet<File> classpath = new HashSet<File>();
		classpath.add(mainSourceSet.getOutput().getClassesDir());
		classpath.addAll(mainSourceSet.getCompileClasspath().getFiles());
		try {
			URL[] urls;
			urls = classpath.stream().map(file -> {
				try {
					log.debug("Classpath+=" + file.getAbsolutePath());
					return file.toURI().toURL();
				} catch (MalformedURLException e) {
					log.warn("Classpath contains malformed URL?", e);
					return null;
				}
			}).toArray(URL[]::new);
			URLClassLoader cl = new URLClassLoader(urls, this.getClass()
					.getClassLoader());
			List<Class<?>> clzzes = new ArrayList<Class<?>>();
			for (String cls : config.getClasses()) {
				log.debug("Class+=" + cls);
				clzzes.add(cl.loadClass(cls));
			}
			cl.close();
			log.debug("Generate code");
			CommandLineInterface cli = new CommandLineInterface();
			cli.setClzzes(clzzes);
			cli.setDirectory(outputDir);
			cli.generateCode();
		} catch (Throwable e) {
			log.error("Error during dejabeans code generation.", e);
		}
	}
}
