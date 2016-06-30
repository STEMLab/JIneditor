package edu.pnu.project;

import java.io.Serializable;

import net.opengis.indoorgml.core.CellSpace;
import net.opengis.indoorgml.core.CellSpaceBoundary;
import net.opengis.indoorgml.core.Edges;
import net.opengis.indoorgml.core.InterEdges;
import net.opengis.indoorgml.core.InterLayerConnection;
import net.opengis.indoorgml.core.MultiLayeredGraph;
import net.opengis.indoorgml.core.Nodes;
import net.opengis.indoorgml.core.PrimalSpaceFeatures;
import net.opengis.indoorgml.core.SpaceLayer;
import net.opengis.indoorgml.core.SpaceLayers;
import net.opengis.indoorgml.core.State;
import net.opengis.indoorgml.core.Transition;

/* Previous version */
public class IndoorGMLIDRegistry implements Serializable {
    /** serialVersionUID */
    private static final long serialVersionUID = -482775087720705728L;
    
    private int cellSpaceID;
    private int cellSpaceBoundaryID;
    private int edgesID;
    private int interEdgesID;
    private int interLayerConnectionID;
    private int multiLayeredGraphID;
    private int nodesID;
    private int primalSpaceFeaturesID;
    private int spaceLayerID;
    private int spaceLayersID;
    private int stateID;
    private int transitionID;

    public void saveIndoorGMLID() {
            cellSpaceID = CellSpace.getLabelNumber();
            cellSpaceBoundaryID = CellSpaceBoundary.getLabelNumber();
            edgesID = Edges.getLabelNumber();
            interEdgesID = InterEdges.getLabelNumber();
            interLayerConnectionID = InterLayerConnection.getLabelNumber();
            multiLayeredGraphID = MultiLayeredGraph.getLabelNumber();
            nodesID = Nodes.getLabelNumber();
            primalSpaceFeaturesID = PrimalSpaceFeatures.getLabelNumber();
            spaceLayerID = SpaceLayer.getLabelNumber();
            spaceLayersID = SpaceLayers.getLabelNumber();
            stateID = State.getLabelNumber();
            transitionID = Transition.getLabelNumber();
    }
    
    public void loadIndoorGMLID() {
            CellSpace.setLabelNumber(cellSpaceID);
            CellSpaceBoundary.setLabelNumber(cellSpaceBoundaryID);
            Edges.setLabelNumber(edgesID);
            InterEdges.setLabelNumber(interEdgesID);
            InterLayerConnection.setLabelNumber(interLayerConnectionID);
            MultiLayeredGraph.setLabelNumber(multiLayeredGraphID);
            Nodes.setLabelNumber(nodesID);
            PrimalSpaceFeatures.setLabelNumber(primalSpaceFeaturesID);
            SpaceLayer.setLabelNumber(spaceLayerID);
            SpaceLayers.setLabelNumber(spaceLayersID);
            State.setLabelNumber(stateID);
            Transition.setLabelNumber(transitionID);
    }

	public int getCellSpaceID() {
		return cellSpaceID;
	}

	public void setCellSpaceID(int cellSpaceID) {
		this.cellSpaceID = cellSpaceID;
	}

	public int getCellSpaceBoundaryID() {
		return cellSpaceBoundaryID;
	}

	public void setCellSpaceBoundaryID(int cellSpaceBoundaryID) {
		this.cellSpaceBoundaryID = cellSpaceBoundaryID;
	}

	public int getEdgesID() {
		return edgesID;
	}

	public void setEdgesID(int edgesID) {
		this.edgesID = edgesID;
	}

	public int getInterEdgesID() {
		return interEdgesID;
	}

	public void setInterEdgesID(int interEdgesID) {
		this.interEdgesID = interEdgesID;
	}

	public int getInterLayerConnectionID() {
		return interLayerConnectionID;
	}

	public void setInterLayerConnectionID(int interLayerConnectionID) {
		this.interLayerConnectionID = interLayerConnectionID;
	}

	public int getMultiLayeredGraphID() {
		return multiLayeredGraphID;
	}

	public void setMultiLayeredGraphID(int multiLayeredGraphID) {
		this.multiLayeredGraphID = multiLayeredGraphID;
	}

	public int getNodesID() {
		return nodesID;
	}

	public void setNodesID(int nodesID) {
		this.nodesID = nodesID;
	}

	public int getPrimalSpaceFeaturesID() {
		return primalSpaceFeaturesID;
	}

	public void setPrimalSpaceFeaturesID(int primalSpaceFeaturesID) {
		this.primalSpaceFeaturesID = primalSpaceFeaturesID;
	}

	public int getSpaceLayerID() {
		return spaceLayerID;
	}

	public void setSpaceLayerID(int spaceLayerID) {
		this.spaceLayerID = spaceLayerID;
	}

	public int getSpaceLayersID() {
		return spaceLayersID;
	}

	public void setSpaceLayersID(int spaceLayersID) {
		this.spaceLayersID = spaceLayersID;
	}

	public int getStateID() {
		return stateID;
	}

	public void setStateID(int stateID) {
		this.stateID = stateID;
	}

	public int getTransitionID() {
		return transitionID;
	}

	public void setTransitionID(int transitionID) {
		this.transitionID = transitionID;
	}

}
