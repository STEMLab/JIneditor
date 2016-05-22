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
package edu.pnu.importexport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import net.opengis.indoorgml.core.CellSpace;
import net.opengis.indoorgml.core.State;
import net.opengis.indoorgml.geometry.LineString;
import net.opengis.indoorgml.geometry.Point;
import net.opengis.indoorgml.geometry.Polygon;

import org.geotools.geometry.jts.JTSFactoryFinder;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.util.AffineTransformation;
import com.vividsolutions.jts.geom.util.AffineTransformationBuilder;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import edu.pnu.gui.CanvasPanel;
import edu.pnu.project.ProjectFile;
import edu.pnu.project.StateOnFloor;
import edu.pnu.util.GeometryUtil;
import edu.pnu.util.JTSUtil;


/**
 * @author Donguk Seo
 *
 */
public class WKTImporter {
    private CanvasPanel panel;
    private ProjectFile project;
    private ArrayList<Coordinate> panelRefs;
    private ArrayList<Coordinate> actualRefs;

    /**
     * 
     */
    public WKTImporter(CanvasPanel panel, ProjectFile project) {
            this.panel = panel;
            this.project = project;
            
            panelRefs = new ArrayList<Coordinate>();
            actualRefs = new ArrayList<Coordinate>();
            
            generateReferencePoints(project);
    }
    
    public void generateReferencePoints(ProjectFile project) {
    		panelRefs.add(new Coordinate(0,0));
    		panelRefs.add(new Coordinate(1,0));
    		panelRefs.add(new Coordinate(1,1));
    		
    		Point bottomLeft = project.getCurrentStateOnFloor().getFloorProperty().getBottomLeftPoint();
    		Point topRight = project.getCurrentStateOnFloor().getFloorProperty().getTopRightPoint();
    		
    		actualRefs.add(new Coordinate(bottomLeft.getPanelX(), bottomLeft.getPanelY()));
    		actualRefs.add(new Coordinate(topRight.getPanelX(), bottomLeft.getPanelY()));
    		actualRefs.add(new Coordinate(topRight.getPanelX(), topRight.getPanelY()));
    }
    
    public void read(File wktFile) {
            GeometryFactory gf = JTSFactoryFinder.getGeometryFactory();
            WKTReader wktReader = new WKTReader(gf);
            FileReader fr = null;
            BufferedReader fileReader = null;
            
            try {
                fr = new FileReader(wktFile);
                fileReader = new BufferedReader(fr);
                
                while(true) {
                    String line = fileReader.readLine();
                    if(line == null) break;
                    
                    //com.vividsolutions.jts.geom.MultiPolygon multiPolygon = (com.vividsolutions.jts.geom.MultiPolygon) wktReader.read(line);
                    com.vividsolutions.jts.geom.Polygon polygon = (com.vividsolutions.jts.geom.Polygon) wktReader.read(line);
                    double counterClockwised = JTSUtil.Orientation2D_Polygon(polygon.getCoordinates().length, polygon.getExteriorRing().getCoordinateSequence());
        	        if(counterClockwised >= 0) {
        	        	polygon = (com.vividsolutions.jts.geom.Polygon) polygon.reverse();
        	        }
        	        
                    com.vividsolutions.jts.geom.Polygon transformation = (com.vividsolutions.jts.geom.Polygon) transformation(polygon);
                    Polygon geometry2D = getIndoorGMLGeometry(transformation);
                    geometry2D = removeDuplicatePoint(geometry2D);
                    if(geometry2D != null)
                        createCellSpace(geometry2D);
                }
            } catch (IOException | ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }       
    }
    
    private Polygon removeDuplicatePoint(Polygon polygon) {
        ArrayList<Point> before = polygon.getExteriorRing().getPoints();
        ArrayList<Point> after = new ArrayList<Point>();
        
        after.add(before.get(0));
        for(int i = 1; i < before.size(); i++) {
            Point p0 = before.get(i - 1);
            Point p1 = before.get(i);
            if(p0.getPanelX() == p1.getPanelX() && p0.getPanelY() == p1.getPanelY())
                    continue;
            else
                after.add(before.get(i));
        }
        if(after.size() >= 4) {
            polygon.getExteriorRing().setPoints(after);
        } else
            polygon = null;
        
        return polygon;
    }
    
