import javax.swing.*;
import javax.swing.text.DefaultStyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.undo.UndoManager;
class FileMenu extends JMenu {
    private Frame frame;
    private void saveAs(Frame frame){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save As");
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (fileToSave.exists()) {
                int result = JOptionPane.showConfirmDialog(this, "The file already exists. Do you want to replace it?", "File already exists", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.NO_OPTION) {
                    saveAs(frame);
                    return;
                }
            }
            String content = frame.getTextArea().getText();
            try (FileWriter writer = new FileWriter(fileToSave)) {
                writer.write(content);
            } catch (IOException e) {
                e.printStackTrace();
            }
            frame.setCurrentFile(fileToSave);
            frame.setTitle(frame.getCurrentFile().getName() + " - Whxpad");
            frame.setUnsavedChanges(false);
        }
    }
    private void save(Frame frame, File file){
        if (file == null) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new File("Untitled.txt"));
            if (frame.getCurrentFile() != null){
                frame.setTitle(frame.getCurrentFile().getName() + " - Whxpad");
            }
            int option = fileChooser.showSaveDialog(frame);
            if (option == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                try (FileWriter writer = new FileWriter(fileToSave)) {
                    writer.write(frame.getTextArea().getText());
                    frame.setCurrentFile(fileToSave);
                } catch (IOException f) {
                    f.printStackTrace();
                }
            }
        } else {
            try (FileWriter writer = new FileWriter(file.getPath())) {
                writer.write(frame.getTextArea().getText());
            } catch (IOException f) {
                f.printStackTrace();
            }
        }
        frame.setUnsavedChanges(false);
    }
    public FileMenu(Frame frame) {
        super("File");
        this.frame = frame;
        setFont(new Font("Consolas", Font.BOLD, 20));
        setForeground(Color.LIGHT_GRAY);

        // New Item
        JMenuItem newItem = new JMenuItem("New");
        add(newItem);
        newItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextArea textArea = frame.getTextArea();
                if (textArea.getText().isEmpty()) {
                    // If the text area is already empty, just clear it and return
                    textArea.setText("");
                    return;
                }
                if (frame.getUnsavedChanges()){
                    int option = JOptionPane.showConfirmDialog(frame, "Do you want to save the changes?", "Save Changes", JOptionPane.YES_NO_CANCEL_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        // If the user wants to save the changes, call the save method and then clear the text area
                        File currentFile = frame.getCurrentFile();

                        if (currentFile != null) {
                            save(frame, currentFile);
                            textArea.setText("");
                            frame.setCurrentFile(null);
                        } else {
                            saveAs(frame);
                            textArea.setText("");
                        }
                        frame.setUnsavedChanges(false);
                    } else if (option == JOptionPane.NO_OPTION) {
                        // If the user does not want to save the changes, just clear the text area
                        textArea.setText("");
                        frame.setCurrentFile(null);
                        frame.setUnsavedChanges(false);
                    }
                }else {
                    textArea.setText("");
                    frame.setCurrentFile(null);
                    frame.setUnsavedChanges(false);
                }
            }
        });
        newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));

        // Open item
        JMenuItem openItem = new JMenuItem("Open");
        add(openItem);
        openItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int option = fileChooser.showOpenDialog(null);
                if (option == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    frame.setCurrentFile(file);
                    frame.setTitle(frame.getCurrentFile().getName() + " - Whxpad");
                    try {
                        String content = FileHandler.openFile(file);
                        frame.getTextArea().setText(content);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));

        // Save Item
        JMenuItem saveItem = new JMenuItem("Save");
        add(saveItem);
        saveItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save(frame, frame.getCurrentFile());
            }
        });
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));

        // Save As Item
        JMenuItem saveAsItem = new JMenuItem("Save As...");
        add(saveAsItem);
        saveAsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAs(frame);
            }
        });
        add(new JSeparator());

        // Exit Item
        JMenuItem exitItem = new JMenuItem("Exit");
        add(exitItem);

        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (frame.getUnsavedChanges()){
                    int option = JOptionPane.showConfirmDialog(frame, "Do you want to save the changes?", "Save Changes", JOptionPane.YES_NO_CANCEL_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        // If the user wants to save the changes, call the save method and then clear the text area
                        File currentFile = frame.getCurrentFile();

                        if (currentFile != null) {
                            save(frame, currentFile);
                            frame.setCurrentFile(null);
                        } else {
                            saveAs(frame);
                        }
                        frame.setUnsavedChanges(false);
                    } else if (option == JOptionPane.NO_OPTION) {
                        // If the user does not want to save the changes, just clear the text area
                        frame.setCurrentFile(null);
                        frame.setUnsavedChanges(false);
                    }
                }
                System.exit(0);
            }
        });

        for(int i=0; i<this.getItemCount();i++){
            JMenuItem item = this.getItem(i);
            if (item != null) {
                item.setFont(new Font("Monospaced", Font.BOLD, 16));
                item.setOpaque(true);
            }
        }

    }
}

class EditMenu extends JMenu {
    public EditMenu(Frame frame) {
        super("Edit");
        setFont(new Font("Consolas", Font.BOLD, 20));
        setForeground(Color.LIGHT_GRAY);

        UndoManager undoManager = new UndoManager();
        frame.getTextArea().getDocument().addUndoableEditListener(undoManager);

        // Cut Item
        JMenuItem cutItem = new JMenuItem("Cut");
        add(cutItem);
        cutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.getTextArea().cut();
            }
        });
        cutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));


        // Copy Item
        JMenuItem copyItem = new JMenuItem("Copy");
        add(copyItem);
        copyItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.getTextArea().copy();
            }
        });
        copyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));

        // Paste Item
        JMenuItem pasteItem = new JMenuItem("Paste");
        add(pasteItem);
        pasteItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.getTextArea().paste();
            }
        });
        pasteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK));

        add(new JSeparator());

        // Undo Item
        JMenuItem undoItem = new JMenuItem("Undo");
        add(undoItem);
        undoItem.addActionListener(e -> {
            if (undoManager.canUndo()) {
                undoManager.undo();
            }
        });
        undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));

        // Redo Item
        JMenuItem redoItem = new JMenuItem("Redo");
        add(redoItem);
        redoItem.addActionListener(e -> {
            if (undoManager.canRedo()) {
                undoManager.redo();
            }
        });
        redoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK));

        add(new JSeparator());

        // Select All Item
        JMenuItem selectAllItem = new JMenuItem("Select All");
        add(selectAllItem);
        selectAllItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.getTextArea().selectAll();
            }
        });
        selectAllItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));

        for(int i=0; i<this.getItemCount();i++){
            JMenuItem item = this.getItem(i);
            if (item != null) {
                item.setFont(new Font("Monospaced", Font.BOLD, 16));
                item.setOpaque(true);
            }
        }
    }
}

class HelpMenu extends JMenu {
    public HelpMenu() {
        super("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        add(aboutItem);
        aboutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("https://www.github.com/burakarslan8/whxpad"));
                } catch (IOException | URISyntaxException ex) {
                    ex.printStackTrace();
                }
            }
        });

        setFont(new Font("Consolas", Font.BOLD, 20));
        setForeground(Color.LIGHT_GRAY);
        for(int i=0; i<this.getItemCount();i++){
            JMenuItem item = this.getItem(i);
            if (item != null) {
                item.setFont(new Font("Monospaced", Font.BOLD, 16));
                item.setOpaque(true);
            }
        }
    }
}