package ofcourse;

import java.util.Collection;

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
}
