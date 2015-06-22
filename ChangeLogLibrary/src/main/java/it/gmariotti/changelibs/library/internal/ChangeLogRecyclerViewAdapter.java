/*
 * ******************************************************************************
 *   Copyright (c) 2013-2015 Gabriele Mariotti.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *  *****************************************************************************
 */

package it.gmariotti.changelibs.library.internal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import it.gmariotti.changelibs.R;
import it.gmariotti.changelibs.library.Constants;
import it.gmariotti.changelibs.library.view.ChangeLogRecyclerView;

/**
 * Created by g.mariotti on 17/06/2015.
 */
public class ChangeLogRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ROW = 0;
    private static final int TYPE_HEADER = 1;

    private final Context mContext;
    private int mRowLayoutId = Constants.mRowLayoutId;
    private int mRowHeaderLayoutId = Constants.mRowHeaderLayoutId;
    private int mStringVersionHeader = Constants.mStringVersionHeader;

    private List<ChangeLogRow> items;

    // -------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------

    public ChangeLogRecyclerViewAdapter(Context mContext,List<ChangeLogRow> items ) {
        this.mContext = mContext;
        if (items == null)
            items = new ArrayList<>();
        this.items = items;
    }

    public void add(LinkedList<ChangeLogRow> rows) {
        int originalPosition= items.size();
        items.addAll(rows);
        notifyItemRangeInserted(originalPosition,originalPosition+rows.size());
    }


    // -------------------------------------------------------------
    // ViewHolder
    // -------------------------------------------------------------

    public static class ViewHolderHeader extends RecyclerView.ViewHolder {
        public TextView versionHeader;
        public TextView dateHeader;

        public ViewHolderHeader(View itemView) {
            super(itemView);
            //VersionName text
            versionHeader = (TextView) itemView.findViewById(R.id.chg_headerVersion);
            //ChangeData text
            dateHeader = (TextView) itemView.findViewById(R.id.chg_headerDate);
        }
    }

    public static class ViewHolderRow extends RecyclerView.ViewHolder {
        public TextView textRow;
        public TextView bulletRow;

        public ViewHolderRow(View itemView) {
            super(itemView);
            textRow = (TextView) itemView.findViewById(R.id.chg_text);
            bulletRow = (TextView) itemView.findViewById(R.id.chg_textbullet);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            final View viewHeader = LayoutInflater.from(parent.getContext()).inflate(mRowHeaderLayoutId, parent, false);
            return new ViewHolderHeader(viewHeader);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(mRowLayoutId, parent, false);
            return new ViewHolderRow(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (isHeader(position)) {
            populateViewHolderHeader((ViewHolderHeader)viewHolder, position);
        } else {
            populateViewHolderRow((ViewHolderRow) viewHolder, position);
        }
    }

    private void populateViewHolderRow(ViewHolderRow viewHolder, int position) {
        ChangeLogRow  item = getItem(position);
        if (item != null){
            if (viewHolder.textRow != null){
                viewHolder.textRow.setText(Html.fromHtml(item.getChangeText(mContext)));
                viewHolder.textRow.setMovementMethod(LinkMovementMethod.getInstance());
            }
            if (viewHolder.bulletRow!=null){
                if (item.isBulletedList()){
                    viewHolder.bulletRow.setVisibility(View.VISIBLE);
                }else{
                    viewHolder.bulletRow.setVisibility(View.GONE);
                }
            }
        }
    }

    private void populateViewHolderHeader(ViewHolderHeader viewHolder, int position) {
        ChangeLogRow  item = getItem(position);
        if (item != null) {
            if (viewHolder.versionHeader != null) {
                StringBuilder sb = new StringBuilder();
                //String resource for Version
                String versionHeaderString = mContext.getString(mStringVersionHeader);
                if (versionHeaderString != null)
                    sb.append(versionHeaderString);
                //VersionName text
                sb.append(item.versionName);

                viewHolder.versionHeader.setText(sb.toString());
            }

            //ChangeData text
            if (viewHolder.dateHeader != null) {
                //Check if exists

                if (item.changeDate != null) {
                    viewHolder.dateHeader.setText(item.changeDate);
                    viewHolder.dateHeader.setVisibility(View.VISIBLE);
                } else {
                    //If item hasn't changedata, hide TextView
                    viewHolder.dateHeader.setText("");
                    viewHolder.dateHeader.setVisibility(View.GONE);
                }
            }
        }
    }


    private boolean isHeader(int position) {
        return getItem(position).isHeader();
    }

    private ChangeLogRow getItem(int position) {
        return items.get(position);
    }


    @Override
    public int getItemViewType(int position) {
        if (isHeader(position))
            return TYPE_HEADER;
        return TYPE_ROW;
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    //-----------------------------------------------------------------------------------
    // Getter and Setter
    //-----------------------------------------------------------------------------------

    public void setRowLayoutId(int mRowLayoutId) {
        this.mRowLayoutId = mRowLayoutId;
    }

    public void setRowHeaderLayoutId(int mRowHeaderLayoutId) {
        this.mRowHeaderLayoutId = mRowHeaderLayoutId;
    }

}
