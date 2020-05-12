package com.pafloca.greenguy;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static com.pafloca.greenguy.IdentificationActivity.sep;

class MainAdapter extends RecyclerView.Adapter {
    private ArrayList<String> dates;
    private ArrayList<String> msg;
    private String hisname;
    private String myname;
    private Bitmap pic;
    private Context mContext;
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private static final String sep2="!sepPourlEsmSg?";
    String myid;
    public MainAdapter(int id,ArrayList<String> dates, ArrayList<String> msg, Bitmap pic, String hisname, String myname,Context context) {
        this.dates=dates;
        this.msg=msg;
        this.pic=pic;
        this.hisname=hisname;
        this.myname=myname;
        mContext = context;
        myid=String.valueOf(id);
    }

    @Override
    public int getItemViewType(int position) {
        if (myid.equals(msg.get(position).split(sep2)[0])){
            return VIEW_TYPE_MESSAGE_SENT;
        }
        return VIEW_TYPE_MESSAGE_RECEIVED ;

    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_msg_send, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_msg_receive, parent, false);
            return new ReceivedMessageHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String message = msg.get(position);
        String date =dates.get(position);
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message,date);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message,date,pic,hisname);
        }
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }
    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;

        SentMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
        }

        void bind(String message,String date) {
            if(date==null){
                Log.d("greend", "pic null");
            }
            if(timeText==null){
                Log.d("greend", "imageview null");
            }

            if(message==""||message.isEmpty()||message==null){
                timeText.setVisibility(View.GONE);
                messageText.setVisibility(View.GONE);
            }
            else {
                message=message.substring(myid.length()+sep2.length(),message.length());
                messageText.setText(message);
                timeText.setText(date);
            }


        }
    }
    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText;
        ImageView profileImage;


        ReceivedMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
            nameText = (TextView) itemView.findViewById(R.id.text_message_name);
            profileImage = (ImageView) itemView.findViewById(R.id.image_message_profile);

        }

        void bind(String message,String date,Bitmap pic,String hisname) {
            if(date==null){
                Log.d("greend", "pic null");
            }
            if(timeText==null){
                Log.d("greend", "imageview null");
            }

            if(message==""||message.isEmpty()||message==null){
                timeText.setVisibility(View.GONE);
                nameText.setVisibility(View.GONE);
                profileImage.setVisibility(View.GONE);
                messageText.setVisibility(View.GONE);
            }
            else{
                message=message.substring(myid.length()+sep2.length(),message.length());
                messageText.setText(message);

                timeText.setText(date);
                nameText.setText(hisname);
                profileImage.setImageBitmap(pic);
            }



        }
    }
}
