package com.project.picasso.cloud;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CloudImageGenerator {
    private static final int REJECT_COUNT = 100;
    private static final int LARGEST_FONT_SIZE = 160;
    private static final int FONT_STEP_SIZE = 5;
    private static final int MINIMUM_FONT_SIZE = 20;
    private static final int MINIMUM_WORD_COUNT = 1;
    public static final String FONT_FAMILY = "Meiryo";
    public static final String[] THEME = ColorCombinations.THEME1;

    private String fontFamily;
    private final int width;
    private final int height;
    private final int padding;
    private BufferedImage bi;
    private ColorCombinations colorTheme;
    private ArrayList<Shape> occupied = new ArrayList<Shape>();
    public static String one;
    public static String two;
    public static String thr;  

    public CloudImageGenerator(int width, int height, int padding) {
        this.width = width;
        this.height = height;
        this.fontFamily = FONT_FAMILY;
        this.padding = padding;
    }

    public CloudImageGenerator() {
        this.width = 1200;  // 기본값 설정
        this.height = 800;  // 기본값 설정
        this.padding = 30;  // 기본값 설정
    }

	String mostFrequentWord = null;
	int maxCount = 0;
    
    public BufferedImage generateImage(Map<String, Integer> wordFrequencies, long seed) {
        Random rand = new Random(seed);
        bi = new BufferedImage(width + 2 * padding, height + 2 * padding, BufferedImage.TYPE_INT_ARGB);
        colorTheme = new ColorCombinations(THEME);
        Graphics2D g = bi.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(colorTheme.background());
        g.fillRect(0, 0, bi.getWidth(), bi.getHeight());
        g.translate(padding, padding);

        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(wordFrequencies.entrySet());
        Collections.sort(entryList, Map.Entry.comparingByValue(Comparator.reverseOrder()));        
        
        int k = LARGEST_FONT_SIZE;
        for (Map.Entry<String, Integer> entry : wordFrequencies.entrySet()) {
            String word = entry.getKey();
            int count = entry.getValue();
            log.info("word:"+word);
            if (count < MINIMUM_WORD_COUNT) continue;
            if (one == null) {
                one = word;
            }else if(two==null) {
            	two=word;
            }else if(thr==null){
            	thr=word;
            }
            int prevK = k;
            if (k > MINIMUM_FONT_SIZE) k = k - FONT_STEP_SIZE;
            Font font = new Font(fontFamily, Font.BOLD, k);
            g.setFont(font);
            FontMetrics fm = g.getFontMetrics();
            Shape s = stringShape(font, fm, word, rand);
            boolean fitted = false;
            for (int i = 0; i < REJECT_COUNT * count; i++) {
                s = stringShape(font, fm, word, rand);
                if (!collision(s.getBounds())) {
                    fitted = true;
                    break;
                }
            }
            if (!fitted) {
                k = prevK;
                continue;
            }
            g.setColor(colorTheme.next());
            g.fill(s);
            occupied.add(s);
        }
        for (Map.Entry<String, Integer> entry : wordFrequencies.entrySet()) {
            String word = entry.getKey();
            int count = entry.getValue();
            if (count > maxCount) {
                maxCount = count;
                mostFrequentWord = word;
            }
        }
        one = mostFrequentWord;
        wordFrequencies.remove(mostFrequentWord);
        maxCount = 0;

        for (Map.Entry<String, Integer> entry : wordFrequencies.entrySet()) {
            String word = entry.getKey();
            int count = entry.getValue();
            if (count > maxCount) {
                maxCount = count;
                mostFrequentWord = word;
            }
        }
        two = mostFrequentWord;
        wordFrequencies.remove(mostFrequentWord);
        maxCount = 0;

        for (Map.Entry<String, Integer> entry : wordFrequencies.entrySet()) {
            String word = entry.getKey();
            int count = entry.getValue();
            if (count > maxCount) {
                maxCount = count;
                mostFrequentWord = word;
            }
        }
        thr = mostFrequentWord;
        wordFrequencies.remove(mostFrequentWord);
        maxCount=0;
        		
        return bi;
    }

    private boolean collision(Rectangle area) {
        for (Shape shape : occupied) {
            if (shape.intersects(area)) return true;
        }
        return false;
    }

    private Shape stringShape(Font font, FontMetrics fm, String word, Random rand) {
        int strWidth = fm.stringWidth(word);
        int strHeight = fm.getAscent();
        int x = rand.nextInt(width - strWidth);
        int y = rand.nextInt(height - strHeight) + strHeight;
        GlyphVector v = font.createGlyphVector(fm.getFontRenderContext(), word);
        AffineTransform at = new AffineTransform();
        at.translate(x, y);
        v.setGlyphTransform(0, at);
        return v.getOutline();
    }

    public void setColorTheme(String[] colorCodes, Color background) {
        colorTheme = new ColorCombinations(colorCodes, Color.white);
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }
    
    public void saveImageToJPEGFile(BufferedImage image, String filePath) {
        try {
            File outputFile = new File(filePath);
            ImageIO.write(image, "png", outputFile);
            System.out.println("이미지가 성공적으로 JPEG 파일로 저장되었습니다: " + filePath);
        } catch (IOException e) {
            System.err.println("이미지 파일 저장 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
