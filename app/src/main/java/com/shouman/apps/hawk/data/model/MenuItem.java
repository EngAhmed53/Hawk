package com.shouman.apps.hawk.data.model;

public class MenuItem {
    private String menuTitle;
    private int menuIcon;

    public MenuItem(String menuTitle, int menuIcon) {
        this.menuTitle = menuTitle;
        this.menuIcon = menuIcon;
    }

    public String getMenuTitle() {
        return menuTitle;
    }

    public void setMenuTitle(String menuTitle) {
        this.menuTitle = menuTitle;
    }

    public int getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(int menuIcon) {
        this.menuIcon = menuIcon;
    }
}
