package ofcoursegui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import ofcourse.Course;
import ofcourse.Network;

public class MyFavPanel extends JPanel {
	
	private class FavListModel extends AbstractListModel {
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
			this.sort();
		}
		public void addAll(String[] _values) {
			for (String v : _values) {
				this.add(v);
			}
		}
		public void remove(String code) {
			int index0 = this.values.indexOf(code);
			values.remove(code);
			this.fireContentsChanged(this, index0, index0);
		}
		public void removeAll() {
			values.clear();
			this.fireContentsChanged(this, 0, 0);
		}
		public void sort() {
			Collections.sort(values);
			this.fireContentsChanged(this, 0, this.getSize());
		}
		public String getFavStr() {
			String fav="";
			String course_code="";
			String[] tmp_code=null;
			for (String s : values) {
				tmp_code = s.substring(0, s.indexOf(" - ")).split(" ");
				course_code = tmp_code[0] + tmp_code[1];
				fav = fav + course_code + "!";
			}
			return fav;
		}
	}
	
	private String favStr = "";
	private Component parent = null;
	private FavListModel favModel = new FavListModel();
	private final JList favList = new JList(favModel);
	
	public MyFavPanel(Component _parent) {
		this.parent = _parent;
		
		this.setLayout(new BorderLayout());
		JPanel pane = new JPanel();
		pane.setBorder(new EmptyBorder(10, 10, 10, 10));
		pane.setLayout(new GridLayout(1, 0, 0, 0));
		favList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		favList.setName("myfavlist");
		JScrollPane favPane = new JScrollPane(favList);
		pane.add(favPane);
		updateFav();
		
		JPanel rmPane = new JPanel();
		rmPane.setLayout(new BorderLayout());
		rmPane.setBorder(new EmptyBorder(2, 10, 10, 15));
		JButton viewBtn = new JButton("View Course Details");
		viewBtn.addActionListener(new viewBtnListener());
		JButton rmBtn = new JButton("Remove from My Favourite");
		rmBtn.addActionListener(new rmBtnListener());
		JPanel innerPane = new JPanel();
		innerPane.setLayout(new GridLayout(1, 2, 5, 2));
		innerPane.add(viewBtn);
		innerPane.add(rmBtn);
		rmPane.add(innerPane, BorderLayout.EAST);
		
		
		this.add(pane, BorderLayout.CENTER);
		this.add(rmPane, BorderLayout.SOUTH);
		this.setVisible(true);
	}
	
	public void addFav(String course_code) {
		course_code = course_code.trim();
		if (course_code.length()==8) { // No Set for the course (e.g. COMP2012 has no set, COMP2012H has set)
			course_code = course_code + " ";
		}
		else if (course_code.length()!=9) { // if has set, length==9, if length!=9, error -> do nothing
			return;
		}
		this.favModel.add(Course.getCourseByName(course_code).getName());
	}
	
	public void clearFav() {
		this.favModel.removeAll();
	}
	
	public void updateFav() {
		if (MainWindow.haveLogined()) {
			this.clearFav();
			this.favStr = Network.getOurNetwork().getMyFav();
			if (this.favStr.equals("404")) return; // Network connection error
			if (this.favStr.length()!=0) {
				for (String str : favStr.split("!")) {
					if (!str.isEmpty())  this.addFav(str);
				}
			}
		}
	}
	
	private class viewBtnListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Object selected = favList.getSelectedValue();
			if (selected == null) {
				JOptionPane.showMessageDialog(MainWindow.contentPane, "No Course Selected.");
				return;
			}
			String objStr = selected.toString();
			String[] tmp_code = objStr.substring(0, objStr.indexOf(" - ")).trim().split(" ");
			String course_code = tmp_code[0]+tmp_code[1];
			if (course_code.length()==8) {
				course_code = course_code + " ";
			}
			Course c = Course.getCourseByName(course_code);
			CourseGUI cgui = null;
    		int tab_pos = MainWindow.searchTabpage.indexOfTab(c.getCode().toString());
    		if (tab_pos==-1) { // the tab of the course does not exist yet
    			cgui = new CourseGUI(c);
        		MainWindow.addClosableTab(MainWindow.searchTabpage, cgui, c.getCode().toString(), null);
    		}
    		else { // the tab of the course already exist
    			cgui = (CourseGUI) MainWindow.searchTabpage.getComponentAt(tab_pos);
    		}
    		MainWindow.searchTabpage.setSelectedComponent(cgui);
		}
	}
	
	private class rmBtnListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Object selected = favList.getSelectedValue();
			if (selected == null) {
				JOptionPane.showMessageDialog(MainWindow.contentPane, "No Course Selected.");
				return;
			}
			favModel.remove(selected.toString());
			System.out.println(">> "+favModel.getFavStr());
			Network.getOurNetwork().setMyFav(favModel.getFavStr());
		}
	}

}
