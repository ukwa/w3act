
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetSshCommandMenusResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetSshCommandMenusResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SshCommandMenus" type="{urn:thesecretserver.com}ArrayOfSshCommandMenu" minOccurs="0"/>
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
@XmlType(name = "GetSshCommandMenusResult", propOrder = {
    "sshCommandMenus",
    "errors"
})
public class GetSshCommandMenusResult {

    @XmlElement(name = "SshCommandMenus")
    protected ArrayOfSshCommandMenu sshCommandMenus;
    @XmlElement(name = "Errors")
    protected ArrayOfString errors;

    /**
     * Gets the value of the sshCommandMenus property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfSshCommandMenu }
     *     
     */
    public ArrayOfSshCommandMenu getSshCommandMenus() {
        return sshCommandMenus;
    }

    /**
     * Sets the value of the sshCommandMenus property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfSshCommandMenu }
     *     
     */
    public void setSshCommandMenus(ArrayOfSshCommandMenu value) {
        this.sshCommandMenus = value;
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
