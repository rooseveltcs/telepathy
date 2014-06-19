package telepathy;
import javax.swing.ImageIcon;

/*
 * Each Symbol class overrides the method getName() [to return 
 * the shape's name, and getImage() which obtains a particular file
 * name for the visual representation before calling the resizeImage(File f)
 * method of the abstract Symbol class. 
 * 
 * @see telepathy.Symbol
 */
public class Sun extends Symbol { 
   
	private final static String name = "Sun";
	private final static String file = "sun.png";
	
	public ImageIcon getImage(){ 
	   return resizeImage(file);
   } 
   public String getName(){ 
      return name; 
   } 
}