package telepathy;
import java.io.File;
import java.io.FileNotFoundException;

/* Test.java
 * @ Mia Vanderwilt
 * @ 06/18/14
 * <p>
 * Serves as a client code to initialize a File (which is a textual representation of the board)
 * and then creates a board using this particular layout. The file is read in the Board class.
 * Currently nothing is done to terminate a game, or transition to the next.
 * @throws FileNotFoundException if the file representing the grid cannot be found.
 */
public class Test {
	public static void main (String[] args) throws FileNotFoundException{
		File f = new File("gridLayout.txt");//File is a text file with row, column, color, symbol seperated by spaces; each line is another tile (left to right)
		Board b = new Board(f);
		if (!b.board.isShowing()){ //TODO: Does not have intended functionality...nothing prompts the Board to close; when Board is closed, nothing happens
			System.out.println("Game Over");
		}
	}
}
