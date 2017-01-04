package com.example.weizifen.myapplication;

/**
 * Created by weizifen on 17/1/3.
 */

public class Down {


    /*名字在线获取*/
    private String name;
    /*图片使用本地的*/
    private int imageId;
    /*获得下载路径*/

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    private String path;

    public Down(String name,int imageId) {
        super();
        this.name=name;
        this.imageId=imageId;
//        this.path=parh;


    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
