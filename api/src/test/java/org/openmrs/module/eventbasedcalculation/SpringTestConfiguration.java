package org.openmrs.module.eventbasedcalculation;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource({ "classpath:applicationContext-service.xml", "classpath*:moduleApplicationContext.xml" })
public class SpringTestConfiguration {
}
