package ofcoursegui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import java.awt.Font;
import java.awt.Color;
import java.awt.SystemColor;

public class CommentGUI extends JPanel {
	public CommentGUI(String Name,String rating,String comment,String dateTime) {
		nameLabel.setText(Name);
		ratingLabel.setText(rating);
		//commentsTextArea.setEditable(false);
		commentsTextArea.setText(comment);
		timeLabel.setText(dateTime);
	}
	Date dNow = new Date();
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
	JLabel nameLabel = new JLabel("<Name>");
	JTextArea commentsTextArea = new JTextArea();
	JLabel rating =new JLabel("Rating:");
	JLabel comment =new JLabel("Comments:");
	JLabel ratingLabel = new JLabel("530*115");
	JLabel timeLabel = new JLabel(dateFormat.format(dNow));
	{

		setLayout(null);
		nameLabel.setBounds(5, 5, 80, 20);
		add(nameLabel);
		commentsTextArea.setBackground(SystemColor.control);
		commentsTextArea.setFont(new Font("SansSerif", Font.PLAIN, 11));

		commentsTextArea.setBounds(35, 22, 280, 78);
		String cm="comments\r\ncomments line2\r\ncomments third veryvery very ..................................................................................................very very very very very very very very very very very very very very very long line on line 3\nline 4.\nline5\n6\n7\n8\n9\n10\n11\n12";
		commentsTextArea.setText(cm);
		JScrollPane scroll = new JScrollPane(commentsTextArea);
	    scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    scroll.setBounds(76, 22, 390, 67);
	    add(scroll);
		//commentsTextPane
		//add(commentsTextArea);
		rating.setBounds(90, 5, 40, 20);
		add(rating);
		comment.setBounds(5, 23, 67, 20);
		add(comment);
		ratingLabel.setBounds(140, 5, 61, 20);
		add(ratingLabel);
		timeLabel.setBounds(346, 99, 120, 20);
		add(timeLabel);
	}
}
