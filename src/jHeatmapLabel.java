import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.jfree.svg.SVGGraphics2D;
import org.jfree.svg.SVGUtils;


public class jHeatmapLabel {
	
	private static File INPUT = null;;
	private static File OUTPUT = new File("OutputHeatmap.svg");
	
	private static Color color = new Color(0,0,0); // Color.BLACK
	private static int borderWidth = 2; // Set thickness of border and tickmarks
	private static int XtickHeight = 10; // Height of X-axis tickmarks
	private static int FONTSIZE = 18; // Set font size

	private static String XleftLabel = ""; // Left X-tickmark label
	private static String XmidLabel = ""; // Mid X-tickmark label
	private static String XrightLabel = ""; // Right X-tickmark label
	
	private static String xLabel = ""; // X-axis label
	private static String yLabel = ""; // Y-axis label
	
	public static void main(String[] args) throws IOException {
		
		// Load command line parameters
		loadConfig(args);
		
		// Import PNG image
		BufferedImage image = ImageIO.read(INPUT);
		// Get initial image size
		int Height = image.getHeight();
		int Width = image.getWidth();

		// Initialize SVG object
		SVGGraphics2D svg = new SVGGraphics2D(Width, Height);
		svg.setColor(color);
		// Set thickness of border
		svg.setStroke(new BasicStroke(borderWidth));
		
		// Draw rectangle around PNG
		svg.drawImage(image, 0, 0, null);
		svg.draw(new Rectangle(0, 0, Width, Height));
		
		// Draw left x-axis tickmark
		svg.drawLine(0, Height, 0, Height + XtickHeight);
		// Draw mid x-axis tickmark
		svg.drawLine((Width / 2), Height, (Width / 2), Height + XtickHeight);
		// Draw right x-axis tickmark
		svg.drawLine(Width, Height, Width, Height + XtickHeight);
		
		// Set font parameters
		svg.setFont(new Font("Arial", Font.PLAIN, FONTSIZE));

		// Draw X-axis tickmark labels
		if(!XleftLabel.equals("")) {
			int XleftSize = svg.getFontMetrics().stringWidth(XleftLabel);
			svg.drawString(XleftLabel, -1 * (XleftSize / 2), Height + XtickHeight + FONTSIZE);
		}
		if(!XmidLabel.equals("")) {
			int XmidSize = svg.getFontMetrics().stringWidth(XmidLabel);
			svg.drawString(XmidLabel, (Width / 2) - (XmidSize / 2), Height + XtickHeight + FONTSIZE);
		}
		if(!XrightLabel.equals("")) {
			int XrightSize = svg.getFontMetrics().stringWidth(XrightLabel);
			svg.drawString(XrightLabel, Width - (XrightSize / 2), Height + XtickHeight + FONTSIZE);
		}
		
		// Draw X-label
		if(!xLabel.equals("")) {
			int Xmidpoint = svg.getFontMetrics().stringWidth(xLabel);
			svg.drawString(xLabel, (Width / 2) - (Xmidpoint / 2), Height + XtickHeight + (FONTSIZE * 2) );
		}
		
		// Draw Y-label
		if(!yLabel.equals("")) {
			int Ymidpoint = svg.getFontMetrics().stringWidth(yLabel);
			// Remember original orientation
			AffineTransform orig = svg.getTransform();
			// Rotate drawing space 180 degrees
			svg.rotate(-Math.PI/2);
			svg.drawString(yLabel, -1 * ((Height / 2) + (Ymidpoint / 2)), -1 * FONTSIZE);
			// Restore original orientation
			svg.setTransform(orig);
		}
		
		// Output file as SVG
		SVGUtils.writeToSVG(OUTPUT, svg.getSVGElement());
	}
	
	public static void loadConfig(String[] command){
		for (int i = 0; i < command.length; i++) {
			switch (command[i].charAt((1))) {
				case 'i':
					INPUT = new File(command[i + 1]);
					i++;
					break;
				case 'o':
					OUTPUT = new File(command[i + 1]);
					i++;
					break;
				case 'c':
					color  = Color.decode(command[i + 1]);
					i++;
					break;
				case 'w':
					borderWidth = Integer.parseInt(command[i + 1]);
					i++;
					break;
				case 't':
					XtickHeight = Integer.parseInt(command[i + 1]);
					i++;
					break;
				case 'f':
					FONTSIZE = Integer.parseInt(command[i + 1]);
					i++;
					break;
				case 'l':
					XleftLabel = command[i + 1];
					i++;
					break;
				case 'm':
					XmidLabel = command[i + 1];
					i++;
					break;
				case 'r':
					XrightLabel = command[i + 1];
					i++;
					break;
				case 'x':
					xLabel = command[i + 1];
					i++;
					break;
				case 'y':
					yLabel = command[i + 1];
					i++;
					break;
				case 'h':
					printUsage();
					System.exit(0);
			}
		}
		if(INPUT == null) {
			System.err.println("No image file specified!!!");
			printUsage();
			System.exit(1);
		} else if(OUTPUT == null) {
			System.err.println("No output file specified!!!");
			printUsage();
			System.exit(1);
		} else if(borderWidth < 1) {
			System.err.println("Invalid line thickness selected!!!");
			printUsage();
			System.exit(1);
		} else if(XtickHeight < 1) {
			System.err.println("Invalid X-tickmark height selected!!!");
			printUsage();
			System.exit(1);
		} else if(FONTSIZE < 1) {
			System.err.println("Invalid font size selected!!!");
			printUsage();
			System.exit(1);
		}	
		System.out.println("-----------------------------------------\nCommand Line Arguments:");
		System.out.println("Input image: " + INPUT);
		System.out.println("Output image: " + OUTPUT);
		System.out.println("-----------------------------------------");
		System.out.println("Output color:\t\t" + color.toString());
		System.out.println("Line thickness:\t\t" + borderWidth);
		System.out.println("X-tickmark height:\t" + XtickHeight);
		System.out.println("Font size:\t\t" + FONTSIZE);
		if(!XleftLabel.equals("")) { System.out.println("Left X-tick label:\t" + XleftLabel); }
		if(!XmidLabel.equals("")) { System.out.println("Mid X-tick label:\t" + XmidLabel); }
		if(!XrightLabel.equals("")) { System.out.println("Right X-tick label:\t" + XrightLabel); }
		if(!xLabel.equals("")) { System.out.println("X-axis label:\t\t" + xLabel); }
		if(!yLabel.equals("")) { System.out.println("Y-axis label:\t\t" + yLabel); }

	}
	
	public static void printUsage() {
		System.err.println("Usage: java -jar HeatmapSVG.jar -i [OriginalImage] -o [ModifiedImage]");
		System.err.println("Required Parameter:");
		System.err.println("Input image\t\t-i");
		System.err.println("\nSupported Options:");
		System.err.println("Output image\t\t-o Default: " + OUTPUT);
		System.err.println("Output color:\t\t-c Must be in Hex format. Default: " + color.toString());
		System.err.println("Line thickness\t\t-w Integer Default: " + borderWidth);
		System.err.println("X-tickmark height\t-t Integer Default: " + XtickHeight);
		System.err.println("Font size\t\t-f Integer Default: " + FONTSIZE);
		System.err.println("Left X-tick label\t-l String");
		System.err.println("Mid X-tick label\t-m String");
		System.err.println("Right X-tick label\t-r String");
		System.err.println("X-axis label\t\t-x String");
		System.err.println("Y-axis label\t\t-y String");
	}
	
}
