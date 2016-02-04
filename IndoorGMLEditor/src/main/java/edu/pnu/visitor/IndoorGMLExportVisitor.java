package edu.pnu.visitor;

import java.util.ArrayList;

import javax.xml.bind.JAXBElement;

import net.opengis.gml.v_3_2_1.AbstractRingPropertyType;
import net.opengis.gml.v_3_2_1.CodeType;
import net.opengis.gml.v_3_2_1.CurvePropertyType;
import net.opengis.gml.v_3_2_1.DirectPositionType;
import net.opengis.gml.v_3_2_1.FeaturePropertyType;
import net.opengis.gml.v_3_2_1.LineStringType;
import net.opengis.gml.v_3_2_1.LinearRingType;
import net.opengis.gml.v_3_2_1.OrientableCurveType;
import net.opengis.gml.v_3_2_1.OrientableSurfaceType;
import net.opengis.gml.v_3_2_1.PointPropertyType;
import net.opengis.gml.v_3_2_1.PointType;
import net.opengis.gml.v_3_2_1.PolygonType;
import net.opengis.gml.v_3_2_1.ShellPropertyType;
import net.opengis.gml.v_3_2_1.ShellType;
import net.opengis.gml.v_3_2_1.SignType;
import net.opengis.gml.v_3_2_1.SolidPropertyType;
import net.opengis.gml.v_3_2_1.SolidType;
import net.opengis.gml.v_3_2_1.StringOrRefType;
import net.opengis.gml.v_3_2_1.SurfacePropertyType;
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
import net.opengis.indoorgml.core.v_1_0.CellSpaceBoundaryMemberType;
import net.opengis.indoorgml.core.v_1_0.CellSpaceBoundaryPropertyType;
import net.opengis.indoorgml.core.v_1_0.CellSpaceBoundaryType;
import net.opengis.indoorgml.core.v_1_0.CellSpaceMemberType;
import net.opengis.indoorgml.core.v_1_0.CellSpacePropertyType;
import net.opengis.indoorgml.core.v_1_0.CellSpaceType;
import net.opengis.indoorgml.core.v_1_0.EdgesType;
import net.opengis.indoorgml.core.v_1_0.IndoorFeaturesType;
import net.opengis.indoorgml.core.v_1_0.InterEdgesType;
import net.opengis.indoorgml.core.v_1_0.InterLayerConnectionMemberType;
import net.opengis.indoorgml.core.v_1_0.InterLayerConnectionType;
import net.opengis.indoorgml.core.v_1_0.MultiLayeredGraphType;
import net.opengis.indoorgml.core.v_1_0.NodesType;
import net.opengis.indoorgml.core.v_1_0.ObjectFactory;
import net.opengis.indoorgml.core.v_1_0.PrimalSpaceFeaturesPropertyType;
import net.opengis.indoorgml.core.v_1_0.PrimalSpaceFeaturesType;
import net.opengis.indoorgml.core.v_1_0.SpaceLayerMemberType;
import net.opengis.indoorgml.core.v_1_0.SpaceLayerPropertyType;
import net.opengis.indoorgml.core.v_1_0.SpaceLayerType;
import net.opengis.indoorgml.core.v_1_0.SpaceLayersType;
import net.opengis.indoorgml.core.v_1_0.StateMemberType;
import net.opengis.indoorgml.core.v_1_0.StatePropertyType;
import net.opengis.indoorgml.core.v_1_0.StateType;
import net.opengis.indoorgml.core.v_1_0.TransitionMemberType;
import net.opengis.indoorgml.core.v_1_0.TransitionPropertyType;
import net.opengis.indoorgml.core.v_1_0.TransitionType;
import net.opengis.indoorgml.geometry.LineString;
import net.opengis.indoorgml.geometry.LinearRing;
import net.opengis.indoorgml.geometry.Point;
import net.opengis.indoorgml.geometry.Polygon;
import net.opengis.indoorgml.geometry.Shell;
import net.opengis.indoorgml.geometry.Solid;
import edu.pnu.project.StateOnFloor;
import edu.pnu.project.TransitionOnFloor;

public class IndoorGMLExportVisitor implements IndoorGMLElementVisitor {
	private ObjectFactory IGMLFactory;
	private net.opengis.gml.v_3_2_1.ObjectFactory GMLFactory;
	
