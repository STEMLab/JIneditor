package edu.pnu.importexport;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.opengis.citygml.building.AbstractBuilding;
import net.opengis.citygml.v_2_0.CityModelType;
import net.opengis.indoorgml.core.CellSpaceBoundary;
import net.opengis.indoorgml.core.IndoorFeatures;

import org.w3c.dom.Document;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

import edu.pnu.project.ProjectFile;
import edu.pnu.util.CityGMLJAXBConvertor;
import edu.pnu.util.IndoorCoordinateGenerator;
import edu.pnu.util.IndoorGML3DGeometryBuilder;
import edu.pnu.util.IndoorGMLCloneGenerator;
import edu.pnu.util.IndoorGMLIDGenerator;

public class CityGMLExporter {
	private ProjectFile project;
	private boolean is3DGeometry = true;
	private List<AbstractBuilding> buildingList;

	public CityGMLExporter(ProjectFile project, List<AbstractBuilding> buildingList) {
		this.project = project;
		this.buildingList = buildingList;
	}

	public void export() throws JAXBException {
		IndoorFeatures indoorFeatures = project.getIndoorFeatures();
		
		IndoorGMLCloneGenerator cloneGenerator = new IndoorGMLCloneGenerator();
		IndoorFeatures clone = cloneGenerator.getClone(indoorFeatures);
		Map<CellSpaceBoundary, CellSpaceBoundary> xLinkBoundaryMap = cloneGenerator.getXLinkBoundaryMap();
		
		IndoorGML3DGeometryBuilder builder = new IndoorGML3DGeometryBuilder(null, clone, xLinkBoundaryMap);
		builder.create3DGeometry();		
		Map<CellSpaceBoundary, CellSpaceBoundary> boundary3DMap = builder.getBoundary3DMap();
		
		IndoorGMLIDGenerator idGenerator = new IndoorGMLIDGenerator(clone, is3DGeometry);
	    IndoorCoordinateGenerator coordinateGenerator = new IndoorCoordinateGenerator(clone, is3DGeometry);
		idGenerator.generateGMLID();
		coordinateGenerator.generate();
		

		CityGMLJAXBConvertor jaxbConvertor = new CityGMLJAXBConvertor(clone, boundary3DMap, project.getBuildingProperty().getFloorProperties(), buildingList);
		JAXBElement<CityModelType> jCityModel = jaxbConvertor.getJAXBElement();
		
		JAXBContext context;
		Marshaller marshaller;
		
		context = JAXBContext.newInstance(
				"net.opengis.citygml.v_2_0"
				+ ":net.opengis.citygml.building.v_2_0"
				+ ":net.opengis.gml.v_3_1_1"
				+ ":org.w3.smil.v_2_0"
				+ ":org.w3.smil.v_2_0.language"
				+ ":org.w3.xlink"
		);
		
		File output = null;
		JFileChooser save = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter( "CityGML Document", "gml" );
		save.setFileFilter(filter);
		int result = save.showSaveDialog(null);
		if( result == JFileChooser.CANCEL_OPTION ) {
			return;
		}
		output = save.getSelectedFile();
		
		
		marshaller = context.createMarshaller();

		//marshaller.setProperty("com.sun.xml.bind.marshaller.NamespacePrefixMapper", new CityGMLNamespaceMapper());
		marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, 
				"http://www.opengis.net/citygml/profiles/base/2.0 http://schemas.opengis.net/citygml/profiles/base/2.0/CityGML.xsd");
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
		/*
		try{
			marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new CityGMLNameSpaceMapper());
		} catch(PropertyException e){
			e.printStackTrace();
		}
		*/
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
			Document document = db.newDocument();
			
			marshaller.marshal(jCityModel, document);
			
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			t.setOutputProperty(OutputKeys.INDENT,"yes");
			t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			DOMSource source = new DOMSource(document);
			StreamResult streamResult = new StreamResult(output);
			t.transform(source, streamResult);
		} catch (TransformerException | ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public class CityGMLNameSpaceMapper extends NamespacePrefixMapper {
		private static final String BASE_PREFIX = "";
		private static final String BASE_URI = "http://www.opengis.net/citygml/profiles/base/2.0";
		
		private static final String CORE_PREFIX = "core";
		private static final String CORE_URI = "http://www.opengis.net/citygml/2.0";
		
		private static final String APPEARANCE_PREFIX = "app";
		private static final String APPEARANCE_URI = "http://www.opengis.net/citygml/appearance/2.0";
		
		private static final String RELIEF_PREFIX = "dem";
		private static final String RELIEF_URI = "http://www.opengis.net/citygml/relief/2.0";
		
		private static final String BUILDING_PREFIX = "bldg";
		private static final String BUILDING_URI = "http://www.opengis.net/citygml/building/2.0";
				
		private static final String GML_PREFIX = "gml";
		private static final String GML_URI = "http://www.opengis.net/gml";
		
		private static final String XLINK_PREFIX = "xlink";
		private static final String XLINK_URI = "http://www.w3.org/1999/xlink";
		
		private static final String XSI_PREFIX = "xsi";
		private static final String XSI_URI = "http://www.w3.org/2001/XMLSchema-instance";
		
		private static final String XAL_PREFIX = "xAL";
		private static final String XAL_URI = "urn:oasis:names:tc:ciq:xsdschema:xAL:2.0";
		
		@Override
		public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
			if(BASE_URI.equals(namespaceUri)) {
				return BASE_PREFIX;
			} else if(CORE_URI.equals(namespaceUri)) {
				return CORE_PREFIX;
			} else if(APPEARANCE_URI.equals(namespaceUri)) {
				return APPEARANCE_PREFIX;
			} else if(RELIEF_URI.equals(namespaceUri)) {
				return RELIEF_PREFIX;
			} else if(BUILDING_URI.equals(namespaceUri)) {
				return BUILDING_PREFIX;
			} else if(GML_URI.equals(namespaceUri)) {
				return GML_PREFIX;
			} else if(XLINK_URI.equals(namespaceUri)) {
				return XLINK_PREFIX;
			} else if(XSI_URI.equals(namespaceUri)) {
				return XSI_PREFIX;
			} else if(XAL_URI.equals(namespaceUri)) {
				return XAL_PREFIX;
			} 
			return suggestion;
		}

		@Override
		public String[] getPreDeclaredNamespaceUris() {
			// TODO Auto-generated method stub
			return new String[] { BASE_URI, CORE_URI, APPEARANCE_URI, RELIEF_URI, BUILDING_URI, GML_URI, XLINK_URI, XSI_URI, XAL_URI };
		}
	}
}
