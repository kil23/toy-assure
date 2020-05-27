package com.channel.service;

import com.channel.spring.SpringConfig;
import org.springframework.context.annotation.*;
import org.springframework.context.annotation.ComponentScan.Filter;

@Configuration
@ComponentScan(//
		basePackages = {"com"},
		excludeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, value = { SpringConfig.class })//
)
@PropertySources({ //
		@PropertySource(value = "classpath:./com/channel/test.properties", ignoreResourceNotFound = true) //
})
public class QaConfig {


}
