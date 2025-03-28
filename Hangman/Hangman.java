import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class Hangman extends JFrame {
    private static final String[] words = {"ant", "baboon", "badger", "bat", "bear", "beaver", "camel",
            "cat", "clam", "cobra", "cougar", "coyote", "crow", "deer",
            "dog", "donkey", "duck", "eagle", "ferret", "fox", "frog", "goat",
            "goose", "hawk", "lion", "lizard", "llama", "mole", "monkey", "moose",
            "mouse", "mule", "newt", "otter", "owl", "panda", "parrot", "pigeon",
            "python", "rabbit", "ram", "rat", "raven", "rhino", "salmon", "seal",
            "shark", "sheep", "skunk", "sloth", "snake", "spider", "stork", "swan",
            "tiger", "toad", "trout", "turkey", "turtle", "weasel", "whale", "wolf",
            "wombat", "zebra"};

    private static final String[] gallows = {"+---+\n" +
    "|   |\n" +
    "    |\n" +
    "    |\n" +
    "    |\n" +
    "    |\n" +
    "=========\n",

    "+---+\n" +
    "|   |\n" +
    "O   |\n" +
    "    |\n" +
    "    |\n" +
    "    |\n" +
    "=========\n",

    "+---+\n" +
    "|   |\n" +
    "O   |\n" +
    "|   |\n" +
    "    |\n" +
    "    |\n" +
    "=========\n",

    " +---+\n" +
    " |   |\n" +
    " O   |\n" +
    "/|   |\n" +
    "     |\n" +
    "     |\n" +
    " =========\n",

    " +---+\n" +
    " |   |\n" +
    " O   |\n" +
    "/|\\  |\n" + 
    "     |\n" +
    "     |\n" +
    " =========\n",

    " +---+\n" +
    " |   |\n" +
    " O   |\n" +
    "/|\\  |\n" +
    "/    |\n" +
    "     |\n" +
    " =========\n",

    " +---+\n" +
    " |   |\n" +
    " O   |\n" +
    "/|\\  |\n" + 
    "/ \\  |\n" +
    "     |\n" +
    " =========\n"};

    private String word;
    private char[] placeholders;
    private char[] missedGuesses;
    private int misses = 0;
    private JLabel gallowsLabel, wordLabel, missesLabel, messageLabel;
    private JTextField guessField;
    private JButton guessButton, newGameButton;

    public Hangman() {
    
        setTitle("Hangman Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setSize(400, 500);

        initializeGame();

        JPanel mainPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        gallowsLabel = new JLabel("<html><pre>" + gallows[misses] + "</pre></html>");
        gallowsLabel.setHorizontalAlignment(JLabel.CENTER);
        mainPanel.add(gallowsLabel);

        wordLabel = new JLabel(getPlaceholdersString());
        wordLabel.setHorizontalAlignment(JLabel.CENTER);
        wordLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
        mainPanel.add(wordLabel);

        missesLabel = new JLabel("Misses: " + getMissedGuessesString());
        missesLabel.setHorizontalAlignment(JLabel.CENTER);
        mainPanel.add(missesLabel);

        JPanel inputPanel = new JPanel(new FlowLayout());
        guessField = new JTextField(5);
        guessButton = new JButton("Guess");
        inputPanel.add(new JLabel("Guess: "));
        inputPanel.add(guessField);
        inputPanel.add(guessButton);
        mainPanel.add(inputPanel);

        messageLabel = new JLabel(" ");
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        mainPanel.add(messageLabel);

        newGameButton = new JButton("New Game");
        newGameButton.setVisible(false);
        add(newGameButton, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        guessButton.addActionListener(e ->processGuess());
        guessField.addActionListener(e -> processGuess());
        newGameButton.addActionListener(e -> {
            initializeGame();
            updateDisplay();
            newGameButton.setVisible(false);
            guessField.setEnabled(true);
            guessButton.setEnabled(true);
        });

        setLocationRelativeTo(null);
    }

    private void initializeGame() {
        word = randomWord();
        placeholders = new char[word.length()];
        Arrays.fill(placeholders, '_');
        missedGuesses = new char[6];
        misses = 0;
        if (messageLabel != null) messageLabel.setText(" ");
    }

    private String randomWord() {
        int randomIndex = (int) (Math.random() * words.length);
        return words[randomIndex];
    }

    private void processGuess() {
        String input = guessField.getText().trim();
        if (input.isEmpty() || input.length() > 1) {
            messageLabel.setText("Please enter a single letter!");
            return;
        }

        char guess = input.toLowerCase().charAt(0);
        guessField.setText("");

        if (checkGuess(guess)) {
            updatePlaceholders(guess);
        } else {
            missedGuesses[misses] = guess;
            misses++;
        }

        updateDisplay();

        if (Arrays.equals(placeholders, word.toCharArray())) {
            messageLabel.setText("GOOD WORK! You won!");
            endGame();
        } else if (misses >= 6) {
            messageLabel.setText("RIP! The word was: " + word);
            endGame();
        }
    }

    private boolean checkGuess(char guess) {
        return word.indexOf(guess) != -1;
    }

    private void updatePlaceholders(char guess) {
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == guess) {
                placeholders[i] = guess;
            }
        }
    }

    private String getPlaceholdersString() {
        StringBuilder sb = new StringBuilder();
        for (char c : placeholders) {
            sb.append(c).append(" ");
        }
        return sb.toString().trim();
    }

    private String getMissedGuessesString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < misses; i++) {
            sb.append(missedGuesses[i]);
        }
        return sb.toString();
    }

    private void updateDisplay() {
        gallowsLabel.setText("<html><pre>" + gallows[misses] + "</pre></html>");
        wordLabel.setText(getPlaceholdersString());
        missesLabel.setText("Misses: " + getMissedGuessesString());
    }

    private void endGame() {
        guessField.setEnabled(false);
        guessButton.setEnabled(false);
        newGameButton.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Hangman game = new Hangman();
            game.setVisible(true);
        });
    }
}