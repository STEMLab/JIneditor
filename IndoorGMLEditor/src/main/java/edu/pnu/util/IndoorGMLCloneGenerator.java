package edu.pnu.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.pnu.project.StateOnFloor;
import edu.pnu.project.TransitionOnFloor;
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
import net.opengis.indoorgml.geometry.LineString;

public class IndoorGMLCloneGenerator {
	
	private Map<Object, Object> IGMLMap;
	private Map<CellSpaceBoundary, CellSpaceBoundary> xLinkBoundaryMap;

	public IndoorGMLCloneGenerator() {
		IGMLMap = new HashMap<Object, Object>();
		xLinkBoundaryMap = new HashMap<CellSpaceBoundary, CellSpaceBoundary>();
	}
	
	public IndoorFeatures getClone(IndoorFeatures indoorFeatures) {
		IndoorFeatures clone = createIndoorFeatures(indoorFeatures);
		clone = createMirrorCellSpaceBoundary(clone);
		
		return clone;
	}
	
	public Map<CellSpaceBoundary, CellSpaceBoundary> getXLinkBoundaryMap() {
		return xLinkBoundaryMap;
	}
	
	private void setAbstractFeatureAttributes(AbstractFeature source, AbstractFeature target) {
		target.setGmlID(source.getGmlID());
		target.setName(source.getName());
		target.setDescription(source.getDescription());
	}
	
	private IndoorFeatures createIndoorFeatures(IndoorFeatures indoorFeatures) {
		IndoorFeatures target = new IndoorFeatures(indoorFeatures);
		IGMLMap.put(indoorFeatures, target);
		
		PrimalSpaceFeatures primalSpaceFeatures = createPrimalSpaceFeatures(indoorFeatures.getPrimalSpaceFeatures());
		target.setPrimalSpaceFeatures(primalSpaceFeatures);
		
		MultiLayeredGraph multiLayeredGraph = createMultiLayeredGraph(indoorFeatures.getMultiLayeredGraph());
		target.setMultiLayeredGraph(multiLayeredGraph);
		
		return target;
	}
	
	private PrimalSpaceFeatures createPrimalSpaceFeatures(PrimalSpaceFeatures primalSpaceFeatures) {
		PrimalSpaceFeatures target = new PrimalSpaceFeatures(primalSpaceFeatures);
		IGMLMap.put(primalSpaceFeatures, target);		
		
		ArrayList<CellSpaceOnFloor> cellSpaceOnFloors = target.getCellSpaceOnFloors();
		for (CellSpaceOnFloor cellSpaceOnFloor : primalSpaceFeatures.getCellSpaceOnFloors()) {
			cellSpaceOnFloors.add(createCellSpaceOnFloor(cellSpaceOnFloor));
		}
		
		ArrayList<CellSpaceBoundaryOnFloor> cellSpaceBoundaryOnFloors = target.getCellSpaceBoundaryOnFloors();
		for (CellSpaceBoundaryOnFloor cellSpaceBoundaryOnFloor : primalSpaceFeatures.getCellSpaceBoundaryOnFloors()) {
			cellSpaceBoundaryOnFloors.add(createCellSpaceBoundaryOnFloor(cellSpaceBoundaryOnFloor));
		}
		
		return target;
	}
	
	private CellSpaceOnFloor createCellSpaceOnFloor(CellSpaceOnFloor cellSpaceOnFloor) {
		CellSpaceOnFloor target = new CellSpaceOnFloor();
		IGMLMap.put(cellSpaceOnFloor, target);
		
		target.setFloorProperty(cellSpaceOnFloor.getFloorProperty());
		
		ArrayList<CellSpace> cellSpaceMember = target.getCellSpaceMember();
		for (CellSpace cellSpace : cellSpaceOnFloor.getCellSpaceMember()) {
			cellSpaceMember.add(createCellSpace(cellSpace));
		}
		
		return target;
	}
	
