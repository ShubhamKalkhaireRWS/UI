package org.sdl.ui;

import javax.swing.*;
import java.awt.*;
import java.io.OutputStream;
import java.io.PrintStream;

public class MainFrame extends JFrame {
    private JTextField urlField;
    private JButton executeButton;
    private JTextArea logArea;
    private JLabel label;

    public MainFrame() {
        initComponents();
        setTitle("Accessibility App");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Launch in full screen
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void initComponents() {
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        label = new JLabel("Enter URL: ");


        gbc.insets = new Insets(50, 20, 50, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        urlField = new JTextField(100);
        executeButton = new JButton("Execute");

        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(label, gbc);

        gbc.gridx = 1;
        contentPanel.add(urlField, gbc);

        gbc.gridx = 2;
        contentPanel.add(executeButton, gbc);

        gbc.gridx = 3;

        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(50, 20, 50, 20);
        gbc2.fill = GridBagConstraints.HORIZONTAL;

        logArea = new JTextArea(20, 50);
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(contentPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        getContentPane().add(mainPanel);

    }

    public void enableExecuteButton() {
        executeButton.setEnabled(true);
    }

    public JTextField getUrlField() {
        return urlField;
    }

    public JButton getExecuteButton() {
        return executeButton;
    }

    public JTextArea getLogArea() {
        return logArea;
    }

    private void redirectSystemStreams() {
        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) {
                updateTextArea(String.valueOf((char) b));
            }

            @Override
            public void write(byte[] b, int off, int len) {
                updateTextArea(new String(b, off, len));
            }

            @Override
            public void write(byte[] b) {
                write(b, 0, b.length);
            }
        };

        System.setOut(new PrintStream(out, true));
        System.setErr(new PrintStream(out, true));
    }

    private void updateTextArea(final String text) {
        SwingUtilities.invokeLater(() -> logArea.append(text));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame();
            }
        });
    }
}
