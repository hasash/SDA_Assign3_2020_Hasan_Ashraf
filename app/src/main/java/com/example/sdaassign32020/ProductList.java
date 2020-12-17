package com.example.sdaassign32020;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


/*
 * A simple {@link Fragment} subclass.
 * @author Hasan Ashraf 2020
 */
public class ProductList extends Fragment {

    private static final String TAG = "RecyclerViewActivity";
    private ArrayList<StockAdapter> mstock = new ArrayList<>();

    public ProductList() {
        // Required empty public constructor
    }
    /**
     * Default on Create View for appp to navigate using the recycler view.
     * A log debug message is present to identify and debug stages in code
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_product_list, container, false);
        // Create an ArrayList of AndroidFlavor objects
        createlist();

        //start it with the view
        Log.d(TAG, "Starting recycler view");

        RecyclerView recyclerView = root.findViewById(R.id.recyclerView_view);
        recyclerView.setHasFixedSize(true); // to stop recycler view to change in size

        StockViewAdpater recyclerViewAdapter = new StockViewAdpater(getContext(), mstock);
        recyclerView.setAdapter(recyclerViewAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return root;
    }
    /**
     * Creates a list of the Image and Text that need to be displayed ont the UI
     * @return The list of items that user entered using recycler view
     */
    public void createlist(){
        mstock.add(new StockAdapter("Logo", "€5-€10", R.drawable.logo));
        mstock.add(new StockAdapter("Image", "€15-€30", R.drawable.image));
        mstock.add(new StockAdapter("Total Colour", "€20-€40", R.drawable.full_print));
        mstock.add(new StockAdapter("Arty", "€15-€25", R.drawable.artistic));
        mstock.add(new StockAdapter("Personal Photos", "€15-€40", R.drawable.personal));
        mstock.add(new StockAdapter("Multi-Language", "€10-€15", R.drawable.multi_language));
        mstock.add(new StockAdapter("Text Plus Image", "€15-€25", R.drawable.text_image));
        mstock.add(new StockAdapter("Text Only", "€5-€10", R.drawable.text_shirt));
        mstock.add(new StockAdapter("Hoddies", "€25-€50", R.drawable.hoddie));
        mstock.add(new StockAdapter("Hats", "€5-€10", R.drawable.hats));
        mstock.add(new StockAdapter("Logo", "€5-€10", R.drawable.logo)); //Repeats to Increase List Size
        mstock.add(new StockAdapter("Image", "€15-€30", R.drawable.image)); //Repeats to increase List Size

    }
}
