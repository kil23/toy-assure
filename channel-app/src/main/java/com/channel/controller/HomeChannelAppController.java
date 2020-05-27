package com.channel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/channel")
public class HomeChannelAppController extends AbstractUiController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String home() {
        return "redirect:/home";
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public ModelAndView channelHome() {
        return mav("channelHome");
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView createChannel() {
        return mav("createChannel");
    }

    @RequestMapping(value = "/order", method = RequestMethod.GET)
    public ModelAndView channelOrder() {
        return mav("channelOrder");
    }

    @RequestMapping(value = "/order-list", method = RequestMethod.GET)
    public ModelAndView channelOrderList() {
        return mav("viewChannelOrders");
    }

    @RequestMapping(value = "/{channelId}/order/{orderId}/item-list", method = RequestMethod.GET)
    public ModelAndView channelOrderItemList() {
        return mav("viewChannelOrderItems");
    }

    @RequestMapping(value = "/{channelId}/listing/{clientId}", method = RequestMethod.GET)
    public ModelAndView channelListings() {
        return mav("viewChannelListings");
    }

    @RequestMapping(value = "/{channelId}/listing", method = RequestMethod.GET)
    public ModelAndView viewChannelListing(){
        return mav("channelListing");
    }
}
