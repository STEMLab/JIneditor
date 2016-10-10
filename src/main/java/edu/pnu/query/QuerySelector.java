package edu.pnu.query;

import java.util.ArrayList;
import java.util.List;

import net.opengis.indoorgml.core.CellSpaceBoundaryOnFloor;
import net.opengis.indoorgml.core.CellSpaceOnFloor;
import net.opengis.indoorgml.core.Edges;
import net.opengis.indoorgml.core.IndoorFeatures;
import net.opengis.indoorgml.core.MultiLayeredGraph;
import net.opengis.indoorgml.core.Nodes;
import net.opengis.indoorgml.core.PrimalSpaceFeatures;
import net.opengis.indoorgml.core.SpaceLayer;
import net.opengis.indoorgml.core.SpaceLayers;
import net.opengis.indoorgml.core.State;
import net.opengis.indoorgml.core.Transition;
import edu.pnu.project.FloorProperty;
import edu.pnu.project.ProjectFile;
import edu.pnu.project.StateOnFloor;
import edu.pnu.project.TransitionOnFloor;

public class QuerySelector {	
	
	public QuerySelector() { }
	
	public static List<FloorProperty> getQueryFloors(ProjectFile project, String[] queryFloorString) {
		List<FloorProperty> queryFloor = new ArrayList<FloorProperty>();
		List<FloorProperty> floorProperty = project.getBuildingProperty().getFloorProperties();
		
		for (FloorProperty fp : floorProperty) {
			for (int i = 0; i < queryFloorString.length; i++) {
				if (fp.getLevel().equals(queryFloorString[i])) {
					queryFloor.add(fp);
				}
			}
		}
		
		return queryFloor;
	}
		
	public static IndoorFeatures getQueryResult(IndoorFeatures indoorFeatures, List<FloorProperty> queryFloorProperty) {
		IndoorFeatures target = new IndoorFeatures(indoorFeatures);
		
		PrimalSpaceFeatures targetPSF = getPrimalSpaceFeatures(indoorFeatures.getPrimalSpaceFeatures(), queryFloorProperty);		
		target.setPrimalSpaceFeatures(targetPSF);
		
		MultiLayeredGraph targetMLG = getMultiLayeredGraph(indoorFeatures.getMultiLayeredGraph(), queryFloorProperty);
		target.setMultiLayeredGraph(targetMLG);
		
		return target;
	}
	
	public static PrimalSpaceFeatures getPrimalSpaceFeatures(PrimalSpaceFeatures primalSpaceFeatures, List<FloorProperty> queryFloorProperty) {
		PrimalSpaceFeatures target = new PrimalSpaceFeatures(primalSpaceFeatures);
		ArrayList<CellSpaceOnFloor> cellSpaceFloors = primalSpaceFeatures.getCellSpaceOnFloors();
		for (CellSpaceOnFloor csof : cellSpaceFloors) {
			if (queryFloorProperty.contains(csof.getFloorProperty())) {
				target.getCellSpaceOnFloors().add(csof);
			}
		}
		
		ArrayList<CellSpaceBoundaryOnFloor> cellSpaceBoundaryFloors = primalSpaceFeatures.getCellSpaceBoundaryOnFloors();
		for (CellSpaceBoundaryOnFloor cbof : cellSpaceBoundaryFloors) {
			if (queryFloorProperty.contains(cbof.getFloorProperty())) {
				target.getCellSpaceBoundaryOnFloors().add(cbof);
			}
		}
		
		return target;
	}
	
	public static MultiLayeredGraph getMultiLayeredGraph(MultiLayeredGraph mlg, List<FloorProperty> queryFloorProperty) {
		MultiLayeredGraph target = new MultiLayeredGraph(mlg);
		
		SpaceLayers targetSpaceLayers = getSpaceLayers(mlg.getSpaceLayers().get(0), queryFloorProperty);
		ArrayList<SpaceLayers> targetSpaceLayersList = new ArrayList<SpaceLayers>();
		targetSpaceLayersList.add(targetSpaceLayers);
		target.setSpaceLayers(targetSpaceLayersList);
		
		return target;
	}
	
	public static SpaceLayers getSpaceLayers(SpaceLayers spaceLayers, List<FloorProperty> queryFloorProperty) {
		SpaceLayers target = new SpaceLayers(spaceLayers);
		
		SpaceLayer targetSpaceLayer = getSpaceLayer(spaceLayers.getSpaceLayerMember().get(0), queryFloorProperty);
		ArrayList<SpaceLayer> targetSpaceLayerList = new ArrayList<SpaceLayer>();
		targetSpaceLayerList.add(targetSpaceLayer);
		target.setSpaceLayerMember(targetSpaceLayerList);
				
		return target;
	}
	
	public static SpaceLayer getSpaceLayer(SpaceLayer spaceLayer, List<FloorProperty> queryFloorProperty) {
		SpaceLayer target = new SpaceLayer(spaceLayer);
		
		Nodes targetNodes = new Nodes(spaceLayer.getNodes().get(0));
		ArrayList<StateOnFloor> stateOnFloors = spaceLayer.getNodes().get(0).getStateOnFloors();
		for (StateOnFloor sof : stateOnFloors) {
			if (queryFloorProperty.contains(sof.getFloorProperty())) {
				targetNodes.getStateOnFloors().add(sof);
			}
		}
		ArrayList<Nodes> targetNodesList = new ArrayList<Nodes>();
		targetNodesList.add(targetNodes);
		target.setNodes(targetNodesList);
		
		Edges targetEdges = new Edges(spaceLayer.getEdges().get(0));
		ArrayList<TransitionOnFloor> transitionOnFloors = spaceLayer.getEdges().get(0).getTransitionOnFloors();
		for (TransitionOnFloor tof : transitionOnFloors) {
			if (queryFloorProperty.contains(tof.getFloorProperty())) {
				//targetEdges.getTransitionOnFloors().add(tof);
				TransitionOnFloor newTOF = getTransitionOnFloors(targetNodes.getStateOnFloors(), tof);
				targetEdges.getTransitionOnFloors().add(newTOF);
			}
		}
		ArrayList<Edges> targetEdgesList = new ArrayList<Edges>();
		targetEdgesList.add(targetEdges);
		target.setEdges(targetEdgesList);
				
		return target;
	}
	
	public static TransitionOnFloor getTransitionOnFloors(ArrayList<StateOnFloor> stateOnFloors, TransitionOnFloor source) {
		TransitionOnFloor target = new TransitionOnFloor();
		
		FloorProperty floorProperty = source.getFloorProperty();
		target.setFloorProperty(floorProperty);
		ArrayList<Transition> transitionMember = source.getTransitionMember();
		ArrayList<Transition> targetTransitionMember = target.getTransitionMember();
		for (Transition transition : transitionMember) {
			if (isValidTransition(stateOnFloors, floorProperty, transition)) {
				targetTransitionMember.add(transition);
			}
		}
		
		return target;
	}
	
	public static boolean isValidTransition(ArrayList<StateOnFloor> stateOnFloors, FloorProperty floorProperty, Transition transition) {
		State[] states = transition.getStates();
		for (State state : states) {
			boolean check = false;
			for (StateOnFloor sof : stateOnFloors) {
				if (sof.getStateMember().contains(state)) {
					check = true;
					break;
				}
			}
			
			if (!check) {
				return false;
			}
		}
				
		return true;
	}
}
