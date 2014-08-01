import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Item implements Entity
{
	int type; // 2 - bombUp 1 - rangeUp 0 - fuseDown
	public BufferedImage img;
	
	public Item(int t) throws IOException
	{
		type = t;
		setImage(t);
	}
	
	public void setImage(int t) throws IOException
	{
		switch(t)
		{
		case 0: img = ImageIO.read(new File("Sprites/Objects/Clock.png")); break;
		case 1: img = ImageIO.read(new File("Sprites/Objects/Fire.png")); break;
		case 2: img = ImageIO.read(new File("Sprites/Objects/Bomb.png")); break;
		}
	}
	
	public BufferedImage returnImage()
	{
		return img;
	}
}
