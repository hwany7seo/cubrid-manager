package com.cubrid.common.ui.cubrid.database.erwin.xmlmodel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {

})
@XmlRootElement(name = "Key_Group_Member")
public class KeyGroupMember {

	@XmlElement(name = "Key_Group_MemberProps", required = true)
	protected KeyGroupMemberPropsList keyGroupMemberProps;
	@XmlAttribute(name = "id", required = true)
	protected String id;
	@XmlAttribute(name = "Name")
	protected String name;

	/**
	 * 
	 * @return possible object is {@link KeyGroupMemberPropsList }
	 * 
	 */
	public KeyGroupMemberPropsList getKeyGroupMemberProps() {
		return keyGroupMemberProps;
	}

	/**
	 * 
	 * @param value allowed object is {@link KeyGroupMemberPropsList }
	 * 
	 */
	public void setKeyGroupMemberProps(KeyGroupMemberPropsList value) {
		this.keyGroupMemberProps = value;
	}

	/**
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getId() {
		return id;
	}

	/**
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setId(String value) {
		this.id = value;
	}

	/**
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setName(String value) {
		this.name = value;
	}

}
