package org.checkinindza.GUI;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;

import org.checkinindza.DataHandler.DataHandler;
import org.checkinindza.Model.Player;

import net.miginfocom.swing.MigLayout;

public class Interface {

    private final DataHandler data;

    public Interface() {
        this.data = new DataHandler();
        this.initializeUI();
    }

    private void initializeUI() {
        JFrame guiFrame = new JFrame();
        guiFrame.setSize(1600, 1000);
        guiFrame.setTitle("Monopoly: Student Edition");
        guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        guiFrame.setResizable(false);
        JLabel logo = buildLogoFromResources();
        guiFrame.add(logo, BorderLayout.NORTH);
        mainWindow mainPanel = new mainWindow();
        guiFrame.add(mainPanel);
        guiFrame.setVisible(true);
    }

    private JLabel buildLogoFromResources() {
        JLabel logoLabel = new JLabel();
        data.setupJLabelIcon("/logo.png", logoLabel);
        logoLabel.setHorizontalAlignment(JLabel.CENTER);
        logoLabel.setVerticalAlignment(JLabel.CENTER);
        return logoLabel;
    }

    /////////////////////////////////////////////
    // -- Main Panel -//
    ///////////////////////////////////////////

    private class mainWindow extends JPanel {

        private final CardLayout cardLayout;
        private JPanel menuPanel;

        public mainWindow() {
            cardLayout = new CardLayout();
            setLayout(cardLayout);
            menuPanel = buildMenuPanel();
            add(menuPanel, "mainMenuPanel");
        }

        private JPanel buildMenuPanel() {
            menuPanel = new JPanel();
            menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

            JButton startButton = GUITools.createAButton("Start", new startButtonAction());
            JButton cardManagerButton = GUITools.createAButton("Card Manager", new cardManagerButtonAction());
            JButton exitButton = GUITools.createAButton("Exit", new exitButtonAction());

            menuPanel.add(Box.createVerticalGlue());
            menuPanel.add(startButton);
            menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            menuPanel.add(cardManagerButton);
            menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            menuPanel.add(exitButton);
            menuPanel.add(Box.createVerticalGlue());

            return menuPanel;
        }

        private void addComponent(JComponent component, String cardName) {
            add(component, cardName);
        }

        private void showCard(String cardNameString) {
            cardLayout.show(this, cardNameString);
        }

