package net.opengis.indoorgml.core;

import java.io.Serializable;
import java.util.ArrayList;

import edu.pnu.project.Floor;

public class CellSpaceOnFloor extends Floor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4925641466252041828L;
	private ArrayList<CellSpace> cellSpaceMember;
	
	public CellSpaceOnFloor() {
		// TODO Auto-generated constructor stub
		cellSpaceMember = new ArrayList<CellSpace>();
	}

	public ArrayList<CellSpace> getCellSpaceMember() {
		return cellSpaceMember;
	}

	public void setCellSpaceMember(ArrayList<CellSpace> cellSpaceMember) {
		this.cellSpaceMember = cellSpaceMember;
	}

}
