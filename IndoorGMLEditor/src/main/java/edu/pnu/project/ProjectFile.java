package edu.pnu.project;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;

import net.opengis.indoorgml.core.CCTV;
import net.opengis.indoorgml.core.CCTVOnFloor;
import net.opengis.indoorgml.core.CellSpace;
import net.opengis.indoorgml.core.CellSpaceBoundary;
import net.opengis.indoorgml.core.Edges;
import net.opengis.indoorgml.core.IndoorFeatures;
import net.opengis.indoorgml.core.InterLayerConnection;
import net.opengis.indoorgml.core.MultiLayeredGraph;
import net.opengis.indoorgml.core.Nodes;
import net.opengis.indoorgml.core.PrimalSpaceFeatures;
import net.opengis.indoorgml.core.SpaceLayer;
import net.opengis.indoorgml.core.SpaceLayers;
import net.opengis.indoorgml.core.State;
import net.opengis.indoorgml.core.Transition;

public class ProjectFile implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7073475664555079035L;
	private int panelWidth;
	private int panelHeight;

	private boolean is3DGeometry;
	
	private IndoorFeatures indoorFeatures;
	private PrimalSpaceFeatures primalSpaceFeatures;
	private MultiLayeredGraph multiLayeredGraph;

	private transient BufferedImage currentFloorPlan;
	private double currentFloorPlanScale;
	
	private CCTVOnFloor currentCCTVOnFloor;
	
	private SpaceLayers currentSpaceLayers;
	private SpaceLayer currentSpaceLayer;
	private Nodes currentNodes;
	private Edges currentEdges;
	private StateOnFloor currentStateOnFloor;
	private TransitionOnFloor currentTransitionOnFloor;
	private String currentFloor;
	
	private IndoorGMLIDRegistry idRegistry;
	
	private BuildingProperty buildingProperty;
	private EditState editState;
	private EditWorkState editWorkState;

	public ProjectFile() {
	    /*
		MultiLayeredGraph.setLabelNumber(1);
		SpaceLayers.setLabelNumber(1);
		SpaceLayer.setLabelNumber(1);
		Nodes.setLabelNumber(1);
		Edges.setLabelNumber(1);
		State.setLabelNumber(1);
		Transition.setLabelNumber(1);
		Point.setLebelNumber(1);
		LineString.setLabelNumber(1);
		InterEdges.setLabelNumber(1);
		InterLayerConnection.setLabelNumber(1);
		*/
		currentFloorPlanScale = 1.0;
		idRegistry = new IndoorGMLIDRegistry();
		editState = EditState.NONE;
	}

	public int getPanelWidth() {
		return panelWidth;
	}

	public void setPanelWidth(int panelWidth) {
		this.panelWidth = panelWidth;
	}

	public boolean getIs3DGeometry() {
		return is3DGeometry;
	}

	public void setIs3DGeometry(boolean is3dGeometry) {
		is3DGeometry = is3dGeometry;
	}

	public int getPanelHeight() {
		return panelHeight;
	}

	public void setPanelHeight(int panelHeight) {
		this.panelHeight = panelHeight;
	}
	
	public BufferedImage getCurrentFloorPlan() {
		if(currentFloorPlan == null) {
			return null;
		}
		return currentFloorPlan;
	}

	public void setCurrentFloorPlan(BufferedImage floorPlan) {
		this.currentFloorPlan = floorPlan;
	}

	public IndoorFeatures getIndoorFeatures() {
		if(indoorFeatures == null) {
			indoorFeatures = new IndoorFeatures();
		}
		return indoorFeatures;
	}

	public void setIndoorFeatures(IndoorFeatures indoorFeatures) {
		this.indoorFeatures = indoorFeatures;
	}

	public PrimalSpaceFeatures getPrimalSpacesFeatures() {
		if(primalSpaceFeatures == null) {
			primalSpaceFeatures = getIndoorFeatures().getPrimalSpaceFeatures();
		}
		return primalSpaceFeatures;
	}

	public void setPrimalSpacesFeatures(PrimalSpaceFeatures primalSpacesFeatures) {
		this.primalSpaceFeatures = primalSpacesFeatures;
	}

	public MultiLayeredGraph getMultiLayeredGraph() {
		if(multiLayeredGraph == null) {
			multiLayeredGraph = getIndoorFeatures().getMultiLayeredGraph();
		}
		return multiLayeredGraph;
	}

	public void setMultiLayeredGraph(MultiLayeredGraph multiLayeredGraph) {
		this.multiLayeredGraph = multiLayeredGraph;
	}

	public SpaceLayers getCurrentSpaceLayers() {
		if(currentSpaceLayers == null) {
			currentSpaceLayers = getMultiLayeredGraph().getSpaceLayers().get(0);
		}
		return currentSpaceLayers;
	}

	public void setCurrentSpaceLayers(SpaceLayers currentSpaceLayers) {
		this.currentSpaceLayers = currentSpaceLayers;
	}

	public SpaceLayer getCurrentSpaceLayer() {
		if(currentSpaceLayer == null) {
			currentSpaceLayer = getCurrentSpaceLayers().getSpaceLayerMember().get(0);
		}
		return currentSpaceLayer;
	}

	public void setCurrentSpaceLayer(SpaceLayer currentSpaceLayer) {
		this.currentSpaceLayer = currentSpaceLayer;
	}

	public BuildingProperty getBuildingProperty() {
		if(buildingProperty == null) {
			buildingProperty = new BuildingProperty();
		}
		return buildingProperty;
	}

	public void setBuildingProperty(BuildingProperty buildingProperty) {
		this.buildingProperty = buildingProperty;
	}

	public EditState getEditState() {
		return editState;
	}

	public void setEditState(EditState editState) {
		this.editState = editState;
	}

	public Nodes getCurrentNodes() {
		if(currentNodes == null) {
			currentNodes = getCurrentSpaceLayer().getNodes().get(0);
		}
		return currentNodes;
	}

	public void setCurrentNodes(Nodes currentNodes) {
		this.currentNodes = currentNodes;
	}

	public Edges getCurrentEdges() {
		if(currentEdges == null) {
			currentEdges = getCurrentSpaceLayer().getEdges().get(0);
		}
		return currentEdges;
	}

	public void setCurrentEdges(Edges currentEdges) {
		this.currentEdges = currentEdges;
	}

	public StateOnFloor getCurrentStateOnFloor() {
		if(currentStateOnFloor == null) {
			ArrayList<StateOnFloor> stateOnFloors = getCurrentNodes().getStateOnFloors();
			if(stateOnFloors.size() > 0) {
				currentStateOnFloor = stateOnFloors.get(0);
			}
		}
		return currentStateOnFloor;
	}

	public void setCurrentStateOnFloor(StateOnFloor currentStateOnFloor) {
		if(currentStateOnFloor == null) {
			currentStateOnFloor = currentNodes.getStateOnFloors().get(0);
		}
		this.currentStateOnFloor = currentStateOnFloor;
	}

	public TransitionOnFloor getCurrentTransitionOnFloor() {
		if(currentTransitionOnFloor == null) {
			ArrayList<TransitionOnFloor> transitionOnFloors = getCurrentEdges().getTransitionOnFloors();
			if(transitionOnFloors.size() > 0) {
				currentTransitionOnFloor = transitionOnFloors.get(0);
			}
		}
		return currentTransitionOnFloor;
	}

	public void setCurrentTransitionOnFloor(
			TransitionOnFloor currentTransitionOnFloor) {
		this.currentTransitionOnFloor = currentTransitionOnFloor;
	}

	public CCTVOnFloor getCurrentCCTVOnFloor() {
		if(currentCCTVOnFloor == null) {
			ArrayList<CCTVOnFloor> cctvOnFloors = getPrimalSpacesFeatures().getCctvOnFloors();
			if(cctvOnFloors.size() > 0) {
				currentCCTVOnFloor = cctvOnFloors.get(0);
			}
		}
		return currentCCTVOnFloor;
	}
	
	public void setCurrentCCTVOnFloor(CCTVOnFloor currentCCTVOnFloor) {
		this.currentCCTVOnFloor = currentCCTVOnFloor;
	}

	public String getCurrentFloor() {
		return currentFloor;
	}

	public void setCurrentFloor(String currentFloor) {
		this.currentFloor = currentFloor;
	}

	public double getCurrentFloorPlanScale() {
		return currentFloorPlanScale;
	}

	public void setCurrentFloorPlanScale(double currentFloorPlanScale) {
		this.currentFloorPlanScale = currentFloorPlanScale;
	}

	public EditWorkState getEditWorkState() {
		return editWorkState;
	}

	public void setEditWorkState(EditWorkState editWorkState) {
		this.editWorkState = editWorkState;
	}

	public void computeGMLCoordinate() {

	}
	
	public void saveIndoorGMLID() {
	        if(idRegistry == null) {
	                idRegistry = new IndoorGMLIDRegistry();
	        }
	        idRegistry.saveIndoorGMLID();
	}
	
	public void loadIndoorGMLID() {
	        if(idRegistry != null) {
	                idRegistry.loadIndoorGMLID();
	        }
	}

	public void addFloorProperty(FloorProperty floorProperty) {
		this.buildingProperty.getFloorProperties().add(floorProperty);

		/*
		ArrayList<SpaceLayers> spaceLayersList = multiLayeredGraph
				.getSpaceLayers();
		for (SpaceLayers spaceLayers : spaceLayersList) {
			ArrayList<SpaceLayer> spaceLayerList = spaceLayers
					.getSpaceLayerMember();
			for (SpaceLayer spaceLayer : spaceLayerList) {
				ArrayList<Nodes> nodesList = spaceLayer.getNodes();
				for (Nodes nodes : nodesList) {
					StateOnFloor stateOnFloor = new StateOnFloor();
					stateOnFloor.setFloorProperty(floorProperty);
					nodes.getStateOnFloors().add(stateOnFloor);
				}

				ArrayList<Edges> edgesList = spaceLayer.getEdges();
				for (Edges edges : edgesList) {
					TransitionOnFloor transitionOnFloor = new TransitionOnFloor();
					transitionOnFloor.setFloorProperty(floorProperty);
					edges.getTransitionOnFloor().add(transitionOnFloor);
				}
			}
		}
		*/

		System.out.println("Floor | level : " + floorProperty.getLevel()
				+ " bottomleft : "
				+ floorProperty.getBottomLeftPoint().toString()
				+ " topright : " + floorProperty.getTopRightPoint().toString()
				+ " height : " + floorProperty.getGroundHeight()
				+ " floorPlanPath : " + floorProperty.getFloorPlanPath());

	}

	public void deleteFloorProperty(FloorProperty floorProperty) {
		ArrayList<SpaceLayers> spaceLayersList = multiLayeredGraph
				.getSpaceLayers();
		for (SpaceLayers spaceLayers : spaceLayersList) {
			ArrayList<SpaceLayer> spaceLayerList = spaceLayers
					.getSpaceLayerMember();
			for (SpaceLayer spaceLayer : spaceLayerList) {
				ArrayList<Nodes> nodesList = spaceLayer.getNodes();
				for (Nodes nodes : nodesList) {
					ArrayList<StateOnFloor> stateOnFloorList = nodes
							.getStateOnFloors();
					StateOnFloor target = null;

					for (StateOnFloor stateOnFloor : stateOnFloorList) {
						if (stateOnFloor.getFloorProperty().equals(
								floorProperty)) {
							target = stateOnFloor;
						}
					}

					if (target != null) {
						deleteStateOnFloor(target);
						stateOnFloorList.remove(target);
					}
				}

				ArrayList<Edges> edgesList = spaceLayer.getEdges();
				for (Edges edges : edgesList) {
					ArrayList<TransitionOnFloor> transitionOnFloorList = edges
							.getTransitionOnFloors();
					TransitionOnFloor target = null;

					for (TransitionOnFloor transitionOnFloor : transitionOnFloorList) {
						if (transitionOnFloor.getFloorProperty().equals(
								floorProperty)) {
							target = transitionOnFloor;
						}
					}

					if (target != null) {
						transitionOnFloorList.remove(target);
					}
				}
			}
		}

		this.buildingProperty.getFloorProperties().remove(floorProperty);
	}

	public void deleteStateOnFloor(StateOnFloor stateOnFloor) {
		ArrayList<State> stateList = stateOnFloor.getStateMember();
		for (State state : stateList) {
			deleteState(state);
		}
	}

	public void deleteState(State selectedState) {
		ArrayList<Edges> edgesList = currentSpaceLayer.getEdges();

		ArrayList<Transition> connects = selectedState.getTransitionReference();
		ArrayList<Double> connectedFloor = new ArrayList<Double>();
		// transition���� �̾��� state���� transition ����
		for (Transition transition : connects) {
			State[] states = transition.getStates();

			State target = null;

			if (states[0].equals(selectedState))
				target = states[1];
			else
				target = states[0];

			target.getTransitionReference().remove(transition);
			if (connectedFloor.contains(target.getPosition().getZ())) {
				connectedFloor.add(target.getPosition().getZ());
			}
		}

		// edges���� transition ����
		for (Edges edges : edgesList) {
			ArrayList<TransitionOnFloor> transitionOnFloorList = edges
					.getTransitionOnFloors();

			for (TransitionOnFloor transitionOnFloor : transitionOnFloorList) {
				ArrayList<Transition> transitionList = transitionOnFloor
						.getTransitionMember();

				for (Transition transition : connects) {
					if (transitionList.contains(transition)) {
						transitionList.remove(transition);
					}
				}
			}
		}

		// duality ����
		CellSpace duality = selectedState.getDuality();
		if(duality != null) {
			duality.setDuality(null);
		}

		// InterLayerConnection
		ArrayList<InterLayerConnection> interLayerConnections = multiLayeredGraph.getInterEdges().get(0).getInterLayerConnectionMember();
		ArrayList<InterLayerConnection> removes = new ArrayList<InterLayerConnection>();
		for(InterLayerConnection ilc : interLayerConnections) {
		        State[] interConnects = ilc.getInterConnects();
		        if(interConnects[0].equals(selectedState) || interConnects[1].equals(selectedState)) {
		                removes.add(ilc);
		        }
		}
		if(removes.size() > 0) {
		        interLayerConnections.removeAll(removes);
		}
		
		// CCTV
		if(selectedState.getCCTVList() != null) {
			for(CCTV cctv : selectedState.getCCTVList()) {
				cctv.setMappedState(null);
			}
		}
		
		currentStateOnFloor.getStateMember().remove(selectedState);

		System.out.println("delete");
	}
	
	public void deleteTransition(Transition transition) {
        State[] states = transition.getStates();
        
        for(State state : states)
            states[0].getTransitionReference().remove(transition);
        
        CellSpaceBoundary duality = transition.getDuality();
        if(duality != null) {
            duality.setDuality(null);
        }
        currentTransitionOnFloor.getTransitionMember().remove(transition);
	}
	
	public void deleteCCTV(CCTV cctv) {
        State mapped = cctv.getMappedState();
        if(mapped != null) {
            mapped.getCCTVList().remove(cctv);
        }

        currentCCTVOnFloor.getCCTVMember().remove(cctv);
	}
}
