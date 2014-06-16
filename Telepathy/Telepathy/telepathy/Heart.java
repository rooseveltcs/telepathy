package telepathy;
import javax.swing.*; 
  
public class Heart extends Symbol { 
   public ImageIcon getImage(){ 
	   String file = "heart.png";
	   return resizeImage(file);
   } 
   public String getName(){ 
      return "Heart"; 
   } 
}