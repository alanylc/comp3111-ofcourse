package ofcoursegui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import ofcourse.Network;

public class FriendRequestGUI extends JDialog {

	public FriendRequestGUI(final Frame parent) {
		super(parent, true);
		final Network network = Network.getOurNetwork();
		
		this.setSize(300, 260);
		this.setLocationRelativeTo(parent);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.setTitle("Friend Requests");
		
//		JPanel sentPane = new JPanel();
//		sentPane.setBorder(new TitledBorder(new LineBorder(new Color(153, 180, 209), 1, true), 
//				"Requests Sent", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
//		sentPane.setLayout(new GridLayout(1, 0, 0, 0));
		
		
		JPanel recPane = new JPanel();
		recPane.setBorder(new TitledBorder(new LineBorder(new Color(153, 180, 209), 1, true), 
				"Requests Received", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		recPane.setLayout(new GridLayout(1, 0, 0, 0));
		
		String[] reqListStr = network.getReqFriendList().split("!");
		DefaultListModel reqModel = new DefaultListModel();
		final JList reqList = new JList(reqModel);
		reqList.setPreferredSize(new Dimension(0, 50));
		for (int i=0; i<reqListStr.length; i++) {
			reqModel.addElement(reqListStr[i]);
		}
		JScrollPane reqPane = new JScrollPane(reqList);
		recPane.add(reqPane);
		
		
		JPanel acceptPane = new JPanel();
		acceptPane.setLayout(new GridLayout(1, 2, 5, 10));
		JButton btnAccept = new JButton("Accept");
		JButton btnIgnore = new JButton("Ignore");
		acceptPane.add(btnAccept);
		acceptPane.add(btnIgnore);
		acceptPane.setBorder(new EmptyBorder(5, 15, 10, 15));
		
		btnIgnore.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		
		btnAccept.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Object[] names = reqList.getSelectedValues();
				if (names.length==0) {
					JOptionPane.showMessageDialog(parent, "No Request Selected!");
				}
				else {
					dispose();
					String failed = "";
					for (Object name : names) {
						String returnCode = network.friendSet(name.toString());
						if (!returnCode.equals("100")) { // not OK (fails)
							failed = failed + name + ", ";
						}
					}
					// trigger the property change listener, so to update friend list and table
					MainWindow.updateFdNeeded.setText(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
					if (failed.equals("")) {
						JOptionPane.showMessageDialog(parent, "Friend Requests Accepted.");
					}
					else {
						JOptionPane.showMessageDialog(parent, "Fails to accept the following requests: "+failed.substring(0, failed.length()-2));
					}
					
				}
			}
		});
		
		JPanel pane = new JPanel();
		pane.setBorder(new EmptyBorder(12, 10, 5, 10));
		pane.setLayout(new GridLayout(1, 1, 0, 10));
		//pane.add(sentPane);
		pane.add(recPane);
		pane.add(acceptPane);
		
		JPanel outerPane = new JPanel();
		outerPane.setLayout(new BorderLayout());
		outerPane.add(pane, BorderLayout.CENTER);
		outerPane.add(acceptPane, BorderLayout.SOUTH);
		
		this.setContentPane(outerPane);
		MainWindow.addEscapeListener(this);
		this.setVisible(true);
	}
	
}
