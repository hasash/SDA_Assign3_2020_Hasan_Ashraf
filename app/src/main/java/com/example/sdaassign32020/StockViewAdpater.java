package com.example.sdaassign32020;
/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

/*
 * @author Hasan Ashraf 2020
 */
public class StockViewAdpater extends RecyclerView.Adapter<StockViewAdpater.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";
    private Context mNewContext;
    private OnItemClickListener mListener;
    private ArrayList<StockAdapter> mstock;

    /**
     * An onclickListener method used to determine the position
     */
    public interface OnItemClickListener{
        void onItemCick(int position);
    }

    StockViewAdpater(Context mNewContext, ArrayList<StockAdapter> mflavor) {
        this.mNewContext = mNewContext;
        this.mstock = mflavor;
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        /**
         * Viewholder class used to repeat the view using a recycler method
         * As only one image and 2 text view are placed
         *
         * @param imageItem, imageText, PriceText
         */
        ImageView imageItem;
        TextView imageText;
        TextView PriceText;
        RelativeLayout itemParentLayout;

        //RelativeLayout listItemLayout

        ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            //grab the image, the text and the layout id's
            imageItem = itemView.findViewById(R.id.imageItem);
            imageText = itemView.findViewById(R.id.Desciption);
            PriceText = itemView.findViewById(R.id.Price);
            itemParentLayout = itemView.findViewById(R.id.listItemLayout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemCick(position);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        /**
         * The main function for the recycler view that creates the list of items
         */
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_list_item, viewGroup, false);
        ViewHolder evh  = new ViewHolder(view, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        /**
         * The function that send the 3 variables to the UI.
         * @param imageText, PriceText, imageItem
         * @return user interface with the 3 varaibles; toast message to used based on what is clicked
         */
        Log.d(TAG, "onBindViewHolder: was called");

        final StockAdapter current_item = mstock.get(position);

        viewHolder.imageText.setText(current_item.getprice());
        viewHolder.PriceText.setText(current_item.getitemname());
        viewHolder.imageItem.setImageResource(current_item.getImageResourceId());

        viewHolder.itemParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mNewContext, current_item.getitemname() + " Selected", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount()
    {
        /**
         * Counter to identify the amount of types recycler is applied
         */
        return mstock.size();
    }


}
