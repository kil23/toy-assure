package com.channel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeAppController extends AbstractUiController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView home() {
        return mav("index");
    }

    @RequestMapping(value = "/channel/home", method = RequestMethod.GET)
    public ModelAndView channelHome() {
        return mav("channelHome");
    }

    @RequestMapping(value = "/channel/create", method = RequestMethod.GET)
    public ModelAndView createChannel() {
        return mav("createChannel");
    }

    @RequestMapping(value = "/channel/order", method = RequestMethod.GET)
    public ModelAndView channelOrder() {
        return mav("channelOrder");
    }

    @RequestMapping(value = "/channel/order-list", method = RequestMethod.GET)
    public ModelAndView channelOrderList() {
        return mav("viewChannelOrders");
    }

    @RequestMapping(value = "/api/{channelId}/channel/{clientId}", method = RequestMethod.GET)
    public ModelAndView channelListings() {
        return mav("viewChannelListings");
    }

    @RequestMapping(value = "/api/{channelId}/listing", method = RequestMethod.GET)
    public ModelAndView viewChannelListing(){
        return mav("channelListing");
    }
}
