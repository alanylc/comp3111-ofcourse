package ofcourse;

import java.util.Collection;
import java.util.ArrayList;;

public class SearchSubject extends SearchCourse {

	private ArrayList<String> subjects = new ArrayList<String>();
	
	public SearchSubject(Collection<Course> prevPipe) {
		super(prevPipe);
	}
	
	public SearchSubject(Collection<Course> prevPipe, ArrayList<String> subjects) {
		super(prevPipe);
		this.subjects = subjects;
	}
	

	@Override
	public boolean checkCriteria(Course course) {
		for(String s : subjects){
			if(course.getCode().getDept().equals(s)) {
				return true;
			}
		}
		return false;
	}

}
