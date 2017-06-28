

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

public class Server {

	private static final int PORT = 2222;
	private static HashSet<String> names = new HashSet<String>();
	private static HashSet<PrintWriter> outWriters = new HashSet<PrintWriter>();
	
	public final static Deck mainDeck=new Deck();

	public static void main(String[] args) throws Exception {
		
		
		System.out.println("Serverul de remi functioneaza.");
		ServerSocket dataSocket = new ServerSocket(PORT);
		Thread st;
		
		try {
			while (true) {
				st = new ClientHandler(dataSocket.accept());
				st.start();
			}
		} finally {
			dataSocket.close();
			System.out.println("Serverul se inchide...");
		}
	}

	private static class ClientHandler extends Thread {
		private String name;
		private Socket socket;
		private BufferedReader inFromClient;
		private PrintWriter outToClient;

		public ClientHandler(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			try {

				inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				outToClient = new PrintWriter(socket.getOutputStream(), true);

				while (true) {
					outToClient.println("SETNAME");
					name = inFromClient.readLine();
					if (name == null) {
						return;
					}
					synchronized (names) {
						if (!names.contains(name)) {
							int nrOfTiles = names.size()==0?15:14;
							names.add(name);
							for(PrintWriter client:outWriters){
								client.println("MESSAGE Server: Jucatorul " + name + " s-a conectat.");
							}
							outToClient.println("ACCEPTED");
							outToClient.println("MESSAGE Server: " + "Ai primit piesele:");
							for(int i=0;i<nrOfTiles;i++){
								Tile tempTile =mainDeck.draw();
								if(i==nrOfTiles-1){
									outToClient.println("LAST" + tempTile.toString());
								} else {
								outToClient.println("TILE" + tempTile.toString());
								}
							}
							break;
						}
					}
				}

				outWriters.add(outToClient);

				while (true) {
					String input = inFromClient.readLine();
					if (input == null) {
						return;
					}
					
					for (PrintWriter writer : outWriters) {
						Tile tempTile=null;
						if (input.equals("Draw")) {
							if(mainDeck.deck.isEmpty())
								writer.println("TILE"+"S-a terminat pachetul");
							else {
								 tempTile = mainDeck.draw();
							}
							writer.println("TILE"+tempTile.toString());
						} else if (input.startsWith("Add to meld ")){
							tempTile = new Tile(input.substring(12));
							mainDeck.add(tempTile);
						} else if (input.startsWith("LAST Add to meld ")){
							tempTile = new Tile(input.substring(17));
							mainDeck.add(tempTile);
						} else if (input.equals("Disconnect")) {
							writer.println("DC");
						} else if (input.startsWith("DC")) {
							mainDeck.add(new Tile(input.substring(3)));
						} else {
							writer.println("MESSAGE " + name + ": " + input);
						}
						
					}
				}
			} catch (IOException e) {
				System.out.println(e);
			} finally {
				if (name != null) {
					names.remove(name);
				}
				if (outToClient != null) {
					outWriters.remove(outToClient);
				}
				try {
					socket.close();
				} catch (IOException e) {
				}
			}
		}
	}
}