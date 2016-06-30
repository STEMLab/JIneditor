package net.opengis.indoorgml.core;

import java.io.Serializable;
import java.util.ArrayList;

import net.opengis.indoorgml.geometry.Point;
import edu.pnu.visitor.IndoorGMLElement;
import edu.pnu.visitor.IndoorGMLElementVisitor;

public class State extends AbstractFeature implements Serializable, IndoorGMLElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6585679677817620981L;
	private static int labelNumber = 1;
	private Point position;
	private CellSpace duality;
	private ArrayList<Transition> connects;
	private Point realPoint;
	
	public State() {
		super.setGmlID("R" + (labelNumber++));
	}
	
	public State(State other) {
		super(other);
		position = other.position.clone();
		
		// duality
		// connects
	}
	
	public Point getPosition() {
		return position;
	}
	
	public void setPosition(Point position) {
		this.position = position;
	}
	
	public CellSpace getDuality() {
		return duality;
	}
	
	public void setDuality(CellSpace duality) {
		if(duality == null)
			this.duality =null;
		else
			//this.duality = duality.replace("#", "");
			this.duality = duality;
	}
	
	public ArrayList<Transition> getTransitionReference() {
		if (connects == null) {
			connects = new ArrayList<Transition>();
		}
		return connects;
	}
	
	public void setTransitions(ArrayList<Transition> transitionReference) {
		connects = transitionReference;
		/*
		for(int tempNum = 0; tempNum < transitionReference.size(); tempNum++){
			connects.add(transitionReference.get(tempNum).replace("#", ""));
		}
		*/
	}

	public static int getLabelNumber() {
		return labelNumber;
	}

	public static void setLabelNumber(int labelNumber) {
		State.labelNumber = labelNumber;
	}

	@Override
	public void accept(IndoorGMLElementVisitor visitor) {
		// TODO Auto-generated method stub
		visitor.visit(this);
	}
}