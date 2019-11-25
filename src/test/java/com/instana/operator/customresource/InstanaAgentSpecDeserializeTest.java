package com.instana.operator.customresource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.instana.operator.file.ClasspathFile;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.instana.operator.customresource.InstanaAgentSpec.DEFAULT_AGENT_CLUSTER_ROLE_BINDING_NAME;
import static com.instana.operator.customresource.InstanaAgentSpec.DEFAULT_AGENT_CLUSTER_ROLE_NAME;
import static com.instana.operator.customresource.InstanaAgentSpec.DEFAULT_AGENT_CONFIG_MAP_NAME;
import static com.instana.operator.customresource.InstanaAgentSpec.DEFAULT_AGENT_CPU_LIMIT;
import static com.instana.operator.customresource.InstanaAgentSpec.DEFAULT_AGENT_CPU_REQ;
import static com.instana.operator.customresource.InstanaAgentSpec.DEFAULT_AGENT_DAEMON_SET_NAME;
import static com.instana.operator.customresource.InstanaAgentSpec.DEFAULT_AGENT_HTTP_LISTEN;
import static com.instana.operator.customresource.InstanaAgentSpec.DEFAULT_AGENT_IMAGE_NAME;
import static com.instana.operator.customresource.InstanaAgentSpec.DEFAULT_AGENT_IMAGE_TAG;
import static com.instana.operator.customresource.InstanaAgentSpec.DEFAULT_AGENT_MEM_LIMIT;
import static com.instana.operator.customresource.InstanaAgentSpec.DEFAULT_AGENT_MEM_REQ;
import static com.instana.operator.customresource.InstanaAgentSpec.DEFAULT_AGENT_MODE;
import static com.instana.operator.customresource.InstanaAgentSpec.DEFAULT_AGENT_RBAC_CREATE;
import static com.instana.operator.customresource.InstanaAgentSpec.DEFAULT_AGENT_SECRET_NAME;
import static com.instana.operator.customresource.InstanaAgentSpec.DEFAULT_AGENT_SERVICE_ACCOUNT_NAME;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.core.StringStartsWith.startsWith;

class InstanaAgentSpecDeserializeTest {

  @Test
  void testDefaultValues() throws IOException {
    String yaml = ClasspathFile.read("/customresource-min.yaml");
    String json = yamlToJson(yaml);
    ObjectMapper mapper = new ObjectMapper();
    InstanaAgentSpec spec = mapper.readValue(json, InstanaAgentSpec.class);

    assertThat(spec.getAgentZoneName(), equalTo("my-k8s-cluster"));
    assertThat(spec.getAgentKey(), equalTo("_PUT_YOUR_AGENT_KEY_HERE_"));
    assertThat(spec.getAgentEndpointHost(), equalTo("saas-us-west-2.instana.io"));
    assertThat(spec.getAgentEndpointPort(), equalTo(443));
    assertThat(spec.getConfigFiles().entrySet(), empty());

    assertThat(spec.getAgentClusterRoleName(), equalTo(DEFAULT_AGENT_CLUSTER_ROLE_NAME));
    assertThat(spec.getAgentClusterRoleBindingName(), equalTo(DEFAULT_AGENT_CLUSTER_ROLE_BINDING_NAME));
    assertThat(spec.getAgentServiceAccountName(), equalTo(DEFAULT_AGENT_SERVICE_ACCOUNT_NAME));
    assertThat(spec.getAgentSecretName(), equalTo(DEFAULT_AGENT_SECRET_NAME));
    assertThat(spec.getAgentDaemonSetName(), equalTo(DEFAULT_AGENT_DAEMON_SET_NAME));
    assertThat(spec.getAgentConfigMapName(), equalTo(DEFAULT_AGENT_CONFIG_MAP_NAME));
    assertThat(spec.isAgentRbacCreate(), equalTo(Boolean.parseBoolean(DEFAULT_AGENT_RBAC_CREATE)));
    assertThat(spec.getAgentImageName(), equalTo(DEFAULT_AGENT_IMAGE_NAME));
    assertThat(spec.getAgentImageTag(), equalTo(DEFAULT_AGENT_IMAGE_TAG));
    assertThat(spec.getAgentMode(), equalTo(DEFAULT_AGENT_MODE));
    assertThat(spec.getAgentCpuReq(), equalTo(Double.parseDouble(DEFAULT_AGENT_CPU_REQ)));
    assertThat(spec.getAgentCpuLimit(), equalTo(Double.parseDouble(DEFAULT_AGENT_CPU_LIMIT)));
    assertThat(spec.getAgentMemReq(), equalTo(Integer.parseInt(DEFAULT_AGENT_MEM_REQ)));
    assertThat(spec.getAgentMemLimit(), equalTo(Integer.parseInt(DEFAULT_AGENT_MEM_LIMIT)));
    assertThat(spec.getAgentHttpListen(), equalTo(DEFAULT_AGENT_HTTP_LISTEN));
    assertThat(spec.getAgentHostRepository(), nullValue());

    assertThat(spec.getAgentDownloadKey(), nullValue());
    assertThat(spec.getAgentProxyHost(), nullValue());
    assertThat(spec.getAgentProxyPort(), nullValue());
    assertThat(spec.getAgentProxyProtocol(), nullValue());
    assertThat(spec.getAgentProxyUser(), nullValue());
    assertThat(spec.getAgentProxyPassword(), nullValue());
    assertThat(spec.isAgentProxyUseDNS(), nullValue());
  }

