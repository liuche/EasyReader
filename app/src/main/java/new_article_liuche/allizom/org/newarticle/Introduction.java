package new_article_liuche.allizom.org.newarticle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Introduction extends AppCompatActivity {
    final Context context = this;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.introduction);
        ((Button) findViewById(R.id.start_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent topicIntent = new Intent(context, TopicChooser.class);
                startActivity(topicIntent);
                finish();
            }
        });
    }
}
