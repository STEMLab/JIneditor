package edu.pnu.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;

import net.opengis.gml.v_3_2_1.AbstractFeatureType;
import net.opengis.gml.v_3_2_1.DirectPositionType;
import net.opengis.gml.v_3_2_1.FeaturePropertyType;
import net.opengis.gml.v_3_2_1.PointType;
import net.opengis.indoorgml.core.AbstractFeature;
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
import net.opengis.indoorgml.core.v_1_0.CellSpaceBoundaryType;
import net.opengis.indoorgml.core.v_1_0.CellSpaceType;
import net.opengis.indoorgml.core.v_1_0.EdgesType;
import net.opengis.indoorgml.core.v_1_0.IndoorFeaturesType;
import net.opengis.indoorgml.core.v_1_0.InterEdgesType;
import net.opengis.indoorgml.core.v_1_0.InterLayerConnectionMemberType;
import net.opengis.indoorgml.core.v_1_0.InterLayerConnectionType;
import net.opengis.indoorgml.core.v_1_0.MultiLayeredGraphType;
import net.opengis.indoorgml.core.v_1_0.NodesType;
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
import net.opengis.indoorgml.core.v_1_0.TransitionType;
import net.opengis.indoorgml.geometry.Point;
import edu.pnu.project.BuildingProperty;
import edu.pnu.project.StateOnFloor;
import edu.pnu.project.TransitionOnFloor;

public class JAXBIndoorGMLConvertor {
	private IndoorFeaturesType indoorFeaturesType;
	private BuildingProperty buildingProperty;
	
	private Map<String, Object> idRegistryMap;
	private Map<String, Object> referenceRegistryMap;
	
	// Unmarshall 할 때 xlink와 duality가있는 객체의 ID를 저장
	// JAXB 객체 내부를 방문하면서 xlink와 duality가 있는 객체에 대해 내부 자료구조로 생성된 객체 저장
	// JAXB 객체 내부 방문이 끝난 후에 한번에 xlink와 duality 추가
	// 기하는 CellSpace, Boundary 기하가 3차원일 경우 2차원으로 변환해서 저장
	// 기하에 xlink가 있을 경우 - Solid의 면이 xlink일 경우는 어차피 없어지므로 상관없다
	// Boundary의 기하가 xlink일 경우는 Solid의 옆면과 동일할 경우
	// Solid의 옆면은 없어질 것이므로 xlink를 지우고 boundary를 정사시킨 2차원 기하로 저장
	//  - 벽과 벽 사이의 Boundary는 하나만 생성되므로 2개의 Boundary가 같은 기하를 xlink로 가지는 경우는 없다.
	//  - 벽에 문이 있을 경우, 보통의 경우 2차원에서는 3개의 기하가 생성(벽-문-벽)되고 3차원에서는 2개의 기하(문, 문 이외의 벽) 생성되는데
	//  - 3차원 기하를 가진 Boundary로부터 2차원 기하를 3개 생성하는 방식을 만들어내야한다.
	// InterLayerConnection은 SpaceLayer가 생성된 후에 생성

	public JAXBIndoorGMLConvertor(IndoorFeaturesType indoorFeaturesType, BuildingProperty buildingProperty) {
		this.indoorFeaturesType = indoorFeaturesType;
		this.buildingProperty = buildingProperty;
		
		this.idRegistryMap = new HashMap<String, Object>();
		this.referenceRegistryMap = new HashMap<String, Object>();
	}
	
	public IndoorFeatures getIndoorFeatures() {
		IndoorFeatures indoorFeatures = createIndoorFeatures(null, indoorFeaturesType);

		// afterTraverse(indoorFeatures);
		
		return indoorFeatures;
	}
	
	private void createAbstractFeature(AbstractFeature target, AbstractFeatureType abstractFeatureType) {
		target.setGmlID(abstractFeatureType.getId());
		if(abstractFeatureType.getName() != null && abstractFeatureType.getName().size() > 0) {
			target.setName(abstractFeatureType.getName().get(0).getValue());
		}
		if(abstractFeatureType.getDescription() != null) {
			target.setName(abstractFeatureType.getDescription().getValue());
		}
	}
	
