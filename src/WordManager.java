import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class WordManager {

	private static final String filePath = "dico.txt";
	private static final WordManager instance = new WordManager();

	private final ArrayList<String> words;

	public WordManager() {
		words = new ArrayList<String>();		
		
		try (BufferedReader br = new BufferedReader(new FileReader(new File(filePath)))) {
			String line;
			while ((line = br.readLine()) != null) {
				words.add(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String generateCustomWord() {
		int r = (int) (Math.random() * words.size());
		
		return words.get(r);
	}

	public static WordManager getInstance() {
		return instance;
	}
}
