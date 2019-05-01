package com.example.idanf.movies;

import android.app.SearchManager;
import android.content.Context;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import com.example.idanf.movies.adapters.MainPageFragmentAdapter;
import com.example.idanf.movies.fragments.MoviesList;
import com.example.idanf.movies.models.GenreDictionary;
import com.example.idanf.movies.models.ListType;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private MainPageFragmentAdapter viewPageAdapter;
    private MoviesList popularFragment;
    private MoviesList topRatedFragment;
    private MoviesList upcomingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initNavMenu();
        initAppBar();
    }

    private void initNavMenu() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        final GenreDictionary genreDictionary = new GenreDictionary();
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        String genreId = genreDictionary.getGenreId(menuItem.getItemId());

                        String popularUrl = generateUrl(ListType.POPULAR, genreId);
                        popularFragment.getArguments().putString("url", popularUrl);
                        popularFragment.updateMovies();

                        String topRatedUrl = generateUrl(ListType.TOP_RATED, genreId);
                        topRatedFragment.getArguments().putString("url", topRatedUrl);
                        topRatedFragment.updateMovies();

                        String upcomingUrl = generateUrl(ListType.UPCOMING, genreId);
                        upcomingFragment.getArguments().putString("url", upcomingUrl);
                        upcomingFragment.updateMovies();

                        return true;
                    }
                });
        navigationView.getMenu().getItem(0).setChecked(true);

    }

    private void initAppBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.mipmap.movies_icon);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        ViewPager viewPager = findViewById(R.id.viewpager);
        if (viewPager != null) {
            setUpViewPager(viewPager, null);
        }
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setUpViewPager(ViewPager viewPager, String genreId) {
        viewPageAdapter = new MainPageFragmentAdapter(getSupportFragmentManager());
        popularFragment = getMoviesFragment(ListType.POPULAR, genreId);
        topRatedFragment = getMoviesFragment(ListType.TOP_RATED, genreId);
        upcomingFragment = getMoviesFragment(ListType.UPCOMING, genreId);
        viewPageAdapter.addFragment(popularFragment, "Popular");
        viewPageAdapter.addFragment(topRatedFragment, "Top Rated");
        viewPageAdapter.addFragment(upcomingFragment, "Upcoming");

        viewPager.setCurrentItem(0);
        viewPager.setAdapter(viewPageAdapter);
    }

    private MoviesList getMoviesFragment(ListType type, String genre) {
        MoviesList movies = new MoviesList();
        Bundle bundle = new Bundle();
        String url = generateUrl(type, genre);
        bundle.putString("url", url);
        movies.setArguments(bundle);
        return movies;
    }

    private String generateUrl(ListType type, String genre) {
        String url = "";
        switch(type) {
            case POPULAR: {
                url = "https://api.themoviedb.org/3/discover/movie?api_key=3a246368687fa6f8ad378b0bf1c8c630&language=en-US&sort_by=popularity.desc&page={0}";
                if(genre != null) {
                    url += "&with_genres=" + genre;
                }
            }
            break;

            case TOP_RATED:
            {
                url = "https://api.themoviedb.org/3/discover/movie?api_key=3a246368687fa6f8ad378b0bf1c8c630&language=en-US&sort_by=vote_average.desc&page={0}&vote_count.gte=200";
                if(genre != null) {
                    url += "&with_genres=" + genre;
                }
            }
                break;
            case UPCOMING:
            {
                SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
                String date = DATE_FORMAT.format(new Date());

                url = "https://api.themoviedb.org/3/discover/movie?api_key=3a246368687fa6f8ad378b0bf1c8c630&language=en-US&sort_by=popularity.desc&page=1&primary_release_date.gte=" + date;
                if(genre != null) {
                    url += "&with_genres=" + genre;
                }
            }
                break;
        }

        return url;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.showSoftInput(v.findFocus(), 0);
                }
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (this.mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}