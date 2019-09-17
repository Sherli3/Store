package store.configuration.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import store.configuration.model.GameObject;
import store.configuration.model.User;
import store.configuration.service.GameObjectService;
import store.configuration.service.impl.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private GameObjectService gameObjectService;
	private Date date = new Date();

	@RequestMapping(value = "/cabinet", method = RequestMethod.GET)
	public String cabinetList(Model model) {
		List<GameObject> listObject = gameObjectService.findAllVerifiedObjects();
		model.addAttribute("listObject", listObject);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findByEmail(auth.getName());
		model.addAttribute("userName",
				"Welcome " + user.getFirstName() + " " + user.getLastName() + " (" + user.getEmail() + ")");

		return "cabinet";
	}

	/*@RequestMapping(value = { "/login" }, method = RequestMethod.GET)
	public ModelAndView login() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("login");
		return modelAndView;
	}*/
	 
	 @RequestMapping(value = "/logout", method = { RequestMethod.GET, RequestMethod.POST })
	    public String logout(ModelMap model) {
	        return "login";
	    }


	@RequestMapping(value = "/registration", method = RequestMethod.GET)
	public ModelAndView registration() {
		ModelAndView modelAndView = new ModelAndView("");
		User user = new User();
		modelAndView.addObject("user", user);
		modelAndView.setViewName("registration");
		// modelAndView.setViewName("cabinet");
		return modelAndView;
	}

	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView("");
		User userExists = userService.findByEmail(user.getEmail());
		if (userExists != null) {
			bindingResult.rejectValue("email", "error.user",
					"There is already a user registered with the email provided");
		}
		if (bindingResult.hasErrors()) {
			modelAndView.setViewName("registration");
		} else {
			long time = date.getTime();
			Timestamp ts = new Timestamp(time);
			user.setCreatedAt(ts);
			userService.saveUser(user);
			modelAndView.addObject("successMessage", "User has been registered successfully");
			modelAndView.addObject("user", new User());
			modelAndView.setViewName("registration");

		}
		return modelAndView;
	}

}
