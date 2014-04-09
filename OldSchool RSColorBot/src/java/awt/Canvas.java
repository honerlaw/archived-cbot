package java.awt;

import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;

public class Canvas extends Component implements Accessible {

	private static final long serialVersionUID = 3517594143810829814L;
	
	private BufferedImage buffer = new BufferedImage(763, 504, BufferedImage.TYPE_INT_RGB);
	private GraphicsConfiguration config;
	
	public Canvas() {
		super();
	}
	
	public Canvas(GraphicsConfiguration config) {
		this();
		this.config = config;
	}
	
	@Override
	public GraphicsConfiguration getGraphicsConfiguration() {
		return config == null ? super.getGraphicsConfiguration() : config;
	}
	
	public BufferedImage getBuffer() {
		return buffer;
	}
	
	@Override
	public void addNotify() {
		super.addNotify();
	}
	
	@Override
	public void createBufferStrategy(int numBuffers) {
		super.createBufferStrategy(numBuffers);
	}
	
	@Override
	public void createBufferStrategy(int numBuffers, BufferCapabilities caps) {
		try {
			super.createBufferStrategy(numBuffers, caps);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public AccessibleContext getAccessibleContext() {
		return super.getAccessibleContext();
	}
	
	@Override
	public BufferStrategy getBufferStrategy() {
		return super.getBufferStrategy();
	}

	@Override
	public void paint(Graphics g) {
		//super.paint(g);
	}
	
	@Override
	public void update(Graphics g) {
		//super.update(g);
	}
	
	@Override
	public Graphics getGraphics() {
		return buffer == null ? super.getGraphics() : buffer.getGraphics();
	}
	
	public Graphics getCanvasGraphics() {
		return super.getGraphics();
	}

}
