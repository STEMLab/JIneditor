package net.opengis.indoorgml.core;

import java.io.Serializable;
import java.util.ArrayList;

public class PrimalSpaceFeatures extends AbstractFeature implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 750541720237269116L;
	private static int labelNumber = 1;
	private ArrayList<CellSpaceOnFloor> cellSpaceOnFloors;
	private ArrayList<CellSpaceBoundaryOnFloor> cellSpaceBoundaryOnFloors;
	
	private ArrayList<CCTVOnFloor> cctvOnFloors;
	
	public PrimalSpaceFeatures() {
		super.setGmlID( "PS" + (labelNumber++) );
	}
	
	public static int getLabelNumber() {
		return labelNumber;
	}


	public static void setLabelNumber(int labelNumber) {
		PrimalSpaceFeatures.labelNumber = labelNumber;
	}

	public ArrayList<CellSpaceOnFloor> getCellSpaceOnFloors() {
		if(cellSpaceOnFloors == null) {
			cellSpaceOnFloors = new ArrayList<CellSpaceOnFloor>();
		}
		return cellSpaceOnFloors;
	}
	
	public void setCellSpaceOnFloors(ArrayList<CellSpaceOnFloor> cellSpaceOnFloors) {
		this.cellSpaceOnFloors = cellSpaceOnFloors;
	}
	
	public ArrayList<CellSpaceBoundaryOnFloor> getCellSpaceBoundaryOnFloors() {
		if(cellSpaceBoundaryOnFloors == null) {
			cellSpaceBoundaryOnFloors = new ArrayList<CellSpaceBoundaryOnFloor>();
		}
		return cellSpaceBoundaryOnFloors;
	}
	
	public void setCellSpaceBoundaryOnFloors(
			ArrayList<CellSpaceBoundaryOnFloor> cellSpaceBoundaryOnFloors) {
		this.cellSpaceBoundaryOnFloors = cellSpaceBoundaryOnFloors;
	}

	public ArrayList<CCTVOnFloor> getCctvOnFloors() {
		if(cctvOnFloors == null) {
			cctvOnFloors = new ArrayList<CCTVOnFloor>();
		}
		return cctvOnFloors;
	}

	public void setCctvOnFloors(ArrayList<CCTVOnFloor> cctvOnFloors) {
		this.cctvOnFloors = cctvOnFloors;
	}
	
}
