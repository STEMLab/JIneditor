package edu.pnu.indoorgml;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBElement;

import net.opengis.indoorgml.core.CellSpace;
import net.opengis.indoorgml.core.CellSpaceBoundary;

import com.vividsolutions.jts.geom.Coordinate;

import edu.pnu.project.FloorProperty;
import edu.pnu.project.ProjectFile;

/**
 * A Manager class for editing indoor spatial data by using JInedit library.
 * This class supports several functions to supervise elements of IndoorGML.
 * Also, IndoorGML document can be generated.
 *  
 * @author Donguk Seo
 *
 */
public interface JIneditManager {
	
	/**
	 * Creates a new project to store elements of IndoorGML.
	 * 
	 * @return The project.
	 */
	public ProjectFile createProject();
	
	/**
	 * Loads the elements of IndoorGML from the exist project.
	 * 
	 * @param project The exist project.
	 * @return The project.
	 */
	public ProjectFile openProject(ProjectFile project);
	
	/**
	 * Saves the elements of IndoorGML to this project.
	 * @return The project.
	 */
	public ProjectFile saveProject() ;
	
	/**
	 * Create a new Floor of a building.
	 * 
	 * @param name the name of a new floor.
	 * @param lowerCorner the coordinate of lower corner of a new floor.
	 * @param upperCorner the coordinate of upper corner of a new floor.
	 * @param floorPlan the image file of floor plan of a new floor.
	 * @return The FloorProperty object.
	 */
	public FloorProperty createFloor(String name, Coordinate lowerCorner, Coordinate upperCorner, File floorPlan);
	
	/**
	 * Deletes the given floor from this project.
	 * Also, all elements of this floor will be removed.
	 * 
	 * @param floor
	 * @return
	 */
	public FloorProperty deleteFloor(FloorProperty floor);
	
	/**
	 * Creates a new CellSpace object at the specified location by coordinates.
	 * 
	 * @param positions the list of coordinates. Should never be null neither empty.
	 * @return The CellSpace object.
	 */
	public CellSpace createCellSpace(List<Coordinate> positions);
	
	/**
	 * Deletes the CellSpace object with the given GML ID.
	 * 
	 * @param id The GML ID.
	 * @return true if this project contained the CellSpace with specified GML ID.
	 */
	public boolean deleteCellSpace(String id);
	
	/**
	 * Create a new CellSpaceBoundary object representing the boundary of the given CellSpace.
	 * @param cellSpace the CellSpace object
	 * @param positions the list of coordinates. Should never be null neither empty.
	 * @return The CellSpaceBoundary object.
	 */
	public CellSpaceBoundary createCellSpaceBoundary(CellSpace cellSpace, List<Coordinate> positions);
	
	/**
	 * Generates a IndoorGML document from the indoor spatial objects of this project.
	 * 
	 * @param is3DGeometry true if the dimension of the coordinates that define Geometry. 
	 * @return The JAXBElement object representing IndoorGML document.
	 */
	public JAXBElement exportIndoorGMLDocument(boolean is3DGeometry);
	
	// jineditmanager, document,  .... API
	
}
