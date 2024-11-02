package org.sitecenter.common.infra;

import java.util.UUID;

/** This class will give you unique instance ID for work with various services like queues, locks, etc.
 * this Id will be unique for application instance and will help to hold that locks for certain application instance.
 *
 * Use as:
 *  @Bean
 *     public InstanceIdProvider instanceIdProvider() {
 *         return new InstanceIdProvider("prefix-");
 *     }
 *
 *  then in code:
 *  @Autowired
 *  InstanceIdProvider instanceIdProvider;
 *
 *  ...
 *  String uniqueAppId = instanceIdProvider.getInstanceId();
 *
 *  ..
 */
public class InstanceIdProvider {
    private final String instanceId;

    public InstanceIdProvider(String prefix) {
        // Generate the ID with prefix and a random UUID
        this.instanceId = prefix + UUID.randomUUID().toString().replace("-", "");
    }

    public String getInstanceId() {
        return instanceId;
    }

    @Override
    public String toString() {
        return "InstanceIdProvider{" +
                "instanceId='" + instanceId + '\'' +
                '}';
    }
}