/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package edu.pnu.test;

import org.geotools.geometry.jts.JTSFactoryFinder;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.util.AffineTransformation;
import com.vividsolutions.jts.geom.util.AffineTransformationBuilder;

/**
 * @author Donguk Seo
 *
 */
public class test {

    /**
     * 
     */
    
    public static void main(String args[]) {
        /*
        37.5116785 127.1021436

        37.5119898 127.1025482

        37.5134122 127.1025103*/
        /*
         * 
         */
        Coordinate coord1 = new Coordinate(0.14195979899497488, 0.056451612903225756);
        Coordinate coord2 = new Coordinate(0.44597989949748745, 0.1088709677419355);
        Coordinate coord3 = new Coordinate(0.9849246231155779, 0.9905913978494624);
        Coordinate coord4 = new Coordinate(37.5116785, 127.1021436);
        Coordinate coord5 = new Coordinate(37.5119898, 127.1025482);
        Coordinate coord6 = new Coordinate(37.5134122, 127.1025103);
        
        Coordinate test = new Coordinate(0.9510050251256281, 0.728494623655914);

        Point p = JTSFactoryFinder.getGeometryFactory().createPoint(test);
        
        AffineTransformation affine = new AffineTransformationBuilder(coord1, coord2, coord3, coord4, coord5, coord6).getTransformation();
        Geometry geom = affine.transform(p);
        System.out.println(geom.toText());
    }

}
