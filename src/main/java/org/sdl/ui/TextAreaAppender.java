package org.sdl.ui;
//
//import org.apache.log4j.AppenderSkeleton;
//import org.apache.log4j.spi.LoggingEvent;

import javax.swing.*;

public class TextAreaAppender {
//extends AppenderSkeleton {
    private JTextArea logArea;

    public TextAreaAppender(JTextArea logArea) {
        this.logArea = logArea;
    }

//    @Override
//    protected void append(LoggingEvent event) {
//        logArea.append(event.getRenderedMessage() + "\n");
//    }
//
//    @Override
//    public void close() {
//        // Nothing to close
//    }
//
//    @Override
//    public boolean requiresLayout() {
//        return false;
//    }
}
