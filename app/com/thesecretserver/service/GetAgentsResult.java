
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetAgentsResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetAgentsResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RemoteAgents" type="{urn:thesecretserver.com}ArrayOfRemoteAgent" minOccurs="0"/>
 *         &lt;element name="Errors" type="{urn:thesecretserver.com}ArrayOfString" minOccurs="0"/>
 *         &lt;element name="Success" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetAgentsResult", propOrder = {
    "remoteAgents",
    "errors",
    "success"
})
public class GetAgentsResult {

    @XmlElement(name = "RemoteAgents")
    protected ArrayOfRemoteAgent remoteAgents;
    @XmlElement(name = "Errors")
    protected ArrayOfString errors;
    @XmlElement(name = "Success")
    protected boolean success;

    /**
     * Gets the value of the remoteAgents property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfRemoteAgent }
     *     
     */
    public ArrayOfRemoteAgent getRemoteAgents() {
        return remoteAgents;
    }

    /**
     * Sets the value of the remoteAgents property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfRemoteAgent }
     *     
     */
    public void setRemoteAgents(ArrayOfRemoteAgent value) {
        this.remoteAgents = value;
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

    /**
     * Gets the value of the success property.
     * 
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Sets the value of the success property.
     * 
     */
    public void setSuccess(boolean value) {
        this.success = value;
    }

}
