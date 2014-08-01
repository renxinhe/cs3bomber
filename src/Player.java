import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.swing.*;

public class Player extends JLabel implements Entity 
{
	protected BufferedImage characterSprite;
	protected Statistics stat;
	public int numBombLeft;
	
	public int thisCharacter;
	public int lifeCount;
	
	private final int WHITE_BOMBER = 0;
	private final int BLACK_BOMBER = 1;
	private final int BULBASAUR = 2;
	private final int PIKACHU = 3;
	private final int NORTH = 0;
	private final int SOUTH = 1;
	private final int WEST = 2;
	private final int EAST = 3;
	private int row;
	private int column;
	private final int initRow;
	private final int initCol;
	
	protected final boolean isBot;
	
	public Player() throws IOException
	{
		characterSprite = ImageIO.read(new File("Sprites/Character Gifs/char_0_2.gif"));
		this.setIcon(new ImageIcon("Sprites/Character Gifs/char_0_2.gif"));
		stat = new Statistics();
		numBombLeft = 0;
		thisCharacter = -1;
		row = 0;
		column = 0;
		lifeCount = 3;
		
		initRow = row;
		initCol = column;
		
		isBot = false;
	}
	
	public Player(int character, boolean bot, int r, int c) throws IOException
	{
		isBot = bot;
		thisCharacter = character;
		row = r;
		column = c;
		stat = new Statistics();
		lifeCount = 3;
		
		initRow = r;
		initCol = c;
		
		//JLabel Icon
		switch(thisCharacter)
		{
			case WHITE_BOMBER: super.setIcon(new ImageIcon("Sprites/Character Gifs/char_0_2.gif")); break;
			case BLACK_BOMBER: super.setIcon(new ImageIcon("Sprites/Character Gifs/char_1_2.gif")); break;
			case BULBASAUR: super.setIcon(new ImageIcon("Sprites/Character Gifs/char_2_2.gif")); break;
			case PIKACHU: super.setIcon(new ImageIcon("Sprites/Character Gifs/char_3_2.gif")); break;
		}
		//BufferedImage
		switch(thisCharacter)
		{
			case WHITE_BOMBER: characterSprite = ImageIO.read(new File("Sprites/Character Gifs/char_0_2.gif")); break;
			case BLACK_BOMBER: characterSprite = ImageIO.read(new File("Sprites/Character Gifs/char_1_2.gif")); break;
			case BULBASAUR: characterSprite = ImageIO.read(new File("Sprites/Character Gifs/char_2_2.gif")); break;
			case PIKACHU: characterSprite = ImageIO.read(new File("Sprites/Character Gifs/char_3_2.gif")); break;
		}
		setStat();
		numBombLeft = stat.bombNumber;
	}
	
	public boolean die() 
	{
		lifeCount--;
		if(lifeCount > 0)
		{
			setRow(initRow);
			setColumn(initCol);
			System.out.println("Lives left: " + lifeCount);
			return true;
		}
		/*characterSprite = null;
		stat.bombNumber = 0;*/
		return false;
	}
	
	public void setStat()
	{
		switch(thisCharacter)
		{
			case BULBASAUR: stat.changeFuseLength(5); stat.changeBombRange(1);  break;	
			case PIKACHU: stat.changeFuseLength(5); stat.changeBombNumber(1); break;
		}
	}
	
	public void charMove(int dir) throws IOException
	{
		//JLabel Icon
		switch(dir)
		{
			case NORTH: super.setIcon(new ImageIcon("Sprites/Character Gifs/char_"+thisCharacter+"_3.gif")); break; 
			case SOUTH: super.setIcon(new ImageIcon("Sprites/Character Gifs/char_"+thisCharacter+"_2.gif")); break;
			case WEST: super.setIcon(new ImageIcon("Sprites/Character Gifs/char_"+thisCharacter+"_4.gif")); break;
			case EAST: super.setIcon(new ImageIcon("Sprites/Character Gifs/char_"+thisCharacter+"_5.gif")); break;
		}
		//BufferedImage
		switch(dir)
		{
			case NORTH: characterSprite = ImageIO.read(new File("Sprites/Character Gifs/char_"+thisCharacter+"_3.gif")); break;
			case SOUTH: characterSprite = ImageIO.read(new File("Sprites/Character Gifs/char_"+thisCharacter+"_2.gif")); break;
			case WEST: characterSprite = ImageIO.read(new File("Sprites/Character Gifs/char_"+thisCharacter+"_4.gif")); break;
			case EAST: characterSprite = ImageIO.read(new File("Sprites/Character Gifs/char_"+thisCharacter+"_5.gif")); break;
		}
	}
	
	public void statChange(int x)
	{
		switch(x)
		{
		case 0: this.stat.changeFuseLength(-5); break;
		case 1: this.stat.changeBombRange(1); break;
		case 2: this.stat.changeBombNumber(1); numBombLeft++; break;
		}
	}

	public static Point getPixelFromIndex(int row, int column)
	{
		return new Point(column*30+20, row*30+20);
	}
	
	public BufferedImage returnImage()
	{
		return characterSprite;
	}
	
	public int getRow()
	{
		return row;
	}
	
	public int getColumn()
	{
		return column;
	}
	
	public void setRow(int r)
	{
		row = r;
	}
	
	public void setColumn(int c)
	{
		column = c;
	}
}