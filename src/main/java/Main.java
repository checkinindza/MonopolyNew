import javax.swing.SwingUtilities;

import org.checkinindza.GUI.Interface;

public class Main {
    public static void main(String[] args) {
        // https://docs.oracle.com/javase/tutorial/uiswing/concurrency/dispatch.html
        // https://tips4java.wordpress.com/2015/04/05/swing-and-java-8/
        SwingUtilities.invokeLater(Interface::new);
    }
}
