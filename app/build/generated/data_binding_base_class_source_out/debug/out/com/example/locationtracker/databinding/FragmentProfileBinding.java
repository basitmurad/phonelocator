// Generated by view binder compiler. Do not edit!
package com.example.locationtracker.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.locationtracker.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentProfileBinding implements ViewBinding {
  @NonNull
  private final FrameLayout rootView;

  @NonNull
  public final LinearLayout btnFeedback;

  @NonNull
  public final LinearLayout btnPrivacy;

  @NonNull
  public final LinearLayout btnRateUs;

  @NonNull
  public final LinearLayout btnShareApp;

  @NonNull
  public final TextView profileName;

  @NonNull
  public final TextView textname;

  private FragmentProfileBinding(@NonNull FrameLayout rootView, @NonNull LinearLayout btnFeedback,
      @NonNull LinearLayout btnPrivacy, @NonNull LinearLayout btnRateUs,
      @NonNull LinearLayout btnShareApp, @NonNull TextView profileName,
      @NonNull TextView textname) {
    this.rootView = rootView;
    this.btnFeedback = btnFeedback;
    this.btnPrivacy = btnPrivacy;
    this.btnRateUs = btnRateUs;
    this.btnShareApp = btnShareApp;
    this.profileName = profileName;
    this.textname = textname;
  }

  @Override
  @NonNull
  public FrameLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentProfileBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentProfileBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_profile, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentProfileBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnFeedback;
      LinearLayout btnFeedback = ViewBindings.findChildViewById(rootView, id);
      if (btnFeedback == null) {
        break missingId;
      }

      id = R.id.btnPrivacy;
      LinearLayout btnPrivacy = ViewBindings.findChildViewById(rootView, id);
      if (btnPrivacy == null) {
        break missingId;
      }

      id = R.id.btnRateUs;
      LinearLayout btnRateUs = ViewBindings.findChildViewById(rootView, id);
      if (btnRateUs == null) {
        break missingId;
      }

      id = R.id.btnShareApp;
      LinearLayout btnShareApp = ViewBindings.findChildViewById(rootView, id);
      if (btnShareApp == null) {
        break missingId;
      }

      id = R.id.profileName;
      TextView profileName = ViewBindings.findChildViewById(rootView, id);
      if (profileName == null) {
        break missingId;
      }

      id = R.id.textname;
      TextView textname = ViewBindings.findChildViewById(rootView, id);
      if (textname == null) {
        break missingId;
      }

      return new FragmentProfileBinding((FrameLayout) rootView, btnFeedback, btnPrivacy, btnRateUs,
          btnShareApp, profileName, textname);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}