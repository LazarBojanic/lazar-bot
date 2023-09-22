package com.lazar.lazarwordleclonebackendspring.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import com.lazar.lazarwordleclonebackendspring.service.SolutionService;

@Profile({"default"})
@Component
public class Runner implements CommandLineRunner{
	@Autowired
	private SolutionService solutionService;

	@Override
	public void run(String... args) throws Exception {

	}
}