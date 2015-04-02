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
	
	protected void matchPattern(){
		
	}
	
	protected void matchPatterns() throws SQLException{
		for(PatPattern ptn : ptnarr){
			char state = isMatched(stc, ptn);
			if(state == 'y'){
				System.out.println(stc + " : " + ptn.toString());
				stmt.executeUpdate("insert into patty.match_result values (\"" + stc + "\", \"" + ptn.toString() + "\", \"" + ptn.getDomains() + "\", \"" + ptn.getRanges() + "\", " + 1 + ")");
			}
			else if(state == 'u'){
				System.out.println(stc + " : " + ptn.toString());
				stmt.executeUpdate("insert into patty.match_result values (\"" + stc + "\", \"" + ptn.toString() + "\", \"" + ptn.getDomains() + "\", \"" + ptn.getRanges() + "\", " + 0 + ")");				
			}
		}
	}
	
	protected void matchAllPatterns(){
	}
	
	private void matchallPatterns() throws SQLException{
	}
	
	public void run(){
		if(matchAllPatterns && ptnarr != null){
			try {
				matchallPatterns();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else if(matchAllPatterns && ptnarr == null){
			matchAllPatterns();
		}
		else{
			matchPattern();
		}
	}

}
