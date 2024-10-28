package me.tiary.dummydata.aop;

import lombok.extern.slf4j.Slf4j;
import me.tiary.dummydata.annotation.EntityGenerationLogging;
import me.tiary.dummydata.annotation.EntityInsertionLogging;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.util.List;

@Aspect
@Component
@Slf4j
public final class EntityManipulationLoggingAspect {
    @AfterReturning(value = "@annotation(entityGenerationLogging)", returning = "totalRows")
    public void logFinishedEntityGeneration(final EntityGenerationLogging entityGenerationLogging,
                                            final long totalRows) {
        log.info("Finished {} generation: {} rows",
                entityGenerationLogging.entity(),
                NumberFormat.getInstance().format(totalRows));
    }

    @Around("@annotation(entityInsertionLogging)")
    public Object logEntityInsertion(final ProceedingJoinPoint joinPoint,
                                     final EntityInsertionLogging entityInsertionLogging) throws Throwable {
        final Object[] args = joinPoint.getArgs();
        final List<?> entities = (List<?>) args[0];

        try {
            final Object result = joinPoint.proceed();

            log.info("Inserted {}: {} rows",
                    entityInsertionLogging.entity(),
                    NumberFormat.getInstance().format(entities.size()));

            return result;
        } catch (final Exception ex) {
            log.error("Failed to insert {}: {}",
                    entityInsertionLogging.entity(),
                    ex.getMessage());

            throw ex;
        }
    }
}