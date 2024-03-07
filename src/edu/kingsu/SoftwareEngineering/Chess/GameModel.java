package edu.kingsu.SoftwareEngineering.Chess;

import java.io.File;
import java.util.*;

import javax.swing.*;

import edu.kingsu.SoftwareEngineering.Chess.Types.pieceType;

import static edu.kingsu.SoftwareEngineering.Chess.Types.*;

/**
 * Holds game model
 * @author Group4
 * @version 1
 */
public class GameModel{
	private Board board, initialBoard;
	// Variables to track the position of the number of moves made
	private int totalMoves=0;
	private LinkedList<Integer> intMoveList=new LinkedList<>();
	private ArrayList<Player> players=new ArrayList<>();
	private Player player;
	private ArrayList<BoardListener> boardListeners=new ArrayList<>();
	private ArrayList<GameEndListener> gameEndListeners=new ArrayList<>();
	private HashMap<String,ImageIcon> pieceImages=new HashMap<>();
	private GameSetupUI menuUi;
	private BoardUI boardUI;
	private boolean tutorialEnabled=false;
	private boolean alwaysShowHints=false;

	/** Default constructor makes a standard game board */
	public GameModel(){
		initialBoard=new Board(Board.DEFAULT);
		board=initialBoard;
	}

	/** Starts a normal new game */
	public void newGame(){
		initialBoard=new Board(Board.DEFAULT);
		board=new Board(initialBoard);
		player=getPlayerFromTeam(WHITE);
		intMoveList=new LinkedList<>();
		totalMoves=0;

		toggleTutorial(false);
		boardUI.unHiglightAll();
		menuUi.clearMovePanel();

		notifyListeners();
	}

	/** Starts a normal new game */
	public void new960Game(){
		initialBoard=new Board(Board.CHESS960);
		board=new Board(initialBoard);
		player=getPlayerFromTeam(WHITE);
		intMoveList=new LinkedList<>();
		totalMoves=0;

		toggleTutorial(false);
		boardUI.unHiglightAll();
		menuUi.clearMovePanel();

		notifyListeners();
	}

	/** Starts a new tutorial game */
	public void newTutorialGame(){
		initialBoard=new Board(Board.DEFAULT);
		board=new Board(initialBoard);

		Player p1=new HumanPlayer(WHITE,boardUI,menuUi);
		Player p2=new AIPlayer(BLACK,2,this,menuUi);
		setPlayers(p1,p2);

		player=getPlayerFromTeam(WHITE);
		intMoveList=new LinkedList<>();
		totalMoves=0;

		toggleTutorial(true);
		boardUI.unHiglightAll();
		menuUi.clearMovePanel();

		notifyListeners();

		TutorialHelper.showTutorialIntroBlurb();
	}

	/**
	 * Returns a new default Board instance
	 * @return The Board instance
	 */
	public Board getStartBoard(){
		initialBoard=new Board(Board.DEFAULT);
		return initialBoard;
	}

	/**
	 * Sets which menu interface should be used by the game
	 * @param menuUi the UI class for the menu
	 */
	public void setMenuUI(GameSetupUI menuUi){
		this.menuUi=menuUi;
	}

	/**
	 * Sets which menu interface should be used by the game
	 * @param menuUi the UI class for the menu
	 */
	public void setBoardUI(BoardUI menuUi){
		this.boardUI=menuUi;
	}

	/** Starts the game by allowing the first player to take a turn */
	public void startGame(){
		player=getPlayerFromTeam(WHITE);
		player.onTurnStart();
	}

	/**
	 * Are we running the tutorial?
	 * @return Whether the tutorial is enabled
	 */
	public boolean isTutorial(){
		return tutorialEnabled;
	}

	/**
	 * Toggles tutorial mode
	 * @param b Whether the tutorial is enabled
	 */
	public void toggleTutorial(boolean b){
		tutorialEnabled=b;
	}

	/**
	 * Do we always show hints?
	 * @return Whether the tutorial is enabled
	 */
	public boolean getAlwaysShowHints(){
		return alwaysShowHints;
	}

	/**
	 * Toggles whether we always show hints
	 * @param b Whether hints always show
	 */
	public void toggleAlwaysShowHints(boolean b){
		alwaysShowHints=b;
	}

