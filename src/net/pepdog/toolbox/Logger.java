package net.pepdog.toolbox;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;

import net.pepdog.main.Main;

public class Logger {
	
	private static String completeLog = "";
	
	public enum LogLevel {
		INFO,
		WARN,
		ERROR
	}
	
	public static void log(LogLevel level, Object message) {
		String toLog = getCurrentTime() + " [" + level.name() + "] "  + "[" + getCallerClassName() + "] " + message.toString();
		completeLog += toLog + "\r\n";
		
		if(level == LogLevel.ERROR) {
			System.err.println(toLog);
		} else {
			System.out.println(toLog);
		}
	}
	
	public static void saveLog() {
		File saveDirectory =  new File(Main.getPEPDOGDir().getPath()+"/logs/");
		if (!saveDirectory.exists()) {
	        try {
	        	saveDirectory.mkdir();
	        } catch (SecurityException e) {
	            System.out.println("nothing");
	        }
	    }
	    
		String path = saveDirectory +"/"+ getCurrentTimeFile() +".log";
		System.out.println(path);
	    File logFile = new File(path);
	    try {
	    	logFile.createNewFile();
	    	FileWriter logWriter = new FileWriter(path);
	    	
			logWriter.write(completeLog);
			logWriter.close();
	    } catch(IOException e)  {
	    	StringWriter ps = new StringWriter();
	    	e.printStackTrace(new PrintWriter(ps));
	    	System.out.println(ps.toString());
	    }
	}
	
	/* https://stackoverflow.com/questions/11306811/how-to-get-the-caller-class-in-java - Denys Seguret */
	/**
	 * returns who ran the function.
	 * @return class name
	 */
	private static String getCallerClassName() { 
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        for(int i = 1; i < stElements.length; i++) {
            StackTraceElement ste = stElements[i];
            if(!ste.getClassName().equals(Logger.class.getName()) && ste.getClassName().indexOf("java.lang.Thread") != 0) {
            	String[] res = ste.getClassName().split("\\.");
                return res[res.length-1];
            }
        }
        return null;
     }
	@SuppressWarnings("unused")
	private static String getCallerOfCallerClassName() { 
	    StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
	    String callerClassName = null;
	    for(int i = 1; i < stElements.length; i++) {
	        StackTraceElement ste = stElements[i];
	        if(!ste.getClassName().equals(Logger.class.getName()) && ste.getClassName().indexOf("java.lang.Thread") != 0) {
	            if(callerClassName == null) {
	                callerClassName = ste.getClassName();
	            } else if (!callerClassName.equals(ste.getClassName())) {
	                return ste.getClassName();
	            }
	        }
	    }
	    return null;
	 }
	
	/**
	 * returns currentTime in the form of [YYYY-MM-DD HH:MM:SS]
	 * @return string
	 */
	private static String getCurrentTime() {
		Calendar cal = Calendar.getInstance();
		
		int dy = cal.get(Calendar.DAY_OF_MONTH);
		int mon = cal.get(Calendar.MONTH) + 1;
		
		int hr = cal.get(Calendar.HOUR_OF_DAY);
		int min = cal.get(Calendar.MINUTE);
		int sec = cal.get(Calendar.SECOND);
		
		String day = "" + dy;
		String month = "" + mon;
		
		String hour = "" + hr;
		String minute = "" + min;
		String second = "" + sec;
		
		if(dy < 10) { day = "0" + dy; }
		if(mon < 10) { month = "0" + mon; }
		
		if(hr < 10) { hour = "0" + hr; }
		if(min < 10) { minute = "0" + min; }
		if(sec < 10) { second = "0" + sec; }
		
		String date = cal.get(Calendar.YEAR)+ "-" + month + "-" + day;
		String time = hour + ":" + minute + ":" + second;

		return "[" + date + " " + time + "]";
	}
	/**
	 * returns currentTime in the form of YYYY-MM-DD_HH.MM.SS
	 * @return
	 */
	private static String getCurrentTimeFile() {
		Calendar cal = Calendar.getInstance();
		
		int dy = cal.get(Calendar.DAY_OF_MONTH);
		int mon = cal.get(Calendar.MONTH) + 1;
		
		int hr = cal.get(Calendar.HOUR_OF_DAY);
		int min = cal.get(Calendar.MINUTE);
		int sec = cal.get(Calendar.SECOND);
		
		String day = "" + dy;
		String month = "" + mon;
		
		String hour = "" + hr;
		String minute = "" + min;
		String second = "" + sec;
		
		if(dy < 10) { day = "0" + dy; }
		if(mon < 10) { month = "0" + mon; }
		
		if(hr < 10) { hour = "0" + hr; }
		if(min < 10) { minute = "0" + min; }
		if(sec < 10) { second = "0" + sec; }
		
		String date = cal.get(Calendar.YEAR)+ "-" + month + "-" + day;
		String time = hour + "." + minute + "." + second;

		return date + "_" + time;
	}
}
