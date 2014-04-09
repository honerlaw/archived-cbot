package org.cbot.script;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.cbot.Bot;

public class ScriptManager extends Thread {
	
	private static Map<String, Script> scripts;
	private static ScheduledExecutorService service;
	
	static {		
		scripts = new HashMap<String, Script>();
		service = Executors.newScheduledThreadPool(1);
	}
	
	public static void load() {
		scripts.clear();
		File file = new File("./scripts/");
		try {
			URLClassLoader loader = new URLClassLoader(new URL[] { file.toURI().toURL() });
			for(String s : file.list()) {
				if(s.contains(".class")) {
					Class<?> clazz = loader.loadClass(s.replace(".class", ""));
					if(clazz.getSuperclass() == Script.class) {
						Script script = (Script) clazz.newInstance();
						scripts.put(script.getName(), script);
					}
					/*if(clazz instanceof Script.) {
						Script script = (Script) clazz;
						scripts.put(script.getName(), script);
					}*/
				}
			}
			loader.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Bot.getSingleton().getBotMenu().updateScriptMenu(scripts);
	}
	
	public static boolean startRunningScript(Script script) {
		if(isScriptRunning()) {
			getRunningScript().change();
		}
		if(!script.isActive()) {
			if(script.start()) {
				submit(script);
				return true;
			}
		}
		return false;
	}
	
	public static boolean isScriptRunning() {
		Collection<Script> s = scripts.values();
		for(Script script : s) {
			if(script.isActive())
				return true;
		}
		return false;
	}
	
	public static Script getRunningScript() {
		Collection<Script> s = scripts.values();
		for(Script script : s) {
			if(script.isActive())
				return script;
		}
		return null;
	}
	
	public static void submit(Script script) {
		getService().schedule(script, 0, TimeUnit.MILLISECONDS);
	}
	
	public static Map<String, Script> getScripts() {
		return scripts;
	}
	
	public static ScheduledExecutorService getService() {
		return service;
	}
	
}
