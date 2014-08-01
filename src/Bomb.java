import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.IOException;

import javax.swing.*;
import java.util.*;
import javax.swing.Timer;

public class Bomb extends JLabel implements Entity, ActionListener, Runnable
{	public ImageIcon bombIcon;
	
	private Timer bombCheck;
	private int timeCounter;
	private Player player;
	private final Point bombLocation;
	public Map map;
	public boolean exploded;
	
	public Bomb(Player player, Map map) throws InterruptedException
	{
		this.player = player;
		this.map = map;
		this.bombLocation = new Point(player.getRow(),player.getColumn());
		bombIcon = new ImageIcon("Sprites/Object Gifs/bombplaced.gif");
		super.setIcon(bombIcon);
		bombCheck = new Timer(100,this);
		bombCheck.start();
	}
	
	public void explode() throws IOException
	{
		bombCheck.stop();
		System.out.println("Bomb Exploded");
		exploded = true;
		bombIcon = new ImageIcon("Sprites/Object Gifs/bombexplode.gif");
		super.setIcon(bombIcon);
		Thread bombExplosionWait = new Thread(this);
		bombExplosionWait.start();
		
			map.destroy(player.stat.bombRange, bombLocation, 0);
			
		
		
	}
	
	public String toString()
	{
		return super.toString() + " bombIcon:" + bombIcon.toString(); 
	}
	
	public BufferedImage returnImage() {return null;}
	
	public void actionPerformed(ActionEvent arg0) 
	{
		timeCounter++;
		//System.out.println(timeCounter + " " + player.stat.fuseLength);
		if(timeCounter >= player.stat.fuseLength)
		{
			try {
				this.explode();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void run()
	{
		int row = bombLocation.x;
		int column = bombLocation.y;
		try
		{
			Thread.sleep(1000);
			System.out.println("Sleeping...");
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		player.numBombLeft++;
		bombIcon = null;
		this.setIcon(new ImageIcon("Sprites/Object Gifs/bombplaced.gif"));
		map.removeBomb(row, column, this);
		Thread.yield();
	}
}

class BombWorker extends SwingWorker<String,Object>
{
	Map myMap;
	int r;
	int c;
	Bomb b;
	public BombWorker(int row, int column, Bomb bomb)
	{
		myMap=bomb.map;
		r = row;
		c = column;
		b = bomb;
	}
	protected String doInBackground() throws Exception
	{
		long s = System.currentTimeMillis();
		System.out.println("Working");
		while(System.currentTimeMillis() - s < 500);
		myMap.removeBomb(r,c,b);
		return "";
	}
	protected void done() {
		try{
			this.cancel(true);
		}
		catch(Exception ignore){}
	}
}