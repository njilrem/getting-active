package com.ga.gettingactive.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ga.gettingactive.TaskContainer;
import com.ga.gettingactive.R;
import com.ga.gettingactive.TaskAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TaskAdapter taskAdapter;

    private String TAG = "HomeFragment";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = root.findViewById(R.id.ongoing_tasks_list);
        if(recyclerView==null)
            Log.d("NULLPOINTER", "Recycler View is null");
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Collection<TaskContainer> tasks = new ArrayList<>();
        tasks.add(new TaskContainer("Помыть жопу", "Помойте вашу жопу"));
        tasks.add(new TaskContainer("Воздержитесь от онанизма", "Корни вашей непродуктивности лежат в интенсивной мастурбации. Если вы хотите " +
                "действительно жестко гриндить по жизни, вы должны перестать заниматься этим хотя бы больше трех раз в день."));
        /*
        DB
         */
        db.collection("tasks/firstBundle/tasks")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            TaskContainer taskContainer = document.toObject(TaskContainer.class);
                            Log.d("TASK", String.valueOf(taskContainer));
                            tasks.add(taskContainer);
                            if(!tasks.contains(taskContainer)){
                                Log.d("ERROR TASKS", "MISSING");
                            }else {
                                Log.d("ERROR TASKS", "AVAILEBLE");
                            }
                        }
                        taskAdapter = new TaskAdapter();
                        recyclerView.setAdapter(taskAdapter);
                        taskAdapter.setItems(tasks);
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
        Log.d("TASKS", String.valueOf(tasks.size()));
        return root;
    }
}