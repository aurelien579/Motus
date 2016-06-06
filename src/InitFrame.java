import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JTextField;

public class InitFrame extends JDialog {

	private static final long serialVersionUID = 1L;
	private static final int CUSTOM_WORD_INDEX = 0;
	private static final int RANDOM_WORD_INDEX = 1;

	private final JComboBox<String> comboBox;
	private final JTextField textField;
	private String word;

	public InitFrame() {
		super();

		textField = new JTextField();
		textField.setPreferredSize(new Dimension(150, textField.getHeight()));

		JButton button = new JButton("Valider");

		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (comboBox.getSelectedIndex() == CUSTOM_WORD_INDEX) {
					word = textField.getText();
				} else if (comboBox.getSelectedIndex() == RANDOM_WORD_INDEX) {
					word = WordManager.getInstance().generateCustomWord();
				}

				dispose();
			}
		});

		comboBox = new JComboBox<String>();
		comboBox.setMaximumSize(comboBox.getPreferredSize());

		comboBox.setModel(new DefaultComboBoxModel<String>(new String[] { "Mot personnalisé", "Mot aléatoire" }));

		comboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (comboBox.getSelectedIndex() == CUSTOM_WORD_INDEX) {
					textField.setEnabled(true);
				} else if (comboBox.getSelectedIndex() == RANDOM_WORD_INDEX) {
					textField.setEnabled(false);
				}
			}
		});

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);

		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(
				layout.createSequentialGroup().addComponent(comboBox).addComponent(textField).addComponent(button));

		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(comboBox)
				.addComponent(textField).addComponent(button));

		pack();
		setMinimumSize(getPreferredSize());

			setTitle("Motus");
		setLocationRelativeTo(null);
	}

	public String getWord() {
		return word;
	}
}
