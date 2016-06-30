package net.opengis.indoorgml.core;

import java.io.Serializable;

import edu.pnu.visitor.IndoorGMLElement;
import edu.pnu.visitor.IndoorGMLElementVisitor;

public class InterLayerConnection extends AbstractFeature implements
		Serializable, IndoorGMLElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5308089914493287237L;
	private static int labelNumber = 1;
	private final static int MAX_STATE_NUM = 2;
	private final static int MAX_SPACELAYER_NUM = 2;

	private State interConnects[] = new State[MAX_STATE_NUM];
	private SpaceLayer connectedLayers[] = new SpaceLayer[MAX_SPACELAYER_NUM];
	private String topology;
	private String comment;
	
	public InterLayerConnection() {
		super.setGmlID( "IL" + (labelNumber++) );
	}
	
	public InterLayerConnection(InterLayerConnection other) {
		super(other);
		
		topology = other.topology;
		comment = other.comment;
		
		// interConnects
		// connectedLayers
	}

	public State[] getInterConnects() {
		return interConnects;
	}

	public void setInterConnects(State[] interConnects) {
		this.interConnects = interConnects;
	}

	public SpaceLayer[] getConnectedLayers() {
		return connectedLayers;
	}

	public void setConnectedLayers(SpaceLayer[] connectedLayers) {
		this.connectedLayers = connectedLayers;
	}
	
	public String getTopology() {
		return topology;
	}

	public void setTopology(String topology) {
		this.topology = topology;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public static int getLabelNumber() {
		return labelNumber;
	}

	public static void setLabelNumber(int labelNumber) {
		InterLayerConnection.labelNumber = labelNumber;
	}

	@Override
	public void accept(IndoorGMLElementVisitor visitor) {
		// TODO Auto-generated method stub
		visitor.visit(this);
	}

}