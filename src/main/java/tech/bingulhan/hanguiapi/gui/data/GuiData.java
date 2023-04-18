package tech.bingulhan.hanguiapi.gui.data;

import lombok.Getter;

import java.util.regex.Pattern;

@Getter
public class GuiData<T> {

    private T t;
    private String key;

    public GuiData(String key,T t) {
        this.key = key;
        this.t = t;
    }
}
