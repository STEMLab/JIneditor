package edu.pnu.util;

import java.util.ArrayList;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.linearref.LinearLocation;
import com.vividsolutions.jts.linearref.LocationIndexedLine;

public class JTSUtil {
    private static final PrecisionModel pm = new PrecisionModel(PrecisionModel.FIXED);
	private static final GeometryFactory gf = new GeometryFactory(pm); 
	
	public static Point snapPointToLineStringByLIL(LineString line, Point point) {
		//new PrecisionModel(
		System.out.println("scale " + pm.getScale());
		LocationIndexedLine lil = new LocationIndexedLine(line);
		LinearLocation here = lil.project(point.getCoordinate());
		Coordinate coord = lil.extractPoint(here);
		Point p = gf.createPoint(coord);
		
		System.out.println("LIL isContains : " + line.contains(p));
		System.out.println("point : " + point.toText());
		System.out.println("extracted poitn : " + p.toText());
		
		return p;
	}
	
	public static Point snapPointToLineStringByEquation(LineString line, Point point) {
		Point startPoint = line.getStartPoint();
        Point endPoint = line.getEndPoint();
		double x1 = startPoint.getX();
		double y1 = startPoint.getY();
		double x2 = endPoint.getX();
        double y2 = endPoint.getY();
        double p1 = point.getX();
        double q1 = point.getY();
        double p2;
        double q2;
        
        if (y1 == y2) {
        	p2 = p1;
        	q2 = y1;
        } else if (x1 == x2) {
        	p2 = x1;
        	q2 = q1;
        } else {
	        double m1 = (y2 - y1) / (x2 - x1);
	        double n1 = y1 - (m1 * x1);
	        double m2 = -1 * 1 / m1;
	        double n2 = q1 - (m2 * p1);
	        
	        p2 = (n2 - n1) / (m1 - m2);
	        q2 = m1 * p2 + n1;
        }
        
        Coordinate coord = new Coordinate(p2, q2);
        Point p = gf.createPoint(coord);
        
        System.out.println("isContains : " + line.contains(p));
        
        return p;
	}
	
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
		int devider = 10000;
		for(int i = 0; i < devider; i++) {
		        double dx = startX + offsetX * ((double) i) / devider;
		        double dy = startY + offsetY * ((double) i) / devider;
				//Coordinate coord = new Coordinate(minX + envelope.getWidth() * ((double) i) / 1000, minY + envelope.getHeight() * ((double)i) / 1000);
		        Coordinate coord = new Coordinate(dx, dy);
                Point p = gf.createPoint(coord);
                
                if(line.contains(p) && point.distance(p) < minDistance) {
                //if (line.contains(p)) {
                        snapPoint = p;
                        minDistance = point.distance(p);
                        //System.out.println("distance changed : line contains point");
                }
		}
		
