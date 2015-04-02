package org.apex.patpat;

import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class PatLemmatizer {
	
	private static StanfordCoreNLP pipeline = null;

	public PatLemmatizer() {
		// TODO 自动生成的构造函数存根
		init();
	}
	
	private static void init(){
		if(pipeline == null){
			Properties props = new Properties();
			props.put("annotators", "tokenize,ssplit,pos,lemma");
			pipeline = new StanfordCoreNLP(props);
		}
	}
	
	public static String lemmatize(String word){
		init();
		Annotation document = new Annotation(word);
		pipeline.annotate(document);
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		String res = "";
		
		for(CoreMap sentence: sentences) {
			for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
				String lema = token.get(LemmaAnnotation.class);
				if(Character.isLetter(lema.charAt(0))){
					res += lema + " ";
				}
			}
		}
		return res;
	}

	public static void main(String[] args) {
		// TODO 自动生成的方法存根
//		String[] str = {"nihao", "nibuhao"};
//		System.out.println(str.toString());
//		PatLemmatizer.lemmatize("What was Obama doing when I broke in?");
		System.out.println(PatLemmatizer.lemmatize("What was Obama doing when I broke in? Who is xiaoming's brother? When did jude made his \"achieves\"?"));
//		PatLemmatizer.lemmatize("was");
	}

}
