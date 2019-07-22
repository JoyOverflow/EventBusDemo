package ouyj.hyena.com.learnpinyin.data;

public class CharacterInfo {
    public int id;
    public String character;
    public String sounds;
    public String pinyin;
    public String done;
//    public CharacterInfo(String name){
//        id = 0;
//        character = name;
//        sounds = "";
//        pinyin = sounds;
//        done = PinYinContract.NO;
//    }
    public CharacterInfo(){
        id = 0;
        character = "";
        sounds = "";
        pinyin = "";
        done = PinYinContract.NO;
    }
}
