package edu.pnu.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
import edu.pnu.project.StateOnFloor;
import edu.pnu.project.TransitionOnFloor;

public class IndoorGMLIDGenerator {
	private boolean is3DGeometry;
	private boolean isMLGHeight;
	private double floorGroundHeight;
	private Point lowerCornerReference;
	private Point upperCornerReference;
	private IndoorFeatures indoorFeatures;

	public IndoorGMLIDGenerator(IndoorFeatures indoorFeatures, boolean is3DGeometry) {
		this.indoorFeatures = indoorFeatures;
		this.is3DGeometry = is3DGeometry;
	}
	
	public void generateGMLID() {		
		traverseIndoorFeatures(indoorFeatures);
	}

	public void traverseIndoorFeatures(IndoorFeatures indoorFeatures) {
		indoorFeatures.setGmlID("IFs");
		
		/*
		PrimalSpaceFeatures.setLabelNumber(1);
		CellSpace.setLabelNumber(1);
		CellSpaceBoundary.setLabelNumber(1);
		*/
		
		/*
		MultiLayeredGraph.setLabelNumber(1);
		SpaceLayers.setLabelNumber(1);
		SpaceLayer.setLabelNumber(1);
		Nodes.setLabelNumber(1);
		Edges.setLabelNumber(1);
		State.setLabelNumber(1);
		Transition.setLabelNumber(1);
		InterEdges.setLabelNumber(1);
		InterLayerConnection.setLabelNumber(1);
		*/
		Point.setLabelNumber(1);
		LineString.setLabelNumber(1);
		Polygon.setLabelNumber(1);
		Solid.setLabelNumber(1);
		
		//isMLGHeight = false;
		//traversePrimalSpaceFeatures(indoorFeatures.getPrimalSpaceFeatures());
		isMLGHeight = true;
		traverseMultiLayeredGraph(indoorFeatures.getMultiLayeredGraph());
	}

