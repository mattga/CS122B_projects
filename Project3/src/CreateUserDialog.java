import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Helpers.QueryTableModel;


public class CreateUserDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField user;
	private JButton add, cancel;
	private JPasswordField pass;
	private JPanel buttonPanel, inputPanel;
	private Statement stmt;
	private JFrame parent;
	private QueryTableModel userqtm;
	
	public CreateUserDialog(JFrame parent, Connection con, QueryTableModel userqtm) {
		try {
			stmt = con.createStatement();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		this.parent = parent;
		this.userqtm = userqtm;
		
		user = new JTextField(15);
		
		pass = new JPasswordField(15);
		
		inputPanel = new JPanel(new GridLayout(2,2));
		inputPanel.add(new JLabel("Username:"));
		inputPanel.add(user);
		inputPanel.add(new JLabel("Password:"));
		inputPanel.add(pass);
		
		add = new JButton("Add");
		add.addActionListener(new CreateUserActionListener());
		
		cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				CreateUserDialog.this.dispose();
			}
		});
		
		buttonPanel = new JPanel();
		buttonPanel.add(add);
		buttonPanel.add(cancel);
		
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.add(inputPanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
		this.setTitle("Create User");
		this.setSize(300,200);
		this.setVisible(true);
	}
	
	private class CreateUserActionListener implements ActionListener {
		public void actionPerformed (ActionEvent e) {
			try {
				ResultSet userCheck = stmt.executeQuery("SELECT * FROM mysql.user WHERE user='" + user.getText() + "' AND host='localhost'");
				if(userCheck.first())
					JOptionPane.showMessageDialog(parent, "The user " + user.getText() + "@localhost already exists.", "User Exists", JOptionPane.ERROR_MESSAGE);
				else
					stmt.executeUpdate("CREATE USER '" + user.getText() + "'@'localhost' IDENTIFIED BY '" + String.valueOf(pass.getPassword()) + "'");
				CreateUserDialog.this.dispose();
				JOptionPane.showMessageDialog(parent, "The user " + user.getText() + "@localhost has been created.", "User Creation Success", JOptionPane.ERROR_MESSAGE);
				userqtm.executeQuery();
			} catch (SQLException e1) {
				JOptionPane.showMessageDialog(parent, e1.getMessage(), "User Creation Failed", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
