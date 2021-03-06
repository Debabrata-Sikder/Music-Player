package com.example.music_player;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.example.music_player.PlayerActivity.mediaPlayer;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    ListView listView;
    String[] items;
    Button hbtnnext,hbtnprev,hbtnpause;
    TextView txtnp;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();



        listView = findViewById(R.id.listviewsongs);
        txtnp = findViewById(R.id.txtnp);

        runtimepermission();

        hbtnnext = findViewById(R.id.hbtnnext);
        hbtnprev = findViewById(R.id.hbtnprev);
        hbtnpause = findViewById(R.id.hbtnpause);



        txtnp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer==null || txtnp.getText().toString().equals(""))
                {
                    Toast.makeText(MainActivity.this, "No song is playing", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent mIntent=new Intent(MainActivity.this, PlayerActivity.class);
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(mIntent);
                }
            }
        });


        hbtnpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer!=null)
                {
                    if (mediaPlayer.isPlaying())
                    {
                        hbtnpause.setBackgroundResource(R.drawable.ic_play_circle);
                        mediaPlayer.pause();

                    }
                    else
                    {
                        hbtnpause.setBackgroundResource(R.drawable.ic_pause_circle);
                        mediaPlayer.start();
                    }
                }
            }
        });


        hbtnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer!=null|| mediaPlayer ==null)
                {
                    hbtnnext.animate().rotation(hbtnnext.getRotation()-360).start();

                }
            }
        });
        hbtnprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer!=null || mediaPlayer ==null)
                {
                    hbtnprev.animate().rotation(hbtnprev.getRotation()+360).start();

                }
            }
        });


    }

    public void runtimepermission()
    {
        Dexter.withContext(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        display();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                    }
                }).check();
    }

    public ArrayList<File> findSong (File file)
    {
        ArrayList<File> arrayList = new ArrayList<>();

        File[] files = file.listFiles();
        for (File singlefile: files)
        {
            if (singlefile.isDirectory() && !singlefile.isHidden())
            {
                arrayList.addAll(findSong(singlefile));
            }
            else
            {
                if (singlefile.length()>2*(1024*1024) && (singlefile.getName().endsWith(".mp3")||singlefile.getName().endsWith(".wav")||singlefile.getName().endsWith(".flac")))
                {
                    arrayList.add(singlefile);

                }
            }

        }
        return arrayList;
    }

    void display()
    {
        final ArrayList<File> mySongs = findSong(Environment.getExternalStorageDirectory());

        items = new String[mySongs.size()];
        for (int i=0;i<mySongs.size();i++)
        {
            items[i] = mySongs.get(i).getName().toString().replace(".mp3","").replace(".wav","").replace(".flac","");
        }
        /*ArrayAdapter<String> myAdapter  = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, items);
        listView.setAdapter(myAdapter);*/

        customAdapter customAdapter = new customAdapter();
        listView.setAdapter(customAdapter);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String songName = (String) listView.getItemAtPosition(position);
                String sname = mySongs.get(position).getName().toString();
                startActivity(new Intent(getApplicationContext(),PlayerActivity.class)
                        .putExtra("songs", mySongs)
                        .putExtra("songname",songName)
                        .putExtra("pos", position));


            }
        });
    }
    class customAdapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = getLayoutInflater().inflate(R.layout.list_song,null);
            TextView textsong = view.findViewById(R.id.txtsongname);
            textsong.setSelected(true);
            textsong.setText(items[position]);
            return view;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId())
        {
            case R.id.youtube:
                Intent mIntent1=new Intent(MainActivity.this, YoutubeActivity.class);
                mIntent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mIntent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(mIntent1);
                break;
            case R.id.about:
                Intent mIntent2=new Intent(MainActivity.this, AboutActivity.class);
                mIntent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mIntent2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(mIntent2);

                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startActivity(startMain);
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        if (mediaPlayer!=null)
        {
            if (mediaPlayer.isPlaying())
            {
                hbtnpause.setBackgroundResource(R.drawable.ic_pause_circle);
            }
            else {
                hbtnpause.setBackgroundResource(R.drawable.ic_play_circle);
            }
            Intent i = getIntent();
            String songName = i.getStringExtra(PlayerActivity.EXTRA_NAME);
            txtnp.setText(songName);
            txtnp.setSelected(true);
        }
        else
        {
            hbtnpause.setBackgroundResource(R.drawable.ic_play_circle);
        }
        super.onResume();
    }

}
