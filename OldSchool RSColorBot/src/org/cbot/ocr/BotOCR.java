package org.cbot.ocr;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.LookupOp;
import java.awt.image.ShortLookupTable;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.cbot.game.ScreenArea;
import org.cbot.image.ImageTool;

public class BotOCR {

	private Map<String, ArrayList<CharacterDataMap>> characterMap = new HashMap<String, ArrayList<CharacterDataMap>>();
	
	public BotOCR() {
		File dir = new File("image/");
		for(String s : dir.list()) {
			if(s.contains(".png")) {
				File img = new File(dir.getAbsolutePath() + File.separator + s);
				loadImages(img);
			}
		}
	}
	
	public void loadImages(File img) {
		try {
			String[] parts = img.getAbsolutePath().split("image/")[1].split("_");
			String identifier = parts[1];
			String type = parts[2].split(".png")[0];
			if((type.startsWith("a") || type.startsWith("A")) || type.startsWith("0") || type.startsWith("1")) {
				if(type.startsWith("a") || type.startsWith("A"))
						type = type.substring(0, type.length() - 1);
				String[] chars = new String[type.length()];
				for(int i = 0; i < chars.length; i++) {
					chars[i] = String.valueOf(type.charAt(i));
				}
				load(ImageIO.read(img), chars, identifier, false, 50);
			}
			if(!type.startsWith("a") && !type.startsWith("A") && !type.startsWith("0") && !type.startsWith("1")) {
				ArrayList<String> string = new ArrayList<String>();
				char[] array = type.toCharArray();
				for(int i = 0; i < array.length; i++) {
					String c = String.valueOf(array[i]);
					if(c.equalsIgnoreCase("s")) {
						string.add("*");
					} else if(c.equalsIgnoreCase("c")) {
						string.add(":");
					} else if(c.equalsIgnoreCase("a")) {
						string.add(String.valueOf(array[++i]) + String.valueOf(array[++i]));
					} else {
						string.add(c);
					}
				}
				String[] temp = new String[string.size()];
				for(int i = 0; i < temp.length; i++) {
					temp[i] = string.get(i);
				}
				load(ImageIO.read(img), temp, identifier, false, 50);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getText(String identifier, int x, int y, int x2, int y2, int brightness, int threshold, boolean spaces, int spaceDistance, boolean invert) {
		int[] data = ImageTool.getPixelsInSection(x, y, x2, y2);
		return scan(getImage(data, (x2 - x), (y2 - y), threshold, invert), identifier, spaces, spaceDistance, brightness, .05);
	}
	
	public String getText(String identifier, ScreenArea area, int brightness, int threshold, boolean spaces, int spaceDistance, boolean invert) {
		int[] bounds = area.getBounds();
		return getText(identifier, bounds[0], bounds[1], bounds[2], bounds[3], brightness, threshold, spaces, spaceDistance, invert);
	}
	
	public String getText(String identifier, ScreenArea area, int brightness, int threshold, boolean spaces,int spaceDistance, int[] colors, double tolerance, boolean invert) {
		int[] bounds = area.getBounds();
		int[] data = ImageTool.getPixelsInSection(area);
		return scan(getImage(data, (bounds[2] - bounds[0]), (bounds[3] - bounds[1]), threshold, colors, tolerance, invert), identifier, spaces, spaceDistance, brightness, .05);
	}
	
	public String getText(String identifier, int x, int y, int x2, int y2, int brightness, int threshold, boolean spaces, int spaceDistance, int[] colors, double tolerance, boolean invert) {
		int[] data = ImageTool.getPixelsInSection(x, y, x2, y2);
		return scan(getImage(data, (x2 - x), (y2 - y), threshold, invert), identifier, spaces, spaceDistance, brightness, .05);
	}
	
	private ArrayList<CharacterDataMap> getCharacterArray(OCRImage image, int brightness, boolean spaces, int spaceDistance) {
		int[][] pos = getCharacterPositions(image, brightness, spaces, spaceDistance);
		ArrayList<CharacterDataMap> temp = new ArrayList<CharacterDataMap>();
		for(int i = 0; i < pos.length; i++) {
			if(pos[i][0] == -1 && pos[i][1] == -1 && pos[i][2] == -1 && pos[i][3] == -1) {
				temp.add(new CharacterDataMap(" "));
				continue;
			}
			int tWidth = pos[i][2] - pos[i][0] + 1;
			int tHeight = pos[i][3] - pos[i][1] + 1;
			int[] tempData = new int[tWidth * tHeight];
			int counter = 0;
			for(int y = pos[i][1]; y < pos[i][1] + tHeight; y++) {
				for(int x = pos[i][0]; x < pos[i][0] + tWidth; x++) {
					tempData[counter++] = image.getData()[(y * image.getWidth()) + x];
				}
			}
			temp.add(new CharacterDataMap(new OCRImage(tempData, tWidth, tHeight)));
		}
		return temp;
	}
	
	private String scan(OCRImage image, String identifier, boolean spaces, int spaceDistance, int brightness, double tolerance) {
		ArrayList<CharacterDataMap> temp = getCharacterArray(image, brightness, spaces, spaceDistance);
		ArrayList<CharacterDataMap> characters = characterMap.get(identifier);
		String str = "";
		for(CharacterDataMap map : characters) {
			for(int i = 0; i < temp.size(); i++) {
				if(temp.get(i).getImage() == null)
					continue;
				if(temp.get(i).equal(map, tolerance)) {
					if(temp.get(i).getCharacter() == null || temp.get(i).getCharacter().isEmpty())
						temp.get(i).setCharacter(map.getCharacter());
				}
			}
		}
		for(CharacterDataMap a : temp) {
			if(a.getCharacter() != null)
				str += a.getCharacter();
		}
		return str.trim();
	}

	private void load(Image image, String[] chars, String identifier, boolean invert, int brightness) {
		OCRImage end = getImage(image, invert);
		int[][] pos = getCharacterPositions(end, brightness, false, 0);
		if(characterMap.get(identifier) == null)
			characterMap.put(identifier, new ArrayList<CharacterDataMap>());
		for(int i = 0; i < pos.length; i++) {
			int width = pos[i][2] - pos[i][0] + 1;
			int height = pos[i][3] - pos[i][1] + 1;
			int[] tempData = new int[width * height];
			int counter = 0;
			for(int y = pos[i][1]; y < pos[i][1] + height; y++) {
				for(int x = pos[i][0]; x < pos[i][0] + width; x++) {
					tempData[counter++] = end.getData()[(y * end.getWidth()) + x];
				}
			}
			characterMap.get(identifier).add(new CharacterDataMap(chars[i], new OCRImage(tempData, width, height)));
		}
	}
	
	private int getEstimatedLength(OCRImage image, int brightness, boolean spaces, int spaceDistance) {
		int count = 0;
		int lastX = 0;
		for(int x = 0; x < image.getWidth(); x++) {
			for(int y = 0; y < image.getHeight(); y++) {
				int color = image.getData()[(y * image.getWidth()) + x];
				if(getBrightness(color, brightness)) {
					if(lastX == 0)
						lastX = x;
					if(x - lastX > 1)
						count++;
					if(spaces && (x - lastX > spaceDistance))
						count += ((x - lastX) / spaceDistance);
					lastX = x;
				}
			}
		}
		return count + 1;
	}

	private int[][] getCharacterPositions(OCRImage image, int brightness, boolean spaces, int spaceDistance) {
		int[][] pos = new int[getEstimatedLength(image, brightness, spaces, spaceDistance)][4];
		int count = 0, lastX = 0;
		for(int x = 0; x < image.getWidth(); x++) {
			for(int y = 0; y < image.getHeight(); y++) {
				int color = image.getData()[(y * image.getWidth()) + x];
				if(getBrightness(color, brightness)) {
					if(lastX == 0)
						lastX = x;
					if(x - lastX > 1)
						count++;
					if(x - lastX > spaceDistance && spaces) {
						int temp = ((x - lastX) / spaceDistance);
						for(int i = 0; i < temp; i++) {
							pos[count][0] = -1;
							pos[count][1] = -1;
							pos[count][2] = -1;
							pos[count][3] = -1;
							count++;
						}
						lastX = x;
					}
					lastX = x;
					if(pos[count][0] == 0) {
						pos[count][0] = x;
						pos[count][1] = y;
					}
					if(x < pos[count][0])
						pos[count][0] = x;
					if(y < pos[count][1])
						pos[count][1] = y;
					if(x > pos[count][2])
						pos[count][2] = x;
					if(y > pos[count][3])
						pos[count][3] = y;
				}
			}
		}
		return pos;
	}
	
	private OCRImage getImage(int[] data, int width, int height, int threshold, boolean invert) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		image.setRGB(0, 0, width, height, data, 0, width);
		if(invert) {
			BufferedImage in = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			short[] invertTable = new short[256];
			for (int i = 0; i < 256; i++) {
				invertTable[i] = (short) (255 - i);
			}
			new LookupOp(new ShortLookupTable(0, invertTable), null).filter(image, in);
			image = in;
		}
		int[] temp = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		for(int i = 0; i < temp.length; i++) {
			if(getBrightness(temp[i], threshold))
				temp[i] = 0;
			else
				temp[i] = 16777215;
		}
		return new OCRImage(temp, width, height);
	}
	
	private OCRImage getImage(int[] data, int width, int height, int threshold, int[] colors, double tolerance, boolean invert) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		image.setRGB(0, 0, width, height, data, 0, width);
		if(invert) {
			BufferedImage in = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			short[] invertTable = new short[256];
			for (int i = 0; i < 256; i++) {
				invertTable[i] = (short) (255 - i);
			}
			new LookupOp(new ShortLookupTable(0, invertTable), null).filter(image, in);
			image = in;
		}
		int[] temp = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		for(int i = 0; i < temp.length; i++) {
			if(getBrightness(temp[i], threshold)) {
				temp[i] = 0;
			} else {
				if(inTolerance(temp[i], colors, tolerance)) {
					temp[i] = 0;
				} else {
					temp[i] = 16777215;
				}
			}
		}
		return new OCRImage(temp, width, height);
	}
	
	private int invert(int color) {
		int[] data = getColorData(color);
		int[] newData = new int[] { 255 - data[0], 255 - data[1], 255 - data[2] };
		return ((newData[0] & 0x0FF) << 16) | ((newData[1] & 0x0FF) << 8) | (newData[2] & 0x0FF);
	}
	
	private boolean inTolerance(int color, int[] colors, double tolerance) {
		for(int a : colors) {
			if(isInTolerance(color, invert(a), tolerance))
				return true;
		}
		return false;
	}
	
	private OCRImage getImage(Image image, boolean invert) {
		BufferedImage temp = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
		temp.getGraphics().drawImage(image, 0, 0, null);
		return getImage(((DataBufferInt) temp.getRaster().getDataBuffer()).getData(), temp.getWidth(), temp.getHeight(), 50, invert);
	}
	
	private boolean getBrightness(int color, int brightness) {
		int[] c = getColorData(color);
	    return ((int) Math.sqrt(c[0] * c[0] * .241 + c[1] * c[1] * .691 + c[2] * c[2] * .068)) < brightness;
	}
	
	private int[] getColorData(int color) {
		return new int[] { color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF };
	}
	
	private boolean isInTolerance(int fColor, int sColor, double tolerance) {
		int[] fc = getColorData(fColor);
		int[] sc = getColorData(sColor);
		return (Math.sqrt(((fc[0]-sc[0])*(fc[0]-sc[0])) + ((fc[1]-sc[1])*(fc[1]-sc[1])) + ((fc[2]-sc[2])*(fc[2]-sc[2]))) / 100) <= tolerance;
	}

}
