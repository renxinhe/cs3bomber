import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.image.*;

import javax.imageio.ImageIO;

public class Breakable implements Entity 
{
	public BufferedImage breakSprite;
	protected boolean willDrop;
	
	public Breakable() throws IOException
	{
		breakSprite = ImageIO.read(new File("TileSet/breakent_"+ TilePanel.POKEGRASS +".png"));
		willDrop = dr0pIt();
	}
	public boolean dr0pIt()
	{
		double d = Math.random();
		if(d<.5)
			return true;
		else
			return false;
	}
	
	public BufferedImage returnImage()
	{
		return breakSprite;
	}
	
	
}
