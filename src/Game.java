import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;
import java.util.random.RandomGenerator;

public class Game {
    private static final String PATH = "Words.txt";
    private void drawHangman(Integer errors)
    {
        IO.println(" +---+");
        if(errors >= 0) IO.println(" |   |");
        if(errors >= 1) IO.println(" 0   |");
        if(errors == 2) IO.println(" |");
        else if(errors == 3) IO.println("/|");
        else if(errors > 3) IO.println("/|\\");
        if(errors == 5) IO.println("/");
        else if(errors == 6) IO.println("/\\");
    }
    public String getWord() throws IOException{
        String text = Files.readString(Path.of(PATH), StandardCharsets.UTF_8);
        String[] words = text.trim().split("\\s+");
        return words[RandomGenerator.getDefault().nextInt(words.length)];
    }
}
