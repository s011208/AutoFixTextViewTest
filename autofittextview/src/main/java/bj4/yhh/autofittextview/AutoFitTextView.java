package bj4.yhh.autofittextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by yen-hsun_huang on 2017/4/28.
 */

public class AutoFitTextView extends AutoResizeTextView {
    public static final int TEXT_VIEW_TYPE_DEFAULT = 0;
    public static final int TEXT_VIEW_TYPE_TITLE = 1;
    public static final int TEXT_VIEW_TYPE_SUB_TITLE = 2;
    public static final int TEXT_VIEW_TYPE_LIST_MAIN_ITEM = 3;
    public static final int TEXT_VIEW_TYPE_LIST_SUB_ITEM = 4;
    public static final int TEXT_VIEW_TYPE_INDICATOR = 5;
    public static final int TEXT_VIEW_TYPE_NUMBER_IN_LIST = 6;

    private static final String TAG = "AutoFitTextView";
    private static final boolean DEBUG = true;

    private static final SparseArray<List<Integer>> TEXT_VIEW_TYPE_SIZE_LIST = new SparseArray<>();
    private final List<Integer> mTextViewTypeSize = new ArrayList<>();
    private int mTextViewType;

    public AutoFitTextView(Context context) {
        this(context, null);
    }

    public AutoFitTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoFitTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AutoFitTextView);
        setTextViewType(typedArray.getInt(R.styleable.AutoFitTextView_textType, TEXT_VIEW_TYPE_DEFAULT));
        typedArray.recycle();
    }

    private static int[] getTextSizeFromTypedArray(Context context, int resourceId) {
        int[] rtn;
        TypedArray array = context.getResources().obtainTypedArray(resourceId);
        List<Integer> sortingLst = new ArrayList<>();
        rtn = new int[array.length()];
        for (int i = 0; i < array.length(); ++i) {
            sortingLst.add(array.getDimensionPixelSize(i, 0));
        }
        array.recycle();
        Collections.sort(sortingLst);
        for (int i = 0; i < sortingLst.size(); ++i) {
            rtn[i] = sortingLst.get(i);
        }
        return rtn;
    }

    private synchronized static List<Integer> getTextViewTypeSize(Context context, int textViewType) {
        if (textViewType == TEXT_VIEW_TYPE_DEFAULT) {
            return null;
        }
        List<Integer> rtn = TEXT_VIEW_TYPE_SIZE_LIST.get(textViewType);
        if (rtn == null) {
            int[] values = null;
            switch (textViewType) {
                case TEXT_VIEW_TYPE_TITLE:
                    values = new int[]{10};
                    break;
                case TEXT_VIEW_TYPE_SUB_TITLE:
                    values = getTextSizeFromTypedArray(context, R.array.size_type_sub_title);
                    break;
                case TEXT_VIEW_TYPE_LIST_MAIN_ITEM:
                    values = getTextSizeFromTypedArray(context, R.array.size_type_list_main_item);
                    break;
                case TEXT_VIEW_TYPE_LIST_SUB_ITEM:
                    values = getTextSizeFromTypedArray(context, R.array.size_type_list_sub_item);
                    break;
                case TEXT_VIEW_TYPE_INDICATOR:
                    values = getTextSizeFromTypedArray(context, R.array.size_type_indicator);
                    break;
                case TEXT_VIEW_TYPE_NUMBER_IN_LIST:
                    values = getTextSizeFromTypedArray(context, R.array.size_type_number_in_list);
                    break;
            }
            if (values != null) {
                rtn = new ArrayList<>();
                for (int value : values) {
                    rtn.add(value);
                }
                TEXT_VIEW_TYPE_SIZE_LIST.put(textViewType, rtn);
            } else {
                return null;
            }
        }
        return rtn;
    }

    private static boolean isValidTextViewType(int type) {
        return (type >= TEXT_VIEW_TYPE_DEFAULT && type <= TEXT_VIEW_TYPE_NUMBER_IN_LIST);
    }

    public void setTextViewType(int type) {
        if (DEBUG) {
            Log.d(TAG, "setTextViewType: " + type);
        }
        if (!isValidTextViewType(type)) {
            throw new UnsupportedOperationException("unsupported type");
        }
        mTextViewType = type;
        resetTextViewTypeSize();
    }

    private void resetTextViewTypeSize() {
        mTextViewTypeSize.clear();
        if (mTextViewType == TEXT_VIEW_TYPE_DEFAULT) return;
        if (!isValidTextViewType(mTextViewType)) {
            throw new UnsupportedOperationException("unsupported type");
        }
        mTextViewTypeSize.addAll(getTextViewTypeSize(getContext(), mTextViewType));
        notifyTextChanged();
    }

    @Override
    int getTextSizeStrategy() {
        if (mTextViewType == TEXT_VIEW_TYPE_DEFAULT) {
            return super.getTextSizeStrategy();
        } else {
            int index = 0;
            while (index < mTextViewTypeSize.size()) {
                int value = testTextSize(mTextViewTypeSize.get(index));
                if (value < 0) {
                    ++index;
                } else if (value > 0) {
                    return mTextViewTypeSize.get(index == 0 ? 0 : index - 1);
                } else {
                    return mTextViewTypeSize.get(index);
                }
            }
            return mTextViewTypeSize.get(mTextViewTypeSize.size() - 1);
        }
    }
}
