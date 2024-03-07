package edu.kingsu.SoftwareEngineering.Chess;

import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

/**
 * Our custom background, hold images and more
 * @author Adam Jansen
 * @version 1
 */
class BackgroundPanel extends JPanel{
	private Image originalBackgroundImage;
	private Image scaledBackgroundImage;

	public BackgroundPanel(Image backgroundImage){
		this.originalBackgroundImage=backgroundImage;
		this.scaledBackgroundImage=null; // Initialize as null
	}

	private Image scaleImage(Image img,int width,int height){
		BufferedImage scaledImage=new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d=scaledImage.createGraphics();

		// Apply quality rendering hints
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

		g2d.drawImage(img,0,0,width,height,null);
		g2d.dispose();

		return scaledImage;
	}

	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);

		// Only rescale if necessary
		if(scaledBackgroundImage==null || scaledBackgroundImage.getWidth(null)!=getWidth() || scaledBackgroundImage.getHeight(null)!=getHeight()){
			scaledBackgroundImage=scaleImage(originalBackgroundImage,getWidth(),getHeight());
		}

		g.drawImage(scaledBackgroundImage,0,0,this);
	}
}