package ofcoursegui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import ofcourse.Course;

public class CourseGUI extends JPanel {
	public final HashMap<Integer, Course.Session> linkage = new HashMap<Integer, Course.Session>();
	public final Course course;
	
	JLabel courseCodeLabel = new JLabel("CourseCode");
	JLabel courseNameLabel = new JLabel("CoureName");
	JButton enrollButton = new JButton("Enroll");
	public final JTable sessionTable = new JTable();
	@SuppressWarnings("serial")
	DefaultTableModel sessionTableModel = new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Session", "Time", "Room", "Instructor"
			}
			
		) {
			Class[] columnTypes = new Class[] {
				String.class, String.class, String.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			public boolean isCellEditable(int row, int column) {
				return false;
			}
	};
	
	{
		//JPanel coursePanel = new JPanel();
		//MainWindow.searchTabpage.addTab("New tab", null, this, null);
		setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 62, 504, 226);
		add(scrollPane);
		
		sessionTable.setModel(sessionTableModel);
		sessionTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		sessionTable.setColumnSelectionAllowed(false);
		sessionTable.setRowSelectionAllowed(true);
		scrollPane.setViewportView(sessionTable);
		
		courseCodeLabel.setBounds(12, 12, 504, 18);
		add(courseCodeLabel);
		
		courseNameLabel.setBounds(12, 32, 504, 18);
		add(courseNameLabel);
		
		enrollButton.setBounds(418, 533, 98, 28);
		add(enrollButton);
		enrollButton.addActionListener(new EnrollButtonListener());
	}
	
	private class EnrollButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int[] selecteds = CourseGUI.this.sessionTable.getSelectedRows();
			ArrayList<Course.Session> ss = new ArrayList<Course.Session>();
			for(int i : selecteds) {
				for (Integer j : linkage.keySet()) {
					if (i == j) {
						ss.add(CourseGUI.this.linkage.get(i));
					}
				}
			}

			MainWindow.own_table.addCourse(course, ss.toArray(new Course.Session[ss.size()]));
		}
	}
	
	public CourseGUI (Course c) {
		if (c == null) throw new NullPointerException();
		setCourse(c);
		this.course = c;
	}
	
	/*public void setCoureCodeText(String couseCode) {
		courseCodeLabel.setText(couseCode);
	}	
	public void setCoureNameText(String couseCode) {
		courseNameLabel.setText(couseCode);
	}*/
	private void setCourse(Course c) {
		courseCodeLabel.setText(c.getCode().toString());
		courseNameLabel.setText(c.getName());
		for (Course.Session s : c.getSessions()) {
			sessionTableModel.addRow(new String[] {s.toString(), s.getSchedule().toString(), s.getRoom().toString(), s.getInstructors().toString()});
			linkage.put(sessionTableModel.getRowCount() - 1, s);
		}
	}
}
