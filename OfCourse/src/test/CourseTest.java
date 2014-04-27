package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;

import ofcourse.Course;
import ofcourse.Course.Session;
import ofcourse.CourseParseThreaded;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CourseTest {
	
	private Course c;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		// this test will change content of course, so need to re-initialize course list every test
		Course.AllCourses = new ArrayList<Course>();
		CourseParseThreaded.parse("COMP"); 
		// the static variable Course.AllCourses should now have the COMP course list
	}

	@After
	public void tearDown() throws Exception {
	}

	
	@Test
	public void testGetCourseByName() {
		int expected=3111;
		assertEquals(expected, Course.getCourseByName("COMP3111 ").getCode().getNumber());
	}
	@Test
	public void testGetCourseByName2() {//Not found
		assertNull(Course.getCourseByName("COMP6666 "));
	}
/*
	@Test
	public void testCourseStringIntCharStringBoolean() {
		fail("Not yet implemented");
	}

	@Test
	public void testCourseCodeStringBoolean() {
		fail("Not yet implemented");
	}
*/
	@Test
	public void testCourseElement() {
		String html="<div class=\"course\">  <div class=\"courseanchor\" style=\"position:relative; float:left; visibility:hidden;\">   <a name=\"COMP3111\">&nbsp;</a>  </div>  <div class=\"courseinfo\">   <div class=\"courseattr popup\">    <span style=\"font-size: 12px; color: #688; font-weight: bold;\">COURSE INFO</span>    <div class=\"popupdetail\">     <table width=\"400\">      <tbody>       <tr>        <th>PRE-REQUISITE</th>        <td>COMP 151/151H (prior to 2009-10) and COMP 171/171H (prior to 2009-10); or COMP 2012/2012H</td>       </tr>       <tr>        <th>EXCLUSION</th>        <td>COMP 3111H, ISOM 3210, RMBI 4420</td>       </tr>       <tr>        <th>PREVIOUS CODE</th>        <td>COMP 211</td>       </tr>       <tr>        <th>DESCRIPTION</th>        <td>Methods and tools for planning, designing, implementing, validating, and maintaining large software systems. Project work to build a software system as a team, using appropriate software engineering tools and techniques.</td>       </tr>      </tbody>     </table>    </div>   </div>  </div>  <h2>COMP 3111 - Software Engineering (4 units)</h2>  <table class=\"sections\" width=\"1012\">   <tbody>    <tr>     <th width=\"85\">Section</th>     <th width=\"190\" style=\"text-align: left\">Date &amp; Time</th>     <th width=\"160\" style=\"text-align: left\">Room</th>     <th width=\"190\" style=\"text-align: left\">Instructor</th>     <th width=\"45\">Quota</th>     <th width=\"45\">Enrol</th>     <th width=\"45\">Avail</th>     <th width=\"45\">Wait</th>     <th width=\"81\">Remarks</th>    </tr>    <tr class=\"newsect secteven\">     <td align=\"center\">L1 (1897)</td>     <td>TuTh 09:00AM - 10:20AM</td>     <td>Lecture Theater G (135)</td>     <td><a href=\"/wcq/cgi-bin/1330/instructor/KIM, Sung Hun\">KIM, Sung Hun</a></td>     <td align=\"center\">120</td>     <td align=\"center\">56</td>     <td align=\"center\">64</td>     <td align=\"center\">0</td>     <td align=\"center\">&nbsp;</td>    </tr>    <tr class=\"newsect sectodd\">     <td align=\"center\">L2 (1899)</td>     <td>TuTh 03:00PM - 04:20PM</td>     <td>Lecture Theater E (143)</td>     <td><a href=\"/wcq/cgi-bin/1330/instructor/KIM, Sung Hun\">KIM, Sung Hun</a></td>     <td align=\"center\">100</td>     <td align=\"center\">85</td>     <td align=\"center\">15</td>     <td align=\"center\">0</td>     <td align=\"center\">      <div class=\"popup classnotes\">       <div class=\"popupdetail\">        &gt; Co-list with COMP 3111H L1       </div>      </div>&nbsp;</td>    </tr>    <tr class=\"newsect secteven\">     <td align=\"center\">T1 (1905)</td>     <td>Th 06:00PM - 06:50PM</td>     <td>Rm 2303, Lift 17-18 (91)</td>     <td><a href=\"/wcq/cgi-bin/1330/instructor/KIM, Sung Hun\">KIM, Sung Hun</a></td>     <td align=\"center\">60</td>     <td align=\"center\">29</td>     <td align=\"center\">31</td>     <td align=\"center\">0</td>     <td align=\"center\">&nbsp;</td>    </tr>    <tr class=\"newsect sectodd\">     <td align=\"center\">T2 (1907)</td>     <td>Mo 09:30AM - 10:20AM</td>     <td>Rm 2303, Lift 17-18 (91)</td>     <td><a href=\"/wcq/cgi-bin/1330/instructor/KIM, Sung Hun\">KIM, Sung Hun</a></td>     <td align=\"center\">60</td>     <td align=\"center\">55</td>     <td align=\"center\">5</td>     <td align=\"center\">0</td>     <td align=\"center\">&nbsp;</td>    </tr>    <tr class=\"newsect secteven\">     <td align=\"center\">T3 (1908)</td>     <td>Th 04:30PM - 05:20PM</td>     <td>Rm 4620, Lift 31-32 (126)</td>     <td><a href=\"/wcq/cgi-bin/1330/instructor/KIM, Sung Hun\">KIM, Sung Hun</a></td>     <td align=\"center\">60</td>     <td align=\"center\">57</td>     <td align=\"center\">3</td>     <td align=\"center\">0</td>     <td align=\"center\">&nbsp;</td>    </tr>    <tr class=\"newsect sectodd\">     <td align=\"center\">LA1 (1901)</td>     <td>Tu 04:30PM - 05:20PM</td>     <td>Rm 4210, Lift 19 (67)</td>     <td><a href=\"/wcq/cgi-bin/1330/instructor/KIM, Sung Hun\">KIM, Sung Hun</a></td>     <td align=\"center\">60</td>     <td align=\"center\">38</td>     <td align=\"center\">22</td>     <td align=\"center\">0</td>     <td align=\"center\">&nbsp;</td>    </tr>    <tr class=\"newsect secteven\">     <td align=\"center\">LA2 (1903)</td>     <td>Mo 03:00PM - 03:50PM</td>     <td>Rm 4210, Lift 19 (67)</td>     <td><a href=\"/wcq/cgi-bin/1330/instructor/KIM, Sung Hun\">KIM, Sung Hun</a></td>     <td align=\"center\">60</td>     <td align=\"center\">47</td>     <td align=\"center\">13</td>     <td align=\"center\">0</td>     <td align=\"center\">&nbsp;</td>    </tr>    <tr class=\"newsect sectodd\">     <td align=\"center\">LA3 (1904)</td>     <td>Th 10:30AM - 11:20AM</td>     <td>Rm 4210, Lift 19 (67)</td>     <td><a href=\"/wcq/cgi-bin/1330/instructor/KIM, Sung Hun\">KIM, Sung Hun</a></td>     <td align=\"center\">60</td>     <td align=\"center\">56</td>     <td align=\"center\">4</td>     <td align=\"center\">0</td>     <td align=\"center\">&nbsp;</td>    </tr>   </tbody>  </table> </div>";
		Document doc = Jsoup.parse(html);
		Element el=doc.select(".course").first();
		Course cc=new Course(el);
		//fail("Not yet implemented");
		String expectedDept="COMP";
		int expectedNum=3111;
		char expectedMod=' ';
		assertEquals(expectedDept, cc.getCode().getDept());
		assertEquals(expectedNum, cc.getCode().getNumber());
		assertEquals(expectedMod, cc.getCode().getMod());
	}

	@Test
	public void testGetDescription() {
		String expected="Methods and tools for planning, designing, implementing, validating, and maintaining large software systems. Project work to build a software system as a team, using appropriate software engineering tools and techniques.";
		c=Course.getCourseByName("COMP3111 ");
		assertEquals(expected, c.getDescription());
		
	}

	@Test
	public void testGetAttributes() {
		String expected="Common Core (S&T) for 2010 & 2011 3Y programs Common Core (S&T) for 2012 3Y programs Common Core (S&T) for 4Y programs";
		c=Course.getCourseByName("COMP1001 ");
		assertEquals(expected, c.getAttributes());
	}
	
	@Test
	public void testGetAttributes2() {//Not found
		c=Course.getCourseByName("COMP2011 ");
		assertNull(c.getAttributes());
	}

	@Test
	public void testGetPreRequisite() {
		String expected="COMP 1021 OR COMP 1022P OR COMP 1022Q";
		c=Course.getCourseByName("COMP2011 ");
		assertEquals(expected, c.getPreRequisite());
	}
	@Test
	public void testGetPreRequisite2() {//Not found
		c=Course.getCourseByName("COMP1999 ");
		assertNull(c.getPreRequisite());
	}

	@Test
	public void testGetCoRequisite() {
		String expected="(For students without prerequisites) MATH 1013 OR MATH 1014 OR MATH 1018 OR MATH 1020 OR MATH 1023 OR MATH 1024";
		c=Course.getCourseByName("COMP2711 ");
		assertEquals(expected, c.getCoRequisite());
	}
	
	@Test
	public void testGetCoRequisite2() {//Not found
		c=Course.getCourseByName("COMP2012 ");
		assertNull(c.getCoRequisite());
	}

	@Test
	public void testGetExclusion() {
		String expected="COMP 2711H, MATH 2343";
		c=Course.getCourseByName("COMP2711 ");
		assertEquals(expected, c.getExclusion());
	}
	
	@Test
	public void testGetExclusion2() {//Not found
		c=Course.getCourseByName("COMP3071 ");
		assertNull(c.getExclusion());
	}

	@Test
	public void testGetPreviousCode() {
		String expected="COMP 170";
		c=Course.getCourseByName("COMP2711 ");
		assertEquals(expected, c.getPreviousCode());
	}
	
	@Test
	public void testGetPreviousCode2() {//Not found
		c=Course.getCourseByName("COMP2011 ");
		assertNull(c.getPreviousCode());
	}

	@Test
	public void testGetCoList() {
		String expected="RMBI 4310";
		c=Course.getCourseByName("COMP4332 ");
		assertEquals(expected, c.getCoList());
	}
	
	@Test
	public void testGetCoList2() {//Not found
		c=Course.getCourseByName("COMP2012 ");
		assertNull(c.getCoList());
	}

	@Test
	public void testGetCode() {
		String expectedDept="COMP";
		int expectedNum=1022;
		char expectedMod='Q';
		c=Course.getCourseByName("COMP1022Q");
		assertEquals(expectedDept, c.getCode().getDept());
		assertEquals(expectedNum, c.getCode().getNumber());
		assertEquals(expectedMod, c.getCode().getMod());
	}
	

	@Test
	public void testIsMatchSession() {//check if true is returned
		boolean expected=true;
		c=Course.getCourseByName("COMP2012 ");
		assertEquals(expected, c.isMatchSession());
	}

	
	@Test	
	public void testIsMatchSession2() {//check if false is returned
		boolean expected=false;
		c=Course.getCourseByName("COMP2011 ");
		assertEquals(expected, c.isMatchSession());
	}
	@Test
	public void testGetSessions() {
		c=Course.getCourseByName("COMP3111 ");
		ArrayList<Session> sA=c.getSessions();
		int[] classList={1897,1899,1905,1907,1908,1901,1903,1904};
		for(int i=0;i<sA.size();i++){
			assertEquals(classList[i],sA.get(i).getClassNo());
		}
	}

	@Test
	public void testGetMaxWaitList() {
		int expected=0;
		c=Course.getCourseByName("COMP3111 ");
		assertEquals(expected, c.getMaxWaitList());
	}
