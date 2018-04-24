package com.askfortrciks.volleywithgsondemo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.askfortrciks.volleywithgsondemo.R;
import com.askfortrciks.volleywithgsondemo.adapter.MoviesRecyclerAdapter;
import com.askfortrciks.volleywithgsondemo.model.MovieList;
import com.askfortrciks.volleywithgsondemo.utils.Utils;
import com.askfortrciks.volleywithgsondemo.volley.WebApiRequest;

import static com.askfortrciks.volleywithgsondemo.constant.WebServicesConstant.API_KEY;
import static com.askfortrciks.volleywithgsondemo.constant.WebServicesConstant.BASE_URL_APPLICATION;
import static com.askfortrciks.volleywithgsondemo.constant.WebServicesConstant.MOVIE;
import static com.askfortrciks.volleywithgsondemo.constant.WebServicesConstant.TOP_RATED;

public class MovieListingActivity extends AppCompatActivity {

    public static final String TAG = "MovieListingActivity";
    private RelativeLayout rlMoviesList;
    private RecyclerView rvMoviesList;
    private LinearLayoutManager linearLayoutManager;

    private ProgressBar progressBar;

    //For Load more functionality
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 2;
    private int firstVisibleItem = 0;
    private int visibleItemCount = 0;
    private int totalItemCount = 0;

    //current page number
    private int pageNumber = 1;


    //Adapter class for movie list recycler view
    MoviesRecyclerAdapter moviesRecyclerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);

        initView();

        callGetTopRatedMoviesApi();
    }

    private void initView() {
        rlMoviesList = findViewById(R.id.rlMoviesList);
        rvMoviesList = findViewById(R.id.rvMoviesList);
        rvMoviesList.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);// new LinearLayoutManager()
        rvMoviesList.setLayoutManager(linearLayoutManager);
        rvMoviesList.clearOnScrollListeners(); //clear scrolllisteners

        moviesRecyclerAdapter=new MoviesRecyclerAdapter(this);

        rvMoviesList.setAdapter(moviesRecyclerAdapter);

        /**
         * For Adding Load more functionality
         *
         */
        rvMoviesList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    visibleItemCount = recyclerView.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if (totalItemCount > previousTotal) {
                            loading = false;
                            previousTotal = totalItemCount;
                        }
                    }
                    if (!loading && (totalItemCount - visibleItemCount)
                            <= (firstVisibleItem + visibleThreshold)) {
                        // End has been reached
                        Log.i("InfiniteScrollListener", "End reached");

                        callGetTopRatedMoviesApi();
                        loading = true;
                    }
                }
            }
        });

    }

    /**
     * Display Progress bar
     */

    private void showProgress() {
        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        rlMoviesList.addView(progressBar, params);

        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * Hide Progress bar
     */

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    /**
     * Call the api to fetch the TopRatedMovies list
     */

    private void callGetTopRatedMoviesApi() {

        /**
         * Checking internet connection before api call.
         * Very important always take care.
         */

        if (!Utils.isNetworkAvailable(this)) {
            Toast.makeText(this,
                    "No internet ..Please connect to internet and start app again",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        showProgress();


        //constructing api url
        String ws_url = BASE_URL_APPLICATION + MOVIE + TOP_RATED +
                "?api_key=" + API_KEY + "&language=en-US&page=" + pageNumber;

        //Using Volley to call api

        WebApiRequest webApiRequest = new WebApiRequest(Request.Method.GET,
                ws_url, ReqSuccessListener(), ReqErrorListener());
        Volley.newRequestQueue(MovieListingActivity.this).add(webApiRequest);
    }

    /**
     * Success listener to handle the movie listing
     * process after api returns the movie list
     *
     * @return
     */

    private Response.Listener<String> ReqSuccessListener() {
        return new Response.Listener<String>() {
            public void onResponse(String response) {
                Log.e("movie list_response", response);
                try {
                    hideProgress();
                    pageNumber++;

                    MovieList movieListModel = (MovieList) Utils.jsonToPojo(response, MovieList.class);

                    if (movieListModel.getResults() != null &&
                            movieListModel.getResults().size() > 0) {
                        moviesRecyclerAdapter.addMovies(movieListModel.getResults());
                    } else {
                        Log.e(TAG, "list empty==");
                    }

                } catch (Exception e) {
                Log.e(TAG,"Exception=="+e.getLocalizedMessage());
                }
            }
        };
    }

    /**
     * To Handle the error
     *
     * @return
     */

    private Response.ErrorListener ReqErrorListener() {
        return new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {

                Log.e("volley error", "volley error");
                Toast.makeText(MovieListingActivity.this, "" +
                        "Server Error..Please try again after sometime", Toast.LENGTH_SHORT).show();
            }
        };
    }

}
