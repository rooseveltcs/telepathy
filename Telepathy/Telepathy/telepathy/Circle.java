package telepathy;
import javax.swing.*; 

public class Circle extends Symbol {
	public ImageIcon getImage(){
		   String file = "circle.png";
		   return resizeImage(file);
	}
	public String getName(){
		return "Circle";
	}
}
