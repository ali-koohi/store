package ir.alikdev.store.adapter;

import ir.alikdev.store.models.Category;

public interface OnCategoryClickListener {

    void onAllClick(int position);

    void onItemClick(int position);
}
