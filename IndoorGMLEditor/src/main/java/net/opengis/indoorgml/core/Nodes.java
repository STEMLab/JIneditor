package net.opengis.indoorgml.core;

import java.io.Serializable;
import java.util.ArrayList;

import edu.pnu.project.StateOnFloor;
import edu.pnu.visitor.IndoorGMLElement;
import edu.pnu.visitor.IndoorGMLElementVisitor;

public class Nodes extends AbstractFeature implements Serializable,
		IndoorGMLElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8097939276530949206L;
	private static int labelNumber = 1;
	private ArrayList<StateOnFloor> stateOnFloors;

	public Nodes() {
		super.setGmlID( "N" + (labelNumber++) );
	}
	
	public Nodes(Nodes other) {
		super(other);
	}

	public ArrayList<StateOnFloor> getStateOnFloors() {
		if (stateOnFloors == null) {
			stateOnFloors = new ArrayList<StateOnFloor>();
		}
		return stateOnFloors;
	}

	public void setStateOnFloors(ArrayList<StateOnFloor> stateOnFloors) {
		this.stateOnFloors = stateOnFloors;
	}

	public static int getLabelNumber() {
		return labelNumber;
	}

	public static void setLabelNumber(int labelNumber) {
		Nodes.labelNumber = labelNumber;
	}

	@Override
	public void accept(IndoorGMLElementVisitor visitor) {
		// TODO Auto-generated method stub
		visitor.visit(this);
	}
}