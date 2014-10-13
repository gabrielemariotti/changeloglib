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

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import it.gmariotti.changelog.demo.fragment.BaseFragment;
import it.gmariotti.changelog.demo.fragment.CustomLayoutFragment;
import it.gmariotti.changelog.demo.fragment.CustomLayoutRowFragment;
import it.gmariotti.changelog.demo.fragment.CustomXmlFileFragment;
import it.gmariotti.changelog.demo.fragment.DialogMaterialFragment;
import it.gmariotti.changelog.demo.fragment.MaterialFragment;
import it.gmariotti.changelog.demo.fragment.StandardFragment;
import it.gmariotti.changelog.demo.fragment.WithoutBulletPointFragment;
import it.gmariotti.changelog.demo.iabutils.IabHelper;
import it.gmariotti.changelog.demo.iabutils.IabResult;
import it.gmariotti.changelog.demo.iabutils.IabUtil;
import it.gmariotti.changelog.demo.utils.PrefUtils;
import it.gmariotti.changelog.demo.utils.UtilsBase;


/**
 *  Main Activity
 *
 *  @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class MainActivity extends ActionBarActivity {


    private UtilsBase mUtils;

    // list of navdrawer items that were actually added to the navdrawer, in order
    private ArrayList<Integer> mNavDrawerItems = new ArrayList<Integer>();

    // symbols for navdrawer items (indices must correspond to array below). This is
    // not a list of items that are necessarily *present* in the Nav Drawer; rather,
    // it's a list of all possible items.

    public static final int NAVDRAWER_ITEM_STD = 0;
    public static final int NAVDRAWER_ITEM_MAT = 1;
    public static final int NAVDRAWER_ITEM_DIALOG = 2;
    public static final int NAVDRAWER_ITEM_WITHOUT_BULLET = 3;
    public static final int NAVDRAWER_ITEM_CUSTOM_XML = 4;
    public static final int NAVDRAWER_ITEM_CUSTOM_HEADER = 5;
    public static final int NAVDRAWER_ITEM_CUSTOM_ROW = 6;

    public static final int NAVDRAWER_ITEM_GITHUB = 7;
    public static final int NAVDRAWER_ITEM_DONATE = 8;
    public static final int NAVDRAWER_ITEM_INFO = 9;

    protected static final int NAVDRAWER_ITEM_INVALID = -1;
    protected static final int NAVDRAWER_ITEM_SEPARATOR = -2;
    protected static final int NAVDRAWER_ITEM_SEPARATOR_SPECIAL = -3;

    private ViewGroup mDrawerItemsListContainer;
    // views that correspond to each navdrawer item, null if not yet created
    private View[] mNavDrawerItemViews = null;

    // titles for navdrawer items (indices must correspond to the above)
    private static final int[] NAVDRAWER_TITLE_RES_ID = new int[]{
            R.string.navdrawer_item_standard ,
            R.string.navdrawer_item_material,
            R.string.navdrawer_item_dialog,
            R.string.navdrawer_item_without_bullter,
            R.string.navdrawer_item_custom_xml,
            R.string.navdrawer_item_custom_header,
            R.string.navdrawer_item_custom_row,
            R.string.navdrawer_item_github,
            R.string.navdrawer_item_donate,
            R.string.navdrawer_item_info
    };


    // icons for navdrawer items (indices must correspond to above array)
    private static final int[] NAVDRAWER_ICON_RES_ID = new int[] {
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_github,
            R.drawable.ic_money,
            R.drawable.ic_l_info
    };

    // delay to launch nav drawer item, to allow close animation to play
    private static final int NAVDRAWER_LAUNCH_DELAY = 250;

    private DrawerLayout mDrawerLayout;
    private UtilsBase.ActionBarDrawerToggleWrapper mDrawerToggle;

    // A Runnable that we should execute when the navigation drawer finishes its closing animation
    private Runnable mDeferredOnDrawerClosedRunnable;
    private Handler mHandler;
    private BaseFragment mSelectedFragment;

    private IabHelper mHelper;

    private static String TAG= "MainActivity";

    private static String BUNDLE_SELECTEDFRAGMENT="BDL_SELFRG";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BUNDLE_SELECTEDFRAGMENT,mSelectedFragment.getSelfNavDrawerItem());
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_changelog_main_activity);

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mUtils = new UtilsBase(this);
        mHandler = new Handler();

        // ---------------------------------------------------------------
        // ...
        String base64EncodedPublicKey= IabUtil.key;

        // compute your public key and store it in base64EncodedPublicKey
        mHelper = new IabHelper(this, base64EncodedPublicKey);
        mHelper.enableDebugLogging(true);

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    Log.d(TAG, "Problem setting up In-app Billing: " + result);
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                // Hooray, IAB is fully set up!
                IabUtil.getInstance().retrieveData(mHelper);
            }
        });

        //-----------------------------------------------------------------

        if (savedInstanceState!=null){
            int mSelectedFragmentIndex = savedInstanceState.getInt(BUNDLE_SELECTEDFRAGMENT);
            mSelectedFragment= selectFragment(mSelectedFragmentIndex);
        }else{
            mSelectedFragment=new StandardFragment();
        }
        if (mSelectedFragment!=null)
            openFragment(mSelectedFragment);

        setupNavDrawer();
        //-----------------------------------------------------------------
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null) mHelper.dispose();
        mHelper = null;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    private BaseFragment selectFragment(int position){
        BaseFragment baseFragment=null;

        switch (position) {
            default:
            case NAVDRAWER_ITEM_STD:
                baseFragment=new StandardFragment();
                break;
            case NAVDRAWER_ITEM_MAT:
                baseFragment=new MaterialFragment();
                break;
            case NAVDRAWER_ITEM_WITHOUT_BULLET:
                baseFragment = new WithoutBulletPointFragment();
                break;
            case NAVDRAWER_ITEM_CUSTOM_XML:
                baseFragment = new CustomXmlFileFragment();
                break;
            case NAVDRAWER_ITEM_CUSTOM_HEADER:
                baseFragment = new CustomLayoutFragment();
                break;
            case NAVDRAWER_ITEM_CUSTOM_ROW:
                baseFragment = new CustomLayoutRowFragment();
                break;
        }
        return baseFragment;
    }


    private void openDialogFragment(DialogFragment dialogStandardFragment) {
        if (dialogStandardFragment!=null){
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment prev = fm.findFragmentByTag("changelogdemo_dialog");
            if (prev != null) {
                ft.remove(prev);
            }
            //ft.addToBackStack(null);

            dialogStandardFragment.show(ft,"changelogdemo_dialog");
        }
    }

    private void openFragment(BaseFragment baseFragment){
        if (baseFragment!=null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.replace(R.id.fragment_main,baseFragment);
            //fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            //mCurrentTitle=baseFragment.getTitleResourceId();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        }
        else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (mDrawerToggle != null && mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public IabHelper getHelper() {
        return mHelper;
    }

    //----------------------------------------------------------------------------
    // Navigation Drawer
    //----------------------------------------------------------------------------

    /**
     * Sets up the navigation drawer as appropriate. Note that the nav drawer will be
     * different depending on whether the attendee indicated that they are attending the
     * event on-site vs. attending remotely.
     */
    private void setupNavDrawer() {
        // What nav drawer item should be selected?
        int selfItem = getSelfNavDrawerItem();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawerLayout == null) {
            return;
        }
        if (selfItem == NAVDRAWER_ITEM_INVALID) {
            // do not show a nav drawer
            View navDrawer = mDrawerLayout.findViewById(R.id.navdrawer);
            if (navDrawer != null) {
                ((ViewGroup) navDrawer.getParent()).removeView(navDrawer);
            }
            mDrawerLayout = null;
            return;
        }

        mDrawerToggle = mUtils.setupDrawerToggle(mDrawerLayout, new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                // run deferred action, if we have one
                if (mDeferredOnDrawerClosedRunnable != null) {
                    mDeferredOnDrawerClosedRunnable.run();
                    mDeferredOnDrawerClosedRunnable = null;
                }

                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                updateStatusBarForNavDrawerSlide(0f);
                onNavDrawerStateChanged(false, false);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                updateStatusBarForNavDrawerSlide(1f);
                onNavDrawerStateChanged(true, false);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                invalidateOptionsMenu();
                onNavDrawerStateChanged(isNavDrawerOpen(), newState != DrawerLayout.STATE_IDLE);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                updateStatusBarForNavDrawerSlide(slideOffset);
                onNavDrawerSlide(slideOffset);
            }
        });
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // populate the nav drawer with the correct items
        populateNavDrawer();

        mDrawerToggle.syncState();

        // When the user runs the app for the first time, we want to land them with the
        // navigation drawer open. But just the first time.
        if (!PrefUtils.isWelcomeDone(this)) {
            // first run of the app starts with the nav drawer open
            PrefUtils.markWelcomeDone(this);
            mDrawerLayout.openDrawer(Gravity.START);
        }
    }

    /**
     * Returns the navigation drawer item that corresponds to this Activity. Subclasses
     * of BaseActivity override this to indicate what nav drawer item corresponds to them
     * Return NAVDRAWER_ITEM_INVALID to mean that this Activity should not have a Nav Drawer.
     */
    protected int getSelfNavDrawerItem() {
        if (mSelectedFragment != null)
            return mSelectedFragment.getSelfNavDrawerItem();
        return NAVDRAWER_ITEM_INVALID;
    }

    // Subclasses can override this for custom behavior
    protected void onNavDrawerStateChanged(boolean isOpen, boolean isAnimating) {

    }

    protected boolean isNavDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(Gravity.START);
    }

    private void updateStatusBarForNavDrawerSlide(float slideOffset) {

    }

    protected void onNavDrawerSlide(float offset) {
    }



    /** Populates the navigation drawer with the appropriate items. */
    private void populateNavDrawer() {

        mNavDrawerItems.clear();

        // Explore is always shown
        mNavDrawerItems.add(NAVDRAWER_ITEM_STD);
        mNavDrawerItems.add(NAVDRAWER_ITEM_MAT);
        mNavDrawerItems.add(NAVDRAWER_ITEM_DIALOG);
        mNavDrawerItems.add(NAVDRAWER_ITEM_CUSTOM_XML);
        mNavDrawerItems.add(NAVDRAWER_ITEM_CUSTOM_HEADER);
        mNavDrawerItems.add(NAVDRAWER_ITEM_CUSTOM_ROW);
        mNavDrawerItems.add(NAVDRAWER_ITEM_WITHOUT_BULLET);

        mNavDrawerItems.add(NAVDRAWER_ITEM_SEPARATOR_SPECIAL);

        mNavDrawerItems.add(NAVDRAWER_ITEM_GITHUB);
        mNavDrawerItems.add(NAVDRAWER_ITEM_DONATE);
        mNavDrawerItems.add(NAVDRAWER_ITEM_INFO);

        createNavDrawerItems();
    }

    private void createNavDrawerItems() {
        mDrawerItemsListContainer = (ViewGroup) findViewById(R.id.navdrawer_items_list);
        if (mDrawerItemsListContainer == null) {
            return;
        }

        mNavDrawerItemViews = new View[mNavDrawerItems.size()];
        mDrawerItemsListContainer.removeAllViews();
        int i = 0;
        for (int itemId : mNavDrawerItems) {
            mNavDrawerItemViews[i] = makeNavDrawerItem(itemId, mDrawerItemsListContainer);
            mDrawerItemsListContainer.addView(mNavDrawerItemViews[i]);
            ++i;
        }
    }

    private View makeNavDrawerItem(final int itemId, ViewGroup container) {
        boolean selected = getSelfNavDrawerItem() == itemId;
        int layoutToInflate = 0;
        if (itemId == NAVDRAWER_ITEM_SEPARATOR) {
            layoutToInflate = R.layout.navdrawer_separator;
        } else if (itemId == NAVDRAWER_ITEM_SEPARATOR_SPECIAL) {
            layoutToInflate = R.layout.navdrawer_separator;
        } else {
            layoutToInflate = R.layout.navdrawer_item;
        }
        View view = getLayoutInflater().inflate(layoutToInflate, container, false);

        if (isSeparator(itemId)) {
            // we are done
            //UIUtils.setAccessibilityIgnore(view);
            return view;
        }

        ImageView iconView = (ImageView) view.findViewById(R.id.icon);
        TextView titleView = (TextView) view.findViewById(R.id.title);
        int iconId = itemId >= 0 && itemId < NAVDRAWER_ICON_RES_ID.length ?
                NAVDRAWER_ICON_RES_ID[itemId] : 0;
        int titleId = itemId >= 0 && itemId < NAVDRAWER_TITLE_RES_ID.length ?
                NAVDRAWER_TITLE_RES_ID[itemId] : 0;

        // set icon and text
        iconView.setVisibility(iconId > 0 ? View.VISIBLE : View.GONE);
        if (iconId > 0) {
            iconView.setImageResource(iconId);
        }
        titleView.setText(getString(titleId));

        formatNavDrawerItem(view, itemId, selected);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNavDrawerItemClicked(itemId);
            }
        });

        return view;
    }

    private void onNavDrawerItemClicked(final int itemId) {
        if (itemId == getSelfNavDrawerItem()) {
            mDrawerLayout.closeDrawer(Gravity.START);
            return;
        }

        if (isSpecialItem(itemId)) {
            goToNavDrawerItem(itemId);
        } else {
            // launch the target Activity after a short delay, to allow the close animation to play
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    goToNavDrawerItem(itemId);
                }
            }, NAVDRAWER_LAUNCH_DELAY);

            // change the active item on the list so the user can see the item changed
            setSelectedNavDrawerItem(itemId);

        }

        mDrawerLayout.closeDrawer(Gravity.START);
    }

    private void formatNavDrawerItem(View view, int itemId, boolean selected) {
        if (isSeparator(itemId)) {
            // not applicable
            return;
        }

        ImageView iconView = (ImageView) view.findViewById(R.id.icon);
        TextView titleView = (TextView) view.findViewById(R.id.title);

        // configure its appearance according to whether or not it's selected
        titleView.setTextColor(selected ?
                getResources().getColor(R.color.navdrawer_text_color_selected) :
                getResources().getColor(R.color.navdrawer_text_color));
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            iconView.setColorFilter(selected ?
                    getResources().getColor(R.color.navdrawer_icon_tint_selected) :
                    getResources().getColor(R.color.navdrawer_icon_tint));
        }
    }

    private boolean isSpecialItem(int itemId) {
        return itemId == NAVDRAWER_ITEM_DONATE || itemId == NAVDRAWER_ITEM_INFO || itemId == NAVDRAWER_ITEM_GITHUB || itemId == NAVDRAWER_ITEM_DIALOG;
    }

    private boolean isSeparator(int itemId) {
        return itemId == NAVDRAWER_ITEM_SEPARATOR || itemId == NAVDRAWER_ITEM_SEPARATOR_SPECIAL;
    }

    private void goToNavDrawerItem(int item) {

        BaseFragment fg = null;
        switch (item) {
            case NAVDRAWER_ITEM_STD:
                fg =selectFragment(item);
                break;
            case NAVDRAWER_ITEM_MAT:
                fg = selectFragment(item);
                break;
            case NAVDRAWER_ITEM_CUSTOM_XML:
                fg = selectFragment(item);
                break;
            case NAVDRAWER_ITEM_WITHOUT_BULLET:
                fg = selectFragment(item);
                break;
            case  NAVDRAWER_ITEM_CUSTOM_ROW:
                fg = selectFragment(item);
                break;
            case  NAVDRAWER_ITEM_CUSTOM_HEADER:
                fg = selectFragment(item);
                break;
            case  NAVDRAWER_ITEM_DIALOG:
                openDialogFragment(new DialogMaterialFragment());
                break;
            case NAVDRAWER_ITEM_DONATE:
                IabUtil.showBeer(this, mHelper);
                break;
            case NAVDRAWER_ITEM_INFO:
                Utils.showAbout(this);
                break;
            case NAVDRAWER_ITEM_GITHUB:
                String url = "https://github.com/gabrielemariotti/changeloglib/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
        }

        if (fg != null){
            mSelectedFragment = fg;
            openFragment(fg);
        }
    }

    /**
     * Sets up the given navdrawer item's appearance to the selected state. Note: this could
     * also be accomplished (perhaps more cleanly) with state-based layouts.
     */
    private void setSelectedNavDrawerItem(int itemId) {
        if (mNavDrawerItemViews != null) {
            for (int i = 0; i < mNavDrawerItemViews.length; i++) {
                if (i < mNavDrawerItems.size()) {
                    int thisItemId = mNavDrawerItems.get(i);
                    formatNavDrawerItem(mNavDrawerItemViews[i], thisItemId, itemId == thisItemId);
                }
            }
        }
    }


}