package test.gui;

import javax.swing.JLabel;

import ofcourse.Network;
import ofcoursegui.MainWindow;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.uispec4j.Trigger;
import org.uispec4j.UISpec4J;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;
import org.uispec4j.interception.MainClassAdapter;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;

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
	public void testClickLogin01() {
		assertFalse(MainWindow.haveLogined());
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
		assertTrue(MainWindow.haveLogined());
	}
	
	@Test
	public void testClickLogin02() {
		WindowInterceptor.init(win.getMenuBar().getMenu("Account").getSubMenu("Login").triggerClick())
			.process(new WindowHandler("LoginGUI") {
					public Trigger process(Window dialog) {
						assertTrue(dialog.titleEquals("Login"));
						dialog.getInputTextBox("username").setText("ctestdab");
						dialog.getPasswordField("password").setPassword("bbbx");
						return dialog.getButton("Login").triggerClick();
					}
			})
			.run();
		assertFalse(MainWindow.haveLogined());
	}
	
	@Test
	public void testClickLogin03() {
		WindowInterceptor.init(win.getMenuBar().getMenu("Account").getSubMenu("Login").triggerClick())
			.process(new WindowHandler("LoginGUI") {
					public Trigger process(Window dialog) {
						assertTrue(dialog.titleEquals("Login"));
						dialog.getInputTextBox("username").setText("");
						dialog.getPasswordField("password").setPassword("bbb");
						return dialog.getButton("Login").triggerClick();
					}
			})
			.run();
		assertFalse(MainWindow.haveLogined());
	}
	
	@Test
	public void testClickLogin04() {
		WindowInterceptor.init(win.getMenuBar().getMenu("Account").getSubMenu("Login").triggerClick())
			.process(new WindowHandler("LoginGUI") {
					public Trigger process(Window dialog) {
						assertTrue(dialog.titleEquals("Login"));
						dialog.getInputTextBox("username").setText("firstLogin");
						dialog.getPasswordField("password").setPassword("");
						return dialog.getButton("Login").triggerClick();
					}
			})
			.run();
		assertFalse(MainWindow.haveLogined());
	}
	
	@Test
	public void testClickRegister01() {
		WindowInterceptor.init(win.getMenuBar().getMenu("Account").getSubMenu("Register").triggerClick())
			.process(new WindowHandler("RegisterGUI") {
					public Trigger process(Window dialog) {
						assertTrue(dialog.titleEquals("Register"));
						dialog.getInputTextBox("username").setText("ctestdab");
						return dialog.getButton("Register").triggerClick();
					}
			})
			.run();
		assertFalse(MainWindow.haveLogined());
	}
	
	@Test
	public void testClickRegister02() {
		WindowInterceptor.init(win.getMenuBar().getMenu("Account").getSubMenu("Register").triggerClick())
			.process(new WindowHandler("RegisterGUI") {
					public Trigger process(Window dialog) {
						assertTrue(dialog.titleEquals("Register"));
						dialog.getInputTextBox("username").setText("ctestd/;ab+x*%a#");
						return dialog.getButton("Register").triggerClick();
					}
			})
			.run();
		assertFalse(MainWindow.haveLogined());
	}
	
	@Test
	public void testClickChangePassword01() {
		Network.login("ctestdae", "eee");
		assertTrue(MainWindow.haveLogined());
		WindowInterceptor.init(win.getMenuBar().getMenu("Account").getSubMenu("Change Password").triggerClick())
		.process(new WindowHandler() {
				public Trigger process(Window dialog) {
					assertTrue(dialog.titleEquals("Change Password"));
					dialog.getPasswordField("opw").setPassword("eee");
					dialog.getPasswordField("npw").setPassword("eee1");
					dialog.getPasswordField("cpw").setPassword("eee1");
					return dialog.getButton("Submit").triggerClick();
				}
		})
		.run();
		assertTrue(MainWindow.haveLogined());
		Network.login("ctestdae", "eee1");
		assertTrue(MainWindow.haveLogined());
		Network.getOurNetwork().newPW("eee1", "eee");
		Network.login("ctestdae", "eee");
		assertTrue(MainWindow.haveLogined());
	}
	
	@Test
	public void testClickChangePassword02() {
		Network.login("ctestdae", "eee");
		assertTrue(MainWindow.haveLogined());
		WindowInterceptor.init(win.getMenuBar().getMenu("Account").getSubMenu("Change Password").triggerClick())
		.process(new WindowHandler() {
				public Trigger process(Window dialog) {
					assertTrue(dialog.titleEquals("Change Password"));
					dialog.getPasswordField("opw").setPassword("eee");
					dialog.getPasswordField("npw").setPassword("eeex");
					dialog.getPasswordField("cpw").setPassword("eeexxxx");
					return dialog.getButton("Submit").triggerClick();
				}
		})
		.run();
		Network.login("ctestdae", "eee");
		assertTrue(MainWindow.haveLogined());
	}
	
	@Test
	public void testClickChangePassword03() {
		Network.login("ctestdae", "eee");
		assertTrue(MainWindow.haveLogined());
		WindowInterceptor.init(win.getMenuBar().getMenu("Account").getSubMenu("Change Password").triggerClick())
		.process(new WindowHandler() {
				public Trigger process(Window dialog) {
					assertTrue(dialog.titleEquals("Change Password"));
					dialog.getPasswordField("opw").setPassword("eeexxy");
					dialog.getPasswordField("npw").setPassword("eee3");
					dialog.getPasswordField("cpw").setPassword("eee3");
					return dialog.getButton("Submit").triggerClick();
				}
		})
		.run();
		Network.login("ctestdae", "eee");
		assertTrue(MainWindow.haveLogined());
	}
	// the following codes are wrong
	/*
	@Test
	public void testClickSeeFriend() {
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
		win.getPanel("contentPane").getButton("See Friend").click();
		System.out.println(">>>>>>>> "+win.getTabGroup("timetableTabpage").getAwtComponent().getTabCount());
		assertEquals(2, win.getTabGroup("timetableTabpage").getAwtComponent().getTabCount());
		
		logout();
		assertEquals(1, win.getTabGroup("timetableTabpage").getAwtComponent().getTabCount());
		win.getButton("See Friend").click();
		assertEquals(1, win.getTabGroup("timetableTabpage").getAwtComponent().getTabCount());
	}
	/*
	@Test
	public void testClickAddFriend01() {
		final Window win = getMainWindow();
		Network.login("ctestdae", "eee");
		new UISpecAdapter() {
			public Window getMainWindow() {
				WindowInterceptor.init(win.getMenuBar().getMenu("Friend").getSubMenu("Add New Friend").triggerClick())
				.process(new WindowHandler() {
					public Trigger process(Window window) throws Exception {
						assertNotNull(window);
						window.getInputTextBox().setText("firstLogin");
						return window.getButton("OK").triggerClick();
					}
				}).run();
				return null;
			}
		};
	}
	
	@Test
	public void testClickAddFriend02() {
		final Window win = getMainWindow();
		Network.logout();
		new UISpecAdapter() {
			public Window getMainWindow() {
				WindowInterceptor.init(win.getMenuBar().getMenu("Friend").getSubMenu("Add New Friend").triggerClick())
				.process(new WindowHandler() {
					public Trigger process(Window window) throws Exception {
						assertNull(window);
						return Trigger.DO_NOTHING;
					}
				}).run();
				return null;
			}
		};
	}
	
	@Test
	public void testClickCheckFriendRequest() {
		final Window win = getMainWindow();
		Network.login("ctestdab", "ctestdab");
		new UISpecAdapter() {
			public Window getMainWindow() {
				WindowInterceptor.init(win.getMenuBar().getMenu("Friend").getSubMenu("Check Friend Requests").triggerClick())
				.process(new WindowHandler() {
					public Trigger process(Window window) throws Exception {
						assertNotNull(window);
						return window.getButton("Ignore").triggerClick();
					}
				}).run();
				return null;
			}
		};
	}
	
	@Test
	public void testClickUpdateFriendsTimeTable() {
		final Window win = getMainWindow();
		Network.login("ctestdab", "ctestdab");
		new UISpecAdapter() {
			public Window getMainWindow() {
				WindowInterceptor.init(win.getMenuBar().getMenu("Friend").getSubMenu("Update Friends' Time Table").triggerClick())
				.process(new WindowHandler() {
					public Trigger process(Window window) throws Exception {
						assertNull(window);
						return Trigger.DO_NOTHING;
					}
				}).run();
				return null;
			}
		};
	}
	
	@Test
	public void testClickUploadMyTimeTable01() {
		final Window win = getMainWindow();
		Network.login("ctestdab", "ctestdab");
		new UISpecAdapter() {
			public Window getMainWindow() {
				WindowInterceptor.init(win.getMenuBar().getMenu("File").getSubMenu("Upload My Time Table").triggerClick())
				.process(new WindowHandler() {
					public Trigger process(Window window) throws Exception {
						assertNull(window);
						return Trigger.DO_NOTHING;
					}
				}).run();
				return null;
			}
		};
	}
	
	@Test
	public void testClickUploadMyTimeTable02() {
		final Window win = getMainWindow();
		Network.logout();
		new UISpecAdapter() {
			public Window getMainWindow() {
				WindowInterceptor.init(win.getMenuBar().getMenu("File").getSubMenu("Upload My Time Table").triggerClick())
				.process(new WindowHandler() {
					public Trigger process(Window window) throws Exception {
						assertNull(window);
						return Trigger.DO_NOTHING;
					}
				}).run();
				return null;
			}
		};
	}
	
	@Test
	public void testClickDownloadMyTimeTable01() {
		final Window win = getMainWindow();
		Network.login("ctestdab", "ctestdab");
		new UISpecAdapter() {
			public Window getMainWindow() {
				WindowInterceptor.init(win.getMenuBar().getMenu("File").getSubMenu("Download My Time Table").triggerClick())
				.process(new WindowHandler() {
					public Trigger process(Window window) throws Exception {
						assertNull(window);
						return Trigger.DO_NOTHING;
					}
				}).run();
				return null;
			}
		};
	}
	
	@Test
	public void testClickDownloadMyTimeTable02() {
		final Window win = getMainWindow();
		Network.logout();
		new UISpecAdapter() {
			public Window getMainWindow() {
				WindowInterceptor.init(win.getMenuBar().getMenu("File").getSubMenu("Download My Time Table").triggerClick())
				.process(new WindowHandler() {
					public Trigger process(Window window) throws Exception {
						assertNull(window);
						return Trigger.DO_NOTHING;
					}
				}).run();
				return null;
			}
		};
	}
	
	@Test
	public void testClickImportTimeTable() {
		final Window win = getMainWindow();
		Network.logout();
		new UISpecAdapter() {
			public Window getMainWindow() {
				WindowInterceptor.init(win.getMenuBar().getMenu("File").getSubMenu("Import Time Table...").triggerClick())
				.process(new WindowHandler() {
					public Trigger process(Window window) throws Exception {
						assertNull(window);
						return Trigger.DO_NOTHING;
					}
				}).run();
				return null;
			}
		};
	}
	
	@Test
	public void testClickExportTimeTable() {
		final Window win = getMainWindow();
		Network.logout();
		new UISpecAdapter() {
			public Window getMainWindow() {
				WindowInterceptor.init(win.getMenuBar().getMenu("File").getSubMenu("Export Time Table...").triggerClick())
				.process(new WindowHandler() {
					public Trigger process(Window window) throws Exception {
						assertNull(window);
						return Trigger.DO_NOTHING;
					}
				}).run();
				return null;
			}
		};
	}
	
	@Test
	public void testClickExportTimeTableAsImage() {
		final Window win = getMainWindow();
		Network.logout();
		new UISpecAdapter() {
			public Window getMainWindow() {
				WindowInterceptor.init(win.getMenuBar().getMenu("File").getSubMenu("Export Time Table as image...").triggerClick())
				.process(new WindowHandler() {
					public Trigger process(Window window) throws Exception {
						assertNull(window);
						return Trigger.DO_NOTHING;
					}
				}).run();
				return null;
			}
		};
	}
	
	@Test
	public void testClickDeleteLast01() {
		final Window win = getMainWindow();
		Network.login("ctestdab", "bbb");
		new UISpecAdapter() {
			public Window getMainWindow() {
				WindowInterceptor.init(win.getButton("See Friend").triggerClick())
				.process(new WindowHandler() {
					public Trigger process(Window window) throws Exception {
						assertNull(window);
						assertTrue(win.findSwingComponent(JTabbedPane.class, "timtableTabpage").getTabCount()==2);
						WindowInterceptor.run(win.getButton("Delete Last").triggerClick());
						assertTrue(win.findSwingComponent(JTabbedPane.class, "timtableTabpage").getTabCount()==1);
						return Trigger.DO_NOTHING;
					}
				}).run();
				return null;
			}
		};
	}
	
	@Test
	public void testClickDeleteLast02() {
		final Window win = getMainWindow();
		Network.logout();
		new UISpecAdapter() {
			public Window getMainWindow() {
				WindowInterceptor.init(win.getButton("See Friend").triggerClick())
				.process(new WindowHandler() {
					public Trigger process(Window window) throws Exception {
						assertNull(window);
						assertTrue(win.findSwingComponent(JTabbedPane.class, "timtableTabpage").getTabCount()==1);
						WindowInterceptor.run(win.getButton("Delete Last").triggerClick());
						assertTrue(win.findSwingComponent(JTabbedPane.class, "timtableTabpage").getTabCount()==1);
						return Trigger.DO_NOTHING;
					}
				}).run();
				return null;
			}
		};
	}
	
	@Test
	public void testClickCommonFreeTime01() {
		final Window win = getMainWindow();
		Network.login("ctestdab", "bbb");
		new UISpecAdapter() {
			public Window getMainWindow() {
				WindowInterceptor.init(win.getButton("Common Free Time").triggerClick())
				.process(new WindowHandler() {
					public Trigger process(Window window) throws Exception {
						assertNull(window);
						return Trigger.DO_NOTHING;
					}
				}).run();
				return null;
			}
		};
	}
	
	@Test
	public void testClickCommonFreeTime02() {
		final Window win = getMainWindow();
		Network.logout();
		new UISpecAdapter() {
			public Window getMainWindow() {
				WindowInterceptor.init(win.getButton("Common Free Time").triggerClick())
				.process(new WindowHandler() {
					public Trigger process(Window window) throws Exception {
						assertNull(window);
						return Trigger.DO_NOTHING;
					}
				}).run();
				return null;
			}
		};
	}
	*/
}
