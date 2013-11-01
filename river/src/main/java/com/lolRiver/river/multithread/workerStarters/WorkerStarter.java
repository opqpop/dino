package com.lolRiver.river.multithread.workerStarters;

import com.lolRiver.river.multithread.workers.Worker;

import java.util.List;
/**
 * @author mxia (mxia@lolRiver.com)
 *         9/29/13
 */

public interface WorkerStarter {
    public List<Worker> workersToStart();

    public boolean canStart();

    public void start();
}
