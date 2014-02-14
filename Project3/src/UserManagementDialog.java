import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.CommunicationException;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import Helpers.QueryTable;
import Helpers.QueryTableModel;
import Helpers.ResourceActionListener;


public class UserManagementDialog extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private QueryTableModel userqtm;
	private QueryTable userqt;
	private JPanel buttonPanel, bodyPanel, privPanel;
	private JButton done;
	private JComboBox<String> resourceComboBox, resObjectComboBox;
	private DefaultComboBoxModel<String> resourceCBM, resObjectCBM;
	private ResultSet objectRS, privRS;
	private PreparedStatement tables, dbs, procs;
	
	public UserManagementDialog(Connection con) {
		bodyPanel = new JPanel(new GridLayout(1,2));
		
		buttonPanel = new JPanel();
		
		try {
			// Init PreparedStatements
			dbs = con.prepareStatement("SHOW DATABASES");
			tables = con.prepareStatement("SHOW TABLES");
			procs = con.prepareStatement("SHOW PROCEDURE STATUS");
			
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
		
		String[] resources = {"Databases", "Tables", "Columns", "Procedures"};
		resourceComboBox = new JComboBox<String>(resources);
		
		resObjectComboBox = new JComboBox<String>(new DefaultComboBoxModel<String>());
		resourceComboBox.addActionListener(new ResourceActionListener(dbs, tables, procs, objectRS, resourceComboBox, resourceComboBox));
		resObjectComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Display privileges
			}
		});
		
		privPanel = new JPanel();
		BoxLayout privLayout = new BoxLayout(privPanel, BoxLayout.Y_AXIS);
		privPanel.setLayout(privLayout);
		privPanel.add(new JLabel("Privileges:"));
		privPanel.add(resourceComboBox);
		privPanel.add(resObjectComboBox);
		
		bodyPanel.add(privPanel);
		
		done = new JButton("Done");
		done.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UserManagementDialog.this.setVisible(false);
			}
		});
		
		buttonPanel.add(done);
		
		this.add(bodyPanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
		this.setSize(800, 500);
		this.setVisible(true);
	}
}
