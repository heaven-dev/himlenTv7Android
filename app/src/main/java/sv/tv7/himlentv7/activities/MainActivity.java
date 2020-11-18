package sv.tv7.himlentv7.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.List;

import sv.tv7.himlentv7.BuildConfig;
import sv.tv7.himlentv7.R;
import sv.tv7.himlentv7.fragments.ArchiveMainFragment;
import sv.tv7.himlentv7.fragments.ArchivePlayerFragment;
import sv.tv7.himlentv7.fragments.CategoriesFragment;
import sv.tv7.himlentv7.fragments.ExitFragment;
import sv.tv7.himlentv7.fragments.GuideFragment;
import sv.tv7.himlentv7.fragments.ProgramInfoFragment;
import sv.tv7.himlentv7.fragments.SearchFragment;
import sv.tv7.himlentv7.fragments.SearchResultFragment;
import sv.tv7.himlentv7.fragments.SeriesFragment;
import sv.tv7.himlentv7.fragments.TvMainFragment;
import sv.tv7.himlentv7.fragments.TvPlayerFragment;
import sv.tv7.himlentv7.helpers.Utils;
import sv.tv7.himlentv7.interfaces.EpgDataLoadedListener;
import sv.tv7.himlentv7.model.ProgramScheduleViewModel;

import static sv.tv7.himlentv7.helpers.Constants.EXIT_OVERLAY_FRAGMENT;
import static sv.tv7.himlentv7.helpers.Constants.LOG_TAG;
import static sv.tv7.himlentv7.helpers.Constants.TV_MAIN_FRAGMENT;
import static sv.tv7.himlentv7.helpers.Constants.PROGRESS_BAR_SIZE;

/**
 * Main activity.
 *  - Load epg data, shows logo and progressbar.
 *  - Opens main fragment.
 *  - Handles keydown events.
 */
public class MainActivity extends AppCompatActivity implements EpgDataLoadedListener {

    private FragmentManager fragmentManager = null;
    private ProgramScheduleViewModel viewModel = null;
    private EpgDataLoadedListener epgDataLoadedListener = null;

    private View fragmentContainer = null;
    private ImageView startupLogo = null;
    private ProgressBar progressBar = null;

