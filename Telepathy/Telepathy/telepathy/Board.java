package telepathy;
//TODO: Only class left to be commented out
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;

import java.io.File;
import java.io.FileNotFoundException;

import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

public class Board {
	
	private static final int BOARD_LENGTH = 1100;
	private static final int BOARD_WIDTH = 1000;
	
	private static final String[] colors = new String[] {"Yellow", "Orange", "Red", "Purple", "Pink", "Blue", "Green", "Silver", "White"};
	private static final Symbol[] symbols = new Symbol[] {new Sun(), new Star(), new Eye(), new Moon(), 
			new Circle(), new Bolt(), new Diamond(), new Hand(), new Heart()};
	
	private static Font FONT;
	
	private File file;
	public JFrame board;//TODO: Change back to private
	private Tile secretSquare;
	
	private Tile[][] gridText;	
	private JPanel gridDisp;
	
	private JPanel titlePanel;
	private GuessTable gTable;
	private JPanel table;
	
	
	public Board (File f) throws FileNotFoundException {
		FONT = setFont();
		file = f;
		
		//BOARD
		board = setBoard(); 
		
		//TITLE PANEL
		titlePanel = setTitleP();
	    board.add(titlePanel, BorderLayout.NORTH); 
        
	    //GRID		
		setGrid();//Initializes  gridText and gridDisp
		
		//Attempt at adding axes
		JPanel gAxes = new JPanel(new BorderLayout());
		gAxes.add(gridDisp, BorderLayout.CENTER);

		gAxes.add(columns(), BorderLayout.NORTH);
		gAxes.add(columns(), BorderLayout.SOUTH);
		
		gAxes.add(rows(), BorderLayout.EAST);
		gAxes.add(rows(), BorderLayout.WEST);
		
		board.add(gAxes, BorderLayout.CENTER);
		
		//SECRET SQUARE
		secretSquare = getSecretSq();
		System.out.println("sS: " + secretSquare + " " + secretSquare.symbol.getName());
		
		JPanel twoTables = new JPanel(new GridLayout(2, 1));
		
		//GUESS TABLE
		gTable = new GuessTable(secretSquare, FONT);
		twoTables.add(gTable.getPanel());
		
		//COLOR/SYMBOL TABLE
		table = setTable();
		twoTables.add(table);
		
		board.add(twoTables, BorderLayout.EAST);
		
		board.setVisible(true);
	}

	private JPanel setTitleP(){
        //TITLE PANEL
        JPanel tPanel = new JPanel(new FlowLayout()); 
        tPanel.setBackground(Color.RED); 
        
        JLabel tLabel = new JLabel(new ImageIcon("fullTitle.png")); 
        tLabel.setBackground(Color.RED); 
        
        tPanel.add(tLabel); 
        return tPanel;
	}
	
	private JFrame setBoard(){
		JFrame board = new JFrame();
		
		//SETTINGS 
        board.setLayout(new BorderLayout()); 
        board.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        board.setLocation(new Point (150, 20)); 
        board.setSize(new Dimension(BOARD_LENGTH, BOARD_WIDTH)); 
        board.setTitle("Telepathy"); 
        
        ImageIcon icon = new ImageIcon("telepathyIcon.jpg"); 
        board.setIconImage(icon.getImage()); 
        
        return board;
	}
	
	private void setGrid() throws FileNotFoundException {
		
		gridText = new Tile[18][18];
		gridDisp = new JPanel(new GridLayout(18, 18));
		gridDisp.setSize(800, 800);
		
		@SuppressWarnings("resource")//?
		final Scanner input = new Scanner(file);
		
		for (int row = 0; row < 18; row++) {
			for (int column = 0; column < 18; column++) {
				
				//Read file to get characteristics of particular tile
				String r = input.next();//ROW
				int clmn = input.nextInt();//COLUMN
				String colorString = input.next();
				Color clr = stringToColor(colorString);//COLOR [uses private helper method]
				String symString = input.next();
				Symbol s = stringToSymbol(symString);
				
				Tile tile = new Tile(r, clmn, clr, s);
				gridText[row][column] = tile;
				
				TileListener tL = new TileListener(tile);
				
				JButton b = tile.getButton();
				b.addMouseListener(tL);
				b.setToolTipText(r + ", " + clmn + ", " + colorString + ", " + symString);
				
				gridDisp.add(tile.getButton());
			}
		}
	}
	
	
	private JPanel columns(){
		JPanel columns = new JPanel(new GridLayout(1, 18));
		JLabel blank = new JLabel();
		columns.add(blank);
		for (int c = 1; c < 19; c++){
			JLabel col = new JLabel("" + c);
			//col.setOpaque(true);
			col.setFont(FONT);
			col.addMouseListener(new EliminateColumn());
			columns.add(col);
		}
		return columns;
	}
	
	private JPanel rows(){
		JPanel rows = new JPanel(new GridLayout(18, 1));
		for (int r = 0; r < 18; r++){
			JLabel row = new JLabel(" " + (char) (r + 65) + " ");
			row.setName(r + "");
			row.setFont(FONT);
			row.addMouseListener(new EliminateRow());
			rows.add(row);
		}
		return rows;
	}
	
