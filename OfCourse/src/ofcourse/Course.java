package ofcourse;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


//TODO: all finished class must be declared final
/**
 * <tt>Course</tt> represents a course. It contains all information that 
 * are normally available in the student center: all sessions, description 
 * code, instructors and quota.
 * 
 * @author Bob
 */ 
public class Course extends Ratable { 
	//TODO: check valid data at all times
	private Code code;
	private boolean matchSession = false;
	private ArrayList<Session> sessions=new ArrayList<Session>();
	private String description = "";
	//private Course[] prerequsite = null;
	private ArrayList<String> CC=new ArrayList<String>();
	private String Attributes;
	private String PreRequisite;
	private String CoRequisite;
	private String Exclusion;
	private String PreviousCode;			//Useless(?) info from course page
	private String CoList;

	public static ArrayList<Course> AllCourses = new ArrayList<Course>();
	
	public static Course getCourseByName(String name) {
		for(Course c : AllCourses) {
			if (name.equals(c.toString())) {
				return c;
			}
		}
		return null;
	}
	
	public static Course getCourseByClassNum(int classno) {
		Session s = null;
		for (Course c : AllCourses) {
			s = c.getSessionByClassNumber(classno);
			if (s!=null) return c;
		}
		return null;
	}
	
	{
		AllCourses.add(this);
	}
	
	/**
	 * Construct a new course with the provided information. 
	 * 
	 * @param dept
	 * @param number
	 * @param modifier
	 * @param name
	 */
	/*
	public Course(String dept, int number, char modifier, String name, boolean matchSession) {
		super(name);
		code = new Code(dept, number, modifier);//TODO: check valid arguments
		setMatchSession(matchSession);
	}
	*/
	/**
	 * Construct a new course with the provided information. 
	 * @param code
	 * @param name
	 */
	/*
	public Course(Code code, String name, boolean matchSession) {
		super(name);
		this.code = (Code) code.clone();//Must be a copy to prevent unexpected modification
		setMatchSession(matchSession);
	}
	*/
	/**
	 * Construct a new course with the html element parsed from html parser.
	 * @param e 
	 */
	public Course(Element e){
		super(e.select("h2").text());//name
		String c=e.select(".courseanchor a").first().attr("name");//code
		try{
		code = new Code(c.substring(0, 4), Integer.parseInt(c.substring(4, 8)), c.charAt(8));
		}catch(Exception ex){																//use ' ' if modifier(H in COMP2012H) not exist
			code = new Code(c.substring(0, 4), Integer.parseInt(c.substring(4, 8)), ' ');
		}
		if(!e.select(".matching").isEmpty())matchSession = true;

		Elements ccES=e.select(".crseattrword");
		for(Element ccE : ccES){
			CC.add(ccE.val());
			//System.out.println(ccE.text());
		}
		Elements popupTrES=e.select(".popupdetail tr th");
		Elements popupTdES=e.select(".popupdetail tr td");	
		for (int i=0;i<popupTrES.size();i++){
			//System.out.println(popupTrES.get(i).text());
			if(popupTrES.get(i).text().equals("ATTRIBUTES"))Attributes=popupTdES.get(i).text();
			if(popupTrES.get(i).text().equals("PRE-REQUISITE"))PreRequisite=popupTdES.get(i).text();
			if(popupTrES.get(i).text().equals("CO-REQUISITE"))CoRequisite=popupTdES.get(i).text();
			if(popupTrES.get(i).text().equals("EXCLUSION"))Exclusion=popupTdES.get(i).text();
			if(popupTrES.get(i).text().equals("PREVIOUS CODE"))PreviousCode=popupTdES.get(i).text();
			if(popupTrES.get(i).text().equals("DESCRIPTION"))description=popupTdES.get(i).text();
			if(popupTrES.get(i).text().equals("CO-LIST WITH"))CoList=popupTdES.get(i).text();
			
		}
		
		//System.out.println(PreRequisite);
		Elements sections=e.select(".sections tr[class]");
		for (Element section : sections){
			//System.out.println(section.className());
			if(section.className().contains("new")){
				Session temp=new Session(section);			//Parse sessions
				sessions.add(temp);
			}
			else{
				sessions.get(sessions.size()-1).extend(section);	//when the line is not a new session, then it contains extra info for the previous session
			}

			
		}
	}
	/**
	 * Gets the description of the course.
	 * @return The string representation of the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * Gets the attributes of the course.
	 * @return The string representation of the attributes
	 */
	public String getAttributes() {
		return Attributes;
	}
	/**
	 * Gets the pre-requisite of the course.
	 * @return The string representation of the pre-requisite
	 */
	public String getPreRequisite() {
		return PreRequisite;
	}
	/**
	 * Gets the co-requisite of the course.
	 * @return The string representation of the co-requisite
	 */
	public String getCoRequisite() {
		return CoRequisite;
	}
	/**
	 * Gets the exclusion of the course.
	 * @return The string representation of the exclusion
	 */
	public String getExclusion() {
		return Exclusion;
	}
	/**
	 * Gets the previous code of the course.
	 * @return The string representation of the previous code
	 */
	public String getPreviousCode() {
		return PreviousCode;
	}
	/**
	 * Gets the "co-list with" of the course.
	 * @return The string representation of the "co-list with"
	 */
	public String getCoList() {
		return CoList;
	}


