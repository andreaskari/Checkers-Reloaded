package ngordnet;

public class WordLengthProcessor implements YearlyRecordProcessor {
    public double process(YearlyRecord yearlyRecord) {
        long numLetters = 0;
        long numWords = 0;
        for (String word: yearlyRecord.words()) {
            int count = yearlyRecord.count(word);
            numLetters += word.length() * count;
            numWords += count;
        }
        return numLetters / (double) numWords;
    }
}
