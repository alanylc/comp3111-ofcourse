package ofcoursegui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

import ofcourse.Course;
import ofcourse.Instructor;
import ofcourse.Network;
import ofcourse.Ratable.Comments;
import ofcourse.TimePeriod;
import ofcourse.TimetableError;

public class CourseGUI extends JPanel {
	public final HashMap<Integer, Course.Session> linkage = new HashMap<Integer, Course.Session>();
	public final Course course;
	
	JLabel courseCodeLabel = new JLabel("CourseCode");
	JLabel courseNameLabel = new JLabel("CoureName");
	JButton enrollButton = new JButton("Enroll");
	JButton btnAddFav = new JButton("Add to My Favourite");
	JButton commentButton = new JButton("Comment");
	public final JTable commentTable=new JTable();

	JLabel avgRating = new JLabel("Average rating:");
	JLabel avgRatingN = new JLabel("5");
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
	@SuppressWarnings("serial")
	CommentTableModel model = new CommentTableModel();
	private class CommentTableModel extends AbstractTableModel {
		 ArrayList<Comments> objs = new ArrayList<Comments>();
	      public Object getValueAt(int rowIndex, int columnIndex) {
	        return objs.get(rowIndex);
	      }
	      public void addRow(Comments obj) {
	    	  objs.add(obj);
	    	  int new_size = objs.size();
	    	  this.fireTableRowsInserted(new_size-1, new_size-1);
	      }
	      public void addAll(ArrayList<Comments> objs) {
	    	  for (Comments c : objs) {
	    		  this.addRow(c);
	    	  }
	      }
	      public void removeAllRows() {
	    	  int last_row = objs.size()-1;
	    	  if (last_row < 0) last_row = 0;
	    	  objs.clear();
	    	  this.fireTableRowsDeleted(0, last_row);
	      }
	      public int getColumnCount() {
	        return 1;
	      }
	      public int getRowCount() {
	        return objs.size();
	      }
	      public Class getColumnClass(int columnIndex) { return CommentGUI.class; }
	      public String getColumnName(int columnIndex) { return "Comments for this course:"; }
	    };
	{
		//JPanel coursePanel = new JPanel();
		//MainWindow.searchTabpage.addTab("New tab", null, this, null);
		setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 62, 504, 199);
		add(scrollPane);
		
		sessionTable.setModel(sessionTableModel);
		sessionTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		sessionTable.setColumnSelectionAllowed(false);
		sessionTable.setRowSelectionAllowed(true);
		
		sessionTable.getColumnModel().getColumn(0).setMaxWidth(55);
		sessionTable.getColumnModel().getColumn(0).setMinWidth(55);
		sessionTable.getColumnModel().getColumn(0).setPreferredWidth(55);
		sessionTable.getColumnModel().getColumn(0).setResizable(false);
		
		sessionTable.getColumnModel().getColumn(1).setMinWidth(65);
		sessionTable.getColumnModel().getColumn(1).setPreferredWidth(100);
		
		sessionTable.getColumnModel().getColumn(2).setMinWidth(65);
		sessionTable.getColumnModel().getColumn(2).setPreferredWidth(150);
		
		sessionTable.getColumnModel().getColumn(3).setMinWidth(65);
		sessionTable.getColumnModel().getColumn(3).setPreferredWidth(150);
		
		sessionTable.setDefaultRenderer(String.class, new MultiLineTableCellRenderer());
		
		scrollPane.setViewportView(sessionTable);
		
		courseCodeLabel.setBounds(12, 12, 504, 18);
		add(courseCodeLabel);
		
		courseNameLabel.setBounds(12, 32, 504, 18);
		add(courseNameLabel);
		
		avgRating.setBounds(22, 271, 100, 18);
		add(avgRating);
		
		avgRatingN.setBounds(112, 271, 36, 18);
		
		add(avgRatingN);
		
		enrollButton.setBounds(418, 533, 98, 28);
		add(enrollButton);
		enrollButton.addActionListener(new EnrollButtonListener());
		
		btnAddFav.setBounds(250, 533, 150, 28);
		add(btnAddFav);
		btnAddFav.addActionListener(new AddFavListener());
		
		commentButton.setBounds(130, 533, 98, 28);
		add(commentButton);
		commentButton.addActionListener(new CommentButtonListener());

		commentTable.setModel(model);
		commentTable.getColumnModel().getColumn(0).setCellRenderer(new CommentRenderer());
		commentTable.getColumnModel().getColumn(0).setCellEditor(new CommentEditor());
		commentTable.getColumnModel().getColumn(0).setResizable(false);
		commentTable.setRowHeight(115);
		
