package in.kichawele.ghibliapi.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TextToImageRequest {
    @JsonProperty("text_prompts")
    private List<TextPrompt> textPrompts;
    
    @JsonProperty("cfg_scale")
    private double cfgScale = 7;
    
    private int height = 512;
    private int width = 768;
    private int samples = 1;
    private int steps = 30;
    
    @JsonProperty("style_preset")
    private String stylePreset;

    public static class TextPrompt {
        private String text;
        
        public TextPrompt() {}
        
        public TextPrompt(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public TextToImageRequest() {}

    public TextToImageRequest(String text, String style) {
        this.textPrompts = List.of(new TextPrompt(text));
        this.stylePreset = style;
    }

    public List<TextPrompt> getTextPrompts() {
        return textPrompts;
    }

    public void setTextPrompts(List<TextPrompt> textPrompts) {
        this.textPrompts = textPrompts;
    }

    public double getCfgScale() {
        return cfgScale;
    }

    public void setCfgScale(double cfgScale) {
        this.cfgScale = cfgScale;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getSamples() {
        return samples;
    }

    public void setSamples(int samples) {
        this.samples = samples;
    }

    public String getStylePreset() {
        return stylePreset;
    }

    public void setStylePreset(String stylePreset) {
        this.stylePreset = stylePreset;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }
}
