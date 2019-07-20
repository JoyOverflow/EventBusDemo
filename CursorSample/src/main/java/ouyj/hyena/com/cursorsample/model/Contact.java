package ouyj.hyena.com.cursorsample.model;

import java.util.List;

/**
 *
 */

public class Contact {
    private String name;
    private List<String> number;

    //电话号码列表
    public List<String> getNumber() {
        return number;
    }
    public void setNumber(List<String> number) {
        this.number = number;
    }

    //联系人的名称
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
