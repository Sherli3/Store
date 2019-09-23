package store.configuration.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.apache.log4j.Logger;

@Aspect
public class LoggerAspectJ {

	private static final Logger logger = Logger.getLogger(LoggerAspectJ.class);

	@Before("execution(* store.configuration.service.impl.*.*(..))")
	public void logBefore(JoinPoint joinPoint) {
		logger.info("before method: " + joinPoint.getSignature().getName());
	}

	@After("execution(* store.configuration.service.impl.*.*(..))")
	public void logAfter(JoinPoint joinPoint) {
		logger.info("after method: " + joinPoint.getSignature().getName());
	}

	@AfterReturning(pointcut = "execution(* store.configuration.service.impl.*.*(..))",
			returning = "result")
	public void logAfterReturning(JoinPoint joinPoint, Object result) {
		logger.info("after return method : " + joinPoint.getSignature().getName());
		logger.info("Method returned value is : " + result);
	}

	@AfterThrowing(pointcut = "execution(* store.configuration.service.impl.*.*(..))",
			throwing = "error")
	public void logThrow(JoinPoint joinPoint, Throwable error) {
		logger.info("exception in method: " + joinPoint.getSignature().getName());
		logger.error("Exception is: " + error);
	}


}
