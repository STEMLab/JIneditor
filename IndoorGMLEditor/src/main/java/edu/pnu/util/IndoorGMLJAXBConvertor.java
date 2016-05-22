package edu.pnu.util;

import java.util.ArrayList;

import javax.xml.bind.JAXBElement;

import net.opengis.gml.v_3_2_1.AbstractFeatureType;
import net.opengis.gml.v_3_2_1.AbstractRingPropertyType;
import net.opengis.gml.v_3_2_1.CodeType;
import net.opengis.gml.v_3_2_1.CurvePropertyType;
import net.opengis.gml.v_3_2_1.DirectPositionType;
import net.opengis.gml.v_3_2_1.FeaturePropertyType;
import net.opengis.gml.v_3_2_1.LineStringType;
import net.opengis.gml.v_3_2_1.LinearRingType;
import net.opengis.gml.v_3_2_1.OrientableCurveType;
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
import net.opengis.indoorgml.core.v_1_0.CellSpaceBoundaryPropertyType;
import net.opengis.indoorgml.core.v_1_0.CellSpaceBoundaryType;
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

public class IndoorGMLJAXBConvertor {
	private ObjectFactory IGMLFactory;
	private net.opengis.gml.v_3_2_1.ObjectFactory GMLFactory;
	
	private boolean is3DGeometry;
	private IndoorFeatures indoorFeatures;
	
	public IndoorGMLJAXBConvertor(IndoorFeatures indoorFeatures, boolean is3DGeometry) {
		this.IGMLFactory = new net.opengis.indoorgml.core.v_1_0.ObjectFactory();
		this.GMLFactory = new net.opengis.gml.v_3_2_1.ObjectFactory();
		
		this.indoorFeatures = indoorFeatures;
		this.is3DGeometry = is3DGeometry;
	}
	
	public JAXBElement<IndoorFeaturesType> getJAXBElement() {
		IndoorFeaturesType indoorFeaturesType = createIndoorFeaturesType(null, indoorFeatures);
		JAXBElement<IndoorFeaturesType> je = IGMLFactory.createIndoorFeatures(indoorFeaturesType);
		
		return je;
	}

	private IndoorFeaturesType createIndoorFeaturesType(IndoorFeaturesType target, IndoorFeatures indoorFeatures) {
		if(target == null) {
			target = IGMLFactory.createIndoorFeaturesType();
		}
		target.setId(indoorFeatures.getGmlID());
		target.getName().add(createCodeType(null, indoorFeatures.getGmlID(), null));
		
		MultiLayeredGraphType multiLayeredGraphType = createMultiLayeredGraphType(null, indoorFeatures.getMultiLayeredGraph());
		target.setMultiLayeredGraph(multiLayeredGraphType);
		
		PrimalSpaceFeaturesType primalSpaceFeaturesType = createPrimalSpaceFeaturesType(null, indoorFeatures.getPrimalSpaceFeatures());
		PrimalSpaceFeaturesPropertyType primalSpaceFeaturesPropertyType = IGMLFactory.createPrimalSpaceFeaturesPropertyType();
		primalSpaceFeaturesPropertyType.setPrimalSpaceFeatures(primalSpaceFeaturesType);
		target.setPrimalSpaceFeatures(primalSpaceFeaturesPropertyType);
		
		return target;
	}

	private PrimalSpaceFeaturesType createPrimalSpaceFeaturesType(PrimalSpaceFeaturesType target, PrimalSpaceFeatures primalSpaceFeatures) {
		if(target == null) {
			target = IGMLFactory.createPrimalSpaceFeaturesType();
		}
		target.setId(primalSpaceFeatures.getGmlID());
		target.getName().add(createCodeType(null, primalSpaceFeatures.getGmlID(), null));
		
		ArrayList<CellSpaceOnFloor> cellSpaceOnFloors = primalSpaceFeatures.getCellSpaceOnFloors();
		for(CellSpaceOnFloor cellSpaceOnFloor : cellSpaceOnFloors) {
			target = createCellSpaceType(target, cellSpaceOnFloor);
		}
		
		ArrayList<CellSpaceBoundaryOnFloor> cellSpaceBoundaryOnFloors = primalSpaceFeatures.getCellSpaceBoundaryOnFloors();
		for(CellSpaceBoundaryOnFloor cellSpaceBoundaryOnFloor : cellSpaceBoundaryOnFloors) {
			target = createCellSpaceBoundaryType(target, cellSpaceBoundaryOnFloor);
		}
		
		return target;
	}

