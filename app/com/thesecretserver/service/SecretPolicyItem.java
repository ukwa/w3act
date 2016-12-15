
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SecretPolicyItem complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SecretPolicyItem">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SecretPolicyItemMapId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="SecretPolicyItemId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="PolicyApplyCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EnabledValue" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="IntegerValue" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="SecretId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="StringValue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ValueType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ParentSecretPolicyItemId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="SectionName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="UserGroupMaps" type="{urn:thesecretserver.com}ArrayOfUserGroupMap" minOccurs="0"/>
 *         &lt;element name="SshCommandMenuGroupMaps" type="{urn:thesecretserver.com}ArrayOfSshCommandMenuGroupMap" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SecretPolicyItem", propOrder = {
    "secretPolicyItemMapId",
    "secretPolicyItemId",
    "policyApplyCode",
    "enabledValue",
    "integerValue",
    "secretId",
    "stringValue",
    "name",
    "description",
    "valueType",
    "parentSecretPolicyItemId",
    "sectionName",
    "userGroupMaps",
    "sshCommandMenuGroupMaps"
})
public class SecretPolicyItem {

    @XmlElement(name = "SecretPolicyItemMapId")
    protected int secretPolicyItemMapId;
    @XmlElement(name = "SecretPolicyItemId")
    protected int secretPolicyItemId;
    @XmlElement(name = "PolicyApplyCode")
    protected String policyApplyCode;
    @XmlElement(name = "EnabledValue", required = true, type = Boolean.class, nillable = true)
    protected Boolean enabledValue;
    @XmlElement(name = "IntegerValue", required = true, type = Integer.class, nillable = true)
    protected Integer integerValue;
    @XmlElement(name = "SecretId", required = true, type = Integer.class, nillable = true)
    protected Integer secretId;
    @XmlElement(name = "StringValue")
    protected String stringValue;
    @XmlElement(name = "Name")
    protected String name;
    @XmlElement(name = "Description")
    protected String description;
    @XmlElement(name = "ValueType")
    protected String valueType;
    @XmlElement(name = "ParentSecretPolicyItemId", required = true, type = Integer.class, nillable = true)
    protected Integer parentSecretPolicyItemId;
    @XmlElement(name = "SectionName")
    protected String sectionName;
    @XmlElement(name = "UserGroupMaps")
    protected ArrayOfUserGroupMap userGroupMaps;
    @XmlElement(name = "SshCommandMenuGroupMaps")
    protected ArrayOfSshCommandMenuGroupMap sshCommandMenuGroupMaps;

    /**
     * Gets the value of the secretPolicyItemMapId property.
     * 
     */
    public int getSecretPolicyItemMapId() {
        return secretPolicyItemMapId;
    }

    /**
     * Sets the value of the secretPolicyItemMapId property.
     * 
     */
    public void setSecretPolicyItemMapId(int value) {
        this.secretPolicyItemMapId = value;
    }

    /**
     * Gets the value of the secretPolicyItemId property.
     * 
     */
    public int getSecretPolicyItemId() {
        return secretPolicyItemId;
    }

    /**
     * Sets the value of the secretPolicyItemId property.
     * 
     */
    public void setSecretPolicyItemId(int value) {
        this.secretPolicyItemId = value;
    }

    /**
     * Gets the value of the policyApplyCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPolicyApplyCode() {
        return policyApplyCode;
    }

    /**
     * Sets the value of the policyApplyCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPolicyApplyCode(String value) {
        this.policyApplyCode = value;
    }

    /**
     * Gets the value of the enabledValue property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEnabledValue() {
        return enabledValue;
    }

    /**
     * Sets the value of the enabledValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEnabledValue(Boolean value) {
        this.enabledValue = value;
    }

    /**
     * Gets the value of the integerValue property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIntegerValue() {
        return integerValue;
    }

    /**
     * Sets the value of the integerValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIntegerValue(Integer value) {
        this.integerValue = value;
    }

    /**
     * Gets the value of the secretId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSecretId() {
        return secretId;
    }

    /**
     * Sets the value of the secretId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSecretId(Integer value) {
        this.secretId = value;
    }

    /**
     * Gets the value of the stringValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStringValue() {
        return stringValue;
    }

    /**
     * Sets the value of the stringValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStringValue(String value) {
        this.stringValue = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
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
     * Gets the value of the valueType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValueType() {
        return valueType;
    }

    /**
     * Sets the value of the valueType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValueType(String value) {
        this.valueType = value;
    }

    /**
     * Gets the value of the parentSecretPolicyItemId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getParentSecretPolicyItemId() {
        return parentSecretPolicyItemId;
    }

    /**
     * Sets the value of the parentSecretPolicyItemId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setParentSecretPolicyItemId(Integer value) {
        this.parentSecretPolicyItemId = value;
    }

    /**
     * Gets the value of the sectionName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSectionName() {
        return sectionName;
    }

    /**
     * Sets the value of the sectionName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSectionName(String value) {
        this.sectionName = value;
    }

    /**
     * Gets the value of the userGroupMaps property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfUserGroupMap }
     *     
     */
    public ArrayOfUserGroupMap getUserGroupMaps() {
        return userGroupMaps;
    }

    /**
     * Sets the value of the userGroupMaps property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfUserGroupMap }
     *     
     */
    public void setUserGroupMaps(ArrayOfUserGroupMap value) {
        this.userGroupMaps = value;
    }

    /**
     * Gets the value of the sshCommandMenuGroupMaps property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfSshCommandMenuGroupMap }
     *     
     */
    public ArrayOfSshCommandMenuGroupMap getSshCommandMenuGroupMaps() {
        return sshCommandMenuGroupMaps;
    }

    /**
     * Sets the value of the sshCommandMenuGroupMaps property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfSshCommandMenuGroupMap }
     *     
     */
    public void setSshCommandMenuGroupMaps(ArrayOfSshCommandMenuGroupMap value) {
        this.sshCommandMenuGroupMaps = value;
    }

}
