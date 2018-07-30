package lk.gov.health.dailymail.controllers;

import lk.gov.health.dailymail.entity.ClientProcess;
import lk.gov.health.dailymail.controllers.util.JsfUtil;
import lk.gov.health.dailymail.controllers.util.JsfUtil.PersistAction;
import lk.gov.health.dailymail.facades.ClientProcessFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.TemporalType;
import lk.gov.health.dailymail.entity.ClientProcessStep;
import lk.gov.health.dailymail.entity.Department;
import lk.gov.health.dailymail.entity.Institute;
import lk.gov.health.dailymail.entity.Item;
import lk.gov.health.dailymail.entity.Subject;
import lk.gov.health.dailymail.facades.ClientProcessStepFacade;
import lk.gov.health.dailymail.facades.DepartmentFacade;
import lk.gov.health.dailymail.facades.SubjectFacade;

@Named
@SessionScoped
public class ClientProcessController implements Serializable {

    @EJB
    private lk.gov.health.dailymail.facades.ClientProcessFacade ejbFacade;
    @EJB
    SubjectFacade subjectFaceFacade;
    @EJB
    DepartmentFacade departmentFacade;
    @EJB
    private ClientProcessStepFacade clientProcessStepFacade;

    @Inject
    WebUserController webUserController;
    @Inject
    private SmsController smsController;

    private List<ClientProcess> items = null;
    private List<ClientProcess> selectedItems = null;
    private ClientProcess selected;

    Date fromDate;
    Date toDate;
    Institute institute;
    Department department;
    Subject subject;

    boolean fixeReceivedDate;
    Date fixedReceivedDate;

    List<Subject> deptSubjects;

    List<Department> myDepartnments = null;
    String searchText;
    private String customSmsMessage;
    private Item processStep;
    private Item processFailure;

    public boolean procesStepIsComplete(Item processStep) {
        String j = "select cps "
                + " from ClientProcessStep cps "
                + " where cps.clientProcess = :cp "
                + " and cps.processStep = :ps";
        Map m = new HashMap();
        m.put("cp", selected);
        m.put("ps", processStep);
        List<ClientProcessStep> cpss = getClientProcessStepFacade().findBySQL(j, m);
        if (cpss == null || cpss.isEmpty()) {
            return false;
        }
        return true;
    }

    public void sendCustomSms() {
        smsController.sendSms(selected.getMobileNo(), customSmsMessage , selected);
        JsfUtil.addSuccessMessage("Saved and SMS Sent");
    }

    public void processStepAction() {
        if (processStep == null) {
            JsfUtil.addErrorMessage("Please select");
            return;
        }
        ClientProcessStep s = new ClientProcessStep();
        s.setClientProcess(selected);
        s.setCompleted(true);
        s.setProcess(processStep.getParentItem());
        s.setProcessStep(processStep);
        s.setStarted(true);
        s.setStartDate(new Date());
        s.setEndDate(new Date());
        getClientProcessStepFacade().create(s);
        smsController.sendSms(selected.getMobileNo(), processStep.getComments(), s);
        JsfUtil.addSuccessMessage("Saved and SMS Sent");
    }

    public void processFailureAction() {
        if (processFailure == null) {
            JsfUtil.addErrorMessage("Please select");
            return;
        }
        ClientProcessStep s = new ClientProcessStep();
        s.setClientProcess(selected);
        s.setCompleted(true);
        s.setProcess(processFailure.getParentItem());
        s.setProcessStep(processFailure);
        s.setStarted(true);
        s.setStartDate(new Date());
        s.setEndDate(new Date());
        getClientProcessStepFacade().create(s);
        smsController.sendSms(selected.getMobileNo(), processFailure.getComments(), s);
        JsfUtil.addSuccessMessage("Saved and SMS Sent");
    }

    public void searchClientProcesses() {
        if (searchText.trim().equals("")) {
            selectedItems = new ArrayList<>();
            return;
        }
        String j = "select c from ClientProcess c"
                + " where lower(c.clientName) like :n or c.mobileNo like :m "
                + " order by c.clientName";
        Map m = new HashMap();
        m.put("n", "%" + searchText.trim().toLowerCase() + "%");
        m.put("m", searchText.trim() + "%");

        selectedItems = getFacade().findBySQL(j, m);
    }

