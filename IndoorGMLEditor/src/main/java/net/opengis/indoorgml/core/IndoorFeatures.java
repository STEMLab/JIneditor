package net.opengis.indoorgml.core;

import java.io.Serializable;

import edu.pnu.visitor.IndoorGMLElement;
import edu.pnu.visitor.IndoorGMLElementVisitor;

public class IndoorFeatures extends AbstractFeature implements Serializable, IndoorGMLElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7930354876372409593L;
	private PrimalSpaceFeatures primalSpaceFeatures;
	private MultiLayeredGraph multiLayeredGraph;
	
	public IndoorFeatures() {
	}

	public PrimalSpaceFeatures getPrimalSpaceFeatures() {
		if(primalSpaceFeatures == null) {
			primalSpaceFeatures = new PrimalSpaceFeatures();
		}
		return primalSpaceFeatures;
	}

	public void setPrimalSpaceFeatures(PrimalSpaceFeatures primalSpaceFeatures) {
		this.primalSpaceFeatures = primalSpaceFeatures;
	}

	public MultiLayeredGraph getMultiLayeredGraph() {
		if(multiLayeredGraph == null) {
			multiLayeredGraph = new MultiLayeredGraph();
		}
		return multiLayeredGraph;
	}

	public void setMultiLayeredGraph(MultiLayeredGraph multiLayeredGraph) {
		this.multiLayeredGraph = multiLayeredGraph;
	}

	@Override
	public void accept(IndoorGMLElementVisitor visitor) {
		// TODO Auto-generated method stub
		visitor.visit(this);
	}

}
