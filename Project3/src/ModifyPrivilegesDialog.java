import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import Helpers.PrivilegesTableModel;

public class ModifyPrivilegesDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton grant, revoke, close;
	private JComboBox<String> tableComboBox;
	private JTabbedPane privilegeTabPane;
	private JPanel databasePanel, dbPanel1, dbPanel2, tablePanel, tablePanel1, tablePanel2, columnPanel, colPanel1, colPanel2, procedurePanel, procPanel1, procPanel2, buttonPanel;
	private List<JCheckBox> dbNameList, dbPrivList, tableNameList, tablePrivList, colNameList, colPrivList, procDBList, procPrivList;
	private ResultSet rs;
	private Statement stmt;
	private String user, host;
	private PrivilegesTableModel model;
	
	public ModifyPrivilegesDialog(JFrame parent, Connection con, int row, PrivilegesTableModel model) {
		this.model = model;
		
		try {
			stmt = con.createStatement();

			rs = stmt.executeQuery("SELECT user,host FROM mysql.user");
			rs.absolute(row+1);

			this.user = rs.getString("user");
			this.host = rs.getString("host");

			this.setTitle("Modify Privileges: " + user + "@" + host);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		rs = null;

		buildDatabasePanel();

		buildTablePanel();

		buildColumnPanel();

		buildProcedurePanel();

		privilegeTabPane = new JTabbedPane();
		privilegeTabPane.addTab("Database", databasePanel);
		privilegeTabPane.addTab("Tables", tablePanel);
		privilegeTabPane.addTab("Columns", columnPanel);
		privilegeTabPane.addTab("Procedures", procedurePanel);

		grant = new JButton("Grant Selected");
		grant.addActionListener(new GrantPrivilegesActionListener());

		revoke = new JButton("Revoke Selected");
		revoke.addActionListener(new RevokePrivilegesActionListener());

		close = new JButton("Close");
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ModifyPrivilegesDialog.this.dispose();
			}
		});

		buttonPanel = new JPanel();
		buttonPanel.add(grant);
		buttonPanel.add(revoke);
		buttonPanel.add(close);

		this.add(privilegeTabPane, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setSize(500,500);
		this.setVisible(true);
	}

	private void buildDatabasePanel() {
		dbPanel1 = new JPanel();
		dbPanel2 = new JPanel();
		BoxLayout dbLayout1 = new BoxLayout(dbPanel1, BoxLayout.Y_AXIS);
		BoxLayout dbLayout2 = new BoxLayout(dbPanel2, BoxLayout.Y_AXIS);
		dbPanel1.setLayout(dbLayout1);
		dbPanel2.setLayout(dbLayout2);

		try {
			rs = stmt.executeQuery("SHOW DATABASES");

			dbNameList = new ArrayList<JCheckBox>();
			JCheckBox dbCheckBox = new JCheckBox("All");
			dbPanel1.add(dbCheckBox);
			dbNameList.add(dbCheckBox);
			if(rs != null && rs.first())
				for(;!rs.isAfterLast();rs.next()) {
					if(!rs.getString(1).equals("information_schema") && !rs.getString(1).equals("performance_schema")) {
						dbCheckBox = new JCheckBox(rs.getString(1));
						dbPanel1.add(dbCheckBox);
						dbNameList.add(dbCheckBox);
					}
				}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbPrivList = new ArrayList<JCheckBox>();
		JCheckBox priv = new JCheckBox("Select");
		dbPanel2.add(priv);
		dbPrivList.add(priv);
		priv = new JCheckBox("Insert");
		dbPanel2.add(priv);
		dbPrivList.add(priv);
		priv = new JCheckBox("Update");
		dbPanel2.add(priv);
		dbPrivList.add(priv);
		priv = new JCheckBox("Delete");
		dbPanel2.add(priv);
		dbPrivList.add(priv);
		priv = new JCheckBox("Create");
		dbPanel2.add(priv);
		dbPrivList.add(priv);
		priv = new JCheckBox("Drop");
		dbPanel2.add(priv);
		dbPrivList.add(priv);
		priv = new JCheckBox("References");
		dbPanel2.add(priv);
		dbPrivList.add(priv);
		priv = new JCheckBox("Index");
		dbPanel2.add(priv);
		dbPrivList.add(priv);
		priv = new JCheckBox("Alter");
		dbPanel2.add(priv);
		dbPrivList.add(priv);
		priv = new JCheckBox("Lock_tables");
		dbPanel2.add(priv);
		dbPrivList.add(priv);
		priv = new JCheckBox("Create_view");
		dbPanel2.add(priv);
		dbPrivList.add(priv);
		priv = new JCheckBox("Show_view");
		dbPanel2.add(priv);
		dbPrivList.add(priv);
		priv = new JCheckBox("Event");
		dbPanel2.add(priv);
		dbPrivList.add(priv);
		priv = new JCheckBox("Trigger");
		dbPanel2.add(priv);
		dbPrivList.add(priv);

		databasePanel = new JPanel(new GridLayout(1, 2));
		databasePanel.add(new JScrollPane(dbPanel1));
		databasePanel.add(new JScrollPane(dbPanel2));
	}

	private void buildTablePanel() {
		tablePanel1 = new JPanel();
		tablePanel2 = new JPanel();
		BoxLayout tableLayout1 = new BoxLayout(tablePanel1, BoxLayout.Y_AXIS);
		BoxLayout tableLayout2 = new BoxLayout(tablePanel2, BoxLayout.Y_AXIS);
		tablePanel1.setLayout(tableLayout1);
		tablePanel2.setLayout(tableLayout2);

		try {
			rs = stmt.executeQuery("SELECT TABLE_SCHEMA, TABLE_NAME FROM information_schema.TABLES WHERE TABLE_SCHEMA != 'information_schema' AND TABLE_SCHEMA != 'performance_schema'");

			JCheckBox tableCheckBox = null;
			tableNameList = new ArrayList<JCheckBox>();
			if(rs != null && rs.first())
				for(;!rs.isAfterLast();rs.next()) {
					tableCheckBox = new JCheckBox(rs.getString(1) + "." + rs.getString(2));
					tablePanel1.add(tableCheckBox);
					tableNameList.add(tableCheckBox);
				}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		tablePrivList = new ArrayList<JCheckBox>();
		JCheckBox priv = new JCheckBox("Select");
		tablePanel2.add(priv);
		tablePrivList.add(priv);
		priv = new JCheckBox("Insert");
		tablePanel2.add(priv);
		tablePrivList.add(priv);
		priv = new JCheckBox("Update");
		tablePanel2.add(priv);
		tablePrivList.add(priv);
		priv = new JCheckBox("Delete");
		tablePanel2.add(priv);
		tablePrivList.add(priv);
		priv = new JCheckBox("Create");
		tablePanel2.add(priv);
		tablePrivList.add(priv);
		priv = new JCheckBox("Drop");
		tablePanel2.add(priv);
		tablePrivList.add(priv);
		priv = new JCheckBox("References");
		tablePanel2.add(priv);
		tablePrivList.add(priv);
		priv = new JCheckBox("Index");
		tablePanel2.add(priv);
		tablePrivList.add(priv);
		priv = new JCheckBox("Alter");
		tablePanel2.add(priv);
		tablePrivList.add(priv);
		priv = new JCheckBox("Create_view");
		tablePanel2.add(priv);
		tablePrivList.add(priv);
		priv = new JCheckBox("Show_view");
		tablePanel2.add(priv);
		tablePrivList.add(priv);
		priv = new JCheckBox("Trigger");
		tablePanel2.add(priv);
		tablePrivList.add(priv);

		tablePanel = new JPanel(new GridLayout(1, 2));
		tablePanel.add(new JScrollPane(tablePanel1));
		tablePanel.add(new JScrollPane(tablePanel2));
	}

	private void buildColumnPanel() {
		colPanel1 = new JPanel();
		colPanel2 = new JPanel();
		BoxLayout colLayout1 = new BoxLayout(colPanel1, BoxLayout.Y_AXIS);
		BoxLayout colLayout2 = new BoxLayout(colPanel2, BoxLayout.Y_AXIS);
		colPanel1.setLayout(colLayout1);
		colPanel2.setLayout(colLayout2);

		tableComboBox = new JComboBox<String>();
		try {
			rs = stmt.executeQuery("SELECT TABLE_SCHEMA, TABLE_NAME FROM information_schema.TABLES WHERE TABLE_SCHEMA != 'information_schema' AND TABLE_SCHEMA != 'performance_schema'");

			if(rs != null && rs.first())
				for(;!rs.isAfterLast();rs.next())
					tableComboBox.addItem(rs.getString(1) + "." + rs.getString(2));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		tableComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				colPanel1.removeAll();
				try {
					rs = stmt.executeQuery("DESCRIBE " + (String)tableComboBox.getSelectedItem());

					colNameList = new ArrayList<JCheckBox>();
					JCheckBox colCheckBox = null;
					if(rs != null && rs.first())
						for(;!rs.isAfterLast();rs.next()) {
							colCheckBox = new JCheckBox(rs.getString(1));
							colPanel1.add(colCheckBox);
							colNameList.add(colCheckBox);
						}
					colPanel1.repaint();
					colPanel1.revalidate();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});

		colPrivList = new ArrayList<JCheckBox>();
		JCheckBox priv = new JCheckBox("Select");
		colPanel2.add(priv);
		colPrivList.add(priv);
		priv = new JCheckBox("Insert");
		colPanel2.add(priv);
		colPrivList.add(priv);
		priv = new JCheckBox("Update");
		colPanel2.add(priv);
		colPrivList.add(priv);
		priv = new JCheckBox("References");
		colPanel2.add(priv);
		colPrivList.add(priv);

		JPanel anotherDamnPanel = new JPanel(new GridLayout(1, 2));
		anotherDamnPanel.add(new JScrollPane(colPanel1));
		anotherDamnPanel.add(new JScrollPane(colPanel2));
		columnPanel = new JPanel();
		columnPanel.add(tableComboBox, BorderLayout.NORTH);
		columnPanel.add(anotherDamnPanel, BorderLayout.CENTER);
	}

	private void buildProcedurePanel() {
		procPanel1 = new JPanel();
		procPanel2 = new JPanel();
		BoxLayout procLayout1 = new BoxLayout(procPanel1, BoxLayout.Y_AXIS);
		BoxLayout procLayout2 = new BoxLayout(procPanel2, BoxLayout.Y_AXIS);
		procPanel1.setLayout(procLayout1);
		procPanel2.setLayout(procLayout2);

		try {
			rs = stmt.executeQuery("SHOW DATABASES");

			procDBList = new ArrayList<JCheckBox>();
			JCheckBox dbCheckBox = new JCheckBox("All");
			procPanel1.add(dbCheckBox);
			procDBList.add(dbCheckBox);
			if(rs != null && rs.first())
				for(;!rs.isAfterLast();rs.next()) {
					if(!rs.getString(1).equals("information_schema") && !rs.getString(1).equals("performance_schema")) {
						dbCheckBox = new JCheckBox(rs.getString(1));
						procPanel1.add(dbCheckBox);
						procDBList.add(dbCheckBox);
					}
				}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		procPrivList = new ArrayList<JCheckBox>();
		JCheckBox priv = new JCheckBox("Create_routine");
		procPanel2.add(priv);
		procPrivList.add(priv);
		priv = new JCheckBox("Alter_routine");
		procPanel2.add(priv);
		procPrivList.add(priv);
		priv = new JCheckBox("Execute");
		procPanel2.add(priv);
		procPrivList.add(priv);

		procedurePanel = new JPanel(new GridLayout(1, 2));
		procedurePanel.add(new JScrollPane(procPanel1));
		procedurePanel.add(new JScrollPane(procPanel2));
	}

	private class GrantPrivilegesActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				// DB Privileges
				if(dbNameList.get(0).getText().equals("All") && dbNameList.get(0).isSelected())
					for(JCheckBox b2 : dbPrivList) {
						if(b2.isSelected())
							stmt.executeUpdate("GRANT " + b2.getText().replace('_', ' ').toUpperCase() + " ON *.* TO " + user + "@" + host);
					}
				else
					for(JCheckBox b : dbNameList) {
						if(b.isSelected())
							for(JCheckBox b2 : dbPrivList) {
								if(b2.isSelected())
									stmt.executeUpdate("GRANT " + b2.getText().replace('_', ' ').toUpperCase() + " ON " + b.getText() + ".* TO " + user + "@" + host);
							}
					}

				// Table Privileges
				for(JCheckBox b : tableNameList) {
					if(b.isSelected())
						for(JCheckBox b2 : tablePrivList) {
							if(b2.isSelected())
								stmt.executeUpdate("GRANT " + b2.getText().replace('_', ' ').toUpperCase() + " ON " + b.getText() + " TO " + user + "@" + host);
						}
				}

				// Col Privileges
				for(JCheckBox b2 : colPrivList) {
					if(b2.isSelected()) {
						boolean firstcol = true;
						String query = "GRANT " + b2.getText().replace('_', ' ').toUpperCase() + " (";
						for(JCheckBox b : colNameList)
							if(b.isSelected())
								if(firstcol) {
									query += b.getText();
									firstcol = false;
								}
								else
									query += "," + b.getText();
						query += ") ON " + (String)tableComboBox.getSelectedItem() + " TO " + user + "@" + host;
						stmt.execute(query);
					}
				}

				// Proc Privileges
				if(procDBList.get(0).getText().equals("All") && procDBList.get(0).isSelected())
					for(JCheckBox b2 : procDBList) {
						if(b2.isSelected())
							stmt.executeUpdate("GRANT " + b2.getText().replace('_', ' ').toUpperCase() + " ON *.* TO " + user + "@" + host);
					}
				else
					for(JCheckBox b : procDBList) {
						if(b.isSelected())
							for(JCheckBox b2 : procPrivList) {
								if(b2.isSelected())
									stmt.executeUpdate("GRANT " + b2.getText().replace('_', ' ').toUpperCase() + " ON " + b.getText() + ".* TO " + user + "@" + host);
							}
					}

				stmt.executeUpdate("FLUSH PRIVILEGES");
				model.fireTableStructureChanged();
			} catch(SQLException e2) {
				JOptionPane.showMessageDialog(ModifyPrivilegesDialog.this, "MovieDB login must have root access in order to Grant or Revoke privileges.", "No Root Access", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private class RevokePrivilegesActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				// DB Privileges
				ResultSet privcheck = null;
				if(dbNameList.get(0).getText().equals("All") && dbNameList.get(0).isSelected())
					for(JCheckBox b2 : dbPrivList) {
						if(b2.isSelected()) {
							System.out.println("SELECT * FROM mysql.user WHERE user='" + user + "' AND host='" + host + "'");
							privcheck = stmt.executeQuery("SELECT * FROM mysql.user WHERE user='" + user + "' AND host='" + host + "'");
							if(privcheck.first() && privcheck.getString(b2.getText() + "_priv").equals("Y"))
								stmt.executeUpdate("REVOKE " + b2.getText().replace('_', ' ').toUpperCase() + " ON *.* FROM " + user + "@" + host);
						}
					}
				else
					for(JCheckBox b : dbNameList) {
						if(b.isSelected())
							for(JCheckBox b2 : dbPrivList) {
								if(b2.isSelected()) {
									privcheck = stmt.executeQuery("SELECT * FROM mysql.db WHERE user='" + user + "' AND host='" + host + "' AND Db='" + b.getText() + "'");
									if(privcheck.first() && privcheck.getString(b2.getText() + "_priv").equals("Y"))
										stmt.executeUpdate("REVOKE " + b2.getText().replace('_', ' ').toUpperCase() + " ON " + b.getText() + ".* FROM " + user + "@" + host);
								}
							}
					}

				// Table Privileges
				for(JCheckBox b : tableNameList) {
					if(b.isSelected())
						for(JCheckBox b2 : tablePrivList) {
							if(b2.isSelected()) {
								privcheck = stmt.executeQuery("SELECT * FROM mysql.tables_priv WHERE user='" + user + "' AND host='" + host + "' AND Db='" + b.getText().split(".")[0] + "' AND Table_name='" + b.getText().split(".")[1] + "'");
								if(privcheck.first() && privcheck.getString("Table_priv").contains(b2.getText()) )
									stmt.executeUpdate("REVOKE " + b2.getText().replace('_', ' ').toUpperCase() + " ON " + b.getText() + " FROM " + user + "@" + host);
							}
						}
				}

				// Col Privileges
				for(JCheckBox b2 : colPrivList) {
					if(b2.isSelected()) {
						boolean firstcol = true;
						String query1 = "REVOKE " + b2.getText().replace('_', ' ').toUpperCase() + " (";
						String query2 = "";
						for(JCheckBox b : colNameList)
							if(b.isSelected())
								if(firstcol) {
									query2 += b.getText();
									firstcol = false;
								}
								else
									query2 += "," + b.getText();
						String query3 = ") ON " + (String)tableComboBox.getSelectedItem() + " FROM " + user + "@" + host;

						privcheck = stmt.executeQuery("SELECT * FROM mysql.tables_priv WHERE user='" + user + "' AND host='" + host + "' AND Db='" + ((String)tableComboBox.getSelectedItem()).split(".")[0] + "' AND Table_name='" + ((String)tableComboBox.getSelectedItem()).split(".")[1] + "'");
						if(privcheck.first() )
							stmt.execute(query1 + query2 + query3);
					}
				}

				// Proc Privileges
				if(procDBList.get(0).getText().equals("All") && procDBList.get(0).isSelected())
					for(JCheckBox b2 : procDBList) {
						if(b2.isSelected())
							stmt.executeUpdate("REVOKE " + b2.getText().replace('_', ' ').toUpperCase() + " ON *.* FROM " + user + "@" + host);
					}
				else
					for(JCheckBox b : procDBList) {
						if(b.isSelected())
							for(JCheckBox b2 : procPrivList) {
								if(b2.isSelected())
									privcheck = stmt.executeQuery("SELECT * FROM mysql.db WHERE user='" + user + "' AND host='" + host + "' AND Db='" + b.getText() + "'");
									if(privcheck.first() && privcheck.getString(b2.getText() + "_priv").equals("Y"))
										stmt.executeUpdate("REVOKE " + b2.getText().replace('_', ' ').toUpperCase() + " ON " + b.getText() + ".* FROM " + user + "@" + host);
							}
					}
				
				model.fireTableStructureChanged();
			} catch(SQLException e2) {
				JOptionPane.showMessageDialog(ModifyPrivilegesDialog.this, "MovieDB login must have root access in order to Grant or Revoke privileges.", "No Root Access", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}