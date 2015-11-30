package edu.pnu.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import edu.pnu.project.FloorProperty;
import edu.pnu.project.ProjectFile;

public class FloorImportDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private JButton btnEdit;
	private JScrollPane scrollPane;

	private MainFrame parent;
	private ProjectFile target;
	private ProjectFile project;

	private HashMap<String, FloorProperty> targetMap;
	private HashMap<String, FloorProperty> projectMap;
	/**
	 * Launch the application.
	 */
/*	public static void main(String[] args) {
		try {
			FloorListDialog dialog = new FloorListDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
*/
	/**
	 * Create the dialog.
	 */
	public FloorImportDialog(MainFrame parent, ProjectFile target, ProjectFile project) {
		this.parent = parent;
		this.target = target;
		this.project = project;
		
		targetMap = new HashMap<String, FloorProperty>();
		ArrayList<FloorProperty> floorPropertyList = target.getBuildingProperty().getFloorProperties();
		for(FloorProperty floorProperty : floorPropertyList) {
			targetMap.put(floorProperty.getLevel(), floorProperty);
		}
		
		projectMap = new HashMap<String, FloorProperty>();
		floorPropertyList = project.getBuildingProperty().getFloorProperties();
		for(FloorProperty floorProperty : floorPropertyList) {
			projectMap.put(floorProperty.getLevel(), floorProperty);
		}
		
		setBounds(100, 100, 672, 303);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		contentPanel.add(getScrollPane());
		getScrollPane().setViewportView(getTable());
		//contentPanel.add(getTable());
		contentPanel.add(getBtnEdit());
		{
			JButton btnDelete = new JButton("Delete");
			btnDelete.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int selectedRow = table.getSelectedRow();
					System.out.println("selectedRow : " + selectedRow);
					if(selectedRow < 0) return;
					
					FloorProperty floorProperty = searchFloorProperty(selectedRow);
					DefaultTableModel model = (DefaultTableModel) table.getModel();
					
					model.removeRow(selectedRow);
					project.deleteFloorProperty(floorProperty);
					
					updateTableModel();
				}
			});
			btnDelete.setBounds(438, 197, 97, 23);
			contentPanel.add(btnDelete);
		}
		{
			JButton btnNew = new JButton("New");
			btnNew.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					FloorPropertyDialog dialog = new FloorPropertyDialog(parent, project, null);
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setModal(true);
					dialog.setVisible(true);
					
					updateTableModel();
				}
			});
			btnNew.setBounds(329, 197, 97, 23);
			contentPanel.add(btnNew);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 230, 656, 33);
			contentPanel.add(buttonPane);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(isImportable()) {
							mergeProjectWithTarget();
							parent.comboBoxFloorRefresh();
						}
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
		updateTableModel();
	}
	private JTable getTable() {
		if (table == null) {
			table = new JTable();
			table.setCellSelectionEnabled(true);			
			table.setModel(new DefaultTableModel(
				new Object[][] {
					{null, null, null, null, null, null, null},
				},
				new String[] {
					"Floor level", "LowerCorner", "UpperCorner", "GroundHeight", "CeilingHeight", "FloorPlanPath", ""
				}
			) {
				Class[] columnTypes = new Class[] {
					String.class, String.class, String.class, Double.class, Object.class, String.class, String.class
				};
				public Class getColumnClass(int columnIndex) {
					return columnTypes[columnIndex];
				}
			});
			table.getColumnModel().getColumn(0).setPreferredWidth(71);
			table.getColumnModel().getColumn(1).setPreferredWidth(87);
			table.getColumnModel().getColumn(2).setPreferredWidth(85);
			table.getColumnModel().getColumn(3).setPreferredWidth(91);
			table.getColumnModel().getColumn(4).setPreferredWidth(90);
			table.getColumnModel().getColumn(5).setPreferredWidth(100);
			table.getColumnModel().getColumn(6).setPreferredWidth(138);
			table.setBounds(0, 0, 437, 16);
			
			updateTableModel();
			parent.comboBoxFloorRefresh();
			
		}
		return table;
	}
	private JButton getBtnEdit() {
		if (btnEdit == null) {
			btnEdit = new JButton("Edit");
			btnEdit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int selectedRow = table.getSelectedRow();
					System.out.println("selectedRow : " + selectedRow);
					if(selectedRow < 0) return;
										
					FloorProperty floorProperty = searchFloorProperty(selectedRow);
					String beforeKey = floorProperty.getLevel();
					
					FloorPropertyDialog dialog = new FloorPropertyDialog(parent, project, floorProperty);
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setModal(true);
					dialog.setVisible(true);
					
					updateProjectMap(beforeKey, floorProperty);
					updateTableModel();
				}
			});
			btnEdit.setBounds(547, 197, 97, 23);
		}
		return btnEdit;
	}
	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setBounds(0, 0, 656, 187);
		}
		return scrollPane;
	}
	private void updateProjectMap(String beforeKey, FloorProperty floorProperty) {
		projectMap.remove(beforeKey);
		projectMap.put(floorProperty.getLevel(), floorProperty);
	}
	private void updateTableModel() {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		
		int rowCount = model.getRowCount();
		for(int i = 0; i < rowCount; i++) {
			model.removeRow(rowCount - i - 1);
		}
		
		System.out.println(model.getRowCount());
		ArrayList<FloorProperty> floorProperties = project.getBuildingProperty().getFloorProperties();
		for(FloorProperty floorProperty : floorProperties) {
			model.addRow(new Object[]{floorProperty.getLevel(), floorProperty.getBottomLeftPoint().toString(), floorProperty.getTopRightPoint().toString(), 
					floorProperty.getGroundHeight(), floorProperty.getCeilingHeight(), floorProperty.getFloorPlanPath(), ""});
		}
		
		isImportable();
	}
	private FloorProperty searchFloorProperty(int selectedRow) {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		String level = (String) model.getValueAt(selectedRow, 0);
		
		FloorProperty floorProperty = projectMap.get(level);
		/*
		String lowerCorner = (String) model.getValueAt(selectedRow, 1);
		String upperCorner = (String) model.getValueAt(selectedRow, 2);
		double height = (double) model.getValueAt(selectedRow, 3);
		String floorPlanPath = (String) model.getValueAt(selectedRow, 4);
		
		ArrayList<FloorProperty> floorPropertyList = project.getBuildingProperty().getFloorProperties();
		for(FloorProperty floorProperty : floorPropertyList) {
			if(floorProperty.getLevel().equals(level) && floorProperty.getBottomLeftPoint().toString().equals(lowerCorner) &&
					floorProperty.getTopRightPoint().toString().equals(upperCorner) && floorProperty.getHeight() == height && 
					floorProperty.getFloorPlanPath().equals(floorPlanPath)) {
				return floorProperty;
			}
		}
		*/
		return floorProperty;
	}
	private int searchModelRow(FloorProperty floorProperty) {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		String level = floorProperty.getLevel();
		int rowCount = model.getRowCount();
		
		for(int row = 0; row < rowCount; row++) {
			if(level.equals(model.getValueAt(row, 0))) {
				return row;
			}
		}
		
		return -1;
	}
	private boolean isImportable() {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		boolean importable = true;
		
		ArrayList<FloorProperty> floorPropertyList = project.getBuildingProperty().getFloorProperties();
		for(FloorProperty floorProperty : floorPropertyList) {
			FloorProperty search = targetMap.get(floorProperty.getLevel());
			
			if(search != null) {
				importable = false;
				
				int row = searchModelRow(floorProperty);
				model.setValueAt("duplicated level name", row, 5);
			}
		}
		
		return importable;
	}
	private void mergeProjectWithTarget() {
		// �� �̸��� ��ġ�� ���� data������ �������� �ʵ��� ��
		// State, Transition, (CellSpace)�� ���� ������ gml id�� ���� �����ϱ� ������ ���ĵ� �������.
		// ��ĥ ������ ������ ���� �� ������ ������ ���� ��ġ�� ���� ����
		// MultiLayeredGraph, SpaceLayer, Nodes, Edges, InterEdges �� ���� �����Ϳ� �׳� ��ġ������
		// MultiLayeredGraph, Spacelayer, Nodes, Edges, InterEdges �� gmlID�� ��ĥ �� �״�� ��ĥ��, ���ο� ��ü�� ��ĥ�� ���ϵ��� �ؾ���
		
		target.getBuildingProperty().getFloorProperties().addAll(project.getBuildingProperty().getFloorProperties());
		target.getCurrentNodes().getStateOnFloors().addAll(project.getCurrentNodes().getStateOnFloors());
		target.getCurrentEdges().getTransitionOnFloors().addAll(project.getCurrentEdges().getTransitionOnFloors());		
	}
}
