package co.il.shivhit.repository;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import co.il.shivhit.model.BlogPost;
import co.il.shivhit.model.BlogPosts;

public class BlogsRepository {
    private final FirebaseFirestore    db;
    private final CollectionReference  collection;
    private final MutableLiveData<BlogPosts> blogsLiveData;
    private ListenerRegistration listenerRegistration;

    private TaskCompletionSource<Boolean> taskCompletion;

    public BlogsRepository(){
        db          = FirebaseFirestore.getInstance();
        collection  = db.collection("Blogs");
        blogsLiveData = new MutableLiveData<>();
    }

    public Task<Boolean> add (BlogPost blogPost){
        taskCompletion = new TaskCompletionSource<>();
        DocumentReference document = collection.document();
        blogPost.setIdFs(document.getId());
        document.set(blogsLiveData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        taskCompletion.setResult(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        taskCompletion.setResult(false);
                    }
                });
        return taskCompletion.getTask();
    }

                /*
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        if (querySnapshot != null && !querySnapshot.isEmpty()){
                            for (DocumentSnapshot document : querySnapshot) {
                                BlogPost blogPost = document.toObject(BlogPost.class);
                                if (blogPost != null)
                                    blogPosts.add(blogPost);
                            }
                        }
                        complete.setResult(blogPosts);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        complete.setException(e);
                    }
                });
                */


    public MutableLiveData<BlogPosts> getAll(){
        /*
        if (listenerRegistration == null){
            ListenerRegistration listener = collection.orderBy("date").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    BlogPosts blogPosts = new BlogPosts();
                    if (error != null){
                        blogPosts = null;
                    }
                    else{
                        if (value != null && !value.isEmpty()){
                            for (DocumentSnapshot document : value) {
                                BlogPost blogPost = document.toObject(BlogPost.class);
                                if (blogPost != null)
                                    blogPosts.add(blogPost);
                            }
                        }
                    }

                    blogsLiveData.postValue(blogPosts);
                }
            });

            listenerRegistration = listener;
        }
         */
        startListener();
        return blogsLiveData;
    }

    public void startListener(){
        if (listenerRegistration == null){
            ListenerRegistration listener = collection.orderBy("date").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    BlogPosts blogPosts = new BlogPosts();
                    if (error != null){
                        blogPosts = null;
                    }
                    else{
                        if (value != null && !value.isEmpty()){
                            for (DocumentSnapshot document : value) {
                                BlogPost blogPost = document.toObject(BlogPost.class);
                                if (blogPost != null)
                                    blogPosts.add(blogPost);
                            }
                        }
                    }

                    blogsLiveData.postValue(blogPosts);
                }
            });

            listenerRegistration = listener;
        }
    }

    private void stopListener(){
        if (listenerRegistration != null){
            listenerRegistration.remove();
            listenerRegistration = null;
        }
    }
}
