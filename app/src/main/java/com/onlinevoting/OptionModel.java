package com.onlinevoting;

public class OptionModel {
    int image ;
    String option;

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public OptionModel(int image, String option){
        this.image = image;
        this.option = option;

    }
    public OptionModel(String option){
        this.option= option;
    }
}
