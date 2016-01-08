package edu.pnu.util;

import java.util.ArrayList;

import net.opengis.indoorgml.geometry.LineString;
import net.opengis.indoorgml.geometry.Point;

public class GeometryUtil {
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
	
	// use JTS
	public static ArrayList<LineString> splitLineString(LineString base, LineString target) {
		ArrayList<LineString> splited = JTSUtil.splitLineString(base, target);

		return splited;
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
}
