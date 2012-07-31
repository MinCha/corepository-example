package com.github.corepo.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
	private VisitationRepository repository;
	private VisiationCountOriginalRepository originalRepository;

	@Autowired
	public HomeController(VisitationRepository repository,
			VisiationCountOriginalRepository originalRepository) {
		this.repository = repository;
		this.originalRepository = originalRepository;
	}

	@RequestMapping(value = "/", method = { RequestMethod.GET })
	public ModelAndView home(@RequestParam(defaultValue = "min") String name) {
		Visitation visitation = repository.find(name);
		visitation.visit();
		repository.update(visitation);

		ModelAndView result = new ModelAndView();
		result.addObject("name", name);
		result.addObject("corepo", visitation);
		result.addObject("db", originalRepository.read(name).getValue());
		result.setViewName("home");
		return result;
	}
}
