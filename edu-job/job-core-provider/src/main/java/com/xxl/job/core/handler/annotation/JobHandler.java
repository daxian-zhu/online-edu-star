package com.xxl.job.core.handler.annotation;

import java.lang.annotation.*;

/**
 * annotation for job handler
 *
 * will be replaced by {@link com.xxl.job.core.handler.annotation.XxlJob}
 *
 * @author 2016-5-17 21:06:49
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Deprecated
public @interface JobHandler {

    String value();

}
