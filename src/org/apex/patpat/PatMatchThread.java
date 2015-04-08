package org.apex.patpat;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apex.util.MySQLConnector;

public abstract class PatMatchThread extends Thread {
	private static Connection con = new MySQLConnector().getConnection();
	private boolean matchAllPatterns = false;
	private String stc = null;
	private PatPattern[] ptnarr = null;
	private Statement stmt = null;

	public PatMatchThread(String threadName, String stc) throws SQLException{
		super(threadName);
		this.stc = stc;
		this.matchAllPatterns = true;
		stmt = con.createStatement();
	}
	
	public PatMatchThread(String threadName, String stc, PatPattern ptn) throws SQLException {
		super(threadName);
		this.stc = stc;
		this.ptnarr = new PatPattern[1];
		ptnarr[0] = ptn;
		stmt = con.createStatement();
	}
	
	public PatMatchThread(String threadName, String stc, PatPattern[] ptnarr) throws SQLException {
		super(threadName);
		this.matchAllPatterns = true;
		this.ptnarr = ptnarr;
		this.stc = stc;
		stmt = con.createStatement();
	}
	
	protected abstract char isMatched(String stc, PatPattern ptn);
	//'y': match all ok. 'n': not match at all. 'u': undeterminded.
	
	protected void matchPatterns() throws SQLException{
		int writeCount = 0;
		StringBuilder data = new StringBuilder();
		
		for(PatPattern ptn : ptnarr){
			char state = isMatched(stc, ptn);
			
			if(state == 'y'){
				String[] domains = ptn.getDomains();
				String[] ranges = ptn.getRanges();
				String[] rels = ptn.getRelations();
				
				for(String relation : rels){
					for(int i = 0 ; i < domains.length ; i ++){
						data.append("(\"").append(stc).append("\",\"").append(ptn.toString()).append("\",\"").append(relation).append("\",\"").append(domains[i]).append("\",\"").append(ranges[i]).append("\",").append("1)");
						writeCount ++;		
//						System.out.println(writeCount);
						if(writeCount != 0 && writeCount % 1000 == 0){
							//Write data for every 1000 items
							try {
								stmt.executeUpdate(new StringBuilder().append("insert into `patty`.`match_result` values ").append(data).toString());
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								System.out.println(new StringBuilder().append("insert into `patty`.`match_result` values ").append(data).toString());
								e.printStackTrace();
							}
							data = new StringBuilder();
						}
						else{
							data.append(',');
						}
					}
				}
				System.out.println(stc + " : " + ptn.toString());
			}
			else if(state == 'u'){
				String[] domains = ptn.getDomains();
				String[] ranges = ptn.getRanges();
				String[] rels = ptn.getRelations();
				
				for(String relation : rels){
					for(int i = 0 ; i < domains.length ; i ++){
						data.append("(\"").append(stc).append("\",\"").append(ptn.toString()).append("\",\"").append(relation).append("\",\"").append(domains[i]).append("\",\"").append(ranges[i]).append("\",").append("1)");
						writeCount ++;
						if(writeCount != 0 && writeCount % 1000 == 0){
//							System.out.println(writeCount);
							//Write data for every 1000 items
							try {
								stmt.executeUpdate(new StringBuilder().append("insert into `patty`.`match_result` values ").append(data).toString());
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								System.out.println(new StringBuilder().append("insert into `patty`.`match_result` values ").append(data).toString());
								e.printStackTrace();
							}
							data = new StringBuilder();
						}
						else{
							data.append(',');
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
				matchPatterns();
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}

//	protected void matchPattern() throws SQLException{
//		StringBuilder data = new StringBuilder();
//		char state = isMatched(stc, ptn);
//		
//		if(state == 'y'){
//			String[] relationArr = ptn.getRelations();
////			System.out.println("relation: " + relationArr.length);
//			for(String relation : relationArr){
//				String[] domainArr = ptn.getDomains();
////				System.out.println("domain: " + domainArr.length);
//				for(String domain : domainArr){
//					String[] rangeArr = ptn.getRanges();
////					System.out.println("range: " + rangeArr.length);
//					for(String range : rangeArr){
//						if(data.length() != 0)data.append(',');
//						data.append("(\"").append(stc).append("\",\"").append(ptn.toString()).append("\",\"").append(relation).append("\",\"").append(domain).append("\",\"").append(range).append("\",").append("1)");
//					}
//				}
//			}
//			System.out.println(stc + " : " + ptn.toString());
//		}
//		else if(state == 'u'){
//			for(String relation : ptn.getRelations()){
//				String[] domains = ptn.getDomains();
//				String[] ranges = ptn.getRanges();
//				
//				for(int i = 0 ; i < domains.length ; i ++){
//					data.append("(\"").append(stc).append("\",\"").append(ptn.toString()).append("\",\"").append(relation).append("\",\"").append(domains[i]).append("\",\"").append(ranges[i]).append("\",").append("1)");
//				}
//			}
////			for(String relation : ptn.getRelations()){
////				for(String domain : ptn.getDomains()){
////					for(String range : ptn.getRanges()){
////						if(data.length() != 0)data.append(',');
////						data.append("(\"").append(stc).append("\",\"").append(ptn.toString()).append("\",\"").append(relation).append("\",\"").append(domain).append("\",\"").append(range).append("\",").append("1)");
////					}
////				}
////			}
//			System.out.println(stc + " : " + ptn.toString());
//		}
////		if(state == 'y'){
////			for(String relation : ptn.getRelations()){
////				for(String domain : ptn.getDomains()){
////					for(String range : ptn.getRanges()){
////						if(data.length() != 0)data += ",";
////						data += "(" + stc + "," + ptn.toString() + "," + relation + "," + domain + "," + range + "," + "1)";
////					}
////				}
////			}
////			System.out.println(stc + " : " + ptn.toString());
////		}
////		else if(state == 'u'){
////			for(String relation : ptn.getRelations()){
////				for(String domain : ptn.getDomains()){
////					for(String range : ptn.getRanges()){
////						if(data.length() != 0)data += ",";
////						data += "(" + stc + "," + ptn.toString() + "," + relation + "," + domain + "," + range + "," + "0)";
////					}
////				}
////			}
////			System.out.println(stc + " : " + ptn.toString());
////		}
//	}
	
}
