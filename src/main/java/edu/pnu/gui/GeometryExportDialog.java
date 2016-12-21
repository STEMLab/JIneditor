package edu.pnu.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;

import net.opengis.indoorgml.core.CellSpaceBoundary;
import net.opengis.indoorgml.core.IndoorFeatures;
import edu.pnu.importexport.IndoorGMLExporter;
import edu.pnu.project.FloorProperty;
import edu.pnu.project.ProjectFile;
import edu.pnu.query.QuerySelector;
import edu.pnu.util.CellSpaceBoundaryBuilder2;
import edu.pnu.util.Geometry3DRemover;
import edu.pnu.util.IndoorGML3DGeometryBuilder;
import edu.pnu.util.IndoorGMLCloneGenerator;

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
						Geometry3DRemover.removeGeometry3D(project.getIndoorFeatures());
						
						// Cell과 Door사이에 대해 만들어지지 않은 boundary 생성
						CellSpaceBoundaryBuilder2 csbb = new CellSpaceBoundaryBuilder2(panel, project.getIndoorFeatures());
						csbb.build();
						
						// 에디터 상에서는 붙어 있는 벽, 문에 대한 boundary가 하나로 되어있지만
						// 실제 IndoorGML의 데이터 모델로는 boundary가 두개 생겨 양쪽에서 봤을 때 벽을 모두 볼 수 있어야 한다.
						// IndoorFeatures 객체를 복사하고, boundary와 transition을 하나씩 더 생성한다.
						IndoorGMLCloneGenerator cloneGenerator = new IndoorGMLCloneGenerator();
						IndoorFeatures clone = cloneGenerator.getClone(project.getIndoorFeatures());
						Map<CellSpaceBoundary, CellSpaceBoundary> xLinkBoundaryMap = cloneGenerator.getXLinkBoundaryMap();
						
						Map<CellSpaceBoundary, CellSpaceBoundary> boundary3DMap = null;
						if(rdbtn3D.isSelected()) {
						//	IndoorGML3DGeometryBuilder builder
							project.setIs3DGeometry(true);
							IndoorGML3DGeometryBuilder builder = new IndoorGML3DGeometryBuilder(panel, clone, xLinkBoundaryMap);
							builder.create3DGeometry();
							
							boundary3DMap = builder.getBoundary3DMap();
						} else {
							project.setIs3DGeometry(false);
						}
						
						// Query Test : Aveneul 1F + 2F
						// Only consider SpaceLayer 1
						/*
						String[] fpString = new String[]{"Avenuel_1F", "Avenuel_2F", "Avenuel_3F", "Avenuel_4F", "Avenuel_5F", "Avenuel_6F",
														"Shopping_1F", "Shopping_2F", "Shopping_3F", "Shopping_4F", "Shopping_5F", "Shopping_6F", "Shopping_B1",
														"Cinema_7F", "Cinema_8F", "Cinema_9F", "Cinema_10F", "Cinema_11F",
														"Parkinglot_B2", "Parkinglot_B3", "Parkinglot_B4", "Parkinglot_B5", "Parkinglot_B6"};
														
						//String[] fpString = new String[]{"Avenuel_1F", "Shopping_1F"};
						String[] fpString = new String[]{"Avenuel_1F"};
						List<FloorProperty> queryFP = QuerySelector.getQueryFloors(project, fpString);
						IndoorFeatures queryIndoorFeatures = QuerySelector.getQueryResult(clone, queryFP);
						
						IndoorGMLExporter exporter = new IndoorGMLExporter(queryIndoorFeatures, project.getIs3DGeometry());
						*/
						IndoorGMLExporter exporter = new IndoorGMLExporter(clone, project.getIs3DGeometry());
						
						try {
							exporter.setBoundary3DMap(boundary3DMap);
							exporter.export();
							
							Geometry3DRemover.removeGeometry3D(clone);
						} catch (Exception e1) {
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
