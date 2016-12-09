package takamk2.local.wish.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import takamk2.local.wish.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskDisplayFragment extends Fragment {


    public TaskDisplayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_display, container, false);
    }

}
