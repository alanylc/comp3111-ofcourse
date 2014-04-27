package test.gui;

import ofcourse.Network;
import ofcoursegui.MainWindow;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.uispec4j.Button;
import org.uispec4j.UISpec4J;
import org.uispec4j.UISpecAdapter;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;
import org.uispec4j.interception.MainClassAdapter;
import org.uispec4j.interception.WindowInterceptor;

public class ListTimeTableGUITest extends UISpecTestCase {
	
	private Button closebtn;
 
	@Before
	public void setUp() throws Exception {
		UISpec4J.init();
	}
		
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testListTimeTableGUI() {
		setAdapter(new MainClassAdapter(MainWindow.class, new String[0]));
		final Window win = this.getMainWindow();
		setAdapter(new UISpecAdapter() {
			@Override
			public Window getMainWindow() {
				Network.login("ctestdab", "bbb");
				Window msg = WindowInterceptor.run(win.getMenuBar().getMenu("File").getSubMenu("Download My Time Table").triggerClick());
				assertTrue(msg.getTextBox("OptionPane.label").getText().contains("success"));
				Window lv = WindowInterceptor.run(win.getTabGroup("timetableTabpage").getSelectedTab().getButton("List View").triggerClick());
				closebtn = lv.getButton("Close");
				return lv;
			}
		});
		Window listview = getMainWindow();
		assertTrue(listview.titleContains("Mine"));	
		listview.getTable().clearSelection();
		closebtn.click();
	}
	
}