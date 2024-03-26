package org.checkinindza.GUI;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionListener;

import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import org.checkinindza.DataHandler.DataHandler;

public class GUITools {
    private static final Font labelFont = new Font("Calibri", Font.BOLD, 18);
    private static final Font textFieldFont = new Font("Calibri", Font.PLAIN, 16);
    private static final Font comboBoxFont = new Font("Calibri", Font.BOLD, 17);
    private static final Dimension componentSize = new Dimension(260, 40);

    public static JButton createAButton(String buttonString, ActionListener actionListener) {
        JButton button = new JButton(buttonString);
        /*
         * https://stackoverflow.com/questions/7229226/should-i-avoid-the-use-of-setpreferredmaximumminimum-size-methods-in-java-sw/7229519#7229519
         * Well in short, setXXXSize shouldn't be used. But as I'm working with limited
         * time, I'll use it
         * So I won't waste as much time figuring out how each Layout
         * Manager works
         */
        button.setPreferredSize(new Dimension(210, 40));
        button.setMaximumSize(new Dimension(210, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(actionListener);
        button.setFont(comboBoxFont);
        button.setMargin(new Insets(5, 0, 0, 0));
        return button;
    }

    public static void removeJComponent(Container container, JComponent componentToDelete) {
        Component[] components = container.getComponents();
        for (Component component : components) {
            if (component.equals(componentToDelete)) {
                container.remove(componentToDelete);
                container.revalidate();
                container.repaint();
                return;
            }
        }
        System.out.println("Component was not found or either was not yet added");
    }

    public static void restartScreen(JComponent component) {
        component.revalidate();
        component.repaint();
    }

    public static JLabel createATextFieldLabel(String labelString) {
        JLabel textFieldLabel = new JLabel(labelString);
        textFieldLabel.setFont(labelFont);
        return textFieldLabel;
    }

    public static JTextField createTextField(String placeHoldeString, DocumentFilter Filter) {
        JTextField textField = new JTextField();
        AbstractDocument doc = (AbstractDocument) textField.getDocument();
        doc.setDocumentFilter(Filter);
        TextPrompt tp = new TextPrompt(placeHoldeString, textField, TextPrompt.Show.FOCUS_LOST);
        tp.setFont(textFieldFont);
        textField.setFont(textFieldFont);
        textField.setPreferredSize(componentSize);
        textField.setMaximumSize(componentSize);
        textField.setMargin(new Insets(4, 10, 0, 0));
        return textField;
    }

    /* private JComboBox<String> createASelectionBox(String[] choiceStrings) {
        JComboBox<String> selectionBox = new JComboBox<>(choiceStrings);
        selectionBox.setPreferredSize(componentSize);
        selectionBox.setRenderer(new BorderListCellRenderer(10, 3));
        selectionBox.setFont(comboBoxFont);
        return selectionBox;
    } */

    // For JComboBox margin manipulation

    public static class BorderListCellRenderer implements ListCellRenderer<Object> {

        private final Border insetBorder;

        private final DefaultListCellRenderer defaultRenderer;

        public BorderListCellRenderer(int leftMargin, int topMargin) {
            this.insetBorder = new EmptyBorder(topMargin, leftMargin, 0, 0);
            this.defaultRenderer = new DefaultListCellRenderer();
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            JLabel renderer = (JLabel) defaultRenderer
                    .getListCellRendererComponent(list, value, index, isSelected,
                            cellHasFocus);
            renderer.setBorder(insetBorder);
            return renderer;
        }
    }

    public static class InputLengthFilter extends DocumentFilter {
        private final int maxCharacters;

        public InputLengthFilter(int maxCharacters) {
            this.maxCharacters = maxCharacters;
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String text, AttributeSet attributes) throws BadLocationException {
            replace(fb, offset, 0, text, attributes);
        }

        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attributes) throws BadLocationException {
            if (text == null) {
                text = "";
            }

            Document doc = fb.getDocument();
            StringBuilder sb = new StringBuilder();
            sb.append(doc.getText(0, doc.getLength()));
            sb.replace(offset, offset + length, text);
            if (sb.length() <= maxCharacters) {
                super.replace(fb, offset, length, text, attributes);
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }

    public static class NumericAndLengthFilter extends DocumentFilter {
        private final int maxCharacters;
        private final boolean rangeLimit;
        private final boolean onlyNumericFilter;
        DataHandler sizHandler;

        public NumericAndLengthFilter(int maxCharacters, boolean rangeLimit, boolean onlyNumericFilter) {
            this.maxCharacters = maxCharacters;
            this.rangeLimit = rangeLimit;
            this.onlyNumericFilter = onlyNumericFilter;
            this.sizHandler = new DataHandler();
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String text, AttributeSet attributes) throws BadLocationException {
            replace(fb, offset, 0, text, attributes);
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attributes) throws BadLocationException {
            // In case someone tries to clear the Document by using setText(null)

            if (text == null) {
                text = "";
            }

            // Build the text string assuming the replacement of the text is successful

            Document doc = fb.getDocument();
            // If you want to add decimal points, otherwise not really needed
            StringBuilder sb = new StringBuilder();
            sb.append(doc.getText(0, doc.getLength()));
            sb.replace(offset, offset + length, text);

            if (validReplace(sb.toString())) {
                if (rangeLimit) {
                    super.replace(fb, offset, length, text, attributes);
                } else if (onlyNumericFilter) {
                    super.replace(fb, offset, length, text, attributes);
                } else if (sb.length() <= maxCharacters) {
                    super.replace(fb, offset, length, text, attributes);
                }
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }

        private boolean validReplace(String text) {
            // In case setText("") is used to clear the Document
            if (text.isEmpty()) {
                return true;
            }

            // Verify input is an Integer
            try {
                if (rangeLimit) {
                    int value = Integer.parseInt(text);
                    return value >= 1 && value <= sizHandler.getCardsCollectionSize();
                } else {
                    Integer.parseInt(text);
                    return true;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }
    }
}

