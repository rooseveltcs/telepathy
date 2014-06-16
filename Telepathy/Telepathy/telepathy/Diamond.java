package telepathy;
import javax.swing.*; 
  
public class Diamond extends Symbol { 
   public ImageIcon getImage(){ 
	   String file = "diamond.png";
	   return resizeImage(file);
   } 
   public String getName(){ 
      return "Diamond"; 
   } 
}