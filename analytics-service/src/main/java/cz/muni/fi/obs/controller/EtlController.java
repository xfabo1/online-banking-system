package cz.muni.fi.obs.controller;

import cz.muni.fi.obs.etl.EtlExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Util controller for launching etl by hand
 */
@RestController
@RequestMapping("/v1/etl")
public class EtlController {

    private final EtlExecutor etlExecutor;

    @Autowired
    public EtlController(EtlExecutor etlExecutor) {
        this.etlExecutor = etlExecutor;
    }

    @PostMapping("/execute")
    public void execute() {
        etlExecutor.executeEtl();
    }
}
