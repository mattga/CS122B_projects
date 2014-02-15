import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JFrame;

import Helpers.QueryTableModel;


public class CreateUserActionListener implements ActionListener {
	private JFrame parent;
	private Connection con;
	private QueryTableModel userqtm;
	
	public CreateUserActionListener(JFrame parent, Connection con, QueryTableModel userqtm) {
		this.parent = parent;
		this.con = con;
		this.userqtm = userqtm;
	}
	
	public void actionPerformed (ActionEvent e) {
		new CreateUserDialog(parent, con, userqtm);
	}
}
