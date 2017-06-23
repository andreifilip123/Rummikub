import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class ClientApp extends JFrame implements Runnable{
	
	static Player client = new Player();
	
	public JPanel mainPanel = new JPanel();
	public JPanel myBoard = new JPanel();
	public JPanel chatPanel = new JPanel(new BorderLayout());
	public JButton showSelectedInConsole = new JButton("Find Selected");
	public JButton sortTiles = new JButton("Sort Tiles");
	public JButton addToMeld = new JButton("Add to Meld");
	
	BufferedReader inFromServer;
	PrintWriter outToServer;
	JTextField newMessage = new JTextField(40);
	JTextArea messageArea = new JTextArea(8, 40);
	
	private String getServerAddress() {
		return JOptionPane.showInputDialog(this, "Enter IP Address of the Server:", "Welcome to the Chatter",
				JOptionPane.QUESTION_MESSAGE);
	}

	private String getUserName() {
		return JOptionPane.showInputDialog(this, "Choose a screen name:", "Screen name selection",
				JOptionPane.PLAIN_MESSAGE);
	}
	
	public void run() {

		String serverAddress = "192.168.0.100";//getServerAddress();
		Socket socket;
		
		try {
			socket = new Socket(serverAddress, 2222);

			inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			outToServer = new PrintWriter(socket.getOutputStream(), true);
			
			showUI();
			
			while (true) {
				String line = inFromServer.readLine();
				if (line.startsWith("SETNAME")) {
					outToServer.println(getUserName());
					newMessage.requestFocus();
				} else if (line.startsWith("ACCEPTED")) {
					newMessage.setEditable(true);
				} else if (line.startsWith("MESSAGE")) {
					messageArea.append(line.substring(8) + "\n");
					messageArea.setCaretPosition(messageArea.getDocument().getLength()-1);
				} else if (line.startsWith("TILE")) {
					client.add(new Tile(line.substring(4)));
				} else if (line.startsWith("LAST")) {
					client.add(new Tile(line.substring(4)));
					sortThenAdd(client, myBoard);
				} else if (line.equals("DC")) {
					sendAllTilesBack();
				} else
					System.exit(0);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sortThenAdd(Player aPlayer,JPanel playerBoard){
		playerBoard.removeAll();
		aPlayer.sortTiles();
		int nr=aPlayer.nrOfTiles();
		for(int i=0;i<nr;i++){
			Tile currentTile = aPlayer.myTiles.get(i);
			Tile nextTile = (i!=(nr-1)) ? aPlayer.myTiles.get(i+1):aPlayer.myTiles.get(i);
			if(!currentTile.COLOR.equals(nextTile.COLOR)){
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
		
		for(int i=0;i<client.nrOfTiles();i++){
			Tile currentTile= client.myTiles.get(i);
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
				List<Tile> selected= client.getSelectedTiles();
				int n=client.getSelectedTiles().size();
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
				sortThenAdd(client,myBoard);
			}
			
		});
		
		addToMeld.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				System.out.println(client.addToMeld());
			}
			
		});
		
		setPreferredSize(new Dimension(1280, 720));
		
		myBoard.setPreferredSize(new Dimension(600,150));
		myBoard.setMaximumSize(new Dimension(600,150));
		
		newMessage.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				outToServer.println(newMessage.getText());
				newMessage.setText("");
			}
		});
		
		chatPanel.add(newMessage,BorderLayout.SOUTH);
		chatPanel.add(new JScrollPane(messageArea),BorderLayout.CENTER);
		
		mainPanel.add(myBoard,BorderLayout.SOUTH);
		mainPanel.add(showSelectedInConsole, BorderLayout.EAST);
		mainPanel.add(sortTiles, BorderLayout.WEST);
		mainPanel.add(addToMeld, BorderLayout.NORTH);
		mainPanel.add(chatPanel, BorderLayout.CENTER);
		setContentPane(mainPanel);
		
		pack();
		setVisible(true);
		
	}
	
	public void sendAllTilesBack(){
		for(int i=0;i<client.nrOfTiles();i++){
			outToServer.println("DC "+client.getTileAsString(i));
			client.removeTileAt(i);
		}
		myBoard.removeAll();
		repaint();
	}
	
	public static void main(String[] args) {

		ClientApp client = new ClientApp();
		
		new Thread(client).start();
		
	}
	
}
