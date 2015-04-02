package org.apex.patpat;


import java.sql.SQLException;
import java.sql.Statement;

import org.apex.util.MySQLConnector;

public abstract class PatMatchThread extends Thread {
	private boolean matchAllPatterns = false;
	private String stc = null;
	private PatPattern ptn = null;
	private PatPattern[] ptnarr = null;
	public static Statement stmt = new MySQLConnector().connect();

	public PatMatchThread(String threadName, String stc){
		super(threadName);
		this.stc = stc;
		this.matchAllPatterns = true;
	}
	
	public PatMatchThread(String threadName, String stc, PatPattern ptn) {
		super(threadName);
		this.stc = stc;
		this.ptn = ptn;
	}
	
	public PatMatchThread(String threadName, String stc, PatPattern[] ptnarr) {
		super(threadName);
		this.matchAllPatterns = true;
		this.ptnarr = ptnarr;
		this.stc = stc;
	}
	
	protected abstract char isMatched(String stc, PatPattern ptn);
	//'y': match all ok. 'n': not match at all. 'u': undeterminded.
	
	protected void matchPattern() throws SQLException{
		StringBuilder data = new StringBuilder();
		char state = isMatched(stc, ptn);
		
		if(state == 'y'){
			String[] relationArr = ptn.getRelations();
			System.out.println("relation: " + relationArr.length);
			for(String relation : relationArr){
				String[] domainArr = ptn.getDomains();
				System.out.println("domain: " + domainArr.length);
				for(String domain : domainArr){
					String[] rangeArr = ptn.getRanges();
					System.out.println("range: " + rangeArr.length);
					for(String range : rangeArr){
						if(data.length() != 0)data.append(',');
						data.append("(\"").append(stc).append("\",\"").append(ptn.toString()).append("\",\"").append(relation).append("\",\"").append(domain).append("\",\"").append(range).append("\",").append("1)");
					}
				}
			}
			System.out.println(stc + " : " + ptn.toString());
		}
		else if(state == 'u'){
			for(String relation : ptn.getRelations()){
				for(String domain : ptn.getDomains()){
					for(String range : ptn.getRanges()){
						if(data.length() != 0)data.append(',');
						data.append("(\"").append(stc).append("\",\"").append(ptn.toString()).append("\",\"").append(relation).append("\",\"").append(domain).append("\",\"").append(range).append("\",").append("1)");
					}
				}
			}
			System.out.println(stc + " : " + ptn.toString());
		}
//		if(state == 'y'){
//			for(String relation : ptn.getRelations()){
//				for(String domain : ptn.getDomains()){
//					for(String range : ptn.getRanges()){
//						if(data.length() != 0)data += ",";
//						data += "(" + stc + "," + ptn.toString() + "," + relation + "," + domain + "," + range + "," + "1)";
//					}
//				}
//			}
//			System.out.println(stc + " : " + ptn.toString());
//		}
//		else if(state == 'u'){
//			for(String relation : ptn.getRelations()){
//				for(String domain : ptn.getDomains()){
//					for(String range : ptn.getRanges()){
//						if(data.length() != 0)data += ",";
//						data += "(" + stc + "," + ptn.toString() + "," + relation + "," + domain + "," + range + "," + "0)";
//					}
//				}
//			}
//			System.out.println(stc + " : " + ptn.toString());
//		}
	}
	
	protected void matchPatterns() throws SQLException{
		int writeCount = 0;
		StringBuilder data = new StringBuilder();
		
		for(PatPattern ptn : ptnarr){
			char state = isMatched(stc, ptn);
			
			if(state == 'y'){
				String[] relationArr = ptn.getRelations();
				String[] domainArr = ptn.getDomains();
				String[] rangeArr = ptn.getRanges();
				
				for(String relation : relationArr){
					for(String domain : domainArr){
						for(String range : rangeArr){
//							System.out.println("nihao");
							if(data.length() != 0)data.append(',');
							data.append("(\"").append(stc).append("\",\"").append(ptn.toString()).append("\",\"").append(relation).append("\",\"").append(domain).append("\",\"").append(range).append("\",").append("1)");
							writeCount ++;
							if(writeCount != 0 && writeCount % 1000 == 0){
//								System.out.println(writeCount);
								//Write data for every 1000 items
//								System.out.println(new StringBuilder().append("insert into `patty`.`match_result` values ").append(data).toString());
								stmt.executeUpdate(new StringBuilder().append("insert into `patty`.`match_result` values ").append(data).toString());
								data = new StringBuilder();
							}
						}
					}
				}
				System.out.println(stc + " : " + ptn.toString());
			}
			else if(state == 'u'){
				for(String relation : ptn.getRelations()){
					for(String domain : ptn.getDomains()){
						for(String range : ptn.getRanges()){
							if(data.length() != 0)data.append(',');
							data.append("(\"").append(stc).append("\",\"").append(ptn.toString()).append("\",\"").append(relation).append("\",\"").append(domain).append("\",\"").append(range).append("\",").append("1)");
							writeCount ++;
						}
					}
				}
				System.out.println(stc + " : " + ptn.toString());
			}
			
		}
		
//		System.out.println(new StringBuilder().append("insert into `patty`.`match_result` values ").append(data).toString());

		if(data.length() != 0){
			stmt.executeUpdate(new StringBuilder().append("insert into `patty`.`match_result` values ").append(data).toString());
		}
	}
	
	protected void matchAllPatterns() throws SQLException{
		
	}
	
	@Override
	public void run(){
		if(matchAllPatterns && ptnarr != null){
			try {
				matchPatterns();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else if(matchAllPatterns && ptnarr == null){
			try {
				matchAllPatterns();
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		else{
			try {
				matchPattern();
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}

}
