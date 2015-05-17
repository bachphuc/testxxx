package com.learn.turtorial1;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by 09520_000 on 5/11/2015.
 */
public class ContactViewHolder extends RecyclerView.ViewHolder{
    protected TextView vName;
    protected TextView vSurname;
    protected TextView vEmail;
    protected TextView vTitle;
    protected TextView vFeedTitle;
    protected ImageView vImage;

    public ContactViewHolder(View view){
        super(view);
        vName = (TextView) view.findViewById(R.id.txtName);
        vSurname = (TextView) view.findViewById(R.id.txtSurname);
        vEmail = (TextView) view.findViewById(R.id.txtEmail);
        vTitle = (TextView) view.findViewById(R.id.title);
        vFeedTitle = (TextView) view.findViewById(R.id.feed_title);
        vImage = (ImageView)view.findViewById(R.id.image);

    }
}
