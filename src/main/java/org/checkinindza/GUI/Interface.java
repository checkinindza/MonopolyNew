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
import java.util.Objects;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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
                        int money = Integer.parseInt(moneyInputField.getText());
                        int points = Integer.parseInt(pointsInputField.getText());
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

            private final Font cardNameFont = new Font("Calibri", Font.BOLD, 30);
            private final Font priceLabelFont = new Font("Calibri", Font.BOLD, 27);
            private final Font pointsLabelFont = new Font("Calibri", Font.BOLD, 25);
            private final int borderThickness = 3;
            private JPanel cardRarityTab;
            private JPanel cardTemplate;
            private JPanel rarityChoicePanel;
            private JLabel cardName;
            private JLabel priceLabel;
            private JLabel pointsLabel;
            private Boolean typeChosenFlag;

            public AddNewCardPanel() {
                setLayout(new MigLayout("hidemode 3"));
                this.cardTemplate = createCardTemplate();
                add(cardTemplate, "pos 950px 120px");
                GUITools.setComponentVisibility(cardTemplate, false);
                add(createSelectionPanel(), "pos 350px 40px");
            }

            private JPanel createCardTemplate() {
                cardTemplate = new JPanel(new MigLayout("width 300px, height 420px, align center",
                        "[]",
                        "[grow]"));
                cardTemplate.setBackground(new Color(189, 234, 211));

                cardTemplate.setBorder(BorderFactory.createLineBorder(Color.BLACK, borderThickness));

                cardRarityTab = new JPanel();
                cardRarityTab.setBackground(new Color(109, 234, 211));
                cardTemplate.add(cardRarityTab, "width 100px, height max(70px, 10%), north, wrap");

                cardName = new JLabel();
                cardName.setFont(cardNameFont);
                cardName.setHorizontalAlignment(SwingConstants.CENTER);

                priceLabel = new JLabel();
                priceLabel.setFont(priceLabelFont);
                priceLabel.setHorizontalAlignment(SwingConstants.CENTER);

                pointsLabel = new JLabel();
                pointsLabel.setFont(pointsLabelFont);
                pointsLabel.setHorizontalAlignment(SwingConstants.CENTER);

                cardTemplate.add(cardName, "north, gaptop 25");
                cardTemplate.add(pointsLabel, "dock center");
                cardTemplate.add(priceLabel, "south, gapbottom 15");

                return cardTemplate;
            }

            private JPanel createSelectionPanel() {
                JPanel selectionPanel = new JPanel(new MigLayout("height 600px, gapy 8, aligny center, hidemode 3"));

                selectionPanel.add(GUITools.createATextFieldLabel("Choose type"), "wrap");
                String[] typeChoices = {"None", "Property", "Utility", "Tax"};
                JComboBox<String> typeSelectionComboBox = GUITools.createASelectionBox(typeChoices);
                selectionPanel.add(typeSelectionComboBox, "wrap");

                selectionPanel.add(GUITools.createATextFieldLabel("Write a title (Max: 25 char.)"), "wrap");
                JTextField titleInputField = GUITools.createTextField("Type in a title...", new GUITools.InputLengthFilter(25));
                titleInputField.getDocument().addDocumentListener(new TextFieldListener(titleInputField, cardName, ""));
                selectionPanel.add(titleInputField, "wrap");

                rarityChoicePanel = new JPanel(new MigLayout("insets 0"));
                String[] rarityChoices = {"None", "Common", "Uncommon", "Rare", "Epic", "Legendary"};
                JComboBox<String> raritySelectionComboBox = GUITools.createASelectionBox(rarityChoices);
                raritySelectionComboBox.addActionListener(e -> changeRarityTabColor(cardRarityTab, (String) Objects.requireNonNull(raritySelectionComboBox.getSelectedItem())));
                rarityChoicePanel.add(GUITools.createATextFieldLabel("How rare is it?"), "wrap");
                rarityChoicePanel.add(raritySelectionComboBox);
                selectionPanel.add(rarityChoicePanel, "wrap");
                GUITools.setComponentVisibility(rarityChoicePanel, false);

                selectionPanel.add(GUITools.createATextFieldLabel("<html>How many points will it be worth? <br> (Only numbers allowed)</html>"), "wrap, hmax 48px");
                JTextField pointsInputField = GUITools.createTextField("Type in points...", new GUITools.NumericAndLengthFilter(0, false, true));
                pointsInputField.getDocument().addDocumentListener(new TextFieldListener(pointsInputField, pointsLabel, "<font size='5'>POINTS </font>"));
                selectionPanel.add(pointsInputField, "wrap");

                selectionPanel.add(GUITools.createATextFieldLabel("<html>How much will it cost? <br> (Only numbers allowed)</html>"), "wrap, hmax 48px");
                JTextField priceInputField = GUITools.createTextField("Type in a price...", new GUITools.NumericAndLengthFilter(0, false, true));
                priceInputField.getDocument().addDocumentListener(new TextFieldListener(priceInputField, priceLabel, "<font size='6'>PRICE $</font>"));
                selectionPanel.add(priceInputField, "wrap");

                JLabel positionTextFieldLabel = GUITools.createATextFieldLabel("<html>At what position you want to insert it?<br>(Only numbers allowed) <br> Current card count " + data.getCardsCollectionSize() + "</html>");
                selectionPanel.add(positionTextFieldLabel, "wrap, hmax 70px");
                JTextField positionInputField = GUITools.createTextField("Type in position...", new GUITools.NumericAndLengthFilter(0, true, false));
                selectionPanel.add(positionInputField, "wrap");

                return selectionPanel;
            }

            private void changeRarityTabColor(JComponent jComponent, String colorChoice) {
                switch (colorChoice) {
                    case "Legendary":
                        jComponent.setBackground(new Color(0, 128, 255));
                        jComponent.setBorder(BorderFactory.createMatteBorder(0, 0, borderThickness, 0, Color.black));
                        break;
                    case "Epic":
                        jComponent.setBackground(new Color(231, 242, 78));
                        jComponent.setBorder(BorderFactory.createMatteBorder(0, 0, borderThickness, 0, Color.black));
                        break;
                    case "Common":
                        jComponent.setBackground(new Color(133, 79, 30));
                        jComponent.setBorder(BorderFactory.createMatteBorder(0, 0, borderThickness, 0, Color.black));
                        break;
                    case "Uncommon":
                        jComponent.setBackground(new Color(212, 35, 133));
                        jComponent.setBorder(BorderFactory.createMatteBorder(0, 0, borderThickness, 0, Color.black));
                        break;
                    case "Rare":
                        jComponent.setBackground(new Color(60, 38, 129));
                        jComponent.setBorder(BorderFactory.createMatteBorder(0, 0, borderThickness, 0, Color.black));
                        break;
                    case "None":
                        jComponent.setBackground(new Color(189, 234, 211));
                        jComponent.setBorder(BorderFactory.createEmptyBorder());
                        break;
                    default:
                        break;
                }
            }

            private class TextFieldListener implements DocumentListener {

                private final JTextField textField;
                private final JLabel label;
                private final String additionalInformation;

                public TextFieldListener(JTextField textField, JLabel label, String additionalInformation) {
                    this.textField = textField;
                    this.label = label;
                    this.additionalInformation = additionalInformation;
                }

                @Override
                public void insertUpdate(DocumentEvent e) {
                    if (typeChosenFlag) {
                        updateLabel();
                    }
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    if (typeChosenFlag) {
                        updateLabel();
                    }
                }

                @Override
                public void changedUpdate(DocumentEvent e){
                }

                private void updateLabel() {
                    if (label == null) {
                        System.out.println("Label is null!");
                        return;
                    }

                    String textFieldRetrieval = textField.getText().toUpperCase();
                    int maxCharacters = 16;

                    StringBuilder formattedText = new StringBuilder("<html><center>" + additionalInformation);

                    for (int i = 0; i < textFieldRetrieval.length(); i++) {
                        formattedText.append(textFieldRetrieval.charAt(i));
                        if ((i + 1) % maxCharacters == 0) {
                            formattedText.append("<br");
                        }
                    }
                    formattedText.append("<center><html>");
                    label.setText(formattedText.toString());
                }
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