    private CellSpace createCellSpace(Polygon geometry2D) {
            CellSpace cellSpace = new CellSpace();
            cellSpace.setGeometry2D(geometry2D);
            project.getCurrentCellSpaceOnFloor().getCellSpaceMember().add(cellSpace);
            
            ArrayList<Point> exteriorPoints = geometry2D.getExteriorRing().getPoints();
            ArrayList<LineString> lineStrings = new ArrayList<LineString>();
            for (int i = 0; i < exteriorPoints.size(); i++) {
                    LineString newLS = new LineString();
                    newLS.getPoints().add(exteriorPoints.get(i).clone());
                    newLS.getPoints().add(exteriorPoints.get((i + 1) % exteriorPoints.size()).clone());
                    lineStrings.add(newLS);
            }
            cellSpace.setLineStringElements(lineStrings);
            
            StateOnFloor stateOnFloor = project.getCurrentStateOnFloor();
            State dualityState = new State();
            Point point = GeometryUtil.getCentroidPointOnPolygon(geometry2D);
            panel.setPanelRatioXY(point);
            dualityState.setPosition(point);
            dualityState.setDuality(cellSpace);
            stateOnFloor.getStateMember().add(dualityState);
            cellSpace.setDuality(dualityState);
            
            return cellSpace;
    }
    
    private Polygon getIndoorGMLGeometry(com.vividsolutions.jts.geom.Geometry geom) {
        //com.vividsolutions.jts.geom.Polygon polygon = (com.vividsolutions.jts.geom.Polygon) ((com.vividsolutions.jts.geom.MultiPolygon) geom).getGeometryN(0);
        //com.vividsolutions.jts.geom.Polygon reversedPolygon = (com.vividsolutions.jts.geom.Polygon) geom.reverse();
        
        Polygon poly = JTSUtil.convertPolygon((com.vividsolutions.jts.geom.Polygon) geom);
        ArrayList<Point> points = poly.getExteriorRing().getPoints();
        for(Point p : points) {
                double x = p.getPanelX();
                double y = p.getPanelY();
                p.setPanelRatioX(x);
                p.setPanelRatioY(1 - y);
                panel.setPanelXYForCurrentScale(p);
        }
        
        return poly; 
    }
    
    private com.vividsolutions.jts.geom.Geometry transformation(com.vividsolutions.jts.geom.Geometry geometry) {
        /*Coordinate coord1 = new Coordinate(-249931.93, -120081.65);
        Coordinate coord2 = new Coordinate(-99946.0, -120081.65);
        Coordinate coord3 = new Coordinate(-99946.0, 19804.81);
        Coordinate coord4 = new Coordinate(0, 0);
        Coordinate coord5 = new Coordinate(1, 0);
        Coordinate coord6 = new Coordinate(1, 1);*/
        
        /*Coordinate coord1 = new Coordinate(-249.93193, -120.08165);
        Coordinate coord2 = new Coordinate(-99.9460, -120.08165);
        Coordinate coord3 = new Coordinate(-99.9460, 198.0481);
        Coordinate coord4 = new Coordinate(0, 0);
        Coordinate coord5 = new Coordinate(1, 0);
        Coordinate coord6 = new Coordinate(1, 1);*/
        
        /*Coordinate coord1 = new Coordinate(-228.47898, -112.37083);
        Coordinate coord2 = new Coordinate(-182.98152, -104.58745);
        Coordinate coord3 = new Coordinate(-102.19993, 18.71582);
        Coordinate coord4 = new Coordinate(0.1444723618090452, 0.05510752688172038);
        Coordinate coord5 = new Coordinate(0.44597989949748745, 0.11021505376344087);
        Coordinate coord6 = new Coordinate(0.9861809045226131, 0.9905913978494624);*/
    	
    	Coordinate coord1 = actualRefs.get(0);
    	Coordinate coord2 = actualRefs.get(1);
    	Coordinate coord3 = actualRefs.get(2);
    	Coordinate coord4 = panelRefs.get(0);
    	Coordinate coord5 = panelRefs.get(1);
    	Coordinate coord6 = panelRefs.get(2);
        
        AffineTransformation affine = new AffineTransformationBuilder(coord1, coord2, coord3, coord4, coord5, coord6).getTransformation();
        com.vividsolutions.jts.geom.Geometry geom = affine.transform(geometry);
        
        return geom;
    }

}
