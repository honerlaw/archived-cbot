package org.cbot;

import java.applet.Applet;
import java.awt.Canvas;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class BotClient {

	private Applet applet;
	private Canvas canvas;

	public void setAppletAndCanvas() {
		try {
			BotStub stub = new BotStub();
			
			ReadableByteChannel ch = Channels.newChannel(new URL(Bot.WORLD + "/" + stub.getParameter("archive")).openStream());
			FileOutputStream fos = new FileOutputStream("client.jar");
			fos.getChannel().transferFrom(ch, 0, Long.MAX_VALUE);
			
			URLClassLoader loader = new URLClassLoader(new URL[] { new File("client.jar").toURI().toURL() });
			
			JarFile file = new JarFile("client.jar");
			Enumeration<JarEntry> en = file.entries();
			while(en.hasMoreElements()) {
				JarEntry entry = en.nextElement();
				if(entry.getName().contains(".class")) {
					loader.loadClass(entry.getName().replace(".class", ""));
				}
			}
			
			/*URL jarFileUrl = new URL("jar:" + Bot.WORLD + "/" + stub.getParameter("archive") + "!/");
			JarURLConnection jarConn = (JarURLConnection) jarFileUrl.openConnection();
			URLClassLoader loader = new URLClassLoader(new URL[] { jarConn.getJarFileURL() });*/
			applet = (Applet) loader.loadClass(stub.getParameter("code").trim().replaceAll(".class", "")).newInstance();
			applet.setStub(stub);
			applet.init();
			applet.start();
			applet.setPreferredSize(Bot.SIZE);
			loader.close();
			file.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public Applet getApplet() {
		return applet;
	}
	
	public Canvas getCanvas() {
		return canvas;
	}
	
	public void setCanvas(Canvas canvas) {
		this.canvas = canvas;
	}
	
}
