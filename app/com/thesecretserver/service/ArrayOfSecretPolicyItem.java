
package com.thesecretserver.service;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfSecretPolicyItem complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfSecretPolicyItem">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SecretPolicyItem" type="{urn:thesecretserver.com}SecretPolicyItem" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfSecretPolicyItem", propOrder = {
    "secretPolicyItem"
})
public class ArrayOfSecretPolicyItem {

    @XmlElement(name = "SecretPolicyItem", nillable = true)
    protected List<SecretPolicyItem> secretPolicyItem;

    /**
     * Gets the value of the secretPolicyItem property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the secretPolicyItem property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSecretPolicyItem().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SecretPolicyItem }
     * 
     * 
     */
    public List<SecretPolicyItem> getSecretPolicyItem() {
        if (secretPolicyItem == null) {
            secretPolicyItem = new ArrayList<SecretPolicyItem>();
        }
        return this.secretPolicyItem;
    }

}
