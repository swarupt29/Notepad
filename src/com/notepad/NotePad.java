package com.notepad; // Package declaration

import java.awt.Dimension;
import java.awt.EventQueue; // Import the EventQueue class for handling events
import java.awt.Font; // Import the Font class for setting fonts
import java.awt.Image;
import java.awt.event.ActionEvent; // Import for handling ActionEvents
import java.awt.event.ActionListener; // ActionListener interface for event handling
import java.awt.datatransfer.StringSelection; // Import for copying text to the clipboard
import java.awt.datatransfer.Transferable; // Transferable interface for clipboard data
import java.io.BufferedWriter; // Import for writing to files
import java.io.FileWriter; // Import for file writing
import java.io.IOException; // Import for handling exceptions
import javax.swing.JEditorPane; // Import for a text editor component
import javax.swing.JFileChooser; // Import for file chooser dialog
import javax.swing.JFrame; // Import for the main application window
import javax.swing.JMenuBar; // Import for the menu bar
import javax.swing.JMenuItem; // Import for menu items
import javax.swing.JMenu; // Import for menus
import javax.swing.JOptionPane; // Import for displaying dialogs
import javax.swing.JScrollPane; // Import for adding a scroll pane
import javax.swing.JTextArea;
import javax.swing.ImageIcon;
import java.awt.Toolkit;

public class NotePad {

    private JFrame frmNotepad; // Main application window
    private JMenuBar menuBar; // Menu bar at the top
    private JMenu menuFile; // File menu
    private JMenu menuEdit; // Edit menu
    private JEditorPane editorPane; // Text editor component
    private String currentFilePath; // Store the path of the current saved file;
    private JMenu fontSizeMenu; // Font size submenu

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    NotePad window = new NotePad();
                    window.frmNotepad.setVisible(true); // Display the main window
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public NotePad() {
        initialize();
    }

    private void initialize() {
    	frmNotepad = new JFrame();
    	frmNotepad.setTitle("Notepad");
    	frmNotepad.setResizable(true); // Allow resizing of the window
    	frmNotepad.setBounds(100, 100, 800, 600); // Set the initial window size and position
    	frmNotepad.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close the application on window close

        // Create a JScrollPane to enable scrolling for the editorPane
        JScrollPane scrollPane = new JScrollPane();
        frmNotepad.getContentPane().add(scrollPane); // Add the scroll pane to the main window

        editorPane = new JEditorPane(); // Create a text editor component
        scrollPane.setViewportView(editorPane); // Set the editorPane as the view of the scrollPane

        menuBar = new JMenuBar(); // Create a menu bar
        menuBar.setFont(new Font("Segoe UI", Font.PLAIN, 15)); // Set the font for the menu bar
        frmNotepad.setJMenuBar(menuBar); // Set the menu bar in the frame

        menuFile = new JMenu("File"); // Create a "File" menu
        menuFile.setFont(new Font("Trebuchet MS", Font.PLAIN, 15)); // Set the font for the menu
        menuBar.add(menuFile); // Add the "File" menu to the menu bar

        // Create an "Open" menu item
        JMenuItem menuItemOpen = new JMenuItem("Open");
        menuItemOpen.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
        menuFile.add(menuItemOpen);
        menuItemOpen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openFile();
            }

