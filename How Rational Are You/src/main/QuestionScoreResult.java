package main;

import main.item.Item;

public class QuestionScoreResult {

	private final int easyPoints = 50;
	private final int mediumPoints = 100;
	private final int hardPoints = 150;
	
	private int noOfEasyQuestions;
	private int noOfEasyQCorrect;
	private int easyQTimeBonusAvg;
	private double easyQTimeBonusOverall;
	private int easyQPointsAvg;
	private double easyQPointsOverall;
	
	private int noOfMediumQuestions;
	private int noOfMediumQCorrect;
	private int mediumQTimeBonusAvg;
	private double mediumQTimeBonusOverall;
	private int mediumQPointsAvg;
	private double mediumQPointsOverall;
	
	private int noOfHardQuestions;
	private int noOfHardQCorrect;
	private int hardQTimeBonusAvg;
	private double hardQTimeBonusOverall;
	private int hardQPointsAvg;
	private double hardQPointsOverall;
	
	private int noOfTotalQuestions;
	private int noOfTotalQCorrect;
	private int totalQTimeBonusAvg;
	private double totalQTimeBonusOverall;
	private int totalQPointsAvg;
	private double totalQPointsOverall;
	
	private int pointsAvailable;
	
	private double percentage; 
	
	public QuestionScoreResult(
			int noOfEasyQuestions,
			int noOfEasyQCorrect,
			int easyQTimeBonusAvg,
			double easyQTimeBonusOverall,
			int easyQPointsAvg,
			double easyQPointsOverall,
			int noOfMediumQuestions,
			int noOfMediumQCorrect,
			int mediumQTimeBonusAvg,
			double mediumQTimeBonusOverall,
			int mediumQPointsAvg,
			double mediumQPointsOverall,
			int noOfHardQuestions,
			int noOfHardQCorrect,
			int hardQTimeBonusAvg,
			double hardQTimeBonusOverall,
			int hardQPointsAvg,
			double hardQPointsOverall,
			int noOfTotalQuestions,
			int noOfTotalQCorrect,
			int totalQTimeBonusAvg,
			double totalQTimeBonusOverall,
			int totalQPointsAvg,
			double totalQPointsOverall)
	{
		this.noOfEasyQuestions = noOfEasyQuestions;
		this.noOfEasyQCorrect = noOfEasyQCorrect;
		this.easyQTimeBonusAvg = easyQTimeBonusAvg;
		this.easyQTimeBonusOverall = easyQTimeBonusOverall;
		this.easyQPointsAvg = easyQPointsAvg;
		this.easyQPointsOverall = easyQPointsOverall;
		this.noOfMediumQuestions = noOfMediumQuestions;
		this.noOfMediumQCorrect = noOfMediumQCorrect;
		this.mediumQTimeBonusAvg = mediumQTimeBonusAvg;
		this.mediumQTimeBonusOverall = mediumQTimeBonusOverall;
		this.mediumQPointsAvg = mediumQPointsAvg;
		this.mediumQPointsOverall = mediumQPointsOverall;
		this.noOfHardQuestions = noOfHardQuestions;
		this.noOfHardQCorrect = noOfHardQCorrect;
		this.hardQTimeBonusAvg = hardQTimeBonusAvg;
		this.hardQTimeBonusOverall = hardQTimeBonusOverall;
		this.hardQPointsAvg = hardQPointsAvg;
		this.hardQPointsOverall = hardQPointsOverall;
		this.setNoOfTotalQuestions(noOfTotalQuestions);
		this.setNoOfTotalQCorrect(noOfTotalQCorrect);
		this.totalQTimeBonusAvg = totalQTimeBonusAvg;
		this.setTotalQTimeBonusOverall(totalQTimeBonusOverall); 
		this.totalQPointsAvg = totalQPointsAvg; 
		this.totalQPointsOverall = totalQPointsOverall;
		this.percentage = 0;
		this.setPointsAvailable((noOfEasyQuestions * easyPoints) + (noOfMediumQuestions * mediumPoints) + (noOfHardQuestions * hardPoints) + (55*noOfTotalQuestions));
	}
	
	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	public double getTotalQTimeBonusOverall() {
		return totalQTimeBonusOverall;
	}

	public void setTotalQTimeBonusOverall(double totalQTimeBonusOverall) {
		this.totalQTimeBonusOverall = totalQTimeBonusOverall;
	}

	public int getNoOfTotalQuestions() {
		return noOfTotalQuestions;
	}

	public void setNoOfTotalQuestions(int noOfTotalQuestions) {
		this.noOfTotalQuestions = noOfTotalQuestions;
	}

	public int getNoOfTotalQCorrect() {
		return noOfTotalQCorrect;
	}

	public void setNoOfTotalQCorrect(int noOfTotalQCorrect) {
		this.noOfTotalQCorrect = noOfTotalQCorrect;
	}

	public int getPointsAvailable() {
		return pointsAvailable;
	}

	public void setPointsAvailable(int pointsAvailable) {
		this.pointsAvailable = pointsAvailable;
	}
}