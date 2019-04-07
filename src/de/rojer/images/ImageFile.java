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
 * @version 7.04.2019
 */
public class ImageFile{

	/**
	 * The width of the image
	 */
	protected int width;

	/**
	 * The height of the image
	 */
	protected int height;

	/**
	 * An array, containing the pixel data of the image
	 */
	protected int[] pixels;

	/**
	 * The image object
	 */
	protected BufferedImage image;

	/**
	 * Create an image-object
	 * @param image the bufferedimage-object to be used
	 */
	public ImageFile(BufferedImage image){
		this.image	= image;
		this.width	= this.image.getWidth();
		this.height	= this.image.getHeight();
		this.pixels	= this.image.getRGB(0, 0, this.width, this.height, null, 0, this.width);
	}

	/**
	 * Create an scaled image-object
	 * @param image the bufferedimage-object to be used
	 * @param width the new width (smaller or equal 0 for normal width)
	 * @param height the new height (smaller or equal 0 for normal height)
	 */
	public ImageFile(BufferedImage image, int width, int height){
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
	 * @param scaleX the factor to scale the width with (greater 0)
	 * @param scaleY the factor to scale the height with (greater 0)
	 */
	public ImageFile(BufferedImage image, float scaleX, float scaleY){
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
	public ImageFile(String path, String fileName){
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
	 * Create an image-object
	 * @param completePath the complete path to the file (with extension)
	 */
	public ImageFile(String completePath){
		try{
			this.image	= ImageIO.read(new File(completePath));
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
	 * @param width the new width (smaller or equal 0 for normal width)
	 * @param height the new height (smaller or equal 0 for normal height)
	 */
	public ImageFile(String path, String fileName, int width, int height){
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
	 * @param completePath the complete path to the file (with extension)
	 * @param width the new width (smaller or equal 0 for normal width)
	 * @param height the new height (smaller or equal 0 for normal height)
	 */
	public ImageFile(String completePath, int width, int height){
		try{
			int newWidth, newHeight;
			newWidth	= (width <= 0) ? image.getWidth() : width;
			newHeight	= (height <= 0) ? image.getHeight() : height;
			this.image	= toBufferedImage(ImageIO.read(new File(completePath)).getScaledInstance(newWidth, newHeight, 0));
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
	 * @param scaleX the factor to scale the width with (greater 0)
	 * @param scaleY the factor to scale the height with (greater 0)
	 */
	public ImageFile(String path, String fileName, float scaleX, float scaleY){
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

	/**
	 * Create an scaled image-object
	 * @param completePath the complete path to the file (with extension)
	 * @param scaleX the factor to scale the width with (greater 0)
	 * @param scaleY the factor to scale the height with (greater 0)
	 */
	public ImageFile(String completePath, float scaleX, float scaleY){
		try{
			BufferedImage image = (BufferedImage)ImageIO.read(new File(completePath));
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

	// "Destructors"

	/**
	 * Destroys this object and all of its data
	 * @param image this image
	 */
	public void destroy(ImageFile image){
		this.width	= 0;
		this.height	= 0;
		this.pixels	= null;
		this.image	= null;
		image		= null;
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
	public ImageFile getSubImage(int posX, int posY, int width, int height){
		try{
			checkForGoodDimensions(posX, posY, width, height);
		}catch(ImpossibleDimensionsException e){
			e.printStackTrace();
		}
		ImageFile newImage;
		newImage = new ImageFile(this.image.getSubimage(posX, posY, width, height));
		return newImage;
	}

	// Helper-Methods

	/**
	 * Checks, if the given dimensions are possible
	 * @param posX the wanted x-position
	 * @param posY the wanted y-position
	 * @param width the wanted width
	 * @param height the wanted height
	 * @throws ImpossibleDimensionsException when the wanted values are not
	 *         suited
	 */
	protected void checkForGoodDimensions(int posX, int posY, int width, int height) throws ImpossibleDimensionsException{
		if (posX < 0 || posX > this.image.getWidth() - 1){
			throw new ImpossibleDimensionsException("" + posX, "0 - " + (this.image.getWidth() - 1));
		}else if (posY < 0 || posY > this.image.getHeight() - 1){
			throw new ImpossibleDimensionsException("" + posY, "0 - " + (this.image.getHeight() - 1));
		}else if (width <= 0 || width + posX > this.image.getWidth()){
			throw new ImpossibleDimensionsException("" + width, "1 - " + (this.image.getWidth() - posX));
		}else if (height <= 0 || height + posY > this.image.getHeight()){ throw new ImpossibleDimensionsException("" + height, "1 - " + (this.image.getHeight() - posY)); }
	}

	/**
	 * Checks, if the given scalings are possible
	 * @param scaleX the wanted x-scale
	 * @param width the wanted width
	 * @param scaleY the wanted y-scale
	 * @param height the wanted height
	 * @throws ImpossibleDimensionsException when the wanted values are not
	 *         suited
	 */
	protected void checkForGoodScaling(float scaleX, int width, float scaleY, int height) throws ImpossibleDimensionsException{
		if (scaleX * width <= 0){
			throw new ImpossibleDimensionsException("" + scaleX + " (at a width of " + width + " )", "Min. " + (float)(1 / (float)width));
		}else if (scaleY * height <= 0){ throw new ImpossibleDimensionsException("" + scaleY + " (at a height of " + width + " )", "Min. " + (float)(1 / (float)height)); }
	}

	/**
	 * Converts an AWT Image object into an bufferedimage-object
	 * @param img the image to convert
	 * @return the converted image
	 */
	protected static BufferedImage toBufferedImage(java.awt.Image img){
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
