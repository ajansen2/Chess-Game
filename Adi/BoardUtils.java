package edu.kingsu.SoftwareEngineering.Chess.Adi;

import javax.management.RuntimeErrorException;

public class BoardUtils{

	/*null for now, need to give it a content */
	//array of boolean of size 64 and indexes that corresponds to respective columns will be turned on
	public static final boolean[] FIRST_COLUMN=initColumn(0);
	public static final boolean[] SECOND_COLUMN=initColumn(1);
	public static final boolean[] SEVENTH_COLUMN=initColumn(6);
	public static final boolean[] EIGHTH_COLUMN=initColumn(7);

	public static final int NUM_TILES=64;
	public static final int NUM_TILES_PER_ROW=8;

	private static boolean[] initColumn(int columnNumber){
		final boolean[] column=new boolean[NUM_TILES];
		do{
			column[columnNumber]=true;
			columnNumber+=NUM_TILES_PER_ROW;
		}while(columnNumber<NUM_TILES);
		return column;
	}
	private BoardUtils(){
		throw new RuntimeException("You cannot instantiate this class");
	}

	//checks if the coordinate is between 0 and 64 and returns true if the cordinate is between
	// the limits
	public static boolean isValidTileCoordinate(final int tileCoordinate){
		return tileCoordinate0 && tileCoordinate<NUM_TILES;
	}
}
