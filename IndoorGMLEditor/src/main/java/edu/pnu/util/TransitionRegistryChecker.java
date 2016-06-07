package edu.pnu.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.opengis.indoorgml.core.Edges;
import net.opengis.indoorgml.core.IndoorFeatures;
import net.opengis.indoorgml.core.MultiLayeredGraph;
import net.opengis.indoorgml.core.Nodes;
import net.opengis.indoorgml.core.SpaceLayer;
import net.opengis.indoorgml.core.SpaceLayers;
import net.opengis.indoorgml.core.State;
import net.opengis.indoorgml.core.Transition;
import edu.pnu.project.StateOnFloor;
import edu.pnu.project.TransitionOnFloor;

public class TransitionRegistryChecker {
	private IndoorFeatures indoorFeatures;
	private Map<String, Transition> transitionRegistry;

	public TransitionRegistryChecker(IndoorFeatures indoorFeatures) {
		this.indoorFeatures = indoorFeatures;
	}
	
	public void check() {
		transitionRegistry = new HashMap<String, Transition>();
		loadTransitionRegistry(indoorFeatures.getMultiLayeredGraph());
		
		traverseIndoorFeatures(indoorFeatures);
	}

	public void traverseIndoorFeatures(IndoorFeatures indoorFeatures) {
		traverseMultiLayeredGraph(indoorFeatures.getMultiLayeredGraph());
	}

	public void traverseMultiLayeredGraph(MultiLayeredGraph multiLayeredGraph) {
		ArrayList<SpaceLayers> spaceLayersList = multiLayeredGraph.getSpaceLayers();
		for(SpaceLayers spaceLayers : spaceLayersList) {
			traverseSpaceLayers(spaceLayers);
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
	}

	public void traverseNodes(Nodes nodes) {
		ArrayList<StateOnFloor> stateOnFloorList = nodes.getStateOnFloors();
		for(StateOnFloor stateOnFloor : stateOnFloorList) {
			traverseStateOnFloor(stateOnFloor);
		}
	}

	public void traverseStateOnFloor(StateOnFloor stateOnFloor) {
		ArrayList<State> stateList = stateOnFloor.getStateMember();
		
		for(State state : stateList) {
			traverseState(state);
		}
	}

	public void traverseState(State state) {
		checkNotExistTransition(state);
	}
	
	private void loadTransitionRegistry(MultiLayeredGraph mlg) {
		ArrayList<SpaceLayers> spaceLayersList = mlg.getSpaceLayers();
		for (SpaceLayers spaceLayers : spaceLayersList) {
			ArrayList<SpaceLayer> spaceLayerList = spaceLayers.getSpaceLayerMember();
			for (SpaceLayer spaceLayer : spaceLayerList) {
				ArrayList<Edges> edgesList = spaceLayer.getEdges();
				for (Edges edges : edgesList) {
					ArrayList<TransitionOnFloor> transitionOnFloorList = edges.getTransitionOnFloors();
					for (TransitionOnFloor transitionOnFloor : transitionOnFloorList) {
						ArrayList<Transition> transitionList = transitionOnFloor.getTransitionMember();
						for (Transition transition : transitionList) {
							if (transitionRegistry.containsKey(transition.getGmlID())) {
								System.out.println("registry : " + transition.getGmlID() + " is already exist");
							} else {
								transitionRegistry.put(transition.getGmlID(), transition);
							}
						}
					}
				}
			}
		}
	}
	
	private void checkNotExistTransition(State state) {
		ArrayList<Transition> connects = state.getTransitionReference();
		ArrayList<Transition> removes = new ArrayList<Transition>();
		for (Transition transition : connects) {
			if (!transitionRegistry.containsKey(transition.getGmlID())) {
				System.out.println("** checkNotExistTransition : " + transition.getGmlID() + " is not exist!!");
				removes.add(transition);
			}
		}
		
		if (removes.size() > 0) {
			connects.removeAll(removes);
		}
	}

}
