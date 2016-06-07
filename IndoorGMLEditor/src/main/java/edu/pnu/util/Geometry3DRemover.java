package edu.pnu.util;

import java.util.ArrayList;

import edu.pnu.project.BoundaryType;
import net.opengis.indoorgml.core.CellSpace;
import net.opengis.indoorgml.core.CellSpaceBoundary;
import net.opengis.indoorgml.core.CellSpaceBoundaryOnFloor;
import net.opengis.indoorgml.core.CellSpaceOnFloor;
import net.opengis.indoorgml.core.IndoorFeatures;
import net.opengis.indoorgml.core.PrimalSpaceFeatures;

public class Geometry3DRemover {
	
	public Geometry3DRemover() {
		
	}
	
	public static void removeGeometry3D(IndoorFeatures indoorFeatures) {
		PrimalSpaceFeatures primalSpaceFeatures = indoorFeatures.getPrimalSpaceFeatures();
		
		ArrayList<CellSpaceOnFloor> cellSpaceOnFloorList = primalSpaceFeatures.getCellSpaceOnFloors();
		ArrayList<CellSpaceBoundaryOnFloor> cellSpaceBoundaryOnFloorList = primalSpaceFeatures.getCellSpaceBoundaryOnFloors();
		for (CellSpaceOnFloor cellSpaceOnFloor : cellSpaceOnFloorList) {
			removeGeometry3D(cellSpaceOnFloor);
		}
		for (CellSpaceBoundaryOnFloor cellSpaceBoundaryOnFloor : cellSpaceBoundaryOnFloorList) {
			removeGeometry3D(cellSpaceBoundaryOnFloor);
		}
	}
	
	public static void removeGeometry3D(CellSpaceOnFloor cellSpaceOnFloor) {
		ArrayList<CellSpace> cellSpaceMember = cellSpaceOnFloor.getCellSpaceMember();
		
		for (CellSpace cellSpace : cellSpaceMember) {
			cellSpace.setGeometry3D(null);
			ArrayList<CellSpaceBoundary> partialBoundedBy = cellSpace.getPartialBoundedBy();
			ArrayList<CellSpaceBoundary> removes = new ArrayList<CellSpaceBoundary>();
			for (CellSpaceBoundary bounded : partialBoundedBy) {
				if (bounded.getBoundaryType() == BoundaryType.Boundary3D) {
					removes.add(bounded);
				}
			}
			if (removes.size() > 0) {
				partialBoundedBy.removeAll(removes);
			}
		}
	}
	
	public static void removeGeometry3D(CellSpaceBoundaryOnFloor cellSpaceBoundaryOnFloor) {
		ArrayList<CellSpaceBoundary> cellSpaceBoundaryMember = cellSpaceBoundaryOnFloor.getCellSpaceBoundaryMember();
		ArrayList<CellSpaceBoundary> removes = new ArrayList<CellSpaceBoundary>();
		
		for (CellSpaceBoundary boundary : cellSpaceBoundaryMember) {
			if (boundary.getBoundaryType() == BoundaryType.Boundary3D) {
				removes.add(boundary);
			}
		}
		if (removes.size() > 0) {
			cellSpaceBoundaryMember.removeAll(removes);
		}
	}

}
