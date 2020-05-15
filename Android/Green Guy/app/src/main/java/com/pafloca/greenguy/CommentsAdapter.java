package com.pafloca.greenguy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;

import static com.pafloca.greenguy.MyProfileActivity.drawableToBitmap;

class CommentsAdapter extends RecyclerView.Adapter {
    private ArrayList<String> commentsName;
    private ArrayList<String> commentsMsg;
    private ArrayList<String> commentsDate;
    private ArrayList<String> commentsUserPic;
    private Context mContext;
    Bitmap defaultPic;
    public CommentsAdapter(ArrayList<String> commentsUserPic,ArrayList<String> commentsName, ArrayList<String> commentsMsg, ArrayList<String> commentsDate, Context context) {
        this.commentsName=commentsName;
        this.commentsMsg=commentsMsg;
        this.commentsDate=commentsDate;
        this.commentsUserPic=commentsUserPic;
        mContext=context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conmment, parent, false);
        return new CommHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((CommHolder) holder).bind(commentsUserPic.get(position),commentsName.get(position),commentsMsg.get(position),commentsDate.get(position));

    }

    @Override
    public int getItemCount() {
        return commentsName.size();
    }
    private class CommHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView auteur;
        TextView datev;

        ImageView user_pic;
        ImageView pic;
        CommHolder(View itemView) {
            super(itemView);

            auteur =  itemView.findViewById(R.id.auteur);
            messageText =  itemView.findViewById(R.id.commtxt);
            datev =  itemView.findViewById(R.id.date);
            pic = itemView.findViewById(R.id.commPIc);
            user_pic = itemView.findViewById(R.id.image_message_profile);
        }

        @SuppressLint("ClickableViewAccessibility")
        void bind(String userpic,String name, String msg, String date) {
            byte[] decodedString = Base64.decode(userpic, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            if(userpic.length()>100){

                user_pic.setImageBitmap(decodedByte);
            }
            else{
                Drawable d = mContext.getResources().getDrawable(R.mipmap.default_profile_foreground);
                defaultPic=drawableToBitmap(d);
                user_pic.setImageBitmap(defaultPic);
            }

            auteur.setText(name);
            datev.setText(getDate(Long.parseLong(date), "dd/MM/yyyy hh:mm"));

            if (msg.length()>100){
                decodedString = Base64.decode(msg, Base64.DEFAULT);
                decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                pic.setImageBitmap(decodedByte);
                messageText.setVisibility(View.GONE);

                pic.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch(event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                // PRESSED
                                return true; // if you want to handle the touch event
                            case MotionEvent.ACTION_UP:
                                // RELEASED
                                return true; // if you want to handle the touch event
                        }
                        return false;
                    }
                });
            }
            else{
                pic.setVisibility(View.GONE);
                messageText.setText(msg);
            }





        }
    }
    public static String getDate(long milliSeconds, String dateFormat)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
