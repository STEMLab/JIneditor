package edu.pnu.util;

import java.util.ArrayList;

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
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.util.AffineTransformation;
import com.vividsolutions.jts.geom.util.AffineTransformationBuilder;

import edu.pnu.project.StateOnFloor;
import edu.pnu.project.TransitionOnFloor;

public class CoordinateTransformUtil {
	private static AffineTransformation transformation;
	private static GeometryFactory gf = JTSFactoryFinder.getGeometryFactory();
	
	public static void setReferences() {
		/*
		쇼핑몰 가장왼쪽아래 모서리
		-69.87857 -18.25298
		37.5153405 127.102927

		쇼핑몰 중간아래 문 들어간곳 모서리
		83.33516 -70.07354
		37.513496 127.104777

		에비뉴엘 왼쪽아래모서리
		-228.58053 -112.35794
		37.512056 127.101688
		*/
		/*
		Coordinate src1 = new Coordinate(-69.87857, -18.25298);
        Coordinate src2 = new Coordinate(83.33516, -70.07354);
        Coordinate src3 = new Coordinate(-228.58053, -112.35794);
        Coordinate dst1 = new Coordinate(37.5153405, 127.102927);
        Coordinate dst2 = new Coordinate(37.513496, 127.104777);
        Coordinate dst3 = new Coordinate(37.512056, 127.101688);
        */

		Coordinate src1 = new Coordinate(-228.335965, -111.999868);
        Coordinate src2 = new Coordinate(-102.385535, 18.624991);
        Coordinate src3 = new Coordinate(147.455221, -71.219842);
        Coordinate dst1 = new Coordinate(127.101781756779332, 37.512041134657203);
        Coordinate dst2 = new Coordinate(127.102497451122758, 37.513555256155222);
        Coordinate dst3 = new Coordinate(127.105501634548247, 37.513692821399175);
        
        transformation = new AffineTransformationBuilder(src1, src2, src3, dst1, dst2, dst3).getTransformation();
	}
	
	public static IndoorFeatures transform(IndoorFeatures indoorFeatures, boolean is3DGeometry) {
		traverseIndoorFeatures(indoorFeatures, is3DGeometry);
		
		return indoorFeatures;
	}

	public static void traverseIndoorFeatures(IndoorFeatures indoorFeatures, boolean is3DGeometry) {
		traversePrimalSpaceFeatures(indoorFeatures.getPrimalSpaceFeatures(), is3DGeometry);
		traverseMultiLayeredGraph(indoorFeatures.getMultiLayeredGraph());
	}

	public static void traversePrimalSpaceFeatures(PrimalSpaceFeatures primalSpaceFeatures, boolean is3DGeometry) {		
		ArrayList<CellSpaceOnFloor> cellSpaceOnFloors = primalSpaceFeatures.getCellSpaceOnFloors();
		for(CellSpaceOnFloor cellSpaceOnFloor : cellSpaceOnFloors) {
			traverseCellSpaceOnFloor(cellSpaceOnFloor, is3DGeometry);
		}
		
		ArrayList<CellSpaceBoundaryOnFloor> cellSpaceBoundaryOnFloors = primalSpaceFeatures.getCellSpaceBoundaryOnFloors();
		for(CellSpaceBoundaryOnFloor cellSpaceBoundaryOnFloor : cellSpaceBoundaryOnFloors) {
			traverseCellSpaceBoundaryOnFloor(cellSpaceBoundaryOnFloor, is3DGeometry);
		}
		
	}

	public static void traverseCellSpaceOnFloor(CellSpaceOnFloor cellSpaceOnFloor, boolean is3DGeometry) {
		ArrayList<CellSpace> cellSpaceMember = cellSpaceOnFloor.getCellSpaceMember();
		for(CellSpace cellSpace : cellSpaceMember) {
			traverseCellSpace(cellSpace, is3DGeometry);
		}
	}

	public static void traverseCellSpace(CellSpace cellSpace, boolean is3DGeometry) {
		if(is3DGeometry) {
			generateSolidCoordinate(cellSpace.getGeometry3D());
		} else {
			generatePolygonCoordinate(cellSpace.getGeometry2D());
		}
	}
	
	public static void traverseCellSpaceBoundaryOnFloor(CellSpaceBoundaryOnFloor cellSpaceBoundaryOnFloor, boolean is3DGeometry) {
		ArrayList<CellSpaceBoundary> cellSpaceBoundaryMember = cellSpaceBoundaryOnFloor.getCellSpaceBoundaryMember();
		for(CellSpaceBoundary cellSpaceBoundary : cellSpaceBoundaryMember) {
			traverseCellSpaceBoundary(cellSpaceBoundary, is3DGeometry);
		}
	}
	
	public static void traverseCellSpaceBoundary(CellSpaceBoundary cellSpaceBoundary, boolean is3DGeometry) {
		// TODO Auto-generated method stub
		if(is3DGeometry) {
			if(cellSpaceBoundary.getGeometry3D() == null) return;
			generatePolygonCoordinate(cellSpaceBoundary.getGeometry3D());
		} else {
			if(cellSpaceBoundary.getGeometry2D() == null) return;
			generateLineStringCoordinate(cellSpaceBoundary.getGeometry2D(), false);
		}
	}

