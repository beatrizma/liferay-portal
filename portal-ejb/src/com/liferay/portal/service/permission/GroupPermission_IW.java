/**
 * Copyright (c) 2000-2007 Liferay, Inc. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.liferay.portal.service.permission;

/**
 * <a href="GroupPermission_IW.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class GroupPermission_IW {
	public static GroupPermission_IW getInstance() {
		return _instance;
	}

	public void check(
		com.liferay.portal.kernel.security.permission.PermissionChecker permissionChecker,
		long groupId, java.lang.String actionId)
		throws com.liferay.portal.security.auth.PrincipalException {
		GroupPermission.check(permissionChecker, groupId, actionId);
	}

	public boolean contains(
		com.liferay.portal.kernel.security.permission.PermissionChecker permissionChecker,
		long groupId, java.lang.String actionId) {
		return GroupPermission.contains(permissionChecker, groupId, actionId);
	}

	private GroupPermission_IW() {
	}

	private static GroupPermission_IW _instance = new GroupPermission_IW();
}