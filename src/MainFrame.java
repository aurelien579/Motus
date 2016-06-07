import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private final String word;
	private final Motus motus;
	private final JPanel mainPanel;
	private final ArrayList<ArrayList<JTextField>> rows;
	private final char[] remainingLetters;

	public MainFrame(final Motus motus, final String word) {
		super();
		this.motus = motus;
		this.word = word;
		this.rows = new ArrayList<ArrayList<JTextField>>();
		this.mainPanel = new JPanel();
		this.remainingLetters = word.toCharArray();		
		
		setTitle("Motus");
		setLocationRelativeTo(null);

		mainPanel.setLayout(new GridLayout(0, word.length() + 1, 5, 5));
		getContentPane().setLayout(new FlowLayout());
		getContentPane().add(mainPanel);

		JButton abortButton = new JButton("Abandonner");
		abortButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				abort();
			}
		});
		getContentPane().add(abortButton);

		addRow();

		pack();
	}

	private void abort() {
		int response = JOptionPane.showConfirmDialog(this,
				"Vous êtes nul !!! Le mot était : " + word + "\nVoulez vous recommencer ?", "Abandon",
				JOptionPane.OK_CANCEL_OPTION);

		if (response == JOptionPane.OK_OPTION) {
			newGame();
		} else {
			dispose();
		}
	}

	private void newGame() {
		motus.init();
		try {
			motus.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public JPanel getMainPanel() {
		return mainPanel;
	}

	private void win() {
		int response = JOptionPane.showConfirmDialog(this,
				"Vous avez gagné ! Le mot était : " + word + "\nVoulez vous recommencer ?", "Bravo",
				JOptionPane.OK_CANCEL_OPTION);

		if (response == JOptionPane.OK_OPTION) {
			newGame();
		} else {
			dispose();
		}
	}

	private void updateRow(ArrayList<JTextField> r) {
		char[] temp = remainingLetters.clone();
		
		int[] lettersToRemove = new int[remainingLetters.length];
		for (int i = 0; i < lettersToRemove.length; i++) {
			lettersToRemove[i] = -1;
		}		
		
		for (int i = 0; i < word.length(); i++) {
			JTextField textField = r.get(i);

			textField.setEditable(false);

			if (isTextFieldCorrect(textField, i)) {
				textField.setBackground(Color.GREEN.darker());
				lettersToRemove[i] = i;
			} else if (String.valueOf(temp).contains(textField.getText()) && !textField.getText().isEmpty()) {
				temp[String.valueOf(temp).indexOf(textField.getText())] = '\0';
				textField.setBackground(Color.ORANGE);
			}
		}
		
		for (int i = 0; i < lettersToRemove.length; i++) {
			if (lettersToRemove[i] != -1)
				remainingLetters[i] = '\0';
		}
	}

	private void addRow(int index) {
		ArrayList<JTextField> row = new ArrayList<JTextField>();
		ArrayList<JTextField> previousRow = null;

		if (index > 0)
			previousRow = rows.get(index - 1);

		if (previousRow != null) {
			updateRow(previousRow);
			if (isCorrect(previousRow)) {
				win();
			}
		}

		for (int i = 0; i < word.length(); i++) {
			JTextField newTextField = createTextField();

			if (previousRow != null) {
				if (isTextFieldCorrect(previousRow.get(i), i)) {
					newTextField.setText(previousRow.get(i).getText());
					newTextField.setEditable(false);
				}
			}

			row.add(newTextField);
			mainPanel.add(newTextField);
		}

		JButton button = new JButton("Ok");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				button.setEnabled(false);
				addRow(index + 1);
			}
		});

		rows.add(row);
		mainPanel.add(button);
		pack();
	}

	private void addRow() {
		addRow(0);
	}

	private boolean isCorrect(ArrayList<JTextField> row) {
		for (int i = 0; i < row.size(); i++) {
			if (!isTextFieldCorrect(row.get(i), i)) {
				return false;
			}
		}

		return true;
	}

	private JTextField createTextField() {
		JTextField t = new JTextField();

		t.setPreferredSize(new Dimension(50, 50));
		t.setMaximumSize(t.getPreferredSize());
		t.setMinimumSize(t.getPreferredSize());
		t.setHorizontalAlignment(JTextField.CENTER);
		t.setFont(new Font("Arial", Font.PLAIN, 20));
		t.setDocument(new JTextFieldLimit());
		t.addFocusListener(new FieldFocusListener(t));
		return t;
	}

	private boolean isTextFieldCorrect(JTextField f, int col) {
		if (f.getText().isEmpty()) {
			return false;
		}

		if (f.getText().charAt(0) == word.charAt(col)) {
			return true;
		} else {
			return false;
		}
	}

	private static class JTextFieldLimit extends PlainDocument {

		private static final long serialVersionUID = 1L;

		private int limit;

		public JTextFieldLimit() {
			super();
			this.limit = 1;
		}

		public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
			if (str == null)
				return;

			if ((getLength() + str.length()) <= limit) {
				super.insertString(offset, str, attr);
				KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();
			}
		}
	}

	private static class FieldFocusListener extends FocusAdapter {

		private JTextField field;

		public FieldFocusListener(JTextField field) {
			this.field = field;
		}

		@Override
		public void focusGained(FocusEvent e) {
			if (!field.isEditable()) {
				KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();
			}
		}
	}
}