	private CellSpace createCellSpace(CellSpace cellSpace) {
		if (IGMLMap.containsKey(cellSpace)) {
			return (CellSpace) IGMLMap.get(cellSpace);
		}
		
		CellSpace target = new CellSpace(cellSpace);
		IGMLMap.put(cellSpace, target);
		
		State sourceDuality = cellSpace.getDuality();
		if (sourceDuality != null) {
			State targetDuality = null;
			if (IGMLMap.containsKey(sourceDuality)) {
				targetDuality = (State) IGMLMap.get(sourceDuality);
			} else {
				targetDuality = createState(sourceDuality);
			}
			target.setDuality(targetDuality);
		}
		
		ArrayList<CellSpaceBoundary> partialBoundedBy = target.getPartialBoundedBy();
		for (CellSpaceBoundary bounded : cellSpace.getPartialBoundedBy()) {
			partialBoundedBy.add(createCellSpaceBoundary(bounded));
		}
		
		ArrayList<LineString> lineStringElements = target.getLineStringElements();
		for (LineString ls : cellSpace.getLineStringElements()) {
			LineString lsClone = null;
			if (IGMLMap.containsKey(ls)) {
				lsClone = (LineString) IGMLMap.get(ls);
			} else {
				lsClone = ls.clone();
				IGMLMap.put(ls, lsClone);
			}
			lineStringElements.add(lsClone);
		}
		
		return target;
	}
	
	private CellSpaceBoundaryOnFloor createCellSpaceBoundaryOnFloor(CellSpaceBoundaryOnFloor cellSpaceBoundaryOnFloor) {
		CellSpaceBoundaryOnFloor target = new CellSpaceBoundaryOnFloor();
		IGMLMap.put(cellSpaceBoundaryOnFloor, target);
		
		target.setFloorProperty(cellSpaceBoundaryOnFloor.getFloorProperty());
		
		ArrayList<CellSpaceBoundary> cellSpaceBoundaryMember = target.getCellSpaceBoundaryMember();
		for (CellSpaceBoundary cellSpaceBoundary : cellSpaceBoundaryOnFloor.getCellSpaceBoundaryMember()) {
			cellSpaceBoundaryMember.add(createCellSpaceBoundary(cellSpaceBoundary));
		}
		
		HashMap<LineString, ArrayList<CellSpaceBoundary>> targetLineBoundaryMap =
				target.getLineStringOfAdjacencyBoundaryMap();
		HashMap<LineString, ArrayList<CellSpaceBoundary>> sourcelineBoundaryMap = 
				cellSpaceBoundaryOnFloor.getLineStringOfAdjacencyBoundaryMap();
		for (LineString sourceLS : sourcelineBoundaryMap.keySet()) {
			if (!IGMLMap.containsKey(sourceLS)) {
				System.out.println("IGMLMap don't have clone of LineString");
				continue;
			}
			
			LineString targetLS = (LineString) IGMLMap.get(sourceLS);
			ArrayList<CellSpaceBoundary> targetBoundaryList = new ArrayList<CellSpaceBoundary>();
			ArrayList<CellSpaceBoundary> sourceBoundaryList = sourcelineBoundaryMap.get(sourceLS);
			for (CellSpaceBoundary sourceBoundary : sourceBoundaryList) {
				if (!IGMLMap.containsKey(sourceBoundary)) {
					continue;
				}
				CellSpaceBoundary targetBoundary = (CellSpaceBoundary) IGMLMap.get(sourceBoundary);
				targetBoundaryList.add(targetBoundary);
			}
			
			targetLineBoundaryMap.put(targetLS, targetBoundaryList);
		}
		
		HashMap<CellSpaceBoundary, ArrayList<CellSpace>> targetBoundaryCellSpaceMap =
				target.getBoundaryOfReferenceCellSpaceMap();
		HashMap<CellSpaceBoundary, ArrayList<CellSpace>> sourceBoundaryCellSpaceMap =
				cellSpaceBoundaryOnFloor.getBoundaryOfReferenceCellSpaceMap();
		for (CellSpaceBoundary sourceBoundary : sourceBoundaryCellSpaceMap.keySet()) {
			if (!IGMLMap.containsKey(sourceBoundary)) {
				continue;
			}
			
			CellSpaceBoundary targetBoundary = (CellSpaceBoundary) IGMLMap.get(sourceBoundary);
			ArrayList<CellSpace> targetCellSpaceList = new ArrayList<CellSpace>();
			ArrayList<CellSpace> sourceCellSpaceList = sourceBoundaryCellSpaceMap.get(sourceBoundary);
			for (CellSpace sourceCellSpace : sourceCellSpaceList) {
				if (!IGMLMap.containsKey(sourceCellSpace)) {
					continue;
				}
				CellSpace targetSellSpace = (CellSpace) IGMLMap.get(sourceCellSpace);
				targetCellSpaceList.add(targetSellSpace);
			}
			targetBoundaryCellSpaceMap.put(targetBoundary, targetCellSpaceList);
		}
		
		return target;
	}
	
