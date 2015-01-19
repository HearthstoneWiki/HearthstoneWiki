package www.hearthstonewiki.gui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import www.hearthstonewiki.R;

public class ImageAdapter extends CursorAdapter {


    public ImageAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = lInflater.inflate(R.layout.card_list_item, viewGroup, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView imageView = (ImageView) view.findViewById(R.id.cardImageView);

        String cardID = cursor.getString(0);
        String PIC_SERVER_URL = "http://wow.zamimg.com/images/hearthstone/cards/enus/original/";
        String PIC_EXTENSION = ".png";
        String url = PIC_SERVER_URL + cardID + PIC_EXTENSION;

        Drawable pic = context.getResources().getDrawable(R.drawable.card_back);

        Picasso.with(context)
                .load(url)
                .resize(434, 585)
                .placeholder(pic)
                .centerCrop()
                .into(imageView);
        view.setTag(cardID);
    }
}
