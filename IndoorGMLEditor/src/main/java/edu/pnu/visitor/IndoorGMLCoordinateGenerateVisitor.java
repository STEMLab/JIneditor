package edu.pnu.visitor;

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
import edu.pnu.project.StateOnFloor;
import edu.pnu.project.TransitionOnFloor;

public class IndoorGMLCoordinateGenerateVisitor implements IndoorGMLElementVisitor {
	private boolean is3DGeometry;
	private boolean isMLGHeight;
	private double floorGroundHeight;
	private Point lowerCornerReference;
	private Point upperCornerReference;

	public IndoorGMLCoordinateGenerateVisitor(boolean is3DGeometry) {
		// TODO Auto-generated constructor stub
		this.is3DGeometry = is3DGeometry;
	}

	@Override
	public void visit(IndoorFeatures indoorFeatures) {
		// TODO Auto-generated method stub
		
		isMLGHeight = false;
		visit(indoorFeatures.getPrimalSpaceFeatures());
		isMLGHeight = true;
		visit(indoorFeatures.getMultiLayeredGraph());
	}

	@Override
	public void visit(PrimalSpaceFeatures primalSpaceFeatures) {		
		ArrayList<CellSpaceOnFloor> cellSpaceOnFloors = primalSpaceFeatures.getCellSpaceOnFloors();
		for(CellSpaceOnFloor cellSpaceOnFloor : cellSpaceOnFloors) {
			visit(cellSpaceOnFloor);
		}
		
		ArrayList<CellSpaceBoundaryOnFloor> cellSpaceBoundaryOnFloors = primalSpaceFeatures.getCellSpaceBoundaryOnFloors();
		for(CellSpaceBoundaryOnFloor cellSpaceBoundaryOnFloor : cellSpaceBoundaryOnFloors) {
			visit(cellSpaceBoundaryOnFloor);
		}
	}

	@Override
	public void visit(CellSpaceOnFloor cellSpaceOnFloor) {		
		lowerCornerReference = cellSpaceOnFloor.getFloorProperty().getBottomLeftPoint();
		upperCornerReference = cellSpaceOnFloor.getFloorProperty().getTopRightPoint();
		floorGroundHeight = cellSpaceOnFloor.getFloorProperty().getGroundHeight();
		ArrayList<CellSpace> cellSpaceMember = cellSpaceOnFloor.getCellSpaceMember();
		for(CellSpace cellSpace : cellSpaceMember) {
			visit(cellSpace);
		}
	}

	@Override
	public void visit(CellSpace cellSpace) {
		if(is3DGeometry) {
			visit(cellSpace.getGeometry3D());
		} else {
			visit(cellSpace.getGeometry2D());
		}
	}
	
	@Override
	public void visit(CellSpaceBoundaryOnFloor cellSpaceBoundaryOnFloor) {
		lowerCornerReference = cellSpaceBoundaryOnFloor.getFloorProperty().getBottomLeftPoint();
		upperCornerReference = cellSpaceBoundaryOnFloor.getFloorProperty().getTopRightPoint();
		floorGroundHeight = cellSpaceBoundaryOnFloor.getFloorProperty().getGroundHeight();
		ArrayList<CellSpaceBoundary> cellSpaceBoundaryMember = cellSpaceBoundaryOnFloor.getCellSpaceBoundaryMember();
		for(CellSpaceBoundary cellSpaceBoundary : cellSpaceBoundaryMember) {
			visit(cellSpaceBoundary);
		}
	}
	
	@Override
	public void visit(CellSpaceBoundary cellSpaceBoundary) {
		// TODO Auto-generated method stub
		if(is3DGeometry) {
			if(cellSpaceBoundary.getGeometry3D() == null) return;
			visit(cellSpaceBoundary.getGeometry3D());
		} else {
			if(cellSpaceBoundary.getGeometry2D() == null) return;
			visit(cellSpaceBoundary.getGeometry2D());
		}
	}

	@Override
	public void visit(MultiLayeredGraph multiLayeredGraph) {		
		ArrayList<SpaceLayers> spaceLayersList = multiLayeredGraph.getSpaceLayers();
		for(SpaceLayers spaceLayers : spaceLayersList) {
			visit(spaceLayers);
		}
		
		ArrayList<InterEdges> interEdgesList = multiLayeredGraph.getInterEdges();
		for(InterEdges interEdges : interEdgesList) {
			visit(interEdges);
		}
	}

