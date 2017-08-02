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

package com.liferay.modern.site.building.fragment.web.internal.display.context;

import com.liferay.modern.site.building.fragment.constants.FragmentPortletKeys;
import com.liferay.modern.site.building.fragment.model.FragmentCollection;
import com.liferay.modern.site.building.fragment.service.FragmentCollectionServiceUtil;
import com.liferay.modern.site.building.fragment.web.util.FragmentPortletUtil;
import com.liferay.portal.kernel.dao.search.EmptyOnClickRowChecker;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.PortalPreferences;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.search.BaseModelSearchResult;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.SortFactoryUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.List;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Jürgen Kappler
 */
public class FragmentDisplayContext {

	public FragmentDisplayContext(
		RenderRequest renderRequest, RenderResponse renderResponse,
		HttpServletRequest request) {

		_renderRequest = renderRequest;
		_renderResponse = renderResponse;
		_request = request;
	}

	public String getDisplayStyle() {
		if (Validator.isNotNull(_displayStyle)) {
			return _displayStyle;
		}

		PortalPreferences portalPreferences =
			PortletPreferencesFactoryUtil.getPortalPreferences(_request);

		_displayStyle = portalPreferences.getValue(
			FragmentPortletKeys.FRAGMENT, "display-style", "icon");

		return _displayStyle;
	}

	public SearchContainer getFragmentCollectionsSearchContainer()
		throws PortalException {

		if (_fragmentCollectionsSearchContainer != null) {
			return _fragmentCollectionsSearchContainer;
		}

		SearchContainer fragmentCollectionsSearchContainer =
			new SearchContainer(
				_renderRequest, _renderResponse.createRenderURL(), null,
				"there-are-no-fragment-collections");

		if (!isSearch()) {
			fragmentCollectionsSearchContainer.setEmptyResultsMessage(
				"there-are-no-fragment-collections.-you-can-add-a-fragment-" +
					"collection-by-clicking-the-plus-button-on-the-bottom-" +
						"right-corner");

			fragmentCollectionsSearchContainer.setEmptyResultsMessageCssClass(
				"taglib-empty-result-message-header-has-plus-btn");
		}
		else {
			fragmentCollectionsSearchContainer.setSearch(true);
		}

		fragmentCollectionsSearchContainer.setRowChecker(
			new EmptyOnClickRowChecker(_renderResponse));

		OrderByComparator<FragmentCollection> orderByComparator =
			FragmentPortletUtil.getFragmentCollectionOrderByComparator(
				getOrderByCol(), getOrderByType());

		fragmentCollectionsSearchContainer.setOrderByCol(getOrderByCol());
		fragmentCollectionsSearchContainer.setOrderByComparator(
			orderByComparator);
		fragmentCollectionsSearchContainer.setOrderByType(getOrderByType());

		fragmentCollectionsSearchContainer.setRowChecker(
			new EmptyOnClickRowChecker(_renderResponse));

		List<FragmentCollection> collections = null;
		int fragmentCollectionsCount = 0;

		ThemeDisplay themeDisplay = (ThemeDisplay)_request.getAttribute(
			WebKeys.THEME_DISPLAY);

		long scopeGroupId = themeDisplay.getScopeGroupId();

		if (isSearch()) {
			Sort sort = null;

			String orderByCol = getOrderByCol();

			if (orderByCol.equals("name")) {
				sort = SortFactoryUtil.getSort(
					FragmentCollection.class, Sort.STRING_TYPE, Field.NAME,
					getOrderByType());
			}
			else if (orderByCol.equals("create-date")) {
				sort = SortFactoryUtil.getSort(
					FragmentCollection.class, Sort.LONG_TYPE, Field.CREATE_DATE,
					getOrderByType());
			}

			BaseModelSearchResult<FragmentCollection> baseModelSearchResult =
				FragmentCollectionServiceUtil.searchFragmentCollections(
					scopeGroupId, getKeywords(),
					fragmentCollectionsSearchContainer.getStart(),
					fragmentCollectionsSearchContainer.getEnd(), sort);

			fragmentCollectionsSearchContainer.setTotal(
				baseModelSearchResult.getLength());

			collections = baseModelSearchResult.getBaseModels();
		}
		else {
			fragmentCollectionsCount =
				FragmentCollectionServiceUtil.getGroupFragmentCollectionsCount(
					scopeGroupId);

			fragmentCollectionsSearchContainer.setTotal(
				fragmentCollectionsCount);

			collections = FragmentCollectionServiceUtil.getFragmentCollections(
				scopeGroupId, fragmentCollectionsSearchContainer.getStart(),
				fragmentCollectionsSearchContainer.getEnd(), orderByComparator);
		}

		fragmentCollectionsSearchContainer.setResults(collections);

		_fragmentCollectionsSearchContainer =
			fragmentCollectionsSearchContainer;

		return _fragmentCollectionsSearchContainer;
	}

	public String getKeywords() {
		if (_keywords != null) {
			return _keywords;
		}

		_keywords = ParamUtil.getString(_request, "keywords");

		return _keywords;
	}

	public String getOrderByCol() {
		if (Validator.isNotNull(_orderByCol)) {
			return _orderByCol;
		}

		_orderByCol = ParamUtil.getString(
			_request, "orderByCol", "create-date");

		return _orderByCol;
	}

	public String getOrderByType() {
		if (Validator.isNotNull(_orderByType)) {
			return _orderByType;
		}

		_orderByType = ParamUtil.getString(_request, "orderByType", "asc");

		return _orderByType;
	}

	public String[] getOrderColumns() {
		return new String[] {"create-date", "name"};
	}

	public boolean isSearch() {
		if (Validator.isNotNull(getKeywords())) {
			return true;
		}

		return false;
	}

	private String _displayStyle;
	private SearchContainer _fragmentCollectionsSearchContainer;
	private String _keywords;
	private String _orderByCol;
	private String _orderByType;
	private final RenderRequest _renderRequest;
	private final RenderResponse _renderResponse;
	private final HttpServletRequest _request;

}