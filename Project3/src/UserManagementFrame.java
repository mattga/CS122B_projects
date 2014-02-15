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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;

import Helpers.PrivilegesTableModel;
import Helpers.PrivilegesTableModel.Resource;
import Helpers.QueryTable;
import Helpers.QueryTableModel;


public class UserManagementFrame extends JFrame {

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
	
	public UserManagementFrame(Connection con) {
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
		privPanel.add(new JLabel("To display privileges, pick a Host/User to the left,"));
		privPanel.add(new JLabel("and then a resource from the following:"));
		privPanel.add(resourceComboBox, BorderLayout.NORTH);
		privPanel.add(new JScrollPane(privTable));
		
		bodyPanel.add(privPanel);
		
		done = new JButton("Done");
		done.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UserManagementFrame.this.setVisible(false);
			}
		});
		
		addUser = new JButton("Add User");
		addUser.addActionListener(new CreateUserActionListener(this, con, userqtm));
		
		editPriv = new JButton("Edit Privileges");
		editPriv.addActionListener(new ModifyPrivilegesActionListener(this, con, userqt, privModel));
		
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
