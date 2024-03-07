package edu.kingsu.SoftwareEngineering.Chess;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.TimerTask;

/**
 * User interface to manage the game board
 * @author Hamza Mhamdi
 * @author Group 4
 * @version 1
 */
public class BoardUI extends JPanel implements MouseListener, MouseMotionListener, BoardListener{
	private static final int TILE_SIZE=80, colSize=8, rowSize=8;
	private final ArrayList<TilePanel> boardTiles;
	private JPanel chessBoard;

	// private static Color lightTileColor=Color.decode("#FFFACD");
	// private static Color darkTileColor=Color.decode("#593E1A");
	private static Color lightTileColor=Color.decode("#808080");
	private static Color darkTileColor=Color.decode("#404040");
	private static Color highlightedTileColor=Color.decode("#24C9C9");
	private static final Color darkHighlightedTileColor=Color.decode("#187078");
	// private static Color highlightedTileGreenColor=Color.decode("#00ff1e");
	private static Color highlightedTileGreenColor=Color.decode("#E6E6FA");
	private static Color highlightedCheckColor=Color.decode("#FF0000");
	private static final Color darkHighlightedTileGreenColor=Color.decode("#008c11");

	private static final Dimension OUTER_FRAME_DIMENSION=new Dimension(1100,1100);
	private static final Dimension BOARD_PANEL_DIMENSION=new Dimension(1200,1200);
	private static final Dimension TILE_PANEL_DIMENSION=new Dimension(TILE_SIZE,TILE_SIZE);

	private int /*X, Y, newX, newY,*/ lastParentWidth=0;
	private final GameModel chessGame;
	private final BoardController controller;
	private GameSetupUI gameSetupUI;
	private java.util.Timer uiTimer;
	private JFrame frame;

	private boolean canClick=false;
	private boolean rotated=false;

	/**
	 * Constructs the BoardUI object from a passed game model
	 * @param chessGame The model
	 */
	public BoardUI(GameModel chessGame,BoardController controller){
		setLayout(new GridLayout(rowSize,colSize));
		this.chessGame=chessGame;
		this.controller=controller;

		chessGame.addListener(this);
		boardTiles=new ArrayList<>();
		setupUI();
	}

	/** Runs all the initial setup methods when the UI is set up */
	private void setupUI(){
		// this.setBackground(Color.BLACK);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		setVisible(true);
		setPreferredSize(BOARD_PANEL_DIMENSION);

		drawTiles();

		// uiUpdate(chessGame.getCurrentBoard());
		// setPeriodicUpdate(300);
	}

	/**
	 * Sets the GameSetupUI that this uses to get themes
	 * @param setupUI The setup UI
	 */
	public void setGameSetupUI(GameSetupUI setupUI){
		this.gameSetupUI=setupUI;
	}

	/**
	 * Sets whether the board sends click events to the board controller
	 * @param b The boolean value to set
	 */
	public void setCanClick(boolean b){
		canClick=b;
	}

	/**
	 * Does the board send click events to the controller right now?
	 * @return Whether the board is interactive
	 */
	public boolean getCanClick(){
		return canClick;
	}

	public void setRotated(boolean b){
		if(rotated!=b){
			drawTiles();
		}
		rotated=b;
	}

	public boolean getRotated(){
		return rotated;
	}

	public void rotateBoard(){
		setRotated(!getRotated());
	}

	/**
	 * Updates the highlight in-check color of the tiles as part of a theme change
	 * @param highlighted The color to set
	 */
	public void changeHighlightedCheckColor(Color highlighted){
		highlightedCheckColor=highlighted;
	}

	/**
	 * Updates the highlighted tile color as part of a theme change
	 * @param highlighted      The color of a tile where a piece could move to
	 * @param pieceHighlighted The color of the tile with the piece the user wants to move
	 */
	public void changeHighlightedTileColors(Color highlighted,Color pieceHighlighted){
		highlightedTileColor=highlighted;
		highlightedTileGreenColor=pieceHighlighted;

		/*for(TilePanel panel: boardTiles){
			panel.highlightTile();
		}*/
	}

