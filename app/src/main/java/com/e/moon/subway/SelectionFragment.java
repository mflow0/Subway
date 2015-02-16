package com.e.moon.subway;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.FriendPickerFragment;
import com.facebook.widget.PickerFragment;
import com.facebook.widget.ProfilePictureView;
import com.facebook.widget.WebDialog;

import java.util.ArrayList;
import java.util.List;


public class SelectionFragment extends Fragment {
    private String name;
    private String facebook_caption;
    private String facebook_link;
    private String facebook_description;
    private String facebook_picture;
    private ProfilePictureView profilePictureView;
    private Facebook activity;
    private Button pickFriendsButton;
    private FragmentManager fm;
    private FriendPickerFragment fragment;

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
        fragment.loadData(false); //친구 데이터 불러오기
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.selection, container, false);
        profilePictureView = (ProfilePictureView) view.findViewById(R.id.selection_profile_pic);
        profilePictureView.setCropped(true);
        profilePictureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.showSettingsFragment();
            }
        });

        fm = getFragmentManager();
        Fragment frag = fm.findFragmentById(R.id.fragment_container);
        fragment = new FriendPickerFragment();

        fragment.setOnErrorListener(new PickerFragment.OnErrorListener() {
            @Override
            public void onError(PickerFragment<?> pickerFragment, FacebookException error) {
                Toast toast = Toast.makeText(getActivity(), "에러 : " + error.getMessage(), Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        fm.beginTransaction().add(R.id.fragment_container, fragment, "friend").commit();

        fragment.setOnDoneButtonClickedListener(new FriendPickerFragment.OnDoneButtonClickedListener() {
            @Override
            public void onDoneButtonClicked(PickerFragment<?> pickerFragment) {
                onFriendPickerDone(fragment);
            }
        });

        return view;
    }

    /**
     * 완료 버튼 클릭시 진행할 메서드
     */
    private void onFriendPickerDone(FriendPickerFragment fragment) {
        fm.popBackStack();
        List<GraphUser> selection = fragment.getSelection();
        if (selection != null && selection.size() > 0) {
            ArrayList<String> names = new ArrayList<String>();
            for (GraphUser user : selection) {
                //publishFeedDialog(user.getId());
                names.add(user.getId());   //user.getName() 친구 이름 받기
            }
            // ,로 에러 발생 여부
            String messages = TextUtils.join(",", names);
            publishFeedDialog(messages);
        } else {
            showAlert("\n  친구목록을 선택해주세요.\n");
        }
    }

    private void showAlert(String message) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    private void onSessionStateChange(final Session session, SessionState state, Exception exception) {
        if (session != null && session.isOpened()) {

        } else {
            profilePictureView.setProfileId(null);
        }
    }

    /**
     * 글쓰기 (feed) dialog 창
     */
    private void publishFeedDialog(String some) {
        name = getResources().getString(R.string.facebook_name);
        facebook_caption = getResources().getString(R.string.facebook_caption);
        facebook_link = getResources().getString(R.string.facebook_link);
        facebook_picture = getResources().getString(R.string.facebook_picture);
        facebook_description = getResources().getString(R.string.facebook_description);

        Bundle params = new Bundle();
        params.putString("to",some);
        params.putString("name", name);
        params.putString("caption", facebook_caption);
        params.putString("description", facebook_description);
        params.putString("link", facebook_link);
        params.putString("picture", facebook_picture);

        // Invoke the dialog
        WebDialog feedDialog = (
                new WebDialog.FeedDialogBuilder(getActivity(),
                        Session.getActiveSession(),
                        params))
                .setOnCompleteListener(new WebDialog.OnCompleteListener() {

                    @Override
                    public void onComplete(Bundle values,
                                           FacebookException error) {
                        if (error == null) {
                            // When the story is posted, echo the success
                            // and the post Id.
                            final String postId = values.getString("post_id");  //post_id 보낸 id 값
                            if (postId != null) {
                                Toast.makeText(getActivity(),
                                        "정상적으로 전송 되었습니다", //+ postId
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                // User clicked the Cancel button
                                Toast.makeText(getActivity(),
                                        "취소 되었습니다",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else if (error instanceof FacebookOperationCanceledException) {
                            // User clicked the "x" button
                            Toast.makeText(getActivity(),
                                    "취소 되었습니다",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Generic, ex: network error
                            Toast.makeText(getActivity(),
                                    "전송 중 에러가 발생하였습니다",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                })
                .build();
        feedDialog.show();
    }


}
