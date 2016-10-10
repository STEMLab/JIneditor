package edu.pnu.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import net.opengis.indoorgml.core.CellSpace;
import net.opengis.indoorgml.core.CellSpaceBoundary;
import net.opengis.indoorgml.core.CellSpaceBoundaryOnFloor;
import net.opengis.indoorgml.core.CellSpaceOnFloor;
import net.opengis.indoorgml.core.Edges;
import net.opengis.indoorgml.core.IndoorFeatures;
import net.opengis.indoorgml.core.InterEdges;
import net.opengis.indoorgml.core.InterLayerConnection;
import net.opengis.indoorgml.core.MultiLayeredGraph;
import net.opengis.indoorgml.core.Nodes;
import net.opengis.indoorgml.core.PrimalSpaceFeatures;
import net.opengis.indoorgml.core.SpaceLayer;
import net.opengis.indoorgml.core.SpaceLayers;
import net.opengis.indoorgml.core.State;
import net.opengis.indoorgml.core.Transition;
import net.opengis.indoorgml.geometry.LineString;
import net.opengis.indoorgml.geometry.LinearRing;
import net.opengis.indoorgml.geometry.Point;
import net.opengis.indoorgml.geometry.Polygon;
import net.opengis.indoorgml.geometry.Shell;
import net.opengis.indoorgml.geometry.Solid;

import org.geotools.geometry.jts.JTSFactoryFinder;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.util.AffineTransformation;
import com.vividsolutions.jts.geom.util.AffineTransformationBuilder;

import edu.pnu.project.StateOnFloor;
import edu.pnu.project.TransitionOnFloor;

public class IndoorCoordinateGenerator {
	private boolean is3DGeometry;
	private boolean isMLGHeight;
	private double floorGroundHeight;
	private Point lowerCornerReference;
	private Point upperCornerReference;
	private IndoorFeatures indoorFeatures;

	public IndoorCoordinateGenerator(IndoorFeatures indoorFeatures, boolean is3DGeometry) {
		this.indoorFeatures = indoorFeatures;
		this.is3DGeometry = is3DGeometry;
	}
	
	public void generate() {
		traverseIndoorFeatures(indoorFeatures);
	}

	public void traverseIndoorFeatures(IndoorFeatures indoorFeatures) {		
		isMLGHeight = false;
		traversePrimalSpaceFeatures(indoorFeatures.getPrimalSpaceFeatures());
		isMLGHeight = true;
		traverseMultiLayeredGraph(indoorFeatures.getMultiLayeredGraph());
	}

	public void traversePrimalSpaceFeatures(PrimalSpaceFeatures primalSpaceFeatures) {		
		ArrayList<CellSpaceOnFloor> cellSpaceOnFloors = primalSpaceFeatures.getCellSpaceOnFloors();
		for(CellSpaceOnFloor cellSpaceOnFloor : cellSpaceOnFloors) {
			traverseCellSpaceOnFloor(cellSpaceOnFloor);
		}
		
		ArrayList<CellSpaceBoundaryOnFloor> cellSpaceBoundaryOnFloors = primalSpaceFeatures.getCellSpaceBoundaryOnFloors();
		for(CellSpaceBoundaryOnFloor cellSpaceBoundaryOnFloor : cellSpaceBoundaryOnFloors) {
			traverseCellSpaceBoundaryOnFloor(cellSpaceBoundaryOnFloor);
		}
		
	}

	public void traverseCellSpaceOnFloor(CellSpaceOnFloor cellSpaceOnFloor) {		
		lowerCornerReference = cellSpaceOnFloor.getFloorProperty().getBottomLeftPoint();
		upperCornerReference = cellSpaceOnFloor.getFloorProperty().getTopRightPoint();
		floorGroundHeight = cellSpaceOnFloor.getFloorProperty().getGroundHeight();
		ArrayList<CellSpace> cellSpaceMember = cellSpaceOnFloor.getCellSpaceMember();
		for(CellSpace cellSpace : cellSpaceMember) {
			traverseCellSpace(cellSpace);
		}
	}

	public void traverseCellSpace(CellSpace cellSpace) {
		if(is3DGeometry) {
			generateSolidCoordinate(cellSpace.getGeometry3D());
		} else {
			generatePolygonCoordinate(cellSpace.getGeometry2D());
		}
	}
	
