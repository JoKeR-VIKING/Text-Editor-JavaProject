// package for the classes
package editor;

// importing all the required modules
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;

// TextEditor class which extends JFrame and ActionListener to show the GUI interface and event handling
public class TextEditor extends JFrame implements ActionListener
{
    // All declarations required to build interface
    JTextArea textArea;
    JPanel panel;
    JTextField textField;
    JMenuBar menuBar;
    JButton openButton, saveButton, searchButton, leftArrow, rightArrow;
    JMenuItem openMenu, saveMenu, exitMenu, startSearch, previousSearch, nextSearch, useRegularExpression;
    JCheckBox regexEnable;
    JFileChooser fileChooser;
    Vector<Integer> allIndexes; // Stores all the indexes matching the string/regex in text area
    Vector<String> matchedStrings; // Stores all the strings matching the regex in text area
    int key; // To iterate over the indexes array

    public TextEditor()
    {
        textArea = new JTextArea(); // The main text area to edit and enter text into
        panel = new JPanel(); // Panel to have all components in place of the frame
        menuBar = new JMenuBar(); // Menu bar with file and search options
        fileChooser = new JFileChooser(); // File Chooser to select and store files from/in the drive
        add(fileChooser);

        this.setTitle("Text Editor"); // Heading for the text editor GUI

        // Important to close the window on clicking close (else it will keep running till you use task manager or stop in IDE)
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Frame Dimensions and centering frame to screen
        this.setSize(600, 500);
        this.setLocationRelativeTo(null);

        // Adding image icons to button for interface and setting fonts
        openButton = new JButton(new ImageIcon("E:\\Text Editor\\Text Editor\\task\\src\\editor\\images\\open-folder.png"));
        saveButton = new JButton(new ImageIcon("E:\\Text Editor\\Text Editor\\task\\src\\editor\\images\\save-icon.png"));
        searchButton = new JButton(new ImageIcon("E:\\Text Editor\\Text Editor\\task\\src\\editor\\images\\search-icon.png"));
        leftArrow = new JButton(new ImageIcon("E:\\Text Editor\\Text Editor\\task\\src\\editor\\images\\left-icon.png"));
        rightArrow = new JButton(new ImageIcon("E:\\Text Editor\\Text Editor\\task\\src\\editor\\images\\right-icon.png"));
        regexEnable = new JCheckBox("Use regex");
        regexEnable.setFont(new Font("Arial", Font.PLAIN, 15));

        // Creating a text field to display the file name and search keywords
        textField = new JTextField();

        // Setting dimensions for buttons
        openButton.setBounds(10, 10, 30, 30);
        saveButton.setBounds(45, 10, 30, 30);
        textField.setBounds(80, 10, 295, 30);
        searchButton.setBounds(380, 10, 30, 30);
        leftArrow.setBounds(415, 10, 30, 30);
        rightArrow.setBounds(450, 10, 30, 30);
        regexEnable.setBounds(485, 10, 100, 30);

        // Adding all buttons to panel (Important to set constraints to null)
        panel.add(openButton, null);
        panel.add(saveButton, null);
        panel.add(textField, null);
        panel.add(searchButton, null);
        panel.add(leftArrow, null);
        panel.add(rightArrow, null);
        panel.add(regexEnable, null);

        // Adding scroll-pane for large text editing
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setLineWrap(true);
        textArea.setFont(new Font("Sans-Serif", Font.PLAIN, 18));

        scrollPane.setBounds(10, 50, 570, 380);
        panel.add(scrollPane, null);

        // Creating menus for file and search sections
        JMenu fileSection = new JMenu("File");
        JMenu searchSection = new JMenu("Search");
        menuBar.add(fileSection);
        menuBar.add(searchSection);
        fileSection.setName("MenuFile");
        searchSection.setName("MenuSearch");

        // Adding items to menu
        openMenu = new JMenuItem("Open");
        saveMenu = new JMenuItem("Save");
        exitMenu = new JMenuItem("Exit");

        startSearch = new JMenuItem("Start Search");
        previousSearch = new JMenuItem("Previous Search");
        nextSearch = new JMenuItem("Next Search");
        useRegularExpression = new JMenuItem("Use Regular Expressions");

        // Adding items to file section
        fileSection.add(openMenu);
        fileSection.add(saveMenu);
        fileSection.addSeparator();
        fileSection.add(exitMenu);

        // Adding items to search section
        searchSection.add(startSearch);
        searchSection.add(previousSearch);
        searchSection.add(nextSearch);
        searchSection.add(useRegularExpression);

        // Adding action listeners to all the buttons
        openButton.addActionListener(this);
        saveButton.addActionListener(this);
        openMenu.addActionListener(this);
        saveMenu.addActionListener(this);
        exitMenu.addActionListener(this);
        startSearch.addActionListener(this);
        previousSearch.addActionListener(this);
        nextSearch.addActionListener(this);
        useRegularExpression.addActionListener(this);
        searchButton.addActionListener(this);
        leftArrow.addActionListener(this);
        rightArrow.addActionListener(this);

        // Setting names for all the fields mentioned above
        textArea.setName("TextArea");
        textField.setName("SearchField");
        saveButton.setName("SaveButton");
        openButton.setName("OpenButton");
        scrollPane.setName("ScrollPane");
        searchButton.setName("StartSearchButton");
        leftArrow.setName("PreviousMatchButton");
        rightArrow.setName("NextMatchButton");
        regexEnable.setName("UseRegExCheckbox");
        fileChooser.setName("FileChooser");
        openMenu.setName("MenuOpen");
        saveMenu.setName("MenuSave");
        exitMenu.setName("MenuExit");
        startSearch.setName("MenuStartSearch");
        previousSearch.setName("MenuPreviousMatch");
        nextSearch.setName("MenuNextMatch");
        useRegularExpression.setName("MenuUseRegExp");

        // setting panel layout to null for removing default border layout
        panel.setLayout(null);

        // Adding panel to frame, setting menubar and setting frame as visible
        this.add(panel);
        this.setJMenuBar(menuBar);
        this.setVisible(true); // add this to end to make sure everything added to screen is visible after painting
    }

