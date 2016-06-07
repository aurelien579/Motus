import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Motus {

	private MainFrame mainFrame;
	
	private boolean initialazed;
	private String secretWord;

	public Motus() {
		initialazed = false;
		
	}
	
	public void init() {
		if (mainFrame != null) {
			mainFrame.dispose();
		}
		
		InitFrame initFrame = new InitFrame();
		initFrame.setModal(true);
		initFrame.setVisible(true);
		
		secretWord = initFrame.getWord();
		if (secretWord == null) {
			System.exit(0);
		}		
		
		mainFrame = new MainFrame(this, secretWord);
		
		initialazed = true;
	}

	public void start() throws Exception {
		if (!initialazed) {
			throw new Exception("Motus not initialized");
		} else {
			mainFrame.setVisible(true);
		}
	}

	public static void main(String[] args) throws Exception {
		setLook();
		Motus motus = new Motus();
		motus.init();
		motus.start();
	}

	private static void setLook() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {

		} catch (ClassNotFoundException e) {

		} catch (InstantiationException e) {

		} catch (IllegalAccessException e) {

		}
	}

}