package hello.advanced;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import hello.advanced.logtrace.FieldLogTrace;
import hello.advanced.logtrace.LogTrace;
import hello.advanced.logtrace.ThreadLocalLogTrace;

@Configuration
public class LogTraceConfig {

	@Bean
	public LogTrace logTrace(){
		//return new FieldLogTrace();
		return new ThreadLocalLogTrace();
	}

}
