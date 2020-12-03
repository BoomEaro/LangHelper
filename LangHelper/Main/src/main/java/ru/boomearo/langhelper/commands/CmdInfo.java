package ru.boomearo.langhelper.commands;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(METHOD)
public @interface CmdInfo {
	String name();
	String description() default "";
	String usage() default "";
	String permission() default "";
	String[] aliases() default {""};
}
