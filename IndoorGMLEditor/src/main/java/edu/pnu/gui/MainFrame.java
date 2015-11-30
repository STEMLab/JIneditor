package edu.pnu.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.opengis.indoorgml.core.CellSpaceBoundaryOnFloor;
import net.opengis.indoorgml.core.CellSpaceOnFloor;
import net.opengis.indoorgml.core.Edges;
import net.opengis.indoorgml.core.IndoorFeatures;
import net.opengis.indoorgml.core.Nodes;
import net.opengis.indoorgml.core.SpaceLayer;
import net.opengis.indoorgml.core.SpaceLayers;
import net.opengis.indoorgml.core.State;
import edu.pnu.project.EditState;
import edu.pnu.project.EditWorkState;
import edu.pnu.project.FloorProperty;
import edu.pnu.project.ProjectFile;
import edu.pnu.project.StateOnFloor;
import edu.pnu.project.TransitionOnFloor;
import edu.pnu.visitor.IndoorGMLIDCoordinateGenerateVisitor;

public class MainFrame extends JFrame implements ComponentListener, KeyListener {

	private JPanel contentPane;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenuItem mntmOpen;
	private JMenuItem mntmSave;
	private JMenu mnImport;
	private JMenuItem mnImportIndoorGML;
	private JMenu mnExport;
	private JMenuItem mnExportToIndoorGML;
	private JMenu mnEdit;
	private JMenu mnBuildingProperties;
	private JMenuItem mntmFloor;
	private JSeparator separator;
	private JMenu mnCreateItem;
	private JMenuItem mntmCellspace;
	private JMenuItem mntmState;
	private JMenuItem mntmTransition;
	private JToolBar toolBar;
	private JButton btnCellspace;
	private JButton btnState;
	private JButton btnTransition;
	private JComboBox comboBox_Floor;

	private JScrollPane scrollPane;
	private SpaceLayerPanel panel;
	private ProjectFile currentProject;
	private JComboBox comboBox_SpaceLayer;
	private JMenuItem mntmSpaceLayers;
	private JButton btnGenerateGmlid;
	private JMenuItem mntmInterlayerconnection;
	private JMenu mnSettings;
	private JButton btnDoorcellspaceboundary;
	private JMenuItem mntmDoorcellspaceboundary;
	private JMenuItem mntmNew;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		addComponentListener(this);
		
