package edu.pnu.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import net.opengis.indoorgml.core.SpaceLayer;
import edu.pnu.project.FloorProperty;
import edu.pnu.project.ProjectFile;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SpaceLayerListDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JScrollPane scrollPane;
	private JTable table;

	private MainFrame parent;
	private ProjectFile project;
	
	private HashMap<String, SpaceLayer> spaceLayerMap;
	private JButton btnNew;
	private JButton btnDelete;
	private JButton btnEdit;
	/**
	 * Launch the application.
	 *//*
	public static void main(String[] args) {
		try {
			LayerListDialog dialog = new LayerListDialog();
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
	public SpaceLayerListDialog(MainFrame parent, ProjectFile project) {
		this.parent = parent;
		this.project = project;
		
		spaceLayerMap = new HashMap<String, SpaceLayer>();
		ArrayList<SpaceLayer> spaceLayerMember = project.getCurrentSpaceLayers().getSpaceLayerMember();
		for(SpaceLayer spaceLayer: spaceLayerMember) {
			spaceLayerMap.put(spaceLayer.getGmlID(), spaceLayer);
		}
		
		setBounds(100, 100, 398, 256);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		contentPanel.add(getScrollPane());
		getScrollPane().setViewportView(getTable());
		contentPanel.add(getBtnNew());
		contentPanel.add(getBtnDelete());
		contentPanel.add(getBtnEdit());
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
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setBounds(0, 0, 382, 143);
		}
		return scrollPane;
	}
	private JTable getTable() {
		if (table == null) {
			table = new JTable();
			table.setCellSelectionEnabled(true);			
			table.setModel(new DefaultTableModel(
				new Object[][] {
					{null, null, null},
				},
				new String[] {
					"GML ID", "Name", ""
				}
			) {
				Class[] columnTypes = new Class[] {
					String.class, String.class, String.class
				};
				public Class getColumnClass(int columnIndex) {
					return columnTypes[columnIndex];
				}
			});
			table.getColumnModel().getColumn(0).setPreferredWidth(55);
			table.getColumnModel().getColumn(0).setMinWidth(55);
			table.getColumnModel().getColumn(1).setPreferredWidth(132);
			table.getColumnModel().getColumn(2).setPreferredWidth(208);
			table.setBounds(0, 0, 437, 16);
			
			updateTableModel();
			parent.comboBoxFloorRefresh();
			
		}
		return table;
	}
	
	private JButton getBtnNew() {
		if (btnNew == null) {
			btnNew = new JButton("New");
			btnNew.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					/*
					SpaceLayerDialog dialog = new SpaceLayerDialog(parent, project, null);
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setModal(true);
					dialog.setVisible(true);
					*/
					SpaceLayer spaceLayer = new SpaceLayer();
					project.getCurrentSpaceLayers().getSpaceLayerMember().add(spaceLayer);
					
					updateSpaceLayerMap(null, spaceLayer);
					updateTableModel();
					parent.comboBoxSpaceLayerRefresh();
				}
			});
			btnNew.setBounds(154, 153, 67, 23);
		}
		return btnNew;
	}
	private JButton getBtnDelete() {
		if (btnDelete == null) {
			btnDelete = new JButton("Delete");
			btnDelete.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int selectedRow = table.getSelectedRow();
					System.out.println("selectedRow : " + selectedRow);
					if(selectedRow < 0) return;
					
					SpaceLayer spaceLayer = searchSpaceLayer(selectedRow);
					DefaultTableModel model = (DefaultTableModel) table.getModel();
					
					model.removeRow(selectedRow);
					project.getCurrentSpaceLayers().getSpaceLayerMember().remove(spaceLayer);
					
					updateSpaceLayerMap(spaceLayer.getGmlID(), null);
					updateTableModel();
					parent.comboBoxSpaceLayerRefresh();
				}
			});
			btnDelete.setBounds(233, 153, 67, 23);
		}
		return btnDelete;
	}
	private JButton getBtnEdit() {
		if (btnEdit == null) {
			btnEdit = new JButton("Edit");
			btnEdit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int selectedRow = table.getSelectedRow();
					System.out.println("selectedRow : " + selectedRow);
					if(selectedRow < 0) return;
										
					SpaceLayer spaceLayer = searchSpaceLayer(selectedRow);
					String beforeKey = spaceLayer.getGmlID();
					
					SpaceLayerDialog dialog = new SpaceLayerDialog(parent, project, spaceLayer);
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setModal(true);
					dialog.setVisible(true);
					
					updateSpaceLayerMap(beforeKey, spaceLayer);
					updateTableModel();
					parent.comboBoxFloorRefresh();
				}
			});
			btnEdit.setBounds(312, 153, 58, 23);
		}
		return btnEdit;
	}
	private void updateSpaceLayerMap(String beforeKey, SpaceLayer spaceLayer) {
		if(beforeKey != null) spaceLayerMap.remove(beforeKey);
		if(spaceLayer != null) spaceLayerMap.put(spaceLayer.getGmlID(), spaceLayer);
	}
	private void updateTableModel() {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		
		int rowCount = model.getRowCount();
		for(int i = 0; i < rowCount; i++) {
			model.removeRow(rowCount - i - 1);
		}
		
		System.out.println(model.getRowCount());
		ArrayList<SpaceLayer> spaceLayerMember = project.getCurrentSpaceLayers().getSpaceLayerMember();
		for(SpaceLayer spaceLayer : spaceLayerMember) {
			model.addRow(new Object[]{spaceLayer.getGmlID(), spaceLayer.getName(), null});
		}
	}
	private SpaceLayer searchSpaceLayer(int selectedRow) {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		String gmlID = (String) model.getValueAt(selectedRow, 0);
		
		SpaceLayer spaceLayer = spaceLayerMap.get(gmlID);
		
		return spaceLayer;
	}
}
