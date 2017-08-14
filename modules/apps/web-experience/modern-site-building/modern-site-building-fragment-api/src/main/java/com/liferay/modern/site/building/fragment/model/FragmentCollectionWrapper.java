/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.modern.site.building.fragment.model;

import aQute.bnd.annotation.ProviderType;

import com.liferay.expando.kernel.model.ExpandoBridge;

import com.liferay.exportimport.kernel.lar.StagedModelType;

import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.service.ServiceContext;

import java.io.Serializable;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * This class is a wrapper for {@link FragmentCollection}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see FragmentCollection
 * @generated
 */
@ProviderType
public class FragmentCollectionWrapper implements FragmentCollection,
	ModelWrapper<FragmentCollection> {
	public FragmentCollectionWrapper(FragmentCollection fragmentCollection) {
		_fragmentCollection = fragmentCollection;
	}

	@Override
	public Class<?> getModelClass() {
		return FragmentCollection.class;
	}

	@Override
	public String getModelClassName() {
		return FragmentCollection.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("uuid", getUuid());
		attributes.put("fragmentCollectionId", getFragmentCollectionId());
		attributes.put("groupId", getGroupId());
		attributes.put("companyId", getCompanyId());
		attributes.put("userId", getUserId());
		attributes.put("userName", getUserName());
		attributes.put("createDate", getCreateDate());
		attributes.put("modifiedDate", getModifiedDate());
		attributes.put("name", getName());
		attributes.put("description", getDescription());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		String uuid = (String)attributes.get("uuid");

		if (uuid != null) {
			setUuid(uuid);
		}

		Long fragmentCollectionId = (Long)attributes.get("fragmentCollectionId");

		if (fragmentCollectionId != null) {
			setFragmentCollectionId(fragmentCollectionId);
		}

		Long groupId = (Long)attributes.get("groupId");

		if (groupId != null) {
			setGroupId(groupId);
		}

		Long companyId = (Long)attributes.get("companyId");

		if (companyId != null) {
			setCompanyId(companyId);
		}

		Long userId = (Long)attributes.get("userId");

		if (userId != null) {
			setUserId(userId);
		}

		String userName = (String)attributes.get("userName");

		if (userName != null) {
			setUserName(userName);
		}

		Date createDate = (Date)attributes.get("createDate");

		if (createDate != null) {
			setCreateDate(createDate);
		}

		Date modifiedDate = (Date)attributes.get("modifiedDate");

		if (modifiedDate != null) {
			setModifiedDate(modifiedDate);
		}

		String name = (String)attributes.get("name");

		if (name != null) {
			setName(name);
		}

		String description = (String)attributes.get("description");

		if (description != null) {
			setDescription(description);
		}
	}

	@Override
	public FragmentCollection toEscapedModel() {
		return new FragmentCollectionWrapper(_fragmentCollection.toEscapedModel());
	}

	@Override
	public FragmentCollection toUnescapedModel() {
		return new FragmentCollectionWrapper(_fragmentCollection.toUnescapedModel());
	}

	@Override
	public boolean isCachedModel() {
		return _fragmentCollection.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _fragmentCollection.isEscapedModel();
	}

	/**
	* Returns <code>true</code> if this fragment collection is in the Recycle Bin.
	*
	* @return <code>true</code> if this fragment collection is in the Recycle Bin; <code>false</code> otherwise
	*/
	@Override
	public boolean isInTrash() {
		return _fragmentCollection.isInTrash();
	}

	/**
	* Returns <code>true</code> if the parent of this fragment collection is in the Recycle Bin.
	*
	* @return <code>true</code> if the parent of this fragment collection is in the Recycle Bin; <code>false</code> otherwise
	*/
	@Override
	public boolean isInTrashContainer() {
		return _fragmentCollection.isInTrashContainer();
	}

	@Override
	public boolean isInTrashExplicitly() {
		return _fragmentCollection.isInTrashExplicitly();
	}

	@Override
	public boolean isInTrashImplicitly() {
		return _fragmentCollection.isInTrashImplicitly();
	}

	@Override
	public boolean isNew() {
		return _fragmentCollection.isNew();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _fragmentCollection.getExpandoBridge();
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<FragmentCollection> toCacheModel() {
		return _fragmentCollection.toCacheModel();
	}

	/**
	* Returns the trash handler for this fragment collection.
	*
	* @return the trash handler for this fragment collection
	* @deprecated As of 7.0.0, with no direct replacement
	*/
	@Deprecated
	@Override
	public com.liferay.portal.kernel.trash.TrashHandler getTrashHandler() {
		return _fragmentCollection.getTrashHandler();
	}

	/**
	* Returns the trash entry created when this fragment collection was moved to the Recycle Bin. The trash entry may belong to one of the ancestors of this fragment collection.
	*
	* @return the trash entry created when this fragment collection was moved to the Recycle Bin
	*/
	@Override
	public com.liferay.trash.kernel.model.TrashEntry getTrashEntry()
		throws com.liferay.portal.kernel.exception.PortalException {
		return _fragmentCollection.getTrashEntry();
	}

	@Override
	public int compareTo(FragmentCollection fragmentCollection) {
		return _fragmentCollection.compareTo(fragmentCollection);
	}

	/**
	* Returns the status of this fragment collection.
	*
	* @return the status of this fragment collection
	*/
	@Override
	public int getStatus() {
		return _fragmentCollection.getStatus();
	}

	@Override
	public int hashCode() {
		return _fragmentCollection.hashCode();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _fragmentCollection.getPrimaryKeyObj();
	}

	@Override
	public java.lang.Object clone() {
		return new FragmentCollectionWrapper((FragmentCollection)_fragmentCollection.clone());
	}

	/**
	* Returns the description of this fragment collection.
	*
	* @return the description of this fragment collection
	*/
	@Override
	public java.lang.String getDescription() {
		return _fragmentCollection.getDescription();
	}

	/**
	* Returns the name of this fragment collection.
	*
	* @return the name of this fragment collection
	*/
	@Override
	public java.lang.String getName() {
		return _fragmentCollection.getName();
	}

	/**
	* Returns the user name of this fragment collection.
	*
	* @return the user name of this fragment collection
	*/
	@Override
	public java.lang.String getUserName() {
		return _fragmentCollection.getUserName();
	}

	/**
	* Returns the user uuid of this fragment collection.
	*
	* @return the user uuid of this fragment collection
	*/
	@Override
	public java.lang.String getUserUuid() {
		return _fragmentCollection.getUserUuid();
	}

	/**
	* Returns the uuid of this fragment collection.
	*
	* @return the uuid of this fragment collection
	*/
	@Override
	public java.lang.String getUuid() {
		return _fragmentCollection.getUuid();
	}

	@Override
	public java.lang.String toString() {
		return _fragmentCollection.toString();
	}

	@Override
	public java.lang.String toXmlString() {
		return _fragmentCollection.toXmlString();
	}

	/**
	* Returns the create date of this fragment collection.
	*
	* @return the create date of this fragment collection
	*/
	@Override
	public Date getCreateDate() {
		return _fragmentCollection.getCreateDate();
	}

	/**
	* Returns the modified date of this fragment collection.
	*
	* @return the modified date of this fragment collection
	*/
	@Override
	public Date getModifiedDate() {
		return _fragmentCollection.getModifiedDate();
	}

	/**
	* Returns the company ID of this fragment collection.
	*
	* @return the company ID of this fragment collection
	*/
	@Override
	public long getCompanyId() {
		return _fragmentCollection.getCompanyId();
	}

	/**
	* Returns the fragment collection ID of this fragment collection.
	*
	* @return the fragment collection ID of this fragment collection
	*/
	@Override
	public long getFragmentCollectionId() {
		return _fragmentCollection.getFragmentCollectionId();
	}

	/**
	* Returns the group ID of this fragment collection.
	*
	* @return the group ID of this fragment collection
	*/
	@Override
	public long getGroupId() {
		return _fragmentCollection.getGroupId();
	}

	/**
	* Returns the primary key of this fragment collection.
	*
	* @return the primary key of this fragment collection
	*/
	@Override
	public long getPrimaryKey() {
		return _fragmentCollection.getPrimaryKey();
	}

	/**
	* Returns the class primary key of the trash entry for this fragment collection.
	*
	* @return the class primary key of the trash entry for this fragment collection
	*/
	@Override
	public long getTrashEntryClassPK() {
		return _fragmentCollection.getTrashEntryClassPK();
	}

	/**
	* Returns the user ID of this fragment collection.
	*
	* @return the user ID of this fragment collection
	*/
	@Override
	public long getUserId() {
		return _fragmentCollection.getUserId();
	}

	@Override
	public void persist() {
		_fragmentCollection.persist();
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_fragmentCollection.setCachedModel(cachedModel);
	}

	/**
	* Sets the company ID of this fragment collection.
	*
	* @param companyId the company ID of this fragment collection
	*/
	@Override
	public void setCompanyId(long companyId) {
		_fragmentCollection.setCompanyId(companyId);
	}

	/**
	* Sets the create date of this fragment collection.
	*
	* @param createDate the create date of this fragment collection
	*/
	@Override
	public void setCreateDate(Date createDate) {
		_fragmentCollection.setCreateDate(createDate);
	}

	/**
	* Sets the description of this fragment collection.
	*
	* @param description the description of this fragment collection
	*/
	@Override
	public void setDescription(java.lang.String description) {
		_fragmentCollection.setDescription(description);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_fragmentCollection.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {
		_fragmentCollection.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_fragmentCollection.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the fragment collection ID of this fragment collection.
	*
	* @param fragmentCollectionId the fragment collection ID of this fragment collection
	*/
	@Override
	public void setFragmentCollectionId(long fragmentCollectionId) {
		_fragmentCollection.setFragmentCollectionId(fragmentCollectionId);
	}

	/**
	* Sets the group ID of this fragment collection.
	*
	* @param groupId the group ID of this fragment collection
	*/
	@Override
	public void setGroupId(long groupId) {
		_fragmentCollection.setGroupId(groupId);
	}

	/**
	* Sets the modified date of this fragment collection.
	*
	* @param modifiedDate the modified date of this fragment collection
	*/
	@Override
	public void setModifiedDate(Date modifiedDate) {
		_fragmentCollection.setModifiedDate(modifiedDate);
	}

	/**
	* Sets the name of this fragment collection.
	*
	* @param name the name of this fragment collection
	*/
	@Override
	public void setName(java.lang.String name) {
		_fragmentCollection.setName(name);
	}

	@Override
	public void setNew(boolean n) {
		_fragmentCollection.setNew(n);
	}

	/**
	* Sets the primary key of this fragment collection.
	*
	* @param primaryKey the primary key of this fragment collection
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_fragmentCollection.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_fragmentCollection.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the user ID of this fragment collection.
	*
	* @param userId the user ID of this fragment collection
	*/
	@Override
	public void setUserId(long userId) {
		_fragmentCollection.setUserId(userId);
	}

	/**
	* Sets the user name of this fragment collection.
	*
	* @param userName the user name of this fragment collection
	*/
	@Override
	public void setUserName(java.lang.String userName) {
		_fragmentCollection.setUserName(userName);
	}

	/**
	* Sets the user uuid of this fragment collection.
	*
	* @param userUuid the user uuid of this fragment collection
	*/
	@Override
	public void setUserUuid(java.lang.String userUuid) {
		_fragmentCollection.setUserUuid(userUuid);
	}

	/**
	* Sets the uuid of this fragment collection.
	*
	* @param uuid the uuid of this fragment collection
	*/
	@Override
	public void setUuid(java.lang.String uuid) {
		_fragmentCollection.setUuid(uuid);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof FragmentCollectionWrapper)) {
			return false;
		}

		FragmentCollectionWrapper fragmentCollectionWrapper = (FragmentCollectionWrapper)obj;

		if (Objects.equals(_fragmentCollection,
					fragmentCollectionWrapper._fragmentCollection)) {
			return true;
		}

		return false;
	}

	@Override
	public StagedModelType getStagedModelType() {
		return _fragmentCollection.getStagedModelType();
	}

	@Override
	public FragmentCollection getWrappedModel() {
		return _fragmentCollection;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _fragmentCollection.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _fragmentCollection.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_fragmentCollection.resetOriginalValues();
	}

	private final FragmentCollection _fragmentCollection;
}