	private CellSpaceBoundary createCellSpaceBoundary(CellSpaceBoundary cellSpaceBoundary) {
		if (IGMLMap.containsKey(cellSpaceBoundary)) {
			return (CellSpaceBoundary) IGMLMap.get(cellSpaceBoundary);
		}
		
		CellSpaceBoundary target = new CellSpaceBoundary(cellSpaceBoundary);
		IGMLMap.put(cellSpaceBoundary, target);
				
		Transition sourceDuality = cellSpaceBoundary.getDuality();
		if (sourceDuality != null) {
			Transition targetDuality = null;
			if (IGMLMap.containsKey(sourceDuality)) {
				targetDuality = (Transition) IGMLMap.get(sourceDuality);
			} else {
				targetDuality = createTransition(sourceDuality);
			}
			target.setDuality(targetDuality);
		}
				
		return target;
	}
	
	private MultiLayeredGraph createMultiLayeredGraph(MultiLayeredGraph mlg) {
		MultiLayeredGraph target = new MultiLayeredGraph(mlg);
		IGMLMap.put(mlg, target);
				
		ArrayList<SpaceLayers> targetSpaceLayersList = new ArrayList<SpaceLayers>();
		for (SpaceLayers sourceSpaceLayers : mlg.getSpaceLayers()) {
			SpaceLayers targetSpaceLayers = createSpaceLayers(sourceSpaceLayers);
			targetSpaceLayersList.add(targetSpaceLayers);
		}
		target.setSpaceLayers(targetSpaceLayersList);
		
		ArrayList<InterEdges> targetInterEdgesList = new ArrayList<InterEdges>();
		for (InterEdges sourceInterEdges : mlg.getInterEdges()) {
			InterEdges targetInterEdges = createInterEdges(sourceInterEdges);
			targetInterEdgesList.add(targetInterEdges);
		}
		target.setInterEdges(targetInterEdgesList);
		
		return target;
	}
	
	private SpaceLayers createSpaceLayers(SpaceLayers spaceLayers) {
		SpaceLayers target = new SpaceLayers(spaceLayers);
		IGMLMap.put(spaceLayers, target);
				
		ArrayList<SpaceLayer> targetSpaceLayerMembrer = new ArrayList<SpaceLayer>();
		for (SpaceLayer sourceSpaceLayer : spaceLayers.getSpaceLayerMember()) {
			SpaceLayer targetSpaceLayer = createSpaceLayer(sourceSpaceLayer);
			targetSpaceLayerMembrer.add(targetSpaceLayer);
		}
		target.setSpaceLayerMember(targetSpaceLayerMembrer);
		
		return target;
	}
	
	private SpaceLayer createSpaceLayer(SpaceLayer spaceLayer) {
		if (IGMLMap.containsKey(spaceLayer)) {
			return (SpaceLayer) IGMLMap.get(spaceLayer);
		}
		
		SpaceLayer target = new SpaceLayer(spaceLayer);
		IGMLMap.put(spaceLayer, target);
				
		ArrayList<Nodes> targetNodesList = new ArrayList<Nodes>();
		for (Nodes sourceNodes : spaceLayer.getNodes()) {
			Nodes targetNodes = createNodes(sourceNodes);
			targetNodesList.add(targetNodes);
		}
		target.setNodes(targetNodesList);
		
		ArrayList<Edges> targetEdgesList = new ArrayList<Edges>();
		for (Edges sourceEdges : spaceLayer.getEdges()) {
			Edges targetEdges = createEdges(sourceEdges);
			targetEdgesList.add(targetEdges);
		}
		target.setEdges(targetEdgesList);
		
		return target;
	}
	
	private Nodes createNodes(Nodes nodes) {
		Nodes target = new Nodes(nodes);
		IGMLMap.put(nodes, target);
				
		ArrayList<StateOnFloor> targetStateOnFloorList = target.getStateOnFloors();
		for (StateOnFloor sourceStateOnFloor : nodes.getStateOnFloors()) {
			StateOnFloor targetStateOnFloor = createStateOnFloor(sourceStateOnFloor);
			targetStateOnFloorList.add(targetStateOnFloor);
		}
		
		return target;
	}
	
