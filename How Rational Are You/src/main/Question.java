package main;

import org.newdawn.slick.SlickException;

public class Question {

	private String file;
	private String answerFile;
	private int amountOfAnswers;
	private int answer;
	private int appeared;
	private int difficulty;
	private String[] choices;
	
	public Question (int id, int amountOfAnswers, int answer, int difficulty, String description, String answerDescription, String[] choices) throws SlickException
	{
		this.file = description;
		this.setAnswerFile(answerDescription);
		this.amountOfAnswers = amountOfAnswers;
		this.answer = answer;
		this.choices = choices;
		this.difficulty = difficulty;
		this.appeared = 0;
	}

	public String[] getChoices() {
		return choices;
	}

	public int getAppeared() {
		return appeared;
	}

	public void setAppeared(int appeared) {
		this.appeared = appeared;
	}

	public int getAnswer() {
		return answer;
	}

	public int getAmountOfAnswers() {
		return amountOfAnswers;
	}

	public String getFile() {
		return file;
	}
	
	public int getDifficulty() {
		return difficulty;
	}

	public String getAnswerFile() {
		return answerFile;
	}

	public void setAnswerFile(String answerFile) {
		this.answerFile = answerFile;
	}

}
