package ofcoursegui;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import ofcourse.Network;

@SuppressWarnings("serial")
public class ChangePwGUI extends JDialog {
	private boolean firstChange = false;
	private Frame parent = null;
	private String oldPW = null;
	
	public ChangePwGUI(final Frame _parent) {
		this(_parent, null, false);
	}
	
	public ChangePwGUI(final Frame _parent, String _oldPW, boolean _firstChange) {
		super(_parent, true);
		this.oldPW = _oldPW;
		this.parent = _parent;
		this.firstChange = _firstChange;
		if (firstChange) {
			this.setSize(300, 160);
		}
		else {
			this.setSize(300, 200);
		}
		this.setLocationRelativeTo(parent);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.setTitle("Change Password");
		JLabel msg0 = new JLabel("Old Password: ");
		JLabel msg1 = new JLabel("New Password: ");
		JLabel msg2 = new JLabel("Confirm New Password: ");
		msg0.setPreferredSize(new Dimension(140, 30));
		msg1.setPreferredSize(new Dimension(140, 30));
		msg2.setPreferredSize(new Dimension(140, 30));
		JButton btnSubmit = new JButton("Submit");
		final JPasswordField opwField = new JPasswordField(10);
		final JPasswordField npwField = new JPasswordField(10);
		final JPasswordField cpwField = new JPasswordField(10);
		opwField.setName("opw");
		npwField.setName("npw");
		cpwField.setName("cpw");
		
		btnSubmit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String npw = new String(npwField.getPassword());
				String cpw = new String(cpwField.getPassword());
				Network network = Network.getOurNetwork();
				if (!npw.equals(cpw)){ // PW and confirm PW not match
					JOptionPane.showMessageDialog(parent, "New Password and Confirm New Password Mismatch!",
							"Change Password", JOptionPane.WARNING_MESSAGE);
				}
				else {
					String returnCode = null;
					if (ChangePwGUI.this.firstChange) { // first time change PW
						returnCode = network.firstNewPW(oldPW, npw);
					}
					else {
						String opw = new String(opwField.getPassword());
						returnCode = network.newPW(opw, npw);
					}
					if (returnCode.equals("100")) { // OK
						dispose();
						JOptionPane.showMessageDialog(parent, "Password Changed Successfully.",
							"Congratulations!", JOptionPane.INFORMATION_MESSAGE);
					}
					else if (returnCode.equals("002")) { // incorrect username / password
						JOptionPane.showMessageDialog(parent, "Wrong Old Password!.",
								"Change Password", JOptionPane.WARNING_MESSAGE);
					}
					else if (returnCode.equals("404")) { 
						JOptionPane.showMessageDialog(parent, "Network Unavailable / Server Down.",
								"Change Password", JOptionPane.WARNING_MESSAGE);
					}
					else {
						JOptionPane.showMessageDialog(parent, "Operation fails.",
								"Change Password", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
			
		});
		
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(8, 10, 10, 10));
		this.setContentPane(contentPane);
		int rows = 4;
		if (firstChange) {
			rows = 3;
		}
		contentPane.setLayout(new GridLayout(rows, 1));
		if (!firstChange) {
			JPanel p0 = new JPanel();
			p0.add(msg0);
			p0.add(opwField);
			contentPane.add(p0);
		}
		JPanel p1 = new JPanel();
		p1.add(msg1);
		p1.add(npwField);
		contentPane.add(p1);
		JPanel p2 = new JPanel();
		p2.add(msg2);
		p2.add(cpwField);
		contentPane.add(p2);
		JPanel p3 = new JPanel();
		p3.add(btnSubmit);
		contentPane.add(p3);
		contentPane.getRootPane().setDefaultButton(btnSubmit);
		contentPane.setVisible(true);
		MainWindow.addEscapeListener(this);
		this.setVisible(true);
	}
	
}
