package realworld.article.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "RWL_ARTICLE_TAG")
public class Tag {
	@Id
	@Column(name = "name")
	private String name;

	/**
	 * Default constructor.
	 */
	public Tag() {
		// NOOP
	}

	/**
	 * Constructor from name.
	 *
	 * @param name The name
	 */
	public Tag(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
