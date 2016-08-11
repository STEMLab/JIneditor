package edu.pnu.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;

import net.opengis.citygml.building.v_2_0.AbstractBoundarySurfaceType;
import net.opengis.citygml.building.v_2_0.AbstractBuildingType;
import net.opengis.citygml.building.v_2_0.AbstractOpeningType;
import net.opengis.citygml.building.v_2_0.BoundarySurfacePropertyType;
import net.opengis.citygml.building.v_2_0.BuildingType;
import net.opengis.citygml.building.v_2_0.CeilingSurfaceType;
import net.opengis.citygml.building.v_2_0.ClosureSurfaceType;
import net.opengis.citygml.building.v_2_0.DoorType;
import net.opengis.citygml.building.v_2_0.FloorSurfaceType;
import net.opengis.citygml.building.v_2_0.GroundSurfaceType;
import net.opengis.citygml.building.v_2_0.InteriorRoomPropertyType;
import net.opengis.citygml.building.v_2_0.InteriorWallSurfaceType;
import net.opengis.citygml.building.v_2_0.OpeningPropertyType;
import net.opengis.citygml.building.v_2_0.RoofSurfaceType;
import net.opengis.citygml.building.v_2_0.RoomType;
import net.opengis.citygml.building.v_2_0.WallSurfaceType;
import net.opengis.citygml.v_2_0.AbstractCityObjectType;
import net.opengis.citygml.v_2_0.CityModelType;
import net.opengis.gml.v_3_1_1.AbstractGMLType;
import net.opengis.gml.v_3_1_1.AbstractGeometryType;
import net.opengis.gml.v_3_1_1.AbstractRingPropertyType;
import net.opengis.gml.v_3_1_1.AbstractRingType;
import net.opengis.gml.v_3_1_1.AbstractSurfaceType;
import net.opengis.gml.v_3_1_1.CompositeSurfaceType;
import net.opengis.gml.v_3_1_1.DirectPositionListType;
import net.opengis.gml.v_3_1_1.FeaturePropertyType;
import net.opengis.gml.v_3_1_1.LinearRingType;
import net.opengis.gml.v_3_1_1.MultiSurfacePropertyType;
import net.opengis.gml.v_3_1_1.MultiSurfaceType;
import net.opengis.gml.v_3_1_1.OrientableSurfaceType;
import net.opengis.gml.v_3_1_1.PolygonType;
import net.opengis.gml.v_3_1_1.SignType;
import net.opengis.gml.v_3_1_1.StringOrRefType;
import net.opengis.gml.v_3_1_1.SurfacePropertyType;
import net.opengis.indoorgml.core.AbstractFeature;
import net.opengis.indoorgml.core.CellSpace;
import net.opengis.indoorgml.core.CellSpaceBoundary;
import net.opengis.indoorgml.core.CellSpaceBoundaryOnFloor;
import net.opengis.indoorgml.core.CellSpaceOnFloor;
import net.opengis.indoorgml.core.IndoorFeatures;
import net.opengis.indoorgml.core.PrimalSpaceFeatures;
import net.opengis.indoorgml.geometry.AbstractGeometry;
import net.opengis.indoorgml.geometry.LineString;
import net.opengis.indoorgml.geometry.LinearRing;
import net.opengis.indoorgml.geometry.Point;
import net.opengis.indoorgml.geometry.Polygon;
import net.opengis.indoorgml.geometry.Shell;
import net.opengis.indoorgml.geometry.Solid;
import edu.pnu.project.BoundaryType;

public class CityGMLJAXBConvertor {
	private net.opengis.citygml.building.v_2_0.ObjectFactory bldgFactory;
	private net.opengis.citygml.v_2_0.ObjectFactory coreFactory;
	private net.opengis.gml.v_3_1_1.ObjectFactory gmlFactory;
	
	private IndoorFeatures indoorFeatures;
	private Map<CellSpaceBoundary, CellSpaceBoundary> boundary3DMap;
	
	public CityGMLJAXBConvertor(IndoorFeatures indoorFeatures, Map<CellSpaceBoundary, CellSpaceBoundary> boundary3DMap) {
		bldgFactory = new net.opengis.citygml.building.v_2_0.ObjectFactory();
		coreFactory = new net.opengis.citygml.v_2_0.ObjectFactory();
		gmlFactory = new net.opengis.gml.v_3_1_1.ObjectFactory();
		
		this.indoorFeatures = indoorFeatures;
		this.boundary3DMap = boundary3DMap;
	}
	
	public JAXBElement<CityModelType> getJAXBElement() {
		CityModelType cityModelType = createCityModelType();
		JAXBElement<CityModelType> jCityModel = coreFactory.createCityModel(cityModelType);
		return jCityModel;
	}
	
