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
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;

import edu.pnu.util.JTSUtil;

/**
 * @author Donguk Seo
 *
 */
public class snapPointTest {

    @Test
    public void snapPointTest() {
        GeometryFactory gf = JTSFactoryFinder.getGeometryFactory();
        Coordinate coord1 = new Coordinate(89.37412487,110.2487538);
        Coordinate coord2 = new Coordinate(125.76543268795456, 130.0001458);
        Coordinate coord3 = new Coordinate(100.02458, 119.88754);
        LineString line = gf.createLineString(new Coordinate[]{coord1, coord2});
        Point point = gf.createPoint(coord3);
        
        Point snapPoint = JTSUtil.snapPointToLineStringByLIL(line, point);
        System.out.println(snapPoint.toText());
        
        Double a = 5.00000;
        String str = String.valueOf(a);
        System.out.println(str);
        
        System.out.println("test");
    }

}
