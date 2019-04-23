package com.redkant.mymojo;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ajts.androidmads.sqliteimpex.SQLiteImporterExporter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.redkant.mymojo.db.Mojo;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

//    public RecyclerView.Adapter mMojoAdapter;

//    private MojoDatabaseHelper db;
//    public List<Mojo> listMojo;

//    private FragmentChart mChartFragment;

    public static final int ADD_MOJO_REQUEST = 1000;
    public static final int EDIT_MOJO_REQUEST = 1001;
    public static final int DELETE_MOJO_REQUEST = 1002;

    private static final int PERMISSION_REQUEST_CODE = 1010;

    SQLiteImporterExporter mSQLiteImporterExporter;
    public static String db_ = "mojo_db";

    SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
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

        MenuItem mi = navigationView.getMenu().findItem(R.id.nav_gki_measurements);
        mi.setChecked(true);
        onNavigationItemSelected(mi);
/*
        listMojo = new ArrayList<>();
        db = new MojoDatabaseHelper(this);

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, Integer.parseInt("-" + mPreferences.getInt("DaysCount", 7)));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss", Locale.ENGLISH);
        db.getFilteredMojo(listMojo, "create_date >= '" + df.format(c.getTime()) + "'");

        mMojoAdapter = new MojoAdapter(this, listMojo);
*/

/*
        RecyclerView recyclerView = findViewById(R.id.rvMojo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mMojoAdapter);
*/

/*
        mChartFragment = (FragmentChart)getSupportFragmentManager().findFragmentById(R.id.frChart);
        if (mChartFragment != null) {
            mChartFragment.setData(listMojo);
        }
*/

        mSQLiteImporterExporter = new SQLiteImporterExporter(getApplicationContext(), db_);
        mSQLiteImporterExporter.setOnImportListener(new SQLiteImporterExporter.ImportListener() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                Log.i("***", "onSuccess: " + message);
            }

            @Override
            public void onFailure(Exception exception) {
                Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("***", "onFailure: " + exception.getMessage());
            }
        });
        mSQLiteImporterExporter.setOnExportListener(new SQLiteImporterExporter.ExportListener() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Exception exception) {
                Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onResume() {
        RefreshMojoDataSet();

        super.onResume();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_menu, menu);
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Создадим новый фрагмент

        switch (item.getItemId()) {
            case R.id.nav_gki_measurements:
                navFragmentClick(GKIMeasurements.class);
                break;
            case R.id.nav_body_measurements:
                navFragmentClick(BodyMeasurements.class);
                break;
            case R.id.nav_settings:
                navSettingsClick();
                break;
            case R.id.nav_import:
                navImportClick();
                break;
            case R.id.nav_export:
                navExportClick();
                break;
            default:
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void navFragmentClick(Class fragmentClass) {
        Fragment fragment = null;
//        Class fragmentClass = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Вставляем фрагмент, заменяя текущий фрагмент
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        // Выделяем выбранный пункт меню в шторке
//        item.setChecked(true);
        // Выводим выбранный пункт в заголовке
//        setTitle(item.getTitle());
    }

    private void navSettingsClick() {
        Intent i = new Intent(this, MojoPreferenceActivity.class);
        startActivity(i);
    }

    private void navExportClick() {
        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle(R.string.warning_title);
        ad.setMessage(R.string.warning_export_db);

        ad.setPositiveButton(R.string.btn_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // check permissions
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {

                    String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/";
                    File directory = new File(path);

                    boolean fMakeDir = true;

                    if (!directory.exists()) {
                        fMakeDir = directory.mkdir();
                    }

                    if (fMakeDir && mSQLiteImporterExporter.isDataBaseExists()) {
                        try {
                            mSQLiteImporterExporter.exportDataBase(path);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "DB Doesn't Exists", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PERMISSION_REQUEST_CODE
                    );
                }
            }
        });

        ad.setNegativeButton(R.string.btn_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        // разрешаем нажатие кнопки Back
        ad.setCancelable(true);
        ad.show();
    }

    private void navImportClick() {
        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle(R.string.warning_title);
        ad.setMessage(R.string.warning_import_db);

        ad.setPositiveButton(R.string.btn_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // check permissions
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {

                    String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/";

                    try {
                        mSQLiteImporterExporter.importDataBase(path);

                        RefreshMojoDataSet();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PERMISSION_REQUEST_CODE
                    );
                }
            }
        });

        ad.setNegativeButton(R.string.btn_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        // разрешаем нажатие кнопки Back
        ad.setCancelable(true);
        ad.show();
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
                mojo.setCreateDate((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss", Locale.ENGLISH)).format(d));

                mojo.setKetoNumber(Float.parseFloat(data.getStringExtra("KETONE")));
                mojo.setGlucoseNumber(Integer.parseInt(data.getStringExtra("GLUCOSE")));
                mojo.setNote(data.getStringExtra("NOTE"));

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

        RefreshMojoDataSet();
    }

    private void RefreshMojoDataSet() {

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, Integer.parseInt("-" + mPreferences.getInt("DaysCount", 7)));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss", Locale.ENGLISH);

        RecyclerView.Adapter mojoAdapter = GKIMeasurements.getmMojoAdapter();
        MojoDatabaseHelper db = GKIMeasurements.getDb();
        List<Mojo> listMojo = GKIMeasurements.getListMojo();

        db.getFilteredMojo(listMojo, "create_date >= '" + df.format(c.getTime()) + "'");

        mojoAdapter.notifyDataSetChanged();
//        mChartFragment.setData(listMojo);

        GKIMeasurements.getmChartFragment().setData(listMojo);

    }
}
