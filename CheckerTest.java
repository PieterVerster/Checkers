import javax.swing.JFrame;

public class CheckerTest
{
	public static void main(String [] args)
	{
		CheckerBoard board = new CheckerBoard();
		board.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		board.setSize(1000, 1000);
		board.setLocationRelativeTo(null);
		board.setVisible(true);
	}
}