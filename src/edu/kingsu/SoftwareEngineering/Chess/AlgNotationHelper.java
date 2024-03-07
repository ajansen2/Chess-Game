/*
Copyright (C) 2023 Christopher Bury. All Rights Reserved.
License is hereby granted to The Kings University to store, compile, run, and display this file for grading and educational purposes.
 */
package edu.kingsu.SoftwareEngineering.Chess;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import edu.kingsu.SoftwareEngineering.Chess.GameSetupUI.PGNFileFilter;
import edu.kingsu.SoftwareEngineering.Chess.Types.Winner;
import edu.kingsu.SoftwareEngineering.Chess.Types.moveTypes;
import edu.kingsu.SoftwareEngineering.Chess.Types.pieceType;

import static edu.kingsu.SoftwareEngineering.Chess.Types.*;

/**
 * This is a helper class with static functions able to deal with algebraic notation
 * @author Adam Janzen, Christopher Bury
 * @version 1
 */
public class AlgNotationHelper{
	/** These letters represent the files on the chess board */
	public static final char[] COORDINATE_LETTERS=new char[]{'a','b','c','d','e','f','g','h'};
	/** These numbers represent the ranks on the chess board */
	public static final char[] COORDINATE_NUMBERS=new char[]{'1','2','3','4','5','6','7','8'};

	private static SlowMove handleCastlingObj(String notation,boolean team){
		if(notation.equals("O-O")) return SlowMove.GenerateCastle(moveTypes.kingSideCastle,team);
		if(notation.equals("O-O-O")) return SlowMove.GenerateCastle(moveTypes.queenSideCastle,team);
		return null;
	}

	private static int handleCastling(String notation,boolean team){
		if(notation.equals("O-O")) return Move.encodeCastle(Move.kSideCastle,team);
		if(notation.equals("O-O-O")) return Move.encodeCastle(Move.qSideCastle,team);
		return Move.blank();
	}

	private static String filterNotation(String notation){
		StringBuilder filteredNotation=new StringBuilder();
		for(int i=0; i<notation.length(); i++){
			char c=notation.charAt(i);

			//Pawn promotion. Return early
			if(c=='=') return filteredNotation.toString();

			if(c!='+' && c!='#' && c!=' ' && c!='x') filteredNotation.append(c);
		}
		return filteredNotation.toString();
	}

	/**
	 * Filters a line of text for comments by modifying the filter info from the previous line.
	 * @param info The info carried over from the previous line with the new current text to filter
	 */
	private static void filterLineForComments(PgnFilterInfo info){
		StringBuilder filteredNotation=new StringBuilder();
		String line=info.newLine;

		for(int i=0; i<line.length(); i++){
			char c=line.charAt(i);

			if(c=='[') info.brackets++;
			else if(c==']') info.brackets--;
			else if(c=='{') info.curlyBrackets++;
			else if(c=='}') info.curlyBrackets--;
			else if(c=='(') info.parantheses++;
			else if(c==')') info.parantheses--;
			else if(!info.inComment()) filteredNotation.append(c);//Parantheses of all kinds don't get to go in the line
		}
		info.newLine=filteredNotation.toString();
	}

	/**
	 * Filters a line of text for period move numbers by modifying the filter info from the previous line.
	 * @param info The info carried over from the previous line with the new current text to filter
	 */
	private static void filterLineForNumberPeriods(PgnFilterInfo info){
		StringBuilder filteredNotation=new StringBuilder();
		String line=info.newLine;

		boolean periodFound=false;//We skip things here until a space is hit
		boolean letterFound=false;
		int lastSafeIndex=0;//The last safe character index

		for(int i=0; i<line.length(); i++){
			char c=line.charAt(i);

			int letterIndex=Coords.fromLetter(c);

			if(letterIndex>=1 && letterIndex<=8){
				letterFound=true;
			}
			if(Character.isDigit(c)){
				//Don't increment the safe index until we hit a non-period character
				//This could be safe, or it could not
				//It's safe if a letter was found previously, however
				if(!letterFound) continue;
			}
			if(c=='.'){
				//No good. When the next space is hit, we skip everything from the last safe index
				periodFound=true;
				continue;
			}else{
				if(c==' ' && periodFound){
					//We found a space, so shift the safe index if there were periods before
					//This skips the pesky periods entirely
					lastSafeIndex=i;
				}
				periodFound=false;
				letterFound=false;

				//Append everything between the last safe index and here
				//This should happen if we ran into a space but with no periods found
				for(int index=lastSafeIndex; index<=i; index++){
					filteredNotation.append(line.charAt(index));
				}

				//Match the safe index with our current forward index because we're safe
				lastSafeIndex=i+1;
			}
		}
		info.newLine=filteredNotation.toString();
	}

