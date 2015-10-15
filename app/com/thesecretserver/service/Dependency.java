
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Dependency complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
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
     * Gets the value of the secretId property.
     * 
     */
    public int getSecretId() {
        return secretId;
    }

    /**
     * Sets the value of the secretId property.
     * 
     */
    public void setSecretId(int value) {
        this.secretId = value;
    }

    /**
     * Gets the value of the secretDependencyTypeId property.
     * 
     */
    public int getSecretDependencyTypeId() {
        return secretDependencyTypeId;
    }

    /**
     * Sets the value of the secretDependencyTypeId property.
     * 
     */
    public void setSecretDependencyTypeId(int value) {
        this.secretDependencyTypeId = value;
    }

    /**
     * Gets the value of the machineName property.
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
     * Sets the value of the machineName property.
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
     * Gets the value of the serviceName property.
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
     * Sets the value of the serviceName property.
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
     * Gets the value of the privilegedAccountSecretId property.
     * 
     */
    public int getPrivilegedAccountSecretId() {
        return privilegedAccountSecretId;
    }

    /**
     * Sets the value of the privilegedAccountSecretId property.
     * 
     */
    public void setPrivilegedAccountSecretId(int value) {
        this.privilegedAccountSecretId = value;
    }

    /**
     * Gets the value of the active property.
     * 
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the value of the active property.
     * 
     */
    public void setActive(boolean value) {
        this.active = value;
    }

    /**
     * Gets the value of the restartOnPasswordChange property.
     * 
     */
    public boolean isRestartOnPasswordChange() {
        return restartOnPasswordChange;
    }

    /**
     * Sets the value of the restartOnPasswordChange property.
     * 
     */
    public void setRestartOnPasswordChange(boolean value) {
        this.restartOnPasswordChange = value;
    }

    /**
     * Gets the value of the waitBeforeSeconds property.
     * 
     */
    public int getWaitBeforeSeconds() {
        return waitBeforeSeconds;
    }

    /**
     * Sets the value of the waitBeforeSeconds property.
     * 
     */
    public void setWaitBeforeSeconds(int value) {
        this.waitBeforeSeconds = value;
    }

    /**
     * Gets the value of the additionalInfo property.
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
     * Sets the value of the additionalInfo property.
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
     * Gets the value of the description property.
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
     * Sets the value of the description property.
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
     * Gets the value of the scriptId property.
     * 
     */
    public int getScriptId() {
        return scriptId;
    }

    /**
     * Sets the value of the scriptId property.
     * 
     */
    public void setScriptId(int value) {
        this.scriptId = value;
    }

    /**
     * Gets the value of the secretDependencyId property.
     * 
     */
    public int getSecretDependencyId() {
        return secretDependencyId;
    }

    /**
     * Sets the value of the secretDependencyId property.
     * 
     */
    public void setSecretDependencyId(int value) {
        this.secretDependencyId = value;
    }

}