	public void traverseCellSpaceBoundaryOnFloor(CellSpaceBoundaryOnFloor cellSpaceBoundaryOnFloor) {
		lowerCornerReference = cellSpaceBoundaryOnFloor.getFloorProperty().getBottomLeftPoint();
		upperCornerReference = cellSpaceBoundaryOnFloor.getFloorProperty().getTopRightPoint();
		floorGroundHeight = cellSpaceBoundaryOnFloor.getFloorProperty().getGroundHeight();
		ArrayList<CellSpaceBoundary> cellSpaceBoundaryMember = cellSpaceBoundaryOnFloor.getCellSpaceBoundaryMember();
		for(CellSpaceBoundary cellSpaceBoundary : cellSpaceBoundaryMember) {
			traverseCellSpaceBoundary(cellSpaceBoundary);
		}
	}
	
	public void traverseCellSpaceBoundary(CellSpaceBoundary cellSpaceBoundary) {
		// TODO Auto-generated method stub
		if(is3DGeometry) {
			if(cellSpaceBoundary.getGeometry3D() == null) return;
			generatePolygonCoordinate(cellSpaceBoundary.getGeometry3D());
		} else {
			if(cellSpaceBoundary.getGeometry2D() == null) return;
			generateLineStringCoordinate(cellSpaceBoundary.getGeometry2D(), false);
		}
	}

	public void traverseMultiLayeredGraph(MultiLayeredGraph multiLayeredGraph) {		
		ArrayList<SpaceLayers> spaceLayersList = multiLayeredGraph.getSpaceLayers();
		for(SpaceLayers spaceLayers : spaceLayersList) {
			traverseSpaceLayers(spaceLayers);
		}
		
		ArrayList<InterEdges> interEdgesList = multiLayeredGraph.getInterEdges();
		for(InterEdges interEdges : interEdgesList) {
			traverseInterEdges(interEdges);
		}
	}

	public void traverseSpaceLayers(SpaceLayers spaceLayers) {		
		ArrayList<SpaceLayer> spaceLayerList = spaceLayers.getSpaceLayerMember();
		for(SpaceLayer spaceLayer : spaceLayerList) {
			traverseSpaceLayer(spaceLayer);
		}
		
	}

	public void traverseSpaceLayer(SpaceLayer spaceLayer) {
		ArrayList<Nodes> nodesList = spaceLayer.getNodes();
		for(Nodes nodes : nodesList) {
			traverseNodes(nodes);
		}
		
		ArrayList<Edges> edgesList = spaceLayer.getEdges();
		for(Edges edges : edgesList) {
			traverseEdges(edges);
		}
	}

	public void traverseNodes(Nodes nodes) {
		ArrayList<StateOnFloor> stateOnFloorList = nodes.getStateOnFloors();
		for(StateOnFloor stateOnFloor : stateOnFloorList) {
			traverseStateOnFloor(stateOnFloor);
		}
	}

	public void traverseStateOnFloor(StateOnFloor stateOnFloor) {
		lowerCornerReference = stateOnFloor.getFloorProperty().getBottomLeftPoint();
		upperCornerReference = stateOnFloor.getFloorProperty().getTopRightPoint();
		floorGroundHeight = stateOnFloor.getFloorProperty().getGroundHeight();
		
		ArrayList<State> stateList = stateOnFloor.getStateMember();
		for(State state : stateList) {
			traverseState(state);
		}
	}

	public void traverseState(State state) {
		generatePointCoordinate(state.getPosition());
	}

	public void traverseEdges(Edges edges) {		
		ArrayList<TransitionOnFloor> transitionOnFloorList = edges.getTransitionOnFloors();
		for(TransitionOnFloor transitionOnFloor : transitionOnFloorList) {
			traverseTransitionOnFloor(transitionOnFloor);
		}
	}

	public void traverseTransitionOnFloor(TransitionOnFloor transitionOnFloor) {		
		lowerCornerReference = transitionOnFloor.getFloorProperty().getBottomLeftPoint();
		upperCornerReference = transitionOnFloor.getFloorProperty().getTopRightPoint();
		floorGroundHeight = transitionOnFloor.getFloorProperty().getGroundHeight();

		ArrayList<Transition> transitionList = transitionOnFloor.getTransitionMember();
		for(Transition transition : transitionList) {
			traverseTransition(transition);
		}
	}

	public void traverseTransition(Transition transition) {
		
		generateLineStringCoordinate(transition.getPath(), true);
	}
	
	public void traverseInterEdges(InterEdges interEdges) {		
		ArrayList<InterLayerConnection> interLayerConnectionList = interEdges.getInterLayerConnectionMember();
		for(InterLayerConnection interLayerConnection : interLayerConnectionList) {
			traverseInterLayerConnection(interLayerConnection);
		}
	}

	public void traverseInterLayerConnection(InterLayerConnection interLayerConnection) {
	}

