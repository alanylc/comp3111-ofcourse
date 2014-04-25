package test.gui;

import ofcoursegui.MainWindow;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.uispec4j.Trigger;
import org.uispec4j.UISpec4J;
import org.uispec4j.UISpecAdapter;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;
import org.uispec4j.interception.MainClassAdapter;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;
import org.uispec4j.utils.MainClassTrigger;

public class LoginGUITest extends UISpecTestCase {
	
	private static String username = null, password = null;

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
		setAdapter(new UISpecAdapter() {
		    public Window getMainWindow() {
		    	final Window[] result = new Window[1];
		        WindowInterceptor.init(new MainClassTrigger(MainWindow.class, new String[0]))
		        	.process(new WindowHandler() {
		        		public Trigger process(Window mainWin) {
		        			Window dialog = WindowInterceptor.run(mainWin.getMenuBar().getMenu("Account").getSubMenu("Login").triggerClick());
		        			WindowInterceptor.init(dialog.getButton("Login").triggerClick())
								.process(new WindowHandler() {
								public Trigger process(Window prompt) {
									result[0] = prompt;
									return prompt.getButton().triggerClick();
								}
								}).run();
							return Trigger.DO_NOTHING;
		        		}
		        	});
		        return result[0];
		    }
		});
	}
		
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testLoginFail() {
		
//		Window dialog = getMainWindow();
//		assertTrue(dialog.titleEquals("Login"));
//		dialog.getInputTextBox("username").setText("ctestdab");
//		dialog.getPasswordField("password").setPassword("ctestdab");
		
		username = "name";
		password = "pw";
		Window prompt = getMainWindow();
		assertTrue(prompt.titleContains("Congr"));
	}
	
}

