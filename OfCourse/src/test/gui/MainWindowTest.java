package test.gui;

import ofcourse.Network;
import ofcoursegui.MainWindow;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.uispec4j.UISpec4J;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;
import org.uispec4j.interception.MainClassAdapter;
import org.uispec4j.interception.WindowInterceptor;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MainWindowTest extends UISpecTestCase {
	
	private Window win = null;
	
	{
		UISpec4J.init();
		UISpec4J.setWindowInterceptionTimeLimit(100000);
	}
	
	@Before
	public void setUp() throws Exception {
		this.setAdapter(new MainClassAdapter(MainWindow.class, new String[0]));
		win = this.getMainWindow();
		
	}
		
	@After
	public void tearDown() throws Exception {
		Network.logout();
		//super.tearDown();
	}
	
	@Test
	public void testUI() { // test clicking buttons, search course, and timetable operations
		
		Network.login("ctestdab", "bbb");
		
//		win.getButton("Delete Last").click();
//		win.getButton("See Friend").click();
//		win.getButton("Search").click();
//		win.getButton("Swap...").click();
//		win.getButton("Drop...").click();
		
		WindowInterceptor.run(win.getMenuBar().getMenu("File").getSubMenu("Download My Time Table").triggerClick()).dispose();
		WindowInterceptor.run(win.getMenuBar().getMenu("File").getSubMenu("Upload My Time Table").triggerClick()).dispose();
		WindowInterceptor.run(win.getMenuBar().getMenu("File").getSubMenu("Export Time Table...").triggerClick()).dispose();
		WindowInterceptor.run(win.getMenuBar().getMenu("File").getSubMenu("Import Time Table...").triggerClick()).dispose();
		WindowInterceptor.run(win.getMenuBar().getMenu("File").getSubMenu("Export Time Table as image...").triggerClick()).dispose();
		WindowInterceptor.run(win.getMenuBar().getMenu("Friend").getSubMenu("Update Friends' Time Table").triggerClick()).dispose();
		WindowInterceptor.run(win.getMenuBar().getMenu("Friend").getSubMenu("Check Friend Requests").triggerClick()).dispose();
		WindowInterceptor.run(win.getMenuBar().getMenu("Friend").getSubMenu("Add New Friend").triggerClick()).dispose();
		WindowInterceptor.run(win.getMenuBar().getMenu("Account").getSubMenu("Login").triggerClick()).dispose();
		WindowInterceptor.run(win.getMenuBar().getMenu("Account").getSubMenu("Register").triggerClick()).dispose();
		WindowInterceptor.run(win.getMenuBar().getMenu("Account").getSubMenu("Change Password").triggerClick()).dispose();
		WindowInterceptor.run(win.getMenuBar().getMenu("Account").getSubMenu("Logout").triggerClick()).dispose();
		

		Network.logout();
		
		WindowInterceptor.run(win.getMenuBar().getMenu("File").getSubMenu("Download My Time Table").triggerClick()).dispose();
		WindowInterceptor.run(win.getMenuBar().getMenu("File").getSubMenu("Upload My Time Table").triggerClick()).dispose();
		WindowInterceptor.run(win.getMenuBar().getMenu("File").getSubMenu("Export Time Table...").triggerClick()).dispose();
		WindowInterceptor.run(win.getMenuBar().getMenu("File").getSubMenu("Import Time Table...").triggerClick()).dispose();
		WindowInterceptor.run(win.getMenuBar().getMenu("File").getSubMenu("Export Time Table as image...").triggerClick()).dispose();
		WindowInterceptor.run(win.getMenuBar().getMenu("Friend").getSubMenu("Update Friends' Time Table").triggerClick()).dispose();
		WindowInterceptor.run(win.getMenuBar().getMenu("Friend").getSubMenu("Check Friend Requests").triggerClick()).dispose();
		WindowInterceptor.run(win.getMenuBar().getMenu("Friend").getSubMenu("Add New Friend").triggerClick()).dispose();
		WindowInterceptor.run(win.getMenuBar().getMenu("Account").getSubMenu("Login").triggerClick()).dispose();
		WindowInterceptor.run(win.getMenuBar().getMenu("Account").getSubMenu("Register").triggerClick()).dispose();
		WindowInterceptor.run(win.getMenuBar().getMenu("Account").getSubMenu("Change Password").triggerClick()).dispose();
		WindowInterceptor.run(win.getMenuBar().getMenu("Account").getSubMenu("Logout").triggerClick()).dispose();
		
	}
}
