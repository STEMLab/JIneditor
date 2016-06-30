package edu.pnu.util;

import java.util.ArrayList;
import java.util.HashMap;

import net.opengis.indoorgml.core.CellSpace;
import net.opengis.indoorgml.core.CellSpaceBoundary;
import net.opengis.indoorgml.core.CellSpaceBoundaryOnFloor;
import net.opengis.indoorgml.core.CellSpaceOnFloor;
import net.opengis.indoorgml.core.IndoorFeatures;
import net.opengis.indoorgml.core.PrimalSpaceFeatures;
import net.opengis.indoorgml.core.State;
import net.opengis.indoorgml.core.Transition;
import net.opengis.indoorgml.geometry.LineString;
import net.opengis.indoorgml.geometry.Point;
import edu.pnu.gui.CanvasPanel;
import edu.pnu.project.BoundaryType;

public class CellSpaceBoundaryBuilder2 {

	private CanvasPanel panel;
	private IndoorFeatures indoorFeatures;
	
	public CellSpaceBoundaryBuilder2(CanvasPanel panel, IndoorFeatures indoorFeatures) {
		this.panel = panel;
		this.indoorFeatures = indoorFeatures;
	}
	
	public void build() {
		//panel.setPanelXYForCurrentScaleForJSK();
		TransitionRegistryChecker checker = new TransitionRegistryChecker(indoorFeatures);
		checker.check();
		
		PrimalSpaceFeatures primalSpaceFeatures = indoorFeatures.getPrimalSpaceFeatures();
		ArrayList<CellSpaceOnFloor> cellSpaceOnFloorList = primalSpaceFeatures.getCellSpaceOnFloors();
		ArrayList<CellSpaceBoundaryOnFloor> cellSpaceBoundaryOnFloorList = primalSpaceFeatures.getCellSpaceBoundaryOnFloors();
		
		for (int p = 0; p < cellSpaceOnFloorList.size(); p++) {
			CellSpaceOnFloor cellSpaceOnFloor = cellSpaceOnFloorList.get(p);
			CellSpaceBoundaryOnFloor cellSpaceBoundaryOnFloor = cellSpaceBoundaryOnFloorList.get(p);
			HashMap<LineString, ArrayList<CellSpaceBoundary>> lineStringOfAdjacencyBoundaryMap =
					cellSpaceBoundaryOnFloor.getLineStringOfAdjacencyBoundaryMap();
			ArrayList<CellSpace> cellSpaceMember = cellSpaceOnFloor.getCellSpaceMember();
			
			ArrayList<CellSpace> doorCells = new ArrayList<CellSpace>();
			ArrayList<CellSpace> roomCells = new ArrayList<CellSpace>();
			for (CellSpace cellSpace : cellSpaceMember) {
				String usage = cellSpace.getDescription("Usage");
				if (usage != null && usage.equals("Door")) {
					doorCells.add(cellSpace);
				} else {
					roomCells.add(cellSpace);
				}
			}
			
			for (CellSpace door : doorCells) {
				ArrayList<LineString> doorLSElements = door.getLineStringElements();
				for (CellSpace room : roomCells) {
					ArrayList<LineString> roomLSElements = room.getLineStringElements();
					
					for (LineString doorLS : doorLSElements) {
						if (lineStringOfAdjacencyBoundaryMap.containsKey(doorLS)) {
							continue;
						}
						for (LineString roomLS : roomLSElements) {
							if (GeometryUtil.isContainsLineString(roomLS, doorLS)) {
								CellSpaceBoundary newBoundary = createCellSpaceBoundaryForJSK(doorLS, roomLS, door, room, cellSpaceBoundaryOnFloor);
		            			newBoundary.setBoundaryType(BoundaryType.Door);
		            			break;
							}
						}
					}
				}
			}
		}
		
		System.out.println("build end");
	}

	public CellSpaceBoundary createCellSpaceBoundaryForJSK(LineString ls, LineString otherLS, CellSpace c1, CellSpace c2, CellSpaceBoundaryOnFloor cellSpaceBoundaryOnFloor) {
    	HashMap<LineString, ArrayList<CellSpaceBoundary>> lineStringOfAdjacencyBoundaryMap = cellSpaceBoundaryOnFloor.getLineStringOfAdjacencyBoundaryMap();
        HashMap<CellSpaceBoundary, ArrayList<CellSpace>> boundaryOfReferenceCellSpaceMap = cellSpaceBoundaryOnFloor.getBoundaryOfReferenceCellSpaceMap();
                
        // geometry2d of boundary will be a intersection between ls and otherLS.
        com.vividsolutions.jts.geom.LineString line1 = JTSUtil.convertJTSLineString(ls);
        com.vividsolutions.jts.geom.LineString line2 = JTSUtil.convertJTSLineString(otherLS);
        double distance1 = line1.getStartPoint().distance(line1.getEndPoint());
        double distance2 = line2.getStartPoint().distance(line2.getEndPoint());
        LineString intersection = new LineString();
        ArrayList<Point> newPoints = intersection.getPoints();
        if (distance1 < distance2) {
        	newPoints.add(ls.getPoints().get(0).clone());
        	newPoints.add(ls.getPoints().get(1).clone());
        } else {
        	newPoints.add(otherLS.getPoints().get(0).clone());
        	newPoints.add(otherLS.getPoints().get(1).clone());
        }
        
        CellSpaceBoundary newBoundary = createCellSpaceBoundary(intersection);
        
        c1.getPartialBoundedBy().add(newBoundary);
        c2.getPartialBoundedBy().add(newBoundary);

        if (!lineStringOfAdjacencyBoundaryMap.containsKey(ls)) {
            lineStringOfAdjacencyBoundaryMap.put(ls,
                    new ArrayList<CellSpaceBoundary>());
        }
        if (!lineStringOfAdjacencyBoundaryMap.containsKey(otherLS)) {
            lineStringOfAdjacencyBoundaryMap.put(otherLS,
                    new ArrayList<CellSpaceBoundary>());
        }
        lineStringOfAdjacencyBoundaryMap.get(ls).add(newBoundary);
        lineStringOfAdjacencyBoundaryMap.get(otherLS).add(newBoundary);

        if (!boundaryOfReferenceCellSpaceMap.containsKey(newBoundary)) {
            boundaryOfReferenceCellSpaceMap.put(newBoundary,
                    new ArrayList<CellSpace>());
        }
        boundaryOfReferenceCellSpaceMap.get(newBoundary).add(c1);
        boundaryOfReferenceCellSpaceMap.get(newBoundary).add(c2);

        cellSpaceBoundaryOnFloor.getCellSpaceBoundaryMember().add(newBoundary);
        
        return newBoundary;
    }
	
	public CellSpaceBoundary createCellSpaceBoundary(LineString geometry2D) {
    	CellSpaceBoundary newBoundary = new CellSpaceBoundary(); // 1. 다른 방과 붙어 있어 벽에 대한 boundary가 있는 경우. 이 때는 여기 조건에 포함되지 않음
        
        ArrayList<Point> newPoints = geometry2D.getPoints();
        for (Point p : newPoints) {
        	if (p.getPanelRatioX() == 0 && p.getPanelRatioY() == 0) {
            	panel.setPanelRatioXY(p);
        	}
        }
        geometry2D.setPoints(newPoints);
        
        geometry2D.setIsReversed(false);
        newBoundary.setGeometry2D(geometry2D);
        newBoundary.setBoundaryType(BoundaryType.CellSpaceBoundary);
        
        return newBoundary;
    }
}
