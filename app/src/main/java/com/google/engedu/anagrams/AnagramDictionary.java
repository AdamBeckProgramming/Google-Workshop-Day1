package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.HashMap;
import java.util.HashSet;
import android.util.Log;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private static HashSet<String> wordSet = new HashSet<String>();
    private static ArrayList<String> wordList = new ArrayList<String>();
    private static HashMap<String, ArrayList<String>> lettersToWords = new HashMap<String, ArrayList<String>>();
    private static HashMap<Integer, ArrayList<String>> sizeToWords = new HashMap<Integer, ArrayList<String>>();
    private static Integer wordLength = DEFAULT_WORD_LENGTH;

    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordSet.add(word);
            wordList.add(word);
            if(sizeToWords.containsKey(word.length())) {
                sizeToWords.get(word.length()).add(word);
            } else {
                ArrayList<String> arrList = new ArrayList<String>();
                arrList.add(word);
                sizeToWords.put(word.length(), arrList);
            }
            String sorted;
            sorted = stringSorter(word);
            if(lettersToWords.containsKey(sorted)) {
                ArrayList<String> appList = lettersToWords.get(sorted);
                appList.add(word);
            } else {
                ArrayList<String> arrList = new ArrayList<String>();
                arrList.add(word);
                lettersToWords.put(sorted, arrList);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        if( !wordSet.contains(word))
        {
            Log.d("isGoodWord", "Word was not in set");
            return false;
        }
        else if (word.toLowerCase().contains(base.toLowerCase()))
        {
            Log.d("isGoodWord", "base was a substring");
            return false;
        }
        Log.d("isGoodWord", "Word was in set, and word was not a substring");
        return true;
    }

    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for(char i = 'a'; i <= 'z'; ++i) {
            String s = new StringBuilder().append(i).append(word).toString();
            s = stringSorter(s);
            if(lettersToWords.containsKey(s)) {
                result.addAll((lettersToWords.get(s)));
            }
        }

        return result;
    }

    public String pickGoodStarterWord() {
        boolean found = false;
        ArrayList<String> result = new ArrayList<String>();
        String s;
        do {
            int randomNum = random.nextInt((wordList.size() - 1));
            s = wordList.get(randomNum);
            result = getAnagramsWithOneMoreLetter(s);

            if( result.size() >= MIN_NUM_ANAGRAMS && s.length() == wordLength && s.length() <= MAX_WORD_LENGTH){
                found = true;
                wordLength++;
                if( wordLength == MAX_WORD_LENGTH) {
                    wordLength = DEFAULT_WORD_LENGTH;
                }
            }
        }while(!found);
        return s;
    }

    private String stringSorter(String input) {
        char[] chars = input.toCharArray();
        Arrays.sort(chars);
        String sorted = new String(chars);
        return sorted;
    }
}