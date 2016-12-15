
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="token" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sshCommandMenu" type="{urn:thesecretserver.com}SshCommandMenu" minOccurs="0"/>
 *         &lt;element name="commandsText" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="deleteCommands" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "token",
    "sshCommandMenu",
    "commandsText",
    "deleteCommands"
})
@XmlRootElement(name = "SaveSSHCommandMenu")
public class SaveSSHCommandMenu {

    protected String token;
    protected SshCommandMenu sshCommandMenu;
    protected String commandsText;
    protected boolean deleteCommands;

    /**
     * Gets the value of the token property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the value of the token property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setToken(String value) {
        this.token = value;
    }

    /**
     * Gets the value of the sshCommandMenu property.
     * 
     * @return
     *     possible object is
     *     {@link SshCommandMenu }
     *     
     */
    public SshCommandMenu getSshCommandMenu() {
        return sshCommandMenu;
    }

    /**
     * Sets the value of the sshCommandMenu property.
     * 
     * @param value
     *     allowed object is
     *     {@link SshCommandMenu }
     *     
     */
    public void setSshCommandMenu(SshCommandMenu value) {
        this.sshCommandMenu = value;
    }

    /**
     * Gets the value of the commandsText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCommandsText() {
        return commandsText;
    }

    /**
     * Sets the value of the commandsText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCommandsText(String value) {
        this.commandsText = value;
    }

    /**
     * Gets the value of the deleteCommands property.
     * 
     */
    public boolean isDeleteCommands() {
        return deleteCommands;
    }

    /**
     * Sets the value of the deleteCommands property.
     * 
     */
    public void setDeleteCommands(boolean value) {
        this.deleteCommands = value;
    }

}
