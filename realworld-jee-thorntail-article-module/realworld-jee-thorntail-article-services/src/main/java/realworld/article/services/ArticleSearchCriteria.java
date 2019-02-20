package realworld.article.services;

import java.util.Collections;
import java.util.List;

/**
 * Article search criteria.
 */
public interface ArticleSearchCriteria {

	/**
	 * Builder pattern.
	 *
	 * @return A builder for the criteria
	 */
	static ArticleSearchCriteriaBuilder builder() {
		return new ArticleSearchCriteriaBuilderImpl();
	}

	/**
	 * Get the tag.
	 *
	 * @return The tag
	 */
	String getTag();

	/**
	 * Get the authors.
	 *
	 * @return The authors
	 */
	List<String> getAuthors();

	/**
	 * Get the favorited by criterion.
	 *
	 * @return The favorited by
	 */
	String getFavoritedBy();

	/**
	 * Get the result limit.
	 *
	 * @return The result limit
	 */
	Integer getLimit();

	/**
	 * Get the starting result.
	 *
	 * @return The starting result
	 */
	Integer getOffset();

	/**
	 * Return a new criteria object with the given author.
	 *
	 * @param author The author
	 * @return A new criteria object
	 */
	ArticleSearchCriteria withAuthor(String author);

	/**
	 * Return a new criteria object with the given list of authors.
	 *
	 * @param authors The authors
	 * @return A new criteria object
	 */
	ArticleSearchCriteria withAuthors(List<String> authors);

	/**
	 * Return a new criteria object with the given favorited by user.
	 *
	 * @param favoritedBy The user
	 * @return A new criteria object
	 */
	ArticleSearchCriteria withFavoritedBy(String favoritedBy);


	/**
	 * Builder interface for the {@code ArticleSearchCriteria}.
	 */
	interface ArticleSearchCriteriaBuilder {
		/**
		 * Set the tag.
		 *
		 * @param tag The tag
		 * @return {@code this}
		 */
		ArticleSearchCriteriaBuilder withTag(String tag);

		/**
		 * Set the author.
		 *
		 * @param author The author
		 * @return {@code this}
		 */
		ArticleSearchCriteriaBuilder withAuthor(String author);

		/**
		 * Set the authors.
		 *
		 * @param authors The list of authors
		 * @return {@code this}
		 */
		ArticleSearchCriteriaBuilder withAuthors(List<String> authors);

		/**
		 * Set the favorited by.
		 *
		 * @param user The favoriter by user
		 * @return {@code this}
		 */
		ArticleSearchCriteriaBuilder favoritedBy(String user);

		/**
		 * Set the result limit.
		 *
		 * @param limit The limit
		 * @return {@code this}
		 */
		ArticleSearchCriteriaBuilder withLimit(Integer limit);

		/**
		 * Set the offset.
		 *
		 * @param offset The offset
		 * @return {@code this}
		 */
		ArticleSearchCriteriaBuilder withOffset(Integer offset);

		/**
		 * Build the criteria object.
		 *
		 * @return A new criteria object
		 */
		ArticleSearchCriteria build();
	}

	class ArticleSearchCriteriaImpl implements ArticleSearchCriteria {
		private String tag;
		private List<String> authors;
		private String favoritedBy;
		private Integer limit;
		private Integer offset;

		ArticleSearchCriteriaImpl(String tag, List<String> authors, String favoritedBy, Integer limit, Integer offset) {
			this.tag = tag;
			this.authors = authors;
			this.favoritedBy = favoritedBy;
			this.limit = limit;
			this.offset = offset;
		}

		@Override
		public ArticleSearchCriteria withAuthor(String author) {
			return new ArticleSearchCriteriaImpl(this.tag, Collections.singletonList(author), this.favoritedBy, this.limit, this.offset);
		}

		@Override
		public ArticleSearchCriteria withAuthors(List<String> authors) {
			return new ArticleSearchCriteriaImpl(this.tag, authors, this.favoritedBy, this.limit, this.offset);
		}

		@Override
		public ArticleSearchCriteria withFavoritedBy(String favoritedBy) {
			return new ArticleSearchCriteriaImpl(this.tag, this.authors, favoritedBy, this.limit, this.offset);
		}

		@Override
		public String getTag() {
			return tag;
		}

		@Override
		public List<String> getAuthors() {
			return authors;
		}

		@Override
		public String getFavoritedBy() {
			return favoritedBy;
		}

		@Override
		public Integer getLimit() {
			return limit;
		}

		@Override
		public Integer getOffset() {
			return offset;
		}
	}

	class ArticleSearchCriteriaBuilderImpl implements ArticleSearchCriteriaBuilder {
		private String tag;
		private List<String> authors;
		private String favoritedBy;
		private Integer limit;
		private Integer offset;

		@Override
		public ArticleSearchCriteria build() {
			return new ArticleSearchCriteriaImpl(tag, authors, favoritedBy, limit, offset);
		}

		@Override
		public ArticleSearchCriteriaBuilder withTag(String tag) {
			this.tag = tag;
			return this;
		}

		@Override
		public ArticleSearchCriteriaBuilder withAuthor(String author) {
			this.authors = Collections.singletonList(author);
			return this;
		}

		@Override
		public ArticleSearchCriteriaBuilder withAuthors(List<String> authors) {
			this.authors = authors;
			return this;
		}

		@Override
		public ArticleSearchCriteriaBuilder favoritedBy(String user) {
			this.favoritedBy = user;
			return this;
		}

		@Override
		public ArticleSearchCriteriaBuilder withLimit(Integer limit) {
			this.limit = limit;
			return this;
		}

		@Override
		public ArticleSearchCriteriaBuilder withOffset(Integer offset) {
			this.offset = offset;
			return this;
		}
	}
}
