package org.cbot;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class BotStub implements AppletStub {
	
	private Map<String, String> parameters = new HashMap<String, String>();
	private URL base;
	
	public BotStub() {
		try {
			base = new URL(Bot.WORLD);
			URLConnection con = base.openConnection();
			BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = null;
			while((line = reader.readLine()) != null) {
				if(line.contains("document.write('")) {
					String[] temp = line.split("'");
					String[] temp2 = temp[1].split("'");
					String[] temp3 = temp2[0].split("=");
					if(temp3.length == 2) {
						parameters.put(temp3[0], temp3[1]);
					} else if(temp3.length == 3) {
						String[] temp4 = temp3[1].split("\"");
						String[] temp5 = temp3[2].split("\"");
						parameters.put(temp4[1], temp5[1]);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void appletResize(int width, int height) { }

	@Override
	public AppletContext getAppletContext() {
		return null;
	}

	@Override
	public URL getCodeBase() {
		return base;
	}

	@Override
	public URL getDocumentBase() {
		return base;
	}

	@Override
	public String getParameter(String key) {
		return parameters.get(key);
	}

	@Override
	public boolean isActive() {
		return true;
	}

}
