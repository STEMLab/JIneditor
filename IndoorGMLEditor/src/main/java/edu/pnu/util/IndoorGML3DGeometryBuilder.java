package edu.pnu.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
	private HashMap<LineString, Polygon> xLink3DMap; // not used
	private ArrayList<LineString> combineLineStrings; // not used
	private CanvasPanel panel; // not used
	
	private Map<CellSpaceBoundary, CellSpaceBoundary> boundary3DMap;
	private Map<CellSpaceBoundary, CellSpaceBoundary> xLinkBoundaryMap;
		
	public IndoorGML3DGeometryBuilder(CanvasPanel panel, IndoorFeatures indoorFeatures, Map<CellSpaceBoundary, CellSpaceBoundary> xLinkBoundaryMap) {
		this.panel = panel;
		this.indoorFeatures = indoorFeatures;
		
		xLink3DMap = new HashMap<LineString, Polygon>();
		combineLineStrings = new ArrayList<LineString>();
		
		boundary3DMap = new HashMap<CellSpaceBoundary, CellSpaceBoundary>();
		this.xLinkBoundaryMap = xLinkBoundaryMap;
	}

	public void create3DGeometry() {
		PrimalSpaceFeatures primalSpaceFeatures = indoorFeatures.getPrimalSpaceFeatures();
		
		ArrayList<CellSpaceOnFloor> cellSpaceOnFloors = primalSpaceFeatures.getCellSpaceOnFloors();
		ArrayList<CellSpaceBoundaryOnFloor> cellSpaceBoundaryOnFloors = primalSpaceFeatures.getCellSpaceBoundaryOnFloors();
		for(int i = 0; i < cellSpaceOnFloors.size(); i++) {
			fromCellSpaceOnFloor(cellSpaceOnFloors.get(i), cellSpaceBoundaryOnFloors.get(i));
			create3DBoundaryFromCellSpace(cellSpaceOnFloors.get(i), cellSpaceBoundaryOnFloors.get(i));
		}
		/*
		for(int i = 0; i < cellSpaceBoundaryOnFloors.size(); i++) {			
			fromCellSpaceBoundaryOnFloor(cellSpaceBoundaryOnFloors.get(i));
		}
		*/
	}
	
	public void create3DBoundaryFromCellSpace(CellSpaceOnFloor cellSpaceOnFloor, CellSpaceBoundaryOnFloor cellSpaceBoundaryOnFloor) {
		double groundHeight = cellSpaceBoundaryOnFloor.getFloorProperty().getGroundHeight();
		double defaultCeilingHeight = cellSpaceBoundaryOnFloor.getFloorProperty().getCeilingHeight();
		double defaultDoorHeight = cellSpaceBoundaryOnFloor.getFloorProperty().getDoorHeight();
		
		ArrayList<CellSpace> cellSpaceMember = cellSpaceOnFloor.getCellSpaceMember();
		ArrayList<CellSpaceBoundary> cellSpaceBoundaryMember = cellSpaceBoundaryOnFloor.getCellSpaceBoundaryMember();
		HashMap<LineString, ArrayList<CellSpaceBoundary>> lineStringBoundaryMap = cellSpaceBoundaryOnFloor.getLineStringOfAdjacencyBoundaryMap();
				        
        for (CellSpace cellSpace : cellSpaceMember) {
        	ArrayList<CellSpaceBoundary> removed = new ArrayList<CellSpaceBoundary>();
        	for (CellSpaceBoundary bounded : cellSpace.getPartialBoundedBy()) {
        		if (bounded.getBoundaryType() == BoundaryType.Boundary3D) {
	        		removed.add(bounded);
	        		cellSpaceBoundaryMember.remove(bounded);
        		} else if (bounded.getGeometry3D() != null) {
        			bounded.setGeometry3D(null);
        		}
        	}
        	cellSpace.getPartialBoundedBy().removeAll(removed);
        }
        
        for (CellSpace cellSpace : cellSpaceMember) {
        	ArrayList<CellSpaceBoundary> generated3DBoundary = new ArrayList<CellSpaceBoundary>();
        	for (LineString ls : cellSpace.getLineStringElements()) {
        		if (!lineStringBoundaryMap.containsKey(ls)) {
        			continue;
        		}
        		
    			ArrayList<CellSpaceBoundary> adjacency = lineStringBoundaryMap.get(ls);
    			int remain = adjacency.size();
    			
    			while (remain > 0) {
    				ArrayList<Point> combinePoints = new ArrayList<Point>();
            		ArrayList<CellSpaceBoundary> refinement = new ArrayList<CellSpaceBoundary>();
            		ArrayList<CellSpaceBoundary> ignore = new ArrayList<CellSpaceBoundary>();
            		
    				for (int i = 0; i < adjacency.size(); i++) {
    					CellSpaceBoundary boundary = adjacency.get(i);
    					if (boundary3DMap.containsKey(boundary)) {
    						continue;
    					}
    					
						LineString geometry2D = boundary.getGeometry2D();						
						if (geometry2D.getxLinkGeometry() != null) {
							if (!ignore.contains(boundary)) {
								ignore.add(boundary);
								remain--;
							}
							continue;
						}
						
						if (boundary.getBoundaryType() == BoundaryType.Door && boundary.getGeometry3D() == null) {
	    					boundary.getGeometry2D().getPoints().get(0).setZ(groundHeight);
	    					boundary.getGeometry2D().getPoints().get(1).setZ(groundHeight);
	    					
	            			Polygon geometry3D = new Polygon();
	            			ArrayList<Point> geometry3DPoints = new ArrayList<Point>();
	            			geometry3DPoints.add(boundary.getGeometry2D().getPoints().get(0).clone());
	            			geometry3DPoints.add(boundary.getGeometry2D().getPoints().get(1).clone());
	        				geometry3D = createPolygonFrom2Points(geometry3DPoints, groundHeight + defaultDoorHeight);
	        				boundary.setGeometry3D(geometry3D);
	        				
	        				remain--;
	        				
	        				boundary3DMap.put(boundary, boundary);
	            		}
	        			
	        			//
	        			if (combinePoints.size() == 0) {
	        				if (boundary.getBoundaryType() == BoundaryType.Door) {
	        					geometry2D.getPoints().get(0).setZ(groundHeight);
	        					geometry2D.getPoints().get(1).setZ(groundHeight);
	        					
	        					ArrayList<Point> clone = new ArrayList<Point>();
	        					clone.add(geometry2D.getPoints().get(0).clone());
	        					clone.add(geometry2D.getPoints().get(1).clone());
	        					
	        					Point cloneLast = clone.get(1).clone();
	        					clone.get(0).setZ(groundHeight + defaultDoorHeight);
	        					clone.get(1).setZ(groundHeight + defaultDoorHeight);
	        					cloneLast.setZ(groundHeight);
	        					clone.add(cloneLast);
	        					        					
	        					combinePoints.addAll(clone);
	        				} else {
	        					geometry2D.getPoints().get(0).setZ(groundHeight);
	        					geometry2D.getPoints().get(1).setZ(groundHeight);
	        					
	            				combinePoints.addAll((ArrayList<Point>) geometry2D.getPoints().clone());
	            				refinement.add(boundary);
	        				}
	        			} else {        			
		        			Point lastPoint = combinePoints.get(combinePoints.size() - 1);
		        			Point startPoint = geometry2D.getPoints().get(0);
		        			if (lastPoint.equalsPanelRatioXY(startPoint)) { // 끝점 같은것과 직선인지 검사해야함
		        				if (boundary.getBoundaryType() == BoundaryType.Door) {
		        					geometry2D.getPoints().get(0).setZ(groundHeight);
		        					geometry2D.getPoints().get(1).setZ(groundHeight);
		        					
		        					ArrayList<Point> clone = new ArrayList<Point>();
		        					clone.add(geometry2D.getPoints().get(0).clone());
		        					clone.add(geometry2D.getPoints().get(1).clone());
		        					
		        					Point cloneLast = clone.get(1).clone();
		        					clone.get(0).setZ(groundHeight + defaultDoorHeight);
		        					clone.get(1).setZ(groundHeight + defaultDoorHeight);
		        					cloneLast.setZ(groundHeight);
		        					clone.add(cloneLast);
		        					        					
		        					combinePoints.addAll(clone);
		        				} else {
		        					geometry2D.getPoints().get(0).setZ(groundHeight);
		        					geometry2D.getPoints().get(1).setZ(groundHeight);
		        					
		        					combinePoints.add(geometry2D.getPoints().get(1).clone());
		        					refinement.add(boundary);
		        				}
		        			}
	        			}
    				}
	        			
        			if (refinement.size() > 0) {
    	        		CellSpaceBoundary newBoundary = new CellSpaceBoundary();
    	        		Polygon geometry3D = createPolygonFrom2Points(combinePoints, defaultCeilingHeight);
    					newBoundary.setBoundaryType(BoundaryType.Boundary3D);
    					newBoundary.setGeometry3D(geometry3D);
    					
    					for (CellSpaceBoundary target : refinement) {
    						boundary3DMap.put(target, newBoundary);
    						remain--;
    					}
    					cellSpaceBoundaryMember.add(newBoundary);
    					generated3DBoundary.add(newBoundary);
    					
    					newBoundary.setDuality(refinement.get(0).getDuality());
            		}

    			}
    			
        	}
        	
        	if (!generated3DBoundary.isEmpty()) {
        		cellSpace.getPartialBoundedBy().addAll(generated3DBoundary);
        	}
        }
        
        // create CellSpaceBoundary has xlink geometry
        for (CellSpace cellSpace : cellSpaceMember) {
        	ArrayList<CellSpaceBoundary> partialBoundedBy = cellSpace.getPartialBoundedBy();
        	ArrayList<CellSpaceBoundary> ignore = new ArrayList<CellSpaceBoundary>();
        	ArrayList<CellSpaceBoundary> generated = new ArrayList<CellSpaceBoundary>();
        	
        	for (CellSpaceBoundary bounded : partialBoundedBy) {
        		if (bounded.getBoundaryType() == BoundaryType.Boundary3D) {
        			continue;
        		}
        		if (boundary3DMap.containsKey(bounded)) {
        			continue;
        		}
        		
        		LineString geometry2D = bounded.getGeometry2D();
        		if (geometry2D.getxLinkGeometry() != null) {
        			CellSpaceBoundary mirrorBoundary = xLinkBoundaryMap.get(bounded);
        			CellSpaceBoundary mirrorBoundary3D = boundary3DMap.get(mirrorBoundary);
        			if (ignore.contains(mirrorBoundary3D)) {
        				continue;
        			} else {
        				ignore.add(mirrorBoundary3D);
        			}
        			
        			CellSpaceBoundary newBoundary = new CellSpaceBoundary();
	        		Polygon geometry3D = new Polygon();
	        		geometry3D.setxLinkGeometry(mirrorBoundary3D.getGeometry3D());
	        		geometry3D.setIsReversed(geometry2D.getIsReversed());
	        		
					newBoundary.setBoundaryType(BoundaryType.Boundary3D);
					newBoundary.setGeometry3D(geometry3D);
					
					boundary3DMap.put(bounded, newBoundary);
					cellSpaceBoundaryMember.add(newBoundary);
					generated.add(newBoundary);
					
					newBoundary.setDuality(bounded.getDuality());
        		}
        	}
        	if (!generated.isEmpty()) {
        		cellSpace.getPartialBoundedBy().addAll(generated);
        	}
        }
	}
	
	public void create3DBoundaryFromCellSpaceByOldModel(CellSpaceOnFloor cellSpaceOnFloor, CellSpaceBoundaryOnFloor cellSpaceBoundaryOnFloor) {
		double groundHeight = cellSpaceBoundaryOnFloor.getFloorProperty().getGroundHeight();
		double ceilingHeight = cellSpaceBoundaryOnFloor.getFloorProperty().getCeilingHeight();
		double defaultDoorHeight = cellSpaceBoundaryOnFloor.getFloorProperty().getDoorHeight();
		
		ArrayList<CellSpace> cellSpaceMember = cellSpaceOnFloor.getCellSpaceMember();
		ArrayList<CellSpaceBoundary> cellSpaceBoundaryMember = cellSpaceBoundaryOnFloor.getCellSpaceBoundaryMember();
		HashMap<CellSpaceBoundary, ArrayList<CellSpace>> boundaryOfReferenceCellSpaceMap = cellSpaceBoundaryOnFloor.getBoundaryOfReferenceCellSpaceMap();
		HashMap<CellSpaceBoundary, CellSpaceBoundary> boundary3DReferenceMap = new HashMap<CellSpaceBoundary, CellSpaceBoundary>();
				        
        for (CellSpace cellSpace : cellSpaceMember) {
        	ArrayList<CellSpaceBoundary> removed = new ArrayList<CellSpaceBoundary>();
        	for (CellSpaceBoundary bounded : cellSpace.getPartialBoundedBy()) {
        		if (bounded.getBoundaryType() == BoundaryType.Boundary3D) {
	        		removed.add(bounded);
	        		cellSpaceBoundaryMember.remove(bounded);
        		} else if (bounded.getGeometry3D() != null) {
        			bounded.setGeometry3D(null);
        		}
        	}
        	cellSpace.getPartialBoundedBy().removeAll(removed);
        }
        
        for (CellSpace cellSpace : cellSpaceMember) {        	
        	ArrayList<CellSpaceBoundary> boundedBy = cellSpace.getPartialBoundedBy();
        	ArrayList<CellSpaceBoundary> generated3DBoundary = new ArrayList<CellSpaceBoundary>();
        	int remain = boundedBy.size();

    		ArrayList<CellSpace> ignore = new ArrayList<CellSpace>();
        	while (remain > 0) {
        		ArrayList<CellSpaceBoundary> combineBoundary = new ArrayList<CellSpaceBoundary>();
        		CellSpace targetCell = null;
        		for (CellSpaceBoundary bounded : boundedBy) {        			
            		if (!boundary3DReferenceMap.containsKey(bounded)) {
            			ArrayList<CellSpace> referenceCellSpaceList = boundaryOfReferenceCellSpaceMap.get(bounded);
            			CellSpace candidate = null;
            			// c1과 c2의 동일한 벽에 위치한 boundary들을 모은다.
            			try {
	            			if (referenceCellSpaceList.size() >= 2) {
		            			for (CellSpace cell : referenceCellSpaceList) {
		            				if (!cell.equals(cellSpace)) {
		            					candidate = cell;
		            					break;
		            				}
		            			}
		            			
		            			if (!ignore.contains(candidate)) {
			            			if (targetCell == null) {
			            				targetCell = candidate;
			                			combineBoundary.add(bounded);
			            			} else if (targetCell.equals(candidate)) {
			                			combineBoundary.add(bounded);
			            			}
		            			}
	            			}
            			} catch (Exception e) {
            				System.out.println("exception");
            			}
            		} else {
            			// 이미 다른 cell에서 bounded의 3D boundary가 생성되어 있다면 그것을 추가한다.
            			CellSpaceBoundary boundary3D = boundary3DReferenceMap.get(bounded);
            			if (!generated3DBoundary.contains(boundary3D)) {
            				generated3DBoundary.add(boundary3D);
            			}
            			remain--;
            		}
            		
            		// 문이면 별도로 생성한다.
            		if (bounded.getBoundaryType() == BoundaryType.Door && bounded.getGeometry3D() == null) {
    					bounded.getGeometry2D().getPoints().get(0).setZ(groundHeight);
    					bounded.getGeometry2D().getPoints().get(1).setZ(groundHeight);
    					
            			Polygon geometry3D = new Polygon();
            			ArrayList<Point> geometry3DPoints = new ArrayList<Point>();
            			geometry3DPoints.add(bounded.getGeometry2D().getPoints().get(0).clone());
            			geometry3DPoints.add(bounded.getGeometry2D().getPoints().get(1).clone());
        				geometry3D = createPolygonFrom2Points(geometry3DPoints, groundHeight + defaultDoorHeight);
        				bounded.setGeometry3D(geometry3D);
        				
        				remain--;
            		} 
            	}
        		
        		// 하나의 벽으로 합칠 수 있는 boundary들의 점을 찾는다.
        		ArrayList<Point> combinePoints = new ArrayList<Point>();
        		ArrayList<CellSpaceBoundary> refinement = new ArrayList<CellSpaceBoundary>();
        		for (int i = 0; i < combineBoundary.size(); i++) {
        			CellSpaceBoundary target = combineBoundary.get(i);
        			LineString geometry2D = target.getGeometry2D();
        			
        			// 
        			if (combinePoints.size() == 0) {
        				if (target.getBoundaryType() == BoundaryType.Door) {
        					geometry2D.getPoints().get(0).setZ(groundHeight);
        					geometry2D.getPoints().get(1).setZ(groundHeight);
        					
        					ArrayList<Point> clone = new ArrayList<Point>();
        					clone.add(geometry2D.getPoints().get(0).clone());
        					clone.add(geometry2D.getPoints().get(1).clone());
        					
        					Point cloneLast = clone.get(1).clone();
        					clone.get(0).setZ(groundHeight + defaultDoorHeight);
        					clone.get(1).setZ(groundHeight + defaultDoorHeight);
        					cloneLast.setZ(groundHeight);
        					clone.add(cloneLast);
        					        					
        					combinePoints.addAll(clone);
        				} else {
        					geometry2D.getPoints().get(0).setZ(groundHeight);
        					geometry2D.getPoints().get(1).setZ(groundHeight);
        					
            				combinePoints.addAll((ArrayList<Point>) geometry2D.getPoints().clone());
            				refinement.add(target);
        				}
        			} else {        			
	        			Point lastPoint = combinePoints.get(combinePoints.size() - 1);
	        			Point startPoint = geometry2D.getPoints().get(0);
	        			if (lastPoint.equalsPanelRatioXY(startPoint)) { // 끝점 같은것과 직선인지 검사해야함
	        				if (target.getBoundaryType() == BoundaryType.Door) {
	        					geometry2D.getPoints().get(0).setZ(groundHeight);
	        					geometry2D.getPoints().get(1).setZ(groundHeight);
	        					
	        					ArrayList<Point> clone = new ArrayList<Point>();
	        					clone.add(geometry2D.getPoints().get(0).clone());
	        					clone.add(geometry2D.getPoints().get(1).clone());
	        					
	        					Point cloneLast = clone.get(1).clone();
	        					clone.get(0).setZ(groundHeight + defaultDoorHeight);
	        					clone.get(1).setZ(groundHeight + defaultDoorHeight);
	        					cloneLast.setZ(groundHeight);
	        					clone.add(cloneLast);
	        					        					
	        					combinePoints.addAll(clone);
	        				} else {
	        					geometry2D.getPoints().get(0).setZ(groundHeight);
	        					geometry2D.getPoints().get(1).setZ(groundHeight);
	        					
	        					combinePoints.add(geometry2D.getPoints().get(1).clone());
	        					refinement.add(target);
	        				}
	        			}
        			}
        		}
        		
        		if (refinement.size() > 0) {
	        		CellSpaceBoundary newBoundary = new CellSpaceBoundary();
	        		Polygon geometry3D = createPolygonFrom2Points(combinePoints, ceilingHeight);
					newBoundary.setBoundaryType(BoundaryType.Boundary3D);
					newBoundary.setGeometry3D(geometry3D);
					
					for (CellSpaceBoundary target : refinement) {
						boundary3DReferenceMap.put(target, newBoundary);
					}
					cellSpaceBoundaryMember.add(newBoundary);
					generated3DBoundary.add(newBoundary);
					
					// for joonseokkim
					newBoundary.setDuality(refinement.get(0).getDuality());
    				boundary3DMap.put(refinement.get(0), newBoundary);
					//
					
					remain--;
        		} else {
        			if (targetCell != null && !ignore.contains(targetCell)) {
            			ignore.add(targetCell);
        			}
        		}
        	}

        	if (!generated3DBoundary.isEmpty()) {
        		cellSpace.getPartialBoundedBy().addAll(generated3DBoundary);
        	}
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
	        
	        if(!boundary.getIsDefaultDoorHeight()) {
	                doorHeight = boundary.getDoorHeight();
	        }
	        
	        for(Point p : ls.getPoints()) {
	                p.setZ(doorHeight);
	        }
	}
		
	public void fromCellSpaceOnFloor(CellSpaceOnFloor cellSpaceOnFloor, CellSpaceBoundaryOnFloor cellSpaceBoundaryOnFloor) {
		ArrayList<CellSpace> cellSpaceMember = cellSpaceOnFloor.getCellSpaceMember();
		for(CellSpace cellSpace : cellSpaceMember) {
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
        if (!cellSpace.getIsDefaultCeiling()) {//if (cellSpace.getDescription("Usage").equals("Door")) {
        	ceilingHeight = cellSpace.getCeilingHeight();
        	//ceilingHeight = groundHeight + doorHeight;
        }
        
		Polygon originPolygon = cellSpace.getGeometry2D();
		LineString exteriorRing = originPolygon.getExteriorRing();
		ArrayList<Point> points = null;
		com.vividsolutions.jts.geom.LineString jtsLine = JTSUtil.convertJTSLineString(exteriorRing);
		double isCounterClockwise = JTSUtil.Orientation2D_Polygon(jtsLine.getNumPoints(), jtsLine.getCoordinateSequence());
		if (cellSpace.getGmlID().equals("C107")) {
			System.out.println("C107found");
		}
		if (cellSpace.getGmlID().equals("C1130")) {
			System.out.println("C1130found");
		}
		if (isCounterClockwise > 0) {			
			points = new ArrayList<Point>();
			ArrayList<Point> originPoints = exteriorRing.getPoints();
			for (int i = originPoints.size() - 1; i >= 0; i--) {
				points.add(originPoints.get(i));
			}
		} else {
			points = exteriorRing.getPoints();
		}
		
		Shell shell = new Shell();
		ArrayList<Polygon> surfaceMember = new ArrayList<Polygon>();
		//ArrayList<LineString> lineStringElements = cellSpace.getLineStringElements();

		// upper side
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
        
        for(int i = 0; i < points.size() - 1; i++) {
            ArrayList<Point> sidePointList = new ArrayList<Point>();
            Point p1 = points.get(i).clone();
            p1.setZ(groundHeight);
            Point p2 = points.get(i+1).clone();
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
	
	public Map<CellSpaceBoundary, CellSpaceBoundary> getBoundary3DMap() {
		return boundary3DMap;
	}
}
