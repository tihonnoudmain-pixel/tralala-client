package dev.tralala.module;

public enum Category {
    COMBAT("Бой"),
    MOVEMENT("Движение"),
    PLAYER("Игрок"),
    RENDER("Визуал"),
    WORLD("Мир");

    private final String title;

    Category(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