	private PrimalSpaceFeaturesType createCellSpaceType(PrimalSpaceFeaturesType target, CellSpaceOnFloor cellSpaceOnFloor) {
		ArrayList<CellSpace> cellSpaceMember = cellSpaceOnFloor.getCellSpaceMember();
		for(CellSpace cellSpace : cellSpaceMember) {
			//cellSpaceMemberType = IGMLFactory.createCellSpaceMemberType();
			CellSpaceType cellSpaceType = createCellSpaceType(null, cellSpace);
			
			//cellSpaceMemberType.setCellSpace(cellSpaceType);
			FeaturePropertyType featurePropertyType = GMLFactory.createFeaturePropertyType();
			JAXBElement<CellSpaceType> jCellSpaceType = IGMLFactory.createCellSpace(cellSpaceType);
			featurePropertyType.setAbstractFeature(jCellSpaceType);
			target.getCellSpaceMember().add(featurePropertyType);
		}
		
		return target;
	}

	private CellSpaceType createCellSpaceType(CellSpaceType target, CellSpace cellSpace) {
		if(target == null) {
			target = IGMLFactory.createCellSpaceType();
		}
		target.setId(cellSpace.getGmlID());
		target.getName().add(createCodeType(null, cellSpace.getGmlID(), null));
		target.setDescription(createStringOrRefType(null, cellSpace.getDescription()));

		State duality = cellSpace.getDuality();
		if(duality != null) {
			StatePropertyType statePropertyType = IGMLFactory.createStatePropertyType();
			statePropertyType.setHref("#" + duality.getGmlID());
			target.setDuality(statePropertyType);
		}
		
		ArrayList<CellSpaceBoundary> partialBoundedBy = cellSpace.getPartialBoundedBy();
		for(CellSpaceBoundary cellSpaceBoundary : partialBoundedBy) {
			if(is3DGeometry && cellSpaceBoundary.getGeometry3D() == null) continue;
			else if(!is3DGeometry && cellSpaceBoundary.getGeometry2D() == null) continue;
			CellSpaceBoundaryPropertyType cellSpaceBoundaryPropertyType = IGMLFactory.createCellSpaceBoundaryPropertyType();
			cellSpaceBoundaryPropertyType.setHref("#" + cellSpaceBoundary.getGmlID());
			
			target.getPartialboundedBy().add(cellSpaceBoundaryPropertyType);
		}
		
		
		if(is3DGeometry) {
			// geometry3D solid 
			System.out.println(cellSpace.getGmlID());
			SolidPropertyType solidPropertyType = createSolidPropertyType(null, cellSpace.getGeometry3D());

			target.setGeometry3D(solidPropertyType);
		} else {
			// geometry2D only polygon
			SurfacePropertyType surfacePropertyType = createSurfacePropertyType(null, cellSpace.getGeometry2D());
			target.setGeometry2D(surfacePropertyType);
		}
		// ExternalReference
		
		return target;
	}

	private PrimalSpaceFeaturesType createCellSpaceBoundaryType(PrimalSpaceFeaturesType target, CellSpaceBoundaryOnFloor cellSpaceBoundaryOnFloor) {
		ArrayList<CellSpaceBoundary> cellSpaceBoundaryMember = cellSpaceBoundaryOnFloor.getCellSpaceBoundaryMember();
		for(CellSpaceBoundary cellSpaceBoundary : cellSpaceBoundaryMember) {
			//cellSpaceBoundaryMemberType = IGMLFactory.createCellSpaceBoundaryMemberType();
			if(is3DGeometry && cellSpaceBoundary.getGeometry3D() == null) continue;
			else if(!is3DGeometry && cellSpaceBoundary.getGeometry2D() == null) continue;
			
			CellSpaceBoundaryType cellSpaceBoundaryType = createCellSpaceBoundaryType(null, cellSpaceBoundary);
						
			//cellSpaceBoundaryMemberType.setCellSpaceBoundary(cellSpaceBoundaryType);
			FeaturePropertyType featurePropertyType = GMLFactory.createFeaturePropertyType();
			JAXBElement<CellSpaceBoundaryType> jCellSpaceBoundaryType = IGMLFactory.createCellSpaceBoundary(cellSpaceBoundaryType);
			featurePropertyType.setAbstractFeature(jCellSpaceBoundaryType);
			target.getCellSpaceBoundaryMember().add(featurePropertyType);
		}
		
		return target;
	}

