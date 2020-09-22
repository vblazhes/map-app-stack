package finki.ukim.mk.map_application;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import finki.ukim.mk.map_application.model.User;
import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserProfileDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserProfileDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserProfileDetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String USER_KEY = "USER";

    // TODO: Rename and change types of parameters
    private User mUser;

    private OnFragmentInteractionListener mListener;

    //LayoutViews
    TextView userNameTextView;
    TextView userUsernameTextView;
    TextView userEmailTextView;
    TextView userRoleTextView;



    public UserProfileDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param user Parameter 1.
     * @return A new instance of fragment UserProfileDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserProfileDetailsFragment newInstance(User user) {
        UserProfileDetailsFragment fragment = new UserProfileDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(USER_KEY, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUser = (User) getArguments().getSerializable(USER_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile_details, container, false);

        userNameTextView = view.findViewById(R.id.user_name_profile);
        userUsernameTextView = view.findViewById(R.id.user_username_profile);
        userEmailTextView = view.findViewById(R.id.user_email_profile);
        userRoleTextView = view.findViewById(R.id.user_role_profile);

        String user_name ="Name:  "+mUser.getName();
        userNameTextView.setText(user_name);

        String user_username ="Username:  "+mUser.getUsername();
        userUsernameTextView.setText(user_username);

        String user_email ="Email:  "+mUser.getEmail();
        userEmailTextView.setText(user_email);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
