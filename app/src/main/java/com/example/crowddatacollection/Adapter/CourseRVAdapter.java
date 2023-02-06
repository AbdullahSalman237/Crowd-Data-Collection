package com.example.crowddatacollection.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crowddatacollection.Details;
import com.example.crowddatacollection.R;

import java.util.ArrayList;

public class CourseRVAdapter extends RecyclerView.Adapter<CourseRVAdapter.ViewHolder> {
    // creating variables for our ArrayList and context
    private ArrayList<Details> DetailsArrayList;
    private Context context;

    // creating constructor for our adapter class
    public CourseRVAdapter(ArrayList<Details> DetailsArrayList, Context context) {
        this.DetailsArrayList = DetailsArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CourseRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.detail_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CourseRVAdapter.ViewHolder holder, int position) {
        // setting data to our text views from our modal class.
        Details courses = DetailsArrayList.get(position);
        String NO =  String.valueOf(position+1)+")";
        holder.no.setText(NO);
        holder.longitude.setText(String.valueOf( courses.getLongitude()));
        holder.latitude.setText(String.valueOf( courses.getLatitude()));
        holder.time.setText(courses.getDateAndTime());
    }

    @Override
    public int getItemCount() {
        // returning the size of our array list.
        return DetailsArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.
        private final TextView longitude;
        private final TextView latitude;
        private final TextView time;
        private final TextView no;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            no=itemView.findViewById(R.id.no);
            longitude = itemView.findViewById(R.id.idTVCourseName);
            latitude = itemView.findViewById(R.id.idTVCourseDuration);
            time= itemView.findViewById(R.id.idTVCourseDescription);
        }
    }
}
