package finki.ukim.mk.map_application;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import finki.ukim.mk.map_application.model.auth.LoginRequest;
import finki.ukim.mk.map_application.model.auth.AuthenticatedUser;
import finki.ukim.mk.map_application.viewmodel.LoginViewModel;

import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "LOGIN_FRAGMENT";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    //Fragment building block objects
    AuthenticatedUser loginAuthenticatedUser;

    //ViewModel
    private LoginViewModel viewModel;

    //Layout views
    private EditText username_et;
    private EditText password_et;
    private Button login_btn;
    private RelativeLayout rl_loginFailed;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(LoginViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        username_et = view.findViewById(R.id.loginUsername_editText);
        password_et = view.findViewById(R.id.loginPassword_editText);
        login_btn = view.findViewById(R.id.loginFragment_btn);
        rl_loginFailed = view.findViewById(R.id.rl_loginFailed);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginRequest loginRequest = new LoginRequest(username_et.getText().toString(), password_et.getText().toString());
                try {
                    onButtonPressed(loginRequest);
                } catch (ExecutionException e) {
                    Toast.makeText(getContext(), "ExecutionException: Login failed!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    Toast.makeText(getContext(), "InterruptedException: Login failed!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(LoginRequest loginRequest) throws ExecutionException, InterruptedException {
        AuthenticatedUser authenticatedUser = viewModel.authenticateUser(loginRequest);

        if (mListener != null && authenticatedUser != null) {
            loginAuthenticatedUser = authenticatedUser;
            mListener.onFragmentInteraction(TAG, authenticatedUser);
        } else {
            rl_loginFailed.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), "Login Failed!", Toast.LENGTH_SHORT).show();
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

//    @Override
//    public void processFinish(LoginSuccessful output) {
//        if(output == null){
//            Toast.makeText(getContext(), "Login failed", Toast.LENGTH_SHORT).show();
//        }else {
//            loginSuccessfulUser = output;
//            isUserLoggedIn = true;
//        }
//    }

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
        void onFragmentInteraction(String TAG, AuthenticatedUser authenticatedUser);
    }
}
