package telepathy;
import javax.swing.*; 
  
public class Hand extends Symbol { 
   public ImageIcon getImage(){ 
	   String file = "hand.png";
	   return resizeImage(file);
   } 
   public String getName(){ 
      return "Hand"; 
   } 
}