package telepathy;
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

/* Board.java
 * @ Mia Vanderwilt
 * @ 6/22/14
 * <p>
 * The bulk of the program; the Board class contains all of the various components of the game itself. Within the board, there is a Grid of Tiles,
 * a Guess Table, containing all of the guesses a user makes and a table with all the Colors and Symbols. 
 * <p>
 * The program chooses one tile in the 18x18 grid and sets that as the SECRET_SQUARE. The User then clicks on Tiles within the Grid and receives information
 * about the accuracy of their guess in the GuessTable. They can guess for as long as they want (usually ~10, the program is only set up for 15 currently), and when
 * they want to eliminate colors/rows/symbols/columns, they click on the headers to gray them out from the grid. When they are fairly certain they have narrowed
 * down the grid to the single SECRET_SQUARE, they double click on a Tile and are asked if they are sure if they would like to solve (SolveCheck). If they press 'Yes' 
 * they are told the SECRET_SQUARE and the game ends, otherwise play continues.
 * 
 * Currently, the game ends any sort of termination. There must be some communication between the solveCheck window and the main JFrame, but it would seem that that would
 * require moving the SolveCheck class into Board as an inner class. The game is still playable, regardless.
 */
public class Board {
	
	private static Font FONT; //Custom font set in the setFont() method called in the constructor
	
	/*Board constants used in determining the dimension of the main JFrame, could potentially be used for positioning other elements or panels within the frame */
	private static final int BOARD_LENGTH = 1100;
	private static final int BOARD_WIDTH = 1000;
	
	public JFrame board; //TODO: Change back to private
	private static Tile SECRET_SQUARE;//Determined in the setSecretSq() method
	
	private File file;//Read in the setGrid() method
	private Tile[][] gridText;//Initialized in the setGrid() method	
	private JPanel gridDisp;//Initialized in the setGrid() method	
	
	private JPanel titlePanel;
	private GuessTable gTable;
	private JPanel table;
	
	/* Arrays used in creating a table, represent the 9 colors and 9 symbols represented in the grid and therefore table */
	private static final String[] COLORS = new String[] {"Yellow", "Orange", "Red", "Purple", "Pink", "Blue", "Green", "Silver", "White"};
	private static final Symbol[] SYMBOLS = new Symbol[] {new Sun(), new Star(), new Eye(), new Moon(), 
			new Circle(), new Bolt(), new Diamond(), new Hand(), new Heart()};

	/* Constructor
	 * Goes through and initializes all the various components of the main JFrame: the Frame itself, Title Panel, Grid,
	 * a Panel with the Grid and Column/Row Panels, the Secret Square, the Panel containing the two tables, the Guess Table
	 * and the Color/Symbol Table.
	 * Adds all of these things to their respective regions of the JFrame and then makes the Frame visible.
	 * 
	 * @param File f the file initialized in client code (Test.java) which is simply a text file that represents the 
	 * layout of the grid
	 * @throws FileNotFoundException if the parameter file cannot be found
	 */
	public Board (File f) throws FileNotFoundException {
		setFont();
		file = f;
		
		//BOARD
		setBoard(); 
		
		//TITLE PANEL
		setTitleP();
	    board.add(titlePanel, BorderLayout.NORTH); 
        
	    //GRID		
		setGrid(); //Initializes  gridText and gridDisp
		
		//Attempt at adding axes
		JPanel gAxes = new JPanel(new BorderLayout());
		gAxes.add(gridDisp, BorderLayout.CENTER);

		gAxes.add(columns(), BorderLayout.NORTH);
		gAxes.add(columns(), BorderLayout.SOUTH);
		
		gAxes.add(rows(), BorderLayout.EAST);
		gAxes.add(rows(), BorderLayout.WEST);
		
		board.add(gAxes, BorderLayout.CENTER);
		
		//SECRET SQUARE
		setSecretSq();
		
		//TWO TABLES (GUESS, COLOR/SYMBOL)
		JPanel twoTables = new JPanel(new GridLayout(2, 1));
		
		//GUESS TABLE
		gTable = new GuessTable(SECRET_SQUARE, FONT);
		twoTables.add(gTable.getPanel());
		//COLOR/SYMBOL TABLE
		setTable();
		twoTables.add(table);
		
		board.add(twoTables, BorderLayout.EAST);
		
		board.setVisible(true);
	}

