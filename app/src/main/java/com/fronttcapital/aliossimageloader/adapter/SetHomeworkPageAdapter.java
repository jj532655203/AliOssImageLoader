package com.fronttcapital.aliossimageloader.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fronttcapital.aliossimageloader.SetHomeworkPageView;

import java.util.List;

public class SetHomeworkPageAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public SetHomeworkPageAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String ossImgPath) {

        SetHomeworkPageView pageView = (SetHomeworkPageView) helper.itemView;
        pageView.refreshView(ossImgPath);

    }
}
