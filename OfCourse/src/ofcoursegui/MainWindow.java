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
	
	/*public class SubjectListModel extends AbstractListModel {
		private static final long serialVersionUID = 1922055068864190054L;
		ArrayList<String> values = new ArrayList<String>();
		public int getSize() {
			return values.size();
		}
		public Object getElementAt(int index) {
			return values.get(index);
		}
		public void addElement(String subject) {
			values.add(subject);
		}
	}*/
	
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
	
	private JPanel contentPane;
	public JTabbedPane timetableTabpage = new JTabbedPane(JTabbedPane.TOP);
	public JTabbedPane searchTabpage = new JTabbedPane(JTabbedPane.TOP);

	
	public Timetable own_table;
	
	/*public final JList subjectJList = new JList();
	public final SubjectListModel subjectList = new SubjectListModel();

	//Button: Search
	JButton searchButton = new JButton("Search");
	*/
	public static java.util.HashMap<JPanel, TimeTableGUI> linkage = new java.util.HashMap<JPanel, TimeTableGUI>();
	private JTable table;

	
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
					for(courseParse cp : result) {
						((NewSearchGUI.SubjectListModel)((frame.newSearchPanel.subjectListModel))).addElement((cp.getSubject()));
					}
					frame.setVisible(true);
					ArrayList<Course> cc = new ArrayList<Course>();
					for(courseParse cp : result) {
						cc.addAll(cp.getCourses());
					}
					/*ArrayList<String> criteria = new ArrayList<String>();
					criteria.add("HUMA");
					criteria.add("ECON");
					SearchCourse cs = new SearchSubject(new SearchAllCourse(cc), criteria);
					for(Course c : cs) {
						System.out.println(c.toString());
					}*/
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */

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
		/*newSearchPanel.setLayout(null);
		
		JPanel subjectPanel = new JPanel();
		subjectPanel.setBorder(new TitledBorder(new LineBorder(new Color(153, 180, 209), 1, true), "Subject", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		subjectPanel.setBounds(12, 12, 504, 131);
		newSearchPanel.add(subjectPanel);
		subjectPanel.setLayout(new GridLayout(1, 0, 0, 0));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		subjectPanel.add(scrollPane_1);
		
		subjectJList.setVisibleRowCount(4);
		subjectJList.setLayoutOrientation(JList.VERTICAL_WRAP);
		subjectJList.setModel(this.subjectList);
		scrollPane_1.setViewportView(subjectJList);
		
		JPanel levelPanel = new JPanel();
		levelPanel.setBorder(new TitledBorder(new LineBorder(new Color(153, 180, 209), 1, true), "Level", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		levelPanel.setBounds(12, 155, 504, 131);
		newSearchPanel.add(levelPanel);
		levelPanel.setLayout(new GridLayout(1, 0, 0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		levelPanel.add(scrollPane);
		
		JList list = new JList();
		list.setModel(new AbstractListModel() {
			int[] values = new int[] {1000, 2000, 3000, 4000, 5000, 6000, 7000};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		SearchCodeIntervalGUI codeIntervalList = new SearchCodeIntervalGUI();
		scrollPane.setViewportView(codeIntervalList);
		
		JPanel otherPanel = new JPanel();
		otherPanel.setBorder(new TitledBorder(new LineBorder(new Color(153, 180, 209), 1, true), "Others", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		otherPanel.setBounds(12, 298, 504, 131);
		newSearchPanel.add(otherPanel);
		
		searchButton.setBounds(418, 533, 98, 28);
		newSearchPanel.add(searchButton);*/
		
		JPanel panel = new JPanel();
		searchTabpage.addTab("New tab", null, panel, null);
		
		table = new JTable();
		table.setBounds(12, 96, 504, 160);
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
			},
			new String[] {
				"New column", "New column", "New column", "New column", "New column"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, Object.class, Object.class, Object.class, Object.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		table.getColumnModel().getColumn(0).setResizable(false);
		panel.setLayout(null);
		panel.add(table);
		
		JLabel lblSearchCriteria = new JLabel("Search Criteria:");
		lblSearchCriteria.setBounds(12, 12, 94, 18);
		panel.add(lblSearchCriteria);
		
		JLabel lblNewLabel = new JLabel("c1");
		lblNewLabel.setBounds(22, 30, 494, 18);
		panel.add(lblNewLabel);
		
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
