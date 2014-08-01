import javax.imageio.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import java.io.*;

public class Game
{
	public static void main(String[] args) throws IOException, LocationOccupiedException, InterruptedException
	{
		
		
		        // from a wave File
		try {	File soundFile = new File("05-green-garden.wav");
		        AudioInputStream audioIn;
				audioIn = AudioSystem.getAudioInputStream(soundFile);
		        Clip clip = AudioSystem.getClip();
		        clip.open(audioIn);
		        clip.start(); 
		        clip.loop(Clip.LOOP_CONTINUOUSLY);} 
		        catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();}
		 
		//TODO: Main Window (JFrame)
		JFrame mainWindow = new JFrame();
		mainWindow.setFocusable(false);
		mainWindow.setSize(1000, 700); // 15x11; 30pix each
		mainWindow.setResizable(false);
		mainWindow.setBackground(new Color(247, 231, 206));
		//mainWindow.setBackground(Color.BLUE);
		mainWindow.setIconImage(ImageIO.read(new File("icon.png")));
		mainWindow.setLocation(200,100);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setTitle("Bomber");
		
		Map map = new Map(new File("Maps/TestMap.txt"));
		
		//TODO: GUIPanel
		GUIPanel gui = new GUIPanel(map);
		gui.setSize(new Dimension(350, 700));
		gui.setLocation(650, 0);
		gui.setLayout(null);
		mainWindow.getContentPane().add(gui);
		
		
		//TODO: Map
		
		map.setSize(new Dimension(20+TilePanel.NUM_COLUMN*30, 20+TilePanel.NUM_ROW*30));
		map.setLocation(0, 0);
		map.setOpaque(false);
		map.setLayout(null);
		mainWindow.getContentPane().add(map);
		
		
		
		//TODO: TilePanel
		mainWindow.getContentPane().add(map.tileContentPane);
		
		//TODO: PlayerPanel
		mainWindow.getContentPane().add(map.playerPane);
		
		//TODO: BombPanel
		mainWindow.getContentPane().add(map.bombPane);
		
		//TODO: SetVisible
		mainWindow.setVisible(true);
	}
}

class GUIPanel extends JPanel implements MouseListener, Runnable
{
	String mousePosition = "";
	public Thread repThread;
	public Map m;
	
	public GUIPanel(Map map)
	{
		this.setFocusable(true);
		this.addMouseListener(this);
		repThread = new Thread(this);
		m = map;
		repThread.start();
	}
	
	public void paintComponent(Graphics g)
	{
		
		//Draw Player Stat Box Background
		
		g.setColor(new Color(240,230,140));
		g.fillRoundRect(30, 30, 285, 600, 30, 30);
		g.setColor(Color.BLACK);
		g.drawRoundRect(29, 29, 287, 602, 30, 30);
		//Names, LifeLeft, BombLeft/NumBomb, BombRange
		g.drawString("PLAYER ONE", 50,50);
		g.drawString("PLAYER TWO", 50,350);
		
		g.drawString("LIVES LEFT: "+m.playerOne.lifeCount, 60, 75);
		g.drawString("BOMBS LEFT: "+m.playerOne.numBombLeft+"/"+m.playerOne.stat.bombNumber,60,100);
		g.drawString("BOMB RANGE: "+(m.playerOne.stat.bombRange-1),60,125);
		
		g.drawString("LIVES LEFT: "+m.playerTwo.lifeCount, 60, 375);
		g.drawString("BOMBS LEFT: "+m.playerTwo.numBombLeft+"/"+m.playerTwo.stat.bombNumber,60,400);
		g.drawString("BOMB RANGE: "+(m.playerTwo.stat.bombRange-1),60,425);
		
	}
	
	public void run()
	{
		while(true)
		{
			try 
			{
				repThread.sleep(100);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			repaint();
		}
	}
	
	public void mouseClicked(MouseEvent e)
	{
		mousePosition = e.getPoint().toString();
		System.out.println(mousePosition);
		System.out.println((int)((e.getY()-20)/30) + " " + (int)((e.getX()-20)/30));
	}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}
}