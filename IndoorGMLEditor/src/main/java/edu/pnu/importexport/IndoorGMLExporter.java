package edu.pnu.importexport;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.opengis.indoorgml.core.CellSpaceBoundary;
import net.opengis.indoorgml.core.IndoorFeatures;
import net.opengis.indoorgml.core.v_1_0.IndoorFeaturesType;

import org.w3c.dom.Document;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

import edu.pnu.project.ProjectFile;
import edu.pnu.util.IndoorCoordinateGenerator;
import edu.pnu.util.IndoorGMLIDGenerator;
import edu.pnu.util.IndoorGMLJAXBConvertor;

public class IndoorGMLExporter {
	private ProjectFile project;
	private IndoorFeatures indoorFeatures;
	private boolean is3DGeometry;
	
	private Map<CellSpaceBoundary, CellSpaceBoundary> boundary3DMap;

	public IndoorGMLExporter(IndoorFeatures indoorFeatures, boolean is3DGeometry) {
		// TODO Auto-generated constructor stub
		this.indoorFeatures = indoorFeatures;
		this.is3DGeometry = is3DGeometry;
	}
	
	public void setBoundary3DMap(Map<CellSpaceBoundary, CellSpaceBoundary> boundary3DMap) {
		this.boundary3DMap = boundary3DMap;
	}
	
	public void export() throws JAXBException{
		//IndoorGMLIDCoordinateGenerateVisitor idCoordinateVisitor = new IndoorGMLIDCoordinateGenerateVisitor(project.getIs3DGeometry());
		IndoorGMLIDGenerator idGenerator = new IndoorGMLIDGenerator(indoorFeatures, is3DGeometry);
	    IndoorCoordinateGenerator coordinateGenerator = new IndoorCoordinateGenerator(indoorFeatures, is3DGeometry);
		IndoorGMLJAXBConvertor jaxbConvertor = new IndoorGMLJAXBConvertor(indoorFeatures, is3DGeometry, boundary3DMap);
		idGenerator.generateGMLID();
		coordinateGenerator.generate();				
		
		JAXBContext jaxbContext = JAXBContext.newInstance("net.opengis.indoorgml.core.v_1_0"
            + ":net.opengis.gml.v_3_2_1");
		
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://www.opengis.net/indoorgml/1.0/core http://schemas.opengis.net/indoorgml/1.0/indoorgmlcore.xsd");
		try{
			marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new IndoorGMLNameSpaceMapper());
		} catch(PropertyException e){
			e.printStackTrace();
		}
		
		JAXBElement<IndoorFeaturesType> je = jaxbConvertor.getJAXBElement();
		
		JFileChooser save = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter( "IndoorGML Document", "gml" );
		save.setFileFilter(filter);
		int result = save.showSaveDialog(null);
		if( result == JFileChooser.CANCEL_OPTION ) {
			return;
		}
		File output = save.getSelectedFile();

		//marshaller.marshal(je, output);
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
			Document document = db.newDocument();
			
			marshaller.marshal(je, document);
			
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
		/*
		project.makeGMLID();
		
		
		// IndoorGML Export
		JAXBContext jaxbContext = JAXBContext.newInstance("net.opengis.indoorgml.core.v_1_0");
		ObjectFactory factory = new ObjectFactory();
		
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://www.opengis.net/indoorgml/1.0/core http://schemas.opengis.net/indoorgml/1.0/indoorgmlcore.xsd");
		try{
			marshaller.setProperty("com.sun.xml.internal.bind.namespacePrefixMapper", new IndoorGMLNameSpaceMapper());
		} catch(PropertyException e){
			
		}
		
		IndoorFeaturesType indoorGMLFeature = factory.createIndoorFeaturesType();
		//indoorGMLFeature.setBoundedBy( mIndoorGMLFeature.getBoundedBy() );
		//indoorGMLFeature.setDescription( mIndoorGMLFeature.getDescription() );
		//indoorGMLFeature.setDescriptionReference( mIndoorGMLFeature.getDescriptionReference() );
		//indoorGMLFeature.setId( mIndoorGMLFeature.getId() );
		indoorGMLFeature.setId( "IFs" );
		//indoorGMLFeature.setIdentifier( mIndoorGMLFeature.getIdentifier() );
		//indoorGMLFeature.setLocation( mIndoorGMLFeature.getLocation() );
		//indoorGMLFeature.setMultiLayeredGraph( mIndoorGMLFeature.getMultiLayeredGraph() );
		//indoorGMLFeature.setPrimalSpaceFeatures( mIndoorGMLFeature.getPrimalSpaceFeatures() );
		
		project.exportToIndoorGML(indoorGMLFeature, factory);
		
		JAXBElement<IndoorFeaturesType> je = factory.createIndoorFeatures(indoorGMLFeature);
		
		JFileChooser save = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter( "IndoorGML Document", "gml" );
		save.setFileFilter(filter);
		int result = save.showSaveDialog(null);
		if( result == JFileChooser.CANCEL_OPTION ) {
			System.exit(1);
		}
		File output = save.getSelectedFile();

		marshaller.marshal(je, output);
		*/
	}
	
	public class IndoorGMLNameSpaceMapper extends NamespacePrefixMapper {
		private static final String DEFAULT_PREFIX = "";
		private static final String DEFAULT_URI = "http://www.opengis.net/indoorgml/1.0/core";
		
		private static final String GML_PREFIX = "gml";
		private static final String GML_URI = "http://www.opengis.net/gml/3.2";
		
		private static final String XLINK_PREFIX = "xlink";
		private static final String XLINK_URI = "http://www.w3.org/1999/xlink";
		
		@Override
		public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
			if(DEFAULT_URI.equals(namespaceUri)) {
				return DEFAULT_PREFIX;
			} else if(GML_URI.equals(namespaceUri)) {
				return GML_PREFIX;
			} else if(XLINK_URI.equals(namespaceUri)) {
				return XLINK_PREFIX;
			}
			return suggestion;
		}

		@Override
		public String[] getPreDeclaredNamespaceUris() {
			// TODO Auto-generated method stub
			return new String[] { DEFAULT_URI, GML_URI, XLINK_URI };
		}
	}
}
