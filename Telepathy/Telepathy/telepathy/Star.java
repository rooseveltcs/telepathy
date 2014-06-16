package telepathy;
import javax.swing.*; 
  
public class Star extends Symbol { 
   public ImageIcon getImage() { 
	   String file = "star.png";
	   return resizeImage(file);
   } 
   public String getName(){ 
      return "Star"; 
   } 
}