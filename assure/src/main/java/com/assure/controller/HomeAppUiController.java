package com.assure.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeAppUiController extends AbstractUiController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView home() {
        return mav("index");
    }

    @RequestMapping(value = "/ui/home", method = RequestMethod.GET)
    public ModelAndView uiHome() {
        return mav("uihome");
    }

    @RequestMapping(value = "/ui/client", method = RequestMethod.GET)
    public ModelAndView clientHome(){
        return mav("client");
    }

    @RequestMapping(value = "/ui/bin", method = RequestMethod.GET)
    public ModelAndView binsHome(){
        return mav("bin");
    }

    @RequestMapping(value = "/ui/order", method = RequestMethod.GET)
    public ModelAndView orderHome(){
        return mav("order");
    }

    @RequestMapping(value = "/api/all-products", method = RequestMethod.GET)
    public ModelAndView viewAllProducts(){
        return mav("viewAllProducts");
    }

    @RequestMapping(value = "/api/{clientId}/product", method = RequestMethod.GET)
    public ModelAndView viewClientProducts(){
        return mav("products");
    }

    @RequestMapping(value = "/api/all-inventory", method = RequestMethod.GET)
    public ModelAndView viewAllInventory(){
        return mav("viewAllInventory");
    }

    @RequestMapping(value = "/api/{binId}/bin-inventory", method = RequestMethod.GET)
    public ModelAndView viewBinInventory(){
        return mav("binInventory");
    }

    @RequestMapping(value = "/ui/order/create", method = RequestMethod.GET)
    public ModelAndView createOrder(){
        return mav("createOrder");
    }

    @RequestMapping(value = "/api/{orderId}/item", method = RequestMethod.GET)
    public ModelAndView viewOrderItems(){
        return mav("viewOrderItems");
    }
}
