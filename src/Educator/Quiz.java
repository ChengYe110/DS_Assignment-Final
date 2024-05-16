package Educator;

import java.time.format.DateTimeFormatter;

public class Quiz {

    private String title, description, theme, content;

    public Quiz() {
    }

    public Quiz(String title, String description, String theme, String content) {
        this.title = title;
        this.description = description;
        this.theme = theme;
        this.content = content;
    }

}