	/**
	 * Filters a line of text for dollar sign numbers by modifying the filter info from the previous line.
	 * @param info The info carried over from the previous line with the new current text to filter
	 */
	private static void filterLineForDollarSigns(PgnFilterInfo info){
		StringBuilder filteredNotation=new StringBuilder();
		String line=info.newLine;

		//We skip things here until a space is hit
		boolean dollarSignFound=false;
		//The last safe character index
		int lastSafeIndex=0;

		for(int i=0; i<line.length(); i++){
			char c=line.charAt(i);

			if(c=='$'){
				//No good. When the next space is hit, we skip everything from the last safe index
				dollarSignFound=true;
				continue;
			}
			if(Character.isDigit(c) && dollarSignFound){
				//Don't increment the safe index until we hit a non number character
				continue;
			}else{
				if(c==' ' && dollarSignFound){
					//We found a space, so shift the safe index if there were periods before
					//This skips the pesky periods entirely
					lastSafeIndex=i;
				}
				dollarSignFound=false;

				//Append everything between the last safe index and here
				//This should happen if we ran into a space but with no periods found
				for(int index=lastSafeIndex; index<=i; index++){
					filteredNotation.append(line.charAt(index));
				}

				//Match the safe index with our current forward index because we're safe
				lastSafeIndex=i+1;
			}
		}
		info.newLine=filteredNotation.toString();
	}

	// This helper class is used to return multiple values from the determineMoveTypeAndChecks method
	private static class MoveInfo{
		moveTypes type;
		int type2;
		int numberOfChecks;
	}

	// This helper class is used to return multiple values from the determineMoveTypeAndChecks method
	private static class PgnFilterInfo{
		String newLine;
		int brackets=0;
		int curlyBrackets=0;
		int parantheses=0;

		public boolean inComment(){
			return brackets!=0 || curlyBrackets!=0 || parantheses!=0;
		}
	}

	// Method to determine move type and checks
	private static MoveInfo determineMoveTypeAndChecks(String notation){
		MoveInfo moveInfo=new MoveInfo();

		moveInfo.type=moveTypes.justMove; // Default type
		moveInfo.type2=Move.normalMove;

		moveInfo.numberOfChecks=0; // Default number of checks

		if(notation.contains("x")){
			moveInfo.type=moveTypes.capture;
			moveInfo.type2=0b00010; //Capture bit flipped
		}
		if(notation.contains("++")){
			moveInfo.numberOfChecks=2;
		}else if(notation.contains("+")){
			moveInfo.numberOfChecks=1;
		}

		return moveInfo;
	}

	// Method to extract start and end positions from a chess move notation
	private static int[] determineCoordinates(String notation){
		// Initialize coordinates with unset state (-1)
		int startX=-1; // X-coordinate for the starting position
		int startY=-1; // Y-coordinate for the starting position
		int endX=-1;   // X-coordinate for the ending position
		int endY=-1;   // Y-coordinate for the ending position

		// The last two characters in the notation should be the end position
		if(notation.length()>=2){
			char fileChar=notation.charAt(notation.length()-2); // File (column)
			char rankChar=notation.charAt(notation.length()-1); // Rank (row)

			System.out.println(fileChar+":"+rankChar);

			// Convert file (a-h) to x-coordinate (0-7)
			if(fileChar>='a' && fileChar<='h'){
				endX=Coords.fromLetter(fileChar)-1; // Coords.fromLetter is 1-indexed
			}
			// Convert rank (1-8) to y-coordinate (0-7)
			if(rankChar>='1' && rankChar<='8'){
				endY=Character.getNumericValue(rankChar)-1; // Character.getNumericValue is 1-indexed
			}
		}

		// Look for starting position differentiators, which are before the end position
		for(int i=0; i<notation.length()-2; i++){
			char ch=notation.charAt(i);
			// If it's a digit, it could be a rank differentiator
			if(Character.isDigit(ch) && startY==-1){
				startY=Character.getNumericValue(ch)-1; // Adjust for 0-indexing
			}
			// If it's a letter in the range a-h, it could be a file differentiator
			else if(ch>='a' && ch<='h' && startX==-1){
				startX=Coords.fromLetter(ch)-1; // Adjust for 0-indexing
			}
		}

		// Return an array with the start and end positions
		return new int[]{startX,startY,endX,endY};
	}


