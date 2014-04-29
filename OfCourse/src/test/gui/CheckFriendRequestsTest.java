package test.gui;

import ofcourse.Network;
import ofcoursegui.MainWindow;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.uispec4j.UISpec4J;
import org.uispec4j.UISpecAdapter;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;
import org.uispec4j.interception.MainClassAdapter;
import org.uispec4j.interception.WindowInterceptor;

public class CheckFriendRequestsTest extends UISpecTestCase {
	  
	@Before
	public void setUp() throws Exception {
		UISpec4J.init();
		long s = 180;
		UISpec4J.setWindowInterceptionTimeLimit(s*1000);
		
		this.setAdapter(new MainClassAdapter(MainWindow.class, new String[0]));
		final Window win = this.getMainWindow();
		Network.login("ctestdae", "eee");
		this.setAdapter(new UISpecAdapter() {
			@Override
			public Window getMainWindow() {
				return WindowInterceptor.run(win.getMenuBar().getMenu("Friend").getSubMenu("Check Friend Requests").triggerClick());
			}
		});
	}
		
	@After
	public void tearDown() throws Exception {
		Network.logout();
		super.tearDown();
	}

	@Test
	public void testCheckFriendRequests() {
		Window fdreq = getMainWindow();
		assertTrue(fdreq.titleContains("Requests"));
		fdreq.getListBox().clearSelection();
		int oldsize = fdreq.getListBox().getSize();
		fdreq.getButton("Accept").click();
		int newsize = fdreq.getListBox().getSize(); 
		fdreq.getButton("Ignore").click();
		assertEquals(oldsize, newsize);
		Network.logout();
		fdreq = getMainWindow();
		assertTrue(fdreq.titleEquals("Error"));
	}
	
}