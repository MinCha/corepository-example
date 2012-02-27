package com.github.writeback.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
	private VisitationWriteBackRepository repository;
	private VisiationCountOriginalRepository originalRepository;
	
	@Autowired
	public HomeController(VisitationWriteBackRepository repository, VisiationCountOriginalRepository originalRepository) {
		this.repository = repository;
		this.originalRepository = originalRepository;
	}
	
	@RequestMapping(value = "/", method = {RequestMethod.GET})
	public ModelAndView home(String name, String age) {
		ModelAndView result = new ModelAndView();

		Visitation visitation = repository.find(name, age);
		visitation.visit();
		repository.update(visitation);
		
		result.addObject("visitation", visitation);
		result.addObject("databaseVisitation", originalRepository.read(name + "_" + age));
		result.setViewName("home");
		return result;
	}
}
