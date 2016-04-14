package edu.pnu.importexport;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.opengis.indoorgml.geometry.Point;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.pnu.project.BuildingProperty;
import edu.pnu.project.FloorProperty;
import edu.pnu.project.IndoorGMLIDRegistry;
import edu.pnu.project.Mapping;
import edu.pnu.project.ProjectMetaData;

public class ProjectMetaDataImporter {
	private Document document;	

	public ProjectMetaDataImporter(String filepath) throws ParserConfigurationException, SAXException, IOException {
		// Get Docuemnt Builder
		DocumentBuilderFactory factory 	= DocumentBuilderFactory.newInstance();
		DocumentBuilder builder 		= factory.newDocumentBuilder();
		// Build Document
		Document document = builder.parse(new File(filepath));
		// Normalize the XML Structure; It's just too important !!
		document.getDocumentElement().normalize();
		
		this.document = document;
	}
	
	public ProjectMetaDataImporter(Document document) {
		this.document = document;
	}
	
	public ProjectMetaData getProjectMetaData() {
		ProjectMetaData metaData = new ProjectMetaData();
		
		Element idRegistryElement = (Element) document.getElementsByTagName(XMLTag.TAG_IDREGISTRY).item(0);
		if(idRegistryElement != null) {
			IndoorGMLIDRegistry idRegistry = createIDRegistry(null, idRegistryElement);
			metaData.setIDRegistry(idRegistry);
		}
		
		Element buildingPropertyElement = (Element) document.getElementsByTagName(XMLTag.TAG_BUILDINGPROPERTY).item(0);
		if(buildingPropertyElement != null) {
			BuildingProperty buildingProperty = createBuildingProperty(null, buildingPropertyElement);
			metaData.setBuildingProperty(buildingProperty);
		}
		
		Element currentFloorElement = (Element) document.getElementsByTagName(XMLTag.TAG_CURRENTFLOOR).item(0);
		if(currentFloorElement != null) {
			String scale = getNodeValuebyName(XMLTag.TAG_CURRENTFLOOR_SCALE, currentFloorElement);
			if(scale != null) {
				metaData.setCurrentScale(Double.parseDouble(scale));
			}
			
			String level = getNodeValuebyName(XMLTag.TAG_CURRENTFLOOR_LEVEL, currentFloorElement);
			if(level != null) {
				metaData.setCurrentFloorLevel(level);
			}
		}
		
		Element mappingsElement = (Element) document.getElementsByTagName(XMLTag.TAG_MAPPINGS).item(0);
		if(mappingsElement != null) {
			List<Mapping> mappings = createMappings(null, metaData.getBuildingProperty(), mappingsElement);
			metaData.setMappings(mappings);
		}
				
		return metaData;
	}
	
	private IndoorGMLIDRegistry createIDRegistry(IndoorGMLIDRegistry target, Element element) {		
		if(target == null) {
			target = new IndoorGMLIDRegistry();
		}
		
		Element indoorFeatures = (Element) element.getElementsByTagName(XMLTag.TAG_IDREGISTRY_INDOORFEATURES);
		if(indoorFeatures != null) {
		}
		
		String label = null;
		label = getNodeValuebyName(XMLTag.TAG_IDREGISTRY_PRIMALSPACEFEATURES, element);
		if(label != null) {
			target.setPrimalSpaceFeaturesID(Integer.parseInt(label));
		}
		
		label = getNodeValuebyName(XMLTag.TAG_IDREGISTRY_CELLSPACE, element);
		if(label != null) {
			target.setCellSpaceID(Integer.parseInt(label));
		}
		
		label = getNodeValuebyName(XMLTag.TAG_IDREGISTRY_CELLSPACEBOUNDARY, element);
		if(label != null) {
			target.setCellSpaceBoundaryID(Integer.parseInt(label));
		}
		
		label = getNodeValuebyName(XMLTag.TAG_IDREGISTRY_MULTILAYEREDGRAPH, element);
		if(label != null) {
			target.setMultiLayeredGraphID(Integer.parseInt(label));
		}
		
		label = getNodeValuebyName(XMLTag.TAG_IDREGISTRY_SPACELAYERS, element);
		if(label != null) {
			target.setSpaceLayersID(Integer.parseInt(label));
		}
		
		label = getNodeValuebyName(XMLTag.TAG_IDREGISTRY_SPACELAYER, element);
		if(label != null) {
			target.setSpaceLayerID(Integer.parseInt(label));
		}
		
		label = getNodeValuebyName(XMLTag.TAG_IDREGISTRY_NODES, element);
		if(label != null) {
			target.setNodesID(Integer.parseInt(label));
		}
		
		label = getNodeValuebyName(XMLTag.TAG_IDREGISTRY_STATE, element);
		if(label != null) {
			target.setStateID(Integer.parseInt(label));
		}
		
		label = getNodeValuebyName(XMLTag.TAG_IDREGISTRY_EDGES, element);
		if(label != null) {
			target.setEdgesID(Integer.parseInt(label));
		}
		
		label = getNodeValuebyName(XMLTag.TAG_IDREGISTRY_TRANSITION, element);
		if(label != null) {
			target.setTransitionID(Integer.parseInt(label));
		}
		
		label = getNodeValuebyName(XMLTag.TAG_IDREGISTRY_INTEREDGES, element);
		if(label != null) {
			target.setInterEdgesID(Integer.parseInt(label));
		}
		
		label = getNodeValuebyName(XMLTag.TAG_IDREGISTRY_INTERLAYERCONNECTION, element);
		if(label != null) {
			target.setInterLayerConnectionID(Integer.parseInt(label));
		}
		
		
		
		//gml label
		
		return target;
	}
	
