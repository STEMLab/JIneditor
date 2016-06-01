package edu.pnu.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.opengis.indoorgml.core.CellSpaceBoundaryOnFloor;
import net.opengis.indoorgml.core.CellSpaceOnFloor;
import net.opengis.indoorgml.core.Edges;
import net.opengis.indoorgml.core.IndoorFeatures;
import net.opengis.indoorgml.core.InterLayerConnection;
import net.opengis.indoorgml.core.Nodes;
import net.opengis.indoorgml.core.PrimalSpaceFeatures;
import net.opengis.indoorgml.core.SpaceLayer;
import net.opengis.indoorgml.core.SpaceLayers;
import net.opengis.indoorgml.core.State;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import edu.pnu.importexport.ProjectMetaDataExporter;
import edu.pnu.importexport.ProjectMetaDataImporter;
import edu.pnu.importexport.WKTImporter;
import edu.pnu.project.EditState;
import edu.pnu.project.EditWorkState;
import edu.pnu.project.FloorProperty;
import edu.pnu.project.ProjectFile;
import edu.pnu.project.ProjectMetaData;
import edu.pnu.project.StateOnFloor;
import edu.pnu.project.TransitionOnFloor;
import edu.pnu.util.IndoorGMLIDGenerator;
import edu.pnu.util.InterLayerConnectionGenerator;

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

    private CanvasPanel panel;

    private ProjectFile currentProject;

    private JComboBox comboBox_SpaceLayer;

    private JMenuItem mntmSpaceLayers;

    private JButton btnGenerateGmlid;

    private JMenuItem mntmInterlayerconnection;

    private JMenu mnSettings;

    private JButton btnDoorcellspaceboundary;

    private JMenuItem mntmDoorcellspaceboundary;

    private JMenuItem mntmNew;

    private JButton btnInterlayerconnection;
    private JPanel stateBarPane;
    private JLabel labelEditState;
    private JLabel lblSpacelayer;
    private JLabel lblFloor;
    private JButton btnNone;
    private JPanel panel_1;
    private JButton btnImport;
    private JTextField textField_ID;
    private JButton btnAa;
    private JMenu mnAssist;
    private JMenuItem mntmGenerateInterlayerconnection;
    private JMenuItem mntmWkt;

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
        setBounds(100, 100, 1300, 800);
        setJMenuBar(getMenuBar_1());

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        contentPane.add(getToolBar(), BorderLayout.NORTH);
        contentPane.add(getPanel_1(), BorderLayout.WEST);

        scrollPane = getScrollPane();
        panel = getSpaceLayerPanel();
        scrollPane.add(panel);
        scrollPane.setViewportView(panel);

        contentPane.add(scrollPane, BorderLayout.CENTER);
        contentPane.add(getStateBarPane(), BorderLayout.SOUTH);

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
            } else if (state == EditState.CREATE_INTERLAYERCONNECTION) {
                panel.getSelectedStateMap().clear();

                currentProject.setEditState(EditState.SELECT_STATE);
            }

            break;
        case KeyEvent.VK_DELETE:
            if (state == EditState.SELECT_STATE) {
                for (State selected : panel.getSelectedStateMap().keySet()) {
                    currentProject.deleteState(selected);
                }
                panel.setSelectedState(null);
                panel.getSelectedStateMap().clear();
                currentProject.setEditState(EditState.NONE);

                repaint();
            }

            break;
        case KeyEvent.VK_ENTER:
            if (state == EditState.CREATE_INTERLAYERCONNECTION) {
                if (workState == EditWorkState.CREATE_INTERLAYERCONNECTION_SELECTEND1) {
                    System.out.println("interlayerconnection_end2");
                    workState = EditWorkState.CREATE_INTERLAYERCONNECTION_SELECTEND2;
                    currentProject.setEditWorkState(workState);
                } else if (workState == EditWorkState.CREATE_INTERLAYERCONNECTION_SELECTEND2) {
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
        // contentPane.setBounds(0, getMenuBar_1().getHeight(), this.getWidth()-20, this.getHeight()-90);
        // System.out.println("width : " + contentPane.getWidth() + "height : " + contentPane.getHeight());
        // getToolBar().setBounds(0, 0, this.getWidth(), getToolBar().getHeight());
        // panel.setBounds(0, getToolBar().getHeight(), this.getWidth()-20, this.getHeight()-90);
    }

    private JMenuBar getMenuBar_1() {
        if (menuBar == null) {
            menuBar = new JMenuBar();
            menuBar.add(getMnFile());
            menuBar.add(getMnEdit());
            menuBar.add(getMnSettings());
            menuBar.add(getMnAssist());
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
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("data (*.dat)",
                            "dat");
                    fileChooser.setFileFilter(filter);

                    int returnVal = fileChooser.showOpenDialog(MainFrame.this);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                    	/* Load Test */
                    	try {
							ProjectMetaDataImporter importer = new ProjectMetaDataImporter("result.xml");
							//ProjectMetaData metaData = importer.getProjectMetaData();
							
						} catch (ParserConfigurationException | SAXException
								| IOException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
                    	/* */
                    	
                        File file = fileChooser.getSelectedFile();
                        FileInputStream fis = null;
                        BufferedInputStream bis = null;
                        ObjectInputStream ois = null;
                        try {
                            fis = new FileInputStream(file);
                            bis = new BufferedInputStream(fis);
                            ois = new ObjectInputStream(bis);

                            currentProject = (ProjectFile) ois.readObject();
                            ois.close();

                            currentProject.loadIndoorGMLID();

                            if (currentProject.getCurrentFloorPlanScale() == 0) {
                                currentProject.setCurrentFloorPlanScale(1.0);
                            }

                            File floorPlanFile = new File(currentProject.getCurrentStateOnFloor()
                                    .getFloorProperty().getFloorPlanPath());
                            BufferedImage floorPlan = null;
                            if (floorPlanFile.exists()) {
                                floorPlan = ImageIO.read(floorPlanFile);
                            }
                            if (floorPlan != null) {
                                System.out.println(currentProject.getCurrentStateOnFloor()
                                        .getFloorProperty().getFloorPlanPath());
                                currentProject.setCurrentFloorPlan(floorPlan);
                            }

                            panel.setProject(currentProject);
                            comboBoxFloorRefresh();
                            comboBoxSpaceLayerRefresh();
                            if (floorPlan != null) {
                                resizePanelPrefferedDimension(floorPlan.getWidth(), floorPlan.getHeight());
                            }
                            panel.repaint();
                            scrollPane.repaint();
                            repaint();
                            pack();
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
                    currentProject.saveIndoorGMLID();

                    JFileChooser fileChooser = new JFileChooser();
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("data (*.dat)",
                            "dat");
                    fileChooser.setFileFilter(filter);

                    int returnVal = fileChooser.showSaveDialog(MainFrame.this);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                    	/* Save Test */
                    	ProjectMetaDataExporter metaDataExporter = new ProjectMetaDataExporter(currentProject, currentProject.getIndoorFeatures());
                    	try {
							Document document = metaDataExporter.createDocument();
							TransformerFactory transformerFactory = TransformerFactory.newInstance();
							transformerFactory.setAttribute("indent-number", new Integer(4));
							Transformer transformer = transformerFactory.newTransformer();
							transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
							transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
							transformer.setOutputProperty(OutputKeys.INDENT, "yes");	
							DOMSource source = new DOMSource(document);
							FileWriter fw = new FileWriter("result.xml");
							StreamResult result = new StreamResult(fw);
							transformer.transform(source, result);
						} catch (ParserConfigurationException | IOException | TransformerException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}                    	
                    	/**/
                    	
                        File file = fileChooser.getSelectedFile();
                        if (!file.getAbsolutePath().substring(file.getAbsolutePath().length() - 4)
                                .equals(".dat")) {
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
            mnImport.add(getMntmWkt());
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
                     * if(currentProject.getIs3DGeometry()) { // IndoorGML3DGeometryBuilder builder IndoorGML3DGeometryBuilder builder = new
                     * IndoorGML3DGeometryBuilder(currentProject.getIndoorFeatures()); builder.create3DGeometry(); }
                     * 
                     * IndoorGMLExporter exporter = new IndoorGMLExporter(currentProject); try { exporter.export(); } catch (JAXBException e1) { //
                     * TODO Auto-generated catch block e1.printStackTrace(); }
                     */

                    GeometryExportDialog dialog = new GeometryExportDialog(panel, currentProject);
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
        ArrayList<FloorProperty> floorProperties = currentProject.getBuildingProperty()
                .getFloorProperties();
        String[] items = new String[floorProperties.size()];
        for (int i = 0; i < floorProperties.size(); i++) {
            items[i] = String.valueOf(floorProperties.get(i).getLevel());
        }
        DefaultComboBoxModel model = new DefaultComboBoxModel(items);

        comboBox_Floor.setModel(model);
    }

    public void comboBoxSpaceLayerRefresh() {
        ArrayList<SpaceLayer> spaceLayerMember = currentProject.getCurrentSpaceLayers()
                .getSpaceLayerMember();
        String[] items = new String[spaceLayerMember.size()];
        for (int i = 0; i < spaceLayerMember.size(); i++) {
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
                    setLabel_CurrentEditState("Create CellSpace");
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
                    setLabel_CurrentEditState("Create State");
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
                    setLabel_CurrentEditState("Create Transition : Choose two states, and press ESC key");
                }
            });
        }
        return mntmTransition;
    }

    private JToolBar getToolBar() {
        if (toolBar == null) {
            toolBar = new JToolBar();
            toolBar.setFloatable(false);
            //toolBar.setBorder(new EmptyBorder(5, 5, 5, 5));
            FlowLayout fl_toolBar = new FlowLayout();
            fl_toolBar.setAlignment(FlowLayout.LEFT);
            toolBar.setLayout(fl_toolBar);
            toolBar.add(getLblSpacelayer());
            toolBar.add(getComboBox_SpaceLayer());
            toolBar.add(getLblFloor());
            toolBar.add(getComboBox_Floor());
            toolBar.add(getBtnImport());
            toolBar.add(getTextField_ID());
            toolBar.add(getBtnAa());
        }
        return toolBar;
    }

    private JButton getBtnCellspace() {
        if (btnCellspace == null) {
            btnCellspace = new JButton("CellSpace");
            btnCellspace.setBackground(Color.white);
            btnCellspace.setOpaque(false);
            btnCellspace.setToolTipText("Create CellSpace");
            btnCellspace.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    currentProject.setEditState(EditState.CREATE_CELLSPACE);
                    currentProject.setEditWorkState(EditWorkState.CREATE_CELLSPACE_POINT1);
                    setLabel_CurrentEditState("Create CellSpace");
                }
            });
        }
        return btnCellspace;
    }

    private JButton getBtnState() {
        if (btnState == null) {
            btnState = new JButton("State");
            btnState.setBackground(Color.white);
            btnState.setOpaque(false);
            btnState.setToolTipText("Create State");
            btnState.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    currentProject.setEditState(EditState.CREATE_STATE);
                    setLabel_CurrentEditState("Create State");
                }
            });
        }
        return btnState;
    }

    private JButton getBtnTransition() {
        if (btnTransition == null) {
            btnTransition = new JButton("Transition");
            btnTransition.setBackground(Color.white);
            btnTransition.setOpaque(false);
            btnTransition.setToolTipText("Create Transition");
            btnTransition.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    currentProject.setEditState(EditState.CREATE_TRANSITION);
                    setLabel_CurrentEditState("Create Transition : Choose two states, and press ESC key");
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
                    currentProject
                            .setEditWorkState(EditWorkState.CREATE_INTERLAYERCONNECTION_SELECTEND1);
                    setLabel_CurrentEditState("Create InterLayerConnection : Choose the state(or states with CTRL key) and press Enter key");
                }
            });
        }
        return mntmInterlayerconnection;
    }

    private JComboBox getComboBox_Floor() {
        if (comboBox_Floor == null) {
            comboBox_Floor = new JComboBox();
            comboBox_Floor.setPreferredSize(new Dimension(200, 20));
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
        for (int i = 0; i < stateOnFloor.size(); i++) {
            if (stateOnFloor.get(i).getFloorProperty().getLevel().equals(selectedComboBoxFloor)) {
                currentProject.setCurrentStateOnFloor(stateOnFloor.get(i));

                nodesFound = true;
                break;
            }
        }

        // search transitionOnFLoor from Edges
        boolean edgesFound = false;
        Edges currentEdges = currentProject.getCurrentEdges();
        ArrayList<TransitionOnFloor> transitionOnFloor = currentEdges.getTransitionOnFloors();
        for (int i = 0; i < transitionOnFloor.size(); i++) {
            if (transitionOnFloor.get(i).getFloorProperty().getLevel()
                    .equals(selectedComboBoxFloor)) {
                currentProject.setCurrentTransitionOnFloor(transitionOnFloor.get(i));

                edgesFound = true;
                break;
            }
        }

        if (!nodesFound && !edgesFound) {
            FloorProperty floorProperty = currentProject.getBuildingProperty().getFloorProperty(
                    (String) selectedComboBoxFloor);
            if (floorProperty == null)
                return;

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
        ArrayList<CellSpaceOnFloor> cellSpaceOnFloors = currentProject.getPrimalSpacesFeatures()
                .getCellSpaceOnFloors();
        for (CellSpaceOnFloor cellSpaceOnFloor : cellSpaceOnFloors) {
            if (cellSpaceOnFloor.getFloorProperty().getLevel().equals(selectedComboBoxFloor)) {
                currentProject.setCurrentCellSpaceOnFloor(cellSpaceOnFloor);

                cellSpaceOnFloorFound = true;
                break;
            }
        }

        boolean cellSpaceBoundaryOnFloorFound = false;
        ArrayList<CellSpaceBoundaryOnFloor> cellSpaceBoundaryOnFloors = currentProject
                .getPrimalSpacesFeatures().getCellSpaceBoundaryOnFloors();
        for (CellSpaceBoundaryOnFloor cellSpaceBoundaryOnFloor : cellSpaceBoundaryOnFloors) {
            if (cellSpaceBoundaryOnFloor.getFloorProperty().getLevel()
                    .equals(selectedComboBoxFloor)) {
                currentProject.setCurrentCellSpaceBoundaryOnFloor(cellSpaceBoundaryOnFloor);

                cellSpaceBoundaryOnFloorFound = true;
                break;
            }
        }

        if (!cellSpaceOnFloorFound && !cellSpaceBoundaryOnFloorFound) {
            FloorProperty floorProperty = currentProject.getBuildingProperty().getFloorProperty(
                    (String) selectedComboBoxFloor);
            if (floorProperty == null)
                return;

            CellSpaceOnFloor tempCellSpaceOnFloor = new CellSpaceOnFloor();
            tempCellSpaceOnFloor.setFloorProperty(floorProperty);

            CellSpaceBoundaryOnFloor tempCellSpaceBoundaryOnFloor = new CellSpaceBoundaryOnFloor();
            tempCellSpaceBoundaryOnFloor.setFloorProperty(floorProperty);

            currentProject.getPrimalSpacesFeatures().getCellSpaceOnFloors()
                    .add(tempCellSpaceOnFloor);
            currentProject.getPrimalSpacesFeatures().getCellSpaceBoundaryOnFloors()
                    .add(tempCellSpaceBoundaryOnFloor);

            currentProject.setCurrentCellSpaceOnFloor(tempCellSpaceOnFloor);
            currentProject.setCurrentCellSpaceBoundaryOnFloor(tempCellSpaceBoundaryOnFloor);
        }

        File floorPlanFile = new File(currentProject.getCurrentStateOnFloor()
                .getFloorProperty().getFloorPlanPath());
        BufferedImage floorPlan = null;
        try {
            if(floorPlanFile.exists()) {
                floorPlan = ImageIO.read(floorPlanFile);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        currentProject.setCurrentFloorPlan(floorPlan);
        panel.repaint();
        scrollPane.repaint();
        if(floorPlan != null) {
            
            /*resizePanelPrefferedDimension(
                    (int) (floorPlan.getWidth() * currentProject.getCurrentFloorPlanScale()),
                    (int) (floorPlan.getHeight() * currentProject.getCurrentFloorPlanScale()));*/
            //resizePanelPrefferedDimension((int) (floorPlan.getWidth()), (int) (floorPlan.getHeight()));
        }
        pack();
        repaint();
    }

    private JComboBox getComboBox_SpaceLayer() {
        if (comboBox_SpaceLayer == null) {
            comboBox_SpaceLayer = new JComboBox();
            comboBox_SpaceLayer.setPreferredSize(new Dimension(200, 20));
            comboBox_SpaceLayer.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    boolean spaceLayerFound = false;
                    SpaceLayers currentSpaceLayers = currentProject.getCurrentSpaceLayers();
                    ArrayList<SpaceLayer> spaceLayerMember = currentSpaceLayers
                            .getSpaceLayerMember();
                    for (SpaceLayer spaceLayer : spaceLayerMember) {
                        if (spaceLayer.getGmlID().equals(comboBox_SpaceLayer.getSelectedItem())) {
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
        if (scrollPane == null) {
            scrollPane = new JScrollPane();
            scrollPane.setToolTipText("");
            scrollPane.setWheelScrollingEnabled(true);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        }

        return scrollPane;
    }

    private CanvasPanel getSpaceLayerPanel() {
        if (panel == null) {
            panel = new CanvasPanel(MainFrame.this);
            panel.setToolTipText("");
            panel.setLocation(5, 28);
            // panel.setProject(currentProject);
            panel.setSize(this.getWidth() - 20, this.getHeight() - 90);
            panel.setPreferredSize(new Dimension(this.getWidth() - 20, this.getHeight() - 90));
        }
        return panel;
    }

    public void resizePanelPrefferedDimension(int width, int height) {
        panel.setPreferredSize(new Dimension(width, height));
        scrollPane.setPreferredSize(new Dimension(width + 5, height + 5));
        labelEditState.setPreferredSize(new Dimension(width, 15));
        //pack();
    }
    
    public void setLabel_CurrentEditState(String state) {
            labelEditState.setText(state);
    }

    private JMenuItem getMntmSpaceLayers() {
        if (mntmSpaceLayers == null) {
            mntmSpaceLayers = new JMenuItem("SpaceLayers");
            mntmSpaceLayers.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    SpaceLayerListDialog dialog = new SpaceLayerListDialog(MainFrame.this,
                            currentProject);
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
                    IndoorGMLIDGenerator generator = new IndoorGMLIDGenerator(currentProject.getIndoorFeatures(),
                            currentProject.getIs3DGeometry());
                    generator.generateGMLID();

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
            btnDoorcellspaceboundary = new JButton("Door");
            btnDoorcellspaceboundary.setBackground(Color.white);
            btnDoorcellspaceboundary.setOpaque(false);
            btnDoorcellspaceboundary.setToolTipText("Create Door(CellSpaceBoundary)");
            btnDoorcellspaceboundary.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    currentProject.setEditState(EditState.CREATE_CELLSPACEBOUNDARY_AS_DOOR);
                    setLabel_CurrentEditState("Create Door(CellSpaceBoundary) : Choose two points on a CellSpaceBoundary");
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
                    setLabel_CurrentEditState("Create Door(CellSpaceBoundary) : Choose two points on a CellSpaceBoundary");
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

    private JButton getBtnInterlayerconnection() {
        if (btnInterlayerconnection == null) {
            btnInterlayerconnection = new JButton("<html>InterLayer<br />Connection</html>");
            btnInterlayerconnection.setBackground(Color.white);
            btnInterlayerconnection.setOpaque(false);
            btnInterlayerconnection.setToolTipText("Create InterLayerConnection");
            btnInterlayerconnection.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    currentProject.setEditState(EditState.CREATE_INTERLAYERCONNECTION);
                    currentProject
                            .setEditWorkState(EditWorkState.CREATE_INTERLAYERCONNECTION_SELECTEND1);
                    setLabel_CurrentEditState("Create InterLayerConnection : Choose the state(or states with CTRL key) and press Enter key");
                }
            });
        }
        return btnInterlayerconnection;
    }
    private JPanel getStateBarPane() {
        if (stateBarPane == null) {
        	stateBarPane = new JPanel();
        	FlowLayout flowLayout = (FlowLayout) stateBarPane.getLayout();
        	flowLayout.setAlignment(FlowLayout.LEFT);
        	stateBarPane.add(getLabelEditState());
        }
        return stateBarPane;
    }
    private JLabel getLabelEditState() {
        if (labelEditState == null) {
        	labelEditState = new JLabel("");
        	labelEditState.setPreferredSize(new Dimension(150, 15));
        }
        return labelEditState;
    }
    private JLabel getLblSpacelayer() {
        if (lblSpacelayer == null) {
        	lblSpacelayer = new JLabel("SpaceLayer");
        }
        return lblSpacelayer;
    }
    private JLabel getLblFloor() {
        if (lblFloor == null) {
        	lblFloor = new JLabel("Floor");
        }
        return lblFloor;
    }
    private JButton getBtnNone() {
        if (btnNone == null) {
        	btnNone = new JButton("Select");
        	btnNone.setBackground(Color.white);
        	btnNone.setOpaque(false);
        	btnNone.setIcon(null);
        	btnNone.addActionListener(new ActionListener() {
        	    public void actionPerformed(ActionEvent arg0) {
        	        panel.keyPressESCAPE(currentProject.getEditState());
        	    }
        	});
        	btnNone.setToolTipText("Select");
        }
        return btnNone;
    }
    private JPanel getPanel_1() {
        if (panel_1 == null) {
        	panel_1 = new JPanel();
        	GridBagLayout gbl_panel_1 = new GridBagLayout();
        	gbl_panel_1.columnWidths = new int[] {5};
        	gbl_panel_1.rowHeights = new int[] {40, 40, 40, 40, 40, 40, 40};
        	gbl_panel_1.columnWeights = new double[]{0.0};
        	gbl_panel_1.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        	panel_1.setLayout(gbl_panel_1);
        	GridBagConstraints gbc_btnNone = new GridBagConstraints();
        	gbc_btnNone.fill = GridBagConstraints.BOTH;
        	gbc_btnNone.insets = new Insets(0, 0, 5, 5);
        	gbc_btnNone.gridx = 0;
        	gbc_btnNone.gridy = 0;
        	panel_1.add(getBtnNone(), gbc_btnNone);
        	GridBagConstraints gbc_btnCellspace = new GridBagConstraints();
        	gbc_btnCellspace.fill = GridBagConstraints.BOTH;
        	gbc_btnCellspace.insets = new Insets(0, 0, 5, 5);
        	gbc_btnCellspace.gridx = 0;
        	gbc_btnCellspace.gridy = 1;
        	panel_1.add(getBtnCellspace(), gbc_btnCellspace);
        	GridBagConstraints gbc_btnDoorcellspaceboundary = new GridBagConstraints();
        	gbc_btnDoorcellspaceboundary.fill = GridBagConstraints.BOTH;
        	gbc_btnDoorcellspaceboundary.insets = new Insets(0, 0, 5, 5);
        	gbc_btnDoorcellspaceboundary.gridx = 0;
        	gbc_btnDoorcellspaceboundary.gridy = 2;
        	panel_1.add(getBtnDoorcellspaceboundary(), gbc_btnDoorcellspaceboundary);
        	GridBagConstraints gbc_btnState = new GridBagConstraints();
        	gbc_btnState.fill = GridBagConstraints.BOTH;
        	gbc_btnState.insets = new Insets(0, 0, 5, 5);
        	gbc_btnState.gridx = 0;
        	gbc_btnState.gridy = 3;
        	panel_1.add(getBtnState(), gbc_btnState);
        	GridBagConstraints gbc_btnTransition = new GridBagConstraints();
        	gbc_btnTransition.fill = GridBagConstraints.BOTH;
        	gbc_btnTransition.insets = new Insets(0, 0, 5, 5);
        	gbc_btnTransition.gridx = 0;
        	gbc_btnTransition.gridy = 4;
        	panel_1.add(getBtnTransition(), gbc_btnTransition);
        	GridBagConstraints gbc_btnInterlayerconnection = new GridBagConstraints();
        	gbc_btnInterlayerconnection.anchor = GridBagConstraints.WEST;
        	gbc_btnInterlayerconnection.fill = GridBagConstraints.VERTICAL;
        	gbc_btnInterlayerconnection.insets = new Insets(0, 0, 0, 5);
        	gbc_btnInterlayerconnection.gridx = 0;
        	gbc_btnInterlayerconnection.gridy = 5;
        	panel_1.add(getBtnInterlayerconnection(), gbc_btnInterlayerconnection);
        }
        return panel_1;
    }
    private JButton getBtnImport() {
        if (btnImport == null) {
        	btnImport = new JButton("Import");
        	btnImport.addActionListener(new ActionListener() {
        	    public void actionPerformed(ActionEvent arg0) {
    	            WKTImporter wktImporter = new WKTImporter(panel, currentProject);
    	            String filePath;
                    JFileChooser fileChooser = new JFileChooser();

                    int returnVal = fileChooser.showOpenDialog(MainFrame.this);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = fileChooser.getSelectedFile();
                        wktImporter.read(file);
                    }
                    panel.repaint();
                    repaint();
        	    }
        	});
        }
        //btnImport.setVisible(false);
        return btnImport;
    }
    private JTextField getTextField_ID() {
        if (textField_ID == null) {
        	textField_ID = new JTextField();
        	textField_ID.setColumns(10);
        }
        //textField_ID.setVisible(false);
        return textField_ID;
    }
    private JButton getBtnAa() {
        if (btnAa == null) {
        	btnAa = new JButton("Search CellSpace");
        	btnAa.addActionListener(new ActionListener() {
        	    public void actionPerformed(ActionEvent arg0) {
        	            panel.searchByID(textField_ID.getText());
        	    }
        	});
        }
        //btnAa.setVisible(false);
        return btnAa;
    }
	private JMenu getMnAssist() {
		if (mnAssist == null) {
			mnAssist = new JMenu("Assist");
			mnAssist.add(getMntmGenerateInterlayerconnection());
		}
		return mnAssist;
	}
	private JMenuItem getMntmGenerateInterlayerconnection() {
		if (mntmGenerateInterlayerconnection == null) {
			mntmGenerateInterlayerconnection = new JMenuItem("Generate InterLayerConnection");
			mntmGenerateInterlayerconnection.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					IndoorFeatures indoorFeatures = currentProject.getIndoorFeatures();
					PrimalSpaceFeatures primalSpace = indoorFeatures.getPrimalSpaceFeatures();
					ArrayList<SpaceLayer> spaceLayerMember = indoorFeatures.getMultiLayeredGraph().getSpaceLayers().get(0).getSpaceLayerMember();
					
					if(spaceLayerMember.size() > 1) {
						SpaceLayer target1 = spaceLayerMember.get(0);
						SpaceLayer target2 = spaceLayerMember.get(1);
						InterLayerConnectionGenerator ilcGenerator = new InterLayerConnectionGenerator(primalSpace, target1, target2);
						List<InterLayerConnection> ilcMember = ilcGenerator.getInterLayerConnections();
						indoorFeatures.getMultiLayeredGraph().getInterEdges().get(0).getInterLayerConnectionMember().addAll(ilcMember);
						panel.repaint();
					}
				}
			});
		}
		return mntmGenerateInterlayerconnection;
	}
	private JMenuItem getMntmWkt() {
		if (mntmWkt == null) {
			mntmWkt = new JMenuItem("WKT");
		}
		return mntmWkt;
	}
}