		//
		setTitle("IndoorGML Editor");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1800, 900);
		setJMenuBar(getMenuBar_1());
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		contentPane.add(getToolBar(), BorderLayout.NORTH);
		
		scrollPane = getScrollPane();
		panel = getSpaceLayerPanel();
		scrollPane.add(panel);
		scrollPane.setViewportView(panel);
		
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		setVisible(true);
		pack();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		EditState state = currentProject.getEditState();
		EditWorkState workState = currentProject.getEditWorkState();
		
		switch (e.getKeyCode()) {
		case KeyEvent.VK_ESCAPE:
			if (state == EditState.CREATE_STATE) {
				currentProject.setEditState(EditState.NONE);
			} else if (state == EditState.CREATE_TRANSITION) {
				panel.setStateEnd1(null);
				panel.setStateEnd2(null);

				currentProject.setEditState(EditState.SELECT_STATE);
			} else if (state == EditState.SELECT_STATE) {
				panel.setSelectedState(null);

				currentProject.setEditState(EditState.NONE);
			} else if(state == EditState.CREATE_INTERLAYERCONNECTION) {
				panel.getSelectedStateMap().clear();
				
				currentProject.setEditState(EditState.SELECT_STATE);
			}

			break;
		case KeyEvent.VK_DELETE:
			if (state == EditState.SELECT_STATE) {
				for(State selected : panel.getSelectedStateMap().keySet()) {
					currentProject.deleteState(selected);
				}
				panel.setSelectedState(null);
				panel.getSelectedStateMap().clear();
				currentProject.setEditState(EditState.NONE);

				repaint();
			}

			break;
		case KeyEvent.VK_ENTER:
			if(state == EditState.CREATE_INTERLAYERCONNECTION) {
				if(workState == EditWorkState.CREATE_INTERLAYERCONNECTION_SELECTEND1) {
					System.out.println("interlayerconnection_end2");
					workState = EditWorkState.CREATE_INTERLAYERCONNECTION_SELECTEND2;
					currentProject.setEditWorkState(workState);
				} else if(workState == EditWorkState.CREATE_INTERLAYERCONNECTION_SELECTEND2) {
					workState = EditWorkState.CREATE_INTERLAYERCONNECTION_CREATE;
					currentProject.setEditWorkState(workState);
					System.out.println("interlayerconnection_create");
					//
					panel.createInterLayerConnection();
					
					currentProject.setEditState(EditState.NONE);
					currentProject.setEditWorkState(EditWorkState.NONE);
				}
			}
			
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void componentResized(ComponentEvent e) {
		//contentPane.setBounds(0, getMenuBar_1().getHeight(), this.getWidth()-20, this.getHeight()-90);
		//System.out.println("width : " + contentPane.getWidth() + "height : " + contentPane.getHeight());
		//getToolBar().setBounds(0, 0, this.getWidth(), getToolBar().getHeight());
		//panel.setBounds(0, getToolBar().getHeight(), this.getWidth()-20, this.getHeight()-90);
	}
	
	private JMenuBar getMenuBar_1() {
		if (menuBar == null) {
			menuBar = new JMenuBar();
			menuBar.add(getMnFile());
			menuBar.add(getMnEdit());
			menuBar.add(getMnSettings());
		}
		return menuBar;
	}
	private JMenu getMnFile() {
		if (mnFile == null) {
			mnFile = new JMenu("File");
			mnFile.add(getMntmNew());
			mnFile.add(getMntmOpen());
			mnFile.add(getMntmSave());
			mnFile.add(getSeparator());
			mnFile.add(getMnImport());
			mnFile.add(getMnExport());
		}
		return mnFile;
	}
	private JMenuItem getMntmOpen() {
		if (mntmOpen == null) {
			mntmOpen = new JMenuItem("Open");
			mntmOpen.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String filePath;
					JFileChooser fileChooser = new JFileChooser();
					FileNameExtensionFilter filter = new FileNameExtensionFilter("data (*.dat)", "dat");
			        fileChooser.setFileFilter(filter);
			        
			        int returnVal = fileChooser.showOpenDialog(MainFrame.this);
		            if( returnVal == JFileChooser.APPROVE_OPTION)
		            {
		                File file = fileChooser.getSelectedFile();
		        		FileInputStream fis = null;
		        		BufferedInputStream bis = null;
		        		ObjectInputStream ois = null;
		        		try {
		        			fis = new FileInputStream(file);
		        			bis = new BufferedInputStream(fis);
		        			ois = new ObjectInputStream(bis);
		        			
		        			currentProject = (ProjectFile)ois.readObject();
		        			ois.close();
		        			
		        			if(currentProject.getCurrentFloorPlanScale() == 0) {
		        				currentProject.setCurrentFloorPlanScale(1.0);
		        			}
		        			BufferedImage floorPlan = ImageIO.read(new File(currentProject.getCurrentStateOnFloor().getFloorProperty().getFloorPlanPath()));
		        			System.out.println(currentProject.getCurrentStateOnFloor().getFloorProperty().getFloorPlanPath());
		        			currentProject.setCurrentFloorPlan(floorPlan);
		        			
		        			panel.setProject(currentProject);
		        			comboBoxFloorRefresh();
		        			comboBoxSpaceLayerRefresh();
		        			panel.repaint();
		        			scrollPane.repaint();
		        			resizePanelPrefferedDimension(floorPlan.getWidth(), floorPlan.getHeight());
		        			pack();
		        			repaint();
		        		} catch (FileNotFoundException e1) {
		        			// TODO Auto-generated catch block
		        			e1.printStackTrace();
		        		} catch (IOException e1) {
		        			// TODO Auto-generated catch block
		        			e1.printStackTrace();
		        		} catch (ClassNotFoundException e1) {
		        			// TODO Auto-generated catch block
		        			e1.printStackTrace();
		        		}
		            }
				}
			});
		}
		return mntmOpen;
	}
	private JMenuItem getMntmSave() {
		if (mntmSave == null) {
			mntmSave = new JMenuItem("Save");
			mntmSave.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFileChooser fileChooser = new JFileChooser();
					FileNameExtensionFilter filter = new FileNameExtensionFilter("data (*.dat)", "dat");
			        fileChooser.setFileFilter(filter);
			        
			        int returnVal = fileChooser.showSaveDialog(MainFrame.this);
		            if( returnVal == JFileChooser.APPROVE_OPTION)
		            {
		                File file = fileChooser.getSelectedFile();
		                if(!file.getAbsolutePath().substring(file.getAbsolutePath().length() - 4).equals(".dat")) {
		                	String filePath = file.getAbsolutePath().concat(".dat");
		                	file = new File(filePath);
		                }
		                
		                System.out.println(file.getPath());
		                
		                System.out.println(file.getAbsolutePath());
		                
						FileOutputStream fos = null;
						BufferedOutputStream bos = null;
						ObjectOutputStream oos = null;
						try {
							fos = new FileOutputStream(file);
							bos = new BufferedOutputStream(fos);
							oos = new ObjectOutputStream(bos);
							
							oos.writeObject(currentProject);
							
							oos.close();							
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
		            }
				}
			});
		}
		return mntmSave;
	}
	private JMenu getMnImport() {
		if (mnImport == null) {
			mnImport = new JMenu("Import");
			mnImport.add(getMnImportIndoorGML());
		}
		return mnImport;
	}
	private JMenuItem getMnImportIndoorGML() {
		if (mnImportIndoorGML == null) {
			mnImportIndoorGML = new JMenuItem("IndoorGML");
			mnImportIndoorGML.setEnabled(false);
		}
		return mnImportIndoorGML;
	}
	private JMenu getMnExport() {
		if (mnExport == null) {
			mnExport = new JMenu("Export");
			mnExport.add(getMnExportToIndoorGML());
		}
		return mnExport;
	}
	private JMenuItem getMnExportToIndoorGML() {
		if (mnExportToIndoorGML == null) {
			mnExportToIndoorGML = new JMenuItem("IndoorGML");
			mnExportToIndoorGML.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					/*
					if(currentProject.getIs3DGeometry()) {
					//	IndoorGML3DGeometryBuilder builder 
						IndoorGML3DGeometryBuilder builder = new IndoorGML3DGeometryBuilder(currentProject.getIndoorFeatures());
						builder.create3DGeometry();
					}
					
					IndoorGMLExporter exporter = new IndoorGMLExporter(currentProject);
					try {
						exporter.export();
					} catch (JAXBException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					*/
					
					GeometryExportDialog dialog = new GeometryExportDialog(currentProject);
					dialog.setModal(true);
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				}
			});
		}
		return mnExportToIndoorGML;
	}
	private JMenu getMnEdit() {
		if (mnEdit == null) {
			mnEdit = new JMenu("Edit");
			mnEdit.add(getMnCreateItem());
			mnEdit.add(getMnBuildingProperties());
			mnEdit.add(getMntmSpaceLayers());
		}
		return mnEdit;
	}
	private JMenu getMnBuildingProperties() {
		if (mnBuildingProperties == null) {
			mnBuildingProperties = new JMenu("Building Properties");
			mnBuildingProperties.add(getMntmFloor());
		}
		return mnBuildingProperties;
	}
	private JMenuItem getMntmFloor() {
		if (mntmFloor == null) {
			mntmFloor = new JMenuItem("Floor");
			mntmFloor.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					FloorListDialog dialog = new FloorListDialog(MainFrame.this, currentProject);
					dialog.setModal(true);
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				}
			});
		}
		return mntmFloor;
	}
	
	public void comboBoxFloorRefresh() {
		ArrayList<FloorProperty> floorProperties = currentProject.getBuildingProperty().getFloorProperties();
		String[] items = new String[floorProperties.size()];
		for(int i=0; i< floorProperties.size(); i++){
			items[i] = String.valueOf(floorProperties.get(i).getLevel());
		}
		DefaultComboBoxModel model = new DefaultComboBoxModel(items);
		
		comboBox_Floor.setModel(model);
	}
	
	public void comboBoxSpaceLayerRefresh() {
		ArrayList<SpaceLayer> spaceLayerMember = currentProject.getCurrentSpaceLayers().getSpaceLayerMember();
		String[] items = new String[spaceLayerMember.size()];
		for(int i = 0; i < spaceLayerMember.size(); i++) {
			items[i] = spaceLayerMember.get(i).getGmlID();
		}
		DefaultComboBoxModel model = new DefaultComboBoxModel(items);
		
		comboBox_SpaceLayer.setModel(model);
	}
	
	private JSeparator getSeparator() {
		if (separator == null) {
			separator = new JSeparator();
		}
		return separator;
	}
	private JMenu getMnCreateItem() {
		if (mnCreateItem == null) {
			mnCreateItem = new JMenu("Create Item");
			mnCreateItem.add(getMntmCellspace());
			mnCreateItem.add(getMntmDoorcellspaceboundary());
			mnCreateItem.add(getMntmState());
			mnCreateItem.add(getMntmTransition());
			mnCreateItem.add(getMntmInterlayerconnection());
		}
		return mnCreateItem;
	}
	private JMenuItem getMntmCellspace() {
		if (mntmCellspace == null) {
			mntmCellspace = new JMenuItem("CellSpace");
			mntmCellspace.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					currentProject.setEditState(EditState.CREATE_CELLSPACE);
					currentProject.setEditWorkState(EditWorkState.CREATE_CELLSPACE_POINT1);
				}
			});
		}
		return mntmCellspace;
	}
	private JMenuItem getMntmState() {
		if (mntmState == null) {
			mntmState = new JMenuItem("State");
			mntmState.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					currentProject.setEditState(EditState.CREATE_STATE);
				}
			});
		}
		return mntmState;
	}
	private JMenuItem getMntmTransition() {
		if (mntmTransition == null) {
			mntmTransition = new JMenuItem("Transition");
			mntmTransition.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					currentProject.setEditState(EditState.CREATE_TRANSITION);
				}
			});
		}
		return mntmTransition;
	}
	private JToolBar getToolBar() {
		if (toolBar == null) {
			toolBar = new JToolBar();
			toolBar.setFloatable(false);
			toolBar.add(getBtnCellspace());
			toolBar.add(getBtnDoorcellspaceboundary());
			toolBar.add(getBtnState());
			toolBar.add(getBtnTransition());
			toolBar.add(getComboBox_SpaceLayer());
			toolBar.add(getComboBox_Floor());
		}
		return toolBar;
	}
	private JButton getBtnCellspace() {
		if (btnCellspace == null) {
			btnCellspace = new JButton("CellSpace");
			btnCellspace.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					currentProject.setEditState(EditState.CREATE_CELLSPACE);
					currentProject.setEditWorkState(EditWorkState.CREATE_CELLSPACE_POINT1);
				}
			});
		}
		return btnCellspace;
	}
	private JButton getBtnState() {
		if (btnState == null) {
			btnState = new JButton("State");
			btnState.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					currentProject.setEditState(EditState.CREATE_STATE);
				}
			});
		}
		return btnState;
	}
	private JButton getBtnTransition() {
		if (btnTransition == null) {
			btnTransition = new JButton("Transition");
			btnTransition.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					currentProject.setEditState(EditState.CREATE_TRANSITION);
				}
			});
		}
		return btnTransition;
	}
	private JMenuItem getMntmInterlayerconnection() {
		if (mntmInterlayerconnection == null) {
			mntmInterlayerconnection = new JMenuItem("InterLayerConnection");
			mntmInterlayerconnection.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.out.println("editstate create interlayerconnection");
					currentProject.setEditState(EditState.CREATE_INTERLAYERCONNECTION);
					currentProject.setEditWorkState(EditWorkState.CREATE_INTERLAYERCONNECTION_SELECTEND1);
				}
			});
		}
		return mntmInterlayerconnection;
	}
	private JComboBox getComboBox_Floor() {
		if (comboBox_Floor == null) {
			comboBox_Floor = new JComboBox();
			comboBox_Floor.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					refreshFloorPlan((String) comboBox_Floor.getSelectedItem());
				}
			});
		}
		return comboBox_Floor;
	}
	
	private void refreshFloorPlan(String selectedComboBoxFloor) {
		// SpaceLayer
		// search stateOnFloor from Nodes
		boolean nodesFound = false;
		Nodes currentNodes = currentProject.getCurrentNodes();
		ArrayList<StateOnFloor> stateOnFloor = currentNodes.getStateOnFloors();
		for(int i=0; i<stateOnFloor.size(); i++){
			if(stateOnFloor.get(i).getFloorProperty().getLevel().equals(selectedComboBoxFloor)){
				currentProject.setCurrentStateOnFloor(stateOnFloor.get(i));
				
				nodesFound = true;
				break;
			}
		}
		
		// search transitionOnFLoor from Edges
		boolean edgesFound = false;
		Edges currentEdges = currentProject.getCurrentEdges();
		ArrayList<TransitionOnFloor> transitionOnFloor = currentEdges.getTransitionOnFloors();
		for(int i=0; i<transitionOnFloor.size(); i++){
			if(transitionOnFloor.get(i).getFloorProperty().getLevel().equals(selectedComboBoxFloor)){
				currentProject.setCurrentTransitionOnFloor(transitionOnFloor.get(i));
				
				edgesFound = true;
				break;
			}
		}
		
		if(!nodesFound && !edgesFound) {
			FloorProperty floorProperty = currentProject.getBuildingProperty().getFloorProperty((String) selectedComboBoxFloor);
			if(floorProperty == null) return;
			
			StateOnFloor tempStateOnFloor = new StateOnFloor();
			tempStateOnFloor.setFloorProperty(floorProperty);
			
			TransitionOnFloor tempTransitionOnFloor = new TransitionOnFloor();
			tempTransitionOnFloor.setFloorProperty(floorProperty);
			
			currentNodes.getStateOnFloors().add(tempStateOnFloor);
			currentEdges.getTransitionOnFloors().add(tempTransitionOnFloor);
			
			currentProject.setCurrentStateOnFloor(tempStateOnFloor);
			currentProject.setCurrentTransitionOnFloor(tempTransitionOnFloor);
		}
		
		// search CellSpaceOnFloor, CellSpaceBoundaryOnFloor
		boolean cellSpaceOnFloorFound = false;
		ArrayList<CellSpaceOnFloor> cellSpaceOnFloors = currentProject.getPrimalSpacesFeatures().getCellSpaceOnFloors();
		for(CellSpaceOnFloor cellSpaceOnFloor : cellSpaceOnFloors) {
			if(cellSpaceOnFloor.getFloorProperty().getLevel().equals(selectedComboBoxFloor)) {
				currentProject.setCurrentCellSpaceOnFloor(cellSpaceOnFloor);
				
				cellSpaceOnFloorFound = true;
				break;
			}
		}
		
		boolean cellSpaceBoundaryOnFloorFound = false;
		ArrayList<CellSpaceBoundaryOnFloor> cellSpaceBoundaryOnFloors = currentProject.getPrimalSpacesFeatures().getCellSpaceBoundaryOnFloors();
		for(CellSpaceBoundaryOnFloor cellSpaceBoundaryOnFloor : cellSpaceBoundaryOnFloors) {
			if(cellSpaceBoundaryOnFloor.getFloorProperty().getLevel().equals(selectedComboBoxFloor)) {
				currentProject.setCurrentCellSpaceBoundaryOnFloor(cellSpaceBoundaryOnFloor);
				
				cellSpaceBoundaryOnFloorFound = true;
				break;
			}
		}
		
		if(!cellSpaceOnFloorFound && !cellSpaceBoundaryOnFloorFound) {
			FloorProperty floorProperty = currentProject.getBuildingProperty().getFloorProperty((String) selectedComboBoxFloor);
			if(floorProperty == null) return;
			
			CellSpaceOnFloor tempCellSpaceOnFloor = new CellSpaceOnFloor();
			tempCellSpaceOnFloor.setFloorProperty(floorProperty);
			
			CellSpaceBoundaryOnFloor tempCellSpaceBoundaryOnFloor = new CellSpaceBoundaryOnFloor();
			tempCellSpaceBoundaryOnFloor.setFloorProperty(floorProperty);
			
			currentProject.getPrimalSpacesFeatures().getCellSpaceOnFloors().add(tempCellSpaceOnFloor);
			currentProject.getPrimalSpacesFeatures().getCellSpaceBoundaryOnFloors().add(tempCellSpaceBoundaryOnFloor);
			
			currentProject.setCurrentCellSpaceOnFloor(tempCellSpaceOnFloor);
			currentProject.setCurrentCellSpaceBoundaryOnFloor(tempCellSpaceBoundaryOnFloor);
		}
		
		BufferedImage floorPlan = null;
		try {
			floorPlan = ImageIO.read(new File(currentProject.getCurrentStateOnFloor().getFloorProperty().getFloorPlanPath()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		currentProject.setCurrentFloorPlan(floorPlan);
		panel.repaint();
		scrollPane.repaint();
		resizePanelPrefferedDimension((int)(floorPlan.getWidth() * currentProject.getCurrentFloorPlanScale()),
				(int)(floorPlan.getHeight() * currentProject.getCurrentFloorPlanScale()));
		pack();
		repaint();
	}

	private JComboBox getComboBox_SpaceLayer() {
		if (comboBox_SpaceLayer == null) {
			comboBox_SpaceLayer = new JComboBox();
			comboBox_SpaceLayer.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					boolean spaceLayerFound = false;
					SpaceLayers currentSpaceLayers = currentProject.getCurrentSpaceLayers();
					ArrayList<SpaceLayer> spaceLayerMember = currentSpaceLayers.getSpaceLayerMember();
					for(SpaceLayer spaceLayer : spaceLayerMember) {
						if(spaceLayer.getGmlID().equals(comboBox_SpaceLayer.getSelectedItem())) {
							currentProject.setCurrentSpaceLayer(spaceLayer);
							currentProject.setCurrentNodes(spaceLayer.getNodes().get(0));
							currentProject.setCurrentEdges(spaceLayer.getEdges().get(0));
							
							System.out.println("spacelayer found");
							
							spaceLayerFound = true;
							refreshFloorPlan((String) comboBox_Floor.getSelectedItem());
							
							
							break;
						}
					}
				}
			});
		}
		return comboBox_SpaceLayer;
	}
	
	public JScrollPane getScrollPane() {
		if(scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setToolTipText("");
		}
		
		return scrollPane;
	}
	
	private SpaceLayerPanel getSpaceLayerPanel(){
		if (panel == null) {
			panel = new SpaceLayerPanel(MainFrame.this);
			panel.setToolTipText("");
			panel.setLocation(5, 28);
			//panel.setProject(currentProject);
			panel.setSize(this.getWidth()-20, this.getHeight()-90);
			panel.setPreferredSize(new Dimension(this.getWidth()-20, this.getHeight()-90));
		}
		return panel;
	}
	
	public void resizePanelPrefferedDimension(int width, int height) {
		panel.setPreferredSize(new Dimension(width, height));
		scrollPane.setPreferredSize(new Dimension(width + 5, height + 5));
	}
	private JMenuItem getMntmSpaceLayers() {
		if (mntmSpaceLayers == null) {
			mntmSpaceLayers = new JMenuItem("SpaceLayers");
			mntmSpaceLayers.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					SpaceLayerListDialog dialog = new SpaceLayerListDialog(MainFrame.this, currentProject);
					dialog.setModal(true);
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				}
			});
		}
		return mntmSpaceLayers;
	}
	private JButton getBtnGenerateGmlid() {
		if (btnGenerateGmlid == null) {
			btnGenerateGmlid = new JButton("Generate GMLID");
			btnGenerateGmlid.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					IndoorGMLIDCoordinateGenerateVisitor visitor = new IndoorGMLIDCoordinateGenerateVisitor(currentProject.getIs3DGeometry());
					IndoorFeatures indoorFeatures = currentProject.getIndoorFeatures();
					indoorFeatures.accept(visitor);
					
					comboBoxSpaceLayerRefresh();
				}
			});
		}
		return btnGenerateGmlid;
	}

	
	private JMenu getMnSettings() {
		if (mnSettings == null) {
			mnSettings = new JMenu("Settings");
		}
		return mnSettings;
	}
	private JButton getBtnDoorcellspaceboundary() {
		if (btnDoorcellspaceboundary == null) {
			btnDoorcellspaceboundary = new JButton("Door(CellSpaceBoundary)");
			btnDoorcellspaceboundary.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					currentProject.setEditState(EditState.CREATE_CELLSPACEBOUNDARY_AS_DOOR);
				}
			});
		}
		return btnDoorcellspaceboundary;
	}
	private JMenuItem getMntmDoorcellspaceboundary() {
		if (mntmDoorcellspaceboundary == null) {
			mntmDoorcellspaceboundary = new JMenuItem("Door(CellSpaceBoundary)");
			mntmDoorcellspaceboundary.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					currentProject.setEditState(EditState.CREATE_CELLSPACEBOUNDARY_AS_DOOR);
				}
			});
		}
		return mntmDoorcellspaceboundary;
	}
	private JMenuItem getMntmNew() {
		if (mntmNew == null) {
			mntmNew = new JMenuItem("New");
			mntmNew.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					currentProject = new ProjectFile();
					panel.setProject(currentProject);
					comboBoxSpaceLayerRefresh();
					comboBoxFloorRefresh();
				}
			});
		}
		return mntmNew;
	}
}
