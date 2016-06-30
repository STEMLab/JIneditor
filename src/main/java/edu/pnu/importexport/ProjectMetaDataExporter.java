package edu.pnu.importexport;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.opengis.indoorgml.core.CellSpace;
import net.opengis.indoorgml.core.CellSpaceBoundary;
import net.opengis.indoorgml.core.CellSpaceBoundaryOnFloor;
import net.opengis.indoorgml.core.CellSpaceOnFloor;
import net.opengis.indoorgml.core.Edges;
import net.opengis.indoorgml.core.IndoorFeatures;
import net.opengis.indoorgml.core.Nodes;
import net.opengis.indoorgml.core.SpaceLayer;
import net.opengis.indoorgml.core.SpaceLayers;
import net.opengis.indoorgml.core.State;
import net.opengis.indoorgml.core.Transition;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.pnu.project.BuildingProperty;
import edu.pnu.project.FloorProperty;
import edu.pnu.project.IndoorGMLIDRegistry;
import edu.pnu.project.ProjectFile;
import edu.pnu.project.StateOnFloor;
import edu.pnu.project.TransitionOnFloor;

public class ProjectMetaDataExporter {
	private ProjectFile project;
	private IndoorFeatures indoorFeatures;
	
	private Document document;	

	public ProjectMetaDataExporter(ProjectFile project, IndoorFeatures indoorFeatures) {
		this.project = project;
		this.indoorFeatures = indoorFeatures;
	}
	
	public Document createDocument() throws ParserConfigurationException {
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
		document = documentBuilder.newDocument();
		
		Element root = getRootElement();
		document.appendChild(root);
		
		return document;
	}
	
	public Element getRootElement() {
		project.saveIndoorGMLID();
		
		Element rootElement = document.createElement(XMLTag.TAG_PROPERTIES);
		
		Element idElement = createIDRegistryElement(project.getIdRegistry());
		rootElement.appendChild(idElement);
		
		Element buildingPropertyElement = createBuildingPropertyElement(project.getBuildingProperty());
		rootElement.appendChild(buildingPropertyElement);
		
		Element currentFloorElement = createCurrentFloorElement(project.getCurrentFloor(), project.getCurrentFloorPlanScale());
		rootElement.appendChild(currentFloorElement);
		
		Element mappingsElement = createMappingsElement(indoorFeatures);
		rootElement.appendChild(mappingsElement);
		
		return rootElement;		
	}
	
