package ofcoursegui;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
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
		scrollPane.setBounds(12, 60, 504, 226);
		add(scrollPane);
		//resultTable.setBounds(12, 96, 504, 160);
		resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		resultTable.setModel(resultTableModel);
		
		resultTable.getColumnModel().getColumn(0).setMaxWidth(90);
		resultTable.getColumnModel().getColumn(0).setMinWidth(90);
		resultTable.getColumnModel().getColumn(0).setPreferredWidth(90);
		resultTable.getColumnModel().getColumn(2).setMaxWidth(55);
		resultTable.getColumnModel().getColumn(2).setMinWidth(55);
		resultTable.getColumnModel().getColumn(2).setPreferredWidth(55);
		resultTable.getColumnModel().getColumn(0).setResizable(false);
		resultTable.getColumnModel().getColumn(1).setResizable(false);
		resultTable.getColumnModel().getColumn(2).setResizable(false);
		setLayout(null);
		//add(resultTable);
		resultTable.setRowHeight(MainWindow.RowHeight);
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
		        		CourseGUI cgui = null;
		        		int tab_pos = MainWindow.searchTabpage.indexOfTab(c.getCode().toString());
		        		if (tab_pos==-1) { // the tab of the course does not exist yet
		        			//System.out.println(c.getCode().toString() + "   " +tab_pos);
		        			cgui = new CourseGUI(c);
			        		MainWindow.addClosableTab(MainWindow.searchTabpage, cgui, c.getCode().toString(), null);
		        		}
		        		else { // the tab of the course already exist
		        			cgui = (CourseGUI) MainWindow.searchTabpage.getComponentAt(tab_pos);
		        		}
		        		MainWindow.searchTabpage.setSelectedComponent(cgui);
		        		
		        		//code for debugging
		        		/*for (int tab_i = 0; tab_i<MainWindow.searchTabpage.getTabCount(); tab_i++) {
		        			Component compo = MainWindow.searchTabpage.getComponentAt(tab_i);
		        			if (compo instanceof CourseGUI) {
		        				String code = ((CourseGUI) compo).course.getCode().toString();
			        			System.out.println("== "+code+" ==");
			        			System.out.println("== "+MainWindow.searchTabpage.getTitleAt(tab_i)+" ==");
		        			}
		        		}*/
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
		String course_name = c.getName().substring(c.getName().indexOf(" - ")+3);
		System.out.println(c.getAvgRating());
		//c.parseComments(); // update rating
		//I am commenting this line because I finally managed to get the avg rating to successfully parsed from courseParse, and since getting avg rating from a number of courses is laggy
		//So that comments and average rating will be updated only when you view a course
		//KK
		resultTableModel.addRow(new String[] {c.toString(), course_name, Float.toString(c.getAvgRating())});
		linkage.put(resultTableModel.getRowCount() - 1, c);
	}
	
	public void setCriteriaText(String criteria) {
		criteriaLabel.setText(criteria);
	}
}
