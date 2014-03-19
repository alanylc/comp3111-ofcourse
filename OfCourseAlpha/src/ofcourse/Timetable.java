package ofcourse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import ofcoursegui.TimeTableGUI;


public class Timetable {
	
	private int table_id = -1;
	//private ArrayList<TimeSlot> table = new ArrayList<TimeSlot>(); 
	//Course, classNo
	private HashMap<Course, ArrayList<Course.Session>> enrolled = new HashMap<Course, ArrayList<Course.Session>>();
	private ofcoursegui.TimeTableGUI gui = new ofcoursegui.TimeTableGUI();
	
	public Timetable(int tid) {
		ofcoursegui.MainWindow.tabpage.add(Integer.toString(tid), gui.initilizeGUIComponent());
		table_id = tid;
	}
	
	public int getTableId() {
		return table_id;
	}
	
	public void setTableId(int id) {
		table_id = id;
	}
	
	
	
/*	public TimeSlot getCourseSlotsById(int course_id) {
//		for (int i=0; i<table.size(); i++) {
//			if (table.get(i).getCourseId().compareTo(new Integer(course_id))==0) {
//				return table.get(i);
//			}
//		}
//		return null;
//	}
*/
	
/*	public ArrayList<Integer> getOccupied() {
//		ArrayList<Integer> arrList = new ArrayList<Integer>();
//		for (TimeSlot course : table) {
//			arrList.addAll(course.getSlots());
//		}
//		return arrList;
//	}
*/
	
	public HashMap<Course, ArrayList<Course.Session>> getEnrolled() {
		return enrolled;
	}

	public void setEnrolled(HashMap<Course, ArrayList<Course.Session>> enrolled) {
		this.enrolled = enrolled;
	}

	public ArrayList<TimeSlot> getOccupied() { 
		ArrayList<TimeSlot> arrList = new ArrayList<TimeSlot>();
		for (java.util.Map.Entry<Course, ArrayList<Course.Session>> enrolledCourse : getEnrolled().entrySet()) {
		    for (Course.Session session : enrolledCourse.getValue()) {
		    	ArrayList<TimePeriod> periods = session.getSchedule();
		    	for (TimePeriod p : periods) {
		    		arrList.addAll(java.util.Arrays.asList(p.getAllSlots()));
		    	}
			}
		}
		return arrList;
	}
	
	private ArrayList<TimeSlot> sessionsToSlots(Course.Session[] sessions) {
		//List all time slots to be used by this course registration
		ArrayList<TimeSlot> timeSlots = new ArrayList<TimeSlot>();
		for (Course.Session s : sessions) {
			ArrayList<TimeSlot> temp =  new ArrayList<TimeSlot>();
			for (TimePeriod tp : s.getSchedule()) {
				temp.addAll(Arrays.asList(tp.getAllSlots()));
			}
			timeSlots.addAll(temp);
		}
		return timeSlots;
	}
	
	public boolean addCourse(String course_id, String[] sessions_id) {
		ArrayList<Course.Session> sessions = new ArrayList<Course.Session>();
		Course c = Course.getCourseByName(course_id);
		if (c == null) return false;
		for (Course.Session s : c.getSessions()) {
			for(String sessionid : sessions_id) {
				//DEBUG System.out.println(s.toString());
				if (s.toString().equals(sessionid)) {
					sessions.add(s);
				}
			}
		}
		if (sessions == null || sessions.size() == 0) 
			return false;
		return addCourse(c, sessions.toArray(new Course.Session[sessions.size()]));
	}
	
	private boolean addCourse(Course course, Course.Session[] sessions) {
		//TODO: Need advanced checking for if lab/tutorial is included
		// if the course has already been enrolled, fails
		if (getEnrolled().containsKey(course)) {
			return false;
		}
		//List all time slots to be used by this course registration
		ArrayList<TimeSlot> timeSlots = sessionsToSlots(sessions);
		// check time conflict within itself
		// efficiency is not a matter: the array tends to be small
		for (int i = 0; i < timeSlots.size(); i++) {
            for (int j = 0; j < timeSlots.size(); j++) {
                if (timeSlots.get(i).equals(timeSlots.get(j)) && i != j) {
                    return false;
                }
            }
        }
		// check time conflict with enrolled
		ArrayList<TimeSlot> occupied = getOccupied();
		boolean conflict = false;
		for (int i = 0; i < timeSlots.size() && !conflict; i++) {
			if (occupied.contains(timeSlots.get(i))) {
				conflict = true;
			}
		}
		if (conflict) {
			return false;
		}
		else {
			//put it to enrolled
			ArrayList<Course.Session> ss = new ArrayList<Course.Session>();
			ss.addAll(Arrays.asList(sessions));
			//duplicate courses are automatically overwritten
			enrolled.put(course, ss);
		}

		//GUI call
		for (Course.Session s : sessions) {
			for (TimePeriod tp : s.getSchedule()) {
				try {
					gui.fillSlots(
							tp.getStartSlot().getID(), 
							tp.getEndSlot().getID(), 
							TimeTableGUI.getRandomBgColor(), 
							new String[] { course.toString(), s.toString() }, 
							course.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return true;
	}
	

	public boolean dropCourse(String course_id) {
		return dropCourse(Course.getCourseByName(course_id));
	}
	
	public boolean dropCourse(Course course) {
		if (course == null) 
			return false;
		if (!getEnrolled().containsKey(course)) {
			return false;
		}
		else {
			getEnrolled().remove(course);
		}
		//GUI call
		gui.unfillSlots(course.toString());
		return true;
	}
	
	public boolean swapCourse(Course origin, Course target, Course.Session[] sessions) {
		boolean conflict = false;
		
		// if target course has already been enrolled, fails
		if (getEnrolled().containsKey(origin)) {
			return false;
		}
		//List all time slots to if the original course is dropped
		ArrayList<TimeSlot> remainingSlots = new ArrayList<TimeSlot>();
		for (java.util.Map.Entry<Course, ArrayList<Course.Session>> enrolledCourse : getEnrolled().entrySet()) {
			if (enrolledCourse.getKey() == origin) continue;
			else {
			    for (Course.Session session : enrolledCourse.getValue()) {
			    	ArrayList<TimePeriod> periods = session.getSchedule();
			    	for (TimePeriod p : periods) {
			    		remainingSlots.addAll(java.util.Arrays.asList(p.getAllSlots()));
			    	}
				}
			}
		}
		//List all time slots to be used by this course registration
		ArrayList<TimeSlot> timeSlots = sessionsToSlots(sessions);
		
		for (TimeSlot ts : timeSlots) {
			if (remainingSlots.contains(ts)) 
				conflict = true;
		}
		// conflict even after dropping
		if (conflict) {
			return false;
		}
		else {
			ArrayList<Course.Session> temp = getEnrolled().get(origin);
			if(dropCourse(origin) == false) return false;
			if(addCourse(target, sessions) == false) {
				addCourse(origin, temp.toArray(new Course.Session[temp.size()]));
			}
		}

		return true;
	}

}
