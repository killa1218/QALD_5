package org.apex.patpat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class PatSentence {
	private List<PatWord> list = null;
	private String stc = null;
	private String[] stcArr = null;

	public PatSentence(String str) {
		stc = str;
		
	}
	
	public List<PatWord> toList(){
		if(list != null){
			return list;
		}
		else{
			list = new LinkedList<PatWord>();
			for(String word : this.toArray()){
				list.add(new PatWord(word));
			}
			
			return list;
		}
	}
	
	@Override
	public String toString(){
		return stc;
	}
	
	public String[] toArray(){
		if(stcArr != null){
			return stcArr;
		}
		else{
			return stcArr = stc.split(" ");
		}
	}
	
//	public static void matchAll() throws IOException{
//		BufferedReader br = new BufferedReader(new FileReader(new File("./rsc/questions.txt")));
//		String question = null;
//		
//		do{
//			question = br.readLine();
//			
//		}while(question != null);
//		
//	}

}