	/**
	 * Returns a copy of the move list.
	 * @return The move list copy. Editing this doesn't change the version stored in
	 * GameLogic.
	 */
	public LinkedList<Integer> getMoveList(){
		return new LinkedList<>(intMoveList);
	}

	/**
	 * Returns the current Board object
	 * @return current Board object
	 */
	public Board getCurrentBoard(){
		return new Board(board);
	}

	/**
	 * Returns the current Player object
	 * @return current Player object
	 */
	public Player getCurrentPlayer(){
		return player;
	}


	/**
	 * Gets the player object that is associated to a team
	 * @param team The team that we need the player for
	 * @return The player which is controlling a given team
	 */
	public Player getPlayerFromTeam(boolean team){
		for(Player value: players){
			//System.out.println(getTeamString(value.getTeam()));
			if(value.getTeam()==team) return value;
		}
		return null;
	}

	/**
	 * Takes two player objects and makes them control their respective pieces
	 * @param p1 Player object for player 1
	 * @param p2 Player object for player 2
	 */
	public void setPlayers(Player p1,Player p2){
		//Tells the players that they're being removed
		for(Player p: players){
			p.onPlayerRemove();
		}

		players.clear();
		players.add(p1);
		players.add(p2);
		player=p1;
	}

	/** Undo a move */
	public void undoMove(){
		if(intMoveList.isEmpty() || totalMoves==0) return;

		--totalMoves;
		board=new Board(initialBoard);
		for(int i=0; i<totalMoves; i++){
			board.makeMove(intMoveList.get(i));
		}

		player.onTurnEnd();
		boolean newTeam=!(player.getTeam());// invert team
		player=getPlayerFromTeam(newTeam);
		player.onTurnStart();

		boardUI.unHiglightAll();

		// notify the listeners
		notifyListeners();
		menuUi.updateTurnLabel();
	}

	/** Undo the undo */
	public void redoMove(){
		if(intMoveList.isEmpty() || totalMoves==intMoveList.size()) return;
		if(totalMoves<intMoveList.size()){
			board.makeMove(intMoveList.get(totalMoves));
			++totalMoves;
		}

		player.onTurnEnd();
		boolean newTeam=!(player.getTeam());// invert team
		player=getPlayerFromTeam(newTeam);
		player.onTurnStart();

		boardUI.unHiglightAll();

		// notify the listeners
		notifyListeners();
		menuUi.updateTurnLabel();
	}

	/** Trims the move list to match the total move number */
	private void trimMoveList(){
		while(intMoveList.size()>totalMoves){
			intMoveList.removeLast();
		}
	}

	/**
	 * Retrieves an ImageIcon object from the cache for this piece
	 * @param pieceName The piece name to search against
	 * @return An ImageIcon object that we should draw for it
	 */
	public ImageIcon getImageFromCache(String pieceName){
		return pieceImages.get(pieceName);
	}

	/**
	 * Sets an image into internal cache for faster loading later
	 * @param pieceName Name of piece
	 * @param icon      The ImageIcon object to store
	 */
	public void setImageFromCache(String pieceName,ImageIcon icon){
		pieceImages.put(pieceName,icon);
	}

	/**
	 * Calculates the total moves made in the game
	 * @return the number of moves made so far
	 */
	public int getMoveCount(){
		return intMoveList.size();
	}

	/**
	 * Get the turn number based on the move stack
	 * @return The current turn number
	 */
	public int getTurnNumber(){
		return (totalMoves/2)+1;
	}

	private boolean repetitionDraw(){
		if (intMoveList.size()<10) return false;

		//Find the top 6 moves and see if there's 3 repeats
		int whiteMove = 0;
		int blackMove = 0;

		boolean team = getCurrentPlayer().getTeam();
		//We don't check the moves in between what we're looking at
		int[] checkedIndexes = new int[]{10,9,6,5,2,1};

		for (int i = 0; i < checkedIndexes.length; i++) {

			int move = intMoveList.get(intMoveList.size()-checkedIndexes[i]);
			//System.out.println("Stalemate Move "+move);
			if (team==WHITE){
				//System.out.println("White Move "+whiteMove);
				if (i <= 1){
					//We don't care about the first 2 moves. Those are what we compare the other 6 to
					whiteMove=move;
				}
				else if (whiteMove!=move) return false;
			}
			else{
				//System.out.println("Black Move "+blackMove);
				if (i <= 1){
					//We don't care about the first 2 moves. Those are what we compare the other 6 to
					blackMove=move;
				}
				else if (blackMove!=move) return false;
			}

			team = !team;
		}

		return true;
	}

