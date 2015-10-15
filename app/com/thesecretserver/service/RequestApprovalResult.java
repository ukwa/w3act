
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für RequestApprovalResult complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="RequestApprovalResult">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:thesecretserver.com}GenericResult">
 *       &lt;sequence>
 *         &lt;element name="ApprovalInfo" type="{urn:thesecretserver.com}ApprovalInfo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RequestApprovalResult", propOrder = {
    "approvalInfo"
})
public class RequestApprovalResult
    extends GenericResult
{

    @XmlElement(name = "ApprovalInfo")
    protected ApprovalInfo approvalInfo;

    /**
     * Ruft den Wert der approvalInfo-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ApprovalInfo }
     *     
     */
    public ApprovalInfo getApprovalInfo() {
        return approvalInfo;
    }

    /**
     * Legt den Wert der approvalInfo-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ApprovalInfo }
     *     
     */
    public void setApprovalInfo(ApprovalInfo value) {
        this.approvalInfo = value;
    }

}