	private JPanel setTable(){
		JPanel table = new JPanel(new GridLayout(10, 2));
		table.setSize(150, 300);
		
		JButton clr = new JButton("Color");
		clr.setFont(FONT);
		table.add(clr);
		JButton sym = new JButton("Symbol");
		sym.setFont(FONT);
		table.add(sym);
		
		for (int i = 0; i < 9; i++){
			JButton c = new JButton(colors[i]);
			c.addMouseListener(new EliminateColor());
			c.setFont(FONT);
			c.setBackground(stringToColor(colors[i]));
			table.add(c);
			JButton s = new JButton(symbols[i].getName(), symbols[i].getImage());
			s.addMouseListener(new EliminateSymbol());
			s.setFont(FONT);
			table.add(s);
		}
			
		return table;
	}
	
	private class EliminateColor extends MouseInputAdapter {
		//TODO: doesn't undo perfectly
		//TODO: Only Does Color; see if there's a potential for polymorphism
		public void mousePressed(MouseEvent event){
			
			JButton button = (JButton) event.getComponent();
			Color clrElim = stringToColor(button.getText());
			for (int r = 0; r < 18; r++){
				for (int c = 0; c < 18; c++){
					Tile tile = gridText[r][c];
					JButton b = tile.getButton();
					
					if (clrElim.equals(tile.color)){
						//if (b.getBackground().equals(Color.DARK_GRAY)){//IF already blacked out
						//	b.setBackground(tile.color);
						//	button.setBackground(tile.color);
						//} else {
							b.setBackground(Color.DARK_GRAY);
							button.setBackground(Color.DARK_GRAY);
						//}
					}
				}
			}
			
		}
	}
	
	private class EliminateSymbol extends MouseInputAdapter {
		public void mousePressed(MouseEvent event) {
			JButton button = (JButton) event.getComponent();
			String symElim = button.getText();
			for (int r = 0; r < 18; r++){
				for (int c = 0; c < 18; c++){
					
					Tile tile = gridText[r][c];
					JButton b = tile.getButton();
					if (symElim.equals(tile.symbol.getName())){
						//if (b.getBackground().equals(Color.DARK_GRAY)){//IF already blacked out
						//	b.setBackground(tile.color);
						//} else {
							b.setBackground(Color.DARK_GRAY);
							button.setBackground(Color.DARK_GRAY);
						//}
					}
				}
			}
		}
	}
	
	private class EliminateRow extends MouseInputAdapter {
		public void mousePressed(MouseEvent event){
			JLabel label = (JLabel) event.getComponent();
			int row = Integer.parseInt(label.getName());
			for (int col = 0; col < 18; col++){
				Tile tile = gridText[row][col];
				JButton b = tile.getButton();
				//if (b.getBackground().equals(Color.DARK_GRAY))//IF already blacked out
				//	b.setBackground(tile.color);
				//else
				b.setBackground(Color.DARK_GRAY);
			}
		}
	}
	
	private class EliminateColumn extends MouseInputAdapter {
		public void mousePressed (MouseEvent event){
			JLabel label = (JLabel) event.getComponent();
			int col = Integer.parseInt(label.getText()) - 1;
			
			for (int row = 0; row < 18; row++){
				Tile tile = gridText[row][col];
				JButton b = tile.getButton();
				
				//if (b.getBackground().equals(Color.DARK_GRAY))//IF already blacked out
				//	b.setBackground(tile.color);
				//else
					b.setBackground(Color.DARK_GRAY);
			}
		}
	}
	
	
	private class TileListener extends MouseInputAdapter {
		
		private Tile tile;
		
		private TileListener(Tile t){
			tile = t;
		}
		
		public void mousePressed(MouseEvent event) {
			if (event.getClickCount() == 1) {
				System.out.println(tile.toString());
				gTable.makeGuess(tile);
			} else if (event.getClickCount() > 1) {
				SolveCheck sCheck = new SolveCheck(tile, FONT, secretSquare);
				System.out.println(sCheck.getToSolve());
				if (sCheck.getToSolve()){
					board.setVisible(false);//TODO: Doesn't have intended functionality
				}
			}
		}
	}
	
	
	private Tile getSecretSq() {
		int row = (int) (Math.random() * 17);
		int column = (int) (Math.random() * 17);
		return gridText[row][column];
	}
		
	private Font setFont() throws FileNotFoundException {
		Font font = null;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new File("fontFile.ttf"));
			font = font.deriveFont(Font.BOLD,20);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(font);
		} catch (Exception ex) {
			font = new Font("Papyrus", Font.BOLD, 14);
		}
		return font;
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
			IllegalArgumentException exc = new IllegalArgumentException("Invalid Color Name");
			throw exc;
		}
	}
	
	/*
	 * 
	 */
	private Symbol stringToSymbol(String s) throws IllegalArgumentException {
		s = s.toLowerCase();
		if (s.equals("sun")){
			return new Sun();
		} else if (s.equals("star")){
			return new Star();
		} else if (s.equals("eye")){
			return new Eye();
		} else if (s.equals("moon")){
			return new Moon();
		} else if (s.equals("circle")){
			return new Circle();
		} else if (s.equals("bolt")){
			return new Bolt();
		} else if (s.equals("diamond")){
			return new Diamond();
		} else if (s.equals("hand")){
			return new Hand();
		} else if (s.equals("heart")){
			return new Heart();
		} else {
			IllegalArgumentException exc = new IllegalArgumentException("Invalid Symbol Name");
			throw exc;
		}
	}
}