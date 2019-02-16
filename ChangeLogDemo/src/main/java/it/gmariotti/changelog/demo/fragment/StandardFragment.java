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
package it.gmariotti.changelog.demo.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.gmariotti.changelog.demo.MainActivity;
import it.gmariotti.changelog.demo.R;


/**
 * ChangeLog Example with standard library.
 *
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class StandardFragment extends BaseFragment {


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.demo_changelog_fragment_standard, container, false);
    }

    @Override
    public int getTitleResourceId() {
        return R.string.demo_changelog_title_standard;
    }

    @Override
    public int getSelfNavDrawerItem() {
        return R.id.nav_ex_standard;
    }
}