	public void traversePrimalSpaceFeatures(PrimalSpaceFeatures primalSpaceFeatures) {
		primalSpaceFeatures.setGmlID("PS" + PrimalSpaceFeatures.getLabelNumber());
		PrimalSpaceFeatures.setLabelNumber(PrimalSpaceFeatures.getLabelNumber() + 1);
		
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
		cellSpace.setGmlID("C" + CellSpace.getLabelNumber());
		CellSpace.setLabelNumber(CellSpace.getLabelNumber() + 1);
		
		if(is3DGeometry) {
			traverseSolid(cellSpace.getGeometry3D());
		} else {
			traversePolygon(cellSpace.getGeometry2D());
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
		if(is3DGeometry) {
			if(cellSpaceBoundary.getGeometry3D() == null) return;
			traversePolygon(cellSpaceBoundary.getGeometry3D());
		} else {
			if(cellSpaceBoundary.getGeometry2D() == null) return;
			traverseLineString(cellSpaceBoundary.getGeometry2D());
		}
		cellSpaceBoundary.setGmlID("CB" + CellSpaceBoundary.getLabelNumber());
		CellSpaceBoundary.setLabelNumber(CellSpaceBoundary.getLabelNumber() + 1);
	}

	public void traverseMultiLayeredGraph(MultiLayeredGraph multiLayeredGraph) {
		//multiLayeredGraph.setGmlID("MG" + MultiLayeredGraph.getLabelNumber());
		//MultiLayeredGraph.setLabelNumber(MultiLayeredGraph.getLabelNumber() + 1);
		
		ArrayList<SpaceLayers> spaceLayersList = multiLayeredGraph.getSpaceLayers();
		for(SpaceLayers spaceLayers : spaceLayersList) {
			traverseSpaceLayers(spaceLayers);
		}
		
		/*
		ArrayList<InterEdges> interEdgesList = multiLayeredGraph.getInterEdges();
		for(InterEdges interEdges : interEdgesList) {
			traverseInterEdges(interEdges);
		}
		*/
	}

	public void traverseSpaceLayers(SpaceLayers spaceLayers) {
		//spaceLayers.setGmlID("SL" + SpaceLayers.getLabelNumber());
		//SpaceLayers.setLabelNumber(SpaceLayers.getLabelNumber() + 1);
		
		ArrayList<SpaceLayer> spaceLayerList = spaceLayers.getSpaceLayerMember();
		for(SpaceLayer spaceLayer : spaceLayerList) {
			traverseSpaceLayer(spaceLayer);
		}
		
	}

	public void traverseSpaceLayer(SpaceLayer spaceLayer) {
		//spaceLayer.setGmlID("IS" + SpaceLayer.getLabelNumber());
		//SpaceLayer.setLabelNumber(SpaceLayer.getLabelNumber() + 1);
		
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
		//nodes.setGmlID("N" + Nodes.getLabelNumber());
		//Nodes.setLabelNumber(Nodes.getLabelNumber() + 1);
		
		ArrayList<StateOnFloor> stateOnFloorList = nodes.getStateOnFloors();
		for(StateOnFloor stateOnFloor : stateOnFloorList) {
			traverseStateOnFloor(stateOnFloor);
		}
	}

	public void traverseStateOnFloor(StateOnFloor stateOnFloor) {
		ArrayList<State> stateList = stateOnFloor.getStateMember();
		
		lowerCornerReference = stateOnFloor.getFloorProperty().getBottomLeftPoint();
		upperCornerReference = stateOnFloor.getFloorProperty().getTopRightPoint();
		floorGroundHeight = stateOnFloor.getFloorProperty().getGroundHeight();
		for(State state : stateList) {
			traverseState(state);
		}
	}

	public void traverseState(State state) {
		//state.setGmlID("R" + State.getLabelNumber());
		//State.setLabelNumber(State.getLabelNumber() + 1);
		
		traversePoint(state.getPosition());
	}

	public void traverseEdges(Edges edges) {
		//edges.setGmlID("E" + Edges.getLabelNumber());
		//Edges.setLabelNumber(Edges.getLabelNumber() + 1);
		
		ArrayList<TransitionOnFloor> transitionOnFloorList = edges.getTransitionOnFloors();
		for(TransitionOnFloor transitionOnFloor : transitionOnFloorList) {
			traverseTransitionOnFloor(transitionOnFloor);
		}
	}

	public void traverseTransitionOnFloor(TransitionOnFloor transitionOnFloor) {
		ArrayList<Transition> transitionList = transitionOnFloor.getTransitionMember();
		
		lowerCornerReference = transitionOnFloor.getFloorProperty().getBottomLeftPoint();
		upperCornerReference = transitionOnFloor.getFloorProperty().getTopRightPoint();
		floorGroundHeight = transitionOnFloor.getFloorProperty().getGroundHeight();
		for(Transition transition : transitionList) {
			traverseTransition(transition);
		}
	}

	public void traverseTransition(Transition transition) {
		//transition.setGmlID("T" + Transition.getLabelNumber());
		//Transition.setLabelNumber(Transition.getLabelNumber() + 1);
		traverseLineString(transition.getPath());
	}
	
	public void traverseInterEdges(InterEdges interEdges) {
		interEdges.setGmlID("IE" + InterEdges.getLabelNumber());
		InterEdges.setLabelNumber(InterEdges.getLabelNumber() + 1);
		
		ArrayList<InterLayerConnection> interLayerConnectionList = interEdges.getInterLayerConnectionMember();
		for(InterLayerConnection interLayerConnection : interLayerConnectionList) {
			traverseInterLayerConnection(interLayerConnection);
		}
	}

	public void traverseInterLayerConnection(InterLayerConnection interLayerConnection) {
		interLayerConnection.setGmlID("IL" + InterLayerConnection.getLabelNumber());
		InterLayerConnection.setLabelNumber(InterLayerConnection.getLabelNumber() + 1);
	}

	public void traversePoint(Point point) {
		point.setGMLID("P" + Point.getLabelNumber());
		Point.setLabelNumber(Point.getLabelNumber() + 1);
		
		point.setRealX(lowerCornerReference.getPanelX() + point.getPanelRatioX() * (upperCornerReference.getPanelX() - lowerCornerReference.getPanelX()));
		point.setRealY(lowerCornerReference.getPanelY() + (1 - point.getPanelRatioY()) * (upperCornerReference.getPanelY() - lowerCornerReference.getPanelY()));
		//point.setY(lowerCornerReference.getY() + point.getPanelRatioY() * (upperCornerReference.getY() - lowerCornerReference.getY()));
		if(!is3DGeometry || isMLGHeight) {
			point.setZ(floorGroundHeight);
		}
	}
	
	public void traverseLineString(LineString lineString) {
		//if(lineString.getxLinkGeometry() != null) return;
		if(lineString.getPoints().size() == 0) return;
		
		lineString.setGMLID("LS" + LineString.getLabelNumber());
		LineString.setLabelNumber(LineString.getLabelNumber() + 1);
		
		ArrayList<Point> points = lineString.getPoints();
		for(Point point : points) {
			point.setRealX(lowerCornerReference.getPanelX() + point.getPanelRatioX() * (upperCornerReference.getPanelX() - lowerCornerReference.getPanelX()));
			point.setRealY(lowerCornerReference.getPanelY() + (1 - point.getPanelRatioY()) * (upperCornerReference.getPanelY() - lowerCornerReference.getPanelY()));
			//point.setY(lowerCornerReference.getY() + point.getPanelRatioY() * (upperCornerReference.getY() - lowerCornerReference.getY()));
			if(!is3DGeometry || isMLGHeight) {
				point.setZ(floorGroundHeight);
			}
		}
	}
	
	public void traverseLinearRing(LinearRing linearRing) {
		ArrayList<Point> points = linearRing.getPoints();
		for(Point point : points) {
			point.setRealX(lowerCornerReference.getPanelX() + point.getPanelRatioX() * (upperCornerReference.getPanelX() - lowerCornerReference.getPanelX()));
			point.setRealY(lowerCornerReference.getPanelY() + (1 - point.getPanelRatioY()) * (upperCornerReference.getPanelY() - lowerCornerReference.getPanelY()));
			//point.setY(lowerCornerReference.getY() + point.getPanelRatioY() * (upperCornerReference.getY() - lowerCornerReference.getY()));
			if(!is3DGeometry || isMLGHeight) {
				point.setZ(floorGroundHeight);
			}
		}
	}

	public void traversePolygon(Polygon polygon) {
		if(polygon.getxLinkGeometry() != null) return;
		
		polygon.setGMLID("POLY" + Polygon.getLabelNumber());
		Polygon.setLabelNumber(Polygon.getLabelNumber() + 1);
		
		traverseLinearRing(polygon.getExteriorRing());
		
		ArrayList<LinearRing> interiorRing = polygon.getInteriorRing();
		for(LinearRing interior : interiorRing) {
			traverseLinearRing(interior);
		}
	}

	public void traverseShell(Shell shell) {
		ArrayList<Polygon> surfaceMember = shell.getSurfaceMember();
		for(Polygon polygon : surfaceMember) {
			traversePolygon(polygon);
		}
	}

	public void traverseSolid(Solid solid) {
		solid.setGMLID("SOLID" + Solid.getLabelNumber());
		Solid.setLabelNumber(Solid.getLabelNumber() + 1);
		
		traverseShell(solid.getExteriorShell());
		
		ArrayList<Shell> interiorShell = solid.getInteriorShell();
		for(Shell interior : interiorShell) {
			traverseShell(interior);
		}
	}
}
