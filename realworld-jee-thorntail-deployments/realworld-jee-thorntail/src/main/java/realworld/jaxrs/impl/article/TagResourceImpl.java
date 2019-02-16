package realworld.jaxrs.impl.article;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import realworld.article.jaxrs.TagList;
import realworld.article.jaxrs.TagResource;
import realworld.article.services.TagService;

/**
 * Tag operations implementation.
 */
@ApplicationScoped
public class TagResourceImpl implements TagResource {

	@Inject
	private TagService tagService;

	@Override
	public TagList get() {
		return new TagList(tagService.getAll());
	}
}
