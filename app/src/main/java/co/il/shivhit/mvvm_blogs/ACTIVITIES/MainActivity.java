package co.il.shivhit.mvvm_blogs.ACTIVITIES;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import co.il.shivhit.model.BlogPosts;
import co.il.shivhit.mvvm_blogs.R;
import co.il.shivhit.viewmodel.BlogsViewModel;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends BaseActivity {

    private BlogsViewModel blogsLiveData;
    private BlogPosts blogPosts;
    private TextView txtKehila;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        blogsLiveData = new BlogsViewModel();

        blogPosts = new BlogPosts();

        initializeViews();
        setObservers();
    }

    @Override
    protected void initializeViews() {

        txtKehila = findViewById(R.id.txtKehila);

        setListeners();
    }

    @Override
    protected void setListeners() {

    }

    private void setObservers(){
        Log.d("PP", "ACT - calling 'getAll()'");
        blogsLiveData.getAll().observe(this, new Observer<BlogPosts>() {
            @Override
            public void onChanged(BlogPosts obsevableBlogPosts) {
                Log.d("PP", "ACT - onChanged : " + obsevableBlogPosts.size());
                if (obsevableBlogPosts != null){
                    blogPosts = obsevableBlogPosts;
                    if (blogPosts.size() > 0) {
                        if (blogPosts.get(0) != null){
                            txtKehila.setText(blogPosts.get(0).getContent());
                        }
                    }
                }
            }
        });
    }
}