import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class Game extends JFrame{
	
	public final static Deck mainDeck=new Deck();

	static Player firstPlayer = new Player(mainDeck,15);
	
	public JPanel mainPanel = new JPanel();
	public JPanel myBoard = new JPanel();
	public JButton showSelectedInConsole = new JButton("Find Selected");
	public JButton sortTiles = new JButton("Sort Tiles");
	public JButton addToMeld = new JButton("Add to Meld");
	
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
	
	public void showUI(){
		setName("Remi");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		mainPanel.setLayout(new BorderLayout());
		
		myBoard.setLayout(new FlowLayout());
		
		myBoard.setBackground(Color.YELLOW);
		
		for(int i=0;i<firstPlayer.nrOfTiles();i++){
			Tile currentTile= firstPlayer.myTiles.get(i);
			myBoard.add(currentTile.image);		
			currentTile.image.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e)
				{
					currentTile.selected = true;
				}				
			});
		}
		
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
				System.out.println(firstPlayer.myTiles.get(1).image.getWidth());
			}
			
		});
		
		setPreferredSize(new Dimension(600, 600));
		
		myBoard.setPreferredSize(new Dimension(600,150));
		myBoard.setMaximumSize(new Dimension(600,150));
		
		mainPanel.add(myBoard,BorderLayout.SOUTH);
		mainPanel.add(showSelectedInConsole, BorderLayout.EAST);
		mainPanel.add(sortTiles, BorderLayout.WEST);
		mainPanel.add(addToMeld, BorderLayout.NORTH);
		setContentPane(mainPanel);
		
		pack();
		setVisible(true);
		
	}
	
	public static void main(String[] args) {
		
//		firstPlayer.printMyTiles();
		System.out.println();
		System.out.println();
		
		new Game().showUI();
		
	}
	
}
