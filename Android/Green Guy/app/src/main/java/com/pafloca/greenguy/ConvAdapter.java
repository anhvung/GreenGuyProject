package com.pafloca.greenguy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class ConvAdapter extends RecyclerView.Adapter {
    private String[] id;
    private String[] name;
    private Context mContext;
    private String[] pic;
    public final static String ALLCONVID="FROFILE_ID65468787484447";
    public final static int TAG=987849874;
    public ConvAdapter(String[] response_id, String[] response_name, String[] pic,Context context) {
        this.id=response_id;
        this.name=response_name;
        this.pic=pic;
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conv, parent, false);
            return new ConvHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ((ConvHolder) holder).bind(name[position],id[position],pic[position]);
    }

    @Override
    public int getItemCount() {
        return name.length;
    }
    private class ConvHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        ImageView user_pic;
        LinearLayout ll;
        ConvHolder(View itemView) {
            super(itemView);

            messageText =  itemView.findViewById(R.id.text_message_name);
            user_pic = itemView.findViewById(R.id.image_message_profile);
            ll = itemView.findViewById(R.id.item_conv);
        }

        void bind(String message,String picture,String id) {
            messageText.setText(message);
            // Format the stored timestamp into a readable String using method.
            byte[] decodedString = Base64.decode(picture, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            user_pic.setImageBitmap(decodedByte);
            ll.setTag(TAG,id);
            ll.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v)
                {
                    Intent intent=new Intent(mContext,ConvActivity.class);
                    intent.putExtra(ALLCONVID, String.valueOf( v.getTag(TAG)));
                    mContext.startActivity(intent);
                }
            });


        }
    }
}
