package com.lockit.apps;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.lockit.Application;
import com.lockit.BaseFragment;
import com.lockit.CustomRecyclerAdapter;
import com.lockit.InstalledApplication;
import com.lockit.databinding.AppsFragmentBinding;

import java.util.List;

public class AppsFragment extends BaseFragment implements ApplicationView {
    AppsViewModel model;

    private CustomRecyclerAdapter<Application, AppItemView> appsAdapter;

    AppsFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = AppsFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        model = new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication()).create(AppsViewModel.class);
        init();
    }

    void init() {
        model.getInitialization().setValue(null);
        new AppsPresenter(new InstalledApplication(this.getActivity()), this);
    }

    @Override
    public void initialized(@NonNull Observer<Void> observer) {
        model.getInitialization().observe(getViewLifecycleOwner(), observer);
    }

    @Override
    @UiThread
    public void showAllApps(List<Application> allApps) {
        binding.apps.setAdapter(appsAdapter(allApps));
        binding.apps.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @NonNull
    private CustomRecyclerAdapter<Application, AppItemView> appsAdapter(List<Application> allApps) {
        if (appsAdapter == null) {
            appsAdapter = new CustomRecyclerAdapter<>(allApps, this::appItemView, this::appSelected);
        }
        return appsAdapter;
    }

    private AppItemView appItemView() {
        return new AppItemView(getContext());
    }

    @Override
    public void appLocked(Observer<String> observer) {
        model.getAppLock().observe(getViewLifecycleOwner(), observer);
    }

    @Override
    @UiThread
    public void showAppLocked() {
        appsAdapter.notifyDataSetChanged();
    }

    @Override
    public void appUnlocked(Observer<String> observer) {
        model.getAppUnlock().observe(getViewLifecycleOwner(), observer);
    }

    @Override
    @UiThread
    public void showAppUnlocked() {
        appsAdapter.notifyDataSetChanged();
    }

    public Void appSelected(Application application) {
        application.toggleLockState();
        if (application.isLocked()) {
            model.getAppLock().setValue(application.getPackageName());
        } else {
            model.getAppUnlock().setValue(application.getPackageName());
        }
        return null;
    }
}
