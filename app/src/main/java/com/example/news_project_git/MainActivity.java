package com.example.news_project_git;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.news_project_git.Models.Articles;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ArrayList<Articles> listitem;

    //get the listView
    ListView lvRss;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView Source;
    CardView hespress,bein,alyaoum,alarabiya,france;
    ImageView saved_items;
    String loc;

    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        lvRss = (ListView) findViewById(R.id.recyclerView);

        saved_items = (ImageView) findViewById(R.id.saved_items);

        hespress = (CardView) findViewById(R.id.hespress);

        bein = (CardView) findViewById(R.id.bein);

        alyaoum = (CardView) findViewById(R.id.alyaoum);

        alarabiya = (CardView) findViewById(R.id.alarabiya);

        france = (CardView) findViewById(R.id.france);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);

        listitem = new ArrayList<Articles>();

        Source = (TextView) findViewById(R.id.Source);

        loc = "BEIN";

        lvRss.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Uri uri = Uri.parse(listitem.get(position).getUrl());

                Intent intent = new Intent(Intent.ACTION_VIEW, uri);

                startActivity(intent);
            }
        });


        ProcessInBackground_Bein p = (ProcessInBackground_Bein) new ProcessInBackground_Bein(listitem,lvRss, swipeRefreshLayout).execute();


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
            try {
                listitem.clear();
                if (loc.equals("BEIN")) {
                    new ProcessInBackground_Bein(listitem, lvRss, swipeRefreshLayout).execute();
                }else if (loc.equals("HESPRESS")){
                        new ProcessInBackground_Hespress(listitem,lvRss,swipeRefreshLayout, new URL("https://www.hespress.com/rss")).execute();
                }else if (loc.equals("ALYAOUM")){
                    new ProcessInBackground_Alyaoum(listitem,lvRss,swipeRefreshLayout).execute();
                }else if (loc.equals("FRANCE")){
                    new ProcessInBackground_France24(listitem,lvRss,swipeRefreshLayout).execute();
                }else if (loc.equals("ALARABIYA")){
                    new ProcessInBackground_Hespress(listitem,lvRss,swipeRefreshLayout,new URL("https://www.alarabiya.net/feed/rss2/ar/technology.xml")).execute();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            }

        });

        hespress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             try {
                swipeRefreshLayout.setRefreshing(true);
                Source.setText("HESPRESS");
                loc = "HESPRESS";
                listitem.clear();
                    new ProcessInBackground_Hespress(listitem,lvRss,swipeRefreshLayout,new URL("https://www.hespress.com/rss")).execute();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });

        bein.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeRefreshLayout.setRefreshing(true);
                Source.setText("BEIN SPORT");
                loc = "BEIN";
                listitem.clear();
                new ProcessInBackground_Bein(listitem,lvRss, swipeRefreshLayout).execute();
            }
        });

        alyaoum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeRefreshLayout.setRefreshing(true);
                Source.setText("ALYAOUM24");
                loc = "ALYAOUM";
                listitem.clear();
                new ProcessInBackground_Alyaoum(listitem,lvRss, swipeRefreshLayout).execute();
            }
        });

        france.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeRefreshLayout.setRefreshing(true);
                Source.setText("FRANCE24");
                loc = "FRANCE";
                listitem.clear();
                new ProcessInBackground_France24(listitem,lvRss, swipeRefreshLayout).execute();
            }
        });

        alarabiya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            try {
                swipeRefreshLayout.setRefreshing(true);
                Source.setText("ALARABIYA");
                loc = "ALARABIYA";
                listitem.clear();
                new ProcessInBackground_Hespress(listitem,lvRss, swipeRefreshLayout,new URL("https://www.alarabiya.net/feed/rss2/ar/technology.xml")).execute();
            } catch (MalformedURLException e) {
                    e.printStackTrace();
             }
            }
        });

        saved_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Saved_Items.class);
                startActivity(intent);
            }
        });


    }


}