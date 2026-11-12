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
    private void drawHangman(int errors)
    {
        IO.print("\033[H\033[2J");
        System.out.flush();
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
    public void mainMenu()
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
        final String CONGRATULATIONS = "Поздравляю! Вы победили!!!";
        final String LOOS = "Вы потратили все попытки.";

        Set<Character> wordLetters = new HashSet<Character>();
        Set<Character> wrongLetters = new HashSet<Character>();

        boolean win = false;
        int errors = 0;
        String word = "";
        char[] inputWord;
        try
        {
            word = getWord();
            inputWord = new char[word.length()];
            Arrays.fill(inputWord,'_');
        }
        catch (IOException e)
        {
            System.err.println("Не удалось прочитать файл" + e.getMessage());
            return;
        }
        for(int i = 0; i < word.length(); i++)
        {
            wordLetters.add(word.charAt(i));
        }

        while(!win && errors < 6)
        {

            drawHangman(errors);
            IO.println(new String(inputWord));
            String inputLetter = IO.readln();
            char letter = ' ';
            if(inputLetter.length() == 1 && Character.isLetter(inputLetter.charAt(0))) letter = inputLetter.charAt(0);
            if(!wordLetters.contains(letter) && !wrongLetters.contains(letter)) {
                errors++;
                wrongLetters.add(letter);
            }
            else
            {
                for(int i = 0; i < word.length(); i++)
                {
                    if(word.charAt(i) == letter) inputWord[i] = letter;
                }
                for(int i = 0; i < word.length(); i++)
                {
                    if(inputWord[i] == word.charAt(i)) win = true;
                    else
                    {
                        win = false;
                        break;
                    }
                }
            }
        }
        int claims = 6 - errors;
        if(errors == 6 || win == false)
        {

            IO.println(LOOS + " Слово: \""+ word + "\", у вас \"" + claims + "\" очков!");
        }
        else
        {
            IO.println(CONGRATULATIONS+" Слово: \""+ word + "\", у вас "+ claims + " очков!" );
        }
    }


}
