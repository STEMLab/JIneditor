package edu.pnu.visitor;

import net.opengis.indoorgml.core.CellSpace;
import net.opengis.indoorgml.core.CellSpaceBoundary;
import net.opengis.indoorgml.core.CellSpaceBoundaryOnFloor;
import net.opengis.indoorgml.core.CellSpaceOnFloor;
import net.opengis.indoorgml.core.Edges;
import net.opengis.indoorgml.core.IndoorFeatures;
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
import net.opengis.indoorgml.geometry.LinearRing;
import net.opengis.indoorgml.geometry.Point;
import net.opengis.indoorgml.geometry.Polygon;
import net.opengis.indoorgml.geometry.Shell;
import net.opengis.indoorgml.geometry.Solid;
import edu.pnu.project.StateOnFloor;
import edu.pnu.project.TransitionOnFloor;


public interface IndoorGMLElementVisitor {

	void visit(IndoorFeatures indoorFeatures);
	
	void visit(PrimalSpaceFeatures primalSpaceFeatures);
	void visit(CellSpaceOnFloor cellSpaceOnFloor);
	void visit(CellSpace cellSpace);
	void visit(CellSpaceBoundaryOnFloor cellSpaceBoundaryOnFloor);
	void visit(CellSpaceBoundary cellSpaceBoundary);
	
	void visit(MultiLayeredGraph multiLayeredGraph);
	void visit(SpaceLayers spaceLayers);
	void visit(SpaceLayer spaceLayer);
	void visit(Nodes nodes);
	void visit(StateOnFloor stateOnFloor);
	void visit(State state);
	void visit(Point point);
	void visit(Edges edges);
	void visit(TransitionOnFloor transitionOnFloor);
	void visit(Transition transition);
	void visit(LineString lineString);
	
	void visit(InterEdges interEdges);
	void visit(InterLayerConnection interLayerConnection);
	
	void visit(LinearRing linearRing);
	void visit(Polygon polygon);
	
	void visit(Shell shell);
	void visit(Solid solid);
	/*
	void visit(Boundary boundary);
	void visit(Path path);
	void visit(Route route);
	void visit(RouteNode routeNode);
	void visit(RouteNodes routeNodes);
	void visit(RouteSegment routeSegment);
	void visit(Space space);
	*/
}
