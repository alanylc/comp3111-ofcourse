package ofcoursegui;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import ofcourse.Course;
import ofcourse.Network;
import ofcourse.TimePeriod;
import ofcourse.TimetableError;

public class CourseGUI extends JPanel {
	public final HashMap<Integer, Course.Session> linkage = new HashMap<Integer, Course.Session>();
	public final Course course;
	
	JLabel courseCodeLabel = new JLabel("CourseCode");
	JLabel courseNameLabel = new JLabel("CoureName");
	JButton enrollButton = new JButton("Enroll");
	JButton btnAddFav = new JButton("Add to My Favourite");
	JLabel ratingLabel=new JLabel("Rating:");
	JLabel commentLabel=new JLabel("Comment:");
	JRadioButton B1 = new JRadioButton("1");
	JRadioButton B2 = new JRadioButton("2");
	JRadioButton B3 = new JRadioButton("3");
	JRadioButton B4 = new JRadioButton("4");
	JRadioButton B5 = new JRadioButton("5");
	ButtonGroup ratingGroup = new ButtonGroup();
	JTextArea commentArea= new JTextArea();
	JButton submitCommentButton = new JButton("Submit Comment");
	
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
		
		sessionTable.getColumnModel().getColumn(0).setMaxWidth(55);
		sessionTable.getColumnModel().getColumn(0).setMinWidth(55);
		sessionTable.getColumnModel().getColumn(0).setPreferredWidth(55);
		sessionTable.getColumnModel().getColumn(0).setResizable(false);
		
		sessionTable.getColumnModel().getColumn(2).setPreferredWidth(170);
		
		scrollPane.setViewportView(sessionTable);
		
		courseCodeLabel.setBounds(12, 12, 504, 18);
		add(courseCodeLabel);
		
		courseNameLabel.setBounds(12, 32, 504, 18);
		add(courseNameLabel);
		
		enrollButton.setBounds(418, 533, 98, 28);
		add(enrollButton);
		enrollButton.addActionListener(new EnrollButtonListener());
		
		btnAddFav.setBounds(250, 533, 150, 28);
		add(btnAddFav);
		btnAddFav.addActionListener(new AddFavListener());
		
		
		ratingGroup.add(B1);
		ratingGroup.add(B2);
		ratingGroup.add(B3);
		ratingGroup.add(B4);
		ratingGroup.add(B5);
		JPanel ratingPanel = new JPanel();
		ratingPanel.setLayout(new GridLayout(1, 5));
		ratingPanel.add(B1);
		ratingPanel.add(B2);
		ratingPanel.add(B3);
		ratingPanel.add(B4);
		ratingPanel.add(B5);
		ratingPanel.setBounds(97, 311, 200, 20);
		add(ratingPanel);
		commentArea.setBounds(97, 341, 260, 60);
		add(commentArea);
		ratingLabel.setBounds(35, 311, 40, 20);
		add(ratingLabel);
		commentLabel.setBounds(35, 341, 80, 20);
		add(commentLabel);
		submitCommentButton.setBounds(370, 341, 120, 30);
		add(submitCommentButton);
		submitCommentButton.addActionListener(new SubmitCommentButtonListener());
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
	private class SubmitCommentButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String rating=getSelectedButtonString(ratingGroup);
			String comment=commentArea.getText();
			Network a=Network.getOurNetwork();
			String reply=a.comment(courseCodeLabel.getText().substring(0, 8), rating, comment);
			switch(Integer.parseInt(reply)){
			case 100:
				JOptionPane.showMessageDialog(getComponent(0),"Comment submitted successfully!","Success!",JOptionPane.INFORMATION_MESSAGE);
				break;
			case 002:
				JOptionPane.showMessageDialog(getComponent(0),"Wrong username or password detected!","Failure!",JOptionPane.WARNING_MESSAGE);
				break;
			case 404:
				JOptionPane.showMessageDialog(getComponent(0),"Network error! Comment is not submitted!","Failure!",JOptionPane.WARNING_MESSAGE);
				break;
			case 200:
				JOptionPane.showMessageDialog(getComponent(0),"You have to login to submit comments!","Failure!",JOptionPane.WARNING_MESSAGE);
				break;
			default:
				JOptionPane.showMessageDialog(getComponent(0),"Error occured, comment is not submitted.","Failure!",JOptionPane.WARNING_MESSAGE);
				break;
				
				
			}
			//TimetableError err_code = MainWindow.own_table.addCourse(course, ss.toArray(new Course.Session[ss.size()]));
			//MainWindow.showError("Enroll Fails");
		}
		public String getSelectedButtonString(ButtonGroup buttonGroup) {
	        for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
	            AbstractButton button = buttons.nextElement();

	            if (button.isSelected()) {
	                return button.getText();
	            }
	        }

	        return "";
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
			if (!returnCode.equals("100")) {
				JOptionPane.showMessageDialog(MainWindow.contentPane, "Operation Fails!",
						"Add to My Favourite", JOptionPane.WARNING_MESSAGE);
			}
			else {
				MainWindow.updateFavNeeded.setText(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
				JOptionPane.showMessageDialog(MainWindow.contentPane, "Course successfully added to My Favourite.",
						"Add to My Favourite", JOptionPane.INFORMATION_MESSAGE);
			}
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
			int numOfDistinctTime = 0;
			ArrayList<ArrayList<Integer>> arr =  new ArrayList<ArrayList<Integer>>();
			for (TimePeriod tp : s.getSchedule()) {
				int[] start_end = tp.getStartEndID();
				ArrayList<Integer> tmp = new ArrayList<Integer>();
				tmp.add(start_end[0]%100);
				tmp.add(start_end[1]%100);
				if (!arr.contains(tmp)){
					numOfDistinctTime++;
				}
				arr.add(tmp);
			}
			if (numOfDistinctTime==0) numOfDistinctTime=1; // Time = TBA
			sessionTableModel.addRow(new String[] {s.toString(), s.getSchedule().toString(), s.getRoom().toString(), s.getInstructors().toString()});
			sessionTable.setRowHeight(sessionTableModel.getRowCount()-1, MainWindow.RowHeight*numOfDistinctTime);
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