	public CityModelType createCityModelType() {
		CityModelType target = coreFactory.createCityModelType();
		
		setAbstractGML(target, indoorFeatures);
		
		// TODO: implementation Envelope
		
		// create Building
		List<FeaturePropertyType> featureMember = target.getFeatureMember();
		FeaturePropertyType fp = gmlFactory.createFeaturePropertyType();
		
		JAXBElement<? extends AbstractCityObjectType> jCityObject = createCityObjectType(indoorFeatures);
		fp.setFeature(jCityObject);
		
		featureMember.add(fp);
		
		
		return target;
	}
	
	public JAXBElement<? extends AbstractCityObjectType> createCityObjectType(IndoorFeatures indoorFeatures) {
		JAXBElement<? extends AbstractCityObjectType> jCityObject= null;
		
		AbstractBuildingType buildingType = createBuildingType(indoorFeatures);
		jCityObject = bldgFactory.createBuilding((BuildingType) buildingType);
		
		return jCityObject;
	}
	
	public void setAbstractGML(AbstractGMLType target, AbstractFeature feature) {
		String gmlID = feature.getGmlID();
		target.setId(gmlID);
		
		String name = feature.getName();
		// name
		
		String description = feature.getDescription("Description");
		if (description != null) {
			StringOrRefType descriptionType = gmlFactory.createStringOrRefType();
			descriptionType.setValue(description);
			target.setDescription(descriptionType);
		}
	}
	
	public void setAbstractGeometry(AbstractGeometryType target, AbstractGeometry geometry) {
		String gmlID = geometry.getGMLID();
		target.setId(gmlID);
	}
	
	public void setCityObjectAttributes(AbstractCityObjectType target, AbstractFeature feature) {
		setAbstractGML(target, feature);
		
		// createionDate
		// terminateDate
		// relativeToTerrain
		// relativeToWater
	}
	
	// Building Module
	public AbstractBuildingType createBuildingType(IndoorFeatures indoorFeatures) {
		AbstractBuildingType target = null;
		
		// TODO: consider BuildingPart - ex) avenuel, cinema, shopping mall, ...
		target = bldgFactory.createBuildingType();
		
		setCityObjectAttributes(target, indoorFeatures);
		
		// clazz
		// functions
		// usages
		// yearOfConstruction
		// yearOfDemolition
		// roofType
		// measuredHeight
		// storeysAboveGround
		// storeyHeightAbove
		// storeyHeightsAboveGroundValus
		// storeyHeightsBelow
		
		// geometry
		
		// Address
		// BoundedBy : roofSurface, ...
		
		// Rooms
		List<InteriorRoomPropertyType> interiorRoomProps = target.getInteriorRoom();
		PrimalSpaceFeatures primalSpaceFeatures = indoorFeatures.getPrimalSpaceFeatures();
		List<CellSpaceOnFloor> cellSpaceOnFloors = primalSpaceFeatures.getCellSpaceOnFloors();
		List<CellSpaceBoundaryOnFloor> cellSpaceBoundaryOnFloors = primalSpaceFeatures.getCellSpaceBoundaryOnFloors();
		for (int i = 0; i < cellSpaceOnFloors.size(); i++) {
			CellSpaceOnFloor cellFloor = cellSpaceOnFloors.get(i);
			CellSpaceBoundaryOnFloor boundaryFloor = cellSpaceBoundaryOnFloors.get(i);
			
			List<CellSpace> cellSpaceMember = cellFloor.getCellSpaceMember();
			for (CellSpace cellSpace : cellSpaceMember) {
				InteriorRoomPropertyType interiorRoomProp = bldgFactory.createInteriorRoomPropertyType();
				RoomType roomType = createRoomType(cellSpace, boundaryFloor);
				interiorRoomProp.setRoom(roomType);
				interiorRoomProps.add(interiorRoomProp);
			}
		}
		
		
		// BuildingInstallations
		// IntBuildingInstalltion
		// BuildingParts
		
		return target;
	}
	
