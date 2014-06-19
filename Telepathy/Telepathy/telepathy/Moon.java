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
public class Moon extends Symbol { 
	
	private final static String name = "Moon";
	private final static String file = "moon.png";
	
	/*
	 * (non-Javadoc)
	 * {@link resizeImage()}  
	 * @see telepathy.Symbol#getImage()
	 */
	@ Override 
	public ImageIcon getImage(){ 
	   return resizeImage(file);
   } 
   
   /*
    * (non-Javadoc)
    * @see telepathy.Symbol#getName()
    */
   @ Override
	public String getName(){ 
      return name; 
   } 
}