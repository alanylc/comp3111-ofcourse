package ofcoursegui;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import ofcourse.Course;

@SuppressWarnings("serial")
public class SearchResultGUI extends JPanel {
	private HashMap<Integer, Course> linkage = new HashMap<Integer, Course>();
	
	private JLabel criteriaLabel = new JLabel("c1");
	
	private JTable resultTable = new JTable();
	public final DefaultTableModel resultTableModel = new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Course", "Name", "Rating"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, String.class, String.class, String.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
	
	
	{
		/*JPanel resultPanel = new JPanel();
		searchTabpage.addTab("New tab", null, resultPanel, null);*/

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 96, 504, 226);
		add(scrollPane);
		//resultTable.setBounds(12, 96, 504, 160);
		resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		resultTable.setModel(resultTableModel);
		
		resultTable.getColumnModel().getColumn(0).setResizable(false);
		setLayout(null);
		//add(resultTable);
		scrollPane.setViewportView(resultTable);
		
		JLabel lblSearchCriteria = new JLabel("Search Criteria:");
		lblSearchCriteria.setBounds(12, 12, 94, 18);
		add(lblSearchCriteria);
		
		criteriaLabel.setBounds(22, 30, 494, 18);
		add(criteriaLabel);
		
		resultTable.addMouseListener(new MouseListener() {
			@Override
			public void mousePressed(MouseEvent e) {
				JTable table = (JTable) e.getSource();
		        Point p = e.getPoint();
		        int row = table.rowAtPoint(p);
		        System.out.println(row);
		        int[] selecteds = table.getSelectedRows();
		        for (int i : selecteds) {
		        	if (i == row) {
		        		Course c = linkage.get(row);
		        		CourseGUI cgui = new CourseGUI(c);
		        		MainWindow.searchTabpage.addTab(c.getCode().toString(), cgui);
		        		MainWindow.searchTabpage.setSelectedComponent(cgui);
		        	}
		        }
		    }

			@Override
			public void mouseClicked(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}
		});
	}
	
	public void addResult(Course c) {
		resultTableModel.addRow(new String[] {c.toString(), c.getName(), Float.toString(c.getAvgRating())});
		linkage.put(resultTableModel.getRowCount() - 1, c);
	}
	
	public void setCriteriaText(String criteria) {
		criteriaLabel.setText(criteria);
	}
}
