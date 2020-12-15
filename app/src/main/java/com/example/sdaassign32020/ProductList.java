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
 * @author Chris Coughlan 2019
 */
public class ProductList extends Fragment {

    private static final String TAG = "RecyclerViewActivity";
    private ArrayList<FlavorAdapter> mFlavor = new ArrayList<>();

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

        FlavorViewAdapter recyclerViewAdapter = new FlavorViewAdapter(getContext(), mFlavor);
        recyclerView.setAdapter(recyclerViewAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return root;
    }
    /**
     * Creates a list of the Image and Text that need to be displayed ont the UI
     * @return The list of items that user entered using recycler view
     */
    public void createlist(){
        mFlavor.add(new FlavorAdapter("Logo", "€5-€10", R.drawable.logo));
        mFlavor.add(new FlavorAdapter("Image", "€15-€30", R.drawable.image));
        mFlavor.add(new FlavorAdapter("Total Colour", "€20-€40", R.drawable.full_print));
        mFlavor.add(new FlavorAdapter("Arty", "€15-€25", R.drawable.artistic));
        mFlavor.add(new FlavorAdapter("Personal Photos", "€15-€40", R.drawable.personal));
        mFlavor.add(new FlavorAdapter("Multi-Language", "€10-€15", R.drawable.multi_language));
        mFlavor.add(new FlavorAdapter("Text Plus Image", "€15-€25", R.drawable.text_image));
        mFlavor.add(new FlavorAdapter("Text Only", "€5-€10", R.drawable.text_shirt));
        mFlavor.add(new FlavorAdapter("Hoddies", "€25-€50", R.drawable.hoddie));
        mFlavor.add(new FlavorAdapter("Hats", "€5-€10", R.drawable.hats));

    }
}