	@Override
	public void visit(SpaceLayers spaceLayers) {		
		ArrayList<SpaceLayer> spaceLayerList = spaceLayers.getSpaceLayerMember();
		for(SpaceLayer spaceLayer : spaceLayerList) {
			visit(spaceLayer);
		}
		
	}

	@Override
	public void visit(SpaceLayer spaceLayer) {
		ArrayList<Nodes> nodesList = spaceLayer.getNodes();
		for(Nodes nodes : nodesList) {
			visit(nodes);
		}
		
		ArrayList<Edges> edgesList = spaceLayer.getEdges();
		for(Edges edges : edgesList) {
			visit(edges);
		}
	}

	@Override
	public void visit(Nodes nodes) {
		ArrayList<StateOnFloor> stateOnFloorList = nodes.getStateOnFloors();
		for(StateOnFloor stateOnFloor : stateOnFloorList) {
			visit(stateOnFloor);
		}
	}

	@Override
	public void visit(StateOnFloor stateOnFloor) {
		ArrayList<State> stateList = stateOnFloor.getStateMember();
		
		lowerCornerReference = stateOnFloor.getFloorProperty().getBottomLeftPoint();
		upperCornerReference = stateOnFloor.getFloorProperty().getTopRightPoint();
		floorGroundHeight = stateOnFloor.getFloorProperty().getGroundHeight();
		for(State state : stateList) {
			visit(state);
		}
	}

	@Override
	public void visit(State state) {		
		visit(state.getPosition());
	}

	@Override
	public void visit(Edges edges) {		
		ArrayList<TransitionOnFloor> transitionOnFloorList = edges.getTransitionOnFloors();
		for(TransitionOnFloor transitionOnFloor : transitionOnFloorList) {
			visit(transitionOnFloor);
		}
	}

	@Override
	public void visit(TransitionOnFloor transitionOnFloor) {
		ArrayList<Transition> transitionList = transitionOnFloor.getTransitionMember();
		
		lowerCornerReference = transitionOnFloor.getFloorProperty().getBottomLeftPoint();
		upperCornerReference = transitionOnFloor.getFloorProperty().getTopRightPoint();
		floorGroundHeight = transitionOnFloor.getFloorProperty().getGroundHeight();
		for(Transition transition : transitionList) {
			visit(transition);
		}
	}

	@Override
	public void visit(Transition transition) {		
		visit(transition.getPath());
	}
	
	@Override
	public void visit(InterEdges interEdges) {		
		ArrayList<InterLayerConnection> interLayerConnectionList = interEdges.getInterLayerConnectionMember();
		for(InterLayerConnection interLayerConnection : interLayerConnectionList) {
			visit(interLayerConnection);
		}
	}

	@Override
	public void visit(InterLayerConnection interLayerConnection) {
	}

	@Override
	public void visit(Point point) {		
		point.setRealX(lowerCornerReference.getPanelX() + point.getPanelRatioX() * (upperCornerReference.getPanelX() - lowerCornerReference.getPanelX()));
		point.setRealY(lowerCornerReference.getPanelY() + (1 - point.getPanelRatioY()) * (upperCornerReference.getPanelY() - lowerCornerReference.getPanelY()));
		//point.setY(lowerCornerReference.getY() + point.getPanelRatioY() * (upperCornerReference.getY() - lowerCornerReference.getY()));
		if(!is3DGeometry || isMLGHeight) {
			point.setZ(floorGroundHeight);
		}
	}
	
	@Override
	public void visit(LineString lineString) {
		//if(lineString.getxLinkGeometry() != null) return;
		if(lineString.getPoints().size() == 0) return;
		
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
	
	@Override
	public void visit(LinearRing linearRing) {
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

	@Override
	public void visit(Polygon polygon) {
		if(polygon.getxLinkGeometry() != null) return;
		
		visit(polygon.getExteriorRing());
		
		ArrayList<LinearRing> interiorRing = polygon.getInteriorRing();
		for(LinearRing interior : interiorRing) {
			visit(interior);
		}
	}

	@Override
	public void visit(Shell shell) {
		ArrayList<Polygon> surfaceMember = shell.getSurfaceMember();
		for(Polygon polygon : surfaceMember) {
			visit(polygon);
		}
	}

	@Override
	public void visit(Solid solid) {		
		visit(solid.getExteriorShell());
		
		ArrayList<Shell> interiorShell = solid.getInteriorShell();
		for(Shell interior : interiorShell) {
			visit(interior);
		}
	}

}
