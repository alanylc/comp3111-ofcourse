package ofcoursegui;


import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;

import ofcourse.Course;
import ofcourse.Instructor;
import ofcourse.Network;
import ofcourse.Ratable.Comments;
import ofcourse.TimePeriod;
import ofcourse.TimetableError;
import javax.swing.ScrollPaneConstants;

@SuppressWarnings("serial")
public class CourseGUI extends JPanel {
	public final HashMap<Integer, Course.Session> linkage = new HashMap<Integer, Course.Session>();
	public final Course course;
	ArrayList<Comments> comments = new ArrayList<Comments>();
	JLabel courseCodeLabel = new JLabel("CourseCode");
	JLabel courseNameLabel = new JLabel("CoureName");
	JButton enrollButton = new JButton("Enroll");
	JButton btnAddFav = new JButton("Add to My Favourite");
	JButton commentButton = new JButton("Comment");
	public final JTable commentTable=new JTable();

	JLabel avgRating = new JLabel("Average rating:");
	JLabel avgRatingN = new JLabel("5");
	public final JTable sessionTable = new JTable();

	DefaultTableModel sessionTableModel = new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Session", "Time", "Room", "Instructor"
			}
			
		) {
			@SuppressWarnings("rawtypes")
			Class[] columnTypes = new Class[] {
				String.class, String.class, String.class, String.class
			};
			@SuppressWarnings({ "unchecked", "rawtypes" })
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
				if (f.getTitle().equals(MainWindow.TITLE) && f.getClass().equals(MainWindow.class)) {
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
	
	private void setCommentRating(Course c) {
		System.out.println(String.valueOf(c.getAvgRating()));
		c.parseComments();
		comments=c.getComments();
		System.out.println(comments.size()+"cmsize");
		avgRatingN.setText(String.valueOf(c.getAvgRating()));
		
		ColumnSpec[] cs = new ColumnSpec[1];
		cs[0] = new ColumnSpec(Sizes.pixel(504));
		RowSpec[] rs = new RowSpec[comments.size()];
		for(int i = 0; i < comments.size(); i++) {
			rs[i] = new RowSpec(Sizes.pixel(115));
		}
		JLayeredPane commentTable = new JLayeredPane();
		commentTable.setBounds(0, 0, 504, 115*comments.size());
		commentTable.setPreferredSize(new Dimension(504,115*comments.size()));
		int i=0;	
		for(Comments cm:comments){
			i++;
			CommentGUI comment=new CommentGUI(cm.getCommentorName(),cm.getRating(),cm.getComments(),cm.getDate());
			System.out.println(cm.getCommentorName());
			comment.setBounds(0, 115*i-115, 504, 115);
			commentTable.add(comment,new Integer(i), 0);
		}
	    JScrollPane pane = new JScrollPane(commentTable);
	    pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	    pane.setBounds(12, 307, 504, 199);
	    add(pane);
	    setVisible(true);
	}
	
	public void updateGUI() {
		while (sessionTableModel.getRowCount()>0) {
			sessionTableModel.removeRow(0);
		}
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
			String schStr = TimePeriod.getDistinctStr(s.getSchedule());
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
