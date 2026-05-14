package com.example.aggregator.service;

import com.example.aggregator.client.AggregatorRestClient;
import com.example.aggregator.model.Entry;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AggregatorService {

    private AggregatorRestClient restClient;

    public AggregatorService(AggregatorRestClient restClient) {
        this.restClient = restClient;
    }

    public Entry getDefinitionFor(String word) {
        return restClient.getDefinitionFor(word);
    }

    public List<Entry> getWordsThatContainSuccessiveLettersAndStartsWith(String chars) {

        List<Entry> wordsThatStartWith = restClient.getWordsStartingWith(chars);
        List<Entry> wordsThatContainSuccessiveLetters = restClient.getWordsThatContainConsecutiveLetters();

        List<Entry> common = new ArrayList<>(wordsThatStartWith);
        common.retainAll(wordsThatContainSuccessiveLetters);

        return common;
    }

    public List<Entry> getWordsThatContainSuccessiveLettersAndContains(String chars) {

        List<Entry> wordsThatContain = restClient.getWordsThatContain(chars);
        List<Entry> wordsThatContainSuccessiveLetters = restClient.getWordsThatContainConsecutiveLetters();

        List<Entry> common = new ArrayList<>(wordsThatContain);
        common.retainAll(wordsThatContainSuccessiveLetters);

        return common;
    }

    public List<Entry> getAllPalindromes() {

        final List<Entry> candidates = new ArrayList<>();

        // Create an array of alphabetic characters from 'a' to 'z'
        char[] alphabet = new char[26];
        for (int i = 0; i < 26; i++) {
            alphabet[i] = (char) ('a' + i);
        }

        // Iterate through each letter of the alphabet
        for (char c : alphabet) {
            String letter = String.valueOf(c);

            // Get words starting and ending with the current letter
            List<Entry> startsWith = restClient.getWordsStartingWith(letter);
            List<Entry> endsWith = restClient.getWordsEndingWith(letter);

            // Keep entries that exist in both lists
            List<Entry> startsAndEndsWith = new ArrayList<>(startsWith);
            startsAndEndsWith.retainAll(endsWith);

            // Add matching entries to candidates list
            candidates.addAll(startsAndEndsWith);
        }

        // Filter palindromes using traditional loop and if-statement
        List<Entry> palindromes = new ArrayList<>();
        for (Entry entry : candidates) {
            String word = entry.getWord();

            // Reverse the word manually using a loop
            String reverse = "";
            for (int i = word.length() - 1; i >= 0; i--) {
                reverse += word.charAt(i);
            }

            // Check if word equals its reverse
            if (word.equals(reverse)) {
                palindromes.add(entry);
            }
        }

        // Sort the palindromes list using bubble sort
        for (int i = 0; i < palindromes.size(); i++) {
            for (int j = i + 1; j < palindromes.size(); j++) {
                Entry entryI = palindromes.get(i);
                Entry entryJ = palindromes.get(j);

                if (entryI.compareTo(entryJ) > 0) {
                    // Swap
                    Entry temp = entryI;
                    palindromes.set(i, entryJ);
                    palindromes.set(j, temp);
                }
            }
        }

        return palindromes;
    }

}
