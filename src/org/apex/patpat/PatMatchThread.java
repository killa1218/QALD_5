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
		String data = "";
		char state = isMatched(stc, ptn);
		
		if(state == 'y'){
			for(String relation : ptn.getRelations()){
				for(String domain : ptn.getDomains()){
					for(String range : ptn.getRanges()){
						if(data.length() != 0)data += ",";
						data += "(" + stc + "," + ptn.toString() + "," + relation + "," + domain + "," + range + "," + "1)";
					}
				}
			}
			System.out.println(stc + " : " + ptn.toString());
		}
		else if(state == 'u'){
			for(String relation : ptn.getRelations()){
				for(String domain : ptn.getDomains()){
					for(String range : ptn.getRanges()){
						if(data.length() != 0)data += ",";
						data += "(" + stc + "," + ptn.toString() + "," + relation + "," + domain + "," + range + "," + "0)";
					}
				}
			}
			System.out.println(stc + " : " + ptn.toString());
		}
	}
	
	protected void matchPatterns() throws SQLException{
		int writeCount = 0;
		String data = "";
		
		for(PatPattern ptn : ptnarr){
			char state = isMatched(stc, ptn);
			if(state == 'y'){
				for(String relation : ptn.getRelations()){
					for(String domain : ptn.getDomains()){
						for(String range : ptn.getRanges()){
							if(data.length() != 0)data += ",";
							data += "(" + stc + "," + ptn.toString() + "," + relation + "," + domain + "," + range + "," + "1)";
						}
					}
				}
				System.out.println(stc + " : " + ptn.toString());
			}
			else if(state == 'u'){
				for(String relation : ptn.getRelations()){
					for(String domain : ptn.getDomains()){
						for(String range : ptn.getRanges()){
							if(data.length() != 0)data += ",";
							data += "(" + stc + "," + ptn.toString() + "," + relation + "," + domain + "," + range + "," + "0)";
						}
					}
				}
				System.out.println(stc + " : " + ptn.toString());
			}
			
			if(writeCount % 1000 == 0){
				//Write data for every 1000 items
				stmt.executeUpdate("insert into `patty`.`match_result values` " + data);
				data = "";
			}
		}

		stmt.executeUpdate("insert into `patty`.`match_result values` " + data);
	}
	
	protected void matchAllPatterns() throws SQLException{
		
	}
	
	public void run(){
		if(matchAllPatterns && ptnarr != null){
			try {
				matchAllPatterns();
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
