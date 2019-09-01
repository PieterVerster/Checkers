import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CheckerBoard extends JFrame
{
	boolean chosenIsKing = false;
	boolean isForward = false;
	
	int turn = 0;		//Black First, then Red
	
	private final int ROWS = 10;
	private final int COLS = 10;
	
	int rowStartIndex = 0;
	int rowEndIndex = ROWS;
	int colStartIndex = 0;
	int colEndIndex = COLS;
	
	private JButton [][] grid = new JButton[ROWS][COLS];
	private CheckerPiece[][] pieces = new CheckerPiece[ROWS][COLS];
	
	public CheckerBoard()
	{
		GridLayout formLayout = new GridLayout(rowEndIndex - rowStartIndex, colEndIndex - colStartIndex);
		setLayout(formLayout);
		setTitle("Checkers");
		
		initialiseBoard();
		
		displayBoard();
	}
	
	public void highlightChosenPiece(int row, int col)
	{
		for(int x = 1; x < grid.length - 1; x++)
		{
			for(int y = 1; y < grid.length - 1; y++)
			{
				if (pieces[x][y].isChosen())		//if any blocks are chosen
				{
					pieces[x][y].toggleChosen();	//unchoose them
				}
				
				if (x == row && y == col)			//if position is on clicked piece
				{
					if (pieces[row][col].isKing())	//and if piece is king
					{
						chosenIsKing = true;		//set to true
					}
					else							//else if piece is not king
					{
						chosenIsKing = false;		//set to false
					}
					
					pieces[row][col].toggleChosen();			//toggle chosen piece
					grid[row][col].setForeground(Color.BLUE);
					//System.out.println("Chosen: " + pieces[row][col].isChosen());
				}
			}
		}
	}
	
	public class MouseHandler implements MouseListener
	{
		public void mousePressed(MouseEvent me)
		{
			JButton button = (JButton) me.getSource();
			
			//Run through array of buttons
			for (int i = 1; i < ROWS - 1; i++)
			{
				for (int j = 1; j < COLS - 1; j++)
				{
					if (button == grid[i][j])								//if button is found
					{	
						if (me.getButton() == MouseEvent.BUTTON1)			//left click
						{
							if (pieces[i][j].getColour() != "none")
							{
								resetSelection();
								
								if (turn % 2 == 0 && button.getBackground() == Color.BLACK || turn % 2 != 0 && button.getBackground() == Color.RED)
								{
									highlightChosenPiece(i, j);
									checkAllChosen();							//only a check, can be removed in final version
									highlightPossibleMoves(i, j);
								}
							}
							else if (grid[i][j].getBackground() == Color.BLUE)
							{
								moveChosenPiece(i, j);
								System.out.println("This works");
								
								checkPromotion(i, j);
							}
						}
						else if (me.getButton() == MouseEvent.BUTTON3)		//right click
						{
							
						}
						
						revalidate();		//redo layout
						repaint();			//reprint layout
						System.out.println("King: " + pieces[i][j].isKing());
					}
					else
					{
						if (me.getButton() == MouseEvent.BUTTON1)			//left click
						{
							setRedAndBlackPiecesBackground();
						}
					}
				}
			}
			
			checkWin();
		}
		
		public void mouseReleased(MouseEvent me)
		{
			//Empty
		}
		
		public void mouseEntered(MouseEvent me)
		{
			//Empty
		}
		
		public void mouseExited(MouseEvent me)
		{
			//Empty
		}
		
		public void mouseClicked(MouseEvent me)
		{
			//Empty
		}
	}
	
	public void checkPromotion(int row, int col)
	{
		if (pieces[row][col].getColour() == "Red" && row == ROWS - 2)
		{
			pieces[row][col].promoteToKing();
		}
		else if (pieces[row][col].getColour() == "Black" && row == 1)
		{
			pieces[row][col].promoteToKing();
		}
	}
	
	public void checkWin()
	{
		boolean redFound = false;
		boolean blackFound = false;
		String message = "";
		
		for (int i = 1; i < ROWS - 1; i++)
		{
			for (int j = 1; j < COLS - 1; j++)
			{
				if (pieces[i][j].getColour() == "Red")
				{
					redFound = true;
				}
				else if (pieces[i][j].getColour() == "Black")
				{
					blackFound = true;
				}
			}
		}
		
		System.out.println("Red Found: " + redFound + ", Black Found: " + blackFound);
		
		if (redFound == false)
		{
			message = "BLACK WINS!";
			System.out.println("BLACK WINS");
		}
		else if (blackFound == false)
		{
			message = "RED WINS!";
			System.out.println("RED WINS");
		}
		
		if (redFound == false || blackFound == false)
		{
			JOptionPane.showMessageDialog(null, message);
		}
	}
	
	public void removeJumpedPiece(int row, int col)
	{
		pieces[row][col].setColour("none");
		grid[row][col].setBackground(null);
		grid[row][col].setText("");
	}
	
	public void moveChosenPiece(int row, int col)
	{
		int removeRow = 0;
		int removeCol = 0;
		
		for(int i = 1; i < grid.length - 1; i++)
		{
			for(int j = 1; j < grid.length - 1; j++)
			{
				if (pieces[i][j].isChosen())
				{	
					if (pieces[i][j].getColour() == "Red")
					{
						pieces[row][col].setColour("Red");
					}
					else if (pieces[i][j].getColour() == "Black")
					{
						pieces[row][col].setColour("Black");
					}
					
					if (chosenIsKing)
					{
						pieces[row][col].promoteToKing();
					}
					
					grid[row][col].setText(pieces[i][j].toString());
					
					try
					{
						removeRow = moveRow(i, row, "remove");
						removeCol = moveCol(j, col, "remove");	
					}
					catch (Exception e)
					{
						System.out.println("Removing piece failed... " + e);
					}
					
					System.out.println("REMOVE: ROW " + removeRow + ", COL " + removeCol);
					removeJumpedPiece(i, j);
					removeJumpedPiece(removeRow, removeCol);
					
					turn++;
					
					resetSelection();
				}
			}
		}
	}
	
	public int moveRow(int newRow, int oldRow, String type)
	{
		int rowUsed = 0;
		
		if (type == "remove")
		{
			rowUsed = oldRow;
		}
		else
		{
			rowUsed = newRow;
		}
		
		if (oldRow - newRow > 0)
		{
			return rowUsed - 1;
		}
		else
		{
			return rowUsed + 1;
		}
	}
	
	public int moveCol(int newCol, int oldCol, String type)
	{
		int colUsed = 0;
		
		if (type == "remove")
		{
			colUsed = oldCol;
		}
		else
		{
			colUsed = newCol;
		}
		
		if (oldCol - newCol > 0)
		{
			return colUsed - 1;
		}
		else
		{
			return colUsed + 1;
		}
	}
	
	public void setRedAndBlackPiecesBackground()
	{
		for(int i = 1; i < grid.length - 1; i++)
		{
			for(int j = 1; j < grid.length - 1; j++)
			{
				if (pieces[i][j].getColour() == "Red")
				{
					grid[i][j].setBackground(Color.RED);
					grid[i][j].setForeground(Color.BLACK);
					grid[i][j].setText(pieces[i][j].toString());
				}
				else if (pieces[i][j].getColour() == "Black")
				{
					grid[i][j].setBackground(Color.BLACK);
					grid[i][j].setForeground(Color.RED);
					grid[i][j].setText(pieces[i][j].toString());
				}
				else
				{
					pieces[i][j].demoteFromKing();
				}
			}
		}
	}
	
	public void resetSelection()
	{
		for (int i = 1; i < grid.length - 1; i++)
		{
			for (int j = 1; j < grid.length - 1; j++)
			{
				setRedAndBlackPiecesBackground();
				
				if (grid[i][j].getBackground() == Color.BLUE && pieces[i][j].getColour() == "none")
				{
					grid[i][j].setBackground(null);
				}
			}
		}
	}
	
	public void checkForward(int row, int col, int x)
	{
		if (pieces[row][col].getColour() == "Red")
		{
			if (x > 0)
			{	
				isForward = true;
			}
			else
			{
				isForward = false;
			}
		}
		else if (pieces[row][col].getColour() == "Black")
		{
			if (x < 0)
			{	
				isForward = true;
			}
			else
			{
				isForward = false;
			}
		}
	}
	
	public void setBackgroundColour(int row, int col, int currentRow, int currentCol)
	{
		int movedRow = 0;
		int movedCol = 0;
		
		if (pieces[currentRow][currentCol].getColour() == "none")
		{	
			if ((!pieces[row][col].isKing() && isForward) || pieces[row][col].isKing())
			{
				grid[currentRow][currentCol].setBackground(Color.BLUE);
			}
		}
		else if (pieces[currentRow][currentCol].getColour() != pieces[row][col].getColour())		//IN PROGRESS
		{
			movedRow = moveRow(currentRow, row, "move");
			movedCol = moveCol(currentCol, col, "move");
			
			if ((!pieces[row][col].isKing() && isForward) || pieces[row][col].isKing())
			{
				grid[movedRow][movedCol].setBackground(Color.BLUE);
			}
			
			System.out.println("ThisRow: " + row + ", ThisCol: " + col + ", Block: " + pieces[row][col].toString());
			System.out.println("CurrentRow: " + currentRow + ", CurrentCol: " + currentCol + ", Block: " + pieces[currentRow][currentCol].toString());
		}
	}
	
	public void highlightPossibleMoves(int row, int col)		//THERE ARE SOME ISSUES HERE>>> CHECK
	{
		for (int x = -1; x <= 1; x++)
		{
			for (int y = -1; y <= 1; y++)
			{
				try 
				{
					int currentRow = row + x;
					int currentCol = col + y;
					
					if (x != 0 && y != 0)		//if not on chosen piece
					{
						checkForward(row, col, x);
						setBackgroundColour(row, col, currentRow, currentCol);
					}
					
					System.out.println("Row: " + currentRow + ", Col: " + currentCol + ", Block: " + pieces[currentRow][currentCol].toString());
				}
				catch (Exception e)
				{
					System.out.println("Something's up: " + e);
				}
			}
		}
	}
	
	public void checkAllChosen()		//only a check, can be removed in final version
	{
		for(int x = 1; x < grid.length - 1; x++)
		{
			for(int y = 1; y < grid.length - 1; y++)
			{
				if(pieces[x][y].isChosen())
				{
					System.out.println("Chosen Square: x = " + x + ", y = " + y);
				}
			}
		}
	}
	
	public void setPiecesOnBoard(String colour)
	{
		Color foreColor = Color.WHITE;
		Color backColor = Color.WHITE;
		int start = 1;
		int end = 4;
		
		if (colour == "Red")
		{
			backColor = Color.RED;
			foreColor = Color.BLACK;
		}
		else if (colour == "Black")
		{
			backColor = Color.BLACK;
			foreColor = Color.RED;
			start = ROWS - 4;
			end = ROWS - 1;
		}
		
		for (int i = start; i < end; i++)		//for the first three rows
		{
			for (int j = 1; j < COLS - 1; j++)
			{
				if (i % 2 != 0 && j % 2 != 0)		//if rows are uneven, and coloumns are uneven
				{
					pieces[i][j].setColour(colour);
					grid[i][j].setBackground(backColor);
					grid[i][j].setForeground(foreColor);
				}
				else if (i % 2 == 0 && j % 2 == 0)	//else if rows are even and coloumns are even
				{
					pieces[i][j].setColour(colour);
					grid[i][j].setBackground(backColor);
					grid[i][j].setForeground(foreColor);
				}
			}
		}
	}
	
	public void initialiseBoard()
	{
		MouseHandler mouseHandler = new MouseHandler();
		
		for (int i = rowStartIndex; i < rowEndIndex; i++)
		{
			for (int j = colStartIndex; j < colEndIndex; j++)
			{
				pieces[i][j] = new CheckerPiece();
			}
		}
		
		for (int i = rowStartIndex; i < rowEndIndex; i++)
		{
			for (int j = colStartIndex; j < colEndIndex; j++)
			{
				grid[i][j] = new JButton(pieces[i][j].toString());
				grid[i][j].addMouseListener(mouseHandler);	//add MouseListener to each button
				add(grid[i][j]);
				
				if (i == 0 || i == rowEndIndex - 1 || j == 0 || j == colEndIndex - 1)
				{
					if (i == 0 && j == 0)
					{
						grid[i][j].setText("T");
					}
					else
					{
						grid[i][j].setVisible(false);
					}
				}
			}
		}
		
		//Red Pieces
		setPiecesOnBoard("Red");
		
		//Black Pieces
		setPiecesOnBoard("Black");
	}
	
	public void displayBoard()
	{
		for (int i = rowStartIndex; i < rowEndIndex; i++)
		{
			for (int j = colStartIndex; j < colEndIndex; j++)
			{
				grid[i][j].setText(pieces[i][j].toString());
			}
		}
	}
	
	public void testingAllSquares()		//Test Method... Can be removed in final version
	{
		for (int i = 0; i < ROWS; i++)
		{
			for (int j = 0; j < COLS; j++)
			{
				System.out.println("Test Block: (" + i + ";" + j + ")" + pieces[i][j].getColour());
			}
		}
	}
}