	private boolean is3DGeometry;
	
	private IndoorFeaturesType indoorFeaturesType;
	private MultiLayeredGraphType multiLayeredGraphType;
	private SpaceLayersType spaceLayersType;
	private SpaceLayerMemberType spaceLayerMemberType;
	private SpaceLayerType spaceLayerType;
	private NodesType nodesType;
	private StateMemberType stateMemberType;
	private StateType stateType;
	private TransitionPropertyType transitionPropertyType;
	private CellSpacePropertyType cellSpacePropertyType;
	private PointPropertyType pointPropertyType;	
	private PointType pointType;
	private EdgesType edgesType;
	private TransitionMemberType transitionMemberType;
	private TransitionType transitionType;
	private CurvePropertyType curvePropertyType;
	private InterEdgesType interEdgesType;
	private InterLayerConnectionMemberType interLayerConnectionMemberType;
	private InterLayerConnectionType interLayerConnectionType;
	private PrimalSpaceFeaturesType primalSpaceFeaturesType;
	private CellSpaceMemberType cellSpaceMemberType;
	private CellSpaceType cellSpaceType;
	private StatePropertyType statePropertyType;
	private CellSpaceBoundaryPropertyType cellSpaceBoundaryPropertyType;
	private AbstractRingPropertyType abstractRingPropertyType;
	private SolidPropertyType solidPropertyType;
	private ShellPropertyType shellPropertyType;
	private SurfacePropertyType surfacePropertyType;
	private CellSpaceBoundaryMemberType cellSpaceBoundaryMemberType;
	private CellSpaceBoundaryType cellSpaceBoundaryType;
	
	public IndoorGMLExportVisitor(boolean is3DGeometry) {
		// TODO Auto-generated constructor stub
		this.IGMLFactory = new net.opengis.indoorgml.core.v_1_0.ObjectFactory();
		this.GMLFactory = new net.opengis.gml.v_3_2_1.ObjectFactory();
		
		this.is3DGeometry = is3DGeometry;
	}
	
	public JAXBElement<IndoorFeaturesType> getJAXBElement() {
		JAXBElement<IndoorFeaturesType> je = IGMLFactory.createIndoorFeatures(indoorFeaturesType);
		
		return je;
	}

	@Override
	public void visit(IndoorFeatures indoorFeatures) {
		// TODO Auto-generated method stub
		indoorFeaturesType = IGMLFactory.createIndoorFeaturesType();
		indoorFeaturesType.setId(indoorFeatures.getGmlID());
		indoorFeaturesType.getName().add(createCodeType(indoorFeatures.getGmlID(), null));
		
		multiLayeredGraphType = IGMLFactory.createMultiLayeredGraphType();
		visit(indoorFeatures.getMultiLayeredGraph());
		indoorFeaturesType.setMultiLayeredGraph(multiLayeredGraphType);
		
		primalSpaceFeaturesType = IGMLFactory.createPrimalSpaceFeaturesType();
		visit(indoorFeatures.getPrimalSpaceFeatures());
		PrimalSpaceFeaturesPropertyType primalSpaceFeaturesPropertyType = IGMLFactory.createPrimalSpaceFeaturesPropertyType();
		primalSpaceFeaturesPropertyType.setPrimalSpaceFeatures(primalSpaceFeaturesType);
		indoorFeaturesType.setPrimalSpaceFeatures(primalSpaceFeaturesPropertyType);
	}

	@Override
	public void visit(PrimalSpaceFeatures primalSpaceFeatures) {
		primalSpaceFeaturesType.setId(primalSpaceFeatures.getGmlID());
		primalSpaceFeaturesType.getName().add(createCodeType(primalSpaceFeatures.getGmlID(), null));
		
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
		ArrayList<CellSpace> cellSpaceMember = cellSpaceOnFloor.getCellSpaceMember();
		for(CellSpace cellSpace : cellSpaceMember) {
			//cellSpaceMemberType = IGMLFactory.createCellSpaceMemberType();
			cellSpaceType = IGMLFactory.createCellSpaceType();
			
			visit(cellSpace);
			
			//cellSpaceMemberType.setCellSpace(cellSpaceType);
			FeaturePropertyType featurePropertyType = GMLFactory.createFeaturePropertyType();
			JAXBElement<CellSpaceType> jCellSpaceType = IGMLFactory.createCellSpace(cellSpaceType);
			featurePropertyType.setAbstractFeature(jCellSpaceType);
			primalSpaceFeaturesType.getCellSpaceMember().add(featurePropertyType);
		}
	}