	private StateOnFloor createStateOnFloor(StateOnFloor stateOnFloor) {
		StateOnFloor target = new StateOnFloor();
		IGMLMap.put(stateOnFloor, target);
		
		target.setFloorProperty(stateOnFloor.getFloorProperty());
		
		ArrayList<State> targetStateMember = target.getStateMember();
		for (State sourceState : stateOnFloor.getStateMember()) {
			State targetState = null;
			if (IGMLMap.containsKey(sourceState)) {
				targetState = (State) IGMLMap.get(sourceState);
			} else {
				targetState = createState(sourceState);
			}
			targetStateMember.add(targetState);
		}
		
		return target;
	}
	
	private State createState(State state) {
		if (IGMLMap.containsKey(state)) {
			return (State) IGMLMap.get(state);
		}
		
		State target = new State(state);
		IGMLMap.put(state, target);
		
		CellSpace sourceDuality = state.getDuality();
		if (sourceDuality != null) {
			CellSpace targetDuality = null;
			if (IGMLMap.containsKey(sourceDuality)) {
				targetDuality = (CellSpace) IGMLMap.get(sourceDuality);				
			} else {
				targetDuality = createCellSpace(sourceDuality);
			}
			target.setDuality(targetDuality);
		}
		
		ArrayList<Transition> targetConnects = target.getTransitionReference();
		for (Transition sourceConnect : state.getTransitionReference()) {
			Transition targetConnect = null;
			if (IGMLMap.containsKey(sourceConnect)) {
				targetConnect = (Transition) IGMLMap.get(sourceConnect);
			} else {
				targetConnect = createTransition(sourceConnect);
			}
			targetConnects.add(targetConnect);
		}
		
		return target;
	}
	
	private Edges createEdges(Edges edges) {
		Edges target = new Edges(edges);
		IGMLMap.put(edges, target);
				
		ArrayList<TransitionOnFloor> targetTransitionOnFloorList = target.getTransitionOnFloors();
		for (TransitionOnFloor sourceTransitionOnFloor : edges.getTransitionOnFloors()) {
			TransitionOnFloor targetTransitionOnFloor = createTransitionOnFloor(sourceTransitionOnFloor);
			targetTransitionOnFloorList.add(targetTransitionOnFloor);
		}
		
		return target;
	}
	
	private TransitionOnFloor createTransitionOnFloor(TransitionOnFloor transitionOnFloor) {
		TransitionOnFloor target = new TransitionOnFloor();
		IGMLMap.put(transitionOnFloor, target);
		
		target.setFloorProperty(transitionOnFloor.getFloorProperty());
		
		ArrayList<Transition> targetTransitionMember = target.getTransitionMember();
		for (Transition sourceTransition : transitionOnFloor.getTransitionMember()) {
			Transition targetTransition = null;
			if (IGMLMap.containsKey(sourceTransition)) {
				targetTransition = (Transition) IGMLMap.get(sourceTransition);
				State[] sourceConnects = sourceTransition.getStates();
				State[] targetConnects = new State[2];
				targetConnects[0] = (State) IGMLMap.get(sourceConnects[0]);
				targetConnects[1] = (State) IGMLMap.get(sourceConnects[1]);
				
				targetTransition.setStates(targetConnects);
			} else {
				targetTransition = createTransition(sourceTransition);
			}
			targetTransitionMember.add(targetTransition);
		}
		
		return target;
	}
	
	private Transition createTransition(Transition transition) {
		if (IGMLMap.containsKey(transition)) {
			return (Transition) IGMLMap.get(transition);
		}
		
		Transition target = new Transition(transition);
		IGMLMap.put(transition, target);
				
		CellSpaceBoundary sourceDuality = transition.getDuality();
		if (sourceDuality != null) {
			CellSpaceBoundary targetDuality = null;
			if (IGMLMap.containsKey(sourceDuality)) {
				targetDuality = (CellSpaceBoundary) IGMLMap.get(sourceDuality);
			} else {
				targetDuality = createCellSpaceBoundary(sourceDuality);
			}
			target.setDuality(targetDuality);
		}
		
		return target;
	}
	
	private InterEdges createInterEdges(InterEdges interEdges) {
		InterEdges target = new InterEdges(interEdges);
		IGMLMap.put(interEdges, target);
				
		ArrayList<InterLayerConnection> targetILCMember = target.getInterLayerConnectionMember();
		for (InterLayerConnection sourceILC : interEdges.getInterLayerConnectionMember()) {
			InterLayerConnection targetILC = createInterLayerConnection(sourceILC);
			targetILCMember.add(targetILC);
		}
		
		return target;
	}
	
