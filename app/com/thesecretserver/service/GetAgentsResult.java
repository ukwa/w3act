
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für GetAgentsResult complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
     * Ruft den Wert der remoteAgents-Eigenschaft ab.
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
     * Legt den Wert der remoteAgents-Eigenschaft fest.
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
     * Ruft den Wert der errors-Eigenschaft ab.
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
     * Legt den Wert der errors-Eigenschaft fest.
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
     * Ruft den Wert der success-Eigenschaft ab.
     * 
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Legt den Wert der success-Eigenschaft fest.
     * 
     */
    public void setSuccess(boolean value) {
        this.success = value;
    }

}
