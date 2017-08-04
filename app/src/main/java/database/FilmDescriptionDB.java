package database;


import java.util.ArrayList;

public class FilmDescriptionDB {

    private int id;
    private String name;
    private String type;
    private int season;
    private int season_max;
    private int episode;
    private int episode_max;
    private int watched;
    private String img_url;



    public FilmDescriptionDB() {
        id = 0;
        name = null;
        type = null;
        season = 1;
        season_max = 1;
        episode = 1;
        episode_max = 1;
        watched = 0;
        img_url = null;
    };


    public FilmDescriptionDB(int id, String name){
        this.id = id;
        this.name = name;
        this.type = "movie";
        this.season = 1;
        this.season_max = 1;
        this.episode = 1;
        this.episode_max = 1;
        this.watched = 0;
        this.img_url = null;
    }

    public FilmDescriptionDB(int id, String name, String type){
        this.id = id;
        this.name = name;
        this.type = type;
        this.season = 1;
        this.season_max = 1;
        this.episode = 1;
        this.episode_max = 1;
        this.watched = 0;
        this.img_url = null;
    }

    public FilmDescriptionDB(int id, String name, String type, String url){
        this.id = id;
        this.name = name;
        this.type = type;
        this.season = 1;
        this.season_max = 1;
        this.episode = 1;
        this.episode_max = 1;
        this.watched = 0;
        this.img_url = url;
    }

    public FilmDescriptionDB(int id, String name, String type, int season, int season_max, int episode, int episode_max){
        this.id = id;
        this.name = name;
        this.type = type;
        this.season = season;
        this.season_max = season_max;
        this.episode = episode;
        this.episode_max = episode_max;
        this.watched = 0;
        this.img_url = null;
    }

    public FilmDescriptionDB(int id, String name, String type, int season, int season_max, int episode, int episode_max, String url){
        this.id = id;
        this.name = name;
        this.type = type;
        this.season = season;
        this.season_max = season_max;
        this.episode = episode;
        this.episode_max = episode_max;
        this.watched = 0;
        this.img_url = url;
    }

    public FilmDescriptionDB(int id, String name, String type, int season, int season_max, int episode, int episode_max, int watched, String url){
        this.id = id;
        this.name = name;
        this.type = type;
        this.season = season;
        this.season_max = season_max;
        this.episode = episode;
        this.episode_max = episode_max;
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
    public String getName() { return name; }

    public void setType(String type) { this.type = type; }
    public String getType() { return type; }

    public void setSeason(int season) {this.season = season; }
    public int getSeason() { return season; };

    public void setSeasonMax(int season_max) {this.season_max = season_max; }
    public int getSeasonMax() { return season_max; };

    public void setEpisode(int episode) {this.episode = episode; }
    public int getEpisode() { return episode; };

    public void setEpisodeMax(int episode_max) {this.episode_max = episode_max; }
    public int getEpisodeMax() { return episode_max; };

    public void setWatched(int status) { this.watched = status; }
    public int getWatched() { return watched; }

    public void setImg(String url) { this.img_url = url; }
    public String getImg() { return img_url; }

    public void setData(int id, String name, String type, int season, int season_max, int episode, int episode_max, String url){
        this.id = id;
        this.name = name;
        this.type = type;
        this.season = season;
        this.season_max = season_max;
        this.episode = episode;
        this.episode_max = episode_max;
        this.img_url = url;
    }


}
