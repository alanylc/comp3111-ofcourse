package test.gui;

import ofcourse.Network;
import ofcoursegui.MainWindow;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.uispec4j.Trigger;
import org.uispec4j.UISpec4J;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;
import org.uispec4j.interception.MainClassAdapter;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MainWindowTest extends UISpecTestCase {

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
	public void testButtons() {
		win.getButton("Delete Last").click();
		win.getButton("See Friend").click();
		WindowInterceptor.run(win.getMenuBar().getMenu("File").getSubMenu("Download My Time Table").triggerClick());
		WindowInterceptor.run(win.getMenuBar().getMenu("File").getSubMenu("Upload My Time Table").triggerClick());
		WindowInterceptor.run(win.getMenuBar().getMenu("Friend").getSubMenu("Update Friends' Time Table").triggerClick());
		WindowInterceptor.run(win.getMenuBar().getMenu("Friend").getSubMenu("Check Friend Requests").triggerClick());
		WindowInterceptor.run(win.getMenuBar().getMenu("Friend").getSubMenu("Add New Friend").triggerClick());
	}

	@Test
	public void testClickLogin01() {
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
	public void testClickLogin02() {
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
	public void testClickLogin03() {
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
	public void testClickLogin04() {
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
	
	@Test
	public void testClickRegister01() {
		WindowInterceptor.init(win.getMenuBar().getMenu("Account").getSubMenu("Register").triggerClick())
			.process(new WindowHandler("RegisterGUI") {
					public Trigger process(Window dialog) {
						assertTrue(dialog.titleEquals("Register"));
						dialog.getInputTextBox("username").setText("ctestdab");
						return dialog.getButton("Register").triggerClick();
					}
			})
			.run();
		assertFalse(MainWindow.haveLogined());
	}
	
	@Test
	public void testClickRegister02() {
		WindowInterceptor.init(win.getMenuBar().getMenu("Account").getSubMenu("Register").triggerClick())
			.process(new WindowHandler("RegisterGUI") {
					public Trigger process(Window dialog) {
						assertTrue(dialog.titleEquals("Register"));
						dialog.getInputTextBox("username").setText("ctestd/;ab+x*%a#");
						return dialog.getButton("Register").triggerClick();
					}
			})
			.run();
		assertFalse(MainWindow.haveLogined());
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
	
	@Test
	public void testClickImportTimeTable() {
		WindowInterceptor.run(win.getMenuBar().getMenu("File").getSubMenu("Import Time Table...").triggerClick());
	}
	
	@Test
	public void testClickExportTimeTable() {
		WindowInterceptor.run(win.getMenuBar().getMenu("File").getSubMenu("Export Time Table...").triggerClick());
	}
	
	@Test
	public void testClickExportTimeTableAsImage() {
		WindowInterceptor.run(win.getMenuBar().getMenu("File").getSubMenu("Export Time Table as image...").triggerClick());
	}
	
}