        private class startButtonAction implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                StartMenuPanel startMenuCard = new StartMenuPanel();
                add(startMenuCard, "startPanel");
                showCard("startPanel");
            }
        }
        private class cardManagerButtonAction implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardManagerTable cardManagerTableCard = new CardManagerTable();
                AddNewCardPanel addNewCardPanel = new AddNewCardPanel();
                add(cardManagerTableCard, "CardManagerCard");
                add(addNewCardPanel, "addNewCardPanel");
                showCard("CardManagerCard");
            }
        }

        private class exitButtonAction implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        }

        /////////////////////////////////////////////
        // -- End of Main Panel -//
        ///////////////////////////////////////////

        /////////////////////////////////////////////
        // -- Start Panel -//
        ///////////////////////////////////////////

        private class StartMenuPanel extends JPanel {

            private JPanel choicesPanel;
            private ButtonGroup playerChoiceGroup;
            private JTextField moneyInputField;
            private JTextField pointsInputField;
    
            public StartMenuPanel() {
                setLayout(new MigLayout("",
                        "[grow]",
                        "[grow]"));
                choicesPanel = buildChoicesPanel();
                add(choicesPanel, "center center");
            }
    
            private JPanel buildChoicesPanel() {
                JPanel tempPanel = new JPanel(new MigLayout("gapy 13",
                    "[center][right][left][c]",
                    "[top][center][b]"
                ));
                JLabel howManyPlayersLabel = GUITools.createATextFieldLabel("How many players?");
                JRadioButton twoPlayersChoice = new JRadioButton("2 players");
                twoPlayersChoice.setFont(new Font("Calibri", Font.PLAIN, 17));
                twoPlayersChoice.setActionCommand("2");
                JRadioButton threePlayersChoice = new JRadioButton("3 players");
                threePlayersChoice.setFont(new Font("Calibri", Font.PLAIN, 17));
                threePlayersChoice.setActionCommand("3");
    
                playerChoiceGroup = new ButtonGroup();
                playerChoiceGroup.add(twoPlayersChoice);
                playerChoiceGroup.add(threePlayersChoice);
    
                JLabel howMuchMoneyLabel = GUITools.createATextFieldLabel("How much money you all start with?");
                moneyInputField = GUITools.createTextField("Type in money...", new GUITools.NumericAndLengthFilter(0, false, true));
    
                JLabel howManyPointsLabel = GUITools.createATextFieldLabel("How many points you need to win?");
                pointsInputField = GUITools.createTextField("Type in points...", new GUITools.NumericAndLengthFilter(0, false, true));
    
                JButton startGameButton = GUITools.createAButton("Start Game", e -> StartGame());
            
                tempPanel.add(howManyPlayersLabel, "wrap");
                tempPanel.add(twoPlayersChoice, "wrap");
                tempPanel.add(threePlayersChoice, "wrap");
                tempPanel.add(howMuchMoneyLabel, "wrap");
                tempPanel.add(moneyInputField, "wrap");
                tempPanel.add(howManyPointsLabel, "wrap");
                tempPanel.add(pointsInputField, "wrap");
                tempPanel.add(startGameButton, "wrap");
    
                return tempPanel;
            }
    
            public void StartGame() {
                if (playerChoiceGroup.getSelection() == null || moneyInputField.getText().isEmpty() ||  pointsInputField.getText().isEmpty()) {
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null, "Check again, something is missing", "WRONG!", JOptionPane.ERROR_MESSAGE);
                } else {
                    Toolkit.getDefaultToolkit().beep();
                    int getResult = JOptionPane.showConfirmDialog(null, "Are you sure about these settings?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    if (getResult == JOptionPane.YES_OPTION) {
                        Integer money = Integer.parseInt(moneyInputField.getText());
                        Integer points = Integer.parseInt(pointsInputField.getText());
                        data.setupPlayers(money, points, Integer.parseInt(playerChoiceGroup.getSelection().getActionCommand()));
                        GameWindow gameWindow = new GameWindow();
                        addComponent(gameWindow, "gameWindow");
                        showCard("gameWindow");
                    }
                }
            }
        } 

    /////////////////////////////////////////////
    //--         End of Start Panel          -//
    ///////////////////////////////////////////

    /////////////////////////////////////////////
    //--         Card Manager Window        --//
    ///////////////////////////////////////////

        private class CardManagerTable extends JPanel {

            private JPanel textFieldPanel;

            public CardManagerTable() {
                setLayout(new MigLayout("",
                    "[grow]",
                    "[grow]"
                ));
                add(setupCardManagerTableWindow(), "east, gapright 20px, gapleft 20px");
                add(setupCardTableModel(), "dock center");
            }

            private JPanel setupCardManagerTableWindow() {
                JPanel controlPanel = new JPanel(new MigLayout("align 50% 50%, insets 0"));
                JButton AddNewCardButton = GUITools.createAButton("Add New Card", e -> showCard("addNewCardPanel"));
                JButton backButton = GUITools.createAButton("Back", e -> showCard("mainMenuPanel"));
                JButton loadButton = GUITools.createAButton("Load all", e ->{
                    if (data.isDataLoaded()) {
                        Toolkit.getDefaultToolkit().beep();
                        JOptionPane.showMessageDialog(null, "List is already loaded!", "Already done", JOptionPane.ERROR_MESSAGE);
                    } else {
                        data.readDataFromJSON();
                        revalidate();
                        repaint();
                    }
                });

                controlPanel.add(AddNewCardButton, "cell 0 1");
                controlPanel.add(setupCardDeletionByChoiceComboBox(), "cell 0 2");
                controlPanel.add(loadButton, "cell 0 5");
                controlPanel.add(backButton, "cell 0 6");
                return controlPanel;
            }

            private JPanel setupCardTableModel() {
                JPanel cardTablePanel = new JPanel(new GridLayout(1, 0));
                JTable cardTable = new JTable(data.new CardTableModel());
                cardTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
                cardTable.setFillsViewportHeight(true);
                cardTable.setFocusable(false);
                cardTable.setRowSelectionAllowed(false);
                cardTable.getTableHeader().setResizingAllowed(false);
                cardTable.getColumnModel().getColumn(0).setMaxWidth(50);
                JScrollPane tablePanelPane = new JScrollPane(cardTable);
                cardTablePanel.add(tablePanelPane);
                return cardTablePanel;
            }

            private JPanel setupCardDeletionByChoiceComboBox() {
                JPanel cardDeletion = new JPanel(new MigLayout("insets 0"));
                JLabel titleLabel = GUITools.createATextFieldLabel("Deletion options: ");
                String[] choices = {"None", "By Position", "By Name", "All" };
                JComboBox<String> deleteHowComboBox = new JComboBox<>(choices);
                deleteHowComboBox.setPreferredSize(new Dimension(210, 40));
                deleteHowComboBox.setRenderer(new GUITools.BorderListCellRenderer(10, 3));
                deleteHowComboBox.setFont(new Font("Calibri", Font.BOLD, 17));
                deleteHowComboBox.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        if (e.getStateChange() == ItemEvent.SELECTED) {
                            String selectedOption = (String) deleteHowComboBox.getSelectedItem();
                            assert selectedOption != null;
                            if (selectedOption.equals("None")) {
                                GUITools.removeJComponent(cardDeletion, textFieldPanel);
                            } else if (selectedOption.equals("By Position")) {
                                GUITools.removeJComponent(cardDeletion, textFieldPanel); // This shouldn't give any issues, as the remove method already checks if the component we want to remove already exists or not.
                                cardDeletion.add(setupDeletionByOption("Type in position..."), "cell 0 3");
                                revalidate();
                                repaint();

                            } else if (selectedOption.equals("By Name")) {
                                GUITools.removeJComponent(cardDeletion, textFieldPanel); // This shouldn't give any issues, as the remove method already checks if the component we want to remove already exists or not.
                                cardDeletion.add(setupDeletionByOption("Type in name..."), "cell 0 3");
                                revalidate();
                                repaint();
                            } else if (selectedOption.equals("All")) {
                                GUITools.removeJComponent(cardDeletion, textFieldPanel); // This shouldn't give any issues, as the remove method already checks if the component we want to remove already exists or not.
                                int userDecisionOnFullDeletion = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete all the cards?", "Confirmation", JOptionPane.YES_NO_OPTION);
                                if (userDecisionOnFullDeletion == 0) {
                                    if (data.deleteAllCards()) {
                                        Toolkit.getDefaultToolkit().beep();
                                        JOptionPane.showMessageDialog(null, "List was deleted successfully!", "Confirmation", JOptionPane.PLAIN_MESSAGE);
                                        revalidate();
                                        repaint();
                                    } else {
                                        Toolkit.getDefaultToolkit().beep();
                                        JOptionPane.showMessageDialog(null, "List is already empty.", "Something went wrong", JOptionPane.ERROR_MESSAGE);
                                    }
                                }
                            }
                        }
                    }
                });
                cardDeletion.add(titleLabel, "wrap");
                cardDeletion.add(deleteHowComboBox);
                return cardDeletion;           
            }

            private JPanel setupDeletionByOption(String placeHolder) {

                textFieldPanel = new JPanel(new MigLayout("insets 0, hidemode 3"));

                JLabel confirmationMessage = GUITools.getConfirmationMessage();
                textFieldPanel.add(confirmationMessage, "cell 0 2");
                confirmationMessage.setVisible(false);

                JTextField cardDeletionField = new JTextField();

                AbstractDocument doc = (AbstractDocument) cardDeletionField.getDocument();
                if (placeHolder.equals("Type in position...")) {
                    doc.setDocumentFilter(new GUITools.NumericAndLengthFilter(0, true, true));
                } else if (placeHolder.equals("Type in name...")) {
                    doc.setDocumentFilter(new GUITools.InputLengthFilter(25));
                }
                TextPrompt tp = new TextPrompt(placeHolder, cardDeletionField, TextPrompt.Show.FOCUS_LOST);
                tp.setFont(new Font("Calibri", Font.PLAIN, 16));
                cardDeletionField.setFont(new Font("Calibri", Font.PLAIN, 16));
                cardDeletionField.setPreferredSize(new Dimension(168, 40));

                Border emptyOutside = BorderFactory.createEmptyBorder();
                Border emptyInside = new EmptyBorder(4, 8, 0, 0);
                CompoundBorder textFieldBorder = new CompoundBorder(emptyOutside, emptyInside);
                cardDeletionField.setBorder(textFieldBorder);

                JButton OKButton = new JButton("OK");
                OKButton.addActionListener(e -> {
                    if (cardDeletionField.getText().isEmpty()) {
                        Toolkit.getDefaultToolkit().beep();
                        JOptionPane.showMessageDialog(null, "Field is empty", "Empty field", JOptionPane.ERROR_MESSAGE);
                    } else if (placeHolder.equals("Type in position...")) {
                        data.deleteCardByPosition((Integer.parseInt(cardDeletionField.getText()) - 1));
                        cardDeletionField.setText("");
                        revalidate();
                        repaint();
                        GUITools.showComponentForAMoment(confirmationMessage);
                    } else if (placeHolder.equals("Type in name...")) {
                        String cardName = cardDeletionField.getText().toLowerCase();
                        int deletionResult = data.deleteCardByName(cardName);
                        if (deletionResult == 0) {
                            Toolkit.getDefaultToolkit().beep();
                            JOptionPane.showMessageDialog(null, "Are you sure you typed the right name?", "Not found!", JOptionPane.ERROR_MESSAGE);
                        } else if (deletionResult == 1) {
                            cardDeletionField.setText("");
                            revalidate();
                            repaint();
                            GUITools.showComponentForAMoment(confirmationMessage);
                        }
                    }
                });
                OKButton.setPreferredSize(new Dimension(40, 40));
                OKButton.setMargin(new Insets(0, 0, 0, 0));

                JPanel textFieldWithButton = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
                Border border = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1);
                textFieldWithButton.setBorder(border);
                textFieldWithButton.add(cardDeletionField);
                textFieldWithButton.add(OKButton);

                textFieldPanel.add(textFieldWithButton, "cell 0 1");
                return textFieldPanel;
            }
        }

        /////////////////////////////////////////////
        //--         End of Card Manager         -//
        ///////////////////////////////////////////

        /////////////////////////////////////////////
        //--         Add New Card Window        --//
        ///////////////////////////////////////////

        private class AddNewCardPanel extends JPanel {

            private final int borderThickness = 3;

            public AddNewCardPanel() {
                setLayout(new MigLayout());
            }

            private JPanel createCardTemplate() {
                JPanel cardTemplate = new JPanel(new MigLayout("width 300px, height 420px, align center",
                        "[]",
                        "[grow]"));
                cardTemplate.setBackground(new Color(189, 234, 211));
                cardTemplate.setBorder(BorderFactory.createLineBorder(Color.BLACK, borderThickness));

                
            }
        }

    /////////////////////////////////////////////
    //--            Game Panel              --//
    ///////////////////////////////////////////

        private class GameWindow extends JPanel {

            public GameWindow() {
                add(setupDiceLogic());
                add(setupPlayerInfoPanel());
            }

            private JLabel setupDiceLogic() {
                JLabel dice = new JLabel();
                data.setupJLabelIcon("/dice_1.png", dice, 105, 100);
                dice.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        String imageName = "/dice_" + data.getRandomDiceValue() + ".png";
                        data.setupJLabelIcon(imageName, dice, 105, 100);
                    }
                });
                return dice;
            }

            private JPanel setupPlayerInfoPanel() {
                JPanel playerInfoPanel = new JPanel();
                Player player = data.getPlayer();

                JPanel playerOnePanel = new JPanel(new MigLayout("",
                    "[][grow, fill][]",
                    ""
                ));
                JLabel playerOneIcon = new JLabel();
                data.setupJLabelIcon("/player.png", playerOneIcon, 93, 93);
                JLabel playerOneTitle = GUITools.createATextFieldLabel("Player one");
                JLabel playerOneMoneyLabel = new JLabel("Money: ");
                playerOneMoneyLabel.setFont(new Font("Calibri", Font.BOLD, 16));
                JLabel playerOneMoneyValueLabel = new JLabel(String.valueOf(player.getMoney()));
                playerOneMoneyValueLabel.setFont(new Font("Calibri", Font.PLAIN, 16));
                JLabel playerOneDollarSign = new JLabel (" $");
                playerOneDollarSign.setFont(new Font("Calibri", Font.BOLD, 16));
                playerOnePanel.add(playerOneTitle, "alignx 7px, cell 0 1");
                playerOnePanel.add(playerOneIcon, "cell 0 2");
                playerOnePanel.add(playerOneMoneyLabel, "gapy 5px, alignx 5px, cell 0 3");
                playerOnePanel.add(playerOneMoneyValueLabel, "cell 0 3");
                playerOnePanel.add(playerOneDollarSign, "cell 0 3");

                JPanel playerTwoPanel = new JPanel(new MigLayout("",
                "[][grow, fill][]",
                ""));
                JLabel playerTwoIcon = new JLabel();
                data.setupJLabelIcon("/player.png", playerTwoIcon, 93, 93);
                JLabel playerTwoTitle = GUITools.createATextFieldLabel("Player Two");

                JLabel playerTwoMoneyLabel = new JLabel("Money: ");
                playerTwoMoneyLabel.setFont(new Font("Calibri", Font.BOLD, 16));
                JLabel playerTwoMoneyValueLabel = new JLabel(String.valueOf(player.getMoney()));
                playerTwoMoneyValueLabel.setFont(new Font("Calibri", Font.PLAIN, 16));
                JLabel playerTwoDollarSign = new JLabel (" $");
                playerTwoDollarSign.setFont(new Font("Calibri", Font.BOLD, 16));

                playerTwoPanel.add(playerTwoTitle, "alignx 7px, cell 0 1");
                playerTwoPanel.add(playerTwoIcon, "cell 0 2");
                playerTwoPanel.add(playerTwoMoneyLabel, "gapy 5px, alignx 5px, cell 0 3");
                playerTwoPanel.add(playerTwoMoneyValueLabel, "cell 0 3");
                playerTwoPanel.add(playerTwoDollarSign, "cell 0 3");

                playerInfoPanel.add(playerOnePanel);
                playerInfoPanel.add(playerTwoPanel);

                return playerInfoPanel;
            }
        }
    }
}
