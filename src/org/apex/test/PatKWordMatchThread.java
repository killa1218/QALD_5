package org.apex.test;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apex.patpat.PatLemmatizer;
import org.apex.patpat.PatMatchThread;
import org.apex.patpat.PatPattern;

public class PatKWordMatchThread extends PatMatchThread {

	public PatKWordMatchThread(String threadName, String stc) throws SQLException {
		super(threadName, stc);
		// TODO 自动生成的构造函数存根
	}

	public PatKWordMatchThread(String threadName, String stc, PatPattern ptn) throws SQLException {
		super(threadName, stc, ptn);
		// TODO 自动生成的构造函数存根
	}

	public PatKWordMatchThread(String threadName, String stc,
			PatPattern[] ptnarr) throws SQLException {
		super(threadName, stc, ptnarr);
		// TODO 自动生成的构造函数存根
	}
	
	public static float getMatchScore(String stc, PatPattern ptn){
		String[] keyWords = ptn.getKeyWords();
		String keyWordPtn = "";
		
		if(keyWords.length == 0 || keyWords.length < 2 && keyWords[0].length() < 3){
			return 0;
		}
		else{
			for(String kw : keyWords){
				keyWordPtn += "(.*)" + kw;
			}
			
			keyWordPtn += "(.*)";
			
			Matcher m = Pattern.compile(keyWordPtn).matcher(PatLemmatizer.lemmatize(stc));
			
			if(m.find()){
				return 1;
			}
			else{
				return 0;
			}
		}
	}

	@Override
	protected char isMatched(String stc, PatPattern ptn) {
		// TODO 自动生成的方法存根
		String[] keyWords = ptn.getKeyWords();
		String keyWordPtn = "";
		
		if(keyWords.length == 0 || keyWords.length < 2 && keyWords[0].length() < 3){
			return 'n';
		}
		else{
			for(String kw : keyWords){
				keyWordPtn += "(.*)" + kw;
			}
			
			keyWordPtn += "(.*)";
			
			Matcher m = Pattern.compile(keyWordPtn).matcher(PatLemmatizer.lemmatize(stc));
			
			if(m.find()){
				return 'y';
			}
			else{
				return 'n';
			}
		}
	}

}