	private CellSpaceBoundaryType createCellSpaceBoundaryType(CellSpaceBoundaryType target, CellSpaceBoundary cellSpaceBoundary) {
		if(target == null) {
			target = IGMLFactory.createCellSpaceBoundaryType();
		}
		target.setId(cellSpaceBoundary.getGmlID());
		target.getName().add(createCodeType(null, cellSpaceBoundary.getGmlID(), null));
		target.setDescription(createStringOrRefType(null, cellSpaceBoundary.getDescription()));

		Transition duality = cellSpaceBoundary.getDuality();
		if(duality != null) {
			TransitionPropertyType transitionPropertyType = IGMLFactory.createTransitionPropertyType();
			transitionPropertyType.setHref("#" + duality.getGmlID());
			target.setDuality(transitionPropertyType);
		}
		
		if(is3DGeometry) {
			// geometry3D solid
			SurfacePropertyType surfacePropertyType = createSurfacePropertyType(null, cellSpaceBoundary.getGeometry3D());
			target.setGeometry3D(surfacePropertyType);
		} else {
			// geometry2D only polygon
			CurvePropertyType curvePropertyType = createCurvePropertyType(null, cellSpaceBoundary.getGeometry2D());
			target.setGeometry2D(curvePropertyType);
		}
		// ExternalReference
		
		return target;
	}

	private MultiLayeredGraphType createMultiLayeredGraphType(MultiLayeredGraphType target, MultiLayeredGraph multiLayeredGraph) {
		if(target == null) {
			target = IGMLFactory.createMultiLayeredGraphType();
		}
		target.setId(multiLayeredGraph.getGmlID());
		target.getName().add(createCodeType(null, multiLayeredGraph.getName(), null));
		target.setDescription(createStringOrRefType(null, multiLayeredGraph.getDescription()));
		
		ArrayList<SpaceLayers> spaceLayersList = multiLayeredGraph.getSpaceLayers();
		for(SpaceLayers spaceLayers : spaceLayersList) {
			SpaceLayersType spaceLayersType = createSpaceLayersType(null, spaceLayers);
			target.getSpaceLayers().add(spaceLayersType);
		}
		
		ArrayList<InterEdges> interEdgesList = multiLayeredGraph.getInterEdges();
		for(InterEdges interEdges : interEdgesList) {
			InterEdgesType interEdgesType = createInterEdgesType(null, interEdges);
			target.getInterEdges().add(interEdgesType);
		}
		
		return target;
	}

	private SpaceLayersType createSpaceLayersType(SpaceLayersType target, SpaceLayers spaceLayers) {
		if(target == null) {
			target = IGMLFactory.createSpaceLayersType();
		}
		target.setId(spaceLayers.getGmlID());
		target.getName().add(createCodeType(null, spaceLayers.getGmlID(), null));
		target.setDescription(createStringOrRefType(null, spaceLayers.getDescription()));
		
		ArrayList<SpaceLayer> spaceLayerList = spaceLayers.getSpaceLayerMember();
		for(SpaceLayer spaceLayer : spaceLayerList) {
			SpaceLayerMemberType spaceLayerMemberType = IGMLFactory.createSpaceLayerMemberType();
			SpaceLayerType spaceLayerType = createSpaceLayerType(null, spaceLayer);
			
			spaceLayerMemberType.setSpaceLayer(spaceLayerType);			
			target.getSpaceLayerMember().add(spaceLayerMemberType);
		}
		
		return target;
	}

