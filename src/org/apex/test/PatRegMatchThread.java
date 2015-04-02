package org.apex.test;

import java.util.regex.Matcher;

import org.apex.patpat.PatLemmatizer;
import org.apex.patpat.PatMatchThread;
import org.apex.patpat.PatPattern;


public class PatRegMatchThread extends PatMatchThread {

	public PatRegMatchThread(String threadName, String stc) {
		super(threadName, stc);
		// TODO 自动生成的构造函数存根
	}

	public PatRegMatchThread(String threadName, String stc, PatPattern ptn) {
		super(threadName, stc, ptn);
		// TODO 自动生成的构造函数存根
	}

	public PatRegMatchThread(String threadName, String stc, PatPattern[] ptnarr) {
		super(threadName, stc, ptnarr);
		// TODO 自动生成的构造函数存根
	}

	@Override
	protected char isMatched(String stc, PatPattern ptn) {
		// TODO 自动生成的方法存根
		Matcher m = ptn.getRegex().matcher(PatLemmatizer.lemmatize(stc));
		
		if(m.find()){
			return 'y';
		}
		return 'n';
	}


}
