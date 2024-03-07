package edu.kingsu.SoftwareEngineering.Chess.Adi;

import java.util.HashMap;
import java.util.Map;

public abstract class Tile{

	//coordinate ranges between 0-63(in total 64 tiles)
	protected final int tileCordinate;
	private static final Map<Integer,EmptyTile> EMPTY_TILES_CACHE=createAllPossibleEmptyTiles();

	// Creating all the possible empty tiles upfront so that we dont have to create it again and again
	// basically it references an integer value with a tile
	private static Map<Integer,EmptyTile> createAllPossibleEmptyTiles(){
		final Map<Integer,EmptyTile> emptyTileMap=new HashMap<>();

		for(int i=0; i<BoardUtils.NUM_TILES; ++i){
			emptyTileMap.put(i,new EmptyTile(i));
		}

		return emptyTileMap;
	}

	//Only method to create a tile
	//if piece is not null (i.e., there is a piece on the tile), it creates a new OccupiedTile object using the tileCoordinate and piece parameters and returns it.
	//If piece is null, it uses a collection named EMPTY_TILES_CACHE to get a Tile object associated with the tileCoordinate and returns it.
	public static Tile createTile(final int tileCoordinate,final Piece piece){
		return piece!=null ? new OccupiedTile(tileCoordinate,piece) : EMPTY_TILES_CACHE.get(tileCoordinate);
	}

	//Constructor that takes int coordinate value and sets the coordinate for that tile.
	//It is private so that anyone who wants to create a tile can use the method above it
	private Tile(final int tileCoordinate){
		this.tileCordinate=tileCoordinate;
	}

	//the two properties that the tile can have
	public abstract boolean isTileOccupied();

	public abstract Piece getPiece();

	//first type of possible tile
	public static final class EmptyTile extends Tile{
		EmptyTile(final int cordinate){
			super(cordinate);
		}

		@Override
		public boolean isTileOccupied(){
			return false;
		}

		@Override
		public Piece getPiece(){
			return null;
		}
	}

	//second type of possible type
	public static final class OccupiedTile extends Tile{
		private final Piece pieceOnTile;

		OccupiedTile(int tileCoordinate,final Piece pieceOnTile){
			super(tileCoordinate);
			this.pieceOnTile=pieceOnTile;
		}

		@Override
		public boolean isTileOccupied(){
			return true;
		}

		@Override
		public Piece getPiece(){
			return pieceOnTile;
		}
	}
}
