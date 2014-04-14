package ofcoursegui;

import ofcourse.*;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JToolBar;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;

import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JSeparator;

import java.awt.Button;

import javax.swing.JButton;
import javax.swing.BoxLayout;
import javax.swing.JInternalFrame;

import java.awt.Panel;

import javax.swing.JTabbedPane;

import java.awt.Checkbox;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout;
import javax.swing.JTextPane;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.JList;
import javax.swing.AbstractListModel;

import java.awt.GridLayout;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;
import com.jgoodies.forms.factories.FormFactory;

import java.awt.Dimension;

import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.File;

import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JScrollBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTextField;

public class MainWindow extends JFrame {
	
	
	public class SearchButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	}
	
	JButton btnDrop = new JButton("Drop...");
	public class DropButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Course selection = own_table.getSelectedCourse();
			if (selection != null) {
				own_table.courseUnselected();
				own_table.dropCourse(selection);
			}
		}
	}
	
	NewSearchGUI newSearchPanel = new NewSearchGUI();
	
	//public static ArrayList<SearchResultGUI> searchResultPanels = new ArrayList<SearchResultGUI>();
	
	private static JPanel contentPane;
	public JTabbedPane timetableTabpage = new JTabbedPane(JTabbedPane.TOP);
	public static JTabbedPane searchTabpage = new JTabbedPane(JTabbedPane.TOP);

	
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
					
					ArrayList<CourseParse> result = CourseParseThreaded.fullparse();
					
//					ArrayList<CourseParse> result = new ArrayList<CourseParse>(); result.add(CourseParse.parse("COMP")); result.add(CourseParse.parse("ELEC"));

					frame.setVisible(true);
					
					for(CourseParse cp : result) {
						((NewSearchGUI.SubjectListModel)((frame.newSearchPanel.subjectListModel))).addElement((cp.getSubject()));
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
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */

	@SuppressWarnings("serial")
	public MainWindow() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1280, 720);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmImportTimeTable = new JMenuItem("Import Time Table...");
		mnFile.add(mntmImportTimeTable);
		mntmImportTimeTable.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
			final JFileChooser fc = new JFileChooser();
		        int returnVal = fc.showOpenDialog(contentPane);
		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            // own_table.tid will not change (only import timetable but not TID)
		            int tid = own_table.getTableId();
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
		
		JMenuItem mntmExportTimeTable = new JMenuItem("Export Time Table...");
		mnFile.add(mntmExportTimeTable);
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
		
		JSeparator separator = new JSeparator();
		mnFile.add(separator);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		timetableTabpage.setBounds(557, 52, 705, 603);
		contentPane.add(timetableTabpage);
		
		own_table = new Timetable(20097657, timetableTabpage);
		
		//Buttons
		JButton btnNewButton = new JButton("See Friend");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				TimeTableGUI test = new TimeTableGUI();
				//JPanel testp = test.initilizeGUIComponent();
				//timetableTabpage.addTab("20140401", null, test, null);
				addClosableTab(timetableTabpage, test, "20140401", null);
			}
		});
		btnNewButton.setBounds(677, 12, 98, 28);
		contentPane.add(btnNewButton);
		
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
		
		//JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		searchTabpage.setBounds(12, 52, 533, 603);
		contentPane.add(searchTabpage);
		
		
		searchTabpage.addTab("New Search", null, newSearchPanel, null);
		searchTabpage.setMnemonicAt(searchTabpage.getTabCount()-1, KeyEvent.VK_S);
		searchTabpage.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		
		btnDrop.setBounds(897, 12, 98, 28);
		contentPane.add(btnDrop);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(557, 12, 116, 28);
		contentPane.add(scrollPane);
		
		JList list = new JList();
		list.setModel(new AbstractListModel() {
			String[] values = new String[] {"20140401"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		scrollPane.setViewportView(list);
		btnDrop.addActionListener(new DropButtonListener());
		
		JButton btnSwap = new JButton("Swap...");
		btnSwap.setBounds(1007, 12, 98, 28);
		contentPane.add(btnSwap);
		btnSwap.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
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
				// a new tab
				TimeTableGUI newTable = new TimeTableGUI();
				
				// get slots that active time table that are filled
				TimeTableGUI activeTable = getSelectedTimeTableGUI();
				int[] filledSlots = activeTable.getFilledSlots();
				ArrayList<Course.Session> sessions_enrolled = new ArrayList<Course.Session>();
				
				// get sessions that are enrolled in own_table
				java.util.Collection<ArrayList<Course.Session>> collection = own_table.getEnrolled().values();
				for (ArrayList<Course.Session> arr : collection) {
					sessions_enrolled.addAll(arr);
				}
				
				
				try {
					// fill the whole new table with green color, to represent free time
					newTable.fillAllSlots(Color.GREEN);
					// fill the slots enrolled in active time table
					newTable.fillSlots(filledSlots, Color.LIGHT_GRAY, "CommonFreeTime");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				// fill the sessions enrolled in own_table
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
				// get title of active time table
				int pos = timetableTabpage.indexOfComponent(activeTable);
				String activeTitle = timetableTabpage.getTitleAt(pos);
				if (activeTitle.indexOf("Mine VS ") == -1  &&  !activeTable.equals(own_table.getGUI())) {
					addClosableTab(timetableTabpage, newTable, "Mine VS "+activeTitle, null);
				}
				else {
					JOptionPane.showMessageDialog(contentPane, "Can only find common free time with time tables of friends.");
				}
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
