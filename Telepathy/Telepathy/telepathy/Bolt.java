package telepathy;
import javax.swing.*; 
  
public class Bolt extends Symbol { 
   public ImageIcon getImage(){ 
	   String file = "bolt.png";
	   return resizeImage(file);
   } 
   public String getName(){ 
      return "Bolt"; 
   } 
}