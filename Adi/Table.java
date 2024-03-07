package edu.kingsu.SoftwareEngineering.Chess.Adi;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class Table{
	private final JFrame gameFrame;
	private static Dimension OUTER_FRAME_DIMENSION=new Dimension(600,600);

	public Table(){
		this.gameFrame=new JFrame("Chess Game");
		final JMenuBar tableMenuBar=new JMenuBar();

		populateMenuBar(tableMenuBar);
		this.gameFrame.setJMenuBar(tableMenuBar);

		this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
		this.gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.gameFrame.setVisible(true);
	}

	private void populateMenuBar(final JMenuBar tableMenuBar){
		tableMenuBar.add(createFileMenu());
	}

	private JMenu createFileMenu(){
		final JMenu fileMenu=new JMenu("File");
		final JMenuItem openPGN=new JMenuItem("Load PGN file");
		openPGN.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				System.out.println("open up that pgn file");
			}
		});
		fileMenu.add(openPGN);
		return fileMenu;
	}
}