	/**
	 * Takes a move and applies it
	 * @param m the integer move to be made
	 */
	public void makeMove(int m){
		trimMoveList();
		System.out.println("Turn number "+getTurnNumber());//+"  GameModel Made: "+Move.describe(m));

		// get start and the end of the move
		Coords startPos=new Coords(Move.getStartIndex(m));

		// System.out.println(board);
		// System.out.println("Start pos for move: "+startPos);

		Piece piece=board.getSquareObj(startPos);//get the type of the piece

		// System.out.println("Piece for move: "+piece);

		if(piece==null){// Handle empty squares, just want to test if we try to drag/click on an empty tile
			System.out.println("No piece found!");
			return;
		}

		ArrayList<Integer> legalMoves=piece.getLegalMoves(board,startPos.getIndex());// get the legalMoves from the piece
		if(moveContained(m,legalMoves)==UNSET){// escape if the move is not legal
			// handle illegal move, we will make it in a jtextfield
			System.out.println("Illegal move!");
			return;
		}

		board.makeMove(m);
		boardUI.unHiglightAll();

		if(menuUi!=null){
			//m=AlgNotationHelper.updateAlgNotationForMoveObj(m,board,this);
			//menuUi.addMoveToPanel(m.PGNNotation());
			menuUi.addMoveToPanel(AlgNotationHelper.algNotationFromMove(m,board,this.getCurrentPlayer().getTeam(),this));
		}

		// update the player
		boolean newTeam=!(player.getTeam());// invert team
		player.onTurnEnd();
		player=getPlayerFromTeam(newTeam);

		if(player.isStalemated(board)){
			System.out.println(player+" has been stalemated");

			for(GameEndListener listener: gameEndListeners){
				listener.onStalemate();
			}
		}
		if(player.isInCheck(board)){
			boardUI.highlightCheck(board.getKingPosObj(player.getTeam()));
		}
		if(player.isCheckmated(board)){
			System.out.println(player+" has been checkmated");
			for(GameEndListener listener: gameEndListeners){
				listener.onCheckmate(getPlayerFromTeam(!newTeam));
			}
		}
		if(repetitionDraw()){
			System.out.println("Repetition draw");

			for(GameEndListener listener: gameEndListeners){
				listener.onRepetition();
			}
		}

		player.onTurnStart();
		++totalMoves;// increment the total number of moves
		intMoveList.addLast(m);
		//moveList.addLast(m);// add the move to the move list

		//menuUi.checkPromotingPawns();
		// notify the listeners
		notifyListeners();
		menuUi.updateTurnLabel();
	}

	/**
	 * Tests if a PGN move is valid and makes it if it is
	 * @param move The move we want to make
	 * @return Whether the move is legal
	 */
	public boolean makeMove(String move){
		System.out.println("String move");
		//SlowMove newMove=AlgNotationHelper.moveFromAlgebraicNotationObj(move,player.getTeam(),board);
		int newMove=AlgNotationHelper.moveFromAlgebraicNotation(move,player.getTeam(),board);

		if (newMove==Move.blank()||newMove==0){
				return false;
		}

		System.out.println("Piece: "+Move.getPieceName(newMove));
		System.out.println("Start pos: "+new Coords(Move.getStartIndex(newMove)));
		System.out.println("End pos: "+new Coords(Move.getEndIndex(newMove)));
		try{
			
			// Test if the created move object actually works without errors
			Board testBoard=new Board(getCurrentBoard());
			testBoard.makeMove(newMove);
		}catch(Exception e){
			// The move is invalid. Do nothing
			System.out.println(e);
			return false;
		}

		// We know that the move can be made without errors, so make the move
		makeMove(newMove);//TODO PLEASE CHANGE THIS TO NEW SYSTEM, OLD SYSTEM DOES NOT WORK ANYMORE
		return true;
	}

