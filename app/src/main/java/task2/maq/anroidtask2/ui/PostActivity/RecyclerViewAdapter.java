package task2.maq.anroidtask2.ui.PostActivity;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.List;

import task2.maq.anroidtask2.R;
import task2.maq.anroidtask2.data.pojo.Post;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Post> posts;

    private final String DATE_FORMAT = "dd-MM-yyyy hh:mm:ss";

    private WeakReference<PostsActivity> activityWeakReference;

    public RecyclerViewAdapter(PostsActivity activity) {
        activityWeakReference = new WeakReference<PostsActivity>(activity);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_card, parent,
                false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.author.setText(posts.get(position).getAuthor());
        holder.content.setText(posts.get(position).getContent());
        holder.date.setText(posts.get(position).getCreateDate().toString(DATE_FORMAT));
        PostsActivity activity = activityWeakReference.get();
        if (activity != null && posts.get(position).getImage() != null) {
            holder.imageView.setVisibility(View.VISIBLE);
            Bitmap b = activity.getImageFromCache(posts.get(position).getImage());
            if (b != null) {
                holder.imageView.setImageBitmap(b);
            } else {
                activity.getImage(posts.get(position).getImage(), position);
            }
        } else {
            holder.imageView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if (posts == null)
            return 0;
        return posts.size();
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    public void addPosts(List<Post> newPosts) {
        int startPos = posts.size();
        posts.addAll(newPosts);
        int finishPos = posts.size();
        notifyItemRangeChanged(startPos, finishPos-startPos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView author;
        private TextView date;
        private TextView content;
        private ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.author = (TextView) itemView.findViewById(R.id.author);
            this.date = (TextView) itemView.findViewById(R.id.date);
            this.content = (TextView) itemView.findViewById(R.id.content);
            this.imageView = (ImageView) itemView.findViewById(R.id.post_image);
        }
    }
}
