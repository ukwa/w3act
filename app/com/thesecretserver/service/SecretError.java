
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SecretError complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SecretError">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ErrorCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ErrorMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AllowsResponse" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="CommentTitle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AdditionalCommentTitle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SecretError", propOrder = {
    "errorCode",
    "errorMessage",
    "allowsResponse",
    "commentTitle",
    "additionalCommentTitle"
})
public class SecretError {

    @XmlElement(name = "ErrorCode")
    protected String errorCode;
    @XmlElement(name = "ErrorMessage")
    protected String errorMessage;
    @XmlElement(name = "AllowsResponse")
    protected boolean allowsResponse;
    @XmlElement(name = "CommentTitle")
    protected String commentTitle;
    @XmlElement(name = "AdditionalCommentTitle")
    protected String additionalCommentTitle;

    /**
     * Gets the value of the errorCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Sets the value of the errorCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrorCode(String value) {
        this.errorCode = value;
    }

    /**
     * Gets the value of the errorMessage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Sets the value of the errorMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrorMessage(String value) {
        this.errorMessage = value;
    }

    /**
     * Gets the value of the allowsResponse property.
     * 
     */
    public boolean isAllowsResponse() {
        return allowsResponse;
    }

    /**
     * Sets the value of the allowsResponse property.
     * 
     */
    public void setAllowsResponse(boolean value) {
        this.allowsResponse = value;
    }

    /**
     * Gets the value of the commentTitle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCommentTitle() {
        return commentTitle;
    }

    /**
     * Sets the value of the commentTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCommentTitle(String value) {
        this.commentTitle = value;
    }

    /**
     * Gets the value of the additionalCommentTitle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdditionalCommentTitle() {
        return additionalCommentTitle;
    }

    /**
     * Sets the value of the additionalCommentTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdditionalCommentTitle(String value) {
        this.additionalCommentTitle = value;
    }

}
