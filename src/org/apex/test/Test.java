package org.apex.test;

public class Test {
	public static void main(String[] argvs){
		String[] str = new String[9];
		for(int i = 0 ; i < 9 ; i ++){
			str[i] = "djkfjdh jdkdhiehk hskggqigegfdj gjsgjfgiwgfi jsgjhffegj";
		}
		
		String data = "";
		
		for(int i = 0 ; i < 9 ; i ++){
			for(int j = 0 ; j < 9 ; j ++){
				data += str[j] + str[i];
			}
		}
	}
}