	private SpaceLayerType createSpaceLayerType(SpaceLayerType target, SpaceLayer spaceLayer) {
		if(target == null) {
			target = IGMLFactory.createSpaceLayerType();
		}
		target.setId(spaceLayer.getGmlID());
		target.getName().add(createCodeType(null, spaceLayer.getGmlID(), null));
        target.setDescription(createStringOrRefType(null, spaceLayer.getDescription()));
		
		ArrayList<Nodes> nodesList = spaceLayer.getNodes();
		for(Nodes nodes : nodesList) {
			NodesType nodesType = createNodesType(null, nodes);
			
			target.getNodes().add(nodesType);
		}
		
		ArrayList<Edges> edgesList = spaceLayer.getEdges();
		for(Edges edges : edgesList) {
			EdgesType edgesType = createEdgesType(null, edges);
			
			target.getEdges().add(edgesType);
		}
		
		return target;
	}

	private NodesType createNodesType(NodesType target, Nodes nodes) {
		if(target == null) {
			target = IGMLFactory.createNodesType();
		}
		target.setId(nodes.getGmlID());
		target.getName().add(createCodeType(null, nodes.getGmlID(), null));
        target.setDescription(createStringOrRefType(null, nodes.getDescription()));
		
		ArrayList<StateOnFloor> stateOnFloorList = nodes.getStateOnFloors();
		for(StateOnFloor stateOnFloor : stateOnFloorList) {
			target = createStateType(target, stateOnFloor);
		}
		
		return target;
	}

	private NodesType createStateType(NodesType target, StateOnFloor stateOnFloor) {
		ArrayList<State> stateList = stateOnFloor.getStateMember();
		for(State state : stateList) {
			StateMemberType stateMemberType = IGMLFactory.createStateMemberType();
			StateType stateType = createStateType(null, state);
			
			stateMemberType.setState(stateType);
			target.getStateMember().add(stateMemberType);
		}
		
		return target;
	}

	private StateType createStateType(StateType target, State state) {
		if(target == null) {
			target = IGMLFactory.createStateType();
		}
		target.setId(state.getGmlID());
		target.getName().add(createCodeType(null, state.getGmlID(), null));
        target.setDescription(createStringOrRefType(null, state.getDescription()));

		ArrayList<Transition> connects = state.getTransitionReference();
		if(state.getTransitionReference().size() > 0) {
			for(Transition connect : connects) {
				TransitionPropertyType transitionPropertyType = IGMLFactory.createTransitionPropertyType();
				transitionPropertyType.setHref("#" + connect.getGmlID());
				
				target.getConnects().add(transitionPropertyType);
			}
		}
		
		CellSpace duality = state.getDuality();
		if(duality != null) {
			CellSpacePropertyType cellSpacePropertyType = IGMLFactory.createCellSpacePropertyType();
			cellSpacePropertyType.setHref("#" + duality.getGmlID());
			
			target.setDuality(cellSpacePropertyType);
		}
		
		PointPropertyType pointPropertyType = createPointPropertyType(null, state.getPosition());
		target.setGeometry(pointPropertyType);
		/*
		if(state.getName() != null) {
			CodeType codeType = GMLFactory.createCodeType();
			codeType.setValue(state.getName());
			
			stateType.getName().add(codeType);
		}*/
		
		return target;
	}

	private EdgesType createEdgesType(EdgesType target, Edges edges) {
		if(target == null) {
			target = IGMLFactory.createEdgesType();
		}
		target.setId(edges.getGmlID());
		target.getName().add(createCodeType(null, edges.getGmlID(), null));
        target.setDescription(createStringOrRefType(null, edges.getDescription()));
		
		ArrayList<TransitionOnFloor> transitionOnFloorList = edges.getTransitionOnFloors();
		for(TransitionOnFloor transitionOnFloor : transitionOnFloorList) {
			target = createTransitionType(target, transitionOnFloor);
		}
		
		return target;
	}

	private EdgesType createTransitionType(EdgesType target, TransitionOnFloor transitionOnFloor) {
		// TODO Auto-generated method stub
		ArrayList<Transition> transitionList = transitionOnFloor.getTransitionMember();
		
		for(Transition transition : transitionList) {
			TransitionMemberType transitionMemberType = IGMLFactory.createTransitionMemberType();
			TransitionType transitionType = createTransitionType(null, transition);
			
			transitionMemberType.setTransition(transitionType);
			target.getTransitionMember().add(transitionMemberType);
		}
		
		return target;
	}

