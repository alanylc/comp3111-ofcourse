package test.gui;

import ofcourse.Network;
import ofcoursegui.MainWindow;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.uispec4j.Trigger;
import org.uispec4j.UISpec4J;
import org.uispec4j.UISpecAdapter;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;
import org.uispec4j.interception.MainClassAdapter;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;

public class ListTimeTableGUITest extends UISpecTestCase {

	{
		UISpec4J.init();
	}
	  
	@Before
	public void setUp() throws Exception {
		this.setAdapter(new MainClassAdapter(MainWindow.class, new String[0]));
		final Window win = this.getMainWindow();
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
		this.setAdapter(new UISpecAdapter() {
			@Override
			public Window getMainWindow() {
				return WindowInterceptor.run(win.getTabGroup("timetableTabpage").getSelectedTab().getButton("List View").triggerClick());
			}
		});
	}
		
	@After
	public void tearDown() throws Exception {
		Network.logout();
	}

	@Test
	public void testListTimeTableGUI() {
		Window listview = getMainWindow();
		assertTrue(listview.titleContains("Mine"));
		listview.getTable().clearSelection();
		listview.getButton("Drop").click();
		listview.getTable().selectRow(0);
		listview.getButton("Drop").click();
		listview.getButton("Close").click();
	}
	
}