	public RoomType createRoomType(CellSpace cellSpace, CellSpaceBoundaryOnFloor boundaryFloor) {
		RoomType target = bldgFactory.createRoomType();
		
		setCityObjectAttributes(target, cellSpace);
		
		// clazz
		// usages
		// RoomInstallation
		// InteriorFurniture
		

		// : The surfaces of the Solid object of CellSpace consists of following sequence(upper, side, lower)
		HashMap<LineString, ArrayList<CellSpaceBoundary>> lineBoundaryMap = boundaryFloor.getLineStringOfAdjacencyBoundaryMap();
		
		List<LineString> lineElements = cellSpace.getLineStringElements();
		Solid solid = cellSpace.getGeometry3D();
		Shell exteriorShell = solid.getExteriorShell();
		List<Polygon> surfaceMember = exteriorShell.getSurfaceMember();
		
		// BoundedBy
		List<BoundarySurfacePropertyType> boundaryPropertyList = target.getCityObjectBoundedBy();
		List<Polygon> solidSurfaceResource = new ArrayList<Polygon>();
		ArrayList<Polygon> boundarySurface = new ArrayList<Polygon>();
		ArrayList<Polygon> doorSurface = new ArrayList<Polygon>();
		
		// BoundedBy : CeilingSurface
		for (int i = 0; i < surfaceMember.size(); i++) {
			String type = null;
			type = "InteriorWallSurface";
			if (i == 0) {
				type = "CeilingSurface";
				boundarySurface.add(surfaceMember.get(i));
			} else if (i == surfaceMember.size() - 1) {
				type = "FloorSurface";
				boundarySurface.add(surfaceMember.get(i));
			} else {
				type = "InteriorWallSurface";
				LineString line = lineElements.get(i - 1);
				if (lineBoundaryMap.containsKey(line)) {
					ArrayList<CellSpaceBoundary> adjacencyBoundary = lineBoundaryMap.get(line);
					ArrayList<CellSpaceBoundary> candidate = new ArrayList<CellSpaceBoundary>();
					if (adjacencyBoundary != null) {
						for (CellSpaceBoundary cellBoundary : adjacencyBoundary) {
							CellSpaceBoundary boundary3D = boundary3DMap.get(cellBoundary);
							if (boundary3D != null) {
								if (!candidate.contains(boundary3D)) {
									candidate.add(boundary3D);
								}
							}
						}
					}
					
					for (CellSpaceBoundary cellBoundary : candidate) {
						if (cellBoundary.getBoundaryType() == BoundaryType.Boundary3D) {
							boundarySurface.add(cellBoundary.getGeometry3D());
						} else if (cellBoundary.getBoundaryType() == BoundaryType.Door) {
							doorSurface.add(cellBoundary.getGeometry3D());
						}
					}
					
					// TODO: SolidSurface Orientation
					// TODO: boundarySurface.isEmpty() && !doorSurface.isEmpty()
					if (boundarySurface.isEmpty() && !doorSurface.isEmpty()) {
						
					}
				} else {
					boundarySurface.add(surfaceMember.get(i));
				}
			}
			
			AbstractBoundarySurfaceType boundary = createAbstractBoundarySurfaceType(boundarySurface, doorSurface, type);
			JAXBElement<? extends AbstractBoundarySurfaceType> jBoundary = createJAXBBoundarySurfaceElement(boundary);

			BoundarySurfacePropertyType bsProp = bldgFactory.createBoundarySurfacePropertyType();
			bsProp.setBoundarySurface(jBoundary);
			boundaryPropertyList.add(bsProp);
			
			solidSurfaceResource.addAll(boundarySurface);
			
			boundarySurface.clear();
			doorSurface.clear();
		}
		
		// Geometry
		// boundarySurface들의 Polygon을 모두 xlink로 가지는 Polygon을 생성한다.
		// boundarySurface의 Polygon이 xlink를 가지고 있으면 방향을 그것의 반대로 한다.
		ArrayList<Polygon> solidSurface = new ArrayList<Polygon>();
		for (Polygon polygon : solidSurfaceResource) {
			Polygon xlinkPolygon = new Polygon();
			if (polygon.getxLinkGeometry() == null) {
				xlinkPolygon.setxLinkGeometry(polygon);
				xlinkPolygon.setIsReversed(true);
			} else {
				xlinkPolygon.setxLinkGeometry(polygon.getxLinkGeometry());
				boolean isReversed = polygon.getIsReversed();
				xlinkPolygon.setIsReversed(!isReversed);
			}
			solidSurface.add(xlinkPolygon);
		}
		MultiSurfacePropertyType multiSurfaceProp = createMultiSurfacePropertyType(solidSurface);
		target.setLod4MultiSurface(multiSurfaceProp);
		
		return target;
	}
	
