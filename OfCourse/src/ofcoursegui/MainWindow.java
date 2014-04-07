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

	JButton btnEnroll = new JButton("Enroll...");
	public class EnrollButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			boolean result =  (own_table.addCourse("COMP2012 ", new String[] { "L1", "LA1A" } ) == TimetableError.NoError);
			boolean result2 =  (own_table.addCourse("LANG2030H", new String[] { "T1" } )  == TimetableError.NoError);
			//DEBUG System.out.println("result: " + result);
			//DEBUG System.out.println("result: " + result2);
			
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

	
	public Timetable own_table;
	
	public static ArrayList<Course> allCourses;
	
	
	public static java.util.HashMap<JPanel, TimeTableGUI> linkage = new java.util.HashMap<JPanel, TimeTableGUI>();
	//private JTable table;

	
	public TimeTableGUI getSelectedTimeTableGUI() {
		if(timetableTabpage.getSelectedComponent() != null && linkage.get(timetableTabpage.getSelectedComponent()) != null) {
			return linkage.get(timetableTabpage.getSelectedComponent());
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
					ArrayList<courseParse> result = courseParse.fullparse();
					frame.setVisible(true);
					
					for(courseParse cp : result) {
						((NewSearchGUI.SubjectListModel)((frame.newSearchPanel.subjectListModel))).addElement((cp.getSubject()));
					}
					allCourses = new ArrayList<Course>();
					for(courseParse cp : result) {
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
		JButton btnNewButton = new JButton("New One");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				TimeTableGUI test = new TimeTableGUI();
				//JPanel testp = test.initilizeGUIComponent();
				timetableTabpage.addTab("20140401", null, test, null);
				linkage.put(test, test);
			}
		});
		btnNewButton.setBounds(557, 12, 98, 28);
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
		btnNewButton_1.setBounds(667, 12, 98, 28);
		contentPane.add(btnNewButton_1);

		//Initialize Enroll button
		btnEnroll.addActionListener(new EnrollButtonListener());
		btnEnroll.setBounds(777, 12, 98, 28);
		contentPane.add(btnEnroll);
		
		//JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		searchTabpage.setBounds(12, 52, 533, 603);
		contentPane.add(searchTabpage);
		
		
		searchTabpage.addTab("New Search", null, newSearchPanel, null);
		
		//JPanel coursePanel = new JPanel();
		/*searchTabpage.addTab("New tab", null, coursePanel, null);
		coursePanel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 62, 504, 226);
		coursePanel.add(scrollPane);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
			},
			new String[] {
				"Session", "Time", "Room", "Instructor"
			}
		));
		scrollPane.setViewportView(table);
		
		JLabel courseCodeLabel = new JLabel("CourseCode");
		courseCodeLabel.setBounds(12, 12, 504, 18);
		coursePanel.add(courseCodeLabel);
		
		JLabel courseNameLabel = new JLabel("CoureName");
		courseNameLabel.setBounds(12, 32, 504, 18);
		coursePanel.add(courseNameLabel);*/
		
		
		
		JLabel lblMmmmmmmm = new JLabel("MMMMMMMM");
		lblMmmmmmmm.setBounds(12, 12, 100, 18);
		contentPane.add(lblMmmmmmmm);
		lblMmmmmmmm.setPreferredSize(new Dimension(100, 18));
		lblMmmmmmmm.setMaximumSize(new Dimension(100, 18)); 
		lblMmmmmmmm.setMinimumSize(new Dimension(100, 18));
		lblMmmmmmmm.setForeground(Color.RED);
		lblMmmmmmmm.setBackground(Color.WHITE);
		lblMmmmmmmm.setOpaque(true);
		lblMmmmmmmm.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JLabel source = ((JLabel)arg0.getSource());
				if (source.getForeground() == Color.ORANGE) {
					source.setForeground(Color.RED);
				}
				else {
					source.setForeground(Color.ORANGE);
				}
			}
		});
		
		btnDrop.setBounds(887, 12, 98, 28);
		contentPane.add(btnDrop);
		btnDrop.addActionListener(new DropButtonListener());
		
		
		
		
		
		
		
	}
}
