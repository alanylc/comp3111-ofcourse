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
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JComponent;
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
	
	private JPanel contentPane;
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
					
					ArrayList<CourseParse> result = CourseParse.fullparse();
					
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
		
		JMenuItem mntmExportTimeTable = new JMenuItem("Export Time Table...");
		mnFile.add(mntmExportTimeTable);
		
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
		
		JButton btnNewButton_1 = new JButton("Delete Last");
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (timetableTabpage.getTabCount() > 1)
					timetableTabpage.remove(timetableTabpage.getTabCount() - 1);
				timetableTabpage.setSelectedIndex(0);
			}
		});
		btnNewButton_1.setBounds(787, 12, 98, 28);
		contentPane.add(btnNewButton_1);
		
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
			    InputMap inputMap = c.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
	
			    // Add the key binding for the keystroke to the action name
			    inputMap.put(controlW, "closeTab");
	
			    // Now add a single binding for the action name to the anonymous action
		    	c.getActionMap().put("closeTab", closeTabAction);
		    }
		  }
}
