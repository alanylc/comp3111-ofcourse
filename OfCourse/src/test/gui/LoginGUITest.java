package test.gui;

import ofcoursegui.MainWindow;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.uispec4j.Trigger;
import org.uispec4j.UISpec4J;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;
import org.uispec4j.interception.MainClassAdapter;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;

public class LoginGUITest extends UISpecTestCase {

	private Window win = null;
	
	{
		UISpec4J.init();
	}
	  
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}
  
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
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
	public void testLogin01() {
		assertFalse(MainWindow.haveLogined());
		WindowInterceptor.init(win.getMenuBar().getMenu("Account").getSubMenu("Login").triggerClick())
			.process(new WindowHandler("LoginGUI") {
					public Trigger process(Window dialog) {
						assertTrue(dialog.titleEquals("Login"));
						dialog.getInputTextBox("username").setText("ctestdab");
						dialog.getPasswordField("password").setPassword("bbb");
						return dialog.getButton("Login").triggerClick();
					}
			})
			.run();
		assertTrue(MainWindow.haveLogined());
	}
	
	@Test
	public void testLogin02() {
		WindowInterceptor.init(win.getMenuBar().getMenu("Account").getSubMenu("Login").triggerClick())
			.process(new WindowHandler("LoginGUI") {
					public Trigger process(Window dialog) {
						assertTrue(dialog.titleEquals("Login"));
						dialog.getInputTextBox("username").setText("ctestdab");
						dialog.getPasswordField("password").setPassword("bbbx");
						return dialog.getButton("Login").triggerClick();
					}
			})
			.run();
		assertFalse(MainWindow.haveLogined());
	}
	
	@Test
	public void testLogin03() {
		WindowInterceptor.init(win.getMenuBar().getMenu("Account").getSubMenu("Login").triggerClick())
			.process(new WindowHandler("LoginGUI") {
					public Trigger process(Window dialog) {
						assertTrue(dialog.titleEquals("Login"));
						dialog.getInputTextBox("username").setText("");
						dialog.getPasswordField("password").setPassword("bbb");
						return dialog.getButton("Login").triggerClick();
					}
			})
			.run();
		assertFalse(MainWindow.haveLogined());
	}
	
	@Test
	public void testLogin04() {
		WindowInterceptor.init(win.getMenuBar().getMenu("Account").getSubMenu("Login").triggerClick())
			.process(new WindowHandler("LoginGUI") {
					public Trigger process(Window dialog) {
						assertTrue(dialog.titleEquals("Login"));
						dialog.getInputTextBox("username").setText("firstLogin");
						dialog.getPasswordField("password").setPassword("");
						return dialog.getButton("Login").triggerClick();
					}
			})
			.run();
		assertFalse(MainWindow.haveLogined());
	}
	
}