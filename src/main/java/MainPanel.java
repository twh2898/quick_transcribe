import java.awt.FileDialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.ini4j.Ini;
import org.ini4j.IniPreferences;

import edu.cmu.sphinx.api.Configuration;

public class MainPanel extends JPanel {

	private static final String PREFS_NODE_GROUP = "Paths";
	private static final String LAST_INPUT_KEY = "Input";
	private static final String LAST_OUTPUT_KEY = "Output";
	private static final String ACOUSTIC_MODEL_KEY = "AcousticModel";
	private static final String DICTIONARY_KEY = "Dictionary";
	private static final String LANGUAGE_MODEL_KEY = "LanguageModel";

	private static final String ACOUSTIC_MODEL_DEFAULT = "resource:/edu/cmu/sphinx/models/en-us/en-us";
	private static final String DICTIONARY_DEFAULT = "resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict";
	private static final String LANGUAGE_MODEL_DEFAULT = "resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin";
	

	private static final int TEXT_FIELD_WIDTH = 30;

	private JTextField inputTextField;
	private JTextField outputTextField;
	private JTextField acousticModelTextField;
	private JTextField dictionaryTextField;
	private JTextField languageModelTextField;

	private JTextArea outputTextArea;

	private Transcriber transcriber;

	private Ini ini;
	private Preferences prefs;
	private String iniFile;

	public MainPanel(String iniFile) {
		this.iniFile = iniFile;
		ini = new Ini();
		prefs = new IniPreferences(ini);
		try {
			ini.load(new File(iniFile));
		} catch (IOException e1) {
		}

		this.setBorder(new EmptyBorder(new Insets(4, 4, 4, 4)));
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		transcriber = null;

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		JButton button;

		add(new JLabel("Input (*.wav)"), gbc);
		gbc.gridx++;
		gbc.weightx = 1.0;
		inputTextField = new JTextField(TEXT_FIELD_WIDTH);
		inputTextField.setText(prefs.node(PREFS_NODE_GROUP).get(LAST_INPUT_KEY, ""));
		add(inputTextField, gbc);
		gbc.gridx++;
		gbc.weightx = 0.0;
		add(fileBrowserButton(inputTextField, LAST_INPUT_KEY), gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		add(new JLabel("Output (*.txt)"), gbc);
		gbc.gridx++;
		gbc.weightx = 1.0;
		outputTextField = new JTextField(TEXT_FIELD_WIDTH);
		outputTextField.setText(prefs.node(PREFS_NODE_GROUP).get(LAST_OUTPUT_KEY, ""));
		add(outputTextField, gbc);
		gbc.gridx++;
		gbc.weightx = 0.0;
		add(fileBrowserButton(outputTextField, LAST_OUTPUT_KEY), gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		add(new JLabel("Acoustic Model (en-us)"), gbc);
		gbc.gridx++;
		gbc.weightx = 1.0;
		acousticModelTextField = new JTextField(TEXT_FIELD_WIDTH);
		acousticModelTextField.setText(prefs.node(PREFS_NODE_GROUP).get(ACOUSTIC_MODEL_KEY, ACOUSTIC_MODEL_DEFAULT));
		add(acousticModelTextField, gbc);
		gbc.gridx++;
		gbc.weightx = 0.0;
		add(fileBrowserButton(acousticModelTextField, ACOUSTIC_MODEL_KEY), gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		add(new JLabel("Dictionary (*.dict)"), gbc);
		gbc.gridx++;
		gbc.weightx = 1.0;
		dictionaryTextField = new JTextField(TEXT_FIELD_WIDTH);
		dictionaryTextField.setText(prefs.node(PREFS_NODE_GROUP).get(DICTIONARY_KEY, DICTIONARY_DEFAULT));
		add(dictionaryTextField, gbc);
		gbc.gridx++;
		gbc.weightx = 0.0;
		add(fileBrowserButton(dictionaryTextField, DICTIONARY_DEFAULT), gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		add(new JLabel("Language Model (*.bin)"), gbc);
		gbc.gridx++;
		gbc.weightx = 1.0;
		languageModelTextField = new JTextField(TEXT_FIELD_WIDTH);
		languageModelTextField.setText(prefs.node(PREFS_NODE_GROUP).get(LANGUAGE_MODEL_KEY, LANGUAGE_MODEL_DEFAULT));
		add(languageModelTextField, gbc);
		gbc.gridx++;
		gbc.weightx = 0.0;
		add(fileBrowserButton(languageModelTextField, LANGUAGE_MODEL_DEFAULT), gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 3;
		gbc.weightx = 1.0;
		JPanel buttonPanel = new JPanel();

		button = new JButton("Run / Pause");
		button.addActionListener((ActionEvent e) -> {
			if (transcriber != null) {
				if (transcriber.isPaused()) {
					transcriber.unpause();
				}
				else {
					transcriber.pause();
				}
			}
			else {
				// TODO: input validation, check that all files exist
	
				Configuration configuration = new Configuration();
	
				configuration.setAcousticModelPath(acousticModelTextField.getText());
				configuration.setDictionaryPath(dictionaryTextField.getText());
				configuration.setLanguageModelPath(languageModelTextField.getText());
	
				transcriber = new Transcriber(outputTextArea, configuration, inputTextField.getText(),
						outputTextField.getText());
				transcriber.start();
			}
		});
		buttonPanel.add(button);

		button = new JButton("Stop");
		button.addActionListener((ActionEvent e) -> {
			if (transcriber != null) {
				transcriber.interrupt();
				try {
					transcriber.join();
				} catch (InterruptedException e1) {
				}
				finally {
					transcriber = null;
				}
			}
		});
		buttonPanel.add(button);
		add(buttonPanel, gbc);

		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.weightx = 0.0;
		add(new JLabel("Transcription:"), gbc);

		gbc.gridy++;
		gbc.gridwidth = 3;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		outputTextArea = new JTextArea(10, 20);
		JScrollPane scroll = new JScrollPane(outputTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(scroll, gbc);
	}

	public void onClose() {
		if (transcriber != null) {
			transcriber.interrupt();
			try {
				transcriber.join();
			} catch (InterruptedException e) {
			}
			transcriber = null;
		}
	}

	private JButton fileBrowserButton(JTextField field, String key) {
		JButton button = new JButton("...");
		button.addActionListener((ActionEvent e) -> {
			FileDialog fd = new FileDialog((java.awt.Frame) null);
			fd.setTitle("Open File");
			if (field.getText().length() > 0) {
				try {
					Path path = Paths.get(field.getText());
					fd.setDirectory(path.getParent().toString());
					fd.setFile(path.getFileName().toString());
				} catch (InvalidPathException ee) {
				}
			}
			fd.setVisible(true);
			if (fd.getFile() != null) {
				field.setText(fd.getDirectory() + fd.getFile());
				prefs.node(PREFS_NODE_GROUP).put(key, field.getText());
				try {
					ini.store(new File(iniFile));
				} catch (IOException e1) {
					// TODO: message failed to store preferences
				}
			}
		});
		return button;
	}
}
