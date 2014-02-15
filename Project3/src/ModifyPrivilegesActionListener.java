import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import Helpers.PrivilegesTableModel;

public class ModifyPrivilegesActionListener implements ActionListener {
	private Connection con;
	private JFrame parent;
	private JTable userqt;
	private PrivilegesTableModel model;
	
	public ModifyPrivilegesActionListener(JFrame parent, Connection con, JTable userqt, PrivilegesTableModel model) {
		this.parent = parent;
		this.con = con;
		this.userqt = userqt;
		this.model = model;
	}

	public void actionPerformed (ActionEvent e) {
		if(userqt.getSelectedRow() < 0)
			JOptionPane.showMessageDialog(parent, "Must select a user to edit.", "No Row Selected", JOptionPane.ERROR_MESSAGE);
		else
			new ModifyPrivilegesDialog(parent, con, userqt.getSelectedRow(), model);
	}
}
