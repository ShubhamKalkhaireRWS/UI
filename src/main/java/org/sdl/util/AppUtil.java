package org.sdl.util;

import static org.sdl.MainApp.appendToLogArea;

import java.text.SimpleDateFormat;
import java.util.Date;


public class AppUtil {

    public static <T> void log(Class<T> className, String msg) {
        String currentDateTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
        appendToLogArea(currentDateTime + " " + className.getName() + " - " + msg);

    }

   
}
