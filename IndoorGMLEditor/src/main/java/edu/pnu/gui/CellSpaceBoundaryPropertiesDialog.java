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
package edu.pnu.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * @author Donguk Seo
 *
 */
public class CellSpaceBoundaryPropertiesDialog extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JLabel lblId;
    private JLabel lblName;
    private JLabel lblDescription;
    private JLabel lblDuality;
    private JTextField textField_ID;
    private JTextField textField_Name;
    private JTextField textField_Description;
    private JTextField textField_Duality;
    private JLabel lblCeilingHeight;
    private JTextField textField_Ceiling;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            CellSpaceBoundaryPropertiesDialog dialog = new CellSpaceBoundaryPropertiesDialog();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the dialog.
     */
    public CellSpaceBoundaryPropertiesDialog() {
        setTitle("Properties");
        setBounds(100, 100, 261, 211);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);
        contentPanel.add(getLblId());
        contentPanel.add(getLblName());
        contentPanel.add(getLblDescription());
        contentPanel.add(getLblDuality());
        contentPanel.add(getTextField_ID());
        contentPanel.add(getTextField_Name());
        contentPanel.add(getTextField_Description());
        contentPanel.add(getTextField_Duality());
        contentPanel.add(getLblCeilingHeight());
        contentPanel.add(getTextField_Ceiling());
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okButton = new JButton("OK");
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
    private JLabel getLblId() {
        if (lblId == null) {
        	lblId = new JLabel("ID");
        	lblId.setBounds(12, 10, 57, 15);
        }
        return lblId;
    }
    private JLabel getLblName() {
        if (lblName == null) {
        	lblName = new JLabel("Name");
        	lblName.setBounds(12, 35, 57, 15);
        }
        return lblName;
    }
    private JLabel getLblDescription() {
        if (lblDescription == null) {
        	lblDescription = new JLabel("Description");
        	lblDescription.setBounds(12, 60, 73, 15);
        }
        return lblDescription;
    }
    private JLabel getLblDuality() {
        if (lblDuality == null) {
        	lblDuality = new JLabel("Duality");
        	lblDuality.setBounds(12, 110, 57, 15);
        }
        return lblDuality;
    }
    private JTextField getTextField_ID() {
        if (textField_ID == null) {
        	textField_ID = new JTextField();
        	textField_ID.setBounds(112, 7, 116, 21);
        	textField_ID.setColumns(10);
        }
        return textField_ID;
    }
    private JTextField getTextField_Name() {
        if (textField_Name == null) {
        	textField_Name = new JTextField();
        	textField_Name.setBounds(112, 32, 116, 21);
        	textField_Name.setColumns(10);
        }
        return textField_Name;
    }
    private JTextField getTextField_Description() {
        if (textField_Description == null) {
        	textField_Description = new JTextField();
        	textField_Description.setBounds(112, 57, 116, 21);
        	textField_Description.setColumns(10);
        }
        return textField_Description;
    }
    private JTextField getTextField_Duality() {
        if (textField_Duality == null) {
        	textField_Duality = new JTextField();
        	textField_Duality.setEditable(false);
        	textField_Duality.setBounds(112, 107, 116, 21);
        	textField_Duality.setColumns(10);
        }
        return textField_Duality;
    }
    private JLabel getLblCeilingHeight() {
        if (lblCeilingHeight == null) {
        	lblCeilingHeight = new JLabel("Ceiling Height");
        	lblCeilingHeight.setBounds(12, 85, 84, 15);
        }
        return lblCeilingHeight;
    }
    private JTextField getTextField_Ceiling() {
        if (textField_Ceiling == null) {
        	textField_Ceiling = new JTextField();
        	textField_Ceiling.setBounds(112, 82, 116, 21);
        	textField_Ceiling.setColumns(10);
        }
        return textField_Ceiling;
    }
}
