
public class HangmanRenderer {
    private static final String CLEAR_BYTES = "\033[H\033[2J";
    private static final String[] DRAWINGS = {
            """
            +---------+
            |
            |
            |
            |__________
            """,
            """
            +---------+
            |         0
            |
            |
            |__________
            """,
            """
            +---------+
            |         0
            |         |
            |
            |__________
            """,
            """
            +---------+
            |         0
            |        /|
            |
            |__________
            """,
            """
            +---------+
            |         0
            |        /|\\
            |
            |__________
            """,
            """
            +---------+
            |         0
            |        /|\\
            |        /
            |__________
            """,
            """
            +---------+
            |         0
            |        /|\\
            |        / \\
            |__________
            """};

    public static void drawHangman(int errors)
    {
        clearConsole();
        IO.println(DRAWINGS[errors]);
    }
    private static void clearConsole()
    {
        IO.print(CLEAR_BYTES);
        System.out.flush();
    }
}
