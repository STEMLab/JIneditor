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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import net.opengis.indoorgml.core.State;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * @author Donguk Seo
 *
 */
public class StatePropertiesDialog extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JLabel lblId;
    private JLabel lblName;
    private JLabel lblDescription;
    private JLabel lblDuality;
    private JTextField textField_ID;
    private JTextField textField_Name;
    private JTextField textField_Description;
    private JTextField textField_Duality;
    
    private State state;

    /**
     * Launch the application.
     */
    /*public static void main(String[] args) {
        try {
            StatePropertiesDialog dialog = new StatePropertiesDialog();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    /**
     * Create the dialog.
     */
    public StatePropertiesDialog(State state) {
        this.state = state;
        
        setTitle("Properties");
        setBounds(100, 100, 240, 191);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);
        contentPanel.add(getLblId());
        contentPanel.add(getLblName());
        contentPanel.add(getLblDescription());
        //contentPanel.add(getLblDuality());
        contentPanel.add(getTextField_ID());
        contentPanel.add(getTextField_Name());
        contentPanel.add(getTextField_Description());
        //contentPanel.add(getTextField_Duality());
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okButton = new JButton("OK");
                okButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                            if(textField_ID.getText() != null) {
                                    state.setGmlID(textField_ID.getText());
                            }
                            if(textField_Name.getText() != null) {
                                    state.setName(textField_Name.getText());
                            }
                            if(textField_Description.getText() != null) {
                                    state.setDescription(textField_Description.getText());
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
        	lblDuality.setBounds(12, 85, 57, 15);
        }
        return lblDuality;
    }
    private JTextField getTextField_ID() {
        if (textField_ID == null) {
        	textField_ID = new JTextField();
        	textField_ID.setBounds(91, 7, 116, 21);
        	textField_ID.setColumns(10);
        }
        textField_ID.setText(state.getGmlID());
        return textField_ID;
    }
    private JTextField getTextField_Name() {
        if (textField_Name == null) {
        	textField_Name = new JTextField();
        	textField_Name.setBounds(91, 32, 116, 21);
        	textField_Name.setColumns(10);
        }
        textField_Name.setText(state.getName());
        return textField_Name;
    }
    private JTextField getTextField_Description() {
        if (textField_Description == null) {
        	textField_Description = new JTextField();
        	textField_Description.setBounds(91, 57, 116, 21);
        	textField_Description.setColumns(10);
        }
        textField_Description.setText(state.getDescription());
        return textField_Description;
    }
    private JTextField getTextField_Duality() {
        if (textField_Duality == null) {
        	textField_Duality = new JTextField();
        	textField_Duality.setEditable(false);
        	textField_Duality.setBounds(91, 82, 116, 21);
        	textField_Duality.setColumns(10);
        }
        if(state.getDuality() != null) {
            textField_Duality.setText(state.getDuality().getGmlID());
        }
        return textField_Duality;
    }
}
