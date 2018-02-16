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
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import activity.CategoryActivity;
import model.Book;
import model.BookCategory;
import test.czreader.R;

/**
 * PublishFragment的RecyclerView适配器
 * Created by cz on 2017-6-15.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<BookCategory> mlist;

    private Context mcontext;


    public RecyclerViewAdapter(Context context,List<BookCategory> mlist)
    {
        mcontext=context;
        this.mlist=mlist;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_items,parent,false);
       final ViewHolder holder=new ViewHolder(view);
        holder.holderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position=holder.getAdapterPosition();
                BookCategory bookCategory =mlist.get(position);

                Intent intent = new Intent(mcontext, CategoryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("category", bookCategory);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtras(bundle);
                mcontext.startActivity(intent);
            }
        });


        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BookCategory category=mlist.get(position);
        String imgUrl=category.getImg();
        Picasso.with(mcontext).load(imgUrl).into(holder.imgBook);

        holder.tvBook.setText(category.getCategoryName());
        holder.tvSummary.setText(category.getSummary());

        if(position==0)
        {
            holder.tvBook.setTextColor(Color.parseColor("#d81e06"));
        }else if(position==1)
        {
            holder.tvBook.setTextColor(Color.parseColor("#08af3c"));
        }else
        {
            holder.tvBook.setTextColor(Color.parseColor("#1296db"));
        }




    }


    @Override
    public int getItemCount() {
        return mlist.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        View holderView;
       ImageView imgBook;
        TextView  tvBook;
        TextView  tvSummary;

       public ViewHolder(View itemView) {
           super(itemView);
           holderView=itemView;
           imgBook= (ImageView) itemView.findViewById(R.id.img_book);
           tvBook= (TextView) itemView.findViewById(R.id.tv_book);
           tvSummary= (TextView) itemView.findViewById(R.id.tv_summary);
       }

   }

}
