package telepathy;
import javax.swing.*; 
  
public class Moon extends Symbol { 
   public ImageIcon getImage(){ 
	   String file = "moon.png";
	   return resizeImage(file);
   } 
   public String getName(){ 
      return "Moon"; 
   } 
}