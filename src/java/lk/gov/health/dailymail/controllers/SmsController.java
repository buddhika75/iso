/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.dailymail.controllers;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.inject.Inject;
import lk.gov.health.dailymail.entity.ClientProcess;
import lk.gov.health.dailymail.entity.ClientProcessStep;
import lk.gov.health.dailymail.entity.Sms;
import lk.gov.health.dailymail.entity.WebUser;
import lk.gov.health.dailymail.facades.SmsFacade;
import lk.mobitel.esms.session.*;
import lk.mobitel.esms.message.*;
import lk.mobitel.esms.ws.*;

/**
 *
 * @author Buddhika
 */
@Named(value = "smsController")
@SessionScoped
public class SmsController implements Serializable {

    @EJB
    SmsFacade smsFacade;
    @Inject
    WebUserController webUserController;

    String smsReceipients;
    String smsMessage;
   
    
    private List<Sms> smses;

    /**
     * Creates a new instance of SmsController
     */
    public SmsController() {
    }

    
    
    public void sendSms(String number, String message) {
        sendSms(number, message, null, null);
    }

    public void sendSms(String number, String message, ClientProcess clientProcess) {
        sendSms(number, message, clientProcess, null);
    }

    public void sendSms(String number, String message, ClientProcessStep clientProcessStep) {
        sendSms(number, message, clientProcessStep.getClientProcess(), clientProcessStep);
    }

    public void sendSms(String number, String message, ClientProcess clientProcess, ClientProcessStep clientProcessStep) {
        //create user object
        User user = new User();
        user.setUsername("esmsusr_sr8");
        user.setPassword("t2l51j");

        //initiate session
        SessionManager sm = SessionManager.getInstance();
        System.out.println("Logging in.....");
        if (!sm.isSession()) {
            sm.login(user);
        }
        System.out.println("Logged in!");

        //create alias obj
        Alias alias = new Alias();
        alias.setAlias("PD Southern");

        //create SmsMessage object 
        SmsMessage msg = new SmsMessage();
        msg.setMessage(message);
        System.out.println("Message length: " + msg.getMessage().length());
        msg.setSender(alias);
        msg.getRecipients().add(number);

        //send sms
        try {
            SMSManager smsMgr = new SMSManager();
            System.out.println("Sending......");
            int ret = smsMgr.sendMessage(msg);
            System.out.println("Sent! " + ret);
            Sms sms = new Sms();
            sms.setClientProcessStep(clientProcessStep);
            sms.setClientProess(clientProcess);
            sms.setMessage(message);
            sms.setSentDate(new Date());
            sms.setSentDateTime(new Date());
            sms.setSentTo(number);
            sms.setSentBy(webUserController.getLoggedUser());
            smsFacade.create(sms);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        /* get received messages for long number     
		SMSManager smsMgr = new SMSManager();
		LongNumber longNUmber=new LongNumber();
		longNUmber.setLongNumber("");
		try {
			List <SmsMessage> receivedMessages=smsMgr.getMessagesFromLongNumber(longNUmber);
			for(int i=0;i<receivedMessages.size();i++){
				System.out.println(receivedMessages.get(i).getSender().getAlias()+","+receivedMessages.get(i).getMessage());
			}
		} catch (NullSessionException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
         */
    }

    
    public List<Sms> getSmses(ClientProcess clientProcess) {
        String j = "select s from Sms s "
                + " where s.clientProess = :cp "
                + " order by s.id";
        Map m = new HashMap();
        m.put("cp", clientProcess);
        smses = smsFacade.findBySQL(j, m);
        return smses;
    }
    
    public List<Sms> getSmses() {
        return smses;
    }

    public void setSmses(List<Sms> smses) {
        this.smses = smses;
    }

}
