package ofcoursegui;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.AbstractListModel;
import javax.swing.JList;

import ofcourse.Course;
import ofcourse.SearchCodeInterval;
import ofcourse.SearchCourse;

@SuppressWarnings("serial")
public class SearchCodeIntervalGUI extends JList implements SearchCriteriaComponent {
	
	SearchCodeInterval sci = null;
	{
		setModel(new AbstractListModel() {
			int[] values = new int[] {1000, 2000, 3000, 4000, 5000, 6000, 7000};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
	}
	
	@Override
	public SearchCourse getSearchCourse(Collection<Course> prevSearch) {
		int[] selecteds = this.getSelectedIndices();
		// If none are selected, it means to include all
		if(selecteds.length == 0) {
			return new SearchCodeInterval(prevSearch, null);
		}
		else {
			ArrayList<int[]> intervals = new ArrayList<int[]>();
			for (int i : selecteds) {
				int[] interval = new int[] { (i + 1) * 1000, (i + 1) * 1000 + 999 };
				intervals.add(interval);
			}
			sci = new SearchCodeInterval(prevSearch, intervals);
		}
		return sci;
	}
	
	

}
