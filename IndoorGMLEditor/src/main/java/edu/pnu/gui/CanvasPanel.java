package edu.pnu.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.SwingUtilities;

import net.opengis.indoorgml.core.AbstractFeature;
import net.opengis.indoorgml.core.CCTV;
import net.opengis.indoorgml.core.CCTVOnFloor;
import net.opengis.indoorgml.core.InterLayerConnection;
import net.opengis.indoorgml.core.SpaceLayer;
import net.opengis.indoorgml.core.State;
import net.opengis.indoorgml.core.Transition;
import net.opengis.indoorgml.geometry.LineString;
import net.opengis.indoorgml.geometry.Point;
import edu.pnu.project.EditState;
import edu.pnu.project.EditWorkState;
import edu.pnu.project.ProjectFile;
import edu.pnu.project.StateOnFloor;
import edu.pnu.project.TransitionOnFloor;
import edu.pnu.util.GeometryUtil;

public class CanvasPanel extends JPanel implements MouseListener, MouseMotionListener,
        MouseWheelListener, KeyListener {
    private MainFrame mainFrame = null;

    private ProjectFile project;

    private State stateEnd1 = null;

    private State stateEnd2 = null;

    private SpaceLayer spaceLayerEnd1 = null;

    private SpaceLayer spaceLayerEnd2 = null;

    private ArrayList<State> statesEnd1 = new ArrayList<State>();

    private ArrayList<State> statesEnd2 = new ArrayList<State>();

    private ArrayList<Point> transitionPoints = new ArrayList<Point>();

    private State selectedState = null;

    private HashMap<State, Color> selectedStateMap = new HashMap<State, Color>();

    private Transition selectedTransition = null;

    private HashMap<Transition, Color> selectedTransitionMap = new HashMap<Transition, Color>();

    private CCTV selectedCCTV = null;
    
    private BufferedImage floorPlan = null;

    private int floorPlanWidth = 0;

    private int floorPlanHeight = 0;

    private double floorPlanScale = 1.0;

    private int previousMouseX;

    private int previousMouseY;

    private boolean isMouseDown = false;

    private int currentKeyEvent = KeyEvent.KEY_RELEASED;

    private JPopupMenu popupMenu_State;

    private JMenuItem mntmStateDuality;

    private JPopupMenu popupMenu_Transition;

    private JMenuItem mntmTransitionDuality;

    private JMenuItem mntmStateProperties;

    private JMenuItem mntmTransitionProperties;

    private JPopupMenu popupMenu_CCTV;
    private JMenuItem mntmNewMenuItem;

    /**
     * Create the panel.
     */
    public CanvasPanel() {
        addPopup(this, getPopupMenu_State());
        add(getPopupMenu_Transition());
        add(getPopupMenu_CCTV());
    }

    public CanvasPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
        this.addKeyListener(this);
        setLayout(null);

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub
        int currentMouseX = e.getX();
        int currentMouseY = e.getY();

        if (!SwingUtilities.isLeftMouseButton(e))
            return;

        EditState currentEditState = project.getEditState();
        //EditWorkState currentEditWorkState = project.getEditWorkState();
        
        // State나 CCTV의 좌표를 현재 좌표로 이동시킨다.
        if (currentEditState == EditState.SELECT_STATE || currentEditState == EditState.MOVE_STATE) {
            for (State state : selectedStateMap.keySet()) {
                movePoint(state.getPosition(), previousMouseX, previousMouseY, currentMouseX,
                        currentMouseY);
            }

            project.setEditState(EditState.MOVE_STATE);
        } else if (currentEditState == EditState.SELECT_CCTV || currentEditState == EditState.MOVE_CCTV) {
            movePoint(selectedCCTV.getLocation(), previousMouseX, previousMouseY, currentMouseX, currentMouseY);
        }        

        previousMouseX = currentMouseX;
        previousMouseY = currentMouseY;

        repaint();
    }

    private void movePoint(Point p, int x1, int y1, int x2, int y2) {
        double panelX1 = (double) x1 / (floorPlanWidth * floorPlanScale);
        double panelY1 = (double) y1 / (floorPlanHeight * floorPlanScale);
        double panelX2 = (double) x2 / (floorPlanWidth * floorPlanScale);
        double panelY2 = (double) y2 / (floorPlanHeight * floorPlanScale);
        double offsetX = panelX2 - panelX1;
        double offsetY = panelY2 - panelY1;

        p.setPanelRatioX(p.getPanelRatioX() + offsetX);
        p.setPanelRatioY(p.getPanelRatioY() + offsetY);
        setPanelXYForCurrentScale(p);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        this.requestFocus();

        previousMouseX = e.getX();
        previousMouseY = e.getY();

        if (project.getEditState() == null)
            return;
        
        EditState currentEditState = project.getEditState();
        EditWorkState currentEditWorkState = project.getEditWorkState();
        if (e.getButton() == 1) { // 1: left button
            if (currentEditState == EditState.CREATE_STATE) {
                StateOnFloor stateOnFloor = project.getCurrentStateOnFloor();

                floorPlan = project.getCurrentFloorPlan();
                if (floorPlan == null)
                    return;
                floorPlanWidth = floorPlan.getWidth();
                floorPlanHeight = floorPlan.getHeight();
                floorPlanScale = project.getCurrentFloorPlanScale();

                State state = new State();
                Point point = new Point();

                if (e.getX() > floorPlanWidth * floorPlanScale)
                    return;
                if (e.getY() > floorPlanHeight * floorPlanScale)
                    return;

                point.setPanelX(e.getX());
                point.setPanelY(e.getY());
                setPanelRatioXY(point);

                state.setPosition(point);
                stateOnFloor.getStateMember().add(state);
            } else if (currentEditState == EditState.CREATE_TRANSITION) {
                selectedState = searchAdjacencyState(e);
                if (selectedState != null) {
                    if (stateEnd1 == null) {
                        stateEnd1 = selectedState;
                        selectedStateMap.put(stateEnd1, Color.YELLOW);
                        spaceLayerEnd1 = project.getCurrentSpaceLayer();

                        transitionPoints.add(selectedState.getPosition());
                        System.out.println("transitionEnd1");
                    } else if (stateEnd1 != null && stateEnd2 == null
                            && !stateEnd1.equals(selectedState)) {
                        stateEnd2 = selectedState;
                        selectedStateMap.put(stateEnd2, Color.YELLOW);
                        spaceLayerEnd2 = project.getCurrentSpaceLayer();

                        transitionPoints.add(selectedState.getPosition());
                        System.out.println("transitionEnd2");
                    }
                } else if (stateEnd1 != null && selectedState == null) {
                    Point point = new Point();

                    if (e.getX() > floorPlanWidth * floorPlanScale)
                        return;
                    if (e.getY() > floorPlanHeight * floorPlanScale)
                        return;

                    point.setPanelRatioX((double) e.getX() / (floorPlanWidth * floorPlanScale));
                    point.setPanelRatioY((double) e.getY() / (floorPlanHeight * floorPlanScale));

                    transitionPoints.add(point);
                }

                if (stateEnd1 != null && stateEnd2 != null) {
                    TransitionOnFloor transitionOnFloor = project.getCurrentTransitionOnFloor();
                    Transition transition = new Transition();
                    LineString path = new LineString();
                    ArrayList<Point> pathPoints = path.getPoints();

                    pathPoints.addAll(transitionPoints);
                    path.setPoints(pathPoints);

                    transition.setStates(new State[] { stateEnd1, stateEnd2 });
                    transition.setPath(path);

                    transitionOnFloor.getTransitionMember().add(transition);
                    stateEnd1.getTransitionReference().add(transition);
                    stateEnd2.getTransitionReference().add(transition);

                    transitionPoints.clear();
                    stateEnd1 = null;
                    stateEnd2 = null;
                    System.out.println("create transition");
                }
            } else if (currentEditState == EditState.CREATE_INTERLAYERCONNECTION) {
                System.out.println("mousepressed_createinterlayerconnection");
                if (currentKeyEvent == KeyEvent.KEY_RELEASED) {
                    if (currentEditWorkState == EditWorkState.CREATE_INTERLAYERCONNECTION_SELECTEND1) {
                        for (State state : statesEnd1) {
                            if (selectedStateMap.containsKey(state)) {
                                selectedStateMap.remove(state);
                            }
                        }
                        statesEnd1.clear();
                    } else if (currentEditWorkState == EditWorkState.CREATE_INTERLAYERCONNECTION_SELECTEND2) {
                        for (State state : statesEnd2) {
                            if (selectedStateMap.containsKey(state)) {
                                selectedStateMap.remove(state);
                            }
                        }
                        statesEnd2.clear();
                    }
                }
                if (currentEditWorkState == EditWorkState.CREATE_INTERLAYERCONNECTION_SELECTEND1
                        && spaceLayerEnd1 != project.getCurrentSpaceLayer()) {
                    for (State state : statesEnd1) {
                        if (selectedStateMap.containsKey(state)) {
                            selectedStateMap.remove(state);
                        }
                    }
                    statesEnd1.clear();
                } else if (currentEditWorkState == EditWorkState.CREATE_INTERLAYERCONNECTION_SELECTEND2
                        && spaceLayerEnd2 != project.getCurrentSpaceLayer()) {
                    for (State state : statesEnd2) {
                        if (selectedStateMap.containsKey(state)) {
                            selectedStateMap.remove(state);
                        }
                    }
                    statesEnd2.clear();
                }

                selectedState = searchAdjacencyState(e);
                if (selectedState != null) {
                    if (currentEditWorkState == EditWorkState.CREATE_INTERLAYERCONNECTION_SELECTEND1) {
                        statesEnd1.add(selectedState);
                        selectedStateMap.put(selectedState, Color.YELLOW);

                        spaceLayerEnd1 = project.getCurrentSpaceLayer();
                        System.out.println("statesEnd1");
                    } else if (!statesEnd1.contains(selectedState)
                            && currentEditWorkState == EditWorkState.CREATE_INTERLAYERCONNECTION_SELECTEND2) {
                        statesEnd2.add(selectedState);
                        selectedStateMap.put(selectedState, Color.MAGENTA);

                        spaceLayerEnd2 = project.getCurrentSpaceLayer();
                        System.out.println("statesEnd2");
                    }
                }
            } else if (currentEditState == EditState.NONE
                    || currentEditState == EditState.SELECT_STATE
                    || currentEditState == EditState.SELECT_CELLSPACE
                    || currentEditState == EditState.SELECT_TRANSITION
                    || currentEditState == EditState.SELECT_CELLSPACEBOUNDARY
                    || currentEditState == EditState.SELECT_CCTV) {
                boolean isSelected = false;
                selectedState = null;
                selectedTransition = null;
                selectedCCTV = null;
                if (currentKeyEvent == KeyEvent.KEY_RELEASED) {
                    selectedStateMap.clear();
                    selectedTransitionMap.clear();
                }

                selectedState = searchAdjacencyState(e);
                if (selectedState != null) {
                    if (currentEditState != EditState.SELECT_STATE) {
                        selectedStateMap.clear();
                    }
                    selectedStateMap.put(selectedState, Color.YELLOW);

                    project.setEditState(EditState.SELECT_STATE);
                    isSelected = true;
                    
                    String selectedStateIDs = "";
                    for(State selected : selectedStateMap.keySet()) {
                        selectedStateIDs += selected.getGmlID() + ", ";
                        //System.out.println("State : " + selected.getGmlID() + ", " + selected.getPosition().getPanelRatioX() + " " + (1 - selected.getPosition().getPanelRatioY()));
                    }
                    selectedStateIDs = selectedStateIDs.substring(0, selectedStateIDs.length() - 2);
                    mainFrame.setLabel_CurrentEditState("Selected State : " + selectedStateIDs );
                }
                
                // CCTV
                if (!isSelected) {
                	selectedCCTV = searchAdjacencyCCTV(e);
                	if (selectedCCTV != null) {
                		if(selectedCCTV.getMappedState() != null) {
                			selectedStateMap.put(selectedCCTV.getMappedState(), Color.BLUE);
                		}
                		
                		project.setEditState(EditState.SELECT_CCTV);
                		isSelected = true;
                		
                		mainFrame.setCCTVProperties(selectedCCTV);
                		mainFrame.setLabel_CurrentEditState("Selected CCTV : " + selectedCCTV.getCctvID());
                	}
                }
                

                if (!isSelected) {
                    selectedTransition = searchAdjacencyTransition(e);
                    if (selectedTransition != null) {
                        if (currentEditState != EditState.SELECT_TRANSITION) {
                            selectedTransitionMap.clear();
                        }
                        selectedTransitionMap.put(selectedTransition, Color.YELLOW);

                        project.setEditState(EditState.SELECT_TRANSITION);
                        isSelected = true;
                        
                        String selectedTransitionIDs = "";
                        for(Transition selected : selectedTransitionMap.keySet()) {
                            selectedTransitionIDs += selected.getGmlID() + ", ";
                        }
                        selectedTransitionIDs = selectedTransitionIDs.substring(0, selectedTransitionIDs.length() - 2);
                        mainFrame.setLabel_CurrentEditState("Selected Transition : " + selectedTransitionIDs);
                    }
                }

               if (!isSelected) {
                    project.setEditState(EditState.NONE);
                    mainFrame.setLabel_CurrentEditState("");
                    mainFrame.setCCTVPropertiesVisible(false);
                    selectedStateMap.clear();
                    selectedTransitionMap.clear();
                }
            } else if (currentEditState == EditState.CREATE_CCTV_MAPPEDSTATE) {
            	selectedState = searchAdjacencyState(e);
            	
            	if (selectedState != null) {
            		if(selectedCCTV.getMappedState() != null) {
            			selectedStateMap.remove(selectedCCTV.getMappedState());
            			selectedCCTV.getMappedState().getCCTVList().remove(selectedCCTV);
            		}
            		
            		if(!selectedState.getCCTVList().contains(selectedCCTV)) {
            			selectedState.getCCTVList().add(selectedCCTV);
            		}
            		selectedCCTV.setMappedState(selectedState);
            		selectedStateMap.put(selectedState, Color.CYAN);
            		System.out.println("create mapping state for cctv");
            	}
            	project.setEditState(EditState.NONE);
            	mainFrame.setLabel_CurrentEditState("");
            	mainFrame.setCCTVProperties(selectedCCTV);
            	mainFrame.updateCCTVTableModel();
            } else if (currentEditState == EditState.CREATE_CCTV) {
            	CCTVOnFloor cctvOnFloor = project.getCurrentCCTVOnFloor();

                floorPlan = project.getCurrentFloorPlan();
                if (floorPlan == null)
                    return;
                floorPlanWidth = floorPlan.getWidth();
                floorPlanHeight = floorPlan.getHeight();
                floorPlanScale = project.getCurrentFloorPlanScale();

                CCTV cctv = new CCTV();
                Point point = new Point();

                if (e.getX() > floorPlanWidth * floorPlanScale)
                    return;
                if (e.getY() > floorPlanHeight * floorPlanScale)
                    return;

                point.setPanelX(e.getX());
                point.setPanelY(e.getY());
                setPanelRatioXY(point);

                cctv.setLocation(point);
                cctvOnFloor.getCCTVMember().add(cctv);
                
                selectedCCTV = cctv;
                
                project.setEditState(EditState.SELECT_CCTV);
                mainFrame.setLabel_CurrentEditState("");
                mainFrame.setCCTVProperties(selectedCCTV);
                mainFrame.updateCCTVTableModel();
                System.out.println("cctv created");
            }
        } else if (e.getButton() == 3) { // 우클릭
            boolean selected = false;
            selectedState = searchAdjacencyState(e);
            if (selectedState != null) {
                if (!selectedStateMap.containsKey(selectedState)) {
                    selectedStateMap.clear();
                }
                selectedStateMap.put(selectedState, Color.YELLOW);

                project.setEditState(EditState.SELECT_STATE);

                System.out.println("select state");
                selected = true;
                getPopupMenu_State().show(this, e.getX(), e.getY());
            }
            
            if(!selected) {
            	selectedCCTV = searchAdjacencyCCTV(e);
            	
            	if(selectedCCTV != null) {
            		if(selectedCCTV.getMappedState() != null) {
            			selectedStateMap.put(selectedCCTV.getMappedState(), Color.CYAN);
            		}
            		
            		project.setEditState(EditState.SELECT_CCTV);
            		mainFrame.setCCTVProperties(selectedCCTV);
            		
            		System.out.println("select cctv");
            		selected = true;
            		getPopupMenu_CCTV().show(this, e.getX(), e.getY());
            	}
            	
            }

            if (!selected) {
                selectedTransition = searchAdjacencyTransition(e);
                if (selectedTransition != null) {
                    if (!selectedTransitionMap.containsKey(selectedTransition)) {
                        selectedTransitionMap.clear();
                    }
                    selectedTransitionMap.put(selectedTransition, Color.YELLOW);

                    project.setEditState(EditState.SELECT_TRANSITION);

                    System.out.println("select transition");
                    selected = true;
                    getPopupMenu_Transition().show(this, e.getX(), e.getY());
                }
            }

            if (!selected) {
                project.setEditState(EditState.NONE);
                mainFrame.setLabel_CurrentEditState("");
                selectedStateMap.clear();
                selectedTransitionMap.clear();
            }
        }

        repaint();
    }

    public State searchAdjacencyState(MouseEvent e) {
        State adjacencyState = null;
        StateOnFloor stateOnFloor = project.getCurrentStateOnFloor();
        ArrayList<State> stateMember = stateOnFloor.getStateMember();
        for (State state : stateMember) {
            if (isAdjacencyPointToPoint(state.getPosition(), e.getX(), e.getY())) {
                adjacencyState = state;

                System.out.println("select state");
                break;
            }
        }

        return adjacencyState;
    }
    
    public CCTV searchAdjacencyCCTV(MouseEvent e) {
        CCTV adjacencyCCTV = null;
        CCTVOnFloor cctvOnFloor = project.getCurrentCCTVOnFloor();
        ArrayList<CCTV> cctvMember = cctvOnFloor.getCCTVMember();
        for (CCTV cctv : cctvMember) {
            if (isAdjacencyPointToPoint(cctv.getLocation(), e.getX(), e.getY())) {
                adjacencyCCTV = cctv;

                System.out.println("select cctv");
                break;
            }
        }

        return adjacencyCCTV;
    }

    public Transition searchAdjacencyTransition(MouseEvent e) {
        Transition adjacencyTransition = null;
        TransitionOnFloor transitionOnFloor = project.getCurrentTransitionOnFloor();
        ArrayList<Transition> transitionMember = transitionOnFloor.getTransitionMember();
        for (Transition transition : transitionMember) {
            if (isAdjacencyPointToLineString(transition.getPath(), e.getX(), e.getY())) {
                adjacencyTransition = transition;

                System.out.println("select transition");
                break;
            }
        }

        return adjacencyTransition;
    }

    // /

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        this.requestFocus();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        isMouseDown = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub

        isMouseDown = false;
        EditState currentEditState = project.getEditState();
        if (currentEditState == EditState.MOVE_STATE) {
            project.setEditState(EditState.SELECT_STATE);
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int direction = e.getWheelRotation();
        double scale = project.getCurrentFloorPlanScale();

        if (currentKeyEvent != KeyEvent.VK_CONTROL){
            JScrollBar verticalScrollBar = mainFrame.getScrollPane().getVerticalScrollBar();
            int currentValue = verticalScrollBar.getValue();
            int increment = verticalScrollBar.getUnitIncrement();
            if(direction > 0) {
                verticalScrollBar.setValue(currentValue + increment * 2);
            } else {
                verticalScrollBar.setValue(currentValue - increment * 2);
            }
            return;
        }

        if (direction > 0) {
            if (scale > 0.3) {
                scale -= 0.1;
            }
        } else {
            if (scale < 5.0) {
                scale += 0.1;
            }
        }
        project.setCurrentFloorPlanScale(scale);

        mainFrame.resizePanelPrefferedDimension(
                (int) (project.getCurrentFloorPlan().getWidth() * scale), (int) (project
                        .getCurrentFloorPlan().getHeight() * scale));
        mainFrame.getScrollPane().revalidate();
        mainFrame.getScrollPane().repaint();
        
        //mainFrame.getScrollPane().getViewport().setViewPosition(new java.awt.Point(e.getX(), e.getY()));

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
        if (project.getEditState() == null)
            return;
        EditState state = project.getEditState();
        EditWorkState workState = project.getEditWorkState();
        currentKeyEvent = e.getKeyCode();
        switch (e.getKeyCode()) {
        case KeyEvent.VK_ESCAPE:
            keyPressESCAPE(state);
            
            /*if (state == EditState.CREATE_STATE) {
                project.setEditState(EditState.NONE);
            } else if (state == EditState.CREATE_TRANSITION) {
                stateEnd1 = null;
                stateEnd2 = null;
                transitionPoints.clear();
            } else if (state == EditState.SELECT_STATE) {
                selectedState = null;
                selectedStateMap.clear();
            } else if (state == EditState.CREATE_INTERLAYERCONNECTION) {
                statesEnd1.clear();
                statesEnd2.clear();
            } else if (state == EditState.CREATE_CELLSPACE) {
                cellSpaceCreatingLineStrings.clear();
            } else if (state == EditState.SELECT_CELLSPACE) {
                selectedCellSpace = null;
                selectedCellSpaceMap.clear();
            } else if (state == EditState.CREATE_CELLSPACE_DUALITY) {

            } else if (state == EditState.CREATE_CELLSPACEBOUNDARY_DUALITY) {

            } else if (state == EditState.CREATE_TRANSITION_DUALITY) {

            } else if (state == EditState.CREATE_STATE_DUALITY) {

            } else if (state == EditState.CREATE_CELLSPACE_AS_DOOR) {

            } else if (state == EditState.CREATE_CELLSPACEBOUNDARY_AS_DOOR) {
                doorPointList.clear();
            }

            project.setEditState(EditState.NONE);
            mainFrame.setLabel_CurrentEditState("");*/
            break;
        case KeyEvent.VK_DELETE:
            if (state == EditState.SELECT_STATE) {
                // project.deleteState(selectedState);
                for (State selected : selectedStateMap.keySet()) {
                    project.deleteState(selected);
                }
                selectedState = null;
                selectedStateMap.clear();
                project.setEditState(EditState.NONE);
            } else if (state == EditState.SELECT_TRANSITION) {
                for (Transition selected : selectedTransitionMap.keySet()) {
                    project.deleteTransition(selected);
                }
                selectedTransition = null;
                selectedTransitionMap.clear();
                project.setEditState(EditState.NONE);
                mainFrame.setLabel_CurrentEditState("");
            } else if(state == EditState.SELECT_CCTV) {
            	project.deleteCCTV(selectedCCTV);
            	selectedCCTV = null;
            	project.setEditState(EditState.NONE);
            	mainFrame.setLabel_CurrentEditState("");
            	mainFrame.updateCCTVTableModel();
            }
            break;
        case KeyEvent.VK_ENTER:
            if (state == EditState.CREATE_INTERLAYERCONNECTION) {
                if (workState == EditWorkState.CREATE_INTERLAYERCONNECTION_SELECTEND1) {
                    System.out.println("interlayerconnection_end2");
                    workState = EditWorkState.CREATE_INTERLAYERCONNECTION_SELECTEND2;
                    project.setEditWorkState(workState);
                    mainFrame.setLabel_CurrentEditState("Create InterLayerConnection : Choose the other state(or states) and press Enter key");
                } else if (workState == EditWorkState.CREATE_INTERLAYERCONNECTION_SELECTEND2) {
                    workState = EditWorkState.CREATE_INTERLAYERCONNECTION_CREATE;
                    project.setEditWorkState(workState);
                    mainFrame.setLabel_CurrentEditState(workState.toString());
                    System.out.println("interlayerconnection_create");
                    //
                    createInterLayerConnection();

                    project.setEditState(EditState.NONE);
                    project.setEditWorkState(EditWorkState.NONE);
                    mainFrame.setLabel_CurrentEditState("");
                }
            } 

            break;
        case KeyEvent.VK_CONTROL:
            System.out.println("pressed control");
            break;
        }

        repaint();
    }
    
    public void keyPressESCAPE(EditState state) {
        if (state == EditState.CREATE_STATE) {
            project.setEditState(EditState.NONE);
        } else if (state == EditState.CREATE_TRANSITION) {
            stateEnd1 = null;
            stateEnd2 = null;
            transitionPoints.clear();
        } else if (state == EditState.SELECT_STATE) {
            selectedState = null;
            selectedStateMap.clear();
        } else if (state == EditState.CREATE_INTERLAYERCONNECTION) {
            statesEnd1.clear();
            statesEnd2.clear();
        } else if (state == EditState.CREATE_TRANSITION_DUALITY) {

        } else if (state == EditState.CREATE_STATE_DUALITY) {

        } else if (state == EditState.CREATE_CCTV) {
        	selectedState = null;
        	mainFrame.setCCTVPropertiesVisible(false);
        } else if (state == EditState.SELECT_CCTV) {
        	selectedState = null;
        	mainFrame.setCCTVPropertiesVisible(false);
        }

        project.setEditState(EditState.NONE);
        mainFrame.setLabel_CurrentEditState("");
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        currentKeyEvent = KeyEvent.KEY_RELEASED;
        System.out.println("release");
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        System.out.println("typed");
    }

    @Override
    protected void paintComponent(Graphics g) {
        // TODO Auto-generated method stub
        super.paintComponent(g);
        if (project == null)
            return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(2));

        if (project.getCurrentFloorPlan() != null) { // floorplan이 변경될 때만 출력하도록 변경
            // System.out.println(project.getCurrentFloorPlan());
            floorPlan = project.getCurrentFloorPlan();
            floorPlanWidth = floorPlan.getWidth();
            floorPlanHeight = floorPlan.getHeight();

            floorPlanScale = project.getCurrentFloorPlanScale();
            mainFrame.resizePanelPrefferedDimension((int) (floorPlanWidth * floorPlanScale),
                    (int) (floorPlanHeight * floorPlanScale));

            g.drawImage(floorPlan, 0, 0, (int) (floorPlanWidth * floorPlanScale),
                    (int) (floorPlanHeight * floorPlanScale), this);
        }

        // display transition(creating transition)
        for (int i = 0; i < transitionPoints.size(); i++) {
            setPanelXYForCurrentScale(transitionPoints.get(i));
            double x1 = transitionPoints.get(i).getPanelX();
            double y1 = transitionPoints.get(i).getPanelY();
            g2.setColor(Color.YELLOW);
            g2.draw(new Ellipse2D.Double(x1 - 5, y1 - 5, 10, 10));
            // g.fillOval(x1 - 5, y1 - 5, 10, 10);

            if (i == transitionPoints.size() - 1)
                break;

            setPanelXYForCurrentScale(transitionPoints.get(i + 1));
            double x2 = transitionPoints.get(i + 1).getPanelX();
            double y2 = transitionPoints.get(i + 1).getPanelY();
            g2.draw(new Ellipse2D.Double(x2 - 5, y2 - 5, 10, 10));

            g2.setColor(Color.BLUE);
            g2.draw(new Line2D.Double(x1, y1, x2, y2));
        }

        // display transition
        if (project.getCurrentTransitionOnFloor() != null) {
            ArrayList<Transition> transitionMember = project.getCurrentTransitionOnFloor()
                    .getTransitionMember();
            for (Transition transition : transitionMember) {
                displayTransition(g, transition, Color.BLUE, floorPlanWidth, floorPlanHeight,
                        floorPlanScale);
            }
        }

        if (project.getCurrentStateOnFloor() != null) {
            floorPlan = project.getCurrentFloorPlan();
            floorPlanScale = project.getCurrentFloorPlanScale();

            // display state

            ArrayList<State> stateList = project.getCurrentStateOnFloor().getStateMember();
            for (State state : stateList) {
                displayState(g, state, Color.RED, floorPlanWidth, floorPlanHeight, floorPlanScale);
            }
        }

        // display CCTV
        if (project.getCurrentCCTVOnFloor() != null) {
            floorPlan = project.getCurrentFloorPlan();
            floorPlanScale = project.getCurrentFloorPlanScale();

            // display cctv

            ArrayList<CCTV> cctvList = project.getCurrentCCTVOnFloor().getCCTVMember();
            for (CCTV cctv : cctvList) {
            	displayCCTV(g2, cctv, Color.GREEN, floorPlanWidth, floorPlanHeight, floorPlanScale);
            }
        }
        
        if (!selectedStateMap.isEmpty()) {
            for (State state : selectedStateMap.keySet()) {
                displayState(g, state, selectedStateMap.get(state), floorPlanWidth,
                        floorPlanHeight, floorPlanScale);
            }
        }
        if (!selectedTransitionMap.isEmpty()) {
            for (Transition transition : selectedTransitionMap.keySet()) {
                displayTransition(g, transition, selectedTransitionMap.get(transition),
                        floorPlanWidth, floorPlanHeight, floorPlanScale);
            }
        }
        if(selectedCCTV != null) {
        	displayCCTV(g2, selectedCCTV, Color.YELLOW, floorPlanWidth, floorPlanHeight, floorPlanScale);
        }
    }

    public void displayState(Graphics g, State state, Color color, int floorPlanWidth,
            int floorPlanHeight, double floorPlanScale) {
        int x = (int) (state.getPosition().getPanelRatioX() * floorPlanWidth * floorPlanScale);
        int y = (int) (state.getPosition().getPanelRatioY() * floorPlanHeight * floorPlanScale);
        if (hasInterLayerConnection(state)) {
            g.setColor(Color.PINK);
        } else {
            g.setColor(color);
        }
        g.fillOval(x - 5, y - 5, 10, 10);
    }

    public void displayTransition(Graphics g, Transition transition, Color color,
            int floorPlanWidth, int floorPlanHeight, double floorPlanScale) {
        ArrayList<State> stateMember = project.getCurrentStateOnFloor().getStateMember();
        State[] states = transition.getStates();

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(2));
        g2.setColor(color);

        ArrayList<Point> points = transition.getPath().getPoints();
        for (int i = 0; i < points.size() - 1; i++) {
            setPanelXYForCurrentScale(points.get(i));
            setPanelXYForCurrentScale(points.get(i + 1));
            double x1 = points.get(i).getPanelX();
            double y1 = points.get(i).getPanelY();
            double x2 = points.get(i + 1).getPanelX();
            double y2 = points.get(i + 1).getPanelY();

            g2.draw(new Line2D.Double(x1, y1, x2, y2));
        }

        double x = 0, y = 0;
        if (!stateMember.contains(states[0])) { // 다른 층의 state와 연결되었을 경우
            setPanelXYForCurrentScale(states[0].getPosition());

            x = states[0].getPosition().getPanelX();
            y = states[0].getPosition().getPanelY();
            g2.setColor(Color.lightGray);
            g2.draw(new Ellipse2D.Double(x - 5, y - 5, 10, 10));
        } else if (!stateMember.contains(states[1])) {
            setPanelXYForCurrentScale(states[1].getPosition());

            x = states[1].getPosition().getPanelX();
            y = states[1].getPosition().getPanelY();
            g2.setColor(Color.lightGray);
            g2.draw(new Ellipse2D.Double(x - 5, y - 5, 10, 10));
        }
    }
    
    public void displayCCTV(Graphics g, CCTV cctv, Color color, int floorPlanWidth, int floorPlanHeight, double floorPlanScale) {
    	setPanelXYForCurrentScale(cctv.getLocation());
    	double x = cctv.getLocation().getPanelX();
    	double y = cctv.getLocation().getPanelY();
    	double orientation = cctv.getOrientation();
    	double fov = cctv.getFov();
    	
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(2));
        g2.setColor(Color.GREEN);
        
        g2.fill(new Ellipse2D.Double(x - 5, y - 5, 10, 10));
        g2.setColor(color);
        g2.fill(new Arc2D.Double(x - 25, y - 25, 50, 50, orientation - (fov / 2), fov, Arc2D.PIE));
    }

    public boolean hasInterLayerConnection(State state) {
        ArrayList<InterLayerConnection> ilcMember = project.getMultiLayeredGraph().getInterEdges()
                .get(0).getInterLayerConnectionMember();
        for (InterLayerConnection ilc : ilcMember) {
            State[] interConnects = ilc.getInterConnects();
            if (interConnects[0] == state || interConnects[1] == state) {
                return true;
            }
        }

        return false;
    }

    public boolean isAdjacencyPointToPoint(Point point, int x, int y) {
        boolean isAdjacency = false;
        double snapBounds = 10;

        setPanelXYForCurrentScale(point);
        double pointX = point.getPanelX();
        double pointY = point.getPanelY();

        if ((Math.abs(pointX - x) <= snapBounds) && (Math.abs(pointY - y) <= snapBounds)) {
            isAdjacency = true;
        }

        return isAdjacency;
    }

    public boolean isAdjacencyPointToLineString(LineString lineString, double x, double y) {
        boolean isAdjacency = false;
        double snapBounds = 10;

        ArrayList<Point> points = lineString.getPoints();
        for (int i = 0; i < points.size() - 1; i++) {
            Point p1 = points.get(i);
            Point p2 = points.get(i + 1);

            setPanelXYForCurrentScale(p1);
            setPanelXYForCurrentScale(p2);
            double x1 = p1.getPanelX();
            double y1 = p1.getPanelY();
            double x2 = p2.getPanelX();
            double y2 = p2.getPanelY();

            double lowerX, lowerY, upperX, upperY;
            if (x1 < x2) {
                lowerX = x1;
                upperX = x2;
            } else {
                lowerX = x2;
                upperX = x1;
            }
            if (y1 < y2) {
                lowerY = y1;
                upperY = y2;
            } else {
                lowerY = y2;
                upperY = y1;
            }

            if (!(lowerX <= x && x <= upperX && lowerY <= y && y <= upperY))
                continue;

            double distance = GeometryUtil.getDistancePointToLine(x1, y1, x2, y2, x, y);
            if (distance <= snapBounds) {
                isAdjacency = true;
            }
        }

        return isAdjacency;
    }

    public void setPanelXYForCurrentScale(Point p) {
        double x = (p.getPanelRatioX() * floorPlanWidth * floorPlanScale);
        double y = (p.getPanelRatioY() * floorPlanHeight * floorPlanScale);

        p.setPanelX(x);
        p.setPanelY(y);
    }

    public void setPanelRatioXY(Point p) {
        double x = p.getPanelX();
        double y = p.getPanelY();

        double ratioX = ((double) x / (floorPlanWidth * floorPlanScale));
        double ratioY = ((double) y / (floorPlanHeight * floorPlanScale));
        /*
        DecimalFormat format = new DecimalFormat(".#####");
        String realXStr = format.format(ratioX);
        String realYStr = format.format(ratioY);
        ratioX = Double.parseDouble(realXStr);
        ratioY = Double.parseDouble(realYStr);
        */
        p.setPanelRatioX(ratioX);
        p.setPanelRatioY(ratioY);
    }

    public void createInterLayerConnection() {
        System.out.println("call createInteRlayerConnection");
        InterLayerConnectionDialog dialog = new InterLayerConnectionDialog(mainFrame, project,
                statesEnd1, statesEnd2, spaceLayerEnd1, spaceLayerEnd2);
        dialog.setModal(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);

        selectedStateMap.clear();
        statesEnd1.clear();
        statesEnd2.clear();
        spaceLayerEnd1 = null;
        spaceLayerEnd2 = null;

        repaint();
    }

    public ProjectFile getProject() {
        return project;
    }

    public void setProject(ProjectFile project) {
        this.project = project;
    }

    public void displayStateID() {

    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }

    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public State getStateEnd1() {
        return stateEnd1;
    }

    public void setStateEnd1(State stateEnd1) {
        this.stateEnd1 = stateEnd1;
    }

    public State getStateEnd2() {
        return stateEnd2;
    }

    public void setStateEnd2(State stateEnd2) {
        this.stateEnd2 = stateEnd2;
    }

    public SpaceLayer getSpaceLayerEnd1() {
        return spaceLayerEnd1;
    }

    public void setSpaceLayerEnd1(SpaceLayer spaceLayerEnd1) {
        this.spaceLayerEnd1 = spaceLayerEnd1;
    }

    public SpaceLayer getSpaceLayerEnd2() {
        return spaceLayerEnd2;
    }

    public void setSpaceLayerEnd2(SpaceLayer spaceLayerEnd2) {
        this.spaceLayerEnd2 = spaceLayerEnd2;
    }

    public ArrayList<State> getStatesEnd1() {
        return statesEnd1;
    }

    public void setStatesEnd1(ArrayList<State> statesEnd1) {
        this.statesEnd1 = statesEnd1;
    }

    public ArrayList<State> getStatesEnd2() {
        return statesEnd2;
    }

    public void setStatesEnd2(ArrayList<State> statesEnd2) {
        this.statesEnd2 = statesEnd2;
    }

    public State getSelectedState() {
        return selectedState;
    }

    public void setSelectedState(State selectedState) {
        this.selectedState = selectedState;
    }

    public HashMap<State, Color> getSelectedStateMap() {
        return selectedStateMap;
    }

    public void setSelectedStateMap(HashMap<State, Color> selectedStateMap) {
        this.selectedStateMap = selectedStateMap;
    }

    public BufferedImage getFloorPlan() {
        return floorPlan;
    }

    public void setFloorPlan(BufferedImage floorPlan) {
        this.floorPlan = floorPlan;
    }

    public int getFloorPlanWidth() {
        return floorPlanWidth;
    }

    public void setFloorPlanWidth(int floorPlanWidth) {
        this.floorPlanWidth = floorPlanWidth;
    }

    public int getFloorPlanHeight() {
        return floorPlanHeight;
    }

    public void setFloorPlanHeight(int floorPlanHeight) {
        this.floorPlanHeight = floorPlanHeight;
    }

    public double getFloorPlanScale() {
        return floorPlanScale;
    }

    public void setFloorPlanScale(double floorPlanScale) {
        this.floorPlanScale = floorPlanScale;
    }

    public boolean isMouseDown() {
        return isMouseDown;
    }

    public void setMouseDown(boolean isMouseDown) {
        this.isMouseDown = isMouseDown;
    }

    public int getCurrentKeyEvent() {
        return currentKeyEvent;
    }

    public void setCurrentKeyEvent(int currentKeyEvent) {
        this.currentKeyEvent = currentKeyEvent;
    }

    private JPopupMenu getPopupMenu_State() {
        if (popupMenu_State == null) {
            popupMenu_State = new JPopupMenu();
            popupMenu_State.add(getMntmStateDuality());
            popupMenu_State.add(getMntmStateProperties());
        }
        return popupMenu_State;
    }

    private static void addPopup(Component component, final JPopupMenu popup) {
        component.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showMenu(e);
                }
            }

            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showMenu(e);
                }
            }

            private void showMenu(MouseEvent e) {
                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        });
    }

    private void showPropertiesDialog(String type, AbstractFeature feature) {
            JDialog dialog = null;
            if(type.equalsIgnoreCase("STATE")) {
                    dialog = new StatePropertiesDialog((State) feature);
            } else if(type.equalsIgnoreCase("TRANSITION")) {
                    dialog = new TransitionPropertiesDialog((Transition) feature);
            }
            
            dialog.setModal(true);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
    }
    private JMenuItem getMntmStateDuality() {
        if (mntmStateDuality == null) {
            mntmStateDuality = new JMenuItem("Duality");
            mntmStateDuality.setEnabled(false);
            mntmStateDuality.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    project.setEditState(EditState.CREATE_STATE_DUALITY);
                }
            });
        }
        return mntmStateDuality;
    }

    private JPopupMenu getPopupMenu_Transition() {
        if (popupMenu_Transition == null) {
            popupMenu_Transition = new JPopupMenu();
            popupMenu_Transition.add(getMntmTransitionDuality());
            popupMenu_Transition.add(getMntmTransitionProperties());
        }
        return popupMenu_Transition;
    }

    private JMenuItem getMntmTransitionDuality() {
        if (mntmTransitionDuality == null) {
            mntmTransitionDuality = new JMenuItem("Duality");
            mntmTransitionDuality.setEnabled(false);
            mntmTransitionDuality.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    project.setEditState(EditState.CREATE_TRANSITION_DUALITY);
                }
            });
        }
        return mntmTransitionDuality;
    }

    private JMenuItem getMntmStateProperties() {
        if (mntmStateProperties == null) {
            mntmStateProperties = new JMenuItem("Properties");
            mntmStateProperties.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    showPropertiesDialog("State", selectedState);
                }
            });
        }
        return mntmStateProperties;
    }

    private JMenuItem getMntmTransitionProperties() {
        if (mntmTransitionProperties == null) {
            mntmTransitionProperties = new JMenuItem("Properties");
            mntmTransitionProperties.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    showPropertiesDialog("Transition", selectedTransition);
                }
            });
        }
        return mntmTransitionProperties;
    }
    
    public void setSelectedCCTV(CCTV selectedCCTV) {
    	selectedState = null;
        selectedTransition = null;
        selectedStateMap.clear();
        selectedTransitionMap.clear();
    	
    	this.selectedCCTV = selectedCCTV;
    }
    public CCTV getSelectedCCTV() {
    	return selectedCCTV;
    }
	private JPopupMenu getPopupMenu_CCTV() {
		if (popupMenu_CCTV == null) {
			popupMenu_CCTV = new JPopupMenu();
			popupMenu_CCTV.add(getMntmNewMenuItem());
		}
		return popupMenu_CCTV;
	}
	private JMenuItem getMntmNewMenuItem() {
		if (mntmNewMenuItem == null) {
			mntmNewMenuItem = new JMenuItem("Mapping state");
			mntmNewMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					project.setEditState(EditState.CREATE_CCTV_MAPPEDSTATE);
				}
			});
		}
		return mntmNewMenuItem;
	}
	
}
