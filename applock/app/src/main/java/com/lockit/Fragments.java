package com.lockit;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public class Fragments {
    public static void loadContentFragment(FragmentActivity fragmentActivity, int containerId,
                                           Fragment fragment) {
        fragmentActivity.getSupportFragmentManager().beginTransaction()
                .replace(containerId, fragment, fragment.getClass().getSimpleName())
                .setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit)
                .commitAllowingStateLoss();
    }

    public static void replaceContentFragment(FragmentActivity fragmentActivity, int containerId,
                                              Fragment fragment,String tag) {
        fragmentActivity.getSupportFragmentManager().beginTransaction()
                .replace(containerId, fragment, tag)
                .setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit)
                .commitAllowingStateLoss();
    }

    public static void loadContentFragmentBackstack(FragmentActivity fragmentActivity, int containerId,
                                           Fragment fragment) {
        fragmentActivity.getSupportFragmentManager().beginTransaction()
                .replace(containerId, fragment, fragment.getClass().getSimpleName())
                .setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit).addToBackStack(null)
                .commitAllowingStateLoss();
    }
}
