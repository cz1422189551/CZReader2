package model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-6-30.
 */

public class LocalBook implements Serializable{

   private int bId;
    private  String bName;
    private  String bPath;

    private long bPos;

    public long getbPos() {
        return bPos;
    }

    public void setbPos(long bPos) {
        this.bPos = bPos;
    }

    public int getbId() {
        return bId;
    }

    public void setbId(int bId) {
        this.bId = bId;
    }

    public String getbName() {
        return bName;
    }

    public void setbName(String bName) {
        this.bName = bName;
    }

    public String getbPath() {
        return bPath;
    }

    public void setbPath(String bPath) {
        this.bPath = bPath;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    private String lastTime;

}