	private static pieceType determinePieceType(String notation,moveTypes type){
		char firstChar=notation.charAt(0);
		char promoChar=' ';
		if(Pattern.compile("[a-h]").matcher(""+firstChar).matches()){
			if(notation.contains("=")){
				type=moveTypes.pawnPromote;

				promoChar=notation.charAt(notation.length()-1);
			}
			return pieceType.pawn;
		}
		return getPieceFromChar(firstChar);
	}

	private static pieceType determinePieceType(String notation){

		if (notation==null||notation.length()==0) return pieceType.blank;

		char firstChar=notation.charAt(0);
		char promoChar=' ';
		if(Pattern.compile("[a-h]").matcher(""+firstChar).matches()){
			if(notation.contains("=")){
				//type=moveTypes.pawnPromote;

				promoChar=notation.charAt(notation.length()-1);
			}
			return pieceType.pawn;
		}
		return getPieceFromChar(firstChar);
	}

	private static SlowMove selectMoveObj(ArrayList<SlowMove> possibleMoves,int startDiffX,int startDiffY,moveTypes type,int numberOfChecks){
		for(SlowMove move: possibleMoves){
			boolean matchX=(startDiffX==-1) || (move.start().getX()==startDiffX);
			boolean matchY=(startDiffY==-1) || (move.start().getY()==startDiffY);
			boolean matchType=move.type().equals(type);
			//boolean matchChecks=(move.numberOfChecks()==numberOfChecks);

			if(matchX && matchY && matchType){
				return move;
			}
		}
		return null; // Return null if no matching move is found
	}

	private static int selectMove(ArrayList<Integer> possibleMoves,int startDiffX,int startDiffY,int moveType,int numberOfChecks){
		for(int move: possibleMoves){
			boolean matchX=(startDiffX==-1) || (Coords.indexToX(Move.getStartIndex(move))==startDiffX);
			boolean matchY=(startDiffY==-1) || (Coords.indexToY(Move.getStartIndex(move))==startDiffY);
			
			//We can pretend pawn double moves are normal moves for these purposes
			boolean matchType=Move.getSpecialCode(move)==moveType 
			|| Move.getSpecialCode(move)==Move.pawnDoubleMove;
			//boolean matchChecks=(move.numberOfChecks()==numberOfChecks);

			System.out.println("Code: " + Move.getSpecialCode(move));
			System.out.println("Move type : " + moveType);

			if(matchX && matchY && matchType){
				return move;
			}
		}
		return Move.blank(); // Return null if no matching move is found
	}


	private static ArrayList<SlowMove> getStartCoordsForPgnMoveObj(Board b,boolean team,Coords endPos,pieceType piecType){
		return b.alliedPiecesObj(team,piecType).stream()
				.flatMap(piecePosPair->piecePosPair.piece().getLegalMovesObj(b,piecePosPair.coords()).stream())
				.filter(move->move.end().getIndex()==endPos.getIndex())
				.peek(move->System.out.println("SlowMove start: "+move.start()))
				.collect(Collectors.toCollection(ArrayList::new));
	}

	private static ArrayList<Integer> getStartCoordsForPgnMove(Board b,boolean team,Coords endPos,pieceType piecType){
		return b.alliedPiecesObj(team,piecType).stream()
				.flatMap(piecePosPair->piecePosPair.piece().getLegalMoves(b,piecePosPair.coords().getIndex()).stream())
				.filter(move->Move.getEndIndex(move)==endPos.getIndex())
				.peek(move->System.out.println("SlowMove start: "+new Coords(Move.getStartIndex(move))))
				.collect(Collectors.toCollection(ArrayList::new));
	}

