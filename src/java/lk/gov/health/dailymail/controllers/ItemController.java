package lk.gov.health.dailymail.controllers;

import lk.gov.health.dailymail.entity.Item;
import lk.gov.health.dailymail.controllers.util.JsfUtil;
import lk.gov.health.dailymail.controllers.util.JsfUtil.PersistAction;
import lk.gov.health.dailymail.facades.ItemFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;
import lk.gov.health.dailymail.entity.ItemCategory;

@Named(value = "itemController")
@SessionScoped
public class ItemController implements Serializable {

    @EJB
    private lk.gov.health.dailymail.facades.ItemFacade ejbFacade;
    private List<Item> items = null;
    private List<Item> processes;
    private List<Item> positions;

    private Item selected;

    public ItemController() {
    }

    public Item getSelected() {
        return selected;
    }

    public void setSelected(Item selected) {
        this.selected = selected;
    }

    public List<Item> getItems(ItemCategory category) {
        String j = "select i from Item i where i.itemCategory=:ic order by i.name";
        Map m = new HashMap();
        m.put("ic", category);
        return getFacade().findBySQL(j, m);
    }

    public List<Item> getProcessSteps(Item process) {
        return getItems(ItemCategory.SubProcess, process, true);
    }
    
    public List<Item> getProcessFailures(Item process) {
        return getItems(ItemCategory.WorkingInstruction, process, false);
    }

    public List<Item> getItems(ItemCategory category, Item parent, boolean byOrderNo) {
        String j = "select i from Item i "
                + " where i.itemCategory=:ic "
                + " and i.parentItem=:p ";
        if (byOrderNo) {
            j += " order by i.orderNo";
        } else {
            j += " order by i.name";
        }
        Map m = new HashMap();
        m.put("ic", category);
        m.put("p", parent);
        return getFacade().findBySQL(j, m);
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ItemFacade getFacade() {
        return ejbFacade;
    }

    public Item prepareCreate() {
        selected = new Item();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ItemCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;
            positions = null;
            processes = null;// Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ItemUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ItemDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;
            positions = null;
            processes = null;
// Invalidate list of items to trigger re-query.
        }
    }

    public List<Item> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public List<Item> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Item> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public List<Item> getProcesses() {
        if (processes == null) {
            processes = getItems(ItemCategory.KeyProcess);
        }
        return processes;
    }

    public void setProcesses(List<Item> processes) {
        this.processes = processes;
    }

    public List<Item> getPositions() {
        if (positions == null) {
            positions = getItems(ItemCategory.Position);
        }
        return positions;
    }

    public void setPositions(List<Item> positions) {
        this.positions = positions;
    }

    @FacesConverter(forClass = Item.class)
    public static class ItemControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ItemController controller = (ItemController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "itemController");
            return controller.getFacade().find(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Item) {
                Item o = (Item) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Item.class.getName()});
                return null;
            }
        }

    }

}
