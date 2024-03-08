package hello.advanced.logtrace;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import hello.advanced.trace.TraceStatus;

class FieldLogTraceTest {

	FieldLogTrace trace = new FieldLogTrace();

	@Test
	void begin_end_level2(){
		TraceStatus status1 = trace.begin("hello1");
		TraceStatus status2 = trace.begin("hello2");
		trace.end(status2);
		trace.end(status1);
	}

	@Test
	void begin_end_level2_ex(){
		TraceStatus status1 = trace.begin("hello1");
		TraceStatus status2 = trace.begin("hello2");
		trace.exception(status2, new IllegalStateException("ex"));
		trace.exception(status1, new IllegalStateException("Ex"));
	}

}