	/**
	 * Calculates a move given from the given board state and a string in algebraic notation
	 * @param notation The string in algebraic notation
	 * @param team     The team that made the move
	 * @param b        The board
	 * @return A SlowMove object that represents the move, does not check for legality
	 */
	public static SlowMove moveFromAlgebraicNotationObj(String notation,boolean team,Board b){
		//Pattern boardletterPattern=Pattern.compile("[a-h]");

		//Handle castling moves and filter notation
		SlowMove castleMove=handleCastlingObj(notation,team);
		if(castleMove!=null) return castleMove;

		//Determine move type and checks
		MoveInfo moveInfo=determineMoveTypeAndChecks(notation);
		moveTypes type=moveInfo.type;
		int numberOfChecks=moveInfo.numberOfChecks;

		notation=filterNotation(notation);

		//System.out.println("Filtered: "+notation);
		//System.err.println("filtered len: "+notation.length());

		//Determine start and end coordinates
		int[] coordinates=determineCoordinates(notation);
		int startDiffX=coordinates[0]; // startX
		int startDiffY=coordinates[1]; // startY
		Coords endPosition=new Coords(coordinates[2],coordinates[3]); // endX, endY

		//Determine piece type
		pieceType pieceCode=determinePieceType(notation,type);

		//Find or create the appropriate SlowMove
		ArrayList<SlowMove> possibleMoves=getStartCoordsForPgnMoveObj(b,team,endPosition,pieceCode);
		SlowMove selectedMove=selectMoveObj(possibleMoves,startDiffX,startDiffY,type,numberOfChecks);

		//System.out.println("Moveeee: "+Move.describe(selectedMove.encodeFASTMove()));

		//Return the selected move
		return selectedMove;
	}

	/**
	 * Calculates a move given from the given board state and a string in algebraic notation
	 * @param notation The string in algebraic notation
	 * @param team     The team that made the move
	 * @param b        The board
	 * @return A SlowMove object that represents the move, does not check for legality
	 */
	public static int moveFromAlgebraicNotation(String notation,boolean team,Board b){

		if (notation==""||notation==" ")return Move.blank();

		//Pattern boardletterPattern=Pattern.compile("[a-h]");

		//Handle castling moves and filter notation
		int castleMove=handleCastling(notation,team);
		if(castleMove!=Move.blankMove) return castleMove;

		//Determine move type and checks
		MoveInfo moveInfo=determineMoveTypeAndChecks(notation);
		int type=moveInfo.type2;
		int numberOfChecks=moveInfo.numberOfChecks;

		if (moveInfo.type==moveTypes.capture){

		}

		notation=filterNotation(notation);

		System.out.println("Filtered: "+notation);

		//Determine start and end coordinates
		int[] coordinates=determineCoordinates(notation);
		int startDiffX=coordinates[0]; // startX
		int startDiffY=coordinates[1]; // startY
		Coords endPosition=new Coords(coordinates[2],coordinates[3]); // endX, endY

		//Determine piece type
		pieceType pieceCode=determinePieceType(notation);

		//Find or create the appropriate move int
		ArrayList<Integer> possibleMoves=getStartCoordsForPgnMove(b,team,endPosition,pieceCode);
		int selectedMove=selectMove(possibleMoves,startDiffX,startDiffY,type,numberOfChecks);

		//Return the selected move
		return selectedMove;
	}

