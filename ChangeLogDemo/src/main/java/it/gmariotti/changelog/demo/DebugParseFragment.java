/*******************************************************************************
 * Copyright (c) 2013 Gabriele Mariotti.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package it.gmariotti.changelog.demo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;

import it.gmariotti.changelog.demo.fragment.BaseFragment;
import it.gmariotti.changelibs.library.internal.ChangeLog;
import it.gmariotti.changelibs.library.parser.XmlParser;

/**
 * Debug Activity for test parsing
 *
 * Created by gabriele on 20/08/13.
 */
public class DebugParseFragment extends BaseFragment {

    private Spinner mSpinner;
    private TextView mTextChangeLog;
    private ArrayAdapter<Integer> mAdapter;

    private static String TAG= "DebugParseActivity";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.demo_changelog_fragment_debuglayout, container, false);
    }


    @Override
    public int getTitleResourceId() {
        return R.string.demo_changelog_title_debug;
    }

    @Override
    public int getSelfNavDrawerItem() {
        return 0;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSpinner = (Spinner) getActivity().findViewById(R.id.chgfiles_spinner);
        mTextChangeLog= (TextView) getActivity().findViewById(R.id.textChangeLog);

        populateSpinner();
    }


    /**
     * Populate spinner with changelog files in res/raw
     */
    private void populateSpinner(){

            Field[] fields=R.raw.class.getFields();
            ResourceSpinner resources[]=null;
            if (fields!=null){
                resources= new ResourceSpinner[fields.length];
                for(int count=0; count < fields.length; count++){
                    try {
                        ResourceSpinner sp=new ResourceSpinner(fields[count].getInt(fields[count]),fields[count].getName());
                        resources[count]=sp;
                    } catch (IllegalAccessException e) {
                        Log.e(TAG,"Error in populate spinner",e);
                    }
                }
            }

            if (resources!=null){
                // Create an ArrayAdapter using a default spinner layout
                ArrayAdapter<ResourceSpinner> adapter = new ArrayAdapter<ResourceSpinner>(getActivity(),android.R.layout.simple_spinner_item,resources);

                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                mSpinner.setAdapter(adapter);

                mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //Integer itemSelected=(Integer)parent.getItemAtPosition(position);
                        ResourceSpinner itemSelected= (ResourceSpinner) parent.getItemAtPosition(position);
                        if (itemSelected!=null)
                            parseFile(itemSelected.resourceId);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
    }

    /**
     *
     */
    private class ResourceSpinner{

        int resourceId;
        String resourceName;

        ResourceSpinner(int resourceId,String resourceName){
            this.resourceId=resourceId;
            this.resourceName=resourceName;
        }

        @Override
        public String toString() {
            return resourceName;
        }
    }



    /**
     * Parse file
     *
     * @param rawFileResourceId
     */
    private void parseFile(int rawFileResourceId){

        XmlParser parse = new XmlParser(this.getActivity(),rawFileResourceId);
        try{
            ChangeLog log=parse.readChangeLogFile();
            if (mTextChangeLog!=null)
                mTextChangeLog.setText(log.toString());
        }catch (Exception e){
            Log.e(TAG,getString(R.string.demo_changelog_error_debug) , e);
            Toast.makeText(this.getActivity(),R.string.demo_changelog_error_debug,Toast.LENGTH_LONG).show();
        }
    }


}