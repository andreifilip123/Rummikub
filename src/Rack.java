import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Timer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.Border;

public class Rack extends JFrame{
	
	public final static Deck mainDeck=new Deck();

	static Player firstPlayer = new Player(mainDeck,15);
	
	public JPanel mainPanel = new JPanel();
	public JPanel myBoard = new JPanel();
	public JButton showSelectedInConsole = new JButton("Find Selected");
	public JButton sortTiles = new JButton("Sort Tiles");
	public JButton addToMeld = new JButton("Add to Meld");
	public JTable rack = new JTable(2, nrOfDropZones);
	
	public static int tileWidth = mainDeck.draw().image.getWidth();
	public static int nrOfDropZones;
	
	public void showUI(){
		setName("Remi");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		mainPanel.setLayout(new BorderLayout());
		
		showSelectedInConsole.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				List<Tile> selected= firstPlayer.getSelectedTiles();
				int n=firstPlayer.getSelectedTiles().size();
				System.out.println();
				System.out.println();
				for(int i=0;i<n;i++){
					System.out.println(selected.get(i).COLOR+" "+selected.get(i).number);
				}
			}
			
		});
		
		sortTiles.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				sortThenAdd(firstPlayer,myBoard);
			}
			
		});
		
		addToMeld.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				System.out.println(firstPlayer.addToMeld());
				if(firstPlayer.addToMeld()>=15){
					System.out.println("Te poti etala !");
				}
				else{
					System.out.println("Nu te poti etala !");
				}
			}
			
		});
		
		this.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				
				nrOfDropZones = (int) Math.floor(getWidth() / 68);
				System.out.println(nrOfDropZones);
				
				for(int i=0;i<firstPlayer.nrOfTiles();i++){
					System.out.println("am adaugat imaginea");
					rack.add(firstPlayer.myTiles.get(i).image);
				}
		    }
		});
		
		setPreferredSize(new Dimension(600, 600));
		
		rack.setPreferredSize(new Dimension(600,150));
		rack.setMaximumSize(new Dimension(600,150));
		
		mainPanel.add(rack,BorderLayout.SOUTH);
		mainPanel.add(showSelectedInConsole, BorderLayout.EAST);
		mainPanel.add(sortTiles, BorderLayout.WEST);
		mainPanel.add(addToMeld, BorderLayout.NORTH);
		setContentPane(mainPanel);
		
		pack();
		setVisible(true);

		
	}

	
	public void sortThenAdd(Player aPlayer,JPanel playerBoard){
		playerBoard.removeAll();
		aPlayer.sortTiles();
		int nr=aPlayer.nrOfTiles();
		for(int i=0;i<nr;i++){
			Tile currentTile = aPlayer.myTiles.get(i);
			Tile nextTile = (i!=(nr-1)) ? aPlayer.myTiles.get(i+1):aPlayer.myTiles.get(i);
			if(currentTile.COLOR!=nextTile.COLOR){
				playerBoard.add(currentTile.image);
				Border paddingBorder = BorderFactory.createEmptyBorder(0,0,0,20);
				currentTile.image.setBorder(BorderFactory.createCompoundBorder(null,paddingBorder));
			}else{
			playerBoard.add(currentTile.image);		
			}
		}
		playerBoard.revalidate();
	}
	
	public static void main(String[] args) {
		new Rack().showUI();
		
		System.out.println(nrOfDropZones);
		System.out.println();
		
	}
}
