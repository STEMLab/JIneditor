package net.opengis.indoorgml.core;

import java.io.Serializable;
import java.util.ArrayList;

import net.opengis.indoorgml.geometry.LineString;
import edu.pnu.visitor.IndoorGMLElement;
import edu.pnu.visitor.IndoorGMLElementVisitor;

public class Transition extends AbstractFeature implements Serializable,
		IndoorGMLElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = -59775726589097799L;
	private static int labelNumber = 1;
	public final static int MAX_STATE_NUM = 2;
	public final static int INIT_WEIGHT = 1;

	private double weight = INIT_WEIGHT;
	private State connects[];
	private CellSpaceBoundary duality;
	private LineString path;

	public Transition() {
        super.setGmlID("T" + (labelNumber++));
        connects = new State[2];
    }
	
	public Transition(Transition other) {
		super(other);
		weight = other.weight;
		path = other.path.clone();
		
		// duality
		// connects;
	}
	
	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public CellSpaceBoundary getDuality() {
		return duality;
	}

	public void setDuality(CellSpaceBoundary duality) {
		if (duality == null)
			this.duality = null;
		else
			// this.duality = duality.replace("#", "");
			this.duality = duality;
	}

	public LineString getPath() {
		return path;
	}

	public void setPath(LineString path) {
		this.path = path;
	}

	public State[] getStates() {
		return connects;
	}

	public void setStates(State states[]) {
		connects = new State[MAX_STATE_NUM];
		// connects[0] = states[0].replace("#", "");
		// connects[1] = states[1].replace("#", "");
		connects[0] = states[0];
		connects[1] = states[1];
	}

	public static int getLabelNumber() {
		return labelNumber;
	}

	public static void setLabelNumber(int labelNumber) {
		Transition.labelNumber = labelNumber;
	}

	@Override
	public void accept(IndoorGMLElementVisitor visitor) {
		// TODO Auto-generated method stub
		visitor.visit(this);
	}
	
}