	@Override
	public void visit(CellSpace cellSpace) {
		// TODO Auto-generated method stub
		cellSpaceType.setId(cellSpace.getGmlID());
		cellSpaceType.getName().add(createCodeType(cellSpace.getGmlID(), null));
		cellSpaceType.setDescription(createStringOrRefType(cellSpace.getDescription()));

		State duality = cellSpace.getDuality();
		if(duality != null) {
			statePropertyType = IGMLFactory.createStatePropertyType();
			statePropertyType.setHref("#" + duality.getGmlID());
			cellSpaceType.setDuality(statePropertyType);
		}
		
		ArrayList<CellSpaceBoundary> partialBoundedBy = cellSpace.getPartialBoundedBy();
		for(CellSpaceBoundary cellSpaceBoundary : partialBoundedBy) {
			if(is3DGeometry && cellSpaceBoundary.getGeometry3D() == null) continue;
			else if(!is3DGeometry && cellSpaceBoundary.getGeometry2D() == null) continue;
			cellSpaceBoundaryPropertyType = IGMLFactory.createCellSpaceBoundaryPropertyType();
			cellSpaceBoundaryPropertyType.setHref("#" + cellSpaceBoundary.getGmlID());
			
			cellSpaceType.getPartialboundedBy().add(cellSpaceBoundaryPropertyType);
		}
		
		
		if(is3DGeometry) {
			// geometry3D solid
			solidPropertyType = GMLFactory.createSolidPropertyType(); 
			System.out.println(cellSpace.getGmlID());
			visit(cellSpace.getGeometry3D());			
			cellSpaceType.setGeometry3D(solidPropertyType);
		} else {
			// geometry2D only polygon
			surfacePropertyType = GMLFactory.createSurfacePropertyType();			
			visit(cellSpace.getGeometry2D());
			cellSpaceType.setGeometry2D(surfacePropertyType);
		}
		// ExternalReference
		
	}

	@Override
	public void visit(CellSpaceBoundaryOnFloor cellSpaceBoundaryOnFloor) {
		// TODO Auto-generated method stub
		ArrayList<CellSpaceBoundary> cellSpaceBoundaryMember = cellSpaceBoundaryOnFloor.getCellSpaceBoundaryMember();
		for(CellSpaceBoundary cellSpaceBoundary : cellSpaceBoundaryMember) {
			//cellSpaceBoundaryMemberType = IGMLFactory.createCellSpaceBoundaryMemberType();
			if(is3DGeometry && cellSpaceBoundary.getGeometry3D() == null) continue;
			else if(!is3DGeometry && cellSpaceBoundary.getGeometry2D() == null) continue;
			cellSpaceBoundaryType = IGMLFactory.createCellSpaceBoundaryType();
			
			visit(cellSpaceBoundary);
						
			//cellSpaceBoundaryMemberType.setCellSpaceBoundary(cellSpaceBoundaryType);
			FeaturePropertyType featurePropertyType = GMLFactory.createFeaturePropertyType();
			JAXBElement<CellSpaceBoundaryType> jCellSpaceBoundaryType = IGMLFactory.createCellSpaceBoundary(cellSpaceBoundaryType);
			featurePropertyType.setAbstractFeature(jCellSpaceBoundaryType);
			primalSpaceFeaturesType.getCellSpaceBoundaryMember().add(featurePropertyType);
		}
	}

