package com.di.raine.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.di.raine.R;
import com.di.raine.branches.Comment;


import java.util.List;

/**
 * Created by kostis on 11/9/2016.
 */
public class ReviewAdapter extends BaseAdapter {
    private List<Comment> rReviewsList;
    private LayoutInflater rInflater;


    public ReviewAdapter(List<Comment> list, LayoutInflater inflater) {
        rReviewsList = list;
        rInflater = inflater;
    }

    @Override
    public int getCount() {
        return rReviewsList.size();
    }

    @Override
    public Object getItem(int position) {
        return rReviewsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewComment comment;

        if (convertView == null) {
            convertView = rInflater.inflate(R.layout.reviewrow,
                    null);
            comment = new ViewComment();

            comment.commentText = (TextView) convertView.findViewById(R.id.TextViewReviewText);
            comment.commentGrade = (TextView) convertView.findViewById(R.id.TextViewForReviewGrade);

            convertView.setTag(comment);
        } else {
            comment = (ViewComment) convertView.getTag();
        }

        Comment curComment = rReviewsList.get(position);
        comment.commentText.setText(curComment.getTextMessage());
        comment.commentGrade.setText(String.valueOf(curComment.getRating()));

        return convertView;
    }


    private class ViewComment {

        TextView commentText;
        TextView commentGrade;
    }
}
