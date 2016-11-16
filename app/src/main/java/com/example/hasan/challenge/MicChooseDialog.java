package com.example.hasan.challenge;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by hasan on 11/13/2016.
 */
public class MicChooseDialog extends DialogFragment {

    public InterfaceCommunicator interfaceCommunicator;

    public interface InterfaceCommunicator {
        void sendRequestCode(int code, int resultCode, Intent data);
    }

   /* public MicChooseDialog(ArrayList<String> mic_options_list){
        mic_options = mic_options_list;
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        try {
            super.onCreateView(inflater, container, savedInstanceState);
            View view = inflater.inflate(R.layout.choose_mic_src, container, false);
            Bundle parameters = getArguments();
            final ArrayList<String> mic_options =  parameters.getStringArrayList("mic_list");
            ArrayAdapter adapter = new ArrayAdapter<String>(view.getContext(), R.layout.activity_listview, mic_options); // simple textview for list item
            final ListView listView = (ListView) view.findViewById(R.id.ChooseMicListView);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    String o = (String) listView.getItemAtPosition(position);
                    Intent data = new Intent();
                    data.putExtra("result", o);
                    //getTargetFragment().onActivityResult(getTargetRequestCode(), 200, getActivity().getIntent());
                    interfaceCommunicator.sendRequestCode(1, 200, data);
                    dismiss();
                }
            });
            return view;
        }
        catch (Exception e){
            Log.e(e.toString(),e.toString());
        }
        return null;


    }
}