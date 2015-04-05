package org.apex.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBBuilder {
	private static Statement stmt = null;
	private static String filepath = "rsc/patty-dataset/";
	
	private static boolean existDB(String dbName){
		if(stmt == null){
			System.out.println("Statement Not Inited");
			return false;
		}
		else{
			try {
				ResultSet rs = stmt.executeQuery("show databases");
				while (rs.next()){
					if(rs.getString(1).equals(dbName)){
						return true;
					}
	            }
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
			return false;
		}
	}

	public static void main(String[] args) throws IOException {
		File d = new File("./" + filepath);
		String[] fNameList = null;
		
		if(d.isDirectory()){
			if(d.canRead()){
				fNameList = d.list();
			}
			else{
				System.out.println(filepath + " can not read!");
			}
		}
		else{
			System.out.println(filepath + " is not a directory!");
		}
		
		stmt = new MySQLConnector().getStatement();
		
		if(!existDB("patty")){
			try {
				stmt.execute("create database patty");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		try {
			stmt.execute("use patty");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		for(int i = 0 ; i < fNameList.length ; i ++){
			String name = fNameList[i].replace(".txt", "");
			String tableName = name.replace("-", "_");
			
			if(!name.matches("README")){
				File f = new File(d.getPath() + "/" + name + ".txt");
				@SuppressWarnings("resource")
				BufferedReader br = new BufferedReader(new FileReader(f));
				String schema = br.readLine();
				String[] fields = schema.split("\t");
				String fld = "";
				
				for(int j = 0 ; j < fields.length ; j ++){
					switch(fields[j].trim()){
						case "patternid":
							fld += "patternid int(32)";
							break;
						case "superpattern":
							fld += "superpattern int(32)";
							break;
						case "subpattern":
							fld += "subpattern int(32)";
							break;
						case "confidence":
							fld += "confidence float(32)";
							break;
						case "range":
							fld += "range_ char(200)";
							break;
						case "patterntext":
							fld += "patterntext text";
							break;
						default:
							fld += fields[j].trim() + " char(200)";
							break;
					}
					if(j != fields.length - 1){
						fld += ",";
					}
				}
				
				String createTable = "create table if not exists " + tableName + " (" + fld + ")";
				
				try {
					stmt.execute(createTable);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				String data = br.readLine();
				int count = 0;
				String sql = "insert into " + tableName + " values ";
				
				while(data != null && !data.trim().equals("")){
					count ++;
					sql += "(\"" + data.trim().replace("\"", "\\\"").replace("\t", "\",\"") + "\")";
					
					if(count > 4999){
						try {
							int num = stmt.executeUpdate(sql);
							if(num != 0){
								System.out.println("insert " + num + " data into " + tableName);
							}
						} catch (SQLException e) {
							System.out.println(sql);
							e.printStackTrace();
							break;
						}
						count = 0;
						sql = "insert into " + tableName + " values ";
					}
					
					data = br.readLine();
					
					if(count > 0 && data != null && !data.equals("")){
						sql += ",";
					}
				}
				try {
					int num = stmt.executeUpdate(sql);
					if(num != 0){
						System.out.println("insert " + num + " data into " + tableName);
					}
				} catch (SQLException e) {
					System.out.println(sql);
					e.printStackTrace();
				}
				
			}
		}
		
	}

}