	private Element createIDRegistryElement(IndoorGMLIDRegistry registry) {
		Element idElement = document.createElement(XMLTag.TAG_IDREGISTRY);
		
		Element indoorFeatures = document.createElement(XMLTag.TAG_IDREGISTRY_INDOORFEATURES);
		indoorFeatures.appendChild(document.createTextNode("1"));
		idElement.appendChild(indoorFeatures);
		
		Element primalSpaceFeatures = document.createElement(XMLTag.TAG_IDREGISTRY_PRIMALSPACEFEATURES);
		primalSpaceFeatures.appendChild(document.createTextNode(String.valueOf(registry.getPrimalSpaceFeaturesID())));
		idElement.appendChild(primalSpaceFeatures);
		
		Element cellSpace = document.createElement(XMLTag.TAG_IDREGISTRY_CELLSPACE);
		cellSpace.appendChild(document.createTextNode(String.valueOf(registry.getCellSpaceID())));
		idElement.appendChild(cellSpace);
		
		Element cellSpaceBoundary = document.createElement(XMLTag.TAG_IDREGISTRY_CELLSPACEBOUNDARY);
		cellSpaceBoundary.appendChild(document.createTextNode(String.valueOf(registry.getCellSpaceBoundaryID())));
		idElement.appendChild(cellSpaceBoundary);
		
		Element spaceLayers = document.createElement(XMLTag.TAG_IDREGISTRY_SPACELAYERS);
		spaceLayers.appendChild(document.createTextNode(String.valueOf(registry.getSpaceLayersID())));
		idElement.appendChild(spaceLayers);
		
		Element spaceLayer = document.createElement(XMLTag.TAG_IDREGISTRY_SPACELAYER);
		spaceLayer.appendChild(document.createTextNode(String.valueOf(registry.getSpaceLayerID())));
		idElement.appendChild(spaceLayer);
		
		Element nodes = document.createElement(XMLTag.TAG_IDREGISTRY_NODES);
		nodes.appendChild(document.createTextNode(String.valueOf(registry.getNodesID())));
		idElement.appendChild(nodes);
		
		Element state = document.createElement(XMLTag.TAG_IDREGISTRY_STATE);
		state.appendChild(document.createTextNode(String.valueOf(registry.getStateID())));
		idElement.appendChild(state);
		
		Element edges = document.createElement(XMLTag.TAG_IDREGISTRY_EDGES);
		edges.appendChild(document.createTextNode(String.valueOf(registry.getEdgesID())));
		idElement.appendChild(edges);
		
		Element transition = document.createElement(XMLTag.TAG_IDREGISTRY_TRANSITION);
		transition.appendChild(document.createTextNode(String.valueOf(registry.getTransitionID())));
		idElement.appendChild(transition);
		/*
		Element point = document.createElement(XMLTag.TAG_IDREGISTRY_POINT);
		point.appendChild(document.createTextNode(String.valueOf(registry.getPointID())));
		idElement.appendChild(point);
		
		Element lineString = document.createElement(XMLTag.TAG_IDREGISTRY_LINESTRING);
		lineString.appendChild(document.createTextNode(String.valueOf(registry.getLineStringID())));
		idElement.appendChild(lineString);
		
		Element polygon = document.createElement(XMLTag.TAG_IDREGISTRY_POLYGON);
		polygon.appendChild(document.createTextNode(String.valueOf(registry.getPolygonID())));
		idElement.appendChild(polygon);
		
		Element solid = document.createElement(XMLTag.TAG_IDREGISTRY_SOLID);
		solid.appendChild(document.createTextNode(String.valueOf(registry.getSolidID())));
		idElement.appendChild(solid);
		*/
		return idElement;
	}

	private Element createBuildingPropertyElement(BuildingProperty buildingProperty) {
		Element building = document.createElement(XMLTag.TAG_BUILDINGPROPERTY);
		ArrayList<FloorProperty> floorPropertyList = buildingProperty.getFloorProperties();
		
		for(FloorProperty floorProperty : floorPropertyList) {
			Element floor = document.createElement(XMLTag.TAG_BUILDINGPROPERTY_FLOORPROPERTY);
			
			Element level = document.createElement(XMLTag.TAG_BUILDINGPROPERTY_FLOORPROPERTY_LEVEL);
			level.appendChild(document.createTextNode(floorProperty.getLevel()));
			floor.appendChild(level);
			
			Element lowerCorner = document.createElement(XMLTag.TAG_BUILDINGPROPERTY_FLOORPROPERTY_LOWERCORNER);
			lowerCorner.appendChild(document.createTextNode(floorProperty.getBottomLeftPoint().toString()));
			floor.appendChild(lowerCorner);
			
			Element upperCorner = document.createElement(XMLTag.TAG_BUILDINGPROPERTY_FLOORPROPERTY_UPPERCORNER);
			upperCorner.appendChild(document.createTextNode(floorProperty.getTopRightPoint().toString()));
			floor.appendChild(upperCorner);
			
			Element groundHeight = document.createElement(XMLTag.TAG_BUILDINGPROPERTY_FLOORPROPERTY_GROUNDHEIGHT);
			upperCorner.appendChild(document.createTextNode(String.valueOf(floorProperty.getGroundHeight())));
			floor.appendChild(groundHeight);
			
			Element ceilingHeight = document.createElement(XMLTag.TAG_BUILDINGPROPERTY_FLOORPROPERTY_CEILINGHEIGHT);
			upperCorner.appendChild(document.createTextNode(String.valueOf(floorProperty.getCeilingHeight())));
			floor.appendChild(ceilingHeight);
			
			Element doorHeight = document.createElement(XMLTag.TAG_BUILDINGPROPERTY_FLOORPROPERTY_DOORHEIGHT);
			upperCorner.appendChild(document.createTextNode(String.valueOf(floorProperty.getDoorHeight())));
			floor.appendChild(doorHeight);
			
			Element floorPlan = document.createElement(XMLTag.TAG_BUILDINGPROPERTY_FLOORPROPERTY_FLOORPLANPATH);
			floorPlan.appendChild(document.createTextNode(floorProperty.getFloorPlanPath()));
			floor.appendChild(floorPlan);
			
			building.appendChild(floor);
		}
		
		return building;
	}
	