  @Test
  void testCustomValues() throws IOException {
    String yaml = ClasspathFile.read("/customresource-max.yaml");
    String json = yamlToJson(yaml);
    ObjectMapper mapper = new ObjectMapper();
    InstanaAgentSpec spec = mapper.readValue(json, InstanaAgentSpec.class);

    assertThat(spec.getAgentZoneName(), equalTo("my-k8s-cluster"));
    assertThat(spec.getAgentKey(), equalTo("_PUT_YOUR_AGENT_KEY_HERE_"));
    assertThat(spec.getAgentEndpointHost(), equalTo("saas-us-west-2.instana.io"));
    assertThat(spec.getAgentEndpointPort(), equalTo(443));
    assertThat(spec.getConfigFiles(), allOf(
        hasEntry(equalTo("configuration.yaml"), startsWith("# You can leave ")),
        hasEntry(equalTo("other"), startsWith("some other config file"))));

    assertThat(spec.getAgentClusterRoleName(), equalTo("test-cluster-role"));
    assertThat(spec.getAgentClusterRoleBindingName(), equalTo("test-cluster-role-binding"));
    assertThat(spec.getAgentServiceAccountName(), equalTo("test-service-account"));
    assertThat(spec.getAgentSecretName(), equalTo("test-secret"));
    assertThat(spec.getAgentDaemonSetName(), equalTo("test-daemon-set"));
    assertThat(spec.getAgentConfigMapName(), equalTo("test-config-map"));
    assertThat(spec.isAgentRbacCreate(), equalTo(Boolean.FALSE));
    assertThat(spec.getAgentImageName(), equalTo("instana/test-image"));
    assertThat(spec.getAgentImageTag(), equalTo("1.2.3"));
    assertThat(spec.getAgentMode(), equalTo("NONE"));
    assertThat(spec.getAgentCpuReq(), equalTo(0.7));
    assertThat(spec.getAgentCpuLimit(), equalTo(1.8));
    assertThat(spec.getAgentMemReq(), equalTo(513));
    assertThat(spec.getAgentMemLimit(), equalTo(518));
    assertThat(spec.getAgentHttpListen(), equalTo("127.0.0.1"));
    assertThat(spec.getAgentHostRepository(), equalTo("/Users/stan/.m2/repository"));

    assertThat(spec.getAgentDownloadKey(), equalTo("test-download-key"));
    assertThat(spec.getAgentProxyHost(), equalTo("proxy.instana.io"));
    assertThat(spec.getAgentProxyPort(), equalTo(8443));
    assertThat(spec.getAgentProxyProtocol(), equalTo("https"));
    assertThat(spec.getAgentProxyUser(), equalTo("proxy-user"));
    assertThat(spec.getAgentProxyPassword(), equalTo("proxy-password"));
    assertThat(spec.isAgentProxyUseDNS(), equalTo(true));
  }

  @Test
  void testEquals() throws Exception {
    String yaml = ClasspathFile.read("/customresource-min.yaml");
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    InstanaAgentSpec spec1 = mapper.readValue(yaml, InstanaAgentSpec.class);
    InstanaAgentSpec spec2 = mapper.readValue(yaml, InstanaAgentSpec.class);
    assertThat(spec1, equalTo(spec2));
    spec2.setAgentCpuReq(0.33);
    assertThat(spec1, not(equalTo(spec2)));
  }

  String yamlToJson(String yaml) throws IOException {
    ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
    Object obj = yamlReader.readValue(yaml, Object.class);
    ObjectMapper jsonWriter = new ObjectMapper();
    return jsonWriter.writeValueAsString(obj);
  }
}
