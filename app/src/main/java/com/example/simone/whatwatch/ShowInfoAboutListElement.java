package com.example.simone.whatwatch;

        import android.app.Activity;
        import android.content.Intent;
        import android.graphics.Color;
        import android.os.Bundle;
        import android.support.v4.content.ContextCompat;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Button;

        import com.squareup.picasso.Picasso;

        import java.net.URL;
        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.concurrent.ExecutionException;


public class ShowInfoAboutListElement extends Activity {

    ArrayList<HashMap<String, Object>> filmInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info_about_list_element);

        final TextView Title = (TextView) findViewById(R.id.Title);
        final ImageView poster = (ImageView) findViewById(R.id.poster);
        final Button add_button = (Button) findViewById(R.id.add_button);
        final TextView overview = (TextView) findViewById(R.id.Overview);
        final TextView rating = (TextView) findViewById(R.id.rating);

        String type = "movie";
        int id_film;
        filmInfo = new ArrayList<>();

        Intent intent = getIntent();
        if(intent != null){
            id_film = getIntent().getIntExtra("id", 0);
            type = getIntent().getStringExtra("type");
            String url = "https://api.themoviedb.org/3/" + type + "/"+ id_film + "?api_key=22dee1f565e5788c58062fdeaf490afc&language=en-US\n";
            try{
                filmInfo = new ParsingInfoFilm(this, type).execute(url).get();
            }catch (ExecutionException e){
                e.printStackTrace();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }

        if(type.equals("movie")){
            Title.setText((String) filmInfo.get(0).get("original_title"));
        }else{
            Title.setText((String) filmInfo.get(0).get("name"));
        }
        Picasso.with(this).load((String) filmInfo.get(0).get("poster_path")).into(poster);
        overview.setText((String) filmInfo.get(0).get("overview"));
        Double ratingValue = Double.parseDouble((String) filmInfo.get(0).get("vote_average"));
        if( ratingValue < 6){
            rating.setTextColor(Color.RED);
        }else if( ratingValue > 7.5){
            rating.setTextColor(Color.GREEN);
        }else{
            rating.setTextColor(ContextCompat.getColor(this, R.color.Orange));
        }
        rating.setText((String) filmInfo.get(0).get("vote_average"));
    }
}