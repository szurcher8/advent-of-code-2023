import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day5
{
    public static long[] seedMappings = new long[0];
    public static long[] getSeeds(String str) {
        Pattern pattern = Pattern.compile("(\\d+ )+\\d+");
        Matcher matcher = pattern.matcher(str);
        String substr = "";
        if (matcher.find())
            substr = matcher.group(0);
        String[] splitSubstr = substr.split(" ");
        long[] seeds = new long[splitSubstr.length];
        for (int i = 0; i < splitSubstr.length; i++)
            seeds[i] = Long.parseLong(splitSubstr[i]);
        return seeds;
    }

    public static HashMap<Long, Long[]> getRangeMap(Scanner scanner) {
        String line = " ";
        HashMap<Long, Long[]> rangeMap = new HashMap<>();
        while (scanner.hasNextLine())
        {
            line = scanner.nextLine();
            if (line.equals(""))
                break;

            String[] splitLine = line.split(" ");
            long matchValue = Long.parseLong(splitLine[1]);
            long mappedValue = Long.parseLong(splitLine[0]);
            long range = Long.parseLong(splitLine[2]);
            Long[] mapAndRange = {mappedValue, range};

            rangeMap.put(matchValue, mapAndRange);
        }

        return rangeMap;
    }

    public static void updateMappings(HashMap<Long, Long[]> rangeMap)
    {
        for (int i = 0; i < seedMappings.length; i++)
        {
            for (Long matchValue : rangeMap.keySet())
            {
                long matchLimit = matchValue + rangeMap.get(matchValue)[1];
                if (seedMappings[i] >= matchValue && seedMappings[i] < matchLimit)
                {
                    long diff = seedMappings[i] - matchValue;
                    seedMappings[i] = rangeMap.get(matchValue)[0] + diff;
                    break;
                }
            }
        }
    }

    public static void main(String[] args)
    {
        if (args.length != 1)
        {
            System.out.println("Invalid arguments. Please provide an input txt file.");
            return;
        }
        try {
            File almanac = new File(args[0]);
            Scanner scanner = new Scanner(almanac);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.contains("seeds"))
                    seedMappings = getSeeds(line);
                else if (line.contains(":"))
                {
                    HashMap<Long, Long[]> rangeMap = getRangeMap(scanner);
                    updateMappings(rangeMap);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
        Arrays.sort(seedMappings);
        System.out.println("Part 1 -- lowest location number = " + seedMappings[0]);
    }
}