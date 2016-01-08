package edu.pnu.project;

import java.io.Serializable;
import java.util.ArrayList;

import net.opengis.indoorgml.core.Transition;

public class TransitionOnFloor extends Floor implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3286564544452553978L;
	private ArrayList<Transition> transitionMember;

	public TransitionOnFloor() {
		// TODO Auto-generated constructor stub
		transitionMember = new ArrayList<Transition>();
	}

	public ArrayList<Transition> getTransitionMember() {
		return transitionMember;
	}

	public void setTransitionMember(ArrayList<Transition> transitionMember) {
		this.transitionMember = transitionMember;
	}

}
