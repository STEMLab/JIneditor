package edu.pnu.test;

import org.geotools.geometry.jts.JTSFactoryFinder;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Polygon;

public class jtspolygontest {

	public jtspolygontest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		Coordinate[] coords = new Coordinate[] {new Coordinate(0, 0), new Coordinate(2, 2), new Coordinate(2, 2), new Coordinate(0, 2), new Coordinate(0, 0)};
		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
		
		LineString exterior = geometryFactory.createLineString(coords);
		Polygon polygon = geometryFactory.createPolygon(coords);
		
		System.out.println("exterior : " + exterior.isValid());
		System.out.println("polygon : " + polygon.isValid());
	}
}
