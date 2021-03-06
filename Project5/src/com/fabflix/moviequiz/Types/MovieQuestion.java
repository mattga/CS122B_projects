package com.fabflix.moviequiz.Types;

public class MovieQuestion {
	public String question;
	public String[] answers = new String[4];
	public int correctAnswerIndex;
	
	public MovieQuestion shuffleAnswers() {
		int length = answers.length;
		
		for(int i = 0; i < length; i++) {
			int j = (int)(Math.random()*length);
			
			// Update correct index
			if(i == correctAnswerIndex)
				correctAnswerIndex = j;
			else if (j == correctAnswerIndex)
				correctAnswerIndex = i;
			
			// Swap
			String temp = answers[i];
			answers[i] = answers[j];
			answers[j] = temp;
		}
		
		return this; // I like to do chained method calls... lol
	}
}
