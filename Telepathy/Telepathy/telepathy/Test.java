package telepathy;
import java.io.File;
import java.io.FileNotFoundException;

public class Test {
	public static void main (String[] args) throws FileNotFoundException{
		
		File f = new File("gridLayout.txt");
		Board b = new Board(f);
		if (!b.board.isShowing()){
			System.out.println("Game Over");
		}
	}
}
