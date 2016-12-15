
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetSshCommandMenuResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetSshCommandMenuResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SshCommandMenu" type="{urn:thesecretserver.com}SshCommandMenu" minOccurs="0"/>
 *         &lt;element name="SshCommands" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Errors" type="{urn:thesecretserver.com}ArrayOfString" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetSshCommandMenuResult", propOrder = {
    "sshCommandMenu",
    "sshCommands",
    "errors"
})
public class GetSshCommandMenuResult {

    @XmlElement(name = "SshCommandMenu")
    protected SshCommandMenu sshCommandMenu;
    @XmlElement(name = "SshCommands")
    protected String sshCommands;
    @XmlElement(name = "Errors")
    protected ArrayOfString errors;

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
     * Gets the value of the sshCommands property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSshCommands() {
        return sshCommands;
    }

    /**
     * Sets the value of the sshCommands property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSshCommands(String value) {
        this.sshCommands = value;
    }

    /**
     * Gets the value of the errors property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfString }
     *     
     */
    public ArrayOfString getErrors() {
        return errors;
    }

    /**
     * Sets the value of the errors property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfString }
     *     
     */
    public void setErrors(ArrayOfString value) {
        this.errors = value;
    }

}
