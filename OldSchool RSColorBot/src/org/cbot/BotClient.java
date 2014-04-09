package org.cbot;

import java.applet.Applet;
import java.awt.Canvas;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;

public class BotClient {

	private Applet applet;
	private Canvas canvas;

	public void setAppletAndCanvas() {
		try {
			BotStub stub = new BotStub();
			URL jarFileUrl = new URL("jar:" + Bot.WORLD + "/" + stub.getParameter("archive") + "!/");
			JarURLConnection jarConn = (JarURLConnection) jarFileUrl.openConnection();
			URLClassLoader loader = new URLClassLoader(new URL[] { jarConn.getJarFileURL() });
			applet = (Applet) loader.loadClass(stub.getParameter("code").trim().replaceAll(".class", "")).newInstance();
			applet.setStub(stub);
			applet.init();
			applet.start();
			applet.setPreferredSize(Bot.SIZE);
			loader.close();
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
