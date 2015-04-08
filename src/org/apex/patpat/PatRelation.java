package org.apex.patpat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import org.apex.util.MySQLConnector;

public class PatRelation {
	private String relation = null;
	protected static Connection con = new MySQLConnector().getConnection();
	
	public PatRelation(String rel) {
		// TODO 自动生成的构造函数存根
		relation = rel;
	}
	
	public String[] getExactPatterns(String kb) throws SQLException{
		ResultSet rs = null;
		LinkedList<String> list = new LinkedList<String>();
		if(kb.equals("yago")){
			rs = con.createStatement().executeQuery("select `relation` from `patty`.`yago_relation_paraphrases` where `relation`=\"" + relation + "\"");
		}
		else{
			rs = con.createStatement().executeQuery("select `relation` from `patty`.`dbpedia_relation_paraphrases` where `relation`=\"" + relation + "\"");
		}
		while(rs.next()){
			list.add(rs.getString(1));
		}
		
		return list.toArray(new String[0]);
	}
	
	public String[] getExactPatterns() throws SQLException{
		return getExactPatterns("dbpedia");
	}
	
//	public String[] getFuzzyPatterns(String kb) throws SQLException{
//		ResultSet rs = null;
//		String extPtn = getExactPatterns(kb);
//		String[] keyWords = new PatPattern(extPtn).getKeyWords(false);
//		String queryPtn = "\";$";
//		LinkedList<String> list = new LinkedList<String>();
//		
//		for(String str : keyWords){
//			queryPtn += "%" + str;
//		}
//		
//		queryPtn += "%;$\"";
//		
//		rs = con.createStatement().executeQuery("select `patterntext` from `wikipedia_patterns` where `patterntext` like " + queryPtn);
//		
//		while(rs.next()){
//			String patterns = rs.getString(1);
//			String[] ptns = patterns.split(";\\$");
//			
//			for(String ptn : ptns){
//				list.add(ptn);
//			}
//		}
//		
//		return list.toArray(new String[0]);
//	}

}
