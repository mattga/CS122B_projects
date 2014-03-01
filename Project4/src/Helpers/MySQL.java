package Helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class MySQL {
	private static PreparedStatement selGenre, selPerson, selBooktitle, selPublisher;
	private static PreparedStatement insDocument, insGenre, insPerson, insBooktitle, insPublisher, insAuthorMapping;
	private static MySQL _this;

	private Connection con;
	
	public MySQL() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con  = DriverManager.getConnection("jdbc:mysql://localhost/documentdb?user=testuser&password=testpass");
			con.setAutoCommit(false);
			
			// Prepare queries
			selGenre = con.prepareStatement("SELECT id FROM tbl_genres WHERE genre_name=?");
			insGenre = con.prepareStatement("INSERT IGNORE INTO tbl_genres (genre_name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);

			selPerson = con.prepareStatement("SELECT id FROM tbl_people WHERE name=?");
			insPerson = con.prepareStatement("INSERT IGNORE INTO tbl_people (name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);

			selBooktitle = con.prepareStatement("SELECT id FROM tbl_booktitle WHERE title=?");
			insBooktitle = con.prepareStatement("INSERT IGNORE INTO tbl_booktitle (title) VALUES (?)", Statement.RETURN_GENERATED_KEYS);

			selPublisher = con.prepareStatement("SELECT id FROM tbl_publisher WHERE publisher_name=?");
			insPublisher = con.prepareStatement("INSERT IGNORE INTO tbl_publisher (publisher_name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);

			insDocument = con.prepareStatement("INSERT IGNORE INTO tbl_dblp_document (title, start_page, end_page, year, volume, number, url," +
					"ee, cdrom, cite, crossref, isbn, series, editor_id, genre_id, booktitle_id, publisher_id) VALUES " +
					"(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

			insAuthorMapping = con.prepareStatement("INSERT IGNORE INTO tbl_author_document_mapping (doc_id, author_id) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);

			con.createStatement().execute("ALTER TABLE `tbl_genres` DISABLE KEYS;");
			con.createStatement().execute("ALTER TABLE `tbl_people` DISABLE KEYS;");
			con.createStatement().execute("ALTER TABLE `tbl_booktitle` DISABLE KEYS;");
			con.createStatement().execute("ALTER TABLE `tbl_publisher` DISABLE KEYS;");
			con.createStatement().execute("ALTER TABLE `tbl_dblp_document` DISABLE KEYS;");
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() {
		if(_this == null)
			_this = new MySQL();
		return _this.con;
	}
	
	public static void commitAll() {
		if(_this == null)
			_this = new MySQL();
		
		try {
			_this.con.createStatement().execute("ALTER TABLE `tbl_genres` ENABLE KEYS;");
			_this.con.createStatement().execute("ALTER TABLE `tbl_people` ENABLE KEYS;");
			_this.con.createStatement().execute("ALTER TABLE `tbl_booktitle` ENABLE KEYS;");
			_this.con.createStatement().execute("ALTER TABLE `tbl_publisher` ENABLE KEYS;");
			_this.con.createStatement().execute("ALTER TABLE `tbl_dblp_document` ENABLE KEYS;");
			_this.con.commit();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static int insertGenre(String genre_name) throws SQLException {
		if(_this == null)
			_this = new MySQL();
		
		// Return genre id if exists
		selGenre.setString(1, genre_name);
		selGenre.execute();
		ResultSet key = selGenre.getResultSet();
		if(key.first())
			return key.getInt(1);

		// Insert genre if not
		insGenre.setString(1, genre_name);
		insGenre.execute();
		key = insGenre.getGeneratedKeys();
		key.first();
		return key.getInt(1);
	}

	public static int insertEditor(String editor_name) throws SQLException {
		if(_this == null)
			_this = new MySQL();
		
		// Return editor id if exists
		selPerson.setString(1, editor_name);
		selPerson.execute();
		ResultSet key = selPerson.getResultSet();
		if(key.first())
			return key.getInt(1);

		// Insert editor if not
		insPerson.setString(1, editor_name);
		insPerson.execute();
		key = insPerson.getGeneratedKeys();
		key.first();
		return key.getInt(1);
	}

	public static int insertBooktitle(String booktitle) throws SQLException {
		if(_this == null)
			_this = new MySQL();
		
		// Return booktitle id if exists
		selBooktitle.setString(1, booktitle);
		selBooktitle.execute();
		ResultSet key = selBooktitle.getResultSet();
		if(key.first())
			return key.getInt(1);

		// Insert booktitle if not
		insBooktitle.setString(1, booktitle);
		insBooktitle.execute();
		key = insBooktitle.getGeneratedKeys();
		key.first();
		return key.getInt(1);
	}

	public static int insertPublisher(String publisher_name) throws SQLException {
		if(_this == null)
			_this = new MySQL();
		
		// Return publisher id if exists
		selPublisher.setString(1, publisher_name);
		selPublisher.execute();
		ResultSet key = selPublisher.getResultSet();
		if(key.first())
			return key.getInt(1);

		// Insert publisher if not
		insPublisher.setString(1, publisher_name);
		insPublisher.execute();
		key = insPublisher.getGeneratedKeys();
		key.first();
		return key.getInt(1);
	}

	public static int insertAuthor(String author_name) throws SQLException {
		if(_this == null)
			_this = new MySQL();
		
		// Return editor id if exists
		selPerson.setString(1, author_name);
		selPerson.execute();
		ResultSet key = selPerson.getResultSet();
		if(key.first())
			return key.getInt(1);

		// Insert editor if not
		insPerson.setString(1, author_name);
		insPerson.execute();
		key = insPerson.getGeneratedKeys();
		key.first();
		return key.getInt(1);
	}

	public static int insertDocument(String title, int start_page, int end_page, int year, int volume, int number, String url, 
			String ee, String cdrom, String cite, String crossref, String isbn, String series, String editor_id, 
			String genre_id, String booktitle_id, String publisher_id) throws SQLException {
		if(_this == null)
			_this = new MySQL();
		
		// Insert document
		insDocument.setString(1, title);
		insDocument.setInt(2, start_page);
		insDocument.setInt(3, end_page);
		insDocument.setInt(4, year);
		insDocument.setInt(5, volume);
		insDocument.setInt(6, number);
		insDocument.setString(7, url);
		insDocument.setString(8, ee);
		insDocument.setString(9, cdrom);
		insDocument.setString(10, cite);
		insDocument.setString(11, crossref);
		insDocument.setString(12, isbn);
		insDocument.setString(13, series);
		insDocument.setString(14, editor_id);
		insDocument.setString(15, genre_id);
		insDocument.setString(16, booktitle_id);
		insDocument.setString(17, publisher_id);
		
		insDocument.execute();
		ResultSet key = insDocument.getGeneratedKeys();
		key.first();
		return key.getInt(1);
	}
	
	public static int insertAuthorMapping(int author_id, int document_id) throws SQLException {
		if(_this == null)
			_this = new MySQL();
		
		insAuthorMapping.setInt(1, document_id);
		insAuthorMapping.setInt(2, author_id);
		
		insAuthorMapping.execute();
		ResultSet key = insAuthorMapping.getGeneratedKeys();
		key.first();
		return key.getInt(1);
	}
}
