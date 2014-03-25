package ofcourse;

import java.util.Collection;

public class SearchAllCourse extends SearchCourse {

	public SearchAllCourse(Collection<Course> prevPipe) {
		super(prevPipe);
	}

	@Override
	public boolean checkCriteria(Course course) {
		return true;
	}

}
