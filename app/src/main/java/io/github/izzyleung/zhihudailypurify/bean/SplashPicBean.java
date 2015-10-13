package io.github.izzyleung.zhihudailypurify.bean;

/**
 * Created by kylin on 15-10-13.
 */
public class SplashPicBean {
    private String title;
    private String image;

    public SplashPicBean(String title, String image){
        this.title = title;
        this.image = image;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
