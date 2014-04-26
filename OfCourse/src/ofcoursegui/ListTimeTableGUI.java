package ofcoursegui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

import ofcourse.Course;
import ofcourse.Course.Session;
import ofcourse.TimePeriod;
import ofcourse.Timetable;

@SuppressWarnings("serial")
public class ListTimeTableGUI extends JFrame {
	
	private Timetable table = null;
	
	private JTable listTimetable = new JTable();
	private final DefaultTableModel timetableModel = new DefaultTableModel(new Object[][] { },
			new String[] {"Course", "Name", "Time"})
			{
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
		
	public void refresh() {
		while (timetableModel.getRowCount() > 0) {
			timetableModel.removeRow(0);
		}
		addRecords(table);
	}
	
	public ListTimeTableGUI(Timetable _table) {
		this.table = _table;
		this.setTitle(table.getGUI().guititle);
		this.setSize(new Dimension(500, 500));
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		listTimetable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listTimetable.setModel(timetableModel);
		listTimetable.getColumnModel().getColumn(0).setMaxWidth(90);
		listTimetable.getColumnModel().getColumn(0).setPreferredWidth(90);
		listTimetable.setRowHeight(MainWindow.RowHeight);
		listTimetable.setDefaultRenderer(String.class, new MultiLineTableCellRenderer());
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 10, 580, 400);
		scrollPane.setViewportView(listTimetable);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		this.addRecords(table);
		JButton dropBtn = new JButton("Drop");
		dropBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int index = listTimetable.getSelectedRow();
				if (index == -1) {
					JOptionPane.showMessageDialog(ListTimeTableGUI.this, "No Course Selected.");
				}
				else {
					String course_code = (String) timetableModel.getValueAt(index, 0);
					timetableModel.removeRow(index);
					table.dropCourse(course_code);
				}
			}
		});
		JButton closeBtn = new JButton("Close");
		closeBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
		if (table == MainWindow.own_table) {
			// only add the drop button for own_table
			btnPanel.add(dropBtn);
		}
		btnPanel.add(closeBtn);
		
		JPanel outerpane = new JPanel(new BorderLayout());
		outerpane.add(scrollPane, BorderLayout.CENTER);
		outerpane.add(btnPanel, BorderLayout.SOUTH);
		this.add(outerpane);
		
		this.setVisible(true);
	}
	
	public void addRecords(Timetable t) {
		for (Entry<Course, ArrayList<Session>> entry : table.getEnrolled().entrySet()) {
			String course_code = entry.getKey().getCode().toString();
			String course_name = entry.getKey().getName();
			course_name = course_name.substring(course_name.indexOf(" - ")+3);
			String time_str = "";
			for (Session s : entry.getValue()) {
				time_str += s.toString() + ": " + TimePeriod.getDistinctStr(s.getSchedule()) + "\n";
			}
			time_str = time_str.substring(0, time_str.length()-1); // remove last "\n"
			timetableModel.addRow(new String[]{course_code, course_name, time_str});
		}
	}
	
}
