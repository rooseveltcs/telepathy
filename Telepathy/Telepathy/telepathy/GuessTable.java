package telepathy;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

public class GuessTable {
	private JPanel gTable;
	private Tile secretSquare;
	private int numG;
	private JButton[] buttons;//keeps track of which buttons to add guess to
	
	private static Font font;//messing around
	
	public GuessTable(Tile sS, Font f) throws FileNotFoundException {
		
		font = f;
		
		numG = 0;
		secretSquare = sS;
		
		gTable = new JPanel(new GridLayout(16, 2));
		gTable.setSize(150, 600);
		
		JButton gs = new JButton("Guess");
		gs.setFont(font);
		gTable.add(gs);
		JButton yn = new JButton("Yes/No");
		yn.setFont(font);
		gTable.add(yn);
		
		buttons = new JButton[30];
		for (int i = 0; i < 30; i++){
			JButton b = new JButton();
			b.setFont(font);
			buttons[i] = b;
			gTable.add(b);
		}
		
	}
	
	private class DepictTile extends MouseInputAdapter {
		private Tile tile;
		private JFrame popUp;
		
		private DepictTile (Tile t ){
			tile = t;
			popUp = new JFrame();
			popUp.setSize(100, 100);
		}
		
		public void mouseEntered(MouseEvent event){
			popUp.setLocationRelativeTo(tile.getButton());
			popUp.add(tile.imageRep);
			popUp.setTitle(tile.toString());
			popUp.setVisible(true); 
		}
		public void mouseExited(MouseEvent event){
			popUp.setVisible(false);
		}
	}
	
	public void makeGuess(Tile guess){
		
		if (numG <= 14) {
			//Puts text on buttons within the guess table
			JButton g = buttons[numG * 2];
			g.setText(guess.toString());//In GUESS COLUMN
			g.addMouseListener(new DepictTile (guess));
			buttons[numG * 2 + 1].setText(accuracy(guess));//In Y/N COLUMN
		} else {
			JButton g = new JButton(guess.toString());
			JButton a = new JButton(accuracy(guess));			
		}
		
		numG++;
	}
	
	public int getNumG(){
		return numG;
	}
	
	private String accuracy (Tile g){
		if (g.row.equals(secretSquare.row)){
			return "Y";
		} else if (g.column == secretSquare.column){
			return "Y";
		} else if (g.color.equals(secretSquare.color)){
			return "Y";
		} else if (g.symbol.getName().equals(secretSquare.symbol.getName())){//symbol.equals(symbol) wasn't working?
			return "Y";
		}
		return "N";
	}
	
	public JPanel getPanel(){
		return gTable;
	}
}