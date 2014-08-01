import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;

public class Map extends JPanel implements KeyListener, MouseListener, Runnable
{
	
	//TIMER
	public Timer mapTimer;
	private Thread repaintThread;
	//TILESETS
	static final int POKEGRASS = 1;
	//ROW AND COLUMN
	static final int NUM_ROW = 21;
	static final int NUM_COLUMN = 21;
	//PLAYERS
	public JPanel playerPane;
	public Player playerOne;
	public Player playerTwo;
	//ARRAYS, PANES
	public TilePanel tileContentPane;
	protected Entity[][][] entityContentPane; //[x][y][0-Breakable 1-Item]
	protected Item[][] itemContentPane;
	protected Player[] players;
	//BOMBS
	public JPanel bombPane;
	public boolean [][] hasBombMatrix;
	//TODO: Delete file test name;
	protected File mapFile; 

	public Map() throws Exception
	{
		this.setFocusable(true);
		this.addKeyListener(this);
		this.setOpaque(false);
		this.setLayout(null);
		this.mapFile = null;
		
		tileContentPane = null;
		itemContentPane = null;
		entityContentPane = null;
		playerPane = null;
		players = null;
		
		playerOne = new Player();
		playerTwo = new Player();
		
		bombPane = null;
		repaintThread = new Thread(this);
		repaintThread.start();
	}
	
	public Map(File mapFile) throws IOException, LocationOccupiedException
	{
		this.setFocusable(true);
		this.addKeyListener(this);
		this.setOpaque(false);
		this.setLayout(null);
		this.mapFile = mapFile;
		
		tileContentPane = new TilePanel(mapFile);
		entityContentPane = new Entity[NUM_ROW][NUM_COLUMN][3];
		itemContentPane = new Item[NUM_ROW][NUM_COLUMN];
		for(int x=0;x<NUM_ROW;x++)
			for(int y=0;y<NUM_COLUMN;y++)
				itemContentPane[x][y]=null;
		
		//Player Panel
		playerPane = new JPanel(null){
			public void paintComponent(Graphics g)
			{
				g.drawLine(0, 0, 0, 0);
			}
		};
		playerPane.setFocusable(true);
		playerPane.setBounds(0, 0, 650, 650);
		players = new Player[2];
		
		playerOne = new Player(0,false,1,1);
		Point playerOneInitialLocation = Player.getPixelFromIndex(playerOne.getRow(), playerOne.getColumn());
		playerOne.setBounds(playerOneInitialLocation.x, playerOneInitialLocation.y, 30, 30);
		players[0] = playerOne;
		playerPane.add(playerOne);
		System.out.println("Player One Added.");
		
		playerTwo = new Player(1,false,NUM_ROW-2,NUM_COLUMN-2);
		Point playerTwoInitialLocation = Player.getPixelFromIndex(playerTwo.getRow(), playerTwo.getColumn());
		playerTwo.setBounds(playerTwoInitialLocation.x, playerTwoInitialLocation.y, 30, 30);
		players[1] = playerTwo;
		playerPane.add(playerTwo);
		System.out.println("Player Two Added.");
		
		fillBreakable(mapFile);
		
		bombPane = new JPanel(null){
			public void paintComponent(Graphics g)
			{
				g.drawLine(0, 0, 0, 0);
			}
		};
		bombPane.setFocusable(true);
		bombPane.setBounds(0, 0, 650, 650);
		hasBombMatrix = new boolean[NUM_ROW][NUM_COLUMN];
		
		repaintThread = new Thread(this);
		repaintThread.start();
	}


