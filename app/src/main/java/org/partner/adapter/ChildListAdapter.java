package org.partner.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.partner.R;
import org.partner.callback.OnItemClickListener;
import org.partner.model.Child;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Adapter class to child display child
 * @author vinayagasundar
 */

public class ChildListAdapter extends RecyclerView.Adapter<ChildListAdapter.ChildViewHolder> {


    /**
     * It'll hold the original value of the data
     */
    private List<Child> mOriginalData;

    /**
     * Copy of the data used to display in the RecyclerView
     */
    private List<Child> mData;


    private OnItemClickListener mOnItemClickListener;

    public ChildListAdapter(List<Child> mData) {
        this.mOriginalData = mData;
        this.mData = new ArrayList<>(this.mOriginalData);
    }


    @Override
    public ChildViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_child_row,
                parent, false);
        return new ChildViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ChildViewHolder holder, int position) {
        Child child = mData.get(position);

        int standard = child.getStandard();
        String standardText = "%s %s";

        if (standard > 0) {
            standardText = String.format(Locale.ENGLISH, standardText, "Class", standard);
        } else {
            standardText = String.format(Locale.ENGLISH, standardText, "Class", "No Data");
        }

        if (TextUtils.isEmpty(child.getUid())) {
            holder.mChildHandleText.setText(child.getFullName());
        } else {
            holder.mChildHandleText.setText(child.getHandle());
        }

        holder.mChildStandardText.setText(standardText);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mData != null ? mData.size() : 0);
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }


    /**
     * Search the given key in the list of child name or handle
     * @param key key which is searched in the list
     */
    public void search(String key) {
        if (TextUtils.isEmpty(key)) {
            mData = new ArrayList<>(mOriginalData);
            notifyDataSetChanged();
            return;
        }

        ArrayList<Child> localCopy = new ArrayList<>();

        for (Child child : mData) {
            if (!TextUtils.isEmpty(child.getFullName())) {
                if (child.getFullName().contains(key)) {
                    localCopy.add(child);
                    continue;
                }
            }

            if (!TextUtils.isEmpty(child.getHandle())) {
                if (child.getHandle().contains(key)) {
                    localCopy.add(child);
                }
            }
        }

        if (localCopy.size() > 0) {
            mData = new ArrayList<>(localCopy);
            notifyDataSetChanged();
        }
    }


    static class ChildViewHolder extends RecyclerView.ViewHolder {

        private TextView mChildHandleText;

        private TextView mChildStandardText;

        ChildViewHolder(View itemView) {
            super(itemView);

            mChildHandleText = (TextView) itemView.findViewById(R.id.child_handle_text);
            mChildStandardText = (TextView) itemView.findViewById(R.id.child_standard_text);
        }
    }
}
