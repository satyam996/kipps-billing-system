
package com.kipps.billingsystem;

import io.camunda.client.CamundaClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CamundaConnectionTest implements CommandLineRunner {

    private final CamundaClient camundaClient;

    public CamundaConnectionTest(CamundaClient camundaClient) {
        this.camundaClient = camundaClient;
    }

    @Override
    public void run(String... args) {
        camundaClient.newTopologyRequest()
                .send()
                .join();

        System.out.println("SUCCESS: Connected to Camunda 8!");
    }
}
