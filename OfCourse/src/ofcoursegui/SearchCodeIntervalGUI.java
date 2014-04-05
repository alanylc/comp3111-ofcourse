package ofcoursegui;

import javax.swing.AbstractListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import ofcourse.SearchCourse;
import ofcourse.SearchCodeInterval;

public class SearchCodeIntervalGUI extends JList implements SearchCriteriaComponent {
	
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
	public SearchCourse getSearchCourse(SearchCourse prevSearch) {
		int[] selecteds = this.getSelectedIndices();
		// If none are selected, it means to include all
		if(selecteds.length == 0) {
			return prevSearch;
		}
		else {
			int[][] intervals = new int[selecteds.length][];
			int c = 0;
			for (int i : selecteds) {
				int[] interval = new int[] { i * 1000, i * 1000 + 999 };
				intervals[c] = interval;
				c++;
			}
			SearchCodeInterval sci = new SearchCodeInterval(prevSearch, intervals);
		}
		return null;
	}

}