	public IndoorFeatures createIndoorFeatures(IndoorFeatures target, IndoorFeaturesType indoorFeaturesType) {
		if(target == null) {
			target = new IndoorFeatures();
		}
		createAbstractFeature(target, indoorFeaturesType);
		
		MultiLayeredGraph multiLayeredGraph = createMultiLayeredGraph(null, indoorFeaturesType.getMultiLayeredGraph());
		target.setMultiLayeredGraph(multiLayeredGraph);
		
		PrimalSpaceFeatures primalSpaceFeatures = createPrimalSpaceFeatures(null, indoorFeaturesType.getPrimalSpaceFeatures());
		target.setPrimalSpaceFeatures(primalSpaceFeatures);
		
		return target;
	}

	private MultiLayeredGraph createMultiLayeredGraph(MultiLayeredGraph target, MultiLayeredGraphType multiLayeredGraphType) {
		if(target == null) {
			target = new MultiLayeredGraph();
		}
		createAbstractFeature(target, multiLayeredGraphType);
		
		ArrayList<SpaceLayersType> spaceLayersTypeList = (ArrayList<SpaceLayersType>) multiLayeredGraphType.getSpaceLayers();
		ArrayList<SpaceLayers> spaceLayersList = new ArrayList<SpaceLayers>();
		for(SpaceLayersType spaceLayersType : spaceLayersTypeList) {
			SpaceLayers spaceLayers = createSpaceLayers(null, spaceLayersType);
			spaceLayersList.add(spaceLayers);
		}
		target.setSpaceLayers(spaceLayersList);
		
		ArrayList<InterEdgesType> interEdgesTypeList = (ArrayList<InterEdgesType>) multiLayeredGraphType.getInterEdges();
		ArrayList<InterEdges> interEdgesList = new ArrayList<InterEdges>();
		for(InterEdgesType interEdgesType : interEdgesTypeList) {
			InterEdges interEdges = createInterEdges(null, interEdgesType);
		}
		target.setInterEdges(interEdgesList);
		
		
		return null;
	}

	private SpaceLayers createSpaceLayers(SpaceLayers target, SpaceLayersType spaceLayersType) {
		if(target == null) {
			target = new SpaceLayers();
		}
		createAbstractFeature(target, spaceLayersType);
		
		ArrayList<SpaceLayerMemberType> spaceLayerMemberTypeList = (ArrayList<SpaceLayerMemberType>) spaceLayersType.getSpaceLayerMember();
		ArrayList<SpaceLayer> spaceLayerMemberList = new ArrayList<SpaceLayer>();
		for(SpaceLayerMemberType spaceLayerMemberType : spaceLayerMemberTypeList) {
			SpaceLayerType spaceLayerType = spaceLayerMemberType.getSpaceLayer();
			SpaceLayer spaceLayer = createSpaceLayer(null, spaceLayerType);
			spaceLayerMemberList.add(spaceLayer);
		}
		target.setSpaceLayerMember(spaceLayerMemberList);
		
		return target;
	}

	private SpaceLayer createSpaceLayer(SpaceLayer target, SpaceLayerType spaceLayerType) {
		if(target == null) {
			target = new SpaceLayer();
		}
		createAbstractFeature(target, spaceLayerType);
		
		ArrayList<NodesType> nodesTypeList = (ArrayList<NodesType>) spaceLayerType.getNodes();
		ArrayList<Nodes> nodesList = new ArrayList<Nodes>();
		for(NodesType nodesType : nodesTypeList) {
			Nodes nodes = createNodes(null, nodesType);
			nodesList.add(nodes);
		}
		target.setNodes(nodesList);
		
		ArrayList<EdgesType> edgesTypeList = (ArrayList<EdgesType>) spaceLayerType.getEdges();
		ArrayList<Edges> edgesList = new ArrayList<Edges>();
		for(EdgesType edgesType : edgesTypeList) {
			Edges edges = createEdges(null, edgesType);
			edgesList.add(edges);
		}
		target.setEdges(edgesList);
				
		idRegistryMap.put(target.getGmlID(), target);
		return target;
	}