	private InterLayerConnection createInterLayerConnection(InterLayerConnection ilc) {
		InterLayerConnection target = new InterLayerConnection(ilc);
		IGMLMap.put(ilc, target);
				
		State[] targetInterConnects = target.getInterConnects();
		for (int i = 0; i < ilc.getInterConnects().length; i++) {
			State targetState = null;
			
			State sourceState = ilc.getInterConnects()[i];
			if (IGMLMap.containsKey(sourceState)) {
				targetState = (State) IGMLMap.get(sourceState);
			} else {
				targetState = createState(sourceState);
			}
			targetInterConnects[i] = targetState;
		}
		
		SpaceLayer[] targetConnectedLayers = target.getConnectedLayers();
		for (int i = 0; i < ilc.getConnectedLayers().length; i++) {
			SpaceLayer targetLayer = null;
			
			SpaceLayer sourceLayer = ilc.getConnectedLayers()[i];
			if (IGMLMap.containsKey(sourceLayer)) {
				targetLayer = (SpaceLayer) IGMLMap.get(sourceLayer);
			} else {
				targetLayer = createSpaceLayer(sourceLayer);
			}
			targetConnectedLayers[i] = targetLayer;
		}
		
		return target;
	}

	private IndoorFeatures createMirrorCellSpaceBoundary(IndoorFeatures indoorFeatures) {
		PrimalSpaceFeatures primalSpaceFeatures = indoorFeatures.getPrimalSpaceFeatures();
		ArrayList<CellSpaceOnFloor> cellSpaceOnFloorList =
				primalSpaceFeatures.getCellSpaceOnFloors();
		ArrayList<CellSpaceBoundaryOnFloor> cellSpaceBoundaryOnFloorList =
				primalSpaceFeatures.getCellSpaceBoundaryOnFloors();
		
		for (int i = 0; i< cellSpaceOnFloorList.size(); i++) {
			createMirrorCellSpaceBoundary(cellSpaceOnFloorList.get(i), cellSpaceBoundaryOnFloorList.get(i), indoorFeatures.getMultiLayeredGraph());
		}
		
		return indoorFeatures;
	}
	
	private void createMirrorCellSpaceBoundary(CellSpaceOnFloor cellSpaceOnFloor, CellSpaceBoundaryOnFloor cellSpaceBoundaryOnFloor, MultiLayeredGraph mlg) {
		HashMap<CellSpaceBoundary, ArrayList<CellSpace>> boundaryCellSpaceMap = cellSpaceBoundaryOnFloor.getBoundaryOfReferenceCellSpaceMap();
		ArrayList<CellSpace> cellSpaceMember = cellSpaceOnFloor.getCellSpaceMember();
		
		for (CellSpace cellSpace : cellSpaceMember) {
			ArrayList<CellSpaceBoundary> boundedBy = cellSpace.getPartialBoundedBy();
			for (CellSpaceBoundary bounded : boundedBy) {
				ArrayList<CellSpace> reference = boundaryCellSpaceMap.get(bounded);
				
				if (reference.size() == 2) {
					//CB2 생성, 기하는 xlink
					//C1 - CB1
					//C2 - CB2 partialBoundedBy

					//boundaryMember CB2 추가
					int indexOf = reference.indexOf(cellSpace);
					CellSpace mirrorCell = reference.get((indexOf + 1) % 2);
					CellSpaceBoundary mirror = createMirrorCellSpaceBoundary(cellSpace, bounded, mirrorCell, cellSpaceBoundaryOnFloor);			
					boundaryCellSpaceMap.put(mirror, new ArrayList<CellSpace>());
					boundaryCellSpaceMap.get(mirror).add(mirrorCell);
					reference.remove(mirrorCell);
					
					xLinkBoundaryMap.put(mirror, bounded);

					//CB1 - T1
					//T2 생성, duality to CB2
					//ST1, ST2에 connect 추가
					//transitionMember 추가	
					Transition duality = bounded.getDuality();
					if (duality != null) {
						Transition mirrorTransition = new Transition();
						mirrorTransition.setWeight(duality.getWeight());
						mirrorTransition.setPath(duality.getPath().clone());
						mirrorTransition.setStates(duality.getStates());
						
						mirror.setDuality(mirrorTransition);
						mirrorTransition.setDuality(mirror);
						
						cellSpace.getDuality().getTransitionReference().add(mirrorTransition);
						mirrorCell.getDuality().getTransitionReference().add(mirrorTransition);
						
						TransitionOnFloor transitionOnFloor = searchTransitionOnFloor(mlg, duality);
						transitionOnFloor.getTransitionMember().add(mirrorTransition);
					}
				}
			}
		}
	}
	
