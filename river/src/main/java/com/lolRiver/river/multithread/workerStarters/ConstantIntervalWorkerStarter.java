package com.lolRiver.river.multithread.workerStarters;

import com.lolRiver.river.multithread.workers.Worker;
import com.lolRiver.river.util.DateUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;
/**
 * @author mxia (mxia@lolRiver.com)
 *         9/30/13
 */

@Component
public abstract class ConstantIntervalWorkerStarter implements WorkerStarter {
    private static final Logger LOGGER = Logger.getLogger(ConstantIntervalWorkerStarter.class.getName());
    private Timestamp lastStart;

    @Override
    public abstract List<Worker> workersToStart();

    public abstract int limitedCallIntervalMinutes();

    @Override
    public boolean canStart() {
        int minutesSinceStart = DateUtil.minutesBetweenUnorderedTimestamps(this.lastStart, DateUtil.getCurrentTimestamp());

        LOGGER.info(this.getClass().getName() + " canStart(): minutesSinceStart: " + minutesSinceStart + ", minutesToWaitBeforeCallingAgain: " + limitedCallIntervalMinutes());

        return limitedCallIntervalMinutes() <= minutesSinceStart;
    }

    @Override
    public void start() {
        this.lastStart = DateUtil.getCurrentTimestamp();
    }

    public void setLastStart(Timestamp lastStart) {
        this.lastStart = lastStart;
    }
}
