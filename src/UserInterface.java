import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class UserInterface extends JPanel implements Runnable{
	//remi stuff
	static Player client = new Player();

	//widths and heights
	private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private static final int screenWidth = (int) screenSize.getWidth();
	private static final int screenHeight = (int) screenSize.getHeight();

	private static final int gameWidth = (int) (0.95 * screenWidth);// 1280;
	private static final int gameHeight = (int) (0.99 * screenHeight);// 720;
	
	private static final int startWidth = (int) (screenWidth - gameWidth) / 2;
	private static final int startHeight = (int) (screenHeight - gameHeight) / 2;
	
	private static final int tableWidth = (int) (0.8 * gameWidth);
	private static final int tableHeight = (int) (0.6 * gameHeight);
	
	private static final int profilePicWidth = (int) (gameWidth - tableWidth) / 4;
	private static final int profilePicHeight = (int) 1.2 * profilePicWidth;
	
	private static final int boardWidth = (int) (0.45 * gameWidth)-startWidth + profilePicWidth / 2;
	private static final int tileWidth = 70;
	private static final int tileHeight = 63;
	private static final int tilesPerRow = boardWidth/(tileWidth+2);
	
	private static final int startWidthInBoard = startWidth + profilePicWidth / 2;
	private static final int firstRow = screenHeight - (int) (0.3 * screenHeight);
	private static final int secondRow = screenHeight - (int) (0.3 * screenHeight) + (int) (0.10 * screenHeight);
	
	
	//images
	public Image profilePic = new ImageIcon(System.getProperty("user.dir") + "\\src\\tiles\\RED11.jpg").getImage();

	//server stuff
	BufferedReader inFromServer;
	static PrintWriter outToServer;
	JTextArea messageArea = new JTextArea();
	JTextField newMessage = new JTextField();
		
	@SuppressWarnings("unused")
	private String getServerAddress() {
		return JOptionPane.showInputDialog(this, "Enter IP Address of the Server:", "Welcome to the Chatter",
				JOptionPane.QUESTION_MESSAGE);
	}
	
	private String getUserName() {
		return JOptionPane.showInputDialog(this, "Choose a screen name:", "Screen name selection",
				JOptionPane.PLAIN_MESSAGE);
	}
	
	//paint components
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		setLayout(null);
		
		messageArea.setEditable(false);
		JScrollPane aux = new JScrollPane(messageArea);
		add(aux);
		messageArea.setAutoscrolls(true);
		
		add(newMessage);
		Font font = new Font("Courier", Font.BOLD,16);
        newMessage.setFont(font);
        messageArea.setFont(font);
        newMessage.requestFocus();
        
		g.setColor(Color.DARK_GRAY);
		g.fillRoundRect(startWidth, startHeight, gameWidth, gameHeight, 30, 30);

		g.setColor(new Color(61, 165, 29));
		g.fillRoundRect(startWidth + (gameWidth - tableWidth) / 2, startHeight + (gameHeight - tableHeight) / 6,
				tableWidth, tableHeight, 30, 30);

		// add left profile pics
		g.drawImage(profilePic, startWidth + profilePicWidth / 2,
				startHeight + (gameHeight - tableHeight) / 6 + profilePicHeight, profilePicWidth, profilePicHeight,
				Color.DARK_GRAY, null);
		g.drawImage(profilePic, startWidth + profilePicWidth / 2, startHeight + tableHeight - profilePicHeight * 3 / 2,
				profilePicWidth, profilePicHeight, Color.DARK_GRAY, null);
		// add right profile pics
		g.drawImage(profilePic, startWidth + gameWidth - profilePicWidth * 3 / 2,
				startHeight + (gameHeight - tableHeight) / 6 + profilePicHeight, profilePicWidth, profilePicHeight,
				Color.DARK_GRAY, null);
		g.drawImage(profilePic, startWidth + gameWidth - profilePicWidth * 3 / 2,
				startHeight + tableHeight - profilePicHeight * 3 / 2, profilePicWidth, profilePicHeight,
				Color.DARK_GRAY, null);

		// add board
		g.setColor(new Color(132, 100, 35));
		g.fillRect(startWidth + profilePicWidth / 2, screenHeight - (int) (0.3 * screenHeight),
				(int) (0.45 * gameWidth), (int) (0.20 * screenHeight));
		g.setColor(Color.WHITE);
		g.drawLine(startWidth + profilePicWidth / 2,
				screenHeight - (int) (0.3 * screenHeight) + (int) (0.10 * screenHeight),
				startWidth + profilePicWidth / 2 + (int) (0.45 * gameWidth),
				screenHeight - (int) (0.3 * screenHeight) + (int) (0.10 * screenHeight));
		
		// add chat
		newMessage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outToServer.println(newMessage.getText());
				newMessage.setText("");
			}
		});
				
		aux.setBounds(new Rectangle(startWidth + profilePicWidth / 2 + gameWidth / 2,
				screenHeight - (int) (0.3 * screenHeight), (int) (0.45 * gameWidth), (int) (0.15 * screenHeight)));
		newMessage.setBounds(new Rectangle(startWidth + profilePicWidth / 2 + gameWidth / 2,
				screenHeight - (int) (0.15 * screenHeight), (int) (0.45 * gameWidth), (int) (0.045 * screenHeight)));
		
	}
	
	
	public void run() {

		String serverAddress = "192.168.43.231";//getServerAddress();
		Socket socket;
				
		try {
			socket = new Socket(serverAddress, 2222);

			inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			outToServer = new PrintWriter(socket.getOutputStream(), true);
						
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
					addTilesToBoard();
				} else if (line.equals("DC")) {
					sendAllTilesBack();
				} else
					System.exit(0);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addTilesToBoard(){
		//add tiles
		
		int paddingLeftForBoard=0;//(int)(0.05*boardWidth);
		int paddingTopForBoard = 10;
		client.sortTiles();
		for(int i=0;i<client.nrOfTiles();i++){
			Tile tempTile = client.getTileAt(i);
			JLabel tempImage = tempTile.image;
			add(tempImage);
			if(i<tilesPerRow){
				tempImage.setBounds(new Rectangle( startWidthInBoard+i*tileWidth+paddingLeftForBoard, firstRow+paddingTopForBoard, tileWidth, tileHeight));
			} else {
				tempImage.setBounds(new Rectangle( startWidthInBoard+(i-tilesPerRow)*tileWidth+paddingLeftForBoard, secondRow+paddingTopForBoard, tileWidth, tileHeight));
			}
		}
	}
	
	public static void sendAllTilesBack(){
		System.out.println("Urmatoarele piese au fost trimise inapoi la server:");
		int i=0;
		while(client.nrOfTiles()>0){
			outToServer.println("DC"+client.getTileAsString(i));
			System.out.println(client.getTileAsString(i));
			client.removeTileAt(i);
		}
	}
	
	public static void createAndShowGui() {
		JFrame frame = new JFrame();

		frame.setPreferredSize(new Dimension(screenWidth, screenHeight));
		frame.setResizable(false);

		JFrame.setDefaultLookAndFeelDecorated(false);
		// frame.setBackground(new Color(0,0,0,0));
		frame.setUndecorated(true);
		UserInterface UI = new UserInterface();
		new Thread(UI).start();
		frame.add(UI);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		frame.addWindowListener( new WindowAdapter()
		 {
		   public void windowClosing(WindowEvent e)
		    {
			   sendAllTilesBack();
			   System.exit(0);
		    }
		  });
		frame.pack();
		frame.setVisible(true);
		
	}

	
	public static void main(String[] args) {
		createAndShowGui();
	}
}
