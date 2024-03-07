package edu.kingsu.SoftwareEngineering.Chess;

import static edu.kingsu.SoftwareEngineering.Chess.Types.*;

/**
 * Class to describe an immutable pair of a Piece and Coords Object position
 * @param piece  A piece object to pair
 * @param coords The coordinate to pair to the piece
 * @author Dalton Herrewynen
 * @version 1
 */
public record PiecePosPair(Piece piece,Coords coords){
	/** Constructor checks for null pointers */
	public PiecePosPair{
		if(piece==null) throw new IllegalArgumentException("The Piece object must not be null.");
		if(coords==null) throw new IllegalArgumentException("The Coords object must not be null.");
		if(coords.isSet()==UNSET) throw new IllegalArgumentException("The Coords object must be set.");
		coords=coords.copy();
	}

	/**
	 * Chained constructor to make a PiecePosPair by converting coordinates into a Coords object
	 * @param p The Piece object
	 * @param x The X coordinate
	 * @param y The Y coordinate
	 */
	public PiecePosPair(Piece p,int x,int y){
		this(p,new Coords(x,y));
	}

	/**
	 * Chained constructor to make a PiecePosPair by an index into a Coords object
	 * @param p The Piece object
	 * @param i The index
	 */
	public PiecePosPair(Piece p,int i){
		this(p,new Coords(i));
	}
}