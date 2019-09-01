public class CheckerPiece
{
	private boolean isKing;
	private boolean isChosen;
	private String colour;
	
	public CheckerPiece()
	{
		colour = "none";
		isKing = false;
	}
	
	public void setColour(String col)
	{
		colour = col;
	}
	
	public String getColour()
	{
		return colour;
	}
	
	public void promoteToKing()
	{
		isKing = true;
	}
	
	public void demoteFromKing()
	{
		isKing = false;
	}
	
	public boolean isKing()
	{
		return isKing;
	}
	
	public boolean isChosen()
	{
		return isChosen;
	}
	
	public void toggleChosen()
	{
		if(isChosen)
		{
			isChosen = false;
		}
		else
		{
			isChosen = true;
		}
	}
	
	public String toString()
	{
		if (isKing)
		{
			if (colour == "Red")
			{
				return "RedKing";
			}
			else if (colour == "Black")
			{
				return "BlackKing";
			}
			
			return " ";
		}
		else
		{
			if (colour == "Red")
			{
				return "Red";
			}
			else if (colour == "Black")
			{
				return "Black";
			}
			
			return " ";
		}
		
		
	}
}