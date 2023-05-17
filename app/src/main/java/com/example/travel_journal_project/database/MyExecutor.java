package com.example.travel_journal_project.database;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MyExecutor {

    private final Executor executor;

    public MyExecutor() {
        executor = Executors.newSingleThreadExecutor();
    }

    public void execute(Runnable runnable) {
        executor.execute(runnable);
    }

}
