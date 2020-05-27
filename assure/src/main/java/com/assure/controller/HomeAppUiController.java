package com.assure.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(value = "/ui")
public class HomeAppUiController extends AbstractUiController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String home() {
        return "redirect:/home";
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public ModelAndView uiHome() {
        return mav("uihome");
    }

    @RequestMapping(value = "/client", method = RequestMethod.GET)
    public ModelAndView clientHome(){
        return mav("client");
    }

    @RequestMapping(value = "/order", method = RequestMethod.GET)
    public ModelAndView orderHome(){
        return mav("order");
    }

    @RequestMapping(value = "/product", method = RequestMethod.GET)
    public ModelAndView productHome(){
        return mav("product");
    }

    @RequestMapping(value = "/inventory", method = RequestMethod.GET)
    public ModelAndView inventoryHome(){
        return mav("inventory");
    }

    @RequestMapping(value = "/order/create", method = RequestMethod.GET)
    public ModelAndView createOrder(){
        return mav("createOrder");
    }

    @RequestMapping(value = "/{orderId}/item", method = RequestMethod.GET)
    public ModelAndView viewOrderItems(){
        return mav("viewOrderItems");
    }
}
