package ofcourse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * This class extends the functionality of CourseParse by using multiple threads when fullparse() is called 
 * and using CourseCache as the cache manager, deciding when an update is needed.
 * The threads are managed locally by a thread pool that reuses a fixed number of threads. The number 
 * can be set manually, but it is recommended to be below 30 for normal use and below 20 for slow computers.
 * @author Bob Lee
 *
 */
public class CourseParseThreaded extends CourseParse{
	public final static CourseCache cacheManager = new CourseCache();
	/**
	 * Number of threads in concurrent to check for update. 
	 * If an update is needed to download and too many threads are running
	 * at the same time, download can fail and only a portion of the subjects 
	 * can be retrieved, and cached version will be used if possible.
	 */
	public static int nThreads = 12;
	
	/**
	 * Same as parse() method in CourseParse, except that CourseCache is used as the cache manager.
	 * @param subject The subject code of courses that need to be parsed. 
	 * @return A <tt>courseParse</tt> that contains all the courses of the specified subject code.
	 */
	public static CourseParse parse(String subject) {
		CourseParse cp = new CourseParse();
		Document doc = cacheManager.getDocument(subject);
		if (doc == null) return null; //Course info cannot be obtained for various reasons
		cp.setSubject(subject);
		
		Elements cs = doc.select("#classes .course");
		for (Element courseE : cs) {
			Course temp = new Course(courseE); // Parse course by course
			cp.add(temp);
		}
		
		//After that, parse the avg ratings into the course
		Network a = Network.getOurNetwork();
		String[][] RatingSummary=a.getSummary(subject); //{{COMP0001,3.000},{COMP0002,4.233},...}
		a.printArray(RatingSummary);
		for(int i = 0; i < RatingSummary.length; i++)
		try{
				cp.findByCode(RatingSummary[i][0]).setAvgRating(Float.parseFloat(RatingSummary[i][1]));
		} catch (NullPointerException e){
			//if found an invalid course(comp6666), do nothing
			System.out.println("I cannot found course " + RatingSummary[i][0]);
		}
		return cp;
	}
	
	/**
	 * Parse all courses of all subjects in quota page automatically, using multiple threads.
	 * @return An ArrayList<> of courseParse objects, each represents a list of courses with the same subject.
	 * @throws IOException 
	 */
	public static ArrayList<CourseParse> fullparse() {
		final List<CourseParse> cpA = Collections.synchronizedList(new ArrayList<CourseParse>());
		Document doc;
		String Subject = "COMP";
		ArrayList<String> SubjectList = new ArrayList<String>();
		
		try {	
			doc = Jsoup.connect(CourseCache.mainURL + Subject).get();
			//writer.println(doc.outerHtml());
			//doc = Jsoup.parse(output, "UTF-8", "http://example.com/URL");
			Elements courses = doc.select("#navigator .depts a");
			for (Element courseE : courses) {
				SubjectList.add(courseE.text());			//It first find a list of majors from /COMP,
			}
			ArrayList<Future> futures = new ArrayList<Future>();
			ExecutorService es = Executors.newFixedThreadPool(nThreads);
			for (final String subject : SubjectList) {
				futures.add(es.submit(new Runnable() {
					@Override
					public void run() {
						CourseParse cp=parse(subject);		// then it parse major by major, each put in a seperate obj.
						//Ignore any unsuccessful fetch of course info, that subject cannot be used
						if (cp == null) return; 
						cpA.add(cp);
					}
				}));
			}
			for (Future f : futures) {
				f.get();
			}
			

		} catch (IOException e) {
			System.out.println("Connection error");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//Sort the results by subject name
		ArrayList<CourseParse> result = new ArrayList<CourseParse>(cpA);
		Collections.sort(result, new Comparator<CourseParse>() {
			@Override
			public int compare(CourseParse o1, CourseParse o2) {
				return o1.getSubject().compareTo(o2.getSubject());
			}
	        
	    });
		return result;
	}
}