	/**
	 * Gets the <tt>Code</tt> object that represent the code of the course.
	 * For example, COMP3111H a course code.
	 * @return The <tt>Code</tt> object
	 */
	public Code getCode() {
		return code;
	}

	/**
	 * Gets whether the course require matching session.
	 * @return <tt>True</tt> means matching is required, otherwise <tt>false</tt>
	 */
	public boolean isMatchSession() {
		return matchSession;
	}

	/**
	 * Gets the sessions of the course. Each session is identified by, 
	 * for example, LA2 or T3, a session type and a session number.
	 * @return sessions An array of sessions
	 */
	public ArrayList<Session> getSessions() {
		return sessions;
	}
	/**
	 * Gets the maximum value of wait list for all the sessions of the course
	 * @return maximum value of wait list of that course
	 */
	public int getMaxWaitList(){						//get the waitlist of all sessions, find the max one
		ArrayList<Integer> i=new ArrayList<Integer>();
		ArrayList<Session> ss=this.getSessions();
		for(Session s:ss)i.add(s.wait);
		return Collections.max(i);
	}
	/**
	 * Sets the sessions of the course. Each session is identified by, 
	 * for example, LA2 or T3, a session type and a session number.
	 * @param sessions An array of sessions
	 */
	/*
	public void setSessions(ArrayList<Session> sessions) {
		this.sessions = sessions;
	}
*/
	/**
	 * Sets whether the course require match session numbers of different sessions.
	 * @param matchSession <tt>True</tt> means matching is required, otherwise <tt>false</tt>
	 */
	public void setMatchSession(boolean matchSession) {
		this.matchSession = matchSession;
	}
	
	/**
	 * Sets the description of the course. 
	 * @param description A <tt>String</tt> of the description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	public Session getSessionByString(String session) {
		for(Session s : getSessions()) {
			if (session.equals(s.toString())) {
				return s;
			}
		}
		return null;
	}

	public Session getSessionByClassNumber(int classNo) {
		for(Session s : getSessions()) {
			if (classNo == (s.getClassNo())) {
				return s;
			}
		}
		return null;
	}
	
	/**
	 * Gets the string representation of the Code object that represent the code of the course.
	 * For example, COMP3111H a course code.
	 * @return The String representation of the Code object
	 */
	public String toString() {
		return code.toString();
	}
	
	public class Code {
		private final String dept;
		private final int number;
		private final char modifier;
		
		public Code(String dept, int number, char modifier) {
			this.dept = new String(dept);//Must be a copy to prevent accidental modification
			this.number = number;//TODO: check valid arguments
			this.modifier = modifier;	//' ' when empty
		}
		
		public String getDept(){
			return dept.toUpperCase();
		}
		
		public int getNumber() {
			return number;
		}
		
		public char getMod() {
			return Character.toUpperCase(modifier);
		}

		@Override
		public String toString() {
			return getDept().toUpperCase() + Integer.toString(getNumber()) + getMod();
		}
	
		public Object clone() {
			return new Code(dept, number, modifier);
		}

		
	}
	
	/**
	 * <tt>Session</tt> represent a single session of a course. Each 
	 * <tt>Session</tt> is defined to be a lecture, or lab, or 
	 * tutorial, together with a number. For example, ABCD1234 L2 
	 * is a lecture session 2 of course with <tt>Code</tt> ABCD1234. 
	 * Therefore, if L2 has lessons on 2 separate days, they count 
	 * as one session. Each lesson of the <tt>Session</tt> is 
	 * represented as a <tt>TimePeriod</tt> and all lessons were 
	 * put in an array. Each session has an array of <tt>Instructor
	 * </tt>s. Each session is also uniquely identified by a 4-digit 
	 * class number.
	 * 
	 * @author Bob
	 *
	 */
	public class Session {
		//TODO: check valid data at all times
		private SessionType sType = SessionType.Lecture;
		private int sNo = 1;		//L1
		private char Set=' ';		//The 'A' in T2A
		
