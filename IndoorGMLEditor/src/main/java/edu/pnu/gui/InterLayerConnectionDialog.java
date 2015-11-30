package edu.pnu.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import net.opengis.indoorgml.core.InterLayerConnection;
import net.opengis.indoorgml.core.SpaceLayer;
import net.opengis.indoorgml.core.State;
import edu.pnu.project.ProjectFile;

public class InterLayerConnectionDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table_End1;
	private JScrollPane scrollPane_End1;
	private JTable table_End2;
	private JScrollPane scrollPane_End2;
	private JTextField textField_Comment;
	private JComboBox comboBox_Topology;
	
	private MainFrame mainFrame;
	private ProjectFile project;
	private ArrayList<State> statesEnd1;
	private ArrayList<State> statesEnd2;
	private SpaceLayer spaceLayerEnd1;
	private SpaceLayer spaceLayerEnd2;

	/**
	 * Launch the application.
	 *//*
	public static void main(String[] args) {
		try {
			InterLayerConnectionDialog dialog = new InterLayerConnectionDialog();
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
	public InterLayerConnectionDialog(MainFrame mainFrame, ProjectFile project, ArrayList<State> statesEnd1, ArrayList<State> statesEnd2, SpaceLayer spaceLayerEnd1, SpaceLayer spaceLayerEnd2) {
		this.mainFrame = mainFrame;
		this.project = project;
		this.statesEnd1 = statesEnd1;
		this.statesEnd2 = statesEnd2;
		this.spaceLayerEnd1 = spaceLayerEnd1;
		this.spaceLayerEnd2 = spaceLayerEnd2;
		
		setBounds(100, 100, 267, 381);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		contentPanel.add(getComboBox_Topology());
		
		contentPanel.add(getScrollPane_End1());
		getScrollPane_End1().setViewportView(getTable_End1());
		contentPanel.add(getScrollPane_End2());
		getScrollPane_End2().setViewportView(getTable_End2());
		{
			JLabel lblComment = new JLabel("Comment");
			lblComment.setBounds(12, 258, 57, 15);
			contentPanel.add(lblComment);
		}
		{
			textField_Comment = new JTextField();
			textField_Comment.setBounds(12, 283, 227, 21);
			contentPanel.add(textField_Comment);
			textField_Comment.setColumns(10);
		}

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						String topology = (String) comboBox_Topology.getSelectedItem();
						String comment = textField_Comment.getText();
						
						ArrayList<InterLayerConnection> ilcMember = project.getMultiLayeredGraph().getInterEdges().get(0).getInterLayerConnectionMember();
						for(State state1 : statesEnd1) {
							for(State state2 : statesEnd2) {
								InterLayerConnection interLayerConnection = new InterLayerConnection();
								State[] interConnects = new State[] { state1, state2 };
								SpaceLayer[] connectedLayers = new SpaceLayer[] { spaceLayerEnd1, spaceLayerEnd2 };
								
								interLayerConnection.setInterConnects(interConnects);
								interLayerConnection.setConnectedLayers(connectedLayers);
								interLayerConnection.setTopology(topology);
								interLayerConnection.setComment(comment);
								
								ilcMember.add(interLayerConnection);
							}
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
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
		updateTableModel();
	}
	private JScrollPane getScrollPane_End1() {
		if (scrollPane_End1 == null) {
			scrollPane_End1 = new JScrollPane();
			scrollPane_End1.setBounds(12, 41, 227, 90);
		}
		return scrollPane_End1;
	}
	private JTable getTable_End1() {
		if(table_End1 == null) {
			table_End1 = new JTable();
			table_End1.setModel(new DefaultTableModel(
				new Object[][] {
					{null, null, null},
				},
				new String[] {
					"SpaceLayer", "State", "Name"
				}
			) {
				Class[] columnTypes = new Class[] {
					String.class, String.class, String.class
				};
				public Class getColumnClass(int columnIndex) {
					return columnTypes[columnIndex];
				}
			});
			table_End1.getColumnModel().getColumn(0).setPreferredWidth(90);
			table_End1.setBounds(12, 41, 227, 90);
		}
		return table_End1;
	}
	private JScrollPane getScrollPane_End2() {
		if (scrollPane_End2 == null) {
			scrollPane_End2 = new JScrollPane();
			scrollPane_End2.setBounds(12, 141, 227, 107);
		}
		return scrollPane_End2;
	}
	private JTable getTable_End2() {
		if(table_End2 == null) {
			table_End2 = new JTable();
			table_End2.setModel(new DefaultTableModel(
				new Object[][] {
					{null, null, null},
				},
				new String[] {
					"SpaceLayer", "State", "Name"
				}
			) {
				Class[] columnTypes = new Class[] {
					String.class, String.class, String.class
				};
				public Class getColumnClass(int columnIndex) {
					return columnTypes[columnIndex];
				}
			});
			table_End2.getColumnModel().getColumn(0).setPreferredWidth(92);
			table_End2.setBounds(12, 141, 2, 2);
		}
		return table_End2;
	}
	private JComboBox getComboBox_Topology() {
		if(comboBox_Topology == null) {
			comboBox_Topology = new JComboBox();

			comboBox_Topology.setModel(new DefaultComboBoxModel(new String[] {"CONTAINS", "OVERLAPS", "EQUALS", "WITHIN", "CROSSES", "INTERSECTS"}));
			comboBox_Topology.setBounds(12, 10, 227, 21);
		}
		
		return comboBox_Topology;
	}
	private void updateTableModel() {
		DefaultTableModel model = (DefaultTableModel) table_End1.getModel();
		
		int rowCount = model.getRowCount();
		for(int i = 0; i < rowCount; i++) {
			model.removeRow(rowCount - i - 1);
		}
		System.out.println(model.getRowCount());
		for(State state : statesEnd1) {
			model.addRow(new Object[]{spaceLayerEnd1.getGmlID(), state.getGmlID(), state.getName()});
		}
		
		model = (DefaultTableModel) table_End2.getModel();
		
		rowCount = model.getRowCount();
		for(int i = 0; i < rowCount; i++) {
			model.removeRow(rowCount - i - 1);
		}
		System.out.println(model.getRowCount());
		for(State state : statesEnd2) {
			model.addRow(new Object[]{spaceLayerEnd2.getGmlID(), state.getGmlID(), state.getName()});
		}
	}
}
