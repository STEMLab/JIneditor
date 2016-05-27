package edu.pnu.util;

import java.util.ArrayList;
import java.util.HashMap;

import net.opengis.indoorgml.core.CellSpace;
import net.opengis.indoorgml.core.CellSpaceBoundary;
import net.opengis.indoorgml.core.CellSpaceBoundaryOnFloor;
import net.opengis.indoorgml.core.CellSpaceOnFloor;
import net.opengis.indoorgml.core.IndoorFeatures;
import net.opengis.indoorgml.core.PrimalSpaceFeatures;
import net.opengis.indoorgml.geometry.LineString;
import net.opengis.indoorgml.geometry.LinearRing;
import net.opengis.indoorgml.geometry.Point;
import net.opengis.indoorgml.geometry.Polygon;
import net.opengis.indoorgml.geometry.Shell;
import net.opengis.indoorgml.geometry.Solid;
import edu.pnu.gui.CanvasPanel;
import edu.pnu.project.BoundaryType;
import edu.pnu.project.FloorProperty;

public class IndoorGML3DGeometryBuilder {
	private IndoorFeatures indoorFeatures;
	private HashMap<LineString, Polygon> xLink3DMap;
	private ArrayList<LineString> combineLineStrings;
	private CanvasPanel panel;
		
	public IndoorGML3DGeometryBuilder(CanvasPanel panel, IndoorFeatures indoorFeatures) {
	        this.panel = panel;
		this.indoorFeatures = indoorFeatures;
		
		xLink3DMap = new HashMap<LineString, Polygon>();
		combineLineStrings = new ArrayList<LineString>();
	}

	public void create3DGeometry() {
		PrimalSpaceFeatures primalSpaceFeatures = indoorFeatures.getPrimalSpaceFeatures();
		
		ArrayList<CellSpaceOnFloor> cellSpaceOnFloors = primalSpaceFeatures.getCellSpaceOnFloors();
		ArrayList<CellSpaceBoundaryOnFloor> cellSpaceBoundaryOnFloors = primalSpaceFeatures.getCellSpaceBoundaryOnFloors();
		for(int i = 0; i < cellSpaceOnFloors.size(); i++) {
			fromCellSpaceOnFloor(cellSpaceOnFloors.get(i), cellSpaceBoundaryOnFloors.get(i));
		}
		
		for(int i = 0; i < cellSpaceBoundaryOnFloors.size(); i++) {
			fromCellSpaceBoundaryOnFloor(cellSpaceBoundaryOnFloors.get(i));
		}
	}
	
