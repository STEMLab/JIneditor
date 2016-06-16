package edu.pnu.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import net.opengis.indoorgml.core.CellSpaceBoundary;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import edu.pnu.project.BoundaryType;

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

    private CellSpaceBoundary cellSpaceBoundary;
    private JLabel lblUsage;
    private JComboBox comboBox_Usage;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
    	try {
    		CellSpaceBoundaryPropertiesDialog dialog = new CellSpaceBoundaryPropertiesDialog(new CellSpaceBoundary());
    		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    		dialog.setVisible(true);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    /**
     * Create the dialog.
     */
    public CellSpaceBoundaryPropertiesDialog(CellSpaceBoundary cellSpaceBoundary) {
        this.cellSpaceBoundary = cellSpaceBoundary;

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
        contentPanel.add(getLblUsage());
        contentPanel.add(getComboBox_Usage());
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okButton = new JButton("OK");
                okButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (textField_ID.getText() != null) {
                            cellSpaceBoundary.setGmlID(textField_ID.getText());
                        }
                        if (textField_Name.getText() != null) {
                            cellSpaceBoundary.setName(textField_Name.getText());
                        }
                        if (textField_Description.getText() != null) {
                            cellSpaceBoundary.setDescription(textField_Description.getText());
                        }
                        if (((String) comboBox_Usage.getSelectedItem()).equals("Boundary")) {
                        	cellSpaceBoundary.setBoundaryType(BoundaryType.CellSpaceBoundary);
                        } else {
                        	cellSpaceBoundary.setBoundaryType(BoundaryType.Door);
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
            textField_ID.setBounds(112, 7, 116, 21);
            textField_ID.setColumns(10);
        }
        textField_ID.setText(cellSpaceBoundary.getGmlID());
        return textField_ID;
    }

    private JTextField getTextField_Name() {
        if (textField_Name == null) {
            textField_Name = new JTextField();
            textField_Name.setBounds(112, 32, 116, 21);
            textField_Name.setColumns(10);
        }
        textField_Name.setText(cellSpaceBoundary.getName());
        return textField_Name;
    }

    private JTextField getTextField_Description() {
        if (textField_Description == null) {
            textField_Description = new JTextField();
            textField_Description.setBounds(112, 57, 116, 21);
            textField_Description.setColumns(10);
        }
        textField_Description.setText(cellSpaceBoundary.getDescription("Description"));
        return textField_Description;
    }

    private JTextField getTextField_Duality() {
        if (textField_Duality == null) {
            textField_Duality = new JTextField();
            textField_Duality.setEditable(false);
            textField_Duality.setBounds(112, 82, 116, 21);
            textField_Duality.setColumns(10);
        }
        if(cellSpaceBoundary.getDuality() != null) {
            textField_Duality.setText(cellSpaceBoundary.getDuality().getGmlID());
        }
        return textField_Duality;
    }
	private JLabel getLblUsage() {
		if (lblUsage == null) {
			lblUsage = new JLabel("Usage");
			lblUsage.setBounds(12, 110, 57, 15);
		}
		return lblUsage;
	}
	private JComboBox getComboBox_Usage() {
		if (comboBox_Usage == null) {
			comboBox_Usage = new JComboBox();
			comboBox_Usage.setModel(new DefaultComboBoxModel(new String[] {"Boundary", "Door"}));
			comboBox_Usage.setBounds(112, 107, 116, 21);
		}
		if (cellSpaceBoundary.getBoundaryType() == BoundaryType.CellSpaceBoundary) {
			comboBox_Usage.setSelectedIndex(0);
		} else if (cellSpaceBoundary.getBoundaryType() == BoundaryType.Door) {
			comboBox_Usage.setSelectedIndex(1);
		}
		return comboBox_Usage;
	}
}
