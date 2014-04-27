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

public class TimeTableOperationsTest extends UISpecTestCase {
	
	private boolean found = false;
	
	@Before
	public void setUp() throws Exception {
	}
	
	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testTimeTableOperations() {
		UISpec4J.init();
		this.setAdapter(new MainClassAdapter(MainWindow.class, new String[0]));
		Window win = this.getMainWindow();
		Network.logout();
		
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
		
		win.getTabGroup("searchTabpage").selectTab("New Search");
		subjectlist.selectIndex(0);
		lvlist.clearSelection();
		searchtab.getButton("Search").click();
		Table result = win.getTabGroup("searchTabpage").getSelectedTab().getTable();
		result.click(2, 0);
		Panel coursegui = win.getTabGroup("searchTabpage").getSelectedTab();
		win.getTabGroup("timetableTabpage").selectTab("Mine");
		coursegui.getTable("sessionTable").selectRows(0);
		coursegui.getButton("Enroll").click();
		
		Panel mine = win.getTabGroup("timetableTabpage").getSelectedTab();
		Component[] comps = mine.getSwingComponents(new ComponentMatcher() {
			@Override
			public boolean matches(Component component) {
				if (component.getClass().equals(JLabel.class)) {
					if (((JLabel) component).getText().contains("ACCT"))
						return true;
					else
						return false;
				}
				else
					return false;
			}
		});
		assertEquals(2, comps.length);
		found = false;
		TextBox clabel = mine.getTextBox(new ComponentMatcher() {
			@Override
			public boolean matches(Component component) {
				if (component.getClass().equals(JLabel.class)) {
					if (((JLabel) component).getText().contains("ACCT") && !found) {
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
		Mouse.click(clabel);
		found = false;
		Mouse.click(mine);
		

		win.getTabGroup("searchTabpage").selectTab("New Search");
		subjectlist.select("COMP");
		lvlist.clearSelection();
		searchtab.getButton("Search").click();
		Table result2 = win.getTabGroup("searchTabpage").getSelectedTab().getTable();
		result2.click(0, 0);
		Panel coursegui2 = win.getTabGroup("searchTabpage").getSelectedTab();
		coursegui2.getButton("Comment").click();
		
	}
}
