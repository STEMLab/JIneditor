package net.opengis.indoorgml.core;

import java.io.Serializable;
import java.util.ArrayList;

import edu.pnu.visitor.IndoorGMLElement;
import edu.pnu.visitor.IndoorGMLElementVisitor;

public class SpaceLayer extends AbstractFeature implements Serializable,
		IndoorGMLElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7497272085077190859L;
	private static int labelNumber = 1;
	private ArrayList<Nodes> nodes;
	private ArrayList<Edges> edges;

	public SpaceLayer() {
		super.setGmlID( "IS" + (labelNumber++) );
		
		nodes = new ArrayList<Nodes>();

		nodes.add(new Nodes());
	}

	public ArrayList<Nodes> getNodes() {
		return nodes;
	}

	public void setNodes(ArrayList<Nodes> nodes) {
		this.nodes = nodes;
	}

	public ArrayList<Edges> getEdges() {
		if(edges == null) {
			edges = new ArrayList<Edges>();
			edges.add(new Edges());
		}
		return edges;
	}

	public void setEdges(ArrayList<Edges> edges) {
		this.edges = edges;
	}

	public static int getLabelNumber() {
		return labelNumber;
	}

	public static void setLabelNumber(int labelNumber) {
		SpaceLayer.labelNumber = labelNumber;
	}

	@Override
	public void accept(IndoorGMLElementVisitor visitor) {
		// TODO Auto-generated method stub
		visitor.visit(this);
	}
}