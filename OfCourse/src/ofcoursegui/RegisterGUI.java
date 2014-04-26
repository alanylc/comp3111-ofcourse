package ofcoursegui;

import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import ofcourse.Network;

@SuppressWarnings("serial")
public class RegisterGUI extends JDialog {
	public RegisterGUI(final Frame parent) {
		super(parent, true);
		this.setSize(300, 130);
		this.setLocationRelativeTo(parent);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.setTitle("Register");
		JLabel msg1 = new JLabel("Username: ");
		JButton btnRegister = new JButton("Register");
		final JTextField usernameTextField = new JTextField(10);
		
		usernameTextField.setName("username");
		
		btnRegister.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String username = usernameTextField.getText();
				Network network = Network.getOurNetwork();
				String returnCode = network.register(username);
				if (returnCode.equals("100")){ // register successfully
					dispose();
					JOptionPane.showMessageDialog(parent, "Register Successfully.",
							"Registration", JOptionPane.INFORMATION_MESSAGE);
				}
				else { // error returned
					String reason = "Unknown Reason";
					// 000: Character error: Name contains invalid characters
					if (returnCode.equals("000")) {
						reason = "Username contains invalid characters.";
					}
					// 001: Username exists: The username that user inputs has been registered
					else if (returnCode.equals("001")) {
						reason = "Username <"+username+"> has been used.";
					}
					// 003: Email not sent: The email containing the password cannot be sent due to server side error
					else if (returnCode.equals("003")) {
						reason = "Mail Server Error.";
					}
					// 404: Network error: Then there is no network or server is down.
					else if (returnCode.equals("404")) {
						reason = "Network Unavailable / Server Down";
						
					}
					JOptionPane.showMessageDialog(parent, "Registeration fails. Reason: "+reason,
							"Registration", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(15, 10, 10, 10));
		this.setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(2, 1));
		JPanel p1 = new JPanel();
		p1.add(msg1);
		p1.add(usernameTextField);
		contentPane.add(p1);
		JPanel p2 = new JPanel();
		p2.add(btnRegister);
		contentPane.add(p2);
		contentPane.getRootPane().setDefaultButton(btnRegister);
		contentPane.setVisible(true);
		MainWindow.addEscapeListener(this);
		this.setVisible(true);
	}
}
