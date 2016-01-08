package net.opengis.indoorgml.geometry;

import java.io.Serializable;
import java.util.ArrayList;

import edu.pnu.visitor.IndoorGMLElementVisitor;

public class Solid extends AbstractGeometry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8858099397667211666L;
	private static int labelNumber = 1;
	private Shell exteriorShell;
	private ArrayList<Shell> interiorShell;
	
	public Solid() {
		// TODO Auto-generated constructor stub
		super.setGMLID( "SOLID" + (labelNumber++) );
		interiorShell = new ArrayList<Shell>();
	}

	public static int getLabelNumber() {
		return labelNumber;
	}

	public static void setLabelNumber(int labelNumber) {
		Solid.labelNumber = labelNumber;
	}

	public Shell getExteriorShell() {
		return exteriorShell;
	}

	public void setExteriorShell(Shell exteriorShell) {
		this.exteriorShell = exteriorShell;
	}

	public ArrayList<Shell> getInteriorShell() {
		return interiorShell;
	}

	public void setInteriorShell(ArrayList<Shell> interiorShell) {
		this.interiorShell = interiorShell;
	}

	@Override
	public void accept(IndoorGMLElementVisitor visitor) {
		// TODO Auto-generated method stub
		
	}

}
