package edu.kingsu.SoftwareEngineering.Chess;

import com.formdev.flatlaf.*;

import static edu.kingsu.SoftwareEngineering.Chess.Types.*;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 * Functions as a "BootLoader" or sorts which will trigger the game to load.
 * @author Group 4
 * @version 1
 */
public class Main{
	public static int AIPowerBlack=0,AIPowerWhite=0;

	/**
	 * Starts the game loading process.
	 * @param args if args has anything, assume debug mode and don't launch UI
	 */
	public static void main(String[] args){
		// enable custom window decorations
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		UIManager.put("flatlaf.frameDecorations",true);
		UIManager.put("flatlaf.menuBarEmbedded",true);
		FlatDarkLaf.setup();
		// FlatLightLaf.setup();
		// for Dalton haha
		// FlatIntelliJLaf.setup();
		for(int i=0; i<args.length; ++i){//parse command line args
			if(args[i].equalsIgnoreCase("white")) AIPowerWhite=Integer.parseInt(args[i+1]);//override the AI power so I can use my improvements
			if(args[i].equalsIgnoreCase("black")) AIPowerBlack=Integer.parseInt(args[i+1]);//even if my group fails to make the GUI set AI power
		}
		if(AIPowerBlack!=0) System.out.println("Black AI max depth override: "+AIPowerBlack);
		if(AIPowerWhite!=0) System.out.println("White AI max depth override: "+AIPowerWhite);
		if(args.length==0 || !args[0].equals("DEBUG")){// run the game here please

			GameModel model=new GameModel();
			AlgNotationHelper algNotationHelper=new AlgNotationHelper();

			BoardController boardController=new BoardController(model);
			model.addListener(boardController);

			PgnController pgnController=new PgnController(model,algNotationHelper);

			BoardUI boardUi=new BoardUI(model,boardController);
			boardController.setBoardUI(boardUi);
			model.setBoardUI(boardUi);

			GameSetupUI ui=new GameSetupUI(model,boardUi,algNotationHelper);
			ui.setBoardController(boardController);
			ui.setPgnController(pgnController);
			boardUi.setGameSetupUI(ui);

			boardController.setSetupUI(ui);

			model.setMenuUI(ui);
		}else{// debug area
			System.out.println("DEBUG MODE");
			debug(args);
		}
	}

	/**
	 * Use this function for debugging code and as a playground
	 * @param args a copy of the commandline args in case it's needed for something
	 */
	public static void debug(String[] args){
	}
}