	/**
	 * Updates the non-highlighted tile colors as part of a theme change
	 * @param light The light color
	 * @param dark  The dark color
	 */
	public void changeTileColors(Color light,Color dark){
		lightTileColor=light;
		darkTileColor=dark;

		for(TilePanel panel: boardTiles){
			panel.assignTileColor();
		}
	}

	/**
	 * Paint this component
	 * @param g the <code>Graphics</code> object to protect
	 */
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);

		uiUpdateIfResize(chessGame.getCurrentBoard());
		// uiUpdate(chessGame.getCurrentBoard());
	}

	/** Draws the tiles on the screen in a board pattern */
	private void drawTiles(){
		//Create labels for rows (numbers) and columns (letters)
		String[] rowLabels={"8","7","6","5","4","3","2","1"};
		String[] colLabels={"A","B","C","D","E","F","G","H"};
		boolean redraw=boardTiles.size()>0;

		for(TilePanel tilePanel: boardTiles){
			chessBoard.remove(tilePanel);
		}
		boardTiles.clear();
		setLayout(new BorderLayout());
		chessBoard=new JPanel(new GridBagLayout());
		GridBagConstraints constraints=new GridBagConstraints();
		// Top side
		if(!redraw)
			for(int col=0; col<colLabels.length; ++col){
				constraints.gridx=col+1; // Start from 1 to leave space for row labels
				constraints.gridy=0;
				constraints.weightx=1;
				constraints.weighty=0.5;
				constraints.fill=GridBagConstraints.BOTH;

				JLabel colLabel=createLabel(colLabels[col]);
				chessBoard.add(colLabel,constraints);
			}
		// Left side
		for(int row=0; row<rowLabels.length; ++row){
			constraints.gridx=0;
			constraints.gridy=row+1;
			constraints.weightx=0.5;
			constraints.weighty=1;
			constraints.fill=GridBagConstraints.BOTH;

			if(!redraw){
				JLabel rowLabel=createLabel(rowLabels[row]);

				chessBoard.add(rowLabel,constraints);
			}

			// Add the panels
			for(int col=0; col<colLabels.length; ++col){
				constraints.gridx=col+1;
				constraints.gridy=row+1;
				constraints.weightx=1;
				constraints.weighty=1;
				constraints.fill=GridBagConstraints.BOTH;

				int coordIndex=Coords.indexFromTopLeftXY(col,row);
				if(getRotated()){
					coordIndex=Coords.XYToIndex(col,row);
				}

				TilePanel tilePanel=new TilePanel(this,coordIndex,chessGame);
				this.boardTiles.add(tilePanel);

				// Set color
				switch((row+col)%2){
					case 1:
						tilePanel.setBackground(lightTileColor);
						break;
					case 0:
					default:
						tilePanel.setBackground(darkTileColor);
				}
				chessBoard.add(tilePanel,constraints);
			}
		}

		// Right side
		if(!redraw){
			for(int col=0; col<colLabels.length; ++col){
				constraints.gridx=col+1;
				constraints.gridy=rowLabels.length+1;
				constraints.weightx=1;
				constraints.weighty=0.5;
				constraints.fill=GridBagConstraints.BOTH;

				JLabel colLabel=createLabel(colLabels[col]);
				chessBoard.add(colLabel,constraints);
			}
			// Bottom side
			for(int row=0; row<rowLabels.length; ++row){
				constraints.gridx=colLabels.length+1;
				constraints.gridy=row+1;
				constraints.weightx=0.5;
				constraints.weighty=1;
				constraints.fill=GridBagConstraints.BOTH;

				JLabel rowLabel=createLabel(rowLabels[row]);
				chessBoard.add(rowLabel,constraints);
			}
		}

		add(chessBoard,BorderLayout.CENTER);
	}

	// helperMethod to create a JLabel with the specified text and font
	private JLabel createLabel(String text){
		JLabel label=new JLabel(text,SwingConstants.CENTER);
		Font labelFont=new Font("Dialog",Font.PLAIN,18);
		label.setFont(labelFont);
		return label;
	}

	/**
	 * Empty mouse click handler
	 * @param e the event to be processed
	 */
	@Override
	public void mouseClicked(MouseEvent e){
	}

	/**
	 * This is called when the board is changed in GameLogic
	 * @param newBoard The new board state
	 */
	@Override
	public void onBoardChanged(Board newBoard){
		// System.out.println("Board change!");
		uiUpdate(newBoard);
	}

	/**
	 * Updates the game UI state from a given board state
	 * @param newBoard The given board state
	 */
	public void uiUpdate(Board newBoard){
		repaint();
		// System.out.println("UI update!");
		for(int i=0; i<boardTiles.size(); ++i){
			Coords coords=new Coords(Coords.indexFromTopLeftIndex(i));// translate the messed up origins
			Piece p=newBoard.getSquareObj(coords);

			// boardTiles.get(i).clearPieceSprite();
			boardTiles.get(i).setPieceSprite(p);
		}
		if(frame!=null)
			frame.pack();
		repaint();
	}

	/**
	 * Update the UI from a given board state if the window has been resized
	 * @param b The given board state
	 */
	public void uiUpdateIfResize(Board b){
		Dimension frameSize=getParent().getSize();
		Dimension screenSize;

		try{
			screenSize=Toolkit.getDefaultToolkit().getScreenSize();
		}catch(Exception e){
			uiUpdate(b);
			return;
		}

		// Only resize things if the parent is actually a different size
		if(lastParentWidth==frameSize.height)
			return;
		lastParentWidth=frameSize.height;

		// int height=screenSize.height*2/3;
		// int width=screenSize.width*2/3;

		int height=frameSize.height;
		int width=frameSize.width;

		if(height>screenSize.height)
			height=screenSize.height;
		if(width>screenSize.width)
			width=screenSize.width;

		if(width>height)
			width=height;// make sure there is always enough vertical room
		setPreferredSize(new Dimension(width,height));

		// Once the board is resized, the icons need redrawing
		uiUpdate(b);
		frame.revalidate();
		frame.repaint();
		// frame.pack();
	}

	/**
	 * Sets a periodic update
	 * @param ms Set to update every time this amount of milliseconds has passed
	 */
	public void setPeriodicUpdate(int ms){
		uiTimer=new java.util.Timer();
		uiTimer.schedule(
				new TimerTask(){
					public void run(){
						System.out.println("Timer");
						uiUpdate(chessGame.getCurrentBoard());
					}
				},1,ms);
	}

	/**
	 * Mouse press handler function
	 * records where the mouse was pressed and calls <code>repaint()<code/> to
	 * process it
	 * @param e the event to be processed
	 */
	@Override
	public void mousePressed(MouseEvent e){
		/*
		 * if(isWithinBoardBounds(e.getX(),e.getY())){
		 * X=e.getX();
		 * Y=e.getY();
		 * repaint();
		 * }
		 */
	}

	/**
	 * Mouse release handler
	 * @param e the event to be processed
	 */
	@Override
	public void mouseReleased(MouseEvent e){
		/*
		 * if(isWithinBoardBounds(e.getX(), e.getY())){
		 * newX=e.getX();
		 * newY=e.getY();
		 * handleDragPiece(e);
		 * repaint();
		 * }
		 */
	}

	/**
	 * Placeholder method for handling drag events
	 * @param e the event object
	 */
	private void handleDragPiece(MouseEvent e){
		// todo
	}

	/**
	 * Empty mouse enter handler
	 * @param e the event to be processed
	 */
	@Override
	public void mouseEntered(MouseEvent e){
		System.out.println("UI updated!");
		uiUpdate(chessGame.getCurrentBoard());
	}

	/**
	 * Sets the current frame
	 * @param frame The frame to set on this object
	 */
	public void setFrame(JFrame frame){
		this.frame=frame;
	}

	/**
	 * Empty mouse exit handler
	 * @param e the event to be processed
	 */
	@Override
	public void mouseExited(MouseEvent e){
	}

	/**
	 * Empty mouse drag handler
	 * @param e the event to be processed
	 */
	@Override
	public void mouseDragged(MouseEvent e){
	}

	/**
	 * Empty mouse move handler
	 * @param e the event to be processed
	 */
	@Override
	public void mouseMoved(MouseEvent e){
	}

	/**
	 * This is called when the tile is clicked. It passes events to the controller
	 * @param coords The coordinates of the clicked tile
	 */
	public void onTileClicked(Coords coords){
		if(canClick) controller.onTileClicked(coords);
	}

	/**
	 * Highlights a tile on the board
	 * @param coords The tile to highlight
	 */
	public void highlightTile(Coords coords){
		boardTiles.get(coords.getIndexTopLeft()).drawCircle(highlightedTileColor);// translate to top left origin
	}

	/**
	 * Highlights a tile on the board green
	 * @param coords The tile to highlight
	 */
	public void highlightTileGreen(Coords coords){
		boardTiles.get(coords.getIndexTopLeft()).drawCircle(highlightedTileGreenColor);// translate to top left origin
	}

	/** Highlights the king if in check to red */
	public void highlightCheck(Coords c){
		boardTiles.get(c.getIndexTopLeft()).drawCircle(highlightedCheckColor);
	}

	/**
	 * Highlights a tile with a star
	 * @param coords The tile to star
	 */
	public void starTile(Coords coords){
		boardTiles.get(coords.getIndexTopLeft()).setStarSprite();// translate to top left origin
	}

	/**
	 * Unstars a tile.
	 * @param coords The tile to revert
	 */
	public void unstarTile(Coords coords){
		boardTiles.get(coords.getIndexTopLeft()).clearStarSprite();// translate to top left origin
	}

	/**
	 * Unhighlights a tile.
	 * @param coords The tile to revert
	 */
	public void unHighlightTile(Coords coords){
		boardTiles.get(coords.getIndexTopLeft()).unHighlightTile();// translate to top left origin
	}

	/** Removes highlight from all tiles */
	public void unHiglightAll(){
		for(int i=0; i<boardTiles.size(); ++i){
			boardTiles.get(i).unHighlightTile();
		}
	}

	/** Removes stars from all tiles */
	public void unstarAll(){
		for(int i=0; i<boardTiles.size(); ++i){
			boardTiles.get(i).clearStarSprite();
		}
	}

	/**
	 * Checks if the X and Y are inside the board
	 * @param x X Coordinate
	 * @param y Y Coordinate
	 * @return True if inside, False if outside
	 */
	private boolean isWithinBoardBounds(int x,int y){
		return (x<8*TILE_SIZE) && (y<8*TILE_SIZE);// Oh, I Screwed up and thought this was the same as the
		// Board... OOOPS!!!!
	}

	/**
	 * Inner class that represents a single board tile
	 * @author Hamza Mhamdi
	 */
	private class TilePanel extends JPanel implements MouseListener{
		private final int tileId, rowOffset;
		private final Coords coords;
		private final BoardUI panel;
		private final GameModel model;

		// private JLayeredPane tileLayers
		private JPanel piecePanel;
		private JPanel starPanel;

		private JLabel starSprite;
		private JLabel pieceSprite;

		private boolean starFlashing;
		private boolean highlighted;

		private Color circleColor;

		/**
		 * Constructs this tile
		 * @param BoardUI The parent board UI
		 * @param tileId  This tile's ID and place
		 */
		TilePanel(final BoardUI BoardUI,final int tileId,final GameModel model){
			super(new BorderLayout());

			this.tileId=tileId;
			this.rowOffset=Coords.indexToY(tileId);
			this.coords=new Coords(tileId);
			this.panel=BoardUI;
			this.model=model;
			setPreferredSize(TILE_PANEL_DIMENSION);
			this.addMouseListener(this);
			// assignTileColor();

			// This is some of the code for putting the star on tiles. It doesn't seem to
			// work
			// tileLayers=new JLayeredPane();
			// tileLayers.setPreferredSize(getSize());
			// tileLayers.setSize(getSize());
			// add(tileLayers, BorderLayout.CENTER);

			// piecePanel=new JPanel(new BorderLayout());
			// piecePanel.setPreferredSize(TILE_PANEL_DIMENSION);
			// piecePanel.setOpaque(true);
			// //tileLayers.add(piecePanel,1);
			// add(piecePanel);

			// starPanel=new JPanel(new BorderLayout());
			// starPanel.setPreferredSize(TILE_PANEL_DIMENSION);
			// starPanel.setOpaque(false);
			// //tileLayers.add(starPanel,2);
			// add(starPanel);

			// adjustPanelSizes();
			validate();
		}

		/** Assigns the black and white tiles in a checkerboard pattern, but for this tile */
		public void assignTileColor(){
			if(highlighted) return;//do not assign normal colors if highlighted
			int tileColorIndex=(rowOffset+tileId)%2;

			switch(tileColorIndex){// using number trickery to generate a checkerboard pattern
				case 0:// dark squares
					setBackground(darkTileColor);
					break;
				case 1:// light squares
				default:// fall through to trick intellij into thinking this is not a 2 outcome switch
					setBackground(lightTileColor);
					break;
			}
		}

		/**
		 * Empty mouse enter handler
		 * @param e the event to be processed
		 */
		@Override
		public void mouseEntered(MouseEvent e){
			// System.out.println("Entered "+tileId);
		}

		/**
		 * Empty mouse exit handler
		 * @param e the event to be processed
		 */
		@Override
		public void mouseExited(MouseEvent e){
		}

		/**
		 * Empty mouse click handler
		 * @param e the event to be processed
		 */
		@Override
		public void mouseClicked(MouseEvent e){
			// System.out.println("Clicked "+tileId);
		}

		/**
		 * Process mouse press on this tile, tells the parent panel this tile was
		 * clicked
		 * @param e the event to be processed
		 */
		@Override
		public void mousePressed(MouseEvent e){
			// System.out.println("Pressed "+tileId);
			panel.onTileClicked(coords);
		}

		/**
		 * Process mouse release on this tile
		 * @param e the event to be processed
		 */
		@Override
		public void mouseReleased(MouseEvent e){
		}

		/**
		 * Sets the sprite for the piece on this tile
		 * @param p The piece
		 */
		public void setPieceSprite(Piece p){
			// Remove existing sprite if any
			clearPieceSprite();
			if(p!=null){// if given a piece, set it here
				// System.out.println("Tile size "+getSize().width);
				JLabel label=new JLabel(p.getImageIcon((int) (getSize().width*0.7f),model,gameSetupUI.getCurrentThemeFolder()));
				label.setPreferredSize(getSize());
				pieceSprite=label;

				add(label,BorderLayout.CENTER);
				// tileLayers.add(label, 100);
				// piecePanel.add(label,BorderLayout.CENTER);
				validate();
				repaint();
			}
			// repaint();
		}

		/** Sets the sprite for the piece on this tile */
		public void setStarSprite(){
			// Remove existing sprite if any
			// clearPieceSprite();
			if(starSprite==null){// if given a piece, set it here
				// System.out.println("Tile size "+getSize().width);
				// JLabel label=new JLabel(Piece.getStarImageIcon((int)
				// (getSize().width*0.6f),model));
				// label.setPreferredSize(getSize());
				// starSprite=label;
				starFlashing=true;
				new Thread(()->{
					while(starFlashing){
						highlightTile(highlightedTileColor);
						try{
							Thread.sleep(200);
						}catch(Exception e){
						}

						highlightTile(highlightedTileGreenColor);
						try{
							Thread.sleep(500);
						}catch(Exception e){
						}
					}
				}).start();

				// add(label,BorderLayout.CENTER);
				// tileLayers.add(label, 101);
				// starPanel.add(label,BorderLayout.CENTER);
				validate();
				repaint();
			}
			// repaint();
		}

		// /** Highlights the tile */
		// public void highlightTile() {
		// setBackground(highlightedTileColor);
		// highlighted=true;
		// switch ((rowOffset+tileId)%2){// using number trickery to generate a
		// checkerboard pattern
		// case 0:// white squares
		// setBackground(highlightedTileColor);
		// break;
		// case 1:// black squares
		// default:// fall through to trick intellij into thinking this is not a 2
		// outcome switch
		// setBackground(darkHighlightedTileColor);
		// break;
		// }
		// }

		// /** Highlights the tile green */
		// public void highlightTileGreen() {
		// setBackground(highlightedTileGreenColor);
		// highlighted=true;
		// switch ((rowOffset+tileId)%2){// using number trickery to generate a
		// checkerboard pattern
		// case 0:// white squares
		// setBackground(highlightedTileGreenColor);
		// break;
		// case 1:// black squares
		// default:// fall through to trick intellij into thinking this is not a 2
		// outcome switch
		// setBackground(darkHighlightedTileGreenColor);
		// break;
		// }
		// }

		// public void highlightCheck() {
		// setBackground(Color.RED);
		// highlighted=true;
		// }

		/** Highlights the tile with an inner circle */
		private void drawCircle(Color color){
			highlighted=true;
			circleColor=color;
			repaint(); // Trigger repaint to draw the highlighted circle
		}

		/** Highlights the tile */
		private void highlightTile(Color color){
			highlighted=true;
			setBackground(color);
		}

		/** Un-Highlights the tile */
		private void unHighlightTile(){
			highlighted=false;
			assignTileColor();
			repaint();
		}

		/** Clears any existing piece sprite */
		private void clearPieceSprite(){
			if(pieceSprite!=null){
				// piecePanel.remove(pieceSprite);
				remove(pieceSprite);
			}
		}

		/** Clears any existing star sprite */
		private void clearStarSprite(){
			if(starSprite!=null){
				// starPanel.remove(starSprite);
			}
			if(starFlashing){
				starFlashing=false;
				assignTileColor();
			}
		}

		private void adjustPanelSizes(){
			System.out.println(getBounds());
			piecePanel.setBounds(getBounds());
			starPanel.setBounds(getBounds());
		}

		// @Override
		// public void paintComponent(Graphics g) {
		// super.paintComponent(g);
		// // adjustPanelSizes();
		// }
		@Override
		protected void paintComponent(Graphics g){
			super.paintComponent(g);

			if(highlighted){
				// // draw a circle in the middle
				// g.setColor(circleColor);
				// // int diameter=Math.min(getWidth(), getHeight()) * 2 / 3;
				// int diameter=70;
				// int x=(getWidth()-diameter)/2;
				// int y=(getHeight()-diameter)/2;
				// g.fillOval(x,y,diameter,diameter);
				// Save the current graphics context
				Graphics2D g2d=(Graphics2D) g.create();

				// Set the transparency level (0.0f for fully transparent, 1.0f for fully
				// opaque)
				float alpha=0.8f; // Adjust as needed
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha));

				// Draw a circle in the middle
				g2d.setColor(circleColor);
				int diameter=65;
				int x=(getWidth()-diameter)/2;
				int y=(getHeight()-diameter)/2;
				g2d.fillOval(x,y,diameter,diameter);

				// Dispose of the graphics context to release resources
				g2d.dispose();
			}
		}
	}

	/**
	 * Gets the screen location of a tile based on its coordinates.
	 * @param coords The coordinates of the tile.
	 * @return The screen location of the tile.
	 */
	public Point getTileLocationOnScreen(Coords coords){
		int tileIndex=coords.getIndexTopLeft();
		TilePanel tilePanel=boardTiles.get(tileIndex);

		Point panelLocation=tilePanel.getLocationOnScreen();
		int xOffset=tilePanel.getSize().width/2;
		int yOffset=tilePanel.getSize().height/2;

		return new Point(panelLocation.x+xOffset,panelLocation.y+yOffset);
	}
}