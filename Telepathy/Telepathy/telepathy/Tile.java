package telepathy;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JLabel;

public class Tile {//extends MouseInputAdapter {
	
	public Symbol symbol; 
	public Color color; 
	public int column; 
	public String row; 
	
	private JButton button;
	public JLabel imageRep;
	
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
	
	public String toString(){
		return row + " " + column;
	}
	
	public JButton getButton(){
		return button;
	}
}