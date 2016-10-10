package edu.pnu.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import net.opengis.citygml.building.AbstractBuilding;

public class CityGMLAbstractBuildingAttributeDialog extends JDialog {
	private AbstractBuilding building;
	private Map<String, AbstractBuilding> buildingMap;

	private final JPanel contentPanel = new JPanel();
	private JLabel lblGmlId;
	private JTextField textField_GMLID;
	private JTextField textField_GMLName;
	private JTextField textField_Class;
	private JTextField textField_Function;
	private JTextField textField_Usage;
	private JTextField textField_YearOfConstruction;
	private JTextField textField_YearOfDemolition;
	private JTextField textField_RoofType;
	private JTextField textField_MeasureHeight;
	private JTextField textField_StoreysAboveGround;
	private JTextField textField_StoreysBelowGround;
	private JTextField textField_StoreysHeightsAboveGround;
	private JTextField textField_StoreyHeightsBelowGround;
	private JComboBox comboBox_BuildingType;
	private JLabel lblParentBuilding;
	private JComboBox comboBox_ParentBuilding;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			CityGMLAbstractBuildingAttributeDialog dialog = new CityGMLAbstractBuildingAttributeDialog(new AbstractBuilding(), null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public CityGMLAbstractBuildingAttributeDialog(AbstractBuilding target, Map<String, AbstractBuilding> buildingMap) {
		this.building = target;
		this.buildingMap = buildingMap;
		
		setTitle("AbstractBuilding attributes");
		setBounds(100, 100, 337, 473);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		contentPanel.add(getLblGmlId());
		contentPanel.add(getTextField_GMLID());
		{
			JLabel lblGmlName = new JLabel("GML Name");
			lblGmlName.setBounds(12, 98, 163, 15);
			contentPanel.add(lblGmlName);
		}
		{
			textField_GMLName = new JTextField();
			textField_GMLName.setBounds(187, 95, 116, 21);
			contentPanel.add(textField_GMLName);
			textField_GMLName.setColumns(10);
		}
		{
			JLabel lblMeasureheight = new JLabel("MeasureHeight");
			lblMeasureheight.setBounds(12, 272, 163, 15);
			contentPanel.add(lblMeasureheight);
		}
		{
			JLabel lblStoreysaboveground = new JLabel("StoreysAboveGround");
			lblStoreysaboveground.setBounds(12, 297, 163, 15);
			contentPanel.add(lblStoreysaboveground);
		}
		{
			JLabel lblStoreyheightsaboveground = new JLabel("StoreyHeightsAboveGround");
			lblStoreyheightsaboveground.setBounds(12, 349, 163, 15);
			contentPanel.add(lblStoreyheightsaboveground);
		}
		{
			JLabel lblClass = new JLabel("Class");
			lblClass.setBounds(12, 123, 163, 15);
			contentPanel.add(lblClass);
		}
		{
			JLabel lblFunction = new JLabel("Function");
			lblFunction.setBounds(12, 148, 163, 15);
			contentPanel.add(lblFunction);
		}
		{
			JLabel lblUsage = new JLabel("Usage");
			lblUsage.setBounds(12, 173, 163, 15);
			contentPanel.add(lblUsage);
		}
		{
			JLabel lblYearofconstruction = new JLabel("YearOfConstruction");
			lblYearofconstruction.setBounds(12, 198, 163, 15);
			contentPanel.add(lblYearofconstruction);
		}
		{
			JLabel lblYearofdemolition = new JLabel("YearOfDemolition");
			lblYearofdemolition.setBounds(12, 222, 163, 15);
			contentPanel.add(lblYearofdemolition);
		}
		{
			JLabel lblRooftype = new JLabel("RoofType");
			lblRooftype.setBounds(12, 247, 163, 15);
			contentPanel.add(lblRooftype);
		}
		{
			JLabel lblStoreysbelowground = new JLabel("StoreysBelowGround");
			lblStoreysbelowground.setBounds(12, 322, 163, 15);
			contentPanel.add(lblStoreysbelowground);
		}
		{
			JLabel lblStoreyheightsbelowground = new JLabel("StoreyHeightsBelowGround");
			lblStoreyheightsbelowground.setBounds(12, 374, 163, 15);
			contentPanel.add(lblStoreyheightsbelowground);
		}
		{
			textField_Class = new JTextField();
			textField_Class.setBounds(187, 120, 116, 21);
			contentPanel.add(textField_Class);
			textField_Class.setColumns(10);
		}
		{
			textField_Function = new JTextField();
			textField_Function.setBounds(187, 145, 116, 21);
			contentPanel.add(textField_Function);
			textField_Function.setColumns(10);
		}
		{
			textField_Usage = new JTextField();
			textField_Usage.setBounds(187, 170, 116, 21);
			contentPanel.add(textField_Usage);
			textField_Usage.setColumns(10);
		}
		{
			textField_YearOfConstruction = new JTextField();
			textField_YearOfConstruction.setBounds(187, 195, 116, 21);
			contentPanel.add(textField_YearOfConstruction);
			textField_YearOfConstruction.setColumns(10);
		}
		{
			textField_YearOfDemolition = new JTextField();
			textField_YearOfDemolition.setBounds(187, 219, 116, 21);
			contentPanel.add(textField_YearOfDemolition);
			textField_YearOfDemolition.setColumns(10);
		}
		{
			textField_RoofType = new JTextField();
			textField_RoofType.setBounds(187, 244, 116, 21);
			contentPanel.add(textField_RoofType);
			textField_RoofType.setColumns(10);
		}
		{
			textField_MeasureHeight = new JTextField();
			textField_MeasureHeight.setBounds(187, 269, 116, 21);
			contentPanel.add(textField_MeasureHeight);
			textField_MeasureHeight.setColumns(10);
		}
		{
			textField_StoreysAboveGround = new JTextField();
			textField_StoreysAboveGround.setBounds(187, 294, 116, 21);
			contentPanel.add(textField_StoreysAboveGround);
			textField_StoreysAboveGround.setColumns(10);
		}
		{
			textField_StoreysBelowGround = new JTextField();
			textField_StoreysBelowGround.setBounds(187, 319, 116, 21);
			contentPanel.add(textField_StoreysBelowGround);
			textField_StoreysBelowGround.setColumns(10);
		}
		{
			textField_StoreysHeightsAboveGround = new JTextField();
			textField_StoreysHeightsAboveGround.setBounds(187, 346, 116, 21);
			contentPanel.add(textField_StoreysHeightsAboveGround);
			textField_StoreysHeightsAboveGround.setColumns(10);
		}
		{
			textField_StoreyHeightsBelowGround = new JTextField();
			textField_StoreyHeightsBelowGround.setBounds(187, 371, 116, 21);
			contentPanel.add(textField_StoreyHeightsBelowGround);
			textField_StoreyHeightsBelowGround.setColumns(10);
		}
		{
			JLabel lblBuildingType = new JLabel("Building Type");
			lblBuildingType.setBounds(12, 10, 163, 15);
			contentPanel.add(lblBuildingType);
		}
		contentPanel.add(getComboBox_BuildingType());
		contentPanel.add(getLblParentBuilding());
		contentPanel.add(getComboBox_ParentBuilding());
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (textField_GMLID.getText().equals("")) {
							return;
						}
						
						if (building == null) {
							building = new AbstractBuilding();
						}
						
						String type = getBuildingType();
						building.setType(type);

						if (type.equals("Building")) {
							building.setParent(null);
							building.getBuildingParts().clear();
						} else if (type.equals("BuildingPart")) {
							AbstractBuilding parent = getParentBuilding();
							doBuildingPartConsistency(parent, building);
						}
						
						building.setGmlID(textField_GMLID.getText());						
						if (!textField_GMLName.getText().equals("")) {
							building.setGmlName(textField_GMLName.getText());
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
		
		if (building != null) {
			setBuildingAttributes(building);
		}
	}
	private JLabel getLblGmlId() {
		if (lblGmlId == null) {
			lblGmlId = new JLabel("GML ID");
			lblGmlId.setBounds(12, 73, 163, 15);
		}
		return lblGmlId;
	}
	private JTextField getTextField_GMLID() {
		if (textField_GMLID == null) {
			textField_GMLID = new JTextField();
			textField_GMLID.setBounds(187, 70, 116, 21);
			textField_GMLID.setColumns(10);
		}
		return textField_GMLID;
	}
	private JComboBox getComboBox_BuildingType() {
		if (comboBox_BuildingType == null) {
			comboBox_BuildingType = new JComboBox();
			comboBox_BuildingType.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					toggleComboBoxParentBuilding();
				}
			});
			comboBox_BuildingType.setModel(new DefaultComboBoxModel(new String[] {"Building", "BuildingPart"}));
			comboBox_BuildingType.setBounds(187, 7, 116, 21);
			
			//comboBox_BuildingType.setSelectedIndex(0);
		}
		return comboBox_BuildingType;
	}
	private JLabel getLblParentBuilding() {
		if (lblParentBuilding == null) {
			lblParentBuilding = new JLabel("Parent Building");
			lblParentBuilding.setBounds(12, 35, 163, 15);
		}
		return lblParentBuilding;
	}
	private JComboBox getComboBox_ParentBuilding() {
		if (comboBox_ParentBuilding == null) {
			comboBox_ParentBuilding = new JComboBox();
			comboBox_ParentBuilding.setEnabled(false);
			comboBox_ParentBuilding.setBounds(187, 32, 116, 21);
			
			updateComboBoxParentBuildingModel();
		}
		return comboBox_ParentBuilding;
	}
	
	public AbstractBuilding getBuilding() {
		return building;
	}
	private String getBuildingType() {
		DefaultComboBoxModel model = (DefaultComboBoxModel) comboBox_BuildingType.getModel();
		String type = (String) model.getSelectedItem();
		
		return type;
	}
	private void setBuildingAttributes(AbstractBuilding building) {
		comboBox_BuildingType.setSelectedItem(building.getType());
		toggleComboBoxParentBuilding();
		
		textField_GMLID.setText(building.getGmlID());
		
		String name = building.getGmlName();
		if (name != null) {
			textField_GMLName.setText(name);
		}
	}
	private void toggleComboBoxParentBuilding() {
		String type = (String) comboBox_BuildingType.getSelectedItem();
		if (type == null) return;
		
		if (type.equals("Building")) {
			comboBox_ParentBuilding.setEnabled(false);
		} else if (type.equals("BuildingPart")) {
			comboBox_ParentBuilding.setEnabled(true);
		}
	}
	private void updateComboBoxParentBuildingModel() {
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
        	AbstractBuilding b = it.next();
        	if (!b.getType().equals("Building")) {
        		continue;
        	}
        	
        	items[i++] = b.getGmlID();
        }
        
        DefaultComboBoxModel model = new DefaultComboBoxModel(items);
        comboBox_ParentBuilding.setModel(model);
        
        if (building != null && building.getType().equals("BuildingPart")) {
        	AbstractBuilding parent = building.getParent();
        	if (parent != null && buildingMap.containsKey(parent.getGmlID())) {
        		comboBox_ParentBuilding.setSelectedItem(building.getParent().getGmlID());
        	}
        }
    }
	private AbstractBuilding getParentBuilding() {
		DefaultComboBoxModel model = (DefaultComboBoxModel) comboBox_ParentBuilding.getModel();
		String id = (String) model.getSelectedItem();
		
		AbstractBuilding parent = buildingMap.get(id);
		return parent;
	}
	private void doBuildingPartConsistency(AbstractBuilding parent, AbstractBuilding buildingPart) {
		List<AbstractBuilding> buildings = new ArrayList<AbstractBuilding>(buildingMap.values());
		for (AbstractBuilding b : buildings) {
			if (b.getType().equals("Building")) {
				if (b.getBuildingParts().contains(buildingPart)) {
					b.getBuildingParts().remove(buildingPart);
				}
			}
		}
		
		parent.getBuildingParts().add(buildingPart);
		buildingPart.setParent(parent);
	}
}