	@Override
	public void visit(CellSpaceBoundary cellSpaceBoundary) {
		// TODO Auto-generated method stub
		cellSpaceBoundaryType.setId(cellSpaceBoundary.getGmlID());
		cellSpaceBoundaryType.getName().add(createCodeType(cellSpaceBoundary.getGmlID(), null));
		cellSpaceBoundaryType.setDescription(createStringOrRefType(cellSpaceBoundary.getDescription()));

		Transition duality = cellSpaceBoundary.getDuality();
		if(duality != null) {
			transitionPropertyType = IGMLFactory.createTransitionPropertyType();
			transitionPropertyType.setHref("#" + duality.getGmlID());
			cellSpaceBoundaryType.setDuality(transitionPropertyType);
		}
		
		if(is3DGeometry) {
			// geometry3D solid
			surfacePropertyType = GMLFactory.createSurfacePropertyType();
			visit(cellSpaceBoundary.getGeometry3D());
			cellSpaceBoundaryType.setGeometry3D(surfacePropertyType);
		} else {
			// geometry2D only polygon
			curvePropertyType = GMLFactory.createCurvePropertyType();			
			visit(cellSpaceBoundary.getGeometry2D());
			cellSpaceBoundaryType.setGeometry2D(curvePropertyType);
		}
		// ExternalReference
	}

	@Override
	public void visit(MultiLayeredGraph multiLayeredGraph) {
		// TODO Auto-generated method stub
		multiLayeredGraphType.setId(multiLayeredGraph.getGmlID());
		multiLayeredGraphType.getName().add(createCodeType(multiLayeredGraph.getName(), null));
		multiLayeredGraphType.setDescription(createStringOrRefType(multiLayeredGraph.getDescription()));
		
		ArrayList<SpaceLayers> spaceLayersList = multiLayeredGraph.getSpaceLayers();
		for(SpaceLayers spaceLayers : spaceLayersList) {
			spaceLayersType = IGMLFactory.createSpaceLayersType();
			visit(spaceLayers);
			multiLayeredGraphType.getSpaceLayers().add(spaceLayersType);
		}
		
		ArrayList<InterEdges> interEdgesList = multiLayeredGraph.getInterEdges();
		for(InterEdges interEdges : interEdgesList) {
			interEdgesType = IGMLFactory.createInterEdgesType();
			visit(interEdges);
			multiLayeredGraphType.getInterEdges().add(interEdgesType);
		}
		
		
	}

	@Override
	public void visit(SpaceLayers spaceLayers) {
		// TODO Auto-generated method stub
		spaceLayersType.setId(spaceLayers.getGmlID());
		spaceLayersType.getName().add(createCodeType(spaceLayers.getGmlID(), null));
		spaceLayersType.setDescription(createStringOrRefType(spaceLayers.getDescription()));
		
		ArrayList<SpaceLayer> spaceLayerList = spaceLayers.getSpaceLayerMember();
		for(SpaceLayer spaceLayer : spaceLayerList) {
			spaceLayerMemberType = IGMLFactory.createSpaceLayerMemberType();
			spaceLayerType = IGMLFactory.createSpaceLayerType();

			visit(spaceLayer);
			
			spaceLayerMemberType.setSpaceLayer(spaceLayerType);			
			spaceLayersType.getSpaceLayerMember().add(spaceLayerMemberType);
		}
	}

	@Override
	public void visit(SpaceLayer spaceLayer) {
		// TODO Auto-generated method stub
		spaceLayerType.setId(spaceLayer.getGmlID());
		spaceLayerType.getName().add(createCodeType(spaceLayer.getGmlID(), null));
        spaceLayerType.setDescription(createStringOrRefType(spaceLayer.getDescription()));
		
		ArrayList<Nodes> nodesList = spaceLayer.getNodes();
		for(Nodes nodes : nodesList) {
			nodesType = IGMLFactory.createNodesType();
			
			visit(nodes);
			
			spaceLayerType.getNodes().add(nodesType);
		}
		
		ArrayList<Edges> edgesList = spaceLayer.getEdges();
		for(Edges edges : edgesList) {
			edgesType = IGMLFactory.createEdgesType();
			
			visit(edges);
			
			spaceLayerType.getEdges().add(edgesType);
		}
	}

	@Override
	public void visit(Nodes nodes) {
		// TODO Auto-generated method stub
		nodesType.setId(nodes.getGmlID());
		nodesType.getName().add(createCodeType(nodes.getGmlID(), null));
        nodesType.setDescription(createStringOrRefType(nodes.getDescription()));
		
		ArrayList<StateOnFloor> stateOnFloorList = nodes.getStateOnFloors();
		for(StateOnFloor stateOnFloor : stateOnFloorList) {
			visit(stateOnFloor);
		}
	}

