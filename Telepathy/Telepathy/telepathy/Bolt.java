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
public class Bolt extends Symbol { 
   
	private final static String name = "Bolt";
	private final static String file = "bolt.png";
	
	public ImageIcon getImage(){ 
	   return resizeImage(file);
   } 
   public String getName(){ 
      return name; 
   } 
}