package net.opengis.indoorgml.core;

import java.io.Serializable;
import java.util.ArrayList;

import edu.pnu.visitor.IndoorGMLElement;
import edu.pnu.visitor.IndoorGMLElementVisitor;

public class MultiLayeredGraph extends AbstractFeature implements Serializable,
		IndoorGMLElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2696458817647102257L;
	private static int labelNumber = 1;
	private ArrayList<SpaceLayers> spaceLayers;
	private ArrayList<InterEdges> interEdges;

	public MultiLayeredGraph() {
		super.setGmlID( "MLG" + (labelNumber++) );
		spaceLayers = new ArrayList<SpaceLayers>();

		spaceLayers.add(new SpaceLayers());
	}

	public ArrayList<SpaceLayers> getSpaceLayers() {
		return spaceLayers;
	}

	public void setSpaceLayers(ArrayList<SpaceLayers> spaceLayers) {
		this.spaceLayers = spaceLayers;
	}

	public ArrayList<InterEdges> getInterEdges() {
		if(interEdges == null) {
			interEdges = new ArrayList<InterEdges>();
			interEdges.add(new InterEdges());
		}
		
		return interEdges;
	}

	public void setInterEdges(ArrayList<InterEdges> interEdges) {
		this.interEdges = interEdges;
	}

	public static int getLabelNumber() {
		return labelNumber;
	}

	public static void setLabelNumber(int labelNumber) {
		MultiLayeredGraph.labelNumber = labelNumber;
	}

	@Override
	public void accept(IndoorGMLElementVisitor visitor) {
		// TODO Auto-generated method stub
		visitor.visit(this);
	}
}