	public void fromCellSpaceBoundaryOnFloor(CellSpaceBoundaryOnFloor cellSpaceBoundaryOnFloor) {
		double groundHeight = cellSpaceBoundaryOnFloor.getFloorProperty().getGroundHeight();
		double ceilingHeight = cellSpaceBoundaryOnFloor.getFloorProperty().getCeilingHeight();
		double defaultDoorHeight = cellSpaceBoundaryOnFloor.getFloorProperty().getDoorHeight();
		ArrayList<CellSpaceBoundary> cellSpaceBoundaryMember = cellSpaceBoundaryOnFloor.getCellSpaceBoundaryMember();
                HashMap<CellSpaceBoundary, ArrayList<CellSpace>> boundaryOfReferenceCellSpaceMap = cellSpaceBoundaryOnFloor.getBoundaryOfReferenceCellSpaceMap();
                
		ArrayList<CellSpaceBoundary> removes = new ArrayList<CellSpaceBoundary>();
                for(CellSpaceBoundary boundary : cellSpaceBoundaryMember) {
                        if(boundary.getBoundaryType() == BoundaryType.Boundary3D) {
                                removes.add(boundary);
                                
                                ArrayList<CellSpace> references = boundaryOfReferenceCellSpaceMap.get(boundary);
                                for(CellSpace reference : references) {
                                        reference.getPartialBoundedBy().remove(boundary);
                                }
                        }
                }
                cellSpaceBoundaryMember.removeAll(removes);
		
		ArrayList<CellSpace> prevReference = null;
		ArrayList<CellSpace> currentReference = null;
		ArrayList<CellSpaceBoundary> newBoundaryList = new ArrayList<CellSpaceBoundary>();
		LineString combineLS = null;
		for(int i = 0; i < cellSpaceBoundaryMember.size(); i++) {
			CellSpaceBoundary boundary = cellSpaceBoundaryMember.get(i);
			
			if(boundary.getGeometry2D() == null) continue;
			
			if(boundary.getBoundaryType() == BoundaryType.Door)
			    setDoorHeight(boundary, defaultDoorHeight);
			
			currentReference = boundaryOfReferenceCellSpaceMap.get(boundary);
			if(prevReference == null) {
				prevReference = currentReference;
				combineLS = new LineString();
				combineLS.setPoints((ArrayList<Point>) boundary.getGeometry2D().getPoints().clone());
			} else if(prevReference.size() == currentReference.size() && prevReference.containsAll(currentReference)) { // 하나의 3D Boundary로 합칠 수 있는 2D Boundary
				combineLS.getPoints().addAll((ArrayList<Point>) boundary.getGeometry2D().getPoints().clone());
			} else { // 다르면 이전까지 합친 boundary를 하나로 생성한다.
				Polygon geometry3D = null;
				for(LineString existingLS : xLink3DMap.keySet()) {
					if(existingLS.getPoints().size() == combineLS.getPoints().size() && existingLS.getPoints().containsAll(combineLS.getPoints())) {
						geometry3D = new Polygon();
						geometry3D.setxLinkGeometry(xLink3DMap.get(existingLS));
						geometry3D.setIsReversed(false);
						break;
					}
				}
				if(geometry3D == null) {
					geometry3D = createPolygonFrom2Points(combineLS.getPoints(), ceilingHeight);
					xLink3DMap.put(combineLS, geometry3D);
				}
				
				if(combineLS.getPoints().size() > 2) {
					CellSpaceBoundary newBoundary = new CellSpaceBoundary();
					newBoundary.setBoundaryType(BoundaryType.Boundary3D);
					newBoundary.setGeometry3D(geometry3D);
					newBoundaryList.add(newBoundary);
					
					for(CellSpace reference : prevReference) {
						reference.getPartialBoundedBy().add(newBoundary);
					}
					boundaryOfReferenceCellSpaceMap.put(newBoundary, (ArrayList<CellSpace>) prevReference.clone());
				} else {
					boundary.setGeometry3D(geometry3D);
				}
				
				prevReference = currentReference;
				combineLS = new LineString();
				combineLS.setPoints((ArrayList<Point>) boundary.getGeometry2D().getPoints().clone()); 
			}
			
			if(boundary.getBoundaryType() == BoundaryType.Door) { // 문이면 문에 대한 boundary를 따로 생성한다.
				Polygon geometry3D = new Polygon();
				geometry3D = createPolygonFrom2Points((ArrayList<Point>)boundary.getGeometry2D().getPoints().clone(), groundHeight);
				boundary.setGeometry3D(geometry3D);
				if(!xLink3DMap.containsKey(boundary.getGeometry2D())) {
					xLink3DMap.put(boundary.getGeometry2D(), geometry3D);
				}
			}
		}
		
		int size = cellSpaceBoundaryMember.size();
		if(size  > 0 && cellSpaceBoundaryMember.get(size - 1).getBoundaryType() != BoundaryType.Door) {
			Polygon geometry3D = null;
			for(LineString existingLS : xLink3DMap.keySet()) {
				if(existingLS.getPoints().size() == combineLS.getPoints().size() && existingLS.getPoints().containsAll(combineLS.getPoints())) {
					geometry3D = new Polygon();
					geometry3D.setxLinkGeometry(xLink3DMap.get(existingLS));
					geometry3D.setIsReversed(false);
					break;
				}
			}
			if(geometry3D == null) {
				geometry3D = createPolygonFrom2Points(combineLS.getPoints(), ceilingHeight);
				xLink3DMap.put(combineLS, geometry3D);
			}
			
			CellSpaceBoundary boundary = null;
			if(combineLS.getPoints().size() > 2) {
				boundary = new CellSpaceBoundary();
				boundary.setBoundaryType(BoundaryType.Boundary3D);
				newBoundaryList.add(boundary);
				
				for(CellSpace reference : prevReference) {
					reference.getPartialBoundedBy().add(boundary);
				}
                                boundaryOfReferenceCellSpaceMap.put(boundary, (ArrayList<CellSpace>) prevReference.clone());
			} else {
				boundary = cellSpaceBoundaryMember.get(cellSpaceBoundaryMember.size() - 1);
			}
			boundary.setGeometry3D(geometry3D);
		}
		
		if(newBoundaryList.size() > 0) {
			cellSpaceBoundaryMember.addAll(newBoundaryList);
		}
		
		prevReference = currentReference;
	}
	
