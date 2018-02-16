package model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-6-15.
 */

public class Book implements Serializable {

    private static final long serialVersionUID = 8748889646L;

    private int bookId;
    private String bookName;
    private String author;
    private String cover;
    private String describe;
    private int categoryId;
    private String path;




    public long getBookSize() {
        return bookSize;
    }

    public void setBookSize(long bookSize) {
        this.bookSize = bookSize;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public String getPartContent() {
        return partContent;
    }

    public void setPartContent(String partContent) {
        this.partContent = partContent;
    }

    //以下字段目前没有存在数据库中，属于后添加的
    private long bookSize;//.txt文件的大小

    private long position; //文件指针位置

    private String partContent; //部分内容

    public float getDisCount() {
        return disCount;
    }

    public void setDisCount(float disCount) {
        this.disCount = disCount;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    private float disCount;

    private float price;


    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
