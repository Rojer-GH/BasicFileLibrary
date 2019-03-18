/**
 * 
 */
package de.rojer.images;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import de.rojer.images.exceptions.ImpossibleDimensionsException;

/**
 * This class can be used to load in images and textures. Saves the width,
 * height and pixel-data. Additionally this information is stored in a
 * bufferedimage-object. The path to the file and the name of the file are saved
 * too.
 * @author Rojer
 * @version 17.03.2019
 */
public class Image{

	protected int width, height;
	protected int[] pixels;

	protected BufferedImage image;

	protected String path, fileName;

	/**
	 * Create an image-object
	 * @param image the bufferedimage-object to be used
	 */
	public Image(BufferedImage image){
		this.image	= image;
		this.width	= this.image.getWidth();
		this.height	= this.image.getHeight();
		this.pixels	= this.image.getRGB(0, 0, this.width, this.height, null, 0, this.width);
	}

	/**
	 * Create an scaled image-object
	 * @param image the bufferedimage-object to be used
	 * @param width the new width ( <= 0 for normal width)
	 * @param height the new height ( <= 0 for normal height)
	 */
	public Image(BufferedImage image, int width, int height){
		int newWidth, newHeight;
		newWidth	= (width <= 0) ? image.getWidth() : width;
		newHeight	= (height <= 0) ? image.getHeight() : height;
		this.image	= toBufferedImage(image.getScaledInstance(newWidth, newHeight, 0));
		this.width	= this.image.getWidth();
		this.height	= this.image.getHeight();
		this.pixels	= this.image.getRGB(0, 0, newWidth, newHeight, null, 0, newWidth);
	}

	/**
	 * Create an scaled image-object
	 * @param image the bufferedimage-object to be used
	 * @param scaleX the factor to scale the width with ( > 0)
	 * @param scaleY the factor to scale the height with ( > 0)
	 */
	public Image(BufferedImage image, float scaleX, float scaleY){
		try{
			checkForGoodScaling(scaleX, image.getWidth(), scaleY, image.getHeight());
		}catch(ImpossibleDimensionsException e){
			e.printStackTrace();
		}
		int newWidth, newHeight;
		newWidth	= (int)(image.getWidth() * scaleX);
		newHeight	= (int)(image.getHeight() * scaleY);
		this.image	= toBufferedImage(image.getScaledInstance(newWidth, newHeight, 0));
		this.width	= this.image.getWidth();
		this.height	= this.image.getHeight();
		this.pixels	= this.image.getRGB(0, 0, newWidth, newHeight, null, 0, newWidth);
	}

	/**
	 * Create an image-object
	 * @param path the path to the image
	 * @param fileName the name of the image (with extension!)
	 */
	public Image(String path, String fileName){
		try{
			this.image	= ImageIO.read(new File(path + "/" + fileName));
			this.width	= this.image.getWidth();
			this.height	= this.image.getHeight();
			this.pixels	= this.image.getRGB(0, 0, this.width, this.height, null, 0, this.width);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * Create an scaled image-object
	 * @param path the path to the image
	 * @param fileName the name of the image (with extension!)
	 * @param width the new width ( <= 0 for normal width)
	 * @param height the new height ( <= 0 for normal height)
	 */
	public Image(String path, String fileName, int width, int height){
		try{
			int newWidth, newHeight;
			newWidth	= (width <= 0) ? image.getWidth() : width;
			newHeight	= (height <= 0) ? image.getHeight() : height;
			this.image	= toBufferedImage(ImageIO.read(new File(path + "/" + fileName)).getScaledInstance(newWidth, newHeight, 0));
			this.width	= this.image.getWidth();
			this.height	= this.image.getHeight();
			this.pixels	= this.image.getRGB(0, 0, newWidth, newHeight, null, 0, newWidth);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * Create an scaled image-object
	 * @param path the path to the image
	 * @param fileName the name of the image (with extension!)
	 * @param scaleX the factor to scale the width with ( > 0)
	 * @param scaleY the factor to scale the height with ( > 0)
	 */
	public Image(String path, String fileName, float scaleX, float scaleY){
		try{
			BufferedImage image = (BufferedImage)ImageIO.read(new File(path + "/" + fileName));
			checkForGoodScaling(scaleX, image.getWidth(), scaleY, image.getHeight());
			int newWidth, newHeight;
			newWidth	= (int)(image.getWidth() * scaleX);
			newHeight	= (int)(image.getHeight() * scaleY);
			this.image	= toBufferedImage(image.getScaledInstance(newWidth, newHeight, 0));
			this.width	= this.image.getWidth();
			this.height	= this.image.getHeight();
			this.pixels	= this.image.getRGB(0, 0, newWidth, newHeight, null, 0, newWidth);
		}catch(IOException e){
			e.printStackTrace();
		}catch(ImpossibleDimensionsException e){
			e.printStackTrace();
		}
	}

	// Methods

	/**
	 * Get a part of this image as an image (helpful for spritesheets!)
	 * @param posX the starting x-position of the subimage
	 * @param posY the starting y-position of the subimage
	 * @param width the width of the subimage
	 * @param height the height of the subimage
	 * @return the subimage
	 */
	public Image getSubImage(int posX, int posY, int width, int height){
		try{
			checkForGoodDimensions(posX, posY, width, height);
		}catch(ImpossibleDimensionsException e){
			e.printStackTrace();
		}
		Image newImage;
		newImage = new Image(this.image.getSubimage(posX, posY, width, height));
		return newImage;
	}

	// Helper-Methods

	private void checkForGoodDimensions(int posX, int posY, int width, int height) throws ImpossibleDimensionsException{
		if (posX < 0 || posX > this.image.getWidth() - 1){
			throw new ImpossibleDimensionsException("" + posX, "0 - " + (this.image.getWidth() - 1));
		}else if (posY < 0 || posY > this.image.getHeight() - 1){
			throw new ImpossibleDimensionsException("" + posY, "0 - " + (this.image.getHeight() - 1));
		}else if (width <= 0 || width + posX > this.image.getWidth()){
			throw new ImpossibleDimensionsException("" + width, "1 - " + (this.image.getWidth() - posX));
		}else if (height <= 0 || height + posY > this.image.getHeight()){ throw new ImpossibleDimensionsException("" + height, "1 - " + (this.image.getHeight() - posY)); }
	}

	private void checkForGoodScaling(float scaleX, int width, float scaleY, int height) throws ImpossibleDimensionsException{
		if (scaleX * width <= 0){
			throw new ImpossibleDimensionsException("" + scaleX + " (at a width of " + width + " )", "Min. " + (float)(1 / (float)width));
		}else if (scaleY * height <= 0){ throw new ImpossibleDimensionsException("" + scaleY + " (at a height of " + width + " )", "Min. " + (float)(1 / (float)height)); }
	}

	private static BufferedImage toBufferedImage(java.awt.Image img){
		if (img instanceof BufferedImage){ return (BufferedImage)img; }

		// Create a buffered image with transparency
		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		// Draw the image on to the buffered image
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		// Return the buffered image
		return bimage;
	}

	// Getters

	/**
	 * @return the width of the image
	 */
	public int getWidth(){ return width; }

	/**
	 * @return the height of the image
	 */
	public int getHeight(){ return height; }

	/**
	 * @return the array of pixels
	 */
	public int[] getPixels(){ return pixels; }

	/**
	 * @return this classes' bufferedimage-object
	 */
	public BufferedImage getBufferedImage(){ return image; }

}
