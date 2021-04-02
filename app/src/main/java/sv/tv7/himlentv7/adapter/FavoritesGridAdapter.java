package sv.tv7.himlentv7.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import sv.tv7.himlentv7.R;
import sv.tv7.himlentv7.helpers.Utils;

import static sv.tv7.himlentv7.helpers.Constants.CAPTION;
import static sv.tv7.himlentv7.helpers.Constants.EMPTY;
import static sv.tv7.himlentv7.helpers.Constants.ID_NULL;
import static sv.tv7.himlentv7.helpers.Constants.IMAGE_PATH;
import static sv.tv7.himlentv7.helpers.Constants.NULL_VALUE;
import static sv.tv7.himlentv7.helpers.Constants.SERIES;
import static sv.tv7.himlentv7.helpers.Constants.SERIES_AND_NAME;
import static sv.tv7.himlentv7.helpers.Constants.TYPE;

/**
 * Grid adapter for favorite items.
 */
public class FavoritesGridAdapter extends RecyclerView.Adapter<FavoritesGridAdapter.SimpleViewHolder> {

    private FragmentActivity activity = null;
    private Context context = null;
    private JSONArray elements = null;

    public FavoritesGridAdapter(FragmentActivity activity, Context context, JSONArray jsonArray) {
        this.activity = activity;
        this.context = context;
        this.elements = jsonArray;
    }

    public JSONObject getElementByIndex(int index) throws Exception {
        if (elements != null && elements.length() > index) {
            return elements.getJSONObject(index);
        }
        return null;
    }

    public JSONArray getElements() {
        return elements;
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout favoriteContainer = null;
        public ImageView favoriteImage = null;
        public TextView seriesAndName = null;
        public TextView caption = null;

        public SimpleViewHolder(View view) {
            super(view);

            favoriteContainer = view.findViewById(R.id.favoriteContainer);
            favoriteImage = view.findViewById(R.id.favoriteImage);
            seriesAndName = view.findViewById(R.id.seriesAndName);
            caption = view.findViewById(R.id.caption);

            // Calculate and set item height
            int itemHeight = Utils.dpToPx(calculateItemHeight());

            if (favoriteContainer != null) {
                ViewGroup.LayoutParams params = favoriteContainer.getLayoutParams();
                params.height = itemHeight;
                favoriteContainer.setLayoutParams(params);
            }

            // Calculate and set image width
            int imageWidth = Utils.dpToPx(calculateImageWidth());

            if (favoriteImage != null) {
                ViewGroup.LayoutParams params = favoriteImage.getLayoutParams();
                params.width = imageWidth;
                favoriteImage.setLayoutParams(params);
            }
        }
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.favorite_element, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder holder, final int position) {
        try {
            JSONObject obj = elements.getJSONObject(position);
            if (obj != null) {

                String value = Utils.getValue(obj, IMAGE_PATH);
                if (value != null && !value.equals(EMPTY) && !value.equals(NULL_VALUE) && !value.contains(ID_NULL)) {
                    Glide.with(context).asBitmap().load(value).into(holder.favoriteImage);
                }
                else {
                    Glide.with(context).asBitmap().load(R.drawable.fallback).into(holder.favoriteImage);
                }

                int imageSrc = 0;
                value = Utils.getValue(obj, TYPE);
                if (value != null) {
                    imageSrc = value.equals(SERIES) ? R.drawable.series : R.drawable.program;
                }

                value = Utils.getValue(obj, SERIES_AND_NAME);
                if (value != null) {
                    holder.seriesAndName.setText(value);
                }

                value = Utils.getValue(obj, CAPTION);
                if (value != null && value.length() > 0) {
                    holder.caption.setText(value);
                }
                else {
                    holder.caption.setText(EMPTY);
                }
            }
        }
        catch (Exception e) {
            Utils.toErrorPage(activity);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return this.elements.length();
    }

    private static double calculateItemHeight() {
        float width = Utils.getScreenHeightDp() - 124;
        return Math.floor(width / 3.5);
    }

    private static double calculateImageWidth() {
        double height = calculateItemHeight();
        return Math.round(height / 0.56);
    }
}
