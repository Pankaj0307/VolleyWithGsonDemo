package com.askfortrciks.volleywithgsondemo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.askfortrciks.volleywithgsondemo.R;
import com.askfortrciks.volleywithgsondemo.model.MovieList;

import java.util.ArrayList;
import java.util.List;

public class MoviesRecyclerAdapter extends RecyclerView.Adapter<MoviesRecyclerAdapter.ViewHolder> {

    public static final String TAG = "MoviesRecyclerAdapter";

    private List<MovieList.Result> moviesList = new ArrayList<>();
    private Context context;


    public MoviesRecyclerAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_movies, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.movieTitle.setText(moviesList.get(position).getTitle());
        holder.subtitle.setText(moviesList.get(position).getReleaseDate());
        holder.description.setText(moviesList.get(position).getOverview());
        holder.rating.setText(String.valueOf(moviesList.get(position).getVoteAverage()));

    }

    /**
     * Add movies list when calling apis
     * @param movies
     */

    public void addMovies(List<MovieList.Result> movies) {
        moviesList.addAll(movies);
        Log.e(TAG, "size of movie list==" + moviesList.size());
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    /**
     * View Holder for common row of movies
     */


    public class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout moviesLayout;
        private TextView movieTitle;
        private TextView subtitle;
        private TextView description;
        private ImageView ratingImage;
        private TextView rating;

        public ViewHolder(View itemView) {
            super(itemView);
            moviesLayout = itemView.findViewById(R.id.movies_layout);
            movieTitle = itemView.findViewById(R.id.movie_title);
            subtitle = itemView.findViewById(R.id.subtitle);
            description = itemView.findViewById(R.id.description);
            ratingImage = itemView.findViewById(R.id.rating_image);
            rating = itemView.findViewById(R.id.rating);

        }
    }
}