    public String toAddNewClientProcess() {
        selected = new ClientProcess();
        return "/client_process/new_client_process";
    }

    public String toSearchClientProcess() {
        selected = null;
        selectedItems = new ArrayList<>();
        return "/client_process/search_client_processes";
    }

    public String saveNewClientProcess() {
        if (selected == null) {
            return "";
        }
        getFacade().create(selected);
        JsfUtil.addSuccessMessage("New Process Created");
        return "/client_process/client_process";
    }

    public List<Department> getMyDepartnments() {
        String j = "select d from Department d "
                + " where d.institute=:ins";
        Map m = new HashMap();
        m.put("ins", institute);
        myDepartnments = getDepartmentFacade().findBySQL(j, m);
        return myDepartnments;
    }

    public List<Subject> getMySubjects() {
        if (selected == null || selected.getToDepartment() == null) {
            return new ArrayList<Subject>();
        }
        String j = "select s from Subject s "
                + " where s.department=:dep";
        Map m = new HashMap();
        m.put("dep", selected.getToDepartment());
        deptSubjects = getSubjectFaceFacade().findBySQL(j, m);
        return deptSubjects;
    }

    public String toAssignMailsToSubjects() {
        selectedItems = new ArrayList<ClientProcess>();
        return "/mail/assign_subjects";
    }

    public void assignMailsToSubject() {
        for (ClientProcess m : selectedItems) {
            m.setSubject(subject);
            getFacade().edit(m);
        }
    }

    public List<Subject> getDeptSubjects() {
        String j = "select s from Subject s "
                + " where s.department=:dep";
        Map m = new HashMap();
        m.put("dep", department);
        deptSubjects = getSubjectFaceFacade().findBySQL(j, m);
        return deptSubjects;
    }

