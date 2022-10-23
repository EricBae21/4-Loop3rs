import java.io.*;
import java.util.*;

public class ProcessFile {
    //public static void main(String[] args) throws FileNotFoundException{
    public ProcessFile() throws FileNotFoundException {
        Scanner txt = new Scanner(new File("output.txt"));
        List<String> importantLines = new ArrayList<>();
        findImportantLines(importantLines, txt);
        List<String> addresses = findAddress(importantLines);

        PrintStream output = new PrintStream("addresses.txt");
        output.print(biggestElement(addresses));
    }

    public static void findImportantLines(List<String> importantLines, Scanner txt) {
        String prevLine = "";
        int count = 0;

        while (txt.hasNextLine()) {
            String line = txt.nextLine();
            if (!(line.indexOf(" Ave ") == -1 && line.indexOf(" Ave. ") == -1 && line.indexOf(" Avenue ") == -1
            && line.indexOf(" St ") == -1 && line.indexOf(" Street ") == -1 && line.indexOf(" NE ") == -1
            && line.indexOf(" St. ") == -1)) {
                count = 3;
            }

            if (count > 0) {
                importantLines.add(prevLine);
                count--;
            }
            prevLine = line;
        }
    }

    public static List<String> findAddress(List<String> importantLines) throws FileNotFoundException{
        List<String> toReturn = new ArrayList<>();
        String bigLine = "";
        for (int i = 0; i < importantLines.size(); i++) {
            bigLine += importantLines.get(i);
        }

        bigLine = bigLine.replaceAll("Street", "st");
        bigLine = bigLine.replaceAll("th", "TH");
        bigLine = bigLine.toUpperCase();
        
        //if bigLine contains the street name, then add it to the toReturn array
        File f = new File("seattle_streets.txt");
        Scanner streets = new Scanner(f);
        String[] tokens = bigLine.split(" ");

        while (streets.hasNextLine()) {
            String street = " " + streets.nextLine();
            int streetSize = street.split(" ").length;
            
            String toCheck = "";
            for (int i = 0; i < bigLine.split(" ").length - streetSize; i++) {
                
                for (int index = i; index < streetSize+i; index++) {
                    toCheck += tokens[index] + " ";
                }

                if (toCheck.contains(street)) {
                    toReturn.add(street);
                }
            }

        }

        return toReturn;
    }
    public static String biggestElement(List<String> toReturn) {
        int max = 0;
        int index = 0;
        for (int i = 0; i < toReturn.size(); i++) {
            if (toReturn.get(i).length() > max) {
                max = toReturn.get(i).length();
                index = i;
            }
        }

        return toReturn.get(index);
    }

}