package com.learn.mobile.customview.dialog;

import android.app.Dialog;
import android.support.annotation.IdRes;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.learn.mobile.R;

/**
 * Created by BachPhuc on 4/28/2016.
 */
public class TutsPlusBottomSheetDialogFragment extends BottomSheetDialogFragment implements NavigationView.OnNavigationItemSelectedListener {
    private OnMenuBottomSheetInteractionListener mListener;
    @MenuRes
    int menuResId;
    String menuTitle;

    public void setMenuTitle(String title) {
        menuTitle = title;
    }

    public void setMenuResId(@MenuRes int menuId) {
        menuResId = menuId;
    }

    public void setOnMenuBottomSheetListener(OnMenuBottomSheetInteractionListener listener) {
        mListener = listener;
    }

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.fragment_bottom_sheet, null);
        NavigationView navigationView = (NavigationView) contentView.findViewById(R.id.bottom_sheet_navigation_view);
        if (menuResId != 0) {
            Menu menu = navigationView.getMenu();
            if (menu != null) {
                menu.clear();
            }
            navigationView.inflateMenu(menuResId);
        }

        if (menuTitle != null) {
            TextView textView = (TextView) contentView.findViewById(R.id.tv_bottom_menu_title);
            if (textView != null) {
                textView.setText(menuTitle);
            }
        }

        navigationView.setNavigationItemSelectedListener(this);
        dialog.setContentView(contentView);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        mListener.onMenuBottomSheetInteraction(item.getItemId(), item.getTitle().toString());
        dismiss();
        return true;
    }

    public interface OnMenuBottomSheetInteractionListener {
        // TODO: Update argument type and name
        void onMenuBottomSheetInteraction(int id, String title);
    }
}
