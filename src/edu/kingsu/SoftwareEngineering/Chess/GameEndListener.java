package edu.kingsu.SoftwareEngineering.Chess;

/**
 * Something the UI classes need to compile, we may remove it in the future.
 * @author Group 4
 * @version 0.1
 */
public interface GameEndListener{
	void onCheckmate(Player winner);

	void onRepetition();
	void onStalemate();
}
