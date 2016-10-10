package edu.pnu.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import net.opengis.citygml.building.AbstractBuilding;
import edu.pnu.project.FloorProperty;
import edu.pnu.project.ProjectFile;

public class CityGMLExportDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table_FloorList;
	private JButton btnNewBuildingpart;
	private JComboBox comboBox_BuildingPart;
	private JTable table_BuildingPartFloor;
	
	private CanvasPanel parent;
	private ProjectFile project;
	
	private Map<String, FloorProperty> floorMap;
	private JScrollPane scrollPane_FloorList;
	private JScrollPane scrollPane_BuildingPartFloor;
	private JButton btnDeleteBuildingpart;
	private JLabel lblBuildingpart;
	private JButton btnEditbuildingpart;
	private Map<String, AbstractBuilding> buildingMap; // GMLID, Building(Part)

	/**
	 * Launch the application.
	 */
	
	public static void main(String[] args) {
		try {
			CityGMLExportDialog dialog = new CityGMLExportDialog(null, null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * Create the dialog.
	 */
	public CityGMLExportDialog(CanvasPanel parent, ProjectFile project) {
		this.parent = parent;
		this.project = project;
		
		floorMap = new HashMap<String, FloorProperty>();
		makeFloorMap();
		
		buildingMap = new HashMap<String, AbstractBuilding>();
		
		setTitle("CityGML Export");
		setBounds(100, 100, 630, 368);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		contentPanel.add(getBtnNewBuildingpart());
		{
			JButton btnRoofsurface = new JButton("RoofSurface");
			btnRoofsurface.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addSelectedFloorToBuilding("Roof");
					updateTableFloorModel();
					updateTableBuildingPartFloorModel();
				}
			});
			btnRoofsurface.setBounds(181, 97, 155, 23);
			contentPanel.add(btnRoofsurface);
		}
		{
			JButton btnWallsurface = new JButton("WallSurface");
			btnWallsurface.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addSelectedFloorToBuilding("Wall");
					updateTableFloorModel();
					updateTableBuildingPartFloorModel();
				}
			});
			btnWallsurface.setBounds(181, 130, 155, 23);
			contentPanel.add(btnWallsurface);
		}
		{
			JButton btnGroundsurface = new JButton("GroundSurface");
			btnGroundsurface.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addSelectedFloorToBuilding("Ground");
					updateTableFloorModel();
					updateTableBuildingPartFloorModel();
				}
			});
			btnGroundsurface.setBounds(181, 163, 155, 23);
			contentPanel.add(btnGroundsurface);
		}
		{
			JButton button = new JButton("<<");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					removeSelectedBuildingPartFloor();
					updateTableFloorModel();
					updateTableBuildingPartFloorModel();
				}
			});
			button.setBounds(181, 196, 155, 23);
			contentPanel.add(button);
		}
		contentPanel.add(getComboBox_BuildingPart());
		contentPanel.add(getScrollPane_FloorList());
		contentPanel.add(getScrollPane_BuildingPartFloor());
		contentPanel.add(getBtnDeleteBuildingpart());
		contentPanel.add(getLblBuildingpart());
		contentPanel.add(getBtnEditbuildingpart());
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	private JTable getTable_FloorList() {
		if (table_FloorList == null) {
			table_FloorList = new JTable();
			table_FloorList.setModel(new DefaultTableModel(
				new Object[][] {
					{null},
				},
				new String[] {
					"Floor"
				}
			) {
				Class[] columnTypes = new Class[] {
					String.class
				};
				public Class getColumnClass(int columnIndex) {
					return columnTypes[columnIndex];
				}
			});
			
			updateTableFloorModel();
		}
		return table_FloorList;
	}
	private JButton getBtnNewBuildingpart() {
		if (btnNewBuildingpart == null) {
			btnNewBuildingpart = new JButton("New");
			btnNewBuildingpart.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					CityGMLAbstractBuildingAttributeDialog dialog = new CityGMLAbstractBuildingAttributeDialog(null, buildingMap);
                    dialog.setModal(true);
                    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    dialog.setVisible(true);
                    
                    AbstractBuilding building = dialog.getBuilding();
                    if (building == null || buildingMap.containsKey(building.getGmlID())) {
                    	
                    } else {
                    	buildingMap.put(building.getGmlID(), building);
                    	updateComboBoxBuildingPartModel();
                    }
				}
			});
			btnNewBuildingpart.setBounds(348, 41, 80, 23);
		}
		return btnNewBuildingpart;
	}
	private JComboBox getComboBox_BuildingPart() {
		if (comboBox_BuildingPart == null) {
			comboBox_BuildingPart = new JComboBox();
			comboBox_BuildingPart.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					updateTableBuildingPartFloorModel();
				}
			});
			comboBox_BuildingPart.setBounds(432, 10, 170, 21);
		}
		return comboBox_BuildingPart;
	}
	private JTable getTable_BuildingPartFloor() {
		if (table_BuildingPartFloor == null) {
			table_BuildingPartFloor = new JTable();
			table_BuildingPartFloor.setModel(new DefaultTableModel(
				new Object[][] {
					{null, null},
				},
				new String[] {
					"Floor", "Type"
				}
			) {
				Class[] columnTypes = new Class[] {
					String.class, String.class
				};
				public Class getColumnClass(int columnIndex) {
					return columnTypes[columnIndex];
				}
			});
		}
		return table_BuildingPartFloor;
	}
	private JScrollPane getScrollPane_FloorList() {
		if (scrollPane_FloorList == null) {
			scrollPane_FloorList = new JScrollPane();
			scrollPane_FloorList.setBounds(12, 10, 157, 277);
			scrollPane_FloorList.setViewportView(getTable_FloorList());
		}
		return scrollPane_FloorList;
	}
	private JScrollPane getScrollPane_BuildingPartFloor() {
		if (scrollPane_BuildingPartFloor == null) {
			scrollPane_BuildingPartFloor = new JScrollPane();
			scrollPane_BuildingPartFloor.setBounds(348, 74, 254, 213);
			scrollPane_BuildingPartFloor.setViewportView(getTable_BuildingPartFloor());
		}
		return scrollPane_BuildingPartFloor;
	}
	private JButton getBtnDeleteBuildingpart() {
		if (btnDeleteBuildingpart == null) {
			btnDeleteBuildingpart = new JButton("Delete");
			btnDeleteBuildingpart.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					AbstractBuilding building = getSelectedBuilding();
					
					if (building != null) {
						buildingMap.remove(building.getGmlID());
					}
					updateComboBoxBuildingPartModel();
				}
			});
			btnDeleteBuildingpart.setBounds(436, 41, 80, 23);
		}
		return btnDeleteBuildingpart;
	}
	private JLabel getLblBuildingpart() {
		if (lblBuildingpart == null) {
			lblBuildingpart = new JLabel("BuildingPart");
			lblBuildingpart.setBounds(348, 10, 72, 15);
		}
		return lblBuildingpart;
	}
	private JButton getBtnEditbuildingpart() {
		if (btnEditbuildingpart == null) {
			btnEditbuildingpart = new JButton("Edit");
			btnEditbuildingpart.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					AbstractBuilding building = getSelectedBuilding();
					CityGMLAbstractBuildingAttributeDialog dialog = new CityGMLAbstractBuildingAttributeDialog(building, buildingMap);
                    dialog.setModal(true);
                    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    dialog.setVisible(true);
                    
                    // TODO
                    AbstractBuilding result = dialog.getBuilding();
                    if (result == null || buildingMap.containsKey(result.getGmlID())) {
                    	
                    } else {
                    	buildingMap.put(building.getGmlID(), building);
                    	updateComboBoxBuildingPartModel();
                    }
				}
			});
			btnEditbuildingpart.setBounds(522, 41, 80, 23);
		}
		return btnEditbuildingpart;
	}
	private void makeFloorMap() {
		if (project == null) return;
		
		ArrayList<FloorProperty> floorPropertyList = project.getBuildingProperty().getFloorProperties();
		for(FloorProperty floorProperty : floorPropertyList) {
			if(!floorMap.containsKey(floorProperty.getLevel()))
				floorMap.put(floorProperty.getLevel(), floorProperty);
		}
	}
	private void updateTableFloorModel() {
		if (project == null) return;
		
		DefaultTableModel model = (DefaultTableModel) table_FloorList.getModel();
		
		int rowCount = model.getRowCount();
		for(int i = 0; i < rowCount; i++) {
			model.removeRow(rowCount - i - 1);
		}
		
		ArrayList<FloorProperty> floorProperties = new ArrayList<FloorProperty>(floorMap.values());
		Collections.sort(floorProperties, new Comparator<FloorProperty>() {
			public int compare(FloorProperty f1, FloorProperty f2) {
				return f1.getLevel().compareTo(f2.getLevel());
			}
		});
		
		for(FloorProperty floorProperty : floorProperties) {
			model.addRow(new Object[]{ floorProperty.getLevel() });
		}
	}
	private void updateComboBoxBuildingPartModel() {
		ArrayList<AbstractBuilding> buildings = new ArrayList<AbstractBuilding>(buildingMap.values());
		Collections.sort(buildings, new Comparator<AbstractBuilding>() {
			public int compare(AbstractBuilding f1, AbstractBuilding f2) {
				return f1.getGmlID().compareTo(f2.getGmlID());
			}
		});
		
        String[] items = new String[buildings.size()];
        
        Iterator<AbstractBuilding> it = buildings.iterator();
        int i = 0;
        while (it.hasNext()) {
        	AbstractBuilding building = it.next();
        	items[i++] = building.getGmlID();
        }
        
        DefaultComboBoxModel model = new DefaultComboBoxModel(items);
        comboBox_BuildingPart.setModel(model);
    }
	private AbstractBuilding getSelectedBuilding() {
		DefaultComboBoxModel model = (DefaultComboBoxModel) comboBox_BuildingPart.getModel();
		String id = (String) model.getSelectedItem();
		if (id == null) return null;
		
		AbstractBuilding building = buildingMap.get(id);
		return building;
	}
	private FloorProperty getSelectedFloor() {
		int selectedRow = table_FloorList.getSelectedRow();
		if (selectedRow == -1) return null;
		
		DefaultTableModel model = (DefaultTableModel) table_FloorList.getModel();
		String level = (String) model.getValueAt(selectedRow, 0);
		
		FloorProperty floorProperty = floorMap.get(level);
		return floorProperty;
	}
	private void addSelectedFloorToBuilding(String floorType) {
		AbstractBuilding building = getSelectedBuilding();
		FloorProperty floor = getSelectedFloor();
		
		if (building == null || floor == null) return;
		
		List<FloorProperty> floorTypeList = null;
		if (floorType.equals("Roof")) {
			floorTypeList = building.getRoofFloor();
		} else if (floorType.equals("Wall")) {
			floorTypeList = building.getWallFloor();
		} else if (floorType.equals("Ground")) {
			floorTypeList = building.getGroundFloor();
		}
		floorTypeList.add(floor);
		
		floorMap.remove(floor.getLevel());
	}
	private void updateTableBuildingPartFloorModel() {
		DefaultTableModel model = (DefaultTableModel) table_BuildingPartFloor.getModel();
		int rowCount = model.getRowCount();
		for(int i = 0; i < rowCount; i++) {
			model.removeRow(rowCount - i - 1);
		}
		
		AbstractBuilding building = getSelectedBuilding();
		if (building == null) return;		
		List<FloorProperty> roofFloor = building.getRoofFloor();
		List<FloorProperty> wallFloor = building.getWallFloor();
		List<FloorProperty> groundFloor = building.getGroundFloor();
		
		for (FloorProperty roof : roofFloor) {
			model.addRow(new String[]{roof.getLevel(), "RoofSurface"});
		}
		for (FloorProperty wall : wallFloor) {
			model.addRow(new String[]{wall.getLevel(), "WallSurface"});
		}
		for (FloorProperty ground : groundFloor) {
			model.addRow(new String[]{ground.getLevel(), "GroundSurface"});
		}
    }
	private void removeSelectedBuildingPartFloor() {
		AbstractBuilding building = getSelectedBuilding();
		FloorProperty floor = null;
		
		int selectedRow = table_BuildingPartFloor.getSelectedRow();
		if (building == null || selectedRow == -1) return;
		
		DefaultTableModel model = (DefaultTableModel) table_BuildingPartFloor.getModel();
		String level = (String) model.getValueAt(selectedRow, 0);
		String type = (String) model.getValueAt(selectedRow, 1);
		
		List<FloorProperty> floorList = null;
		if (type.equals("RoofSurface")) {
			floorList = building.getRoofFloor();
		} else if (type.equals("WallSurface")) {
			floorList = building.getWallFloor();
		} else if (type.equals("GroundSurface")) {
			floorList = building.getGroundFloor();
		}
		
		for (FloorProperty f : floorList) {
			if (f.getLevel().equals(level)) {
				floor = f;
				break;
			}
		}
		
		if (floor != null) {
			floorList.remove(floor);
			floorMap.put(floor.getLevel(), floor);
		}
	}
	public List<AbstractBuilding> getBuildingList() {
		List<AbstractBuilding> buildingList = new ArrayList<AbstractBuilding>(buildingMap.values());
		Collections.sort(buildingList, new Comparator<AbstractBuilding>() {
			public int compare(AbstractBuilding b1, AbstractBuilding b2) {
				return b1.getGmlID().compareTo(b2.getGmlID());
			}
		});
		return buildingList;
	}
}
