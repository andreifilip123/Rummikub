import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;

public class Tile {

	@Override
	public String toString() {
		return COLOR + " " + number;
	}

	public String COLOR;
	
	public int number;
	
	public int value;
	
	public boolean selected=false;
	
	private BufferedImage auxImage;
	
	public JLabel image;
	
	int xPos;
	int yPos;
	
	int releaseX;
	int releaseY;
	
	Tile(){
		COLOR="null";
		number=0;
		value=0;
	}
	
	Tile(String color, int nr){
		COLOR=color;
		number=nr;
		setValue();
		setImage();
	}
	
	Tile(String toString) {
		COLOR = toString.split(" ")[0];
		number = Integer.parseInt(toString.split(" ")[1]);
		setValue();
		setImage();
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
		
		auxImage = scaleImage(65,63,System.getProperty("user.dir")+"\\src\\tiles\\"+COLOR+(number)+".jpg");
		//auxImage = ImageIO.read(new File(System.getProperty("user.dir")+"\\src\\tiles\\"+COLOR+(number)+".jpg"));
		image = new JLabel(new ImageIcon(auxImage));
		
		image.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				xPos = e.getX();
				yPos = e.getY();
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				selected = true;
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				releaseX=e.getX();
				releaseY=e.getY();
			}
		});
		
		image.addMouseMotionListener(new MouseMotionAdapter() {
			
			@Override
			public void mouseDragged(MouseEvent e) {
				JComponent jc = (JComponent)e.getSource();
				image.setBounds(jc.getX()+e.getX()-xPos, jc.getY()+e.getY()-yPos, image.getWidth(), image.getHeight());
			}
		});
	}
	
	public Point getDropZone(){
		return new Point(releaseX,releaseY);
	}
	
	public Boolean sameColorAs(Tile aux) {
		if(this.COLOR.equals(aux.COLOR))
			return true;
		return false;
	}
	
	public Boolean sameNumberAs(Tile aux) {
		if(this.number==aux.number){
			return true;
		}
		return false;
	}
	
	public Boolean sameTileAs(Tile aux) {
		if(sameColorAs(aux)&&sameNumberAs(aux)){
			return true;
		}
		return false;
	}
	
	public BufferedImage scaleImage(int WIDTH, int HEIGHT, String filename) {
	    BufferedImage bi = null;
	    try {
	        ImageIcon ii = new ImageIcon(filename);//path to image
	        //                     width, height, image type
	        bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	        Graphics2D g2d = (Graphics2D) bi.createGraphics();
	        //g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY));
	        g2d.drawImage(ii.getImage(), 0, 0, WIDTH, HEIGHT, null);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	    return bi;
	}
}
