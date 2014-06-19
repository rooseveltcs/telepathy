package telepathy;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JLabel;

/* Tile.java
 * @ Mia Vanderwilt
 * @ 06/18/14
 * <p>
 * A Tile represents a single square on the 18x18 grid. Each tile has four characteristics:
 * a symbol, color, column, and row. Each of  these characteristics is initialized with a 
 * value given as a parameter in the constructor. Each tile also has a button which allows a 
 * user to guess a tile by pressing the JButton on the GUI.Each tile also has an image 
 * representation in the form of a JLabel (used in the solveCheck window and guess table pop-ups.
 */
public class Tile {
	
	public Symbol symbol; 
	public Color color; 
	public int column; 
	public String row; 
	
	/* JButton used in GUI; action listener is in the Board class for access to the GuessTable */
	private JButton button;
	/* JLabel used to represent a tile on the GuessTable and the solveCheck JFrame */
	public JLabel imageRep;
	
	/* Constructor: 4 Characteristics of the tile initialized with parameters; sets respective JLabel and JButton */
	public Tile (String r, int clmn, Color clr, Symbol s){
	      symbol = s; 
	      color = clr; 
	      column = clmn; 
	      row = r;      
	      
	      button = new JButton(symbol.getImage());
	      button.setBackground(color);

	      imageRep = new JLabel(symbol.getImage());
	      imageRep.setOpaque(true);
	      imageRep.setBackground(color);
	}
	
	/* Row and Column as depicted on the GuessTable */
	public String toString(){
		return row + " " + column;
	}
	
	public JButton getButton(){
		return button;
	}
}