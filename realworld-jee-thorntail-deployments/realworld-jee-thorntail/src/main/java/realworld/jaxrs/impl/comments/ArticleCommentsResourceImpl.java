package realworld.jaxrs.impl.comments;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import realworld.comments.jaxrs.ArticleCommentsResource;
import realworld.comments.jaxrs.CommentsList;
import realworld.comments.jaxrs.CreationParam;
import realworld.comments.model.CommentData;
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
	public CommentData add(String slug, CreationParam param) {
		return commentService.add(slug, param);
	}

	@Override
	public void delete(String slug, String id) {
		commentService.delete(id);
	}
}