	/**
	 * Gets an algebraic notation move from an integer move
	 * @param m     The integer move
	 * @param b     The board
	 * @param team  The player team
	 * @param model The game model
	 * @return The algebraic notation move
	 */
	public static String algNotationFromMove(int m,Board b,boolean team,GameModel model){
		int pieceCode=Move.getPieceCode(m);
		pieceType type=PieceCode.decodePieceType(pieceCode);
		int moveType=Move.getSpecialCode(m);
		char pieceChar=PieceCode.decodeChar(pieceCode);

		Coords start=new Coords(Move.getStartIndex(m));
		Coords end=new Coords(Move.getEndIndex(m));

		if(moveType==Move.kSideCastle) return "O-O";
		if(moveType==Move.qSideCastle) return "O-O-O";

		String moveString="";
		Player p=model.getPlayerFromTeam(team);
		Player opponent=model.getPlayerFromTeam(!team);

		boolean capture=Move.isCapture(m);
		boolean pawnPromote=Move.isPawnPromotion(m);

		if(pieceChar!='P' && pieceChar!='p') moveString+=""+charUppercase(pieceChar);
		//Adding any required differentiating characters for board position
		if((pieceChar=='P' || pieceChar=='p') && capture){
			//Pawns always have an a-h differentiator when they capture
			moveString+=""+start.getXPGN();
			if(capture || moveType==Move.EnPassantCapture) moveString+="x";
		}else{
			if(capture || moveType==Move.EnPassantCapture) moveString+="x";

			//Everything else differentiates when other pieces of the same allied type are present
			ArrayList<Integer> playerMoves=p.possibleMoves(b);

			for(Integer playerMove: playerMoves){
				Coords playerStart=new Coords(Move.getStartIndex(playerMove));
				Coords playerEnd=new Coords(Move.getEndIndex(playerMove));
				int playerPieceCode=Move.getPieceCode(playerMove);

				//This basically checks if the move is invalid
				if(playerEnd.getSet()==UNSET || playerStart.getSet()==UNSET) continue;

				if(pieceCode==playerPieceCode && playerEnd.getIndex()==end.getIndex()){
					//Two pieces of the same type and team have the same destination. Add a differentiator based on start pos
					boolean columnEqual=(start.getY()==playerStart.getY());
					boolean rowEqual=(start.getX()==playerEnd.getX());

					if(!rowEqual && !columnEqual) moveString+=""+start.getXPGN()+start.getYPGN();
					else if(!columnEqual) moveString+=""+start.getYPGN();
					else if(!rowEqual) moveString+=""+start.getXPGN();

					break;
				}
			}
		}

		moveString+=""+end.getXPGN()+end.getYPGN();
		System.out.println(end.getXPGN()+", "+end.getYPGN());

		//Dalton can edit later. Yeah, I know it's inefficient, sorry
		ArrayList<PiecePosPair> oppKingPairs=b.alliedPiecesObj(!team,pieceType.king);
		if(oppKingPairs.size()>0){
			//Adding check marker if we inflict check
			PiecePosPair kingPos=oppKingPairs.get(0);
			King oppKing=(King) kingPos.piece();
			if(opponent.isCheckmated(b)){
				moveString+="#";
			}else if(oppKing.isInCheck(b,kingPos.coords().getIndex())){
				moveString+="+";
			}
		}

		if(pawnPromote){
			char piece=Character.toUpperCase(PieceCode.decodeChar(pieceCode));
			moveString+="="+piece;
		}

		//if(m.numberOfChecks()==1) moveString+="+";
		//else if(m.numberOfChecks()>=2) moveString+="++";

		System.out.println(moveString);
		return moveString;
	}

	/**
	 * For a move with an end position and a given board state, calculate the position that the piece started from.
	 * @param m The move
	 * @param b The board state before the move was made
	 * @return The start position. (8,8) is returned if no start position is found.
	 */
	public static Coords calculateStartPos(SlowMove m,Board b){
		//For every possible move in the board state
		return new Coords(m.start());
	}

	/**
	 * Does the string have a valid algebraic coordinate substring starting at the specified index?
	 * @param s         The algebraic notation string to check
	 * @param checkFrom The index of the string to check the file letter at
	 * @return Whether the coordinate substring is valid
	 */
	public static boolean isCoordinateSubstring(String s,int checkFrom){
		//Make sure we don't get index out of range errors first
		//We're checking two consecutive characters, so we have to check if the index of the file is at least two
		//characters away from the end of the string
		if(checkFrom>=s.length()-1) return FAIL;
		//Is the first character a valid letter and the second a valid number?
		char first=s.charAt(checkFrom);
		char second=s.charAt(checkFrom+1);

		boolean check=FAIL;
		for(char c: COORDINATE_LETTERS){
			if(first==c){
				check=SUCCESS;
				break;
			}
		}
		if(check==FAIL) return check;

		check=FAIL;
		for(char c: COORDINATE_NUMBERS){
			if(second==c){
				check=SUCCESS;
				break;
			}
		}
		return check;
	}

