package test.gui;

import ofcoursegui.MainWindow;

import org.uispec4j.Trigger;
import org.uispec4j.UISpec4J;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;
import org.uispec4j.interception.MainClassAdapter;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;

public class MainWindowTest extends UISpecTestCase {

	  static {
	      UISpec4J.init();
	  }
	  
	  protected void setUp() throws Exception {
		    setAdapter(new MainClassAdapter(MainWindow.class, new String[0]));
	  }

	  public void testInitialState() {
		  Window win = getMainWindow();
		  
		  // click Login
		  WindowInterceptor.init(win.getMenuBar().getMenu("Account").getSubMenu("Login").triggerClick())
		  .process(new WindowHandler() {
			  public Trigger process(Window dialog) {
		      assertTrue(dialog.titleEquals("Login"));
		      dialog.getInputTextBox("username").setText("a");
		      return dialog.getButton("Login").triggerClick();
		    }
		  })
		  .run();
		  Window dialog = WindowInterceptor.run(win.getMenuBar().getMenu("Account")
				  .getSubMenu("Login").triggerClick());
		  assertTrue(dialog.titleEquals("Login"));
		  dialog.getInputTextBox("username").setText("ctestdab");
		  dialog.getPasswordField("password").setPassword("ctestdab");
	      
	  }
}
