package ofcoursegui;

import java.awt.Adjustable;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.AbstractListModel;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import ofcourse.Course;
import ofcourse.CourseParse;
import ofcourse.CourseParseThreaded;
import ofcourse.Network;
import ofcourse.SearchAllCourse;
import ofcourse.SearchCourse;
import ofcourse.SearchSubject;
import ofcourse.TimePeriod;
import ofcourse.Timetable;
import ofcourse.TimetableError;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {
	
	public static HashMap<String, Timetable> friends = new HashMap<String, Timetable>();
	public static String username=null, password=null;
	//public static Network network = Network.login("ctestcaa", "aaa");
	public static Network network = Network.getOurNetwork();
	
	class MyListModel extends AbstractListModel {
		ArrayList<String> values = new ArrayList<String>();
		public int getSize() {
			return values.size();
		}
		public Object getElementAt(int index) {
			return values.get(index);
		}
		public void add(String _value) {
			int old_size = getSize();
			values.add(_value);
			this.fireContentsChanged(this, old_size, old_size);
		}
		public void addAll(String[] _values) {
			for (String v : _values) {
				add(v);
			}
		}
		public void removeAll() {
			values.clear();
			this.fireContentsChanged(this, 0, 0);
		}
	}
	
	MyListModel fdListModel = new MyListModel();
	
	public static JLabel loginAs;
	public static final int RowHeight = 20;
	
	public static JLabel updateFdNeeded = new JLabel();
	public static JLabel updateFavNeeded = new JLabel();

	public static boolean haveLogined() {
		String str = network.getFriendList();
		return (!str.equals("002") && !str.equals("200"));
	}
	
	public void updateFriendsTimetable(boolean prompt) {
		// if have not logged in, prompt and clear friend list
		if (!haveLogined()) {
			fdListModel.removeAll();
			if (prompt) showNotLoginError();
			return;
		}
		String values[] = network.getFriendList().split("!");
		fdListModel.removeAll();
		for (String value : values) {
			if (!value.isEmpty())  fdListModel.add(value);
		}
		String[][] fdListandTable = network.getFriendListAndTimeTable();
		for (String[] oneRecord : fdListandTable) {
			String fdname = oneRecord[0];
			Timetable table = friends.get(fdname);
			if (table==null) {
				table = new Timetable(fdname);
			}
			table.importString(oneRecord[1]); 
			friends.put(fdname, table);
		}
		if (prompt) JOptionPane.showMessageDialog(contentPane, "Timetables of friends updated successfully.");
	}
	
	public void updateMyFavTab(boolean prompt) {
		// if have not logged in, prompt and clear my fav tab
		if (!haveLogined()) {
			myFavPanel.clearFav();
			if (prompt) showNotLoginError();
			return;
		}
		myFavPanel.updateFav();
	}
	
	JButton btnDrop = new JButton("Drop...");
	public class DropButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (timetableTabpage.getSelectedComponent() != own_table.getGUI()) {
				JOptionPane.showMessageDialog(contentPane, "Own timetable tab must be the active tab for the operation.");
				return;
			}
			Course selection = own_table.getSelectedCourse();
			if (selection != null) {
				own_table.courseUnselected();
				own_table.dropCourse(selection);
			}
			else { // selection == null
				JOptionPane.showMessageDialog(contentPane, "Select a course in own timetable first.");
			}
		}
	}
	
	NewSearchGUI newSearchPanel = new NewSearchGUI();
	
	//public static ArrayList<SearchResultGUI> searchResultPanels = new ArrayList<SearchResultGUI>();
	
	public static JPanel contentPane;
	public static JTabbedPane timetableTabpage = new JTabbedPane(JTabbedPane.TOP);
	public static JTabbedPane searchTabpage = new JTabbedPane(JTabbedPane.TOP);
	public static MyFavPanel myFavPanel = new MyFavPanel(contentPane);

	
	public static Timetable own_table;
	
	public static ArrayList<Course> allCourses;
	
	
	//public static java.util.HashMap<JPanel, TimeTableGUI> linkage = new java.util.HashMap<JPanel, TimeTableGUI>();
	//private JTable table;

	
	public TimeTableGUI getSelectedTimeTableGUI() {
		if(timetableTabpage.getSelectedComponent() != null) {
			return (TimeTableGUI)(timetableTabpage.getSelectedComponent());
		}
		return null;
	} 
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
					frame.setTitle("OfCourse");
					
					ArrayList<CourseParse> result = CourseParseThreaded.fullparse();
					
