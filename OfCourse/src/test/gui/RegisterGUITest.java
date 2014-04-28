package test.gui;

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

public class RegisterGUITest extends UISpecTestCase {

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
		super.tearDown();
	}
	
	private void logout() {
		WindowInterceptor.run(win.getMenuBar().getMenu("Account").getSubMenu("Logout").triggerClick());
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
	}
	
	
	@Test
	public void testClickRegister03() {
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
		Window err = WindowInterceptor.run(win.getMenuBar().getMenu("Account").getSubMenu("Register").triggerClick());
		assertTrue(err.getTextBox("OptionPane.label").getText().contains("logout first"));
		assertTrue(MainWindow.haveLogined());
	}

}