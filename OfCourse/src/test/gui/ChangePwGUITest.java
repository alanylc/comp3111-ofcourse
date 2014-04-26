package test.gui;

import ofcourse.Network;
import ofcoursegui.MainWindow;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.uispec4j.Trigger;
import org.uispec4j.UISpec4J;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;
import org.uispec4j.interception.MainClassAdapter;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;

public class ChangePwGUITest extends UISpecTestCase {

	private Window win = null;
	
	{
		UISpec4J.init();
	}
	  
	@Before
	public void setUp() throws Exception {
		this.setAdapter(new MainClassAdapter(MainWindow.class, new String[0]));
		win = this.getMainWindow();
		logout();
	}
		
	@After
	public void tearDown() throws Exception {
		logout();
	}
	
	private void logout() {
		WindowInterceptor.run(win.getMenuBar().getMenu("Account").getSubMenu("Logout").triggerClick());
	}
	

	@Test
	public void testClickChangePassword01() {
		Network.login("ctestdae", "eee");
		assertTrue(MainWindow.haveLogined());
		WindowInterceptor.init(win.getMenuBar().getMenu("Account").getSubMenu("Change Password").triggerClick())
		.process(new WindowHandler() {
				public Trigger process(Window dialog) {
					assertTrue(dialog.titleEquals("Change Password"));
					dialog.getPasswordField("opw").setPassword("eee");
					dialog.getPasswordField("npw").setPassword("eee1");
					dialog.getPasswordField("cpw").setPassword("eee1");
					return dialog.getButton("Submit").triggerClick();
				}
		})
		.run();
		assertTrue(MainWindow.haveLogined());
		Network.login("ctestdae", "eee1");
		assertTrue(MainWindow.haveLogined());
		Network.getOurNetwork().newPW("eee1", "eee");
		Network.login("ctestdae", "eee");
		assertTrue(MainWindow.haveLogined());
	}
	
	@Test
	public void testClickChangePassword02() {
		Network.login("ctestdae", "eee");
		assertTrue(MainWindow.haveLogined());
		WindowInterceptor.init(win.getMenuBar().getMenu("Account").getSubMenu("Change Password").triggerClick())
		.process(new WindowHandler() {
				public Trigger process(Window dialog) {
					assertTrue(dialog.titleEquals("Change Password"));
					dialog.getPasswordField("opw").setPassword("eee");
					dialog.getPasswordField("npw").setPassword("eeex");
					dialog.getPasswordField("cpw").setPassword("eeexxxx");
					return dialog.getButton("Submit").triggerClick();
				}
		})
		.run();
		Network.login("ctestdae", "eee");
		assertTrue(MainWindow.haveLogined());
	}
	
	@Test
	public void testClickChangePassword03() {
		Network.login("ctestdae", "eee");
		assertTrue(MainWindow.haveLogined());
		WindowInterceptor.init(win.getMenuBar().getMenu("Account").getSubMenu("Change Password").triggerClick())
		.process(new WindowHandler() {
				public Trigger process(Window dialog) {
					assertTrue(dialog.titleEquals("Change Password"));
					dialog.getPasswordField("opw").setPassword("eeexxy");
					dialog.getPasswordField("npw").setPassword("eee3");
					dialog.getPasswordField("cpw").setPassword("eee3");
					return dialog.getButton("Submit").triggerClick();
				}
		})
		.run();
		Network.login("ctestdae", "eee");
		assertTrue(MainWindow.haveLogined());
	}
	
}