	@Override
	public void visit(StateOnFloor stateOnFloor) {
		// TODO Auto-generated method stub
		ArrayList<State> stateList = stateOnFloor.getStateMember();
		for(State state : stateList) {
			stateMemberType = IGMLFactory.createStateMemberType();
			stateType = IGMLFactory.createStateType();
			
			visit(state);
			
			stateMemberType.setState(stateType);
			nodesType.getStateMember().add(stateMemberType);
		}
	}

	@Override
	public void visit(State state) {
		// TODO Auto-generated method stub
		stateType.setId(state.getGmlID());
		stateType.getName().add(createCodeType(state.getGmlID(), null));
        stateType.setDescription(createStringOrRefType(state.getDescription()));

		ArrayList<Transition> connects = state.getTransitionReference();
		if(state.getTransitionReference().size() > 0) {
			for(Transition connect : connects) {
				transitionPropertyType = IGMLFactory.createTransitionPropertyType();
				transitionPropertyType.setHref("#" + connect.getGmlID());
				
				stateType.getConnects().add(transitionPropertyType);
			}
		}
		
		CellSpace duality = state.getDuality();
		if(duality != null) {
			cellSpacePropertyType = IGMLFactory.createCellSpacePropertyType();
			cellSpacePropertyType.setHref("#" + duality.getGmlID());
			
			stateType.setDuality(cellSpacePropertyType);
		}
		
		pointPropertyType = GMLFactory.createPointPropertyType();
		pointType = GMLFactory.createPointType();
		
		visit(state.getPosition());
		
		pointPropertyType.setPoint(pointType);
		stateType.setGeometry(pointPropertyType);
		/*
		if(state.getName() != null) {
			CodeType codeType = GMLFactory.createCodeType();
			codeType.setValue(state.getName());
			
			stateType.getName().add(codeType);
		}*/
	}

	@Override
	public void visit(Edges edges) {
		// TODO Auto-generated method stub
		edgesType.setId(edges.getGmlID());
		edgesType.getName().add(createCodeType(edges.getGmlID(), null));
        edgesType.setDescription(createStringOrRefType(edges.getDescription()));
		
		ArrayList<TransitionOnFloor> transitionOnFloorList = edges.getTransitionOnFloors();
		for(TransitionOnFloor transitionOnFloor : transitionOnFloorList) {
			visit(transitionOnFloor);
		}
	}

	@Override
	public void visit(TransitionOnFloor transitionOnFloor) {
		// TODO Auto-generated method stub
		ArrayList<Transition> transitionList = transitionOnFloor.getTransitionMember();
		
		for(Transition transition : transitionList) {
			transitionMemberType = IGMLFactory.createTransitionMemberType();
			transitionType = IGMLFactory.createTransitionType();
			
			visit(transition);
			
			transitionMemberType.setTransition(transitionType);
			edgesType.getTransitionMember().add(transitionMemberType);
		}
	}

	@Override
	public void visit(Transition transition) {
		// TODO Auto-generated method stub
		transitionType.setId(transition.getGmlID());
		transitionType.getName().add(createCodeType(transition.getGmlID(), null));
        transitionType.setDescription(createStringOrRefType(transition.getDescription()));
		
		State[] states = transition.getStates();
		for(State state : states) {
			StatePropertyType statePropertyType = IGMLFactory.createStatePropertyType();
			statePropertyType.setHref("#" + state.getGmlID());
			
			transitionType.getConnects().add(statePropertyType);
		}
		
		CellSpaceBoundary duality = transition.getDuality();
		if(duality != null) {
			CellSpaceBoundaryPropertyType cellSpaceBoundaryPropertyType = IGMLFactory.createCellSpaceBoundaryPropertyType();
			cellSpaceBoundaryPropertyType.setHref("#" + duality.getGmlID());
			
			transitionType.setDuality(cellSpaceBoundaryPropertyType);			
		}
		
		transitionType.setWeight(transition.getWeight());
		
		curvePropertyType = GMLFactory.createCurvePropertyType();
		visit(transition.getPath());
		transitionType.setGeometry(curvePropertyType);
		/*
		if(transition.getName() != null) {
			CodeType codeType = GMLFactory.createCodeType();
			codeType.setValue(transition.getName());
			
			transitionType.getName().add(codeType);
		}
		*/
	}

