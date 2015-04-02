package org.apex.test;

import java.util.List;
import java.util.regex.Matcher;

import org.apex.patpat.PatMatchThread;
import org.apex.patpat.PatPattern;
import org.apex.patpat.PatSentence;

import edu.stanford.nlp.ling.TaggedWord;

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
		Matcher m = ptn.getRegex().matcher(stc);
		int groupCount = m.groupCount();
		List<TaggedWord> taggedList = PatPattern.tagger.apply(new PatSentence(stc).toList());
		
		for(int i = 0 ; i < groupCount && m.find() ; i ++){
//			if(!typeMatch(types[i], taggedList, m.group(i + 1))){
//				return 'u';
//			}
		}
		
		m.reset();
		
		if(m.find()){
			return 'y';
		}
		else{
			return 'n';
		}		
	}


}