	private Nodes createNodes(Nodes target, NodesType nodesType) {
		if(target == null) {
			target = new Nodes();
		}
		createAbstractFeature(target, nodesType);
		
		ArrayList<StateMemberType> stateMemberTypeList = (ArrayList<StateMemberType>) nodesType.getStateMember();
		List<State> stateList = new ArrayList<State>();
		for(StateMemberType stateMemberType : stateMemberTypeList) {
			StateType stateType = stateMemberType.getState();
			State state = createState(null, stateType);
			
			stateList.add(state);			
		}
		ArrayList<StateOnFloor> stateOnFloorList = FloorElementBuilder.createStateOnFloors(null, stateList);
		target.setStateOnFloors(stateOnFloorList);
				
		return target;
	}

	private State createState(State target, StateType stateType) {
		if(target == null) {
			target = new State();
		}
		createAbstractFeature(target, stateType);
		
		// state의 duality 처리 필요
		// state의 connects 처리 필요
		stateType.getGeometry();
		
		idRegistryMap.put(target.getGmlID(), target);
		return target;
	}
	
	private Edges createEdges(Edges target, EdgesType edgesType) {
		if(target == null) {
			target = new Edges();
		}
		createAbstractFeature(target, edgesType);
		
		ArrayList<TransitionMemberType> transitionMemberTypeList = (ArrayList<TransitionMemberType>) edgesType.getTransitionMember();
		List<Transition> transitionList = new ArrayList<Transition>();
		for(TransitionMemberType transitionMemberType : transitionMemberTypeList) {
			TransitionType transitionType = transitionMemberType.getTransition();
			Transition transition = createTransition(null, transitionType);
			
			transitionList.add(transition);
		}
		ArrayList<TransitionOnFloor> transitionOnFloorList = FloorElementBuilder.createTransitionOnFloor(null, transitionList);
		target.setTransitionOnFloors(transitionOnFloorList);
		
		return target;
	}
	
	private Transition createTransition(Transition target, TransitionType transitionType) {
		if(target == null) {
			target = new Transition();
		}
		createAbstractFeature(target, transitionType);
		
		
		idRegistryMap.put(target.getGmlID(), target);
		return target;
	}
	
	private InterEdges createInterEdges(InterEdges target, InterEdgesType interEdgesType) {
		if(target == null) {
			target = new InterEdges();
		}
		createAbstractFeature(target, interEdgesType);
		
		ArrayList<InterLayerConnectionMemberType> ilcMemberTypeList = (ArrayList<InterLayerConnectionMemberType>) interEdgesType.getInterLayerConnectionMember();
		ArrayList<InterLayerConnection> ilcList = new ArrayList<InterLayerConnection>();
		for(InterLayerConnectionMemberType ilcMemberType : ilcMemberTypeList) {
			InterLayerConnectionType ilcType = ilcMemberType.getInterLayerConnection();
			InterLayerConnection ilc = createInterLayerConnection(null, ilcType);
			ilcList.add(ilc);
		}
		target.setInterLayerConnectionMember(ilcList);
		
		return target;
	}
	
	private InterLayerConnection createInterLayerConnection(InterLayerConnection target, InterLayerConnectionType ilcType) {
		if(target == null) {
			target = new InterLayerConnection();
		}
		createAbstractFeature(target, ilcType);
		
		ArrayList<SpaceLayerPropertyType> spaceLayerTypeList = (ArrayList<SpaceLayerPropertyType>) ilcType.getConnectedLayers();
		ArrayList<SpaceLayer> spaceLayerList = new ArrayList<SpaceLayer>();
		for(SpaceLayerPropertyType spaceLayerPropertyType : spaceLayerTypeList) {
			SpaceLayerType spaceLayerType = spaceLayerPropertyType.getSpaceLayer();
			SpaceLayer spaceLayer = (SpaceLayer) idRegistryMap.get(spaceLayerType.getId());
			spaceLayerList.add(spaceLayer);
		}
		SpaceLayer[] connectedLayers = new SpaceLayer[2];
		spaceLayerList.toArray(connectedLayers);
		target.setConnectedLayers(connectedLayers);
		
		ArrayList<StatePropertyType> stateTypeList = (ArrayList<StatePropertyType>) ilcType.getInterConnects();
		ArrayList<State> stateList = new ArrayList<State>();
		for(StatePropertyType statePropertyType : stateTypeList) {
			StateType stateType = statePropertyType.getState();
			State state = (State) idRegistryMap.get(stateType.getId());
			stateList.add(state);
		}
		State[] interConnects = new State[2];
		stateList.toArray(interConnects);
		target.setInterConnects(interConnects);
		
		return target;
	}
	
