package main.item;

import main.Character;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class ItemList  {

	private int size;
	private Item item[];
	
	public ItemList() throws SlickException {
		size = 12;
		item = new Item[size];
		
		item[0] = new Item(0, 50, 150, "Armor", "Armor", new Image("simple/items/armorbg.png"));
		item[1] = new Item(1, 100, 200, "Axe", "Axe", new Image("simple/items/axebg.png"));
		item[2] = new Item(2, 50, 100, "Book", "Book", new Image("simple/items/bookbg.png"));
		item[3] = new Item(3, 100, 150, "Bow", "Bow", new Image("simple/items/bowbg.png"));
		item[4] = new Item(4, 50, 100, "Coin", "Coin", new Image("simple/items/coinbg.png"));
		item[5] = new Item(5, 200, 250, "Double Axe", "Double Axe", new Image("simple/items/dblaxebg.png"));
		item[6] = new Item(6, 150, 200, "Document", "Document", new Image("simple/items/documentbg.png"));
		item[7] = new Item(7, 300, 400, "Gem", "Gem", new Image("simple/items/gembg.png"));
		item[8] = new Item(8, 200, 300, "Sword", "Sword", new Image("simple/items/swordbg.png"));
		item[9] = new Item(9, 250, 300, "Tome", "Tome", new Image("simple/items/tome.png"));
		item[10] = new Item(10, 50, 100, "Tools", "Tools", new Image("simple/items/toolsbg.png"));
		item[11] = new Item(11, 20, 80, "Wooden Sword", "Wooden Sword", new Image("simple/items/woodenswordbg.png"));
	}
	
	public Item[] getItems(){
		return item;	
	}
	
	public int getSize(){
		return size;
	}
}