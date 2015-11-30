package net.opengis.indoorgml.core;

import java.io.Serializable;
import java.util.ArrayList;

import edu.pnu.visitor.IndoorGMLElement;
import edu.pnu.visitor.IndoorGMLElementVisitor;

public class SpaceLayers extends AbstractFeature implements Serializable, IndoorGMLElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4063752783470065682L;
	private static int labelNumber = 1;
	private ArrayList<SpaceLayer> spaceLayerMember;
	
	public SpaceLayers() {
		super.setGmlID( "SL" + (labelNumber++) );
		spaceLayerMember = new ArrayList<SpaceLayer>();
		
		spaceLayerMember.add(new SpaceLayer());
	}

	public ArrayList<SpaceLayer> getSpaceLayerMember() {
		return spaceLayerMember;
	}
	public void setSpaceLayerMember(ArrayList<SpaceLayer> spaceLayerMember) {
		this.spaceLayerMember = spaceLayerMember;
	}

	public static int getLabelNumber() {
		return labelNumber;
	}

	public static void setLabelNumber(int labelNumber) {
		SpaceLayers.labelNumber = labelNumber;
	}

	@Override
	public void accept(IndoorGMLElementVisitor visitor) {
		// TODO Auto-generated method stub
		visitor.visit(this);
	}
}