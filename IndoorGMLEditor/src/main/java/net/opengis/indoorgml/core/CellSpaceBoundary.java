package net.opengis.indoorgml.core;

import java.io.Serializable;

import net.opengis.indoorgml.geometry.LineString;
import net.opengis.indoorgml.geometry.Polygon;
import edu.pnu.project.BoundaryType;
import edu.pnu.visitor.IndoorGMLElement;
import edu.pnu.visitor.IndoorGMLElementVisitor;

public class CellSpaceBoundary extends AbstractFeature implements Serializable, IndoorGMLElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5440851342654177829L;
	private static int labelNumber = 1;
	private Transition duality;
	private Polygon geometry3D;
	private LineString geometry2D;
	private String externalReference;
	
	// for door
	private BoundaryType boundaryType;
	// for 3D
	private double ceilingHeight;
	
	public CellSpaceBoundary() {
		super.setGmlID( "CB" + (labelNumber++) );
		// TODO Auto-generated constructor stub
	}

	public static int getLabelNumber() {
		return labelNumber;
	}

	public static void setLabelNumber(int labelNumber) {
		CellSpaceBoundary.labelNumber = labelNumber;
	}

	public Transition getDuality() {
		return duality;
	}

	public void setDuality(Transition duality) {
		this.duality = duality;
	}

	public Polygon getGeometry3D() {
		return geometry3D;
	}

	public void setGeometry3D(Polygon geometry3D) {
		this.geometry3D = geometry3D;
	}

	public LineString getGeometry2D() {
		return geometry2D;
	}

	public void setGeometry2D(LineString geometry2D) {
		this.geometry2D = geometry2D;
	}

	public String getExternalReference() {
		return externalReference;
	}

	public void setExternalReference(String externalReference) {
		this.externalReference = externalReference;
	}

	public BoundaryType getBoundaryType() {
		return boundaryType;
	}

	public void setBoundaryType(BoundaryType boundaryType) {
		this.boundaryType = boundaryType;
	}

	public double getCeilingHeight() {
		return ceilingHeight;
	}

	public void setCeilingHeight(double ceilingHeight) {
		this.ceilingHeight = ceilingHeight;
	}

	@Override
	public void accept(IndoorGMLElementVisitor visitor) {
		// TODO Auto-generated method stub
	        visitor.visit(this);
	}

}