    public void actionPerformed(ActionEvent ae)
    {
        File file; // File instance

        // Action Listener for save button
        if (ae.getSource() == saveButton || ae.getSource() == saveMenu)
        {
            // open dialog box for file chooser
            int r = fileChooser.showOpenDialog(null);

            // if opened file
            if (r == fileChooser.APPROVE_OPTION)
            {
                file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                textField.setText(file.getName());

                try
                {
                    // Creating file writers
                    FileWriter fw = new FileWriter(file);
                    BufferedWriter bw = new BufferedWriter(fw);

                    // Writing text area to file and closing it
                    bw.write(textArea.getText());
                    bw.flush();
                    bw.close();
                }
                catch (Exception e)
                {
                    JOptionPane.showMessageDialog(this, "Cannot save without file name");
                }
            }
        }
        // Action Listener for open button
        else if (ae.getSource() == openButton || ae.getSource() == openMenu)
        {
            // open dialog box for file chooser
            int r = fileChooser.showOpenDialog(null);

            // if opened file
            if (r == fileChooser.APPROVE_OPTION)
            {
                file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                textField.setText(file.getName());

                try
                {
                    if (file.createNewFile()) // if no file with name exists create new one
                        textArea.setText(""); // Setting text area to empty when loading new file

                    // Creating file readers and pasting them in text area
                    FileReader fr = new FileReader(file);
                    int ch;
                    String completeText = "";

                    // important to not use buffered readLine() function and read byte by byte
                    while ((ch = fr.read()) != -1)
                        completeText += (char)ch;

                    // Setting text area to whole file string
                    textArea.setText(completeText);
                }
                catch (Exception e)
                {
                    JOptionPane.showMessageDialog(this, "File Not Found");
                }
            }
        }
        // Action Listener for regex menu button
        else if (ae.getSource() == useRegularExpression)
        {
            regexEnable.setSelected(true);
        }
        // Action Listener for search button
        else if (ae.getSource() == searchButton || ae.getSource() == startSearch)
        {
            String searchString = textField.getText(); // Pattern or string to find
            // initialising both vectors
            allIndexes = new Vector<>();
            matchedStrings = new Vector<>();

            // if regex matching pattern or else matching string
            if (regexEnable.isSelected())
            {
                Pattern pattern = Pattern.compile(searchString);
                Matcher matcher = pattern.matcher(textArea.getText());

                // Matching till no other matches are available
                while (matcher.find())
                {
                    allIndexes.add(matcher.start());
                    matchedStrings.add(matcher.group());
                }
            }
            else
            {
                int idx = textArea.getText().indexOf(searchString); // getting idx for first match

                // Matching till no other matches are available
                while (idx != -1)
                {
                    allIndexes.add(idx);
                    matchedStrings.add(textField.getText());
                    idx = textArea.getText().indexOf(searchString, idx + 1); // setting next idx to match from idx + 1 till end for non-repeated idx's
                }
            }

            key = 0; // starting from first match
            if (allIndexes.size() > 0)
            {
                // highlighting the first word by default on search press
                textArea.setCaretPosition(allIndexes.get(key) + matchedStrings.get(key).length());
                textArea.select(allIndexes.get(key), allIndexes.get(key) + matchedStrings.get(key).length());
                textArea.grabFocus();
            }
            else
            {
                // if no matches are found with regex or non-regex
                JOptionPane.showMessageDialog(this, "No matches found");
            }
        }
        // Action Listener for next search button
        else if (ae.getSource() == nextSearch || ae.getSource() == rightArrow)
        {
            key = (key + 1) % allIndexes.size(); // wrapping the key in a circular fashion

            // highlighting the text matched or found
            textArea.setCaretPosition(allIndexes.get(key) + matchedStrings.get(key).length());
            textArea.select(allIndexes.get(key), allIndexes.get(key) + matchedStrings.get(key).length());
            textArea.grabFocus();
        }
        // Action Listener for previous search button
        else if (ae.getSource() == previousSearch || ae.getSource() == leftArrow)
        {
            key = (key - 1 + allIndexes.size()) % allIndexes.size(); // wrapping the key in a circular fashion

            // highlighting the text matched or found
            textArea.setCaretPosition(allIndexes.get(key) + matchedStrings.get(key).length());
            textArea.select(allIndexes.get(key), allIndexes.get(key) + matchedStrings.get(key).length());
            textArea.grabFocus();
        }
        // Action Listener for exit menu button
        else if (ae.getSource() == exitMenu)
        {
            dispose(); // safer way to end application on exit
        }
    }
}