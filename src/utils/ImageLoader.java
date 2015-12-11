package utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by Gvendurst on 11.12.2015.
 */
public class ImageLoader {
	public static BufferedImage loadImage(String fileName){
		try {
			return ImageIO.read(ImageLoader.class.getResourceAsStream(fileName));
		}
		catch (IllegalArgumentException e){
			System.out.println("Image not found (" + fileName +")");
		}
		catch (IOException e){
			System.out.println("Error opening file (" + fileName +")");
		}

		return null;
	}
}
