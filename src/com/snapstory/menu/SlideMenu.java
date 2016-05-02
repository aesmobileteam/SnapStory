package com.snapstory.menu;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.snapstory.AppClass;
import com.snapstory.Login;
import com.snapstory.ProjectsActivity;
import com.snapstory.R;

public class SlideMenu {

	
	public static MenuDrawer setMenu(final Activity mActivity, int layoutId) {

		final MenuDrawer mMenuDrawer = MenuDrawer.attach(mActivity,Position.LEFT);
		mMenuDrawer.setContentView(layoutId);
		mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN);
		mMenuDrawer.setDropShadowEnabled(false);
		mMenuDrawer.setBackgroundColor(mActivity.getResources().getColor(R.color.md__defaultBackground));


		mMenuDrawer.setMenuView(R.layout.menu_view);

		
		

		((LinearLayout)mActivity.findViewById(R.id.ProjectsList)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mActivity.startActivity(new Intent(mActivity,ProjectsActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
				mActivity.finish();

			}
		});

		((LinearLayout)mActivity.findViewById(R.id.SignOut)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AppClass.logOut();
				mActivity.startActivity(new Intent(mActivity,Login.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
				mActivity.finish();
			}
		});

		
		
		
		mMenuDrawer
		.setOnInterceptMoveEventListener(new MenuDrawer.OnInterceptMoveEventListener() {
			@Override
			public boolean isViewDraggable(View v, int dx, int x, int y) {
				return v instanceof SeekBar;
			}
		});

		return mMenuDrawer;
	}



}
