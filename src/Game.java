import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.random.RandomGenerator;

public class Game {
    private static final String PATH = "Words.txt";
    private static final String LOSE = "Вы потратили все попытки.";
    private static final String CONGRATULATIONS = "Поздравляю! Вы победили!!!";
    private static final String START_GAME = "1";
    private static final String QUIT_GAME = "2";
    private static final String MAIN_MENU_CHOICE = """
            ====ВИСЕЛИЦА====
            %s. Начать игру
            %s. Выйти""".formatted(START_GAME, QUIT_GAME);
    private static final String INPUT_LETTER_TEXT = "Введите букву: ";
    private static final int MAX_ERRORS = 6;
    private int errors;
    private Set<Character> targetLetters = new HashSet<>();
    private Set<Character> wrongLetters = new HashSet<>();

    private boolean isLose() {
        return errors == MAX_ERRORS;
    }

    public void start() {
        openMainMenu();
    }

    private void handleGuess(char inputLetter, String targetWord, char[] mask) {
        if (isNotCorrectLetter(inputLetter)) {
            wrongLetters.add(inputLetter);
            errors++;
        } else {
            for (int i = 0; i < targetWord.length(); i++) {
                if (targetWord.charAt(i) == inputLetter) mask[i] = inputLetter;
            }
        }
    }

    private void renderGameState(char[] mask) {
        HangmanRenderer.drawHangman(errors);
        IO.println(" " + new String(mask));
    }

    private void addLettersInSet(String targetWord) {
        for (int i = 0; i < targetWord.length(); i++) {
            targetLetters.add(targetWord.charAt(i));
        }
    }

    private boolean isGameOver(String targetWord, char[] mask) {
        return isLose() || isWin(targetWord, mask);
    }

    private void playRound(String targetWord, char[] mask) {
        do {
            char inputLetter;
            String input;

            renderGameState(mask);
            input = IO.readln(INPUT_LETTER_TEXT);

            if (isValidLetter(input)) {
                inputLetter = Character.toLowerCase(input.charAt(0));
            } else {
                continue;
            }

            handleGuess(inputLetter, targetWord, mask);
        }
        while (!isWin(targetWord, mask) && !isLose());
        renderGameState(mask);
    }

    private void writeResult(String targetWord, char[] mask) {
        if (errors == 6 || !isWin(targetWord, mask)) {
            System.out.printf(LOSE + " Слово: \"" + targetWord + "\".");
        } else {
            int claims = 6 - errors;
            System.out.printf(CONGRATULATIONS + " Слово: \"" + targetWord + "\", у вас " + claims + " очков!");
        }
    }

    private boolean isWin(String targetWord, char[] mask) {
        boolean win = false;
        for (int i = 0; i < targetWord.length(); i++) {
            if (mask[i] == targetWord.charAt(i)) win = true;
            else {
                win = false;
                break;
            }
        }
        return win;
    }

    private boolean isNotCorrectLetter(char letter) {
        return !targetLetters.contains(letter) && !wrongLetters.contains(letter);
    }

    private boolean isValidLetter(String letter) {
        return letter.length() == 1 && isRussian(letter.charAt(0));

    }

    private boolean isRussian(char symbol)
    {
        return (symbol >= 'А' && symbol <= 'я') || symbol == 'ё' || symbol == 'Ё';
    }

    private String getWord()
    {
        String text;
        try
        {
             text = Files.readString(Path.of(PATH), StandardCharsets.UTF_8);
        }
        catch(IOException e)

        {
            throw new UncheckedIOException("Не удалось прочитать файл: " + PATH, e);
        }

        String[] words = text.trim().split("\\s*,\\s*");
        return words[RandomGenerator.getDefault().nextInt(words.length)];
}
    private void openMainMenu()
    {
        String choice = "";
        while(!choice.equals(QUIT_GAME))
        {
            IO.println();
            choice = IO.readln(MAIN_MENU_CHOICE);
            switch(choice)
            {
                case START_GAME:
                    play();
                    break;
                case QUIT_GAME:
                    break;
                default:
                    IO.println("Такого варианта нет. Введите ещё раз!");
                    break;
            }
        }
    }
    private void play()
    {

        String targetWord;
        char[] mask;

        targetWord = getWord().toLowerCase();
        mask = new char[targetWord.length()];
        Arrays.fill(mask,'_');

        targetLetters.clear();
        wrongLetters.clear();

        errors = 0;

        addLettersInSet(targetWord);
        playRound(targetWord, mask);
        writeResult(targetWord, mask);
    }



}