	/**
	 * Is this character a coordinate letter?
	 * @param c The character
	 * @return The boolean
	 */
	public static boolean isCoordLetter(Character c){
		for(char c2: COORDINATE_LETTERS){
			if(c2==c){
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns where a coordinate substring points to on the board in integer coordinates.
	 * @param s         The string in algebraic notation
	 * @param checkFrom The index of the string to check the file letter at
	 * @return The coordinates. Will be UNSET if string is invalid
	 */
	public static Coords coordinateSubstringPosition(String s,int checkFrom){
		//Is our substring valid, at least?
		if(!isCoordinateSubstring(s,checkFrom)) return new Coords();
		//Is the first character a valid letter and the second a valid number?
		char first=s.charAt(checkFrom);
		char second=s.charAt(checkFrom+1);

		int firstNum=-1, secondNum=-1;

		for(int i=0; i<COORDINATE_LETTERS.length; ++i){
			if(first==COORDINATE_LETTERS[i]){
				firstNum=i;
				break;
			}
		}
		for(int i=0; i<COORDINATE_NUMBERS.length; ++i){
			if(second==COORDINATE_NUMBERS[i]){
				secondNum=i;
				break;
			}
		}
		//Check if either variable wasn't initialized
		if(firstNum==-1 || secondNum==-1) return new Coords();
		return new Coords(firstNum,secondNum);

		// Coords coords=new Coords();
		// boolean b=coords.setFromPGN(s);
		// if(b) return coords;
	}

	/**
	 * Gets the list of moves from a PGN file.
	 * @param pgnFile    The file to load
	 * @param startBoard The starting board object
	 * @return A list of moves in the file
	 */
	public ArrayList<SlowMove> getMovesFromPGNObj(File pgnFile,Board startBoard) throws FileNotFoundException{
		FileInputStream is=new FileInputStream(pgnFile);
		Scanner scan=new Scanner(is);
		scan.useDelimiter("\n");

		ArrayList<SlowMove> moves=new ArrayList<>();

		//O is move number, 1 is white's move, 2 is black's move
		int moveTokenRead=0;
		boolean firstMoveFound=true, gameOver=false, team=WHITE;
		Board b=new Board(startBoard);
		//These track where comments are
		PgnFilterInfo filterInfo=new PgnFilterInfo();

		while(scan.hasNext() && !gameOver){
			String line=scan.next();

			//This space helps the filtering out a bit
			line+=" ";

			filterInfo.newLine=line;
			filterLineForComments(filterInfo);
			filterLineForNumberPeriods(filterInfo);
			filterLineForDollarSigns(filterInfo);
			line=filterInfo.newLine;

			if(line=="") continue;

			
			if(firstMoveFound){
				//Use a regex pattern to split by spaces and periods.
				//Idea from here: https://www.baeldung.com/java-string-split-multiple-delimiters
				String[] splitString=line.split("[. \n\r]");

				System.out.println("SlowMove Line: "+line);
				//System.out.println("String 0: "+splitString[0]);
				//System.out.println("String 1: "+splitString[1]);

				for(int stringIndex=0; stringIndex<splitString.length; stringIndex++){
					String s=splitString[stringIndex];
					System.out.println("================");
					System.out.println("Current string: "+s);
					System.out.println("String len: "+s.length());

					//Skip the empty string
					if(s=="") continue;
					if(s.length()==0) continue;

					//We're at a turn number, skip it
					//We should be filtering for these already
					// if(moveTokenRead==0){
					// 	++moveTokenRead;
					// 	continue;
					// }

					//We're at an algebraic notation move, so add it
					String moveString=s;
					if(Objects.equals(moveString,"1-0")){
						//The previous move won the game for white
						SlowMove previousMove=moves.get(moves.size()-1);
						previousMove=new SlowMove(previousMove,Winner.white);
						gameOver=true;
						break;
					}
					if(Objects.equals(moveString,"0-1")){
						//The previous move won the game for black
						SlowMove previousMove=moves.get(moves.size()-1);
						previousMove=new SlowMove(previousMove,Winner.black);
						gameOver=true;
						break;
					}

					System.out.println(b);

					//generate a new move from algebraic notation
					SlowMove m=moveFromAlgebraicNotationObj(moveString,team,b);//supply team by calculation rather than if statements
					moves.add(m);

					if(m==null) return moves;

					b.makeMove(m.encodeFASTMove());//The old line seemed to do literally nothing, Someone want to help me check it?
					//Increment the kind of token we're reading
					++moveTokenRead;
					team=!team;
					if(moveTokenRead>=3) moveTokenRead=0;
				}
			}
		}
		return moves;
	}

	/**
	 * Gets the list of moves from a PGN file.
	 * @param pgnFile    The file to load
	 * @param startBoard The starting board object
	 * @return A list of moves in the file
	 */
	public ArrayList<Integer> getMovesFromPGN(File pgnFile,Board startBoard) throws FileNotFoundException{
		FileInputStream is=new FileInputStream(pgnFile);
		Scanner scan=new Scanner(is);
		scan.useDelimiter("\n");

		ArrayList<Integer> moves=new ArrayList<>();

		//O is move number, 1 is white's move, 2 is black's move
		int moveTokenRead=0;
		boolean firstMoveFound=true, gameOver=false, team=WHITE;
		Board b=new Board(startBoard);
		//These track where comments are
		int curlyBrackets=0, parentheses=0;

		//These track where comments are
		PgnFilterInfo filterInfo=new PgnFilterInfo();

		while(scan.hasNext() && !gameOver){
			String line=scan.next();


			//This space helps the filtering out a bit
			line+=" ";

			filterInfo.newLine=line;
			filterLineForComments(filterInfo);
			filterLineForNumberPeriods(filterInfo);
			filterLineForDollarSigns(filterInfo);
			line=filterInfo.newLine;

			
			if(line=="") continue;

			if(firstMoveFound){
				//Use a regex pattern to split by spaces and periods.
				//Idea from here: https://www.baeldung.com/java-string-split-multiple-delimiters
				String[] splitString=line.split("[. \n\r]");

				System.out.println("SlowMove Line: "+line);
				//System.out.println("String 0: "+splitString[0]);
				//System.out.println("String 1: "+splitString[1]);

				for(int stringIndex=0; stringIndex<splitString.length; stringIndex++){
					String s=splitString[stringIndex];
					System.out.println("================");
					System.out.println("Current string: "+s);
					System.out.println("String len: "+s.length());

					//Skip the empty string
					if(s=="") continue;
					if(s.length()==0) continue;

					//We're at a turn number, skip it
					// if(moveTokenRead==0){
					// 	++moveTokenRead;
					// 	continue;
					// }
					//We're at an algebraic notation move, so add it
					String moveString=s;
					if(Objects.equals(moveString,"1-0")){
						//The previous move won the game for white
						int previousMove=moves.get(moves.size()-1);
						//previousMove=new SlowMove(previousMove,Winner.white);
						gameOver=true;
						break;
					}
					if(Objects.equals(moveString,"0-1")){
						//The previous move won the game for black
						int previousMove=moves.get(moves.size()-1);
						//previousMove=new SlowMove(previousMove,Winner.black);
						gameOver=true;
						break;
					}

					System.out.println(b);

					//generate a new move from algebraic notation
					int m=moveFromAlgebraicNotation(moveString,team,b);//supply team by calculation rather than if statements
					moves.add(m);

					if(m==Move.blank()||m==0) return moves;

					b.makeMove(m);//The old line seemed to do literally nothing, Someone want to help me check it?
					//Increment the kind of token we're reading
					++moveTokenRead;
					team=!team;
					if(moveTokenRead>=3) moveTokenRead=0;
				}
			}
		}
		return moves;
	}

	/**
	 * Creates a PGN file at the specified path using the listed moves
	 * @param moves The move list
	 * @param path  The file path
	 * @return The PGN file
	 */
	public File createPgnFile(LinkedList<Integer> moves,String path,GameModel model){
		Board board=new Board(Board.DEFAULT);//starting board
		boolean team=WHITE;
		int moveNum=0;
		String currentLine="";
		File newFile=new File(path);
		FileOutputStream os;
		PrintStream ps;

		try{
			os=new FileOutputStream(newFile);
			ps=new PrintStream(os);
		}catch(Exception e){
			return newFile;
		}

		for(int i=0; i<moves.size(); i++){
			//Make a new number every 2 moves
			if(team==WHITE){
				//Because moveNum starts at 0, the first number printed will be 1
				++moveNum;
				currentLine+=moveNum+". ";
			}

			currentLine+=algNotationFromMove(moves.get(i),board,team,model)+" ";
			team=!team;//change player

			//Make a new line every 8 moves
			if(moveNum%8==0 && moveNum!=1 && team==BLACK){
				ps.println(currentLine);
				currentLine="";
			}
		}
		ps.println(currentLine);
		try{
			os.close();
		}catch(Exception e){
			return newFile;
		}
		return newFile;
	}
}