	public void generatePointCoordinate(Point point) {	        
        DecimalFormat format = new DecimalFormat(".##########");
        double realX = lowerCornerReference.getPanelX() + point.getPanelRatioX() * (upperCornerReference.getPanelX() - lowerCornerReference.getPanelX());
        double realY = lowerCornerReference.getPanelY() + (1 - point.getPanelRatioY()) * (upperCornerReference.getPanelY() - lowerCornerReference.getPanelY());
        String realXStr = format.format(realX);
        String realYStr = format.format(realY);
        
        realX = Double.parseDouble(realXStr);
        realY = Double.parseDouble(realYStr);
		point.setRealX(realX);
		point.setRealY(realY);
		//point.setY(lowerCornerReference.getY() + point.getPanelRatioY() * (upperCornerReference.getY() - lowerCornerReference.getY()));
		if(!is3DGeometry || isMLGHeight) {
			point.setZ(floorGroundHeight);
		}
	}
	
	public void generateLineStringCoordinate(LineString lineString, boolean isSetEndPoints) {
		//if(lineString.getxLinkGeometry() != null) return;
		if(lineString.getPoints().size() == 0) return;
				
		ArrayList<Point> points = lineString.getPoints();
		Point start = points.get(0);
		Point end = points.get(points.size() - 1);
		boolean isFloorConnection = false;
		double offset = Double.NaN;
		if (start.getZ() != end.getZ()) {
			isFloorConnection = true;
			offset = (end.getZ() - start.getZ()) / (points.size() - 1);
		}
		
		for(int i = 0; i < points.size(); i++) {
			if (isSetEndPoints) {
				if (i == 0 || i == points.size() - 1) continue;
			}
			Point point = points.get(i);
			
			generatePointCoordinate(point);
	                //point.setY(lowerCornerReference.getY() + point.getPanelRatioY() * (upperCornerReference.getY() - lowerCornerReference.getY()));
			
			if(!is3DGeometry || isMLGHeight) {
				if (!isFloorConnection) {
					point.setZ(floorGroundHeight);
				} else {
					double z = start.getZ() + (offset * i);
					point.setZ(z);
				}
			}
		}
	}
	
	public void generateLinearRingCoordinate(LinearRing linearRing) {
		ArrayList<Point> points = linearRing.getPoints();
		for(Point point : points) {
			generatePointCoordinate(point);
	                //point.setY(lowerCornerReference.getY() + point.getPanelRatioY() * (upperCornerReference.getY() - lowerCornerReference.getY()));
			if(!is3DGeometry || isMLGHeight) {
				point.setZ(floorGroundHeight);
			}
		}
	}

	public void generatePolygonCoordinate(Polygon polygon) {
		if(polygon.getxLinkGeometry() != null) return;
		
		generateLinearRingCoordinate(polygon.getExteriorRing());
		
		ArrayList<LinearRing> interiorRing = polygon.getInteriorRing();
		for(LinearRing interior : interiorRing) {
			generateLinearRingCoordinate(interior);
		}
	}

	public void generateShellCoordinate(Shell shell) {
		ArrayList<Polygon> surfaceMember = shell.getSurfaceMember();
		for(Polygon polygon : surfaceMember) {
			generatePolygonCoordinate(polygon);
		}
	}

	public void generateSolidCoordinate(Solid solid) {		
		generateShellCoordinate(solid.getExteriorShell());
		
		ArrayList<Shell> interiorShell = solid.getInteriorShell();
		for(Shell interior : interiorShell) {
			generateShellCoordinate(interior);
		}
	}
	
	private static Point transformation(double x, double y) {
	        Coordinate coord1 = new Coordinate(0.14195979899497488, 0.056451612903225756);
	        Coordinate coord2 = new Coordinate(0.44597989949748745, 0.1088709677419355);
	        Coordinate coord3 = new Coordinate(0.9849246231155779, 0.9905913978494624);
	        Coordinate coord4 = new Coordinate(37.5116785, 127.1021436);
	        Coordinate coord5 = new Coordinate(37.5119898, 127.1025482);
	        Coordinate coord6 = new Coordinate(37.5134122, 127.1025103);
	        
	        Coordinate test = new Coordinate(x, y);

	        com.vividsolutions.jts.geom.Point p = JTSFactoryFinder.getGeometryFactory().createPoint(test);
	        
	        AffineTransformation affine = new AffineTransformationBuilder(coord1, coord2, coord3, coord4, coord5, coord6).getTransformation();
	        Geometry geom = affine.transform(p);

            Point resultP = JTSUtil.convertPoint((com.vividsolutions.jts.geom.Point) geom);
            
            return resultP;
	}

}