		private Set<TimePeriod> schedule = new HashSet<TimePeriod>();//in Wk HH:MMAM - HH:MMPM
		private int classNo;//usually 4-digit number for course enrollment
		private Set<Instructor> instructors= new HashSet<Instructor>();//This hashset does not prevent duplicate but others can, investigation pending.
 
		//private int availableQuota = 0;
		private int quota;
		private int enrol;
		private int availableQuota;
		private int wait;
		//TODO: every room has a ID; use ID? No, some rooms has no ID.
		private Set<String> room=new HashSet<String>(); 
		private String remarks="";
		/**
		 * 
		 */
		private void AddDateTime(String datetime){
			if(datetime.length()>30)datetime=datetime.substring(26);
			if(!datetime.equals("TBA") && !(datetime.length()>30)){	//ignore TBA and datetime which have yyyy-mm-dd (which is >30 long)
				String wd=datetime.substring(0,datetime.indexOf(" "));
				String time=datetime.substring(datetime.indexOf(" "));
				String[] wdA=wd.split("(?<=\\G..)");//split every two chars for weekday part 
				for(String sub:wdA){
					TimePeriod t=new TimePeriod(sub,time);
					schedule.add(t);
				}
			}
		}
		/**
		 * Construct a new session with the html element parsed from html parser.
		 * @param Esection 
		 */
		private Session(Element Esection) {
			Elements datas=Esection.select("td");
			String section=datas.get(0).text();
			String subS="";
			
			if(section.substring(0,1).equals("T")){				//set L/T/LA and cut it away
				setSType(SessionType.Tutorial);
				subS=section.substring(1,section.indexOf(" "));
			}
			else if(section.substring(1,2).equals("A")){
				setSType(SessionType.Laboratory);
				subS=section.substring(2,section.indexOf(" "));
			}
			else if(section.substring(0,1).equals("R")){
				setSType(SessionType.Research);
				subS=section.substring(1,section.indexOf(" "));
			}
			else {
				setSType(SessionType.Lecture);
				subS=section.substring(1,section.indexOf(" "));
			}
			 
			String[] subSA=subS.split("(?<=\\d)(?=\\p{L})");//	split "01B" into two	
			setSNo(Integer.parseInt(subSA[0]));			//"01" in LA01B part
			if(subSA.length==2)Set=subSA[1].charAt(0);	//'B' in LA01B part
			//System.out.println(section);
			setClassNo(Integer.parseInt(section.substring(section.indexOf("(")+1, section.indexOf(")"))));
			
			String datetime=datas.get(1).text();
			//System.out.println(datetime);
			AddDateTime(datetime);			
			room.add(datas.get(2).text());
			Elements tempEs=datas.get(3).select("a");
			for (Element tempE: tempEs){				//There could be more than one Instructors on one line
				Instructor tempI=new Instructor(tempE.text());
				instructors.add(tempI);
			}
			try{quota=Integer.parseInt(datas.get(4).text());}	//Sometimes the quota is underlined and have an obj in it
			catch(NumberFormatException e){
				quota=Integer.parseInt(datas.get(4).select("span").text());
				
			}
			enrol=Integer.parseInt(datas.get(5).text());
			availableQuota=Integer.parseInt(datas.get(6).text());
			wait=Integer.parseInt(datas.get(7).text());
			remarks=datas.get(8).select(".popupdetail").text();
			
			// TODO Auto-generated constructor stub
		}

		/**
		 * Add additional information to the session with the html element parsed from html parser.
		 * @param Esection 
		 */
		private void extend(Element Esection) {			//ref line 99
			// TODO Auto-generated method stub
			Elements datas=Esection.select("td");

			AddDateTime(datas.get(0).text());
			room.add(datas.get(1).text());
			Instructor tempI=new Instructor(datas.get(2).select("a").text());
			instructors.add(tempI);

		}
		
