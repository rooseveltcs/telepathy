package telepathy;
import javax.swing.*; 

public class Sun extends Symbol { 
   public ImageIcon getImage(){ 
	   String file = "sun.png";
	   return resizeImage(file);
   } 
   public String getName(){ 
      return "Sun"; 
   } 
}