package database;


public class FilmDescriptionDB {

    private int id;
    private String name;
    private String type;
    private int episode;
    private int watched;
    private String img_url;



    public FilmDescriptionDB() {
        id = 0;
        name = null;
        type = null;
        episode = 1;
        watched = 0;
        img_url = null;
    };

    public FilmDescriptionDB(String name) {
        this.name = name;
    }

    public FilmDescriptionDB(int id) { this.id = id; }

    public FilmDescriptionDB(int id, String name, String type){
        this.id = id;
        this.name = name;
        this.type = type;
        this.episode = 0;
        this.watched = 0;
        this.img_url = null;
    }

    public FilmDescriptionDB(int id, String name, String type, String url){
        this.id = id;
        this.name = name;
        this.type = type;
        this.episode = 0;
        this.watched = 0;
        this.img_url = url;
    }

    public FilmDescriptionDB(int id, String name, String type, int episode){
        this.id = id;
        this.name = name;
        this.type = type;
        this.episode = episode;
        this.watched = 0;
        this.img_url = null;
    }

    public FilmDescriptionDB(int id, String name, String type, int episode, String url){
        this.id = id;
        this.name = name;
        this.type = type;
        this.episode = episode;
        this.watched = 0;
        this.img_url = url;
    }

    public FilmDescriptionDB(int id, String name, String type, int episode, int watched, String url){
        this.id = id;
        this.name = name;
        this.type = type;
        this.episode = episode;
        this.watched = watched;
        this.img_url = url;
    }


    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return id;
    }

    public void setName(String name) { this.name = name; }
    public String getName(){
        return name;
    }

    public void setType(String type) { this.type = type; }
    public String getType() { return type; }

    public void setEpisode(int episode) {this.episode = episode; }
    public int getEpisode(){ return episode; };

    public void setWatched(int status) { this.watched = status; }
    public int getWatched() { return watched; }

    public void setImg(String url) { this.img_url = url; }
    public String getImg() { return img_url; }

    public void setData(int id, String name, String type, int episode, String url){
        this.id = id;
        this.name = name;
        this.type = type;
        this.episode = episode;
        this.img_url = url;
    }
}