/*
	@Test
	public void testSetSessions() {
		fail("Not yet implemented");
	}
*/
	@Test
	public void testSetMatchSession() {//test for true->false
		boolean expected=false;
		c=Course.getCourseByName("COMP2012 ");
		c.setMatchSession(expected);
		assertEquals(expected, c.isMatchSession());
	}
	
	@Test
	public void testSetMatchSession2() {//test for false->true
		boolean expected=true;
		c=Course.getCourseByName("COMP2011 ");
		c.setMatchSession(expected);
		assertEquals(expected, c.isMatchSession());
	}

	@Test
	public void testSetDescription() {
		String expected="This course has 100% guarantee of getting A-grade";
		c=Course.getCourseByName("COMP3111 ");
		c.setDescription(expected);
		assertEquals(expected, c.getDescription());
	}

	@Test
	public void testGetSessionByString() {
		c=Course.getCourseByName("COMP3111 ");
		Session s=c.getSessionByString("L1");
		System.out.println(s.getClassNo());
		int expected=1897;
		assertEquals(expected,s.getClassNo());
	}

	@Test
	public void testGetSessionByClassNumber() {
		c=Course.getCourseByName("COMP3111 ");
		int[] classList={1897,1899,1905,1907,1908,1901,1903,1904};
		for(int i=0;i<classList.length;i++){
			Session s=c.getSessionByClassNumber(classList[i]);
			assertEquals(classList[i],s.getClassNo());
		}
	}
	
	@Test
	public void testGetSessionByClassNumber2() {
		c=Course.getCourseByName("COMP3111 ");
		Session s=c.getSessionByClassNumber(6666);
		assertNull(s);
	}
	

	@Test
	public void testToString() {
		String expected="COMP3111 ";
		c=Course.getCourseByName("COMP3111 ");
		assertEquals(expected, c.toString());
	}
	

}
