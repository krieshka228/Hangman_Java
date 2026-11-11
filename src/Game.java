public class Game {
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
}
