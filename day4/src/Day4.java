import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;

public class Day4
{
    /**
     * Given a string formatted as "Card 00: 12 34 | 56  7 89", return an int[] of the numbers in a substring.
     * @param card the source string
     * @param start starting substring index
     * @param end ending substring index
     * @return int[] of the numbers in the substring
     */
    public static int[] convertSubstrToIntArray(String card, int start, int end)
    {
        String substr = card.substring(start, end);
        substr = substr.replaceAll("[:|]", "");
        substr = substr.strip();
        String[] splitSubstr = substr.split(" +");
        int[] intArray = new int[splitSubstr.length];
        for (int i = 0; i < splitSubstr.length; i++)
            intArray[i] = Integer.parseInt(splitSubstr[i]);

        return intArray;
    }

    /**
     * Tallies up the total points across multiple cards.
     * @param args input txt file. Cards are formatted "Card 00: 12 34 | 56  7 89", one per line.
     */
    public static void main(String[] args)
    {
        int winningStart;
        int numbersStart;
        int tally;
        int total = 0;
        try {
            if (args.length == 0)
                throw new FileNotFoundException();
            String fileName = args[0];
            File inputFile = new File(fileName);
            Scanner scanner = new Scanner(inputFile);
            while (scanner.hasNextLine()) {
                String card = scanner.nextLine();

                // Split the card into two int arrays:
                // the winning numbers (btwn : and |), and your own numbers (right of |).
                winningStart = card.indexOf(":");
                numbersStart = card.indexOf("|");
                tally = -1;
                int[] winningArray = convertSubstrToIntArray(card, winningStart, numbersStart);
                int[] numbersArray = convertSubstrToIntArray(card, numbersStart, card.length());

                // Add the winning numbers to a HashSet.
                // Check your numbers against them; any matches increase the tally.
                HashSet<Integer> winningSet = new HashSet<>();
                for (int i : winningArray)
                    winningSet.add(i);
                for (int i : numbersArray)
                {
                    if (winningSet.contains(i))
                        tally++;
                }

                // Add points to the running total.
                // The first match is 1pt; every subsequent match doubles the card's score.
                if (tally < 0)
                    continue;
                total += Math.pow(2, tally);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }

        System.out.println("Total points: " + total);
    }
}