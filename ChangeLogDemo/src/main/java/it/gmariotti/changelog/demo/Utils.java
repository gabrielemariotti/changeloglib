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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import it.gmariotti.changelibs.library.view.ChangeLogListView;

/**
 * Created by gabriele on 28/08/13.
 *
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class Utils {

    public static void showAbout(ActionBarActivity activity) {

        FragmentManager fm = activity.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("dialog_about");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        new AboutDialog().show(ft,"dialog_about");
    }

    /**
     * About Dialog
     */
    public static class AboutDialog extends DialogFragment {

        private static final String VERSION_UNAVAILABLE = "N/A";

        public AboutDialog() {
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get app version
            PackageManager pm = getActivity().getPackageManager();
            String packageName = getActivity().getPackageName();
            String versionName;
            try {
                PackageInfo info = pm.getPackageInfo(packageName, 0);
                versionName = info.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                versionName = VERSION_UNAVAILABLE;
            }

            // Build the about body view and append the link to see OSS licenses
            //SpannableStringBuilder aboutBody = new SpannableStringBuilder();
            //aboutBody.append(Html.fromHtml(getString(R.string.about_body, versionName)));


            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            View rootView = layoutInflater.inflate(R.layout.demo_changelog_dialog_about, null);
            TextView nameAndVersionView = (TextView) rootView.findViewById(
                    R.id.app_name_and_version);
            nameAndVersionView.setText(Html.fromHtml(
                    getString(R.string.title_about, versionName)));

            TextView aboutBodyView = (TextView) rootView.findViewById(R.id.about_body);
            aboutBodyView.setText(Html.fromHtml(getString(R.string.about_body)));
            aboutBodyView.setMovementMethod(new LinkMovementMethod());

            return new AlertDialog.Builder(getActivity())
                    //.setTitle(R.string.title_about)
                    .setView(rootView)
                    .setPositiveButton(R.string.about_ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                }
                            }
                    )
                    .create();
        }
    }

    public static void showChangeLog(ActionBarActivity activity) {

        FragmentManager fm = activity.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("changelog_about");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        new ChangeLogDialog().show(ft,"changeLog_about");
    }

    /**
     * ChangeLogDialog
     */
    public static class ChangeLogDialog extends DialogFragment {


        public ChangeLogDialog() {
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            ChangeLogListView chgList=(ChangeLogListView)layoutInflater.inflate(R.layout.demo_changelog_dialog_changelog, null);


            return new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.demo_changelog_title_changelog)
                    .setView(chgList)
                    .setPositiveButton(R.string.about_ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                }
                            }
                    )
                    .create();

        }
    }



}
