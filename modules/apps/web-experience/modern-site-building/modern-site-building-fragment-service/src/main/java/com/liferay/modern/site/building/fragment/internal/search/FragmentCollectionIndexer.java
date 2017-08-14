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

package com.liferay.modern.site.building.fragment.internal.search;

import com.liferay.modern.site.building.fragment.model.FragmentCollection;
import com.liferay.modern.site.building.fragment.service.FragmentCollectionLocalService;
import com.liferay.modern.site.building.fragment.service.permission.FragmentCollectionPermission;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.BaseIndexer;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.IndexWriterHelperUtil;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.Summary;
import com.liferay.portal.kernel.search.filter.BooleanFilter;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Portal;

import java.util.Locale;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Jürgen Kappler
 */
@Component(immediate = true, service = Indexer.class)
public class FragmentCollectionIndexer extends BaseIndexer<FragmentCollection> {

	public static final String CLASS_NAME = FragmentCollection.class.getName();

	public static final String FRAGMENT_COLLECTION_ID = "fragmentCollectionId";

	public FragmentCollectionIndexer() {
		setDefaultSelectedFieldNames(
			FRAGMENT_COLLECTION_ID, Field.NAME, Field.UID);
		setFilterSearch(true);
		setPermissionAware(true);
	}

	@Override
	public String getClassName() {
		return CLASS_NAME;
	}

	@Override
	public boolean hasPermission(
			PermissionChecker permissionChecker, String entryClassName,
			long entryClassPK, String actionId)
		throws Exception {

		FragmentCollection fragmentCollection =
			_fragmentCollectionLocalService.getFragmentCollection(entryClassPK);

		return FragmentCollectionPermission.contains(
			permissionChecker, fragmentCollection, ActionKeys.VIEW);
	}

	@Override
	public void postProcessSearchQuery(
			BooleanQuery searchQuery, BooleanFilter fullQueryBooleanFilter,
			SearchContext searchContext)
		throws Exception {

		if (searchContext.getAttributes() == null) {
			return;
		}

		addSearchTerm(searchQuery, searchContext, Field.NAME, true);
	}

	@Override
	protected void doDelete(FragmentCollection fragmentCollection)
		throws Exception {

		deleteDocument(
			fragmentCollection.getCompanyId(),
			fragmentCollection.getFragmentCollectionId());
	}

	@Override
	protected Document doGetDocument(FragmentCollection fragmentCollection)
		throws Exception {

		if (_log.isDebugEnabled()) {
			_log.debug("Indexing fragment collection " + fragmentCollection);
		}

		Document document = getBaseModelDocument(
			CLASS_NAME, fragmentCollection);

		document.addKeyword(
			FRAGMENT_COLLECTION_ID,
			fragmentCollection.getFragmentCollectionId());

		document.addText(Field.NAME, fragmentCollection.getName());

		if (_log.isDebugEnabled()) {
			_log.debug(
				"Document " + fragmentCollection + " indexed successfully");
		}

		return document;
	}

	@Override
	protected Summary doGetSummary(
		Document document, Locale locale, String snippet,
		PortletRequest portletRequest, PortletResponse portletResponse) {

		return null;
	}

	@Override
	protected void doReindex(FragmentCollection fragmentCollection)
		throws Exception {

		Document document = getDocument(fragmentCollection);

		IndexWriterHelperUtil.updateDocument(
			getSearchEngineId(), fragmentCollection.getCompanyId(), document,
			isCommitImmediately());
	}

	@Override
	protected void doReindex(String className, long classPK) throws Exception {
		FragmentCollection fragmentCollection =
			_fragmentCollectionLocalService.getFragmentCollection(classPK);

		doReindex(fragmentCollection);
	}

	@Override
	protected void doReindex(String[] ids) throws Exception {
		long companyId = GetterUtil.getLong(ids[0]);

		reindexFragmentCollections(companyId);
	}

	protected void reindexFragmentCollections(final long companyId)
		throws PortalException {

		final IndexableActionableDynamicQuery indexableActionableDynamicQuery =
			_fragmentCollectionLocalService.
				getIndexableActionableDynamicQuery();

		indexableActionableDynamicQuery.setCompanyId(companyId);
		indexableActionableDynamicQuery.setPerformActionMethod(
			new ActionableDynamicQuery.
				PerformActionMethod<FragmentCollection>() {

				@Override
				public void performAction(
					FragmentCollection fragmentCollection) {

					try {
						Document document = getDocument(fragmentCollection);

						indexableActionableDynamicQuery.addDocuments(document);
					}
					catch (PortalException pe) {
						if (_log.isWarnEnabled()) {
							_log.warn(
								"Unable to index fragment collection " +
									fragmentCollection.
										getFragmentCollectionId(),
								pe);
						}
					}
				}

			});
		indexableActionableDynamicQuery.setSearchEngineId(getSearchEngineId());

		indexableActionableDynamicQuery.performActions();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		FragmentCollectionIndexer.class);

	@Reference
	private FragmentCollectionLocalService _fragmentCollectionLocalService;

	@Reference
	private Portal _portal;

}