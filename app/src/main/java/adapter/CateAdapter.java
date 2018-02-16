package adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import model.Book;
import test.czreader.R;
import util.Util;

/**
 * Created by Administrator on 2017-6-19.
 */

public class CateAdapter extends BaseAdapter {

    private static final String TAG = "CateAdapter";

    private LayoutInflater mInflater;

    private List<Book> books;

    Context context;

    public CateAdapter(List<Book> books, Context context) {
        this.books = books;
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return books.size();
    }

    @Override
    public Object getItem(int position) {
        return books.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.frg_book_list_item, null);
            viewHolder.imgCover = (ImageView) convertView.findViewById(R.id.img_cate_list_cover);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_cate_list_title);
            viewHolder.tvSummary = (TextView) convertView.findViewById(R.id.tv_cate_list_summary);
            viewHolder.tvAuthor = (TextView) convertView.findViewById(R.id.tv_cate_list_author);
            viewHolder.tvDiscount = (TextView) convertView.findViewById(R.id.tv_cate_list_discount);
            viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.tv_cate_list_price);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Book book =books.get(position);

        Picasso.with(context).load(book.getCover()).into(viewHolder.imgCover);

        viewHolder.tvTitle.setText(book.getBookName());
        viewHolder.tvSummary.setText(book.getDescribe());
        viewHolder.tvAuthor.setText(book.getAuthor());
        return convertView;
    }


    class ViewHolder {
        TextView tvTitle, tvSummary, tvAuthor,tvDiscount,tvPrice;
        ImageView imgCover;
    }
}