	private Element createCurrentFloorElement(String currentFloorLevel, double currentScale) {
		Element currentFloor = document.createElement(XMLTag.TAG_CURRENTFLOOR);
		
		if(currentScale != 0) {
			Element scale = document.createElement(XMLTag.TAG_CURRENTFLOOR_SCALE);
			scale.appendChild(document.createTextNode(String.valueOf(currentScale)));
			currentFloor.appendChild(scale);
		}
		
		if(currentFloorLevel != null) {
			Element level = document.createElement(XMLTag.TAG_CURRENTFLOOR_LEVEL);
			level.appendChild(document.createTextNode(currentFloorLevel));
			currentFloor.appendChild(level);
		}
		
		if(currentFloor.getChildNodes().getLength() == 0) return null;
		
		return currentFloor;
	}
	
	private Element createMappingsElement(IndoorFeatures indoorFeatures) {
		Element mappings = document.createElement(XMLTag.TAG_MAPPINGS);
		
		ArrayList<CellSpaceOnFloor> cellSpaceOnFloors = indoorFeatures.getPrimalSpaceFeatures().getCellSpaceOnFloors();
		for(CellSpaceOnFloor cellSpaceOnFloor : cellSpaceOnFloors) {
			Element mapping = createCellSpaceMappingElement(cellSpaceOnFloor);
			if(mapping != null) {
				mappings.appendChild(mapping);
			}
		}
		
		ArrayList<CellSpaceBoundaryOnFloor> cellSpaceBoundaryOnFloors = indoorFeatures.getPrimalSpaceFeatures().getCellSpaceBoundaryOnFloors();
		for(CellSpaceBoundaryOnFloor cellSpaceBoundaryOnFloor : cellSpaceBoundaryOnFloors) {
			Element mapping = createCellSpaceBoundaryMappingElement(cellSpaceBoundaryOnFloor);
			if(mapping != null) {
				mappings.appendChild(mapping);
			}
		}
		
		ArrayList<SpaceLayers> spaceLayerss = indoorFeatures.getMultiLayeredGraph().getSpaceLayers();
		for(SpaceLayers spaceLayers : spaceLayerss) {
			ArrayList<SpaceLayer> spaceLayerMembers = spaceLayers.getSpaceLayerMember();
			for(SpaceLayer spaceLayer : spaceLayerMembers) {
				ArrayList<Nodes> nodess = spaceLayer.getNodes();
				for(Nodes nodes : nodess) {
					ArrayList<StateOnFloor> stateOnFloors = nodes.getStateOnFloors();
					for(StateOnFloor stateOnFloor : stateOnFloors) {
						Element mapping = createStateMappingElement(stateOnFloor);
						if(mapping != null) {
							mappings.appendChild(mapping);
						}
					}
				}
				
				ArrayList<Edges> edgess = spaceLayer.getEdges();
				for(Edges edges : edgess) {
					ArrayList<TransitionOnFloor> transitionOnFloors = edges.getTransitionOnFloors();
					for(TransitionOnFloor transitionOnFloor : transitionOnFloors) {
						Element mapping = createTransitionMappingElement(transitionOnFloor);
						if(mapping != null) {
							mappings.appendChild(mapping);
						}
					}
				}
			}
		}
		
		
		return mappings;
	}
	
	private Element createCellSpaceMappingElement(CellSpaceOnFloor cellSpaceOnFloor) {
		Element mapping = document.createElement(XMLTag.TAG_MAPPINGS_MAPPING);
		mapping.setAttribute(XMLTag.TAG_MAPPINGS_MAPPING_LEVEL, cellSpaceOnFloor.getFloorProperty().getLevel());
		mapping.setAttribute(XMLTag.TAG_MAPPINGS_MAPPING_TYPE, XMLTag.TAG_MAPPINGS_MAPPING_CELLSPACE);
		
		ArrayList<CellSpace> cellSpaces = cellSpaceOnFloor.getCellSpaceMember();
		if(cellSpaces.size() == 0) return null;
		
		for(CellSpace cellSpace : cellSpaces) {
			Element cellSpaceElement = document.createElement(XMLTag.TAG_MAPPINGS_MAPPING_CELLSPACE);
			Element idElement = document.createElement(XMLTag.TAG_MAPPINGS_MAPPING_ID);
			
			idElement.appendChild(document.createTextNode(cellSpace.getGmlID()));
			cellSpaceElement.appendChild(idElement);
			mapping.appendChild(cellSpaceElement);				
		}
		
		return mapping;
	}
	
