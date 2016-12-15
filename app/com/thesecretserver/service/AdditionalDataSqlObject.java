
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AdditionalDataSqlObject complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AdditionalDataSqlObject">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Params" type="{urn:thesecretserver.com}ArrayOfSqlScriptArgument2" minOccurs="0"/>
 *         &lt;element name="PasswordChangerId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Version" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Database" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Port" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AdditionalDataSqlObject", propOrder = {
    "params",
    "passwordChangerId",
    "version",
    "database",
    "port"
})
public class AdditionalDataSqlObject {

    @XmlElement(name = "Params")
    protected ArrayOfSqlScriptArgument2 params;
    @XmlElement(name = "PasswordChangerId")
    protected int passwordChangerId;
    @XmlElement(name = "Version")
    protected int version;
    @XmlElement(name = "Database")
    protected String database;
    @XmlElement(name = "Port")
    protected String port;

    /**
     * Gets the value of the params property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfSqlScriptArgument2 }
     *     
     */
    public ArrayOfSqlScriptArgument2 getParams() {
        return params;
    }

    /**
     * Sets the value of the params property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfSqlScriptArgument2 }
     *     
     */
    public void setParams(ArrayOfSqlScriptArgument2 value) {
        this.params = value;
    }

    /**
     * Gets the value of the passwordChangerId property.
     * 
     */
    public int getPasswordChangerId() {
        return passwordChangerId;
    }

    /**
     * Sets the value of the passwordChangerId property.
     * 
     */
    public void setPasswordChangerId(int value) {
        this.passwordChangerId = value;
    }

    /**
     * Gets the value of the version property.
     * 
     */
    public int getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     */
    public void setVersion(int value) {
        this.version = value;
    }

    /**
     * Gets the value of the database property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatabase() {
        return database;
    }

    /**
     * Sets the value of the database property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatabase(String value) {
        this.database = value;
    }

    /**
     * Gets the value of the port property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPort() {
        return port;
    }

    /**
     * Sets the value of the port property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPort(String value) {
        this.port = value;
    }

}
