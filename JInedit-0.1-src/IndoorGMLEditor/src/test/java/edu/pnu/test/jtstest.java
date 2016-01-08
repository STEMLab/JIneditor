package edu.pnu.test;

import org.geotools.geometry.jts.JTSFactoryFinder;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;



public class jtstest {

	public jtstest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		double scale = Math.pow(10, 11);
		PrecisionModel pm = new PrecisionModel(PrecisionModel.FLOATING_SINGLE);
		GeometryFactory geometryFactory = new GeometryFactory(pm);
		//GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

		Coordinate[] coords  =
		 new Coordinate[] {new Coordinate(1.55552341, 1.55345511), new Coordinate(4.65551567, 4.75556471)};
		LineString line = geometryFactory.createLineString(coords);
		
		Coordinate[] coords2  =
				 new Coordinate[] {new Coordinate(0.5, 0.5), new Coordinate(0.3, 0.3) };
		LineString line2 = geometryFactory.createLineString(coords2);
		
		Coordinate[] coords3 =
				 new Coordinate[] {new Coordinate(5, 2), new Coordinate(6, 2) };
		LineString line3 = geometryFactory.createLineString(coords3);
		
		Coordinate[] coords4  =
				 new Coordinate[] {new Coordinate(0, 2), new Coordinate(6, 2) };
		LineString line4 = geometryFactory.createLineString(coords4);
		
		Coordinate[] coords5  =
				 new Coordinate[] {new Coordinate(5, 0), new Coordinate(5, 4) };
		LineString line5 = geometryFactory.createLineString(coords5);
		
		Coordinate[] coords6  =
				 new Coordinate[] {new Coordinate(0.6, 0.6), new Coordinate(0.2, 0.2) };
		LineString line6 = geometryFactory.createLineString(coords5);
		
		Coordinate[] coords7  =
				 new Coordinate[] {new Coordinate(0.2, 0.2), new Coordinate(0.6, 0.6) };
				LineString line7 = geometryFactory.createLineString(coords);
				
		Coordinate coord = new Coordinate(0.4, 0.4);
		Point p = geometryFactory.createPoint(coord);
		
		System.out.println("-----------------------");
		System.out.println(line.intersects(line2));
		System.out.println(line.intersects(line3));
		System.out.println(line.intersects(line4));
		System.out.println(line.intersects(line5));
		
		System.out.println("-----------------------");
		System.out.println(line.overlaps(line2));
		System.out.println(line.overlaps(line3));
		System.out.println(line.overlaps(line4));
		System.out.println(line.overlaps(line5));
		
		System.out.println("-----------------------");
		System.out.println(line.contains(line));
		System.out.println(line.contains(line2));
		System.out.println(line.contains(line3));
		System.out.println(line.contains(line4));
		System.out.println(line.contains(line5));
		
		Geometry intersection = line.intersection(line2);
		LineString intersectionLS = (LineString) intersection;
		System.out.println(intersectionLS.toString());
		Geometry difference = line.difference(intersectionLS);
		System.out.println(difference.toString());
		/*
		intersection = line.intersection(line);
		intersectionLS = (LineString) intersection;
		System.out.println(intersectionLS.toString());
		difference = line.difference(intersectionLS);
		System.out.println(difference.toString());
		System.out.println(difference.isEmpty());
		
		System.out.println("-----------------------");
		System.out.println(line.equals(line6));
		System.out.println(line.equals(line7));
		System.out.println(line.equalsExact(line6));
		System.out.println(line.equalsExact(line7));
		*/
		System.out.println(line.getArea());
		
		Point centroid = line.getCentroid();
		System.out.println(centroid.toString());
		System.out.println(line.contains(centroid));
	}

}
