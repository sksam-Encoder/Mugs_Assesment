package com.example.mugsassesment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class FragmentHome extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //graph of x-y axis of array 2 1 1 2 3 2 4
        int []a ={2,1,1,2,3,2,4};

        GraphView graphView = view.findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();


        for (int i=0; i<7 ;i++)
        {

            series.appendData(new DataPoint(i,a[i]),true,7);
        }

        graphView.addSeries(series);

            return  view;
    }
}