	private TransitionType createTransitionType(TransitionType target, Transition transition) {
		if(target == null) {
			target = IGMLFactory.createTransitionType();
		}
		target.setId(transition.getGmlID());
		target.getName().add(createCodeType(null, transition.getGmlID(), null));
        target.setDescription(createStringOrRefType(null, transition.getDescription()));
		
		State[] states = transition.getStates();
		for(State state : states) {
			StatePropertyType statePropertyType = IGMLFactory.createStatePropertyType();
			statePropertyType.setHref("#" + state.getGmlID());
			
			target.getConnects().add(statePropertyType);
		}
		
		CellSpaceBoundary duality = transition.getDuality();
		if(duality != null) {
			CellSpaceBoundaryPropertyType cellSpaceBoundaryPropertyType = IGMLFactory.createCellSpaceBoundaryPropertyType();
			cellSpaceBoundaryPropertyType.setHref("#" + duality.getGmlID());
			
			target.setDuality(cellSpaceBoundaryPropertyType);			
		}
		
		target.setWeight(transition.getWeight());
		
		CurvePropertyType curvePropertyType = createCurvePropertyType(null, transition.getPath());
		target.setGeometry(curvePropertyType);
		/*
		if(transition.getName() != null) {
			CodeType codeType = GMLFactory.createCodeType();
			codeType.setValue(transition.getName());
			
			transitionType.getName().add(codeType);
		}
		*/
		
		return target;
	}

	private InterEdgesType createInterEdgesType(InterEdgesType target, InterEdges interEdges) {
		if(target == null) {
			target = IGMLFactory.createInterEdgesType();
		}
		target.setId(interEdges.getGmlID());
		target.getName().add(createCodeType(null, interEdges.getGmlID(), null));
        target.setDescription(createStringOrRefType(null, interEdges.getDescription()));
		
		ArrayList<InterLayerConnection> interLayerConnectionList = interEdges.getInterLayerConnectionMember();
		for(InterLayerConnection interLayerConnection : interLayerConnectionList) {
			InterLayerConnectionMemberType interLayerConnectionMemberType = IGMLFactory.createInterLayerConnectionMemberType();
			InterLayerConnectionType interLayerConnectionType = createInterLayerConnectionType(null, interLayerConnection);
			
			interLayerConnectionMemberType.setInterLayerConnection(interLayerConnectionType);
			target.getInterLayerConnectionMember().add(interLayerConnectionMemberType);
		}
		
		return target;
	}

	private InterLayerConnectionType createInterLayerConnectionType(InterLayerConnectionType target, InterLayerConnection interLayerConnection) {
		if(target == null) {
			target = IGMLFactory.createInterLayerConnectionType();
		}
		target.setId(interLayerConnection.getGmlID());
		target.getName().add(createCodeType(null, interLayerConnection.getGmlID(), null));
        target.setDescription(createStringOrRefType(null, interLayerConnection.getDescription()));
		target.setTypeOfTopoExpression(interLayerConnection.getTopology());
		target.setComment(interLayerConnection.getComment());
		
		State[] interConnects = interLayerConnection.getInterConnects();
		for(State state : interConnects) {
			StatePropertyType statePropertyType = IGMLFactory.createStatePropertyType();
			statePropertyType.setHref("#" + state.getGmlID());
			
			target.getInterConnects().add(statePropertyType);
		}
		
		SpaceLayer[] connectedLayers = interLayerConnection.getConnectedLayers();
		for(SpaceLayer spaceLayer : connectedLayers) {
			SpaceLayerPropertyType spaceLayerPropertyType = IGMLFactory.createSpaceLayerPropertyType();
			spaceLayerPropertyType.setHref("#" + spaceLayer.getGmlID());
			target.getConnectedLayers().add(spaceLayerPropertyType);
		}
		
		return target;
	}

