package com.example.news_project_git;

import android.os.AsyncTask;
import android.widget.ListView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.news_project_git.Models.Articles;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ProcessInBackground_Alyaoum extends AsyncTask<Integer, Void, Exception> {
    ArrayList<Articles> listitem;

    //get the listView
    ListView lvRss;

    SwipeRefreshLayout swipeRefreshLayout = null;

    public ProcessInBackground_Alyaoum(ArrayList<Articles> listitem, ListView lvRss, SwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.listitem = listitem;
        this.lvRss = lvRss;
    }

    Exception exception = null;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    public InputStream getInputStream(URL url) {
        try {
            return url.openConnection().getInputStream();
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    protected Exception doInBackground(Integer... integers) {

        try {
            ArrayList<URL> url = new ArrayList<URL>();
            URL url1 = new URL("https://alyaoum24.com/rss");

            url.add(url1);
            //url.add(url2);
            for (int j = 0; j < 1; j++) {

                //process xml file
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

                factory.setNamespaceAware(false);

                XmlPullParser xpp = factory.newPullParser();

                xpp.setInput(this.getInputStream(url.get(j)), "UTF_8");

                boolean insideItem = false;
                ArrayList<String> titles = new ArrayList<>();
                ArrayList<String> links = new ArrayList<>();
                ArrayList<String> description = new ArrayList<>();
                ArrayList<String> imgs = new ArrayList<>();
                ArrayList<String> date = new ArrayList<>();
                ArrayList<String> author = new ArrayList<>();


                int eventType = xpp.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        if (xpp.getName().equalsIgnoreCase("item")) {
                            insideItem = true;
                        } else if (xpp.getName().equalsIgnoreCase("title")) {
                            if (insideItem) {
                                titles.add(xpp.nextText());
                            }
                        } else if (xpp.getName().equalsIgnoreCase("link")) {
                            if (insideItem) {
                                links.add(xpp.nextText());
                            }
                        } else if (xpp.getName().equalsIgnoreCase("description")) {
                            if (insideItem) {
                                //if (xpp.nextText().equalsIgnoreCase("<a ")) continue;
                                description.add(xpp.nextText());
                            }
                        }else if (xpp.getName().equalsIgnoreCase("pubDate")) {
                            if (insideItem) {
                                date.add(xpp.nextText());
                            }
                        } else if (xpp.getName().equalsIgnoreCase("dc:creator")) {
                            if (insideItem) {
                                author.add(xpp.nextText());
                            }
                        } else if (xpp.getName().equalsIgnoreCase("media:content")) {
                            if (insideItem) {
                                imgs.add(xpp.getAttributeValue(1));
                            }
                        }
                    } else if (eventType == XmlPullParser.END_DOCUMENT && xpp.getName().equalsIgnoreCase("item")) {
                        insideItem = false;
                    }
                    eventType = xpp.next();
                }

                for (int i = 0; i < titles.size(); i++) {
                    Articles ls = new Articles();
                    ls.setTitle(titles.get(i));
                    ls.setDescription(description.get(i));
                    ls.setUrl(links.get(i));
                    ls.setUrlToImage(imgs.get(i));
                    ls.setPublishedAt(date.get(i));
                    ls.setAuthor(author.get(i));
                    listitem.add(ls);

                }

            }
        } catch (MalformedURLException e) {
            exception = e;
        } catch (XmlPullParserException e) {
            exception = e;
        } catch (IOException e) {
            exception = e;
        }


        return exception;
    }

    @Override
    protected void onPostExecute(Exception s) {

        super.onPostExecute(s);

        listAdapter list_Adapter = new listAdapter(listitem);

        lvRss.setAdapter(list_Adapter);
        swipeRefreshLayout.setRefreshing(false);

    }

}
