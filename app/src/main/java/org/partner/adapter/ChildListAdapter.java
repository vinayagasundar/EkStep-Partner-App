package org.partner.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.partner.R;
import org.partner.model.Child;

import java.util.List;
import java.util.Locale;

/**
 * Adapter class to child display child
 * @author vinayagasundar
 */

public class ChildListAdapter extends RecyclerView.Adapter<ChildListAdapter.ChildViewHolder> {


    private List<Child> mData;

    public ChildListAdapter(List<Child> mData) {
        this.mData = mData;
    }


    @Override
    public ChildViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_child_row,
                parent, false);
        return new ChildViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChildViewHolder holder, int position) {
        Child child = mData.get(position);

        int age = child.getAge();
        String ageText = "%s %s";

        if (age > 0) {
            ageText = String.format(Locale.ENGLISH, ageText, "Class", age);
        } else {
            ageText = String.format(Locale.ENGLISH, ageText, "Class", "No Data");
        }

        holder.mChildHandleText.setText(child.getHandle());
        holder.mChildStandardText.setText(ageText);
    }

    @Override
    public int getItemCount() {
        return (mData != null ? mData.size() : 0);
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
