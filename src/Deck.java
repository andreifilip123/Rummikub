import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

	public List<Tile> deck = new ArrayList<Tile>();

	public void printDeck() {
		for (int i = 0; i < deck.size(); i++) {
			System.out.println(deck.get(i).COLOR + " " + deck.get(i).number + " ");
		}
	}

	public Tile draw() {
		Tile auxPiece = deck.get(deck.size() - 1);
		deck.remove(auxPiece);
		return auxPiece;
	}

	public void add(Tile tileToBeAdded) {
		deck.add(tileToBeAdded);
	}
	
	public Deck() {
		// create all the pieces
		for (int i = 1; i <= 4; i++) {
			for (int j = 1; j <= 13; j++) {
				Tile auxPiece = new Tile();

				switch (i) {
				case 1:
					auxPiece.COLOR = "BLUE";
					break;
				case 2:
					auxPiece.COLOR = "BLACK";
					break;
				case 3:
					auxPiece.COLOR = "YELLOW";
					break;
				case 4:
					auxPiece.COLOR = "RED";
					break;
				}

				auxPiece.number = j;

				auxPiece.setValue();
				auxPiece.setImage();

				deck.add(auxPiece);
			}
		}
		for (int i = 1; i <= 4; i++) {
			for (int j = 1; j <= 13; j++) {
				Tile auxPiece = null;

				switch (i) {
				case 1:
					auxPiece = new Tile("BLUE", j);
					break;
				case 2:
					auxPiece = new Tile("BLACK", j);
					break;
				case 3:
					auxPiece = new Tile("YELLOW", j);
					break;
				case 4:
					auxPiece = new Tile("RED", j);
					break;
				}

				deck.add(auxPiece);
			}
		}
		Tile jollyr = new Tile("JOLLY", 14);

		deck.add(jollyr);

		Tile jollyb = new Tile("JOLLY", 15);

		deck.add(jollyb);

		// randomize
		Collections.shuffle(deck);
	}

}
