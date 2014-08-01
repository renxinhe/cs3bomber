import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;


public class TilePanel extends JPanel
{
	static final int NUM_ROW = 21;
	static final int NUM_COLUMN = 21;
	
	static final int POKEGRASS = 1;
	
	public Tile[][] tileMatrix;
	protected File mapFile;
	
	public TilePanel()
	{
		tileMatrix = null;
		mapFile = null;
		this.setOpaque(false);
		this.setLayout(null);
	}
	
	public TilePanel(File mapFile) throws IOException
	{
		tileMatrix = new Tile[NUM_ROW][NUM_COLUMN];
		this.setOpaque(false);
		this.setLayout(null);
		this.setSize(new Dimension(20+NUM_COLUMN*30, 20+NUM_ROW*30));
		this.setLocation(0, 0);
		this.mapFile = mapFile;
		//TODO: Make static variable
		readMap(TilePanel.POKEGRASS);
	}
	
	protected void readMap(int tileSet) throws IOException
	{
		Scanner input = new Scanner(mapFile);
		System.out.println("Map Imported.");
		for(int i=0; i<NUM_ROW; i++)
		{
			String row = input.nextLine();
			for(int j=0; j<NUM_COLUMN; j++)
			{
				//Put in mapping convention
				int name = row.charAt(j)-48;
				tileMatrix[i][j] = new Tile(tileSet, name);
			}
		}
		System.out.println("Tiles Painted.");
	}
	
	public void paintComponent(Graphics g)
	{
		for(int i=20; i<(NUM_ROW)*30; i+=30)
			for(int j=20; j<(NUM_COLUMN)*30; j+=30)
				g.drawImage(tileMatrix[(i-20)/30][(j-20)/30].image, j, i, null);
	}
}