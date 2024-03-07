import javax.swing.JFrame;

public class MainInterface extends JFrame {
    public MainInterface() {
        this.initializeUI();
    }

    private void initializeUI() {
        setSize(1600, 1000);
        setTitle("Monopoly: Student Edition");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }
}
