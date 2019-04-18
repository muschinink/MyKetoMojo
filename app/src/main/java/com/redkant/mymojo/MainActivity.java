package com.redkant.mymojo;

import android.content.Intent;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.redkant.mymojo.db.Mojo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mMojoRecyclerView;
    public RecyclerView.Adapter mMojoAdapter;

    private MojoDatabaseHelper db;
    public List<Mojo> listMojo;

    private FragmentChart mChartFragment;

    public static final int ADD_MOJO_REQUEST = 1000;
    public static final int EDIT_MOJO_REQUEST = 1001;
    public static final int DELETE_MOJO_REQUEST = 1002;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddEditMojoActivity.class);
                intent.putExtra("requestCode", ADD_MOJO_REQUEST);
                startActivityForResult(intent, ADD_MOJO_REQUEST);
            }
        });

        listMojo = new ArrayList<>();
        db = new MojoDatabaseHelper(this);
        db.getAllMojo(listMojo);
        mMojoAdapter = new MojoAdapter(this, listMojo);

        mMojoRecyclerView = findViewById(R.id.rvMojo);
        mMojoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMojoRecyclerView.setAdapter(mMojoAdapter);

        mChartFragment = (FragmentChart)getSupportFragmentManager().findFragmentById(R.id.frChart);
        mChartFragment.setData(listMojo);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            Toast.makeText(this, "search", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_import) {
            Toast.makeText(this, "import pressed", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_export) {
            Toast.makeText(this, "export pressed", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        MojoDatabaseHelper db = new MojoDatabaseHelper(this);

        Mojo mojo = new Mojo();
        mojo.setID(data.getIntExtra("ID", 0));

        if (requestCode == ADD_MOJO_REQUEST || requestCode == EDIT_MOJO_REQUEST) {
            try {
                SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.ENGLISH);
                Date d = df.parse(data.getStringExtra("CREATE_DATE") + " " + data.getStringExtra("CREATE_TIME"));
                mojo.setCreateDate((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss")).format(d));

                mojo.setKetoNumber(Float.parseFloat(data.getStringExtra("KETONE")));
                mojo.setGlucoseNumber(Integer.parseInt(data.getStringExtra("GLUCOSE")));
                mojo.setWeight(Float.parseFloat(data.getStringExtra("WEIGHT")));

                if (requestCode == EDIT_MOJO_REQUEST && mojo.getID() != 0) {
                    db.updateMojo(mojo);
                }

                if (requestCode == ADD_MOJO_REQUEST) {
                    db.addMojo(mojo);
                }
            } catch (ParseException e) {
                Log.i("***", "error");
            }
        }

        if (requestCode == DELETE_MOJO_REQUEST && mojo.getID() != 0) {
            db.deleteMojo(mojo);
        }

        db.getAllMojo(listMojo);
        mMojoAdapter.notifyDataSetChanged();

        mChartFragment.setData(listMojo);
    }

}
