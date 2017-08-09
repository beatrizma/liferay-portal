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

package com.liferay.modern.site.building.fragment.service.permission;

import com.liferay.exportimport.kernel.staging.permission.StagingPermissionUtil;
import com.liferay.modern.site.building.fragment.model.FragmentEntry;
import com.liferay.modern.site.building.fragment.service.FragmentEntryLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.PortletProvider;
import com.liferay.portal.kernel.portlet.PortletProviderUtil;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.BaseModelPermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.util.HashUtil;

import java.util.Map;
import java.util.Objects;

import org.osgi.service.component.annotations.Component;

/**
 * @author Jürgen Kappler
 */
@Component(
	property = {"model.class.name=com.liferay.modern.site.building.fragment.model.FragmentEntry"},
	service = BaseModelPermissionChecker.class
)
public class FragmentEntryPermission implements BaseModelPermissionChecker {

	public static void check(
			PermissionChecker permissionChecker, FragmentEntry fragmentEntry,
			String actionId)
		throws PortalException {

		if (!contains(permissionChecker, fragmentEntry, actionId)) {
			throw new PrincipalException.MustHavePermission(
				permissionChecker, FragmentEntry.class.getName(),
				fragmentEntry.getFragmentEntryId(), actionId);
		}
	}

	public static void check(
			PermissionChecker permissionChecker, long fragmentEntryId,
			String actionId)
		throws PortalException {

		if (!contains(permissionChecker, fragmentEntryId, actionId)) {
			throw new PrincipalException.MustHavePermission(
				permissionChecker, FragmentEntry.class.getName(),
				fragmentEntryId, actionId);
		}
	}

	public static boolean contains(
		PermissionChecker permissionChecker, FragmentEntry fragmentEntry,
		String actionId) {

		Map<Object, Object> permissionChecksMap =
			permissionChecker.getPermissionChecksMap();

		PermissionCacheKey permissionCacheKey = new PermissionCacheKey(
			fragmentEntry.getFragmentEntryId(), actionId);

		Boolean contains = (Boolean)permissionChecksMap.get(permissionCacheKey);

		if (contains == null) {
			contains = _contains(permissionChecker, fragmentEntry, actionId);

			permissionChecksMap.put(permissionCacheKey, contains);
		}

		return contains;
	}

	public static boolean contains(
			PermissionChecker permissionChecker, long fragmentEntryId,
			String actionId)
		throws PortalException {

		Map<Object, Object> permissionChecksMap =
			permissionChecker.getPermissionChecksMap();

		PermissionCacheKey permissionCacheKey = new PermissionCacheKey(
			fragmentEntryId, actionId);

		Boolean contains = (Boolean)permissionChecksMap.get(permissionCacheKey);

		if (contains == null) {
			FragmentEntry fragmentEntry =
				FragmentEntryLocalServiceUtil.getFragmentEntry(fragmentEntryId);

			contains = _contains(permissionChecker, fragmentEntry, actionId);

			permissionChecksMap.put(permissionCacheKey, contains);
		}

		return contains;
	}

	@Override
	public void checkBaseModel(
			PermissionChecker permissionChecker, long groupId, long primaryKey,
			String actionId)
		throws PortalException {

		check(permissionChecker, primaryKey, actionId);
	}

	private static boolean _contains(
		PermissionChecker permissionChecker, FragmentEntry fragmentEntry,
		String actionId) {

		String portletId = PortletProviderUtil.getPortletId(
			FragmentEntry.class.getName(), PortletProvider.Action.EDIT);

		Boolean hasPermission = StagingPermissionUtil.hasPermission(
			permissionChecker, fragmentEntry.getGroupId(),
			FragmentEntry.class.getName(), fragmentEntry.getFragmentEntryId(),
			portletId, actionId);

		if (hasPermission != null) {
			return hasPermission.booleanValue();
		}

		if (permissionChecker.hasOwnerPermission(
				fragmentEntry.getCompanyId(), FragmentEntry.class.getName(),
				fragmentEntry.getFragmentCollectionId(),
				fragmentEntry.getUserId(), actionId)) {

			return true;
		}

		return permissionChecker.hasPermission(
			fragmentEntry.getGroupId(), FragmentEntry.class.getName(),
			fragmentEntry.getFragmentEntryId(), actionId);
	}

	private static class PermissionCacheKey {

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}

			if (!(obj instanceof PermissionCacheKey)) {
				return false;
			}

			PermissionCacheKey permissionCacheKey = (PermissionCacheKey)obj;

			if ((_entryId == permissionCacheKey._entryId) &&
				Objects.equals(_actionId, permissionCacheKey._actionId)) {

				return true;
			}

			return false;
		}

		@Override
		public int hashCode() {
			int hash = HashUtil.hash(0, _entryId);

			return HashUtil.hash(hash, _actionId);
		}

		private PermissionCacheKey(long entryId, String actionId) {
			_entryId = entryId;
			_actionId = actionId;
		}

		private final String _actionId;
		private final long _entryId;

	}

}