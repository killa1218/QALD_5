package org.apex.test;

import java.sql.SQLException;

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

	@Override
	protected char isMatched(String stc, PatPattern ptn) {
		// TODO 自动生成的方法存根
		return 0;
	}

}
