package main;



import java.io.IOException;

import main.textpage.TextPage;
import main.textpage.TextPage.TextPageFrame;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;


import com.esotericsoftware.kryonet.Client;

import TWLSlick.BasicTWLGameState;
import TWLSlick.RootPane;
import de.matthiasmann.twl.ResizableFrame.ResizableAxis;
import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.TextArea;

public class Test extends BasicTWLGameState {

	public Client client;
	DialogLayout p1ResultPanel, p2ResultPanel;
	
	int gcw;
	int gch;
	
	private int current_puzzle_id;
	private PuzzleList puzzle_list;
	private Puzzle[] puzzles;
	private Puzzle current_puzzle;
	private String[] current_choices;
	private String puzzle_file;
	private int noOfAnswers;
	private int correctAnswer;
	private int puzzle_difficulty;
	private int no_of_puzzles;
	
	public Test(int main) {
		client = HRRUClient.conn.getClient();
		try {
			puzzle_list = new PuzzleList("Puzzle.txt");
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.enter(gc, sbg);
		TextPageFrame textpageframe = new TextPageFrame("demo.html");
		textpageframe.setSize(600, 200);
		textpageframe.setDraggable(false);
		textpageframe.setResizableAxis(ResizableAxis.NONE);
		textpageframe.setTheme("textpageframe");
        rootPane.add(textpageframe);
        
		// Set up puzzle variables
		puzzles = puzzle_list.getPuzzle_list();
		puzzle_list.getNumberOfPuzzles();
		
		current_puzzle_id = HRRUClient.cs.getActivity_id();
		current_puzzle = puzzles[1];

		current_choices = current_puzzle.getChoices();
		puzzle_file = current_puzzle.getFile();
		current_puzzle.getAmountOfAnswers();
		correctAnswer = current_puzzle.getAnswer();
		puzzle_difficulty = current_puzzle.getDifficulty();
		
		System.out.println(puzzle_file);
        
	}

	@Override
	protected RootPane createRootPane() {
		assert rootPane == null : "RootPane already created";

		RootPane rp = new RootPane(this);
		rp.setTheme("");
		rp.getOrCreateActionMap().addMapping(this);
		return rp;
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		gcw = gc.getWidth();
		gch = gc.getHeight();
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
	
	}

	@Override
	public int getID() {
		return 3;
	}

}