	@Override
	public void visit(InterEdges interEdges) {
		// TODO Auto-generated method stub
		interEdgesType.setId(interEdges.getGmlID());
		interEdgesType.getName().add(createCodeType(interEdges.getGmlID(), null));
        interEdgesType.setDescription(createStringOrRefType(interEdges.getDescription()));
		
		ArrayList<InterLayerConnection> interLayerConnectionList = interEdges.getInterLayerConnectionMember();
		for(InterLayerConnection interLayerConnection : interLayerConnectionList) {
			interLayerConnectionMemberType = IGMLFactory.createInterLayerConnectionMemberType();
			interLayerConnectionType = IGMLFactory.createInterLayerConnectionType();
			
			visit(interLayerConnection);
			
			interLayerConnectionMemberType.setInterLayerConnection(interLayerConnectionType);
			interEdgesType.getInterLayerConnectionMember().add(interLayerConnectionMemberType);
		}
	}

	@Override
	public void visit(InterLayerConnection interLayerConnection) {
		// TODO Auto-generated method stub
		interLayerConnectionType.setId(interLayerConnection.getGmlID());
		interLayerConnectionType.getName().add(createCodeType(interLayerConnection.getGmlID(), null));
        interLayerConnectionType.setDescription(createStringOrRefType(interLayerConnection.getDescription()));
		interLayerConnectionType.setTypeOfTopoExpression(interLayerConnection.getTopology());
		interLayerConnectionType.setComment(interLayerConnection.getComment());
		
		State[] interConnects = interLayerConnection.getInterConnects();
		for(State state : interConnects) {
			StatePropertyType statePropertyType = IGMLFactory.createStatePropertyType();
			statePropertyType.setHref("#" + state.getGmlID());
			
			interLayerConnectionType.getInterConnects().add(statePropertyType);
		}
		
		SpaceLayer[] connectedLayers = interLayerConnection.getConnectedLayers();
		for(SpaceLayer spaceLayer : connectedLayers) {
			SpaceLayerPropertyType spaceLayerPropertyType = IGMLFactory.createSpaceLayerPropertyType();
			spaceLayerPropertyType.setHref("#" + spaceLayer.getGmlID());
			interLayerConnectionType.getConnectedLayers().add(spaceLayerPropertyType);
		}
	}

	@Override
	public void visit(Point point) {
		// TODO Auto-generated method stub
		pointType.setId(point.getGMLID());
		pointType.getName().add(createCodeType(point.getGMLID(), null));
		
		DirectPositionType directPositionType = GMLFactory.createDirectPositionType();
		directPositionType.getValue().add(point.getRealX());
		directPositionType.getValue().add(point.getRealY());
		directPositionType.getValue().add(point.getZ());
		
		pointType.setPos(directPositionType);
	}
	
	@Override
	public void visit(LineString lineString) {
		// TODO Auto-generated method stub
		if(is3DGeometry && lineString.getxLinkGeometry() != null) {
			OrientableCurveType orientableCurveType = GMLFactory.createOrientableCurveType();
			CurvePropertyType baseCurvePropertyType = GMLFactory.createCurvePropertyType();
			
			baseCurvePropertyType.setHref("#" + lineString.getxLinkGeometry().getGMLID());
			orientableCurveType.setBaseCurve(baseCurvePropertyType);
			if(lineString.getIsReversed()) {
				orientableCurveType.setOrientation(SignType.VALUE_1);
			} else {
				orientableCurveType.setOrientation(SignType.VALUE_2);
			} 
			JAXBElement<OrientableCurveType> jOrientableCurveType = GMLFactory.createOrientableCurve(orientableCurveType);
			curvePropertyType.setAbstractCurve(jOrientableCurveType);
		} else {
			LineStringType lineStringType = GMLFactory.createLineStringType();
			lineStringType.setId(lineString.getGMLID());
			//lineStringType.getName().add(createCodeType(lineString.getGMLID(), null));
			ArrayList<Point> points = lineString.getPoints();
			for(int i = 0; i < points.size(); i++) {
	                        Point point = points.get(i);
	                        /*if(i != 0 && points.get(i - 1).getRealX() == points.get(i).getRealX()
	                                && points.get(i - 1).getRealY() == points.get(i).getRealY()
	                                && points.get(i - 1).getZ() == points.get(i).getZ()) 
	                            continue;*/
				DirectPositionType directPositionType = GMLFactory.createDirectPositionType();
				directPositionType.getValue().add(point.getRealX());
				directPositionType.getValue().add(point.getRealY());
				directPositionType.getValue().add(point.getZ());
				
				JAXBElement<DirectPositionType> jPosition = GMLFactory.createPos(directPositionType);
				lineStringType.getPosOrPointPropertyOrPointRep().add(jPosition);
			}
			
			JAXBElement<LineStringType> jAbstractCurve = GMLFactory.createLineString(lineStringType);
			curvePropertyType.setAbstractCurve(jAbstractCurve);
		}
		
	}
	
