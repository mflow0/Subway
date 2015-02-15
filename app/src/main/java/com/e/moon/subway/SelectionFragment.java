package com.e.moon.subway;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.FriendPickerFragment;
import com.facebook.widget.PickerFragment;
import com.facebook.widget.ProfilePictureView;

import java.util.ArrayList;
import java.util.List;


public class SelectionFragment extends Fragment {
    private FragmentActivity context;
    private List<GraphUser> tags;
    private ProfilePictureView profilePictureView;
    private Facebook activity;
    private ViewGroup controlsContainer;
    private Button pickFriendsButton;

    private UiLifecycleHelper uiHelper;
    private Session.StatusCallback sessionCallback = new Session.StatusCallback() {
        @Override
        public void call(final Session session, final SessionState state, final Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (Facebook) getActivity();
        uiHelper = new UiLifecycleHelper(getActivity(), sessionCallback);
        uiHelper.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.selection, container, false);
        controlsContainer = (ViewGroup) view.findViewById(R.id.main_ui_container);
        pickFriendsButton = (Button) view.findViewById(R.id.pickFriendsButton);
        pickFriendsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onClickPickFriends();
            }
        });
        profilePictureView = (ProfilePictureView) view.findViewById(R.id.selection_profile_pic);
        profilePictureView.setCropped(true);

        profilePictureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.showSettingsFragment();
            }
        });
        return view;
    }

    private void onClickPickFriends() {
        final FriendPickerFragment fragment = new FriendPickerFragment();
        setFriendPickerListeners(fragment);
        showPickerFragment(fragment);
    }

    private void setFriendPickerListeners(final FriendPickerFragment fragment) {
        fragment.setOnDoneButtonClickedListener(new FriendPickerFragment.OnDoneButtonClickedListener() {
            @Override
            public void onDoneButtonClicked(PickerFragment<?> pickerFragment) {
                onFriendPickerDone(fragment);
            }
        });
    }

    private void onFriendPickerDone(FriendPickerFragment fragment) {
        context=(FragmentActivity) activity;
        FragmentManager fm = context.getSupportFragmentManager();
        fm.popBackStack();
        String results = "";
        List<GraphUser> selection = fragment.getSelection();
        tags = selection;
        if (selection != null && selection.size() > 0) {
            ArrayList<String> names = new ArrayList<String>();
            for (GraphUser user : selection) {
                names.add(user.getName());
            }
            results = TextUtils.join(", ", names);
        } else {
            results = "없음";
        }
        showAlert("친구목록", results);
    }
    private void showAlert(String title, String message) {
        new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.ok, null)
                .show();
        controlsContainer.setVisibility(View.VISIBLE);
    }

    private void showPickerFragment(PickerFragment<?> fragment) {
        fragment.setOnErrorListener(new PickerFragment.OnErrorListener() {
            @Override
            public void onError(PickerFragment<?> pickerFragment, FacebookException error) {
                Toast toast = Toast.makeText(getActivity(), "에러 : "+error.getMessage(), Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        context=(FragmentActivity) activity;
        FragmentManager fm = context.getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
        controlsContainer.setVisibility(View.GONE);
// We want the fragment fully created so we can use it immediately.
        fm.executePendingTransactions();
        fragment.loadData(false);
    }

    private void onSessionStateChange(final Session session, SessionState state, Exception exception) {
        if (session != null && session.isOpened()) {

        } else {
            profilePictureView.setProfileId(null);
        }
    }


}
