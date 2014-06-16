package telepathy;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

public class Table {
	private String[] colors;
	private Symbol[] symbols;
	private JPanel panel;
	
	public Table() {
		colors = new String[] {"yellow", "orange", "red", "purple", "pink", "blue", "green", "silver", "white"};
		symbols = new Symbol[] {new Sun(), new Star(), new Eye(), new Moon(), 
				new Circle(), new Bolt(), new Diamond(), new Hand(), new Heart()};
		panel = new JPanel(new GridLayout(10, 2));
		panel.setSize(150, 300);
		
		JButton clr = new JButton("Color");
		clr.setFont(new Font("SansSeriff", Font.BOLD, 14));
		panel.add(clr);
		JButton sym = new JButton("Symbol");
		sym.setFont(new Font("SansSeriff", Font.BOLD, 14));
		panel.add(sym);
		
		for (int i = 0; i < 9; i++){
			JButton c = new JButton(colors[i]);
			c.setBackground(stringToColor(colors[i]));
			panel.add(c);
			JButton s = new JButton(symbols[i].getName());
			s.setIcon(symbols[i].getImage());
			panel.add(s);
		}
		
		
	}
	public JPanel getPanel(){
		return panel;
	}

	private Color stringToColor(String s) throws IllegalArgumentException {
		s = s.toLowerCase();
		if (s.equals("yellow")){
			return Color.YELLOW;
		} else if (s.equals("orange")){
			return Color.ORANGE;
		} else if (s.equals("red")){
			return Color.RED;
		} else if (s.equals("purple")){
			return Color.MAGENTA;
		} else if (s.equals("pink")){
			return Color.PINK;
		} else if (s.equals("blue")){
			return Color.CYAN;
		} else if (s.equals("green")){
			return Color.GREEN;
		} else if (s.equals("silver")){
			return Color.LIGHT_GRAY;
		} else if (s.equals("white")){
			return Color.WHITE;
		} else {
			return null;
			//throw IllegalArgumentException;
		}	
	}
}
