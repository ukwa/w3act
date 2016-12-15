
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AdditionalDataSshObject complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AdditionalDataSshObject">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Port" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LineEnding" type="{urn:thesecretserver.com}LineEnding"/>
 *         &lt;element name="DoNotUseEnvironment" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="Params" type="{urn:thesecretserver.com}ArrayOfSshScriptArgument2" minOccurs="0"/>
 *         &lt;element name="Version" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AdditionalDataSshObject", propOrder = {
    "port",
    "lineEnding",
    "doNotUseEnvironment",
    "params",
    "version"
})
public class AdditionalDataSshObject {

    @XmlElement(name = "Port")
    protected String port;
    @XmlElement(name = "LineEnding", required = true)
    @XmlSchemaType(name = "string")
    protected LineEnding lineEnding;
    @XmlElement(name = "DoNotUseEnvironment")
    protected boolean doNotUseEnvironment;
    @XmlElement(name = "Params")
    protected ArrayOfSshScriptArgument2 params;
    @XmlElement(name = "Version")
    protected int version;

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

    /**
     * Gets the value of the lineEnding property.
     * 
     * @return
     *     possible object is
     *     {@link LineEnding }
     *     
     */
    public LineEnding getLineEnding() {
        return lineEnding;
    }

    /**
     * Sets the value of the lineEnding property.
     * 
     * @param value
     *     allowed object is
     *     {@link LineEnding }
     *     
     */
    public void setLineEnding(LineEnding value) {
        this.lineEnding = value;
    }

    /**
     * Gets the value of the doNotUseEnvironment property.
     * 
     */
    public boolean isDoNotUseEnvironment() {
        return doNotUseEnvironment;
    }

    /**
     * Sets the value of the doNotUseEnvironment property.
     * 
     */
    public void setDoNotUseEnvironment(boolean value) {
        this.doNotUseEnvironment = value;
    }

    /**
     * Gets the value of the params property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfSshScriptArgument2 }
     *     
     */
    public ArrayOfSshScriptArgument2 getParams() {
        return params;
    }

    /**
     * Sets the value of the params property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfSshScriptArgument2 }
     *     
     */
    public void setParams(ArrayOfSshScriptArgument2 value) {
        this.params = value;
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

}