	private PointPropertyType createPointPropertyType(PointPropertyType target, Point point) {
		if(target == null) {
			target = GMLFactory.createPointPropertyType();
		}
		PointType pointType = GMLFactory.createPointType();
		pointType.setId(point.getGMLID());
		pointType.getName().add(createCodeType(null, point.getGMLID(), null));
		
		DirectPositionType directPositionType = GMLFactory.createDirectPositionType();
		directPositionType.getValue().add(point.getRealX());
		directPositionType.getValue().add(point.getRealY());
		directPositionType.getValue().add(point.getZ());
		
		pointType.setPos(directPositionType);
		target.setPoint(pointType);
		return target;
	}
	
	private CurvePropertyType createCurvePropertyType(CurvePropertyType target, LineString lineString) {
		if(target == null) {
			target = GMLFactory.createCurvePropertyType();
		}
		
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
			target.setAbstractCurve(jOrientableCurveType);
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
			target.setAbstractCurve(jAbstractCurve);
		}
		
		return target;
	}
	
	private AbstractRingPropertyType createAbstractRingPropertyType(AbstractRingPropertyType target, LinearRing linearRing) {
		if(target == null) {
			target = GMLFactory.createAbstractRingPropertyType();
		}
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
		target.setAbstractRing(jExteriorRing);
		
		return target;
	}

	private SurfacePropertyType createSurfacePropertyType(SurfacePropertyType target, Polygon _polygon) {
		if(target == null) {
			target = GMLFactory.createSurfacePropertyType();
		}
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
        AbstractRingPropertyType abstractRingPropertyType = createAbstractRingPropertyType(null, polygon.getExteriorRing());
        polygonType.setExterior(abstractRingPropertyType);
        
        // interior
        ArrayList<LinearRing> interiorRings = polygon.getInteriorRing();
        for(LinearRing interiorRing : interiorRings) {
                abstractRingPropertyType = createAbstractRingPropertyType(null, interiorRing);
                polygonType.getInterior().add(abstractRingPropertyType);
        }
        JAXBElement<PolygonType> jPolygonType = GMLFactory.createPolygon(polygonType);
        target.setAbstractSurface(jPolygonType);
        
        return target;
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

	private ShellPropertyType createShellPropertyType(ShellPropertyType target, Shell shell) {
		if(target == null) {
			target = GMLFactory.createShellPropertyType();
		}
		ShellType shellType = GMLFactory.createShellType();
		ArrayList<Polygon> surfaceMember = shell.getSurfaceMember();
		for(Polygon polygon : surfaceMember) {
			SurfacePropertyType surfacePropertyType = createSurfacePropertyType(null, polygon);
			shellType.getSurfaceMember().add(surfacePropertyType);
		}
		/*Polygon polygon = surfaceMember.get(surfaceMember.size() - 1);
		surfacePropertyType = GMLFactory.createSurfacePropertyType();                 
                visit(polygon);
                shellType.getSurfaceMember().add(surfacePropertyType);*/
		
		target.setShell(shellType);
		return target;
	}

	private SolidPropertyType createSolidPropertyType(SolidPropertyType target, Solid solid) {
		// TODO Auto-generated method stub
		if(target == null) {
			target = GMLFactory.createSolidPropertyType();
		}
		SolidType solidType = GMLFactory.createSolidType();
		solidType.setId(solid.getGMLID());
		//solidType.getName().add(createCodeType(solid.getGMLID(), null));
		
		// exteior
		ShellPropertyType shellPropertyType = createShellPropertyType(null, solid.getExteriorShell());
		solidType.setExterior(shellPropertyType);
				
		// interior
		
		
		JAXBElement<SolidType> jSolidType = GMLFactory.createSolid(solidType);
		target.setAbstractSolid(jSolidType);
		
		return target;
	}
	
	private CodeType createCodeType(CodeType target, String name, String codeSpace) {
        if(name == null && codeSpace == null)
        	return null;
        
        if(target == null) {
        	target = GMLFactory.createCodeType();
        }
	        
		if(name != null) {
			target.setValue(name);
		}
		if(codeSpace != null) {
			target.setCodeSpace(codeSpace);
		}
		
		return target;
	}
	
	private StringOrRefType createStringOrRefType(StringOrRefType target, String value) {
        if(value == null)
        	return null;
        
        if(target == null) {
        	target= GMLFactory.createStringOrRefType();
        }
        
        target.setValue(value);
        return target;
	}

	private void setDescription(AbstractFeatureType target, String description) {
		
	}
}