	public AbstractBoundarySurfaceType createAbstractBoundarySurfaceType(List<Polygon> boundarySurface, List<Polygon> doorSurface, String type) {
		AbstractBoundarySurfaceType target = null;
		
		if (type.equals("RoofSurface")) {
			target = bldgFactory.createRoofSurfaceType();
		} else if (type.equals("GroundSurface")) {
			target = bldgFactory.createGroundSurfaceType();
		} else if (type.equals("WallSurface")) {
			target = bldgFactory.createWallSurfaceType();
		} else if (type.equals("CeilingSurface")) {
			target = bldgFactory.createCeilingSurfaceType();
		} else if (type.equals("FloorSurface")) {
			target = bldgFactory.createFloorSurfaceType();
		} else if (type.equals("InteriorWallSurface")) {
			target = bldgFactory.createInteriorWallSurfaceType();
		} else if (type.equals("ClosureSurface")) {
			target = bldgFactory.createClosureSurfaceType();
		}
		
		// Opening (only door)
		if (doorSurface.size() > 0) {
			List<OpeningPropertyType> openingProps = target.getOpening();
			for (Polygon door : doorSurface) {
				OpeningPropertyType openingProp = bldgFactory.createOpeningPropertyType();
				AbstractOpeningType openingType = createAbstractOpeningType(door);
				JAXBElement<? extends AbstractOpeningType> jOpeningType = createJAXBOpeningElement(openingType);
				openingProp.setOpening(jOpeningType);
				openingProps.add(openingProp);
			}
		}
		
		// Geometry
		MultiSurfacePropertyType multiSurfaceProp = createMultiSurfacePropertyType(boundarySurface);
		target.setLod4MultiSurface(multiSurfaceProp);
		
		return target;
	}
	
	public JAXBElement<? extends AbstractBoundarySurfaceType> createJAXBBoundarySurfaceElement(AbstractBoundarySurfaceType target) {
		JAXBElement<? extends AbstractBoundarySurfaceType> jBoundary = null;
		
		if(target instanceof RoofSurfaceType) {
			jBoundary = bldgFactory.createRoofSurface((RoofSurfaceType) target);
		} else if(target instanceof GroundSurfaceType) {
			jBoundary = bldgFactory.createGroundSurface((GroundSurfaceType) target);
		} else if(target instanceof WallSurfaceType) {
			jBoundary = bldgFactory.createWallSurface((WallSurfaceType) target);
		} else if(target instanceof CeilingSurfaceType) {
			jBoundary = bldgFactory.createCeilingSurface((CeilingSurfaceType) target);
		} else if(target instanceof FloorSurfaceType) {
			jBoundary = bldgFactory.createFloorSurface((FloorSurfaceType) target);
		} else if(target instanceof InteriorWallSurfaceType) {
			jBoundary = bldgFactory.createInteriorWallSurface((InteriorWallSurfaceType) target);
		} else if(target instanceof ClosureSurfaceType) {
			jBoundary = bldgFactory.createClosureSurface((ClosureSurfaceType) target);
		}
		
		return jBoundary;
	}
	
	public AbstractOpeningType createAbstractOpeningType(Polygon geometry) {
		AbstractOpeningType target = null;
		
		// consider only door
		target = bldgFactory.createDoorType();
		
		MultiSurfacePropertyType multiSurfaceProp = createMultiSurfacePropertyType(geometry);
		target.setLod4MultiSurface(multiSurfaceProp);
		
		return target;
	}
	
	public JAXBElement<? extends AbstractOpeningType> createJAXBOpeningElement(AbstractOpeningType target) {
		JAXBElement<? extends AbstractOpeningType> jOpening = null;
		
		if (target instanceof DoorType) {
			jOpening = bldgFactory.createDoor((DoorType) target);
		}
		
		return jOpening;
	}
	
	public MultiSurfacePropertyType createMultiSurfacePropertyType(Polygon polygon) {
		ArrayList<Polygon> polygons = new ArrayList<Polygon>();
		polygons.add(polygon);
		return createMultiSurfacePropertyType(polygons);
	}
	
	public MultiSurfacePropertyType createMultiSurfacePropertyType(List<Polygon> polygons) {
		MultiSurfacePropertyType target = gmlFactory.createMultiSurfacePropertyType();
		
		MultiSurfaceType multiSurface = createMultiSurfaceType(polygons);
		target.setMultiSurface(multiSurface);
		
		return target;
	}
	
	public MultiSurfaceType createMultiSurfaceType(List<Polygon> polygons) {
		MultiSurfaceType target = gmlFactory.createMultiSurfaceType();
		
		List<SurfacePropertyType> surfaceMember = target.getSurfaceMember();
		SurfacePropertyType surfaceProp = createSurfacePropertyType(polygons);
		surfaceMember.add(surfaceProp);
		
		return target;
	}
	
	public SurfacePropertyType createSurfacePropertyType(Polygon polygon) {
		ArrayList<Polygon> polygons = new ArrayList<Polygon>();
		polygons.add(polygon);
		return createSurfacePropertyType(polygons);
	}
	
