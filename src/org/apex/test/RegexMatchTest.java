package org.apex.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;

import org.apex.patpat.PatPattern;
import org.apex.util.MySQLConnector;

public class RegexMatchTest {

	public static void main(String[] args) throws SQLException, IOException {
		// TODO 自动生成的方法存根
		PatPattern[] ptnarr = null;
		HashSet<String> ptnSet = new HashSet<String>();
		Statement stmt = new MySQLConnector().getStatement();
		ResultSet rs = stmt.executeQuery("select `patterntext` from `patty`.`wikipedia_patterns` where 1");
		
		while(rs.next()){
			String ptnset = rs.getString(1);
			String[] ptnsetArr = ptnset.split(";\\$");
			for(String ptn : ptnsetArr){
				ptnSet.add(ptn);
			}
		}
		
		System.out.println("Pattern Number:" + ptnSet.size());
		
		ptnarr = new PatPattern[ptnSet.size()];
		
		int curid=0;
		for (String s:ptnSet){
			ptnarr[curid++] = new PatPattern(s);
		}
		
		System.out.println("PatPattern Array built.");
		
		BufferedReader br = new BufferedReader(new FileReader(new File("./data/questions_splitted_200.txt")));
		String question = null;

		do{
			question = br.readLine();
//			System.out.println(question);
			if(question != null){
				new PatRegMatchThread(question, question, ptnarr.clone()).start();
			}
//			break;
		}while(question != null);
		
		br.close();
	}

}
