package com.axioms.voca.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toolbar;

import com.axioms.voca.R;
import com.axioms.voca.base.GlobalApplication;
import com.axioms.voca.databinding.ActivityMainBinding;
import com.axioms.voca.dialog.CommDialog;
import com.axioms.voca.fragment.MainDictFragment;
import com.axioms.voca.fragment.MainSrchFragment;
import com.axioms.voca.fragment.MainVocaFragment;
import com.axioms.voca.permission.Permission;
import com.axioms.voca.permission.PermissionListener;
import com.axioms.voca.tts.SpeechHelper;
import com.axioms.voca.util.CommUtil;
import com.axioms.voca.util.LogUtil;
import com.axioms.voca.util.ToastUtil;
import com.axioms.voca.vo.VoVoca;
import com.axioms.voca.vo.VoVocaListArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    ActivityMainBinding binding;

    public interface OnPlayListener {
        void onSpeak(String str);
        void onStop();
    }

    public interface OnBackPressedListener {
        boolean onBack();
    }

    private OnBackPressedListener onBackPressedListener;

    public void setOnBackPressedListener(OnBackPressedListener listener) {
        this.onBackPressedListener = listener;
    }

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the szections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    //private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setActivity(this);

        //setContentView(R.layout.activity_main);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        //mViewPager = (ViewPager) findViewById(R.id.container);
        binding.container.setAdapter(mSectionsPagerAdapter);
        binding.container.setCurrentItem(1);

        //Permission 설정
        Permission.with(this)
                .setPermissionListener(permissionListener)
                .setTitle(R.string.dialog_permission_title)
                .setMessage(R.string.dialog_permission_conts)
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


    }

    PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            LogUtil.i("onPermissionGranted");
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            LogUtil.i("onPermissionDenied");
        }
    };

    public void setFragment(int pos) {
        LogUtil.i("pos :: " + pos);
        binding.container.setCurrentItem(pos);
    }

    /** BackPressd Time */
    private long pressedTime = 0;

    /**
     * BackPressed
     */
    @Override
    public void onBackPressed() {
        if (onBackPressedListener.onBack()) {
            //onBackPressedListener.onBack();
        } else {
            if (pressedTime == 0) {
                ToastUtil.show(this, R.string.cmm_onbackpress);
                pressedTime = System.currentTimeMillis();
            } else {
                int seconds = (int) (System.currentTimeMillis() - pressedTime);
                if (seconds > 2000) {
                    ToastUtil.show(this, R.string.cmm_onbackpress);
                    pressedTime = 0;
                } else {
                    super.onBackPressed();
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            }
        }
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
////        if (id == R.id.action_settings) {
////            return true;
////        }
//
//        return super.onOptionsItemSelected(item);
//    }

//    /**
//     * A placeholder fragment containing a simple view.
//     */
//    public static class PlaceholderFragment extends Fragment {
//        /**
//         * The fragment argument representing the section number for this
//         * fragment.
//         */
//        private static final String ARG_SECTION_NUMBER = "section_number";
//
//        public PlaceholderFragment() {
//        }
//
//        /**
//         * Returns a new instance of this fragment for the given section
//         * number.
//         */
//        public static PlaceholderFragment newInstance(int sectionNumber) {
//            PlaceholderFragment fragment = new PlaceholderFragment();
//            Bundle args = new Bundle();
//            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//            fragment.setArguments(args);
//            return fragment;
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//            View rootView = inflater.inflate(R.layout.fragment_main_voca, container, false);
//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
//            return rootView;
//        }
//    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            switch (position) {
                case 0:
                    return MainDictFragment.newInstance(position);
                case 1:
                    MainVocaFragment fragment = MainVocaFragment.newInstance(position);
                    fragment.setAdapterData(getTestData());
                    return fragment;

                default:
                    return MainSrchFragment.newInstance(position);
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }

    private ArrayList<VoVoca> getTestData() {
        String msg = null;
        try {
            msg = CommUtil.getData(this, "vocabulary.json");
            VoVocaListArray voVocaListArray = GlobalApplication.getGson().fromJson(msg, VoVocaListArray.class);

            LogUtil.i("size : " + VoVocaListArray.getInstance().getVOCALIST_LIST().size());
            return VoVocaListArray.getInstance().getVOCALIST_LIST().get(0).getVOCA_LIST();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
