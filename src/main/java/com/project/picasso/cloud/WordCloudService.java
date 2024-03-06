package com.project.picasso.cloud;


import java.awt.Dimension;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;
import org.springframework.stereotype.Service;

@Service
public class WordCloudService {
    private static final String[] stopWords = {"の", "が", "それでも", "ます", "た", "は", "です", "。", "やる", "ある", "も","、","(",")"
    		,"、","（","）","*","』","『","「","」","[","]"," ","　"};
    public static final int WIDTH = 1200;
    public static final int HEIGHT = 800;
    public static final int PADDING = 30;

    public BufferedImage generateWordCloud(String text) {
        Map<String, Integer> wordFrequencies = performKuromojiAnalysis(text);
        CloudImageGenerator generator = new CloudImageGenerator(WIDTH, HEIGHT, PADDING);
        return generator.generateImage(wordFrequencies, System.currentTimeMillis());
    }

    private static Map<String, Integer> performKuromojiAnalysis(String text) {
//    	if (text == null) {
//    		return Collections.emptyMap();
//    	}
    	
    	
        Tokenizer tokenizer = new Tokenizer.Builder().build();
        List<Token> tokens = tokenizer.tokenize(text);
        Map<String, Integer> wordFrequencies = new HashMap<>();

        for (Token token : tokens) {
        	if (token.getAllFeaturesArray()[0].equals("名詞")) { 
            String baseForm = token.getSurface();
            if (!isStopWord(baseForm)) {
                wordFrequencies.put(baseForm, wordFrequencies.getOrDefault(baseForm, 0) + 1);
            }
        }
        }
        return wordFrequencies;
    }

    private static boolean isStopWord(String baseForm) {
        for (String stopWord : stopWords) {
            if (stopWord.equals(baseForm)) {
                return true;
            }
        }
        return false;
    }

    private static Dimension calcScreenSize(Insets insets) {
        int width = insets.left + insets.right + WIDTH + PADDING * 2;
        int height = insets.top + insets.bottom + HEIGHT + PADDING * 2;
        return new Dimension(width, height);
    }
}