	public void setDoorHeight(CellSpaceBoundary boundary, double defaultDoorHeight) {
	        LineString ls = boundary.getGeometry2D();
	        double doorHeight = defaultDoorHeight;
	        
	        if(!boundary.getIsDefaultDoor()) {
	                doorHeight = boundary.getDoorHeight();
	        }
	        
	        for(Point p : ls.getPoints()) {
	                p.setZ(doorHeight);
	        }
	}
		
	public void fromCellSpaceOnFloor(CellSpaceOnFloor cellSpaceOnFloor, CellSpaceBoundaryOnFloor cellSpaceBoundaryOnFloor) {
		ArrayList<CellSpace> cellSpaceMember = cellSpaceOnFloor.getCellSpaceMember();
		for(CellSpace cellSpace : cellSpaceMember) {
		        if(cellSpace.getGmlID().equalsIgnoreCase("C1"))
		            System.out.println("C1 found");
			Solid solid = createSolidForCellSpace(cellSpace, cellSpaceOnFloor.getFloorProperty());
			cellSpace.setGeometry3D(solid);
		}
	}
	
	public Solid createSolidForCellSpace(CellSpace cellSpace, FloorProperty floorProperty) {
		Solid solid = new Solid();
		
		Shell exteriorShell = createShellFromPolygon(cellSpace, floorProperty);
		solid.setExteriorShell(exteriorShell);
		
		return solid;		
	}
	
