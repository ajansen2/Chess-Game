package edu.kingsu.SoftwareEngineering.Chess;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.tree.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Objects;
import java.util.Stack;

import static edu.kingsu.SoftwareEngineering.Chess.Types.*;

import com.formdev.flatlaf.*;

import edu.kingsu.SoftwareEngineering.Chess.Types.pieceType;

/**
 * The New Game menu user interface.
 * 
 * @author Hamza Mhamdi
 * @author Group 4
 * @version 1
 */
public class GameSetupUI extends JPanel implements GameEndListener {
	private static JFrame frame;
	private JPanel gameSetupPanel, gamePanel, playerPanel, buttonPanel, inputPanel;
	private JPanel whitePlayerPanel, blackPlayerPanel, aiLvlPanel, chessPanel, moveListPanel;
	private JButton playGame, startGame, loadGame, tutorialAndOpeningsButton, aboutMenu;
	private JComboBox<String> playerType, aiLvl;
	private final String[] playerTypeString = { "Human", "AI" };
	private final String[] aiLvlString = { "Level 1", "Level 2", "Level 3" };
	private final String AI_OPTION = "AI";
	private final String[] playerTypeChoices = { "Human", "Human" };
	private final int[] aiLevelChoices = { 1, 1 };
	private final Font buttonFont = new Font("Dialog", Font.BOLD, 24);
	private JTextArea moveList;
	private JTextField inputTextField;
	private JScrollPane scrollPane;
	private JMenuBar menuBar;
	private JMenu fileMenu, editMenu, tutorialMenu, helpMenu;
	private JMenuItem kingMenuItem, pawnMenuItem, rookMenuItem, bishopMenuItem, knightMenuItem, queenMenuItem,
			rulesMenuItem, loadMenuItem, newGameMenuItem, saveMenuItem, exitMenuItem, undoMoveMenuItem,
			redoMoveMenuItem, uiMenuItem, savingMenuItem, movesMenuItem, pgnMenuItem;
	private final String relativePath = "src" + File.separator + "assets" + File.separator + "PGN";
	private final String pgnPath = System.getProperty("user.dir") + File.separator + relativePath;
	private BoardUI boardUI;
	private BoardController boardController;
	private PgnController pgnController;
	private GameModel chessGame;
	private AlgNotationHelper algHelper;
	private JLabel pointsLabel;
	private String currentTheme = "Dark Theme";
	int blackPlayerPoints = 0, whitePlayerPoints = 0;
	int width = 30, height = 30;
	String lastUndoneMove = ""; // To store the last undone move
	Stack<String> undoneMoves = new Stack<>();
	private final String[] themes = { "Dark Theme", "Light Theme", "The Kings University Theme", "Oilers Theme",
			"Chess.com Theme", "Legacy Theme" };
	private final String[] themeFolders = { "DarkTheme", "LightTheme", "KingsUniversityTheme", "OilersTheme",
			"ChessTheme", "LegacyTheme" };

	ImageIcon originalHintIcon = new ImageIcon(
			Objects.requireNonNull(Main.class.getClassLoader().getResource("img/Hint_Button.png")));
	Image scaledHintImage = originalHintIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
	ImageIcon hintIcon = new ImageIcon(scaledHintImage);
	ImageIcon originalRedoIcon = new ImageIcon(
			Objects.requireNonNull(Main.class.getClassLoader().getResource("img/Redo_Button.png")));
	Image scaledRedoImage = originalRedoIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
	ImageIcon redoIcon = new ImageIcon(scaledRedoImage);
	ImageIcon originalUndoIcon = new ImageIcon(
			Objects.requireNonNull(Main.class.getClassLoader().getResource("img/Undo_Button.png")));
	Image scaledUndoImage = originalUndoIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
	ImageIcon undoIcon = new ImageIcon(scaledUndoImage);