	private BuildingProperty createBuildingProperty(BuildingProperty target, Element element) {
		if(target == null) {
			target = new BuildingProperty();
		}
		
		NodeList nodeList = element.getElementsByTagName(XMLTag.TAG_BUILDINGPROPERTY_FLOORPROPERTY);
		for(int i = 0; i < nodeList.getLength(); i++) {
			FloorProperty floorProperty = new FloorProperty();
			String value = null;
			
			value = getNodeValuebyName(XMLTag.TAG_BUILDINGPROPERTY_FLOORPROPERTY_LEVEL, (Element) nodeList.item(i));
			if(value != null) {
				floorProperty.setLevel(value);
			}
			
			value = getNodeValuebyName(XMLTag.TAG_BUILDINGPROPERTY_FLOORPROPERTY_LOWERCORNER, (Element) nodeList.item(i));
			if(value != null) {
				floorProperty.setBottomLeftPoint(createPoint(value));
			}
			
			value = getNodeValuebyName(XMLTag.TAG_BUILDINGPROPERTY_FLOORPROPERTY_UPPERCORNER, (Element) nodeList.item(i));
			if(value != null) {
				floorProperty.setTopRightPoint(createPoint(value));
			}
			
			value = getNodeValuebyName(XMLTag.TAG_BUILDINGPROPERTY_FLOORPROPERTY_GROUNDHEIGHT, (Element) nodeList.item(i));
			if(value != null) {
				floorProperty.setGroundHeight(Double.parseDouble(value));
			}
			
			value = getNodeValuebyName(XMLTag.TAG_BUILDINGPROPERTY_FLOORPROPERTY_CEILINGHEIGHT, (Element) nodeList.item(i));
			if(value != null) {
				floorProperty.setCeilingHeight(Double.parseDouble(value));
			}
			
			value = getNodeValuebyName(XMLTag.TAG_BUILDINGPROPERTY_FLOORPROPERTY_DOORHEIGHT, (Element) nodeList.item(i));
			if(value != null) {
				floorProperty.setDoorHeight(Double.parseDouble(value));
			}
			
			value = getNodeValuebyName(XMLTag.TAG_BUILDINGPROPERTY_FLOORPROPERTY_FLOORPLANPATH, (Element) nodeList.item(i));
			if(value != null) {
				floorProperty.setFloorPlanPath(value);
			}
			
			target.getFloorProperties().add(floorProperty);
		}		
		
		return target;
	}
	
	private List<Mapping> createMappings(List<Mapping> target, BuildingProperty buildingProperty, Element element) {
		Map<String, Mapping> mappingMap = new HashMap<String, Mapping>();
		
		if(target == null) {
			target = new ArrayList<Mapping>();
		}
		
		NodeList nodeList = element.getElementsByTagName(XMLTag.TAG_MAPPINGS_MAPPING);
		for(int i = 0; i < nodeList.getLength(); i++) {
			Element child = (Element) nodeList.item(i);
			
			String level = child.getAttribute(XMLTag.TAG_MAPPINGS_MAPPING_LEVEL);
			if(level != null) {
				if(!mappingMap.containsKey(level)) {
					Mapping mapping = new Mapping(level, buildingProperty.getFloorProperty(level));
					mappingMap.put(level, mapping);
				}
			} else {
				//throw IllegalFormatException("Attribute level is not availiable");
			}
			
			Mapping mapping = mappingMap.get(level);
			String type = child.getAttribute(XMLTag.TAG_MAPPINGS_MAPPING_TYPE);
			Node mappedNode = element.getFirstChild();
			
			do {
				String id = getNodeValuebyName(XMLTag.TAG_MAPPINGS_MAPPING_ID, (Element) mappedNode);
				mapping.addMappedElement(type, id);
				
				mappedNode = mappedNode.getNextSibling();
			} while(mappedNode != null);
		}
		
		target.addAll(mappingMap.values());
		
		return target;
	}

	private String getNodeValuebyName(String tagName, Element element){
		String 	value = null;
		Node node  = element.getFirstChild();
		
		do {
			if(node.getNodeName().equalsIgnoreCase(tagName)){
				value = node.getFirstChild().getNodeValue();
				break;
			}
			else
				node = node.getNextSibling();
		} while(node != null);
		
		if(value != null){
			value = value.replace("\n", "");
			value = value.replace("\t", "");
		}
		
		return value;
	}
	
	private int getLabelFromGMLID(String id) {
		String label = id.replaceAll("[^0-9]", "");
		
		return Integer.parseInt(label);
	}
	
	private int getLabelFromElement(Element element) {
		String id = getNodeValuebyName("gml:name", element);
		
		if(id != null) {
			return getLabelFromGMLID(id);
		}
		
		return -1;
	}
	
	private Point createPoint(String value) {
		value = value.replace("(", "");
		value = value.replace(")", "");
		
		String[] values = value.split(", ");
		Point point = new Point();
		point.setPanelX(Double.parseDouble(values[0]));
		point.setPanelY(Double.parseDouble(values[1]));
		point.setZ(Double.parseDouble(values[2]));
		
		return point;
	}
}
