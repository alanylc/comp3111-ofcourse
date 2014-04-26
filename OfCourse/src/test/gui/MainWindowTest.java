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
		win.getButton("Search").click();
		win.getButton("Swap...").click();
		win.getButton("Drop...").click();
		WindowInterceptor.run(win.getButton("List View").triggerClick());
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
