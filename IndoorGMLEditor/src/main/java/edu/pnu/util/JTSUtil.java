package edu.pnu.util;

import java.util.ArrayList;

import org.geotools.geometry.jts.JTSFactoryFinder;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;

public class JTSUtil {
	private static GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
	
	public static Point snapPointToLineString(LineString line, Point point) {
		Envelope envelope = line.getEnvelopeInternal();
		double minX = envelope.getMinX();
		double minY = envelope.getMinY();
		
		double minDistance = 999;
		Point snapPoint = null;
		
		for(int i = 0; i < 1000; i++) {
			Coordinate coord = new Coordinate(minX + envelope.getWidth() * ((double) i) / 1000, minY + envelope.getHeight() * ((double)i) / 1000);
			Point p = geometryFactory.createPoint(coord);
			
			if(point.distance(p) < minDistance) {
				snapPoint = p;
				minDistance = point.distance(p);
			}
		}
		
		return line.getCentroid();
		//return snapPoint;
	}
	public static Point convertJTSPoint(net.opengis.indoorgml.geometry.Point p) {
		Coordinate coord = new Coordinate(p.getPanelRatioX(), p.getPanelRatioY());
		
		Point point = geometryFactory.createPoint(coord);
		
		return point;
	}
	
	public static LineString convertJTSLineString(net.opengis.indoorgml.geometry.LineString ls) {
		ArrayList<Coordinate> coords = new ArrayList<Coordinate>();
		for(net.opengis.indoorgml.geometry.Point p : ls.getPoints()) {
			double x = p.getPanelRatioX();
			double y = p.getPanelRatioY();
			
			coords.add(new Coordinate(x, y));
		}
		Coordinate[] coordsArr = new Coordinate[coords.size()];
		coords.toArray(coordsArr);
		LineString lineString = geometryFactory.createLineString(coordsArr);
				
		return lineString;
	}
	
	public static net.opengis.indoorgml.geometry.Point convertPoint(Point point) {
		net.opengis.indoorgml.geometry.Point newPoint = new net.opengis.indoorgml.geometry.Point();
		newPoint.setPanelRatioX(point.getX());
		newPoint.setPanelRatioY(point.getY());
		
		return newPoint;
	}
	
	public static net.opengis.indoorgml.geometry.LineString convertLineString(LineString line) {
		net.opengis.indoorgml.geometry.LineString newLS = new net.opengis.indoorgml.geometry.LineString();
		ArrayList<net.opengis.indoorgml.geometry.Point> points = newLS.getPoints();
		for(int j = 0; j < line.getNumPoints(); j++) {
			Point p = line.getPointN(j);
			net.opengis.indoorgml.geometry.Point newPoint = convertPoint(p);
			points.add(newPoint);
		}
		
		return newLS;
	}
	
	public static net.opengis.indoorgml.geometry.LineString reverseLineString(net.opengis.indoorgml.geometry.LineString ls) {
		for(int i = 0; i < ls.getPoints().size() / 2; i++) {
			net.opengis.indoorgml.geometry.Point p1 = ls.getPoints().get(i);
			net.opengis.indoorgml.geometry.Point p2 = ls.getPoints().get(ls.getPoints().size() - 1 - i);
			
			ls.getPoints().set(i, p2);
			ls.getPoints().set(ls.getPoints().size() - 1 - i, p1);
		}
		
		return ls;
	}

	public static ArrayList<net.opengis.indoorgml.geometry.LineString> splitLineString(net.opengis.indoorgml.geometry.LineString ls1, net.opengis.indoorgml.geometry.LineString ls2) {
		ls2 = ls2.clone();
		ArrayList<net.opengis.indoorgml.geometry.LineString> splited = new ArrayList<net.opengis.indoorgml.geometry.LineString>();
		LineString line1 = convertJTSLineString(ls1);
		LineString line2 = convertJTSLineString(ls2);
		
		Geometry difference = line1.difference(line2);
		if(difference.getGeometryType().equalsIgnoreCase("MultiLineString")) {
			for(int i = 0; i < difference.getNumGeometries(); i++) {
				LineString line = (LineString) difference.getGeometryN(i);
				net.opengis.indoorgml.geometry.LineString newLS = convertLineString(line);

				splited.add(newLS);
			}
			if(((LineString) difference.getGeometryN(0)).getEndPoint().equals(line2.getEndPoint())) { // 역방향일경우 뒤집어준다.
				ls2 = reverseLineString(ls2);
			}
			splited.add(1, ls2);
		} else if(difference.getGeometryType().equalsIgnoreCase("LineString")) {
			LineString differenceLS = (LineString) difference;
			if(differenceLS.isEmpty()) {
				splited.add(ls1);
			} else {
				net.opengis.indoorgml.geometry.LineString newLS = convertLineString(differenceLS);
				if(differenceLS.getEndPoint().equals(line2.getStartPoint())) {
					splited.add(newLS);
					splited.add(ls2);
				} else if(differenceLS.getEndPoint().equals(line2.getEndPoint())) {
					ls2 = reverseLineString(ls2);
					splited.add(newLS);
					splited.add(ls2);
				} else if(differenceLS.getStartPoint().equals(line2.getEndPoint())) {
					splited.add(ls2);
					splited.add(newLS);
				} else if(differenceLS.getStartPoint().equals(line2.getStartPoint())) {
					ls2 = reverseLineString(ls2);
					splited.add(ls2);
					splited.add(newLS);
				}
			}
		}
		
		return splited;
	}
}