		if(snapPoint != null && line.contains(snapPoint)) {
		        System.out.println("JTSUtil : snappoint is found");
		}
		return snapPoint;
	}
	public static Point convertJTSPoint(net.opengis.indoorgml.geometry.Point p) {
		Coordinate coord = new Coordinate(p.getPanelX(), p.getPanelY());
		
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
	
	public static net.opengis.indoorgml.geometry.LinearRing convertLinearRing(LinearRing ring) {
	    net.opengis.indoorgml.geometry.LineString lineString = convertLineString(ring);
	    net.opengis.indoorgml.geometry.LinearRing linearRing = new net.opengis.indoorgml.geometry.LinearRing();
	    
	    linearRing.setPoints(lineString.getPoints());
	    
	    return linearRing;
	}
	
	public static net.opengis.indoorgml.geometry.Polygon convertPolygon(Polygon polygon) {
	        
	        LinearRing exteriorRing = (LinearRing) polygon.getExteriorRing();
	        net.opengis.indoorgml.geometry.LinearRing exterior = convertLinearRing(exteriorRing);
	        net.opengis.indoorgml.geometry.Polygon poly = new net.opengis.indoorgml.geometry.Polygon();
	        poly.setExteriorRing(exterior);
	        
	        return poly;
	}
	
	public static int isSimilarOrientation(LineString line1, LineString line2) {
		Point p1 = line1.getStartPoint();
		Point q1 = line1.getEndPoint();
		Point p2 = line2.getStartPoint();
		Point q2 = line2.getEndPoint();
		
		int dx1;
		int dy1;
		int dx2;
		int dy2;
		
		double epsilon = 0.005;
		
		double value = p1.getX() - q1.getX(); 
		if (Math.abs(value) < epsilon) {
			dx1 = 0;
		} else if (value > 0) {
			dx1 = 1;
		} else {
			dx1 = -1;
		}
		
		value = p1.getY() - q1.getY();
		if (Math.abs(value) < epsilon) {
			dy1 = 0;
		} else if (value > 0) {
			dy1 = 1;
		} else {
			dy1 = -1;
		}
		
		value = p2.getX() - q2.getX(); 
		if (Math.abs(value) < epsilon) {
			dx2 = 0;
		} else if (value > 0) {
			dx2 = 1;
		} else {
			dx2 = -1;
		}
		
		value = p2.getY() - q2.getY();
		if (Math.abs(value) < epsilon) {
			dy2 = 0;
		} else if (value > 0) {
			dy2 = 1;
		} else {
			dy2 = -1;
		}
		
		int result = 0; // not similar
		if (dx1 == dx2 && dy1 == dy2) {
			result = 1; // similar
		} else if ((dx1 == dx2 && dy1 == -dy2) ||
				(dx1 == -dx2 && dy1 == dy2)) {
			result = -1; // similar, but orientation is reversed.
		}
		
		return result;
	}

	public static ArrayList<net.opengis.indoorgml.geometry.LineString> splitLineString(net.opengis.indoorgml.geometry.LineString ls1, net.opengis.indoorgml.geometry.LineString ls2) {
		ls2 = ls2.clone();
		ArrayList<net.opengis.indoorgml.geometry.LineString> splited = new ArrayList<net.opengis.indoorgml.geometry.LineString>();
		LineString line1 = convertJTSLineString(ls1);
		LineString line2 = convertJTSLineString(ls2);
		
		if (isSimilarOrientation(line1, line2) == -1) {
			line2 = (LineString) line2.reverse();
		} else if (isSimilarOrientation(line1, line2) == 0) {
			System.out.println("not similar orientation between lines");
			return null;
		}
		ArrayList<Point> points = new ArrayList<Point>();
		points.add(line1.getStartPoint());
		points.add(line2.getStartPoint());
		if (line1.getStartPoint().equals(line2.getStartPoint())) {
			points.remove(1);
		}
		points.add(line2.getEndPoint());
		points.add(line1.getEndPoint());
		if (line2.getEndPoint().equals(line1.getEndPoint())) {
			points.remove(3);
		}
		
		for (int i = 0; i < points.size() - 1; i++) {
			Coordinate coord1 = points.get(i).getCoordinate();
			Coordinate coord2 = points.get(i + 1).getCoordinate();
			
			LineString line = gf.createLineString(new Coordinate[]{coord1, coord2});
			net.opengis.indoorgml.geometry.LineString newLS = convertLineString(line);

			splited.add(newLS);
		}
		
		return splited;
		
		/*
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
		*/
	}
	
	public static double IsLeft(Coordinate P0, Coordinate P1, Coordinate P2)
        {
            return ((P1.X - P0.X) * (P2.Y - P0.Y)
                    - (P2.X - P0.X) * (P1.Y - P0.Y));
        }

        //===================================================================

        // orientation2D_Polygon(): tests the orientation of a simple polygon
        //    Input:  int n = the number of vertices in the polygon
        //            Point* V = an array of n+1 vertices with V[n]=V[0]
        //    Return: >0 for counterclockwise 
        //            =0 for none (degenerate)
        //            <0 for clockwise
        //    Note: this algorithm is faster than computing the signed area.
        public static double Orientation2D_Polygon(int n, CoordinateSequence V)
        {
            // first find rightmost lowest vertex of the polygon
            int rmin = 0;
            double xmin = V.getOrdinate(0, 0);
            double ymin = V.getOrdinate(0, 1);

            for (int i = 1; i < n; i++)
            {
                if (V.getOrdinate(i, 1) > ymin)
                    continue;
                if (V.getOrdinate(i, 1) == ymin)
                {    // just as low
                    if (V.getOrdinate(i, 0) < xmin)   // and to left
                        continue;
                }
                rmin = i;          // a new rightmost lowest vertex
                xmin = V.getOrdinate(i, 0);
                ymin = V.getOrdinate(i, 1);
            }

            // test orientation at this rmin vertex
            // ccw <=> the edge leaving is left of the entering edge
            if (rmin == 0)
                return IsLeft(V.getCoordinate(n - 1), V.getCoordinate(0), V.getCoordinate(1));
            else
                return IsLeft(V.getCoordinate(rmin - 1), V.getCoordinate(rmin), V.getCoordinate((rmin + 1) % n));
        }
}