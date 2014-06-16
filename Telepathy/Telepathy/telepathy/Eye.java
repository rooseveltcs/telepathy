package telepathy;
import javax.swing.*; 
  
public class Eye extends Symbol { 
   public ImageIcon getImage(){ 
	   String file = "eye.png";
	   return resizeImage(file);
   } 
   public String getName(){ 
      return "Eye"; 
   } 
}