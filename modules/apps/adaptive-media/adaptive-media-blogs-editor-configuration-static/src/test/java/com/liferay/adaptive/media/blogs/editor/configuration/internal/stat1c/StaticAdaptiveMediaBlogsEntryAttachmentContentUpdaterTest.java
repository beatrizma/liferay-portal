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

package com.liferay.adaptive.media.blogs.editor.configuration.internal.stat1c;

import com.liferay.adaptive.media.image.html.AdaptiveMediaImageHTMLTagFactory;
import com.liferay.portal.kernel.portletfilerepository.PortletFileRepositoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.StringPool;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Alejandro Tardín
 */
@PrepareForTest(PortletFileRepositoryUtil.class)
@RunWith(PowerMockRunner.class)
public class StaticAdaptiveMediaBlogsEntryAttachmentContentUpdaterTest
	extends PowerMockito {

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		PowerMockito.mockStatic(PortletFileRepositoryUtil.class);
		Mockito.when(
			PortletFileRepositoryUtil.getPortletFileEntryURL(
				Mockito.isNull(ThemeDisplay.class), Mockito.eq(_fileEntry),
				Mockito.eq(StringPool.BLANK))
		).thenReturn(
			_FILE_ENTRY_IMAGE_URL
		);

		Mockito.when(
			_fileEntry.getFileEntryId()
		).thenReturn(
			_FILE_ENTRY_IMAGE_ID
		);

		_staticAdaptiveMediaBlogsEntryAttachmentContentUpdater.
			setAdaptiveMediaImageHTMLTagFactory(
				_adaptiveMediaImageHTMLTagFactory);
	}

	@Test
	public void testReturnsStaticHTMLTag() throws Exception {
		String originalImgTag = "<img src=\"" + _FILE_ENTRY_IMAGE_URL + "\" />";
		String expectedStaticTag = "staticPictureTag";

		Mockito.when(
			_adaptiveMediaImageHTMLTagFactory.create(originalImgTag, _fileEntry)
		).thenReturn(
			expectedStaticTag
		);

		String staticTag =
			_staticAdaptiveMediaBlogsEntryAttachmentContentUpdater.
				getBlogsEntryAttachmentFileEntryImgTag(_fileEntry);

		Assert.assertEquals(expectedStaticTag, staticTag);
	}

	private static final long _FILE_ENTRY_IMAGE_ID = 1989L;

	private static final String _FILE_ENTRY_IMAGE_URL = "theUrl";

	@Mock
	private AdaptiveMediaImageHTMLTagFactory _adaptiveMediaImageHTMLTagFactory;

	@Mock
	private FileEntry _fileEntry;

	private final StaticAdaptiveMediaBlogsEntryAttachmentContentUpdater
		_staticAdaptiveMediaBlogsEntryAttachmentContentUpdater =
			new StaticAdaptiveMediaBlogsEntryAttachmentContentUpdater();

}