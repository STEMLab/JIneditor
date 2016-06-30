package edu.pnu.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.opengis.indoorgml.core.CellSpace;
import net.opengis.indoorgml.core.CellSpaceBoundary;
import net.opengis.indoorgml.core.CellSpaceBoundaryOnFloor;
import net.opengis.indoorgml.core.CellSpaceOnFloor;
import net.opengis.indoorgml.core.State;
import net.opengis.indoorgml.core.Transition;
import edu.pnu.project.FloorProperty;
import edu.pnu.project.Mapping;
import edu.pnu.project.ProjectMetaData;
import edu.pnu.project.StateOnFloor;
import edu.pnu.project.TransitionOnFloor;

public class FloorElementBuilder {
	
	public static ArrayList<CellSpaceOnFloor> createCellSpaceOnFloors(ProjectMetaData metaData, List<CellSpace> cellSpaceList) {
		Map<FloorProperty, CellSpaceOnFloor> map = new HashMap<FloorProperty, CellSpaceOnFloor>();
		List<Mapping> mappings = metaData.getMappings();
		
		for(Mapping mapping : mappings) {
			CellSpaceOnFloor cellSpaceOnFloor = new CellSpaceOnFloor();
			cellSpaceOnFloor.setFloorProperty(mapping.getFloorProperty());
			
			map.put(mapping.getFloorProperty(), cellSpaceOnFloor);
		}
		
		for(CellSpace cellSpace : cellSpaceList) {
			for(Mapping mapping : mappings) {
				if(mapping.isContainsMappedElement("CellSpace", cellSpace.getGmlID())) {
					CellSpaceOnFloor cellSpaceOnFloor = map.get(mapping.getFloorProperty());
					cellSpaceOnFloor.getCellSpaceMember().add(cellSpace);
				}
			}
		}
		
		ArrayList<CellSpaceOnFloor> results = new ArrayList<CellSpaceOnFloor>();
		for(CellSpaceOnFloor cellSpaceOnFloor : map.values()) {
			if(cellSpaceOnFloor.getCellSpaceMember().size() > 0) {
				results.add(cellSpaceOnFloor);
			}
		}
		
		return results;
	}
	
	public static ArrayList<CellSpaceBoundaryOnFloor> createCellSpaceBoundaryOnFloors(ProjectMetaData metaData, List<CellSpaceBoundary> cellSpaceBoundaryList) {
		Map<FloorProperty, CellSpaceBoundaryOnFloor> map = new HashMap<FloorProperty, CellSpaceBoundaryOnFloor>();
		List<Mapping> mappings = metaData.getMappings();
		
		for(Mapping mapping : mappings) {
			CellSpaceBoundaryOnFloor cellSpaceBoundaryOnFloor = new CellSpaceBoundaryOnFloor();
			cellSpaceBoundaryOnFloor.setFloorProperty(mapping.getFloorProperty());
			
			map.put(mapping.getFloorProperty(), cellSpaceBoundaryOnFloor);
		}
		
		for(CellSpaceBoundary cellSpaceBoundary : cellSpaceBoundaryList) {
			for(Mapping mapping : mappings) {
				if(mapping.isContainsMappedElement("CellSpaceBoundary", cellSpaceBoundary.getGmlID())) {
					CellSpaceBoundaryOnFloor cellSpaceBoundaryOnFloor = map.get(mapping.getFloorProperty());
					cellSpaceBoundaryOnFloor.getCellSpaceBoundaryMember().add(cellSpaceBoundary);
				}
			}
		}
		
		ArrayList<CellSpaceBoundaryOnFloor> results = new ArrayList<CellSpaceBoundaryOnFloor>();
		for(CellSpaceBoundaryOnFloor cellSpaceBoundaryOnFloor : map.values()) {
			if(cellSpaceBoundaryOnFloor.getCellSpaceBoundaryMember().size() > 0) {
				results.add(cellSpaceBoundaryOnFloor);
			}
		}
		
		return results;
	}
	
	public static ArrayList<StateOnFloor> createStateOnFloors(ProjectMetaData metaData, List<State> stateList) {
		Map<FloorProperty, StateOnFloor> map = new HashMap<FloorProperty, StateOnFloor>();
		List<Mapping> mappings = metaData.getMappings();
		
		for(Mapping mapping : mappings) {
			StateOnFloor stateOnFloor = new StateOnFloor();
			stateOnFloor.setFloorProperty(mapping.getFloorProperty());
			
			map.put(mapping.getFloorProperty(), stateOnFloor);
		}
		
		for(State state : stateList) {
			for(Mapping mapping : mappings) {
				if(mapping.isContainsMappedElement("State", state.getGmlID())) {
					StateOnFloor stateOnFloor = map.get(mapping.getFloorProperty());
					stateOnFloor.getStateMember().add(state);
				}
			}
		}
		
		ArrayList<StateOnFloor> results = new ArrayList<StateOnFloor>();
		for(StateOnFloor stateOnFloor : map.values()) {
			if(stateOnFloor.getStateMember().size() > 0) {
				results.add(stateOnFloor);
			}
		}
		
		return results;
	}
	
	public static ArrayList<TransitionOnFloor> createTransitionOnFloor(ProjectMetaData metaData, List<Transition> transitionList) {
		Map<FloorProperty, TransitionOnFloor> map = new HashMap<FloorProperty, TransitionOnFloor>();
		List<Mapping> mappings = metaData.getMappings();
		
		for(Mapping mapping : mappings) {
			TransitionOnFloor transitionOnFloor = new TransitionOnFloor();
			transitionOnFloor.setFloorProperty(mapping.getFloorProperty());
			
			map.put(mapping.getFloorProperty(), transitionOnFloor);
		}
		
		for(Transition transition : transitionList) {
			for(Mapping mapping : mappings) {
				if(mapping.isContainsMappedElement("Transition", transition.getGmlID())) {
					TransitionOnFloor transitionOnFloor = map.get(mapping.getFloorProperty());
					transitionOnFloor.getTransitionMember().add(transition);
				}
			}
		}
		
		ArrayList<TransitionOnFloor> results = new ArrayList<TransitionOnFloor>();
		for(TransitionOnFloor transitionOnFloor : map.values()) {
			if(transitionOnFloor.getTransitionMember().size() > 0) {
				results.add(transitionOnFloor);
			}
		}
		
		return results;
	}	
	
}
