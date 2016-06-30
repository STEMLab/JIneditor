package net.opengis.indoorgml.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import edu.pnu.project.Floor;
import net.opengis.indoorgml.geometry.LineString;

public class CellSpaceBoundaryOnFloor extends Floor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3632013332695708224L;
	private ArrayList<CellSpaceBoundary> cellSpaceBoundaryMember;
	private HashMap<LineString, ArrayList<LineString>> xLink2DMap;
	// xlink 정보를 저장하여 참조되고 있는 geometry가 삭제될 때 참조 하고 있는 나머지 geometry중 하나의 gmlID로 xlink를 변경한다. 
	
		// private HashMap<Polygon, ArrayList<Polygon>> xLink3DMap;
		// 현재는 export할 때마다 3DGeometry를 무조건 생성하고 있으므로
		// cellspace를 삭제할 때 xlink정보를 수정해줄 필요가 없다.
	
	private HashMap<LineString, ArrayList<CellSpaceBoundary>> lineStringOfAdjacencyBoundaryMap;
	private HashMap<CellSpaceBoundary, ArrayList<CellSpace>> boundaryOfReferenceCellSpaceMap;
	
	public CellSpaceBoundaryOnFloor() {
		// TODO Auto-generated constructor stub
		cellSpaceBoundaryMember = new ArrayList<CellSpaceBoundary>();
		xLink2DMap = new HashMap<LineString, ArrayList<LineString>>();
		
		lineStringOfAdjacencyBoundaryMap = new HashMap<LineString, ArrayList<CellSpaceBoundary>>();
		boundaryOfReferenceCellSpaceMap = new HashMap<CellSpaceBoundary, ArrayList<CellSpace>>();
	}

	public ArrayList<CellSpaceBoundary> getCellSpaceBoundaryMember() {
		return cellSpaceBoundaryMember;
	}

	public void setCellSpaceBoundaryMember(
			ArrayList<CellSpaceBoundary> cellSpaceBoundaryMember) {
		this.cellSpaceBoundaryMember = cellSpaceBoundaryMember;
	}

	public HashMap<LineString, ArrayList<LineString>> getxLink2DMap() {
		return xLink2DMap;
	}

	public void setxLink2DMap(HashMap<LineString, ArrayList<LineString>> xLink2DMap) {
		this.xLink2DMap = xLink2DMap;
	}

	public HashMap<LineString, ArrayList<CellSpaceBoundary>> getLineStringOfAdjacencyBoundaryMap() {
		return lineStringOfAdjacencyBoundaryMap;
	}

	public void setLineStringOfAdjacencyBoundaryMap(
			HashMap<LineString, ArrayList<CellSpaceBoundary>> lineStringOfAdjacencyBoundaryMap) {
		this.lineStringOfAdjacencyBoundaryMap = lineStringOfAdjacencyBoundaryMap;
	}

	public HashMap<CellSpaceBoundary, ArrayList<CellSpace>> getBoundaryOfReferenceCellSpaceMap() {
		return boundaryOfReferenceCellSpaceMap;
	}

	public void setBoundaryOfReferenceCellSpaceMap(
			HashMap<CellSpaceBoundary, ArrayList<CellSpace>> boundaryOfReferenceCellSpaceMap) {
		this.boundaryOfReferenceCellSpaceMap = boundaryOfReferenceCellSpaceMap;
	}

}
