package www.hearthstonewiki.gui.views;

import android.graphics.Bitmap;

import com.squareup.picasso.Transformation;

/**
 * Created by uzzz on 19.01.15.
 */
public class CardTransformation implements Transformation {

    @Override
    public Bitmap transform(Bitmap source) {

        Bitmap result = Bitmap.createBitmap(source, 5, 70, source.getWidth() - 10, source.getHeight() - 100);
        if (result != source) {
            source.recycle();
        }
        return result;
    }

    @Override public String key() { return "card_crop"; }
}
