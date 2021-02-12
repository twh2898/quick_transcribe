import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.prefs.Preferences;

import javax.swing.JFrame;

public class MainFrame extends JFrame {

	public MainFrame() {
		this("defaults.ini");
	}
	
	public MainFrame(String iniFile) {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("The Really Awsome Software That Does Some Really Cool Things");

		MainPanel panel = new MainPanel(iniFile);
		this.setContentPane(panel);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO: clean up threads when closing
				// TODO: confirm close if thread is running
				panel.onClose();
			}
		});

		this.pack();
		this.setMinimumSize(this.getSize());
		this.setVisible(true);
		this.setLocationRelativeTo(null);
	}
}
