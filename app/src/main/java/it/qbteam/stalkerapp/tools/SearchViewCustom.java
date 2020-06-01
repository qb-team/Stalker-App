package it.qbteam.stalkerapp.tools;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SearchViewCustom {

    protected int mSearchBackGroundResource = 0;
    protected int mSearchIconResource = 0;
    protected boolean mSearchIconInside = false;
    protected boolean mSearchIconOutside = false;
    protected int mSearchVoiceIconResource = 0;
    protected int mSearchTextColorResource = 0;
    protected int mSearchHintColorResource = 0;
    protected String mSearchHintText = "";
    protected int mInputType = Integer.MIN_VALUE;
    protected int mSearchCloseIconResource = 0;
    protected TextView.OnEditorActionListener mEditorActionListener;
    protected Resources mResources;

    public SearchViewCustom setSearchBackGroundResource(int searchBackGroundResource) {
        mSearchBackGroundResource = searchBackGroundResource;
        return this;
    }

    public SearchViewCustom setSearchIconResource(int searchIconResource, boolean inside, boolean outside) {
        mSearchIconResource = searchIconResource;
        mSearchIconInside = inside;
        mSearchIconOutside = outside;
        return this;
    }

    public SearchViewCustom setSearchVoiceIconResource(int searchVoiceIconResource) {
        mSearchVoiceIconResource = searchVoiceIconResource;
        return this;
    }

    public SearchViewCustom setSearchTextColorResource(int searchTextColorResource) {
        mSearchTextColorResource = searchTextColorResource;
        return this;
    }

    public SearchViewCustom setSearchHintColorResource(int searchHintColorResource) {
        mSearchHintColorResource = searchHintColorResource;
        return this;
    }

    public SearchViewCustom setSearchHintText(String searchHintText) {
        mSearchHintText = searchHintText;
        return this;
    }

    public SearchViewCustom setInputType(int inputType) {
        mInputType = inputType;
        return this;
    }

    public SearchViewCustom setSearchCloseIconResource(int searchCloseIconResource) {
        mSearchCloseIconResource = searchCloseIconResource;
        return this;
    }

    public SearchViewCustom setEditorActionListener(TextView.OnEditorActionListener editorActionListener) {
        mEditorActionListener = editorActionListener;
        return this;
    }

    public void format(SearchView searchView) {
        if (searchView == null) {
            return;
        }

        mResources = searchView.getContext().getResources();
        int id;

        if (mSearchBackGroundResource != 0) {
            id = getIdentifier("search_plate");
            View view = searchView.findViewById(id);
            view.setBackgroundResource(mSearchBackGroundResource);

            id = getIdentifier("submit_area");
            view = searchView.findViewById(id);
            view.setBackgroundResource(mSearchBackGroundResource);
        }

        if (mSearchVoiceIconResource != 0) {
            id = getIdentifier("search_voice_btn");
            ImageView view = (ImageView) searchView.findViewById(id);
            view.setImageResource(mSearchVoiceIconResource);
        }

        if (mSearchCloseIconResource != 0) {
            id = getIdentifier("search_close_btn");
            ImageView view = (ImageView) searchView.findViewById(id);
            view.setImageResource(mSearchCloseIconResource);
        }

        id = getIdentifier("search_src_text");
        TextView view = (TextView) searchView.findViewById(id);
        if (mSearchTextColorResource != 0) {
            view.setTextColor(mResources.getColor(mSearchTextColorResource));
        }
        if (mSearchHintColorResource != 0) {
            view.setHintTextColor(mResources.getColor(mSearchHintColorResource));
        }
        if (mInputType > Integer.MIN_VALUE) {
            view.setInputType(mInputType);
        }
        if (mSearchIconResource != 0) {
            ImageView imageView = (ImageView) searchView.findViewById(getIdentifier("search_mag_icon"));

            if (mSearchIconInside) {
                Drawable searchIconDrawable = mResources.getDrawable(mSearchIconResource);
                int size = (int) (view.getTextSize() * 1.25f);
                searchIconDrawable.setBounds(0, 0, size, size);

                SpannableStringBuilder hintBuilder = new SpannableStringBuilder("   ");
                hintBuilder.append(mSearchHintText);  // we can't append view.getHint() because that includes the old icon
                hintBuilder.setSpan(
                        new ImageSpan(searchIconDrawable),
                        1,
                        2,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );

                view.setHint(hintBuilder);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
            }
            if (mSearchIconOutside) {
                int searchImgId = getIdentifier("search_button");
                imageView = (ImageView) searchView.findViewById(searchImgId);

                imageView.setImageResource(mSearchIconResource);
            }
        }

        if (mEditorActionListener != null) {
            view.setOnEditorActionListener(mEditorActionListener);
        }
    }

    protected int getIdentifier(String literalId) {
        return mResources.getIdentifier(
                String.format("android:id/%s", literalId),
                null,
                null
        );
    }
}