package Helpers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

public class ResourceActionListener implements ActionListener {
	private PreparedStatement dbs, tables, procs;
	private ResultSet objectRS;
	private JComboBox<String> resObjectComboBox, resourceComboBox;

	public ResourceActionListener(PreparedStatement dbs, PreparedStatement tables, PreparedStatement procs, ResultSet objectRS, JComboBox<String> resourceComboBox, JComboBox<String> resObjectComboBox) {
		this.dbs = dbs;
		this.tables = tables;
		this.procs = procs;
		this.objectRS = objectRS;
		this.resObjectComboBox = resObjectComboBox;
		this.resourceComboBox = resourceComboBox;
	}

	public void actionPerformed (ActionEvent e) {
		resObjectComboBox.removeAllItems();

		try {
			switch(resourceComboBox.getSelectedIndex()) {
			case 0:
				objectRS = dbs.executeQuery();
				break;
			case 1:
				objectRS = tables.executeQuery();
				break;
			case 2:
				// Display from columns_priv
				return;
			case 3:
				objectRS = procs.executeQuery();
				break;
			}

			if(objectRS != null)
				for(objectRS.first();objectRS.isAfterLast();objectRS.next())
					((DefaultComboBoxModel<String>)resObjectComboBox.getModel()).addElement(objectRS.getString(1));
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
}
