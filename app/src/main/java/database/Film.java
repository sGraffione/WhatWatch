package database;



public class Film {

    private int id;
    private String name;
    //private String type;
    private int watched;
    private String img_url;



    public Film(int id, String name, /*String type,*/ int watched){
        this.id = id;
        this.name = name;
        //this.type = type;
        this.watched = watched;
        this.img_url = null;
    }

    public Film(int id, String name, /*String type,*/ int watched, String img_url){
        this.id = id;
        this.name = name;
        //this.type = type;
        this.watched = watched;
        this.img_url = img_url;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    /*public void setType(String type){
        this.type = type;
    }*/

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

    /*public String getType(){
        return type;
    }*/

    public int getWatched(){
        return watched;
    }

    public String getImgUrl(){
        return img_url;
    }


}