		/**
		 * Construct a new Session with the specified session type: 
		 * Lecture, Laboratory or Tutorial, a session number, an 
		 * array of time periods for the lessons, and the class number 
		 * that uniquely identifies the session.
		 * 
		 * @param sessionType		Type of session: Lecture, Laboratory or Tutorial
		 * @param sessionNumber		e.g LA2, 2 is the session number of this lab session
		 * @param classNo			class number that uniquely identifies the session (4 digit integer)
		 */
		public Session(SessionType sessionType, int sessionNumber, int classNo) {
			setSType(sessionType);
			setSNo(sessionNumber);
			setClassNo(classNo);
		}
		
		
		/**
		 * Gets the "Set" of the session.
		 * e.g LA2B, B is the set of the session.
		 * @return The character representing Set.
		 */
		public char getSet() {
			return Set;
		}
		
		/**
		 * Gets the number of students enrolled to that session.
		 * @return The number of students enrolled to that session.
		 */
		public int getEnrol() {
			return enrol;
		}
		
		/**
		 * Gets the available quota of the session.
		 * @return The available quota.
		 */
		public int getavailableQuota() {
			return availableQuota;
		}
		
		/**
		 * Gets the TOTAL quota of the session.
		 * @return The total quota.
		 */
		public int getQuota() {
			return quota;
		}
		
		/**
		 * Gets the number of student wait-listed to that session.
		 * @return The number of student wait-listed.
		 */
		public int getwait() {
			return wait;
		}
		
		/**
		 * Gets the Remarks of the session.
		 * @return The String representation of the remarks.
		 */
		public String getRemarks() {
			return remarks;
		}
		/** 
		 * Sets the total quota of the session.
		 * 
		 * @param quota The total quota
		 */
		public void setQuota(int quota) {
			this.quota = quota;
		}
		

		/**
		 * Gets the session type of this session.
		 * 
		 * @return A <tt>SessionType</tt> enum. Lecture, Laboratory, Tutorial or Research
		 */
		public SessionType getSType() {
			return sType;
		}
		
		/**
		 * Sets the session type of this session.
		 * 
		 * @param sType A <tt>SessionType</tt> enum. Lecture, Laboratory, Tutorial or Research
		 */
		public void setSType(SessionType sType) {
			this.sType = sType;
		}
		
		/**
		 * Gets the session number,e.g. for LA2, the session number is 2.
		 * 
		 * @return The session number of this session
		 */
		public int getSNo() {
			return sNo;
		}
		
		/**
		 * Sets the session number,e.g. for LA2, the session number is 2.
		 * 
		 * @param sessionNumber The session number of this session
		 */
		public void setSNo(int sessionNumber) {
			//TODO: check valid arguments
			this.sNo = sessionNumber;
		}
		
		/**
		 * Gets the 4-digit number that identifies this session.
		 * 
		 * @return a 4-digit number
		 */
		public int getClassNo() {
			return classNo;
		}
		
		/**
		 * Sets the 4-digit number that identifies this session.
		 * 
		 * @return a 4-digit number
		 */
		public void setClassNo(int classNo) {
			this.classNo = classNo;
		}
		
		/**
		 * Gets the schedule of this session.
		 * 
		 * @return An array of <tt>TimePeriod</tt>. Each of them represent a continuous period of a lesson of this session.
		 */
		
		public Set<TimePeriod> getSchedule() {
			return schedule;
		}

/*
		/**
		 * Sets the schedule of this session.
		 * 
		 * @param schedule An array of <tt>TimePeriod</tt>. Each of them represent a continuous period of a lesson of this session.
		 */
/*
		public void setSchedule(Set<TimePeriod> schedule) {
			this.schedule = new HashSet<TimePeriod>(schedule);//Must be a copy to prevent accidental modification
		}
*/
		/**
		 * Gets the room of this session.
		 * 
		 * @return An array of <tt>String</tt> representing the location of session being held.
		 */
		public Set<String> getRoom() {
			return room;
		}
		
		/**
		 * Gets all instructors teaching the session.
		 * 
		 * @return An array of <tt>Instructor</tt> that are teaching the course.
		 */
		public Set<Instructor> getInstructors() {
			return instructors;
		}
/*
		/**
		 * Sets all instructors teaching the session.
		 * 
		 * @param instructors An array of <tt>Instructor</tt> that are teaching the course.
		 */
/*
		public void setInstructors(Set<Instructor> instructors) {
			this.instructors = new HashSet<Instructor>(instructors);//Must be a copy to prevent accidental modification
		}
*/		
		@Override
		public String toString() {
			//TODO: session type is invalid case
			String type = getSType() == SessionType.Laboratory ? "LA" : getSType().toString().substring(0, 1).toUpperCase();
			String set = "";
			if (getSet()!=' '){
				set = Character.toString(getSet());
			}
			return type + getSNo() + set;
		}

	}
}
