package co.il.shivhit.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import co.il.shivhit.model.BlogPost;
import co.il.shivhit.model.BlogPosts;
import co.il.shivhit.repository.BlogsRepository;

public class BlogsViewModel extends ViewModel {
    private BlogsRepository repository;
    private MutableLiveData<BlogPosts> blogPostsLiveDAta;

    private MutableLiveData<Boolean>   successOperation;

    public BlogsViewModel() {
        repository = new BlogsRepository();
        blogPostsLiveDAta = new MutableLiveData<>();

        successOperation = new MutableLiveData<>();
    }

    public void save(BlogPost blogPost){
        successOperation.setValue(true);
    }

    public LiveData<Boolean> getSuccessOperation(){
        return successOperation;
    }

    public LiveData<BlogPosts> getAll(){
        blogPostsLiveDAta = repository.getAll();
        return blogPostsLiveDAta;
    }
}