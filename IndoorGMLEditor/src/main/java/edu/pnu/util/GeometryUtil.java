package edu.pnu.util;

import java.util.ArrayList;

import net.opengis.indoorgml.geometry.LineString;
import net.opengis.indoorgml.geometry.Point;
import net.opengis.indoorgml.geometry.Polygon;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;

public class GeometryUtil {
        private static final PrecisionModel pm = new PrecisionModel(PrecisionModel.FLOATING);
        public static final GeometryFactory jtsFactory = new GeometryFactory(pm); 
	// use JTS
	public static boolean isContainsLineString(LineString ls1, LineString ls2) {
		com.vividsolutions.jts.geom.LineString line1 = JTSUtil.convertJTSLineString(ls1);
		com.vividsolutions.jts.geom.LineString line2 = JTSUtil.convertJTSLineString(ls2);
		
		return line1.contains(line2);
	}
	
	public static boolean isWithinLineString(LineString ls1, LineString ls2) {		
		return isContainsLineString(ls2, ls1);
	}
	
	public static boolean isOverlapsLineString(LineString ls1, LineString ls2) {
		com.vividsolutions.jts.geom.LineString line1 = JTSUtil.convertJTSLineString(ls1);
		com.vividsolutions.jts.geom.LineString line2 = JTSUtil.convertJTSLineString(ls2);
		
		return line1.overlaps(line2);
	}
	
	public static boolean isCoversLineString(LineString ls1, LineString ls2) {
		com.vividsolutions.jts.geom.LineString line1 = JTSUtil.convertJTSLineString(ls1);
		com.vividsolutions.jts.geom.LineString line2 = JTSUtil.convertJTSLineString(ls2);
		
		return line1.covers(line2);
	}
	
	public static boolean isCoveredByLineString(LineString ls1, LineString ls2) {
		return isCoversLineString(ls2, ls1);
	}
	
	public static boolean isEqualsLineString(LineString ls1, LineString ls2) {
		com.vividsolutions.jts.geom.LineString line1 = JTSUtil.convertJTSLineString(ls1);
		com.vividsolutions.jts.geom.LineString line2 = JTSUtil.convertJTSLineString(ls2);
		
		return line1.equals(line2);
	}
	
	public static boolean isEqualsIgnoreReverseLineString(LineString ls1, LineString ls2) {
		com.vividsolutions.jts.geom.LineString line1 = (com.vividsolutions.jts.geom.LineString) JTSUtil.convertJTSLineString(ls1).reverse();
		com.vividsolutions.jts.geom.LineString line2 = JTSUtil.convertJTSLineString(ls2);
		
		return line1.equals(line2);
	}
	
	public static boolean isTouchesLineString(LineString ls1, LineString ls2) {
		com.vividsolutions.jts.geom.LineString line1 = JTSUtil.convertJTSLineString(ls1);
		com.vividsolutions.jts.geom.LineString line2 = JTSUtil.convertJTSLineString(ls2);
		
		return line1.touches(line2);
	}
	
	public static boolean isIntersectsLineString(LineString ls1, LineString ls2) {
		com.vividsolutions.jts.geom.LineString line1 = JTSUtil.convertJTSLineString(ls1);
		com.vividsolutions.jts.geom.LineString line2 = JTSUtil.convertJTSLineString(ls2);
		
		return line1.intersects(line2);
	}
	
	public static boolean isDisjointLineString(LineString ls1, LineString ls2) {
		return !isIntersectsLineString(ls1, ls2);
	}
	
	public static LineString getIntersectionLineString(LineString ls1, LineString ls2) {
	        com.vividsolutions.jts.geom.LineString line1 = JTSUtil.convertJTSLineString(ls1);
	        com.vividsolutions.jts.geom.LineString line2 = JTSUtil.convertJTSLineString(ls2);
	        com.vividsolutions.jts.geom.LineString intersection = (com.vividsolutions.jts.geom.LineString) line1.intersection(line2);
	        
	        return JTSUtil.convertLineString(intersection);
	}
	
	// use JTS
	public static ArrayList<LineString> splitLineString(LineString base, LineString target) {
		ArrayList<LineString> splited = JTSUtil.splitLineString(base, target);

		return splited;
	}
	
	public static Point getSnapPointToLineString(double x1, double y1, double x2, double y2, double x, double y) {
	    Coordinate coord1 = new Coordinate(x1, y1);
	    Coordinate coord2 = new Coordinate(x2, y2);
	    Coordinate coord3 = new Coordinate(x, y);
            com.vividsolutions.jts.geom.Point p = jtsFactory.createPoint(coord3);
            
            com.vividsolutions.jts.geom.LineString ls = jtsFactory.createLineString(new Coordinate[]{coord1, coord2});
            
            com.vividsolutions.jts.geom.Point snapPoint = JTSUtil.snapPointToLineString(ls, p);
            Point snap = null;
            if(snapPoint != null) {
                snap = new Point();
                snap.setPanelX(snapPoint.getX());
                snap.setPanelY(snapPoint.getY());
            }
            return snap;
        }
	
	public static Point getSnapPointToLineString(LineString ls, double x, double y) {
		Point p = new Point();
		p.setPanelRatioX(x);
		p.setPanelRatioY(y);
		
		return getSnapPointToLineString(ls, p);
	}
	
	public static Point getSnapPointToLineString(LineString ls, Point p) {
		com.vividsolutions.jts.geom.LineString line = JTSUtil.convertJTSLineString(ls);
		com.vividsolutions.jts.geom.Point point = JTSUtil.convertJTSPoint(p);
		
		double snapBounds = 2;
		double distance = line.distance(point) * 100;
		if(distance > snapBounds) return null;
		
		com.vividsolutions.jts.geom.Point snapPoint = JTSUtil.snapPointToLineString(line, point);
		Point snapP = JTSUtil.convertPoint(snapPoint);
		
		return snapP;
	}
	
	public static double getDistancePointToLineString(LineString ls, Point p) {
            com.vividsolutions.jts.geom.LineString line = JTSUtil.convertJTSLineString(ls);
            com.vividsolutions.jts.geom.Point point = JTSUtil.convertJTSPoint(p);
            
            return line.distance(point);
	}
	
	public static double getDistancePointToLineString(LineString ls, double x, double y) {
            com.vividsolutions.jts.geom.LineString line = JTSUtil.convertJTSLineString(ls);
            Coordinate coord = new Coordinate(x, y);
            com.vividsolutions.jts.geom.Point point = jtsFactory.createPoint(coord);
            
            return line.distance(point);
        }
	
	public static double getDistancePointToLine(double linex1, double liney1, double linex2, double liney2, double x, double y) {
	    Coordinate coord1 = new Coordinate(linex1, liney1);
	    Coordinate coord2 = new Coordinate(linex2, liney2);
	    Coordinate coord3 = new Coordinate(x, y);
            com.vividsolutions.jts.geom.LineString line = jtsFactory.createLineString(new Coordinate[]{coord1, coord2});
            com.vividsolutions.jts.geom.Point point = jtsFactory.createPoint(coord3);
            
            return line.distance(point);
        }
	
	public static Point getCentroidPointOnPolygon(Polygon polygon) {
	    com.vividsolutions.jts.geom.Polygon jtsPolygon = JTSUtil.convertJTSPolygon(polygon);
	    com.vividsolutions.jts.geom.Point centroid = jtsPolygon.getCentroid();
	    
	    return JTSUtil.convertPoint(centroid);
	}
}
