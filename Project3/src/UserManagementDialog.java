import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.CommunicationException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;

import Helpers.PrivilegesTableModel;
import Helpers.PrivilegesTableModel.Resource;
import Helpers.QueryTable;
import Helpers.QueryTableModel;


public class UserManagementDialog extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private QueryTableModel userqtm;
	private QueryTable userqt;
	private PrivilegesTableModel privModel;
	private JTable privTable;
	private JPanel buttonPanel, bodyPanel, privPanel;
	private JButton done, addUser, editPriv;
	private JComboBox<String> resourceComboBox;
	
	public UserManagementDialog(Connection con) {
		bodyPanel = new JPanel(new GridLayout(1,2));
		
		buttonPanel = new JPanel();
		
		try {
			userqtm = new QueryTableModel(con, "SELECT Host,User from mysql.user");
			userqt = new QueryTable(userqtm);

			bodyPanel.add(new JScrollPane(userqt));
		} catch (CommunicationException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		privModel = new PrivilegesTableModel(con);
		privTable = new JTable(privModel);
		
		String[] resources = {"Databases", "Tables", "Columns", "Procedures"};
		resourceComboBox = new JComboBox<String>(resources);
		resourceComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					privModel.setResource(Resource.values()[resourceComboBox.getSelectedIndex()], userqt.getSelectedRow());
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		privPanel = new JPanel();
		privPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED), "Privileges"));
		privPanel.add(resourceComboBox, BorderLayout.NORTH);
		privPanel.add(new JScrollPane(privTable));
		
		bodyPanel.add(privPanel);
		
		done = new JButton("Done");
		done.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UserManagementDialog.this.setVisible(false);
			}
		});
		
		addUser = new JButton("Add User");
		
		editPriv = new JButton("Edit Privileges");
		editPriv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(userqt.getSelectedRow() < 0)
					JOptionPane.showMessageDialog(UserManagementDialog.this, "Must select a user to edit.", "No Row Selected", JOptionPane.ERROR_MESSAGE);
				else
					;// init create user dialog
			}
		});
		
		buttonPanel.add(addUser);
		buttonPanel.add(editPriv);
		buttonPanel.add(done);
		
		this.setTitle("User Management");
		this.add(bodyPanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
		this.setSize(950, 550);
		this.setVisible(true);
	}
}