	private PrimalSpaceFeatures createPrimalSpaceFeatures(PrimalSpaceFeatures target, PrimalSpaceFeaturesPropertyType primalSpaceFeaturesPropertyType) {
		if(target == null) {
			target = new PrimalSpaceFeatures();
		}
		PrimalSpaceFeaturesType primalSpaceFeaturesType = primalSpaceFeaturesPropertyType.getPrimalSpaceFeatures();
		createAbstractFeature(target, primalSpaceFeaturesType);
		
		ArrayList<FeaturePropertyType> cellSpaceMemberTypeList = (ArrayList<FeaturePropertyType>) primalSpaceFeaturesType.getCellSpaceMember();
		List<CellSpace> cellSpaceList = new ArrayList<CellSpace>();
		for(FeaturePropertyType featurePropertyType : cellSpaceMemberTypeList) {
			JAXBElement<CellSpaceType> jCellSpaceType = (JAXBElement<CellSpaceType>) featurePropertyType.getAbstractFeature();
			CellSpaceType cellSpaceType = jCellSpaceType.getValue();
			CellSpace cellSpace = createCellSpace(null, cellSpaceType);
			
			cellSpaceList.add(cellSpace);
		}
		ArrayList<CellSpaceOnFloor> cellSpaceOnFloorList = FloorElementBuilder.createCellSpaceOnFloors(null, cellSpaceList);
		target.setCellSpaceOnFloors(cellSpaceOnFloorList);
		
		ArrayList<FeaturePropertyType> cellSpaceBoundaryMemberTypeList = (ArrayList<FeaturePropertyType>) primalSpaceFeaturesType.getCellSpaceBoundaryMember();
		List<CellSpaceBoundary> cellSpaceBoundaryList = new ArrayList<CellSpaceBoundary>();
		for(FeaturePropertyType featurePropertyType : cellSpaceBoundaryMemberTypeList) {
			JAXBElement<CellSpaceBoundaryType> jCellSpaceBoundaryType = (JAXBElement<CellSpaceBoundaryType>) featurePropertyType.getAbstractFeature();
			CellSpaceBoundaryType cellSpaceBoundaryType = jCellSpaceBoundaryType.getValue();
			CellSpaceBoundary cellSpaceBoundary = createCellSpaceBoundary(null, cellSpaceBoundaryType);
			
			cellSpaceBoundaryList.add(cellSpaceBoundary);
		}
		ArrayList<CellSpaceBoundaryOnFloor> cellSpaceBoundaryOnFloorList = FloorElementBuilder.createCellSpaceBoundaryOnFloors(null, cellSpaceBoundaryList);
		target.setCellSpaceBoundaryOnFloors(cellSpaceBoundaryOnFloorList);
								
		
		return target;
	}
	
	private CellSpace createCellSpace(CellSpace target, CellSpaceType cellSpaceType) {
		if(target == null) {
			target = new CellSpace();
		}
		createAbstractFeature(target, cellSpaceType);
		
		idRegistryMap.put(target.getGmlID(), target);
		return target;
	}
	
	private CellSpaceBoundary createCellSpaceBoundary(CellSpaceBoundary target, CellSpaceBoundaryType cellSpaceBoundaryType) {
		if(target == null) {
			target = new CellSpaceBoundary();
		}
		createAbstractFeature(target, cellSpaceBoundaryType);
		
		idRegistryMap.put(target.getGmlID(), target);
		return target;
	}
	
	private Point createPoint(Point target, PointType pointType) {
		if(target == null) {
			target = new Point();
		}
		target.setGMLID(pointType.getId());
		DirectPositionType directPositionType = pointType.getPos();
		
		
		directPositionType.getValue();
		
		return target;
	}
}
