package new_article_liuche.allizom.org.newarticle;

import com.androidzeitgeist.featurizer.Featurizer;
import com.androidzeitgeist.featurizer.features.WebsiteFeatures;
import com.daprlabs.cardstack.SwipeDeck;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ArticleView extends Activity {
    public static final String LOGTAG = "ArticleView";

    final String[] STACK_URLS = {"http://www.vox.com/2016/7/11/12129162/pokemon-go-android-ios-game",
                                 "http://www.thedailybeast.com/articles/2016/07/11/pokemon-go-is-a-hacker-s-dream.html",
                                 "http://www.bloomberg.com/news/articles/2016-07-11/nintendo-s-pokemon-hit-gives-early-taste-of-smartphone-success",
                                 "http://appleinsider.com/articles/16/07/11/pokemon-go-is-earning-apples-app-store-3x-as-much-money-as-nintendo",
                                 "http://www.wsj.com/articles/pokemon-go-craze-raises-safety-issues-1468365058",
                                 "http://www.vox.com/2016/7/12/12159198/pokemon-go-exercise-increase/in/11925171"};
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_container);

        final Featurizer featurizer = new Featurizer();

        final LinkedList<CardModel> cardList = new LinkedList();

        final SwipeDeck container = (SwipeDeck) findViewById(R.id.swipe_deck);
        final SwipeDeckAdapter adapter = new SwipeDeckAdapter(cardList, context);
        container.setAdapter(adapter);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                for (String url : STACK_URLS) {
                    try {
                        final WebsiteFeatures features = featurizer.featurize(url);
                        cardList.add(new CardModel(context, url, features.getTitle(), features.getImageUrl(), features.getDescription()));
                    } catch (IOException e) {
                        Log.e(LOGTAG, "Error loading url, skipping");
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
        thread.start();

        container.setEventCallback(new SwipeDeck.SwipeEventCallback() {
            @Override
            public void cardSwipedLeft(int position) {}

            @Override
            public void cardSwipedRight(int position) {
                final Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(((CardModel) adapter.getItem(position)).uri);
                startActivity(intent, ActivityOptions.makeScaleUpAnimation(container, container.getWidth(), container.getHeight()/2, 100, 100).toBundle());
            }

            @Override
            public void cardsDepleted() {}

            @Override
            public void cardActionDown() {}

            @Override
            public void cardActionUp() {}
        });
    }

    public static class CardModel {
        Uri uri;
        String title;
        String imageUrl;
        String description;

        public CardModel(Context context, String url, String title, String imageUrl, String description) {
            this.uri = Uri.parse(url);
            this.title = title;
            this.imageUrl = imageUrl;
            this.description = description;
        }
    }


    public class SwipeDeckAdapter extends BaseAdapter {

        private List<CardModel> data;
        private Context context;

        public SwipeDeckAdapter(List<CardModel> data, Context context) {
            this.data = data;
            this.context = context;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View v = convertView;
            if(v == null){
                LayoutInflater inflater = getLayoutInflater();
                v = inflater.inflate(R.layout.card_item, parent, false);
            }

            final CardModel cardModel = data.get(position);

            final ImageView imageView = (ImageView) v.findViewById(R.id.card_image);
            Picasso.with(context).setLoggingEnabled(true);
            Picasso.with(context).load(cardModel.imageUrl).into(imageView);

            ((TextView) v.findViewById(R.id.card_title)).setText(cardModel.title);
            ((TextView) v.findViewById(R.id.card_description)).setText(cardModel.description);

            return v;
        }
    }

}
