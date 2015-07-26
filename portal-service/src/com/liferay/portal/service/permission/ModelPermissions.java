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

package com.liferay.portal.service.permission;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Jorge Ferrer
 */
public class ModelPermissions implements Serializable {

	public void addRolePermissions(String roleName, String actionId) {
		List<String> roleNames = getRolesWithPermission(actionId);

		roleNames.add(roleName);

		List<String> actionIds = getActionIds(roleName);

		actionIds.add(actionId);

		_roleNames.add(roleName);
	}

	public void addRolePermissions(String roleName, String[] actionIds) {
		for (String actionId : actionIds) {
			addRolePermissions(roleName, actionId);
		}
	}

	public List<String> getActionIds(String roleName) {
		List<String> actionIds = _roleNamesMap.get(roleName);

		if (actionIds == null) {
			actionIds = new ArrayList<>();

			_roleNamesMap.put(roleName, actionIds);
		}

		return actionIds;
	}

	public Collection<String> getRoleNames() {
		return _roleNames;
	}

	public List<String> getRolesWithPermission(String actionId) {
		List<String> roles = _actionsMap.get(actionId);

		if (roles == null) {
			roles = new ArrayList<>();

			_actionsMap.put(actionId, roles);
		}

		return roles;
	}

	public boolean isEmpty() {
		return _roleNames.isEmpty();
	}

	private final Map<String, List<String>> _actionsMap = new HashMap<>();
	private final Set<String> _roleNames = new HashSet<>();
	private final Map<String, List<String>> _roleNamesMap = new HashMap<>();

}