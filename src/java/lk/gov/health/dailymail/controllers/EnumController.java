/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.dailymail.controllers;

import javax.inject.Named;
import javax.enterprise.context.ApplicationScoped;
import lk.gov.health.dailymail.entity.ItemCategory;

/**
 *
 * @author pdhssp
 */
@Named(value = "enumController")
@ApplicationScoped
public class EnumController {

    /**
     * Creates a new instance of EnumController
     */
    public EnumController() {
    }

    public ItemCategory[] getItemCategories() {
        return ItemCategory.values();
    }

}
