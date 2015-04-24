
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for User complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="User">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="UserName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DisplayName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DomainId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="IsApplicationAccount" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="RadiusTwoFactor" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="EmailTwoFactor" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="RadiusUserName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EmailAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Password" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Enabled" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "User", propOrder = {
    "id",
    "userName",
    "displayName",
    "domainId",
    "isApplicationAccount",
    "radiusTwoFactor",
    "emailTwoFactor",
    "radiusUserName",
    "emailAddress",
    "password",
    "enabled"
})
public class User {

    @XmlElement(name = "Id", required = true, type = Integer.class, nillable = true)
    protected Integer id;
    @XmlElement(name = "UserName")
    protected String userName;
    @XmlElement(name = "DisplayName")
    protected String displayName;
    @XmlElement(name = "DomainId", required = true, type = Integer.class, nillable = true)
    protected Integer domainId;
    @XmlElement(name = "IsApplicationAccount")
    protected boolean isApplicationAccount;
    @XmlElement(name = "RadiusTwoFactor")
    protected boolean radiusTwoFactor;
    @XmlElement(name = "EmailTwoFactor")
    protected boolean emailTwoFactor;
    @XmlElement(name = "RadiusUserName")
    protected String radiusUserName;
    @XmlElement(name = "EmailAddress")
    protected String emailAddress;
    @XmlElement(name = "Password")
    protected String password;
    @XmlElement(name = "Enabled")
    protected boolean enabled;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setId(Integer value) {
        this.id = value;
    }

    /**
     * Gets the value of the userName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the value of the userName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserName(String value) {
        this.userName = value;
    }

    /**
     * Gets the value of the displayName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets the value of the displayName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisplayName(String value) {
        this.displayName = value;
    }

    /**
     * Gets the value of the domainId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getDomainId() {
        return domainId;
    }

    /**
     * Sets the value of the domainId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setDomainId(Integer value) {
        this.domainId = value;
    }

    /**
     * Gets the value of the isApplicationAccount property.
     * 
     */
    public boolean isIsApplicationAccount() {
        return isApplicationAccount;
    }

    /**
     * Sets the value of the isApplicationAccount property.
     * 
     */
    public void setIsApplicationAccount(boolean value) {
        this.isApplicationAccount = value;
    }

    /**
     * Gets the value of the radiusTwoFactor property.
     * 
     */
    public boolean isRadiusTwoFactor() {
        return radiusTwoFactor;
    }

    /**
     * Sets the value of the radiusTwoFactor property.
     * 
     */
    public void setRadiusTwoFactor(boolean value) {
        this.radiusTwoFactor = value;
    }

    /**
     * Gets the value of the emailTwoFactor property.
     * 
     */
    public boolean isEmailTwoFactor() {
        return emailTwoFactor;
    }

    /**
     * Sets the value of the emailTwoFactor property.
     * 
     */
    public void setEmailTwoFactor(boolean value) {
        this.emailTwoFactor = value;
    }

    /**
     * Gets the value of the radiusUserName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRadiusUserName() {
        return radiusUserName;
    }

    /**
     * Sets the value of the radiusUserName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRadiusUserName(String value) {
        this.radiusUserName = value;
    }

    /**
     * Gets the value of the emailAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Sets the value of the emailAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmailAddress(String value) {
        this.emailAddress = value;
    }

    /**
     * Gets the value of the password property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the value of the password property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassword(String value) {
        this.password = value;
    }

    /**
     * Gets the value of the enabled property.
     * 
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets the value of the enabled property.
     * 
     */
    public void setEnabled(boolean value) {
        this.enabled = value;
    }

}
