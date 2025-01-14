// Generated by view binder compiler. Do not edit!
package com.example.locationtracker.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.locationtracker.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentConnectBinding implements ViewBinding {
  @NonNull
  private final FrameLayout rootView;

  @NonNull
  public final AppCompatButton appCompatButton2;

  @NonNull
  public final EditText editText;

  @NonNull
  public final TextView textView11;

  @NonNull
  public final TextView textView13;

  @NonNull
  public final TextView textView14;

  @NonNull
  public final View view;

  private FragmentConnectBinding(@NonNull FrameLayout rootView,
      @NonNull AppCompatButton appCompatButton2, @NonNull EditText editText,
      @NonNull TextView textView11, @NonNull TextView textView13, @NonNull TextView textView14,
      @NonNull View view) {
    this.rootView = rootView;
    this.appCompatButton2 = appCompatButton2;
    this.editText = editText;
    this.textView11 = textView11;
    this.textView13 = textView13;
    this.textView14 = textView14;
    this.view = view;
  }

  @Override
  @NonNull
  public FrameLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentConnectBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentConnectBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_connect, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentConnectBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.appCompatButton2;
      AppCompatButton appCompatButton2 = ViewBindings.findChildViewById(rootView, id);
      if (appCompatButton2 == null) {
        break missingId;
      }

      id = R.id.editText;
      EditText editText = ViewBindings.findChildViewById(rootView, id);
      if (editText == null) {
        break missingId;
      }

      id = R.id.textView11;
      TextView textView11 = ViewBindings.findChildViewById(rootView, id);
      if (textView11 == null) {
        break missingId;
      }

      id = R.id.textView13;
      TextView textView13 = ViewBindings.findChildViewById(rootView, id);
      if (textView13 == null) {
        break missingId;
      }

      id = R.id.textView14;
      TextView textView14 = ViewBindings.findChildViewById(rootView, id);
      if (textView14 == null) {
        break missingId;
      }

      id = R.id.view;
      View view = ViewBindings.findChildViewById(rootView, id);
      if (view == null) {
        break missingId;
      }

      return new FragmentConnectBinding((FrameLayout) rootView, appCompatButton2, editText,
          textView11, textView13, textView14, view);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
