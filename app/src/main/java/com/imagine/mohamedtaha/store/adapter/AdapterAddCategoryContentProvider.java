package com.imagine.mohamedtaha.store.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.imagine.mohamedtaha.store.R;
import com.imagine.mohamedtaha.store.data.TaskContract.TaskEntry;

/**
 * Created by MANASATT on 25/11/17.
 */

public class AdapterAddCategoryContentProvider extends RecyclerView.Adapter<AdapterAddCategoryContentProvider.CategoryViewHolder> {
    //class variables for the cursor that holds category data and the Context
    private Cursor mCursor;
    private Context mContext;
    private showDetial mListener;
    private showSale showSale;
    private showEdit showEdit;


    public AdapterAddCategoryContentProvider(showDetial listener, Context mContext, showSale showSale, showEdit showEdit) {
        this.mListener = listener;
        this.mContext = mContext;
        this.showSale = showSale;
        this.showEdit = showEdit;
    }

    //Called when viewHolders are called to fill a RecycleView.
    //retrun A new CategoryViewHolder that holds the view for each category
    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the task_layout to a view
        //  View view = LayoutInflater.from(mContext).inflate(R.layout.custom_category,parent,false);
        //return new CategoryViewHolder(view);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_category, parent, false);
        final CategoryViewHolder categoryViewHolder = new CategoryViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = categoryViewHolder.getAdapterPosition();
                mCursor.moveToPosition(position);
                if (mListener != null) mListener.itemShowDetail(mCursor);
            }
        });

        return categoryViewHolder;


    }
//Called by the RecycleView to display data at a specified position in the Cursor.

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {

        int product_name_index = mCursor.getColumnIndex(TaskEntry.KEY_PRODUCT_NAME);
        int product_price_index = mCursor.getColumnIndex(TaskEntry.KEY_PRICE);
        int quantity_index = mCursor.getColumnIndex(TaskEntry.KEY_QUANTITY);
        int product_picture_index = mCursor.getColumnIndex(TaskEntry.KEY_PICTURE);

        mCursor.moveToPosition(position); //get to the right location in the cursor

        //Determine the values of the wanted data
        String product_name = mCursor.getString(product_name_index);
        String product_price = mCursor.getString(product_price_index);
        String product_quantity = mCursor.getString(quantity_index);
        byte[] product_picture = mCursor.getBlob(product_picture_index);

        Bitmap bitmap = BitmapFactory.decodeByteArray(product_picture, 0, product_picture.length);

        //Set values
        holder.product_name_view.setText(product_name);
        holder.product_price_view.setText(product_price);
        holder.quantity_view.setText(product_quantity);
        holder.product_picture.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 200, 200, false));

    }

    //Returns the number of items todisplay
    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    /**
     * When data changes and a re-query occurs, this function swaps the old Cursor
     * with a newly updated Cursor (Cursor c) that is passed in.
     */
    public Cursor swapCursor(Cursor c) {
        //check if this cursor is the same as the previous cursor(mCursor)
        if (mCursor == c) {
            return null; //bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; //new cursor value assigned

        //check if this is a valid cursor ,then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

    public interface showDetial {
        void itemShowDetail(Cursor cursor);
    }

    public interface showSale {
        void itemShowSale(Cursor cursor);
    }

    public interface showEdit {
        void itemShowEdit(Cursor cursor);
    }

    //Inner class for creating ViewHolders
    class CategoryViewHolder extends RecyclerView.ViewHolder {
        //Class variables for the name category and date category
        TextView product_name_view, product_price_view, quantity_view;
        ImageView product_picture;

        Button bt_sale, bt_edit;


        public CategoryViewHolder(final View itemView) {
            super(itemView);
            product_name_view = (TextView) itemView.findViewById(R.id.tv_product_name);
            product_price_view = (TextView) itemView.findViewById(R.id.tv_product_price);
            quantity_view = (TextView) itemView.findViewById(R.id.tv_product_quantity);
            product_picture = (ImageView) itemView.findViewById(R.id.Image_edit);
            bt_sale = (Button) itemView.findViewById(R.id.bt_sale);
            bt_sale.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();
                    mCursor.moveToPosition(position);
                    if (mListener != null) showSale.itemShowSale(mCursor);

                }
            });

            bt_edit = (Button) itemView.findViewById(R.id.bt_edit);
            bt_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    mCursor.moveToPosition(position);
                    if (mListener != null) showEdit.itemShowEdit(mCursor);
                }
            });

        }
    }
}