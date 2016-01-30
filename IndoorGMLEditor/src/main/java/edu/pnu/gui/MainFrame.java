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
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.xml.bind.JAXBException;

import org.apache.commons.lang3.math.NumberUtils;

import net.opengis.indoorgml.core.CCTV;
import net.opengis.indoorgml.core.CCTVOnFloor;
import net.opengis.indoorgml.core.Edges;
import net.opengis.indoorgml.core.IndoorFeatures;
import net.opengis.indoorgml.core.Nodes;
import net.opengis.indoorgml.core.SpaceLayer;
import net.opengis.indoorgml.core.SpaceLayers;
import net.opengis.indoorgml.core.State;
import edu.pnu.importexport.IndoorGMLExporter;
import edu.pnu.project.EditState;
import edu.pnu.project.EditWorkState;
import edu.pnu.project.FloorProperty;
import edu.pnu.project.ProjectFile;
import edu.pnu.project.StateOnFloor;
import edu.pnu.project.TransitionOnFloor;
import edu.pnu.visitor.IndoorGMLIDGenerateVisitor;
import javax.swing.ListSelectionModel;

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

	private JMenuItem mntmState;

	private JMenuItem mntmTransition;

	private JToolBar toolBar;

	private JButton btnState;

	private JButton btnTransition;

	private JComboBox comboBox_Floor;

	private JScrollPane scrollPane;

	private CanvasPanel canvasPanel;

	private ProjectFile currentProject;

	private JComboBox comboBox_SpaceLayer;

	private JMenuItem mntmSpaceLayers;

	private JButton btnGenerateGmlid;

	private JMenuItem mntmInterlayerconnection;

	private JMenu mnSettings;

	private JMenuItem mntmNew;

	private JButton btnInterlayerconnection;
	private JPanel stateBarPane;
	private JLabel labelEditState;
	private JLabel lblSpacelayer;
	private JLabel lblFloor;
	private JButton btnNone;
	private JPanel panel_Pallete;
	private JButton btnCctv;
	private JPanel panel_CCTVProperties;
	private JLabel lblCctvId;
	private JLabel lblIp;
	private JLabel lblSourcelocation;
	private JTextField textField_CCTVID;
	private JTextField textField_CCTVIP;
	private JTextField textField_SourceLocation;
	private JLabel lblUrlId;
	private JTextField textField_URLID;
	private JLabel lblUrlPassword;
	private JTextField textField_URLPassword;
	private JLabel lblWidth;
	private JTextField textField_Width;
	private JLabel lblHeight;
	private JTextField textField_Height;
	private JLabel lblFramerate;
	private JTextField textField_Framerate;
	private JLabel lblOrientation;
	private JTextField textField_Orientation;
	private JLabel lblFov;
	private JTextField textField_Fov;
	private JLabel lblAspect;
	private JTextField textField_Aspect;
	private JLabel lblMappedState;
	private JTextField textField_MappedState;
	private JLabel lblInstallatinonTime;
	private JTable table_CCTVList;
	private JSpinner spinner_DateTime;
	private JScrollPane scrollPane_CCTVList;

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
		setTitle("IndoorGML + CCTV Editor");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 800);
		setJMenuBar(getMenuBar_1());

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		contentPane.add(getToolBar(), BorderLayout.NORTH);
		contentPane.add(getPanel_Pallete(), BorderLayout.WEST);

		scrollPane = getScrollPane();
		canvasPanel = getSpaceLayerPanel();
		scrollPane.add(canvasPanel);
		scrollPane.setViewportView(canvasPanel);

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

		// MainFrame에 포커스가 와있을 때의 key event
		switch (e.getKeyCode()) {
			case KeyEvent.VK_ESCAPE :
				if (state == EditState.CREATE_STATE) {
					currentProject.setEditState(EditState.NONE);
				} else if (state == EditState.CREATE_TRANSITION) {
					canvasPanel.setStateEnd1(null);
					canvasPanel.setStateEnd2(null);

					currentProject.setEditState(EditState.SELECT_STATE);
				} else if (state == EditState.SELECT_STATE) {
					canvasPanel.setSelectedState(null);

					currentProject.setEditState(EditState.NONE);
				} else if (state == EditState.CREATE_INTERLAYERCONNECTION) {
					canvasPanel.getSelectedStateMap().clear();

					currentProject.setEditState(EditState.SELECT_STATE);
				}

				break;
			case KeyEvent.VK_DELETE :
				if (state == EditState.SELECT_STATE) {
					for (State selected : canvasPanel.getSelectedStateMap().keySet()) {
						currentProject.deleteState(selected);
					}
					canvasPanel.setSelectedState(null);
					canvasPanel.getSelectedStateMap().clear();
					currentProject.setEditState(EditState.NONE);

					repaint();
				}

				break;
			case KeyEvent.VK_ENTER :
				if (state == EditState.CREATE_INTERLAYERCONNECTION) {
					if (workState == EditWorkState.CREATE_INTERLAYERCONNECTION_SELECTEND1) {
						System.out.println("interlayerconnection_end2");
						workState = EditWorkState.CREATE_INTERLAYERCONNECTION_SELECTEND2;
						currentProject.setEditWorkState(workState);
					} else if (workState == EditWorkState.CREATE_INTERLAYERCONNECTION_SELECTEND2) {
						workState = EditWorkState.CREATE_INTERLAYERCONNECTION_CREATE;
						currentProject.setEditWorkState(workState);
						canvasPanel.createInterLayerConnection();

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
				// 작업을 내용을 저장한 데이터 파일을 불러오는 이벤트
				public void actionPerformed(ActionEvent e) {
					// 불러올 데이터 파일을 선택한다.
					String filePath;
					JFileChooser fileChooser = new JFileChooser();
					FileNameExtensionFilter filter = new FileNameExtensionFilter(
							"data (*.dat)", "dat");
					fileChooser.setFileFilter(filter);

					int returnVal = fileChooser.showOpenDialog(MainFrame.this);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File file = fileChooser.getSelectedFile();
						FileInputStream fis = null;
						BufferedInputStream bis = null;
						ObjectInputStream ois = null;
						try {
							// 파일로부터 ProjectFile 객체를 읽어온다.
							fis = new FileInputStream(file);
							bis = new BufferedInputStream(fis);
							ois = new ObjectInputStream(bis);

							currentProject = (ProjectFile) ois.readObject();
							ois.close();

							currentProject.loadIndoorGMLID();

							if (currentProject.getCurrentFloorPlanScale() == 0) {
								currentProject.setCurrentFloorPlanScale(1.0);
							}

							File floorPlanFile = new File(currentProject
									.getCurrentStateOnFloor()
									.getFloorProperty().getFloorPlanPath());
							BufferedImage floorPlan = null;
							if (floorPlanFile.exists()) {
								floorPlan = ImageIO.read(floorPlanFile);
							}
							if (floorPlan != null) {
								currentProject.setCurrentFloorPlan(floorPlan);
							}

							canvasPanel.setProject(currentProject);
							
							// SpaceLaeyr와 Floor의 콤보박스를 초기화한다.
							comboBoxFloorRefresh();
							comboBoxSpaceLayerRefresh();
							if (floorPlan != null) {
								resizePanelPrefferedDimension(
										floorPlan.getWidth(),
										floorPlan.getHeight());
							}
							canvasPanel.repaint();
							scrollPane.repaint();
							updateCCTVTableModel();
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
				// 작업 중인 내용을 파일로 저장하는 이벤트
				public void actionPerformed(ActionEvent e) {
					currentProject.saveIndoorGMLID();

					JFileChooser fileChooser = new JFileChooser();
					FileNameExtensionFilter filter = new FileNameExtensionFilter(
							"data (*.dat)", "dat");
					fileChooser.setFileFilter(filter);

					int returnVal = fileChooser.showSaveDialog(MainFrame.this);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File file = fileChooser.getSelectedFile();
						if (!file.getAbsolutePath()
								.substring(file.getAbsolutePath().length() - 4)
								.equals(".dat")) {
							String filePath = file.getAbsolutePath().concat(
									".dat");
							file = new File(filePath);
						}
						// 저장될 파일의 경로와 같은 위치에 FloorPlan 이미지 파일을 복사한다.
						copyFloorPlan(file);

						FileOutputStream fos = null;
						BufferedOutputStream bos = null;
						ObjectOutputStream oos = null;
						try {
							fos = new FileOutputStream(file);
							bos = new BufferedOutputStream(fos);
							oos = new ObjectOutputStream(bos);

							// ProjectFile 객체를 파일로 저장한다.
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
	
	/**
	 * file과 같은 위치의 경로에 FloorPlan들을 모두 복사한다.
	 * @param file
	 */
	private void copyFloorPlan(File file) {
		ArrayList<FloorProperty> floorProperties = currentProject.getBuildingProperty().getFloorProperties();
		
		for(FloorProperty floorProperty : floorProperties) {
			int lastIndex = floorProperty.getFloorPlanPath().lastIndexOf('\\');
			String fromPath = floorProperty.getFloorPlanPath();
			String toPath = file.getParent() + floorProperty.getFloorPlanPath().substring(lastIndex);
			
			Path from = Paths.get(fromPath);
			Path to = Paths.get(toPath);
			
			try {
				Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
				floorProperty.setFloorPlanPath(toPath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
			mnExportToIndoorGML = new JMenuItem("IndoorGML + CCTV");
			mnExportToIndoorGML.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					IndoorGMLExporter exporter = new IndoorGMLExporter(currentProject);
					try {
						exporter.export();
					} catch (JAXBException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
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
				// 층 정보를 관리하는 Dialog를 표시하는 이벤트
				public void actionPerformed(ActionEvent arg0) {
					FloorListDialog dialog = new FloorListDialog(
							MainFrame.this, currentProject);
					dialog.setModal(true);
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				}
			});
		}
		return mntmFloor;
	}

	/**
	 * 층의 이름이 표시되는 콤보박스의 내용을 갱신
	 */
	public void comboBoxFloorRefresh() {
		ArrayList<FloorProperty> floorProperties = currentProject
				.getBuildingProperty().getFloorProperties();
		String[] items = new String[floorProperties.size()];
		for (int i = 0; i < floorProperties.size(); i++) {
			items[i] = String.valueOf(floorProperties.get(i).getLevel());
		}
		DefaultComboBoxModel model = new DefaultComboBoxModel(items);

		comboBox_Floor.setModel(model);
	}

	/**
	 * SpaceLayer의 ID가 표시되는 콤보박스의 내용을 갱신
	 */
	public void comboBoxSpaceLayerRefresh() {
		ArrayList<SpaceLayer> spaceLayerMember = currentProject
				.getCurrentSpaceLayers().getSpaceLayerMember();
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
			mnCreateItem.add(getMntmState());
			mnCreateItem.add(getMntmTransition());
			mnCreateItem.add(getMntmInterlayerconnection());
		}
		return mnCreateItem;
	}

	private JMenuItem getMntmState() {
		if (mntmState == null) {
			mntmState = new JMenuItem("State");
			mntmState.addActionListener(new ActionListener() {
				// 작업 상태를 State 생성으로 변환하는 이벤트
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
				// 작업 상태를 Transition 생성으로 변환하는 이벤트
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
			// toolBar.setBorder(new EmptyBorder(5, 5, 5, 5));
			FlowLayout fl_toolBar = new FlowLayout();
			fl_toolBar.setAlignment(FlowLayout.LEFT);
			toolBar.setLayout(fl_toolBar);
			toolBar.add(getLblSpacelayer());
			toolBar.add(getComboBox_SpaceLayer());
			toolBar.add(getLblFloor());
			toolBar.add(getComboBox_Floor());
		}
		return toolBar;
	}

	private JButton getBtnState() {
		if (btnState == null) {
			btnState = new JButton("State");
			btnState.setBackground(Color.white);
			btnState.setOpaque(false);
			btnState.setToolTipText("Create State");
			btnState.addActionListener(new ActionListener() {
				// 작업 상태를 State 생성으로 변환하는 이벤트
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
				// 작업 상태를 Transition 생성으로 변환하는 이벤트
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
				// 작업 상태를 InterLayerConnection 생성으로 변환하는 이벤트
				public void actionPerformed(ActionEvent e) {
					currentProject
							.setEditState(EditState.CREATE_INTERLAYERCONNECTION);
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
				// 콤보박스의 Item이 선택되면 해당 이름을 가지는 층으로 화면이 변경되도록 하는 이벤트
				public void actionPerformed(ActionEvent arg0) {
					refreshFloorPlan((String) comboBox_Floor.getSelectedItem());
				}
			});
		}
		return comboBox_Floor;
	}

	/**
	 * 콤보박스에서 선택된 층에 대하여 화면을 변경하는 함수
	 * @param selectedComboBoxFloor
	 */
	private void refreshFloorPlan(String selectedComboBoxFloor) {
		// 콤보박스에서 선택된 층의 이름과 같은 층정보를 가지는 StateOnFloor를 검색한다.
		boolean nodesFound = false;
		Nodes currentNodes = currentProject.getCurrentNodes();
		ArrayList<StateOnFloor> stateOnFloor = currentNodes.getStateOnFloors();
		for (int i = 0; i < stateOnFloor.size(); i++) {
			if (stateOnFloor.get(i).getFloorProperty().getLevel()
					.equals(selectedComboBoxFloor)) {
				currentProject.setCurrentStateOnFloor(stateOnFloor.get(i));

				nodesFound = true;
				break;
			}
		}

		// 콤보박스에서 선택된 층의 이름과 같은 층정보를 가지는 TransitionOnFloor를 검색한다.
		boolean edgesFound = false;
		Edges currentEdges = currentProject.getCurrentEdges();
		ArrayList<TransitionOnFloor> transitionOnFloor = currentEdges
				.getTransitionOnFloors();
		for (int i = 0; i < transitionOnFloor.size(); i++) {
			if (transitionOnFloor.get(i).getFloorProperty().getLevel()
					.equals(selectedComboBoxFloor)) {
				currentProject.setCurrentTransitionOnFloor(transitionOnFloor
						.get(i));

				edgesFound = true;
				break;
			}
		}

		// 층정보를 생성하고 처음 콤보박스에서 해당 층을 선택할 경우 검색이 되지 않는다.
		// 검색되지 않았다면 새로운 StateOnFloor와 TransitionOnFloor를 생성한다.
		if (!nodesFound && !edgesFound) {
			FloorProperty floorProperty = currentProject.getBuildingProperty()
					.getFloorProperty((String) selectedComboBoxFloor);
			if (floorProperty == null)
				return;

			// StateOnFloor 생성
			StateOnFloor tempStateOnFloor = new StateOnFloor();
			tempStateOnFloor.setFloorProperty(floorProperty);

			// TransitionOnFloor 생성
			TransitionOnFloor tempTransitionOnFloor = new TransitionOnFloor();
			tempTransitionOnFloor.setFloorProperty(floorProperty);

			// Nodes와 Edges에 추가한다.
			currentNodes.getStateOnFloors().add(tempStateOnFloor);
			currentEdges.getTransitionOnFloors().add(tempTransitionOnFloor);

			// 현재 작업중인 StateOnFloor와 TransitonOnFloor로 설정한다.
			currentProject.setCurrentStateOnFloor(tempStateOnFloor);
			currentProject.setCurrentTransitionOnFloor(tempTransitionOnFloor);
		}
		
		// CCTV
		// 콤보박스에서 선택된 층의 이름과 같은 층정보를 가지는 CCTVOnFloor를 검색한다.
		boolean cctvOnFloorFound = false;
		ArrayList<CCTVOnFloor> cctvOnFloors = currentProject.getPrimalSpacesFeatures().getCctvOnFloors();
		for (CCTVOnFloor cctvOnFloor : cctvOnFloors) {
			if (cctvOnFloor.getFloorProperty().getLevel().equalsIgnoreCase(selectedComboBoxFloor)) {
				currentProject.setCurrentCCTVOnFloor(cctvOnFloor);
				
				cctvOnFloorFound = true;
				break;
			}
		}
		
		// 검색되지 않았다면 CCTVOnFloor를 생성한다.
		if (!cctvOnFloorFound) {
			FloorProperty floorProperty = currentProject.getBuildingProperty()
					.getFloorProperty((String) selectedComboBoxFloor);
			if (floorProperty == null)
				return;
			
			CCTVOnFloor tempCCTVOnFloor = new CCTVOnFloor();
			tempCCTVOnFloor.setFloorProperty(floorProperty);

			currentProject.getPrimalSpacesFeatures().getCctvOnFloors().add(tempCCTVOnFloor);

			currentProject.setCurrentCCTVOnFloor(tempCCTVOnFloor);
		}

		// 선택된 층에 해당하는 FloorPlan으로 화면을 변경한다.
		File floorPlanFile = new File(currentProject.getCurrentStateOnFloor()
				.getFloorProperty().getFloorPlanPath());
		BufferedImage floorPlan = null;
		try {
			if (floorPlanFile.exists()) {
				floorPlan = ImageIO.read(floorPlanFile);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		currentProject.setCurrentFloorPlan(floorPlan);
		
		// 패널의 화면과 CCTV의 목록을 갱신한다.
		canvasPanel.repaint();
		scrollPane.repaint();
		updateCCTVTableModel();
		if (floorPlan != null) {
			resizePanelPrefferedDimension((int) (floorPlan.getWidth()),
					(int) (floorPlan.getHeight()));
		}
		pack();
		repaint();
	}

	private JComboBox getComboBox_SpaceLayer() {
		if (comboBox_SpaceLayer == null) {
			comboBox_SpaceLayer = new JComboBox();
			comboBox_SpaceLayer.setPreferredSize(new Dimension(200, 20));
			comboBox_SpaceLayer.addActionListener(new ActionListener() {
				// 콤보박스의 Item이 선택되면 해당 ID을 가지는 SpaceLayer로 화면이 변경되도록 하는 이벤트
				public void actionPerformed(ActionEvent arg0) {
					SpaceLayers currentSpaceLayers = currentProject
							.getCurrentSpaceLayers();
					ArrayList<SpaceLayer> spaceLayerMember = currentSpaceLayers
							.getSpaceLayerMember();
					
					for (SpaceLayer spaceLayer : spaceLayerMember) {
						if (spaceLayer.getGmlID().equals(
								comboBox_SpaceLayer.getSelectedItem())) {
							currentProject.setCurrentSpaceLayer(spaceLayer);
							currentProject.setCurrentNodes(spaceLayer
									.getNodes().get(0));
							currentProject.setCurrentEdges(spaceLayer
									.getEdges().get(0));

							// 동일한 GMLID를 갖는 SpaceLayer를 찾아 화면을 갱신한다.
							refreshFloorPlan((String) comboBox_Floor
									.getSelectedItem());

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
		if (canvasPanel == null) {
			canvasPanel = new CanvasPanel(MainFrame.this);
			FlowLayout flowLayout = (FlowLayout) canvasPanel.getLayout();
			canvasPanel.setToolTipText("");
			canvasPanel.setLocation(5, 28);
			// panel.setProject(currentProject);
			canvasPanel.setSize(this.getWidth() - 20, this.getHeight() - 90);
			canvasPanel.setPreferredSize(new Dimension(this.getWidth() - 20, this
					.getHeight() - 90));
		}
		return canvasPanel;
	}

	/**
	 * 패널의 크기를 조정한다.
	 * @param width
	 * @param height
	 */
	public void resizePanelPrefferedDimension(int width, int height) {
		canvasPanel.setPreferredSize(new Dimension(width, height));
		scrollPane.setPreferredSize(new Dimension(width + 5, height + 5));
		labelEditState.setPreferredSize(new Dimension(width, 15));
		// pack();
	}

	public void setLabel_CurrentEditState(String state) {
		labelEditState.setText(state);
	}

	private JMenuItem getMntmSpaceLayers() {
		if (mntmSpaceLayers == null) {
			mntmSpaceLayers = new JMenuItem("SpaceLayers");
			mntmSpaceLayers.addActionListener(new ActionListener() {
				// SpaceLayer를 관리하는 Dialog를 표시하는 이벤트
				public void actionPerformed(ActionEvent e) {
					SpaceLayerListDialog dialog = new SpaceLayerListDialog(
							MainFrame.this, currentProject);
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
					IndoorGMLIDGenerateVisitor visitor = new IndoorGMLIDGenerateVisitor(
							currentProject.getIs3DGeometry());
					IndoorFeatures indoorFeatures = currentProject
							.getIndoorFeatures();
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

	private JMenuItem getMntmNew() {
		if (mntmNew == null) {
			mntmNew = new JMenuItem("New");
			mntmNew.addActionListener(new ActionListener() {
				// 새로운 ProjectFile을 생성하는 이벤트
				public void actionPerformed(ActionEvent e) {
					currentProject = new ProjectFile();
					canvasPanel.setProject(currentProject);
					comboBoxSpaceLayerRefresh();
					comboBoxFloorRefresh();
				}
			});
		}
		return mntmNew;
	}

	private JButton getBtnInterlayerconnection() {
		if (btnInterlayerconnection == null) {
			btnInterlayerconnection = new JButton(
					"<html>InterLayer<br />Connection</html>");
			btnInterlayerconnection.setBackground(Color.white);
			btnInterlayerconnection.setOpaque(false);
			btnInterlayerconnection
					.setToolTipText("Create InterLayerConnection");
			btnInterlayerconnection.addActionListener(new ActionListener() {
				// 작업 상태를 InterLayerConnection 생성으로 변환하는 이벤트
				public void actionPerformed(ActionEvent arg0) {
					currentProject
							.setEditState(EditState.CREATE_INTERLAYERCONNECTION);
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
					canvasPanel.keyPressESCAPE(currentProject.getEditState());
				}
			});
			btnNone.setToolTipText("Select");
		}
		return btnNone;
	}
	private JPanel getPanel_Pallete() {
		if (panel_Pallete == null) {
			panel_Pallete = new JPanel();
			GridBagLayout gbl_panel_Pallete = new GridBagLayout();
			gbl_panel_Pallete.columnWidths = new int[]{200, 40};
			gbl_panel_Pallete.rowHeights = new int[]{40, 40, 40, 40, 40, 200};
			gbl_panel_Pallete.columnWeights = new double[]{1.0, 1.0};
			gbl_panel_Pallete.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
			panel_Pallete.setLayout(gbl_panel_Pallete);
			GridBagConstraints gbc_btnNone = new GridBagConstraints();
			gbc_btnNone.fill = GridBagConstraints.BOTH;
			gbc_btnNone.insets = new Insets(0, 0, 5, 5);
			gbc_btnNone.gridx = 0;
			gbc_btnNone.gridy = 0;
			panel_Pallete.add(getBtnNone(), gbc_btnNone);
			GridBagConstraints gbc_btnState = new GridBagConstraints();
			gbc_btnState.fill = GridBagConstraints.BOTH;
			gbc_btnState.insets = new Insets(0, 0, 5, 5);
			gbc_btnState.gridx = 0;
			gbc_btnState.gridy = 1;
			panel_Pallete.add(getBtnState(), gbc_btnState);
			GridBagConstraints gbc_btnTransition = new GridBagConstraints();
			gbc_btnTransition.fill = GridBagConstraints.BOTH;
			gbc_btnTransition.insets = new Insets(0, 0, 5, 5);
			gbc_btnTransition.gridx = 0;
			gbc_btnTransition.gridy = 2;
			panel_Pallete.add(getBtnTransition(), gbc_btnTransition);
			GridBagConstraints gbc_btnInterlayerconnection = new GridBagConstraints();
			gbc_btnInterlayerconnection.insets = new Insets(0, 0, 5, 5);
			gbc_btnInterlayerconnection.fill = GridBagConstraints.BOTH;
			gbc_btnInterlayerconnection.gridx = 0;
			gbc_btnInterlayerconnection.gridy = 3;
			panel_Pallete.add(getBtnInterlayerconnection(),
					gbc_btnInterlayerconnection);
			GridBagConstraints gbc_btnCctv = new GridBagConstraints();
			gbc_btnCctv.insets = new Insets(0, 0, 5, 5);
			gbc_btnCctv.fill = GridBagConstraints.BOTH;
			gbc_btnCctv.gridx = 0;
			gbc_btnCctv.gridy = 4;
			panel_Pallete.add(getBtnCctv(), gbc_btnCctv);
			getPanel_CCTVProperties().setLayout(null);
			GridBagConstraints gbc_scrollPane_CCTVList = new GridBagConstraints();
			gbc_scrollPane_CCTVList.insets = new Insets(0, 0, 5, 0);
			gbc_scrollPane_CCTVList.fill = GridBagConstraints.BOTH;
			gbc_scrollPane_CCTVList.gridx = 1;
			gbc_scrollPane_CCTVList.gridy = 0;
			gbc_scrollPane_CCTVList.gridheight = 6;
			panel_Pallete.add(getScrollPane_CCTVList(), gbc_scrollPane_CCTVList);
			getScrollPane_CCTVList().setViewportView(getTable_CCTVList());
			GridBagConstraints gbc_panel_CCTVProperties = new GridBagConstraints();
			gbc_panel_CCTVProperties.gridheight = 1;
			gbc_panel_CCTVProperties.insets = new Insets(0, 0, 5, 5);
			gbc_panel_CCTVProperties.fill = GridBagConstraints.BOTH;
			gbc_panel_CCTVProperties.gridx = 0;
			gbc_panel_CCTVProperties.gridy = 5;
			panel_Pallete.add(getPanel_CCTVProperties(), gbc_panel_CCTVProperties);			
		}
		return panel_Pallete;
	}
	private JButton getBtnCctv() {
		if (btnCctv == null) {
			btnCctv = new JButton("CCTV");
			btnCctv.setToolTipText("Create CCTV");
			btnCctv.setBackground(Color.white);
			btnCctv.setOpaque(false);
			btnCctv.addActionListener(new ActionListener() {
				// 작업 상태를 CCTV 생성으로 변환하는 이벤트
				public void actionPerformed(ActionEvent arg0) {
					currentProject.setEditState(EditState.CREATE_CCTV);
					setLabel_CurrentEditState("Create CCTV");
				}
			});
		}
		return btnCctv;
	}
	private JPanel getPanel_CCTVProperties() {
		if (panel_CCTVProperties == null) {
			panel_CCTVProperties = new JPanel();
			panel_CCTVProperties.add(getLblCctvId());
			panel_CCTVProperties.add(getLblIp());
			panel_CCTVProperties.add(getLblSourcelocation());
			panel_CCTVProperties.add(getTextField_CCTVID());
			panel_CCTVProperties.add(getTextField_CCTVIP());
			panel_CCTVProperties.add(getTextField_SourceLocation());
			panel_CCTVProperties.add(getLblUrlId());
			panel_CCTVProperties.add(getTextField_URLID());
			panel_CCTVProperties.add(getLblUrlPassword());
			panel_CCTVProperties.add(getTextField_URLPassword());
			panel_CCTVProperties.add(getLblWidth());
			panel_CCTVProperties.add(getTextField_Width());
			panel_CCTVProperties.add(getLblHeight());
			panel_CCTVProperties.add(getTextField_Height());
			panel_CCTVProperties.add(getLblFramerate());
			panel_CCTVProperties.add(getTextField_Framerate());
			panel_CCTVProperties.add(getLblOrientation());
			panel_CCTVProperties.add(getTextField_Orientation());
			panel_CCTVProperties.add(getLblFov());
			panel_CCTVProperties.add(getTextField_Fov());
			panel_CCTVProperties.add(getLblAspect());
			panel_CCTVProperties.add(getTextField_Aspect());
			panel_CCTVProperties.add(getLblMappedState());
			panel_CCTVProperties.add(getTextField_MappedState());
			panel_CCTVProperties.add(getLblInstallatinonTime());
			panel_CCTVProperties.add(getSpinner_DateTime());
		}
		//panel_CCTVProperties.setVisible(false);
		return panel_CCTVProperties;
	}
	/**
	 * CCTV의 속성 정보를 화면에 보이게 할 지의 여부 설정
	 * @param value
	 */
	public void setCCTVPropertiesVisible(boolean value) {
		panel_CCTVProperties.setVisible(value);
	}
	private JLabel getLblCctvId() {
		if (lblCctvId == null) {
			lblCctvId = new JLabel("CCTV ID");
			lblCctvId.setBounds(12, 13, 57, 15);
		}
		return lblCctvId;
	}
	private JLabel getLblIp() {
		if (lblIp == null) {
			lblIp = new JLabel("IP");
			lblIp.setBounds(12, 38, 57, 15);
		}
		return lblIp;
	}
	private JLabel getLblSourcelocation() {
		if (lblSourcelocation == null) {
			lblSourcelocation = new JLabel("SourceLocation");
			lblSourcelocation.setBounds(12, 63, 99, 15);
		}
		return lblSourcelocation;
	}
	private JTextField getTextField_CCTVID() {
		if (textField_CCTVID == null) {
			textField_CCTVID = new JTextField();
			textField_CCTVID.addFocusListener(new FocusAdapter() {
				// 선택된 CCTV의 ID가 변경되었을 때 발생하는 이벤트
				@Override
				public void focusLost(FocusEvent arg0) {
					if(canvasPanel.getSelectedCCTV() != null && textField_CCTVID.getText() != null) {
						canvasPanel.getSelectedCCTV().setCctvID(textField_CCTVID.getText());
						updateCCTVTableModel();
					}
				}
			});
			textField_CCTVID.setBounds(81, 10, 107, 21);
			textField_CCTVID.setColumns(10);
		}
		return textField_CCTVID;
	}
	private JTextField getTextField_CCTVIP() {
		if (textField_CCTVIP == null) {
			textField_CCTVIP = new JTextField();
			textField_CCTVIP.addFocusListener(new FocusAdapter() {
				// 선택된 CCTV의 IP가 변경되었을 때 발생하는 이벤트
				@Override
				public void focusLost(FocusEvent arg0) {
					if(canvasPanel.getSelectedCCTV() != null && textField_CCTVIP.getText() != null) {
						canvasPanel.getSelectedCCTV().setIp(textField_CCTVIP.getText());
					}
				}
			});
			textField_CCTVIP.setBounds(81, 35, 107, 21);
			textField_CCTVIP.setColumns(10);
		}
		return textField_CCTVIP;
	}
	private JTextField getTextField_SourceLocation() {
		if (textField_SourceLocation == null) {
			textField_SourceLocation = new JTextField();
			textField_SourceLocation.addFocusListener(new FocusAdapter() {
				// 선택된 CCTV의 SourceLocation이 변경되었을 때 발생하는 이벤트
				@Override
				public void focusLost(FocusEvent e) {
					if(canvasPanel.getSelectedCCTV() != null && textField_SourceLocation.getText() != null) {
						canvasPanel.getSelectedCCTV().setSourceLocation(textField_SourceLocation.getText());
					}
				}
			});
			textField_SourceLocation.setBounds(59, 88, 129, 21);
			textField_SourceLocation.setColumns(10);
		}
		return textField_SourceLocation;
	}
	private JLabel getLblUrlId() {
		if (lblUrlId == null) {
			lblUrlId = new JLabel("ID");
			lblUrlId.setBounds(12, 179, 57, 15);
		}
		return lblUrlId;
	}
	private JTextField getTextField_URLID() {
		if (textField_URLID == null) {
			textField_URLID = new JTextField();
			textField_URLID.addFocusListener(new FocusAdapter() {
				// 선택된 CCTV의 URL의 ID가 변경되었을 때 발생하는 이벤트
				@Override
				public void focusLost(FocusEvent e) {
					if(canvasPanel.getSelectedCCTV() != null && textField_URLID.getText() != null) {
						canvasPanel.getSelectedCCTV().setId(textField_URLID.getText());
					}
				}
			});
			textField_URLID.setBounds(81, 176, 107, 21);
			textField_URLID.setColumns(10);
		}
		return textField_URLID;
	}
	private JLabel getLblUrlPassword() {
		if (lblUrlPassword == null) {
			lblUrlPassword = new JLabel("Password");
			lblUrlPassword.setBounds(12, 207, 57, 15);
		}
		return lblUrlPassword;
	}
	private JTextField getTextField_URLPassword() {
		if (textField_URLPassword == null) {
			textField_URLPassword = new JTextField();
			textField_URLPassword.addFocusListener(new FocusAdapter() {
				// 선택된 CCTV의 URL Password가 변경되었을 때 발생하는 이벤트
				@Override
				public void focusLost(FocusEvent e) {
					if(canvasPanel.getSelectedCCTV() != null && textField_URLPassword.getText() != null) {
						canvasPanel.getSelectedCCTV().setPassword(textField_URLPassword.getText());
					}
				}
			});
			textField_URLPassword.setBounds(81, 204, 107, 21);
			textField_URLPassword.setColumns(10);
		}
		return textField_URLPassword;
	}
	private JLabel getLblWidth() {
		if (lblWidth == null) {
			lblWidth = new JLabel("Width");
			lblWidth.setBounds(12, 232, 57, 15);
		}
		return lblWidth;
	}
	private JTextField getTextField_Width() {
		if (textField_Width == null) {
			textField_Width = new JTextField();
			textField_Width.addFocusListener(new FocusAdapter() {
				// 선택된 CCTV의 영상의 Width가 변경되었을 때 발생하는 이벤트
				@Override
				public void focusLost(FocusEvent e) {
					if(canvasPanel.getSelectedCCTV() != null && !textField_Width.getText().equals("")) {
						if(isNumeric(textField_Width.getText())) {
							canvasPanel.getSelectedCCTV().setWidth(Double.parseDouble(textField_Width.getText()));
						}
					}
				}
			});
			textField_Width.setBounds(81, 229, 107, 21);
			textField_Width.setColumns(10);
		}
		return textField_Width;
	}
	private JLabel getLblHeight() {
		if (lblHeight == null) {
			lblHeight = new JLabel("Height");
			lblHeight.setBounds(12, 257, 57, 15);
		}
		return lblHeight;
	}
	private JTextField getTextField_Height() {
		if (textField_Height == null) {
			textField_Height = new JTextField();
			textField_Height.addFocusListener(new FocusAdapter() {
				// 선택된 CCTV의 영상의 Height가 변경되었을 때 발생하는 이벤트
				@Override
				public void focusLost(FocusEvent e) {
					if(canvasPanel.getSelectedCCTV() != null && !textField_Height.getText().equals("")) {
						if(isNumeric(textField_Height.getText())) {
							canvasPanel.getSelectedCCTV().setHeight(Double.parseDouble(textField_Height.getText()));
						}
					}
				}
			});
			textField_Height.setBounds(81, 254, 107, 21);
			textField_Height.setColumns(10);
		}
		return textField_Height;
	}
	private JLabel getLblFramerate() {
		if (lblFramerate == null) {
			lblFramerate = new JLabel("Framerate");
			lblFramerate.setBounds(12, 282, 57, 15);
		}
		return lblFramerate;
	}
	private JTextField getTextField_Framerate() {
		if (textField_Framerate == null) {
			textField_Framerate = new JTextField();
			textField_Framerate.addFocusListener(new FocusAdapter() {
				// 선택된 CCTV의 영상의 Frame rate가 변경되었을 때 발생하는 이벤트
				@Override
				public void focusLost(FocusEvent e) {
					if(canvasPanel.getSelectedCCTV() != null && !textField_Framerate.getText().equals("")) {
						if(isNumeric(textField_Framerate.getText())) {
							canvasPanel.getSelectedCCTV().setFramerate(Double.parseDouble(textField_Framerate.getText()));
						}
					}
				}
			});
			textField_Framerate.setBounds(81, 279, 107, 21);
			textField_Framerate.setColumns(10);
		}
		return textField_Framerate;
	}
	private JLabel getLblOrientation() {
		if (lblOrientation == null) {
			lblOrientation = new JLabel("Orientation");
			lblOrientation.setBounds(12, 307, 67, 15);
		}
		return lblOrientation;
	}
	private JTextField getTextField_Orientation() {
		if (textField_Orientation == null) {
			textField_Orientation = new JTextField();
			textField_Orientation.addFocusListener(new FocusAdapter() {
				// 선택된 CCTV의 Orientation이 변경되었을 때 발생하는 이벤트
				@Override
				public void focusLost(FocusEvent e) {
					if(canvasPanel.getSelectedCCTV() != null && !textField_Orientation.getText().equals("")) {
						if(isNumeric(textField_Orientation.getText())) {
							canvasPanel.getSelectedCCTV().setOrientation(Double.parseDouble(textField_Orientation.getText()));
						}
					}
				}
			});
			textField_Orientation.setBounds(81, 304, 107, 21);
			textField_Orientation.setColumns(10);
		}
		return textField_Orientation;
	}
	private JLabel getLblFov() {
		if (lblFov == null) {
			lblFov = new JLabel("Fov");
			lblFov.setBounds(12, 332, 57, 15);
		}
		return lblFov;
	}
	private JTextField getTextField_Fov() {
		if (textField_Fov == null) {
			textField_Fov = new JTextField();
			textField_Fov.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent e) {
					// 선택된 CCTV의 Fov가 변경되었을 때 발생하는 이벤트
					if(canvasPanel.getSelectedCCTV() != null && !textField_Fov.getText().equals("")) {
						if(isNumeric(textField_Fov.getText())) {
							canvasPanel.getSelectedCCTV().setFov(Double.parseDouble(textField_Fov.getText()));
						}
					}
				}
			});
			textField_Fov.setBounds(81, 329, 107, 21);
			textField_Fov.setColumns(10);
		}
		return textField_Fov;
	}
	private JLabel getLblAspect() {
		if (lblAspect == null) {
			lblAspect = new JLabel("Aspect");
			lblAspect.setBounds(12, 357, 57, 15);
		}
		return lblAspect;
	}
	private JTextField getTextField_Aspect() {
		if (textField_Aspect == null) {
			textField_Aspect = new JTextField();
			textField_Aspect.addFocusListener(new FocusAdapter() {
				// 선택된 CCTV의 영상의 Aspect가 변경되었을 때 발생하는 이벤트
				@Override
				public void focusLost(FocusEvent e) {
					if(canvasPanel.getSelectedCCTV() != null && !textField_Aspect.getText().equals("")) {
						if(isNumeric(textField_Aspect.getText())) {
							canvasPanel.getSelectedCCTV().setAspect(Double.parseDouble(textField_Aspect.getText()));
						}
					}
				}
			});
			textField_Aspect.setBounds(81, 354, 107, 21);
			textField_Aspect.setColumns(10);
		}
		return textField_Aspect;
	}
	private JLabel getLblMappedState() {
		if (lblMappedState == null) {
			lblMappedState = new JLabel("State");
			lblMappedState.setBounds(12, 382, 67, 15);
		}
		return lblMappedState;
	}
	private JTextField getTextField_MappedState() {
		if (textField_MappedState == null) {
			textField_MappedState = new JTextField();
			textField_MappedState.setEditable(false);
			textField_MappedState.setBounds(81, 379, 107, 21);
			textField_MappedState.setColumns(10);
		}
		return textField_MappedState;
	}
	/**
	 * 문자열이 실수인지 판단하는 함수
	 * @param str
	 * @return
	 */
	private boolean isNumeric(String str) {
		try {
			double d = Double.parseDouble(str);
		} catch(NumberFormatException e) {
			return false;
		}
		return true;
	}
	/**
	 * CCTV의 속성 정보를 표시하는 함수
	 * @param cctv
	 */
	public void setCCTVProperties(CCTV cctv) {
		textField_CCTVID.setText("");
		textField_CCTVIP.setText("");
		textField_SourceLocation.setText("");
		textField_URLID.setText("");
		textField_URLPassword.setText("");
		textField_Width.setText("");
		textField_Height.setText("");
		textField_Framerate.setText("");
		textField_Orientation.setText("");
		textField_Aspect.setText("");
		textField_Fov.setText("");
		textField_MappedState.setText("");
		
		textField_CCTVID.setText(cctv.getCctvID());
		textField_CCTVIP.setText(cctv.getIp());
		textField_SourceLocation.setText(cctv.getSourceLocation());
		textField_URLID.setText(cctv.getId());
		textField_URLPassword.setText(cctv.getPassword());
		if (cctv.getWidth() != 0) {
			textField_Width.setText(String.valueOf(cctv.getWidth()));
		}
		if (cctv.getHeight() != 0) {
			textField_Height.setText(String.valueOf(cctv.getHeight()));
		}
		if (cctv.getFramerate() != 0) {
			textField_Framerate.setText(String.valueOf(cctv.getFramerate()));			
		}
		if (cctv.getOrientation() != 0) {
			textField_Orientation.setText(String.valueOf(cctv.getOrientation()));
		}
		if (cctv.getAspect() != 0) {
			textField_Aspect.setText(String.valueOf(cctv.getAspect()));
		}
		if (cctv.getFov() != 0) {
			textField_Fov.setText(String.valueOf(cctv.getFov()));
		}
		if (cctv.getMappedState() != null) {
			textField_MappedState.setText(cctv.getMappedState().getGmlID());
		}
		((SpinnerDateModel) spinner_DateTime.getModel()).setValue(cctv.getInstallationTime().getTime());
		
		panel_CCTVProperties.setVisible(true);
	}
	private JLabel getLblInstallatinonTime() {
		if (lblInstallatinonTime == null) {
			lblInstallatinonTime = new JLabel("Installatinon Time");
			lblInstallatinonTime.setBounds(12, 119, 107, 15);
		}
		return lblInstallatinonTime;
	}
	private JSpinner getSpinner_DateTime() {
		if (spinner_DateTime == null) {
			SpinnerModel model = new SpinnerDateModel();
			spinner_DateTime = new JSpinner(new SpinnerDateModel(new Date(1453788930252L), new Date(1390716930252L), null, Calendar.DAY_OF_MONTH));
			spinner_DateTime.addChangeListener(new ChangeListener() {
				// 선택된 CCTV의 Installation Time이 변경되었을 때 발생하는 이벤트
				public void stateChanged(ChangeEvent arg0) {
					SpinnerDateModel model = (SpinnerDateModel) spinner_DateTime.getModel();
					canvasPanel.getSelectedCCTV().getInstallationTime().setTime(model.getDate());
				}
			});
			spinner_DateTime.setBounds(59, 144, 129, 22);
		}
		return spinner_DateTime;
	}
	private JTable getTable_CCTVList() {
		if (table_CCTVList == null) {
			table_CCTVList = new JTable();
			table_CCTVList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			table_CCTVList.addMouseListener(new MouseAdapter() {
				// CCTV의 목록에서 CCTV가 선택되었을 때 발생하는 이벤트
				@Override
				public void mouseClicked(MouseEvent arg0) {
					int selectedRow = table_CCTVList.getSelectedRow();
					if(selectedRow >= 0 && selectedRow < table_CCTVList.getRowCount()) {
						// 목록의 CCTV 이름과 동일한 CCTV를 찾는다.
						CCTV selectedCCTV = searchCCTVByID((String) table_CCTVList.getValueAt(selectedRow, 0));
						if(selectedCCTV != null) {
							canvasPanel.setSelectedCCTV(selectedCCTV);
					        setCCTVProperties(selectedCCTV);
							canvasPanel.repaint();
						}
					}
				}
			});
			table_CCTVList.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"CCTV ID"
				}
			) {
				Class[] columnTypes = new Class[] {
					String.class
				};
				public Class getColumnClass(int columnIndex) {
					return columnTypes[columnIndex];
				}
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			});
			
		}
		return table_CCTVList;
	}
	/**
	 * 현재 층의 CCTVOnFloor에서 동일한 CCTV ID를 가지는 CCTV를 검색한다.
	 * @param cctvID
	 * @return
	 */
	private CCTV searchCCTVByID(String cctvID) {
		ArrayList<CCTV> cctvList = currentProject.getCurrentCCTVOnFloor().getCCTVMember();
		for(CCTV cctv : cctvList) {
			if(cctv.getCctvID() != null && cctv.getCctvID().equals(cctvID)) {
				return cctv;
			}
		}
		
		return null;
	}
	/**
	 * CCTV의 ID를 표시하는 Tale의 목록을 갱신한다.
	 */
	public void updateCCTVTableModel() {
		DefaultTableModel model = (DefaultTableModel) table_CCTVList.getModel();
		
		int rowCount = model.getRowCount();
		for(int i = 0; i < rowCount; i++) {
			model.removeRow(rowCount - i - 1);
		}
		
		ArrayList<CCTV> cctvList = currentProject.getCurrentCCTVOnFloor().getCCTVMember();
		for(CCTV cctv : cctvList) {
			model.addRow(new Object[]{cctv.getCctvID()});
		}
	}
	private JScrollPane getScrollPane_CCTVList() {
		if (scrollPane_CCTVList == null) {
			scrollPane_CCTVList = new JScrollPane();
			scrollPane_CCTVList.setWheelScrollingEnabled(true);
			scrollPane_CCTVList.setPreferredSize(new Dimension(80, 200));
		}
		return scrollPane_CCTVList;
	}
}
