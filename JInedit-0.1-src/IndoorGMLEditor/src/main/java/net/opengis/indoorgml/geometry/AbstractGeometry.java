package net.opengis.indoorgml.geometry;

import java.io.Serializable;

import edu.pnu.visitor.IndoorGMLElement;

public abstract class AbstractGeometry implements Serializable, IndoorGMLElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = -642109571759557543L;
	private String gmlID;
	private AbstractGeometry xLinkGeometry;

	public String getGMLID() {
		return gmlID;
	}

	public void setGMLID(String gmlID) {
		this.gmlID = gmlID;
	}

	public AbstractGeometry getxLinkGeometry() {
		return xLinkGeometry;
	}

	public void setxLinkGeometry(AbstractGeometry xLinkGeometry) {
		this.xLinkGeometry = xLinkGeometry;
	}
}
