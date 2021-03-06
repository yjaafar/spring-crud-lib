package io.github.vincemann.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;

@SpringBootApplication
@ComponentScan(basePackages = {
		"io.github.vincemann.demo",
		"io.github.vincemann.generic.crud.lib"})
@Slf4j
public class PetClinicGuruVersionApplication {

	public static void main(String[] args){
		ApplicationContext context = SpringApplication.run(PetClinicGuruVersionApplication.class, args);
	}

}
