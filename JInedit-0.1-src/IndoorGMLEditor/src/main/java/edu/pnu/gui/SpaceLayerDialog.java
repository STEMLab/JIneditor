package edu.pnu.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import net.opengis.indoorgml.core.SpaceLayer;
import edu.pnu.project.ProjectFile;

public class SpaceLayerDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JLabel lblGmlId;
	private JTextField textField_gmlid;
	private JLabel lblName;
	private JTextField textField_name;

	/**
	 * Launch the application.
	 *//*
	public static void main(String[] args) {
		try {
			SpaceLayerDialog dialog = new SpaceLayerDialog();
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
	public SpaceLayerDialog(MainFrame parent, ProjectFile project, SpaceLayer spaceLayer) {
		setBounds(100, 100, 280, 172);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		contentPanel.add(getLblGmlId());
		contentPanel.add(getTextField_gmlid());
		contentPanel.add(getLblName());
		contentPanel.add(getTextField_name());
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String gmlID = textField_gmlid.getText();
						String name = textField_name.getText();
						
						if(spaceLayer != null) {
							spaceLayer.setGmlID(gmlID);
							spaceLayer.setName(name);
							
							parent.comboBoxSpaceLayerRefresh();
							
							dispose();
							return;
						}
						
						ArrayList<SpaceLayer> spaceLayerMember = project.getCurrentSpaceLayers().getSpaceLayerMember();
						for(SpaceLayer spaceLayer : spaceLayerMember) {
							if(spaceLayer.getGmlID().equalsIgnoreCase(gmlID)) {
								System.out.println("duplicated gmlid");
								return;
							}
						}
						
						SpaceLayer spaceLayer = new SpaceLayer();
						spaceLayer.setGmlID(gmlID);
						spaceLayer.setName(name);
						
						spaceLayerMember.add(spaceLayer);
						parent.comboBoxSpaceLayerRefresh();
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
		
		if(spaceLayer != null) {
			textField_gmlid.setText(String.valueOf(spaceLayer.getGmlID()));
			textField_name.setText(String.valueOf(spaceLayer.getName()));
		}
	}
	private JLabel getLblGmlId() {
		if (lblGmlId == null) {
			lblGmlId = new JLabel("GML ID");
			lblGmlId.setBounds(12, 10, 57, 15);
		}
		return lblGmlId;
	}
	private JTextField getTextField_gmlid() {
		if (textField_gmlid == null) {
			textField_gmlid = new JTextField();
			textField_gmlid.setBounds(81, 7, 116, 21);
			textField_gmlid.setColumns(10);
		}
		return textField_gmlid;
	}
	private JLabel getLblName() {
		if (lblName == null) {
			lblName = new JLabel("Name");
			lblName.setBounds(12, 31, 57, 15);
		}
		return lblName;
	}
	private JTextField getTextField_name() {
		if (textField_name == null) {
			textField_name = new JTextField();
			textField_name.setBounds(81, 28, 116, 21);
			textField_name.setColumns(10);
		}
		return textField_name;
	}
}
