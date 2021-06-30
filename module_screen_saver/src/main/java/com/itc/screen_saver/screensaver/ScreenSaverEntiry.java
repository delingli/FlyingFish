package com.itc.screen_saver.screensaver;

import java.io.Serializable;


public class ScreenSaverEntiry implements Serializable {

    /**
     * id : 9
     * screen_id : 11
     * equipment_id : 43
     * launch_time : 60
     * created_at : 2021-06-29 10:15:21
     * name : tuo1
     * material_id : 4
     * type : 1
     * path : uploads/20210625/07c63a52f8c700ac2d61241ea59d8d4f.jpg
     */

    private int id;
    private int screen_id;
    private int equipment_id;
    private int launch_time;
    private String created_at;
    private String name;
    private int material_id;
    private int type;
    private String path;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScreen_id() {
        return screen_id;
    }

    public void setScreen_id(int screen_id) {
        this.screen_id = screen_id;
    }

    public int getEquipment_id() {
        return equipment_id;
    }

    public void setEquipment_id(int equipment_id) {
        this.equipment_id = equipment_id;
    }

    public int getLaunch_time() {
        return launch_time;
    }

    public void setLaunch_time(int launch_time) {
        this.launch_time = launch_time;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaterial_id() {
        return material_id;
    }

    public void setMaterial_id(int material_id) {
        this.material_id = material_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "ScreenSaverEntiry{" +
                "id=" + id +
                ", screen_id=" + screen_id +
                ", equipment_id=" + equipment_id +
                ", launch_time=" + launch_time +
                ", created_at='" + created_at + '\'' +
                ", name='" + name + '\'' +
                ", material_id=" + material_id +
                ", type=" + type +
                ", path='" + path + '\'' +
                '}';
    }
}
