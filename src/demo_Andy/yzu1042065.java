package demo_Andy;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class yzu1042065 extends JFrame {
	JFrame parent;
	myPane mypane;

	public yzu1042065() {
		super("demo image");
		setSize(800, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		parent = this;
		mypane = new myPane();

		Container c = getContentPane();

		JButton openButton = new JButton("Open");

		openButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser chooser = new JFileChooser("images");

				int option = chooser.showOpenDialog(parent);
				if (option == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					loadImage(file);
				} else {
				}
			}
		});

		JButton grayButton = new JButton("GRAY");
		grayButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				mypane.gray();
			}
		});

		JButton lineButton = new JButton("LINE");
		lineButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				mypane.line();
			}
		});

		JButton AndyButton = new JButton("Andy");
		AndyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				mypane.Andy();
			}
		});

		JPanel top = new JPanel();
		top.add(openButton);
		top.add(grayButton);
		top.add(lineButton);
		top.add(AndyButton);
		c.add(top, "First");
		c.add(new JScrollPane(mypane), "Center");
	}

	private void loadImage(File file) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			System.out.println("read error: " + e.getMessage());
		}
		mypane.setImage(image);
	}

	public static void main(String args[]) {
		yzu1042065 vc = new yzu1042065();
		vc.setVisible(true);
	}
}

class myPane extends JPanel {
	BufferedImage image;
	Dimension size = new Dimension();

	public myPane() {
	}

	public myPane(BufferedImage image) {
		this.image = image;
		setComponentSize();
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, this);
	}

	public Dimension getPreferredSize() {
		return size;
	}

	public void setImage(BufferedImage bi) {
		image = bi;
		setComponentSize();
		repaint();

	}

	public void gray() {

		for (int x = 0; x < image.getWidth(); x++)
			for (int y = 0; y < image.getHeight(); y++) {
				int rgb = image.getRGB(x, y);
				rgb = (rgb & 0xff) << 16 | (rgb & 0xff) << 8 | (rgb & 0xff);
				image.setRGB(x, y, rgb);
			}
		setComponentSize();
		repaint();

	}

	public void line() {
		int W = image.getWidth();
		int H = image.getHeight();
		int r, g, b;
		int rgb;
		int x, y;
		int[][][] A = new int[W][H][3];
		int[][][] B = new int[W][H][3];

		double[][] M = { { 1.5, 0, 1.5 }, { 0, 0, 0 }, { -1, 0, -1 } };

//---- read original pixel valuem----
		for (x = 0; x < W; x++)
			for (y = 0; y < H; y++) {
				rgb = image.getRGB(x, y);
				r = (rgb >> 16) & 0xff;
				g = (rgb >> 8) & 0xff;
				b = rgb & 0xff;
				A[x][y][0] = r;
				A[x][y][1] = g;
				A[x][y][2] = b;
				B[x][y][0] = B[x][y][1] = B[x][y][2] = 0;
			}
//--- image processing ---- 
		int i, j, k;
		int s;

		for (x = 1; x < W - 1; x++)
			for (y = 1; y < H - 1; y++) {
				for (k = 0; k < 3; k++) {
					for (i = 0; i < 3; i++)
						for (j = 0; j < 3; j++)
							B[x][y][k] += A[x + i - 1][y + j - 1][k] * M[i][j];
					if (B[x][y][k] > 255)
						B[x][y][k] = 255;
					if (B[x][y][k] < 0)
						B[x][y][k] = 0;
				}
			}
//------- set result to image -------

		for (x = 0; x < W; x++)
			for (y = 0; y < H; y++) {
				r = B[x][y][0];
				g = B[x][y][1];
				b = B[x][y][2];
				rgb = (r & 0xff) << 16 | (g & 0xff) << 8 | (b & 0xff);

				image.setRGB(x, y, rgb);
			}

		setComponentSize();
		repaint();
	}
	//------- Andy Style -------
	public void Andy() {
		//------- Get max & min gray -------
		float max=0;
		float min=255;
		for (int i = 0; i < image.getWidth(); i++)
			for (int j = 0; j < image.getHeight(); j++) {
				int rgb = image.getRGB(i, j);
				rgb = (rgb & 0xff) << 16 | (rgb & 0xff) << 8 | (rgb & 0xff);
				Color color=new Color(rgb);
				int gray=(color.getRed()+color.getGreen()+color.getBlue())/3;

				if(gray>max)max=gray;
				if(gray<min)min=gray;
			}
		//------- Get color -------
		for (int x = 0; x < image.getWidth(); x++)
			for (int y = 0; y < image.getHeight(); y++) {
				int rgb = image.getRGB(x, y);
				rgb = (rgb & 0xff) << 16 | (rgb & 0xff) << 8 | (rgb & 0xff);
				Color color=new Color(rgb);
				int gray=(color.getRed()+color.getGreen()+color.getBlue())/3;
				
				float level=(gray-min)/(max-min);
				System.out.println("level:"+level+"max"+"|gray:"+gray);

				if (level >0.85) {
					Color HotPink = new Color(219, 98, 129);
					rgb = HotPink.getRGB();
				}
				else if (level > 0.5&&level <0.89) {
					Color Complexion = new Color(235, 192, 167);
					rgb = Complexion.getRGB();
				}
				else if (level  > 0.2&&level <0.5) {
					Color myYellow = new Color(254, 206, 107);
					rgb = myYellow.getRGB();
				}
				else {
					Color myBlack = new Color(37,22,22); 
					rgb = myBlack.getRGB();	
				}
				image.setRGB(x, y, rgb);

			}
		setComponentSize();
		repaint();

	}

	private void setComponentSize() {
		if (image != null) {
			size.width = image.getWidth();
			size.height = image.getHeight();
			revalidate();
		}
	}

}