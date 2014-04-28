package test.gui;

import java.awt.Component;

import javax.swing.JLabel;

import ofcourse.Network;
import ofcoursegui.MainWindow;
import ofcoursegui.SearchCodeIntervalGUI;
import ofcoursegui.SearchSubjectGUI;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.uispec4j.ListBox;
import org.uispec4j.Mouse;
import org.uispec4j.Panel;
import org.uispec4j.Table;
import org.uispec4j.TextBox;
import org.uispec4j.UISpec4J;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;
import org.uispec4j.finder.ComponentMatcher;
import org.uispec4j.interception.MainClassAdapter;
import org.uispec4j.interception.WindowInterceptor;

public class SearchTest extends UISpecTestCase {

	private boolean found = false;

	@Before
	public void setUp() throws Exception {
	}
		
	@After
	public void tearDown() throws Exception {
		Network.logout();
	}

	@Test
	public void testSearch() {
		UISpec4J.init();
		this.setAdapter(new MainClassAdapter(MainWindow.class, new String[0]));
		Window win = this.getMainWindow();
		
		Network.login("ctestdab", "bbb");
		
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
		win.getTabGroup("timetableTabpage").selectTab("Mine");
		coursegui3.getButton("Enroll").click();
		
		win.getTabGroup("searchTabpage").selectTab("New Search");
		subjectlist.clearSelection();;
		lvlist.clearSelection();
		int size = win.getTabGroup("searchTabpage").getAwtComponent().getTabCount();
		searchtab.getButton("Search").click();
		int nsize = win.getTabGroup("searchTabpage").getAwtComponent().getTabCount();
		assertEquals(nsize, size);
		
		
		win.getTabGroup("searchTabpage").selectTab("New Search");
		subjectlist.selectIndex(0);
		lvlist.clearSelection();
		searchtab.getButton("Search").click();
		Table tresult = win.getTabGroup("searchTabpage").getSelectedTab().getTable();
		tresult.click(2, 0);
		Panel cgui = win.getTabGroup("searchTabpage").getSelectedTab();
		win.getTabGroup("timetableTabpage").selectTab("Mine");
		cgui.getTable("sessionTable").selectRows(0);
		cgui.getButton("Enroll").click();
		
		Panel mine = win.getTabGroup("timetableTabpage").getSelectedTab();
		WindowInterceptor.run(win.getMenuBar().getMenu("File").getSubMenu("Download My Time Table").triggerClick());
		System.out.println(mine.getDescription());
		Component[] comps = mine.getSwingComponents(new ComponentMatcher() {
			@Override
			public boolean matches(Component component) {
				if (component.getClass().equals(JLabel.class)) {
					if (((JLabel) component).getText().contains("COMP1001"))
						return true;
					else
						return false;
				}
				else
					return false;
			}
		});
		assertTrue(comps.length>0);
		found = false;
		TextBox clabel = mine.getTextBox(new ComponentMatcher() {
			@Override
			public boolean matches(Component component) {
				if (component.getClass().equals(JLabel.class)) {
					if (((JLabel) component).getText().contains("COMP1001") && !found) {
						found = true;
						return true;
					}
					else
						return false;
				}
				else
					return false;
			}
		});
		found = false;
		Mouse.click(clabel);
		Mouse.click(mine);
		Mouse.click(clabel);
		win.getPanel("contentPane").getButton("Swap").click();
		win.getPanel("contentPane").getButton("Drop").click();
		win.getPanel("contentPane").getButton("Common Free Time").click();
		

		win.getTabGroup("searchTabpage").selectTab("New Search");
		subjectlist.select("COMP");
		lvlist.clearSelection();
		searchtab.getButton("Search").click();
		Table tresult2 = win.getTabGroup("searchTabpage").getSelectedTab().getTable();
		tresult2.click(0, 0);
		Panel cgui2 = win.getTabGroup("searchTabpage").getSelectedTab();
		cgui2.getButton("Comment").click();
		
	}
}
