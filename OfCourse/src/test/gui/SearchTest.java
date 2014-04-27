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
import org.uispec4j.UISpec4J;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;
import org.uispec4j.finder.ComponentMatcher;
import org.uispec4j.interception.MainClassAdapter;
import org.uispec4j.interception.WindowInterceptor;

public class SearchTest extends UISpecTestCase {
	
	private Window win = null;
		
	@Before
	public void setUp() throws Exception {
		UISpec4J.init();
		this.setAdapter(new MainClassAdapter(MainWindow.class, new String[0]));
		win = this.getMainWindow();
		WindowInterceptor.run(win.getMenuBar().getMenu("Account").getSubMenu("Logout").triggerClick());
	}
	
	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testSearch() {

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
		result.click(1, 0);
		Panel coursegui = win.getTabGroup("searchTabpage").getSelectedTab();
		coursegui.getButton("Enroll").click();
		
		win.getTabGroup("searchTabpage").selectTab("New Search");
		subjectlist.selectIndex(0);
		lvlist.clearSelection();
		searchtab.getButton("Search").click();
		Table result2 = win.getTabGroup("searchTabpage").getSelectedTab().getTable();
		result2.click(2, 0);
		Panel coursegui2 = win.getTabGroup("searchTabpage").getSelectedTab();
		coursegui2.getButton("Enroll").click();
		
		win.getTabGroup("searchTabpage").selectTab("New Search");
		subjectlist.selectIndex(0);
		lvlist.clearSelection();
		searchtab.getButton("Search").click();
		Table result3 = win.getTabGroup("searchTabpage").getSelectedTab().getTable();
		result3.click(2, 0);
		Panel coursegui3 = win.getTabGroup("searchTabpage").getSelectedTab();
		coursegui3.getButton("Enroll").click();
		
		win.getTabGroup("searchTabpage").selectTab("New Search");
		subjectlist.clearSelection();;
		lvlist.clearSelection();
		int size = win.getTabGroup("searchTabpage").getAwtComponent().getTabCount();
		searchtab.getButton("Search").click();
		int nsize = win.getTabGroup("searchTabpage").getAwtComponent().getTabCount();
		assertEquals(nsize, size);
	}
	
}
