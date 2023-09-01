/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package csc.pkg104;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class CSC104 extends JFrame implements ActionListener, WindowListener {
    private JButton openButton;
    private JButton saveButton;
    private JTextArea textArea;
    private JLabel imageLabel;

    public CSC104() {
        setTitle("File Handling");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create components
        openButton = new JButton("Open File");
        saveButton = new JButton("Save File");
        textArea = new JTextArea();
        imageLabel = new JLabel();

        // Add components to content pane
        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        container.add(openButton, BorderLayout.NORTH);
        container.add(saveButton, BorderLayout.SOUTH);
        container.add(new JScrollPane(textArea), BorderLayout.CENTER);
        container.add(imageLabel, BorderLayout.WEST);

        // Register event listeners
        openButton.addActionListener(this);
        saveButton.addActionListener(this);
        addWindowListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == openButton) {
            openFile();
        } else if (e.getSource() == saveButton) {
            saveFile();
        }
    }

    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text and Image files", "txt", "jpg", "jpeg", "png", "gif"));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String extension = getFileExtension(file);
            if (extension != null) {
                if (extension.equals("txt")) {
                    openTextFile(file);
                } else if (extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png") || extension.equals("gif")) {
                    openImageFile(file);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Unsupported file format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void openTextFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            textArea.setText(content.toString());
            imageLabel.setIcon(null);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error reading text file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openImageFile(File file) {
        try {
            Image image = ImageIO.read(file);
            if (image != null) {
                imageLabel.setIcon(new ImageIcon(image));
                textArea.setText("");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error opening image file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error reading image file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text and Image files", "txt", "jpg", "jpeg", "png", "gif"));
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String extension = getFileExtension(file);
            if (extension != null) {
                if (extension.equals("txt")) {
                    saveTextFile(file);
                } else if (extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png") || extension.equals("gif")) {
                    saveImageFile(file);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Unsupported file format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveTextFile(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file));
             PrintWriter printWriter = new PrintWriter(writer)) {
            printWriter.print(textArea.getText());
            JOptionPane.showMessageDialog(this,
                    "Text file saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error saving text file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveImageFile(File file) {
        // Get the image icon from the label
        Icon icon = imageLabel.getIcon();
        if (icon instanceof ImageIcon) {
            Image image = ((ImageIcon) icon).getImage();
            try {
                BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
                Graphics2D g2d = bufferedImage.createGraphics();
                g2d.drawImage(image, 0, 0, null);
                g2d.dispose();
                String format = getFileExtension(file);
                if (format != null) {
                    ImageIO.write(bufferedImage, format, file);
                    JOptionPane.showMessageDialog(this,
                            "Image file saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Unsupported image format.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error saving image file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "No image to save.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getFileExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex >= 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }
        return null;
    }

    // WindowListener interface methods
    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to exit?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            System.exit(0);
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CSC104 gui = new CSC104();
            gui.setVisible(true);
        });
    }
}
