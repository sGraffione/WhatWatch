package database;



public class Tv {

    private int id_series;
    private int id_season;
    private String name;
    private int episode_current;
    private int episode_max;
    private int season_current;
    private int season_max;
    private int watched;
    private String img_url_series;
    private String img_url_season;





    public Tv(int id_series, int id_season, String name,
              int episode_max, int season_max, int watched){
        this.id_series = id_series;
        this.id_season = id_season;
        this.name = name;
        this.episode_current = 1;
        this.episode_max = episode_max;
        this.season_current = 1;
        this.season_max = season_max;
        this.watched = watched;
        this.img_url_series = null;
        this.img_url_season = null;
    }



    public Tv(int id_series, int id_season, String name,
              int episode_max, int season_max, int watched,
              String img_url_series, String img_url_season){
        this.id_series = id_series;
        this.id_season = id_season;
        this.name = name;
        this.episode_current = 1;
        this.episode_max = episode_max;
        this.season_current = 1;
        this.season_max = season_max;
        this.watched = watched;
        this.img_url_series = img_url_series;
        this.img_url_season = img_url_season;
    }


    public int getIdSeries() {
        return id_series;
    }

    public void setIdSeries(int id_series) {
        this.id_series = id_series;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdSeason() {
        return id_season;
    }

    public void setIdSeason(int id_season) {
        this.id_season = id_season;
    }

    public int getEpisodeCurrent() {
        return episode_current;
    }

    public void setEpisodeCurrent(int episode_current) {
        this.episode_current = episode_current;
    }

    public int getEpisodeMax() {
        return episode_max;
    }

    public void setEpisodeMax(int episode_max) {
        this.episode_max = episode_max;
    }

    public int getSeasonCurrent() {
        return season_current;
    }

    public void setSeasonCurrent(int season_current) {
        this.season_current = season_current;
    }

    public int getSeasonMax() {
        return season_max;
    }

    public void setSeasonMax(int season_max) {
        this.season_max = season_max;
    }

    public int getWatched() {
        return watched;
    }

    public void setWatched(int watched) {
        this.watched = watched;
    }

    public String getImgUrlSeries() {
        return img_url_series;
    }

    public void setImgUrlSeries(String img_url_series) {
        this.img_url_series = img_url_series;
    }

    public String getImgUrlSeason() {
        return img_url_season;
    }

    public void setImgUrlSeason(String img_url_season) {
        this.img_url_season = img_url_season;
    }
}
