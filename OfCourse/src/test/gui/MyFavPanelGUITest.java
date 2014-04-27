package test.gui;

import java.awt.Component;

import ofcoursegui.MainWindow;
import ofcoursegui.SearchCodeIntervalGUI;
import ofcoursegui.SearchSubjectGUI;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.uispec4j.ListBox;
import org.uispec4j.Panel;
import org.uispec4j.Table;
import org.uispec4j.Trigger;
import org.uispec4j.UISpec4J;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;
import org.uispec4j.finder.ComponentMatcher;
import org.uispec4j.interception.MainClassAdapter;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;

public class MyFavPanelGUITest extends UISpecTestCase {

	private Window win = null;
	
	{
		UISpec4J.init();
	}
		
	@Before
	public void setUp() throws Exception {
		this.setAdapter(new MainClassAdapter(MainWindow.class, new String[0]));
		win = this.getMainWindow();
		//Network.login("ctestdab", "bbb");
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
	}
		
	@After
	public void tearDown() throws Exception {
		logout();
	}
	
	private void logout() {
		WindowInterceptor.run(win.getMenuBar().getMenu("Account").getSubMenu("Logout").triggerClick());
	}
	
	@Test
	public void testMyFavPanel() {
		
		win.getTabGroup("searchTabpage").selectTab("My Favourite");
		int oldfavsize = win.getTabGroup("searchTabpage").getSelectedTab().getListBox("myfavlist").getSize();
		
		win.getTabGroup("searchTabpage").selectTab("New Search");
		Panel searchtab = win.getTabGroup("searchTabpage").getSelectedTab();
		ListBox subjectlist = searchtab.getListBox(new ComponentMatcher() {
			@Override
			public boolean matches(Component component) {
				if (component.getClass().equals(SearchSubjectGUI.class))
					return true;
				else
					return false;
			}
		});
		ListBox lvlist = searchtab.getListBox(new ComponentMatcher() {
			@Override
			public boolean matches(Component component) {
				if (component.getClass().equals(SearchCodeIntervalGUI.class))
					return true;
				else
					return false;
			}
		});
		subjectlist.selectIndex(0);
		lvlist.selectIndices(0, 3);
		searchtab.getButton("Search").click();
		Table result = win.getTabGroup("searchTabpage").getSelectedTab().getTable();
		result.click(0, 0);
		Panel coursegui = win.getTabGroup("searchTabpage").getSelectedTab();
		// add an ACCT course to my favourite
		coursegui.getButton("Add to My Favourite").click();
		coursegui.getButton("Enroll").click();
		
		
		// the account must have at least 1 favourite course for this test
		win.getTabGroup("searchTabpage").selectTab("My Favourite");
		int newfavsize = win.getTabGroup("searchTabpage").getSelectedTab().getListBox("myfavlist").getSize();
		assertEquals(oldfavsize+1, newfavsize);
		win.getTabGroup("searchTabpage").getSelectedTab().getListBox("myfavlist").selectIndex(0);
		String course_selected = win.getTabGroup("searchTabpage").getSelectedTab().getListBox("myfavlist").getAwtComponent().getSelectedValue().toString();
		win.getTabGroup("searchTabpage").getSelectedTab().getButton("View Course Details").click();
		assertTrue(win.getTabGroup("searchTabpage").getSelectedTab().getDescription().contains(course_selected.substring(0, 11)));
		win.getTabGroup("searchTabpage").selectTab("My Favourite");
		win.getTabGroup("searchTabpage").getSelectedTab().getListBox("myfavlist").selectIndex(0);
		win.getButton("Remove from My Favourite").click();
		int newfavsize2 = win.getTabGroup("searchTabpage").getSelectedTab().getListBox("myfavlist").getSize();
		assertEquals(newfavsize-1, newfavsize2);
	}
	
}