package Tag5.ProPra23_Verteilte_Systeme_Code.src.de.umr.ds.task1;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.WritableRaster;

public class ImageProcessing {

	/**
	 * Loads an image from the given file path
	 *
	 * @param path The path to load the image from
	 * @return BufferedImage
	 */
	private static BufferedImage loadImage(String path) throws IOException {
		BufferedImage img = null;
		try {
			File file = new File(path);
			img = ImageIO.read(file);
		}
		catch (IOException e) {
			System.out.println(e);
		}
		return img;
	}

	/**
	 * Converts the input RGB image to a single-channel gray scale array.
	 * 
	 * @param img The input RGB image
	 * @return A 2-D array with intensities
	 */
	private static int[][] convertToGrayScaleArray(BufferedImage img) {
		int[][] grey2DArr = new int[img.getWidth()][img.getHeight()];

		for(int x = 0; x < img.getWidth(); x++) {
			for(int y = 0; y < img.getHeight(); y++) {
				Color rgb = new Color(img.getRGB(x, y));
				int alpha = 255;
				int r = rgb.getRed();
				int g = rgb.getGreen();
				int b = rgb.getBlue();
				int grey = (int) Math.floor(0.299 * r + 0.587 * g + 0.114 * b);
				grey2DArr[x][y] = grey;
			}
		}
		
		return grey2DArr;
	}

	/**
	 * Converts a single-channel (gray scale) array to an RGB image.
	 * 
	 * @param img The input image array
	 * @return BufferedImage
	 */
	private static BufferedImage convertToBufferedImage(int[][] img) {
		BufferedImage bufferedImage = new BufferedImage(img.length, img[0].length, BufferedImage.TYPE_INT_RGB);
		for(int x = 0; x < img.length; x++) {
			for(int y = 0; y < img[0].length; y++) {
				int rgb = img[x][y] << 16 | img[x][y] << 8 | img[x][y];
				bufferedImage.setRGB(x, y, rgb);
			}
		}
		
		return bufferedImage;
	}

	/**
	 * Saves an image to the given file path
	 *
	 * @param img The RGB image
	 * @param path The path to save the image to
	 */
	private static void saveImage(BufferedImage img, String path) throws IOException {
		try {
			File file = new File(path);
			ImageIO.write(img, "jpg", file);
		}
		catch (IOException e) {
			System.out.println(e);
		}
	}

	/**
	 * Converts input image to gray scale and applies the kernel.
	 * 
	 * @param img The RGB input image
	 * @param kernel The kernel to apply
	 * @return The convolved gray-scale image
	 */
	private static BufferedImage filter(BufferedImage img, Kernel kernel) {
		int[][] grey2DArr = convertToGrayScaleArray(img);
		int[][] result = kernel.convolve(grey2DArr);
		return convertToBufferedImage(result);
	}

	/*
	private static BufferedImage filter(BufferedImage img, Kernel kernel) {
        int padding = kernel.getWidth() / 2; // Padding size is based on kernel width

        // Create a padded image
        BufferedImage paddedImage = new BufferedImage(img.getWidth() + 2 * padding, img.getHeight() + 2 * padding, BufferedImage.TYPE_INT_RGB);
        paddedImage.getGraphics().drawImage(img, padding, padding, null);

        ConvolveOp convolveOp = new ConvolveOp(kernel);
        return convolveOp.filter(paddedImage, null);
    }
   */

	// TODO Task 1g)


	public static void main(String[] args) {

		// TODO

	}

}