	private CellSpaceBoundary createMirrorCellSpaceBoundary(CellSpace sourceCell, CellSpaceBoundary sourceBoundary, CellSpace mirrorCell, CellSpaceBoundaryOnFloor cellSpaceBoundaryOnFloor) {
		HashMap<LineString, ArrayList<CellSpaceBoundary>> lineStringBoundaryMap = cellSpaceBoundaryOnFloor.getLineStringOfAdjacencyBoundaryMap();
		ArrayList<LineString> lsElements = sourceCell.getLineStringElements();
		
		LineString sourceLS = null;
		for (LineString ls : lsElements) {
			if (lineStringBoundaryMap.containsKey(ls)) {
				ArrayList<CellSpaceBoundary> adjacency = lineStringBoundaryMap.get(ls);
				if (adjacency.contains(sourceBoundary)) {
					sourceLS = ls;
					break;
				}
			}
		}
		
		if (sourceLS != null) {
			if (GeometryUtil.isSimilarOrientation(sourceLS, sourceBoundary.getGeometry2D()) == -1) {
				sourceBoundary.getGeometry2D().reverse();
			}
		}
		
		CellSpaceBoundary mirror = new CellSpaceBoundary();
		mirror.setGeometry2D(sourceBoundary.getGeometry2D().clone());
		mirror.setExternalReference(sourceBoundary.getExternalReference());
		mirror.setBoundaryType(sourceBoundary.getBoundaryType());
		mirror.setDoorHeight(sourceBoundary.getDoorHeight());
		mirror.setIsDefaultDoorHeight(sourceBoundary.getIsDefaultDoorHeight());
		
		mirror.getGeometry2D().setxLinkGeometry(sourceLS);
		mirror.getGeometry2D().setIsReversed(true);
		
		mirrorCell.getPartialBoundedBy().remove(sourceBoundary);
		mirrorCell.getPartialBoundedBy().add(mirror);		
		cellSpaceBoundaryOnFloor.getCellSpaceBoundaryMember().add(mirror);
		
		for (LineString mirrorLS : mirrorCell.getLineStringElements()) {
			if (lineStringBoundaryMap.containsKey(mirrorLS)) {
				ArrayList<CellSpaceBoundary> adjacency = lineStringBoundaryMap.get(mirrorLS);
				if (adjacency.contains(sourceBoundary)) {
					adjacency.remove(sourceBoundary);
					adjacency.add(mirror);
				}
			}
		}
		
		return mirror;
	}
	
	private StateOnFloor searchStateOnFloor(MultiLayeredGraph mlg, State state) {
		ArrayList<SpaceLayers> spaceLayersMember = mlg.getSpaceLayers();
		for (SpaceLayers spaceLayers : spaceLayersMember) {
			ArrayList<SpaceLayer> spaceLayerMember = spaceLayers.getSpaceLayerMember();
			for (SpaceLayer spaceLayer : spaceLayerMember) {
				for (Nodes nodes : spaceLayer.getNodes()) {
					for (StateOnFloor stateOnFloor : nodes.getStateOnFloors()) {
						if (stateOnFloor.getStateMember().contains(state)) {
							return stateOnFloor;
						}
					}
				}
			}
		}
		
		return null;
	}
	
	private TransitionOnFloor searchTransitionOnFloor(MultiLayeredGraph mlg, Transition transition) {
		ArrayList<SpaceLayers> spaceLayersMember = mlg.getSpaceLayers();
		for (SpaceLayers spaceLayers : spaceLayersMember) {
			ArrayList<SpaceLayer> spaceLayerMember = spaceLayers.getSpaceLayerMember();
			for (SpaceLayer spaceLayer : spaceLayerMember) {
				for (Edges edges : spaceLayer.getEdges()) {
					for (TransitionOnFloor transitionOnFloor : edges.getTransitionOnFloors()) {
						if (transitionOnFloor.getTransitionMember().contains(transition)) {
							return transitionOnFloor;
						}
					}
				}
			}
		}
		
		return null;
	}
}