	@Override
	public void visit(LinearRing linearRing) {
		// TODO Auto-generated method stub
		LinearRingType linearRingType = GMLFactory.createLinearRingType();
		ArrayList<Point> points = linearRing.getPoints();
		for(int i = 0; i < points.size(); i++) {
		        Point point = points.get(i);
		        /*if(i != 0 && points.get(i - 1).getRealX() == points.get(i).getRealX()
                                && points.get(i - 1).getRealY() == points.get(i).getRealY()
                                && points.get(i - 1).getZ() == points.get(i).getZ()) 
                            continue;*/
			DirectPositionType directPositonType = GMLFactory.createDirectPositionType();
			directPositonType.getValue().add(point.getRealX());
			directPositonType.getValue().add(point.getRealY());
			directPositonType.getValue().add(point.getZ());
			
			JAXBElement<DirectPositionType> jPosition = GMLFactory.createPos(directPositonType);
			linearRingType.getPosOrPointPropertyOrPointRep().add(jPosition);
		}
		
		JAXBElement<LinearRingType> jExteriorRing = GMLFactory.createLinearRing(linearRingType);
		abstractRingPropertyType.setAbstractRing(jExteriorRing);
	}

	@Override
	public void visit(Polygon _polygon) {
		// TODO Auto-generated method stub
	    
	        Polygon polygon = null;
	        if(_polygon.getxLinkGeometry() != null) {
	                System.out.println("-------Orientable Surface---------");
	                polygon = new Polygon();
	                Polygon xlinkGeometry = (Polygon) _polygon.getxLinkGeometry();
	                if(!_polygon.getIsReversed()) {
    	                        ArrayList<Point> points = xlinkGeometry.getExteriorRing().getPoints();
                                ArrayList<Point> newPoints = new ArrayList<Point>();
                                for(int i = 0; i < points.size(); i++) {
                                        Point newPoint = points.get(i).clone();
                                        newPoint.setRealX(points.get(i).getRealX());
                                        newPoint.setRealY(points.get(i).getRealY());
                                        newPoints.add(newPoint);
                                }
                                LinearRing linearRing = new LinearRing();
                                linearRing.setPoints(newPoints);
                                polygon.setExteriorRing(linearRing);
	                } else {
	                        ArrayList<Point> points = xlinkGeometry.getExteriorRing().getPoints();
	                        ArrayList<Point> newPoints = new ArrayList<Point>();
	                        for(int i = points.size() - 1; i >= 0; i--) {
	                                newPoints.add(points.get(i).clone());
	                        }
	                        LinearRing linearRing = new LinearRing();
	                        linearRing.setPoints(newPoints);
	                        polygon.setExteriorRing(linearRing);
	                }
	                
	        } else {
	                polygon = _polygon;
	        }
	        PolygonType polygonType = GMLFactory.createPolygonType();
                polygonType.setId(polygon.getGMLID());
                //polygonType.getName().add(createCodeType(polygon.getGMLID(), null));
                
                // exterior
                abstractRingPropertyType = GMLFactory.createAbstractRingPropertyType();
                visit(polygon.getExteriorRing());
                polygonType.setExterior(abstractRingPropertyType);
                
                // interior
                ArrayList<LinearRing> interiorRings = polygon.getInteriorRing();
                for(LinearRing interiorRing : interiorRings) {
                        abstractRingPropertyType = GMLFactory.createAbstractRingPropertyType();                 
                        visit(interiorRing);
                        polygonType.getInterior().add(abstractRingPropertyType);
                }
                JAXBElement<PolygonType> jPolygonType = GMLFactory.createPolygon(polygonType);
                surfacePropertyType.setAbstractSurface(jPolygonType);
		/*if(polygon.getxLinkGeometry() != null) {
			OrientableSurfaceType orientableSurfaceType = GMLFactory.createOrientableSurfaceType();
			SurfacePropertyType baseSurfacePropertyType = GMLFactory.createSurfacePropertyType();
			
			baseSurfacePropertyType.setHref("#" + polygon.getxLinkGeometry().getGMLID());
			orientableSurfaceType.setBaseSurface(baseSurfacePropertyType);
			if(polygon.getIsReversed()) {
				orientableSurfaceType.setOrientation(SignType.VALUE_1);
			} else {
				orientableSurfaceType.setOrientation(SignType.VALUE_2);
			} 
			JAXBElement<OrientableSurfaceType> jOrientableSurfaceType = GMLFactory.createOrientableSurface(orientableSurfaceType);
			surfacePropertyType.setAbstractSurface(jOrientableSurfaceType);
		} else {
			PolygonType polygonType = GMLFactory.createPolygonType();
			polygonType.setId(polygon.getGMLID());
			//polygonType.getName().add(createCodeType(polygon.getGMLID(), null));
			
			// exterior
			abstractRingPropertyType = GMLFactory.createAbstractRingPropertyType();
			visit(polygon.getExteriorRing());
			polygonType.setExterior(abstractRingPropertyType);
			
			// interior
			ArrayList<LinearRing> interiorRings = polygon.getInteriorRing();
			for(LinearRing interiorRing : interiorRings) {
				abstractRingPropertyType = GMLFactory.createAbstractRingPropertyType();			
				visit(interiorRing);
				polygonType.getInterior().add(abstractRingPropertyType);
			}
			JAXBElement<PolygonType> jPolygonType = GMLFactory.createPolygon(polygonType);
			surfacePropertyType.setAbstractSurface(jPolygonType);
		}*/
	}

