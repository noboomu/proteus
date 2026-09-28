package io.sinistral.proteus.protocol;

import com.fasterxml.jackson.annotation.JsonInclude;

/** Summary of application health, currently an empty placeholder. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HealthSummary {

    /** Creates an empty health summary. */
    public HealthSummary() {}
}