	/**
	 * Constructs a new game UI from a given model
	 * 
	 * @param model The model to use
	 */
	public GameSetupUI(GameModel model, BoardUI boardUI, AlgNotationHelper algHelper) {
		this.chessGame = model;
		chessGame.addGameEndListener(this);
		this.boardUI = boardUI;
		this.algHelper = algHelper;

		frame = new JFrame("Chess320");

		// ImageIcon icon=new ImageIcon("/src/assets/img/icon.jpg");
		// frame.setIconImage(icon.getImage());

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(1450, 1200));
		// frame.setSize(new Dimension(1000,800)); //THIS FOR ADAM
		frame.setPreferredSize(new Dimension(1450, 1200));
		// frame.setPreferredSize(new Dimension(1000, 800)); //THIS FOR ADAM
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);// evil trick to fix the boardResizing

		// Load the background image
		ClassLoader classLoader = Main.class.getClassLoader();
		// java.net.URL
		// backgroundImageURL=classLoader.getResource("img/1wallpaper.jpg");
		java.net.URL backgroundImageURL = classLoader.getResource("img/testwallpaper.jpg");

		ImageIcon backgroundImageIcon = new ImageIcon(backgroundImageURL);
		Image backgroundImage = new ImageIcon(backgroundImageURL).getImage();

		// Create the gameSetupPanel with the custom background
		gameSetupPanel = new BackgroundPanel(backgroundImage);
		gameSetupPanel.setLayout(new GridBagLayout());

		playGame = new JButton("Play Custom Game");
		playGame.setFont(buttonFont);
		// playGame.setForeground(Color.BLACK);
		// playGame.setBackground(Color.WHITE);
		playGame.setFocusPainted(false);
		playGame.setPreferredSize(new Dimension(400, 200));

		loadGame = new JButton("Load Game");
		loadGame.setFont(buttonFont);
		// loadGame.setForeground(Color.BLACK);
		// loadGame.setBackground(Color.WHITE);
		loadGame.setPreferredSize(new Dimension(400, 200));

		tutorialAndOpeningsButton = new JButton("Strategies and Openings");
		tutorialAndOpeningsButton.setFont(buttonFont);
		// tutorialAndOpeningsButton.setForeground(Color.BLACK);
		// tutorialAndOpeningsButton.setBackground(Color.WHITE);
		tutorialAndOpeningsButton.setPreferredSize(new Dimension(400, 200));

		aboutMenu = new JButton("Play Tutorial Game");
		aboutMenu.setFont(buttonFont);
		// aboutMenu.setForeground(Color.BLACK);
		// aboutMenu.setBackground(Color.WHITE);
		aboutMenu.setPreferredSize(new Dimension(400, 200));

		JLabel titleLabel = new JLabel("Chess320", SwingConstants.CENTER);
		titleLabel.setForeground(Color.decode("#FFFFFF"));
		titleLabel.setFont(buttonFont);
		// titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

		// todo fix the title to be on top.
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.insets = new Insets(0, 0, 0, 0);
		gameSetupPanel.add(titleLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 0;
		// constraints.insets=new Insets(20,0,0,0);
		gameSetupPanel.add(playGame, constraints);

		constraints.gridx = 2;
		constraints.gridy = 1;
		gameSetupPanel.add(aboutMenu, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		gameSetupPanel.add(tutorialAndOpeningsButton, constraints);

		constraints.gridx = 1;
		constraints.gridy = 2;
		gameSetupPanel.add(loadGame, constraints);

		playerPanel = new JPanel();
		playerPanel.setLayout(new GridBagLayout());
		aiLvlPanel = new JPanel();
		aiLvlPanel.setLayout(new FlowLayout());
		aiLvlPanel.add(new JLabel("AI Level: "));
		aiLvl = new JComboBox<>(aiLvlString);
		aiLvlPanel.add(aiLvl);

		moveListPanel = new JPanel();

		// todo add background image
		/*
		 * JLayeredPane layeredPane=new JLayeredPane();
		 * frame.setContentPane(layeredPane);
		 * String imagePath="1wallpaper.jpg";
		 * ImageIcon backgroundImage=new ImageIcon(imagePath);
		 * JLabel backgroundLabel=new JLabel(backgroundImage);
		 * backgroundLabel.setBounds(0, 0, backgroundImage.getIconWidth(),
		 * backgroundImage.getIconHeight());
		 * layeredPane.add(backgroundLabel, Integer.valueOf(0));
		 * gameSetupPanel.setBounds(0, 0, frame.getWidth(), frame.getHeight());
		 * layeredPane.add(gameSetupPanel, Integer.valueOf(1));
		 * frame.addComponentListener(new ComponentAdapter(){
		 *
		 * @Override
		 * public void componentResized(ComponentEvent e){
		 * adjustMoveListPanelSize();
		 * }
		 * });
		 */

		chessPanel = new JPanel();
		// gameSetupPanel.setBackground(Color.BLACK);
		frame.add(gameSetupPanel);
		frame.pack();
		frame.setVisible(true);

		playGame.addActionListener(e -> onGameStart());
		playGame.setUI(new CustomButtonUI());
		// todo
		aboutMenu.addActionListener(e -> tutoGame());
		aboutMenu.setUI(new CustomButtonUI());
		tutorialAndOpeningsButton.addActionListener(e -> tutoMenu());
		tutorialAndOpeningsButton.setUI(new CustomButtonUI());
		loadGame.addActionListener(e -> loadGameMainMenu());
		loadGame.setUI(new CustomButtonUI());
	}

	private void adjustMoveListPanelSize() {// todo
		int moveListWidth = (int) (frame.getWidth() * 0.25);
		int boardWidth = (int) (frame.getWidth() * 0.75);
		int newHeight = frame.getHeight();
		moveListPanel.setPreferredSize(new Dimension(moveListWidth, newHeight));
		chessPanel.setPreferredSize(new Dimension(boardWidth, newHeight));
		frame.revalidate();
		frame.repaint();
	}

	/** Loads a pgn game from the main menu */
	private void loadGameMainMenu() {
		JFileChooser fileChooser = new JFileChooser(pgnPath);
		fileChooser.setFileFilter(new PGNFileFilter());
		int result = fileChooser.showOpenDialog(frame);
		if (result == JFileChooser.APPROVE_OPTION) {
			frame.getContentPane().removeAll();
			startGame(chessGame, false);
			File selectedFile = fileChooser.getSelectedFile();
			pgnController.loadGame(selectedFile);
		}
	}

	/** Asks the user for a PGN file and loads it into the game */
	private void loadGame() {
		JFileChooser fileChooser = new JFileChooser(pgnPath);
		fileChooser.setFileFilter(new PGNFileFilter());
		int result = fileChooser.showOpenDialog(frame);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			pgnController.loadGame(selectedFile);
		}
	}

	/** Asks the user for a PGN file location and saves it */
	private void saveGame() {
		System.out.println("testSave");
		JFileChooser fileChooser = new JFileChooser(pgnPath);
		fileChooser.setFileFilter(new PGNFileFilter());
		int result = fileChooser.showSaveDialog(frame);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			pgnController.saveGame(selectedFile.getAbsolutePath());
		}
	}

	public void setBoardController(BoardController bc) {
		boardController = bc;
	}

	public void setPgnController(PgnController bc) {
		pgnController = bc;
	}

	// todo add a button that returns us to main menu maybe an arrow button that
	// repaints the initial state, we dont need to save the panel as its redundant
	// and will make game slow but we will discuss it

	/** Runs the initial starting steps */
	private void onGameStart() {
		gameSetupPanel.setVisible(false);
		playerPanel = new JPanel();
		playerPanel.setLayout(new BorderLayout());

		// todo reset the board to initial state when u go to main menu.
		// chessGame=new GameModel();
		// boardController=new BoardController(chessGame);
		// boardUI=new BoardUI(chessGame,boardController);

		whitePlayerPanel = new JPanel();
		blackPlayerPanel = new JPanel();

		Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);

		blackPlayerPanel.setBorder(padding);
		whitePlayerPanel.setBorder(padding);

		blackPlayerPanel.setBackground(Color.decode("#1A1A1A"));
		// whitePlayerPanel.setBackground(Color.WHITE);

		// JPanel whiteAiLvlPanel=createPlayerPanel(whitePlayerPanel,"White",0);
		// JPanel blackAiLvlPanel=createPlayerPanel(blackPlayerPanel,"Black",1);
		createPlayerPanel(whitePlayerPanel, "White", 0);
		createPlayerPanel(blackPlayerPanel, "Black", 1);

		startGame = new JButton("Start Game");
		startGame.setFont(buttonFont);
		// startGame.setForeground(Color.BLACK);
		// startGame.setBackground(Color.WHITE);
		startGame.setOpaque(true);
		startGame.setPreferredSize(new Dimension(250, 100));
		startGame.setBounds(200, 50, 100, 150);
		startGame.addActionListener(e -> startGame(chessGame, false));
		// set the custom ButtonUI for your JButton
		startGame.setUI(new CustomButtonUI());

		// create more panels bcuz why not ?
		JPanel bnwPanel = new JPanel(new GridLayout(2, 1));
		bnwPanel.add(whitePlayerPanel);
		bnwPanel.add(blackPlayerPanel);

		playerPanel.add(bnwPanel, BorderLayout.CENTER);
		playerPanel.add(startGame, BorderLayout.SOUTH);

		frame.getContentPane().remove(gameSetupPanel);
		// frame.getContentPane().setBackground(Color.BLACK);

		frame.add(playerPanel);
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * Creation of new players, creates the new player UI
	 * 
	 * @param parent       The parent panel to attach to
	 * @param playerString The strings of the player types to show on the combo box
	 * @param playerIndex  0 for white, 1 for black
	 * @return The modified JPanel
	 */
	public JPanel createPlayerPanel(JPanel parent, String playerString, int playerIndex) {
		JPanel whiteAiLvlPanel = new JPanel(new BorderLayout());

		// if (parent == whitePlayerPanel) {
		// 	JButton goToMainMenuButton = new JButton("Main Menu");
		// 	goToMainMenuButton.setFont(buttonFont);
		// 	goToMainMenuButton.setUI(new CustomButtonUI());
		// 	goToMainMenuButton.addActionListener(e -> {
		// 		frame.getContentPane().removeAll();
		// 		frame.add(gameSetupPanel);
		// 		gameSetupPanel.setVisible(true);
		// 		frame.revalidate();
		// 		frame.repaint();
		// 	});

		// 	parent.add(goToMainMenuButton, 0);
		// }

		whiteAiLvlPanel.add(new JLabel("AI Level: "));
		JComboBox<String> whiteAiLvl = new JComboBox<>(aiLvlString);
		whiteAiLvl.addActionListener(
				e -> aiLevelChoices[playerIndex] = whiteAiLvl.getSelectedIndex());
		whiteAiLvlPanel.add(whiteAiLvl);

		JLabel playerLabel = new JLabel(playerString + " Player: ");
		playerLabel.setFont(new Font("Dialog", Font.BOLD, 28));
		parent.add(playerLabel);

		JComboBox<String> whitePlayerType = new JComboBox<>(playerTypeString);
		whitePlayerType.setFont(new Font("Dialog", Font.PLAIN, 20));
		whiteAiLvl.setFont(new Font("Dialog", Font.PLAIN, 20));
		whitePlayerType.setPreferredSize(new Dimension(120, 30));
		// whitePlayerType.setPreferredSize(null);
		whitePlayerType.addActionListener(e -> onPlayerTypeChange(whitePlayerType, whiteAiLvlPanel, playerIndex));

		// Set the background image based on player type
		String fileName = playerString.equals("White") ? "WhitepiecesBackground.jpg" : "BlackpiecesBackground.jpg";
		ClassLoader classLoader = Main.class.getClassLoader();
		java.net.URL backgroundImageURL = classLoader.getResource("img/" + fileName);
		ImageIcon backgroundImageIcon = new ImageIcon(backgroundImageURL);

		
		// Create the background panel
		BackgroundPanel backgroundPanel = new BackgroundPanel(backgroundImageIcon.getImage());

		// Set the layout for the player panel
		parent.setLayout(new BorderLayout());

		// // Add the background panel to the player panel
		// parent.add(backgroundPanel, BorderLayout.CENTER);
		// parent.add(whiteAiLvlPanel, BorderLayout.SOUTH);
		// if (parent == whitePlayerPanel) {
		// parent.add(whitePlayerType);
		// JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		// JButton goToMainMenuButton=new JButton("Main Menu");
		// goToMainMenuButton.setFont(buttonFont);
		// goToMainMenuButton.setUI(new CustomButtonUI());
		// goToMainMenuButton.addActionListener(e -> {
		// frame.getContentPane().removeAll();
		// frame.add(gameSetupPanel);
		// gameSetupPanel.setVisible(true);
		// frame.revalidate();
		// frame.repaint();
		// });

		// buttonPanel.add(whitePlayerType);
		// buttonPanel.add(goToMainMenuButton);

		// parent.add(buttonPanel, BorderLayout.NORTH);
		// parent.add(backgroundPanel, BorderLayout.CENTER);
		// parent.add(whiteAiLvlPanel, BorderLayout.SOUTH);
		// // parent.add(whiteAiLvlPanel, BorderLayout.NORTH);
		// }
		// Add the background panel to the player panel
		parent.add(backgroundPanel, BorderLayout.CENTER);
		parent.add(whitePlayerType,BorderLayout.NORTH);
		parent.add(whiteAiLvlPanel, BorderLayout.SOUTH);

		if (parent == whitePlayerPanel) {
			JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

			JButton goToMainMenuButton = new JButton("Main Menu");
			goToMainMenuButton.setFont(buttonFont);
			goToMainMenuButton.setUI(new CustomButtonUI());
			goToMainMenuButton.addActionListener(e -> {
				frame.getContentPane().removeAll();
				frame.add(gameSetupPanel);
				gameSetupPanel.setVisible(true);
				frame.revalidate();
				frame.repaint();
			});

			buttonPanel.add(goToMainMenuButton);
			buttonPanel.add(whitePlayerType);

			parent.add(buttonPanel, BorderLayout.NORTH);
		}

		onPlayerTypeChange(whitePlayerType, whiteAiLvlPanel, playerIndex);

		return whiteAiLvlPanel;
	}

	// public JPanel createPlayerPanel(JPanel parent, String playerString, int playerIndex) {
	// 	JPanel aiLvlPanel = new JPanel(new BorderLayout());
	// 	aiLvlPanel.add(new JLabel("AI Level: "));
	
	// 	JComboBox<String> aiLvl = new JComboBox<>(aiLvlString);
	// 	aiLvl.addActionListener(e -> aiLevelChoices[playerIndex] = aiLvl.getSelectedIndex());
	// 	aiLvlPanel.add(aiLvl);
	
	// 	JLabel playerLabel = new JLabel(playerString + " Player: ");
	// 	playerLabel.setFont(new Font("Dialog", Font.BOLD, 28));
	// 	parent.add(playerLabel);
	
	// 	JComboBox<String> playerType = new JComboBox<>(playerTypeString);
	// 	playerType.setFont(new Font("Dialog", Font.PLAIN, 20));
	// 	playerType.setPreferredSize(new Dimension(120, 30));
	// 	playerType.addActionListener(e -> onPlayerTypeChange(playerType, aiLvlPanel, playerIndex));
	
	// 	// Set the background image based on player type
	// 	String fileName = playerString.equals("White") ? "WhitepiecesBackground.jpg" : "BlackpiecesBackground.jpg";
	// 	ClassLoader classLoader = Main.class.getClassLoader();
	// 	java.net.URL backgroundImageURL = classLoader.getResource("img/" + fileName);
	// 	ImageIcon backgroundImageIcon = new ImageIcon(backgroundImageURL);
	
	// 	// Create the background panel
	// 	BackgroundPanel backgroundPanel = new BackgroundPanel(backgroundImageIcon.getImage());
	
	// 	// Set the layout for the parent panel
	// 	parent.setLayout(new BorderLayout());
	
	// 	// Add the background panel and AI level panel to the parent panel
	// 	parent.add(backgroundPanel, BorderLayout.CENTER);
	// 	parent.add(aiLvlPanel, BorderLayout.SOUTH);
	
	// 	// Check if it's the white player panel
	// 	if (playerString.equals("White")) {
	// 		JButton goToMainMenuButton = new JButton("Main Menu");
	// 		goToMainMenuButton.setFont(buttonFont);
	// 		goToMainMenuButton.setUI(new CustomButtonUI());
	// 		goToMainMenuButton.addActionListener(e -> {
	// 			frame.getContentPane().removeAll();
	// 			frame.add(gameSetupPanel);
	// 			gameSetupPanel.setVisible(true);
	// 			frame.revalidate();
	// 			frame.repaint();
	// 		});
	
	// 		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	// 		buttonPanel.add(goToMainMenuButton);
	// 		buttonPanel.add(playerType);
	// 		parent.add(buttonPanel, BorderLayout.NORTH);
	// 	}
	
	// 	onPlayerTypeChange(playerType, aiLvlPanel, playerIndex);
	
	// 	return aiLvlPanel;
	// }
	// public JPanel createPlayerPanel(JPanel parent, String playerString, int
	// playerIndex) {
	// JPanel playerPanel=new JPanel(new BorderLayout());

	// if (parent == whitePlayerPanel) {
	// JButton goToMainMenuButton=new JButton("Main Menu");
	// goToMainMenuButton.setFont(buttonFont);
	// goToMainMenuButton.setUI(new CustomButtonUI());
	// goToMainMenuButton.addActionListener(e -> {
	// frame.getContentPane().removeAll();
	// frame.add(gameSetupPanel);
	// gameSetupPanel.setVisible(true);
	// frame.revalidate();
	// frame.repaint();
	// });

	// playerPanel.add(goToMainMenuButton, BorderLayout.NORTH);
	// }

	// // Set background image based on player color
	// String fileName=playerString.equals("White") ? "WhiteBackground.png" :
	// "BlackBackground.png";
	// String filePath=System.getProperty("user.dir") + "/assets/img/" + fileName;
	// ImageIcon backgroundImage=new ImageIcon(filePath);
	// JLabel backgroundLabel=new JLabel(backgroundImage);
	// playerPanel.add(backgroundLabel, BorderLayout.CENTER);

	// playerPanel.add(new JLabel("AI Level: "), BorderLayout.WEST);
	// JComboBox<String> aiLevelComboBox=new JComboBox<>(aiLvlString);
	// aiLevelComboBox.addActionListener(e -> aiLevelChoices[playerIndex] =
	// aiLevelComboBox.getSelectedIndex() + 2);
	// playerPanel.add(aiLevelComboBox, BorderLayout.EAST);

	// JLabel playerLabel=new JLabel(playerString + " Player: ");
	// playerLabel.setFont(new Font("Dialog", Font.BOLD, 28));
	// playerPanel.add(playerLabel, BorderLayout.SOUTH);

	// JComboBox<String> playerTypeComboBox=new JComboBox<>(playerTypeString);
	// playerTypeComboBox.setFont(new Font("Dialog", Font.PLAIN, 20));
	// aiLevelComboBox.setFont(new Font("Dialog", Font.PLAIN, 20));
	// playerTypeComboBox.setPreferredSize(new Dimension(120, 30));
	// playerTypeComboBox.addActionListener(e ->
	// onPlayerTypeChange(playerTypeComboBox, playerPanel, playerIndex));
	// playerPanel.add(playerTypeComboBox, BorderLayout.SOUTH);

	// onPlayerTypeChange(playerTypeComboBox, playerPanel, playerIndex);

	// parent.add(playerPanel);
	// return playerPanel;
	// }

	/**
	 * Handles player type changes in the combo box UI
	 * 
	 * @param playerTypeComboBox The combo box to check
	 * @param aiLvlPanel         A panel with a level selector
	 * @param playerIndex        The index of the player we're setting values for
	 */
	private void onPlayerTypeChange(JComboBox<String> playerTypeComboBox, JComponent aiLvlPanel, int playerIndex) {
		aiLvlPanel.setVisible(playerTypeComboBox.getSelectedItem().equals(AI_OPTION));
		playerTypeChoices[playerIndex] = (String) playerTypeComboBox.getSelectedItem();
	}

	/**
	 * Reads the player type in a combo box and returns the string
	 * 
	 * @return The selected player string
	 */
	public String chosenPlayerType() {
		return (String) playerType.getSelectedItem();
	}

	/** Specifies what files are accepted by the JFileChooser */
	class PGNFileFilter extends FileFilter {
		@Override
		public boolean accept(File file) {
			if (file.isDirectory())
				return true;
			String fileName = file.getName().toLowerCase();
			return fileName.endsWith(".pgn");
		}

		@Override
		public String getDescription() {
			return "PGN Files (*.pgn)";
		}
	}

	/** Start the game */
	private void startGame(GameModel model, boolean tutorial) {
		// //Create a new model if we want to start a new game
		// if(model==null){
		// model=new GameModel();
		// }
		// //cant use lambda expression bcuz local variables referenced from a lambda
		// expression must be final or effectively final
		// final GameModel finalModel=model;
		// //need to make a new gamemodel to start a newGame,
		// chessGame.someMethodToResetTheGameModel(model)?

		JPanel leftPanel = new JPanel(), rightPanel = new JPanel(), topPanel = new JPanel();
		JButton mainMenuButton = new JButton("Main Menu"), settingsButton = new JButton("Settings"),
				saveButton = new JButton("Save"), loadButton = new JButton("Load"),
				undoButton = new JButton("Undo", undoIcon), redoButton = new JButton("Redo", redoIcon),
				hintButton = new JButton("Hint", hintIcon), confirmMove = new JButton("Confirm");
		Font buttonFont = new Font("Dialog", Font.PLAIN, 18);
		hintButton.addActionListener(e -> showHint());
		hintButton.setFont(buttonFont);
		hintButton.setUI(new CustomButtonUI());

		System.out.println("Start game is working");

		if (!tutorial)
			sendPlayersToModel();// if not playing tutorial mode

		playerPanel.setVisible(false);
		frame.getContentPane().remove(playerPanel);

		gamePanel = new JPanel();
		gamePanel.setLayout(new BorderLayout());

		// chessPanel=new JPanel();
		// Create an instance of BoardUI and add it to chessPanel
		// GameLogic gameLogic=new GameLogic();
		// BoardUI boardUI=new BoardUI(chessGame);
		// boardUI.setPreferredSize(800,800);
		chessPanel.add(boardUI, BorderLayout.CENTER);
		// chessPanel.setPreferredSize(new Dimension());

		topPanel.setLayout(new BorderLayout());
		// testing with size50
		topPanel.setPreferredSize(new Dimension(frame.getWidth(), 50));

		saveButton.setPreferredSize(new Dimension(140, 40));
		saveButton.setUI(new CustomButtonUI());
		saveButton.setFont(buttonFont);
		loadButton.setPreferredSize(new Dimension(140, 40));
		loadButton.setFont(buttonFont);
		loadButton.setUI(new CustomButtonUI());
		saveButton.addActionListener(e -> saveGame());
		loadButton.addActionListener(e -> loadGame());
		pointsLabel = new JLabel("Player Turn: White ", SwingConstants.CENTER);
		ClassLoader classLoader = Main.class.getClassLoader();
		java.net.URL thinkingImageURL = classLoader.getResource("img/thinking.gif");
		ImageIcon thinkingIcon = new ImageIcon(thinkingImageURL);
		pointsLabel.setIcon(thinkingIcon);
		pointsLabel.setFont(new Font("Dialog", Font.BOLD, 24));

		// what to do on the settings?
		settingsButton.setPreferredSize(new Dimension(195, 40));
		settingsButton.setFont(buttonFont);
		settingsButton.addActionListener(e -> settingsMenu());
		settingsButton.setUI(new CustomButtonUI());
		// alt+s
		settingsButton.setMnemonic(KeyEvent.VK_S);

		mainMenuButton.setPreferredSize(new Dimension(195, 40));
		mainMenuButton.setFont(buttonFont);
		// alt+m
		mainMenuButton.setMnemonic(KeyEvent.VK_M);
		// gotta fix it, so it actually repaints the 1st one zzzzz
		// mainMenuButton.addActionListener(e->new GameSetupUI(model,boardUI));
		mainMenuButton.addActionListener(e -> {
			menuBar.setVisible(false);
			moveList.setText("");
			frame.getContentPane().removeAll();
			frame.add(gameSetupPanel);
			gameSetupPanel.setVisible(true);
			frame.revalidate();
			frame.repaint();
		});
		mainMenuButton.setUI(new CustomButtonUI());
		JButton tutorialButton = new JButton("Tutorial & Strategies");
		tutorialButton.setPreferredSize(new Dimension(240, 40));
		tutorialButton.setFont(buttonFont);
		tutorialButton.setUI(new CustomButtonUI());
		tutorialButton.addActionListener(e -> tutoMenu());
		// add components to top panel
		leftPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		leftPanel.add(saveButton);
		leftPanel.add(loadButton);
		leftPanel.add(tutorialButton);

		topPanel.add(leftPanel, BorderLayout.WEST);
		topPanel.add(pointsLabel, BorderLayout.CENTER);

		rightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		rightPanel.add(settingsButton);
		rightPanel.add(mainMenuButton);
		topPanel.add(rightPanel, BorderLayout.EAST);

		// moveListPanel=new JPanel();
		moveList = new JTextArea(200, 100);
		moveList.setEditable(false);
		moveList.setFont(new Font("Dialog", Font.PLAIN, 20));
		// moveList.setForeground(Color.WHITE);

		inputTextField = new JTextField(20);
		inputTextField.addActionListener(e -> pgnInputMove());

		undoButton.setPreferredSize(new Dimension(120, 40));
		undoButton.setFont(buttonFont);
		undoButton.setUI(new CustomButtonUI());
		undoButton.addActionListener(e -> {
			// Perform the undo operation on the model
			model.undoMove();
			String lastMove = removeLastMove(moveList);
			String lastMove2 = peekLastMove(moveList);

			if (lastMove2.contains(".")) {
				// If the next last move has a period, remove another move because it's a number
				// label
				lastMove = removeLastMove(moveList);
			}

			if (!lastMove.isEmpty())
				undoneMoves.push(lastMove);// store the move to the spare stack for redo

			// Ensure the UI update is done on the EDT
			SwingUtilities.invokeLater(() -> {
				moveList.repaint();
				moveList.revalidate();
			});
		});

		redoButton.setPreferredSize(new Dimension(130, 40));
		redoButton.setFont(buttonFont);
		redoButton.setUI(new CustomButtonUI());
		redoButton.addActionListener(e -> {
			if (!undoneMoves.empty()) {
				String moveToAdd = undoneMoves.pop();// get previously undone move from stack
				model.redoMove();
				moveList.append(" " + moveToAdd);// put it back on the list
				// Update the UI if necessary
				moveList.repaint();
				moveList.revalidate();
			}
		});

		hintButton.setPreferredSize(new Dimension(130, 40));
		hintButton.setMnemonic(KeyEvent.VK_H);
		confirmMove.setPreferredSize(new Dimension(130, 30));
		confirmMove.setFont(buttonFont);
		confirmMove.addActionListener(e -> {
			addMoveToPanel(moveList.getText());
			pgnInputMove();
		});
		confirmMove.setMnemonic(KeyEvent.VK_ENTER);
		// confirmMove.addActionListener(e -> pgnInputMove());
		confirmMove.setUI(new CustomButtonUI());

		inputPanel = new JPanel();
		inputPanel.setLayout(new BorderLayout());

		// create a nested panel for the buttons
		buttonPanel = new JPanel();
		buttonPanel.add(undoButton);
		buttonPanel.add(redoButton);
		buttonPanel.add(hintButton);

		inputPanel.add(buttonPanel, BorderLayout.NORTH);
		inputPanel.add(inputTextField, BorderLayout.WEST);
		inputPanel.add(confirmMove, BorderLayout.EAST);
		inputPanel.setPreferredSize(new Dimension(280, 100));

		// moveList.setBackground(Color.WHITE);
		moveListPanel.setLayout(new BorderLayout());
		moveListPanel.add(inputPanel, BorderLayout.SOUTH);
		moveListPanel.add(new JScrollPane(moveList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
		moveListPanel.setPreferredSize(new Dimension(400, 600));
		moveListPanel.setFocusable(false);
		frame.setJMenuBar(createMenuBar());

		// chessPanel.setBackground(Color.DARK_GRAY);
		gamePanel.add(topPanel, BorderLayout.NORTH);
		// todo maybe make it west?
		gamePanel.add(chessPanel, BorderLayout.CENTER);
		// gamePanel.add(boardUI,BorderLayout.WEST);
		gamePanel.add(moveListPanel, BorderLayout.EAST);

		// gamePanel.setBackground(Color.BLACK);

		// frame.getContentPane().setBackground(Color.BLACK);
		boardUI.setFrame(frame);
		frame.getContentPane().add(gamePanel);
		frame.revalidate();
		frame.repaint();
		frame.pack();
		frame.setVisible(true);

		if (!tutorial) {
			chessGame.newGame();
			chessGame.startGame();// if not playing tutorial, start game
		}

		boardUI.uiUpdateIfResize(chessGame.getCurrentBoard());
		// boardUI.uiUpdate(chessGame.getCurrentBoard());
		// boardUI.uiUpdate(chessGame.getCurrentBoard());
	}

	/**
	 * See the last move on the text area without removing it
	 * 
	 * @param textArea The text area
	 * @return The last move
	 */
	private String peekLastMove(JTextArea textArea) {
		String content = textArea.getText().trim();
		int lastSpaceIndex = content.lastIndexOf(" ");
		if (lastSpaceIndex != -1) {
			String lastMove = content.substring(lastSpaceIndex + 1);

			// textArea.setText(content.substring(0,lastSpaceIndex));
			return lastMove;
		} else {
			// textArea.setText("");
			return content; // If there's only one move, return it
		}
	}

	// Utility method to remove the last move from a JTextArea and return it
	private String removeLastMove(JTextArea textArea) {
		String content = textArea.getText().trim();
		int lastSpaceIndex = content.lastIndexOf(" ");
		if (lastSpaceIndex != -1) {
			String lastMove = content.substring(lastSpaceIndex + 1);

			textArea.setText(content.substring(0, lastSpaceIndex));
			return lastMove;
		} else {
			textArea.setText("");
			return content; // If there's only one move, return it
		}
	}

	public void updateTurnLabel() {
		String currentPlayer = chessGame.getCurrentPlayer().getTeam() == WHITE ? "White" : "Black";
		pointsLabel.setText("\t Player Turn: " + currentPlayer);
		ClassLoader classLoader = Main.class.getClassLoader();
		java.net.URL thinkingImageURL = classLoader.getResource("img/thinking.gif");
		ImageIcon thinkingIcon = new ImageIcon(thinkingImageURL);
		pointsLabel.setIcon(thinkingIcon);

	}

	/** Sets the players of the model depending on our settings */
	private void sendPlayersToModel() {
		// Initialize some default players for now
		Player p1, p2;

		if (playerTypeChoices[0].equals("Human"))
			p1 = new HumanPlayer(WHITE, boardUI, this);
		else
			p1 = new AIPlayer(WHITE, aiLevelChoices[0], chessGame, this);

		if (playerTypeChoices[1].equals("Human"))
			p2 = new HumanPlayer(BLACK, boardUI, this);
		else
			p2 = new AIPlayer(BLACK, aiLevelChoices[1], chessGame, this);

		boolean currentPlayerWhite = chessGame.getCurrentPlayer() == null
				|| chessGame.getCurrentPlayer().getTeam() != BLACK;

		if (currentPlayerWhite)
			chessGame.setPlayers(p1, p2);
		else
			chessGame.setPlayers(p2, p1);
	}

	/** This is called when we want to make a PGN input move */
	private void pgnInputMove() {
		boolean bool = boardController.pgnMoveInput(inputTextField.getText());
		if (bool) {
			inputTextField.setText("");
		} else {
			JOptionPane.showMessageDialog(frame, "Invalid Move.", "What a noob", JOptionPane.WARNING_MESSAGE);
			inputTextField.setText("");
		}
	}

	/**
	 * This sets whether we can interact with the move input field
	 * 
	 * @param b Can we interact with it?
	 */
	public void setPgnInputInteractable(boolean b) {
		inputTextField.setEditable(b);
	}

	/**
	 * Adds a move to the displayed list beside the board
	 * 
	 * @param moveString The next move string
	 */
	public void addMoveToPanel(String moveString) {
		// Get the current move count using a method call
		int moveCount = chessGame.getMoveCount();
		// Determine the player's color based on the current player
		String playerColor = (chessGame.getCurrentPlayer().getTeam() == Types.WHITE) ? "White" : "Black";
		// Need to adjust the move number for Black, since it should only increment
		// after White's move
		int displayedMoveNumber = (playerColor.equals("Black")) ? (moveCount / 2 + 1) : (moveCount / 2);
		// Append the move number before the first move and every subsequent odd move
		if (moveCount % 2 == 0) {
			// If it's the first move, reset the text. Otherwise, add a newline before the
			// move number.
			if (moveCount == 0)
				clearMovePanel();
			else
				moveList.append("\n");
			// Append the move number
			moveList.append((moveCount / 2 + 1) + ". ");
		}
		// Append the move
		moveList.append(moveString + " ");
		// Update the turn label based on the current player
		updateTurnLabel();
	}

	/** Clears the PGN text list */
	public void clearMovePanel() {
		moveList.setText("");
	}

	/** Create the menubar in a separate method */
	private JMenuBar createMenuBar() {
		menuBar = new JMenuBar();

		fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		newGameMenuItem = new JMenuItem("New Game");
		JMenuItem tutorialGameMenuItem = new JMenuItem("Tutorial Game");
		tutorialGameMenuItem.setMnemonic(KeyEvent.VK_T);
		tutorialGameMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK));
		tutorialGameMenuItem.addActionListener(e -> {
			tutoGame();
		});
		newGameMenuItem.addActionListener(e -> {
			chessGame.newGame();
			moveList.setText("");
		});
		// adding a mnemonic so that when the user presses alt+E(on windows/linux) or
		// option+e(on Mac)
		newGameMenuItem.setMnemonic(KeyEvent.VK_N);
		newGameMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));

		saveMenuItem = new JMenuItem("Save Game");
		saveMenuItem.setMnemonic(KeyEvent.VK_S);
		saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
		saveMenuItem.addActionListener(e -> saveGame());

		exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.setMnemonic(KeyEvent.VK_F4);
		exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_DOWN_MASK));
		exitMenuItem.addActionListener(e -> handleExit());

		loadMenuItem = new JMenuItem("Load Game");
		loadMenuItem.setMnemonic(KeyEvent.VK_O);
		loadMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
		loadMenuItem.addActionListener(e -> loadGame());
		fileMenu.add(newGameMenuItem);
		// fileMenu.add(tutorialGameMenuItem);
		fileMenu.add(saveMenuItem);
		fileMenu.add(loadMenuItem);
		fileMenu.add(exitMenuItem);

		editMenu = new JMenu("Edit");
		editMenu.setMnemonic(KeyEvent.VK_E);
		undoMoveMenuItem = new JMenuItem("Undo Move");
		undoMoveMenuItem.addActionListener(e -> {
			chessGame.undoMove();
			String lastMove = removeLastMove(moveList);
			if (!lastMove.isEmpty())
				undoneMoves.push(lastMove);// store the move to the spare stack for redo

			// Ensure the UI update is done on the EDT
			SwingUtilities.invokeLater(() -> {
				moveList.repaint();
				moveList.revalidate();
			});
		});
		undoMoveMenuItem.setMnemonic(KeyEvent.VK_Z);
		undoMoveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
		redoMoveMenuItem = new JMenuItem("Redo Move");
		redoMoveMenuItem.addActionListener(e -> {
			if (!undoneMoves.empty()) {
				String moveToAdd = undoneMoves.pop();// get previously undone move from stack
				chessGame.redoMove();
				moveList.append(" " + moveToAdd);// put it back on the list
				// Update the UI if necessary
				moveList.repaint();
				moveList.revalidate();
			}
		});
		redoMoveMenuItem.setMnemonic(KeyEvent.VK_R);
		redoMoveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK));
		editMenu.add(undoMoveMenuItem);
		editMenu.add(redoMoveMenuItem);

		tutorialMenu = new JMenu("Tutorial");
		tutorialMenu.setMnemonic(KeyEvent.VK_T);
		rulesMenuItem = new JMenuItem("Rules of Chess");
		kingMenuItem = new JMenuItem("The King");
		pawnMenuItem = new JMenuItem("The Pawn");
		rookMenuItem = new JMenuItem("The Rook");
		bishopMenuItem = new JMenuItem("The Bishop");
		knightMenuItem = new JMenuItem("The Knight");
		queenMenuItem = new JMenuItem("The Queen");
		rulesMenuItem.addActionListener(e -> openRulesFrame("Rules of Chess"));
		kingMenuItem.addActionListener(e -> openRulesFrame("The King"));
		pawnMenuItem.addActionListener(e -> openRulesFrame("The Pawn"));
		rookMenuItem.addActionListener(e -> openRulesFrame("The Rook"));
		bishopMenuItem.addActionListener(e -> openRulesFrame("The Bishop"));
		knightMenuItem.addActionListener(e -> openRulesFrame("The Knight"));
		queenMenuItem.addActionListener(e -> openRulesFrame("The Queen"));
		tutorialMenu.add(rulesMenuItem);
		tutorialMenu.add(kingMenuItem);
		tutorialMenu.add(pawnMenuItem);
		tutorialMenu.add(rookMenuItem);
		tutorialMenu.add(bishopMenuItem);
		tutorialMenu.add(knightMenuItem);
		tutorialMenu.add(queenMenuItem);

		helpMenu = new JMenu("Help");
		helpMenu.setMnemonic(KeyEvent.VK_H);

		uiMenuItem = new JMenuItem("UI");
		savingMenuItem = new JMenuItem("Saving and Loading");
		movesMenuItem = new JMenuItem("Making Moves");
		pgnMenuItem = new JMenuItem("Algebraic Notation");
		JMenuItem aboutMenuItem = new JMenuItem("About The Application");
		uiMenuItem.addActionListener(e -> openHelpFrame("Ui Help"));
		savingMenuItem.addActionListener(e -> openHelpFrame("Saving and Loading"));
		movesMenuItem.addActionListener(e -> openHelpFrame("Moves"));
		pgnMenuItem.addActionListener(e -> openHelpFrame("Pgn"));
		aboutMenuItem.addActionListener(e -> aboutMenu());
		helpMenu.add(uiMenuItem);
		helpMenu.add(savingMenuItem);
		helpMenu.add(movesMenuItem);
		helpMenu.add(pgnMenuItem);
		helpMenu.add(aboutMenuItem);

		setFont(menuBar);

		setMenuFont(fileMenu);
		setMenuFont(editMenu);
		setMenuFont(tutorialMenu);
		setMenuFont(helpMenu);

		setMenuItemFont(newGameMenuItem);
		setMenuItemFont(tutorialGameMenuItem);
		setMenuItemFont(saveMenuItem);
		setMenuItemFont(loadMenuItem);
		setMenuItemFont(exitMenuItem);
		setMenuItemFont(undoMoveMenuItem);
		setMenuItemFont(redoMoveMenuItem);
		setMenuItemFont(rulesMenuItem);
		setMenuItemFont(kingMenuItem);
		setMenuItemFont(pawnMenuItem);
		setMenuItemFont(rookMenuItem);
		setMenuItemFont(bishopMenuItem);
		setMenuItemFont(knightMenuItem);
		setMenuItemFont(queenMenuItem);
		setMenuItemFont(uiMenuItem);
		setMenuItemFont(savingMenuItem);
		setMenuItemFont(movesMenuItem);
		setMenuItemFont(pgnMenuItem);
		setMenuItemFont(aboutMenuItem);

		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(tutorialMenu);
		menuBar.add(helpMenu);

		return menuBar;
	}

	// helper methods to set the font for the entire menu

	/** Sets the font for the menu */
	private void setMenuFont(JMenu menu) {
		setFont(menu);
		for (Component component : menu.getMenuComponents()) {
			if (component instanceof JMenuItem) {
				setFont(component);
			}
		}
	}

	/** Sets the font for the menu items */
	private void setMenuItemFont(JMenuItem menuItem) {
		setFont(menuItem);
	}

	/** Sets the main font */
	private void setFont(Component component) {
		component.setFont(new Font("Dialog", Font.PLAIN, 18));
	}

	/** Shows the confirm exit dialog box */
	private void handleExit() {
		int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Confirmation",
				JOptionPane.YES_NO_OPTION);
		if (result == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}

	/**
	 * Shows the rules of the game
	 * 
	 * @param rulesTitle Which rule to display
	 */
	private void openRulesFrame(String rulesTitle) {
		JFrame rulesFrame = new JFrame(rulesTitle);
		JLabel rulesLabel = new JLabel();
		rulesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		rulesFrame.setSize(new Dimension(900, 600));
		rulesLabel.setText(GameSetupUIContent.lookupRulesText(rulesTitle));// that saved a few lines
		rulesLabel.setHorizontalAlignment(JLabel.CENTER);
		rulesLabel.setVerticalAlignment(JLabel.CENTER);
		rulesLabel.setFont(new Font("Dialog", Font.BOLD, 18));
		rulesFrame.add(rulesLabel);
		rulesFrame.setLocationRelativeTo(null);
		rulesFrame.setVisible(true);
	}

	/**
	 * Opens the help interface
	 * 
	 * @param helpTitle Which help topic to display
	 */
	private void openHelpFrame(String helpTitle) {
		JFrame helpFrame = new JFrame(helpTitle);
		helpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		helpFrame.setSize(new Dimension(900, 600));

		JLabel helpLabel = new JLabel();
		switch (helpTitle) {
			case "Ui Help":
				helpLabel.setText(
						"<html><center>To Start A New Game you can press CTRL+N.<br><br>Alternatively you can open the File Menu by pressing ALT+F and browsing to New Game</center></html>");
				break;
			case "Saving and Loading":
				helpLabel.setText(
						"<html><center>To Save a Game you can press CTRL+S or Press on The Save Button.<br><br>To Load a Game you can press CTRL+O or Press on The Load Button</center></html>");
				break;
			case "Moves":
				helpLabel.setText(
						"<html><center>To Make a Move you can press on the piece and it will show you which moves are legal.<br><br> You can alternatively input the piece you want to move on the input box.</center></html>");
				break;
			case "Pgn":
				helpLabel.setText(
						"<html><center>PGN stands for Portable Game Notation.<br>It is a plain-text computer-processible format for recording chess games (both the moves and related data) that is easy to read and understand by humans as well.<br>PGN is widely used for the representation of chess game information, and it has become a standard for the interchange of chess games on the internet.<br><br>In PGN, a chess game is represented in a structured manner, including details like the moves played, game information (such as event, site, date), players, result, and additional annotations.<br> The format is versatile and can be used to store a single game or a collection of games.</center></html>");
				break;
		}
		helpLabel.setHorizontalAlignment(JLabel.CENTER);
		helpLabel.setVerticalAlignment(JLabel.CENTER);
		helpLabel.setFont(new Font("Dialog", Font.BOLD, 18));
		helpFrame.add(helpLabel);
		helpFrame.setLocationRelativeTo(null);
		helpFrame.setVisible(true);
	}
	// TODO 2mrw

	public void showAIDecisionSpinner() {
		if (chessGame.getCurrentPlayer().isAI()) {
			ClassLoader classLoader = Main.class.getClassLoader();
			java.net.URL thinkingImageURL = classLoader.getResource("img/thinking.gif");
			ImageIcon thinkingIcon = new ImageIcon(thinkingImageURL);
			// thinkingIcon=scaleIcon(thinkingIcon, 50, 50);

			// Update the pointsLabel text and icon
			pointsLabel.setIcon(thinkingIcon);
			pointsLabel.setText("AI is Thinking...");
			System.out.println("Ai is thinking");

		}
	}

	public void hideAIDecisionSpinner() {

		if (!chessGame.getCurrentPlayer().isAI()) {
			// After AI move, reset the pointsLabel
			String currentPlayer = chessGame.getCurrentPlayer().getTeam() == WHITE ? "White" : "Black";
			pointsLabel.setIcon(null);
			pointsLabel.setText("Player Turn :" + currentPlayer);
			System.out.println("hiding the spinner");
		}

	}

	private ImageIcon scaleIcon(ImageIcon icon, int width, int height) {
		Image img = icon.getImage();
		Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		return new ImageIcon(scaledImg);
	}

	// private void showAIDecisionSpinner() {
	// Player currentPlayer=chessGame.getCurrentPlayer();
	// System.out.println("Checking if current player is AI..."); // Debug print

	// if (currentPlayer != null && currentPlayer.isAI()) {
	// System.out.println("Current player is AI. Showing thinking spinner."); //
	// Debug print
	// showThinkingSpinner();
	// } else {
	// System.out.println("Current player is not AI or is null."); // Debug print
	// }
	// }

	// private void showThinkingSpinner() {
	// System.out.println("Preparing to show AI thinking dialog."); // Debug print
	// JDialog dialog=new JDialog(frame, "AI Thinking...", true);
	// ImageIcon icon=new ImageIcon("thinking.gif");

	// JLabel label=new JLabel(icon);
	// label.setHorizontalAlignment(JLabel.CENTER);

	// JLabel messageLabel=new JLabel("AI Thinking...", JLabel.CENTER);
	// messageLabel.setFont(new Font("Dialog", Font.PLAIN, 16));

	// dialog.setLayout(new BorderLayout());
	// dialog.add(label, BorderLayout.CENTER);
	// dialog.add(messageLabel, BorderLayout.SOUTH);

	// dialog.setSize(200, 200);
	// dialog.setLocationRelativeTo(frame);
	// dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
	// dialog.setUndecorated(true);
	// dialog.setVisible(true);

	// System.out.println("AI thinking dialog displayed."); // Debug print
	// }

	// // Method to hide the dialog
	// private void hideThinkingSpinner(JDialog dialog) {
	// System.out.println("Hiding AI thinking dialog."); // Debug print
	// dialog.setVisible(false);
	// dialog.dispose();
	// System.out.println("AI thinking dialog hidden."); // Debug print
	// }

	private void settingsMenu() {
		JFrame settingsFrame = new JFrame("Settings Menu");
		JPanel settingsPanel = new JPanel(new BorderLayout());
		JPanel topPanel = new JPanel(new FlowLayout());
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new GridLayout(3, 2, 5, 5)); // 3 rows, 2 columns, with spacing
		leftPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		JCheckBox showHints = new JCheckBox("Show Hints");
		showHints.addActionListener(e -> showHint());
		topPanel.add(showHints);

		JLabel whitePlayerLabel = new JLabel("White Player:", SwingConstants.CENTER);
		whitePlayerLabel.setFont(new Font("Dialog", Font.BOLD, 16));
		String[] playerTypes = { "Human", "AI" };
		JComboBox<String> whitePlayerDropdown = new JComboBox<>(playerTypes);
		JComboBox<String> whiteAIDifficulty = new JComboBox<>(aiLvlString);
		whiteAIDifficulty.setEnabled(false); // Disabled by default, enabled if AI is selected
		whiteAIDifficulty.addActionListener(e -> aiLevelChoices[0] = whiteAIDifficulty.getSelectedIndex());

		whitePlayerDropdown.addActionListener(e -> {
			String selectedType = (String) whitePlayerDropdown.getSelectedItem();
			whiteAIDifficulty.setEnabled("AI".equals(selectedType));
			onPlayerTypeChange(whitePlayerDropdown, whiteAIDifficulty, 0);
		});

		onPlayerTypeChange(whitePlayerDropdown, whiteAIDifficulty, 0);

		JLabel blackPlayerLabel = new JLabel("Black Player:", SwingConstants.CENTER);
		blackPlayerLabel.setFont(new Font("Dialog", Font.BOLD, 16));
		JComboBox<String> blackPlayerDropdown = new JComboBox<>(playerTypes);
		JComboBox<String> blackAIDifficulty = new JComboBox<>(aiLvlString);
		blackAIDifficulty.setEnabled(false); // Disabled by default, enabled if AI is selected
		blackAIDifficulty.addActionListener(e -> aiLevelChoices[1] = blackAIDifficulty.getSelectedIndex());

		blackPlayerDropdown.addActionListener(e -> {
			String selectedType = (String) blackPlayerDropdown.getSelectedItem();
			blackAIDifficulty.setEnabled("AI".equals(selectedType));
			onPlayerTypeChange(blackPlayerDropdown, blackAIDifficulty, 1);
		});

		onPlayerTypeChange(blackPlayerDropdown, blackAIDifficulty, 1);

		// Add components to the left panel
		leftPanel.add(whitePlayerLabel);
		leftPanel.add(blackPlayerLabel);
		leftPanel.add(whitePlayerDropdown);
		leftPanel.add(blackPlayerDropdown);
		leftPanel.add(whiteAIDifficulty);
		leftPanel.add(blackAIDifficulty);

		settingsPanel.add(leftPanel, BorderLayout.CENTER);

		JComboBox<String> themeDropdown = new JComboBox<>(themes);
		themeDropdown.setSelectedItem(getCurrentTheme());
		topPanel.add(themeDropdown);

		settingsPanel.add(topPanel, BorderLayout.NORTH);

		JButton cancelButton = new JButton("Cancel");
		JButton applyButton = new JButton("Apply");
		JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

		cancelButton.setPreferredSize(new Dimension(100, 40));
		cancelButton.setUI(new CustomButtonUI());
		cancelButton.addActionListener(e -> settingsFrame.dispose());

		applyButton.setPreferredSize(new Dimension(100, 40));
		applyButton.setUI(new CustomButtonUI());
		applyButton.addActionListener(e -> {
			applySettings((String) themeDropdown.getSelectedItem(), showHints.isSelected());
			settingsFrame.dispose();
		});

		btnPanel.add(applyButton);
		btnPanel.add(cancelButton);
		settingsPanel.add(btnPanel, BorderLayout.SOUTH);

		settingsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		settingsFrame.setSize(new Dimension(500, 300));
		settingsFrame.add(settingsPanel);
		settingsFrame.setLocationRelativeTo(null);
		settingsFrame.setVisible(true);

		updateComboboxPlayerChoices(blackPlayerDropdown, blackAIDifficulty, BLACK);
		updateComboboxPlayerChoices(whitePlayerDropdown, whiteAIDifficulty, WHITE);
	}

	/**
	 * Applies the user selected settings
	 * 
	 * @param selectedTheme The theme to apply
	 * @param showHints     Do I show hints? True for yes, False for no
	 */
	private void applySettings(String selectedTheme, boolean showHints) {
		// applyTheme based on the selected theme from the dropdown
		applyTheme(selectedTheme);
		// frame.repaint();
		// frame.revalidate();
		chessGame.toggleAlwaysShowHints(showHints);
		sendPlayersToModel();
		// Start the new current player's turn
		chessGame.getCurrentPlayer().onTurnStart();
	}

	private void updateComboboxPlayerChoices(JComboBox<String> player, JComboBox<String> aiLevel, boolean team) {
		Player p = chessGame.getPlayerFromTeam(team);
		if (p.isAI()) {
			player.setSelectedItem("AI");

			AIPlayer aiPlayer = (AIPlayer) p;
			aiLevel.setSelectedIndex(aiPlayer.getSearchDepth());
		} else {
			player.setSelectedItem("Human");
		}
	}

	/**
	 * helper method to apply the selected theme
	 * 
	 * @param selectedTheme (light,dark)
	 */
	private void applyTheme(String selectedTheme) {
		switch (selectedTheme) {
			case "Dark Theme":
				// Highlighted, pieceHighlighted
				FlatDarkLaf.setup();
				// boardUI.changeTileColors(Color.decode("#404040"),Color.decode("#1A1A1A"));
				boardUI.changeTileColors(Color.decode("#808080"), Color.decode("#404040"));
				boardUI.changeHighlightedTileColors(Color.WHITE, Color.decode("#FFFACD"));
				break;
			case "Light Theme":
				FlatLightLaf.setup();
				boardUI.changeTileColors(Color.decode("#F5F5F5"), Color.decode("#808080"));
				boardUI.changeHighlightedTileColors(Color.decode("#1A1A1A"), Color.decode("#666666"));
				// boardUI.changeTileColors(Color.decode("#D2B48C"), Color.decode("#8B4513"));
				break;
			case "The Kings University Theme":
				chessPanel.setBackground(Color.decode("#7CA2D3"));
				UIManager.put("Label.background", Color.decode("#F5F500"));
				UIManager.put("Label.foreground", Color.decode("#F5F5F5"));
				UIManager.put("Button.background", Color.decode("#F5F5F5"));
				UIManager.put("Button.foreground", Color.decode("#7CA2D3"));
				UIManager.put("Panel.background", Color.decode("#7CA2D3"));
				UIManager.put("Panel.foreground", Color.decode("#FF00FF"));
				UIManager.put("TextArea.background", Color.decode("#7CA2D3"));
				UIManager.put("TextArea.foreground", Color.decode("#F5F5F5"));// TextColor
				UIManager.put("Menu.background", Color.decode("#F5F5F5"));
				UIManager.put("Menu.selectionBackground", Color.decode("#7CA2D3"));
				UIManager.put("Menu.foreground", Color.decode("#7CA2D3"));
				UIManager.put("MenuBar.background", Color.decode("#F5F5F5"));
				UIManager.put("MenuBar.foreground", Color.decode("#7CA2D3"));
				boardUI.changeTileColors(Color.LIGHT_GRAY, Color.decode("#7CA2D3"));
				boardUI.changeHighlightedCheckColor(Color.decode("#ff6600"));
				boardUI.changeHighlightedTileColors(Color.decode("#FF9900"), Color.decode("#FCFCFC"));
				FlatIntelliJLaf.setup();
				break;
			case "Oilers Theme":
				boardUI.setBackground(Color.decode("#ff6600"));
				moveListPanel.setBackground(Color.decode("#ff6600"));
				chessPanel.setBackground(Color.decode("#ff6600"));
				UIManager.put("Label.background", Color.decode("#003366"));
				UIManager.put("Label.foreground", Color.decode("#003366"));
				UIManager.put("Button.background", Color.decode("#003366"));
				UIManager.put("Button.foreground", Color.decode("#ff6600"));
				UIManager.put("Panel.background", Color.decode("#ff6600"));
				UIManager.put("Panel.foreground", Color.decode("#ff6600"));
				UIManager.put("TextArea.background", Color.decode("#ff6600"));
				UIManager.put("TextArea.foreground", Color.decode("#003366"));// TextColor
				UIManager.put("Menu.background", Color.decode("#003366"));
				UIManager.put("Menu.selectionBackground", Color.decode("#003366"));
				UIManager.put("Menu.foreground", Color.decode("#003366"));
				UIManager.put("MenuBar.background", Color.decode("#ff6600"));
				UIManager.put("MenuBar.foreground", Color.decode("#003366"));
				FlatIntelliJLaf.setup();

				// boardUI.changeTileColors(Color.decode("#FF4E01"),Color.decode("#041E41"));
				// boardUI.changeTileColors(Color.decode("#FF6600"), Color.decode("#003366"));
				boardUI.changeTileColors(Color.decode("#808080"), Color.decode("#F5F5F5"));
				boardUI.changeHighlightedCheckColor(Color.decode("#1C1C1C"));
				boardUI.changeHighlightedTileColors(Color.decode("#FF9900"), Color.decode("#666666"));
				break;
			case "Legacy Theme":
				FlatLightLaf.setup();
				// boardUI.changeTileColors(Color.decode("#F5F5F5"), Color.decode("#593E1A"));
				boardUI.changeHighlightedTileColors(Color.decode("#266A2E"), Color.decode("#666666"));
				boardUI.changeTileColors(Color.decode("#D2B48C"), Color.decode("#8B4513"));
				break;
			case "Chess.com Theme":
				chessPanel.setBackground(Color.decode("#EEEED2"));
				UIManager.put("Label.foreground", Color.decode("#593E1A"));
				UIManager.put("Button.background", Color.decode("#EEEED2"));
				UIManager.put("Button.foreground", Color.decode("#593E1A"));
				UIManager.put("Panel.background", Color.decode("#EEEED2"));
				UIManager.put("TextArea.background", Color.decode("#EEEED2"));
				UIManager.put("TextArea.foreground", Color.decode("#593E1A"));
				UIManager.put("Menu.background", Color.decode("#EEEED2"));
				UIManager.put("Menu.selectionBackground", Color.decode("#769656"));
				UIManager.put("Menu.foreground", Color.decode("#769656"));
				UIManager.put("MenuBar.background", Color.decode("#EEEED2"));
				UIManager.put("MenuBar.foreground", Color.decode("#769656"));
				FlatIntelliJLaf.setup();
				boardUI.changeTileColors(Color.decode("#EEEED2"), Color.decode("#769656"));
				boardUI.changeHighlightedTileColors(Color.decode("#593E1A"), Color.decode("#FFFACD"));
				boardUI.changeHighlightedCheckColor(Color.decode("#FF0000"));
				break;
			default:
				FlatDarkLaf.setup();
				break;
		}
		currentTheme = selectedTheme;
		SwingUtilities.invokeLater(() -> {
			SwingUtilities.updateComponentTreeUI(frame);
			frame.revalidate();
			frame.repaint();
		});

		// Updates the UI to use the new images
		boardUI.uiUpdate(chessGame.getCurrentBoard());
	}

	/**
	 * Get the currently active theme.
	 * 
	 * @return The name of the active theme.
	 */
	public String getCurrentTheme() {
		return currentTheme;
	}

	/**
	 * Get the currently active theme folder.
	 * 
	 * @return The name of the active theme folder.
	 */
	public String getCurrentThemeFolder() {
		for (int index = 0; index < themes.length; index++) {
			String theme = themes[index];
			if (getCurrentTheme() == theme) {
				return themeFolders[index];
			}
		}
		return "";
	}

	/** Opens the about menu drop down */
	private void aboutMenu() {
		JLabel titleLabel, creatorLabel, versionLabel, descriptionLabel;
		JFrame aboutFrame = new JFrame("About The Application");
		JPanel aboutPanel = new JPanel(new GridBagLayout());
		aboutFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		aboutFrame.setSize(new Dimension(500, 400));
		aboutFrame.add(aboutPanel);

		titleLabel = new JLabel("Chess 320");
		titleLabel.setFont(new Font("Dialog", Font.BOLD, 22));
		titleLabel.setHorizontalAlignment(JLabel.CENTER);

		creatorLabel = new JLabel(
				"<html>Created by:<br>Adam Jansen<br>Aditya Aggarwal<br>Christopher Bury<br>Dalton Herrewynen<br>Hamza Mhamdi</html>");
		creatorLabel.setHorizontalAlignment(JLabel.CENTER);
		creatorLabel.setVerticalAlignment(JLabel.CENTER);

		versionLabel = new JLabel("Version: 0.1");
		versionLabel.setHorizontalAlignment(JLabel.CENTER);
		versionLabel.setVerticalAlignment(JLabel.CENTER);

		descriptionLabel = new JLabel(
				"<html>&nbsp This chess game application allows you to play chess with various <br>&nbsp settings and features.<br>&nbsp It was created as a project for our Software Engineering Class <br>&nbsp purposes.</html>");
		descriptionLabel.setHorizontalAlignment(JLabel.CENTER);
		descriptionLabel.setVerticalAlignment(JLabel.CENTER);

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 1.0;
		constraints.weighty = 0.1;
		constraints.fill = GridBagConstraints.HORIZONTAL;

		aboutPanel.add(titleLabel, constraints);

		constraints.gridy = 1;
		aboutPanel.add(creatorLabel, constraints);

		constraints.gridy = 2;
		aboutPanel.add(versionLabel, constraints);

		constraints.gridy = 3;
		constraints.weighty = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		aboutPanel.add(descriptionLabel, constraints);

		aboutFrame.setLocationRelativeTo(null);
		aboutFrame.setVisible(true);
	}

	/** Displays the Stalemate end of game message */
	@Override
	public void onStalemate() {
		JFrame gameState = new JFrame("Game Stalemate");
		JLabel stateLabel = new JLabel("Stalemate! No legal moves.");
		stateLabel.setFont(new Font("Dialog", Font.BOLD, 24));
		stateLabel.setHorizontalAlignment(JLabel.CENTER);
		stateLabel.setVerticalAlignment(JLabel.CENTER);
		gameState.setSize(new Dimension(800, 400));
		gameState.setLayout(new BorderLayout());
		gameState.add(stateLabel, BorderLayout.CENTER);
		gameState.setLocationRelativeTo(null);
		gameState.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		gameState.setVisible(true);
	}

	/** Displays the Stalemate end of game message */
	@Override
	public void onRepetition() {
		JFrame gameState = new JFrame("Game Draw");
		JLabel stateLabel = new JLabel("Repetition draw!");
		stateLabel.setFont(new Font("Dialog", Font.BOLD, 24));
		stateLabel.setHorizontalAlignment(JLabel.CENTER);
		stateLabel.setVerticalAlignment(JLabel.CENTER);
		gameState.setSize(new Dimension(800, 400));
		gameState.setLayout(new BorderLayout());
		gameState.add(stateLabel, BorderLayout.CENTER);
		gameState.setLocationRelativeTo(null);
		gameState.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		gameState.setVisible(true);
		// stops the ai from making moves and displaying the frame nonstop
		// AiPlayer aiPlayer.killMoveThread();
	}

	/** Displays the Checkmate end of game message */
	@Override
	public void onCheckmate(Player winner) {
		JFrame gameState = new JFrame("Game Over");
		JPanel btnsPanel = new JPanel();
		JLabel gameWinner = new JLabel("--> " + winner + " <-- has won by Checkmate");
		JButton redoLastMoveButton = new JButton("Undo Move");
		redoLastMoveButton.setPreferredSize(new Dimension(200, 60));
		redoLastMoveButton.setFont(buttonFont);
		redoLastMoveButton.setUI(new CustomButtonUI());
		redoLastMoveButton.addActionListener(e -> {
			gameState.dispose();
			chessGame.undoMove();
		});
		JButton startNewGameButton = new JButton("Start a New Game");
		startNewGameButton.setPreferredSize(new Dimension(320, 60));
		startNewGameButton.setFont(buttonFont);
		startNewGameButton.setUI(new CustomButtonUI());
		startNewGameButton.addActionListener(e -> {
			gameState.dispose();
			chessGame.newGame();
			moveList.setText("");
		});
		JButton goToMainMenuButton = new JButton("Main Menu");
		goToMainMenuButton.setPreferredSize(new Dimension(200, 60));
		goToMainMenuButton.setFont(buttonFont);
		goToMainMenuButton.setUI(new CustomButtonUI());
		goToMainMenuButton.addActionListener(e -> {
			gameState.dispose();
			menuBar.setVisible(false);
			frame.getContentPane().removeAll();
			frame.add(gameSetupPanel);
			gameSetupPanel.setVisible(true);
			frame.revalidate();
			frame.repaint();
		});
		btnsPanel.add(redoLastMoveButton);
		btnsPanel.add(goToMainMenuButton);
		btnsPanel.add(startNewGameButton);
		gameWinner.setFont(new Font("Dialog", Font.BOLD, 28));
		gameWinner.setHorizontalAlignment(JLabel.CENTER);
		gameWinner.setVerticalAlignment(JLabel.CENTER);
		gameState.setSize(new Dimension(800, 400));
		gameState.setLayout(new BorderLayout());
		gameState.add(gameWinner, BorderLayout.CENTER);
		gameState.add(btnsPanel, BorderLayout.SOUTH);
		gameState.setLocationRelativeTo(null);
		gameState.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		gameState.setVisible(true);
	}

	private void tutoMenu() {
		JFrame tutorialFrame = new JFrame("Chess Strategies");
		tutorialFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		tutorialFrame.setSize(1800, 1200);

		// Create a root node for the tree
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Chess Tutorial");
		rootNode.add(new DefaultMutableTreeNode("How to Play Chess"));
		rootNode.add(new DefaultMutableTreeNode("Rules of The Pieces"));
		rootNode.add(new DefaultMutableTreeNode("Special Moves"));

		// Add expandable sections
		DefaultMutableTreeNode openingNode = new DefaultMutableTreeNode("Openings");
		openingNode.add(new DefaultMutableTreeNode("Italian Game"));
		openingNode.add(new DefaultMutableTreeNode("Ruy Lopez"));
		openingNode.add(new DefaultMutableTreeNode("Queen's Gambit"));
		openingNode.add(new DefaultMutableTreeNode("King's Gambit"));
		openingNode.add(new DefaultMutableTreeNode("French Defence"));
		openingNode.add(new DefaultMutableTreeNode("English Opening"));
		rootNode.add(openingNode);

		DefaultMutableTreeNode middleGameNode = new DefaultMutableTreeNode("Middle Game");
		middleGameNode.add(new DefaultMutableTreeNode("Opening Transition to Middle Game"));
		middleGameNode.add(new DefaultMutableTreeNode("Central Control"));
		middleGameNode.add(new DefaultMutableTreeNode("Pawn Structures"));
		middleGameNode.add(new DefaultMutableTreeNode("Piece Activity"));
		middleGameNode.add(new DefaultMutableTreeNode("Tactical Themes"));
		middleGameNode.add(new DefaultMutableTreeNode("Forking Opportunities"));
		rootNode.add(middleGameNode);

		DefaultMutableTreeNode endGameNode = new DefaultMutableTreeNode("End Game");
		endGameNode.add(new DefaultMutableTreeNode("Pawn Endgames"));
		endGameNode.add(new DefaultMutableTreeNode("Knight Endings"));
		endGameNode.add(new DefaultMutableTreeNode("Bishop Endings"));
		endGameNode.add(new DefaultMutableTreeNode("Queen Endgames"));
		endGameNode.add(new DefaultMutableTreeNode("King and Pawn vs. King and Pawn"));
		endGameNode.add(new DefaultMutableTreeNode("Opposite-Colored Bishop Endings"));
		endGameNode.add(new DefaultMutableTreeNode("Two Bishops Endings"));
		endGameNode.add(new DefaultMutableTreeNode("Rook and Pawn vs. Rook"));
		endGameNode.add(new DefaultMutableTreeNode("Minor Piece Endings"));
		endGameNode.add(new DefaultMutableTreeNode("Conversion Technique in Endgames"));
		endGameNode.add(new DefaultMutableTreeNode("Creating Passed Pawns in Endgames"));
		endGameNode.add(new DefaultMutableTreeNode("Endgame Tactics and Themes"));
		endGameNode.add(new DefaultMutableTreeNode("Endgame Strategy Principles"));
		rootNode.add(endGameNode);

		// Create the tree
		JTree tutorialTree = new JTree(rootNode);

		// Create a scroll pane for the tree
		JScrollPane treeScrollPane = new JScrollPane(tutorialTree);

		// Create a text area for displaying content
		JTextArea contentArea = new JTextArea();
		contentArea.setFont(new Font("Dialog", Font.PLAIN, 24));
		contentArea.setEditable(false);
		// contentArea.setCaretPosition(0);

		// Create a split pane to separate tree and content
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeScrollPane,
				new JScrollPane(contentArea));
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(225);

		// Add ActionListener to handle tree node selection
		// tutorialTree.addTreeSelectionListener(e->{
		// DefaultMutableTreeNode selectedNode=(DefaultMutableTreeNode)
		// tutorialTree.getLastSelectedPathComponent();
		// if(selectedNode!=null && selectedNode.isLeaf()){
		// // Display content based on the selected node
		// String nodeText=selectedNode.toString();
		// // Simulate loading content based on the selected node
		// String content=loadContent(nodeText);
		// contentArea.setText(content);
		// }
		// });
		tutorialTree.addTreeSelectionListener(e -> {
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tutorialTree.getLastSelectedPathComponent();
			if (selectedNode != null && selectedNode.isLeaf()) {
				// Display content based on the selected node
				String nodeText = selectedNode.toString();
				// Simulate loading content based on the selected node
				String content = loadContent(nodeText);
				contentArea.setText(content);

				// Scroll to the top of the content area
				contentArea.setCaretPosition(0);
			}
		});

		// Add a button to collapse/expand all nodes
		JButton collapseExpandButton = new JButton("Collapse/Expand All");
		collapseExpandButton.setUI(new CustomButtonUI());
		collapseExpandButton.setFont(buttonFont);
		collapseExpandButton.addActionListener(new ActionListener() {
			boolean isCollapsed = true;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (isCollapsed) {
					expandAll(tutorialTree, new TreePath(rootNode), true);
					isCollapsed = false;
				} else {
					collapseAll(tutorialTree, new TreePath(rootNode), true);
					isCollapsed = true;
				}
			}
		});

		// Create a panel for the button
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(collapseExpandButton);

		// Add components to the frame
		tutorialFrame.add(splitPane, BorderLayout.CENTER);
		tutorialFrame.add(buttonPanel, BorderLayout.SOUTH);

		tutorialFrame.setLocationRelativeTo(null);
		tutorialFrame.setVisible(true);
	}

	// Simulate loading content based on the selected node
	private String loadContent(String nodeName) {
		return GameSetupUIContent.lookupHelpContent(nodeName);
	}

	/** Asks the BoardController to show a hint to the user */
	private void showHint() {
		boardController.showHint();
	}

	/** Method to expand all nodes in a JTree */
	private void expandAll(JTree tree, TreePath parent, boolean expand) {
		// Traverse children
		for (int i = 0; i < tree.getModel().getChildCount(parent.getLastPathComponent()); i++) {
			TreePath path = parent.pathByAddingChild(tree.getModel().getChild(parent.getLastPathComponent(), i));
			expandAll(tree, path, expand);
		}
		// Expand/collapse node
		if (expand)
			tree.expandPath(parent);
		else
			tree.collapsePath(parent);
	}

	/** Method to collapse all nodes in a JTree */
	private void collapseAll(JTree tree, TreePath parent, boolean collapse) {
		// Traverse children
		for (int i = 0; i < tree.getModel().getChildCount(parent.getLastPathComponent()); i++) {
			TreePath path = parent.pathByAddingChild(tree.getModel().getChild(parent.getLastPathComponent(), i));
			collapseAll(tree, path, collapse);
		}
		// Expand/collapse node
		if (collapse)
			tree.collapsePath(parent);
		else
			tree.expandPath(parent);
	}

	/** Launch tutorial game */
	private void tutoGame() {
		gameSetupPanel.setVisible(false);
		startGame(chessGame, true);
		chessGame.newTutorialGame();
		moveList.setText("");
		boardUI.setCanClick(true);
	}

	/** inner class that handles the pawn promotion */
	private class PawnPromotionDialog extends JDialog {
		private pieceType selectedPiece;
		private boolean isWhite;

		public PawnPromotionDialog(JFrame parent, Point location, boolean isWhite) {
			super(parent, "Pawn Promotion", true);
			this.isWhite = isWhite;

			setSize(350, 120);
			setLocation(location);
			setLayout(new GridLayout(1, 4));
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);

			pieceType[] pieceTypes = pieceType.values();

			for (pieceType type : pieceTypes) {
				if (type != pieceType.pawn && type != pieceType.king && type != pieceType.blank) {
					addPieceButton(type);
				}
			}
		}

		private ImageIcon loadPieceIconForDialog(String theme, pieceType type, boolean isWhite, int size) {
			try {
				// String color=isWhite ? "White" : "Black";
				String color = chessGame.getCurrentPlayer().getTeam() == isWhite ? "Black" : "White";
				String iconName = color + " " + type + ".png";
				return loadPieceIcon(theme, iconName, size);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		private void addPieceButton(pieceType type) {
			JButton button = new JButton();
			button.setUI(new CustomButtonUI());

			// Load the image icon for the piece
			try {
				ImageIcon pieceIcon = loadPieceIconForDialog(getCurrentThemeFolder(), type, isWhite, 80);
				if (pieceIcon != null) {
					button.setIcon(pieceIcon);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			button.addActionListener(e -> {
				selectedPiece = type;
				dispose(); // close the dialog when a button is clicked
			});
			add(button);
		}

		private ImageIcon loadPieceIcon(String theme, String iconName, int size) {
			try {
				ClassLoader classLoader = Main.class.getClassLoader();
				java.net.URL imageURL = classLoader.getResource("img/" + theme + "/" + iconName);

				if (imageURL != null) {
					ImageIcon pieceIcon = new ImageIcon(imageURL);
					if (pieceIcon.getImageLoadStatus() != MediaTracker.COMPLETE) {
						System.out.println("Error loading image: " + imageURL);
						return null;
					} else {
						Image scaledImage = pieceIcon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
						return new ImageIcon(scaledImage);
					}
				} else {
					System.out.println("Image not found: " + iconName);
					return null;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		private String getPieceIconPath(pieceType type) {
			String projectRoot = System.getProperty("user.dir");
			String filePath = projectRoot + "/assets/img/White " + type.toString().toLowerCase() + ".png";
			System.out.println("File Path: " + filePath); // Print the file path for debugging
			return filePath;
		}

		public pieceType getSelectedPiece() {
			return selectedPiece;
		}

		/**
		 * Gets the user-selected piece Char code
		 * 
		 * @return a char containing the user's choice
		 */
		public char getSelectedPieceChar() {
			return switch (getSelectedPiece()) {
				case queen -> 'Q';
				case knight -> 'N';
				case bishop -> 'B';
				case rook -> 'R';
				default -> ' ';
			};
		}
	}

	/**
	 * Checks for pawn promotion and opens a dialog for the player to choose the
	 * promoted piece.
	 * This method should be called when a pawn reaches the promotion rank.
	 * 
	 * @param coords The coordinates of the promoted pawn.
	 */
	public void handlePawnPromotion2(Coords coords) {
		// if
		// (Types.charUppercase(PieceCode.decodeChar(chessGame.getCurrentBoard().getSquare(coords)))
		// == 'P'){
		// Pause the game
		boardUI.setCanClick(false);

		boolean isWhite = charUppercase(PieceCode.decodeChar(chessGame.getCurrentBoard().getSquare(coords))) == 'P';

		// Get the screen coordinates of the tile where the pawn was promoted
		Point tileLocation = boardUI.getTileLocationOnScreen(coords);
		// System.out.println("Pawn promo 2");
		// Display promotion dialog relative to the tile
		PawnPromotionDialog promotionDialog = new PawnPromotionDialog(frame, tileLocation, isWhite);
		promotionDialog.setVisible(true);

		// System.out.println("Pawn promo 3");
		// Resume the game
		boardUI.setCanClick(true);

		// Get the selected piece type from the dialog
		pieceType selectedPiece = promotionDialog.getSelectedPiece();
		if (selectedPiece != null) {
			boardController.promotionPiecePicked(selectedPiece);
		}
		// }
	}

	public class CustomButtonUI extends BasicButtonUI {
		@Override
		protected void paintButtonPressed(Graphics g, AbstractButton b) {
			// Leave this method empty to prevent the default blue background when pressed
		}
	}

	/**
	 * Bootloader function for the game
	 * 
	 * @param args Command line arguments, we don't have a use for these yet
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {// Best practices for Swing is create JFrame in here for thread safety
			// GameModel model=new GameModel();
			// GameSetupUI ui=new GameSetupUI(model);
			// ui.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			// ui.frame.setSize(new Dimension(1920, 1080));
			// ui.frame.setPreferredSize(new Dimension(1920, 1080));
			// ui.frame.setLocationRelativeTo(null);
		});
	}
}