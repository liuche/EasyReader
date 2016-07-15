package new_article_liuche.allizom.org.newarticle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class TopicChooser extends AppCompatActivity {

    final static String[] TOPIC_ARRAY = {"politics", "baseball", "science", "technology", "health", "psychology", "environment", "education", "pokemon", "global warming", "gaming", "food", "money", "computer programming", "investing", "philosophy", "books", "design", "entertainment", "cats", "artificial intelligence", "mathematics", "nutrition", "football", "sports", "Kim Kardashian", "music", "cooking", "Donald Trump", "Australia", "travel", "dining"};
    FloatingActionButton doneButton;
    final Context context = this;
    FlowLayout topicListView;
    LayoutInflater layoutInflater;
    int selectedCount = 0;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setTitle("Choose 5 topics");
        setContentView(R.layout.topic_chooser);

        layoutInflater = LayoutInflater.from(context);

        doneButton = (FloatingActionButton) findViewById(R.id.done_button);
        doneButton.setEnabled(false);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent articleIntent = new Intent(context, ArticleView.class);
                startActivity(articleIntent);
                finish();
            }
        });

        final View.OnClickListener topicClickListener = new OnTopicClickListener();
        topicListView = (FlowLayout) findViewById(R.id.topic_list);
        for (String topic : TOPIC_ARRAY) {
            final Button item = (Button) layoutInflater.inflate(R.layout.topic_item, null);
            item.setText(topic);
            item.setSelected(false);
            item.setOnClickListener(topicClickListener);
            topicListView.addView(item);
        }
    }

    class OnTopicClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            // Update selection.
            view.setSelected(!view.isSelected());

            // Update UI to match new state.
            final int colorInt;
            if (view.isSelected()) {
                selectedCount++;
                colorInt = R.color.bel_red_text;
            } else {
                colorInt = R.color.cardview_dark_background;
                selectedCount--;
            }
            view.setBackgroundColor(ContextCompat.getColor(context, colorInt));

            // Update "Done" button state.
            doneButton.setEnabled(selectedCount >= 5);
            if (selectedCount >= 5) {
                doneButton.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorAccent));
            } else {
                doneButton.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.bel_lightgrey_text));
            }
        }
    }
}
