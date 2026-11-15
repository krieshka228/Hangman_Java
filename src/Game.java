import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.random.RandomGenerator;

public class Game {
    private static final String PATH = "Words.txt";
    private static final String LOOS = "Вы потратили все попытки.";
    private static final String CONGRATULATIONS = "Поздравляю! Вы победили!!!";
    private int errors;
    private  Set<Character> targetLetters = new HashSet<>();
    private Set<Character> wrongLetters = new HashSet<>();


    public void start()
    {
        openMainMenu();
    }
    private void handleGuess(char inputLetter, String targetWord, char[] inputWord)
    {
        if(isNotCorrectLetter(inputLetter)) {
            wrongLetters.add(inputLetter);
            errors++;
        }
        else
        {
            for(int i = 0; i < targetWord.length(); i++)
            {
                if(targetWord.charAt(i) == inputLetter) inputWord[i] = inputLetter;
            }
        }
    }
    private void renderGameState(char[] inputWord)
    {
        drawHangman();
        IO.println("\n\n\n\n");
        IO.println(" " + new String(inputWord));
    }
    private void addLettersInSet(String targetWord)
    {
        for(int i = 0; i < targetWord.length(); i++)
        {
            targetLetters.add(targetWord.charAt(i));
        }
    }
    private void playRound(String targetWord, char[] inputWord)
    {
        while(!isWin(targetWord, inputWord) && errors < 6)
        {
            char inputLetter;
            String input;

            renderGameState(inputWord);

            input = IO.readln();

            if(isValidLetter(input)) { inputLetter = Character.toLowerCase(input.charAt(0)); }
            else { continue; }

            handleGuess(inputLetter, targetWord, inputWord);
        }
    }
    private void clearConsole()
    {
        IO.print("\033[H\033[2J");
        System.out.flush();
    }
    private void writeResult(String targetWord, char[] inputWord)
    {
        if(errors == 6 || !isWin(targetWord, inputWord))
        {
            System.out.printf(LOOS + " Слово: \""+ targetWord + "\".");
        }
        else
        {
            int claims = 6 - errors;
            System.out.printf(CONGRATULATIONS+" Слово: \""+ targetWord + "\", у вас "+ claims + " очков!" );
        }
    }
    private boolean isWin(String targetWord, char[] inputWord)
    {
        boolean win = false;
        for(int i = 0; i < targetWord.length(); i++)
        {
            if(inputWord[i] == targetWord.charAt(i)) win = true;
            else
            {
                win = false;
                break;
            }
        }
        return win;
    }
    private boolean isNotCorrectLetter(char letter)
    {
        return !targetLetters.contains(letter) && !wrongLetters.contains(letter);
    }
    private boolean isValidLetter(String letter)
    {
        return letter.length() == 1 && isCyrillic(letter.charAt(0));

    }
    private boolean isCyrillic(char symbol)
    {
        int code = (int) symbol;
        return (code >= 1040 && code <= 1103) || code == 1025 || code == 1105;
    }
    private void drawHangman()
    {
        clearConsole();
        IO.println(" +---+");
        if(errors >= 0) IO.println(" |   |");
        if(errors >= 1) IO.println(" 0   |");
        if(errors == 2) IO.println(" |");
        else if(errors == 3) IO.println("/|");
        else if(errors > 3) IO.println("/|\\");
        if(errors == 5) IO.println("/");
        else if(errors == 6) IO.println("/\\");
    }
    private String getWord() throws IOException{
        String text = Files.readString(Path.of(PATH), StandardCharsets.UTF_8);
        String[] words = text.trim().split("\\s*,\\s*");
        return words[RandomGenerator.getDefault().nextInt(words.length)];
    }
    private void openMainMenu()
    {
        String choice = "";
        final String MAIN_MENU_CHOICE = "====ВИСЕЛИЦА====\n1. Начать игру\n2. Выйти";
        final String START_GAME = "1";
        final String QUIT_GAME = "2";
        while(!choice.equals(QUIT_GAME))
        {
            choice = IO.readln(MAIN_MENU_CHOICE);
            switch(choice)
            {
                case START_GAME:
                    playGame();
                    break;
                case QUIT_GAME:
                    break;
            }
        }
    }
    private void playGame()
    {

        String targetWord;
        char[] inputWord;

        try
        {
            targetWord = getWord().toLowerCase();
            inputWord = new char[targetWord.length()];
            Arrays.fill(inputWord,'_');
        }
        catch (IOException e)
        {
            System.err.println("Не удалось прочитать файл" + e.getMessage());
            return;
        }

        targetLetters.clear();
        wrongLetters.clear();

        errors = 0;

        addLettersInSet(targetWord);
        playRound(targetWord, inputWord);
        writeResult(targetWord, inputWord);
    }


}
