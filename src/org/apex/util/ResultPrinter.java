package org.apex.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;

public class ResultPrinter {

	public ResultPrinter() {
		// TODO 自动生成的构造函数存根
	}

	public static void main(String[] args) throws IOException, SQLException {
		// TODO 自动生成的方法存根
		String fileName = "no-entity question regex match result.txt";
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("./data/" + fileName)));
		Connection con = new MySQLConnector().getConnection();
		ResultSet questions = con.createStatement().executeQuery("select distinct `question` from `patty`.`match_result` where 1");
		
		while(questions.next()){
			String question = questions.getString(1);
			bw.write(question + "\n");
//			System.out.println(questions.getMetaData());
			
			ResultSet ptn_rel = con.createStatement().executeQuery("select distinct `pattern`,`relation` from `patty`.`match_result` where `question` = \"" + question + "\"");
			
			while(ptn_rel.next()){
				bw.write("\t" + ptn_rel.getString(1) + "\t" + ptn_rel.getString(2) + "\n");
			}
			
			ptn_rel.close();
			
			System.out.println(question + "output finish.");
		}
		
		bw.close();
		questions.close();
	}

}
