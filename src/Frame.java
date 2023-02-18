import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.io.File;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Frame extends JFrame{
    private JTextArea textArea;
    private File currentFile;
    private boolean unsavedChanges;

    public JTextArea getTextArea() {
        return textArea;
    }
    public boolean getUnsavedChanges(){
        return unsavedChanges;
    }
    public File getCurrentFile() {
        return currentFile;
    }
    public void setCurrentFile(File file) {
        currentFile = file;
    }
    public void setUnsavedChanges(boolean unsavedChanges){
        this.unsavedChanges=unsavedChanges;
    }
    public class BackgroundMenuBar extends JMenuBar {
        Color bgColor=new Color(100,100,100);

        public void setColor(Color color) {
            bgColor=color;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(bgColor);
            g2d.fillRect(0, 0, getWidth() - 1, getHeight() - 1);

        }
    }
    public Frame(){
        super("Whxpad");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(840,600));
        setLayout(new BorderLayout());

        try {
            // Set the look and feel to Nimbus
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        textArea = new JTextArea();
        textArea.setFont(new Font("Monospaced", Font.BOLD, 16));
        textArea.setPreferredSize(new Dimension(800, 600));
        textArea.setForeground(Color.WHITE);
        textArea.setBackground(new Color(33, 32, 32));

        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                unsavedChanges = true;
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                unsavedChanges = true;
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                unsavedChanges = true;
            }
        });

        JScrollPane scrollPane = new JScrollPane(textArea);

        Border thickBorder = BorderFactory.createLineBorder(new Color(79, 78, 78),2,true);

        scrollPane.getVerticalScrollBar().setBorder(thickBorder);
        scrollPane.getHorizontalScrollBar().setBorder(thickBorder);

        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(58, 57, 57);
                this.trackColor = new Color(38, 38, 38);
                this.thumbDarkShadowColor = new Color(50, 50, 50);
                this.thumbHighlightColor = new Color(31, 29, 29, 255);
                this.thumbLightShadowColor = new Color(79, 78, 78);
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return new JButton() {
                    @Override
                    public Dimension getPreferredSize() {
                        return new Dimension(0, 0);
                    }
                };
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return new JButton() {
                    @Override
                    public Dimension getPreferredSize() {
                        return new Dimension(0, 0);
                    }
                };
            }
        });

        scrollPane.getHorizontalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(58, 57, 57);
                this.trackColor = new Color(38, 38, 38);
                this.thumbDarkShadowColor = new Color(50, 50, 50);
                this.thumbHighlightColor = new Color(31, 29, 29, 255);
                this.thumbLightShadowColor = new Color(79, 78, 78);
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return new JButton() {
                    @Override
                    public Dimension getPreferredSize() {
                        return new Dimension(0, 0);
                    }
                };
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return new JButton() {
                    @Override
                    public Dimension getPreferredSize() {
                        return new Dimension(0, 0);
                    }
                };
            }
        });

        scrollPane.getViewport().setBackground(Color.DARK_GRAY);
        scrollPane.setCorner(JScrollPane.LOWER_RIGHT_CORNER,null);
        scrollPane.setBorder(null);

        add(scrollPane,BorderLayout.CENTER);

        BackgroundMenuBar menuBar = new BackgroundMenuBar();
        menuBar.setOpaque(true);
        menuBar.setColor(new Color(50,50,50));
        menuBar.setBorder(thickBorder);
        menuBar.setBorderPainted(true);
        menuBar.add(new FileMenu(this));
        menuBar.add(new EditMenu(this));
        menuBar.add(new HelpMenu());

        setJMenuBar(menuBar);
        pack();
        setVisible(true);
    }

}

