package com.project.picasso.cloud;

import java.awt.Color;

	/**
	 * You can find more combinations from http://www.colorcombos.com/
	 * @author epicdevs
	 */
	public class ColorCombinations {
		// 여기서 색상변경
	    public static String[] THEME1 = { "#00FA9A", "#006400", "#48D1CC", "#6A5ACD", "#FF1493" };
	    

	    private int idx = 0;
	    private final Color background;
	    private final Color[] scheme;
	    
	    public ColorCombinations(String[] colors) {
	        this(colors, Color.BLACK);
	    }
	    
	    public ColorCombinations(String[] colors, Color background) {
	        scheme = new Color[colors.length];
	        for (int i = 0; i < colors.length; i++) {
	            scheme[i] = Color.decode(colors[i]);
	        }
	        this.background = Color.white;
	    }

	    public Color next() {
	        idx = idx % scheme.length;
	        return scheme[idx++];
	    }
	    
	    public Color background() {
	        return background;
	    }
	}

