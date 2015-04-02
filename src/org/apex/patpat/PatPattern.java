package org.apex.patpat;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.apex.util.MySQLConnector;

import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class PatPattern {
	public static MaxentTagger tagger = null;
	private static Statement stmt = new MySQLConnector().connect();
	private Pattern ptn = null;
//	private Type[] types = null;
	private String pattern;

	public PatPattern(String pattern) throws SQLException{
		this.pattern = pattern;
	}
	
	public String[] getRelations(){
		//still has problem
		ResultSet rs = null;
		LinkedList<String> resList = new LinkedList<String>();
		try {
			rs = stmt.executeQuery("select `relation` from `patty`.`dbpedia_relation_paraphrases` where `pattern`=\"" + pattern + "\"");
			while(rs.next()){
				resList.add(rs.getString(1));
			}
			return resList.toArray(new String[0]);
		} catch (SQLException e) {
			System.out.println("ERROR: Geting relation of " + pattern + " error!");
			e.printStackTrace();
			return null;
		}
		
	}
	
	public String[] getRelationsOf(String kb){
		String table = "dbpedia";
		
		switch(kb.toLowerCase()){
			case "yago":
				table = "`patty`.`yago_relation_paraphrases`";
				break;
			case "dbpedia":
				table = "`patty`.`dbpedia_relation_paraphrases`";
				break;
		}
		
		ResultSet rs = null;
		LinkedList<String> resList = new LinkedList<String>();
		try {
			rs = stmt.executeQuery("select `relation` from " + table + " where `pattern`=\"" + pattern + "\"");
			while(rs.next()){
				resList.add(rs.getString(1));
			}
			return resList.toArray(new String[0]);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public Pattern getRegex(){
		if(ptn != null){
			return ptn;
		}
		else{
			return ptn = Pattern.compile(Pattern.compile("\\[\\[\\w+\\]\\]").matcher(pattern).replaceAll("(.*)"), Pattern.DOTALL + Pattern.CASE_INSENSITIVE);
		}
	}
	
	public String[] getKeyWords(){
		// Those whose POS tag is JJ or JJR or JJS or NN or NNP or NNS or NNPS or MD or FW or LS or RB or RBR or RBS or RP or VB or VBD or VBG or VBN or VBP or VBZ or WDT or WP or WP$ or WRB
		LinkedList<String> resList = new LinkedList<String>();
		
		if(tagger == null){
			tagger = new MaxentTagger("./lib/models/english-left3words-distsim.tagger");
		}
		
		List<TaggedWord> rl = tagger.apply(new PatSentence(pattern).toList());
		
		for(TaggedWord tw : rl){
			char firstChar = tw.tag().charAt(0);
			if(firstChar == 'J' || firstChar == 'N' || firstChar == 'M' || firstChar == 'F' || firstChar == 'L' || firstChar == 'R' || firstChar == 'V' || firstChar == 'W'){
				resList.add(tw.word());				
			}
		}
		
		return resList.toArray(new String[0]);
	}
	
//	public Type[] getTypeArray() throws SQLException{
//		if(types != null){
//			return types;
//		}
//		else{
//			LinkedList<Type> tmp = new LinkedList<Type>();
//			for (int i = 0; i < pattern.length(); i++){
//				if (pattern.charAt(i) == '['){
//					tmp.add(new Type(pattern.substring(i + 2, i + 5),i + 2,i + 4));
//					i += 7;
//				}
//			}
//			return types = (Type[])tmp.toArray();
//		}
//	}


//	private boolean typeMatch(Type type, List<TaggedWord> taggedList, String group) {
//		String[] words = group.split(" ");
//		
//		for(String word : words){
//			for(TaggedWord tw : taggedList){
//				if(word.equals(tw.word())){
//					for(String tag : type.getTags()){
//						if(tag.equals(tw.tag())){
//							return true;
//						}
//					}
//				}
//			}
//		}
//		
//		return false;
//	}
	
	public String[] getDomains(){
		ResultSet rs = null;
		
		try {
			rs = stmt.executeQuery("select `domain` from `wikipedia_patterns` where `patterntext` like \"%" + pattern + "%\"");
			LinkedList<String> resList = new LinkedList<String>();
			while(rs.next()){
				resList.add(rs.getString(1));
			}
			return resList.toArray(new String[0]);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public String[] getRanges(){
		ResultSet rs = null;
		
		try {
			rs = stmt.executeQuery("select `range_` from `wikipedia_patterns` where `patterntext` like \"%" + pattern + "%\"");
			LinkedList<String> resList = new LinkedList<String>();
			while(rs.next()){
				resList.add(rs.getString(1));
			}
			return resList.toArray(new String[0]);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	@Override
	public String toString(){
		return pattern;
	}
	
//	public static void pickoutPatterns(String fileName) throws SQLException, IOException{
//		File f = null;
//		BufferedWriter bw = null;
//		Map<String, Boolean> hash = new HashMap<String, Boolean>();
//		ResultSet rs = stmt.executeQuery("select `patty`.`patterntext` from `wikipedia_patterns` where 1");
//		
//		if(fileName.contains("/")){
//			f = new File(fileName);
//		}
//		else{
//			f = new File("./rsc/" + fileName);
//		}
//		bw = new BufferedWriter(new FileWriter(f));
//		if(f.isFile() && f.canWrite()){
//			while(rs.next()){
//				String ptnset = rs.getString(1);
//				String[] ptnsetArr = ptnset.split(";\\$");
//				for(String ptn : ptnsetArr){
//					if(!hash.containsKey(ptn)){
//						hash.put(ptn, true);
//					}
//				}
//			}
//		}
//		else{
//			System.out.println("Error: " + fileName + "can not write!");
//		}
//	}
	
//	public static void pickoutPatterns(String fileName, String path){
//		if(new File(path).isDirectory()){
////			pickoutPatterns(path + "/" + fileName);
//		}
//		else{
//			System.out.println("Error: " + path + " is not a directory!");
//		}
//	}
	
	public static void main(String[] argv) throws SQLException{
//		PatSentence pstc = new PatSentence("Who is the youngest player in the Premier League?");
//		
//		String str = tagger.tagString("Who is the youngest player in the Premier League?");
//		
//		List<TaggedWord> rs = tagger.apply(pstc.toList());
//		
//		System.out.println(str);
//		
//		for(TaggedWord tw : rs){
//			System.out.println(tw.word());
//		}
		
//		Pattern ptn = Pattern.compile("(.*) directed by", Pattern.DOTALL + Pattern.CASE_INSENSITIVE);
//		Matcher m = ptn.matcher("Give me all movies directed by Francis Ford Coppola.");
//		while(m.find()){
//			System.out.println(m.groupCount());
//			System.out.println(m.group(1));			
//		}
//			System.out.println(new Date().toString());			
		
		PatPattern ptn = new PatPattern("best known for [[prp]] role in");
		System.out.println(ptn.getRegex().toString());
		
	}
	
}

//final class Type{
//	private String notation = null;
//	private String[] tag = null;
//	private int start = 0;
//	private int end = 0;
//	private static Statement stmt = new MySQLConnector().connect();
//	
//	private static HashMap<String,String[]> tagmap=new HashMap<String,String[]>();
//	
//	public Type(String t, int beg, int end) throws SQLException{
//		notation = t;
//		this.start = beg;
//		this.end = end;
//		ResultSet rs = null;
//		if (tagmap.containsKey(t)){
//			tag=tagmap.get(t);
//			return;
//		}
//		
//		try {
//			stmt.execute("use patty");
//			rs = stmt.executeQuery("select `pos_tag` from `pattern_type_info` where `notation`=\"" + t + "\"");
//		} catch (SQLException e) {
//			e.printStackTrace();
//			System.out.println("Querying 'tag' Error!");
//			System.out.println("select `pos_tag` from `pattern_type_info` where `notation`=\"" + t + "\"");
//			stmt.getConnection().close();
//			stmt.close();
//			return ;
//		}
//		if(rs.last()){
//			tag = new String[rs.getRow()];
//			rs.first();
//			do{
//				tag[rs.getRow() - 1] = rs.getString(1);
//			}while(rs.next());
//		}
//		tagmap.put(t, tag);
//	}
//	
//	public Type(String t, int beg) throws SQLException{
//		notation = t;
//		this.start = beg;
//		this.end = beg + notation.length() - 1;
//		ResultSet rs = null;
//		
//		try {
//			stmt.execute("use patty");
//			rs = stmt.executeQuery("select `pos_tag` from `pattern_type_info` where `notation`=\"" + t + "\"");
//		} catch (SQLException e) {
//			e.printStackTrace();
//			System.out.println("Querying 'tag' Error!");
//			System.out.println("select `pos_tag` from `pattern_type_info` where `notation`=\"" + t + "\"");
//			stmt.getConnection().close();
//			stmt.close();
//			return ;
//		}
//		if(rs.last()){
//			tag = new String[rs.getRow()];
//			rs.first();
//			do{
//				tag[rs.getRow() - 1] = rs.getString(1);
//			}while(rs.next());
//		}
//	}
//	
//	public String[] getTags(){
//		return tag;
//	}
//	
//	public String getNotation(){
//		return notation;
//	}
//	
//	public int getStartPosition(){
//		return start;
//	}
//	
//	public int getEndPosition(){
//		return end;
//	}
//	
//}