	@Override
	public void visit(Shell shell) {
		// TODO Auto-generated method stub
		ShellType shellType = GMLFactory.createShellType();
		ArrayList<Polygon> surfaceMember = shell.getSurfaceMember();
		for(Polygon polygon : surfaceMember) {
			surfacePropertyType = GMLFactory.createSurfacePropertyType();			
			visit(polygon);
			shellType.getSurfaceMember().add(surfacePropertyType);
		}
		/*Polygon polygon = surfaceMember.get(surfaceMember.size() - 1);
		surfacePropertyType = GMLFactory.createSurfacePropertyType();                 
                visit(polygon);
                shellType.getSurfaceMember().add(surfacePropertyType);*/
		
		shellPropertyType.setShell(shellType);
	}

	@Override
	public void visit(Solid solid) {
		// TODO Auto-generated method stub
		SolidType solidType = GMLFactory.createSolidType();
		solidType.setId(solid.getGMLID());
		//solidType.getName().add(createCodeType(solid.getGMLID(), null));
		
		// exteior
		shellPropertyType = GMLFactory.createShellPropertyType();
		visit(solid.getExteriorShell());
		solidType.setExterior(shellPropertyType);
		
		// interior
		
		JAXBElement<SolidType> jSolidType = GMLFactory.createSolid(solidType);
		solidPropertyType.setAbstractSolid(jSolidType);
	}
	
	public CodeType createCodeType(String name, String codeSpace) {
	        CodeType codeType = null;
	        
	        if(name == null && codeSpace == null)
	                return codeType;
	        else
	            codeType = GMLFactory.createCodeType();
	        
		if(name != null) {
			codeType.setValue(name);
		}
		if(codeSpace != null) {
			codeType.setCodeSpace(codeSpace);
		}
		
		return codeType;
	}
	
	public StringOrRefType createStringOrRefType(String value) {
	        StringOrRefType strOrRefType = null;
	        
	        if(value == null)
	                return strOrRefType;
	        else
	                strOrRefType = GMLFactory.createStringOrRefType();
	        
	        strOrRefType.setValue(value);
	        return strOrRefType;
	}

}
