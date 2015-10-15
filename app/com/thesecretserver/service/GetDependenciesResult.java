
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für GetDependenciesResult complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="GetDependenciesResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Dependencies" type="{urn:thesecretserver.com}ArrayOfDependency" minOccurs="0"/>
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
@XmlType(name = "GetDependenciesResult", propOrder = {
    "dependencies",
    "errors",
    "success"
})
public class GetDependenciesResult {

    @XmlElement(name = "Dependencies")
    protected ArrayOfDependency dependencies;
    @XmlElement(name = "Errors")
    protected ArrayOfString errors;
    @XmlElement(name = "Success")
    protected boolean success;

    /**
     * Ruft den Wert der dependencies-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfDependency }
     *     
     */
    public ArrayOfDependency getDependencies() {
        return dependencies;
    }

    /**
     * Legt den Wert der dependencies-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfDependency }
     *     
     */
    public void setDependencies(ArrayOfDependency value) {
        this.dependencies = value;
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
