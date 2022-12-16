package com.example.news_project_git;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.news_project_git.Models.Articles;
import com.example.news_project_git.Models.HelperClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class listAdapter extends BaseAdapter {
    ArrayList<Articles> listItem ;

    DatabaseReference myRef;

    List<Articles> Savedlist;

    listAdapter(ArrayList<Articles> list) {
        this.listItem = list;
    }

    @Override
    public int getCount() {
        return listItem.size();
    }

    @Override
    public Object getItem(int i) {
        return listItem.get(i).getTitle();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        myRef = FirebaseDatabase.getInstance("https://newsapp-25c3a-default-rtdb.europe-west1.firebasedatabase.app//").getReference(new HelperClass().getMacAddr());

        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());

        View view1 = layoutInflater.inflate(R.layout.items, null);

        TextView title = view1.findViewById(R.id.tvTitle);
        TextView author = view1.findViewById(R.id.tvSource);
        TextView pubDate = view1.findViewById(R.id.tvDate);
        ImageView img = view1.findViewById(R.id.image);
        ImageView star = (ImageView) view1.findViewById(R.id.star);


        title.setText(listItem.get(i).getTitle());
        author.setText(listItem.get(i).getAuthor());
        pubDate.setText(dateTime(listItem.get(i).getPublishedAt()));



        try {
            Picasso.get().load(listItem.get(i).getUrlToImage())
                    .error(R.drawable.ic_action_img)
                    .placeholder(R.drawable.ic_action_img).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .into(img);


        } catch (Exception e) {}

        if (listItem.get(i).isSaved()) new HelperClass().fillStar(star);

        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listItem.get(i).isSaved()) {
                    try {
                        Picasso.get().load(R.drawable.star)
                                .error(R.drawable.ic_action_img)
                                .placeholder(R.drawable.star).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                                .into(star);

                    } catch (Exception e) {}
                    myRef.child(listItem.get(i).getKey()).removeValue();
                    listItem.get(i).setSaved(false);
                }else{
                    new HelperClass().save(star,listItem,i);
                }
            }
        });

        return view1;
    }

    public String dateTime(String t){
        PrettyTime prettyTime = new PrettyTime(new Locale(getCountry()));
        String time = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
            Date date = simpleDateFormat.parse(t);
            time = prettyTime.format(date);
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public String getCountry(){
        Locale locale = Locale.getDefault();
        String country = locale.getCountry();
        return country.toLowerCase();
    }



}
