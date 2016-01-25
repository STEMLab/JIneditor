package edu.pnu.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import net.opengis.indoorgml.core.CCTV;
import net.opengis.indoorgml.core.CCTVOnFloor;
import net.opengis.indoorgml.core.IndoorFeatures;

public class CCTVExporter {
	private IndoorFeatures indoorFeatures;

	public CCTVExporter(IndoorFeatures indoorFeatures) {
		this.indoorFeatures = indoorFeatures;
	}
	
	public void export(File file) {
		ArrayList<CCTVOnFloor> cctvOnFloors = indoorFeatures.getPrimalSpaceFeatures().getCctvOnFloors();
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(file);
			for(CCTVOnFloor cctvOnFloor : cctvOnFloors) {
				for(CCTV cctv : cctvOnFloor.getCCTVMember()) {
					String line = getCSVLine(cctv);					
					
				}
			}
			
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	private String getCSVLine(CCTV cctv) {
		StringBuilder builder = new StringBuilder();
		
		if(cctv.getCctvID() != null) {
			builder.append(cctv.getCctvID() + ",");
		} else {
			builder.append(" ,");
		}
		
		if(cctv.getIp() != null) {
			builder.append(cctv.getIp() + ",");
		} else {
			builder.append(" ,");
		}
		
		if(cctv.getSourceLocation() != null) {
			builder.append(cctv.getSourceLocation() + ",");
		} else {
			builder.append(" ,");
		}
		
		if(cctv.getId() != null) {
			builder.append(cctv.getId() + ",");
		} else {
			builder.append(" ,");
		}
		
		if(cctv.getPassword() != null) {
			builder.append(cctv.getPassword() + ",");
		} else {
			builder.append(" ,");
		}
		
		builder.append(cctv.getWidth() + ",");
		builder.append(cctv.getHeight() + ",");
		builder.append(cctv.getFramerate() + ",");
		builder.append(cctv.getOrientation() + ",");
		builder.append(cctv.getFov() + ",");
		builder.append(cctv.getAspect() + ",");
		builder.append("POINT(" + cctv.getLocation().getRealX() + " " + cctv.getLocation().getRealY() + " " + cctv.getLocation().getZ() + "), ");
		
		if(cctv.getMappedState() != null) {
			builder.append(cctv.getMappedState().getGmlID());
		}
		
		return builder.toString();
	}
}
