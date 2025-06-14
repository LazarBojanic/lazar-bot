package com.lazar.lazarwordleclonebackendspring.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.util.Map;
import java.util.stream.Collectors;

@Component
public class Runner implements CommandLineRunner {
	@Autowired
	private RequestMappingHandlerMapping requestMappingHandlerMapping;

	@Override
	public void run(String... args) throws Exception {
		//printAllMappings();
	}
	public void printAllMappings() {
		Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();

		handlerMethods.entrySet().stream()
				.collect(Collectors.groupingBy(
						entry -> entry.getKey().getMethodsCondition().getMethods().stream()
								.findFirst()
								.map(Enum::name)
								.orElse("UNDEFINED")))
				.entrySet().stream()
				.sorted(Map.Entry.comparingByKey()) // Sort by HTTP method
				.forEach(methodGroup -> {
					System.out.println("\n=== " + methodGroup.getKey() + " ===");
					methodGroup.getValue().stream()
							.sorted((e1, e2) -> {
								String path1 = e1.getKey().getPatternValues().stream().findFirst().orElse("");
								String path2 = e2.getKey().getPatternValues().stream().findFirst().orElse("");
								return path1.compareTo(path2);
							})
							.forEach(entry -> {
								String patterns = String.join(", ", entry.getKey().getPatternValues());
								System.out.println(patterns + " -> " + entry.getValue().getShortLogMessage());
							});
				});
	}
}