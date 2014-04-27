package test.gui;

import java.awt.Component;

import javax.swing.JTable;

import ofcoursegui.MainWindow;
import ofcoursegui.MultiLineTableCellRenderer;
import ofcoursegui.SearchCodeIntervalGUI;
import ofcoursegui.SearchSubjectGUI;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.uispec4j.ListBox;
import org.uispec4j.Panel;
import org.uispec4j.Table;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;
import org.uispec4j.finder.ComponentMatcher;
import org.uispec4j.interception.MainClassAdapter;

public class MultiLineTableCellRendererTest extends UISpecTestCase {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetTableCellRendererComponent() {
		setAdapter(new MainClassAdapter(MainWindow.class, new String[0]));
		Window win = getMainWindow();
		
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
		JTable table = coursegui.getTable("sessionTable").getAwtComponent();
		
		Component old = coursegui.getTable("sessionTable").getSwingRendererComponentAt(0, 0);
		Component c = null;
		
		MultiLineTableCellRenderer r = new MultiLineTableCellRenderer();
		c = r.getTableCellRendererComponent(table, new String("a\nb\nc\nd"), false, false, 0, 0);
		assertNotSame(c, old);
		c = r.getTableCellRendererComponent(table, null, false, false, 0, 0);
		assertNotSame(c, old);
		c = r.getTableCellRendererComponent(table, new String("a\nb\nc\nd\ne"), true, false, 0, 0);
		assertNotSame(c, old);
		c = r.getTableCellRendererComponent(table, new String("a\nb\nc\nd\ne"), false, true, 0, 0);
		assertNotSame(c, old);
		c = r.getTableCellRendererComponent(table, new String("a\nb\nc\nd\ne"), true, true, 0, 0);
		assertNotSame(c, old);

	}

}