    /**
     * onCreate() - Android lifecycle method.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);

            if (BuildConfig.DEBUG) {
                Log.d(LOG_TAG, "MainActivity.onCreate() called.");
            }

            fragmentManager = Utils.getFragmentManager(this);

            viewModel = ViewModelProviders.of(this).get(ProgramScheduleViewModel.class);
            this.setEpgDataLoadedListener(this);

            setContentView(R.layout.activity_main);

            fragmentContainer = findViewById(R.id.fragment_container);
            startupLogo = findViewById(R.id.startupLogo);

            progressBar = findViewById(R.id.startupProgressBar);
            progressBar.setScaleY(PROGRESS_BAR_SIZE);
            progressBar.setScaleX(PROGRESS_BAR_SIZE);

            viewModel.getEpgData(this);
        }
        catch(Exception e) {
            if (BuildConfig.DEBUG) {
                Log.d(LOG_TAG, "MainActivity.onCreate(): Exception: " + e);
            }
            Utils.showErrorToast(getApplicationContext(), getString(R.string.toast_something_went_wrong));
        }
    }

    /**
     * Handles key down events and sends events to visible fragment.
     * @param keyCode
     * @param events
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent events) {
        try {
            if (BuildConfig.DEBUG) {
                Log.d(LOG_TAG, "MainActivity.onKeyDown(): keyCode: " + keyCode);
            }

            Fragment fragment = this.getVisibleFragment();
            if (fragment != null) {
                if (fragment instanceof TvMainFragment) {
                    // TV main fragment visible - event to main fragment
                    return ((TvMainFragment) fragment).onKeyDown(keyCode, events);
                }
                if (fragment instanceof ArchiveMainFragment) {
                    // Archive main fragment visible - event to main fragment
                    return ((ArchiveMainFragment) fragment).onKeyDown(keyCode, events);
                }
                else if (fragment instanceof TvPlayerFragment) {
                    // Video player fragment visible - event to video player fragment
                    return ((TvPlayerFragment) fragment).onKeyDown(keyCode, events);
                }
                else if (fragment instanceof ArchivePlayerFragment) {
                    // Archive player fragment visible - event to archive player fragment
                    return ((ArchivePlayerFragment) fragment).onKeyDown(keyCode, events);
                }
                else if (fragment instanceof ProgramInfoFragment) {
                    // Program info fragment visible - event to program info fragment
                    return ((ProgramInfoFragment) fragment).onKeyDown(keyCode, events);
                }
                else if (fragment instanceof CategoriesFragment) {
                    // Categories fragment visible - event to program info fragment
                    return ((CategoriesFragment) fragment).onKeyDown(keyCode, events);
                }
                else if (fragment instanceof SeriesFragment) {
                    // Series fragment visible - event to program info fragment
                    return ((SeriesFragment) fragment).onKeyDown(keyCode, events);
                }
                else if (fragment instanceof GuideFragment) {
                    // Guide fragment visible - event to program info fragment
                    return ((GuideFragment) fragment).onKeyDown(keyCode, events);
                }
                else if (fragment instanceof SearchFragment) {
                    // Search fragment visible - event to program info fragment
                    return ((SearchFragment) fragment).onKeyDown(keyCode, events);
                }
                else if (fragment instanceof SearchResultFragment) {
                    // Search result fragment visible - event to program info fragment
                    return ((SearchResultFragment) fragment).onKeyDown(keyCode, events);
                }
                else if (fragment instanceof ExitFragment) {
                    // Exit overlay fragment visible - event to exit overlay fragment
                    return ((ExitFragment) fragment).onKeyDown(keyCode, events);
                }
            }
        }
        catch(Exception e) {
            if (BuildConfig.DEBUG) {
                Log.d(LOG_TAG, "MainActivity.onKeyDown(): Exception: " + e);
            }
            Utils.showErrorToast(getApplicationContext(), getString(R.string.toast_something_went_wrong));
        }

        return super.onKeyDown(keyCode, events);
    }

    /**
     * Callback to success epg date load.
     */
    @Override
    public void onEpgDataLoaded() {
        if (BuildConfig.DEBUG) {
            Log.d(LOG_TAG, "MainActivity.onEpgDataLoaded(): EpgData load/parse ok.");
        }

        startupLogo.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        fragmentContainer.setVisibility(View.VISIBLE);

        Utils.toPage(TV_MAIN_FRAGMENT, this, false, false,null);
    }

    /**
     * Callback to error epg date load.
     */
    @Override
    public void onEpgDataLoadError(String message) {
        if (BuildConfig.DEBUG) {
            Log.d(LOG_TAG, "MainActivity.onEpgDataLoadError(): EpgData load/parse error: " + message);
        }
        Utils.showErrorToast(getApplicationContext(), getString(R.string.toast_something_went_wrong));
    }

    /**
     * Returns visible fragment.
     * @return
     */
    private Fragment getVisibleFragment(){
        // Possible multiple fragments visible because exit overlay fragment
        List<Fragment> visibleFragments = new ArrayList<>();

        if (fragmentManager != null) {
            List<Fragment> fragments = fragmentManager.getFragments();
            if(fragments != null) {
                for(Fragment fragment : fragments){
                    if(fragment != null && fragment.isVisible())
                        visibleFragments.add(fragment);
                }
            }

            Fragment visibleFragment = null;
            if (visibleFragments.size() > 1) {
                visibleFragment = fragmentManager.findFragmentByTag(EXIT_OVERLAY_FRAGMENT);
            }

            if (visibleFragment != null) {
                // return exit overlay fragment
                return visibleFragment;
            }
        }

        return visibleFragments.get(0);
    }

    /**
     * Creates epg data load listener.
     * @param epgDataLoadedListener
     */
    private void setEpgDataLoadedListener(EpgDataLoadedListener epgDataLoadedListener) {
        this.epgDataLoadedListener = epgDataLoadedListener;
    }
}
