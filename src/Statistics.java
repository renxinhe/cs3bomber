
public class Statistics
{
	public int fuseLength; //time between bomb explosions in tenths of a second
	public int bombRange; //radius of bomb explosion in tiles
	public int bombNumber; //number of bombs that can be rendered onscreen per player
	public Item item; //Current equipped item
	
	public Statistics()
	{
		fuseLength = 30;
		bombRange = 2;
		bombNumber = 1;
		item = null;
	}
	
	
	public void changeFuseLength(int changeAmount) 
	{
		int temp = fuseLength + changeAmount;
		if(temp < 1)
			fuseLength = 1;
		else if(temp > 60)
			fuseLength = 60;
		else
			fuseLength = temp;
	}
	
	public void changeBombRange(int changeAmount) 
	{
		int temp = bombRange + changeAmount;
		if(temp < 1)
			bombRange = 1;
		else if(temp > 5)
			bombRange = 5;
		else
			bombRange = temp;
	}
	
	public void changeBombNumber(int changeAmount)
	{
		int temp = bombNumber + changeAmount;
		if(temp < 1)
			bombNumber = 1;
		else if(temp > 5)
			bombNumber = 5;
		else
			bombNumber = temp;
	}
}
