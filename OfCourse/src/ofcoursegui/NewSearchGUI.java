package ofcoursegui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import ofcourse.Course;
import ofcourse.SearchAllCourse;
import ofcourse.SearchCodeInterval;
import ofcourse.SearchCourse;
import ofcourse.SearchSubject;

public class NewSearchGUI extends JPanel {

	private static int searchCount = 0;
	private JPanel subjectPanel = new JPanel();
	private JPanel levelPanel = new JPanel();
	JPanel otherPanel = new JPanel();
	//JList: subjectJList, for showing the subjects
	//ListModel: subjectList, the backing data for the JList
	private final JList subjectJList = new JList();
	public final AbstractListModel subjectListModel = new SubjectListModel();
	public final static class SubjectListModel extends AbstractListModel {
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
	};

	//Button: Search
	private final JButton searchButton = new JButton("Search");
	
	private final SearchCodeIntervalGUI levelList = new SearchCodeIntervalGUI();
	
	{
		setLayout(null);
		
		subjectPanel.setBorder(new TitledBorder(new LineBorder(new Color(153, 180, 209), 1, true), "Subject", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		subjectPanel.setBounds(12, 12, 504, 131);
		subjectPanel.setLayout(new GridLayout(1, 0, 0, 0));
		add(subjectPanel);
		
		JScrollPane scrollPaneSubject = new JScrollPane();
		scrollPaneSubject.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPaneSubject.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		subjectPanel.add(scrollPaneSubject);
		
		subjectJList.setVisibleRowCount(4);
		subjectJList.setLayoutOrientation(JList.VERTICAL_WRAP);
		subjectJList.setModel(this.subjectListModel);
		scrollPaneSubject.setViewportView(subjectJList);
		

		levelPanel.setBorder(new TitledBorder(new LineBorder(new Color(153, 180, 209), 1, true), "Level", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		levelPanel.setBounds(12, 155, 504, 131);
		levelPanel.setLayout(new GridLayout(1, 0, 0, 0));
		add(levelPanel);
		
		JScrollPane scrollPaneLevel = new JScrollPane();
		levelPanel.add(scrollPaneLevel);
		
		scrollPaneLevel.setViewportView(levelList);
		
		otherPanel.setBorder(new TitledBorder(new LineBorder(new Color(153, 180, 209), 1, true), "Others", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		otherPanel.setBounds(12, 298, 504, 131);
		add(otherPanel);
		
		searchButton.setBounds(418, 533, 98, 28);
		add(searchButton);
		
		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//JOptionPane.showMessageDialog(null, levelList.getSearchCourse(null).toString());
				

				ArrayList<String> subjectCriteria = new ArrayList<String>();
				for(Object sub : subjectJList.getSelectedValues()) {
					subjectCriteria.add((String)sub);
				}
								
				SearchCourse cs = levelList.getSearchCourse(new SearchSubject(new SearchAllCourse(MainWindow.allCourses), subjectCriteria));
				
				SearchResultGUI newResult = new SearchResultGUI();
				for(Course c : cs) {
					//System.out.println(c.toString());
					newResult.addResult(c);
				}
				newResult.setCriteriaText(cs.toString());
				//System.out.println(cs.size());
				//System.out.println(cs.toString());
				
				
				
				//TODO: Remove dependency on MainWindow
				searchCount++;
				//MainWindow.searchTabpage.addTab("Result"+searchCount, null, newResult, null);
				MainWindow.addClosableTab(MainWindow.searchTabpage, newResult, "Result"+searchCount, null);
				MainWindow.searchTabpage.setSelectedComponent(newResult);
			}
		});
	}
	
}
