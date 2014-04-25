package test.gui;

import ofcoursegui.LoginGUI;
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

public class MainWindowTest extends UISpecTestCase {

	static {
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
		setAdapter(new MainClassAdapter(MainWindow.class, new String[0]));
	}
		
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testClickLogin() {
		
		Window win = getMainWindow();
	
		// click Login
		WindowInterceptor.init(win.getMenuBar().getMenu("Account").getSubMenu("Login").triggerClick())
			.process(new WindowHandler() {
					public Trigger process(Window dialog) {
					assertTrue(dialog.titleEquals("Login"));
					dialog.getInputTextBox("username").setText("ctestdab");
					dialog.getPasswordField("password").setPassword("ctestdab");
					return dialog.getButton("Login").triggerClick();
					}
			})
			.run();
	}
	
	@Test
	public void testClickRegister() {
		
		Window win = getMainWindow();
	
		// click Register
		WindowInterceptor.init(win.getMenuBar().getMenu("Account").getSubMenu("Register").triggerClick())
			.process(new WindowHandler() {
					public Trigger process(Window dialog) {
					assertTrue(dialog.titleEquals("Register"));
					dialog.getInputTextBox("username").setText("ctestdab");
					return dialog.getButton("Register").triggerClick();
					}
			})
			.run();
	}
	
}
