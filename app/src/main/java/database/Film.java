package database;



public class Film {

    private int id;
    private String name;
    private int watched;
    private String img_url;
    private int index;



    public Film(int id, String name, int watched){
        this.id = id;
        this.name = name;
        this.watched = watched;
        this.img_url = null;
    }

    public Film(int id, String name, int watched, String img_url){
        this.id = id;
        this.name = name;
        this.watched = watched;
        this.img_url = img_url;
    }

    public Film(int id, String name, int watched, String img_url, int index){
        this.id = id;
        this.name = name;
        this.watched = watched;
        this.img_url = img_url;
        this.index = index;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setWatched(int watched){
        this.watched = watched;
    }

    public void setImgUrl(String img){
        this.img_url = img;
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public int getWatched(){
        return watched;
    }

    public String getImgUrl(){
        return img_url;
    }

    public int getIndex() { return index; }

    public void setIndex(int index) { this.index = index; }


}
