package org.cbot.ocr;

import java.applet.Applet;
import java.awt.Canvas;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import org.cbot.Bot;

public class OCRImage {
	
	private Applet applet;
	private Canvas canvas;
	private int[] data;
	private int width;
	private int height;
	
	public OCRImage(int[] data, int width, int height) {
		this.data = data;
		this.width = width;
		this.height = height;
	}
	
	public OCRImage minimize(int size) {
		this.data = getMinimizedData(size);
		this.width /= size;
		this.height /= size;
		return this;
	}
	
	public Applet getApplet() {
		return applet;
	}
	
	public void setCanvas(Canvas canvas) {
		this.canvas = canvas;
	}
	
	public Canvas getCanvas() {
		return canvas;
	}
	
	public int[] getData() {
		if(canvas != null)
			return ((DataBufferInt) canvas.getBuffer().getRaster().getDataBuffer()).getData();
		return data;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getData(int x, int y) {
		return getData()[(y * getWidth()) + x];
	}
	
	public int[] getColorData(int x, int y) {
		return getColorData(getData(x, y));
	}
	
	public int[] getColorData(int color) {
		return new int[] { color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF };
	}
	
	public boolean isInTolerance(int fColor, int sColor, double tolerance) {
		int[] fc = getColorData(fColor);
		int[] sc = getColorData(sColor);
		return (Math.sqrt(((fc[0]-sc[0])*(fc[0]-sc[0])) + ((fc[1]-sc[1])*(fc[1]-sc[1])) + ((fc[2]-sc[2])*(fc[2]-sc[2]))) / 100) <= tolerance;
	}
	
	public int[] getColorLoc(int color, double tolerance) {
		for(int x = 0; x < Bot.WIDTH; x++) {
			for(int y = 0; y < Bot.HEIGHT; y++) {
				if(isInTolerance(getData(x, y), color, tolerance))
					return new int[] { x, y };
			}
		}
		return new int[] { -1, -1 };
	}
	
	public int[] getColorLocInArea(int color, double tolerance, int x, int y, int x2, int y2) {
		for(int xx = x; xx < x2; xx++) {
			for(int yy = y; yy < y2; yy++) {
				if(isInTolerance(getData(xx, yy), color, tolerance))
					return new int[] { xx, yy };
			}
		}
		return new int[] { -1, -1 };
	}
	
	/*public int[] getColorLocInArea(int color, double tolerance, ScriptArea area) {
		int[] bounds = area.getBounds();
		return getColorLocInArea(color, tolerance, bounds[0], bounds[1], bounds[2], bounds[3]);
	}*/
	
	public boolean isColorInArea(int color, double tolerance, int x, int y, int x2, int y2) {
		for(int xx = x; xx < x2; xx++) {
			for(int yy = y; yy < y2; yy++) {
				if(isInTolerance(getData(xx, yy), color, tolerance))
					return true;
			}
		}
		return false;
	}
	
	/*public boolean isColorInArea(int color, double tolerance, ScriptArea area) {
		int[] bounds = area.getBounds();
		return isColorInArea(color, tolerance, bounds[0], bounds[1], bounds[2], bounds[3]);
	}
	
	public boolean isColorNotInarea(int color, double tolerance, ScriptArea area) {
		int[] data = getDataInSection(area);
		for(int i = 0; i < data.length; i++) {
			if(!isInTolerance(data[i], color, tolerance))
				return true;
		}
		return false;
	}*/
	
	public int[] getDataInSection(int x, int y, int x2, int y2) {
		int[] temp = new int[(x2 - x) * (y2 - y)];
		int count = 0;
		for(int yy = y; yy < y2; yy++) {
			for(int xx = x; xx < x2; xx++) {
				if(xx >= getWidth() || yy >= getHeight()) {
					temp[count++] = 0;
				} else { 
					temp[count++] = getData(xx, yy);
				}
			}
		}
		return temp;
	}
	
	/*public int[] getDataInSection(ScriptArea area) {
		int[] bounds = area.getBounds();
		return getDataInSection(bounds[0], bounds[1], bounds[2], bounds[3]);
	}
	
	public int[] getColorLocInSpiral(final int color, final double tolerance, int x, int y, int distance) {
		return spiral(x, y, distance, new SpiralCallback() {
			@Override
			public boolean callback(int xx, int yy) {
				if(xx < 0 || xx > Bot.WIDTH || yy < 0 || yy > Bot.HEIGHT)
					return false;
				if(isInTolerance(getData(xx, yy), color, tolerance))
					return true;
				return false;
			}
		});
	}*/
	
	public int[] getMenuLocationValuesSpiral(int color, final double tolerance, int x, int y, int distance) {
		int minX = 0;
		int maxX = 0;
		int minY = 0;
		int maxY = 0;
		int count = 0;
		int amount = 0;
		int temp = 0;
		boolean[] check = new boolean[2];
		while((amount / 2) <= distance) {
			if(x > 0 && x < Bot.WIDTH && y > 0 && y < Bot.HEIGHT && isInTolerance(getData(x, y), color, tolerance)) {
				if(minX == 0 || x < minX)
					minX = x;
				if(maxX == 0 || x > maxX)
					maxX = x;
				if(minY == 0 || y < minY)
					minY = y;
				if(maxY == 0 || y > maxY)
					maxY = y;
			}
			if(count == 0) {
				if(!check[0]) {
					amount++;
					check[0] = true;
				}
				x += 1;
				temp++;
				if(temp == amount) {
					count++;
					temp = 0;
					check[0] = false;
				}
			} else if(count == 1) {
				y += 1;
				temp++;
				if(temp == amount) {
					count++;
					temp = 0;
				}
			} else if(count == 2) {
				if(!check[1]) {
					amount++;
					check[1] = true;
				}
				x -= 1;
				temp++;
				if(temp == amount) {
					count++;
					temp = 0;
					check[1] = false;
				}
			} else if(count == 3) {
				y -= 1;
				temp++;
				if(temp == amount) {
					count = 0;
					temp = 0;
				}
			}
		}
		return new int[] { minX, minY, maxX, maxY };
	}
	
	public int[] findImage(OCRImage image, int startX, int startY, int endX, int endY, double tolerance) {
		int tx = 0;
		int ty = 0;
		int count = 0;
		int highest = 0;
		for(int y = startY; y < endY; y++) {
			for(int x = startX; x < endX; x++) {
				count = 0;
				for(int yy = 0; yy < image.getHeight(); yy++) {
					for(int xx = 0; xx < image.getWidth(); xx++) {
						int fColor = getData(x + xx, y + yy);
						int sColor = image.getData(xx, yy);
						if(isInTolerance(fColor, sColor, tolerance)) {
							count++;
						}
					}
				}
				if(count > highest) {
					highest = count;
					tx = x;
					ty = y;
				}
			}
		}
		return new int[] { tx, ty };
	}
	
	public int[] findImageFast(OCRImage other) {
		int width = getWidth() - other.getWidth();
		int height = getHeight() - other.getHeight();
		int count = 0;
		int loops = 1;
		int threshold = (other.getWidth() * loops) - (4 * loops);
		int highest = 0;
		int[] coords = new int[2];
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				count = 0;
				for(int yy = 0; yy < loops; yy++) {
					for(int xx = 0; xx < other.getWidth(); xx++) {
						int fColor = getData(x + xx, y + yy);
						int sColor = other.getData(xx, yy);
						if(isInTolerance(fColor, sColor, .3)) {
							count++;
						}
					}
				}
				if(count > threshold) {
					count = 0;
					for(int yy = 0; yy < other.getHeight(); yy++) {
						for(int xx = 0; xx < other.getWidth(); xx++) {
							int fColor = getData(x + xx, y + yy);
							int sColor = other.getData(xx, yy);
							if(isInTolerance(fColor, sColor, .3)) {
								count++;
							}
						}
					}
					if(count > highest) {
						highest = count;
						coords[0] = x;
						coords[1] = y;
					}
				}
			}
		}
		return coords;
	}
	
	/*private int[] spiral(int x, int y, int distance, SpiralCallback callback) {
		int count = 0;
		int amount = 0;
		int temp = 0;
		boolean[] check = new boolean[2];
		while((amount / 2) <= distance) {
			if(callback.callback(x, y))
				return new int[] { x, y };
			if(count == 0) {
				if(!check[0]) {
					amount++;
					check[0] = true;
				}
				x += 1;
				temp++;
				if(temp == amount) {
					count++;
					temp = 0;
					check[0] = false;
				}
			} else if(count == 1) {
				y += 1;
				temp++;
				if(temp == amount) {
					count++;
					temp = 0;
				}
			} else if(count == 2) {
				if(!check[1]) {
					amount++;
					check[1] = true;
				}
				x -= 1;
				temp++;
				if(temp == amount) {
					count++;
					temp = 0;
					check[1] = false;
				}
			} else if(count == 3) {
				y -= 1;
				temp++;
				if(temp == amount) {
					count = 0;
					temp = 0;
				}
			}
		}
		return new int[] { -1, -1 };
	}*/
	
	public BufferedImage getBufferedImage() {
		BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		for(int y = 0; y < image.getHeight(); y++) {
			for(int x = 0; x < image.getWidth(); x++) {
				image.getRaster().setPixel(x, y, getColorData(x, y));
			}
		}
		return image;
	}
	
	private int[] getMinimizedData(int size) {
		int w = getWidth() / size;
		int h = getHeight() / size;
		int[] temp = new int[w * h];
		int count = 0;
		for(int y = 0; y < h; y++) {
			for(int x = 0; x < w; x++) {
				temp[count++] = getAverage(x * size, y * size, size);
			}
		}
		return temp;
	}
	
	private int getAverage(int x, int y, int size) {
		int[] color = new int[3];
		int count = 0;
		for(int yy = 0; yy < size; yy++) {
			for(int xx = 0; xx < size; xx++) {
				int[] colors = getColorData(x + xx, y + yy);
				color[0] += colors[0];
				color[1] += colors[1];
				color[2] += colors[2];
				count++;
			}
		}
		return (color[2] / count) | (color[1] / count) << 8 | (color[0] / count) << 16;
	}
	
}
