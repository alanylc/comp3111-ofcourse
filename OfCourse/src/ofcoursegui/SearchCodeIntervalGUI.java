package ofcoursegui;

import java.util.Collection;

import javax.swing.AbstractListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import ofcourse.Course;
import ofcourse.SearchAllCourse;
import ofcourse.SearchCourse;
import ofcourse.SearchCodeInterval;

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
			return new SearchAllCourse(prevSearch);
		}
		else {
			int[][] intervals = new int[selecteds.length][];
			int c = 0;
			for (int i : selecteds) {
				int[] interval = new int[] { (i + 1) * 1000, (i + 1) * 1000 + 999 };
				intervals[c] = interval;
				c++;
			}
			sci = new SearchCodeInterval(prevSearch, intervals);
		}
		return sci;
	}
	
	

}
