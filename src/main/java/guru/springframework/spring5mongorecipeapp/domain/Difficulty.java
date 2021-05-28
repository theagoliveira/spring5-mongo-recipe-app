package guru.springframework.spring5recipeapp.domain;

public enum Difficulty {

    EASY, MEDIUM, HARD;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }

}