	/* setTitleP()
	 * Sets the JPanel in the upper-most region of the Board JFrame, with TelepathyLogo and Red Background
	 * No functionality.
	 */
	private void setTitleP(){
        //TITLE PANEL
        titlePanel = new JPanel(new FlowLayout()); 
        titlePanel.setBackground(Color.RED); 
        
        JLabel tLabel = new JLabel(new ImageIcon("fullTitle.png")); 
        tLabel.setBackground(Color.RED); 
        
        titlePanel.add(tLabel); 
	}
	
	/* setBoard()
	 * Simply initializes board and works with the settings for the main JFrame (ie Size, Closing Operation, Location, Layout, Title, and IconImage)
	 * Initially, all this was located in constructor, but this is an attempt to clean that up.
	 */
	private void setBoard(){
		//SETTINGS 
        board = new JFrame();
		board.setLayout(new BorderLayout()); 
        board.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        board.setLocation(new Point (150, 20)); //TODO: These should be class constants
        board.setSize(new Dimension(BOARD_LENGTH, BOARD_WIDTH)); 
        board.setTitle("Telepathy"); 
        
        ImageIcon icon = new ImageIcon("telepathyIcon.jpg"); 
        board.setIconImage(icon.getImage()); 
	}
	
	/* setGrid()
	 * Initializes the gridText 2D array of Tiles, and the gridDisp 18x18 JPanel. Reads the file (textual rep. of Grid) and
	 * creates a tile with the given characteristics. Adds that to gridText and then gets the JButton for that tile, attaches a 
	 * listener and then adds it to the gridDisp.
	 * <p>
	 * Scans the file, reads row, column, color, symbol (in that order) converts the color/symbol names into the real deal using 
	 * private helper methods (at bottom of Board class). Creates a tile passing those 4 characteristics as parameters to the 
	 * constructor. Adds the Tile Listener [inner class of Board; seen below] which will act when a user clicks the JButton of the 
	 * Tile in the Grid. Also adds a ToolTip for clarification.
	 * 
	 * @throws FileNotFoundException for the file to be read
	 */
	private void setGrid() throws FileNotFoundException {
		
		gridText = new Tile[18][18];
		gridDisp = new JPanel(new GridLayout(18, 18));
		gridDisp.setSize(800, 800);
		
		@SuppressWarnings("resource")//? Eclipse thing.
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
	
	/* columns()
	 * Returns a horizontal JPanel with a list of columns (#s 1-18) with EliminateColumn listeners attached
	 * so the user can easily eliminate all the tiles in a column by pressing the JLabel with the column header
	 * on it. Used in the gAxes panel above and below the Grid. The alignment is a little bit off, but ToolTips are
	 * added to the Tiles for clarification. 
	 * 
	 * @return JPanel columns
	 */
	private JPanel columns(){
		JPanel columns = new JPanel(new GridLayout(1, 18));
		JLabel blank = new JLabel();
		columns.add(blank);
		for (int c = 1; c < 19; c++){
			JLabel col = new JLabel("" + c);
			col.setFont(FONT);
			col.addMouseListener(new EliminateColumn());
			columns.add(col);
		}
		return columns;
	}
	
	/* rows()
	 * Returns a vertical JPanel with a list of rows (Letters A-R) with EliminateRow listeners attached so the
	 * user can easily eliminate all the tiles in a row by pressing the JLabel row header. Used in gAxes panel 
	 * to the left and right of the Grid. 
	 * 
	 * @return JPanel rows 
	 */
	private JPanel rows(){
		JPanel rows = new JPanel(new GridLayout(18, 1));
		for (int r = 0; r < 18; r++){
			//Takes the row # and converts it to a String by getting the char equivalent (O + 65 = "A")
			JLabel row = new JLabel(" " + (char) (r + 65) + " ");//Accounts for the fact that the alphabet is shifted
			row.setName(r + "");
			row.setFont(FONT);
			row.addMouseListener(new EliminateRow());
			rows.add(row);
		}
		return rows;
	}
	
	/* setTable()
	 * Creates the Table in the lower right with a list of all the Colors and Symbols. The user can then click on any of these symbols/colors and
	 * Eliminate them from the grid through the following Eliminate classes. 
	 * 
	 * @return JPanel table the completed table with all of its functionality, not altered in any other method. Takes the work out of the constructor.
	 */
	private void setTable(){
		table = new JPanel(new GridLayout(10, 2));
		table.setSize(150, 300);
		
		//Table Headers
		JButton clr = new JButton("Color");
		clr.setFont(FONT);
		table.add(clr);
		JButton sym = new JButton("Symbol");
		sym.setFont(FONT);
		table.add(sym);
		
		for (int i = 0; i < 9; i++){
			JButton c = new JButton(COLORS[i]);//Takes each one of the 9 colors (listed in a static instance field) and makes a button of that color, with that name
			c.addMouseListener(new EliminateColor());
			c.setFont(FONT);
			c.setBackground(stringToColor(COLORS[i]));
			table.add(c);
			JButton s = new JButton(SYMBOLS[i].getName(), SYMBOLS[i].getImage());//Takes each one of the 9 symbols (listed in a static instance field) and makes a button with that symbol/name
			s.addMouseListener(new EliminateSymbol());
			s.setFont(FONT);
			table.add(s);
		}
	}
	
	//Below are the Eliminate classes, they each gray out a certain characteristic, allowing the user to narrow down their guesses.
	
	// TODO: There are FOUR Eliminate Classes, one for each Tile characteristic. Surely there is room for polymorphism by using an Eliminatable interface
	// but that would require a new Color class.
	// TODO: Currently none of the classes have a way of undoing an elimination. I took that feature out because there was no way to check if an eliminated 
	// square would have been eliminated by a different action. Possibly array lists. Good feature to have though.
	// TODO: It might be nice to have colored borders around the tiles, currently a user has to read the ToolTip.
	
	/* EliminateColor.java
	 * [inner class to Board]
	 * 
	 * When a button in the Color column of the Table is pressed, every Tile of that color in the Grid will turn dark gray. In essence, crossing it out.
	 * As with all EliminateXX files, the method of undoing the action is commented out because I was unsure of how to undo some eliminations, but not others.
	 * Currently it doesn't set the Tile's Color field to DARK_GRAY, it just alters the Background color of the button. It also turns the color button on the 
	 * Table dark gray. Pressing the table button again doesn't do anything. User can still guess 'crossed-out' squares. 
	 */ 
	private class EliminateColor extends MouseInputAdapter {
		/* Searches the entire gridText (textual representation of the Grid) for tiles that fit the characteristic, then gets that tile and alters it. */
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
							b.setBackground(Color.DARK_GRAY);//In the Grid
							button.setBackground(Color.DARK_GRAY);//In the Table
						//}
					}
				}
			}
		}
	}
	
	/* EliminateSymbol.java
	 * [inner class to Board]
	 * 
	 * Same functionality of EliminateColor class above. Description copied/altered below.
	 * 
	 * When a button in the Symbol column of the Table is pressed, every Tile with that symbol in the Grid will turn dark gray. In essence, crossing it out.
	 * As with all EliminateXX files, the method of undoing the action is commented out because I was unsure of how to undo some eliminations, but not others.
	 * Currently it doesn't set the Tile's Color field to DARK_GRAY, it just alters the Background color of the button. It also turns the symbol button on the 
	 * Table dark gray. Pressing the table button again doesn't do anything. User can still guess 'crossed-out' squares. 
	 */ 
	private class EliminateSymbol extends MouseInputAdapter {
		/* Searches the entire gridText (textual representation of the Grid) for tiles that fit the characteristic, then gets that tile and alters it. */
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
	
	/* EliminateRow.java
	 * [inner class to Board]
	 * 
	 * When a JLabel in the Row panel of the gAxes panel is pressed, the user is crossing out every tile in that row. This class then sets the color of every
	 * tile in that row to dark gray. As with all EliminateXX files, the method of undoing the action is commented out because I was unsure of how to undo some eliminations, but not others.
	 * Pressing the row label again doesn't do anything. User can still guess 'crossed-out' squares. 
	 */ 
	private class EliminateRow extends MouseInputAdapter {
		public void mousePressed(MouseEvent event){
			JLabel label = (JLabel) event.getComponent();
			int row = Integer.parseInt(label.getName());
			/* Parses through each Tile in that Row, column by column */
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
	
	/* EliminateColumn.java
	 * [inner class to Board]
	 * 
	 * Same functionality as EliminateRow; description is copied/altered below. 
	 * 
	 * When a JLabel in the Column panel of the gAxes panel is pressed, the user is crossing out every tile in that column. This class then sets the color of every
	 * tile in that column to dark gray. As with all EliminateXX files, the method of undoing the action is commented out because I was unsure of how to undo some eliminations, but not others.
	 * Pressing the column label again doesn't do anything. User can still guess 'crossed-out' squares. 
	 */ 
	private class EliminateColumn extends MouseInputAdapter {
		public void mousePressed (MouseEvent event){
			JLabel label = (JLabel) event.getComponent();
			int col = Integer.parseInt(label.getText()) - 1;
			/* Parses through each Tile in that Column, row by row */
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
	 
	/* TileListener.java
	 * [Inner class to Board]
	 * Makes guess of whatever tile has been clicked. If tile is double clicked: prompts a solveCheck window to open, with an
	 * opportunity for the user to solve.
	 * <p>
	 * Attached to each JButton in the 18x18 grid, the tile listener makes a guess of the tile by sending it as a parameter to the 
	 * gTable. If the tile is double clicked the user is prompted to solve, and the board SHOULD close if the user does choose to solve.
	 * However, as of yet, nothing terminates the program.
	 */
	private class TileListener extends MouseInputAdapter {
		
		private Tile tile; //Guessed Tile
		
		private TileListener(Tile t){
			tile = t;
		}
		
		/* mousePressed()
		 * [See above class description]
		 * @param MouseEvent event When a user presses a Tile on the 18x18 grid.
		 */
		public void mousePressed(MouseEvent event) {
			/* If the tile was clicked only once (the norm), then a guess is made */
			if (event.getClickCount() == 1) {
				System.out.println(tile.toString());//TODO: Used in testing 
				gTable.makeGuess(tile);
			/* If the tile was double clicked (to solve), then a guess is made and a SolveCheck frame opens in order to make sure the user wants to end game */
			} else if (event.getClickCount() > 1) {
				SolveCheck sCheck = new SolveCheck(tile, FONT, SECRET_SQUARE);
				System.out.println(sCheck.getToSolve());//Changes to true when user presses "Yes" on solveCheck
				if (sCheck.getToSolve()){
					board.setVisible(false); //TODO: Doesn't have intended functionality; supposed to close main JFrame after a user solves.
				}
			}
		}
	}
	
	/* setSecretSq()
	 * Randomly selects one of the tiles in the 18x18 grid to be the secret square. The user then tries to guess this square, and the accuracy of their
	 * guesses depends on this square. A new square is chosen each game. Sets the static instance field SECRET_SQUARE. For now, prints out the secret square in order
	 * to verify that the program is working properly.
	 */
	private void setSecretSq() {
		int row = (int) (Math.random() * 17);
		int column = (int) (Math.random() * 17);
		SECRET_SQUARE = gridText[row][column];
		
		System.out.println("sS: " + SECRET_SQUARE + " " + SECRET_SQUARE.symbol.getName());//Used in testing to verify that program works; TODO: remove
	}
	
	/* setFont()
	 * Attempts to set a custom font by reading the 'fontFile.ttf' (currently the GueyHandWritten font) and then creating
	 * a font that is BOLD and size 20. If the file is not found, or there is any other exception, it catches it and sets 
	 * the font as Papyrus, BOLD, size 14. Font then is set as the static instance variable (FONT).
	 * 
	 * @throws FileNotFoundException if the font file cannot be found 
	 */
	private void setFont() throws FileNotFoundException {
		try {
			FONT = Font.createFont(Font.TRUETYPE_FONT, new File("fontFile.ttf"));
			FONT = FONT.deriveFont(Font.BOLD,20);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(FONT);
		} catch (Exception ex) {
			FONT = new Font("Papyrus", Font.BOLD, 14);
		}
	}
	
	/* stringToColor()
	 * A dictionary of sorts, takes in a color name (from the text file representing the board) and returns the Color 
	 * that matches the name. Used in the tile class (as well as the setTable method) in order to initialize the Color 
	 * instance field. 
	 * 
	 * @param String s the string representing the Color name (ie "yellow")
	 * @return Color with the respective name (s)
	 * @throws IllegalArgumentException exc if the color name is invalid (ie the original file has an error)
	 */
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
	
	/* stringToSymbol()
	 * A dictionary of sorts, takes in a symbol name (from the text file representing the board) and returns the Symbol 
	 * that matches the name. Used in the tile class (as well as the setTable method) in order to initialize the Symbol 
	 * instance field. 
	 * 
	 * @param String s the string representing the Symbol name (ie "sun")
	 * @return Symbol with the respective name
	 * @throws IllegalArgumentException exc if the symbol name is invalid (ie the original file has an error)
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