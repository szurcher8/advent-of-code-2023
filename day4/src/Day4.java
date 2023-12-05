import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
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
     * Part 1: Tallies up the total points across multiple cards. Part 2: Counts all copies of cards after duplication.
     * @param args input txt file. Cards are formatted "Card 00: 12 34 | 56  7 89", one per line.
     */
    public static void main(String[] args)
    {
        int currentCard = 1;
        int winningStart;
        int numbersStart;
        int tally; // the number of matches between the winning numbers and your numbers for a given card
        int pointTotal = 0;
        int cardTotal = 0;
        HashMap<Integer, Integer> cardMap = new HashMap<>();
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
                tally = 0;
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

                // PART 1: Add points to the running point total.
                // The first match is 1pt; every subsequent match doubles the card's score.
                if (tally > 0)
                    pointTotal += Math.pow(2, tally-1);

                // PART 2: Create duplicates of cards.
                // Make an entry for the current card, or increment an existing entry to account for the original.
                int numCopies = cardMap.containsKey(currentCard) ? cardMap.get(currentCard)+1 : 1;
                cardMap.put(currentCard, numCopies);
                // Add duplicate cards to corresponding map entries, accounting for each copy of the current card.
                for (int i = 1; i <= tally; i++)
                {
                    int nextCard = currentCard + i;
                    int existingCards = cardMap.containsKey(nextCard) ? cardMap.get(nextCard) : 0;
                    cardMap.put(nextCard, existingCards + numCopies);
                }
                currentCard++;
            }
            scanner.close();

            // PART 2: Sum all the values of the card map.
            for (Integer card : cardMap.keySet())
                cardTotal += cardMap.get(card);

        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }

        System.out.println("Part 1 -- Total points:\t" + pointTotal);
        System.out.println("Part 2 -- Total cards:\t" + cardTotal);
    }
}