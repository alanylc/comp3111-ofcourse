package ofcoursegui;

import java.util.Collection;

import ofcourse.Course;
import ofcourse.SearchCourse;

public interface SearchCriteriaComponent {
	SearchCourse getSearchCourse(Collection<Course> prevSearch); 
}
