import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;

public class Tile {

	public String COLOR;
	
	public int number;
	
	public int value;
	
	public boolean selected=false;
	
	private BufferedImage auxImage;
	
	public JLabel image;
	
	int xPos;
	int yPos;
	
	Tile(){
		COLOR="null";
		number=0;
		value=0;
	}
	
	Tile(String color, int nr){
		COLOR=color;
		number=nr;
	}

	public void setValue() {
		if(number==1){
			value=25;
		}else if(number<10){
			value=5;
		}else if(number>=10 && number <=13){
			value=10;
		}else if(number==14 || number==15){
			value=50;
		}
	}
	
	public int getValue(){
		return value;
	}
	
	public void setImage() {
		
		try {
			auxImage = ImageIO.read(new File(System.getProperty("user.dir")+"\\src\\tiles\\"+COLOR+(number)+".jpg"));
			image = new JLabel(new ImageIcon(auxImage));
			
			
			image.addMouseListener(new MouseAdapter() {
				
				@Override
				public void mousePressed(MouseEvent e) {
					xPos = e.getX();
					yPos = e.getY();
				}
				
				@Override
				public void mouseClicked(MouseEvent e)
				{
					selected = true;
				}
			});
			
			image.addMouseMotionListener(new MouseMotionAdapter() {
				
				@Override
				public void mouseDragged(MouseEvent e) {
					JComponent jc = (JComponent)e.getSource();
					image.setBounds(jc.getX()+e.getX()-xPos, jc.getY()+e.getY()-yPos, image.getWidth(), image.getHeight());
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