//					ArrayList<CourseParse> result = new ArrayList<CourseParse>(); result.add(CourseParse.parse("COMP")); result.add(CourseParse.parse("ELEC"));

					frame.setVisible(true);
					
					for(CourseParse cp : result) {
						((SearchSubjectGUI.SubjectListModel)((frame.newSearchPanel.subjectJList.subjectListModel))).addElement((cp.getSubject()));
					}
					allCourses = new ArrayList<Course>();
					for(CourseParse cp : result) {
						allCourses.addAll(cp.getCourses());
					}
					ArrayList<String> criteria = new ArrayList<String>();
					criteria.add("HUMA");
					criteria.add("ECON");
					SearchCourse cs = new SearchSubject(new SearchAllCourse(allCourses), criteria);
					for(Course c : cs) {
						System.out.println(c.toString());
					}
					System.out.println(cs.toString());
					
					frame.updateFriendsTimetable(false);
					frame.updateMyFavTab(false);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	

	/**
	 * Create the frame.
	 */
	
	private class GraySep extends JSeparator {
		public GraySep() {
			setForeground(Color.GRAY);;
		}
	}

	public MainWindow() {
		
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//setBounds(100, 100, 1280, 720);
		setSize(1280, 720);
		setLocationRelativeTo(null);
		
		// this label will not be added to the pane, just for action listening
		updateFdNeeded.addPropertyChangeListener(new PropertyChangeListener(){
		   @Override
		   public void propertyChange(PropertyChangeEvent event){
		     if (event.getPropertyName().equals("text")){
		        updateFriendsTimetable(false);
		     }
		   }
		});
		
		// this label will not be added to the pane, just for action listening
		updateFavNeeded.addPropertyChangeListener(new PropertyChangeListener(){
		   @Override
		   public void propertyChange(PropertyChangeEvent event){
		     if (event.getPropertyName().equals("text")){
		        updateMyFavTab(false);
		     }
		   }
		});
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenu mnAccount = new JMenu("Account");
		menuBar.add(mnAccount);
		
		JMenu mnFriend = new JMenu("Friend");
		menuBar.add(mnFriend);
		
		JMenuItem mntmUploadMine = new JMenuItem("Upload My Time Table");
		mnFile.add(mntmUploadMine);
		
		JMenuItem mntmDownloadMine = new JMenuItem("Download My Time Table");
		mnFile.add(mntmDownloadMine);
		
		mnFile.add(new GraySep());
		
		JMenuItem mntmImportTimeTable = new JMenuItem("Import Time Table...");
		mnFile.add(mntmImportTimeTable);
		
		JMenuItem mntmExportTimeTable = new JMenuItem("Export Time Table...");
		mnFile.add(mntmExportTimeTable);
		
		JMenuItem mntmRegister = new JMenuItem("Register");
		mnAccount.add(mntmRegister);
		
		JMenuItem mntmChangePw = new JMenuItem("Change Password");
		mnAccount.add(mntmChangePw);
		
		mnAccount.add(new GraySep());
		
		JMenuItem mntmLogin = new JMenuItem("Login");
		mnAccount.add(mntmLogin);
		
		JMenuItem mntmLogout = new JMenuItem("Logout");
		mnAccount.add(mntmLogout);

		JMenuItem mntmAddNewFd = new JMenuItem("Add New Friend");
		mnFriend.add(mntmAddNewFd);
		
		JMenuItem mntmChkRequest = new JMenuItem("Check Friend Requests");
		mnFriend.add(mntmChkRequest);
		
		JMenuItem mntmUpdateFdTimetable = new JMenuItem("Update Friends' Time Tables");
		mnFriend.add(mntmUpdateFdTimetable);
		
		
		
		
		mntmImportTimeTable.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
			final JFileChooser fc = new JFileChooser();
		        int returnVal = fc.showOpenDialog(contentPane);
		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            // own_table.tid will not change (only import timetable but not TID)
		            String tid = own_table.getTableId();
		            boolean success = own_table.importFile(file.getAbsolutePath());
		            own_table.setTableId(tid);
		            if (success) {
		            	JOptionPane.showMessageDialog(contentPane,
							    "Successfully import time table from file: "+file.getName(),
							    "Import Success",
							    JOptionPane.INFORMATION_MESSAGE);
		            }
		            else {
		            	JOptionPane.showMessageDialog(contentPane,
							    "Fail to import time table from file: "+file.getName(),
							    "Import Fails",
							    JOptionPane.WARNING_MESSAGE);
		            }
		        }
		   }
		});
		
		mntmExportTimeTable.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
			final JFileChooser fc = new JFileChooser();
		        int returnVal = fc.showSaveDialog(contentPane);
		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            boolean success = own_table.exportFile(file.getAbsolutePath());
		            if (success) {
		            	JOptionPane.showMessageDialog(contentPane,
							    "Successfully export time table to file: "+file.getName(),
							    "Export Success",
							    JOptionPane.INFORMATION_MESSAGE);
		            }
		            else {
		            	JOptionPane.showMessageDialog(contentPane,
							    "Fail to export time table to file: "+file.getName(),
							    "Export Fails",
							    JOptionPane.WARNING_MESSAGE);
		            }
		        }
		   }
		});
		
		mntmUploadMine.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!haveLogined()) {
					showNotLoginError();
				}
				else {
					String exportStr = own_table.exportString();
					String returnVal = network.setTimeTable(exportStr);
					if (returnVal.equals("100")) {
						JOptionPane.showMessageDialog(contentPane, "Upload successfully.");
					}
					else {
						JOptionPane.showMessageDialog(contentPane, "Upload fails.");
					}
				}
			}
		});
		
		mntmDownloadMine.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!haveLogined()) {
					showNotLoginError();
				}
				else {
					String instr = network.getTimeTable();
					if (own_table.importString(instr)) {
						JOptionPane.showMessageDialog(contentPane, "Download successfully.");
					}
					else {
						JOptionPane.showMessageDialog(contentPane, "Download fails.");
					}
				}
			}
		});
		
		
		
		mntmRegister.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (haveLogined()) {
					JOptionPane.showMessageDialog(contentPane, "You have to logout first.");
				}
				else {
					new RegisterGUI(MainWindow.this);
				}
			}
		});
		
		
		mntmChangePw.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!haveLogined()) {
					showNotLoginError();
				}
				else {
//					JPasswordField pf = new JPasswordField();
//					int okCxl = JOptionPane.showConfirmDialog(null, pf, "Enter Current Password",
//							JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
//					if (okCxl == JOptionPane.OK_OPTION) {
//						String pw = new String(pf.getPassword());
						new ChangePwGUI(MainWindow.this);
//					}
				}
			}
		});
		
		
		mntmLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (haveLogined()) {
					JOptionPane.showMessageDialog(contentPane, "You have already logged in.");
				}
				else {
					new LoginGUI(MainWindow.this);
				}
			}
		});
		
		
		mntmLogout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (haveLogined()) {
					Network.logout();
					// close all friends tab, and clear own_table
					while (timetableTabpage.getTabCount()==2) {
						timetableTabpage.remove(1);
					}
					own_table.importString("Mine!");
					MainWindow.loginAs.setText("Currently Login As: <Anonymous>");
					JOptionPane.showMessageDialog(contentPane, "You have logged out successfully.");
				}
				else {
					JOptionPane.showMessageDialog(contentPane, "You have not logged in yet.");
				}
			}
		});
		
		
		mntmUpdateFdTimetable.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				updateFriendsTimetable(true);
			}
		});
		
		mntmAddNewFd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!haveLogined()) {
					showNotLoginError();
					return;
				}
				String fdname = (String) JOptionPane.showInputDialog(contentPane,
							"Please input your friend's username: ", "Add New Friend",
							JOptionPane.PLAIN_MESSAGE, null, null, "");
				if (fdname==null) return; // user cancels input
				if (fdname.equals(Network.getOurNetworkUserName())) {
					JOptionPane.showMessageDialog(contentPane, "You cannot add yourself as friend.");
					return;
				}
				String returnCode = network.friendReq(fdname);
				if (returnCode.equals("100")) {
					JOptionPane.showMessageDialog(contentPane, "Friend Request Sent Successfully.");
				}
				else if (returnCode.equals("004")) {
					JOptionPane.showMessageDialog(contentPane, "Friend Request Previously Sent.");
				}
				else {
					JOptionPane.showMessageDialog(contentPane,
						    "Fail to sent the friend request.",
						    "Error",
						    JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		
		mntmChkRequest.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!haveLogined()) {
					showNotLoginError();
					return;
				}
				new FriendRequestGUI(MainWindow.this);
			}
		});
		
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		timetableTabpage.setBounds(557, 52, 705, 603);
		contentPane.add(timetableTabpage);
		
		own_table = new Timetable(Network.getOurNetworkUserName(), timetableTabpage);
		

		final JList friend_list = new JList();
		friend_list.setModel(fdListModel);
		friend_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		
		JScrollPane friend_scrollPane = new JScrollPane();
		friend_scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		friend_scrollPane.setBounds(557, 16, 116, 23);
		friend_scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent evt) {
			    Adjustable source = evt.getAdjustable();
			    if (evt.getValueIsAdjusting()) {
			      return;
			    }
			    if (evt.getAdjustmentType() == AdjustmentEvent.TRACK) {
			    	if (source.getVisibleAmount()!=0 && friend_list.getComponentCount()!=0) {
			    		int index = evt.getValue() / source.getVisibleAmount();
			    		friend_list.setSelectedIndex(index);
			    	}
			    }
			  }
		});
		friend_scrollPane.setViewportView(friend_list);
		contentPane.add(friend_scrollPane);
		
		
		
		//Buttons
		JButton btnSeeFriend = new JButton("See Friend");
		btnSeeFriend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!haveLogined()) {
					showNotLoginError();
					return;
				}
				Object obj = friend_list.getSelectedValue();
				if (obj!=null && !obj.toString().isEmpty()) { // if there is an item selected 
					String friend_name =  obj.toString();
					TimeTableGUI friendTable = friends.get(friend_name).getGUI();
					int index = timetableTabpage.indexOfTab(friend_name);
					if (index==-1) { // not exist
						addClosableTab(timetableTabpage, friendTable, friend_name, null);
					}
					else { // existing tab, switch to it
						timetableTabpage.setSelectedIndex(index);
					}
				}
				else {
					JOptionPane.showMessageDialog(contentPane, "Please select a friend first.",
							"See Friend's Timetable", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		btnSeeFriend.setBounds(677, 12, 98, 28);
		contentPane.add(btnSeeFriend);
		
		JButton btnDeleteLastTab = new JButton("Delete Last");
		btnDeleteLastTab.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int indexSelected = timetableTabpage.getSelectedIndex();
				if (timetableTabpage.getTabCount() > 1) {
					if (timetableTabpage.getTabCount()-1 == indexSelected) 
						timetableTabpage.setSelectedIndex(indexSelected-1);
					timetableTabpage.remove(timetableTabpage.getTabCount() - 1);
				}
				
			}
		});
		btnDeleteLastTab.setBounds(787, 12, 98, 28);
		contentPane.add(btnDeleteLastTab);
		
		
		String theUser = Network.getOurNetworkUserName();
		if (!haveLogined()) {
			 theUser = "<Anonymous>";
		}
		loginAs = new JLabel("Currently Login As: "+theUser);
		loginAs.setForeground(Color.BLUE);
		loginAs.setBounds(12, 12, 500, 28);
		// TODO: add update of own_table and friend_table after login/logout
		loginAs.addPropertyChangeListener(new PropertyChangeListener(){
		   @Override
		   public void propertyChange(PropertyChangeEvent event){
		     if (event.getPropertyName().equals("text")){
		    	 // trigger the specify events
		    	 updateFdNeeded.setText(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
		    	 updateFavNeeded.setText(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
		     }
		   }
		});
		contentPane.add(loginAs);
		
		
		
		//JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		searchTabpage.setBounds(12, 52, 533, 603);
		contentPane.add(searchTabpage);
		
		
		searchTabpage.add(myFavPanel);
		int pos_fav = searchTabpage.indexOfComponent(myFavPanel);
		String title_fav = "My Favourite";
		JLabel label_fav = new JLabel(title_fav);
		label_fav.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
		searchTabpage.setTabComponentAt(pos_fav, label_fav);
		searchTabpage.setTitleAt(pos_fav, title_fav);
		searchTabpage.setMnemonicAt(pos_fav, KeyEvent.VK_F);
		
		searchTabpage.add(newSearchPanel);
		int pos = searchTabpage.indexOfComponent(newSearchPanel);
		String title = "New Search";
		JLabel label = new JLabel(title);
		label.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
		searchTabpage.setTabComponentAt(pos, label);
		searchTabpage.setTitleAt(pos, title);
		searchTabpage.setMnemonicAt(pos, KeyEvent.VK_S);
		searchTabpage.setSelectedComponent(newSearchPanel);
		
		
		
		searchTabpage.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		
		btnDrop.setBounds(897, 12, 98, 28);
		contentPane.add(btnDrop);
		btnDrop.addActionListener(new DropButtonListener());
		
		JButton btnSwap = new JButton("Swap...");
		btnSwap.setBounds(1007, 12, 98, 28);
		contentPane.add(btnSwap);
		btnSwap.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (timetableTabpage.getSelectedComponent() != own_table.getGUI()) {
					JOptionPane.showMessageDialog(contentPane, "Own timetable tab must be the active tab for the operation.");
					return;
				}
				Course origin = own_table.getSelectedCourse();
				if (origin==null) {
					JOptionPane.showMessageDialog(contentPane,
						    "Must select a course in own time table for swapping.",
						    "Swap Fails",
						    JOptionPane.WARNING_MESSAGE);
				}
				else {
					Component c = searchTabpage.getSelectedComponent();
					if (!(c instanceof CourseGUI)) {
						JOptionPane.showMessageDialog(contentPane,
							    "A course details page must be the active tab.",
							    "Swap Fails",
							    JOptionPane.WARNING_MESSAGE);
					}
					else {
						CourseGUI cgui = (CourseGUI) c;
						Course.Session[] sessions = cgui.getSelectedSessions();
						if (sessions.length==0) {
							JOptionPane.showMessageDialog(contentPane,
								    "No sessions of target course selected.",
								    "Swap Fails",
								    JOptionPane.WARNING_MESSAGE);
						}
						else {
							Course target = Course.getCourseByClassNum(sessions[0].getClassNo());
							TimetableError err = own_table.swapCourse(origin, target, sessions);
							showError(err, "Swap Fails");
						}
					}
				}
			}
		});
		
		JButton btnFindFreeTime = new JButton("Common Free Time");
		btnFindFreeTime.setBounds(1117, 12, 150, 28);
		contentPane.add(btnFindFreeTime);
		btnFindFreeTime.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				if (!haveLogined()) {
					showNotLoginError();
					return;
				}
				
				// a new tab
				TimeTableGUI newTable = new TimeTableGUI();
				
				// get slots that active time table that are filled
				TimeTableGUI activeTable = getSelectedTimeTableGUI();
				String friend_name = timetableTabpage.getTitleAt(timetableTabpage.getSelectedIndex());
				ArrayList<Course.Session> sessions_enrolled = new ArrayList<Course.Session>();
				Timetable friend_table = friends.get(friend_name);
				
				// get title of active time table
				int pos = timetableTabpage.indexOfComponent(activeTable);
				String activeTitle = timetableTabpage.getTitleAt(pos);
				if (activeTitle.indexOf("Mine VS ") != -1  ||  activeTable.equals(own_table.getGUI())) {
					JOptionPane.showMessageDialog(contentPane, "Can only find common free time with time tables of friends.",
							"Find Common Free Time", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				
				// get sessions that are enrolled in own_table
				for (ArrayList<Course.Session> arr : own_table.getEnrolled().values()) {
					sessions_enrolled.addAll(arr);
				}
				
				// get sessions that are enrolled in friend_table but not in own_table
				for (ArrayList<Course.Session> arr : friend_table.getEnrolled().values()) {
					for (Course.Session s : arr) {
						if (!sessions_enrolled.contains(s)) {
							sessions_enrolled.add(s);
						}
					}
				}
				
				
				try {
					// fill the whole new table with green color, to represent free time
					newTable.fillAllSlots(Color.GREEN);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				// fill the sessions enrolled in own_table and friend_table
				for (Course.Session s : sessions_enrolled) {
					for (TimePeriod tp : s.getSchedule()) {
						try {
							newTable.fillSlots(tp.getStartSlot().getID(), tp.getEndSlot().getID(),
									Color.LIGHT_GRAY, new String[]{}, "CommonFreeTime");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				String newTitle = "Mine VS "+activeTitle;
				/*int theIndex = timetableTabpage.indexOfTab(newTitle);
				if (theIndex!=-1) {
					timetableTabpage.remove(theIndex);
				}*/
				addClosableTab(timetableTabpage, newTable, newTitle, null);
			}
		});
	}
	
	
		// This method is obtained from the Internet
		//  http://paperjammed.com/2012/11/22/adding-tab-close-buttons-to-a-jtabbedpane-in-java-swing/
		/**
		   * Adds a component to a JTabbedPane with a little "close tab" button on the
		   * right side of the tab.
		   *
		   * @param tabbedPane the JTabbedPane
		   * @param c any JComponent
		   * @param title the title for the tab
		   * @param icon the icon for the tab, if desired
		   */
		  public static void addClosableTab(final JTabbedPane tabbedPane, final JComponent c, final String title,
		          final Icon icon) {
		    // Add the tab to the pane without any label
		    tabbedPane.addTab(null, c);
		    int pos = tabbedPane.indexOfComponent(c);

		    // Create a FlowLayout that will space things 5px apart
		    FlowLayout f = new FlowLayout(FlowLayout.CENTER, 5, 0);

		    // Make a small JPanel with the layout and make it non-opaque
		    JPanel pnlTab = new JPanel(f);
		    pnlTab.setOpaque(false);

		    // Add a JLabel with title and the left-side tab icon
		    JLabel lblTitle = new JLabel(title);
		    lblTitle.setIcon(icon);

		    // Create a JButton for the close tab button
		    JButton btnClose = new JButton("x");
		    btnClose.setForeground(Color.GRAY);
		    btnClose.setOpaque(false);

		    // Configure icon and rollover icon for button
//		    btnClose.setRolloverIcon(CLOSE_TAB_ICON);
//		    btnClose.setRolloverEnabled(true);
//		    btnClose.setIcon(RGBGrayFilter.getDisabledIcon(btnClose, CLOSE_TAB_ICON));

		    // Set border null so the button doesn't make the tab too big
		    btnClose.setBorder(null);

		    // Make sure the button can't get focus, otherwise it looks funny
		    btnClose.setFocusable(false);

		    // Put the panel together
		    pnlTab.add(lblTitle);
		    pnlTab.add(btnClose);

		    // Add a thin border to keep the image below the top edge of the tab
		    // when the tab is selected
		    pnlTab.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));

		    // Now assign the component for the tab
		    tabbedPane.setTabComponentAt(pos, pnlTab);
		    tabbedPane.setTitleAt(pos, title);

		    // Add the listener that removes the tab
		    ActionListener listener = new ActionListener() {
		      @Override
		      public void actionPerformed(ActionEvent e) {
		        // The component parameter must be declared "final" so that it can be
		        // referenced in the anonymous listener class like this.
		        tabbedPane.remove(c);
		      }
		    };
		    btnClose.addActionListener(listener);

		    // Optionally bring the new tab to the front
		    tabbedPane.setSelectedComponent(c);

		    //-------------------------------------------------------------
		    // Bonus: Adding a <Ctrl-W> keystroke binding to close the tab
		    //-------------------------------------------------------------
		    if (!(c instanceof TimeTableGUI)){  // only add this to searchTabpage but not timetableTabpage
		    	AbstractAction closeTabAction = new AbstractAction() {
			      @Override
			      public void actionPerformed(ActionEvent e) {
			        tabbedPane.remove(c);
			      }
			    };
	
			    // Create a keystroke
			    KeyStroke controlW = KeyStroke.getKeyStroke("control W");
	
			    // Get the appropriate input map using the JComponent constants.
			    // This one works well when the component is a container. 
			    InputMap inputMap = c.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW); 
	
			    // Add the key binding for the keystroke to the action name
			    inputMap.put(controlW, "closeTab");
	
			    // Now add a single binding for the action name to the anonymous action
		    	c.getActionMap().put("closeTab", closeTabAction);
		    }
		  }
		  
		  public static void addEscapeListener(final JDialog dialog) {
			  final KeyStroke escapeStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0); 
			  final String dispatchWindowClosingActionMapKey = "com.spodding.tackline.dispatch:WINDOW_CLOSING";  
			  Action dispatchClosing = new AbstractAction() { 
				  public void actionPerformed(ActionEvent event) { 
					  dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING)); 
				  } 
			  }; 
			  JRootPane root = dialog.getRootPane(); 
			  root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeStroke, dispatchWindowClosingActionMapKey); 
			  root.getActionMap().put( dispatchWindowClosingActionMapKey, dispatchClosing); 
		  }
		  
		  public static void showNotLoginError() {
			  JOptionPane.showMessageDialog(contentPane,
					    "Only available after logged in.",
					    "Error",
					    JOptionPane.WARNING_MESSAGE);
		  }
		  
		  // show a error prompt
		  public static void showError(TimetableError err, String title) {
			  Component component = contentPane;
			  switch (err) {
				case NoError:
					// do nothing if enrollment is successful
					break;
				case CourseEnrolled:
					JOptionPane.showMessageDialog(component,
						    "The course has already been enrolled.",
						    title,
						    JOptionPane.WARNING_MESSAGE);
					break;
				case CourseNotEnrolled:
					// this error should not be returned by addCourse()
					System.out.println("[CourseNotEnrolled] Unexpected error code returned by addCourse()");
					break;
				case CourseNotExists:
					// this message should never be prompted as the result list should only show existing course 
					JOptionPane.showMessageDialog(component,
						    "The course does not exists.",
						    title,
						    JOptionPane.WARNING_MESSAGE);
					break;
				case DuplicateSessionType:
					JOptionPane.showMessageDialog(component,
						    "Choose only one per session type.",
						    title,
						    JOptionPane.WARNING_MESSAGE);
					break;
				case InvalidSessions:
					// this message should never be prompted as the result list should only show valid sessions
					JOptionPane.showMessageDialog(component,
						    "Selected sessions are invalid.",
						    title,
						    JOptionPane.WARNING_MESSAGE);
					break;
				case SelfConflicts:
					JOptionPane.showMessageDialog(component,
						    "Between sessions chosen, there are time conflicts.",
						    title,
						    JOptionPane.WARNING_MESSAGE);
					break;
				case SessionTypeMissed:
					JOptionPane.showMessageDialog(component,
						    "Must choose one per session type.",
						    title,
						    JOptionPane.WARNING_MESSAGE);
					break;
				case SessionsNotMatched:
					JOptionPane.showMessageDialog(component,
						    "Matching between Lecture/Tutorial/Lab is required.",
						    title,
						    JOptionPane.WARNING_MESSAGE);
					break;
				case TimeConflicts:
					JOptionPane.showMessageDialog(component,
						    "Course with sessions selected has time conflicts with course already enrolled.",
						    title,
						    JOptionPane.WARNING_MESSAGE);
					break;
				case OtherErrors:
					// this error should not be returned by addCourse()
					System.out.println("[OtherErrors] Unexpected error code returned by addCourse()");
					break;
				default:
					System.out.println("Error code missed");
					break;
			}
		  }
}
