package org.sdl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.sdl.task.SeleniumHandler;
import org.sdl.ui.MainFrame;
import org.sdl.util.AppUtil;

import com.google.common.util.concurrent.ListeningScheduledExecutorService;

public class MainApp {
	
	public static MainFrame mainFrame = new MainFrame();
	private static ListeningScheduledExecutorService executor;
	public static Future<?> taskFuture;
	
	
	public static void main(String[] args) {

		SwingUtilities.invokeLater(() -> { 
			// TextAreaAppender appender = new TextAreaAppender(mainFrame.getLogArea()); 
			// logger.addAppender(appender);

			// displayLogFileContents(mainFrame.getLogArea(), "logs/application.log");

			JButton executeButton = mainFrame.getExecuteButton();
			JTextField urlField = mainFrame.getUrlField();

			SeleniumHandler seleniumHandler = new SeleniumHandler(mainFrame);

			executeButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					executeButton.setEnabled(false); // Disable button during execution String
					String url = urlField.getText();
					seleniumHandler.setUrl(url);
					seleniumHandler.executeInBackground();
				}
			});

		});

	
	}

	private static void displayLogFileContents(JTextArea logArea, String filePath) {
		File logFile = new File(filePath);
		logArea.setText("");
		if (logFile.exists()) {
			try (BufferedReader reader = new BufferedReader(
					new InputStreamReader(new FileInputStream(logFile), StandardCharsets.UTF_8))) {
				String line;
				while ((line = reader.readLine()) != null) {
					logArea.append(line + "\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			logArea.append("Log file not found.");
		}
	}

	public static void appendToLogArea(String msg) {
		JTextArea logArea = mainFrame.getLogArea();
		logArea.append("\n");
		logArea.append(msg);
	}
}
