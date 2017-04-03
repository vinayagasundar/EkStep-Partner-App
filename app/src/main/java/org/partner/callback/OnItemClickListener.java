package org.partner.callback;

import android.support.v7.widget.RecyclerView;

/**
 * Interface for RecyclerView item click event
 * @author vinayagasundar
 */

public interface OnItemClickListener {

    /**
     * When click on item in {@link RecyclerView} it'll be called
     * @param position item position in the {@link RecyclerView}
     */
    void onItemClick(int position);
}
