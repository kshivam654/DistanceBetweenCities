package app.sharma.distancebetweencities;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    private EditText mSearchFeild;
    private RecyclerView recyclerView;
    Context context;
    ArrayList<Places> arrayList = new ArrayList<>();

    public SearchAdapter(Context context, ArrayList<Places> arrayList, EditText mSearchFeild, RecyclerView recyclerView) {
        this.context = context;
        this.arrayList = arrayList;
        this.mSearchFeild = mSearchFeild;
        this.recyclerView = recyclerView;
    }

    class SearchViewHolder extends RecyclerView.ViewHolder {

        //        TextView profileImage;
        TextView name, description;
        RelativeLayout relativeLayout;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
//            profileImage = (TextView) itemView.findViewById(R.id.profile_image);
            name = (TextView) itemView.findViewById(R.id.name_text);
//            description = (TextView) itemView.findViewById(R.id.description_text);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.parent_layout);
        }

        public void onClick(final int position, final String name) {
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSearchFeild.setText(name);
                    recyclerView.removeAllViews();
//                    Intent intent = new Intent(context, SecondActivity.class);
//                    context.startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_layout, parent, false);
        return new SearchAdapter.SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        holder.name.setText(arrayList.get(position).getName());
//        holder.profileImage.setText(arrayList.get(position).getImage());
//        switch (arrayList.get(position).getImage().toString()){
//            case "HOTEL": holder.profileImage.setBackgroundColor(ContextCompat.getColor(context, R.color.pink));
//                holder.profileImage.setTextColor(ContextCompat.getColor(context, R.color.darkpink));
//            break;
//            case "PLACE": holder.profileImage.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
//                holder.profileImage.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
//                break;
//            case "VILLA": holder.profileImage.setBackgroundColor(ContextCompat.getColor(context, R.color.yellow));
//                holder.profileImage.setTextColor(ContextCompat.getColor(context, R.color.darkyellow));
//                break;
//        }
//        holder.description.setText(arrayList.get(position).getDescription());

        holder.onClick(position, arrayList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}