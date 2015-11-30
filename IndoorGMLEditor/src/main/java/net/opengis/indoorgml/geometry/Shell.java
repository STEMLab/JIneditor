package net.opengis.indoorgml.geometry;

import java.io.Serializable;
import java.util.ArrayList;

public class Shell implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4788766762821872665L;
	private ArrayList<Polygon> surfaceMember;
	
	public Shell() {
		// TODO Auto-generated constructor stub
	}

	public ArrayList<Polygon> getSurfaceMember() {
		return surfaceMember;
	}

	public void setSurfaceMember(ArrayList<Polygon> surfaceMember) {
		this.surfaceMember = surfaceMember;
	}

}
