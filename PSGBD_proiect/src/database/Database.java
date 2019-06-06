package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

	private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String USER = "student";
    private static final String PASSWORD = "34dh34";
    private static Connection connection = null;
    private Database() { }
    public static Connection getConnection() throws SQLException {
        if (connection == null) {
            createConnection();
        }
        return connection;
    }
    //Implement the method createConnection()
	private static void createConnection() throws SQLException {
		try {
		 Class.forName("oracle.jdbc.OracleDriver");
		 connection = DriverManager.getConnection(
		 URL, USER, PASSWORD);
		} catch(ClassNotFoundException e) {
		 System.err.print("ClassNotFoundException: " + e) ;
		} catch(SQLException e) {
		 System.err.println("SQLException: " + e);
		}
	}
    //Implement the method closeConnection()
	public static void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
    //Implement the method commit()
	public static void commit() {
		
		try {
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
    //Implement the method rollback()  
	public static void rollback() throws SQLException {
		
		connection.rollback();
	}
	
	
}
