package main;

import java.io.IOException;
import java.util.Random;

import main.item.Item;
import main.item.ItemList;
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
import de.matthiasmann.twl.textarea.SimpleTextAreaModel;
import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.TextArea;
import de.matthiasmann.twl.ValueAdjuster;
import de.matthiasmann.twl.ValueAdjusterFloat;
import de.matthiasmann.twl.ValueAdjusterInt;

public class Test extends BasicTWLGameState {

	public Client client;
	DialogLayout p1ResultPanel, p2ResultPanel;
	
	int gcw;
	int gch;
	
	DialogLayout itemPanel;
	Label itemName, itemValue;
	TextArea itemDescription;
	SimpleTextAreaModel itemDescriptionModel;
	
	ItemList itemList;
	Item items[];
	Item currentItem;
	
	ValueAdjusterInt vaBid;

	
	Button btnSubmit;
	
	private boolean playedBefore = false;
	private Random rand = new Random();
	
	public Test(int main) {
		client = HRRUClient.conn.getClient();
		
	}

	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.enter(gc, sbg);
		playedBefore = true;
		
		int item_id = rand.nextInt(itemList.getSize());
        currentItem = items[item_id];
        
        itemName = new Label("Name: " + currentItem.getName());
        int value = rand.nextInt(currentItem.getMaxValue() - currentItem.getMinValue() + 1) + currentItem.getMinValue();
        itemValue = new Label("Value: " + value);
        System.out.println(itemValue.getText());
        itemDescriptionModel.setText("Description: /n" + currentItem.getDescription());
       
        vaBid = new ValueAdjusterInt();
		vaBid.setMinMaxValue(0, 400);
		vaBid.setSize(200, 30);
		vaBid.setPosition((gcw/2) - vaBid.getWidth()/2, 450);
		
		btnSubmit = new Button("Submit Bid");
		btnSubmit.setSize(200, 30);
		btnSubmit.setPosition((gcw/2) - btnSubmit.getWidth()/2 - 2,490);
		btnSubmit.setTheme("choicebutton");
		
		
		rootPane.add(btnSubmit);
		rootPane.add(vaBid);
		rootPane.add(itemPanel);
		
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
		
		itemList = new ItemList();
		items = itemList.getItems();
		
		itemPanel = new DialogLayout();
		itemPanel.setTheme("item-panel");
		itemPanel.setSize(400, 220);
		itemPanel.setPosition(50, 100);
		
		itemDescriptionModel = new SimpleTextAreaModel();
		itemDescription = new TextArea(itemDescriptionModel);
		
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.drawImage(currentItem.getItemImage(), 75, 125);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
	
	}

	@Override
	public int getID() {
		return 3;
	}

}