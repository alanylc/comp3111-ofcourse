package test.gui;

import javax.swing.JTabbedPane;

import ofcourse.Network;
import ofcoursegui.MainWindow;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.uispec4j.Trigger;
import org.uispec4j.UISpec4J;
import org.uispec4j.UISpecAdapter;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;
import org.uispec4j.interception.MainClassAdapter;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;

public class MainWindowTest extends UISpecTestCase {
	
	private Window win = null;

	static {
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
		setAdapter(new MainClassAdapter(MainWindow.class, new String[0]));
		win = getMainWindow();
	}
		
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testClickLogin01() {
		Network.logout();
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
		Network.logout();
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
		Network.logout();
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
		Network.logout();
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
		Network.logout();
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
		Network.logout();
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
		Network.login("ctestdae", "eee1");
		assertTrue(MainWindow.haveLogined());
		Network.getOurNetwork().newPW("eee1", "eee");
	}
	
	@Test
	public void testClickChangePassword02() {
		Network.login("ctestdae", "eee");
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
		WindowInterceptor.init(win.getMenuBar().getMenu("Account").getSubMenu("Change Password").triggerClick())
		.process(new WindowHandler() {
				public Trigger process(Window dialog) {
					assertTrue(dialog.titleEquals("Change Password"));
					dialog.getPasswordField("opw").setPassword("eeexx");
					dialog.getPasswordField("npw").setPassword("eee3");
					dialog.getPasswordField("cpw").setPassword("eee3");
					return dialog.getButton("Submit").triggerClick();
				}
		})
		.run();
		Network.login("ctestdae", "eee");
		assertTrue(MainWindow.haveLogined());
	}
	
	@Test
	public void testClickSeeFriend01() {
		Network.login("ctestdab", "bbb");
		new UISpecAdapter() {
			public Window getMainWindow() {
				WindowInterceptor.init(win.getButton("See Friend").triggerClick())
				.process(new WindowHandler() {
					public Trigger process(Window window) throws Exception {
						assertNull(window);
						assertTrue(win.findSwingComponent(JTabbedPane.class, "timtableTabpage").getTabCount()==2);
						return Trigger.DO_NOTHING;
					}
				}).run();
				return null;
			}
		};
	}
	
	@Test
	public void testClickSeeFriend02() {
		Network.logout();
		new UISpecAdapter() {
			public Window getMainWindow() {
				WindowInterceptor.init(win.getButton("See Friend").triggerClick())
				.process(new WindowHandler() {
					public Trigger process(Window window) throws Exception {
						assertNull(window);
						assertTrue(win.findSwingComponent(JTabbedPane.class, "timtableTabpage").getTabCount()==1);
						return Trigger.DO_NOTHING;
					}
				}).run();
				return null;
			}
		};
	}
	
	@Test
	public void testClickAddFriend01() {
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
	
}