	public SurfacePropertyType createSurfacePropertyType(List<Polygon> polygons) {
		SurfacePropertyType target = gmlFactory.createSurfacePropertyType();
		
		JAXBElement<? extends AbstractSurfaceType> surface = createAbstractSurfaceType(polygons);
		target.setSurface(surface);
		
		return target;
	}
	
	public JAXBElement<? extends AbstractSurfaceType> createAbstractSurfaceType(List<Polygon> polygons) {
		AbstractSurfaceType target = null;
		
		if (polygons.size() == 1) {
			Polygon polygon = polygons.get(0);
			if (polygon.getxLinkGeometry() == null) {
				target = gmlFactory.createPolygonType();
				PolygonType polygonType = (PolygonType) target;
				
				setAbstractGeometry(polygonType, polygon);
						
				LinearRing exteriorRing = polygon.getExteriorRing();
				if (exteriorRing != null) {
					AbstractRingPropertyType ringProp = createAbstractRingPropertyType(exteriorRing);
					JAXBElement<AbstractRingPropertyType> jRingProp = gmlFactory.createExterior(ringProp);
					polygonType.setExterior(jRingProp);
				}
				
				List<JAXBElement<AbstractRingPropertyType>> interiorRingProps = polygonType.getInterior();
				for (int i = 0; i < polygon.getInteriorRing().size(); i++) {
					LinearRing interiorRing = polygon.getInteriorRing().get(i);
					AbstractRingPropertyType ringProp = createAbstractRingPropertyType(interiorRing);
					JAXBElement<AbstractRingPropertyType> jRingProp = gmlFactory.createExterior(ringProp);
					interiorRingProps.add(jRingProp);
				}
			} else if (polygon.getxLinkGeometry() != null) {
				// OrientableSurface
				target = gmlFactory.createOrientableSurfaceType();
				OrientableSurfaceType osType = (OrientableSurfaceType) target;
				
				if (polygon.getIsReversed()) {
					osType.setOrientation(SignType.VALUE_1);
				} else {
					osType.setOrientation(SignType.VALUE_2);
				}
				
				SurfacePropertyType baseSurface = gmlFactory.createSurfacePropertyType();
				baseSurface.setHref("#" + polygon.getxLinkGeometry().getGMLID());
				osType.setBaseSurface(baseSurface);
			}
		} else if(polygons.size() > 1) {
			target = gmlFactory.createCompositeSurfaceType();
			CompositeSurfaceType s = (CompositeSurfaceType) target;
			
			List<SurfacePropertyType> surfaceMember = s.getSurfaceMember();
			
			for (int i = 0; i < polygons.size(); i++) {
				Polygon polygon = polygons.get(i);
				SurfacePropertyType surfaceProp = createSurfacePropertyType(polygon);
				surfaceMember.add(surfaceProp);
			}
		}
		
		JAXBElement<? extends AbstractSurfaceType> jSurface = null;
		if (target instanceof PolygonType) {
			jSurface = gmlFactory.createPolygon((PolygonType) target);
		} else if (target instanceof OrientableSurfaceType) {
			jSurface = gmlFactory.createOrientableSurface((OrientableSurfaceType) target);
		} else if (target instanceof CompositeSurfaceType) {
			jSurface = gmlFactory.createCompositeSurface((CompositeSurfaceType) target);
		}
		
		return jSurface;
	}
	
	public AbstractRingPropertyType createAbstractRingPropertyType(LineString ring) {
		AbstractRingPropertyType target = gmlFactory.createAbstractRingPropertyType();
		JAXBElement<? extends AbstractRingType> jRing = createRingType(ring);
		target.setRing(jRing);
		return target;
	}
	
	public JAXBElement<? extends AbstractRingType> createRingType(LineString ring) {
		AbstractRingType target = null;
		
		target = gmlFactory.createLinearRingType();
		LinearRingType linearRing = (LinearRingType) target;
		
		DirectPositionListType dPositionList = gmlFactory.createDirectPositionListType();
		List<Double> dList = dPositionList.getValue();
		
		for (int i = 0; i < ring.getPoints().size(); i++) {
			Point point = ring.getPoints().get(i);
			dList.add(point.getRealX());
			dList.add(point.getRealY());
			dList.add(point.getZ());
		}
		linearRing.setPosList(dPositionList);
		
		JAXBElement<? extends AbstractRingType> jRing = null;
		if (target instanceof LinearRingType) {
			jRing = gmlFactory.createLinearRing((LinearRingType) target);
		}
		
		return jRing;
	}
	
}
