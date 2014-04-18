package ofcoursegui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import ofcourse.Course;
import ofcourse.Network;

@SuppressWarnings("serial")
public class AddCommentGUI extends JDialog {
	String courseName="";
	private Frame parent = null;
	JLabel ratingLabel=new JLabel("Rating:");
	JLabel commentLabel=new JLabel("Comment:");
	JRadioButton B1 = new JRadioButton("1");
	JRadioButton B2 = new JRadioButton("2");
	JRadioButton B3 = new JRadioButton("3");
	JRadioButton B4 = new JRadioButton("4");
	JRadioButton B5 = new JRadioButton("5");
	ButtonGroup ratingGroup = new ButtonGroup();
	JTextArea commentArea= new JTextArea();
	JButton submitCommentButton = new JButton("Submit");
	public AddCommentGUI(final Frame parent, final String string) {
		super(parent, true);
		this.parent = parent;
		this.setSize(470, 235);
		this.setLocationRelativeTo(MainWindow.contentPane);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.setTitle("Comment");
		courseName=string;
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(8, 10, 10, 10));
		JPanel commentPanel = new JPanel();
		commentPanel.setLayout(null);
		ratingGroup.add(B1);
		ratingGroup.add(B2);
		ratingGroup.add(B3);
		ratingGroup.add(B4);
		ratingGroup.add(B5);
		//B1.setSelected(true);
		JPanel ratingPanel = new JPanel();
		ratingPanel.setLayout(new GridLayout(1, 5));
		ratingPanel.add(B1);
		ratingPanel.add(B2);
		ratingPanel.add(B3);
		ratingPanel.add(B4);
		ratingPanel.add(B5);
		ratingPanel.setBounds(87, 10, 195, 23);
		commentPanel.add(ratingPanel);
		commentArea.setBounds(64, 38, 334, 87);
		
		JScrollPane scroll = new JScrollPane(commentArea);
	    scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    scroll.setBounds(87, 43, 349, 96);
	    commentPanel.add(scroll);
		
		//commentPanel.add(commentArea);
		ratingLabel.setBounds(10, 10, 67, 23);
		commentPanel.add(ratingLabel);
		commentLabel.setBounds(10, 40, 67, 23);
		commentPanel.add(commentLabel);
		submitCommentButton.setBounds(351, 155, 87, 29);
		commentPanel.add(submitCommentButton);
		submitCommentButton.addActionListener(new SubmitCommentButtonListener());
		MainWindow.addEscapeListener(this);
		contentPane.setLayout(new GridLayout(0, 1, 0, 0));
		contentPane.add(commentPanel);
		contentPane.setVisible(true);
		this.setContentPane(contentPane);
		this.setVisible(true);
	}
	private class SubmitCommentButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Component parentComp = AddCommentGUI.this;
			String rating=getSelectedButtonString(ratingGroup);
			if (rating.isEmpty()) {
				JOptionPane.showMessageDialog(parentComp,"Rating not selected yet!","Failure!",JOptionPane.WARNING_MESSAGE);
				return;
			}
			String comment=commentArea.getText();
			Network a=Network.getOurNetwork();
			String reply=a.comment(courseName, rating, comment);
			switch(Integer.parseInt(reply)){
			case 100:
				dispose();
				Course.getCourseByName(courseName).setAvgRating(Float.valueOf(rating));
				// as the add comment GUI is tied to MainWindow, the selected component must be CourseGUI 
//				int sindex = MainWindow.searchTabpage.getSelectedIndex();
//				CourseGUI comp = (CourseGUI) MainWindow.searchTabpage.getSelectedComponent();
//				MainWindow.searchTabpage.setComponentAt(sindex, comp.cloneGUI());
				CourseGUI c = (CourseGUI) MainWindow.searchTabpage.getSelectedComponent();
				c.updateGUI();
				JOptionPane.showMessageDialog(parentComp,"Comment submitted successfully!","Success!",JOptionPane.INFORMATION_MESSAGE);
				break;
			case 002:
				JOptionPane.showMessageDialog(parentComp,"Wrong username or password detected!","Failure!",JOptionPane.WARNING_MESSAGE);
				break;
			case 404:
				JOptionPane.showMessageDialog(parentComp,"Network error! Comment is not submitted!","Failure!",JOptionPane.WARNING_MESSAGE);
				break;
			case 200:
				JOptionPane.showMessageDialog(parentComp,"You have to login to submit comments!","Failure!",JOptionPane.WARNING_MESSAGE);
				break;
			default:
				JOptionPane.showMessageDialog(parentComp,"Error occured, comment is not submitted.","Failure!",JOptionPane.WARNING_MESSAGE);
				break;
				
				
			}
		}
		public String getSelectedButtonString(ButtonGroup buttonGroup) {
	        for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
	            AbstractButton button = buttons.nextElement();

	            if (button.isSelected()) {
	                return button.getText();
	            }
	        }

	        return "";
	    }
	}

}

