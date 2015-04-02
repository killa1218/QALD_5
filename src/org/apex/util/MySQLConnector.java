package org.apex.util;

import java.sql.*;

public class MySQLConnector {
	private Connection con = null;
	private String url = "jdbc:mysql://127.0.0.1:3306";
	private String username = "root";
	private String password = "";
	
	public Statement connect(){
		Statement stmt = null;
		
		try {
	        Class.forName("com.mysql.jdbc.Driver");
	        System.out.println("MySQL Driver Loaded!"); 
	    } catch (ClassNotFoundException e) {
	        System.out.println("MySQL Driver Loading Failed!");
	        e.printStackTrace();
	    }//load driver
		
	    try {
	        con = DriverManager.getConnection(url, username, password);
	        stmt = con.createStatement();
	        System.out.println("MySQL Connected!"); 
	    } catch(SQLException e) {
	        System.out.println("MySQL Connection Error!"); 
	    }//connect
	    
		return stmt;		
	}
	
	public MySQLConnector server(String srv, String port){
		this.url = "jdbc:mysql://" + srv + ":" + port;
		
		return this;
	}//set server and port
	
	public MySQLConnector server(String srv){
		this.url = "jdbc:mysql://" + srv + ":3306";
		
		return this;
	}//set server
	
	public MySQLConnector username(String uname){
		this.username = uname;
		
		return this;
	}//set username
	
	public MySQLConnector password(String pwd){
		this.username = pwd;
		
		return this;
	}//set password
	

}
