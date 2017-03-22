package com.example.gadfly.projectgadfly;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;


public class AboutFragment extends Fragment {
    View myView;
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            myView = inflater.inflate(R.layout.fragment_about, null );
            TextView text = (TextView) myView.findViewById(R.id.textAbout);
            text.setText("This is Gadfly project");
            return myView;
    }
}