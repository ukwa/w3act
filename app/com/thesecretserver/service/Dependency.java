
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für Dependency complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="Dependency">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SecretId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="SecretDependencyTypeId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="MachineName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ServiceName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PrivilegedAccountSecretId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Active" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="RestartOnPasswordChange" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="WaitBeforeSeconds" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="AdditionalInfo" type="{urn:thesecretserver.com}AdditionalDependencyInfoJson" minOccurs="0"/>
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ScriptId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="SecretDependencyId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Dependency", propOrder = {
    "secretId",
    "secretDependencyTypeId",
    "machineName",
    "serviceName",
    "privilegedAccountSecretId",
    "active",
    "restartOnPasswordChange",
    "waitBeforeSeconds",
    "additionalInfo",
    "description",
    "scriptId",
    "secretDependencyId"
})
public class Dependency {

    @XmlElement(name = "SecretId")
    protected int secretId;
    @XmlElement(name = "SecretDependencyTypeId")
    protected int secretDependencyTypeId;
    @XmlElement(name = "MachineName")
    protected String machineName;
    @XmlElement(name = "ServiceName")
    protected String serviceName;
    @XmlElement(name = "PrivilegedAccountSecretId")
    protected int privilegedAccountSecretId;
    @XmlElement(name = "Active")
    protected boolean active;
    @XmlElement(name = "RestartOnPasswordChange")
    protected boolean restartOnPasswordChange;
    @XmlElement(name = "WaitBeforeSeconds")
    protected int waitBeforeSeconds;
    @XmlElement(name = "AdditionalInfo")
    protected AdditionalDependencyInfoJson additionalInfo;
    @XmlElement(name = "Description")
    protected String description;
    @XmlElement(name = "ScriptId")
    protected int scriptId;
    @XmlElement(name = "SecretDependencyId")
    protected int secretDependencyId;

    /**
     * Ruft den Wert der secretId-Eigenschaft ab.
     * 
     */
    public int getSecretId() {
        return secretId;
    }

    /**
     * Legt den Wert der secretId-Eigenschaft fest.
     * 
     */
    public void setSecretId(int value) {
        this.secretId = value;
    }

    /**
     * Ruft den Wert der secretDependencyTypeId-Eigenschaft ab.
     * 
     */
    public int getSecretDependencyTypeId() {
        return secretDependencyTypeId;
    }

    /**
     * Legt den Wert der secretDependencyTypeId-Eigenschaft fest.
     * 
     */
    public void setSecretDependencyTypeId(int value) {
        this.secretDependencyTypeId = value;
    }

    /**
     * Ruft den Wert der machineName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMachineName() {
        return machineName;
    }

    /**
     * Legt den Wert der machineName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMachineName(String value) {
        this.machineName = value;
    }

    /**
     * Ruft den Wert der serviceName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * Legt den Wert der serviceName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceName(String value) {
        this.serviceName = value;
    }

    /**
     * Ruft den Wert der privilegedAccountSecretId-Eigenschaft ab.
     * 
     */
    public int getPrivilegedAccountSecretId() {
        return privilegedAccountSecretId;
    }

    /**
     * Legt den Wert der privilegedAccountSecretId-Eigenschaft fest.
     * 
     */
    public void setPrivilegedAccountSecretId(int value) {
        this.privilegedAccountSecretId = value;
    }

    /**
     * Ruft den Wert der active-Eigenschaft ab.
     * 
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Legt den Wert der active-Eigenschaft fest.
     * 
     */
    public void setActive(boolean value) {
        this.active = value;
    }

    /**
     * Ruft den Wert der restartOnPasswordChange-Eigenschaft ab.
     * 
     */
    public boolean isRestartOnPasswordChange() {
        return restartOnPasswordChange;
    }

    /**
     * Legt den Wert der restartOnPasswordChange-Eigenschaft fest.
     * 
     */
    public void setRestartOnPasswordChange(boolean value) {
        this.restartOnPasswordChange = value;
    }

    /**
     * Ruft den Wert der waitBeforeSeconds-Eigenschaft ab.
     * 
     */
    public int getWaitBeforeSeconds() {
        return waitBeforeSeconds;
    }

    /**
     * Legt den Wert der waitBeforeSeconds-Eigenschaft fest.
     * 
     */
    public void setWaitBeforeSeconds(int value) {
        this.waitBeforeSeconds = value;
    }

    /**
     * Ruft den Wert der additionalInfo-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link AdditionalDependencyInfoJson }
     *     
     */
    public AdditionalDependencyInfoJson getAdditionalInfo() {
        return additionalInfo;
    }

    /**
     * Legt den Wert der additionalInfo-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link AdditionalDependencyInfoJson }
     *     
     */
    public void setAdditionalInfo(AdditionalDependencyInfoJson value) {
        this.additionalInfo = value;
    }

    /**
     * Ruft den Wert der description-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Legt den Wert der description-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Ruft den Wert der scriptId-Eigenschaft ab.
     * 
     */
    public int getScriptId() {
        return scriptId;
    }

    /**
     * Legt den Wert der scriptId-Eigenschaft fest.
     * 
     */
    public void setScriptId(int value) {
        this.scriptId = value;
    }

    /**
     * Ruft den Wert der secretDependencyId-Eigenschaft ab.
     * 
     */
    public int getSecretDependencyId() {
        return secretDependencyId;
    }

    /**
     * Legt den Wert der secretDependencyId-Eigenschaft fest.
     * 
     */
    public void setSecretDependencyId(int value) {
        this.secretDependencyId = value;
    }

}
