package com.lolRiver.river.controllers;

import com.lolRiver.river.multithread.BackgroundWorkerSubmitter;
import com.lolRiver.river.multithread.RawDataWorkerSubmitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author mxia (mxia@lolRiver.com)
 *         9/29/13
 */

@Controller
public class WorkerSubmitterController {
    @Autowired
    RawDataWorkerSubmitter rawDataWorkerSubmitter;
    @Autowired
    BackgroundWorkerSubmitter backgroundWorkerSubmitter;

    @RequestMapping(value = {"/rawDataWorkerSubmitter/start"}, method = RequestMethod.GET, produces = "text/plain")
    @ResponseBody
    public ResponseEntity<String> start() throws Exception {
        rawDataWorkerSubmitter.setStartNow(true);
        return new ResponseEntity<String>("Started", HttpStatus.OK);
    }

        @RequestMapping(value = {"/rawDataWorkerSubmitter/instantStart"}, method = RequestMethod.GET,
                   produces = "text/plain")
    @ResponseBody
    public ResponseEntity<String> start(@RequestParam(value = "startNow", defaultValue = "false") boolean startNow) throws Exception {
        rawDataWorkerSubmitter.setup(startNow);
        return new ResponseEntity<String>("Started with startNow: " + startNow, HttpStatus.OK);
    }

    @RequestMapping(value = {"/workerSubmitter/stop"}, method = RequestMethod.GET, produces = "text/plain")
    @ResponseBody
    public ResponseEntity<String> stop() throws Exception {
        backgroundWorkerSubmitter.teardown();
        rawDataWorkerSubmitter.teardown();
        return new ResponseEntity<String>("Stopped", HttpStatus.OK);
    }
}
