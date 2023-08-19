package co.il.shivhit.viewmodel;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import co.il.shivhit.model.BlogPosts;
import co.il.shivhit.repository.BlogsRepository;

/*
public class BlogsViewModel extends ViewModel {
    private BlogsRepository repository;

    public BlogsViewModel(){
        repository = new BlogsRepository();
    }

    public MutableLiveData<BlogPosts> getAll(){
        MutableLiveData<BlogPosts> mutableLiveData = new MutableLiveData<>();

        repository.getAll()
                .addOnSuccessListener(new OnSuccessListener<BlogPosts>() {
                    @Override
                    public void onSuccess(BlogPosts blogPosts) {
                        mutableLiveData.setValue(blogPosts);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mutableLiveData.setValue(null);
                    }
                });
        return mutableLiveData;
    }
}

 */

public class BlogsViewModel extends ViewModel {
    private BlogsRepository repository;
    private MutableLiveData<BlogPosts> blogPostsLiveDAta;

    public BlogsViewModel() {
        repository = new BlogsRepository();
        blogPostsLiveDAta = new MutableLiveData<>();
    }

    public LiveData<BlogPosts> getAll(){
        blogPostsLiveDAta = repository.getAll();
        return blogPostsLiveDAta;
    }
}