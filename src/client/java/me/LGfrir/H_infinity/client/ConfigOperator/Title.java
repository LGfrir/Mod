package me.LGfrir.H_infinity.client.ConfigOperator;

public class Title implements Config {
    String Title;

    public Title(String Title) {
        this.Title = Title;
    }

    public String getTitle() {
        return Title;
    }


    @Override
    public String getDescription() {
        return "";
    }
}