	    JScrollPane pane = new JScrollPane(commentTable);
	    pane.setBounds(12, 307, 504, 199);
	    add(pane);
	}
	@SuppressWarnings("serial")
	public class CommentEditor extends AbstractCellEditor implements TableCellEditor{
		public Component getTableCellEditorComponent(JTable table, Object value,
			      boolean isSelected, int row, int column) {
			Comments cm=(Comments)value;
		    CommentGUI comment=new CommentGUI(cm.getCommentorName(),cm.getRating(),cm.getComments(),cm.getDate());
		    return comment;
			  }

		@Override
		public Object getCellEditorValue() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	@SuppressWarnings("serial")
	public class CommentRenderer extends DefaultTableCellRenderer {

		  /*
		   * @see TableCellRenderer#getTableCellRendererComponent(JTable, Object, boolean, boolean, int, int)
		   */
		  public Component getTableCellRendererComponent(JTable table, Object value,
		                                                 boolean isSelected, boolean hasFocus, 
		                                                 int row, int column) {
			Comments cm=(Comments)value;
		    CommentGUI comment=new CommentGUI(cm.getCommentorName(),cm.getRating(),cm.getComments(),cm.getDate());
		    return comment;
		  }
		}
	private class CommentButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!MainWindow.haveLogined()) {
				MainWindow.showNotLoginError();
				return;
			}
			Frame[] frames = MainWindow.getFrames();
			Frame mainFrame = null;
			for (Frame f : frames) {
				if (f.getTitle().equals(MainWindow.TITLE)) {
					mainFrame = f;
					break;
				}
			}
			new AddCommentGUI(mainFrame, course.getCode().toString());
		}
	}
	private class EnrollButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (MainWindow.timetableTabpage.getSelectedComponent() != MainWindow.own_table.getGUI()) {
				JOptionPane.showMessageDialog(MainWindow.contentPane, "Own timetable tab must be the active tab for the operation.");
				return;
			}
			int[] selecteds = CourseGUI.this.sessionTable.getSelectedRows();
			ArrayList<Course.Session> ss = new ArrayList<Course.Session>();
			for(int i : selecteds) {
				for (Integer j : linkage.keySet()) {
					if (i == j) {
						ss.add(CourseGUI.this.linkage.get(i));
					}
				}
			}

			TimetableError err_code = MainWindow.own_table.addCourse(course, ss.toArray(new Course.Session[ss.size()]));
			MainWindow.showError(err_code, "Enroll Fails");
		}
	}

	private class AddFavListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!MainWindow.haveLogined()) {
				MainWindow.showNotLoginError();
				return;
			}
			String newFav = CourseGUI.this.course.getCode().toString();
			Network network = Network.getOurNetwork();
			String myfav = network.getMyFav();
			String[] favCs = myfav.split("!");
			for (String str : favCs) {
				if (str.equals(newFav)) {
					JOptionPane.showMessageDialog(MainWindow.contentPane, "Course already in My Favourite.",
							"Add to My Favourite", JOptionPane.WARNING_MESSAGE);
					return;
				}
			}
			myfav = myfav + newFav + "!";
			String returnCode = network.setMyFav(myfav);
			
			if (returnCode.equals("100")) {
				MainWindow.updateFavNeeded.setText(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
				JOptionPane.showMessageDialog(MainWindow.contentPane, "Course successfully added to My Favourite.",
						"Add to My Favourite", JOptionPane.INFORMATION_MESSAGE);
			}
			else if (returnCode.equals("404")) {
				JOptionPane.showMessageDialog(MainWindow.contentPane, "Network Unavailable / Server Down.",
						"Change Password", JOptionPane.WARNING_MESSAGE);
			}
			else {
				JOptionPane.showMessageDialog(MainWindow.contentPane, "Operation Fails!",
						"Add to My Favourite", JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	
	public CourseGUI (Course c) {
		if (c == null) throw new NullPointerException();
		setCourse(c);
		setCommentRating(c);
		this.course = c;
	}
	
	public void setCommentRating(Course c) {
		c.parseComments();
		model.removeAllRows();
		model.addAll(c.getComments());
		System.out.println(String.valueOf(c.getAvgRating()));
		avgRatingN.setText(String.valueOf(c.getAvgRating()));
	}
	
	public void updateGUI() {
		setCourse(this.course);
		setCommentRating(this.course);
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
			TreeMap<String, String> arr =  new TreeMap<String, String>();
			for (TimePeriod tp : s.getSchedule()) {
				String timeStr = null;
				try {
					String tmp = tp.toString();
					String tmp_day = tp.getStartSlot().getDay().toString();
					timeStr = tmp.substring(tmp.indexOf(tmp_day)+tmp_day.length()+1); // one space
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				String data = null;
				try {
					data = arr.get(timeStr)==null ? "" : arr.get(timeStr);
					data = data + tp.getStartSlot().getDay().toString();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				arr.put(timeStr, data);
			}
			String schStr = "";
			Iterator<Entry<String, String>> it = arr.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, String> entry = it.next();
				schStr += entry.getValue() + " " + entry.getKey();
				if (it.hasNext()) schStr += "\n";
			}
			if (arr.entrySet().size()==0) {
				schStr = "TBA";
			}
			String roomStr = "";
			Iterator<String> it_r = s.getRoom().iterator();
			while (it_r.hasNext()) {
				roomStr +=it_r.next();
				if (it_r.hasNext()) roomStr += "\n";
			}
			if (roomStr.isEmpty()) roomStr = "TBA";
			String instructorStr = "";
			Iterator<Instructor> it_i = s.getInstructors().iterator();
			while (it_i.hasNext()) {
				instructorStr += it_i.next().toString();
				if (it_i.hasNext()) instructorStr += "\n";
			}
			if (instructorStr.isEmpty()) instructorStr = "TBA";
			sessionTableModel.addRow(new String[] {s.toString(), schStr, roomStr, instructorStr});
			linkage.put(sessionTableModel.getRowCount()-1, s);
		}
	}
	
	// return sessions selected, course of sessions can be obtained by class no
	public Course.Session[] getSelectedSessions() {
		int[] selecteds = CourseGUI.this.sessionTable.getSelectedRows();
		ArrayList<Course.Session> ss = new ArrayList<Course.Session>();
		for(int i : selecteds) {
			for (Integer j : linkage.keySet()) {
				if (i == j) {
					ss.add(CourseGUI.this.linkage.get(i));
				}
			}
		}
		return ss.toArray(new Course.Session[ss.size()]);
	}
}
