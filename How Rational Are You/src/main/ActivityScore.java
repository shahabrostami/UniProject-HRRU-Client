package main;

public class ActivityScore {

	private int activity;
	private int activity_id;
	private int choice;
	private int points;
	private int difficulty;
	private int elapsedtime;
	private int overall;
	private boolean correct;
	
	public ActivityScore(int activity, int points, int difficulty, int elapsedtime, int overall, boolean correct)
	{
		this.setActivity(activity);
		this.setPoints(points);
		this.setDifficulty(difficulty);
		this.setElapsedtime(elapsedtime);
		this.setOverall(overall);
		this.setCorrect(correct);
	}

	public int getActivity() {
		return activity;
	}

	public void setActivity(int activity) {
		this.activity = activity;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	public int getElapsedtime() {
		return elapsedtime;
	}

	public void setElapsedtime(int elapsedtime) {
		this.elapsedtime = elapsedtime;
	}
	
	public boolean getCorrect() {
		return this.correct;
	}
	
	public void setCorrect(boolean correct) {
		this.correct = correct;
	}

	public int getOverall() {
		return overall;
	}

	public void setOverall(int overall) {
		this.overall = overall;
	}

	public int getActivity_id() {
		return activity_id;
	}

	public void setActivity_id(int activity_id) {
		this.activity_id = activity_id;
	}

	public int getChoice() {
		return choice;
	}

	public void setChoice(int choice) {
		this.choice = choice;
	}



}
