package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.gui.AddNewFriendTest;
import test.gui.ChangePwGUITest;
import test.gui.CheckFriendRequestsTest;
import test.gui.ListTimeTableGUITest;
import test.gui.LoginGUITest;
import test.gui.MainWindowTest;
import test.gui.MyFavPanelGUITest;
import test.gui.RegisterGUITest;
import test.gui.SearchTest;

@RunWith(Suite.class)
@SuiteClasses({ CodeTest.class, CourseTest.class, NetworkTest.class, WeekDayTest.class, InstructorTest.class, 
		SessionTest.class, TimetableTest.class, TimeSlotTest.class, TimePeriodTest.class, 
		CourseParseTest.class, CourseParseThreadedTest.class, MainWindowTest.class,
		LoginGUITest.class, RegisterGUITest.class, ChangePwGUITest.class,
		MyFavPanelGUITest.class, AddNewFriendTest.class, CheckFriendRequestsTest.class, 
		ListTimeTableGUITest.class, SearchTest.class})
public class AllTests {

}
