/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import Controllers.WizardController;

/**
 *
 * @author OBAI
 */
public abstract class WizardView {

    public  WizardController wizard;

    public void setWizard(WizardController wizard) {
        this.wizard = wizard;
    }
}
