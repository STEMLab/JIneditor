package edu.pnu.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.opengis.indoorgml.core.Edges;
import net.opengis.indoorgml.core.MultiLayeredGraph;
import net.opengis.indoorgml.core.Nodes;
import net.opengis.indoorgml.core.SpaceLayer;
import net.opengis.indoorgml.core.SpaceLayers;
import net.opengis.indoorgml.geometry.Point;
import edu.pnu.project.FloorProperty;
import edu.pnu.project.ProjectFile;
import edu.pnu.project.StateOnFloor;
import edu.pnu.project.TransitionOnFloor;

public class FloorPropertyDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField_Level;
	private JTextField textField_BottomLeftPointX;
	private JTextField textField_BottomLeftPointY;
	private JTextField textField_TopRightPointX;
	private JTextField textField_TopRightPointY;
	private JTextField textField_GroundHeight;
	private JTextField textField_FloorPlanPath;
	private JLabel lblCeilingheight;
	private JTextField textField_CeilingHeight;

	/**
	 * Launch the application.
	 */
	/*
	public static void main(String[] args) {
		try {
			FloorPropertyDialog dialog = new FloorPropertyDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	/**
	 * Create the dialog.
	 */
	public FloorPropertyDialog(MainFrame parent, ProjectFile project, FloorProperty floorProperty) {
		
		////////
		setTitle("Floor Properties");
		setBounds(100, 100, 375, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{0, 0, 0, 0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblLevel = new JLabel("Level");
			GridBagConstraints gbc_lblLevel = new GridBagConstraints();
			gbc_lblLevel.insets = new Insets(0, 0, 5, 5);
			gbc_lblLevel.anchor = GridBagConstraints.EAST;
			gbc_lblLevel.gridx = 0;
			gbc_lblLevel.gridy = 0;
			contentPanel.add(lblLevel, gbc_lblLevel);
		}
		{
			textField_Level = new JTextField();
			GridBagConstraints gbc_textField_Level = new GridBagConstraints();
			gbc_textField_Level.insets = new Insets(0, 0, 5, 5);
			gbc_textField_Level.fill = GridBagConstraints.HORIZONTAL;
			gbc_textField_Level.gridx = 1;
			gbc_textField_Level.gridy = 0;
			contentPanel.add(textField_Level, gbc_textField_Level);
			textField_Level.setColumns(10);
		}
		{
			JLabel lblBottomLeftPoint = new JLabel("BottomLeftPoint");
			GridBagConstraints gbc_lblBottomLeftPoint = new GridBagConstraints();
			gbc_lblBottomLeftPoint.anchor = GridBagConstraints.EAST;
			gbc_lblBottomLeftPoint.insets = new Insets(0, 0, 5, 5);
			gbc_lblBottomLeftPoint.gridx = 0;
			gbc_lblBottomLeftPoint.gridy = 1;
			contentPanel.add(lblBottomLeftPoint, gbc_lblBottomLeftPoint);
		}
		{
			textField_BottomLeftPointX = new JTextField();
			GridBagConstraints gbc_textField_BottomLeftPointX = new GridBagConstraints();
			gbc_textField_BottomLeftPointX.insets = new Insets(0, 0, 5, 5);
			gbc_textField_BottomLeftPointX.fill = GridBagConstraints.HORIZONTAL;
			gbc_textField_BottomLeftPointX.gridx = 1;
			gbc_textField_BottomLeftPointX.gridy = 1;
			contentPanel.add(textField_BottomLeftPointX, gbc_textField_BottomLeftPointX);
			textField_BottomLeftPointX.setColumns(10);
		}
		{
			textField_BottomLeftPointY = new JTextField();
			GridBagConstraints gbc_textField_BottomLeftPointY = new GridBagConstraints();
			gbc_textField_BottomLeftPointY.insets = new Insets(0, 0, 5, 5);
			gbc_textField_BottomLeftPointY.gridx = 2;
			gbc_textField_BottomLeftPointY.gridy = 1;
			contentPanel.add(textField_BottomLeftPointY, gbc_textField_BottomLeftPointY);
			textField_BottomLeftPointY.setColumns(10);
		}
		{
			JLabel lblTopRightPoint = new JLabel("TopRightPoint");
			GridBagConstraints gbc_lblTopRightPoint = new GridBagConstraints();
			gbc_lblTopRightPoint.anchor = GridBagConstraints.EAST;
			gbc_lblTopRightPoint.insets = new Insets(0, 0, 5, 5);
			gbc_lblTopRightPoint.gridx = 0;
			gbc_lblTopRightPoint.gridy = 2;
			contentPanel.add(lblTopRightPoint, gbc_lblTopRightPoint);
		}
		{
			textField_TopRightPointX = new JTextField();
			GridBagConstraints gbc_textField_TopRightPointX = new GridBagConstraints();
			gbc_textField_TopRightPointX.insets = new Insets(0, 0, 5, 5);
			gbc_textField_TopRightPointX.fill = GridBagConstraints.HORIZONTAL;
			gbc_textField_TopRightPointX.gridx = 1;
			gbc_textField_TopRightPointX.gridy = 2;
			contentPanel.add(textField_TopRightPointX, gbc_textField_TopRightPointX);
			textField_TopRightPointX.setColumns(10);
		}
		{
			textField_TopRightPointY = new JTextField();
			GridBagConstraints gbc_textField_TopRightPointY = new GridBagConstraints();
			gbc_textField_TopRightPointY.insets = new Insets(0, 0, 5, 5);
			gbc_textField_TopRightPointY.gridx = 2;
			gbc_textField_TopRightPointY.gridy = 2;
			contentPanel.add(textField_TopRightPointY, gbc_textField_TopRightPointY);
			textField_TopRightPointY.setColumns(10);
		}
		{
			JLabel lblGroundHeight = new JLabel("GroundHeight");
			GridBagConstraints gbc_lblGroundHeight = new GridBagConstraints();
			gbc_lblGroundHeight.anchor = GridBagConstraints.EAST;
			gbc_lblGroundHeight.insets = new Insets(0, 0, 5, 5);
			gbc_lblGroundHeight.gridx = 0;
			gbc_lblGroundHeight.gridy = 3;
			contentPanel.add(lblGroundHeight, gbc_lblGroundHeight);
		}
		{
			textField_GroundHeight = new JTextField();
			GridBagConstraints gbc_textField_GroundHeight = new GridBagConstraints();
			gbc_textField_GroundHeight.insets = new Insets(0, 0, 5, 5);
			gbc_textField_GroundHeight.fill = GridBagConstraints.HORIZONTAL;
			gbc_textField_GroundHeight.gridx = 1;
			gbc_textField_GroundHeight.gridy = 3;
			contentPanel.add(textField_GroundHeight, gbc_textField_GroundHeight);
			textField_GroundHeight.setColumns(10);
		}
		{
			lblCeilingheight = new JLabel("CeilingHeight");
			GridBagConstraints gbc_lblCeilingheight = new GridBagConstraints();
			gbc_lblCeilingheight.anchor = GridBagConstraints.EAST;
			gbc_lblCeilingheight.insets = new Insets(0, 0, 5, 5);
			gbc_lblCeilingheight.gridx = 0;
			gbc_lblCeilingheight.gridy = 4;
			contentPanel.add(lblCeilingheight, gbc_lblCeilingheight);
		}
		{
			textField_CeilingHeight = new JTextField();
			GridBagConstraints gbc_textField_CeilingHeight = new GridBagConstraints();
			gbc_textField_CeilingHeight.insets = new Insets(0, 0, 5, 5);
			gbc_textField_CeilingHeight.fill = GridBagConstraints.HORIZONTAL;
			gbc_textField_CeilingHeight.gridx = 1;
			gbc_textField_CeilingHeight.gridy = 4;
			contentPanel.add(textField_CeilingHeight, gbc_textField_CeilingHeight);
			textField_CeilingHeight.setColumns(10);
		}
		{
			JLabel lblFloorPlan = new JLabel("Floor Plan");
			GridBagConstraints gbc_lblFloorPlan = new GridBagConstraints();
			gbc_lblFloorPlan.anchor = GridBagConstraints.EAST;
			gbc_lblFloorPlan.insets = new Insets(0, 0, 5, 5);
			gbc_lblFloorPlan.gridx = 0;
			gbc_lblFloorPlan.gridy = 5;
			contentPanel.add(lblFloorPlan, gbc_lblFloorPlan);
		}
		{
			{
				textField_FloorPlanPath = new JTextField();
				GridBagConstraints gbc_textField_FloorPlanPath = new GridBagConstraints();
				gbc_textField_FloorPlanPath.insets = new Insets(0, 0, 5, 5);
				gbc_textField_FloorPlanPath.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField_FloorPlanPath.gridx = 1;
				gbc_textField_FloorPlanPath.gridy = 5;
				contentPanel.add(textField_FloorPlanPath, gbc_textField_FloorPlanPath);
				textField_FloorPlanPath.setColumns(10);
			}
		}
		JButton btnOpenFile = new JButton("Open File");
		btnOpenFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("image", "jpg", "png", "gif");
		        fileChooser.setFileFilter(filter);
		        
		        int returnVal = fileChooser.showOpenDialog(FloorPropertyDialog.this);
		            if( returnVal == JFileChooser.APPROVE_OPTION)
		            {
		                File file = fileChooser.getSelectedFile();
		                textField_FloorPlanPath.setText(file.toString());
		            }
			}
		});
		
		GridBagConstraints gbc_btnOpenFile = new GridBagConstraints();
		gbc_btnOpenFile.anchor = GridBagConstraints.WEST;
		gbc_btnOpenFile.insets = new Insets(0, 0, 5, 5);
		gbc_btnOpenFile.gridx = 2;
		gbc_btnOpenFile.gridy = 5;
		contentPanel.add(btnOpenFile, gbc_btnOpenFile);
		
		if(floorProperty != null) {
			textField_Level.setText(String.valueOf(floorProperty.getLevel()));
			textField_BottomLeftPointX.setText(String.valueOf(floorProperty.getBottomLeftPoint().getPanelX()));
			textField_BottomLeftPointY.setText(String.valueOf(floorProperty.getBottomLeftPoint().getPanelY()));
			textField_TopRightPointX.setText(String.valueOf(floorProperty.getTopRightPoint().getPanelX()));
			textField_TopRightPointY.setText(String.valueOf(floorProperty.getTopRightPoint().getPanelY()));			
			textField_GroundHeight.setText(String.valueOf(floorProperty.getGroundHeight()));
			textField_CeilingHeight.setText(String.valueOf(floorProperty.getCeilingHeight()));
			//textField_DoorHeight.setText(String.valueOf(floorProperty.getDoorHeight()));
			textField_FloorPlanPath.setText(String.valueOf(floorProperty.getFloorPlanPath()));
		}
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						ArrayList<FloorProperty> floorProperties = project.getBuildingProperty().getFloorProperties();
						
						String level = textField_Level.getText();
						Point bottomLeft = new Point();
						bottomLeft.setXYZ(textField_BottomLeftPointX.getText() + " " +
										textField_BottomLeftPointY.getText() + " " +
										textField_CeilingHeight.getText());
						Point topRight = new Point();
						topRight.setXYZ(textField_TopRightPointX.getText() + " " +
										textField_TopRightPointY.getText() + " " +
										textField_CeilingHeight.getText());
						double groundHeight = Double.parseDouble(textField_GroundHeight.getText());
						double ceilingHeight = Double.parseDouble(textField_CeilingHeight.getText());
						//double doorHeight = Double.parseDouble(textField_DoorHeight.getText());
						String floorPlanPath = textField_FloorPlanPath.getText();
						
						if(floorProperty != null){
							floorProperty.setLevel(level);
							floorProperty.setBottomLeftPoint(bottomLeft);
							floorProperty.setTopRightPoint(topRight);
							floorProperty.setGroundHeight(groundHeight);
							floorProperty.setCeilingHeight(ceilingHeight);
							//floorProperty.setDoorHeight(doorHeight);
							floorProperty.setFloorPlanPath(floorPlanPath);
							
							parent.comboBoxFloorRefresh();
							
							dispose();
							return;
						}
						
						
						for(int i=0; i<floorProperties.size(); i++){
							if(floorProperties.get(i).getLevel().equals(level)){
								System.out.println("duplicated floor level");
								
								return;
							}
						}
						
						FloorProperty floorProperty = new FloorProperty();
						floorProperty.setLevel(level);
						floorProperty.setBottomLeftPoint(bottomLeft);
						floorProperty.setTopRightPoint(topRight);
						floorProperty.setGroundHeight(groundHeight);
						floorProperty.setCeilingHeight(ceilingHeight);
						//floorProperty.setDoorHeight(doorHeight);
						floorProperty.setFloorPlanPath(floorPlanPath);
						
						project.addFloorProperty(floorProperty);
						parent.comboBoxFloorRefresh();
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

}
