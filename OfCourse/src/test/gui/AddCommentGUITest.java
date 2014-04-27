package test.gui;

import java.awt.Component;

import ofcourse.Network;
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
import org.uispec4j.UISpecAdapter;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;
import org.uispec4j.finder.ComponentMatcher;
import org.uispec4j.interception.MainClassAdapter;
import org.uispec4j.interception.WindowInterceptor;

public class AddCommentGUITest extends UISpecTestCase {
	  
	@Before
	public void setUp() throws Exception {
		UISpec4J.init();
		this.setAdapter(new MainClassAdapter(MainWindow.class, new String[0]));
		final Window win = this.getMainWindow();
		Network.login("ctestdae", "eee");
		this.setAdapter(new UISpecAdapter() {
			@Override
			public Window getMainWindow() {
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
				return WindowInterceptor.run(coursegui.getButton("Comment").triggerClick());
			}
		});
	}
		
	@After
	public void tearDown() throws Exception {
		Network.logout();
	}

	@Test
	public void testAddComment() {
		Window addcmt = getMainWindow();
		assertTrue(addcmt.titleEquals("Comment"));
		addcmt.getButton("Submit").click();
		addcmt.getRadioButton("5").click();
		addcmt.getButton("Submit").click();
	}
	
}