    public List<ClientProcess> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(List<ClientProcess> selectedItems) {
        this.selectedItems = selectedItems;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public void setDeptSubjects(List<Subject> deptSubjects) {
        this.deptSubjects = deptSubjects;
    }

    public SubjectFacade getSubjectFaceFacade() {
        return subjectFaceFacade;
    }

    public String listInsMails() {
        String j;
        j = "select m from Mail m "
                + " where m.receivedDateTime between :fd and :td"
                + " and m.toInstitute=:ins"
                + " order by m.id";
        Map m = new HashMap();
        m.put("ins", institute);
        TimeZone t = TimeZone.getTimeZone("Asia/Colombo");
        Calendar fc = Calendar.getInstance(t);
        fc.setTime(getFromDate());
        fc.add(Calendar.MINUTE, -330);
        Calendar tc = Calendar.getInstance(t);
        tc.setTime(getToDate());
        tc.add(Calendar.MINUTE, -330);
        m.put("fd", fc.getTime());
        m.put("td", tc.getTime());

        items = getFacade().findBySQL(j, m);
        return "/mail/insMails";
    }

    public String listDeptMails() {
        String j;
        j = "select m from Mail m "
                + " where m.receivedDateTime between :fd and :td"
                + " and m.toDepartment=:ins"
                + " order by m.id";
        Map m = new HashMap();
        TimeZone t = TimeZone.getTimeZone("Asia/Colombo");
        Calendar fc = Calendar.getInstance(t);
        fc.setTime(getFromDate());
        fc.add(Calendar.MINUTE, -330);
        Calendar tc = Calendar.getInstance(t);
        tc.setTime(getToDate());
        tc.add(Calendar.MINUTE, -330);
        m.put("fd", fc.getTime());
        m.put("td", tc.getTime());
        m.put("ins", department);
        items = getFacade().findBySQL(j, m);
        return "/mail/depMails";

    }

    public String listDeptMailsDaily() {
        String j;
        j = "select m from Mail m "
                + " where m.receivedDateTime = :fd "
                + " and m.toDepartment=:dep"
                + " order by m.id";
        Map m = new HashMap();
        TimeZone t = TimeZone.getTimeZone("Asia/Colombo");
        Calendar fc = Calendar.getInstance(t);
        fc.setTime(getFromDate());
        fc.add(Calendar.MINUTE, +330);

        m.put("fd", fc.getTime());
        m.put("dep", department);
        items = getFacade().findBySQL(j, m, TemporalType.DATE);
        return "/mail/depMails";
    }

    public String listDeptMailsDailyByEnteredDate() {
        String j;
        j = "select m from Mail m "
                + " where m.addedDate = :fd "
                + " and m.toDepartment=:dep"
                + " order by m.id";
        Map m = new HashMap();
        TimeZone t = TimeZone.getTimeZone("Asia/Colombo");
        Calendar fc = Calendar.getInstance(t);
        fc.setTime(getFromDate());
        fc.add(Calendar.MINUTE, +330);

        m.put("fd", fc.getTime());
        m.put("dep", department);
        items = getFacade().findBySQL(j, m, TemporalType.DATE);
        return "/mail/depMails_created_date";
    }

    public String listDeptMailsDailyByReceivedDate() {
        String j;
        j = "select m from Mail m "
                + " where m.receivedDate = :fd "
                + " and m.toDepartment=:dep"
                + " order by m.id";
        Map m = new HashMap();
        TimeZone t = TimeZone.getTimeZone("Asia/Colombo");
        Calendar fc = Calendar.getInstance(t);
        fc.setTime(getFromDate());
        fc.add(Calendar.MINUTE, +330);

        m.put("fd", fc.getTime());
        m.put("dep", department);
        items = getFacade().findBySQL(j, m, TemporalType.DATE);
        return "/mail/depMails_received_date";
    }

    public String listDeptMailsDailyByLetterDate() {
        String j;
        j = "select m from Mail m "
                + " where m.letterDate = :fd "
                + " and m.toDepartment=:dep"
                + " order by m.id";
        Map m = new HashMap();
        TimeZone t = TimeZone.getTimeZone("Asia/Colombo");
        Calendar fc = Calendar.getInstance(t);
        fc.setTime(getFromDate());
        fc.add(Calendar.MINUTE, +330);

        m.put("fd", fc.getTime());
        m.put("dep", department);
        items = getFacade().findBySQL(j, m, TemporalType.DATE);
        return "/mail/depMails_letter_date";
    }

    public String listUnassignedDeptMails() {
        String j;
        j = "select m from Mail m "
                + " where m.receivedDateTime between :fd and :td"
                + " and m.toDepartment=:ins "
                + " and m.subject is null "
                + " order by m.id";
        Map m = new HashMap();
        TimeZone t = TimeZone.getTimeZone("Asia/Colombo");
        Calendar fc = Calendar.getInstance(t);
        fc.setTime(getFromDate());
        fc.add(Calendar.MINUTE, -330);
        Calendar tc = Calendar.getInstance(t);
        tc.setTime(getToDate());
        tc.add(Calendar.MINUTE, -330);
        m.put("fd", fc.getTime());
        m.put("td", tc.getTime());
        m.put("ins", department);
        items = getFacade().findBySQL(j, m);
        return "/mail/assign_subjects";

    }

    public String listUnassignedAndAssignedDeptMails() {
        String j;
        j = "select m from Mail m "
                + " where m.receivedDateTime between :fd and :td"
                + " and m.toDepartment=:ins "
                + " order by m.id";
        Map m = new HashMap();
        TimeZone t = TimeZone.getTimeZone("Asia/Colombo");
        Calendar fc = Calendar.getInstance(t);
        fc.setTime(getFromDate());
        fc.add(Calendar.MINUTE, -330);
        Calendar tc = Calendar.getInstance(t);
        tc.setTime(getToDate());
        tc.add(Calendar.MINUTE, -330);
        m.put("fd", fc.getTime());
        m.put("td", tc.getTime());
        m.put("ins", department);
        items = getFacade().findBySQL(j, m);
        return "/mail/assign_subjects";

    }

    public ClientProcessController() {
    }

    public WebUserController getWebUserController() {
        return webUserController;
    }

    public Date getFromDate() {
        if (fromDate == null) {
            fromDate = new Date();
        }
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        if (toDate == null) {
            toDate = new Date();
        }
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Institute getInstitute() {
        return institute;
    }

    public void setInstitute(Institute institute) {
        this.institute = institute;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String toListDepLettersByCreatedDate() {
        items = new ArrayList<ClientProcess>();
        return "/mail/depMails_created_date";
    }

    public String toListDepLettersByLetterDate() {
        items = new ArrayList<ClientProcess>();
        return "/mail/depMails_letter_date";
    }

    public String toListDepLettersByReceivedDate() {
        items = new ArrayList<ClientProcess>();
        return "/mail/depMails_received_date";
    }

    public String toAddNewMail() {
        selected = new ClientProcess();
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Asia/Colombo"));
//        c.add(Calendar.HOUR, 12);
        selected.setLetterDate(c.getTime());
        selected.setLetterDateTime(c.getTime());
        System.out.println("selected.getAddedTime() = " + selected.getLetterDate());
        selected.setReceivedDate(c.getTime());
        selected.setReceivedDateTime(c.getTime());
        System.out.println("selected.getAddedTime() = " + selected.getReceivedDate());
        if (getWebUserController().getLoggedUser() != null) {
            System.out.println("webUserController.loggedUser = " + getWebUserController().getLoggedUser());
            selected.setToInstitute(getWebUserController().getLoggedUser().getInstitute());
            institute = selected.getToInstitute();
        } else {
            selected.setToInstitute(institute);
            System.out.println("Logged User is null");
        }
        return "/mail/addMail";
    }

    public String saveNewMail() {
        if (selected == null) {
            JsfUtil.addErrorMessage("Nothing to save");
            return "";
        }
        if (selected.getToInstitute() == null) {
            JsfUtil.addErrorMessage("Select Institute");
            return "";
        }
        TimeZone t = TimeZone.getTimeZone("Asia/Colombo");
        Calendar c = Calendar.getInstance(t);
        selected.setAddedDate(c.getTime());
        System.out.println("selected.getAddedDate() = " + selected.getAddedDate());
        selected.setAddedTime(c.getTime());
        System.out.println("selected.getAddedTime() = " + selected.getAddedTime());
        selected.setAddedUser(webUserController.loggedUser);
        getFacade().create(selected);
        institute = selected.getToInstitute();
        JsfUtil.addSuccessMessage("Letter Saved");
        return toAddNewMail();
    }

    public ClientProcess getSelected() {
        return selected;
    }

    public void setSelected(ClientProcess selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ClientProcessFacade getFacade() {
        return ejbFacade;
    }

    public ClientProcess prepareCreate() {
        selected = new ClientProcess();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("MailCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("MailUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("MailDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null;
            items = null;
        }
    }

    public List<ClientProcess> getItems() {
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

    public List<ClientProcess> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<ClientProcess> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public DepartmentFacade getDepartmentFacade() {
        return departmentFacade;
    }

    public void addTime() {
        List<ClientProcess> mails = getFacade().findAll();
        for (ClientProcess m : mails) {
            m.setReceivedDateTime(m.getReceivedDate());
            m.setLetterDateTime(m.getLetterDate());
            getFacade().edit(m);
        }
    }

    public Item getProcessStep() {
        return processStep;
    }

    public void setProcessStep(Item processStep) {
        this.processStep = processStep;
    }

    public Item getProcessFailure() {
        return processFailure;
    }

    public void setProcessFailure(Item processFailure) {
        this.processFailure = processFailure;
    }

    private ClientProcessStepFacade getClientProcessStepFacade() {
        return clientProcessStepFacade;
    }

    public SmsController getSmsController() {
        return smsController;
    }

    public String getCustomSmsMessage() {
        return customSmsMessage;
    }

    public void setCustomSmsMessage(String customSmsMessage) {
        this.customSmsMessage = customSmsMessage;
    }

    @FacesConverter(forClass = ClientProcess.class)
    public static class MailControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ClientProcessController controller = (ClientProcessController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "mailController");
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
            if (object instanceof ClientProcess) {
                ClientProcess o = (ClientProcess) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), ClientProcess.class.getName()});
                return null;
            }
        }

    }

    public ClientProcessFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(ClientProcessFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public boolean isFixeReceivedDate() {
        return fixeReceivedDate;
    }

    public void setFixeReceivedDate(boolean fixeReceivedDate) {
        this.fixeReceivedDate = fixeReceivedDate;
    }

    public Date getFixedReceivedDate() {
        return fixedReceivedDate;
    }

    public void setFixedReceivedDate(Date fixedReceivedDate) {
        this.fixedReceivedDate = fixedReceivedDate;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

}
