package com.ga.gettingactive.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ga.gettingactive.Task;
import com.ga.gettingactive.R;
import com.ga.gettingactive.TaskAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TaskAdapter taskAdapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = root.findViewById(R.id.ongoing_tasks_list);
        if(recyclerView==null)
            Log.d("NULLPOINTER", "Recycler View is null");
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        taskAdapter = new TaskAdapter();
        Collection<Task> tasks = new ArrayList<>();
        tasks.add(new Task("Помыть жопу", "Помойте вашу жопу"));
        tasks.add(new Task("Воздержитесь от онанизма", "Корни вашей непродуктивности лежат в интенсивной мастурбации. Если вы хотите " +
                "действительно жестко гриндить по жизни, вы должны перестать заниматься этим хотя бы больше трех раз в день."));
        recyclerView.setAdapter(taskAdapter);
        taskAdapter.setItems(tasks);

        return root;
    }
}