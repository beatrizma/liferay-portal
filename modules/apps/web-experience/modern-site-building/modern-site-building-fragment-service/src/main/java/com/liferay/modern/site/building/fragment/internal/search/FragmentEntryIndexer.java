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

import com.liferay.modern.site.building.fragment.model.FragmentEntry;
import com.liferay.modern.site.building.fragment.service.FragmentEntryLocalService;
import com.liferay.modern.site.building.fragment.service.permission.FragmentEntryPermission;
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
public class FragmentEntryIndexer extends BaseIndexer<FragmentEntry> {

	public static final String CLASS_NAME = FragmentEntry.class.getName();

	public static final String FRAGMENT_ENTRY_ID = "fragmentEntryId";

	public FragmentEntryIndexer() {
		setDefaultSelectedFieldNames(FRAGMENT_ENTRY_ID, Field.NAME, Field.UID);
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

		FragmentEntry fragmentEntry =
			_fragmentEntryLocalService.getFragmentEntry(entryClassPK);

		return FragmentEntryPermission.contains(
			permissionChecker, fragmentEntry, ActionKeys.VIEW);
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
	protected void doDelete(FragmentEntry fragmentEntry) throws Exception {
		deleteDocument(
			fragmentEntry.getCompanyId(), fragmentEntry.getFragmentEntryId());
	}

	@Override
	protected Document doGetDocument(FragmentEntry fragmentEntry)
		throws Exception {

		if (_log.isDebugEnabled()) {
			_log.debug("Indexing fragment entry " + fragmentEntry);
		}

		Document document = getBaseModelDocument(CLASS_NAME, fragmentEntry);

		document.addKeyword(
			FRAGMENT_ENTRY_ID, fragmentEntry.getFragmentEntryId());

		document.addText(Field.NAME, fragmentEntry.getName());

		if (_log.isDebugEnabled()) {
			_log.debug("Document " + fragmentEntry + " indexed successfully");
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
	protected void doReindex(FragmentEntry fragmentEntry) throws Exception {
		Document document = getDocument(fragmentEntry);

		IndexWriterHelperUtil.updateDocument(
			getSearchEngineId(), fragmentEntry.getCompanyId(), document,
			isCommitImmediately());
	}

	@Override
	protected void doReindex(String className, long classPK) throws Exception {
		FragmentEntry fragmentEntry =
			_fragmentEntryLocalService.getFragmentEntry(classPK);

		doReindex(fragmentEntry);
	}

	@Override
	protected void doReindex(String[] ids) throws Exception {
		long companyId = GetterUtil.getLong(ids[0]);

		reindexFragmentEntries(companyId);
	}

	protected void reindexFragmentEntries(final long companyId)
		throws PortalException {

		final IndexableActionableDynamicQuery indexableActionableDynamicQuery =
			_fragmentEntryLocalService.getIndexableActionableDynamicQuery();

		indexableActionableDynamicQuery.setCompanyId(companyId);
		indexableActionableDynamicQuery.setPerformActionMethod(
			new ActionableDynamicQuery.PerformActionMethod<FragmentEntry>() {

				@Override
				public void performAction(FragmentEntry fragmentEntry) {
					try {
						Document document = getDocument(fragmentEntry);

						indexableActionableDynamicQuery.addDocuments(document);
					}
					catch (PortalException pe) {
						if (_log.isWarnEnabled()) {
							_log.warn(
								"Unable to index fragment entry " +
									fragmentEntry.getFragmentEntryId(),
								pe);
						}
					}
				}

			});
		indexableActionableDynamicQuery.setSearchEngineId(getSearchEngineId());

		indexableActionableDynamicQuery.performActions();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		FragmentEntryIndexer.class);

	@Reference
	private FragmentEntryLocalService _fragmentEntryLocalService;

	@Reference
	private Portal _portal;

}