package com.lockit;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import retrofit.RetrofitError;
import rx.Observable;
import rx.android.app.AppObservable;
import rx.subjects.PublishSubject;

public class BaseFragment extends Fragment {
    PublishSubject<Void> detached = PublishSubject.create();
//    private AsyncLoader asyncLoader;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        asyncLoader = AsyncLoader.dialog(getActivity());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        detached.onNext(null);
    }

    protected <T> Observable<T> bindObservable(Observable<T> in) {
        return AppObservable.bindFragment(this, in).takeUntil(detached);
    }

    public void showLoader() {
        try {
//            asyncLoader.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void hideLoader() {
        try {
//            asyncLoader.hide();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void error(final Throwable throwable) {
        new Handler(Looper.getMainLooper()).post(() -> genericError(throwable));
    }

    private void genericError(final Throwable throwable) {
        Logger.logError(throwable);

        if (isAuthError(throwable)) {
            showUnauthorizedDialog();
            return;
        }

        String errorMessage =
                isNetworkError(throwable) ? "Network error" : "Some error occurred. Try again.";
        if (getActivity() != null) {
            Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
            hideLoader();
        }
    }

    private void showUnauthorizedDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setMessage("Seems your session expired. Please sign in.")
//                .setPositiveButton("OK", (dialog, which) -> {
//                    AuthActivity_.intent(getActivity()).isSigninMode(true).start();
//                    getActivity().finish();
//                }).show();
    }

    private boolean isAuthError(final Throwable throwable) {
        return throwable instanceof RetrofitError && isAuthError((RetrofitError) throwable);
    }

    private boolean isAuthError(final RetrofitError retrofitError) {
        if (retrofitError.getResponse() != null) {
            return retrofitError.getResponse().getStatus() == 401;
        }
        return false;
    }

    private boolean isNetworkError(final Throwable throwable) {
        if (throwable instanceof RetrofitError) {
            return ((RetrofitError) throwable).getKind().equals(RetrofitError.Kind.NETWORK);
        }
        return false;
    }

    protected boolean onBackPressed() {
        return false;
    }
}
