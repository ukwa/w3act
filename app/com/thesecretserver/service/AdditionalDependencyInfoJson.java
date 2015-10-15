
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für AdditionalDependencyInfoJson complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="AdditionalDependencyInfoJson">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Regex" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PowershellArguments" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SshArguments" type="{urn:thesecretserver.com}ArrayOfSshScriptArgument" minOccurs="0"/>
 *         &lt;element name="SqlArguments" type="{urn:thesecretserver.com}ArrayOfSqlScriptArgument" minOccurs="0"/>
 *         &lt;element name="OdbcConnectionArguments" type="{urn:thesecretserver.com}ArrayOfOdbcConnectionArg" minOccurs="0"/>
 *         &lt;element name="Port" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Database" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ServerKeyDigest" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AdditionalDependencyInfoJson", propOrder = {
    "regex",
    "powershellArguments",
    "sshArguments",
    "sqlArguments",
    "odbcConnectionArguments",
    "port",
    "database",
    "serverKeyDigest"
})
public class AdditionalDependencyInfoJson {

    @XmlElement(name = "Regex")
    protected String regex;
    @XmlElement(name = "PowershellArguments")
    protected String powershellArguments;
    @XmlElement(name = "SshArguments")
    protected ArrayOfSshScriptArgument sshArguments;
    @XmlElement(name = "SqlArguments")
    protected ArrayOfSqlScriptArgument sqlArguments;
    @XmlElement(name = "OdbcConnectionArguments")
    protected ArrayOfOdbcConnectionArg odbcConnectionArguments;
    @XmlElement(name = "Port")
    protected String port;
    @XmlElement(name = "Database")
    protected String database;
    @XmlElement(name = "ServerKeyDigest")
    protected String serverKeyDigest;

    /**
     * Ruft den Wert der regex-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegex() {
        return regex;
    }

    /**
     * Legt den Wert der regex-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegex(String value) {
        this.regex = value;
    }

    /**
     * Ruft den Wert der powershellArguments-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPowershellArguments() {
        return powershellArguments;
    }

    /**
     * Legt den Wert der powershellArguments-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPowershellArguments(String value) {
        this.powershellArguments = value;
    }

    /**
     * Ruft den Wert der sshArguments-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfSshScriptArgument }
     *     
     */
    public ArrayOfSshScriptArgument getSshArguments() {
        return sshArguments;
    }

    /**
     * Legt den Wert der sshArguments-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfSshScriptArgument }
     *     
     */
    public void setSshArguments(ArrayOfSshScriptArgument value) {
        this.sshArguments = value;
    }

    /**
     * Ruft den Wert der sqlArguments-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfSqlScriptArgument }
     *     
     */
    public ArrayOfSqlScriptArgument getSqlArguments() {
        return sqlArguments;
    }

    /**
     * Legt den Wert der sqlArguments-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfSqlScriptArgument }
     *     
     */
    public void setSqlArguments(ArrayOfSqlScriptArgument value) {
        this.sqlArguments = value;
    }

    /**
     * Ruft den Wert der odbcConnectionArguments-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfOdbcConnectionArg }
     *     
     */
    public ArrayOfOdbcConnectionArg getOdbcConnectionArguments() {
        return odbcConnectionArguments;
    }

    /**
     * Legt den Wert der odbcConnectionArguments-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfOdbcConnectionArg }
     *     
     */
    public void setOdbcConnectionArguments(ArrayOfOdbcConnectionArg value) {
        this.odbcConnectionArguments = value;
    }

    /**
     * Ruft den Wert der port-Eigenschaft ab.
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
     * Legt den Wert der port-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPort(String value) {
        this.port = value;
    }

    /**
     * Ruft den Wert der database-Eigenschaft ab.
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
     * Legt den Wert der database-Eigenschaft fest.
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
     * Ruft den Wert der serverKeyDigest-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServerKeyDigest() {
        return serverKeyDigest;
    }

    /**
     * Legt den Wert der serverKeyDigest-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServerKeyDigest(String value) {
        this.serverKeyDigest = value;
    }

}
