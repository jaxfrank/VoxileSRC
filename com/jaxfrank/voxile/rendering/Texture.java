package com.jaxfrank.voxile.rendering;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL11.*;

public class Texture {
	// the texture files width and height variables
	private int width, height;
	// the returned texture
	private int texture;

	// our constructor which takes in the path
	// for the texture file. e.g. "tile.png"
	public Texture(String path) {
		texture = load(path);
	}

	// our load function which is passed the
	// path from our constructor.
	private int load(String path) {
		// declares an int array of all the pixels
		// that will be in the texture and instantiates
		// this array with nothing in it.
		int[] pixels = null;
		try {
			// our bufferedImage object which will read in
			// all of the data from our image file so that
			// we can process this data and apply it to a texture
			BufferedImage image = ImageIO.read(new FileInputStream("./res/" + path));
			// gets the width and height of the image
			// file that we have just loaded in.
			width = image.getWidth();
			height = image.getHeight();
			// sets our pixels array to equal an int array
			// with 1 value for every pixel in the image.
			// e.g 100px * 100px image would equal 10,000
			// pixels in our array.
			pixels = new int[width * height];
			// returns
			image.getRGB(0, 0, width, height, pixels, 0, width);
		} catch (IOException e) {
			e.printStackTrace();
		}

		int[] data = new int[width * height];
		for (int i = 0; i < width * height; i++) {
			// parses our alpha, red, green and blue
			// values from our pixels array
			// and then assigns them to our
			// data array in the order which openGL
			// will read them in.
			int a = (pixels[i] & 0xff000000) >> 24;
			int r = (pixels[i] & 0xff0000) >> 16;
			int g = (pixels[i] & 0xff00) >> 8;
			int b = (pixels[i] & 0xff);

			data[i] = a << 24 | b << 16 | g << 8 | r;
		}

		// generates our openGL Texture
		int result = glGenTextures();
		// binds this texture as a 2D Texture
		glBindTexture(GL_TEXTURE_2D, result);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

		IntBuffer buffer = BufferUtils.createIntBuffer(data.length * 4);
		buffer.put(data);
		buffer.flip();
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA,
				GL_UNSIGNED_BYTE, buffer);

		glBindTexture(GL_TEXTURE_2D, 0);
		return result;
	}

	public void bind() {
		// this code is basically similar to opening
		// a layer in photoshop on which you'll
		// be working with.
		glBindTexture(GL_TEXTURE_2D, texture);
	}

	public void unbind() {
		// once you are done with your layer
		// you then close it so you can work
		// on others.
		glBindTexture(GL_TEXTURE_2D, 0);
	}

}

// import java.io.FileInputStream;
// import java.io.IOException;
// import java.io.InputStream;
// import java.nio.ByteBuffer;
//
// import de.matthiasmann.twl.utils.PNGDecoder;
// import de.matthiasmann.twl.utils.PNGDecoder.Format;
//
// import static org.lwjgl.opengl.GL11.*;
// import static org.lwjgl.opengl.GL13.*;
//
// public class Texture {
//
// private int id;
//
// private int width;
// private int height;
//
// public Texture(String path) {
// id = glGenTextures();
// loadPNG(path);
// }
//
// public void bind() {
// glActiveTexture(GL_TEXTURE0);
// glBindTexture(GL_TEXTURE_2D, id);
// }
//
// private void loadPNG(String path) {
// try {
// InputStream in = new FileInputStream("./res/" + path);
// PNGDecoder decoder = new PNGDecoder(in);
//
// width = decoder.getWidth();
// height = decoder.getHeight();
//
// System.out.println("Width: " + width);
// System.out.println("Height: " + height);
//
// ByteBuffer buf = ByteBuffer.allocateDirect(4 * width * height);
// decoder.decode(buf, width * 4, Format.RGBA);
// buf.flip();
// bind();
// glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGBA,
// GL_UNSIGNED_BYTE, buf);
//
// glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
// glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
//
// glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
//
// in.close();
// } catch(IOException e){
// e.printStackTrace();
// }
// }
//
// public int getWidth() {
// return width;
// }
//
// public int getHeight() {
// return height;
// }
// }
