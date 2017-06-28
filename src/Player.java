import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.JLabel;

public class Player {

	List<Tile> myTiles = new ArrayList<Tile>();

	public Comparator<Tile> tileComparator = new Comparator<Tile>() {

		@Override
		public int compare(Tile t1, Tile t2) {
			if (t1.COLOR.equals(t2.COLOR)) {
				if (t1.number > t2.number) {
					return 1;
					// return 1 if first arg is greater than second arg
				} else if (t1.number < t2.number) {
					if(t1.number==1 && t2.number==13)
						return 1;
					else
						return -1;
				} else
					return 0;
			} else if (t1.COLOR.compareTo(t2.COLOR) > 0) {
				return 1;
			} else if (t1.COLOR.compareTo(t2.COLOR) < 0) {
				return -1;
			}
			return 0;
		}

		/*
		 * public int compareColor(Tile t1, Tile t2) {
		 * if(t1.COLOR.compareTo(t2.COLOR)>0) { return 1; } else
		 * if(t1.COLOR.compareTo(t2.COLOR)<0){ return -1; } return 0; }
		 */
	};

	public Player() {

	}

	public void add(Tile tileToBeAdded) {
		myTiles.add(tileToBeAdded);
	}
	
	public void remove(Tile tileToBeRemoved) {
		myTiles.remove(tileToBeRemoved);
	}

	public Tile getTileAt(int index) {
		return myTiles.get(index);
	}

	public void removeTileAt(int index) {
		myTiles.remove(index);
	}

	
	public String getTileAsString(int index) {
		return myTiles.get(index).toString();
	}

	public void printMyTiles() {
		for (int i = 0; i < myTiles.size(); i++) {
			System.out.println(myTiles.get(i).COLOR + " " + myTiles.get(i).number + " ");
		}
	}

	public void sortTiles() {
		myTiles.sort(tileComparator);
	}

	public JLabel getTileImage(Tile tile) {
		JLabel currentTileImage = new JLabel();
		for (int i = 0; i < myTiles.size(); i++) {
			Tile currentTile = myTiles.get(i);
			if (currentTile.COLOR == tile.COLOR && currentTile.number == tile.number) {
				return currentTile.image;
			}
		}
		return currentTileImage;
	}

	public int nrOfTiles() {
		return myTiles.size();
	}

	public List<Tile> getSelectedTiles() {

		List<Tile> selectedTiles = new ArrayList<Tile>();

		for (int i = 0; i < myTiles.size(); i++) {
			if (myTiles.get(i).selected == true)
				selectedTiles.add(myTiles.get(i));
		}

		/*Tile has1=null;
		Tile has13=null;
		for (int i= 0; i < selectedTiles.size(); i++){
			Tile currentTile = selectedTiles.get(i);
			if (currentTile.number == 1)
				has1 = currentTile;
			if (currentTile.number == 13)
				has13 = currentTile;
		}

		if(has1!=null && has13!=null){
			has1.number = 14;
		}*/
		
		return selectedTiles;
	}

	public Boolean checkSuita(List<Tile> tilesToCheck) {
		int length = tilesToCheck.size();
		Boolean has1 = false, has13 = false;

		if (length < 3)
			return false;

		for (int i = 0; i < length; i++) {
			Tile currentTile = tilesToCheck.get(i);
			if (currentTile.number == 1)
				has1 = true;
			if (currentTile.number == 13)
				has13 = true;
		}

		if (has1 && has13)
			;
		else
			tilesToCheck.sort(tileComparator);

		for (int i = 0; i < length - 1; i++) {
			Tile currentTile = tilesToCheck.get(i);
			Tile nextTile = tilesToCheck.get(i + 1);
			if (currentTile.sameColorAs(nextTile) == false) {
				return false;
			}
			if (nextTile.number - currentTile.number != 1) {
				return false;
			}
		}
		return true;
	}

	public Boolean checkTerta(List<Tile> tilesToCheck) {
		int length = tilesToCheck.size();
		tilesToCheck.sort(tileComparator);

		if (length < 3)
			return false;

		for (int i = 0; i < length - 1; i++) {
			Tile currentTile = tilesToCheck.get(i);
			Tile nextTile = tilesToCheck.get(i + 1);
			if (currentTile.sameTileAs(nextTile) == true) {
				return false;
			}
			if (currentTile.sameNumberAs(nextTile) == false) {
				return false;
			}
		}
		return true;
	}

	public int punctajSuita(List<Tile> tiles) {
		int length = tiles.size();
		int punctaj = 0;
		for (int i = 0; i < length - 1; i++) {
			if (i == length - 2) {
				if (tiles.get(i).number == 1 && tiles.get(i + 1).number == 2) {
					punctaj += 5;
				} else if (tiles.get(i).number == 13 && tiles.get(i + 1).number == 1) {
					punctaj += 20;
					break;
				} else
					punctaj += tiles.get(i).getValue();
			}
			if (tiles.get(i).number == 1 && tiles.get(i + 1).number == 2) {
				punctaj += 5;
			} else if (tiles.get(i).number == 13 && tiles.get(i + 1).number == 1) {
				punctaj += 20;
				break;
			} else
				punctaj += tiles.get(i).getValue();
		}
		return punctaj;
	}

	public int punctajTerta(List<Tile> tiles) {
		Tile auxTile = tiles.get(0);
		int nrOfTiles = tiles.size();
		if (auxTile.number == 1) {
			return nrOfTiles * 25;
		} else if (auxTile.number < 10) {
			return nrOfTiles * 5;
		} else
			return nrOfTiles * 10;
	}

	public Boolean addToMeld(PrintWriter serverOutput) {
		List<Tile> selectedTiles = getSelectedTiles();
		int punctaj = 0;

		if (checkSuita(selectedTiles) == true) {
			serverOutput.println("E suita:");
			for (int i = 0; i < selectedTiles.size(); i++) {
				serverOutput.print(selectedTiles.get(i).number + " ");
			}
			serverOutput.println();
			punctaj = punctajSuita(selectedTiles);
			serverOutput.println("Punctaj: " + punctaj);
		} else if (checkTerta(selectedTiles) == true) {
			serverOutput.println("E terta:");
			for (int i = 0; i < selectedTiles.size(); i++) {
				serverOutput.print(selectedTiles.get(i).number + " ");
			}
			serverOutput.println();
			punctaj = punctajTerta(selectedTiles);
			serverOutput.println("Punctaj: " + punctaj);
		}

		if (punctaj >= 15)
			return true;
		else
			return false;
	}

	public void meld(PrintWriter serverOutput) {
		/*if(addToMeld(serverOutput)){
			List<Tile> selectedTiles = getSelectedTiles();
			
			for(int i=0;i<selectedTiles.size();i++){
				
			}
		}*/
	}

}
