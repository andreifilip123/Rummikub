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

		return selectedTiles;
	}

	public Boolean checkSuita(List<Tile> tilesToCheck) {
		int length = tilesToCheck.size();
		tilesToCheck.sort(tileComparator);

		for (int i = 0; i < length - 1; i++) {
			if (tileComparator.compare(tilesToCheck.get(i), tilesToCheck.get(i + 1)) > 0
					|| tileComparator.compare(tilesToCheck.get(i), tilesToCheck.get(i + 1)) == 0) {
				return false;
			}
			if (tilesToCheck.get(i + 1).number - tilesToCheck.get(i).number > 1) {
				return false;
			}
		}
		return true;
	}

	public Boolean checkTerta(List<Tile> tilesToCheck) {
		//int length = tilesToCheck.size();
		tilesToCheck.sort(tileComparator);

		/*for (int i = 0; i < length - 1; i++) {

			return false;

		}*/
		return true;
	}

	public int addToMeld() {
		List<Tile> selectedTiles = getSelectedTiles();
		int punctaj = 0;
		selectedTiles.sort(tileComparator);

		if (checkSuita(selectedTiles) == true) {
			System.out.println("E suita:");
			for (int i = 0; i < selectedTiles.size() - 1; i++) {
				System.out.print(selectedTiles.get(i).number + " ");
				if (i == selectedTiles.size() - 2) {
					System.out.print(selectedTiles.get(i + 1).number);
					if (selectedTiles.get(i).number == 1 && selectedTiles.get(i + 1).number == 2) {
						punctaj += 5;
					} else if (selectedTiles.get(i).number == 13 && selectedTiles.get(i + 1).number == 1) {
						punctaj += 20;
						break;
					} else
						punctaj += selectedTiles.get(i).getValue();
				}
				if (selectedTiles.get(i).number == 1 && selectedTiles.get(i + 1).number == 2) {
					punctaj += 5;
				} else if (selectedTiles.get(i).number == 13 && selectedTiles.get(i + 1).number == 1) {
					punctaj += 20;
					break;
				} else
					punctaj += selectedTiles.get(i).getValue();
			}
			System.out.println();
		} else if (checkTerta(selectedTiles) == true) {
			System.out.println("E terta:");
			for (int i = 0; i < selectedTiles.size() - 1; i++) {
				System.out.print(selectedTiles.get(i).number + " ");
				punctaj += selectedTiles.get(i).getValue();
			}
			System.out.println();
		}

		/*
		 * for(int i=0;i<selectedTiles.size()-1;i++){ Tile
		 * a=selectedTiles.get(i); Tile b=selectedTiles.get(i+1);
		 * if(a.COLOR==b.COLOR && a.number<b.number){ if(a.number==1 &&
		 * b.number==2){ punctaj+=5; //a.selected=true; }else if(a.number==13 &&
		 * b.number==1){ punctaj+=10; //a.selected=true; }else{
		 * punctaj+=a.getValue(); //a.selected=true; } }else if(a.number ==
		 * b.number && a.COLOR != b.COLOR)
		 * punctaj+=selectedTiles.get(i).getValue(); }
		 */
		if (punctaj >= 15)
			return punctaj;
		else
			return 0;
	}

	public void meld() {

	}

}
