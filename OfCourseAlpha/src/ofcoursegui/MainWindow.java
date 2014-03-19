package ofcoursegui;

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

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 527580194533541981L;
	private JPanel contentPane;
	public static JTabbedPane tabpage= new JTabbedPane(JTabbedPane.TOP);

	public ofcourse.Timetable own_table = new ofcourse.Timetable(20097657);
	
	public static java.util.HashMap<JPanel, TimeTableGUI> linkage = new java.util.HashMap<JPanel, TimeTableGUI>();

	public TimeTableGUI getSelectedTimeTableGUI() {
		if(tabpage.getSelectedComponent() != null && linkage.get(tabpage.getSelectedComponent()) != null) {
			return linkage.get(tabpage.getSelectedComponent());
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
					frame.setVisible(true);
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
		
		tabpage.setBounds(557, 52, 705, 603);
		contentPane.add(tabpage);
		
		JPanel panel = new JPanel();
		tabpage.addTab("New tab", null, panel, null);
		
		JLabel lblMmmmmmmm = new JLabel("MMMMMMMM");
		lblMmmmmmmm.setPreferredSize(new Dimension(100, 18));
		lblMmmmmmmm.setMaximumSize(new Dimension(100, 18)); 
		lblMmmmmmmm.setMinimumSize(new Dimension(100, 18));
		lblMmmmmmmm.setSize(new Dimension(100, 18));
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
		panel.setLayout(null);
		panel.add(lblMmmmmmmm);
		
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
				JPanel testp = test.initilizeGUIComponent();
				tabpage.addTab("Whole New World", null, testp, null);
				linkage.put(testp, test);
			}
		});
		btnNewButton.setBounds(557, 12, 98, 28);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Delete Last");
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (tabpage.getTabCount() > 1)
					tabpage.remove(tabpage.getTabCount() - 1);
				tabpage.setSelectedIndex(0);
			}
		});
		btnNewButton_1.setBounds(667, 12, 98, 28);
		contentPane.add(btnNewButton_1);

		ofcourse.Course comp3111 = new ofcourse.Course("COMP", 3111, ' ', "Software Engineering", false);
		ArrayList<ofcourse.Course.Session> ss = new ArrayList<ofcourse.Course.Session>();
		ofcourse.Course.Session session = comp3111.new Session(ofcourse.SessionType.Lecture, 1, 1234);
		ArrayList<ofcourse.TimePeriod> tp = new ArrayList<ofcourse.TimePeriod>();
		tp.add(new ofcourse.TimePeriod(ofcourse.TimeSlot.getTimeSlotByID(206), ofcourse.TimeSlot.getTimeSlotByID(212)));
		session.setSchedule(tp);
		ss.add(session);
		comp3111.setSessions(ss);
		
		
		JButton btnNewButton_2 = new JButton("Enroll in COMP3111 ");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/*if(linkage.containsKey(tabpage.getSelectedComponent())) {
					TimeTableGUI test = linkage.get(tabpage.getSelectedComponent());
					try {
						test.fillSlots(212, 218, Color.ORANGE, new String[] {"COMP3111"}, "COMP3111");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}*/
				boolean result =  own_table.addCourse("COMP3111 ", new String[] { "L1" } );
				//DEBUG System.out.println(result);
				
			}
		});
		btnNewButton_2.setBounds(777, 12, 98, 28);
		contentPane.add(btnNewButton_2);
		
		
		
		
		
		
	}
}
