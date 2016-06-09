package edu.pnu.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;
import javax.xml.bind.JAXBException;

import edu.pnu.importexport.IndoorGMLExporter;
import edu.pnu.project.ProjectFile;
import edu.pnu.util.IndoorGML3DGeometryBuilder;

public class GeometryExportDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private CanvasPanel panel = null;
	private ProjectFile project = null;
	private JRadioButton rdbtn2D = null;
	private JRadioButton rdbtn3D = null;

	/**
	 * Launch the application.
	 */
	/*
	public static void main(String[] args) {
		try {
			GeometryExportDialog dialog = new GeometryExportDialog();
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
	public GeometryExportDialog(CanvasPanel panel, ProjectFile project) {
	    setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.project = project;
		this.panel = panel;
		
		setTitle("GeometryType");
		setBounds(100, 100, 170, 150);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			rdbtn2D = new JRadioButton("2D");
			rdbtn2D.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					rdbtn2D.setSelected(true);
					rdbtn3D.setSelected(false);
				}
			});
			contentPanel.add(rdbtn2D, BorderLayout.NORTH);
		}
		{
			rdbtn3D = new JRadioButton("3D (Extrude)");
			rdbtn3D.setSelected(true);
			rdbtn3D.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					rdbtn2D.setSelected(false);
					rdbtn3D.setSelected(true);
				}
			});
			
			contentPanel.add(rdbtn3D, BorderLayout.CENTER);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(rdbtn3D.isSelected()) {
						//	IndoorGML3DGeometryBuilder builder
							project.setIs3DGeometry(true);
							IndoorGML3DGeometryBuilder builder = new IndoorGML3DGeometryBuilder(panel, project.getIndoorFeatures());
							builder.create3DGeometry();
						} else {
							project.setIs3DGeometry(false);
						}
						
						IndoorGMLExporter exporter = new IndoorGMLExporter(project);
						try {
							exporter.export();
						} catch (JAXBException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}

}
