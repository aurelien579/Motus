import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

	private String word;
	private ArrayList<ArrayList<JTextField>> rows;
	private JPanel mainPanel;
	private final Motus motus;

	public MainFrame(Motus motus) {
		super();
		this.motus = motus;
		rows = new ArrayList<ArrayList<JTextField>>();
		setTitle("Motus");
		setLocationRelativeTo(null);
	}

	public void setWord(String word) {
		this.word = word;
		mainPanel = new JPanel();

		GridLayout layout = new GridLayout(0, word.length() + 1);
		layout.setHgap(5);
		layout.setVgap(5);
		mainPanel.setLayout(layout);

		getContentPane().setLayout(new FlowLayout());
		getContentPane().add(mainPanel);

		addRow();

		pack();
	}
	
	public JPanel getMainPanel() {
		return mainPanel;
	}

	private void addRow(int previousRowIndex) {
		ArrayList<JTextField> previousRow = rows.get(previousRowIndex);
		if (isCorrect(previousRow)) {
			for (JTextField f : previousRow) {
				f.setBackground(Color.GREEN.darker());
			}
			
			JOptionPane.showMessageDialog(this,
                    "Vous avez gagné ! Le mot était : " + word, "Bravo",
                    JOptionPane.INFORMATION_MESSAGE);
			motus.init();
			try {
				motus.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
		
		ArrayList<JTextField> row = new ArrayList<JTextField>();
		
		for (int i = 0; i < previousRow.size(); i++) {
			JTextField newTextField = createTextField();
			
			JTextField f = previousRow.get(i);
			f.setEditable(false);
			if (isTextFieldCorrect(f, i)) {
				f.setBackground(Color.GREEN.darker());
				newTextField.setText(f.getText());
				newTextField.setEditable(false);
			} else if (word.contains(f.getText()) && !f.getText().isEmpty()) {
				f.setBackground(Color.ORANGE);
			}
			
			row.add(newTextField);
			mainPanel.add(newTextField);
		}		
		
		JButton button = new JButton("Ok");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				button.setEnabled(false);
				addRow(previousRowIndex + 1);				
			}
		});

		rows.add(row);
		mainPanel.add(button);
		pack();
	}

	private void addRow() {
		if (rows.size() == 0) {
			int rowIndex = 0;
			ArrayList<JTextField> row = new ArrayList<JTextField>();

			for (int i = 0; i < word.length(); i++) {
				JTextField f = createTextField();
				mainPanel.add(f);
				row.add(f);
			}

			JButton button = new JButton("Ok");
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					button.setEnabled(false);
					addRow(rowIndex);	
				}
			});

			rows.add(row);
			mainPanel.add(button);
		}
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

	public static class JTextFieldLimit extends PlainDocument {

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
}