	private Element createCellSpaceBoundaryMappingElement(CellSpaceBoundaryOnFloor cellSpaceBoundaryOnFloor) {
		Element mapping = document.createElement(XMLTag.TAG_MAPPINGS_MAPPING);
		mapping.setAttribute(XMLTag.TAG_MAPPINGS_MAPPING_LEVEL, cellSpaceBoundaryOnFloor.getFloorProperty().getLevel());
		mapping.setAttribute(XMLTag.TAG_MAPPINGS_MAPPING_TYPE, XMLTag.TAG_MAPPINGS_MAPPING_CELLSPACEBOUNDARY);
		
		ArrayList<CellSpaceBoundary> cellSpaceBoundaries = cellSpaceBoundaryOnFloor.getCellSpaceBoundaryMember();
		if(cellSpaceBoundaries.size() == 0) return null;
		
		for(CellSpaceBoundary cellSpaceBoundary : cellSpaceBoundaries) {
			Element cellSpaceBoundaryElement = document.createElement(XMLTag.TAG_MAPPINGS_MAPPING_CELLSPACEBOUNDARY);
			Element idElement = document.createElement(XMLTag.TAG_MAPPINGS_MAPPING_ID);
			
			idElement.appendChild(document.createTextNode(cellSpaceBoundary.getGmlID()));
			cellSpaceBoundaryElement.appendChild(idElement);
			mapping.appendChild(cellSpaceBoundaryElement);				
		}
		
		return mapping;
	}
	
	private Element createStateMappingElement(StateOnFloor stateOnFloor) {
		Element mapping = document.createElement(XMLTag.TAG_MAPPINGS_MAPPING);
		mapping.setAttribute(XMLTag.TAG_MAPPINGS_MAPPING_LEVEL, stateOnFloor.getFloorProperty().getLevel());
		mapping.setAttribute(XMLTag.TAG_MAPPINGS_MAPPING_TYPE, XMLTag.TAG_MAPPINGS_MAPPING_STATE);
		
		ArrayList<State> states = stateOnFloor.getStateMember();
		if(states.size() == 0) return null;
		
		for(State state : states) {
			Element stateElement = document.createElement(XMLTag.TAG_MAPPINGS_MAPPING_STATE);
			Element idElement = document.createElement(XMLTag.TAG_MAPPINGS_MAPPING_ID);
			
			idElement.appendChild(document.createTextNode(state.getGmlID()));
			stateElement.appendChild(idElement);
			mapping.appendChild(stateElement);				
		}
		
		return mapping;
	}
	
	private Element createTransitionMappingElement(TransitionOnFloor transitionOnFloor) {
		Element mapping = document.createElement(XMLTag.TAG_MAPPINGS_MAPPING);
		mapping.setAttribute(XMLTag.TAG_MAPPINGS_MAPPING_LEVEL, transitionOnFloor.getFloorProperty().getLevel());
		mapping.setAttribute(XMLTag.TAG_MAPPINGS_MAPPING_TYPE, XMLTag.TAG_MAPPINGS_MAPPING_TRANSITION);
		
		ArrayList<Transition> transitions = transitionOnFloor.getTransitionMember();
		if(transitions.size() == 0) return null;
		
		for(Transition transition : transitions) {
			Element transitionElement = document.createElement(XMLTag.TAG_MAPPINGS_MAPPING_TRANSITION);
			Element idElement = document.createElement(XMLTag.TAG_MAPPINGS_MAPPING_ID);
			
			idElement.appendChild(document.createTextNode(transition.getGmlID()));
			transitionElement.appendChild(idElement);
			mapping.appendChild(transitionElement);				
		}
		
		return mapping;
	}
}
