package ofcourse;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JOptionPane;

public class SearchCodeInterval extends SearchCourse {
	private int[][] intervals = null;
	public SearchCodeInterval(Collection<Course> prevPipe) {
		super(prevPipe);
	}
	
	public SearchCodeInterval(Collection<Course> prevPipe, int[][] intervals) {
		super(prevPipe);
		this.intervals = intervals;
	}
	
	@Override
	public boolean checkCriteria(Course course) {
		for(int[] interval : intervals) {
			int num = course.getCode().getNumber();
			if (num >= interval[0] && num <= interval[1]) 
				continue;
			else 
				return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder result;
		if (prevPipe != null) result = new StringBuilder(prevPipe.toString());
		else result = new StringBuilder("");
		result.append("Code is");
		ArrayList<int[]> intervs = new ArrayList<int[]>();
		for (int i = 0; i < intervals.length; i++) {
			if (i - 1 >= 0 && intervals[i][0] - intervals[i - 1][1] == 1) {
				intervs.get(intervs.size() - 1)[1] = (intervals[i][1]);
			}
			else  {
				intervs.add(new int[] {intervals[i][0], intervals[i][1]});
			}
		}
		for (int i = 0; i < intervs.size(); i++) {
			result.append(" " + intervs.get(i)[0] + "-" + intervs.get(i)[1]);
		}
		
		return result.toString();
	}
}
