import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class Interface {

    private JFrame guiFrame;
    private JLabel logo;

    public Interface() {
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
        mainWindowPanel mainPanel = new mainWindowPanel();
        guiFrame.add(mainPanel);
        guiFrame.setVisible(true);
    }

    private JLabel buildLogoFromResources() {
        JLabel logoLabel = new JLabel();
        InputStream stream = getClass().getResourceAsStream("logo.png");
        try {
            ImageIcon logoImage = new ImageIcon(ImageIO.read(stream));
            logoLabel.setIcon(logoImage);
            logoLabel.setHorizontalAlignment(JLabel.CENTER);
            logoLabel.setVerticalAlignment(JLabel.CENTER);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return logoLabel;
    }

    /////////////////////////////////////////////
    // -- Main Panel -//
    ///////////////////////////////////////////

    private class mainWindowPanel extends JPanel {

        private CardLayout cardLayout;
        private JPanel menuPanel;

        public mainWindowPanel() {
            cardLayout = new CardLayout();
            setLayout(cardLayout);
            menuPanel = buildMenuPanel();
            add(menuPanel, "mainMenuPanel");
            StartMenuPanel startMenuCard = new StartMenuPanel();
            add(startMenuCard, "startPanel");
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

        private void showCard(String cardNameString) {
            cardLayout.show(this, cardNameString);
        }

        private class startButtonAction implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCard("startPanel");
            }
        }

        private class cardManagerButtonAction implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCard("CardManagerCard");
            }
        }

        private class exitButtonAction implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
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
            twoPlayersChoice.setActionCommand("2 players");
            JRadioButton threePlayersChoice = new JRadioButton("3 players");
            threePlayersChoice.setFont(new Font("Calibri", Font.PLAIN, 17));
            threePlayersChoice.setActionCommand("3 players");

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
            } else if (playerChoiceGroup.getSelection().getActionCommand().equals("2 players")) {
                Toolkit.getDefaultToolkit().beep();
                int getResult = JOptionPane.showConfirmDialog(null, "You do not want to change anything?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (getResult == JOptionPane.NO_OPTION) {
                    Integer money = Integer.parseInt(moneyInputField.getText());
                    Integer points = Integer.parseInt(pointsInputField.getText());
                    
                }
            }
        }
    } 

    /////////////////////////////////////////////
    // -- End of Start Panel -//
    ///////////////////////////////////////////
}
