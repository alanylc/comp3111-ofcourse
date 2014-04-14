package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CodeTest.class, CourseTest.class, NetworkTest.class,
		SessionTest.class, TimetableTest.class, TimeSlotTest.class })
public class AllTests {

}