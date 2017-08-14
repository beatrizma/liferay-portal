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

import com.liferay.modern.site.building.fragment.model.FragmentCollection;
import com.liferay.modern.site.building.fragment.service.base.FragmentCollectionLocalServiceBaseImpl;
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
 * The implementation of the fragment collection local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.liferay.modern.site.building.fragment.service.FragmentCollectionLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see FragmentCollectionLocalServiceBaseImpl
 * @see com.liferay.modern.site.building.fragment.service.FragmentCollectionLocalServiceUtil
 */
public class FragmentCollectionLocalServiceImpl
	extends FragmentCollectionLocalServiceBaseImpl {

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. Always use {@link com.liferay.modern.site.building.fragment.service.FragmentCollectionLocalServiceUtil} to access the fragment collection local service.
	 */

	@Indexable(type = IndexableType.REINDEX)
	@Override
	public FragmentCollection addFragmentCollection(
			long userId, long groupId, String name, String description,
			ServiceContext serviceContext)
		throws PortalException {

		User user = userLocalService.getUser(userId);

		long fragmentCollectionId = counterLocalService.increment();

		FragmentCollection fragmentCollection =
			fragmentCollectionPersistence.create(fragmentCollectionId);

		fragmentCollection.setGroupId(groupId);
		fragmentCollection.setCompanyId(user.getCompanyId());
		fragmentCollection.setUserId(user.getUserId());
		fragmentCollection.setUserName(user.getFullName());
		fragmentCollection.setName(name);
		fragmentCollection.setDescription(description);

		fragmentCollectionPersistence.update(fragmentCollection);

		resourceLocalService.addModelResources(
			fragmentCollection, serviceContext);

		return fragmentCollection;
	}

	@Indexable(type = IndexableType.DELETE)
	@Override
	public FragmentCollection deleteFragmentCollection(
			long fragmentCollectionId)
		throws PortalException {

		FragmentCollection fragmentCollection = getFragmentCollection(
			fragmentCollectionId);

		resourceLocalService.deleteResource(
			fragmentCollection.getCompanyId(),
			FragmentCollection.class.getName(),
			ResourceConstants.SCOPE_INDIVIDUAL,
			fragmentCollection.getFragmentCollectionId());

		fragmentCollectionPersistence.remove(fragmentCollection);

		return fragmentCollection;
	}

	@Override
	public FragmentCollection fetchFragmentCollection(
		long fragmentCollectionId) {

		return fragmentCollectionPersistence.fetchByPrimaryKey(
			fragmentCollectionId);
	}

	@Override
	public List<FragmentCollection> getFragmentCollections(Hits hits)
		throws PortalException {

		List<Document> documents = hits.toList();

		List<FragmentCollection> fragmentCollections = new ArrayList<>(
			documents.size());

		for (Document document : documents) {
			long fragmentCollectionId = GetterUtil.getLong(
				document.get("fragmentCollectionId"));

			FragmentCollection fragmentCollection =
				fragmentCollectionLocalService.fetchFragmentCollection(
					fragmentCollectionId);

			if (fragmentCollection == null) {
				fragmentCollections = null;

				Indexer<FragmentCollection> indexer =
					IndexerRegistryUtil.getIndexer(FragmentCollection.class);

				long companyId = GetterUtil.getLong(
					document.get(Field.COMPANY_ID));

				indexer.delete(companyId, document.getUID());
			}
			else if (fragmentCollections != null) {
				fragmentCollections.add(fragmentCollection);
			}
		}

		return fragmentCollections;
	}

	@Override
	public List<FragmentCollection> getFragmentCollections(
			long groupId, int start, int end)
		throws PortalException {

		return getFragmentCollections(groupId, start, end, null);
	}

	@Override
	public List<FragmentCollection> getFragmentCollections(
			long groupId, int start, int end,
			OrderByComparator<FragmentCollection> orderByComparator)
		throws PortalException {

		return fragmentCollectionPersistence.findByGroupId(
			groupId, start, end, orderByComparator);
	}

	@Override
	public BaseModelSearchResult<FragmentCollection> searchFragmentCollections(
			long companyId, long groupId, String title, int start, int end,
			Sort sort)
		throws PortalException {

		SearchContext searchContext = buildSearchContext(
			companyId, groupId, title, start, end, sort);

		return searchFragmentCollections(searchContext);
	}

	@Indexable(type = IndexableType.REINDEX)
	@Override
	public FragmentCollection updateFragmentCollection(
			long fragmentCollectionId, String name, String description)
		throws PortalException {

		FragmentCollection fragmentCollection =
			fragmentCollectionPersistence.findByPrimaryKey(
				fragmentCollectionId);

		fragmentCollection.setName(name);
		fragmentCollection.setDescription(description);

		fragmentCollectionPersistence.update(fragmentCollection);

		return fragmentCollection;
	}

	protected SearchContext buildSearchContext(
		long companyId, long groupId, String name, int start, int end,
		Sort sort) {

		SearchContext searchContext = new SearchContext();

		searchContext.setAttribute(Field.NAME, name);
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

	protected BaseModelSearchResult<FragmentCollection>
			searchFragmentCollections(SearchContext searchContext)
		throws PortalException {

		Indexer<FragmentCollection> indexer =
			IndexerRegistryUtil.nullSafeGetIndexer(FragmentCollection.class);

		for (int i = 0; i < 10; i++) {
			Hits hits = indexer.search(searchContext);

			List<FragmentCollection> fragmentCollections =
				getFragmentCollections(hits);

			if (fragmentCollections != null) {
				return new BaseModelSearchResult<>(
					fragmentCollections, hits.getLength());
			}
		}

		throw new SearchException(
			"Unable to fix the search index after 10 attempts");
	}

}