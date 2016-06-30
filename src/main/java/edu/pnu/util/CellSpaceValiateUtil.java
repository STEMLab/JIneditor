package edu.pnu.util;

import net.opengis.indoorgml.core.IndoorFeatures;
import edu.pnu.project.ProjectFile;

public class CellSpaceValiateUtil {
	private ProjectFile project;

	public CellSpaceValiateUtil(ProjectFile project) {
		this.project = project;
	}
	
	public void validate() {
		IndoorFeatures indoorFeatures = project.getIndoorFeatures();
		double scale = project.getCurrentFloorPlanScale();
		
	}

}
