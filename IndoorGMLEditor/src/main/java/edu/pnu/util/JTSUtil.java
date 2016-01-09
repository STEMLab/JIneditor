package edu.pnu.util;

import java.util.ArrayList;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;

public class JTSUtil {
        private static final PrecisionModel pm = new PrecisionModel(PrecisionModel.FLOATING);
	private static final GeometryFactory gf = new GeometryFactory(pm); 
	
	public static Point snapPointToLineString(LineString line, Point point) {
	        Point startPoint = line.getStartPoint();
	        Point endPoint = line.getEndPoint();
		double startX = startPoint.getX();
		double startY = startPoint.getY();
		double endX = endPoint.getX();
                double endY = endPoint.getY();
                double offsetX = endX - startX;
                double offsetY = endY - startY;
		
		double minDistance = 15;
		Point snapPoint = null;
		
		for(int i = 0; i < 5000; i++) {
		        double dx = startX + offsetX * ((double) i) / 5000;
		        double dy = startY + offsetY * ((double) i) / 5000;
			//Coordinate coord = new Coordinate(minX + envelope.getWidth() * ((double) i) / 1000, minY + envelope.getHeight() * ((double)i) / 1000);
		        Coordinate coord = new Coordinate(dx, dy);
                        Point p = gf.createPoint(coord);
                        
                        if(point.distance(p) < minDistance) {
                            System.out.println("distance changed");
                        }
                        if(line.contains(p) && point.distance(p) < minDistance) {
                                snapPoint = p;
                                minDistance = point.distance(p);
                                System.out.println("distance changed when line contains point");
                        }
		}
		
		if(snapPoint != null && line.contains(snapPoint)) {
		    System.out.println("snappoint is contained");
		}
		return snapPoint;
	}
	public static Point convertJTSPoint(net.opengis.indoorgml.geometry.Point p) {
		Coordinate coord = new Coordinate(p.getPanelRatioX(), p.getPanelRatioY());
		
		Point point = gf.createPoint(coord);
		
		return point;
	}
	
	public static LineString convertJTSLineString(net.opengis.indoorgml.geometry.LineString ls) {
		ArrayList<Coordinate> coords = new ArrayList<Coordinate>();
		for(net.opengis.indoorgml.geometry.Point p : ls.getPoints()) {
			double x = p.getPanelX();
			double y = p.getPanelY();
			
			coords.add(new Coordinate(x, y));
		}
		Coordinate[] coordsArr = new Coordinate[coords.size()];
		coords.toArray(coordsArr);
		LineString lineString = gf.createLineString(coordsArr);
				
		return lineString;
	}
	
	public static LinearRing convertJTSLinearRing(net.opengis.indoorgml.geometry.LinearRing ring) {
	        LineString lineString = convertJTSLineString(ring);
	        Coordinate[] coords = lineString.getCoordinates();
	        LinearRing jtsRing = gf.createLinearRing(coords);
	        
	        return jtsRing;
	}
	
	public static Polygon convertJTSPolygon(net.opengis.indoorgml.geometry.Polygon poly) {
	        LinearRing exterior = convertJTSLinearRing(poly.getExteriorRing());
	        LinearRing[] interior = null; // Polygon have no interiorRings in this version.
	        Polygon polygon = gf.createPolygon(exterior, interior);
	        
	        return polygon;
	}
	
	public static net.opengis.indoorgml.geometry.Point convertPoint(Point point) {
		net.opengis.indoorgml.geometry.Point newPoint = new net.opengis.indoorgml.geometry.Point();
		newPoint.setPanelX(point.getX());
		newPoint.setPanelY(point.getY());
		
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