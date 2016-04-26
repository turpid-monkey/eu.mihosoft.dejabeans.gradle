package eu.mihosoft.dejabeans.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class DejabeansPlugin implements Plugin<Project> {

	public void apply(Project prj) {
	
		prj.getExtensions().create("dejabeans", DejabeansExtension.class);
		prj.getTasks().create("dejanerate", DejanerateTask.class);
	    
	}
}
