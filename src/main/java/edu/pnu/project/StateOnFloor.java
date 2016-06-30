package edu.pnu.project;

import java.io.Serializable;
import java.util.ArrayList;

import net.opengis.indoorgml.core.State;

public class StateOnFloor extends Floor implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6908880729660529527L;
	private ArrayList<State> stateMember;
	
	public StateOnFloor() {
		// TODO Auto-generated constructor stub
		stateMember = new ArrayList<State>();
	}

	public ArrayList<State> getStateMember() {
		return stateMember;
	}

	public void setStateMember(ArrayList<State> stateMember) {
		this.stateMember = stateMember;
	}
}