	public Shell createShellFromPolygon(CellSpace cellSpace, FloorProperty floorProperty) {
		// CellSpace의 LineStringElements를 extrude시켜서 옆면을 만든다.
		// linestring에 인접한 boundary 중 동일한 기하가 있을 경우 xlink 정보를 만든다.
		// 윗면, 아랫면은 exteriorRing의 points에 높이를 바닥과 천장 높이를 적용시킨다.
		// 옆면은 CellSpcaeBoundary의 기하를 이용한다. 2D Geometry에서 xlink로 되어있는 boundary를 위해 Map에 정보를 저장해서 참조하도록 한다.
			
		double groundHeight = floorProperty.getGroundHeight();
		double doorHeight = floorProperty.getDoorHeight();
        double ceilingHeight = floorProperty.getCeilingHeight();
        
		//if(!cellSpace.getIsDefaultCeiling()) {
		//        ceilingHeight = cellSpace.getCeilingHeight();
		//}
		
		
		Polygon originPolygon = cellSpace.getGeometry2D();
		Polygon counterClockwisedPolygon = GeometryUtil.getCouterClockwisedPolygon(originPolygon);
		ArrayList<Point> exteriorPoints = counterClockwisedPolygon.getExteriorRing().getPoints();
		ArrayList<LineString> lineStrings = new ArrayList<LineString>();
		for (int i = 0; i < exteriorPoints.size() - 1; i++) {
		    LineString newLS = new LineString();
		    newLS.getPoints().add(exteriorPoints.get(i).clone());
		    newLS.getPoints().add(exteriorPoints.get((i + 1) % exteriorPoints.size()).clone());
		    lineStrings.add(newLS);
		}
		cellSpace.setLineStringElements(lineStrings);
		
		
		Shell shell = new Shell();
		ArrayList<Polygon> surfaceMember = new ArrayList<Polygon>();
		ArrayList<LineString> lineStringElements = cellSpace.getLineStringElements();

		// upper side
                ArrayList<Point> points = cellSpace.getGeometry2D().getExteriorRing().getPoints();
                ArrayList<Point> upperPointList = new ArrayList<Point>();
                for(int i = 0; i < points.size(); i++) {
                        Point point = points.get(i).clone();
                        point.setZ(ceilingHeight);
                        
                        upperPointList.add(point);
                }
                LinearRing upperRing = new LinearRing();
                upperRing.setPoints(upperPointList);
                Polygon upperSurface = new Polygon();
                upperSurface.setExteriorRing(upperRing);
                surfaceMember.add(upperSurface);
                
                ArrayList<Point> originPointList = cellSpace.getGeometry2D().getExteriorRing().getPoints();
                for(int i = 0; i < originPointList.size() - 1; i++) {
                        ArrayList<Point> sidePointList = new ArrayList<Point>();
                        Point p1 = originPointList.get(i).clone();
                        p1.setZ(groundHeight);
                        Point p2 = originPointList.get(i+1).clone();
                        p2.setZ(groundHeight);
                        Point p3 = upperPointList.get(i+1).clone();
                        p3.setZ(ceilingHeight);
                        Point p4 = upperPointList.get(i).clone();
                        p4.setZ(ceilingHeight);/*
                        panel.setPanelRatioXY(p1);
                        panel.setPanelRatioXY(p2);
                        panel.setPanelRatioXY(p3);
                        panel.setPanelRatioXY(p4);*/
                        
                        sidePointList.add(p1);
                        sidePointList.add(p2);
                        sidePointList.add(p3);
                        sidePointList.add(p4);
                        sidePointList.add(p1);
                        
                        LinearRing sideRing = new LinearRing();
                        sideRing.setPoints(sidePointList);
                        Polygon sideSurface = new Polygon();
                        sideSurface.setExteriorRing(sideRing);
                        surfaceMember.add(sideSurface);
                }
                
                // lower side
                ArrayList<Point> lowerPointList = new ArrayList<Point>();
                for(int i = points.size() - 1; i >= 0; i--) {
                        Point point = points.get(i).clone();
                        point.setZ(groundHeight);
                        
                        lowerPointList.add(point);
                }
                LinearRing lowerRing = new LinearRing();
                lowerRing.setPoints(lowerPointList);
                Polygon lowerSurface = new Polygon();
                lowerSurface.setExteriorRing(lowerRing);
                surfaceMember.add(lowerSurface);
                
		
		// side facet : shell의 옆면
                /*
		LineString combineLS = null;
		for(int i = 0; i < lineStringElements.size(); i++) {
			// 3차원으로 extrude 할 때 옆면 외에 2D에는 없는 윗면, 아랫면의 기하도 추가로 만들어야함
		        Point checkP1 = lineStringElements.get(i).getPoints().get(0);
		        Point checkP2 = lineStringElements.get(i).getPoints().get(1);
		        panel.setPanelRatioXY(checkP1);
		        panel.setPanelRatioXY(checkP2);
		        
		        if(checkP1.getPanelRatioX() == checkP2.getPanelRatioX() && checkP1.getPanelRatioY() == checkP2.getPanelRatioY())
		            continue;
		        
			Polygon sideFacet = null;
			LineString line = lineStringElements.get(i);
			ArrayList<Point> geometryPoints = line.getPoints();
                        ArrayList<Point> pointList = new ArrayList<Point>();
                        for(int j = geometryPoints.size() - 1; i >= 0; i--) {
                                Point p = geometryPoints.get(i);
                                Point newPoint = p.clone();
                                if(newPoint.getZ() != doorHeight) {
                                        newPoint.setZ(groundHeight);
                                }
                                pointList.add(newPoint);
                        }
                        
                        sideFacet = createPolygonFrom2Points(pointList, ceilingHeight);
                        surfaceMember.add(sideFacet);
			*/
                        /********** 임시로 주석처리 
			if(combineLS == null) {
				combineLS = new LineString();
				for(Point p : geometry2D.getPoints()) {
					Point newPoint = p.clone();
					newPoint.setZ(groundHeight);
					combineLS.getPoints().add(newPoint);
				}
			}
			
			LineString nextLS = lineStringElements.get((i + 1) % lineStringElements.size());
			Point nextP2 = nextLS.getPoints().get(1);
			LineString tempLS = new LineString();
			Point p1 = geometry2D.getPoints().get(0);
			Point p2 = geometry2D.getPoints().get(1);
			tempLS.getPoints().clear();
			tempLS.getPoints().add(p1);
			tempLS.getPoints().add(nextP2);
			
			if(i < (lineStringElements.size() - 1) && GeometryUtil.isContainsLineString(tempLS, geometry2D)
					&& GeometryUtil.isContainsLineString(tempLS, nextLS)) {
				for(Point p : nextLS.getPoints()) {
					Point newPoint = p.clone();
					newPoint.setZ(groundHeight);
					combineLS.getPoints().add(newPoint);
				}
			} else {
				if(combineLS.getPoints().size() > 2) {
					for(LineString combine : combineLineStrings) {
						if(GeometryUtil.isEqualsLineString(combine, combineLS)) {
							combineLS.setxLinkGeometry(combine);
							combineLS.setIsReversed(true);
						} else if(GeometryUtil.isEqualsIgnoreReverseLineString(combine, combineLS)) {
							combineLS.setxLinkGeometry(combine);
							combineLS.setIsReversed(false);
						} else {
							combineLineStrings.add(combineLS);
						}
					}
					geometry2D = combineLS;
				}				
				combineLS = null;
				
				if(geometry2D.getxLinkGeometry() != null) {
					Polygon xLinkPolygon = null;
					if(xLink3DMap.containsKey(geometry2D.getxLinkGeometry())) {
						xLinkPolygon = xLink3DMap.get(geometry2D.getxLinkGeometry());
					} else {
						// xlink에 해당되는 3D geometry가 map에 없을 경우 먼저 polygon을 생성하여 저장하고 xlink를 걸어준다
						ArrayList<Point> xLinkGeometryPoints = ((LineString) geometry2D.getxLinkGeometry()).getPoints();					
						ArrayList<Point> pointList = new ArrayList<Point>();
						for(Point p : xLinkGeometryPoints) {
							Point newPoint = p.clone();
							newPoint.setZ(groundHeight);
							pointList.add(newPoint);
						}
						
						xLinkPolygon = createPolygonFrom2Points(pointList, ceilingHeight);
						xLink3DMap.put((LineString) geometry2D.getxLinkGeometry(), xLinkPolygon);
					}
					sideFacet = new Polygon();
					sideFacet.setxLinkGeometry(xLinkPolygon);
					sideFacet.setIsReversed(geometry2D.getIsReversed()); // 방향은 geometry2D에 있는 방향을 가져온다.
				} else {
					if(xLink3DMap.containsKey(geometry2D)) { // 이미 만들어져 있는 경우
						sideFacet = xLink3DMap.get(geometry2D); // shell의 옆면을 map에서 가져온다.
					} else {
						ArrayList<Point> geometryPoints = geometry2D.getPoints();
						ArrayList<Point> pointList = new ArrayList<Point>();
						for(Point p : geometryPoints) {
							Point newPoint = p.clone();
							if(newPoint.getZ() != doorHeight) {
								newPoint.setZ(groundHeight);
							}
							pointList.add(newPoint);
						}
						
						sideFacet = createPolygonFrom2Points(pointList, ceilingHeight); // map에 없는 경우 옆면 생성, map에 추가
						xLink3DMap.put((LineString) geometry2D, sideFacet); // CellSpaceBoundary의 geometry3D는 xlink
					}
				}
				surfaceMember.add(sideFacet);
			}
			임시로 추석 처리*/
			
			/*
			Point p1 = geometry2D.getPoints().get(0);
			Point p2 = geometry2D.getPoints().get(1);
			if(p1.getZ() == doorHeight && p2.getZ() == doorHeight) { // Door
				LineString combineLS = new LineString();
				combineLS.setPoints((ArrayList<Point>) geometry2D.getPoints().clone());
				LineString tempLS = new LineString();

				int prevIdx;
				if(i == 0) prevIdx = lineStringElements.size() - 1;
				else prevIdx = i - 1;
				for(int j = prevIdx; j >= 0; j--) {
					// 한쪽 벽에 문이 여러개 있는 경우 문에 대한 기하가 뚫려 있는 하나의 기하로 만들어야 한다.
					// LineString이 하나 이상 합쳐 질 경우 벽-문-벽-문이나 문-벽-문의 경우이다.
					LineString prevLS = lineStringElements.get(j);
					Point prevP1 = prevLS.getPoints().get(0);
					tempLS.getPoints().clear();
					tempLS.getPoints().add(prevP1);
					tempLS.getPoints().add(p2);
					if(GeometryUtil.isContainsLineString(tempLS, geometry2D) && GeometryUtil.isContainsLineString(tempLS, prevLS)) {
						if(xLink3DMap.containsKey(prevLS)) {
							surfaceMember.remove(xLink3DMap.get(prevLS));
							xLink3DMap.remove(prevLS);
						}
						combineLS.getPoints().addAll(0, prevLS.getPoints());
					} else {
						break;
					}
					
					if(j == 0) prevIdx = lineStringElements.size() - 1;
				}
				
				int nextIdx = (i + 1) % lineStringElements.size();
				int count = 0;
				for(int j = nextIdx; j < lineStringElements.size(); j++) {
					LineString nextLS = lineStringElements.get(j);
					Point nextP2 = nextLS.getPoints().get(1);
					tempLS.getPoints().clear();
					tempLS.getPoints().add(p1);
					tempLS.getPoints().add(nextP2);
					if(GeometryUtil.isContainsLineString(tempLS, geometry2D) && GeometryUtil.isContainsLineString(tempLS, nextLS)) {
						combineLS.getPoints().addAll(nextLS.getPoints());
						count++;
					} else {
						break;
					}
					if(j == lineStringElements.size() - 1) j = 0;
				}
				i = i + count;
				
				if(combineLS.getPoints().size() > 2) {
					for(LineString combine : combineLineStrings) {
						if(GeometryUtil.isEqualsLineString(combine, combineLS)) {
							combineLS.setxLinkGeometry(combine);
							combineLS.setIsReversed(true);
						} else if(GeometryUtil.isEqualsIgnoreReverseLineString(combine, combineLS)) {
							combineLS.setxLinkGeometry(combine);
							combineLS.setIsReversed(false);
						} else {
							combineLineStrings.add(combineLS);
						}
					}
					geometry2D = combineLS;
				}
				
			}
			*/
			
		//}
		/*
		// side facet
		for(int i = 0; i < points.size(); i++) {
			Point p1 = points.get(i).clone();
			Point p2 = points.get((i + 1)).clone();
			p1.setZ(groundHeight);
			p2.setZ(groundHeight);

			pointList = new ArrayList<Point>();
			pointList.add(p1);
			pointList.add(p2);
			
			Polygon surface = createPolygonFrom2Points(pointList, ceilingHeight);
			surfaceMember.add(surface);
		}
		*/
		/*// upper side
		ArrayList<Point> points = cellSpace.getGeometry2D().getExteriorRing().getPoints();
		ArrayList<Point> pointList = new ArrayList<Point>();
		for(int i = 0; i < points.size(); i++) {
			Point point = points.get(i).clone();
			point.setZ(ceilingHeight);
			
			pointList.add(point);
		}
		LinearRing upperRing = new LinearRing();
		upperRing.setPoints(pointList);
		Polygon upperSurface = new Polygon();
		upperSurface.setExteriorRing(upperRing);
		surfaceMember.add(upperSurface);
		
		// lower side
		pointList = new ArrayList<Point>();
		for(int i = points.size() - 1; i >= 0; i--) {
			Point point = points.get(i).clone();
			point.setZ(groundHeight);
			
			pointList.add(point);
		}
		LinearRing lowerRing = new LinearRing();
		lowerRing.setPoints(pointList);
		Polygon lowerSurface = new Polygon();
		lowerSurface.setExteriorRing(lowerRing);
		surfaceMember.add(lowerSurface);*/
		
		shell.setSurfaceMember(surfaceMember);
		
		return shell;		
	}
	
	public Polygon createPolygonFrom2Points(ArrayList<Point> points, double ceilingHeight) {
		int size = points.size();
		Point p2 = points.get(size - 1).clone();
		p2.setZ(ceilingHeight);
		Point p1 = points.get(0).clone();
		p1.setZ(ceilingHeight);

		points.add(p2);
		points.add(p1);
		points.add(points.get(0).clone());
		
		LinearRing linearRing = new LinearRing();
		linearRing.setPoints(points);
		Polygon polygon = new Polygon();
		polygon.setExteriorRing(linearRing);
		
		return polygon;		
	}
}
