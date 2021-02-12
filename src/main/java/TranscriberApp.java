
import javax.swing.SwingUtilities;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class TranscriberApp {

    public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
					| UnsupportedLookAndFeelException e) {
			}
			
			new MainFrame();
		});
    }
}

