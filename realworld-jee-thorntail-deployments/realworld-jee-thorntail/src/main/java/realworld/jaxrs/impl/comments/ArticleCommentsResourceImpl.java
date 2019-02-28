package realworld.jaxrs.impl.comments;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import realworld.comments.jaxrs.ArticleCommentsResource;
import realworld.comments.jaxrs.CommentDataWrapper;
import realworld.comments.jaxrs.CommentsList;
import realworld.comments.jaxrs.CreationParam;
import services.realworld.comments.services.CommentService;

/**
 * Implementation of the comments operations.
 */
@ApplicationScoped
public class ArticleCommentsResourceImpl implements ArticleCommentsResource {

	@Inject
	private CommentService commentService;

	@Override
	public CommentsList get(String slug) {
		return new CommentsList(commentService.findArticleComments(slug));
	}

	@Override
	public CommentDataWrapper add(String slug, CreationParam param) {
		return new CommentDataWrapper(commentService.add(slug, param));
	}

	@Override
	public void delete(String slug, String id) {
		commentService.delete(id);
	}
}
