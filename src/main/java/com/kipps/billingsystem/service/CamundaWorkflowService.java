
package com.kipps.billingsystem.service;

import io.camunda.client.CamundaClient;
import org.springframework.stereotype.Service;

@Service
public class CamundaWorkflowService {

    private final CamundaClient camundaClient;

    public CamundaWorkflowService(CamundaClient camundaClient) {
        this.camundaClient = camundaClient;
    }

    public CamundaClient getCamundaClient() {
        return camundaClient;
    }
}
