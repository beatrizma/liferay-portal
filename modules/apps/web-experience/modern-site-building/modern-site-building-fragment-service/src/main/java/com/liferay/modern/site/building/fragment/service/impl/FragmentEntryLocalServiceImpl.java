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

package com.liferay.modern.site.building.fragment.service.impl;

import com.liferay.modern.site.building.fragment.model.FragmentEntry;
import com.liferay.modern.site.building.fragment.service.base.FragmentEntryLocalServiceBaseImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.BaseModelSearchResult;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.QueryConfig;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.util.ArrayList;
import java.util.List;

/**
 * The implementation of the fragment entry local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.liferay.modern.site.building.fragment.service.FragmentEntryLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see FragmentEntryLocalServiceBaseImpl
 * @see com.liferay.modern.site.building.fragment.service.FragmentEntryLocalServiceUtil
 */
public class FragmentEntryLocalServiceImpl
	extends FragmentEntryLocalServiceBaseImpl {

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. Always use {@link com.liferay.modern.site.building.fragment.service.FragmentEntryLocalServiceUtil} to access the fragment entry local service.
	 */

	@Indexable(type = IndexableType.REINDEX)
	@Override
	public FragmentEntry addFragmentEntry(
			long userId, long groupId, long fragmentCollectionId, String name,
			String css, String html, String js, ServiceContext serviceContext)
		throws PortalException {

		User user = userLocalService.getUser(userId);

		long fragmentEntryId = counterLocalService.increment();

		FragmentEntry fragmentEntry = fragmentEntryPersistence.create(
			fragmentEntryId);

		fragmentEntry.setGroupId(groupId);
		fragmentEntry.setCompanyId(user.getCompanyId());
		fragmentEntry.setUserId(user.getUserId());
		fragmentEntry.setUserName(user.getFullName());
		fragmentEntry.setFragmentCollectionId(fragmentCollectionId);
		fragmentEntry.setName(name);
		fragmentEntry.setCss(css);
		fragmentEntry.setHtml(html);
		fragmentEntry.setJs(js);

		fragmentEntryPersistence.update(fragmentEntry);

		resourceLocalService.addModelResources(fragmentEntry, serviceContext);

		return fragmentEntry;
	}

	@Indexable(type = IndexableType.DELETE)
	@Override
	public FragmentEntry deleteFragmentEntry(long fragmentEntryId)
		throws PortalException {

		FragmentEntry fragmentEntry = getFragmentEntry(fragmentEntryId);

		resourceLocalService.deleteResource(
			fragmentEntry.getCompanyId(), FragmentEntry.class.getName(),
			ResourceConstants.SCOPE_INDIVIDUAL,
			fragmentEntry.getFragmentEntryId());

		fragmentEntryPersistence.remove(fragmentEntry);

		return fragmentEntry;
	}

	@Override
	public List<FragmentEntry> fetchFragmentEntries(long fragmentCollectionId) {
		return fragmentEntryPersistence.findByFragmentCollectionId(
			fragmentCollectionId);
	}

	@Override
	public FragmentEntry fetchFragmentEntry(long fragmentEntryId) {
		return fragmentEntryPersistence.fetchByPrimaryKey(fragmentEntryId);
	}

	@Override
	public List<FragmentEntry> getFragmentEntries(Hits hits)
		throws PortalException {

		List<Document> documents = hits.toList();

		List<FragmentEntry> fragmentEntries = new ArrayList<>(documents.size());

		for (Document document : documents) {
			long fragmentEntryId = GetterUtil.getLong(
				document.get("fragmentEntryId"));

			FragmentEntry fragmentEntry =
				fragmentEntryLocalService.fetchFragmentEntry(fragmentEntryId);

			if (fragmentEntry == null) {
				fragmentEntries = null;

				Indexer<FragmentEntry> indexer = IndexerRegistryUtil.getIndexer(
					FragmentEntry.class);

				long companyId = GetterUtil.getLong(
					document.get(Field.COMPANY_ID));

				indexer.delete(companyId, document.getUID());
			}
			else if (fragmentEntries != null) {
				fragmentEntries.add(fragmentEntry);
			}
		}

		return fragmentEntries;
	}

	@Override
	public List<FragmentEntry> getFragmentEntries(
			long fragmentCollectionId, int start, int end)
		throws PortalException {

		return fragmentEntryPersistence.findByFragmentCollectionId(
			fragmentCollectionId, start, end);
	}

	@Override
	public List<FragmentEntry> getFragmentEntries(
			long fragmentCollectionId, int start, int end,
			OrderByComparator<FragmentEntry> orderByComparator)
		throws PortalException {

		return fragmentEntryPersistence.findByFragmentCollectionId(
			fragmentCollectionId, start, end, orderByComparator);
	}

	@Override
	public BaseModelSearchResult<FragmentEntry> searchFragmentEntries(
			long companyId, long groupId, long fragmentEntryId, String title,
			int start, int end, Sort sort)
		throws PortalException {

		SearchContext searchContext = buildSearchContext(
			companyId, groupId, fragmentEntryId, title, start, end, sort);

		return searchFragmentEntries(searchContext);
	}

	@Indexable(type = IndexableType.REINDEX)
	@Override
	public FragmentEntry updateFragmentEntry(
			long fragmentEntryId, String name, String css, String html,
			String js)
		throws PortalException {

		FragmentEntry fragmentEntry = fragmentEntryPersistence.findByPrimaryKey(
			fragmentEntryId);

		fragmentEntry.setName(name);
		fragmentEntry.setCss(css);
		fragmentEntry.setHtml(html);
		fragmentEntry.setJs(js);

		fragmentEntryPersistence.update(fragmentEntry);

		return fragmentEntry;
	}

	protected SearchContext buildSearchContext(
		long companyId, long groupId, long fragmentEntryId, String name,
		int start, int end, Sort sort) {

		SearchContext searchContext = new SearchContext();

		searchContext.setAttribute(Field.NAME, name);
		searchContext.setAttribute("fragmentEntryId", fragmentEntryId);
		searchContext.setCompanyId(companyId);
		searchContext.setEnd(end);
		searchContext.setGroupIds(new long[] {groupId});
		searchContext.setKeywords(name);
		searchContext.setSorts(sort);
		searchContext.setStart(start);

		QueryConfig queryConfig = searchContext.getQueryConfig();

		queryConfig.setHighlightEnabled(false);
		queryConfig.setScoreEnabled(false);

		return searchContext;
	}

	protected BaseModelSearchResult<FragmentEntry>
			searchFragmentEntries(SearchContext searchContext)
		throws PortalException {

		Indexer<FragmentEntry> indexer = IndexerRegistryUtil.nullSafeGetIndexer(
			FragmentEntry.class);

		for (int i = 0; i < 10; i++) {
			Hits hits = indexer.search(searchContext);

			List<FragmentEntry> fragmentEntries = getFragmentEntries(hits);

			if (fragmentEntries != null) {
				return new BaseModelSearchResult<>(
					fragmentEntries, hits.getLength());
			}
		}

		throw new SearchException(
			"Unable to fix the search index after 10 attempts");
	}

}