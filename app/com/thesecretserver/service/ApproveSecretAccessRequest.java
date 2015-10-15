
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für anonymous complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="approvalId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="hours" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="userOverride" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
    "approvalId",
    "hours",
    "userOverride"
})
@XmlRootElement(name = "ApproveSecretAccessRequest")
public class ApproveSecretAccessRequest {

    protected String approvalId;
    protected String hours;
    protected boolean userOverride;

    /**
     * Ruft den Wert der approvalId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApprovalId() {
        return approvalId;
    }

    /**
     * Legt den Wert der approvalId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApprovalId(String value) {
        this.approvalId = value;
    }

    /**
     * Ruft den Wert der hours-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHours() {
        return hours;
    }

    /**
     * Legt den Wert der hours-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHours(String value) {
        this.hours = value;
    }

    /**
     * Ruft den Wert der userOverride-Eigenschaft ab.
     * 
     */
    public boolean isUserOverride() {
        return userOverride;
    }

    /**
     * Legt den Wert der userOverride-Eigenschaft fest.
     * 
     */
    public void setUserOverride(boolean value) {
        this.userOverride = value;
    }

}
