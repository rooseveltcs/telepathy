package telepathy;
import java.awt.Image;
import javax.swing.*; 

public abstract class Symbol {
	abstract ImageIcon getImage();
	abstract String getName();
	public ImageIcon resizeImage(String file){
		ImageIcon icon = new ImageIcon(file);
		Image img = icon.getImage();
		ImageIcon resizedIcon = new ImageIcon(img.getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH));
		return resizedIcon; 
	}
}


  
