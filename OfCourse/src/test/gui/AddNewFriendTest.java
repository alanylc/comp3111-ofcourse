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

public class AddNewFriendTest extends UISpecTestCase {

	{
		UISpec4J.init();
	}
	  
	@Before
	public void setUp() throws Exception {
		this.setAdapter(new MainClassAdapter(MainWindow.class, new String[0]));
		final Window win = this.getMainWindow();
		Network.login("ctestdae", "eee");
		this.setAdapter(new UISpecAdapter() {
			@Override
			public Window getMainWindow() {
				return WindowInterceptor.run(win.getMenuBar().getMenu("Friend").getSubMenu("Add New Friend").triggerClick());
			}
		});
	}
		
	@After
	public void tearDown() throws Exception {
		Network.logout();
	}

	@Test
	public void testCheckFriendRequests() {
		Window newFd = getMainWindow();
		assertTrue(newFd.titleContains("New Friend"));
		Network.logout();
		newFd = getMainWindow();
		assertTrue(newFd.titleEquals("Error"));
	}
	
}