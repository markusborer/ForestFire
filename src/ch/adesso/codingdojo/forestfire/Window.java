package ch.adesso.codingdojo.forestfire;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Window extends JFrame {

	private int width;
	private int height;
	private long lastTime;
	private BufferedImage image;
	private int paintCount;

	public Window(int width, int height) {
		super();
		this.width = width;
		this.height = height;
		initialize();
	}

	private void initialize() {
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		JPanel panel = new JPanel() {
			@Override
			public void paint(Graphics graphics) {
				super.paint(graphics);
				graphics.drawImage(image, 0, 0, null);
				showFramesCount();
			}
		};
		panel.setPreferredSize(new Dimension(width, height));
		this.add(panel);
		this.pack();
	}

	public void setColor(int x, int y, int[] color) {
		image.getRaster().setPixel(x, y, color);
	}

	private void showFramesCount() {
		paintCount++;
		if (paintCount == 10) {
			paintCount = 0;
			long time = System.nanoTime();
			long frames = 10000000000l / (time - lastTime);
			setTitle("Frames " + frames + " (Running with " + ForestFire.THREAD_COUNT + " threads)");
			lastTime = time;
		}
	}

}
