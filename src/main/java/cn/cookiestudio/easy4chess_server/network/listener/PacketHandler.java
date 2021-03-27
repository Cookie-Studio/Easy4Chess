package cn.cookiestudio.easy4chess_server.network.listener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PacketHandler {
    int priority() default 4;//MEDIUM is 4 QAQ
    boolean IgnoreCanceled() default false;
}

