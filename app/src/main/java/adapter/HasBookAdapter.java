package adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;


import activity.LocalReaderActivity;
import activity.OnLineReadActivity;
import model.LocalBook;
import test.czreader.R;

/**
 * PublishFragment的RecyclerView适配器
 * Created by cz on 2017-6-30.
 */

public class HasBookAdapter extends RecyclerView.Adapter<HasBookAdapter.ViewHolder> {

    private List<LocalBook> mlist;

    private Context mcontext;


    public HasBookAdapter(Context context, List<LocalBook> mlist)
    {
        mcontext=context;
        this.mlist=mlist;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_hasbook_items,parent,false);
       final ViewHolder holder=new ViewHolder(view);
        holder.holderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position=holder.getAdapterPosition();
                LocalBook localBook =mlist.get(position);

                Intent intent = new Intent(mcontext, LocalReaderActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("localBookItem", localBook);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtras(bundle);
                mcontext.startActivity(intent);
            }
        });


        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LocalBook localBook=mlist.get(position);

        holder.imgBook.setImageResource(R.drawable.bookcover);
        holder.tvTitle.setText(localBook.getbName());

    }


    @Override
    public int getItemCount() {
        return mlist.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        View holderView;
       ImageView imgBook;
        TextView  tvTitle;


       public ViewHolder(View itemView) {
           super(itemView);
           holderView=itemView;
           imgBook= (ImageView) itemView.findViewById(R.id.img_hasbook);
           tvTitle= (TextView) itemView.findViewById(R.id.tv_hasbook_title);

       }

   }

}
