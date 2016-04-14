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
import net.opengis.indoorgml.geometry.LineString;
import net.opengis.indoorgml.geometry.Point;
import net.opengis.indoorgml.geometry.Polygon;
import net.opengis.indoorgml.geometry.Solid;

public class IDRegistry implements Serializable {
    /** serialVersionUID */
    private static final long serialVersionUID = -482775087720705728L;

    private int primalSpaceFeaturesID;
    private int cellSpaceID;
    private int cellSpaceBoundaryID;
    private int multiLayeredGraphID;
    private int spaceLayersID;
    private int spaceLayerID;
    private int nodesID;
    private int stateID;
    private int edgesID;
    private int transitionID;
    private int interEdgesID;
    private int interLayerConnectionID;
    
    private int pointID;
    private int lineStringID;
    private int polygonID;
    private int solidID;

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
            
            pointID = Point.getLabelNumber();
            lineStringID = LineString.getLabelNumber();
            polygonID = Polygon.getLabelNumber();
            solidID = Solid.getLabelNumber();
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
            
            Point.setLabelNumber(pointID);
            LineString.setLabelNumber(lineStringID);
            Polygon.setLabelNumber(polygonID);
            Solid.setLabelNumber(solidID);
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

	public int getPointID() {
		return pointID;
	}

	public void setPointID(int pointID) {
		this.pointID = pointID;
	}

	public int getLineStringID() {
		return lineStringID;
	}

	public void setLineStringID(int lineStringID) {
		this.lineStringID = lineStringID;
	}

	public int getPolygonID() {
		return polygonID;
	}

	public void setPolygonID(int polygonID) {
		this.polygonID = polygonID;
	}

	public int getSolidID() {
		return solidID;
	}

	public void setSolidID(int solidID) {
		this.solidID = solidID;
	}

}