	//Fills map with breakable objects
	public void fillBreakable(File mappy) throws LocationOccupiedException,IOException
	{
		Scanner in = new Scanner(mappy);
		while(!in.nextLine().equals("DESTRUCTIBLE"));
		String s = in.nextLine();
		int count = 0;
		while(count<NUM_ROW)
		{
			for(int i=0;i<s.length();i++)
			{
				if(s.charAt(i)!='1')
					entityContentPane[count][i][0]=null;
				else
				{
					if(this.tileContentPane.tileMatrix[count][i].getName()==1)
						entityContentPane[count][i][0]=new Breakable();
					else
						throw new LocationOccupiedException("["+count+","+i+"]");
				}
			}
			count++;
			s=in.nextLine();
		}
		System.out.println("Breakable Filled.");
	}
	//Draws entityContentPane
	public void paintComponent(Graphics g)
	{
		//Draw Breakable
		for(int i=20; i<(NUM_ROW)*30; i+=30)
			for(int j=20; j<(NUM_COLUMN)*30; j+=30)
			{
				try
				{
					g.drawImage(entityContentPane[(i-20)/30][(j-20)/30][0].returnImage(), j, i, null);
				}
				catch(Exception e){}
			}
		
		for(int i=20; i<(NUM_ROW)*30; i+=30)
			for(int j=20; j<(NUM_COLUMN)*30; j+=30)
			{
				try
				{
					g.drawImage(itemContentPane[(i-20)/30][(j-20)/30].returnImage(), j, i, null);
				}
				catch(Exception e){}
			}
		
		//Draw Entity
		for(int i=20; i<(NUM_ROW)*30; i+=30)
			for(int j=20; j<(NUM_COLUMN)*30; j+=30)
			{
				try
				{
					g.drawImage(entityContentPane[(i-20)/30][(j-20)/30][1].returnImage(), j, i, null);
				}
				catch(Exception e){}
			}

		//Repaint
		//repaint();
	}
	public boolean canMove(int r, int c)
	{	try
		{
			if(tileContentPane.tileMatrix[r][c].getName()==1 && entityContentPane[r][c][0]==null && !hasBombMatrix[r][c])
				return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	public void win(int playerNumber)
	{
		System.out.println("ENDGAME");
		JOptionPane.showMessageDialog(null, "Player "+playerNumber+" WINS");
		System.exit(0);
	}
	
	public void destroy(int range, Point curbomb, int direction) throws IOException //1-East 2-North 3-West 4-South 0-Null
	{
		if(range<=0)
			return;
		if(curbomb.x<1 || curbomb.x>=NUM_ROW-1 || curbomb.y<1 || curbomb.y>=NUM_COLUMN-1)
			return;
		int r = curbomb.x;
		int c = curbomb.y;
		
		
		//Player
		if(tileContentPane.tileMatrix[r][c].getName() == 1 && playerOne.getRow()==r && playerOne.getColumn()==c)
		{
			if(!playerOne.die())
				win(2);
			Point playerOnePixelLocation = Player.getPixelFromIndex(playerOne.getRow(), playerOne.getColumn());
			playerOne.setLocation(playerOnePixelLocation.x, playerOnePixelLocation.y);
			return;
		}
		
		if(tileContentPane.tileMatrix[r][c].getName() == 1 && playerTwo.getRow()==r && playerTwo.getColumn()==c)
		{
			if(!playerTwo.die())
				win(1);
			Point playerTwoPixelLocation = Player.getPixelFromIndex(playerTwo.getRow(), playerTwo.getColumn());
			playerTwo.setLocation(playerTwoPixelLocation.x, playerTwoPixelLocation.y);
			return;
		}
		//Solid Blocks
		if(tileContentPane.tileMatrix[r][c].getName() != 1)
			return;
		
		//Draw Fire
				try{ Graphics g = this.getGraphics();
				g.drawImage(ImageIO.read(new File("Sprites/Objects/Burning.png")), c*30+20, r*30+20, null);
				}
				catch(Exception e){e.printStackTrace();}
	
		//Empty Space
		if(tileContentPane.tileMatrix[r][c].getName() == 1)
		{
			
		}
		//Breakables
		if(entityContentPane[r][c][0] != null)
		{
			entityContentPane[r][c][0] = null;
			Random randInt = new Random();
			int probs = randInt.nextInt(101);
			if(probs < 7)
			{
				itemContentPane[r][c]= new Item(0);
				return;
			}
			if(probs < 14)
			{
				itemContentPane[r][c] = new Item(1);
				return;
			}
			if(probs < 23)
			{
				itemContentPane[r][c] = new Item(2);
				return;
			}
			
			return;
		}
		
		switch(direction)
		{
			case 4: case 2:
			{
				destroy(range-1, new Point(curbomb.x+1,curbomb.y),4);
				destroy(range-1, new Point(curbomb.x-1,curbomb.y),2);
				break;
			}
			case 1: case 3:
			{
				destroy(range-1, new Point(curbomb.x,curbomb.y+1),1);
				destroy(range-1, new Point(curbomb.x,curbomb.y-1),3);
				break;
			}
			case 0:
			{
				destroy(range-1, new Point(curbomb.x+1,curbomb.y),4);
				destroy(range-1, new Point(curbomb.x-1,curbomb.y),2);
				destroy(range-1, new Point(curbomb.x,curbomb.y+1),1);
				destroy(range-1, new Point(curbomb.x,curbomb.y-1),3);
				
			}
		}
	}
	
	public void removeBomb(int row, int column, Bomb bomb)
	{
		this.hasBombMatrix[row][column] = false;
		this.bombPane.remove(bomb);
	}
	
	public void mouseClicked(MouseEvent arg0) {}

	public void mouseEntered(MouseEvent arg0) {}

	public void mouseExited(MouseEvent arg0) {}

	public void mousePressed(MouseEvent arg0) {}

	public void mouseReleased(MouseEvent arg0) {}

	public void keyTyped(KeyEvent arg0) {}

	public void keyReleased(KeyEvent arg0) {}

	public void keyPressed(KeyEvent arg0) {
		System.out.println(arg0);
		System.out.println("bomb left:" + playerOne.numBombLeft);
		//PLAYER 1
		if(arg0.getKeyCode() == KeyEvent.VK_W)
		{
			try {playerOne.charMove(0);} 
				catch (IOException e) {e.printStackTrace();}
			if(canMove(playerOne.getRow()-1,playerOne.getColumn()))
				playerOne.setRow(playerOne.getRow()-1);
		}
		else if(arg0.getKeyCode() == KeyEvent.VK_S)
		{
			try {playerOne.charMove(1);} 
				catch (IOException e) {e.printStackTrace();}
			if(canMove(playerOne.getRow()+1,playerOne.getColumn()))
				playerOne.setRow(playerOne.getRow()+1);
		}
		else if(arg0.getKeyCode() == KeyEvent.VK_A)
		{
			try {playerOne.charMove(2);} 
				catch (IOException e) {e.printStackTrace();}
			if(canMove(playerOne.getRow(),playerOne.getColumn()-1))
				playerOne.setColumn(playerOne.getColumn()-1);
		}
		else if(arg0.getKeyCode() == KeyEvent.VK_D)
		{
			try {playerOne.charMove(3);} 
				catch (IOException e) {e.printStackTrace();}
			if(canMove(playerOne.getRow(),playerOne.getColumn()+1))
				playerOne.setColumn(playerOne.getColumn()+1);
		}
		Point playerOnePixelLocation = Player.getPixelFromIndex(playerOne.getRow(), playerOne.getColumn());
		playerOne.setLocation(playerOnePixelLocation.x, playerOnePixelLocation.y);
		if(itemContentPane[playerOne.getRow()][playerOne.getColumn()]!=null)
		{
			playerOne.statChange(itemContentPane[playerOne.getRow()][playerOne.getColumn()].type);
			itemContentPane[playerOne.getRow()][playerOne.getColumn()]=null;
		}
		
		//PLAYER 2
		if(arg0.getKeyCode() == KeyEvent.VK_UP)
		{
			try {playerTwo.charMove(0);} 
				catch (IOException e) {e.printStackTrace();}
			if(canMove(playerTwo.getRow()-1,playerTwo.getColumn()))
				playerTwo.setRow(playerTwo.getRow()-1);
		}
		else if(arg0.getKeyCode() == KeyEvent.VK_DOWN)
		{
			try {playerTwo.charMove(1);} 
				catch (IOException e) {e.printStackTrace();}
			if(canMove(playerTwo.getRow()+1,playerTwo.getColumn()))
				playerTwo.setRow(playerTwo.getRow()+1);
		}
		else if(arg0.getKeyCode() == KeyEvent.VK_LEFT)
		{
			try {playerTwo.charMove(2);} 
				catch (IOException e) {e.printStackTrace();}
			if(canMove(playerTwo.getRow(),playerTwo.getColumn()-1))
				playerTwo.setColumn(playerTwo.getColumn()-1);
		}
		else if(arg0.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			try {playerTwo.charMove(3);} 
				catch (IOException e) {e.printStackTrace();}
			if(canMove(playerTwo.getRow(),playerTwo.getColumn()+1))
				playerTwo.setColumn(playerTwo.getColumn()+1);
		}
		
		Point playerTwoPixelLocation = Player.getPixelFromIndex(playerTwo.getRow(), playerTwo.getColumn());
		playerTwo.setLocation(playerTwoPixelLocation.x, playerTwoPixelLocation.y);
		if(itemContentPane[playerTwo.getRow()][playerTwo.getColumn()]!=null)
		{
			playerTwo.statChange(itemContentPane[playerTwo.getRow()][playerTwo.getColumn()].type);
			itemContentPane[playerTwo.getRow()][playerTwo.getColumn()]=null;
		}
		
		//BOMBS
		if(arg0.getKeyCode() == KeyEvent.VK_SPACE)
		{
			int row = playerOne.getRow();
			int column = playerOne.getColumn();
			if(!hasBombMatrix[row][column] && playerOne.numBombLeft>0)
			{
				hasBombMatrix[row][column] = true;
				Bomb bomb = null;
				try
				{
					bomb = new Bomb(playerOne, this);
					Point currentPixelLocation = Player.getPixelFromIndex(row, column);
					bomb.setBounds(currentPixelLocation.x, currentPixelLocation.y, 30, 30);
					playerOne.numBombLeft--;
				}
				catch (InterruptedException e){e.printStackTrace();}
				bombPane.add(bomb);
				
				for(int x=0;x<NUM_ROW;x++)
				{
					for(int y=0;y<NUM_COLUMN;y++)
						System.out.print(hasBombMatrix[x][y] ? "T " : "F ");
					System.out.println();
				}
			}
		}
		
		if(arg0.getKeyCode() == KeyEvent.VK_NUMPAD0)
		{
			int row = playerTwo.getRow();
			int column = playerTwo.getColumn();
			if(!hasBombMatrix[row][column] && playerTwo.numBombLeft>0)
			{
				hasBombMatrix[row][column] = true;
				Bomb bomb = null;
				try
				{
					bomb = new Bomb(playerTwo, this);
					Point currentPixelLocation = Player.getPixelFromIndex(row, column);
					bomb.setBounds(currentPixelLocation.x, currentPixelLocation.y, 30, 30);
					playerTwo.numBombLeft--;
					System.out.println("P2 Bomb Left: " + playerTwo.numBombLeft);
				}
				catch (InterruptedException e){e.printStackTrace();}
				bombPane.add(bomb);
			}
		}
		 
		//REPAINT
		repaint();
	}

	public void run()
	{
		while(true)
		{
			try 
			{
				repaintThread.sleep(300);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			repaint();
		}
	}
}