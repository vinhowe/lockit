package com.lockit.apps;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lockit.Application;
import com.lockit.BaseFragment;
import com.lockit.CustomRecyclerAdapter;
import com.lockit.R;
import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.IgnoredWhenDetached;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import rx.Observable;
import rx.subjects.PublishSubject;

import static com.lockit.BusProvider.bus;

@EFragment(R.layout.apps_fragment)
public class AppsFragment extends BaseFragment implements ApplicationView {
    private PublishSubject<Void> initialization = PublishSubject.create();
    private PublishSubject<String> appLock = PublishSubject.create();
    private PublishSubject<String> appUnlock = PublishSubject.create();

    @ViewById
    RecyclerView apps;

    private CustomRecyclerAdapter<Application, AppItemView> appsAdapter;

    @AfterViews
    void init() {
        bus().register(this);
        initialization.onNext(null);
    }

    @Override
    public Observable<Void> initialized() {
        return bindObservable(initialization.asObservable());
    }

    @Override
    @UiThread
    @IgnoredWhenDetached
    public void showAllApps(List<Application> allApps) {
        apps.setAdapter(appsAdapter(allApps));
        apps.setLayoutManager(new LinearLayoutManager(getActivity()));
//        ItemClickSupport.addTo(apps)
//                .setOnItemClickListener(
//                        (view, position, id) -> appSelected(appsAdapter.getItem(position)));
    }

    @NonNull
    private CustomRecyclerAdapter<Application, AppItemView> appsAdapter(List<Application> allApps) {
        if (appsAdapter == null)
            appsAdapter = new CustomRecyclerAdapter<>(allApps, this::appItemView);
        return appsAdapter;
    }

    private AppItemView appItemView() {
        return AppItemView_.build(getActivity());
    }

    @Override
    public Observable<String> appLocked() {
        return bindObservable(appLock.asObservable());
    }

    @Override
    @UiThread
    @IgnoredWhenDetached
    public void showAppLocked() {
        appsAdapter.notifyDataSetChanged();
    }

    @Override
    public Observable<String> appUnlocked() {
        return bindObservable(appUnlock.asObservable());
    }

    @Override
    @UiThread
    @IgnoredWhenDetached
    public void showAppUnlocked() {
        appsAdapter.notifyDataSetChanged();
    }

    @Subscribe
    public void appSelected(Application application) {
        if (application.isLocked())
            appUnlock.onNext(application.getPackageName());
        else
            appLock.onNext(application.getPackageName());
        application.toggleLockState();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bus().unregister(this);
    }
}
