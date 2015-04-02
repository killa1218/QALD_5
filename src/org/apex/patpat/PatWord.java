package org.apex.patpat;

import edu.stanford.nlp.ling.HasWord;

@SuppressWarnings("serial")
public class PatWord implements HasWord {
	private String word = null;

	public PatWord() {
		// TODO 自动生成的构造函数存根
	}

	public PatWord(String arg) {
		// TODO 自动生成的构造函数存根
		this.setWord(arg);
	}

	@Override
	public void setWord(String arg0) {
		// TODO 自动生成的方法存根
		word = arg0;
	}

	@Override
	public String word() {
		// TODO 自动生成的方法存根
		return word;
	}

}
