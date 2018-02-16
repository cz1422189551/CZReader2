package model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-6-18.
 */

public class BookCategory implements Serializable {

    private static final long serialVersionUID=8715451231544L;
    private int categoryId;
    private String categoryName;

    private String img;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    private String summary;
}
