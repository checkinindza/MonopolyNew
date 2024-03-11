package org.checkinindza.GUI;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import org.checkinindza.DataHandler.DataHandler;
import org.checkinindza.Model.Player;

import net.miginfocom.swing.MigLayout;

public class Interface {

    private JFrame guiFrame;
    private JLabel logo;
    private DataHandler data;

    public Interface() {
        this.data = new DataHandler();
        System.out.println(data.getCardsCollectionSize());
        this.initializeUI();
    }

    private void initializeUI() {
        guiFrame = new JFrame();
        guiFrame.setSize(1600, 1000);
        guiFrame.setTitle("Monopoly: Student Edition");
        guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        guiFrame.setResizable(false);
        logo = buildLogoFromResources();
        guiFrame.add(logo, BorderLayout.NORTH);
        mainWindow mainPanel = new mainWindow();
        guiFrame.add(mainPanel);
        guiFrame.setVisible(true);
    }

    private JLabel buildLogoFromResources() {
        JLabel logoLabel = new JLabel();
        data.setupJLabelIcon("logo.png", logoLabel);
        logoLabel.setHorizontalAlignment(JLabel.CENTER);
        logoLabel.setVerticalAlignment(JLabel.CENTER);
        return logoLabel;
    }

    /////////////////////////////////////////////
    // -- Main Panel -//
    ///////////////////////////////////////////

    private class mainWindow extends JPanel {

        private CardLayout cardLayout;
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
                add(cardManagerTableCard, "CardManagerCard");
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
                JButton AddNewCardButton = GUITools.createAButton("Add New Card", null);
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
                deleteHowComboBox.addItemListener(null);
                cardDeletion.add(titleLabel, "wrap");
                cardDeletion.add(deleteHowComboBox);
                return cardDeletion;           
            }
        }

/*         private class CardManagerAddCardWindow extends JPanel {

        } */

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
                data.setupJLabelIcon("dice_1.png", dice, 105, 100);
                dice.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        String imageName = "dice_" + data.getRandomDiceValue() + ".png";
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
                data.setupJLabelIcon("player.png", playerOneIcon, 93, 93);
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
                data.setupJLabelIcon("player.png", playerTwoIcon, 93, 93);
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
