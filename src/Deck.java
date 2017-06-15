import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

	public List<Tile> deck=new ArrayList<Tile>();
	
	public void initialize(){
		
		//create all the pieces
		for(int i=1;i<=4;i++){
			for(int j=1;j<=13;j++){
				Tile auxPiece = new Tile();
				
				switch(i){
				case 1: auxPiece.COLOR="BLUE";break;
				case 2: auxPiece.COLOR="BLACK";break;
				case 3: auxPiece.COLOR="YELLOW";break;
				case 4: auxPiece.COLOR="RED";break;
				}
				
				auxPiece.number=j;

				auxPiece.setValue();
				auxPiece.setImage();
				
				deck.add(auxPiece);
			}
		}
		Tile jollyr=new Tile();
		jollyr.COLOR="JOLLY";
		jollyr.number=14;
		jollyr.setValue();
		jollyr.setImage();
		
		deck.add(jollyr);

		Tile jollyb=new Tile();
		jollyb.COLOR="JOLLY";
		jollyb.number=15;
		jollyb.setValue();
		jollyb.setImage();
		
		deck.add(jollyb);
		
		//randomize
		Collections.shuffle(deck);
	}
	
	public void printDeck(){
		for(int i=0;i<deck.size();i++){
			System.out.println(deck.get(i).COLOR+" "+deck.get(i).number+" ");
		}
	}
	
	public Tile draw(){
		Tile auxPiece=deck.get(deck.size()-1);
		deck.remove(auxPiece);
		return auxPiece;
	}
	
	public Deck(){
		this.initialize();
	}

}
