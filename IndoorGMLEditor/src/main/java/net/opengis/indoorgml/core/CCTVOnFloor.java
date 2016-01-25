package net.opengis.indoorgml.core;

import java.io.Serializable;
import java.util.ArrayList;

import edu.pnu.project.Floor;

public class CCTVOnFloor extends Floor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1279712858691175504L;

	private ArrayList<CCTV> CCTVMember;
	
	public CCTVOnFloor() {
		CCTVMember = new ArrayList<CCTV>();
	}

	public ArrayList<CCTV> getCCTVMember() {
		return CCTVMember;
	}

	public void setCCTVMember(ArrayList<CCTV> cCTVMember) {
		CCTVMember = cCTVMember;
	}

}