            private void openFile() {
                // TODO Auto-generated method stub
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(frmNotepad);

                if (result == JFileChooser.APPROVE_OPTION) {
                    try {
                        String fileName = fileChooser.getSelectedFile().getAbsolutePath();
                        currentFilePath = fileName; // Update the current file path
                        editorPane.setText(""); // Clear the editor content

                        // Read the content from the selected file and set it in the editor
                        java.nio.file.Path filePath = java.nio.file.Paths.get(fileName);
                        String fileContent = new String(java.nio.file.Files.readAllBytes(filePath));
                        editorPane.setText(fileContent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // Create a "Save" menu item
        JMenuItem menuItemSave = new JMenuItem("Save");
        menuItemSave.setFont(new Font("Trebuchet MS", Font.PLAIN, 15)); // Set the font for the menu item
        menuFile.add(menuItemSave); // Add the "Save" menu item to the "File" menu
        menuItemSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveFile(); // Call the saveFile method when clicked
            }
        });

        // Create a "Save As" menu item
        JMenuItem menuItemSaveAs = new JMenuItem("Save As");
        menuItemSaveAs.setFont(new Font("Trebuchet MS", Font.PLAIN, 15)); // Set the font for the menu item
        menuFile.add(menuItemSaveAs); // Add the "Save As" menu item to the "File" menu
        menuItemSaveAs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveFileAs(); // Call the saveFileAs method when clicked
            }
        });

        // Create an "Exit" menu item
     // Create an "Exit" menu item
        JMenuItem menuItemExit = new JMenuItem("Exit");
        menuItemExit.setFont(new Font("Trebuchet MS", Font.PLAIN, 15)); // Set the font for the menu item
        menuFile.add(menuItemExit); // Add the "Exit" menu item to the "File" menu
        menuItemExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(frmNotepad, "Are you sure you want to exit?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    System.exit(0); // Exit the application when "Yes" is clicked
                }
            }
        });



        menuEdit = new JMenu("Edit"); // Create an "Edit" menu
        menuEdit.setFont(new Font("Trebuchet MS", Font.PLAIN, 15)); // Set the font for the menu
        menuBar.add(menuEdit); // Add the "Edit" menu to the menu bar

        // Create a "Copy" menu item
        JMenuItem menuItemCopy = new JMenuItem("Copy");
        menuItemCopy.setFont(new Font("Trebuchet MS", Font.PLAIN, 15)); // Set the font for the menu item
        menuEdit.add(menuItemCopy); // Add the "Copy" menu item to the "Edit" menu
        menuItemCopy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                copyText(); // Call the copyText method when clicked
            }
        });

        // Create a "Cut" menu item
        JMenuItem menuItemCut = new JMenuItem("Cut");
        menuItemCut.setFont(new Font("Trebuchet MS", Font.PLAIN, 15)); // Set the font for the menu item
        menuEdit.add(menuItemCut); // Add the "Cut" menu item to the "Edit" menu
        menuItemCut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cutText(); // Call the cutText method when clicked
            }
        });

        // Create a "Paste" menu item
        JMenuItem menuItemPaste = new JMenuItem("Paste");
        menuItemPaste.setFont(new Font("Trebuchet MS", Font.PLAIN, 15)); // Set the font for the menu item
        menuEdit.add(menuItemPaste); // Add the "Paste" menu item to the "Edit" menu
        menuItemPaste.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pasteText(); // Call the pasteText method when clicked
            }
        });

        fontSizeMenu = new JMenu("Size"); // Create a "Size" submenu
        fontSizeMenu.setFont(new Font("Trebuchet MS", Font.PLAIN, 15)); // Set the font for the submenu
        menuEdit.add(fontSizeMenu); // Add the "Size" submenu to the "Edit" menu

        JMenu menuAbout = new JMenu("About"); // Create an "About" menu
        menuAbout.setFont(new Font("Trebuchet MS", Font.PLAIN, 15)); // Set the font for the menu
        menuBar.add(menuAbout); // Add the "About" menu to the menu bar

        // Add an ActionListener to show the "About" dialog with a random description when clicked
        JMenuItem menuItemAbout = new JMenuItem("About");
        menuItemAbout.setFont(new Font("Trebuchet MS", Font.PLAIN, 15)); // Set the font for the menu item
        menuAbout.add(menuItemAbout); // Add the "About" menu item to the "About" menu
        menuItemAbout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showAboutDialog(); // Call the showAboutDialog method when clicked
            }
        });

        JMenuItem menuItemZoomIn = new JMenuItem("Zoom In");
        menuItemZoomIn.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
        menuEdit.add(menuItemZoomIn);
        menuItemZoomIn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                zoomIn();
            }

            private void zoomIn() {
                // TODO Auto-generated method stub
                Font currentFont = editorPane.getFont();
                int newSize = currentFont.getSize() + 2; // Increase font size by 2 points
                editorPane.setFont(currentFont.deriveFont((float) newSize));
            }
        });

        JMenuItem menuItemZoomOut = new JMenuItem("Zoom Out");
        menuItemZoomOut.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
        menuEdit.add(menuItemZoomOut);
        menuItemZoomOut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                zoomOut();
            }

            private void zoomOut() {
                // TODO Auto-generated method stub
                Font currentFont = editorPane.getFont();
                int newSize = currentFont.getSize() - 2; // Decrease font size by 2 points
                editorPane.setFont(currentFont.deriveFont((float) newSize));
            }
        });

        createFontSizeSubMenu(); // Create the font size submenu
    }

    private Image getImage(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	private void createFontSizeSubMenu() {
        Integer[] fontSizes = { 8, 10, 12, 14, 16, 18, 20, 24, 28, 32, 36, 40 }; // Array of font sizes
        for (Integer fontSize : fontSizes) {
            JMenuItem menuItem = new JMenuItem(fontSize.toString()); // Create a menu item for each font size
            menuItem.setFont(new Font("Trebuchet MS", Font.PLAIN, 15)); // Set the font for the menu item
            fontSizeMenu.add(menuItem); // Add the font size menu item to the font size submenu
            menuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    changeFontSize(fontSize); // Call the changeFontSize method with the selected font size
                }
            });
        }
    }

    private void changeFontSize(int fontSize) {
        editorPane.setFont(new Font(editorPane.getFont().getName(), editorPane.getFont().getStyle(), fontSize)); // Change the font size of the editorPane
    }

    private void saveFile() {
        if (currentFilePath == null) {
            // If the file hasn't been saved yet, use "Save As"
            saveFileAs();
        } else {
            // Save the content to the current file
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(currentFilePath)); // Create a writer for the file
                writer.write(editorPane.getText()); // Write the text from the editorPane to the file
                writer.close(); // Close the writer
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveFileAs() {
        JFileChooser fileChooser = new JFileChooser(); // Create a file chooser dialog
        int result = fileChooser.showSaveDialog(frmNotepad); // Show the "Save" dialog

        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                String fileName = fileChooser.getSelectedFile().getAbsolutePath(); // Get the selected file path
                if (!fileName.endsWith(".txt")) {
                    fileName += ".txt"; // Ensure the file has a .txt extension
                }
                BufferedWriter writer = new BufferedWriter(new FileWriter(fileName)); // Create a writer for the file
                writer.write(editorPane.getText()); // Write the text from the editorPane to the file
                writer.close(); // Close the writer
                currentFilePath = fileName; // Set the current file path
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void copyText() {
        String selectedText = editorPane.getSelectedText(); // Get the selected text
        if (selectedText != null) {
            Transferable clipboardContents = new StringSelection(selectedText); // Create a clipboard transferable with the selected text
            java.awt.datatransfer.Clipboard clipboard = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard(); // Get the system clipboard
            clipboard.setContents(clipboardContents, null); // Set the clipboard contents to the selected text
        }
    }

    private void cutText() {
        copyText(); // Copy selected text to clipboard
        editorPane.replaceSelection(""); // Delete selected text
    }

    private void pasteText() {
        java.awt.datatransfer.Clipboard clipboard = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard(); // Get the system clipboard
        Transferable clipboardContents = clipboard.getContents(this); // Get clipboard contents
        if (clipboardContents != null && clipboardContents.isDataFlavorSupported(java.awt.datatransfer.DataFlavor.stringFlavor)) {
            try {
                String pastedText = (String) clipboardContents.getTransferData(java.awt.datatransfer.DataFlavor.stringFlavor); // Get pasted text from clipboard
                editorPane.replaceSelection(pastedText); // Replace the selected text with the pasted text
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Method to show the "About" dialog with a random description
    private void showAboutDialog() {
        String[] descriptions = {
            "Project Description:",
            "This project is a Java-based Notepad application that provides basic text editing and file management functionalities. It offers features like creating, editing, saving, and opening text documents. Users can also copy, cut, and paste text within the application. Additionally, the application allows users to change the font size for better text readability.",
            "",
            "Created by Swarup Thamke.",
            "GitHub: https://github.com/swarupt29"
        };

        StringBuilder aboutMessage = new StringBuilder();
        for (String line : descriptions) {
            aboutMessage.append(line).append("\n");
        }

        JTextArea textArea = new JTextArea(aboutMessage.toString());
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setOpaque(false);
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 180));

        JOptionPane.showMessageDialog(
            frmNotepad,
            scrollPane,
            "About",
            JOptionPane.PLAIN_MESSAGE
        );
    }
}