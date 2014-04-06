package ofcoursegui;

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
	JTable resultTable = new JTable();
	JLabel criteriaLabel = new JLabel("c1");
	DefaultTableModel resultTableModel = new DefaultTableModel(
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
		resultTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
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
	}
	
	public void addResult(Course c) {
		resultTableModel.addRow(new String[] {c.toString(), c.getName(), Float.toString(c.getAvgRating())});
	}
	
	public void setCriteriaText(String criteria) {
		criteriaLabel.setText(criteria);
	}
}
