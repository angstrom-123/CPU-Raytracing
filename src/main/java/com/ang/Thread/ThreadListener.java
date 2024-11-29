package com.ang.Thread;

public interface ThreadListener {
    void threadComplete(Worker w);
    void forceStop();
}