	public static void traverseMultiLayeredGraph(MultiLayeredGraph multiLayeredGraph) {		
		ArrayList<SpaceLayers> spaceLayersList = multiLayeredGraph.getSpaceLayers();
		for(SpaceLayers spaceLayers : spaceLayersList) {
			traverseSpaceLayers(spaceLayers);
		}
		
		ArrayList<InterEdges> interEdgesList = multiLayeredGraph.getInterEdges();
		for(InterEdges interEdges : interEdgesList) {
			traverseInterEdges(interEdges);
		}
	}

	public static void traverseSpaceLayers(SpaceLayers spaceLayers) {		
		ArrayList<SpaceLayer> spaceLayerList = spaceLayers.getSpaceLayerMember();
		for(SpaceLayer spaceLayer : spaceLayerList) {
			traverseSpaceLayer(spaceLayer);
		}
		
	}

	public static void traverseSpaceLayer(SpaceLayer spaceLayer) {
		ArrayList<Nodes> nodesList = spaceLayer.getNodes();
		for(Nodes nodes : nodesList) {
			traverseNodes(nodes);
		}
		
		ArrayList<Edges> edgesList = spaceLayer.getEdges();
		for(Edges edges : edgesList) {
			traverseEdges(edges);
		}
	}

	public static void traverseNodes(Nodes nodes) {
		ArrayList<StateOnFloor> stateOnFloorList = nodes.getStateOnFloors();
		for(StateOnFloor stateOnFloor : stateOnFloorList) {
			traverseStateOnFloor(stateOnFloor);
		}
	}

	public static void traverseStateOnFloor(StateOnFloor stateOnFloor) {
		ArrayList<State> stateList = stateOnFloor.getStateMember();
		for(State state : stateList) {
			traverseState(state);
		}
	}

	public static void traverseState(State state) {
		generatePointCoordinate(state.getPosition());
	}

	public static void traverseEdges(Edges edges) {		
		ArrayList<TransitionOnFloor> transitionOnFloorList = edges.getTransitionOnFloors();
		for(TransitionOnFloor transitionOnFloor : transitionOnFloorList) {
			traverseTransitionOnFloor(transitionOnFloor);
		}
	}

	public static void traverseTransitionOnFloor(TransitionOnFloor transitionOnFloor) {
		ArrayList<Transition> transitionList = transitionOnFloor.getTransitionMember();
		for(Transition transition : transitionList) {
			traverseTransition(transition);
		}
	}

	public static void traverseTransition(Transition transition) {
		if (transition.getGmlID().equalsIgnoreCase("T9440")) {
			System.out.println("T9440 fonud");
		}
		generateLineStringCoordinate(transition.getPath(), true);
	}
	
	public static void traverseInterEdges(InterEdges interEdges) {		
		ArrayList<InterLayerConnection> interLayerConnectionList = interEdges.getInterLayerConnectionMember();
		for(InterLayerConnection interLayerConnection : interLayerConnectionList) {
			traverseInterLayerConnection(interLayerConnection);
		}
	}

	public static void traverseInterLayerConnection(InterLayerConnection interLayerConnection) {
	}

	public static void generatePointCoordinate(Point point) {
		double x = point.getRealX();
		double y = point.getRealY();
		
		Coordinate tCoord = transformation(x, y);
		
		point.setRealX(tCoord.x);
		point.setRealY(tCoord.y);
		
		// longitude, altitude reverse
		/*
		point.setRealX(tCoord.y);
		point.setRealY(tCoord.x);
		*/
	}
	
	public static void generateLineStringCoordinate(LineString lineString, boolean isSetEndPoints) {
		if(lineString.getPoints().size() == 0) return;
				
		ArrayList<Point> points = lineString.getPoints();
		
		for(int i = 0; i < points.size(); i++) {
			if (isSetEndPoints) {
				if (i == 0 || i == points.size() - 1) continue;
			}
			Point point = points.get(i);

			generatePointCoordinate(point);
		}
	}
	
	public static void generateLinearRingCoordinate(LinearRing linearRing) {
		ArrayList<Point> points = linearRing.getPoints();
		for(Point point : points) {
			generatePointCoordinate(point);
		}
	}

	public static void generatePolygonCoordinate(Polygon polygon) {
		if(polygon.getxLinkGeometry() != null) return;
		
		generateLinearRingCoordinate(polygon.getExteriorRing());
		
		ArrayList<LinearRing> interiorRing = polygon.getInteriorRing();
		for(LinearRing interior : interiorRing) {
			generateLinearRingCoordinate(interior);
		}
	}

	public static void generateShellCoordinate(Shell shell) {
		ArrayList<Polygon> surfaceMember = shell.getSurfaceMember();
		for(Polygon polygon : surfaceMember) {
			generatePolygonCoordinate(polygon);
		}
	}

	public static void generateSolidCoordinate(Solid solid) {		
		generateShellCoordinate(solid.getExteriorShell());
		
		ArrayList<Shell> interiorShell = solid.getInteriorShell();
		for(Shell interior : interiorShell) {
			generateShellCoordinate(interior);
		}
	}
	
	private static Coordinate transformation(double x, double y) {	        
        Coordinate coord = new Coordinate(x, y);

        com.vividsolutions.jts.geom.Point source = gf.createPoint(coord);
        Geometry geom = transformation.transform(source);
        
        return geom.getCoordinate();
	}

}
