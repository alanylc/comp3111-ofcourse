package ofcourse;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JOptionPane;

import ofcoursegui.MainWindow;

public class SearchCodeInterval extends SearchCourse {
	private ArrayList<int[]> intervals = new ArrayList<int[]>();
	public SearchCodeInterval(Collection<Course> prevPipe) {
		super(prevPipe);
	}
	
	public SearchCodeInterval(Collection<Course> prevPipe, ArrayList<int[]> intervals) {
		super(prevPipe);
		this.intervals = intervals;
	}
	
	@Override
	public boolean checkCriteria(Course course) {
		if (intervals == null) return true;
		for(int[] interval : intervals) {
			int num = course.getCode().getNumber();
			if (num >= interval[0] && num <= interval[1]) 
				return true;
			else 
				continue;
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuilder result;
		if (prevPipe != null) result = new StringBuilder(prevPipe.toString());
		else result = new StringBuilder("");
		if(intervals == null || intervals.size() == 0) { 
			return result.toString();
		}
		result.append("Code is");
		ArrayList<int[]> intervs = new ArrayList<int[]>();
		for (int i = 0; i < intervals.size(); i++) {
			if (i - 1 >= 0 && intervals.get(i)[0] - intervals.get(i - 1)[1] == 1) {
				intervs.get(intervs.size() - 1)[1] = (intervals.get(i)[1]);
			}
			else  {
				intervs.add(new int[] {intervals.get(i)[0], intervals.get(i)[1]});
			}
		}
		for (int i = 0; i < intervs.size(); i++) {
			result.append(" " + intervs.get(i)[0] + "-" + intervs.get(i)[1]);
		}

		result.append(". ");
		return result.toString();
	}
}
