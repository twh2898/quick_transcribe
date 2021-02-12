import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class MainFrame extends JFrame {

	public MainFrame() {
		this("defaults.ini");
	}
	
	public MainFrame(String iniFile) {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Sphinx Quick Transcribe");

		MainPanel panel = new MainPanel(iniFile);
		this.setContentPane(panel);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				panel.onClose();
			}
		});

		this.pack();
		this.setMinimumSize(this.getSize());
		this.setVisible(true);
		this.setLocationRelativeTo(null);
	}
}
