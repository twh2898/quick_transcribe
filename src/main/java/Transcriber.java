import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JTextArea;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;

public class Transcriber extends Thread {
	
	private boolean paused;
	
	private Configuration config;
	private String inputPath;
	private String outputPath;
	
	private StreamSpeechRecognizer recognizer;
	private FileWriter outputWriter;
	
	private JTextArea textArea;
	
	public Transcriber(JTextArea textArea, Configuration config, String inputPath, String outputPath) {
		paused = false;
		this.textArea = textArea;
		this.config = config;
		this.inputPath = inputPath;
		this.outputPath = outputPath;
	}
	
	public void run() {
		try {
			recognizer = new StreamSpeechRecognizer(config);
			InputStream stream = new FileInputStream(new File(inputPath));
			recognizer.startRecognition(stream);

			outputWriter = new FileWriter(outputPath);
			
			dothething();
		}
		catch (Exception e) {
			textArea.append("[AN ERROR OCCURED]\n");
			e.printStackTrace();
		}
		finally {
			recognizer.stopRecognition();
			try {
				outputWriter.close();
			} catch (IOException e) {
			}
			textArea.append("[STOPPED]\n");
		}
	}
	
	public synchronized boolean isPaused() {
		return paused;
	}
	
	public synchronized void pause() {
		paused = true;
	}
	
	public synchronized void unpause() {
		// Only notify once
		if (paused) {
			paused = false;
			this.notify();
		}
	}
	
	@Override
	public void interrupt() {
		unpause();
		super.interrupt();
	}
	
	private void dothething() throws Exception {
		textArea.append("[STARTING]\n");
		SpeechResult result;
		while ((result = recognizer.getResult()) != null && !this.isInterrupted()) {
			boolean localPaused;
			synchronized (this) {
				localPaused = paused;
			}
			if (localPaused) {
				textArea.append("[PAUSED]\n");
				synchronized (this) {
					this.wait();
				}
				textArea.append("[RESUME]\n");
			}
			String hypo = result.getHypothesis();
			textArea.append(hypo);
			textArea.append("\n");
			outputWriter.write(hypo);
			outputWriter.write('\n');
		}
	}
}
