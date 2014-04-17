package ofcoursegui;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.AbstractListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;

import ofcourse.Course;
import ofcourse.SearchAllCourse;
import ofcourse.SearchCourse;
import ofcourse.SearchSubject;

public class SearchSubjectGUI extends JList implements SearchCriteriaComponent {
	
	public final AbstractListModel subjectListModel = new SubjectListModel();
	public final static class SubjectListModel extends AbstractListModel {
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
	
	{
		setModel(this.subjectListModel);
		setVisibleRowCount(5);
		setLayoutOrientation(JList.HORIZONTAL_WRAP);
	}
	
	@Override
	public SearchCourse getSearchCourse(Collection<Course> prevSearch) {
		ArrayList<String> subjectCriteria = new ArrayList<String>();
		for(Object sub : getSelectedValues()) {
			subjectCriteria.add((String)sub);
		}
		
		if (subjectCriteria.size()==0) {
			//JOptionPane.showMessageDialog(MainWindow.contentPane, "You have to choose at least one subject first.");
			//return null;
			return new SearchSubject(prevSearch, null);
		}
		return new SearchSubject(prevSearch, subjectCriteria);
	}

}
