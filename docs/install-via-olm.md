Install Using the Operator Lifecycle Manager (OLM)
--------------------------------------------------

### Prerequisites for OpenShift

If you are installing the operator into OpenShift, note the extra steps required [here](openshift.md).

### Steps

First, install the Instana agent operator from [OperatorHub.io](https://operatorhub.io/), or [OpenShift Container Platform](https://www.openshift.com/), or [OKD](https://www.okd.io/). If you are a developer and want to test source code changes, follow the instruction on [run-operator-registry-locally.md](run-operator-registry-locally.md) to set up a local operator registry.

If you don't already have one, create the target namespace where the Instana agent should be installed. The agent does not need to run in the same namespace as the operator. Most users create a new namespace `instana-agent` for running the agents.

Third, create a custom resource with the agent configuration in the target namespace. The operator will pick up the custom resource and install the Instana agent accordingly.

The following is a minimal template of the custom resource:

```yaml
apiVersion: instana.io/v1alpha1
kind: InstanaAgent
metadata:
  name: instana-agent
  namespace: instana-agent
spec:
  agent.zone.name: '{{ (optional) name of the zone of the host }}'
  agent.key: '{{ put your Instana agent key here }}'
  agent.endpoint.host: '{{ the monitoring ingress endpoint }}'
  agent.endpoint.port: '{{ the monitoring ingress endpoint port, wrapped in quotes }}'
config.files:
  configuration.yaml: |
    # You can leave this empty, or use this to configure your instana agent.
    # See https://docs.instana.io/quick_start/agent_setup/container/kubernetes/
```

Save the template in a file `instana-agent.yaml` and edit the following values:

* If your target namespace is not `instana-agent`, replace the `namespace:` accordingly.
* `agent.key` must be set with your Instana agent key.
* `agent.endpoint` must be set with the monitoring ingress endpoint, generally either `saas-us-west-2.instana.io` or `saas-eu-west-1.instana.io`.
* `agent.endpoint.port` must be set with the monitoring ingress port, generally "443" (wrapped in quotes).
* `agent.zone.name` should be set with the name of the Kubernetes cluster that is be displayed in Instana.

For advanced configuration, you can edit the contents of the `configuration.yaml` file. View documentation here: [https://docs.instana.io/quick_start/agent_setup/container/kubernetes/](https://docs.instana.io/quick_start/agent_setup/container/kubernetes/).

Apply the custom resource with `kubectl apply -f instana-agent.yaml`. After some time, you should see `instana-agent` Pods being created on each node of your cluster, and your cluster should show on the infrastructure map on your Instana Web interface.

## Uninstalling

In order to uninstall the Instana agent, simply remove the custom resource with `kubectl delete -f instana-agent.yaml`.
