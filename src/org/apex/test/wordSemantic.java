package org.apex.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

import org.apex.util.vec;

public class wordSemantic {
	HashMap<String, vec> map=new HashMap<String, vec>();
	static int size=71291;
	public wordSemantic() {
		try {
			BufferedReader br=new BufferedReader(new FileReader("./rsc/vectors.bin"));
			String str;
			String[] s;
			br.readLine();
			for (int i=0;i<size;i++) {
				str=br.readLine();
				s=str.split(" ");
				vec v=new vec();
				for (int j=0;j<200;j++) {
					v.wordvec[j]=Double.valueOf(s[j+1]);
				}
				map.put(s[0], v);
			}
		} catch (Exception e) {
			System.err.println(e);
		}
	}
	public double relation(String a, String b) {
		vec v1=map.get(a);
		vec v2=map.get(b);
		if(v1==null||v2==null) return -1;
		else return vec.getcos(v1, v2);
	}
	public static void main(String[] args) {
		wordSemantic h=new wordSemantic();
		System.out.println(h.relation("start","source"));
		
	}
}
