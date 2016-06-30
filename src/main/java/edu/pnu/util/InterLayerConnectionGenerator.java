package edu.pnu.util;

import java.util.ArrayList;
import java.util.List;

import edu.pnu.project.StateOnFloor;
import net.opengis.indoorgml.core.CellSpace;
import net.opengis.indoorgml.core.InterLayerConnection;
import net.opengis.indoorgml.core.PrimalSpaceFeatures;
import net.opengis.indoorgml.core.SpaceLayer;
import net.opengis.indoorgml.core.State;

public class InterLayerConnectionGenerator {
	private SpaceLayer target1SpaceLayer, target2SpaceLayer;
	private PrimalSpaceFeatures primalSpace;

	// InterLayerConnection은 target1에 있는 State를 기준으로 생성한다.
	// target1의 State의 Duality CellSpace에 포함되는 State를 target2에서 찾는다.
	// 찾은 State가 1개면 equals, 2개 이상이면 Contains로 생성
	// 다른 Topology 관계는 일단 제외 - target2의 State가 dulaity가 있을 때 target1의 state의 duality의 기하 contains관계 체크
	public InterLayerConnectionGenerator(PrimalSpaceFeatures primalSpace, SpaceLayer target1, SpaceLayer target2) {
		this.primalSpace = primalSpace;
		this.target1SpaceLayer = target1;
		this.target2SpaceLayer = target2;
	}
	
	public List<InterLayerConnection> getInterLayerConnections() {
		List ilcMembers = new ArrayList<InterLayerConnection>();

		ArrayList<StateOnFloor> target1SOF = target1SpaceLayer.getNodes().get(0).getStateOnFloors();
		ArrayList<StateOnFloor> target2SOF = target2SpaceLayer.getNodes().get(0).getStateOnFloors();
		
		for(StateOnFloor target1sof : target1SOF) {
			for(StateOnFloor target2sof : target2SOF) {
				if(target1sof.getFloorProperty().equals(target2sof.getFloorProperty())) {
					ArrayList<State> target1States = (ArrayList<State>) target1sof.getStateMember().clone();
					ArrayList<State> target2States = (ArrayList<State>) target2sof.getStateMember().clone();
					for(State target1State : target1States) {
						if(target1State.getDuality() == null) continue;
						
						CellSpace duality = target1State.getDuality();
						List<State> candidates = new ArrayList<State>();
						for(State target2State : target2States) {
							// target2State가 duality가 있을 경우 추가 필요
							
							if(GeometryUtil.isContainsPolygon(duality.getGeometry2D(), target2State.getPosition())) {
								candidates.add(target2State);
							}
						}
						if(candidates.size() > 0) {
							List generatedILC = createILCFromCandidate(target1State, candidates, target1SpaceLayer, target2SpaceLayer);
							ilcMembers.addAll(generatedILC);
						}
					}
					
					break;
				}
			}
		}
		
		return ilcMembers;
	}
	
	private List<InterLayerConnection> createILCFromCandidate(State target, List<State> candidates, 
			SpaceLayer target1SpaceLayer, SpaceLayer target2SpaceLayer) {
		List<InterLayerConnection> ilcs = new ArrayList<InterLayerConnection>();
		
		String topology = null;
		if(candidates.size() == 1) {
			topology = "EQUALS";
		} else if(candidates.size() > 1) {
			topology = "CONTAINS";
		}

		SpaceLayer[] connectedLayers = new SpaceLayer[]{target1SpaceLayer, target2SpaceLayer};
		for(State candidate : candidates) {
			State[] interConnects = new State[]{target, candidate};
			InterLayerConnection ilc = new InterLayerConnection();
			
			ilc.setInterConnects(interConnects);
			ilc.setConnectedLayers(connectedLayers);
			ilc.setTopology(topology);
			ilcs.add(ilc);
		}
		
		return ilcs;
	}
}
