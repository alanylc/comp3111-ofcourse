package ofcoursegui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;

import java.awt.Font;

public class CommentGUI extends JPanel {
	public CommentGUI() {
	}
	Date dNow = new Date();
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
	JLabel nameLabel = new JLabel("<Name>");
	JTextArea commentsTextArea = new JTextArea();
	JLabel ratingLabel = new JLabel("3");
	JLabel timeLabel = new JLabel(dateFormat.format(dNow));
	{

		setLayout(null);
		nameLabel.setBounds(5, 5, 80, 20);
		add(nameLabel);
		commentsTextArea.setFont(new Font("SansSerif", Font.PLAIN, 11));
		commentsTextArea.setEditable(false);

		commentsTextArea.setBounds(35, 22, 280, 78);
		String cm="comments\r\ncomments line2\r\ncomments third veryvery very ..................................................................................................very very very very very very very very very very very very very very very long line on line 3\nline 4.\nline5\n6\n7\n8\n9\n10\n11\n12";
		commentsTextArea.setText(cm);
		JScrollPane scroll = new JScrollPane(commentsTextArea);
	    scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    scroll.setBounds(35, 22, 280, 78);
	    add(scroll);
		//commentsTextPane
		//add(commentsTextArea);
		ratingLabel.setBounds(115, 5, 40, 20);
		add(ratingLabel);
		timeLabel.setBounds(235, 96, 100, 20);
		add(timeLabel);
	}
}
