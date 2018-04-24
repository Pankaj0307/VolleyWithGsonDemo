# VolleyWithGsonDemo

Call api using Volley and Parse json using Gson.

For a webservice call Volley Library is used and for parsing the json response we have used Gson.

Following demo is created on Android Studio 3.1.1

dependencies {

    //Gson
    implementation 'com.google.code.gson:gson:2.8.2'

    //Volley
    implementation 'com.android.volley:volley:1.1.0'
}

Please visit https://www.themoviedb.org/account/signup to register for your account and to genearte your own api_key.

Api Used here is from :
developers.themoviedb.org/3/getting-started/

Note :Substitute "<<api_key>>" with your personal key. You have to generate your developer key on the 
following link:

https://www.themoviedb.org/documentation/api

For Fetching top rated movies list from tmdb api called here is:

https://api.themoviedb.org/3/movie/top_rated?api_key=<<api_key>>&language=en-US&page=1

