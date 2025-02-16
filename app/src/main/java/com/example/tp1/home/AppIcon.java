package com.example.tp1.home;

public class AppIcon {

    private final  String app_name_;
    private final int icon_;

    public AppIcon(String app_name, int icon)
    {
        this.app_name_ =app_name;
        this.icon_ =icon;
    }
    public String get_app_name()
    {
        return app_name_;
    }
    public int get_icon()
    {
        return icon_;
    }
}