import java.awt.Graphics;
import java.awt.image.*;
import java.io.*;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Tile
{
	private int name;
	private int tileSet;
	public BufferedImage image;
	public boolean solid;
	
	public Tile()
	{
		name = -1;
		tileSet = 0;
		image = null;
		solid = false;
	}
	
	public Tile(int tileSet, int name) throws IOException
	{
		//File Name Sample: TileSet/tile_1_0.png
		this.name = name;
		this.tileSet = tileSet;
		this.image = ImageIO.read(new File("TileSet/tile_" + tileSet + "_" + name + ".png"));
	}
	public int getName()
	{
		return name;
	}
}