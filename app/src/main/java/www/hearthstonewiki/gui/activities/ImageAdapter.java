package www.hearthstonewiki.gui.activities;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;

import com.squareup.picasso.Picasso;

import www.hearthstonewiki.R;

public class ImageAdapter extends CursorAdapter {


    public ImageAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = lInflater.inflate(R.layout.card_item, viewGroup, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.cardImageView);
        Picasso.with(context)
                .load("http://wow.zamimg.com/images/hearthstone/cards/enus/original/EX1_165.png")
                .resize(434, 658)
                .into(imageView);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView imageView = (ImageView) view.findViewById(R.id.cardImageView);
        Picasso.with(context)
                .load("http://wow.zamimg.com/images/hearthstone/cards/enus/original/EX1_165.png")
                .resize(434, 658)
                .into(imageView);

        //SimpleCursorAdapter simpleCursorAdapter;
        //simpleCursorAdapter.ser
    }
}