	/**
	 * Checks if a move has the same ending position as anything in a move list.
	 * @param m     The move
	 * @param moves The list of moves
	 * @return Whether we find a match
	 */
	private boolean moveContained(SlowMove m,ArrayList<SlowMove> moves){
		if(m==null) return false;// can't be true if the move is null
		for(SlowMove gotMove: moves){
			if(gotMove==null) continue;
			if(m.end().getIndex()==gotMove.end().getIndex()) return true;
		}
		return false;
	}

	/**
	 * Checks if a move has the same ending position as anything in a move list.
	 * @param m     The move
	 * @param moves The list of moves
	 * @return Whether we find a match
	 */
	private boolean moveContained(Integer m,ArrayList<Integer> moves){
		if(m==null) return false;// can't be true if the move is null
		for(Integer gotMove: moves){
			if(gotMove==null) continue;
			if(Move.getEndIndex(m)==Move.getEndIndex(gotMove)) return true;
		}
		return false;
	}

	/**
	 * Exports the move history to a PGN file
	 * TODO replace with pgn controller
	 */
	public void exportToPgn(){
	}

	/**
	 * Loads move history from a PGN file
	 * TODO replace with pgn controller
	 * @param pgnFile The file the user supplied
	 */
	public void loadFromPgn(File pgnFile){
	}

	/** Notifies all the listeners of any relevant changes */
	public void notifyListeners(){
		for(BoardListener listener: boardListeners){
			listener.onBoardChanged(board);
		}
	}

	/**
	 * Adds a new listener to the list of listeners
	 * @param listener the listener to add
	 */
	public void addListener(BoardListener listener){
		boardListeners.add(listener);
	}

	/**
	 * Adds a new game end listener to the list of listeners
	 * @param listener the listener to add
	 */
	public void addGameEndListener(GameEndListener listener){
		gameEndListeners.add(listener);
	}

	/**
	 * Checks if the King for a given team is in check
	 * @param team Which team's king to look for
	 * @return True if King is in Check, False otherwise
	 */
	public boolean isKingInCheck(boolean team){
		King king;
		if(team==WHITE){
			king=(King) board.getSquareObj(board.getKingPosObj(WHITE));
			return king.isInCheck(board,board.getKingPosObj(WHITE).getIndex());
		}else{
			king=(King) board.getSquareObj(board.getKingPosObj(BLACK));
			return king.isInCheck(board,board.getKingPosObj(BLACK).getIndex());
		}
	}

	/**
	 * Promotes a pawn at the specified coordinates to the given piece type.
	 * @param coords        The coordinates of the promoted pawn.
	 * @param selectedPiece The piece type to promote the pawn to.
	 */
	public void promotePawn(Coords coords,pieceType selectedPiece){
		if(selectedPiece!=null){
			// Get the pawn at the specified coordinates
			Pawn pawn=(Pawn) getCurrentBoard().getSquareObj(coords);

			if(pawn!=null){
				Piece promotedPiece=createPiece(selectedPiece,pawn.getTeam());// Create a new piece of the selected type

				getCurrentBoard().setSquare(PieceCode.Blank,coords);// Remove the pawn from the board
				getCurrentBoard().setSquare(promotedPiece,coords);// Place the promoted piece on the board

				notifyListeners();// Notify listeners of the board change
			}
		}
	}

	/**
	 * Creates a new piece of the specified type and team.
	 * @param type The type of the piece.
	 * @param team The team of the piece.
	 * @return A new piece of the specified type and team.
	 */
	private Piece createPiece(pieceType type,boolean team){
		return switch(type){
			case queen -> new Queen(team);
			case rook -> new Rook(team);
			case bishop -> new Bishop(team);
			case knight -> new Knight(team);
			default -> new Queen(team);//Default to queen if the selected piece type is not recognized
		};
	}
	/*
	 * +undoMove()
	 * +redoMove()
	 * +makeMove(SlowMove)
	 * +exportToPgn()
	 * +loadFromPgn(File)
	 * +highlightLegalMoves(Piece)
	 */
}
