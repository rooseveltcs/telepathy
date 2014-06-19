package telepathy;

import java.awt.Image;
import javax.swing.ImageIcon;

/* Symbol.java
 * @ Mia Vanderwilt
 * @ 06/18/14
 * <p>
 * An abstract class that contains the abstract methods getImage() which returns the 
 * image of the Symbol (used in the GUI) and getName() which returns the name of the symbol.
 * It also contains the (non-abstract) resizeImage() method which resizes the symbol's 
 * image to 40x40. 
 * <p>
 * Used in the Tile class as one one of the four characteristics of a Tile. Implemented in
 * nine symbol subclasses (bolt, circle, diamond, eye, hand, heart, moon, star, and sun). 
 */
public abstract class Symbol {
	
	/* Static instance field that represents the width & height of the image produced 
	 * by resizeImage(); dictates the size of the image on the Board */
	private static final int DIMENSION = 40;

	abstract ImageIcon getImage();
	
	/* getName()
	 * Returns the Name of the Symbol subclass. Always one word, with the first letter
	 * upper case (ie "Moon") a static instance field in each subclass.
	 * 
	 * @return name of shape 
	 */
	abstract String getName();
	
	/* resizeImage()
	 * Returns an ImageIcon used on the JButtons in the Grid, as well as in all other 
	 * visual representations of the Tiles. Is given a file name as a parameter, then 
	 * uses that to create an ImageIcon; extracts the Image from the ImageIcon; resizes
	 * the Image and converts it into an ImageIcon by creating a scaled instance of the 
	 * Image {@link DIMENSION}. 
	 * 
	 * @param file name of file located in the subclasses (ie Moon class); varies depending
	 * on subclass, a static instance field within each subclass.
	 * @return resized ImageIcon of width DIMENSION pixels and height DIMENSION pixels
	 */
	public ImageIcon resizeImage(String file){
		ImageIcon icon = new ImageIcon(file);
		Image img = icon.getImage();
		ImageIcon resizedIcon = new ImageIcon(img.getScaledInstance(DIMENSION, DIMENSION, java.awt.Image.SCALE_SMOOTH));
		return resizedIcon; 
	}
}


  
